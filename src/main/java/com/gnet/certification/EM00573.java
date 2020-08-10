package com.gnet.certification;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import com.jfinal.kit.StrKit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourse;
import com.gnet.model.admin.CcCourseOutline;
import com.gnet.model.admin.CcCourseOutlineHistory;
import com.gnet.model.admin.User;



/**
 * 专业负责人审核教师编写大纲内容(不允许修改大纲)
 * 
 * @author xzl
 * 
 * @date 2016年7月13日
 *
 */
@Service("EM00573")
@Transactional(readOnly=false)
public class EM00573 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		Map<String, Boolean> result = new HashMap<>();
		
		//课程教学大纲文本编号
		Long courseOutlineId = paramsLongFilter(param.get("courseOutlineId"));
		String auditComment = paramsStringFilter(param.get("auditComment"));
		//状态(审核通过，驳回)
		Boolean audit = paramsBooleanFilter(param.get("audit"));
	    
	    //专业负责人信息
		User user = UserCacheKit.getUser(request.getHeader().getToken());
		if(user == null ){
			return renderFAIL("0536", response, header);
		}
	    if(courseOutlineId == null){
			return renderFAIL("0531", response, header);
		}
		if(audit == null){
			return renderFAIL("0535", response, header);
		}

		if(!audit && StrKit.isBlank(auditComment)){
			return renderFAIL("0873",response, header);
		}

	    //通过课程大纲编号得到课程信息和执笔人信息
		CcCourse course = CcCourse.dao.findByCourseOutlineId(courseOutlineId);
		if(course == null){
			return renderFAIL("0251", response, header);
		}
		String courseName = course.getStr("name");
		Long targetId = course.getLong("author_id");
		String teacherName = course.getStr("authorName");
		auditComment = StrKit.isBlank(auditComment) ? "通过" : auditComment;

		if(targetId == null){
			return renderFAIL("0160", response, header);
		}
		
		CcCourseOutline courseOutline = CcCourseOutline.dao.findFilteredById(courseOutlineId);
		//更新课程大纲信息
		Date date = new Date();
		courseOutline.set("modify_date", date);
		courseOutline.set("status", audit?CcCourseOutline.STATUS_AUDIT_PASS:CcCourseOutline.STATUS_AUDIT_DISMISSED);
		courseOutline.set("audit_comment", auditComment);
		if(!courseOutline.update()){
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		String event = String.format("教师【编号:%s】%s%s了教师%s编写的【%s】的课程大纲", user.getLong("id"), user.getStr("name"), audit ? "通过" : "驳回", teacherName, courseName);
		
		//保存专业负责人审核大纲历史记录
		CcCourseOutlineHistory courseOutlineHistory = new CcCourseOutlineHistory();
		courseOutlineHistory.set("create_date", date);
		courseOutlineHistory.set("modify_date", date);
		courseOutlineHistory.set("outline_id", courseOutlineId);
		courseOutlineHistory.set("trigger_id", user.getLong("id"));
		courseOutlineHistory.set("target_id", targetId);
		courseOutlineHistory.set("event", event);
		courseOutlineHistory.set("event_type", audit?CcCourseOutlineHistory.TYPE_AUDIT_PASS:CcCourseOutlineHistory.TYPE_AUDIT_DISMISSED);
		courseOutlineHistory.set("audit_comment", auditComment);
		courseOutlineHistory.set("is_del", Boolean.FALSE);
	   
        if(!courseOutlineHistory.save()){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	    
	}
	
}
