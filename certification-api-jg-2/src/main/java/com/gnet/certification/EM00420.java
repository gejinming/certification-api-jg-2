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
import com.gnet.model.admin.CcTeacherCourse;
import com.gnet.object.CcTeacherCourseOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * @author sll
 *
 */
@Service("EM00420")
@Transactional(readOnly=false)
public class EM00420 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		Long teacherId = paramsLongFilter(param.get("teacherId"));
		String name = paramsStringFilter(param.get("name"));
		
		if (teacherId == null) {
			return renderFAIL("0014", response, header);
		}
		
		// 进行分页
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcTeacherCourseOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		Map<String, Object> ccTeacherCoursesMap = Maps.newHashMap();
		Page<CcTeacherCourse> ccTeacherCoursePage = CcTeacherCourse.dao.page(pageable, teacherId, name);
		List<CcTeacherCourse> ccTeacherCourseList = ccTeacherCoursePage.getList();
		// 判断是否分页
		if(pageable.isPaging()){
			ccTeacherCoursesMap.put("totalRow", ccTeacherCoursePage.getTotalRow());
			ccTeacherCoursesMap.put("totalPage", ccTeacherCoursePage.getTotalPage());
			ccTeacherCoursesMap.put("pageSize", ccTeacherCoursePage.getPageSize());
			ccTeacherCoursesMap.put("pageNumber", ccTeacherCoursePage.getPageNumber());
		}
	
		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (CcTeacherCourse temp : ccTeacherCourseList) {
			Map<String, Object> ccTeacherCourse = new HashMap<>();
			ccTeacherCourse.put("id", temp.get("id"));
			ccTeacherCourse.put("teacherCourseId", temp.get("teacher_course_id"));
			ccTeacherCourse.put("courseName", temp.get("courseName"));
			ccTeacherCourse.put("createDate", temp.get("create_date"));
			ccTeacherCourse.put("modifyDate", temp.get("modify_date"));
			ccTeacherCourse.put("planId", temp.get("plan_id"));
			ccTeacherCourse.put("code", temp.get("code"));
			ccTeacherCourse.put("name", temp.get("name"));
			ccTeacherCourse.put("type", temp.get("type"));
			ccTeacherCourse.put("typeName", DictUtils.findLabelByTypeAndKey("courseType", temp.getInt("type")));
			ccTeacherCourse.put("moduleId", temp.get("module_id"));
			ccTeacherCourse.put("moduleName", temp.get("module_name"));
			ccTeacherCourse.put("hierarchyId", temp.get("hierarchy_id"));
			ccTeacherCourse.put("hierarchyName", temp.get("hierarchy_name"));
			ccTeacherCourse.put("propertyId", temp.get("property_id"));
			ccTeacherCourse.put("propertyName", temp.get("property_name"));
			ccTeacherCourse.put("directionId", temp.get("direction_id"));
			ccTeacherCourse.put("directionName", temp.get("direction_name"));
			ccTeacherCourse.put("credit", temp.get("credit"));
			ccTeacherCourse.put("allHours", temp.get("all_hours"));
			ccTeacherCourse.put("theoryHours", temp.get("theory_hours"));
			ccTeacherCourse.put("experimentHours", temp.get("experiment_hours"));
			ccTeacherCourse.put("practiceHours", temp.get("practice_hours"));
			ccTeacherCourse.put("indepentHours", temp.get("indepent_hours"));
			ccTeacherCourse.put("weekHour", temp.get("week_hour"));
			ccTeacherCourse.put("participator", temp.get("participator"));
			ccTeacherCourse.put("applicationMajor", temp.get("application_major"));
			ccTeacherCourse.put("department", temp.get("department"));
			ccTeacherCourse.put("prerequisite", temp.get("prerequisite"));
			ccTeacherCourse.put("nextrequisite", temp.get("nextrequisite"));
			ccTeacherCourse.put("teamLeader", temp.get("team_leader"));
			ccTeacherCourse.put("professorLeader", temp.get("professor_leader"));
			ccTeacherCourse.put("aduitDean", temp.get("aduit_dean"));
			ccTeacherCourse.put("courseGroupId", temp.get("course_group_id"));
			ccTeacherCourse.put("sort", temp.get("sort"));
			ccTeacherCourse.put("remark", temp.get("remark"));
			ccTeacherCourse.put("operateComputerHours", temp.getBigDecimal("operate_computer_hours"));
			ccTeacherCourse.put("teamMember", temp.getStr("team_member"));
			list.add(ccTeacherCourse);
		}
		
		ccTeacherCoursesMap.put("list", list);
		
		return renderSUC(ccTeacherCoursesMap, response, header);
	}
}
