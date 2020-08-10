package com.gnet.model.admin;

import java.util.List;

import com.alibaba.druid.util.StringUtils;
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
 * @table cc_school
 * @author zsf
 * @version 1.0
 * @date 2016年06月25日 18:39:35
 *
 */
@TableBind(tableName = "cc_school")
public class School extends DbModel<School> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final School dao = new School();
	
	/**
	 * 学校名是否存在判断
	 * 
	 * @param name
	 * @param originValue  排除值，一般用于编辑校验
	 * @return
	 */
	public boolean isExisted(String name, String originValue) {
		StringBuffer sql = new StringBuffer("select count(1) from " + this.tableName + " cs ");
		sql.append("left join " + Office.dao.tableName + " so on so.id=cs.id ");
		
		if(!StringUtils.isEmpty(originValue)) {
			sql.append("where so.name = ? and so.name != ? and so.is_del = ? ");
			return Db.queryLong(sql.toString(), name, originValue, Boolean.FALSE) > 0;
		} else {
			sql.append("where so.name = ? and so.is_del = ? ");
			return Db.queryLong(sql.toString(), name, Boolean.FALSE) > 0;
		}
	}
	
	/**
	 * 学校编号是否存在判断
	 * 
	 * @param code
	 * @param originValue  排除值，一般用于编辑校验
	 * @return
	 */
	public boolean isExistedOnCode(String code, String originValue) {
		StringBuffer sql = new StringBuffer("select count(1) from " + this.tableName + " cs ");
		sql.append("left join " + Office.dao.tableName + " so on so.id=cs.id ");
		
		if(originValue != null) {
			sql.append("where so.code = ? and so.code != ? and cs.is_del = ? ");
			return Db.queryLong(sql.toString(), code, originValue, Boolean.FALSE) > 0;
		} else {
			sql.append("where so.code = ? and cs.is_del = ? ");
			return Db.queryLong(sql.toString(), code, Boolean.FALSE) > 0;
		}
	}
	
	/**
	 * 
	 * 是否存在此学校名
	 * 
	 * @description 根据学校名查询是否存在该School
	 * @sql select count(1) from cc_school where name=?
	 * @version 1.0
	 * @param name
	 * @return
	 */
	public boolean isExisted(String name) {
		return isExisted(name, null);
	}
	
	/**
	 * 
	 * 是否存在此学校编号
	 * 
	 * @description 根据学校名查询是否存在该School
	 * @sql select count(1) from cc_school where name=?
	 * @version 1.0
	 * @param code
	 * @return
	 */
	public boolean isExistedOnCode(String code) {
		return isExistedOnCode(code, null);
	}
	
	/**
	 * 根据id查找学校信息，附带学校所对应的office的信息
	 * @param id
	 * 		学校编号
	 * @return
	 * 		学校信息
	 */
	public School findById(Long id) {
		StringBuffer sql = new StringBuffer("select so.*, su.loginName from " + tableName + " cs ");
		sql.append("left join " + Office.dao.tableName + " so on so.id=cs.id ");
		sql.append("left join " + User.dao.tableName + " su on su.id = cs.admin_id ");
		sql.append("where so.id = ? and cs.is_del = ? and so.is_del=? and ( su.is_del = ? or su.is_del is null ) ");
		return this.findFirst(sql.toString(), id, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
	}

	/**
	 * 查看学校列表分页
	 * 
	 * @return
	 */
	public Page<School> page(Pageable pageable, String name) {
		List<Object> params = Lists.newArrayList();
		StringBuilder exceptSql = new StringBuilder("from " + School.dao.tableName + " cs ");
		exceptSql.append("left join " + Office.dao.tableName + " so on so.id=cs.id ");
		exceptSql.append("left join " + User.dao.tableName + " su on su.id = cs.admin_id ");
		exceptSql.append("where cs.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and so.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and (su.is_del = ? or su.is_del is null ) ");
		params.add(Boolean.FALSE);
		
		if(StrKit.notBlank(name)) {
			exceptSql.append("and so.name like ? ");
			params.add(name + "%");
		}
		
		// 删选条件
		
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		
		return School.dao.paginate(pageable, "select cs.*,so.name as name,so.code as code,so.description as description, su.loginName ", exceptSql.toString(), params.toArray());
	}

	/**
	 * 查看学校列表分页(只返回学校本身信息)
	 * @param pageable
	 * @return
	 */
	public Page<School> pageList(Pageable pageable) {
		List<Object> params = Lists.newArrayList();
		StringBuilder exceptSql = new StringBuilder("from " + School.dao.tableName + " cs ");
		exceptSql.append("left join " + Office.dao.tableName + " so on so.id=cs.id ");
		exceptSql.append("where cs.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and so.is_del = ? ");
		params.add(Boolean.FALSE);	
		
		// 删选条件
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return School.dao.paginate(pageable, "select cs.*,so.name as name,so.code as code,so.description as description ", exceptSql.toString(), params.toArray());
	}
}
