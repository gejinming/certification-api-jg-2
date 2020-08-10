package com.gnet.model.admin;

import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

@TableBind(tableName = "sys_api_permission")
public class ApiPermission extends DbModel<ApiPermission> {

	private static final long serialVersionUID = 2854434931065580816L;

	public static final ApiPermission dao = new ApiPermission();

	/**
	 * 根据功能权限数据找到所有API权限
	 *
	 * @param permissionIds
	 * @return
	 */
	public List<ApiPermission> findApiPermissionByPermissionId (Long[] permissionIds){

		StringBuilder sql = new StringBuilder("select * from " + tableName + " sap ");
		sql.append("left join " + PermissionHaveApi.dao.tableName + " spha on sap.id = spha.api_permission_id ");
		sql.append("where spha.permission_id in ("+  CollectionKit.convert(permissionIds, ",") + ") ");

		return find(sql.toString() );
	}

	/**
	 * 接口列表
	 * @param pageable
	 * @param code
	 * @param name
	 * @return
	 */
	public Page<ApiPermission> page(Pageable pageable, String code, String name) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("from " + tableName + " where 1=1 ");
		// 条件删选
		if (!StrKit.isBlank(code)) {
			sql.append("and code like '" + StringEscapeUtils.escapeSql(code) + "%' ");
		}
		if (!StrKit.isBlank(name)) {
			sql.append("and name like '" + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			sql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}

		return ApiPermission.dao.paginate(pageable, "select *", sql.toString(), params.toArray());
	}

	/**
	 * 接口编码
	 * @param code
	 * @param id
	 * @return
	 */
	public Boolean isRepeatCode(String code, Long id) {
		if(id != null){
			return Db.queryLong("select count(1) from " + tableName + " where code = ? and id != ? ", code, id) > 0;
		}else{
			return  Db.queryLong("select count(1) from " + tableName + " where code= ? ", code) > 0;
		}
	}
}
