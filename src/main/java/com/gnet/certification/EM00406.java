package com.gnet.certification;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcEvalute;
import com.gnet.model.admin.CcEvaluteType;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.SpringContextHolder;;

/**
 * 某课程考评点类别增加或修改接口
 * @author SY
 * 
 * @date 2017年8月14日
 * 
 */
@Service("EM00406")
@Transactional(readOnly=false)
public class EM00406 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Map<String, Object> map = new HashMap<>();
		
		Long teacherCourseId = paramsLongFilter(param.get("teacherCourseId"));
		JSONArray typePercentageArray = paramsJSONArrayFilter(param.get("typePercentageArray"));

		if(teacherCourseId == null) {
			return renderFAIL("0250", response, header);
		}
		
		/**
		 * 获取老的信息。
		 * 判断那些是新增的，那些是更新，那些是删除。
		 * 对于删除，判断是否有被cc_evalute使用，被使用就不允许删除。
		 * 其他的直接操作。
		 */
		Boolean isSuccess;
		List<CcEvaluteType> ccEvaluteTypesOld = CcEvaluteType.dao.findFilteredByColumn("teacher_course_id", teacherCourseId);
		Map<Integer, CcEvaluteType> oldMap = new HashMap<>();
		List<CcEvaluteType> addList = new ArrayList<>();
		List<CcEvaluteType> editList = new ArrayList<>();
		List<Long> delTypeId = new ArrayList<>();
		Date date = new Date();
		// 解析获取老的数据
		for(CcEvaluteType temp : ccEvaluteTypesOld) {
			oldMap.put(temp.getInt("type"), temp);
		}
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		// 判断那些是新增的，那些是更新，那些是删除。
		for(int i = 0; i < typePercentageArray.size(); i++) {
			JSONObject typePercentageMap = (JSONObject) typePercentageArray.get(i); 
			Integer type = Integer.valueOf(typePercentageMap.get("type").toString());
			if(type == null) {
				// 如果发生错误，直接忽略
				continue;
			}
			String percentageStr = (StringUtils.isBlank(typePercentageMap.get("percentage").toString()) ? 0 : typePercentageMap.get("percentage")).toString();
			Integer percentage = Integer.valueOf(percentageStr);
			
			CcEvaluteType oldTemp = oldMap.get(type);
			if(oldTemp == null) {
				// 是新的
				CcEvaluteType newTemp = new CcEvaluteType();
				newTemp.set("id", idGenerate.getNextValue());
				newTemp.set("create_date", date);
				newTemp.set("modify_date", date);
				newTemp.set("teacher_course_id", teacherCourseId);
				newTemp.set("type", type);
				newTemp.set("percentage", percentage);
				newTemp.set("is_del", CcEvaluteType.DEL_NO);
				addList.add(newTemp);
			} else {
				// 如果存在，删除这个，等到未删除的就是被这次遗弃的。
				oldMap.remove(type);
				
				// 本次需要更新的
				oldTemp.set("modify_date", date);
				oldTemp.set("type", type);
				oldTemp.set("percentage", percentage);
				editList.add(oldTemp);
			}
		}
		
		// 遍历查找，是否本次需要删除某个类型。
		List<Long> delEvaluteTypeIds = new ArrayList<>(); 
		for(Map.Entry<Integer, CcEvaluteType> entry : oldMap.entrySet()) { 
			CcEvaluteType temp = entry.getValue();
			delEvaluteTypeIds.add(temp.getLong("id"));
			delTypeId.add(temp.getLong("id"));
		}
		// 如果不是空的，就开始准备删除
		if(!delEvaluteTypeIds.isEmpty()) {
			Long[] delIds = delEvaluteTypeIds.toArray(new Long[delEvaluteTypeIds.size()]);
			// 先判断是否已被使用，被使用直接返回false
			List<CcEvalute> ccEvalutes = CcEvalute.dao.findFilteredByColumnIn("evalute_type_id", delIds);
			Boolean isEmpty = ccEvalutes.isEmpty();
			if(!isEmpty) {
				return renderFAIL("0903", response, header);
			}
			
			// 删除
			isSuccess = CcEvaluteType.dao.deleteAll(delIds, date);
			if(!isSuccess) {
				map.put("isSuccess", isSuccess);
				return renderSUC(map, response, header);
			}
		}

		// 新增保存
		isSuccess = CcEvaluteType.dao.batchSave(addList);
		if(!isSuccess) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			map.put("isSuccess", isSuccess);
			return renderSUC(map, response, header);
		}
		
		// 更新
		isSuccess = CcEvaluteType.dao.batchUpdate(editList, "modify_date,type,percentage");
		if(!isSuccess) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			map.put("isSuccess", isSuccess);
			return renderSUC(map, response, header);
		}

		map.put("isSuccess", isSuccess);
		return renderSUC(map, response, header);
	}
	
}
