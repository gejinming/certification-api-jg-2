package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.service.CcCourseOutlineService;
import com.gnet.service.CcCourseService;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.plugin.activerecord.Record;

/**
 * 批量删除课程表
 * 
 * @author SY
 * 
 * @date 2016年06月28日 14:26:40
 *
 */
@Service("EM00174")
@Transactional(readOnly=false)
public class EM00174 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String, Object> result = new HashMap<>();
		
		Date date = new Date();
		
		List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		String token = request.getHeader().getToken();
		User user = UserCacheKit.getUser(token);
		
		// 删除前判断一下是否可以删除
		CcCourseService ccCourseService = SpringContextHolder.getBean(CcCourseService.class);
		Record record = ccCourseService.valiudateDelete(idsArray);
		if (!record.getBoolean("isSuccess")) {
			return renderFAIL("0256", response, header, record.getStr("reason"));
		}

		//判断是否存在课程目标
		List<CcIndication> ccIndications = CcIndication.dao.findByColumnIn("course_id", idsArray);
		if(!ccIndications.isEmpty()){
			return renderFAIL("1110", response, header);
		}
		
		// 判断是否还有教师开课课程表在使用
		List<CcTeacherCourse> ccTeacherCourses = CcTeacherCourse.dao.findFilteredByColumnIn("course_id", idsArray);
		if (!ccTeacherCourses.isEmpty()) {
			return renderFAIL("0724", response, header);
		}
		
		// 删除CcCourse
		if (!CcCourse.dao.deleteAll(idsArray, date)) {
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		// 删除课程指标点关联表
		if (!CcIndicationCourse.dao.deleteAllByColumn("course_id", idsArray, date)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		// 删除CcPlanTermCourse
		if(!CcPlanTermCourse.dao.deleteAllByColumn("course_id", idsArray, date)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		CcCourseOutlineService ccCourseOutlineService = SpringContextHolder.getBean(CcCourseOutlineService.class);
		// 删除CcCourseOutline
		if (!ccCourseOutlineService.deleteByCourseIds(idsArray, user, date)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}	
	
