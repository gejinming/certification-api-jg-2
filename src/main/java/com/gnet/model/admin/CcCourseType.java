package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @type model
 * @description 课程类别表的操作，包括对数据的增删改查与列表
 * @table cc_course_type
 * @author gjm
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course_type")
public class CcCourseType extends DbModel<CcCourseType> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcCourseType dao = new CcCourseType();
	
	/**
	 * 检查课程类别编号是否唯一，在某个版本下
	 * @param name
	 * 			新编号
	 * @param originValue
	 * 			原先编号（可以为空）
	 * @param planId
	 * 			版本编号
	 * @return
	 */
	public boolean isExisted(String name, String originValue, Long planId) {
		if (StrKit.notBlank(originValue)) {
			return Db.queryLong("select count(1) from " + tableName + " where type_value = ? and type_value != ? and is_del = ? and plan_id = ? ",  name, originValue, Boolean.FALSE, planId) > 0;
		} else {
			return Db.queryLong("select count(1) from " + tableName + " where type_value = ? and is_del = ? and plan_id = ? ",  name, Boolean.FALSE, planId) > 0;
		}


	}
	/**
	 * 检查课程类别名称是否唯一，在某个版本下
	 * @param name
	 * 			新名称
	 * @param originValue
	 * 			原先名称（可以为空）
	 * @param planId
	 * 			版本编号
	 * @return
	 */
	public boolean isExistedName(String name, String originValue, Long planId) {
		if (StrKit.notBlank(originValue)) {
			return Db.queryLong("select count(1) from " + tableName + " where type_name = ? and type_name != ? and is_del = ? and plan_id = ? ",  name, originValue, Boolean.FALSE, planId) > 0;
		} else {
			return Db.queryLong("select count(1) from " + tableName + " where type_name = ? and is_del = ? and plan_id = ? ",  name, Boolean.FALSE, planId) > 0;
		}


	}
	
	/**
	 * 检查课程层次名称是否唯一，在某个版本下
	 * @param name
	 * 			新名称
	 * @param planId
	 * 			版本编号
	 * @return
	 */
	public boolean isExisted(String name, Long planId) {
		return isExisted(name, null, planId);
	}


	/**
	 * 查看课程类别列表分页
	 * @param pageable
	 * @param planId
	 * 			持续改进版本编号
	 * @param name
	 *          课程层次名称
	 * @return
	 */
	public Page<CcCourseType> page(Pageable pageable, Long planId) {
		
		List<Object> params = Lists.newArrayList();
		String selectString = "select cct.* ";
		StringBuilder exceptSql = new StringBuilder("from " + CcCourseType.dao.tableName + " cct ");
		exceptSql.append("where cct.plan_id = ? ");
		params.add(planId);
		// 增加条件，为非软删除的
		exceptSql.append("and cct.is_del=? ");
		params.add(Boolean.FALSE);
		
		/*if (StrKit.notBlank(name)) {
			exceptSql.append("and cchs.name like '%" + StringEscapeUtils.escapeSql(name) + "%' ");
		}*/
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcCourseType.dao.paginate(pageable, selectString, exceptSql.toString(), params.toArray());

	}

	public CcCourseType findCourseType(Long planId,String typeValue){
		ArrayList<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select * from " + CcCourseType.dao.tableName + " where plan_id=? and type_value=? and is_del=0 ");
		params.add(planId);
		params.add(typeValue);
		return findFirst(sql.toString(),params.toArray());
	}

}
