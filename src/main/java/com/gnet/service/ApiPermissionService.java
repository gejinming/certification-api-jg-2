package com.gnet.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.gnet.api.Request;
import com.gnet.api.kit.UserCacheKit;

/**
 * api权限service
 * 
 * @author sll
 *
 */
@Component("apiPermissionService")
public class ApiPermissionService {

	/**
	 * 检验当前用户是否有API权限
	 * 
	 * @param service
	 * @param r
	 * @return
	 */
	public Boolean hasApiPermission(String permission, String token){

		if (permission == null || token == null) {
			return false;
		}
		List<String> apiPermissionCodes = UserCacheKit.getApiPermissions(token);
		for (String apiPermissionCode: apiPermissionCodes) {
			if (apiPermissionCode.equals(permission)) {
				//有访问权限
				return true;
			}
		}
		return false;
	}
	
}
