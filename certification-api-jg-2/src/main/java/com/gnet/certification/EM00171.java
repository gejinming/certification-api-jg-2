package com.gnet.certification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourse;
import com.gnet.model.admin.CcPlanTermCourse;
import com.gnet.utils.DictUtils;

/**
 * 查看课程(包括课程大纲信息)详情
 * 
 * @author SY
 * 
 * @date 2016年06月28日 14:26:40
 *
 */
@Service("EM00171")
@Transactional(readOnly=true)
public class EM00171 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		
		if (id == null) {
			return renderFAIL("0250", response, header);
		}
		
		CcCourse temp = CcCourse.dao.findByCourseId(id);
		if(temp == null) {
			return renderFAIL("0251", response, header);
		}

		// 获取培养计划课程学期表
		List<CcPlanTermCourse> ccPlanTermCourseList = CcPlanTermCourse.dao.findWithPlanTermByCourseId(temp.getLong("id"));
		// 上课学期
		List<CcPlanTermCourse> ccPlanTermCourseClassList = new ArrayList<>();
		// 考试学期
		List<CcPlanTermCourse> ccPlanTermCourseExamList = new ArrayList<>();
		for(CcPlanTermCourse ccPlanTermCourse : ccPlanTermCourseList) {
			CcPlanTermCourse tempSave = new CcPlanTermCourse();
			tempSave.put("id", ccPlanTermCourse.getLong("id"));
			tempSave.put("planTermId", ccPlanTermCourse.getLong("plan_term_id"));
			tempSave.put("weekHour", ccPlanTermCourse.getBigDecimal("week_hour"));
			tempSave.put("createDate", ccPlanTermCourse.get("create_date"));
			tempSave.put("modifyDate", ccPlanTermCourse.get("modify_date"));
			tempSave.put("type", ccPlanTermCourse.get("type"));
			tempSave.put("remark", ccPlanTermCourse.get("remark"));
			// 培养计划学期表
			tempSave.put("yearName", ccPlanTermCourse.get("yearName"));
			tempSave.put("termName", ccPlanTermCourse.get("termName"));
			tempSave.put("year", ccPlanTermCourse.get("year"));
			tempSave.put("term", ccPlanTermCourse.get("term"));
			tempSave.put("termType", ccPlanTermCourse.get("termType"));
			tempSave.put("planId", ccPlanTermCourse.get("planId"));
			tempSave.put("weekNums", ccPlanTermCourse.get("weekNums"));
			if(CcPlanTermCourse.TYPE_CLASS.equals(tempSave.get("type"))) {
				ccPlanTermCourseClassList.add(tempSave);
			} else {
				ccPlanTermCourseExamList.add(tempSave);
			}
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("id", temp.getLong("id"));
		map.put("createDate", temp.getDate("create_date"));
		map.put("modifyDate", temp.getDate("modify_date"));
		map.put("planId", temp.getLong("plan_id"));
		map.put("code", temp.getStr("code"));
		map.put("name", temp.getStr("name"));
		map.put("englishName", temp.getStr("english_name"));
		map.put("hierarchyId", temp.getLong("hierarchy_id"));
		map.put("hierarchyName", temp.getStr("hierarchyName"));
		map.put("seconderyHierarchyId", temp.get("hierarchy_secondary_id"));
		map.put("hierarchySecondaryName", temp.get("hierarchySecondaryName"));
		map.put("propertyId", temp.getLong("property_id"));
		map.put("propertyName", temp.getStr("propertyName"));
		map.put("propertySecondaryId", temp.getLong("property_secondary_id"));
		map.put("propertySecondaryName", temp.getStr("propertySecondaryName"));
		map.put("directionId", temp.getLong("direction_id"));
		map.put("credit", temp.getBigDecimal("credit"));
		map.put("allHours", temp.getBigDecimal("all_hours"));
		map.put("theoryHours", temp.getBigDecimal("theory_hours"));
		map.put("experimentHours", temp.getBigDecimal("experiment_hours"));
		map.put("operateComputerHours", temp.getBigDecimal("operate_computer_hours"));
		map.put("practiceHours", temp.getBigDecimal("practice_hours"));
		map.put("weekHour", temp.getBigDecimal("week_hour"));
		map.put("exercisesHours",temp.getBigDecimal("exercises_hours"));
		map.put("dicussHours", temp.getBigDecimal("dicuss_hours"));
		map.put("extracurricularHours", temp.getBigDecimal("extracurricular_hours"));
		map.put("applicationMajor", temp.getStr("application_major"));
		map.put("participator", temp.getStr("participator"));
		map.put("department", temp.getStr("department"));
		map.put("prerequisite", temp.getStr("prerequisite"));
		map.put("nextrequisite", temp.getStr("nextrequisite"));
		map.put("teamLeader", temp.getStr("team_leader"));
		map.put("teamMember", temp.getStr("team_member"));
		map.put("professorLeader", temp.getStr("professor_leader"));
		map.put("aduitDean", temp.getStr("aduit_dean"));
		map.put("courseGroupId", temp.get("course_group_id"));
		map.put("remark", temp.getStr("remark"));

		map.put("moduleId", temp.getLong("module_id"));
		map.put("moduleName", temp.getStr("moduleName"));
		map.put("indepentHours", temp.getBigDecimal("indepent_hours"));
		map.put("sort", temp.getInt("sort"));
		map.put("type", temp.getInt("type"));
		map.put("typeName", DictUtils.findLabelByTypeAndKey("courseType", temp.getInt("type")));
		
		map.put("planTermCourseClassList", ccPlanTermCourseClassList);
		map.put("planTermCourseExamList", ccPlanTermCourseExamList);
		map.put("typeId",temp.getLong("type_id"));
		
		return renderSUC(map, response, header);
	}

}
