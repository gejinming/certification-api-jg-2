package com.gnet.certification;

import java.math.BigDecimal;
import java.util.*;

import com.gnet.model.admin.*;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.DictUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.service.CcIndicationCourseService;
import com.gnet.utils.PriceUtils;
import com.gnet.utils.SpringContextHolder;

/**
 * 增加或编辑课程指标点关系
 * 
 * @author xzl
 * 
 * @date 2016年10月28日10:25:50
 *
 */
@Service("EM00276")
@Transactional(readOnly=false)
public class EM00276 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Map<String, Object> result = new HashMap<>();
		
		Long indicationId = paramsLongFilter(param.get("indicationId"));
		Long courseId = paramsLongFilter(param.get("courseId"));
		BigDecimal weight = paramsBigDecimalFilter(param.get("weight"));
		String eduAim = paramsStringFilter(param.get("eduAim"));
		String means = paramsStringFilter(param.get("means"));
		String source = paramsStringFilter(param.get("source"));
		String way = paramsStringFilter(param.get("way"));
		if (indicationId == null) {
			return renderFAIL("0362", response, header);
		}
		if (courseId == null) {
			return renderFAIL("0363", response, header);
		}
		if (weight == null) {
			return renderFAIL("0364", response, header);
		}
		
		//单个权重不能超过1
		if(PriceUtils.greaterThan(weight, CcIndicationCourse.MAX_WEIGHT)){
			return renderFAIL("0760", response, header);
		}
		
		CcCourse course = CcCourse.dao.findFilteredById(courseId);
		if(course == null){
			return renderFAIL("0251", response, header);
		}

		BigDecimal twos[] = {CcEvaluteLevel.LEVEL_TOW_A_VALUE, CcEvaluteLevel.LEVEL_TOW_B_VALUE};
		BigDecimal fives[] = {CcEvaluteLevel.LEVEL_FIVE_A_VALUE, CcEvaluteLevel.LEVEL_FIVE_B_VALUE, CcEvaluteLevel.LEVEL_FIVE_C_VALUE, CcEvaluteLevel.LEVEL_FIVE_D_VALUE, CcEvaluteLevel.LEVEL_FIVE_E_VALUE};

		Date date = new Date();
		List<CcEvaluteLevel> ccEvaluteLevels = CcEvaluteLevel.dao.findByCourseId(courseId);
		List<CcEvaluteLevel> evaluteLevels = new ArrayList<>();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		if(!ccEvaluteLevels.isEmpty()){
			for(CcEvaluteLevel evaluteLevel : ccEvaluteLevels){
				Integer level = evaluteLevel.getInt("level");
				Long teacherCourseId = evaluteLevel.getLong("teacher_course_id");
				if(CcEvaluteLevel.LEVEL_TOW.equals(level)){
					for(int i=1; i<= CcEvaluteLevel.LEVEL_TOW; i++){
						CcEvaluteLevel ccEvaluteLevel = new CcEvaluteLevel();
						ccEvaluteLevel.set("id", idGenerate.getNextValue());
						ccEvaluteLevel.set("create_date", date);
						ccEvaluteLevel.set("modify_date", date);
						ccEvaluteLevel.set("level_name", DictUtils.findLabelByTypeAndKey("evaluteLevelTow", i));
						ccEvaluteLevel.set("score", twos[i-1]);
						ccEvaluteLevel.set("teacher_course_id", teacherCourseId);
						ccEvaluteLevel.set("indication_id", indicationId);
						ccEvaluteLevel.set("level", CcEvaluteLevel.LEVEL_TOW);
						ccEvaluteLevel.set("is_del", Boolean.FALSE);
						evaluteLevels.add(ccEvaluteLevel);
					}
				}else if(CcEvaluteLevel.LEVEL_FIVE.equals(level)){
					for(int i=1; i<= CcEvaluteLevel.LEVEL_FIVE; i++ ){
						CcEvaluteLevel ccEvaluteLevel = new CcEvaluteLevel();
						ccEvaluteLevel.set("id", idGenerate.getNextValue());
						ccEvaluteLevel.set("create_date", date);
						ccEvaluteLevel.set("modify_date", date);
						ccEvaluteLevel.set("level_name", DictUtils.findLabelByTypeAndKey("evaluteLevelFive", i));
						ccEvaluteLevel.set("score", fives[i-1]);
						ccEvaluteLevel.set("teacher_course_id", teacherCourseId);
						ccEvaluteLevel.set("indication_id", indicationId);
						ccEvaluteLevel.set("is_del", Boolean.FALSE);
						ccEvaluteLevel.set("level", CcEvaluteLevel.LEVEL_FIVE);
						evaluteLevels.add(ccEvaluteLevel);
					}
				}
			}
			if(!CcEvaluteLevel.dao.batchSave(evaluteLevels)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				result.put("isSuccess", true);
			}
		}

		Long courseGroupId = course.getLong("course_group_id");
		// 专业方向编号
		Long directionId = course.getLong("direction_id");
		CcIndicationCourseService ccIndicationCourseService = SpringContextHolder.getBean(CcIndicationCourseService.class);
		CcIndicationCourse ccIndicationCourse = CcIndicationCourse.dao.findByCourseIdAndIndicationId(courseId, indicationId);
		if(ccIndicationCourse != null){
			//修改后权重差值加上原本的指点的权重和
			CcMajorDirection ccMajorDirection = null;
			if (directionId != null) {
				ccMajorDirection = CcMajorDirection.dao.findById(directionId);
			}
			
			Map<String, BigDecimal> weightDirectionMap = ccIndicationCourseService.getIndicationWeightDirection(indicationId, courseGroupId, courseId, directionId);
			
			BigDecimal updateWeight = null;
			if (ccMajorDirection != null && weightDirectionMap.get(ccMajorDirection.getStr("name")) != null) {
				updateWeight = PriceUtils.add(weight, weightDirectionMap.get(ccMajorDirection.getStr("name")));
				
			} else if (ccMajorDirection == null && !weightDirectionMap.isEmpty() && weightDirectionMap.get("无方向") == null) {
				for (Map.Entry<String, BigDecimal> entry : weightDirectionMap.entrySet()) {
					updateWeight = (updateWeight == null || PriceUtils.greaterThan(entry.getValue(), updateWeight)) ? entry.getValue() : updateWeight;
				}
				
				updateWeight = PriceUtils.add(updateWeight, weight);
			} else if (weightDirectionMap.get("无方向") != null) {
				updateWeight = PriceUtils.add(weight, weightDirectionMap.get("无方向"));
				
			} else {
				updateWeight = weight;
			}
			
			if(PriceUtils.greaterThan(updateWeight, CcIndicationCourse.MAX_WEIGHT)){
				return renderFAIL("0762", response, header);
			}
			
			if(courseGroupId != null){
				if(!ccIndicationCourseService.batchUpdate(courseGroupId, indicationId, weight)){
					result.put("isSuccess", false);
					return renderSUC(result, response, header);
				}
			}
			
			ccIndicationCourse.set("modify_date", date);
			ccIndicationCourse.set("indication_id", indicationId);
			ccIndicationCourse.set("course_id", courseId);
			ccIndicationCourse.set("weight", weight);
			ccIndicationCourse.set("edu_aim", eduAim);
			ccIndicationCourse.set("means", means);
			ccIndicationCourse.set("source", source);
			ccIndicationCourse.set("way", way);
			
			if(!ccIndicationCourse.update()){
				if(courseGroupId != null){
				    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}	
			result.put("isSuccess", true);
		}else{
			
			// 获取各个方向的权重值
			Map<String, BigDecimal> weightDirectionMap = ccIndicationCourseService.getIndicationWeightDirection(indicationId, courseGroupId, null, directionId);
			
			CcMajorDirection ccMajorDirection = null;
			if (directionId != null) {
				ccMajorDirection = CcMajorDirection.dao.findById(directionId);
			}
			
			BigDecimal allWeight = null;
			if (ccMajorDirection != null && weightDirectionMap.get(ccMajorDirection.getStr("name")) != null) {
				allWeight = PriceUtils.add(weight, weightDirectionMap.get(ccMajorDirection.getStr("name")));
				
			} else if (ccMajorDirection == null && !weightDirectionMap.isEmpty() && weightDirectionMap.get("无方向") == null) {
				// 维护一门无专业方向的课程且该指标点下存在方向课
				for (Map.Entry<String, BigDecimal> entry : weightDirectionMap.entrySet()) {
					allWeight = (allWeight == null || PriceUtils.greaterThan(entry.getValue(), allWeight)) ? entry.getValue() : allWeight;
					if(PriceUtils.greaterThan(PriceUtils.add(allWeight, weight), CcIndicationCourse.MAX_WEIGHT)){
						return renderFAIL("0760", response, header, "课程指标点在" + entry.getKey() + "下的权重已为" + entry.getValue() + "加上" + weight + "就超过1了");
					}
				}
				
				allWeight = PriceUtils.add(allWeight, weight);
			} else if (weightDirectionMap.get("无方向") != null) {
				allWeight = PriceUtils.add(weight, weightDirectionMap.get("无方向"));
				
			} else {
				allWeight = weight;
			}
			
			//判断增加后的权重和是否大于1
			if(PriceUtils.greaterThan(allWeight, CcIndicationCourse.MAX_WEIGHT)){
				return renderFAIL("0760", response, header);
			}
			
			if(courseGroupId != null){
				if(!ccIndicationCourseService.batchUpdate(courseGroupId, indicationId, weight)){
					result.put("isSuccess", false);
					return renderSUC(result, response, header);
				}
			}
			
			ccIndicationCourse = new CcIndicationCourse();
			ccIndicationCourse.set("create_date", date);
			ccIndicationCourse.set("modify_date", date);
			ccIndicationCourse.set("indication_id", indicationId);
			ccIndicationCourse.set("course_id", courseId);
			ccIndicationCourse.set("weight", weight);
			ccIndicationCourse.set("edu_aim", eduAim);
			ccIndicationCourse.set("means", means);
			ccIndicationCourse.set("source", source);
			ccIndicationCourse.set("way", way);
			ccIndicationCourse.set("is_del", Boolean.FALSE);
			if(!ccIndicationCourse.save()){
				if(courseGroupId != null){
				    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}	
			result.put("isSuccess", true);
			result.put("id", ccIndicationCourse.getLong("id"));
		}

		return renderSUC(result, response, header);
	}
}
