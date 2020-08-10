package com.gnet.certification;

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
import com.gnet.plugin.id.IdGenerate;
import com.gnet.response.ServiceResponse;
import com.gnet.service.CcCourseOutlineService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 负责人强制审核通过的大纲改为待审核大纲
 * 
 * @author xzl
 * 
 * @date 2017年10月6日
 *
 */
@Service("EM00730")
@Transactional(readOnly=false)
public class EM00730 extends BaseApi implements IApi{
	
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


		List<CcCourseOutline> ccCourseOutlines = CcCourseOutline.dao.findByCourseOutlineIds(courseOutlineIds.toArray(new Long[courseOutlineIds.size()]));
        if(ccCourseOutlines.isEmpty()){
			return renderFAIL("0537", response, header);
		}

		CcCourse course = CcCourse.dao.findByCourseOutlineId(ccCourseOutlines.get(0).getLong("id"));
		if(course == null){
			return renderFAIL("0251", response, header);
		}

		List<CcCourseOutlineHistory> ccCourseOutlineHistories = Lists.newArrayList();

		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		Date date = new Date();
		for(CcCourseOutline ccCourseOutline : ccCourseOutlines){
			if(!CcCourseOutline.STATUS_AUDIT_PASS.equals(ccCourseOutline.getInt("status"))){
				return renderFAIL("0880", response, header);
			}

			ccCourseOutline.set("modify_date", date);
			ccCourseOutline.set("status", CcCourseOutline.STATUS_PENDING_AUDIT);
			ccCourseOutline.set("audit_comment", null);

			String event = String.format("负责人【编号:%s】%s强制把,教师【编号:%s】%s编写的审核通过【%s】的课程大纲改为待审核", user.getLong("id"), user.getStr("name"), ccCourseOutline.getLong("author_id"), ccCourseOutline.getStr("authorName"), ccCourseOutline.getStr("courseName"));
			CcCourseOutlineHistory courseOutlineHistory = new CcCourseOutlineHistory();

			courseOutlineHistory.set("id", idGenerate.getNextValue());
			courseOutlineHistory.set("create_date", date);
			courseOutlineHistory.set("modify_date", date);
			courseOutlineHistory.set("outline_id", ccCourseOutline.getLong("id"));
			courseOutlineHistory.set("trigger_id", user.getLong("id"));
			courseOutlineHistory.set("target_id", ccCourseOutline.getLong("author_id"));
			courseOutlineHistory.set("event", event);
			courseOutlineHistory.set("event_type", CcCourseOutlineHistory.TYPE_FORCE_PENDING_AUDIT);
			courseOutlineHistory.set("is_del", Boolean.FALSE);

			ccCourseOutlineHistories.add(courseOutlineHistory);
		}

		if(!CcCourseOutline.dao.batchUpdate(ccCourseOutlines, "modify_date, status, audit_comment")){
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
