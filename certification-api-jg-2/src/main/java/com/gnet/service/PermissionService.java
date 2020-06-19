package com.gnet.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Component;

import com.gnet.model.admin.Permission;
import com.gnet.model.admin.RolePermission;
import com.gnet.model.admin.UserRole;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jfinal.kit.StrKit;

/**
 * @author sll
 * 
 * @date 2016年7月28日22:38:56
 *
 */
@Component("permissionService")
public class PermissionService {

	/**
	 * 根据用户编号查找该用户的所有功能权限
	 * 
	 * @param userId
	 * @return
	 */
	public List<Permission> findByUserId(Long userId){
		
		UserRole userRole = UserRole.dao.findById(userId);
		List<Permission> permissions = Lists.newArrayList();
		
		if (userRole != null && StrKit.notBlank(userRole.getStr("roles"))) {
			List<RolePermission> rolePermissions = RolePermission.dao.findByColumnIn("role_id", CollectionKit.convert(userRole.getStr("roles"), ","));
			
			HashSet<String> permissionCodes = Sets.newHashSet();
			for (RolePermission rolePermission: rolePermissions) {
				if (StrKit.isBlank(rolePermission.getStr("permissions"))) {
					continue;
				}
				
				//得到一个角色的所有权限
				String[] rolePermissionCodes = CollectionKit.convert(rolePermission.getStr("permissions"), ",");
				for (String rolePermissionCode : rolePermissionCodes) {
					if (!permissionCodes.contains(rolePermissionCode)) {
						permissionCodes.add(rolePermissionCode);
					}
				}
			}
			
			if(!permissionCodes.isEmpty()){
				permissions = Permission.dao.findByColumnIn("code", permissionCodes.toArray(new String[permissionCodes.size()]));
			}
			
		}
		
		return permissions;
	}	

}
