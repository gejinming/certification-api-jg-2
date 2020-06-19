package com.gnet.certification;

import java.util.Map;

import org.springframework.stereotype.Service;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.User;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;

/**
 * 检查用户名是否唯一
 * 
 * @author wct
 * @Date 2016年6月16日
 */
@Service("EM00033")
public class EM00033 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		String loginName = paramsStringFilter(params.get("loginName"));
		String originValue = paramsStringFilter(params.get("originValue"));
		// 用户名不能为空过滤
		if (StrKit.isBlank(loginName)) {
			return renderFAIL("0010", response, header);
		}
		
		// 结果返回
		Map<String, Object> result = Maps.newHashMap();
		result.put("isExisted", User.dao.isExisted(loginName, originValue));
		return renderSUC(result, response, header);
	}
	
}
