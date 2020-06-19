package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSONArray;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.Role;
import com.gnet.model.admin.RolePermission;
import com.gnet.utils.CollectionKit;

/**
 * 
 * 编辑角色
 * 
 * @author sll
 *
 */
@Service("EM00010")
@Transactional(readOnly=false)
public class EM00010  extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Long id = paramsLongFilter(param.get("id"));
		String name = paramsStringFilter(param.get("name"));
		String description = paramsStringFilter(param.get("description"));
		JSONArray permissionIds = paramsJSONArrayFilter(param.get("permissionIds"));
		JSONArray authorizationScope = paramsJSONArrayFilter(param.get("authorizationScope"));
		if (id == null) {
			return renderFAIL("0020", response, header);
		}
		
		Role role = new Role();
		role.set("description", description);
		role.put("permissionIds", permissionIds);
		role.set("id", id);
		role.set("name", name);
		role.set("modify_date", new Date());
		role.put("authorizationScope", authorizationScope);
		
		Boolean isSuccess = update(role);
		
		Map<String, Boolean> result = new HashMap<>();
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}
	
	/**
	 * 更新角色
	 * 
	 * @param role
	 * @return
	 */
	private Boolean update(Role role){

		JSONArray permissionIds = role.get("permissionIds");
		String[] permissionIdsArray = {};
		
		if (permissionIds.size() > 0) {
			permissionIdsArray = new String[permissionIds.size()];   
	        for (int i=0; i<permissionIds.size(); i++) {   
	        	permissionIdsArray[i] = permissionIds.getString(i);   
	        }
		}
		
		// 处理authorizationScope
		JSONArray authorizationScope =  role.get("authorizationScope");
		String authorizationScopeStr = "";
		for(int i = 0; i < authorizationScope.size(); i++ ) {
			authorizationScopeStr = authorizationScopeStr + authorizationScope.getString(i) + ",";
		}
		authorizationScopeStr = authorizationScopeStr.length() > 1 ? authorizationScopeStr.substring(0, authorizationScopeStr.length() - 1) : null;
		role.set("authorization_scope", authorizationScopeStr);
		
		if (!role.update()) {
			return false;
		}

		// 更新角色信息
		if (role.get("permissionIds") == null) {
			return true;
		}
		RolePermission rolePermission = RolePermission.dao.findById(role.getLong("id"));
		Boolean result;
		if (rolePermission != null) {
			// 已存在，则进行角色权限
			rolePermission.set("permissions", CollectionKit.convert(permissionIdsArray, ",", false));
			result = rolePermission.update();
		} else {
			// 不存在，新增该角色权限关联
			rolePermission = new RolePermission();
			rolePermission.set("role_id", role.getLong("id"));
			rolePermission.set("permissions", CollectionKit.convert(permissionIdsArray, ",", false));
			result = rolePermission.save();
		}
		
		if (!result) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		} else {
			return true;
		}
	}
}
