package com.gnet.certification;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
 * 专业负责人确认分配某门课程大纲编写任务给某个教师 
 * 
 * @author xzl
 * 
 * @date 2016年7月25日
 *
 */
@Deprecated
@Service("EM00579")
@Transactional(readOnly=false)
public class EM00579 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		Map<String, Boolean> result = new HashMap<>();
		
		//课程教学大纲文本编号
		Long courseOutlineId = paramsLongFilter(param.get("courseOutlineId"));
		User user  = UserCacheKit.getUser(request.getHeader().getToken());
		
		if(courseOutlineId == null){
			return renderFAIL("0531", response, header);
		}
		//通过课程大纲编号得到课程信息和执笔人信息
		CcCourse course = CcCourse.dao.findByCourseOutlineId(courseOutlineId);
		if(course == null){
			return renderFAIL("0251", response, header);
		}
		CcCourseOutline courseOutline = CcCourseOutline.dao.findFilteredById(courseOutlineId);
		//确认分配把状态设为未开始,更新课程大纲信息
		Date date = new Date();
		courseOutline.set("modify_date", date);
		courseOutline.set("status", CcCourseOutline.STATUS_NOT_START);
		if(!courseOutline.update()){
        	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
        }
		
		String event = "专业负责人"+ user.getStr("name") + "确认分配教师"+ course.getStr("teacherName") + "编写【" + course.getStr("name") +"】的课程大纲"; 
		
		//保存专业负责人确认分配某门课程大纲编写给某个教师 任务历史记录
		CcCourseOutlineHistory courseOutlineHistory = new CcCourseOutlineHistory();
		courseOutlineHistory.set("create_date", date);
		courseOutlineHistory.set("modify_date", date);
		courseOutlineHistory.set("outline_id", courseOutlineId);
		courseOutlineHistory.set("trigger_id", user.getLong("id"));
		courseOutlineHistory.set("event", event);
		courseOutlineHistory.set("event_type", CcCourseOutlineHistory.TYPE_AUDIT_PASS);
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
