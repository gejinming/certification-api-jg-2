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
import com.gnet.model.admin.CcCourseModule;
import com.google.common.collect.Maps;

/**
 * 所属模块合计组分组接口
 * 
 * @author wct
 * @date 2016年8月8日
 */
@Transactional(readOnly = false)
@Service("EM00659")
public class EM00659 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long zoneId = paramsLongFilter(params.get("zoneId"));
		Long sumGroupId = paramsLongFilter(params.get("sumGroupId"));
		// 课程区域分区编号不能为空
		if (zoneId == null) {
			return renderFAIL("0663", response, header);
		}
		// 合计组编号
		if (sumGroupId == null) {
			return renderFAIL("0668", response, header);
		}
		// 课程区域
		CcCourseModule ccCourseModule = CcCourseModule.dao.findByZoneId(zoneId);
		// 区域所属模块不存在
		if (ccCourseModule == null) {
			return renderFAIL("0667", response, header);
		}
		
		ccCourseModule.set("sum_group_id", sumGroupId);
		ccCourseModule.set("modify_date", new Date());
		boolean isSuccess = ccCourseModule.update();
		
		// 返回结果
		Map<String, Object> result = Maps.newHashMap();
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}

}
