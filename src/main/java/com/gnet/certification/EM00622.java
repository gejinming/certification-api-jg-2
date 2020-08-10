package com.gnet.certification;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.CcCourseGroupCourse;
import com.gnet.model.admin.CcCourseOutlineTemplateModule;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourse;
import com.gnet.model.admin.CcCourseGroup;
import com.google.common.collect.Maps;

/**
 * 增加课程组--限选表
 * 
 * @author SY
 * 
 * @date 2016年07月14日 11:10:53
 *
 */
@Service("EM00622")
@Transactional(readOnly=false)
public class EM00622 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		List<Long> courseIds = paramsJSONArrayFilter(param.get("courseIds"), Long.class);
		BigDecimal allHours = paramsBigDecimalFilter(param.get("allHours"));
		Long planId = paramsLongFilter(param.get("planId"));
		String remark = paramsStringFilter(param.get("remark"));
		String groupName = paramsStringFilter(param.get("groupName"));
		Integer type = paramsIntegerFilter(param.get("typeId"));//CcCourseGroup.TYPE_LIMITED_SELECT;
		
		if (allHours == null) {
			return renderFAIL("0564", response, header);
		}
		if (planId == null) {
			return renderFAIL("0565", response, header);
		}
		if(courseIds.isEmpty() || courseIds.size() < 2) {
			return renderFAIL("0567", response, header);
		}

		if(CcCourse.dao.isGroup(courseIds,1)) {
			return renderFAIL("2552", response, header);
		}
		if (CcCourse.dao.isGroupMange(courseIds,2)){
			return renderFAIL("0566", response, header);
		}
		
		List<CcCourse> ccCourseList = CcCourse.dao.findFilteredByColumnIn("id", courseIds.toArray(new Long[courseIds.size()]));
		if(ccCourseList.isEmpty()){
			return renderFAIL("0568", response, header);
		}
		//同一课程组成下的课程区域必须相同(理论课程的所属模块必须相同，实践课程的课程层次，专业方向，课程性质必须相同),课程类型必须相同
		//通过第一个课程来判断课程类型
		Map<Integer, String> flagMap = Maps.newHashMap();
		for(CcCourse course : ccCourseList){
			Integer courseType = course.getInt("type");
			String zoneId = null;
			// 判断是否为实践课
			if (courseType.equals(CcCourse.TYPE_PRACTICE)) {
				if (course.getLong("module_id") == null) {
					return renderFAIL("0404", response, header);
				}
				
				zoneId = String.valueOf(course.getLong("module_id"));
			} else {
				if (course.getLong("hierarchy_id") == null) {
					return renderFAIL("0104", response, header);
				}
				
				zoneId = String.valueOf(course.getLong("hierarchy_id")) + String.valueOf(course.getLong("property_id"));
			}
			
			if (flagMap.get(courseType) == null) {
				flagMap.put(courseType, zoneId);
			} else {
				if (!zoneId.equals(flagMap.get(courseType))) {
					return renderFAIL("0569", response, header, "课程组中理论课的课程层次和课程性质不完全相同或实践课的所属模块不完全相同");
				}
			}
		}
		
		Date date = new Date();
		
		CcCourseGroup ccCourseGroup = new CcCourseGroup();
		
		ccCourseGroup.set("create_date", date);
		ccCourseGroup.set("modify_date", date);
		ccCourseGroup.set("credit", new BigDecimal(0));
		ccCourseGroup.set("type", type);
		ccCourseGroup.set("all_hours", allHours);
		ccCourseGroup.set("plan_id", planId);
		ccCourseGroup.set("remark", remark);
		ccCourseGroup.set("group_name",groupName);
		ccCourseGroup.set("is_del", Boolean.FALSE);
		Boolean isSuccess = ccCourseGroup.save();
		
		Long courseGroupId = ccCourseGroup.getLong("id");
		
		Map<String, Object> result = new HashMap<>();
		result.put("id", courseGroupId);
		
		if(!isSuccess) {
			result.put("isSuccess", isSuccess);
			return renderSUC(result, response, header);
		}
		if (type==1){
			// 更新course
			for(CcCourse temp : ccCourseList) {
				temp.set("modify_date", date);
				temp.set("course_group_id", courseGroupId);
			}

		}else {
			for(CcCourse temp : ccCourseList) {
				temp.set("modify_date", date);
				temp.set("course_group_mange_id", courseGroupId);
			}
		}
		isSuccess = CcCourse.dao.batchUpdate(ccCourseList, "modify_date,course_group_id,course_group_mange_id");

		
		if(!isSuccess) {
			result.put("isSuccess", isSuccess);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return renderSUC(result, response, header);
		}
		
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}
}
