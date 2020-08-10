package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcVersion;
import com.jfinal.kit.StrKit;

/**
 * 同一专业下版本名称唯一性验证的接口
 * 
 * @author sll
 *
 */
@Service("EM00099")
public class EM00099 extends BaseApi implements IApi {
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		Long majorId = paramsLongFilter(params.get("majorId"));
		String name = paramsStringFilter(params.get("name"));
		String originValue = paramsStringFilter(params.get("originValue"));
		
		// 版本名称不能为空
		if (StrKit.isBlank(name)) {
			return renderFAIL("0147", response, header);
		}
		
		// 结果返回
		Map<String, Boolean> result = new HashMap<>();
		result.put("isExisted", CcVersion.dao.isExisted(name, majorId, originValue));
		return renderSUC(result, response, header);
	}

}
