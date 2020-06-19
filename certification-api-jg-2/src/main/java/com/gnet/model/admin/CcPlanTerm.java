package com.gnet.model.admin;

import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.pager.Pageable;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import org.apache.commons.lang3.StringUtils;


/**
 * 
 * @type model
 * @table cc_plan_term
 * @author sll
 * @version 1.0
 * @date 2016年07月04日 08:30:41
 *
 */
@TableBind(tableName = "cc_plan_term")
public class CcPlanTerm extends DbModel<CcPlanTerm> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcPlanTerm dao = new CcPlanTerm();
	
	/**
	 * 培养计划学年学期唯一性验证
	 * 
	 * @param year
	 * @param term
	 * @param yearOriginValue
	 * @param termOriginValue
	 * @return
	 */
	public Boolean isExists(Integer year, Integer term, String yearName, String termName, Long planId, Long planTermOriginValueId){
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select count(1) from " + tableName + " ");
		sql.append("where year = ? and term = ? and year_name = ? and term_name = ? and is_del = ? and plan_id = ? ");
		params.add(year);
		params.add(term);
		params.add(yearName);
		params.add(termName);
		params.add(CcPlanTerm.DEL_NO);
		params.add(planId);
		if (planTermOriginValueId != null) {
			sql.append("and id != ?");
			params.add(planTermOriginValueId);
		}
		
		
		return Db.queryLong(sql.toString(), params.toArray()) > 0;
	}
	
	
	/**
	 * 查看抽象学期表列表分页
	 * 
	 * @return
	 */
	public Page<CcPlanTerm> page(Pageable pageable, String yearName, String termName, Long planId) {
		StringBuilder exceptSql = new StringBuilder("from " + CcPlanTerm.dao.tableName + " ");
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append("where is_del = ? ");
		params.add(Boolean.FALSE);
		
		// 删选条件
		if (!StrKit.isBlank(yearName)) {
			exceptSql.append("and year_name like '" + org.apache.commons.lang.StringEscapeUtils.escapeSql(yearName) + "%' ");
		}
		if (!StrKit.isBlank(termName)) {
			exceptSql.append("and term_name like '" + org.apache.commons.lang.StringEscapeUtils.escapeSql(termName) + "%' ");
		}
		if (planId != null) {
			exceptSql.append("and plan_id = ? ");
			params.add(planId);
		}
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		} else {
		 	exceptSql.append("order by year, term_type, term ");
 		}
		
		return CcPlanTerm.dao.paginate(pageable, "select * ", exceptSql.toString(), params.toArray());
	}
	
	/**
	 * 获得培养计划学期，并进行排序
	 * 
	 * @param planId 培养计划编号
	 * @return
	 */
    public List<CcPlanTerm> findAllSort(Long planId, Integer reportType) {
        //理论课不存在短学期课程
        if (reportType.equals(1)) {
            return find("select * from " + CcPlanTerm.dao.tableName + " where plan_id = ? and is_del = ? and term_type=1  order by sort desc, term_type asc, year asc, term asc", planId, DEL_NO);
        } else {
            return find("select * from " + CcPlanTerm.dao.tableName + " where plan_id = ? and is_del = ?  order by sort desc, term_type asc, year asc, term asc", planId, DEL_NO);
        }

    }
	/**
	 * 学年学期学期类型唯一唯一性判断
	 * @param year
	 * @param term
	 * @param termType
	 * @param id
	 * @param planId
	 * @return
	 */
	public Boolean isExists(Integer year, Integer term, Integer termType, Long id, Long planId) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select count(1) from " + tableName + " ");
		sql.append("where is_del = ? and year = ? and plan_id = ? ");
		param.add(DEL_NO);
		param.add(year);
		param.add(planId);
		sql.append("and term  = ? ");
		param.add(term);
		sql.append("and term_type = ? ");
		param.add(termType);
        if(id != null){
        	sql.append("and id != ? ");
        	param.add(id);
        }
		
		return Db.queryLong(sql.toString(), param.toArray()) > 0;
	}
	

	/**
	 * 学年名称学期名称学期类型唯一性验证
	 * @param yearName
	 * @param termName
	 * @param termType
	 * @param id
	 * @param planId
	 * @return
	 */
	public Boolean isExists(String yearName, String termName, Integer termType, Long id, Long planId) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select count(1) from " + tableName + " ");
		sql.append("where is_del = ? and year_name = ? and plan_id = ? ");
		param.add(DEL_NO);
		param.add(yearName);
		param.add(planId);
		sql.append("and term_name = ? ");
		param.add(termName);
		sql.append("and term_type = ? ");
		param.add(termType);
        if(id != null){
        	sql.append("and id != ? ");
        	param.add(id);
        }
		
		return Db.queryLong(sql.toString(), param.toArray()) > 0;
	}


	public List<CcPlanTerm> findByPlanId(Long planId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sb = new StringBuilder("select cpt.term_name termName, cpt.year_name yearName, cpt.id planTermId ");
		sb.append("from " + tableName + "  cpt ");
		sb.append("where cpt.plan_id = ? ");
		params.add(planId);
		sb.append("and cpt.is_del = ? ");
		params.add(Boolean.FALSE);
		return find(sb.toString(), params.toArray());
	}

}
