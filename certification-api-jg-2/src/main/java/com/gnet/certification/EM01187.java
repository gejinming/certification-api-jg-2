package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 增加课程分组--限选表
 * 
 * @author gjm
 * 
 * @date 2016年07月14日 11:10:53
 *
 */
@Service("EM01187")
@Transactional(readOnly=false)
public class EM01187 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		List<Long> courseIds = paramsJSONArrayFilter(param.get("courseIds"), Long.class);
		BigDecimal allHours = paramsBigDecimalFilter(param.get("allHours"));
		Long planId = paramsLongFilter(param.get("planId"));
		String remark = paramsStringFilter(param.get("remark"));
		String groupName = paramsStringFilter(param.get("groupName"));
		Integer type = paramsIntegerFilter(param.get("typeId"));
		
		if (allHours == null) {
			return renderFAIL("0564", response, header);
		}
		if (planId == null) {
			return renderFAIL("0565", response, header);
		}
		if(courseIds.isEmpty() || courseIds.size() < 2) {
			return renderFAIL("0567", response, header);
		}
		/*if(CcCourse.dao.isGroupMange(courseIds,2)) {
			return renderFAIL("0566", response, header);
		}*/
		if(CcCourse.dao.isGroup(courseIds,1)) {
			return renderFAIL("2551", response, header);
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

		CcCourseGroupMange ccCourseGroup = new CcCourseGroupMange();

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
		//将数据添加到中间表cc_course_group_mange_group
		// 更新CcCourseGroup
		List<CcCourseGroupMangeGroup> ccCourseGroupCourseList = Lists.newArrayList();
		for (int i=0; i<courseIds.size();i++){
			CcCourseGroupMangeGroup ccCourseGroupMageGroup = new CcCourseGroupMangeGroup();
			ccCourseGroupMageGroup.set("mange_group_id",courseGroupId);
			ccCourseGroupMageGroup.set("course_id", courseIds.get(i));
			ccCourseGroupCourseList.add(ccCourseGroupMageGroup);
		}
		isSuccess=CcCourseGroupMangeGroup.dao.batchSave(ccCourseGroupCourseList);
		if(!isSuccess) {
			result.put("isSuccess", isSuccess);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return renderSUC(result, response, header);
		}
		
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}
}
