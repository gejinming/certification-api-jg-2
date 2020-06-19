package com.gnet.model.admin;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * 
 * @type model
 * @description 角色model，对角色信息进行管理，如查询、删除等功能。
 * @table sys_role
 * @author Administrator
 * @version 1.0
 *
 */
@TableBind(tableName = "sys_role")
public class Role extends DbModel<Role> {

	private static final long serialVersionUID = 7146911907088939070L;

	public static final Role dao = new Role();
	
	/**
	 * 查看角色列表分页
	 * 
	 * @param pageable
	 * @param name
	 * @return
	 */
	public Page<Role> page(Pageable pageable, String name) {
		StringBuilder exceptSql = new StringBuilder("from " + Role.dao.tableName + " sr ");
		exceptSql.append("left join sys_role_permission srp on srp.role_id = sr.id ");
		exceptSql.append("where 1=1 ");
		List<Object> params = Lists.newArrayList();
		
		// 条件删选
		if (!StrKit.isBlank(name)) {
			exceptSql.append("and sr.name like '%" + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		return Role.dao.paginate(pageable, "select sr.*, srp.permissions permissions ", exceptSql.toString(), params.toArray());
	}
	
	
	/**
	 * 
	 * 是否存在此角色名字
	 * 
	 * @description 根据角色姓名查询是否存在该角色
	 * @sql select count(1) from sys_role where name=?
	 * @version 1.0
	 * @param role_name
	 * @return
	 */
	public boolean isExisted(String role_name) {
		return this.isExisted(role_name, null);
	}

	/**
	 * 
	 * 是否存在此角色名字
	 * 
	 * @description 根据角色姓名查询是否存在该角色
	 * @sql select count(1) from sys_role where name=?
	 * @version 1.0
	 * @param role_name
	 * @return
	 */
	public boolean isExisted(String role_name, String originValue) {
		if (StrKit.notBlank(originValue)) {
			return Db.queryLong("select count(1) from " + tableName + " where name=? and name!=?", role_name, originValue) > 0;
		} else {
			return Db.queryLong("select count(1) from " + tableName + " where name=?", role_name) > 0;
		}
	}

	/**
	 * 找到该用户的角色门
	 * 
	 * @description 根据用户编号查找该用户的角色信息
	 * @sql select * from sys_role where id in (select permissions from
	 *      sys_user_role where user_id=?")
	 * @version 1.0
	 * @param userid
	 * @return
	 */
	public List<Role> findRolesByUserId(Long userid) {
		String subsql = "select permissions from sys_user_role where user_id=?";
		String sql = "select * from " + tableName + " where id in (" + subsql + ")";
		return find(sql, userid);
	}

	/**
	 * 找到该用户的角色名门
	 * 
	 * @description 根据用户编号查询该用户的角色名
	 * @sql select name from sys_role where id in (select permissions from
	 *      sys_user_role where user_id=?)
	 * @version 1.0
	 * @param userid
	 * @return
	 */
	public List<String> findRolesNameByUserId(Long userid) {
		String subsql = "select permissions from sys_user_role where user_id=?";
		String sql = "select name from " + tableName + " where id in (" + subsql + ")";
		return Db.query(sql);
	}

	/**
	 * 获取对应name的角色
	 * 
	 * @description 根据姓名获取对应姓名的角色
	 * @sql select * from sys_role where name=?
	 * @version 1.0
	 * @author SY
	 * @param name
	 *            角色名字
	 * @return
	 */
	public Role findByName(String name) {
		return findFirst("select * from " + tableName + " where name=? ", name);
	}

	/**
	 * 删除所有
	 * 
	 * @description 根据编号删除所有的角色
	 * @sql delete from sys_role where id in (34,45,...)
	 * @version 1.0
	 * @param ids
	 */
	public void deleteAll(String[] ids) {
		// 进行批量删除操作
		Db.update("delete from " + tableName + " where id in (" + CollectionKit.convert(ids, ",", false) + ")");
	}

}
