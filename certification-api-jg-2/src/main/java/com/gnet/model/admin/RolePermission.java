package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.jfinal.plugin.activerecord.Db;

/**
 * 
 * @type model
 * @description 角色权限model，可批量删除角色，也可查询是否存在某些权限
 * @table sys_role_permission
 * @author Administrator
 * @version 1.0
 */
@TableBind(pkName = "role_id", tableName = "sys_role_permission")
public class RolePermission extends DbModel<RolePermission> {

	private static final long serialVersionUID = 5682716613921848015L;

	public static final RolePermission dao = new RolePermission();

	/**
	 * @description 根据角色编号批量删除角色
	 * @sql delete from sys_role_permission where role_id in (45,57,...)
	 * @version 1.0
	 * @param ids
	 */
	public void deleteAll(String[] ids) {
		// 进行批量删除操作
		Db.update("delete from " + tableName + " where role_id in (" + CollectionKit.convert(ids, ",", false) + ")");
	}

	/**
	 * 是否有此权限
	 * 
	 * @description 根据权限查找是否存在此权限
	 * @sql select count(1) from sys_role_permission where permissions like ?
	 * @version 1.0
	 * @param permission
	 * @return
	 */
	public boolean hasPermission(Long permission) {
		long count = Db.queryLong("select count(1) from " + tableName + " where permissions like '%" + permission + "'");
		return count > 0;
	}

	/**
	 * 是否有这些权限
	 * 
	 * @description 查询是否有这些权限
	 * @sql select count(1) from sys_role_permission where permissions like ?
	 * @version 1.0
	 * @param permissions
	 * @return
	 */
	public boolean hasPermissions(Long[] permissions) {
		for (Long permission : permissions) {
			if (hasPermission(permission)) {
				return true;
			}
		}
		return false;
	}
}
