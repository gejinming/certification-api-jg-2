package com.gnet.certification;

import com.gnet.annotation.RequirePermission;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.ApiPermission;
import com.gnet.model.admin.CcVersion;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 查看接口某条信息
 * 
 * @author xzl
 * @Date 2018年1月9日14:27:15
 */
@Service("EM00921")
public class EM00921 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Long id = paramsLongFilter(param.get("id"));

		if(id == null){
			return renderFAIL("1160", response, header);
		}

		ApiPermission apiPermission = ApiPermission.dao.findById(id);
		if(apiPermission == null){
			return renderFAIL("1161", response, header);
		}

		// 结果返回
		Map<String, Object> result = new HashMap<>();
		result.put("id", apiPermission.get("id"));
		result.put("createDate", apiPermission.get("create_date"));
		result.put("modifyDate", apiPermission.get("modify_date"));
		result.put("code", apiPermission.get("code"));
		result.put("name", apiPermission.get("name"));
		result.put("description", apiPermission.get("description"));
		return renderSUC(result, response, header);
	}
	
}
