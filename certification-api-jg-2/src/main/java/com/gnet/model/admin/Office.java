package com.gnet.model.admin;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.gnet.Constant;
import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;

/**
 * @type model
 * @description office的model
 * @table sys_office
 * @author Administrator
 * @version 1.0
 */
@TableBind(tableName = "sys_office")
public class Office extends DbModel<Office> {

	private static final long serialVersionUID = -917936874298062848L;

	/**
	 * 学校类别
	 */
	public static String TYPE_SCHOOL = "0";
	/**
	 * 学院类别
	 */
	public static String TYPE_BRANCH = "1";
	/**
	 * 专业类别
	 */
	public static String TYPE_MAJOR = "2";
	/**
	 * 行政班类别
	 */
	public static String TYPE_CLAZZ = "3";

	public static final Office dao = new Office();

	/**
	 * 是否存在此code
	 * 
	 * @description 根据code查询是否存在该code
	 * @sql select count(1) from sys_office where code=?
	 * @version 1.0
	 * @param code
	 * @return
	 */
	public boolean isExisted(String code) {
		long count = Db.queryLong("select count(1) from " + tableName + " where code=?", code);
		return count > 0;
	}
	
	/**
	 * 编辑时判断是否存在此code
	 * 
	 * @description 根据code查询是否存在该code
	 * @sql select count(1) from sys_office where code=? and id!=?
	 * @version 1.0
	 * @param code
	 * @return
	 */
	public boolean isExisted(String code, String originValue) {
		if (StrKit.notBlank(originValue)) {
			return Db.queryLong("select count(1) from " + tableName + " where code=? and is_del=? and code!=?", code, DEL_NO, originValue) > 0;
		} else {
			return Db.queryLong("select count(1) from " + tableName + " where code=? and is_del=?", code, DEL_NO) > 0;
		}
	}

	/**
	 * 查找所有的公司级别的组织
	 * 
	 * @description 根据类型查询所有的公司级别的组织
	 * @sql select * from sys_office where type=?
	 * @version 1.0
	 * @return
	 */
	public List<Office> findAllTop() {
		return find("select * from " + tableName + " where type=?", Constant.OFFICE_TYPE_COMPANY);
	}
	
	/**
	 * 查找学校下的所有专业
	 * @param schoolId
	 * 			学校编号
	 * @return
	 * 			专业列表
	 */
	public List<Office> findMajorsBySchoolId(String schoolId) {
		StringBuffer sql = new StringBuffer("select office1.*,office2.name as parent_name from " + tableName + " office1 ");
		sql.append("left join " + tableName + " office2 on office2.id=office1.parentid ");
		sql.append("where office1.classid like ? and office1.type=?");
		sql.append("order by office1.parentid,office1.id ");
		return find(sql.toString(), "," + schoolId + ",%", Office.TYPE_MAJOR);
	}
	
	/**
	 * 查找专业下的所有行政班
	 * @param majorId
	 * 			专业编号
	 * @return
	 * 			行政班列表
	 */
	public List<Office> findClazzsByMajorId(Long majorId) {
		String sql = "select * from " + tableName + " where parentid=? and type=? ";
		return find(sql, majorId, Office.TYPE_CLAZZ);
	}
	
	/**
	 * 是否只有一个孩子
	 * 
	 * @description 根据父亲编号查找是否只有一个孩子
	 * @sql select count(1) from sys_office where parentid=?
	 * @version 1.0
	 * @param parentid
	 * @return
	 */
	public boolean hasOnlyChild(Long parentid) {
		long count = Db.queryLong("select count(1) from " + tableName + " where parentid=?", parentid);
		return count == 1;
	}

	/**
	 * 保存一个空值
	 * 
	 * @description 保存一个空值
	 * @sql insert into
	 *      sys_office(create_date,modify_date,classid,parentid,parent_classid,
	 *      isleaf,type,code,name,grade,is_system) values
	 *      ('2015-09-03','2015-09-04','-1','-1','-1',true,'','','','-1',false)
	 * @version 1.0
	 * @return
	 */
	public Long saveNull() {
		Office office = new Office();
		office.set("create_date", new Date());
		office.set("modify_date", office.getDate("create_date"));
		office.set("classid", "-1");
		office.set("parentid", -1);
		office.set("parent_classid", "-1");
		office.set("isleaf", Boolean.TRUE);
		office.set("type", "");
		office.set("code", "");
		office.set("name", "");
		office.set("grade", -1);
		office.set("is_system", Constant.NOTSYSTEM);
		office.save();
		return office.getLong("id");
	}

	/**
	 * 删除这些classids及以下的对象
	 * 
	 * @description 根据教学班编号删除这些编号以下的对象
	 * @sql delete from sys_office where classid like ?%
	 * @version 1.0
	 * @param classids
	 */
	public void deleteByLike(String[] classids) {
		List<String> sqlList = Lists.newArrayList();
		for (String classid : classids) {
			sqlList.add("delete from " + tableName + " where classid like '" + classid + "%'");
		}
		// 进行批量删除操作
		Db.batch(sqlList, classids.length);
	}
	
	public List<Office> getDepartmentByName(String[] names, Long officeId) {
		StringBuffer sql = new StringBuffer("select id,name from sys_office ");
		sql.append("where name in (" + CollectionKit.convert(names, ",", true) + ") ");
		sql.append("and parent_classid like ? ");
		sql.append("and grade != 0 ");
		sql.append("order by instr('" + CollectionKit.convert(names, ",", false) + "',name)");
		return find(sql.toString(), "%" + officeId + "%");
	}
	
	/**
	 * 获得所有(未删除)部门
	 * 
	 * @param name
	 * @param code
	 * @return
	 */
	public List<Office> findAll(String name, String code) {
		StringBuilder sql = new StringBuilder("select * from " + tableName + " ");
		sql.append("where " + IS_DEL_LABEL + "=? ");
		if (StrKit.notBlank(name)) {
			sql.append("and name like '%" + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		if (StrKit.notBlank(code)) {
			sql.append("and code like '%" + StringEscapeUtils.escapeSql(code) + "%' ");
		}
		return find(sql.toString(), DEL_NO);
	}
	
	/**
	 * 根据层次查找部门的树结构（排除学校节点)
	 * 
	 * @param type
	 * @return
	 */
	public List<Office> findDepartmentTreeByLevel(String type, String departmentId){
		return findDepartmentTreeByLevel(type, departmentId, Boolean.FALSE);
	}
	
	/**
	 * 根据层次查找部门的树结构
	 * 
	 * @param type
	 * @param departmentId
	 * @param includeSchool
	 * 			是否包括顶层节点(特指学校节点)
	 * @return
	 */
	public List<Office> findDepartmentTreeByLevel(String type, String departmentId, Boolean includeSchool){
		
		List<Object> params = Lists.newArrayList();
		StringBuilder sb = new StringBuilder("select so.*, sop.*, office.name parentName, cc.grade from " + Office.dao.tableName + " so ");
		sb.append("left join " + Office.dao.tableName + " office on office.id = so.parentid ");
		sb.append(" left join " + OfficePath.dao.tableName + " sop on sop.id = so.id ");
		sb.append("left join " + CcClass.dao.tableName + " cc on cc.id = so.id ");
		sb.append(" where so.type <= ? ");
		params.add(type);
		sb.append(" and office_ids like '%,"+ departmentId +",%' ");
		sb.append(" and so.is_del = ? ");
		params.add(Boolean.FALSE);
		sb.append("and (cc.is_del = ? or cc.is_del is null ) ");
		params.add(DEL_NO);
		if(includeSchool) {
			/*
			 *  这里office表示的是父节点，由于学校已经是最顶层了，所以加入office.is_del = false条件会导致学校节点被过滤
			 *  加入so.type = " + TYPE_SCHOOL，以后会重新把这个过滤取消
			 */
			sb.append(" and (so.type = " + TYPE_SCHOOL + " or office.is_del = ?) ");
			params.add(Boolean.FALSE);
		} else {
			sb.append(" and office.is_del = ? ");
			params.add(Boolean.FALSE);
		}
		sb.append(" order by sop.office_ids ");
		
		return find(sb.toString(), params.toArray());
	}
	
	/**
	 * 通过编号获得部门（附部门路径）
	 * 
	 * @param id
	 * @return
	 */
	public Office findByIdWithPath(Long id) {
		StringBuilder sql = new StringBuilder("select so.*, sop.office_ids office_path from " + Office.dao.tableName + " so ");
		sql.append("left join " + OfficePath.dao.tableName + " sop on sop.id = so.id ");
		sql.append("where so.id = ? and so.is_del = ? ");
		sql.append("limit 1");
		return findFirst(sql.toString(), id, DEL_NO);
	}
	
	/**
	 * 获取某部门下所有下级部门
	 * 
	 * @param id
	 * @return
	 */
	public List<Office> findUnderOfficeByPath(String path) {
		StringBuilder sql = new StringBuilder("select so.* from " + Office.dao.tableName + " so ");
		sql.append("left join " + OfficePath.dao.tableName + " sop on sop.id = so.id ");
		sql.append("where sop.office_ids like '" + path + "%' and so.is_del = ? ");
		return find(sql.toString(), DEL_NO);
	}

	/**
	 * 根据部门编号获取所有相关的专业
	 * 
	 * @param officeId
	 * @return
	 */
	public List<Office> findMajorByPath(String officePath) {
		return findOfficeByPath(officePath, Office.TYPE_MAJOR);
	}
	
	/**
	 * 根据部门编号获取所有相关的学院
	 * 
	 * @param officeId
	 * @return
	 */
	public List<Office> findInstituteByPath(String officePath) {
		return findOfficeByPath(officePath, Office.TYPE_BRANCH);
	}

	/**
	 * 根据部门路径和部门类型获取部门列表
	 * @param officePath
	 * @param type
	 * @return
	 */
	public List<Office> findOfficeByPath(String officePath, String type){
		StringBuilder sql = new StringBuilder("select so.id from " + Office.dao.tableName + " so ");
		sql.append("left join sys_office_path sop on sop.id = so.id ");
		sql.append("where sop.office_ids like '" + officePath + "%' and so.type = ? and so.is_del = ? ");
		return Office.dao.find(sql.toString(), type, DEL_NO);
	}
}