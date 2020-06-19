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
 * @description 次要课程性质表操作，包括对数据的增删改查与列表
 * @table cc_course_property_secondary
 * @author SY
 * @version 1.0
 * @date 2019年12月3日16:14:16
 *
 */
@TableBind(tableName = "cc_course_property_secondary")
public class CcCoursePropertySecondary extends DbModel<CcCoursePropertySecondary> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcCoursePropertySecondary dao = new CcCoursePropertySecondary();
	
	/**
	 * 查询列表
	 * @param pageable
	 * @param planId
	 * @param propertyName
	 * @return
	 */
	public Page<CcCoursePropertySecondary> page(Pageable pageable, Long planId, String propertyName) {
			
		String selectString = "select ccps.* ";
		StringBuilder exceptSql = new StringBuilder("from " + CcCoursePropertySecondary.dao.tableName + " ccps ");
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append("where ccps.is_del = ? ");
		params.add(Boolean.FALSE);
		// 删选条件
		if(planId != null) {
			exceptSql.append("and ccps.plan_id = ? ");
			params.add(planId);
		}
		if (!StrKit.isBlank(propertyName)) {
			exceptSql.append("and ccps.property_name like '" + StringEscapeUtils.escapeSql(propertyName) + "%' ");
		}
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcCoursePropertySecondary.dao.paginate(pageable, selectString, exceptSql.toString(), params.toArray());

		}

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

}
