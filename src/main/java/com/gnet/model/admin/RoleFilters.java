package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.jfinal.plugin.activerecord.Db;

/**
 * 数据权限 Model 层
 * 
 * @type model
 * @description 数据权限model，可批量删除数据权限
 * @table sys_role_filters
 * @author xuq
 * @date 2014-12-10 18:06:59:770
 * @version 1.0
 */
@TableBind(tableName = "sys_role_filters", pkName = "role_id")
public class RoleFilters extends DbModel<RoleFilters> {

	private static final long serialVersionUID = 1L;

	public static final RoleFilters dao = new RoleFilters();

	/**
	 * @description 根据权限编号批量删除数据权限
	 * @sql delete from sys_role_filters where id in (34,45,...)
	 * @version 1.0 通过编号删除数据权限
	 * @param ids
	 *            数据权限编号
	 */
	public void deleteAll(String[] ids) {
		// 进行批量删除操作
		Db.update("delete from " + tableName + " where id in (" + CollectionKit.convert(ids, ",", false) + ")");
	}

}
