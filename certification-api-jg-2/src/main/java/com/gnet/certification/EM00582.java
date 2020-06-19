package com.gnet.certification;


import java.math.BigDecimal;
import java.util.*;


import com.gnet.model.admin.*;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;


/**
 * 专业负责人给某门课程的大纲下发修订任务
 * 
 * @author xzl
 * 
 * @date 2016年8月4日
 *
 */
@Service("EM00582")
@Transactional(readOnly=false)
public class EM00582 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		Map<String, Boolean> result = new HashMap<>();

		List<Long> courseOutlineIds = paramsJSONArrayFilter(param.get("courseOutlineIds"), Long.class);
		User user = UserCacheKit.getUser(request.getHeader().getToken());

		if(courseOutlineIds.isEmpty()){
			return renderFAIL("0531", response, header);
		}

		CcCourse course = CcCourse.dao.findByCourseOutlineId(courseOutlineIds.get(0));
		if(course == null){
			return renderFAIL("0251", response, header);
		}

		List<CcCourseOutline> ccCourseOutlines = CcCourseOutline.dao.findByCourseOutlineIds(courseOutlineIds.toArray(new Long[courseOutlineIds.size()]));
		List<CcCourseOutlineHistory> ccCourseOutlineHistories = Lists.newArrayList();

		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		Date date = new Date();
		for(CcCourseOutline ccCourseOutline : ccCourseOutlines){

			ccCourseOutline.set("modify_date", date);
			ccCourseOutline.set("status", CcCourseOutline.STATUS_NOT_SUBMIT);

			String event = String.format("专业负责人【编号:%s】%s给课程【%s(编号:%s)】的大纲%s,下发了修订任务", user.getLong("id"), user.getStr("name"), ccCourseOutline.getStr("courseName"), ccCourseOutline.getLong("course_id"), ccCourseOutline.getStr("name"));
			CcCourseOutlineHistory courseOutlineHistory = new CcCourseOutlineHistory();

			courseOutlineHistory.set("id", idGenerate.getNextValue());
			courseOutlineHistory.set("create_date", date);
			courseOutlineHistory.set("modify_date", date);
			courseOutlineHistory.set("outline_id", ccCourseOutline.getLong("id"));
			courseOutlineHistory.set("trigger_id", user.getLong("id"));
			courseOutlineHistory.set("event", event);
			courseOutlineHistory.set("event_type", CcCourseOutlineHistory.TYPE_ISSUE_TASK);
			courseOutlineHistory.set("is_del", Boolean.FALSE);

			ccCourseOutlineHistories.add(courseOutlineHistory);
		}

		if(!CcCourseOutline.dao.batchUpdate(ccCourseOutlines, "modify_date, status")){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		if(!CcCourseOutlineHistory.dao.batchSave(ccCourseOutlineHistories)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	    
	}
	
}
