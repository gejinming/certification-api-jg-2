package com.gnet.model.admin;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * 
 * @type model
 * @table cc_class
 * @author sll
 * @version 1.0
 * @date 2016年06月29日 17:46:25
 *
 */
@TableBind(tableName = "cc_class")
public class CcClass extends DbModel<CcClass> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcClass dao = new CcClass();
	
	/**
	 * 根据编号查找行政班具体信息
	 * 
	 * @param id
	 * @return
	 */
	public CcClass findById(Long id){
		
		StringBuilder sql = new StringBuilder("select cc.*, so.*, major.name majorName, institute.id instituteId, institute.name instituteName from " + tableName + " cc ");
		sql.append("left join " + Office.dao.tableName + " so on so.id = cc.id ");
		sql.append("left join " + Office.dao.tableName + " major on major.id = so.parentid ");
		sql.append("left join " + Office.dao.tableName + " institute on institute.id = major.parentid " );
		sql.append("where so.is_del = ? and cc.is_del = ? ");
		sql.append("and cc.id = ? ");
		
		List<Object> params = Lists.newArrayList();
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		params.add(id);
		
		return findFirst(sql.toString(), params.toArray());
	}
	
	/**
	 * 查看行政班列表分页
	 * @param majorId 
	 * 
	 * @return
	 */
	public Page<CcClass> page(Pageable pageable, String name, Long[] majorIds, Integer grade, Long majorId) {
		
		StringBuilder exceptSql = new StringBuilder(" from " + CcClass.dao.tableName + " cc ");
		exceptSql.append(" left join " + Office.dao.tableName + " office on office.id = cc.id ");
		exceptSql.append("left join " + Office.dao.tableName + " major on office.parentid = major.id " );
		exceptSql.append("left join " + Office.dao.tableName + " institute on major.parentid = institute.id " );
		exceptSql.append(" where cc.is_del = ? ");
		
		
		List<Object> params = Lists.newArrayList();
		params.add(Boolean.FALSE);
		
		// 删选条件
		if (!StrKit.isBlank(name)) {
			exceptSql.append("and office.name like '" + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		if(grade != null){
			exceptSql.append("and cc.grade = ? ");
			params.add(grade);
		}

		if(majorId != null){
			exceptSql.append("and office.parentid = ? ");
			params.add(majorId);
		}else if (majorIds.length > 0) {
			//数据权限过滤
			exceptSql.append("and office.parentid in (" + CollectionKit.convert(majorIds, ",") + ") ");
		} else {
			exceptSql.append("and 0=1 ");
		}
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcClass.dao.paginate(pageable, "select cc.*, office.*, major.name majorName, institute.name instituteName ", exceptSql.toString(), params.toArray());
	}
	
	
	/**
	 * 获得行政班列表(不分页)
	 * @return
	 */
	public List<CcClass> findAll() {
		StringBuilder exceptSql = new StringBuilder("select * from " + CcClass.dao.tableName + " ");
		exceptSql.append("where is_del = ? ");
		List<Object> params = Lists.newArrayList();
		
		params.add(Boolean.FALSE);
		// 删选条件
		// 增加条件，为非软删除的
		exceptSql.append("and is_del=? ");
		params.add(Boolean.FALSE);
		
		return find(exceptSql.toString(), params.toArray());
	}
	
	/**
	 * 获得学校下的所有未删除行政班
	 * 
	 * @param schoolId
	 * @return
	 */
	public List<CcClass> getClassBySchool(Long schoolId) {
		StringBuilder sql = new StringBuilder("select clazz.*, cc.grade, major.name majorName from " + tableName + " cc ");
		sql.append("inner join sys_office clazz on clazz.id = cc.id and clazz.is_del = ? ");
		sql.append("inner join sys_office major on major.id = clazz.parentid and major.is_del = ? ");
		sql.append("inner join sys_office_path sop on sop.id = clazz.id ");
		sql.append("where sop.office_ids like '," + schoolId.toString() + ",%' ");
		sql.append("and clazz." + IS_DEL_LABEL + "=?");
		return find(sql.toString(), DEL_NO, DEL_NO, DEL_NO);
	}
	
	/**
	 * 通过教学班名称获得教学班
	 * 
	 * @param names
	 * @param schoolId
	 * @return
	 */
	public List<CcClass> getClassByNames(String[] names, Long schoolId) {
		StringBuilder sql = new StringBuilder("select so.*, cc.grade from " + tableName + " cc ");
		sql.append("left join sys_office so on so.id = cc.id ");
		sql.append("left join sys_office_path sop on sop.id = so.id ");
		sql.append("where sop.office_ids like '," + schoolId.toString() + ",%' ");
		sql.append("and so.name in (" + CollectionKit.convert(names, ",", true) + ") ");
		sql.append("and so." + IS_DEL_LABEL + "=?");
		return find(sql.toString(), DEL_NO);
	}
	

	/**
	 * 返回同一个学校下的行政班
	 * @param schoolId
	 *           学校编号
	 * @param classId
	 *          行政班编号
	 * @return
	 */
	public List<CcClass> findBySchoolId(Long schoolId, Long classId) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select office1.* from " + Office.dao.tableName + " office1 ");
		sql.append("left join " + Office.dao.tableName + " office2 on office2.id = office1.parentid ");
		sql.append("left join " + Office.dao.tableName + " office3 on office3.id = office2.parentid ");
		sql.append("left join " + Office.dao.tableName + " office4 on office4.id = office3.parentid ");
		sql.append("where office4.id = ? ");
		param.add(schoolId);
		sql.append("and office1.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and office2.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and office3.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and office4.is_del = ? ");
		param.add(Boolean.FALSE);
		if(classId != null){
			sql.append("and office1.id != ? ");
			param.add(classId);
		}

		return find(sql.toString(), param.toArray());
	}


	/**
	 * 同一个学校下行政班名称是否重复
	 * @param schoolId
	 *           学校编号
	 * @param name
	 *          行政班名称
	 * @param classId 
	 *          行政班编号
	 * @return
	 */
	public boolean isExistedName(Long schoolId, String name, Long classId) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select count(1) from " + Office.dao.tableName + " class ");
		sql.append("left join " + Office.dao.tableName + " major on major.id = class.parentid ");
		sql.append("left join " + Office.dao.tableName + "  institute on institute.id = major.parentid ");
		sql.append("left join " + Office.dao.tableName + " school on school.id = institute.parentid ");
		sql.append("where school.id = ? ");
		param.add(schoolId);
		sql.append("and class.name = ? ");
		param.add(name);
		sql.append("and class.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and major.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and institute.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and school.is_del = ? ");
		param.add(Boolean.FALSE);
		if(classId != null){
			sql.append("and class.id != ? ");
			param.add(classId);
		}
		
		return Db.queryLong(sql.toString(), param.toArray()) > 0;
	}

	/**
	 * 同一个学校下行政班代码是否重复
	 * @param schoolId
	 *           学校编号
	 * @param code
	 *          行政班代码
	 * @param classId 
	 *          行政班编号
	 * @return
	 */
	public boolean isExistedCode(Long schoolId, String code, Long classId) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select count(1) from " + Office.dao.tableName + " class ");
		sql.append("left join " + Office.dao.tableName + " major on major.id = class.parentid ");
		sql.append("left join " + Office.dao.tableName + "  institute on institute.id = major.parentid ");
		sql.append("left join " + Office.dao.tableName + " school on school.id = institute.parentid ");
		sql.append("where school.id = ? ");
		param.add(schoolId);
		sql.append("and class.code = ? ");
		param.add(code);
		sql.append("and class.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and major.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and institute.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and school.is_del = ? ");
		param.add(Boolean.FALSE);
		if(classId != null){
			sql.append("and class.id != ? ");
			param.add(classId);
		}
		
		return Db.queryLong(sql.toString(), param.toArray()) > 0;
	}
	
	/**
	 * 根据部门编号获取所有相关的行政班
	 * 
	 * @param officeId
	 * @return
	 */
	public List<Office> findClassByPath(Long officeId) {
		StringBuilder sql = new StringBuilder("select id from " + Office.dao.tableName + " so ");
		sql.append("left join sys_office_path sop on sop.id = so.id ");
		sql.append("where sop.office_ids like '%," + officeId + ",%' and so.type = ? and so.is_del = ? ");
		return Office.dao.find(sql.toString(), Office.TYPE_CLAZZ, DEL_NO);
	}


	/**
	 * 某个专业不小于年级minGrade且不等于excludeGrade年级的行政班
	 * @param pageable
	 * @param majorId
	 * @param minGrade
	 * @param excludeGrade
	 * @return
	 */
	public Page<CcClass> page(Pageable pageable, Long majorId, Integer minGrade, Integer excludeGrade) {
		return page(pageable, majorId, null, minGrade, excludeGrade);
	}


	/**
	 * 某个专业某些年级的行政班
	 * @param pageable
	 * @param majorId
	 * @param gradeArray
	 * @return
	 */
	public Page<CcClass> page(Pageable pageable, Long majorId, Integer[] gradeArray) {
		return page(pageable, majorId, gradeArray, null, null);
	}
	
	/**
	 * 某个版本不在特定年级的行政班
	 * @param pageable
	 * @param grade
	 * @param majorId
	 * @param excludeGrade
	 * @return
	 */
    private Page<CcClass> page(Pageable pageable, Long majorId, Integer[] grade, Integer minGrade, Integer excludeGrade) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("from " + tableName + " cc ");
		sql.append(" left join " + Office.dao.tableName + " office on office.id = cc.id ");
		sql.append("left join " + Office.dao.tableName + " major on office.parentid = major.id ");
		sql.append("where cc.is_del = ? ");
		param.add(DEL_NO);
		sql.append("and major.id = ? ");
		param.add(majorId);
		sql.append("and major.is_del = ? ");
		param.add(DEL_NO);
		sql.append("and office.is_del = ? ");
		param.add(DEL_NO);
		if(minGrade != null){
			sql.append("and cc.grade >= ? ");
			param.add(minGrade);
			sql.append("and cc.grade != ? ");
			param.add(excludeGrade);
		}else{
			sql.append("and cc.grade in (" + CollectionKit.convert(grade, ",") + ") ");
		}
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			sql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		return CcClass.dao.paginate(pageable, "select cc.*, office.*, major.name majorName ", sql.toString(), param.toArray());
	}

	/**
	 * 找到专业下的行政班
	 * @param majorId
	 * @return
	 */
	public List<CcClass> findByMajorId(Long majorId) {
		StringBuilder sql = new StringBuilder("select cc.grade from " + CcClass.dao.tableName + " cc ");
		sql.append("inner join " + Office.dao.tableName + " so on so.id = cc.id ");
		sql.append("where so.parentid = ? and so.is_del = ? and cc.is_del = ? ");
		sql.append("group by cc.grade order by cc.grade desc ");
		return find(sql.toString(), majorId, DEL_NO, DEL_NO);
	}
}
