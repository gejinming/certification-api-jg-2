package com.gnet.model.admin;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

/**
 * @type model
 * @description 等级制度表操作，包括对数据的增删改查与列表
 * @table cc_level
 * @author SY
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_level")
public class CcLevel extends DbModel<CcLevel> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcLevel dao = new CcLevel();

	/**
	 * 查看专业列表分页
	 * @param pageable
	 * @param planId
	 * @param name 
	 *
	 * @return
	 */
	public Page<CcLevel> page(Pageable pageable, Long planId, String name) {
		
		String selectString = "select cl.* ";
		StringBuilder exceptSql = new StringBuilder("from " + CcLevel.dao.tableName + " cl ");
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append("where cl.is_del = ? ");
		params.add(Boolean.FALSE);
		// 删选条件
		if(planId != null) {
			exceptSql.append("and cl.plan_id = ? ");
			params.add(planId);
		}
		if (!StrKit.isBlank(name)) {
			exceptSql.append("and cl.name like '" + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcLevel.dao.paginate(pageable, selectString, exceptSql.toString(), params.toArray());

	}

}
