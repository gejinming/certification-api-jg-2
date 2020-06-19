package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.PermissionHaveApi;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.Constant;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.Permission;
import com.jfinal.kit.StrKit;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * 增加权限
 * 
 * @author sll
 * 
 * @date 2016年6月6日15:16:19
 *
 */
@Service("EM00013")
@Transactional(readOnly=false)
public class EM00013 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		String code = paramsStringFilter(param.get("code"));
		String name = paramsStringFilter(param.get("name"));
		String pname = paramsStringFilter(param.get("pname"));
		String description = paramsStringFilter(param.get("description"));
		List<Long> interfaceIds = paramsJSONArrayFilter(param.get("interfaceIds"), Long.class);

		if (StrKit.isBlank(code)) {
			return renderFAIL("0023", response, header);
		}
		if (StrKit.isBlank(name)) {
			return renderFAIL("0024", response, header);
		}
		if (StrKit.isBlank(pname)) {
			return renderFAIL("0025", response, header);
		}
		if (Permission.dao.isExistedCode(code)) {
			return renderFAIL("0022", response, header);
		}
		if (Permission.dao.isExistedName(name, pname)){
			return renderFAIL("0028", response, header);
		}
		
		Permission permission = new Permission();
		Date date = new Date();
		permission.set("create_date", date);
		permission.set("modify_date", date);
		permission.set("code", code);
		permission.set("name", name);
		permission.set("pname", pname);
		permission.set("is_system", Constant.NOTSYSTEM);
		if (StrKit.notBlank(description)) {
			permission.set("description", description);
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		if(!permission.save()){
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		List<PermissionHaveApi> permissionHaveApis = Lists.newArrayList();
        if(!interfaceIds.isEmpty()){
        	for(Long id : interfaceIds){
				PermissionHaveApi permissionHaveApi = new PermissionHaveApi();
				permissionHaveApi.set("create_date", date);
				permissionHaveApi.set("modify_date", date);
				permissionHaveApi.set("api_permission_id", id);
				permissionHaveApi.set("permission_id", permission.getLong("id"));
				permissionHaveApis.add(permissionHaveApi);
			}
			if(!PermissionHaveApi.dao.batchSave(permissionHaveApis)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}

		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}
	
}
