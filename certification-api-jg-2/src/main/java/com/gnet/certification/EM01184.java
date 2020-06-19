package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourse;
import com.gnet.model.admin.CcCourseProperty;
import com.gnet.model.admin.CcCourseType;
import com.gnet.model.admin.CcPlanCourseZone;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 删除课程类别某条信息
 * 
 * @author gjm
 * @Date 2016年6月20日14:09:05
 */
@Service("EM01184")
public class EM01184 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		// 获取数据
		Date date = new Date();
		List<Long> ids = paramsJSONArrayFilter(params.get("ids"), Long.class);
		// ids不能为空信息的过滤
		if (ids == null || ids.size()==0) {
			return renderFAIL("2500", response, header);
		}
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		// 判断是否还有课程表在使用
		List<CcCourse> ccCourses = CcCourse.dao.findFilteredByColumnIn("type_id", idsArray);
		if(!ccCourses.isEmpty()) {
			return renderFAIL("2505", response, header);
		}
		/*// 判断是否还有培养计划课程分区表在使用
		List<CcPlanCourseZone> ccPlanCourseZones = CcPlanCourseZone.dao.findFilteredByColumnIn("zone_id", idsArray);
		if(!ccPlanCourseZones.isEmpty()) {
			return renderFAIL("0709", response, header);
		}*/
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		// 返回操作是否成功
		Boolean isSuccess = CcCourseType.dao.deleteAll(idsArray, date);
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}
	
}
