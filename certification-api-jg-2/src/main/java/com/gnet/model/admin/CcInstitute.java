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
 * @table sys_office
 * @author zsf
 * @version 1.0
 * @date 2016年06月26日 18:57:47
 *
 */
@TableBind(tableName = "cc_institute")
public class CcInstitute extends DbModel<CcInstitute> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcInstitute dao = new CcInstitute();
	
	/**
	 * 学院名是否存在判断
	 * 
	 * @param name
	 * @param originValue  排除值，一般用于编辑校验
	 * @return
	 */
	public boolean isExisted(Long schoolId, String name, String originValue) {
		if (originValue != null) {
			return Db.queryLong("select count(1) from sys_office where parentid=? and name = ? and name != ? and is_del = ? and type = ?  ", schoolId, name, originValue, Boolean.FALSE, Office.TYPE_BRANCH) > 0;
		} else {
			return Db.queryLong("select count(1) from sys_office where parentid=? and name = ? and is_del = ? and type = ? ", schoolId, name, Boolean.FALSE, Office.TYPE_BRANCH) > 0;
		}
	}
	
	/**
	 * 学院编码是否存在判断
	 * 
	 * @param code
	 * @param originValue  排除值，一般用于编辑校验
	 * @return
	 */
	public boolean isExistedOnCode(Long schoolId, String code, String originValue) {
		if (originValue != null) {
			return Db.queryLong("select count(1) from sys_office where parentid=? and code = ? and code != ? and is_del = ? and type = ?  ", schoolId, code, originValue, Boolean.FALSE, Office.TYPE_BRANCH) > 0;
		} else {
			return Db.queryLong("select count(1) from sys_office where parentid=? and code = ? and is_del = ? and type = ? ", schoolId, code, Boolean.FALSE, Office.TYPE_BRANCH) > 0;
		}
	}
	
	/**
	 * 
	 * 是否存在此学院名
	 * 
	 * @description 根据学院名查询是否存在该CcInstitute
	 * @sql select count(1) from sys_office where name=?
	 * @version 1.0
	 * @param name
	 * @return
	 */
	public boolean isExisted(Long schoolId, String name) {
		return isExisted(schoolId, name, null);
	}
	
	/**
	 * 
	 * 是否存在此学院编码
	 * 
	 * @description 根据学院名查询是否存在该CcInstitute
	 * @sql select count(1) from sys_office where name=?
	 * @version 1.0
	 * @param code
	 * @return
	 */
	public boolean isExistedOnCode(Long schoolId, String code) {
		return isExistedOnCode(schoolId, code, null);
	}
	
	/**
	 * 根据id查找指定学院信息
	 * @param id
	 * 			学院编号
	 * @return
	 * 			学院信息
	 */
	public CcInstitute findById(Long id) {
		StringBuffer sql = new StringBuffer("select so.* from " + Office.dao.tableName + " so ");
		sql.append("left join " + this.tableName + " ci on ci.id=so.id ");
		sql.append("where so.id=? and so.is_del=? ");
		return this.findFirst(sql.toString(), id, Boolean.FALSE);
	}

	/**
	 * 获得学院列表(不分页)
	 * @param pageable 
	 * @param code
	 * @param name
	 * @return
	 */
	public List<CcInstitute> findAll(Pageable pageable, String code, String name) {
		StringBuilder exceptSql = new StringBuilder("select so.* from " + CcInstitute.dao.tableName + " ci ");
		exceptSql.append("left join " + Office.dao.tableName + " so on so.id=ci.id ");
		exceptSql.append("where 1=1 ");
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append("and so.is_del = ? ");
		params.add(Boolean.FALSE);
		// 删选条件
		if (!StrKit.isBlank(code)) {
			exceptSql.append("and so.code like '" + org.apache.commons.lang.StringEscapeUtils.escapeSql(code) + "%' ");
		}
		if (!StrKit.isBlank(name)) {
			exceptSql.append("and so.name like '" + org.apache.commons.lang.StringEscapeUtils.escapeSql(name) + "%' ");
		}
		// 增加条件，为非软删除的
		exceptSql.append("and ci.is_del=? ");
		params.add(Boolean.FALSE);
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return find(exceptSql.toString(), params.toArray());
	}
	
	/**
	 * 查看学院列表分页
	 *
	 * @param name
	 * @param inDepartmentOffice 获取的学院必须在该部门下
	 * @return
	 */
	public Page<CcInstitute> page(Pageable pageable, String name, Office inDepartmentOffice) {
		StringBuilder exceptSql = new StringBuilder("from " + Office.dao.tableName + " so ");
		exceptSql.append("left join " + CcInstitute.dao.tableName + " ci on ci.id=so.id ");
		exceptSql.append("where 1=1 ");
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append("and so.is_del = ? and so.type = ? ");
		params.add(Boolean.FALSE);
		params.add(Office.TYPE_BRANCH);
		
		//数据权限
		if (inDepartmentOffice != null) {
			String type = inDepartmentOffice.getStr("type");
			if (Office.TYPE_SCHOOL.equals(type)) {
				//学校
				exceptSql.append("and so.parentid=? ");
				params.add(inDepartmentOffice.get("id"));
			} else if (Office.TYPE_BRANCH.equals(type)) {
				//学院
				exceptSql.append("and so.id=? ");
				params.add(inDepartmentOffice.get("id"));
			} else {
				//其它级别的部门无权查询学院列表
				exceptSql.append("and 1=2 ");
			}
		}
		// 删选条件
		if (!StrKit.isBlank(name)) {
			exceptSql.append("and so.name like '" + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcInstitute.dao.paginate(pageable, "select so.* ", exceptSql.toString(), params.toArray());
	}

}
