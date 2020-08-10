package com.gnet.certification;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.gnet.response.ServiceResponse;
import com.gnet.service.CcCourseOutlineService;
import com.gnet.utils.SpringContextHolder;
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
 * 教师课程大纲的编写提交接口
 * 
 * @author xzl
 * 
 * @date 2016年7月14日
 *
 */

@Service("EM00581")
@Transactional(readOnly=false)
public class EM00581 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		Map<String, Boolean> result = new HashMap<>();
		
		//课程教学大纲编号
		Long courseOutlineId = paramsLongFilter(param.get("courseOutlineId"));
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
			
		CcCourseOutline courseOutline = CcCourseOutline.dao.findById(courseOutlineId);
		if(courseOutline == null){
			return renderFAIL("0537", response, header);
		}

		CcCourseOutlineService ccCourseOutlineService = SpringContextHolder.getBean(CcCourseOutlineService.class);
		ServiceResponse serviceResponse = ccCourseOutlineService.canChangeStatus(courseOutline, course);
		if(!serviceResponse.isSucc()){
			return renderFAIL("0875", response, header, serviceResponse.getContent());
		}

		if(!courseOutline.getInt("status").equals(CcCourseOutline.STATUS_NOT_SUBMIT)){
			return renderFAIL("0538", response, header);
		}

		//课程大纲的保存
		Date date = new Date();
		courseOutline.set("modify_date", date);
		courseOutline.set("status", CcCourseOutline.STATUS_PENDING_AUDIT);
		if(!courseOutline.update()){
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
        }

		String event = String.format("教师(教师编号:%s)%s提交了【%s】的课程大纲(大纲编号:%s)", user.getLong("id"), user.getStr("name"), course.getStr("name"), courseOutlineId);
		CcCourseOutlineHistory courseOutlineHistory = new CcCourseOutlineHistory();
		courseOutlineHistory.set("create_date", date);
		courseOutlineHistory.set("modify_date", date);
		courseOutlineHistory.set("outline_id", courseOutlineId);
		courseOutlineHistory.set("trigger_id", user.getLong("id"));
		courseOutlineHistory.set("event", event);
		courseOutlineHistory.set("event_type", CcCourseOutlineHistory.TYPE_SUBMIT);
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
