package com.gnet.certification;

import com.gnet.Constant;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.CollectionKit;
import com.gnet.utils.ConvertUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

/**
 * 新增课程目标
 * 
 * @author xzl
 * 
 * @date 2017年11月21日16:56:47
 *
 */
@Service("EM00800")
@Transactional(readOnly=false)
public class EM00800 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Map<String, Object> result = new HashMap<String, Object>();
		List<LinkedHashMap> courseTargets = paramsJSONArrayFilter(param.get("courseTargets"), LinkedHashMap.class);
        Long courseIndicationId = paramsLongFilter(param.get("courseIndicationId"));

        if(courseIndicationId == null){
        	return renderFAIL("0360", response, header);
		}

        if(courseTargets.isEmpty()){
        	return renderFAIL("1102", response, header);
		}

		CcIndicationCourse ccIndicationCourse = CcIndicationCourse.dao.findFilteredById(courseIndicationId);
        if(ccIndicationCourse == null){
			return renderFAIL("1103", response, header);
		}
		Long courseId = ccIndicationCourse.getLong("course_id");

		//已经存在的课程目标
		List<CcIndication> ccIndications = CcIndication.dao.findFilteredByColumn("course_id", courseId);
		Map<Integer, CcIndication> sortMap = new HashMap<>();
		List<Integer> sorts = new ArrayList<>();
		for(CcIndication ccIndication : ccIndications){
			sorts.add(ccIndication.getInt("sort"));
			sortMap.put(ccIndication.getInt("sort"), ccIndication);
		}


		//需要增加、修改、删除的课程目标列表
        List<CcIndication> addIndications = new ArrayList<>();
		List<CcIndication> editIndications = new ArrayList<>();
		List<Long> deleteIndicationIds = new ArrayList<>();

		//已经存在的课程目标和课程指标点的关联
		List<CcCourseTargetIndication> ccCourseTargetIndications = CcCourseTargetIndication.dao.findByColumn("indication_course_id", courseIndicationId);
		//已存在关联的课程目标和课程指标点的关联编号
		List<Long> courseTargetIndicationsIds = Lists.newArrayList();
        Map<String, Long> courseTargetIndicationsMap = new HashMap<>();
		for(CcCourseTargetIndication ccCourseTargetIndication : ccCourseTargetIndications){
            Long id = ccCourseTargetIndication.getLong("id");
			courseTargetIndicationsIds.add(id);
			String key = String.format("%s-%s", ccCourseTargetIndication.getLong("indication_id"), ccCourseTargetIndication.getLong("indication_course_id"));
			courseTargetIndicationsMap.put(key, id);
		}

         //需要增加的课程目标和课程指标点的关联
		List<CcCourseTargetIndication> addCourseTargetIndications = new ArrayList<>();

		Date date = new Date();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		//相同的sort
		List<Integer> sameSorts = new ArrayList<>();
        Map<Integer, Integer> sortIndexMap = new HashMap<>();
        for(int i=1; i<=courseTargets.size(); i++){
			Map<String, Object> map = courseTargets.get(i-1);
			String content = ConvertUtils.convert(map.get("content"), String.class).trim();
			Integer sort = ConvertUtils.convert(map.get("sort"), Integer.class);
            Boolean isRelate = ConvertUtils.convert(map.get("isRelate"), Boolean.class);

			if(sort == null){
				return renderFAIL("1105", response, header, String.format("第%s个课程目标序号不能为空",i));
			}

			if(sort != i){
				return renderFAIL("1108", response, header, String.format("第%s个课程目标序号为%s",i, sort));
			}

            if(StringUtils.isBlank(content)){
                return renderFAIL("1104", response, header, String.format("第%s个课程目标内容不能为空",i));
			}

			if(isRelate == null){
				return renderFAIL("1106", response, header, String.format("第%s个课程目标是否关联课程指标点不能为空",i));
			}

			if(sortIndexMap.get(sort) == null){
				sortIndexMap.put(sort, i);
			}else{
				return renderFAIL("1107", response, header, String.format("第%s个课程目标的序号和第%s的课程目标的序号重复", i, sortIndexMap.get(sort)));
			}

			CcIndication temp = sortMap.get(sort);
			Long indicationId = idGenerate.getNextValue();
			//获得增改课程目标
			if(temp== null){
				CcIndication ccIndication = new CcIndication();
				ccIndication.set("id", indicationId);
				ccIndication.set("create_date", date);
				ccIndication.set("modify_date", date);
				ccIndication.set("course_id", courseId);
				ccIndication.set("content", content);
				ccIndication.set("sort", sort);
				ccIndication.set("expected_value", CcIndication.DEFAULT_EXPECTED_VALUE);
				ccIndication.set("is_del", false);
				addIndications.add(ccIndication);

			}else{
				sameSorts.add(sort);
				//如果内容发生变化则进行更新
                if(!temp.getStr("content").equals(content)){
					temp.set("modify_date", date);
					temp.set("content", content);
					editIndications.add(temp);
				}
			}

			if(isRelate){
				String key = String.format("%s-%s", indicationId, courseIndicationId);
				Long courseTargetIndicationsId = courseTargetIndicationsMap.get(key);
				if(courseTargetIndicationsId != null && !courseTargetIndicationsIds.isEmpty()){
					courseTargetIndicationsIds.remove(courseTargetIndicationsId);
				}else{
					CcCourseTargetIndication ccCourseTargetIndication = new CcCourseTargetIndication();
					ccCourseTargetIndication.set("id", idGenerate.getNextValue());
					ccCourseTargetIndication.set("create_date", date);
					ccCourseTargetIndication.set("modify_date", date);
					ccCourseTargetIndication.set("indication_id", temp== null ? indicationId : temp.getLong("id"));
					ccCourseTargetIndication.set("indication_course_id", courseIndicationId);
					addCourseTargetIndications.add(ccCourseTargetIndication);
				}
			}

		}

		//得到新保存后的需要删除的课程目标
        if(!sorts.isEmpty()){
			sorts = CollectionKit.getDifferenceSetByGuava(sorts,sameSorts);
		}

		for(Integer sort : sorts){
			deleteIndicationIds.add(sortMap.get(sort).getLong("id"));
		}

		//删除前验证是否能删除
		if(!deleteIndicationIds.isEmpty()){
            //与开课课程成绩组成已关联的课程目标
			List<CcIndication> ccIndicationList = CcIndication.dao.findByIndicationIds(deleteIndicationIds);
			String errorMesage = null;
			for(CcIndication ccIndication : ccIndicationList){
				Integer sort = ccIndication.getInt("sort");
				errorMesage = errorMesage == null ? String.format("CO%s", sort) : String.format("%s,CO%s", errorMesage, sort);
			}
			if(StringUtils.isNotBlank(errorMesage)){
				return renderFAIL("1109", response, header, String.format("课程目标%s已关联成绩组成不允许删除", errorMesage));
			}
			if(!CcIndication.dao.deleteAll(deleteIndicationIds.toArray(new Long[deleteIndicationIds.size()]), date)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}

		if(!addIndications.isEmpty()){
			if(!CcIndication.dao.batchSave(addIndications)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}

		if(!editIndications.isEmpty()){
			if(!CcIndication.dao.batchUpdate(editIndications, "modify_date, content")){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}

       //先删除旧的课程指标点下的课程目标
		if(!CcCourseTargetIndication.dao.deleteAllByColumn("indication_course_id", courseIndicationId, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
       if(!courseTargetIndicationsIds.isEmpty()){
		    Long[] courseTargetIndicationsIdLists = courseTargetIndicationsIds.toArray(new Long[courseTargetIndicationsIds.size()]);
		    if(!CcCourseTargetIndication.dao.deleteAll(courseTargetIndicationsIdLists, date)){
			   TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			   result.put("isSuccess", false);
			   return renderSUC(result, response, header);
		    }
		   //删除大纲中的相应信息
           if(!CcCourseOutlineIndications.dao.deleteAllByColumn("course_target_indication_id", courseTargetIndicationsIdLists, date)){
			   TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			   result.put("isSuccess", false);
			   return renderSUC(result, response, header);
		   }
	   }

		//保存新的课程指标点下的课程目标
		if(!addCourseTargetIndications.isEmpty()){
			if(!CcCourseTargetIndication.dao.batchSave(addCourseTargetIndications)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}

		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}
	
}
