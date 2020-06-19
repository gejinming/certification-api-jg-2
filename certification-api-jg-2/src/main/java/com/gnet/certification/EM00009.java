package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSONArray;
import com.gnet.Constant;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.Role;
import com.gnet.model.admin.RolePermission;
import com.gnet.utils.CollectionKit;
import com.jfinal.kit.StrKit;

/**
 * 增加角色
 * 
 * @author sll
 * 
 * @date 2016年6月6日15:13:14
 *
 */
@Service("EM00009")
@Transactional(readOnly=false)
public class EM00009 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		String name = paramsStringFilter(param.get("name"));
		String description = paramsStringFilter(param.get("description"));
		JSONArray permissionIds = paramsJSONArrayFilter(param.get("permissionIds"));
		JSONArray authorizationScope = paramsJSONArrayFilter(param.get("authorizationScope"));
		
		if (StrKit.isBlank(name)) {
			return renderFAIL("0019", response, header);
		}
		
		Date date = new Date();
		Role role = new Role();
		role.set("create_date", date);
		role.set("modify_date", date);
		role.set("name", name);
		role.set("description", description);
		role.put("permissionIds", permissionIds);
		role.set("is_system", Constant.NOTSYSTEM);
		role.put("authorizationScope", authorizationScope);
		
		Boolean isSuccess = save(role);
		Map<String,Object> result = new HashMap<>();
		result.put("isSuccess", isSuccess);
		
		return renderSUC(result, response, header);
	}
	
	/**
	 * 保存角色
	 * 
	 * @param role
	 * @return
	 */
	private Boolean save(Role role){
		
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
				
		if (!role.save()) {
			return false;
		}

		// 角色权限关联保存
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
