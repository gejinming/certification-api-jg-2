package com.gnet.model.admin;


import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;

import java.util.List;

@TableBind(tableName = "sys_permission_have_api")
public class PermissionHaveApi extends DbModel<PermissionHaveApi> {

	private static final long serialVersionUID = -8766825548254858818L;

	public final static PermissionHaveApi dao = new PermissionHaveApi();

	/**
	 * 权限和接口是否已存在关联
	 * @param permissionId
	 * @param apiPermissionId
	 */
	public boolean isExist(Long permissionId, Long apiPermissionId) {
		if(permissionId != null){
			return Db.queryLong("select count(1) from " + tableName + " where api_permission_id = ? and permission_id = ? ", apiPermissionId, permissionId) > 0;
		}else{
			return Db.queryLong("select count(1) from " + tableName + " where api_permission_id = ? ", apiPermissionId) > 0;
		}
	}

	/**
	 * 删除权限和接口的关联
	 * @param permissionId
	 */
	public boolean delete(Long permissionId) {
		return Db.update("delete from " + tableName + " where permission_id = ? ", permissionId) >= 0;
	}

	/**
	 * 权限下已关联的接口
	 * @param permissionId
	 * @return
	 */
	public List<PermissionHaveApi> findByPermissionId(Long permissionId) {
		StringBuilder sql = new StringBuilder("select ap.* from " + tableName + " pha ");
		sql.append("inner join " + ApiPermission.dao.tableName + " ap on ap.id = pha.api_permission_id ");
		sql.append("where pha.permission_id = ? ");
		sql.append("order by ap.code ");
		return find(sql.toString(), permissionId);
	}
}
