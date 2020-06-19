package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.ApiPermission;
import com.gnet.model.admin.CcEnrollment;
import com.jfinal.kit.StrKit;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 检查接口code是否唯一
 * 
 * @author xzl
 * @Date 2018年1月9日15:08:22
 */
@Service("EM00925")
public class EM00925 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		String code =  paramsStringFilter(params.get("code"));
		Long id = paramsLongFilter(params.get("id"));

		if(StrKit.isBlank(code)){
			return renderFAIL("1163", response, header);
		}

		// 结果返回
		Map<String, Object> result = new HashMap<>();
		
		result.put("isExisted", ApiPermission.dao.isRepeatCode(code, id));
		return renderSUC(result, response, header);
	}

}
