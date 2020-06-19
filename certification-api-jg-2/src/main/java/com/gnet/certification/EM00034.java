package com.gnet.certification;

import java.util.Map;

import org.springframework.stereotype.Service;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.Role;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;

/**
 * 角色名称是否存在接口
 * 
 * @author wct
 * @Date 2016年6月16日
 */
@Service("EM00034")
public class EM00034 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		String name = paramsStringFilter(params.get("name"));
		String originValue = paramsStringFilter(params.get("originValue"));
		
		// 角色名称为空过滤
		if (StrKit.isBlank(name)) {
			return renderFAIL("0019", response, header);
		}
		
		// 返回结果
		Map<String, Object> result = Maps.newHashMap();
		result.put("isExisted", Role.dao.isExisted(name, originValue));
		return renderSUC(result, response, header);
	}
}
