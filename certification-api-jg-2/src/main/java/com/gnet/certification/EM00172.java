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
import com.gnet.service.CcPlanTermCourseService;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.kit.StrKit;

/**
 * 增加课程表-理论
 * 
 * @author SY
 * 
 * @date 2016年06月28日 14:26:40
 *
 */
@Service("EM00172")
@Transactional(readOnly=false)
public class
EM00172 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Long planId = paramsLongFilter(param.get("planId"));
		String code = paramsStringFilter(param.get("code"));
		String name = paramsStringFilter(param.get("name"));
		String englishName = paramsStringFilter(param.get("englishName"));
		Long hierarchyId = paramsLongFilter(param.get("hierarchyId"));
		Long hierarchySecondaryId = paramsLongFilter(param.get("hierarchySecondaryId"));
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
		//课程编码验证
        if (CcCourse.dao.isExisted("code", planId, code)) {
            return renderFAIL("0270", response, header);
        }
		//学时为空的赋值0
		if (theoryHours == null) {
			theoryHours=paramsBigDecimalFilter(0);
		}
		if (experimentHours == null) {
			experimentHours=paramsBigDecimalFilter(0);
		}
		if (practiceHours == null) {
			practiceHours=paramsBigDecimalFilter(0);
		}
		if(operateComputerHours == null){
			operateComputerHours=paramsBigDecimalFilter(0);
		}
		if (weekHour == null) {
			weekHour=paramsBigDecimalFilter(0);
		}
		if (exercisesHours==null){
            exercisesHours=paramsBigDecimalFilter(0);
        }
		if (dicussHours==null){
            dicussHours=paramsBigDecimalFilter(0);
        }
        if (extracurricularHours==null){
            extracurricularHours=paramsBigDecimalFilter(0);
        };
		//下面判断条件
		if (planTermClassIds.isEmpty()) {
			return renderFAIL("0268", response, header);
		}
		if (planId == null) {
			return renderFAIL("0252", response, header);
		}
		/*if (StrKit.isBlank(code)) {
			return renderFAIL("0253", response, header);
		}
		if (StrKit.isBlank(name)) {
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
		
		CcCourse ccCourse = new CcCourse();
		
		ccCourse.set("create_date", date);
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
		ccCourse.set("practice_hours", practiceHours);
		ccCourse.set("operate_computer_hours", operateComputerHours);
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
	    ccCourse.set("direction_change_date", date);
	    ccCourse.set("type_id",typeId);
		Boolean isSuccess = ccCourse.save();
		
		Map<String, Object> result = new HashMap<>();
		
		if(!isSuccess) {
			result.put("isSuccess", isSuccess);
			return renderSUC(result, response, header);
		}
		
		Long courseId = ccCourse.getLong("id");
		
		CcPlanTermCourseService ccPlanTermCourseService = SpringContextHolder.getBean(CcPlanTermCourseService.class);
		isSuccess = ccPlanTermCourseService.addCcPlanTermCourse(courseId, planTermExamIds, planTermClassIds, date);
		if(!isSuccess) {
			result.put("isSuccess", isSuccess);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return renderSUC(result, response, header);
		}

		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}
}
