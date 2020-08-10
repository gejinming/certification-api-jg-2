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
 * @table cc_major_direction
 * @author sll
 * @version 1.0
 * @date 2016年06月28日 17:57:45
 *
 */
@TableBind(tableName = "cc_major_direction")
public class CcMajorDirection extends DbModel<CcMajorDirection> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcMajorDirection dao = new CcMajorDirection();
	
	/**
	 * 方向名称是否存在判断
	 * 
	 * @param name
	 * @param originValue  排除值，一般用于编辑校验
	 * @return
	 */
	public boolean isExisted(String name, Long planId, String originValue) {
		if (StrKit.notBlank(originValue)) {
			return Db.queryLong("select count(1) from cc_major_direction where name = ? and name != ? and plan_id = ? and is_del = ? ", name, originValue, planId, Boolean.FALSE) > 0;
		} else {
			return Db.queryLong("select count(1) from cc_major_direction where name = ? and plan_id = ? and is_del = ? ", name, planId, Boolean.FALSE) > 0;
		}
	}
	
	/**
	 * 
	 * 是否存在此方向名称
	 * 
	 * @description 根据方向名称查询是否存在该CcMajorDirection
	 * @sql select count(1) from cc_major_direction where name=?
	 * @version 1.0
	 * @param name
	 * @return
	 */
	public boolean isExisted(String name) {
		return isExisted(name, null, null);
	}

	/**
	 * 获得专业方向列表(不分页)
	 * @param name
	 * @return
	 */
	public List<CcMajorDirection> findAll(String name) {
		StringBuilder exceptSql = new StringBuilder("select * from " + CcMajorDirection.dao.tableName + " ");
		exceptSql.append("where 1=1 ");
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append("and is_del = ? ");
		params.add(Boolean.FALSE);
		// 删选条件
		if (!StrKit.isBlank(name)) {
			exceptSql.append("and name like '" + org.apache.commons.lang.StringEscapeUtils.escapeSql(name) + "%' ");
		}
		// 增加条件，为非软删除的
		exceptSql.append("and is_del=? ");
		params.add(Boolean.FALSE);
		
		return find(exceptSql.toString(), params.toArray());
	}

	/**
	 * 查看专业方向列表分页
	 * 
	 * @param name
	 * @return
	 */
	public Page<CcMajorDirection> page(Pageable pageable, Long planId, String name) {
		List<Object> params = Lists.newArrayList();
		StringBuilder exceptSql = new StringBuilder("from " + CcMajorDirection.dao.tableName + " cmd ");
		exceptSql.append("where cmd.plan_id = ? ");
		params.add(planId);
		// 增加条件，为非软删除的
		exceptSql.append("and cmd.is_del=? ");
		params.add(Boolean.FALSE);
		
		// 删选条件
		if (StrKit.notBlank(name)) {
			exceptSql.append("and name like '" + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcMajorDirection.dao.paginate(pageable, "select * ", exceptSql.toString(), params.toArray());
	}

}
