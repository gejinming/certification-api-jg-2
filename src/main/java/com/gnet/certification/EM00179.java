package com.gnet.certification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.gnet.model.admin.CcPlanTermCourse;
import com.gnet.service.CcMajorTeacherService;
import com.gnet.service.CcPlanTermCourseService;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.kit.StrKit;

/**
 * 编辑-实践-课程表
 * 
 * @author SY
 * 
 * @date 2016年7月27日19:49:58
 *
 */
@Service("EM00179")
@Transactional(readOnly=false)
public class EM00179 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		Long planId = paramsLongFilter(param.get("planId"));
		String code = paramsStringFilter(param.get("code"));
		String name = paramsStringFilter(param.get("name"));
		String englishName = paramsStringFilter(param.get("englishName"));
		Long directionId = paramsLongFilter(param.get("directionId"));
		BigDecimal credit = paramsBigDecimalFilter(param.get("credit"));
		BigDecimal allHours = paramsBigDecimalFilter(param.get("allHours"));
		BigDecimal weekHour = paramsBigDecimalFilter(param.get("weekOrWeekHour"));
		String applicationMajor = paramsStringFilter(param.get("applicationMajor"));
		String participator = paramsStringFilter(param.get("participator"));
		String department = paramsStringFilter(param.get("department"));
		String prerequisite = paramsStringFilter(param.get("prerequisite"));
		String nextrequisite = paramsStringFilter(param.get("nextrequisite"));
		String teamLeader = paramsStringFilter(param.get("teamLeader"));
		String teamMember = paramsStringFilter(param.get("teamMember"));
		String professorLeader = paramsStringFilter(param.get("professorLeader"));
		String aduitDean = paramsStringFilter(param.get("aduitDean"));
		String remark = paramsStringFilter(param.get("remark"));
		Long moduleId = paramsLongFilter(param.get("moduleId"));
		Integer sort = paramsIntegerFilter(param.get("sort"));
		BigDecimal indepentHours = paramsBigDecimalFilter("indepentHours");
		Integer courseType = paramsIntegerFilter(param.get("courseType"));
		Integer courseScoreType = paramsIntegerFilter(param.get("courseScoreType"));

		// 培养计划课程学期详情表
		List<Long> planTermClassIds = paramsJSONArrayFilter(param.get("planTermClassIds"), Long.class);
		
		if (planTermClassIds.isEmpty()) {
			return renderFAIL("0268", response, header);
		}
		if (id == null) {
			return renderFAIL("0250", response, header);
		}
		if (planId == null) {
			return renderFAIL("0252", response, header);
		}
		if (StrKit.isBlank(code)) {
			return renderFAIL("0253", response, header);
		}
		if (StrKit.isBlank(name)) {
			return renderFAIL("0254", response, header);
		}
		if (credit == null) {
			return renderFAIL("0259", response, header);
		}
		if (weekHour == null) {
			return renderFAIL("0265", response, header);
		}
		if(moduleId == null){
			return renderFAIL("0404", response, header);
		}
		 
		
		Date date = new Date();
		CcCourse ccCourse = CcCourse.dao.findFilteredById(id);
		if(ccCourse == null) {
			return renderFAIL("0251", response, header);
		}

		if (CcCourse.dao.isExisted("code", code, planId, id)) {
			return renderFAIL("0270", response, header);
		}
		Long orginalDirectioniId = ccCourse.getLong("direction_id");
		ccCourse.set("modify_date", date);
		ccCourse.set("plan_id", planId);
		ccCourse.set("code", code);
		ccCourse.set("name", name);
		ccCourse.set("english_name", englishName);
		ccCourse.set("direction_id", directionId);
		ccCourse.set("credit", credit);
		ccCourse.set("all_hours", allHours);
		ccCourse.set("week_hour", weekHour);
		ccCourse.set("application_major", applicationMajor);
		ccCourse.set("participator", participator);
		ccCourse.set("department", department);
		ccCourse.set("prerequisite", prerequisite);
		ccCourse.set("nextrequisite", nextrequisite);
		ccCourse.set("team_leader", teamLeader);
		ccCourse.set("professor_leader", professorLeader);
		ccCourse.set("aduit_dean", aduitDean);
		ccCourse.set("team_member", teamMember);
		ccCourse.set("remark", remark);
		ccCourse.set("is_del", Boolean.FALSE);
		ccCourse.set("module_id", moduleId);
		ccCourse.set("sort", sort);
		ccCourse.set("type", CcCourse.TYPE_PRACTICE);
		ccCourse.set("indepent_hours", indepentHours);
		ccCourse.set("course_type",courseType);
		ccCourse.set("course_score_type",courseScoreType);
		if((orginalDirectioniId == null && directionId != null) || (orginalDirectioniId != null && !orginalDirectioniId.equals(directionId))){
			ccCourse.set("direction_change_date", date);
		}
		Boolean isSuccess = ccCourse.update();
		Map<String, Boolean> result = new HashMap<>();
		if(!isSuccess) {
			result.put("isSuccess", isSuccess);
			return renderSUC(result, response, header);
		}
		
		// 删除原本的培养计划课程学期详情表
		// 删除CcPlanTermCourse
		if(!CcPlanTermCourse.dao.deleteAllByColumnHard("course_id", id)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		// 建立培养计划课程学期详情表
		CcPlanTermCourseService ccPlanTermCourseService = SpringContextHolder.getBean(CcPlanTermCourseService.class);
		isSuccess = ccPlanTermCourseService.addCcPlanTermCourse(id, new ArrayList<Long>(), planTermClassIds, date);
		if(!isSuccess) {
			result.put("isSuccess", isSuccess);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			renderSUC(result, response, header);
		}

		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}
	
}
