package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourse;
import com.gnet.model.admin.CcCourseGroup;
import com.gnet.model.admin.CcCourseGroupMange;
import com.gnet.model.admin.CcCourseGroupMangeGroup;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 编辑课程组--限选表
 * 
 * @author GJM
 * 
 * @date 2016年07月14日 11:10:53
 *
 */
@Service("EM01189")
@Transactional(readOnly=false)
public class EM01189 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		BigDecimal allHours = paramsBigDecimalFilter(param.get("allHours"));
		Long planId = paramsLongFilter(param.get("planId"));
		String remark = paramsStringFilter(param.get("remark"));
		String groupName = paramsStringFilter(param.get("groupName"));
		List<Long> courseIds = paramsJSONArrayFilter(param.get("courseIds"), Long.class);
		
		Integer type = paramsIntegerFilter(param.get("typeId"));
		
		if (id == null) {
			return renderFAIL("0560", response, header);
		}
		if (type == null) {
			return renderFAIL("0563", response, header);
		}
		if (allHours == null) {
			return renderFAIL("0564", response, header);
		}
		if (planId == null) {
			return renderFAIL("0565", response, header);
		}
		if(courseIds.isEmpty() || courseIds.size() < 2) {
			return renderFAIL("0567", response, header);
		}
		if(CcCourse.dao.isGroup(courseIds, id)) {
			return renderFAIL("0566", response, header);
		}
		
		Map<String, Boolean> result = new HashMap<>();
		
		Date date = new Date();
		CcCourseGroupMange ccCourseGroup = CcCourseGroupMange.dao.findFilteredById(id);
		if(ccCourseGroup == null) {
			return renderFAIL("0561", response, header);
		}
		ccCourseGroup.set("modify_date", date);
		ccCourseGroup.set("type", type);
		ccCourseGroup.set("all_hours", allHours);
		ccCourseGroup.set("plan_id", planId);
		ccCourseGroup.set("remark", remark);
		ccCourseGroup.set("group_name", groupName);
		Boolean isSuccess = ccCourseGroup.update();
		if(!isSuccess) {
			result.put("isSuccess", isSuccess);
			return renderSUC(result, response, header);
		}
		
		Long courseGroupId = ccCourseGroup.getLong("id");
		//先删除原关联
		isSuccess=CcCourseGroupMangeGroup.dao.deleteAllByColumn("mange_group_id",courseGroupId,date);
		if(!isSuccess) {
			result.put("isSuccess", isSuccess);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return renderSUC(result, response, header);
		}
		
		// 新增联系
		List<CcCourseGroupMangeGroup> ccCourseGroupCourseList = Lists.newArrayList();

		for (int i=0; i<courseIds.size();i++){
			CcCourseGroupMangeGroup ccCourseGroupCourse = new CcCourseGroupMangeGroup();
			ccCourseGroupCourse.set("mange_group_id",courseGroupId);
			ccCourseGroupCourse.set("course_id", courseIds.get(i));
			ccCourseGroupCourseList.add(ccCourseGroupCourse);
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
