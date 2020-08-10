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
import com.jfinal.kit.StrKit;



/**
 * 教师课程大纲的编写保存接口
 * 
 * @author xzl
 * 
 * @date 2016年7月14日
 *
 */
@Deprecated
@Service("EM00576")
@Transactional(readOnly=false)
public class EM00576 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		Map<String, Boolean> result = new HashMap<>();
		
		//课程教学大纲文本编号
		Long courseOutlineId = paramsLongFilter(param.get("courseOutlineId"));
		//课程教学大纲内容
		String content = paramsStringFilter(param.get("content"));
		User user  = UserCacheKit.getUser(request.getHeader().getToken());
		if(user == null ){
			return renderFAIL("0536", response, header);
		}
		if(courseOutlineId == null){
			return renderFAIL("0531", response, header);
		}
		//通过课程大纲编号得到课程信息和执笔人信息
		CcCourse course = CcCourse.dao.findByCourseOutlineId(courseOutlineId);
		if(course == null){
			return renderFAIL("0251", response, header);
		}
		
		if(StrKit.notBlank(content) && content.length() > CcCourseOutline.CONTENT_MAX_LEANGHT){
			return renderFAIL("0540", response, header);
		}
			
		CcCourseOutline courseOutline = CcCourseOutline.dao.findFilteredById(courseOutlineId);
		
		//课程大纲的保存
		Date date = new Date();
		courseOutline.set("modify_date", date);
		courseOutline.set("content", content);
		courseOutline.set("status", CcCourseOutline.STATUS_NOT_SUBMIT);
		if(!courseOutline.update()){
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
        }
		
		String event = "教师(编号：" + user.getLong("id") + ")" + user.getStr("name") + "保存编写的【" + course.getStr("name") + "(编号："+ course.getLong("id") + ")" + "】的课程大纲";
		CcCourseOutlineHistory courseOutlineHistory = new CcCourseOutlineHistory();
		courseOutlineHistory.set("create_date", date);
		courseOutlineHistory.set("modify_date", date);
		courseOutlineHistory.set("outline_id", courseOutlineId);
		courseOutlineHistory.set("trigger_id", user.getLong("id"));
		courseOutlineHistory.set("event", event);
		courseOutlineHistory.set("event_type", CcCourseOutlineHistory.TYPE_COMPILE);
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
