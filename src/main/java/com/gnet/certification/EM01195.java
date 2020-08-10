package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
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
 * 编辑教学分组--限选表
 * 
 * @author GJM
 * 
 * @date 2016年07月14日 11:10:53
 *
 */
@Service("EM01195")
@Transactional(readOnly=false)
public class EM01195 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		BigDecimal allHours = paramsBigDecimalFilter(param.get("allHours"));
		Long planId = paramsLongFilter(param.get("planId"));
		String remark = paramsStringFilter(param.get("remark"));
		String groupName = paramsStringFilter(param.get("groupName"));
		List<Long> groupIds = paramsJSONArrayFilter(param.get("groupId"), Long.class);
		
		Integer type = CcCourseGroup.TYPE_LIMITED_SELECT;
		
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
		if(groupIds.isEmpty() || groupIds.size() < 2) {
			return renderFAIL("0567", response, header);
		}
		if(CcCourse.dao.isGroup(groupIds, id)) {
			return renderFAIL("0566", response, header);
		}
		//建立分级教学时，不同课程组的总学时和学分必须相同
		List<CcCourse> groupCourseList = CcCourse.dao.sumGroupMangeCreditAndHours(groupIds.toArray(new Long[groupIds.size()]));
		BigDecimal credit =new BigDecimal("0");
		BigDecimal allHouse =new BigDecimal("0");
		//判断学时和学分是否相同
		for (int i=0;i<groupCourseList.size();i++){
			CcCourse ccCourse = groupCourseList.get(i);
			BigDecimal credit1 = ccCourse.getBigDecimal("credit");
			BigDecimal allHours1 = ccCourse.getBigDecimal("all_hours");
			if (i==0){
				credit=credit1;
				allHouse=allHours1;
			}else {
				//比较
				int flag = credit.compareTo(credit1);
				int flag1 = allHouse.compareTo(allHours1);
				if(flag!=0 || flag1!=0){
					return renderFAIL("2553", response, header);
				}
			}

		}
		Map<String, Boolean> result = new HashMap<>();
		
		Date date = new Date();
		CcCourseGroupMangeTeach ccCourseGroup = CcCourseGroupMangeTeach.dao.findFilteredById(id);
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
		isSuccess= CcCourseGroupTeachMange.dao.deleteAllByColumn("teach_group_id",courseGroupId,date);
		if(!isSuccess) {
			result.put("isSuccess", isSuccess);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return renderSUC(result, response, header);
		}

		// 新增联系
		List<CcCourseGroupTeachMange> ccCourseGroupTeachMangeList = Lists.newArrayList();

		for (int i=0; i<groupIds.size();i++){
			CcCourseGroupTeachMange GroupCourse = new CcCourseGroupTeachMange();
			GroupCourse.set("teach_group_id",courseGroupId);
			GroupCourse.set("group_id", groupIds.get(i));
			ccCourseGroupTeachMangeList.add(GroupCourse);
		}
		isSuccess=CcCourseGroupTeachMange.dao.batchSave(ccCourseGroupTeachMangeList);
		if(!isSuccess) {
			result.put("isSuccess", isSuccess);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return renderSUC(result, response, header);
		}
		
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}
	
}
