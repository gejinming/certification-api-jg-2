package com.gnet.model.admin;

import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

/**
 * @type model
 * @description 等级制度详细表操作，包括对数据的增删改查与列表
 * @table cc_level_detail
 * @author SY
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_level_detail")
public class CcLevelDetail extends DbModel<CcLevelDetail> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcLevelDetail dao = new CcLevelDetail();

	/**
	 * 查看专业列表分页
	 * @param pageable
	 * @param levelId
	 *
	 * @return
	 */
	public Page<CcLevelDetail> page(Pageable pageable, Long levelId) {
		
		String selectString = "select cld.* ";
		StringBuilder exceptSql = new StringBuilder("from " + CcLevelDetail.dao.tableName + " cld ");
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append("where cld.is_del = ? ");
		params.add(Boolean.FALSE);
		// 删选条件
		if(levelId != null) {
			exceptSql.append("and cld.level_id = ? ");
			params.add(levelId);
		}
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcLevelDetail.dao.paginate(pageable, selectString, exceptSql.toString(), params.toArray());

	}

}
