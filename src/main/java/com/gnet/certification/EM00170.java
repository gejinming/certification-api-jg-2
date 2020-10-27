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
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcCourse;
import com.gnet.model.admin.CcPlanTermCourse;
import com.gnet.model.admin.CcVersion;
import com.gnet.object.CcCourseOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看课程表列表
 * 
 * @author SY
 * 
 * @date 2016年06月28日 14:26:40
 * 
 */
@Service("EM00170")
@Transactional(readOnly=true)
public class EM00170 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		String code = paramsStringFilter(param.get("code"));
		String name = paramsStringFilter(param.get("name"));
		Integer credit = paramsIntegerFilter(param.get("credit"));
		Long planId = paramsLongFilter(param.get("planId"));
		//课程层次
		String hierarchyName = paramsStringFilter(param.get("hierarchyName"));
		//课程性质
		String propertyName = paramsStringFilter(param.get("propertyName"));
		if(param.containsKey("planId") && planId == null){
			return renderFAIL("1009", response, header, "planId的参数值非法");
		}
		Long directionId = paramsLongFilter(param.get("directionId"));
		if(param.containsKey("directionId") && directionId == null){
			return renderFAIL("1009", response, header, "directionId的参数值非法");
		}
		Long majorId = paramsLongFilter(param.get("majorId"));
		if(param.containsKey("majorId") && majorId == null){
			return renderFAIL("1009", response, header, "majorId的参数值非法");
		}
		Integer grade = paramsIntegerFilter(param.get("grade"));
		if(param.containsKey("grade") && grade == null){
			return renderFAIL("1009", response, header, "grade的参数值非法");
		}
		
		if(majorId != null && grade != null){
			planId = CcVersion.dao.findNewestVersion(majorId, grade);
			if (planId == null) {
				return renderFAIL("0671", response, header);
			}
		}
		
		// 培养计划编号为空过滤
		if (planId == null) {
			return renderFAIL("0660", response, header);
		}
		//没有传递directionId参数说明忽略专业方向，所以ignoreDirection为true
		Boolean ignoreDirection = !param.containsKey("directionId");
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcCourseOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		Map<String, Object> ccCoursesMap = Maps.newHashMap();
		Page<CcCourse> ccCoursePage = CcCourse.dao.page(pageable, code, name, credit, planId, directionId, ignoreDirection, hierarchyName, propertyName, null);
		List<CcCourse> ccCourseList = ccCoursePage.getList();
		// 判断是否分页
		if(pageable.isPaging()){
			ccCoursesMap.put("totalRow", ccCoursePage.getTotalRow());
			ccCoursesMap.put("totalPage", ccCoursePage.getTotalPage());
			ccCoursesMap.put("pageSize", ccCoursePage.getPageSize());
			ccCoursesMap.put("pageNumber", ccCoursePage.getPageNumber());
		}
	
		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		//如果该版本下没有课程直接返回
		if(ccCourseList.isEmpty()){
			ccCoursesMap.put("list", list);
			return renderSUC(ccCoursesMap, response, header);
		}
		// 建立map以及获取courseIds
		Long[] courseIds = new Long[ccCourseList.size()];
		// map<courseId, List<CcPlanTermCourse>>
		Map<Long, List<CcPlanTermCourse>> coursePlanTermCourseMap = new HashMap<>();
		for(int i = 0; i < ccCourseList.size(); i++) {
			CcCourse temp = ccCourseList.get(i);
			Long courseId = temp.getLong("id");
			courseIds[i] = courseId;
			coursePlanTermCourseMap.put(courseId, new ArrayList<CcPlanTermCourse>());
		}
		// 获取培养计划课程学期表，以及培养计划学期名
		List<CcPlanTermCourse> ccPlanTermCourseList = CcPlanTermCourse.dao.findWithPlanTermByCourseId(courseIds); 
		for(CcPlanTermCourse temp : ccPlanTermCourseList) {
			Long courseId = temp.getLong("course_id");
			List<CcPlanTermCourse> ccPlanTermCourseMapList = coursePlanTermCourseMap.get(courseId);
			ccPlanTermCourseMapList.add(temp);
		}
		
		for (CcCourse temp : ccCourseList) {

			Map<String, Object> ccCourse = new HashMap<>();
			ccCourse.put("id", temp.get("id"));
			ccCourse.put("createDate", temp.get("create_date"));
			ccCourse.put("modifyDate", temp.get("modify_date"));
			ccCourse.put("planId", temp.get("plan_id"));
			ccCourse.put("code", temp.get("code"));
			ccCourse.put("name", temp.get("name"));
			ccCourse.put("allWeight", temp.getBigDecimal("allWeight"));
			ccCourse.put("englishName", temp.get("english_name"));
			ccCourse.put("hierarchyId", temp.get("hierarchy_id"));
			ccCourse.put("hierarchyName", temp.get("hierarchyName"));
			ccCourse.put("hierarchySecondaryId", temp.get("hierarchy_secondary_id"));
			ccCourse.put("hierarchySecondaryName", temp.get("hierarchySecondaryName"));
			ccCourse.put("propertyId", temp.get("property_id"));
			ccCourse.put("propertyName", temp.get("propertyName"));
			ccCourse.put("propertySecondaryId", temp.get("property_secondary_id"));
			ccCourse.put("propertySecondaryName", temp.get("propertySecondaryName"));
			ccCourse.put("directionId", temp.get("direction_id"));
			ccCourse.put("directionName", temp.get("directionName"));
			ccCourse.put("credit", temp.get("credit"));
			ccCourse.put("allHours", temp.get("all_hours"));
			ccCourse.put("theoryHours", temp.get("theory_hours"));
			ccCourse.put("experimentHours", temp.get("experiment_hours"));
			ccCourse.put("practiceHours", temp.get("practice_hours"));
			ccCourse.put("operateComputerHours", temp.getBigDecimal("operate_computer_hours"));
			ccCourse.put("weekHour", temp.get("week_hour"));
			ccCourse.put("applicationMajor", temp.get("application_major"));
			ccCourse.put("participator", temp.get("participator"));
			ccCourse.put("department", temp.get("department"));
			ccCourse.put("prerequisite", temp.get("prerequisite"));
			ccCourse.put("nextrequisite", temp.get("nextrequisite"));
			ccCourse.put("courseGroupId", temp.get("course_group_id"));
			ccCourse.put("courseGroupMangeId", temp.get("mange_group_id"));
			ccCourse.put("remark", temp.get("remark"));
			ccCourse.put("exercisesHours",temp.getBigDecimal("exercises_hours"));
			ccCourse.put("dicussHours", temp.getBigDecimal("dicuss_hours"));
			ccCourse.put("extracurricularHours", temp.getBigDecimal("extracurricular_hours"));
			
			ccCourse.put("moduleId", temp.get("module_id"));
			ccCourse.put("moduleName", temp.get("moduleName"));
			ccCourse.put("indepentHours", temp.get("indepent_hours"));
			ccCourse.put("teamLeader", temp.get("team_leader"));
			ccCourse.put("teamMember", temp.getStr("team_member"));
			ccCourse.put("professorLeader", temp.get("professor_leader"));
			ccCourse.put("aduitDean", temp.get("aduit_dean"));
			ccCourse.put("sort", temp.get("sort"));
			ccCourse.put("type", temp.get("type"));
			ccCourse.put("typeName", DictUtils.findLabelByTypeAndKey("courseType", temp.getInt("type")));
			ccCourse.put("courseType",temp.get("course_type"));
			ccCourse.put("courseScoreType",temp.get("course_score_type"));
			
			// 获取培养计划课程学期表
			List<CcPlanTermCourse> ccPlanTermCourseMapList = coursePlanTermCourseMap.get(temp.get("id"));
			// 上课学期
			List<CcPlanTermCourse> ccPlanTermCourseClassList = new ArrayList<>();
			// 考试学期
			List<CcPlanTermCourse> ccPlanTermCourseExamList = new ArrayList<>();
			for(CcPlanTermCourse ccPlanTermCourse : ccPlanTermCourseMapList) {
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
			ccCourse.put("planTermCourseClassList", ccPlanTermCourseClassList);
			ccCourse.put("planTermCourseExamList", ccPlanTermCourseExamList);
			list.add(ccCourse);
		}
		
		ccCoursesMap.put("list", list);
		
		return renderSUC(ccCoursesMap, response, header);
	}
}
