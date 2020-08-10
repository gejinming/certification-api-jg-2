package com.gnet.model.admin;


import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * 
 * @type model
 * @description 权限model
 * @table sys_permission
 * @author Administrator
 * @version 1.0
 */
@TableBind(tableName = "sys_permission")
public class Permission extends DbModel<Permission> {

	private static final long serialVersionUID = -5612351602182448445L;

	public static final Permission dao = new Permission();
	
	/**
	 * 是否存在此code
	 * 
	 * @description 根据code查找是否存在此code
	 * @sql select count(1) from sys_permission where code=?
	 * @version 1.0
	 * @param code
	 * @return
	 */
	public boolean isExistedCode(String code) {
		return this.isExistedCode(code, null);
	}

	/**
	 * 是否存在此code
	 * 
	 * @description 根据code查找是否存在此code
	 * @sql select count(1) from sys_permission where code=?
	 * @version 1.0
	 * @param code
	 * @return
	 */
	public boolean isExistedCode(String code, String originValue) {
		if (StrKit.notBlank(originValue)) {
			return Db.queryLong("select count(1) from " + tableName + " where code=? and code!=?", code, originValue) > 0;
		} else {
			return Db.queryLong("select count(1) from " + tableName + " where code=? ", code) > 0;
		}
	}

	
	/**
	 * 同一权限分类下名称是否重复
	 * @param name
	 * @param pname
	 * @return
	 */
	public boolean isExistedName(String name, String pname) {
		return isExistedName(name, null, pname);
	}

	/**
	 * 同一权限分类下名称是否重复
	 * @param name
	 * @param originValue
	 * @param pname
	 * @return
	 */
	public boolean isExistedName(String name, String originValue, String pname) {
		if(StrKit.notBlank(originValue)){
			return Db.queryLong("select count(1) from " + tableName + " where name = ? and name != ? and pname = ? ", name, originValue, pname) > 0;
		}else{
			return Db.queryLong("select count(1) from " + tableName + " where name = ? and pname = ? ", name, pname) > 0;
		}
	}
	
	/**
	 * 查看权限列表分页
	 * 
	 * @param pageable
	 * @param code
	 * @param name
	 * @param pname
	 * @return
	 */
	public Page<Permission> page(Pageable pageable, String code, String name, String pname){
		StringBuilder exceptSql = new StringBuilder("from " + Permission.dao.tableName + " where 1=1 ");
		List<Object> params = Lists.newArrayList();
		
		// 条件删选
		if (!StrKit.isBlank(code)) {
			exceptSql.append("and code like '" + StringEscapeUtils.escapeSql(code) + "%' ");
		}
		if (!StrKit.isBlank(name)) {
			exceptSql.append("and name like '" + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		if (!StrKit.isBlank(pname)) {
			exceptSql.append("and pname like '" + StringEscapeUtils.escapeSql(pname) + "%' ");
		}
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return Permission.dao.paginate(pageable, "select *", exceptSql.toString(), params.toArray());
	}
	
}
