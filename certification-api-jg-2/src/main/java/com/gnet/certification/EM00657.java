package com.gnet.certification;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcPlanCourseZone;
import com.gnet.model.admin.CcPlanCourseZoneTerm;
import com.google.common.collect.Maps;

/**
 * 编辑培养计划分区信息
 * 
 * @author wct
 * @date 2016年8月8日
 */
@Service("EM00657")
@Transactional(readOnly = false)
public class EM00657 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long zoneId = paramsLongFilter(params.get("zoneId"));
		BigDecimal credit = paramsBigDecimalFilter(params.get("credit"));
		BigDecimal allHours = paramsBigDecimalFilter(params.get("allHours"));
		BigDecimal theoryHours = paramsBigDecimalFilter(params.get("theoryHours"));
		BigDecimal experimentHours = paramsBigDecimalFilter(params.get("experimentHours"));
		BigDecimal practiceHours = paramsBigDecimalFilter(params.get("practiceHours"));
		BigDecimal independentHours = paramsBigDecimalFilter(params.get("independentHours"));
		BigDecimal exercisesHours = paramsBigDecimalFilter(params.get("exercisesHours"));
		BigDecimal dicussHours = paramsBigDecimalFilter(params.get("dicussHours"));
		BigDecimal extracurricularHours = paramsBigDecimalFilter(params.get("extracurricularHours"));
		BigDecimal operateComputerHours = paramsBigDecimalFilter(paramsBigDecimalFilter(params.get("operateComputerHours")));

		JSONObject termObject = paramsJSONObjectFilter(params.get("termObject"));
		
		// 课程区域编号为空过滤
		if (zoneId == null) {
			return renderFAIL("0663", response, header);
		}
		CcPlanCourseZone ccPlanCourseZone = CcPlanCourseZone.dao.findFilteredById(zoneId);
		// 课程区域不存在
		if (ccPlanCourseZone == null) {
			return renderFAIL("0664", response, header);
		}
		
		Date date = new Date();
		// 寻找历史的学期学时信息
		List<CcPlanCourseZoneTerm> ccPlanCourseZoneTerms = CcPlanCourseZoneTerm.dao.findFilteredByColumn("plan_course_zone_id", zoneId);
		// 更新数据准备
		for (CcPlanCourseZoneTerm ccPlanCourseZoneTerm : ccPlanCourseZoneTerms) {
			String termIdStr = ccPlanCourseZoneTerm.getLong("plan_term_id").toString();
			if (termObject.get(termIdStr) != null) {
				ccPlanCourseZoneTerm.set("least_ave_week_hours", termObject.get(termIdStr));
			} else {
				ccPlanCourseZoneTerm.set("least_ave_week_hours", null);
			}
			
			ccPlanCourseZoneTerm.set("modify_date", date);
		}
		
		// 结果返回
		Map<String, Object> result = Maps.newHashMap();
		// 更新学期数据
		if (!ccPlanCourseZoneTerms.isEmpty() && !CcPlanCourseZoneTerm.dao.batchUpdate(ccPlanCourseZoneTerms, "modify_date, least_ave_week_hours")) {
			result.put("isSuccess", Boolean.FALSE);
			return renderSUC(result, response, header);
		}
		
		// 课程区域编辑保存
		ccPlanCourseZone.set("least_score", credit);
		ccPlanCourseZone.set("least_hours", allHours);
		ccPlanCourseZone.set("least_theory_hours", theoryHours);
		ccPlanCourseZone.set("least_experiment_hours", experimentHours);
		ccPlanCourseZone.set("least_practice_hours", practiceHours);
		ccPlanCourseZone.set("least_independent_hours", independentHours);
		ccPlanCourseZone.set("least_exercises_hours", exercisesHours);
		ccPlanCourseZone.set("least_dicuss_hours", dicussHours);
		ccPlanCourseZone.set("least_extracurricular_hours", extracurricularHours);
		ccPlanCourseZone.set("least_operate_computer_hours", operateComputerHours);
		ccPlanCourseZone.set("modify_date", date);
		if (!ccPlanCourseZone.update()) {
			result.put("isSuccess", Boolean.FALSE);
			return renderSUC(result, response, header);
		}
		
		result.put("isSuccess", Boolean.TRUE);
		return renderSUC(result, response, header);
	}

}
