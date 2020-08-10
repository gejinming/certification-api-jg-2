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
 * @table cc_enrollement
 * @author sll
 * @version 1.0
 * @date 2016年07月19日 09:41:29
 *
 */
@TableBind(tableName = "cc_enrollment")
public class CcEnrollment extends DbModel<CcEnrollment> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcEnrollment dao = new CcEnrollment();
	
	/**
	 * 检查同一专业下年份的唯一性
	 * 
	 * @param year
	 * @param majorId
	 * @return
	 */
	public Boolean isExists(Integer year, Integer originValue, Long majorId) {
		if (originValue != null) {
			return Db.queryLong("select count(*) from " + tableName + " where year = ? and year != ? and major_id = ? and is_del = ? ", year, originValue, majorId, Boolean.FALSE ) > 0;
		} else {
			return Db.queryLong("select count(*) from " + tableName + " where year = ? and major_id = ? and is_del = ? ", year, majorId, Boolean.FALSE ) > 0;
		}
	}
	
	/**
	 * 检查同一专业下年份的唯一性
	 * 
	 * @param year
	 * @param majorId
	 * @return
	 */
	public Boolean isExists(Integer year, Long majorId) {
		return isExists(year, null, majorId);
	}
	
	/**
	 * 查看招生情况表列表分页
	 * @param majorId 
	 * 
	 * @return
	 */
	public Page<CcEnrollment> page(Pageable pageable, Integer startYear, Integer endYear, String majorName, Long majorId) {
		StringBuilder exceptSql = new StringBuilder("from " + CcEnrollment.dao.tableName + " ce ");
		exceptSql.append(" left join " + Office.dao.tableName + " so on so.id = ce.major_id ");
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append("where so.is_del = ? and ce.is_del = ? ");
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		
		// 删选条件
		if (startYear != null) {
			exceptSql.append(" and ce.year >= ? ");
			params.add(startYear);
		}
		if (endYear != null) {
			exceptSql.append(" and ce.year <= ? ");
			params.add(endYear);
		}
		if(majorId !=null){
			exceptSql.append(" and so.id = ? ");
			params.add(majorId);
		}
		if (StrKit.notBlank(majorName)) {
			exceptSql.append("and so.name like '%" + StringEscapeUtils.escapeSql(majorName) + "%' ");
		}
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcEnrollment.dao.paginate(pageable, "select ce.*,so.name major_name ", exceptSql.toString(), params.toArray());
	}

	
	/**
	 * 根据编号查找详情
	 * 
	 * @return
	 */
	public CcEnrollment findFilteredById(Long id){
		StringBuilder exceptSql = new StringBuilder("select ce.*, so.name major_name from " + CcEnrollment.dao.tableName + " ce ");
		exceptSql.append(" left join " + Office.dao.tableName + " so on so.id = ce.major_id ");
		exceptSql.append(" where ce.id = ? ");
		
		List<Object> params = Lists.newArrayList();
		params.add(id);
		
		return findFirst(exceptSql.toString(), params.toArray());
	}
}
