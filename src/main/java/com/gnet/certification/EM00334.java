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
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourse;
import com.gnet.model.admin.CcCourseModule;
import com.gnet.model.admin.CcPlanCourseZone;

/**
 * 批量删除所属模块
 * 
 * @author xzl
 * 
 * @date 2016年7月4日
 *
 */
@Service("EM00334")
@Transactional(readOnly=false)
public class EM00334 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String,Boolean> result = new HashMap<>();
		
		Date date = new Date();
		
		List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		// 判断是否还有课程表在使用
 		List<CcCourse> ccCourses = CcCourse.dao.findFilteredByColumnIn("module_id", idsArray);
 		if(!ccCourses.isEmpty()) {
 			return renderFAIL("402", response, header);
 		}
 		
 		// 判断是否还有培养计划课程分区表在使用
 		List<CcPlanCourseZone> ccPlanCourseZones = CcPlanCourseZone.dao.findFilteredByColumnIn("zone_id", idsArray);
 		if(!ccPlanCourseZones.isEmpty()) {
 			return renderFAIL("403", response, header);
 		}

		// 删除CcCourseModule
		if(!CcCourseModule.dao.deleteAll(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}	
	
