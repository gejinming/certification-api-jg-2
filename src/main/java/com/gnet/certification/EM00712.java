package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcCourseOutline;
import com.gnet.model.admin.CcVersion;
import com.gnet.model.admin.User;
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
 * 教师需要编写(审核)大纲的列表
 * @author xzl
 * @date 2017-8-23 19:22:57
 */
@Service("EM00712")
@Transactional(readOnly=true)
public class EM00712 extends BaseApi implements IApi{

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 分页参数
		Integer pageNumber = paramsIntegerFilter(params.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(params.get("pageSize"));
		String orderProperty = paramsStringFilter(params.get("orderProperty"));
		String orderDirection = paramsStringFilter(params.get("orderDirection"));
		//课程名称
		String courseName = paramsStringFilter(params.get("courseName"));
		//大纲名称
		String name = paramsStringFilter(params.get("name"));
		Integer status = paramsIntegerFilter(params.get("status"));
		Boolean isNeedWriteList = paramsBooleanFilter(params.get("isNeedWriteList"));
        String majorName = paramsStringFilter(params.get("majorName"));


		User user  = UserCacheKit.getUser(request.getHeader().getToken());
		if(user == null){
			return renderFAIL("0161", response, header);
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
		Page<CcCourseOutline> page = CcCourseOutline.dao.pageByTeacherId(pageable, user.getLong("id"), name, courseName, majorName, status, isNeedWriteList);

		List<CcCourseOutline> ccCourseOutlineList = page.getList();
		//判断是否分页
		if(pageable.isPaging()){
			// 分页信息返回
			result.put("pageNumber", page.getPageNumber());
			result.put("pageSize", page.getPageSize());
			result.put("totalRow", page.getTotalRow());
			result.put("totalPage", page.getTotalPage());
		}
		
		//返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for(CcCourseOutline temp: ccCourseOutlineList){
			Map<String, Object> ccCourseOutline = Maps.newHashMap();
			ccCourseOutline.put("state",temp.getInt("versionState"));
			ccCourseOutline.put("id", temp.getLong("id"));
			ccCourseOutline.put("createDate", temp.getDate("create_date"));
			ccCourseOutline.put("modifyDate", temp.getDate("modify_date"));
			ccCourseOutline.put("planId", temp.getLong("plan_id"));
			ccCourseOutline.put("courseName", temp.getStr("name"));
			ccCourseOutline.put("englishName", temp.getStr("english_name"));
			ccCourseOutline.put("teacherName", temp.getStr("teacherName"));
			ccCourseOutline.put("propertyName", temp.getStr("propertyName"));
			ccCourseOutline.put("hierarchyName", temp.getStr("hierarchyName"));
			ccCourseOutline.put("moduleName", temp.getStr("moduleName"));
			ccCourseOutline.put("type", temp.getInt("type"));
			ccCourseOutline.put("typeName", DictUtils.findLabelByTypeAndKey("courseType", temp.getInt("type")));
			ccCourseOutline.put("code", temp.getStr("code"));
			ccCourseOutline.put("hierarchyId", temp.getLong("hierarchy_id"));
			ccCourseOutline.put("propertyId", temp.getLong("property_id"));
			ccCourseOutline.put("directionId", temp.getLong("direction_id"));
			ccCourseOutline.put("credit", temp.getBigDecimal("credit"));
			ccCourseOutline.put("allHours", temp.getBigDecimal("all_hours"));
			ccCourseOutline.put("theoryHours", temp.getBigDecimal("theory_hours"));
			ccCourseOutline.put("experimentHours", temp.getBigDecimal("experiment_hours"));
			ccCourseOutline.put("practiceHours", temp.getBigDecimal("practice_hours"));
			ccCourseOutline.put("indepentHours", temp.getBigDecimal("indepent_hours"));
			ccCourseOutline.put("weekHour", temp.getBigDecimal("week_hour"));
			ccCourseOutline.put("applicationMajor", temp.getStr("application_major"));
			ccCourseOutline.put("participator", temp.getStr("participator"));
			ccCourseOutline.put("department", temp.getStr("department"));
			ccCourseOutline.put("prerequisite", temp.getStr("prerequisite"));
			ccCourseOutline.put("nextrequisite", temp.getStr("nextrequisite"));
			ccCourseOutline.put("teamLeader", temp.getStr("team_leader"));
			ccCourseOutline.put("professorLeader", temp.getStr("professor_leader"));
			ccCourseOutline.put("aduitDean", temp.getStr("aduit_dean"));
			ccCourseOutline.put("sort", temp.getInt("sort"));
			ccCourseOutline.put("courseGroupId",temp.getLong("course_group_id"));
			ccCourseOutline.put("remark", temp.get("remark"));
			ccCourseOutline.put("versionName", temp.get("versionName"));
			ccCourseOutline.put("majorName", temp.get("majorName"));
			ccCourseOutline.put("operateComputerHours", temp.getBigDecimal("operate_computer_hours"));
			ccCourseOutline.put("teamMember", temp.getStr("team_member"));
			ccCourseOutline.put("status", temp.getInt("status"));
			ccCourseOutline.put("statusName", DictUtils.findLabelByTypeAndKey("courseOutlineStatus", temp.getInt("status")));
			ccCourseOutline.put("courseOutlineId", temp.getLong("courseOutlineId"));
			ccCourseOutline.put("courseOutlineName", temp.getStr("courseOutlineName"));
			ccCourseOutline.put("outlineTypeId", temp.getLong("outline_type_id"));
			ccCourseOutline.put("outlineTypeName", temp.getStr("outlineTypeName"));
			ccCourseOutline.put("outlineTemplateId", temp.getLong("outline_template_id"));
			if(isNeedWriteList == null){
				ccCourseOutline.put("isAuditList", user.getLong("id").equals(temp.getLong("auditor_id")));
				ccCourseOutline.put("isWriteList", user.getLong("id").equals(temp.getLong("author_id")));
			}else if(isNeedWriteList){
				ccCourseOutline.put("isAuditList", false);
				ccCourseOutline.put("isWriteList", true);
			}else{
				ccCourseOutline.put("isAuditList", true);
				ccCourseOutline.put("isWriteList", false);
			}


			list.add(ccCourseOutline);
		}
		
		result.put("list", list);

		//返回结果
		return renderSUC(result, response, header);

	}

}
