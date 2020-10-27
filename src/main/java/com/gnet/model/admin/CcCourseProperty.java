package com.gnet.model.admin;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * @type model
 * @description 课程性质表操作，包括对数据的增删改查与列表
 * @table cc_course_property
 * @author SY
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course_property")
public class CcCourseProperty extends DbModel<CcCourseProperty> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcCourseProperty dao = new CcCourseProperty();

	/**
	 * 检查课程层次名称是否唯一，在某个版本下
	 * @param propertyName
	 * 			新名称
	 * @param originValue
	 * 			原先名称（可以为空）
	 * @param planId
	 * 			版本编号
	 * @return
	 */
	public boolean isExisted(String propertyName, String originValue, Long planId) {
		if (StrKit.notBlank(originValue)) {
			return Db.queryLong("select count(1) from " + tableName + " where property_name = ? and property_name != ? and is_del = ? and plan_id = ? ",  propertyName, originValue, Boolean.FALSE, planId) > 0;
		} else {
			return Db.queryLong("select count(1) from " + tableName + " where property_name = ? and is_del = ? and plan_id = ? ",  propertyName, Boolean.FALSE, planId) > 0;
		}
	}
	
	/**
	 * 检查课程层次名称是否唯一，在某个版本下
	 * @param propertyName
	 * 			新名称
	 * @param planId
	 * 			版本编号
	 * @return
	 */
	public boolean isExisted(String propertyName, Long planId) {
		return isExisted(propertyName, null, planId);
	}

	/**
	 * 查看专业列表分页
	 * @param pageable
	 * @param propertyName 
	 * @param planId
	 *
	 * @return
	 */
	public Page<CcCourseProperty> page(Pageable pageable, Long planId, String propertyName) {
		
		String selectString = "select ccp.* ";
		StringBuilder exceptSql = new StringBuilder("from " + CcCourseProperty.dao.tableName + " ccp ");
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append("where ccp.is_del = ? ");
		params.add(Boolean.FALSE);
		// 删选条件
		if(planId != null) {
			exceptSql.append("and ccp.plan_id = ? ");
			params.add(planId);
		}
		if (!StrKit.isBlank(propertyName)) {
			exceptSql.append("and ccp.property_name like '" + StringEscapeUtils.escapeSql(propertyName) + "%' ");
		}
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcCourseProperty.dao.paginate(pageable, selectString, exceptSql.toString(), params.toArray());

	}

	public CcCourseProperty findCourseProperty(String name,Long planId){
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select * from " + CcCourseProperty.dao.tableName + " where property_name=? and plan_id =? and is_del=0 limit 1");
		params.add(name);
		params.add(planId);
		return findFirst(sql.toString(),params.toArray());
	}

}
