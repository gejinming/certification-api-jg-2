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
import com.gnet.model.admin.CcMajorDirection;
import com.gnet.model.admin.CcMajorStudent;
import com.gnet.model.admin.CcPlanCourseZone;
import com.gnet.model.admin.CcReportMajor;

/**
 * 批量删除专业方向
 * 
 * @author sll
 * 
 * @date 2016年06月28日 17:57:45
 *
 */
@Service("EM00194")
@Transactional(readOnly=false)
public class EM00194 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		
		Date date = new Date();
		
		List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		Map<String, Object> result = new HashMap<>();
		
		// 判断是否还有课程表在使用
 		List<CcCourse> ccCourses = CcCourse.dao.findFilteredByColumnIn("direction_id", idsArray);
 		if(!ccCourses.isEmpty()) {
 			return renderFAIL("0285", response, header);
 		}
 		
 		// 判断是否还有培养计划课程分区表在使用
 		List<CcPlanCourseZone> ccPlanCourseZones = CcPlanCourseZone.dao.findFilteredByColumnIn("zone_id", idsArray);
 		if(!ccPlanCourseZones.isEmpty()) {
 			return renderFAIL("0286", response, header);
 		}
 		
 		// 判断是否还有专业认证学生表在使用
 		List<CcMajorStudent> ccMajorStudents = CcMajorStudent.dao.findByColumnIn("major_direction_id", idsArray);
 		if(!ccMajorStudents.isEmpty()) {
 			return renderFAIL("0287", response, header);
 		}
 		
 		// 判断是否还有指标点报表数据统计表在使用
 		List<CcReportMajor> ccReportMajors = CcReportMajor.dao.findFilteredByColumnIn("major_direction_id", idsArray);
 		if(!ccReportMajors.isEmpty()) {
 			return renderFAIL("0288", response, header);
 		}
		
		// 删除CcMajorDirection
		if(!CcMajorDirection.dao.deleteAll(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}	
	
