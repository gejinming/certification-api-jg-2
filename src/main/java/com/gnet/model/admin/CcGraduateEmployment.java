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
 * @table cc_graduate_employment
 * @author sll
 * @version 1.0
 * @date 2016年07月20日 21:54:24
 *
 */
@TableBind(tableName = "cc_graduate_employment")
public class CcGraduateEmployment extends DbModel<CcGraduateEmployment> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcGraduateEmployment dao = new CcGraduateEmployment();
	
		/**
	 * 检查年份的唯一性
	 * 
	 * @param year
	 * @return
	 */
	public Boolean isExists(Integer year, Integer originValue, Long majorId){
		if (originValue != null) {
			return Db.queryLong("select count(*) from " + tableName + " where year = ? and year != ? and major_id = ? and is_del = ? ", year, originValue, majorId, Boolean.FALSE) > 0;
		} else {
			return Db.queryLong("select count(*) from " + tableName + " where year = ? and major_id = ? and is_del = ? ", year, majorId, Boolean.FALSE) > 0;
		}
	}
	
	/**
	 * 检查年份的唯一性
	 * 
	 * @param year
	 * @return
	 */
	public Boolean isExists(Integer year, Long majorId) {
		return isExists(year, null, majorId);
	}
	
	/**
	 * 查看毕业生就业情况表列表分页
	 *
	 * @param majorId
	 * @return
	 */
	public Page<CcGraduateEmployment> page(Pageable pageable, Integer startYear, Integer endYear, Long majorId) {
		StringBuilder exceptSql = new StringBuilder("from " + CcGraduateEmployment.dao.tableName + " cge ");
		exceptSql.append(" left join " + Office.dao.tableName + " so on so.id = cge.major_id  ");
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append("where so.is_del = ? and cge.is_del = ? ");
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		
		// 删选条件
		if (startYear != null) {
			exceptSql.append("and year >= ? ");
			params.add(startYear);
		}
		if (endYear != null) {
			exceptSql.append("and year <= ? ");
			params.add(endYear);
		}
		if (majorId != null) {
			exceptSql.append("and major_id = ? ");
			params.add(majorId);
		}
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcGraduateEmployment.dao.paginate(pageable, "select cge.*,so.name major_name", exceptSql.toString(), params.toArray());
	}

	
	/* (non-Javadoc)
	 * 根据编号查找毕业生就业情况详细
	 * 
	 * @see com.gnet.model.DbModel#findFilteredById(java.lang.Long)
	 */
	public CcGraduateEmployment findFilteredById(Long id) {
		
		StringBuilder exceptSql = new StringBuilder("select cge.*, so.name major_name from " + CcGraduateEmployment.dao.tableName + " cge ");
		exceptSql.append(" left join " + Office.dao.tableName + " so on so.id = cge.major_id ");
		
		exceptSql.append(" where so.is_del = ? and cge.is_del = ? ");
		exceptSql.append(" and cge.id = ? ");
		
		List<Object> params = Lists.newArrayList();
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		params.add(id);
		
		return findFirst(exceptSql.toString(), params.toArray());
	}

}
