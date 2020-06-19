package com.gnet.certification;

import java.math.BigDecimal;
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
import com.gnet.service.CcPlanTermCourseService;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.kit.StrKit;

/**
 * 编辑-理论-课程表
 * 
 * @author SY
 * 
 * @date 2016年06月28日 14:26:40
 *
 */
@Service("EM00173")
@Transactional(readOnly=false)
public class EM00173 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		Long planId = paramsLongFilter(param.get("planId"));
		String code = paramsStringFilter(param.get("code"));
		String name = paramsStringFilter(param.get("name"));
		String englishName = paramsStringFilter(param.get("englishName"));
		Long hierarchyId = paramsLongFilter(param.get("hierarchyId"));
		Long hierarchySecondaryId = paramsLongFilter(param.get("seconderyHierarchyId"));
		Long propertyId = paramsLongFilter(param.get("propertyId"));
		Long propertySecondaryId = paramsLongFilter(param.get("propertySecondaryId"));
		Long directionId = paramsLongFilter(param.get("directionId"));
		BigDecimal credit = paramsBigDecimalFilter(param.get("credit"));
		BigDecimal allHours = paramsBigDecimalFilter(param.get("allHours"));
		BigDecimal theoryHours = paramsBigDecimalFilter(param.get("theoryHours"));
		BigDecimal experimentHours = paramsBigDecimalFilter(param.get("experimentHours"));
		BigDecimal operateComputerHours = paramsBigDecimalFilter(param.get("operateComputerHours"));
		BigDecimal practiceHours = paramsBigDecimalFilter(param.get("practiceHours"));
		BigDecimal weekHour = paramsBigDecimalFilter(param.get("weekHour"));
		BigDecimal exercisesHours = paramsBigDecimalFilter(param.get("exercisesHours"));
		BigDecimal dicussHours = paramsBigDecimalFilter(param.get("dicussHours"));
		BigDecimal extracurricularHours = paramsBigDecimalFilter(param.get("extracurricularHours"));
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
		Integer sort = paramsIntegerFilter(param.get("sort"));
		BigDecimal indepentHours = paramsBigDecimalFilter("indepentHours");
		Long typeId = paramsLongFilter(param.get("typeId"));
		// 培养计划课程学期详情表
		List<Long> planTermClassIds = paramsJSONArrayFilter(param.get("planTermClassIds"), Long.class);
		List<Long> planTermExamIds = paramsJSONArrayFilter(param.get("planTermExamIds"), Long.class);
		
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
		/*if (StrKit.isBlank(name)) {
			return renderFAIL("0254", response, header);
		}*/
		if (credit == null) {
			return renderFAIL("0259", response, header);
		}
		if (allHours == null) {
			return renderFAIL("0260", response, header);
		}
		if (theoryHours == null) {
			return renderFAIL("0261", response, header);
		}
		if (experimentHours == null) {
			return renderFAIL("0262", response, header);
		}
		if (practiceHours == null) {
			return renderFAIL("0263", response, header);
		}
		if(operateComputerHours == null){
			return  renderFAIL("0264", response, header);
		}
		if (weekHour == null) {
			return renderFAIL("0265", response, header);
		}
		if(hierarchyId == null){
			return renderFAIL("0104", response, header);
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
		ccCourse.set("hierarchy_id", hierarchyId);
		ccCourse.set("hierarchy_secondary_id", hierarchySecondaryId);
		ccCourse.set("property_id", propertyId);
		ccCourse.set("property_secondary_id", propertySecondaryId);
		ccCourse.set("direction_id", directionId);
		ccCourse.set("credit", credit);
		ccCourse.set("all_hours", allHours);
		ccCourse.set("theory_hours", theoryHours);
		ccCourse.set("experiment_hours", experimentHours);
		ccCourse.set("operate_computer_hours", operateComputerHours);
		ccCourse.set("practice_hours", practiceHours);
		ccCourse.set("exercises_hours",exercisesHours);
		ccCourse.set("dicuss_hours", dicussHours);
		ccCourse.set("extracurricular_hours", extracurricularHours);
		ccCourse.set("week_hour", weekHour);
		ccCourse.set("application_major", applicationMajor);
		ccCourse.set("participator", participator);
		ccCourse.set("department", department);
		ccCourse.set("prerequisite", prerequisite);
		ccCourse.set("nextrequisite", nextrequisite);
		ccCourse.set("team_leader", teamLeader);
		ccCourse.set("team_member", teamMember);
		ccCourse.set("professor_leader", professorLeader);
		ccCourse.set("aduit_dean", aduitDean);
		ccCourse.set("remark", remark);
		ccCourse.set("is_del", Boolean.FALSE);
		ccCourse.set("sort", sort);
		ccCourse.set("type", CcCourse.TYPE_THEORY);
		ccCourse.set("indepent_hours", indepentHours);
		ccCourse.set("type_id",typeId);
		if((orginalDirectioniId == null && directionId != null) || (orginalDirectioniId != null && !orginalDirectioniId.equals(directionId))){
			ccCourse.set("direction_change_date", date);
		}
		Boolean isSuccess = ccCourse.update();
		Map<String, Object> result = new HashMap<>();
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
		isSuccess = ccPlanTermCourseService.addCcPlanTermCourse(id, planTermExamIds, planTermClassIds, date);
		if(!isSuccess) {
			result.put("isSuccess", isSuccess);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			renderSUC(result, response, header);
		}


		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}
	
}
