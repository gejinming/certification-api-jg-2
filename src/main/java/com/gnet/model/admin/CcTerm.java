package com.gnet.model.admin;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;


/**
 * 
 * @type model
 * @table cc_term
 * @author sll
 * @version 1.0
 * @date 2016年07月03日 17:31:09
 *
 */
@TableBind(tableName = "cc_term")
public class CcTerm extends DbModel<CcTerm> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcTerm dao = new CcTerm();
	
	/**
	 * 查看学期表列表分页
	 * 
	 * @return
	 */
	public Page<CcTerm> page(Pageable pageable, Integer startYear, Integer endYear, Integer term, Long schoolId) {
		StringBuilder exceptSql = new StringBuilder("from " + CcTerm.dao.tableName + " ");
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append("where is_del = ? ");
		params.add(Boolean.FALSE);
		
		if(startYear != null){
			exceptSql.append("and start_year = ? ");
			params.add(startYear);
		}
		
		if(endYear != null){
			exceptSql.append("and end_year = ? ");
			params.add(endYear);
		}
		
		if(term != null){
			exceptSql.append("and term = ? ");
			params.add(term);
		}
		
		// 数据权限校验
		if (schoolId != null) {
			exceptSql.append("and school_id = ? ");
			params.add(schoolId);
		} else {
			exceptSql.append("and 0 = 1 ");
		}
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		} else {
			exceptSql.append("order by start_year, term_type, term ");
		}
		
		return CcTerm.dao.paginate(pageable, "select * ", exceptSql.toString(), params.toArray());
	}
	
	
	/**
	 * 学年学期唯一性验证
	 * 
	 * @param startYear
	 * @param endYear
	 * @return
	 */
	public Boolean isExists(Integer startYear, Integer endYear, Integer term, Integer termType, Long schoolId, Long originValueId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sb = new StringBuilder("select count(1) from " + tableName + " " );
		sb.append(" where start_year = ? and end_year = ? and term = ? and term_type = ? and school_id = ? and is_del = ? ");
		
		params.add(startYear);
		params.add(endYear);
		params.add(term);
		params.add(termType);
		params.add(schoolId);
		params.add(Boolean.FALSE);
		
		// 忽略原值
		if (originValueId != null) {
			sb.append("and id != ? ");
			params.add(originValueId);
		}
		
		return Db.queryLong(sb.toString(), params.toArray()) > 0;
	}


}
