package com.gnet.certification;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.PermissionHaveApi;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.Permission;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * 权限的编辑
 * 
 * @author sll
 * 
 * @date 2016年6月5日18:04:21
 *
 */
@Service("EM00014")
@Transactional(readOnly=false)
public class EM00014 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		String name = paramsStringFilter(param.get("name"));
		String pname = paramsStringFilter(param.get("pname"));
		String description = paramsStringFilter(param.get("description"));
		List<Long> interfaceIds = paramsJSONArrayFilter(param.get("interfaceIds"), Long.class);
		
		if (id == null) {
			return renderFAIL("0027", response, header);
		}
		if(StrKit.isBlank(pname)){
			return renderFAIL("0029", response, header);
		}
		if(StrKit.isBlank(name)){
			return renderFAIL("0024", response, header);
		}
		
		Permission permission = Permission.dao.findById(id);
		if(permission == null){
			return renderFAIL("0030", response, header);
		}

		if (Permission.dao.isExistedName(name, permission.getStr("name"), pname)){
			return renderFAIL("0028", response, header);
		}
		
		permission.set("name", name);
		permission.set("pname", pname);
		permission.set("description", description);
		permission.set("id", id);
		permission.set("modify_date", new Date());
		
		Map<String, Object> result = Maps.newHashMap();
		if(!permission.update()){
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		//先删除之前已经存在的
        if(!PermissionHaveApi.dao.delete(id)){
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		Date date = new Date();
		List<PermissionHaveApi> permissionHaveApis = Lists.newArrayList();
		if(!interfaceIds.isEmpty()){
			for(Long interfaceId : interfaceIds){
				PermissionHaveApi permissionHaveApi = new PermissionHaveApi();
				permissionHaveApi.set("create_date", date);
				permissionHaveApi.set("modify_date", date);
				permissionHaveApi.set("api_permission_id", interfaceId);
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
