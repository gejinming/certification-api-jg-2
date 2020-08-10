package com.gnet.model.admin;

import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;

import jodd.util.ArraysUtil;

/**
 * @type model
 * @description 用户和角色的关联model，可批量保存、批量删除用户和角色的关系
 * @table sys_user_role
 * @author Administrator
 * @version 1.0
 *
 */
@TableBind(pkName = "user_id", tableName = "sys_user_role")
public class UserRole extends DbModel<UserRole> {

	private static final long serialVersionUID = 4251096985293308102L;

	public static final UserRole dao = new UserRole();

	/**
	 * 批量保存user和role的关系
	 * 
	 * @description 批量保存用户和角色的联系
	 * @sql insert into sys_user_role (user_id, roles) values(?,?)
	 * @version 1.0
	 * @author SY
	 * @param userRoleList
	 *            一个user和role的联系的list
	 * @return
	 */
	public Boolean batchAddUserRole(List<UserRole> userRoleList) {
		Object paras[][] = new Object[userRoleList.size()][];
		for (int i = 0; i < userRoleList.size(); i++) {
			paras[i] = new Object[] { userRoleList.get(i).getLong("user_id"), userRoleList.get(i).getLong("roles") };
		}
		return !ArraysUtil.contains(Db.batch("insert into " + UserRole.dao.tableName + " (user_id, roles) values(?,?)", paras), 0);
	}

	/**
	 * 是否存在此角色
	 * 
	 * @description 根据角色编号查找是否存在该角色
	 * @sql select count(1) from sys_user_role where roles like '46'
	 * @version 1.0
	 * @param role_id
	 * @return
	 */
	public boolean hasRole(String role_id) {
		long count = Db.queryLong("select count(1) from " + UserRole.dao.tableName + " where roles like '" + role_id + "'");
		return count > 0;
	}
	
	/**
	 * 是否存在此角色
	 * @param role_id
	 * @return
	 */
	public boolean hasRole(Long role_id) {
		long count = Db.queryLong("select count(1) from " + UserRole.dao.tableName + " where roles like '" + role_id + "'");
		return count > 0;
	}

	/**
	 * 是否存在这些角色中任何一个
	 * 
	 * @description 判断是否存在这些角色中任何一个
	 * @sql select count(1) from sys_user_role where roles like '46'
	 * @version 1.0
	 * @param role_ids
	 * @return
	 */
	public boolean hasRoles(String[] role_ids) {
		for (String role_id : role_ids) {
			if (hasRole(role_id)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否存在这些角色中任何一个
	 * @param role_ids
	 * @return
	 */
	public boolean hasRoles(Long[] role_ids) {
		for (Long role_id : role_ids) {
			if (hasRole(role_id)) {
				return true;
			}
		}
		return false;
	}

}
