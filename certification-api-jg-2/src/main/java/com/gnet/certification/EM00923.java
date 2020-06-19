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
 * 编辑接口
 * 
 * @author xzl
 * 
 * @date 2018年1月9日15:02:32
 *
 */
@Service("EM00923")
@Transactional(readOnly=false)
public class EM00923 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		String name = paramsStringFilter(param.get("name"));
		String code = paramsStringFilter(param.get("code"));
		String description = paramsStringFilter(param.get("description"));
		
		if (id == null) {
			return renderFAIL("1160", response, header);
		}

		if(StrKit.isBlank(name)){
			return renderFAIL("1162", response, header);
		}

		if(StrKit.isBlank(code)){
			return renderFAIL("1163", response, header);
		}

		if(ApiPermission.dao.isRepeatCode(code, id)){
			return renderFAIL("1164", response, header);
		}

		ApiPermission apiPermission = ApiPermission.dao.findById(id);
		if(apiPermission == null){
			renderFAIL("1161", response, header);
		}


		apiPermission.set("modify_date", new Date());
		apiPermission.set("code", code);
		apiPermission.set("name", name);
		apiPermission.set("description", description);

		Map<String, Boolean> result = new HashMap<>();
		result.put("isSuccess", apiPermission.update());
		return renderSUC(result, response, header);
	}
	
}
