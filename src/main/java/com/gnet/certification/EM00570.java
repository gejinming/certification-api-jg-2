package com.gnet.certification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.CcCourseOutline;
import com.gnet.utils.ConvertUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
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
import com.gnet.model.admin.CcVersion;
import com.gnet.object.CcCourseOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;
/**
 * 专业负责人得到当前版本的所有课程的大纲编写任务列表
 * @author xzl
 * @Date 2016年7月13日
 */
@Service("EM00570")
@Transactional(readOnly=true)
public class EM00570 extends BaseApi implements IApi{

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 分页参数
		Integer pageNumber = paramsIntegerFilter(params.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(params.get("pageSize"));
		String orderProperty = paramsStringFilter(params.get("orderProperty"));
		String orderDirection = paramsStringFilter(params.get("orderDirection"));
		//课程编码
		String code = paramsStringFilter(params.get("code"));
		//课程名称
		String name = paramsStringFilter(params.get("courseName"));
		//课程层次
		String hierarchyName = paramsStringFilter(params.get("hierarchyName"));
		//课程性质
		String propertyName = paramsStringFilter(params.get("propertyName"));
        //大纲状态
		Integer courseOutlineStatus = paramsIntegerFilter(params.get("status"));


		//版本编号
		Long planId = paramsLongFilter(params.get("planId"));
		if(params.containsKey("planId") && planId == null) {
		    return renderFAIL("1009", response, header, "planId的参数值非法");
		}
		Long majorId = paramsLongFilter(params.get("majorId"));
		if(params.containsKey("majorId") && majorId == null) {
		    return renderFAIL("1009", response, header, "majorId的参数值非法");
		}
		Integer grade = paramsIntegerFilter(params.get("grade"));
		if(params.containsKey("grade") && grade == null) {
		    return renderFAIL("1009", response, header, "grade的参数值非法");
		}
		
		if(majorId != null && grade != null){
			planId = CcVersion.dao.findNewestVersion(majorId, grade);
		}
		
		if(planId == null){
			return renderFAIL("0140", response, header);
		}
		
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcCourseOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		//查询并返回结果		
		Map<String, Object> result = new HashMap<>();

		Map<Long, List<Map<String, Object>>> courseoutlineMap = new HashMap<>();
		List<CcCourseOutline> ccCourseOutlines = CcCourseOutline.dao.listByPlanId(planId, code, name, courseOutlineStatus);
		for(CcCourseOutline temp : ccCourseOutlines){
			Map<String, Object> map = new HashMap<>();
			Integer status = temp.getInt("status");
			Long courseId = temp.getLong("courseId");
			map.put("courseOutlineId", temp.getLong("courseOutlineId"));
			map.put("courseOutlineName", temp.getStr("courseOutlineName"));
			map.put("status", status);
			map.put("statusName", DictUtils.findLabelByTypeAndKey("courseOutlineStatus", status));
			map.put("outlineTypeName", temp.getStr("outlineTypeName"));
			map.put("authorName", temp.getStr("authorName"));
			map.put("auditorName", temp.getStr("auditorName"));

			List<Map<String, Object>> courseOutlines = courseoutlineMap.get(courseId);
			if(courseOutlines == null){
				courseOutlines = Lists.newArrayList();
				courseoutlineMap.put(courseId, courseOutlines);
			}
			courseOutlines.add(map);

		}
		Page<CcCourse> page = CcCourse.dao.page(pageable, code, name, null, planId, null, true, hierarchyName, propertyName, courseOutlineStatus);
		List<CcCourse> courseList = page.getList();
		//判断是否分页
		if(pageable.isPaging()){
			// 分页信息返回
			result.put("pageNumber", page.getPageNumber());
			result.put("pageSize", page.getPageSize());
			result.put("totalRow", page.getTotalRow());
			result.put("totalPage", page.getTotalPage());
		}
		
		//返回内容过滤
		Map<Long, CcCourse> courseMap = new HashMap<>();
		List<Map<String, Object>> list = new ArrayList<>();
		for(CcCourse temp: courseList){
			Map<String, Object> ccCourse = Maps.newHashMap();
			Long courseId = temp.getLong("id");
			ccCourse.put("id", courseId);
			ccCourse.put("createDate", temp.getDate("create_date"));
			ccCourse.put("modifyDate", temp.getDate("modify_date"));
			ccCourse.put("planId", temp.getLong("plan_id"));
			ccCourse.put("courseName", temp.getStr("name"));
			ccCourse.put("englishName", temp.getStr("english_name"));
			ccCourse.put("teacherName", temp.getStr("teacherName"));
			ccCourse.put("propertyName", temp.getStr("propertyName"));
			ccCourse.put("hierarchyName", temp.getStr("hierarchyName"));
			ccCourse.put("moduleName", temp.getStr("moduleName"));
			ccCourse.put("type", temp.getInt("type"));
			ccCourse.put("code", temp.getStr("code"));
			ccCourse.put("hierarchyId", temp.getLong("hierarchy_id"));
			ccCourse.put("propertyId", temp.getLong("property_id"));
			ccCourse.put("directionId", temp.getLong("direction_id"));
			ccCourse.put("credit", temp.getBigDecimal("credit"));
			ccCourse.put("allHours", temp.getBigDecimal("all_hours"));
			ccCourse.put("theoryHours", temp.getBigDecimal("theory_hours"));
			ccCourse.put("experimentHours", temp.getBigDecimal("experiment_hours"));
			ccCourse.put("practiceHours", temp.getBigDecimal("practice_hours"));
			ccCourse.put("indepentHours", temp.getBigDecimal("indepent_hours"));
			ccCourse.put("weekHour", temp.getBigDecimal("week_hour"));
			ccCourse.put("applicationMajor", temp.getStr("application_major"));
			ccCourse.put("participator", temp.getStr("participator"));
			ccCourse.put("department", temp.getStr("department"));
			ccCourse.put("prerequisite", temp.getStr("prerequisite"));
			ccCourse.put("nextrequisite", temp.getStr("nextrequisite"));
			ccCourse.put("teamLeader", temp.getStr("team_leader"));
			ccCourse.put("professorLeader", temp.getStr("professor_leader"));
			ccCourse.put("aduitDean", temp.getStr("aduit_dean"));
			ccCourse.put("sort", temp.getInt("sort"));
			ccCourse.put("courseGroupId",temp.getLong("course_group_id"));
			ccCourse.put("remark", temp.get("remark"));
			ccCourse.put("operateComputerHours", temp.getBigDecimal("operate_computer_hours"));
			ccCourse.put("teamMember", temp.getStr("team_member"));
			ccCourse.put("courseOutlines", courseoutlineMap.get(courseId));
			list.add(ccCourse);
		}

		result.put("list", list);
		
		//返回结果
		return renderSUC(result, response, header);
		
				
	}

}
