package com.gnet.certification;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcPlanCourseZone;
import com.google.common.collect.Maps;

/**
 * 控制合计信息和至少信息显示接口
 * 
 * @author wct
 * @date 2016年8月8日
 */
@Transactional(readOnly = false)
@Service("EM00658")
public class EM00658 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long zoneId = paramsLongFilter(params.get("zoneId"));
		Boolean isShow = paramsBooleanFilter(params.get("isShow"));
		Integer itemType = paramsIntegerFilter(params.get("itemType"));
		
		// 课程区域编号不能为空
		if (zoneId == null) {
			return renderFAIL("0663", response, header);
		}
		// 是否显示信息不能为空
		if (isShow == null) {
			return renderFAIL("0665", response, header);
		}
		
		CcPlanCourseZone ccPlanCourseZone = CcPlanCourseZone.dao.findFilteredById(zoneId);
		// 课程区域不存在过滤
		if (ccPlanCourseZone == null) {
			return renderFAIL("0664", response, header);
		}
		
		if (CcPlanCourseZone.ITEM_SUMINFO.equals(itemType)) {
			ccPlanCourseZone.set("show_sum_score", isShow);
		} else if (CcPlanCourseZone.ITEM_LEASTINFO.equals(itemType)) {
			ccPlanCourseZone.set("show_least_score", isShow);
		} else {
			return renderFAIL("0666", response, header);
		}
		
		ccPlanCourseZone.set("modify_date", new Date());
		boolean isSuccess = ccPlanCourseZone.update();
		
		// 返回结果
		Map<String, Object> result = Maps.newHashMap();
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}

}
