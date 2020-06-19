package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseHierarchy;
import com.jfinal.kit.StrKit;

/**
 * 检查课程层次名称是否唯一
 * 
 * @author xzl
 * @Date 2016年7月6日
 */
@Service("EM00055")
public class EM00055 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		Long planId = paramsLongFilter(params.get("planId"));
		String name = paramsStringFilter(params.get("name"));
		String originValue = paramsStringFilter(params.get("originValue"));
		
		// 课程层次名称不能为空
		if (StrKit.isBlank(name)) {
			return renderFAIL("0102", response, header);
		}
		// planId不能为空信息的过滤
		if (planId == null) {
			return renderFAIL("0140", response, header);
		}
		
		// 结果返回
		Map<String, Boolean> result = new HashMap<>();
		result.put("isExisted", CcCourseHierarchy.dao.isExisted(name, originValue, planId));
		return renderSUC(result, response, header);
	}

}
