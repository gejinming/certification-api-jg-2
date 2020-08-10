package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseProperty;
import com.jfinal.kit.StrKit;

/**
 * 检查课程性质名称是否唯一
 * 
 * @author SY
 * @Date 2016年7月13日19:46:00
 */
@Service("EM00065")
public class EM00065 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		Long planId = paramsLongFilter(params.get("planId"));
		String propertyName = paramsStringFilter(params.get("propertyName"));
		String originValue = paramsStringFilter(params.get("originValue"));
		
		// planId不能为空信息的过滤
		if (planId == null) {
			return renderFAIL("0140", response, header);
		}
		// name不能为空信息的过滤
		if (StrKit.isBlank(propertyName)) {
			return renderFAIL("0111", response, header);
		}
		
		// 结果返回
		Map<String, Boolean> result = new HashMap<>();
		result.put("isExisted", CcCourseProperty.dao.isExisted(propertyName, originValue, planId));
		return renderSUC(result, response, header);
	}

}
