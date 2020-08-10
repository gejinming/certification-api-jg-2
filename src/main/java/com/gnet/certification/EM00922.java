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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 增加接口
 * 
 * @author xzl
 * 
 * @date 2018年1月9日14:37:04
 *
 */
@Service("EM00922")
@Transactional(readOnly=false)
public class EM00922 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
        String name = paramsStringFilter(param.get("name"));
		String code = paramsStringFilter(param.get("code"));
		String description = paramsStringFilter(param.get("description"));

        if(StrKit.isBlank(name)){
        	return renderFAIL("1162", response, header);
		}

		if(StrKit.isBlank(code)){
			return renderFAIL("1163", response, header);
		}

		if(ApiPermission.dao.isRepeatCode(code, null)){
           return renderFAIL("1164", response, header);
		}

		
		Date date = new Date();
        ApiPermission apiPermission = new ApiPermission();
        apiPermission.set("create_date", date);
        apiPermission.set("modify_date", date);
        apiPermission.set("code", code);
        apiPermission.set("name", name);
        apiPermission.set("description", description);
        apiPermission.set("is_system", true);
		
		Map<String, Object> result = new HashMap<>();
		result.put("isSuccess", apiPermission.save());

		return renderSUC(result, response, header);
	}
}
