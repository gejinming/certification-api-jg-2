package com.gnet.certification;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.CcCourseHierarchySecondary;
import org.springframework.stereotype.Service;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourse;
import com.gnet.model.admin.CcCourseHierarchy;
import com.gnet.model.admin.CcPlanCourseZone;
import com.google.common.collect.Maps;

/**
 * 删除次要课程层次某条信息
 * 
 * @author SY
 * @Date 2019年12月9日13:51:55
 */
@Service("EM01124")
public class EM01124 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		// 获取数据
		Date date = new Date();
		
		List<Long> ids = paramsJSONArrayFilter(params.get("ids"), Long.class);
		// majorId不能为空信息的过滤
		if (ids == null) {
			return renderFAIL("0100", response, header);
		}
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		// 判断是否还有课程表在使用
		List<CcCourse> ccCourses = CcCourse.dao.findFilteredByColumnIn("hierarchy_secondary_id", idsArray);
		if(!ccCourses.isEmpty()) {
			return renderFAIL("0708", response, header);
		}
		// 判断是否还有培养计划课程分区表在使用
		List<CcPlanCourseZone> ccPlanCourseZones = CcPlanCourseZone.dao.findFilteredByColumnIn("zone_id", idsArray);
		if(!ccPlanCourseZones.isEmpty()) {
			return renderFAIL("0710", response, header);
		}
		
		Map<String, Object> result = Maps.newHashMap();
		// 返回操作是否成功
		result.put("isSuccess", CcCourseHierarchySecondary.dao.deleteAll(idsArray, date));
		return renderSUC(result, response, header);
	}
	
}
