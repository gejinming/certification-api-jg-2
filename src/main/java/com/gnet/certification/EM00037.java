package com.gnet.certification;

import java.util.Map;

import org.springframework.stereotype.Service;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.Permission;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;

/**
 * 同一权限分类下的权限名称是否唯一
 * 
 * @author xzl
 * @Date 2016年8月16日
 */
@Service("EM00037")
public class EM00037 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		String name = paramsStringFilter(params.get("name"));
		String originValue = paramsStringFilter(params.get("originValue"));
		String pname = paramsStringFilter(params.get("pname"));
		
		// 名称为空过滤
		if (StrKit.isBlank(name)) {
			return renderFAIL("0024", response, header);
		}
		//权限分类名是否为空过滤
	    if (StrKit.isBlank(pname)){
	    	return renderFAIL("0025", response, header);
	    }
		
		//结果返回
	    Map<String, Object> result = Maps.newHashMap();
		result.put("isExisted", Permission.dao.isExistedName(name, originValue, pname));
		return renderSUC(result, response, header);
	}
	
}
