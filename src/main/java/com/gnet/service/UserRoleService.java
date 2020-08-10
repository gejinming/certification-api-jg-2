package com.gnet.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.gnet.model.admin.User;
import com.gnet.model.admin.UserRole;
import com.gnet.utils.CollectionKit;

/**
 * @author SY
 * @date 2016年6月25日19:09:13
 */
@Component("userRoleService")
public class UserRoleService {
	

	/**
	 * 通过传递过来的user数据进行保存
	 * @param user
	 */
	public boolean saveByUser(User user) {
		Long userId = user.getLong("id");
		List<Long> roleIds = user.get("roleIds");
		
		//角色关联保存
		UserRole userRole = UserRole.dao.findById(userId);
		Boolean result;
		if (userRole != null) {
			// 已经存在角色列表，进行角色的更新操作
			userRole.set("roles", CollectionKit.convert(roleIds, ","));
			result =  userRole.update();
		} else {
			// 不存在，新增该角色权限关联
			userRole = new UserRole();
			userRole.set("user_id", userId);
			userRole.set("roles", CollectionKit.convert(roleIds, ","));
			result =  userRole.save();
		}
		
		return result;
	}

}
