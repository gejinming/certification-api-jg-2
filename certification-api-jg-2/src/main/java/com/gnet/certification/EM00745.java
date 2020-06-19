package com.gnet.certification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.gnet.model.admin.CcStudentEvalute;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;

/**
 * 
 * 保存或更新批量的学生考评点分析法
 * 参考EM00400
 * @author SY
 * @Date 2017年10月15日
 */
@Transactional(readOnly=false)
@Service("EM00745")
@Deprecated
public class EM00745 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		
		// 参考接口160的上传和获取
		JSONArray ccStudentEvaluteArray = paramsJSONArrayFilter(param.get("ccStudentEvalute"));
		
		List<CcStudentEvalute> ccStudentEvaluteEditList = new ArrayList<>();
		List<CcStudentEvalute> ccStudentEvaluteAddList = new ArrayList<>();
		for(int i = 0; i < ccStudentEvaluteArray.size(); i++) {
			JSONObject map = (JSONObject) ccStudentEvaluteArray.get(i);
			
			// 获取数据
			Long evaluteId = map.getLong("evaluteId");
			Long studentId = map.getLong("studentId");
			Long levelId = map.getLong("levelId");
			// 课程考评点编号不能为空过滤
			if (evaluteId == null) {
				return renderFAIL("0370", response, header);
			}
			// 学生编号不能为空过滤
			if (studentId == null) {
				return renderFAIL("0330", response, header);
			}
			
			Map<String, Object> searchParams = Maps.newHashMap();
			searchParams.put("evalute_id", evaluteId);
			searchParams.put("student_id", studentId);
			CcStudentEvalute ccStudentEvalute = CcStudentEvalute.dao.findFirstByColumn(searchParams, Boolean.TRUE);
			
			Date date = new Date();
			if (ccStudentEvalute == null) {
				IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
				ccStudentEvalute = new CcStudentEvalute();
				ccStudentEvalute.set("evalute_id", evaluteId);
				ccStudentEvalute.set("student_id", studentId);
				ccStudentEvalute.set("level_id", levelId);
				ccStudentEvalute.set("create_date", date);
				ccStudentEvalute.set("modify_date", date);
				ccStudentEvalute.set("is_del", CcStudentEvalute.DEL_NO);
				ccStudentEvalute.set("id", idGenerate.getNextValue());
				ccStudentEvaluteAddList.add(ccStudentEvalute);
			} else {
				ccStudentEvalute.set("level_id", levelId);
				ccStudentEvalute.set("modify_date", date);
				ccStudentEvaluteEditList.add(ccStudentEvalute);
			}
		}
		
		
		// 返回结果
		Map<String, Object> result = Maps.newHashMap();
		Boolean isSuccess = CcStudentEvalute.dao.batchSave(ccStudentEvaluteAddList);
		if(!isSuccess) {
			result.put("isSuccess", isSuccess);
			return renderSUC(result, response, header);
		}
		isSuccess = CcStudentEvalute.dao.batchUpdate(ccStudentEvaluteEditList, "level_id,modify_date");
		if(!isSuccess) {
			result.put("isSuccess", isSuccess);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return renderSUC(result, response, header);
		}
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}
	
}
