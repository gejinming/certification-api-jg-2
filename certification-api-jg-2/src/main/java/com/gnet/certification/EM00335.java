package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseModule;
import com.jfinal.kit.StrKit;

/**
 * 检查模块名称是否唯一
 * 
 * @author xzl
 * @Date 2016年7月4日
 */
@Service("EM00335")
public class EM00335 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		String moduleName = paramsStringFilter(params.get("moduleName"));
		Long planId = paramsLongFilter(params.get("planId"));
		String originValue = paramsStringFilter(params.get("originValue"));
		
		// 模块名称不能为空
		if (StrKit.isBlank(moduleName)) {
			return renderFAIL("0400", response, header);
		}
		if (planId == null) {
			return renderFAIL("0421", response, header);
		}
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		result.put("isExisted", CcCourseModule.dao.isExisted(moduleName, planId, originValue));
		return renderSUC(result, response, header);
	}

}
