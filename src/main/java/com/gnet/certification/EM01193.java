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
 * 增加教学分组--限选表
 * 
 * @author gjm
 * 
 * @date 2020年06月10日 11:10:53
 *
 */
@Service("EM01193")
@Transactional(readOnly=false)
public class EM01193 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		List<Long> courseGroupIds = paramsJSONArrayFilter(param.get("courseGroupIds"), Long.class);
		BigDecimal allHours = paramsBigDecimalFilter(param.get("allHours"));
		Long planId = paramsLongFilter(param.get("planId"));
		String remark = paramsStringFilter(param.get("remark"));
		String groupName = paramsStringFilter(param.get("groupName"));
		Integer type = CcCourseGroupMange.TYPE_LIMITED_SELECT;
		
		if (allHours == null) {
			return renderFAIL("0564", response, header);
		}
		if (planId == null) {
			return renderFAIL("0565", response, header);
		}
		if(courseGroupIds.isEmpty() || courseGroupIds.size() < 2) {
			return renderFAIL("0567", response, header);
		}
		/*if(CcCourse.dao.isGroup(courseGroupIds)) {
			return renderFAIL("0566", response, header);
		}*/
		//建立分级教学时，不同课程组的总学时和学分必须相同
		List<CcCourse> groupCourseList = CcCourse.dao.sumGroupMangeCreditAndHours(courseGroupIds.toArray(new Long[courseGroupIds.size()]));
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

		List<CcCourseGroupMange> ccCourseList = CcCourseGroupMange.dao.findFilteredByColumnIn("id", courseGroupIds.toArray(new Long[courseGroupIds.size()]));
		if(ccCourseList.isEmpty()){
			return renderFAIL("0568", response, header);
		}

		Date date = new Date();

		CcCourseGroupMangeTeach ccCourseGroup = new CcCourseGroupMangeTeach();

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

		//将数据添加到中间表CcCourseGroupTeachMange
		List<CcCourseGroupTeachMange> ccCourseGroupTeachMangeList = Lists.newArrayList();
		for (int i=0; i<courseGroupIds.size();i++){
			CcCourseGroupTeachMange ccCourseGroupMageGroup = new CcCourseGroupTeachMange();
			ccCourseGroupMageGroup.set("teach_group_id",courseGroupId);
			ccCourseGroupMageGroup.set("group_id", courseGroupIds.get(i));
			ccCourseGroupTeachMangeList.add(ccCourseGroupMageGroup);
		}
		isSuccess=CcCourseGroupTeachMange.dao.batchSave(ccCourseGroupTeachMangeList);
		if(!isSuccess) {
			result.put("isSuccess", isSuccess);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return renderSUC(result, response, header);
		}


		if(!isSuccess) {
			result.put("isSuccess", isSuccess);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return renderSUC(result, response, header);
		}
		
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}
}
