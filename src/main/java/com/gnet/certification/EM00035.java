package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.Permission;
import com.jfinal.kit.StrKit;

/**
 * 权限代码是否存在过滤
 * 
 * @author xzl
 * @Date 2016年8月15日
 */
@Service("EM00035")
public class EM00035 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		String code = paramsStringFilter(params.get("code"));
		String originValue = paramsStringFilter(params.get("originValue"));
		
		// 编码为空过滤
		if (StrKit.isBlank(code)) {
			return renderFAIL("0027", response, header);
		}
		
		//结果返回
		Map<String, Object> result = new HashMap<>();
		result.put("isExisted", Permission.dao.isExistedCode(code, originValue));
		return renderSUC(result, response, header);
	}
	
}
