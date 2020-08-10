package com.gnet.certification;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;

/**
 * 用户注销
 * @author wct
 * @Date 2016年6月4日
 */
@Service("EM00002")
public class EM00002 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = Maps.newHashMap();
		String token = paramsStringFilter(params.get("token"));
		// Token不能为空过滤
		if (StrKit.isBlank(token)) {
			return renderFAIL("0051", response, header);
		}
		
		UserCacheKit.logout(token);
		
		return renderSUC(null, response, header);
	}

}
