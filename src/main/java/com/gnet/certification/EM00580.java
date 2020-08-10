package com.gnet.certification;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.gnet.model.admin.CcCourseOutline;
import com.gnet.model.admin.CcCourseOutlineHistory;
import com.gnet.model.admin.User;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;

/**
 * 专业负责人一键下发修订任务
 * 
 * @author xzl
 * 
 * @date 2016年7月25日
 *
 */
@Service("EM00580")
@Transactional(readOnly=false)
public class EM00580 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		Map<String, Object> result = new HashMap<>();
		
		User user  = UserCacheKit.getUser(request.getHeader().getToken());
		//版本编号
		Long planId = paramsLongFilter(param.get("planId"));
		
		if(planId == null){
			return renderFAIL("0140", response, header);
		}
		if(user == null){
			return renderFAIL("0081", response, header);
		}
		
		Integer[] status  = new Integer[]{CcCourseOutline.STATUS_ASSIGNED_NOT_CONFIRMED, CcCourseOutline.STATUS_NOT_START};

		List<CcCourseOutline> courseOutlineLists = CcCourseOutline.dao.findCourseOutlineByStatusAndPlanId(planId, status);
		if(courseOutlineLists.isEmpty()){
			result.put("isSuccess", true);
			return renderSUC(result, response, header);
		}
		
		Date date = new Date();
		List<CcCourseOutlineHistory> courseOutlineHistoryLists = Lists.newArrayList();
		for(CcCourseOutline courseOutline : courseOutlineLists){

			courseOutline.set("modify_date", date);
			courseOutline.set("status", CcCourseOutline.STATUS_NOT_SUBMIT);
			
			CcCourseOutlineHistory courseOutlineHistory = new CcCourseOutlineHistory();
			String event = String.format("专业负责人:%s给课程【%s(编号：%s）】的%s下发了修订任务", user.getLong("id"),  courseOutline.getStr("courseName"), courseOutline.getLong("courseId"), courseOutline.getStr("name"));
			IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
			courseOutlineHistory.set("id", idGenerate.getNextValue());
			courseOutlineHistory.set("create_date", date);
			courseOutlineHistory.set("modify_date", date);
			courseOutlineHistory.set("outline_id", courseOutline.getLong("id"));
			courseOutlineHistory.set("trigger_id", user.getLong("id"));
			courseOutlineHistory.set("event", event);
			courseOutlineHistory.set("event_type", CcCourseOutlineHistory.TYPE_ISSUE_TASK);
			courseOutlineHistory.set("is_del", Boolean.FALSE);
			courseOutlineHistoryLists.add(courseOutlineHistory);
		}
		
		//课程大纲状态批量更新
		if(!CcCourseOutline.dao.batchUpdate(courseOutlineLists, "modify_date,status")){
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
	    //批量保存专业负责人下发修订任务记录
		if(!CcCourseOutlineHistory.dao.batchSave(courseOutlineHistoryLists)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}
