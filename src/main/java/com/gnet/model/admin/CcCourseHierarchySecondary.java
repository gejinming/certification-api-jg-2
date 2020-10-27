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
 * @description 次要课程层次表操作，包括对数据的增删改查与列表
 * @table cc_course_hierarchy_secondary
 * @author SY
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course_hierarchy_secondary")
public class CcCourseHierarchySecondary extends DbModel<CcCourseHierarchySecondary> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcCourseHierarchySecondary dao = new CcCourseHierarchySecondary();
	
	/**
	 * 检查课程层次名称是否唯一，在某个版本下
	 * @param name
	 * 			新名称
	 * @param originValue
	 * 			原先名称（可以为空）
	 * @param planId
	 * 			版本编号
	 * @return
	 */
	public boolean isExisted(String name, String originValue, Long planId) {
		if (StrKit.notBlank(originValue)) {
			return Db.queryLong("select count(1) from " + CcCourseHierarchySecondary.dao.tableName + " where name = ? and name != ? and is_del = ? and plan_id = ? ",  name, originValue, Boolean.FALSE, planId) > 0;
		} else {
			return Db.queryLong("select count(1) from " + CcCourseHierarchySecondary.dao.tableName + " where name = ? and is_del = ? and plan_id = ?  ",  name, Boolean.FALSE, planId) > 0;
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
	 * 查看专业列表分页
	 * @param pageable
	 * @param planId
	 * 			持续改进版本编号
	 * @param name
	 *          课程层次名称
	 * @return
	 */
	public Page<CcCourseHierarchySecondary> page(Pageable pageable, Long planId, String name) {
		
		List<Object> params = Lists.newArrayList();
		String selectString = "select cchs.* ";
		StringBuilder exceptSql = new StringBuilder("from " + CcCourseHierarchySecondary.dao.tableName + " cchs ");
		exceptSql.append("where cchs.plan_id = ? ");
		params.add(planId);
		// 增加条件，为非软删除的
		exceptSql.append("and cchs.is_del=? ");
		params.add(Boolean.FALSE);
		
		if (StrKit.notBlank(name)) {
			exceptSql.append("and cchs.name like '%" + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcCourseHierarchySecondary.dao.paginate(pageable, selectString, exceptSql.toString(), params.toArray());

	}
	/*
	 * @param name
	 * @return com.gnet.model.admin.CcCourseHierarchy
	 * @author Gejm
	 * @description: 通过层次名找信息
	 * @date 2020/10/12 17:22
	 */
	public CcCourseHierarchySecondary findHierarchySecondaryId(String name,Long planId){
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select * from " + CcCourseHierarchySecondary.dao.tableName + " where name=? and plan_id=? and is_del=0 limit 1");
		params.add(name);
		params.add(planId);
		return findFirst(sql.toString(),params.toArray());
	}

}
