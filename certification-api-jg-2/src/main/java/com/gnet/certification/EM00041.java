package com.gnet.certification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.Role;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;

/**
 * 根据用户角色返回用户可赋予教师的角色列表
 * 
 * @author xzl
 * 
 * @date 2016年11月2日10:53:56
 *
 */
@Service("EM00041")
@Transactional(readOnly=true)
public class EM00041 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
				
		List<String> roles = UserCacheKit.getRoles(request.getHeader().getToken());
		Map<String, Object> rolesMap = Maps.newHashMap();
		List<Map<String, Object>> list = new ArrayList<>();
		if(roles.isEmpty()){
			return renderFAIL("0012", response, header);
		}
		List<Role> sysRoles = Role.dao.findByColumnIn("id", roles.toArray(new String[roles.size()])); 
		if(sysRoles.isEmpty()){
			return renderFAIL("0770", response, header);
		}
		String key = "";
		for(Role role : sysRoles){
			String authorizationScope = role.getStr("authorization_scope");
			key = ( authorizationScope == null ? key : authorizationScope + "," + key );
		}
		
		if(StrKit.isBlank(key)){
			rolesMap.put("list", list);
			return renderSUC(rolesMap, response, header);
		}
		
		String[] roleIds = key.split(",");
		if (roleIds != null && roleIds.length > 0) {
			List<Role> roleList = Role.dao.findByColumnIn("id", roleIds); 
			for (Role temp: roleList) {
				Map<String, Object> role = Maps.newHashMap();
				role.put("id", temp.get("id"));
				role.put("name", temp.get("name"));
				role.put("isSystem", temp.get("is_system"));
				role.put("description", temp.get("description"));
				role.put("createDate", temp.get("create_date"));
				role.put("modifyDate", temp.get("modify_date"));
				role.put("authorizationScope", temp.getStr("authorization_scope"));
				list.add(role);
			}
		}
		
		rolesMap.put("list", list);
		return renderSUC(rolesMap, response, header);
	}
	
}
