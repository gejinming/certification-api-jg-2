package com.gnet.certification;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourse;
import com.gnet.model.admin.CcIndicationCourse;
import com.gnet.model.admin.CcMajorDirection;
import com.gnet.service.CcIndicationCourseService;
import com.gnet.utils.PriceUtils;
import com.gnet.utils.SpringContextHolder;

/**
 * 编辑指标点课程关系表
 * 
 * @author SY
 * 
 * @date 2016年06月30日 21:17:12
 *
 */
@Service("EM00273")
@Transactional(readOnly=false)
public class EM00273 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		Map<String, Boolean> result = new HashMap<>();
		
		Long id = paramsLongFilter(param.get("id"));
		Long indicationId = paramsLongFilter(param.get("indicationId"));
		Long courseId = paramsLongFilter(param.get("courseId"));
		BigDecimal weight = paramsBigDecimalFilter(param.get("weight"));
		String eduAim = paramsStringFilter(param.get("eduAim"));
		String means = paramsStringFilter(param.get("means"));
		String source = paramsStringFilter(param.get("source"));
		String way = paramsStringFilter(param.get("way"));
		
		if (id == null) {
			return renderFAIL("0360", response, header);
		}
		if (indicationId == null) {
			return renderFAIL("0362", response, header);
		}
		if (courseId == null) {
			return renderFAIL("0363", response, header);
		}
		if (weight == null) {
			return renderFAIL("0364", response, header);
		}
		
		if(!PriceUtils.lessThan(CcIndicationCourse.MIN_WEIGHT, weight)){
			return renderFAIL("0764", response, header);
		}
		CcIndicationCourse ccIndicationCourse = CcIndicationCourse.dao.findFilteredById(id);
		CcCourse course = CcCourse.dao.findFilteredById(courseId);
		if(course == null){
			return renderFAIL("0251", response, header);
		}
		if(ccIndicationCourse == null) {
			return renderFAIL("0361", response, header);
		}
		if(CcIndicationCourse.dao.isExisted(courseId, id, indicationId)){
			return renderFAIL("0368", response, header);
		}
		CcIndicationCourseService ccIndicationCourseService = SpringContextHolder.getBean(CcIndicationCourseService.class);
		
		Long courseGroupId = course.getLong("course_group_id");
		// 专业方向编号
		Long directionId = course.getLong("direction_id");
		// 获取各个方向的权重值
		Map<String, BigDecimal> weightDirectionMap = ccIndicationCourseService.getIndicationWeightDirection(indicationId, courseGroupId, courseId, directionId,null,null);
		// 修改后权重差值加上原本的指点的权重和
		CcMajorDirection ccMajorDirection = null;
		if (directionId != null) {
			ccMajorDirection = CcMajorDirection.dao.findById(directionId);
		}
		
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
		
		if(PriceUtils.greaterThan(updateWeight,CcIndicationCourse.MAX_WEIGHT)){
			return renderFAIL("0762", response, header);
		}
		
		if(courseGroupId != null){
			if(!ccIndicationCourseService.batchUpdate(courseGroupId, indicationId, weight)){
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}
		
		Date date = new Date();
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
		
		return renderSUC(result, response, header);
	}
	
}
