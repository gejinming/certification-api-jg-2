package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcMajorDirection;
import com.jfinal.kit.StrKit;

/**
 * 检查专业方向名称是否唯一
 * 
 * @author xzl
 * @Date 2016年7月6日
 */
@Service("EM00195")
public class EM00195 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		String name = paramsStringFilter(params.get("name"));
		Long planId = paramsLongFilter(params.get("planId"));
		String originValue = paramsStringFilter(params.get("originValue"));
		
		// 专业方向名称不能为空
		if (StrKit.isBlank(name)) {
			return renderFAIL("0282", response, header);
		}
		// 培养计划编号不能为空
		if (planId == null) {
			return renderFAIL("0421", response, header);
		}
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		result.put("isExisted", CcMajorDirection.dao.isExisted(name, planId, originValue));
		return renderSUC(result, response, header);
	}

}
