package com.gnet.certification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.Role;
import com.gnet.model.admin.RolePermission;
import com.jfinal.kit.StrKit;

/**
 * 查看角色详细
 * 
 * @author sll
 * 
 * @date 2016年6月7日08:56:43
 */
@Service("EM00017")
@Transactional(readOnly=true)
public class EM00017 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		
		if (id == null) {
			return renderFAIL("0020", response, header);
		}
		
		Role temp = Role.dao.findById(id);
		RolePermission rolePermission = RolePermission.dao.findFirstByColumn("role_id", id);
		
		Role role = new Role();
		role.put("id", temp.getLong("id"));
		role.put("createDate", temp.getDate("create_date"));
		role.put("modifyDate", temp.getDate("modify_date"));
		role.put("description", temp.getStr("description"));
		role.put("isSystem", temp.getBoolean("is_system"));
		String authorizationScope = temp.getStr("authorization_scope");
		role.put("authorizationScope", authorizationScope == null ? null : authorizationScope.split(","));
		role.put("name", temp.getStr("name"));
		
		if (rolePermission != null) {
			List<String> permissionIds = new ArrayList<String>();
			String permissions = rolePermission.getStr("permissions");
			if (StrKit.notBlank(permissions)) {
				for (String permission : permissions.split(",")) {
					permissionIds.add(permission.trim());
				}
			}
			role.put("permissionIds", permissionIds);
		}
		
		return renderSUC(role, response, header);
	}

}
