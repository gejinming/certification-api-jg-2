package com.gnet.model.admin;

import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;

/**
 * 
 * @type model
 * @table cc_school
 * @author SY
 * @version 1.0
 * @date 2016年7月19日18:22:25
 *
 */
@TableBind(tableName = "cc_school")
public class CcSchool extends DbModel<CcSchool> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcSchool dao = new CcSchool();
	

	/**
	 * 获得学校下的所有未删除Office
	 * 
	 * @param schoolId
	 * @return
	 */
	public List<Office> findBySchool(Long schoolId) {
		StringBuilder sql = new StringBuilder("select so.* from " + Office.dao.tableName + " so ");
		sql.append("left join " + OfficePath.dao.tableName + " sop on sop.id = so.id ");
		sql.append("where sop.office_ids like '," + schoolId.toString() + ",%' ");
		sql.append("and so." + IS_DEL_LABEL + "=?");
		return Office.dao.find(sql.toString(), DEL_NO);
	}
	
	/**
	 * 获得学校下的所有未删除Office
	 * 
	 * @param department
	 * @return
	 */
	public List<Office> findByDepartment(Long department) {
		StringBuilder sql = new StringBuilder("select so.* from " + Office.dao.tableName + " so ");
		sql.append("left join " + OfficePath.dao.tableName + " sop on sop.id = so.id ");
		sql.append("where sop.office_ids like '%," + department.toString() + ",%' ");
		sql.append("and so." + IS_DEL_LABEL + "=?");
		return Office.dao.find(sql.toString(), DEL_NO);
	}

}
