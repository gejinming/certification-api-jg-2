package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcCourse;
import com.gnet.model.admin.CcCourseGroup;
import com.gnet.model.admin.CcPlanTermCourse;
import com.gnet.model.admin.CcVersion;
import com.gnet.object.CcCourseGroupOrderType;
import com.gnet.object.CcCourseOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查看课程分组列表
 * 
 * @author gjm
 * 
 * @date 2016年06月28日 14:26:40
 * 
 */
@Service("EM01186")
@Transactional(readOnly=true)
public class EM01186 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		Long planId = paramsLongFilter(param.get("planId"));
		Integer type = paramsIntegerFilter(param.get("type"));
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
		


		Map<String, Object> ccCoursesMap = Maps.newHashMap();
		Page<CcCourseGroup> ccCoursePage = CcCourseGroup.dao.pageGroup(pageable, planId, directionId, ignoreDirection, hierarchyName, propertyName, null);
		List<CcCourseGroup> ccCourseList = ccCoursePage.getList();

		List<Map<String, Object>> list = new ArrayList<>();

		for (CcCourseGroup temp : ccCourseList) {
			Map<String, Object> ccCourse = new HashMap<>();
			ccCourse.put("id", temp.get("id"));
			ccCourse.put("groupName", temp.get("group_name"));
			ccCourse.put("moduleName", temp.get("moduleName"));
			ccCourse.put("propertyName", temp.get("propertyName"));
			ccCourse.put("propertySecondaryName", temp.get("propertySecondaryName"));
			ccCourse.put("directionName", temp.get("directionName"));
			ccCourse.put("hierarchyName", temp.get("hierarchyName"));
			ccCourse.put("hierarchyId", temp.get("hierarchy_id"));
			ccCourse.put("propertyId", temp.get("property_id"));
			ccCourse.put("directionId", temp.get("direction_id"));
			ccCourse.put("moduleId", temp.get("module_id"));
			ccCourse.put("typeId", temp.get("type"));

			list.add(ccCourse);
		}
		
		ccCoursesMap.put("list", list);
		
		return renderSUC(ccCoursesMap, response, header);
	}
}
