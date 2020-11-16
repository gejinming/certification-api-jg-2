package com.gnet.model.admin;


import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.gnet.Constant;
import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * @type model
 * @description 课程表操作，包括对数据的增删改查与列表
 * @table cc_teacher
 * @author SY
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_teacher")
public class CcTeacher extends DbModel<CcTeacher> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcTeacher dao = new CcTeacher();
	
	/**
	 * 35岁以下
	 */
	public static final String UNDERTHIRTYFIVE = "35岁以下";
	/**
	 * 36-45岁
	 */
	public static final String ABOVETHIRTYFIVEUNDERFORTYFIVE = "36-45岁";
	/**
	 * 46-60岁
	 */
	public static final String ABOVEFORTYFIVEUNDERSIXTY = "46-60岁";
	/**
	 * 60岁以上
	 */
	public static final String ABOVESIXTY = "60岁以上";
	
	/**
	 * 其他(教师没有输入出生日期无法得知年龄)
	 */
	public static final String OTHER = "其他";
	
	/**
	 * 35岁
	 */
	public static final int THIRTYFIVE = -35;
	/**
	 * 45岁
	 */
	public static final int FORTYFIVE = -45;
	/**
	 * 60岁
	 */
	public static final int SIXTY = -60;
	
	/**
	 * 最高学位为其他时的类型(默认)
	 */
	public static final Integer HIGHESTDEGREES_TYPE = 4;
	
	/**
	 * 职称为其他时的类型(默认)
	 */
	public static final Integer JOBTITLE_TYPE = 4;
	
	/**
	 * 性别为男时的类型
	 */
	public static final Integer SEX_MAN = 0;
	
	
	/**
	 * 教师工号是否存在判断
	 * 
	 * @param code
	 * @param originValue  排除值，一般用于编辑校验
	 * @param schoolId 
	 * @return
	 */
	public boolean isExisted(String code, String originValue, Long schoolId) {
		if (StrKit.notBlank(originValue)) {
			return Db.queryLong("select count(1) from cc_teacher where code = ? and code != ? and is_del = ? and school_id = ? ", code, originValue, Boolean.FALSE, schoolId) > 0;
		} else {
			return Db.queryLong("select count(1) from cc_teacher where code = ? and is_del = ? and school_id = ? ", code, Boolean.FALSE, schoolId) > 0;
		}
	}
	
	/**
	 * 
	 * 是否存在此教师工号
	 * 
	 * @description 根据教师工号查询是否存在该CcTeacher
	 * @sql select count(1) from cc_teacher where code=?
	 * @version 1.0
	 * @param code
	 * @param schoolId
	 * @return
	 */
	public boolean isExisted(String code, Long schoolId) {
		return isExisted(code, null, schoolId);
	}
	
	
	/**
	 * 查看教师列表分页
	 * @param pageable
	 * @param majorId
	 * 			专业编号
	 * @param name
	 * 			名字
	 * @param email 
	 * @param loginName 
	 * @param inDepartmentOffice 如果不为null，则只查询在该部门下的教师  
	 * @return
	 */
	public Page<CcTeacher> page(Pageable pageable, Long majorId, String name, String loginName, String email, String majorName, String code, String department, Long versionId, Office inDepartmentOffice, String roleId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder selectString = new StringBuilder("select ct.*, su.loginName , su.type, su.department, su.login_date, so.name as department_name, sur.roles roles,so.name major_name, major.name major_name, institute.name institute_name, school.name schoolName ");
		if (versionId != null) {
			selectString.append(",version_id ");
		}
		StringBuilder exceptSql = new StringBuilder("from " + CcTeacher.dao.tableName + " ct ");
		exceptSql.append("left join " + User.dao.tableName + " su on su.id = ct.id ");
		exceptSql.append("left join " + Office.dao.tableName + " so on so.id = su.department ");
		exceptSql.append("left join " + UserRole.dao.tableName + " sur on sur.user_id = su.id ");
		exceptSql.append("left join " + Office.dao.tableName + " major on major.id = ct.major_id ");
		exceptSql.append("left join " + Office.dao.tableName + " institute on institute.id = ct.institute_id " );
		exceptSql.append("left join " + Office.dao.tableName + " school on school.id = ct.school_id " );
		if (versionId != null) {
			exceptSql.append("left join " + CcMajorTeacher.dao.tableName + " cmt on cmt.teacher_id = ct.id ");
		}
		exceptSql.append("where su.loginName != '" + Constant.USER_ADMIN + "' ");
		exceptSql.append("and ct.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and su.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and so.is_del = ? ");
		params.add(Boolean.FALSE);
		if(StrKit.notBlank(roleId)){
			exceptSql.append("and sur.roles like '%" + StringEscapeUtils.escapeSql(roleId) + "%' ");
		}
		exceptSql.append("and (major.is_del = ? or major.is_del is null) ");
		params.add(Boolean.FALSE);
		exceptSql.append("and (institute.is_del = ? or institute.is_del is null) ");
		params.add(Boolean.FALSE);
		exceptSql.append("and (school.is_del = ? or school.is_del is null ) ");
		params.add(Boolean.FALSE);
		if(versionId != null){
			exceptSql.append("and cmt.version_id = ? ");
			params.add(versionId);
			exceptSql.append("and cmt.is_del = ? ");
			params.add(Boolean.FALSE);
		}
		
		// 数据权限过滤
		if (inDepartmentOffice != null) {
			String departmentType = inDepartmentOffice.getStr("type");
			if (Office.TYPE_SCHOOL.equals(departmentType)) {
				//学校
				exceptSql.append("and (ct.school_id = ?) ");
				params.add(inDepartmentOffice.get("id"));
			} else if (Office.TYPE_BRANCH.equals(departmentType)) {
				//学院
				exceptSql.append("and (ct.institute_id = ?) ");
				params.add(inDepartmentOffice.get("id"));
			} else if (Office.TYPE_MAJOR.equals(departmentType)) {
				//专业
				exceptSql.append("and (ct.major_id = ?) ");
				params.add(inDepartmentOffice.get("id"));
			}
		}
		// 删选条件
		if(majorId != null) {
			exceptSql.append("and ct.major_id like '%" + StringEscapeUtils.escapeSql(majorId.toString()) + "%' ");
		}
		if(StrKit.notBlank(name)) {
			exceptSql.append("and ct.name like '%" + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		if (StrKit.notBlank(loginName)) {
			exceptSql.append("and su.loginName like '" + org.apache.commons.lang.StringEscapeUtils.escapeSql(loginName) + "%' ");
		}
		if (StrKit.notBlank(email)) {
			exceptSql.append("and su.email like '" + StringEscapeUtils.escapeSql(email) + "%' ");
		}
		if(StrKit.notBlank(majorName)) {
			exceptSql.append("and so.name like '%" + StringEscapeUtils.escapeSql(majorName) + "%' ");
		}
		if (StrKit.notBlank(code)) {
			exceptSql.append("and ct.code like '%" + StringEscapeUtils.escapeSql(code) + "%' ");
		}
		if (StrKit.notBlank(department)) {
			exceptSql.append("and su.department like '" + org.apache.commons.lang.StringEscapeUtils.escapeSql(department) + "%' ");
		}

		
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		} else {
			exceptSql.append("order by ct.modify_date desc ");
		}
		
		return CcTeacher.dao.paginate(pageable, selectString.toString(), exceptSql.toString(), params.toArray());

	}
	
	/**
	 * 获取所有的信息
	 * @param majorId
	 * 			专业编号
	 * @param name
	 * 			名字
	 * @param email 
	 * @param loginName 
	 * @param majorIds 
	 * @return
	 */
	public List<CcTeacher> findAll(Pageable pageable, Long majorId, String name, String loginName, String email, String majorName, String code, String department, Long versionId, Long[] majorIds) {
		List<Object> params = Lists.newArrayList();
		StringBuilder exceptSql = new StringBuilder("select ct.*, su.loginName , su.type, su.department, su.login_date, so.name as department_name, sur.roles roles,so.name major_name, major.name major_name, institute.name institute_name ");
	    if (versionId != null) {
		  exceptSql.append(",version_id ");
		}
		exceptSql.append("from " + CcTeacher.dao.tableName + " ct ");
		exceptSql.append("left join " +  User.dao.tableName + " su on su.id = ct.id ");
		exceptSql.append("left join " +  Office.dao.tableName + " so on so.id = su.department ");
		exceptSql.append("left join " +  UserRole.dao.tableName + " sur on sur.user_id = su.id ");
		exceptSql.append("left join " + Office.dao.tableName + " major on major.id = ct.major_id ");
		exceptSql.append("left join " + Office.dao.tableName + " institute on institute.id = ct.institute_id " );
		if (versionId != null) {
		  exceptSql.append("left join " + CcMajorTeacher.dao.tableName + " cmt on cmt.teacher_id = ct.id ");
		}
		
		exceptSql.append("where su.loginName != '" + Constant.USER_ADMIN + "' ");
		exceptSql.append("and ct.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and su.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and so.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and (major.is_del = ? or major.is_del is null) ");
		params.add(Boolean.FALSE);
		exceptSql.append("and (institute.is_del = ? or institute.is_del is null) ");
		params.add(Boolean.FALSE);
		if(versionId != null){
			exceptSql.append("and cmt.version_id = ? ");
			params.add(versionId);
			exceptSql.append("and cmt.is_del = ? ");
			params.add(Boolean.FALSE);
		}
		
		// 数据权限过滤
		if (majorIds.length > 0) {
			exceptSql.append("and ct.major_id in (" + CollectionKit.convert(majorIds, ",") + ") ");
		} else {
			exceptSql.append("and 0=1 ");
		}
		// 删选条件
		if(majorId != null) {
			exceptSql.append("and ct.major_id like '%" + StringEscapeUtils.escapeSql(majorId.toString()) + "%' ");
		}
		if(StrKit.notBlank(name)) {
			exceptSql.append("and ct.name like '%" + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		if (StrKit.notBlank(loginName)) {
			exceptSql.append("and su.loginName like '" + org.apache.commons.lang.StringEscapeUtils.escapeSql(loginName) + "%' ");
		}
		if (StrKit.notBlank(email)) {
			exceptSql.append("and su.email like '" + StringEscapeUtils.escapeSql(email) + "%' ");
		}
		if(StrKit.notBlank(majorName)) {
			exceptSql.append("and so.name like '%" + StringEscapeUtils.escapeSql(majorName) + "%' ");
		}
		if (StrKit.notBlank(code)) {
			exceptSql.append("and ct.code like '%" + StringEscapeUtils.escapeSql(code) + "%' ");
		}
		if (StrKit.notBlank(department)) {
			exceptSql.append("and su.department like '" + org.apache.commons.lang.StringEscapeUtils.escapeSql(department) + "%' ");
		}
		
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		return find(exceptSql.toString(),params.toArray());
	}

	/**
	 * 通过教师编号获取所有数据
	 * @param teacherId
	 * @return
	 */
	public CcTeacher findAllById(Long teacherId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sb = new StringBuilder("select ct.*, su.loginName , su.type, su.department, su.login_date, so.name as department_name, sur.roles roles, major.name major_name, institute.name institute_name, school.name schoolName,school.id schoolId from " + tableName + " ct ");
		sb.append("left join " +  User.dao.tableName + " su on su.id = ct.id ");
		sb.append("left join " +  Office.dao.tableName + " so on so.id = su.department ");
		sb.append("left join " +  UserRole.dao.tableName + " sur on sur.user_id = su.id ");
		sb.append("left join " + Office.dao.tableName + " major on major.id = ct.major_id ");
		sb.append("left join " + Office.dao.tableName + " institute on institute.id = ct.institute_id " );
		sb.append("left join " + Office.dao.tableName + " school on school.id = ct.school_id ");
		sb.append("where ct.id = ? ");
		params.add(teacherId);
		sb.append("and ct.is_del = ? ");
		params.add(Boolean.FALSE);
		sb.append("and su.is_del = ? ");
		params.add(Boolean.FALSE);
		sb.append("and so.is_del = ? ");
		params.add(Boolean.FALSE);
		sb.append("and (major.is_del = ? or major.is_del is null) ");
		params.add(Boolean.FALSE);
		sb.append("and (institute.is_del = ? or institute.is_del is null) ");
		params.add(Boolean.FALSE);
		sb.append("and school.is_del = ? ");
		params.add(Boolean.FALSE);
		return findFirst(sb.toString(), params.toArray());
	}
	
	/**
	 * 根据学校编号获取学校下在职且未删除的所有教师
	 * 
	 * @param schoolId
	 * @return
	 */
	public Page<CcTeacher> page(Pageable pageable, Long schoolId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("from " + tableName + " ct ");
		sql.append("where ct.school_id = ? and ct.is_del = ? and ct.is_leave = ? ");
		params.add(schoolId);
		params.add(DEL_NO);
		params.add(DEL_NO);
		
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			sql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		return paginate(pageable, "select * ", sql.toString(), params.toArray());
	}

	/**
	 * 获取学校下学号存在的教师
	 * 
	 * @param codes
	 * 			教师的职工号列表
	 * @param schoolId
	 * 			学校编号
	 * @return
	 */
	public List<Record> existedTeachers(String[] codes, Long schoolId) {
		StringBuilder sql = new StringBuilder("select ct.* from " + tableName + " ct ");
		sql.append("where ct.school_id = ? ");
		sql.append("and ct.code in (" + CollectionKit.convert(codes, ",", true) + ") ");
		sql.append("and ct." + IS_DEL_LABEL + "=?");
		return Db.find(sql.toString(), schoolId, DEL_NO);
	}

	
	/**
	 * 统计同一专业下各个职称的人数
	 * @param majorId
	 * @return
	 */
	public List<CcTeacher> getDifferentJobTeacherNum(Long majorId) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select count(1) number, ct.job_title jobTitle from " + tableName + " ct ");
		sql.append("where ct.major_id = ? ");
		param.add(majorId);
		sql.append("and ct.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ct.is_leave = ? ");
		param.add(Boolean.FALSE);
		sql.append("group by ct.job_title ");
		return find(sql.toString(), param.toArray());
	}
	
	/**
	 * 某个专业下未离职的教师总数
	 * @param majorId
	 * @return
	 */
	public Long getTeacherNum(Long majorId) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select count(1) from " + tableName + " ct ");
		sql.append("where ct.major_id = ? ");
		param.add(majorId);
		sql.append("and ct.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ct.is_leave = ? ");
		param.add(Boolean.FALSE);
		return Db.queryLong(sql.toString(), param.toArray());
	}

	/**
	 * 统计同一专业下各个学位的人数
	 * @param majorId
	 * @return
	 */
	public List<CcTeacher> getDifferentHighestDegreesTeacherNum(Long majorId) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select count(1) number, ct.highest_degrees highestDegrees from " + tableName + " ct ");
		sql.append("where ct.major_id = ? ");
		param.add(majorId);
		sql.append("and ct.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ct.is_leave = ? ");
		param.add(Boolean.FALSE);
		sql.append("group by ct.highest_degrees ");
		return find(sql.toString(), param.toArray());
	}
	
	
	/**
	 * 没有出生日期的教师数量
	 * @param majorId
	 * @return
	 */
	public Long getBirthdayIsNullNum(Long majorId){
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select count(1) from " + tableName + " ct ");
		sql.append("where ct.major_id = ? ");
		param.add(majorId);
		sql.append("and ct.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ct.is_leave = ? ");
		param.add(Boolean.FALSE);
		sql.append("and birthday is null ");
		return Db.queryLong(sql.toString(), param.toArray());
	}
	
	
	
	/**
	 * 返回某个年龄区间的教师数量
	 * @param bigDate
	 *         大一点日期
	 * @param smallerDate
	 *         小一点日期  
	 * @param majorId
	 *         专业编号
	 * @return
	 */
	public Long getSectionNumber(Date bigDate, Date smallerDate, Long majorId) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select count(1) from " + tableName + " ct " );
		sql.append("where ct.major_id = ? ");
		param.add(majorId);
		if(smallerDate != null){
			sql.append("and ct.birthday >= ? ");
			param.add(smallerDate);
		}
		if(bigDate != null){
			sql.append("and ct.birthday < ? ");
			param.add(bigDate);
		}
		sql.append("and ct.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ct.is_leave = ? ");
		param.add(Boolean.FALSE);
		return Db.queryLong(sql.toString(), param.toArray());
	}

	/**
	 * 查看专业教师联系列表分页 
	 * @param schoolId 
	 * @param versionId 
	 * 
	 * @return
	 */
	public Page<CcTeacher> page(Pageable pageable, Long versionId, Long schoolId, Long majorId, String code, String name, List<Long> officeIds) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("from " + CcTeacher.dao.tableName + " ct ");
		sql.append("inner join " + User.dao.tableName + " su on su.id = ct.id and su.is_del = ? ");
		sql.append("left join " + CcMajorTeacher.dao.tableName + " cmt  on ct.id = cmt.teacher_id and cmt.version_id = ? ");
		sql.append("left join " + Office.dao.tableName + " major on major.id = ct.major_id ");
		sql.append("left join " + Office.dao.tableName + " institute on institute.id = ct.institute_id ");
		sql.append("where ct.is_del = ? and (ct.major_id != ? or ct.major_id is null) ");
		sql.append("and (major.is_del = ? or major.is_del is null) ");
		sql.append("and (institute.is_del = ? or institute.is_del is null) ");
		sql.append("and ct.school_id = ? ");
		// 教师工号查询
		if (StrKit.notBlank(code)) {
			sql.append("and ct.code like '" + StringEscapeUtils.escapeSql(code) + "%' ");
		}
		
		// 教师名称查询
		if (StrKit.notBlank(name)) {
			sql.append("and ct.name like '" + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		
		// 子部门编号
		if (officeIds != null && !officeIds.isEmpty()) {
			sql.append("and su.department in (" + CollectionKit.convert(officeIds, ",") + ") ");
		}

		sql.append("group by ct.id, cmt.is_del ");
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			sql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}else{
			sql.append("order by ct.id ");
		}
		
		params.add(false);
		params.add(versionId);
		params.add(false);
		params.add(majorId);
		params.add(false);
		params.add(false);
		params.add(schoolId);
		   
		return CcTeacher.dao.paginate(pageable, "select ct.*, major.name majorName, institute.name instituteName, cmt.is_del isMajorTeacher ", sql.toString(), params.toArray());
	}
	
	/**
	 * 返回专业或学院下的教师
	 * @param pageable
	 * @param majorId
	 * @param instituteId
	 * @param officeId 
	 * @param name 
	 * @param code 
	 * @return
	 */
	public Page<CcTeacher> page(Pageable pageable, Long majorId, Long instituteId, Long officeId, String code, String name) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("from " + CcTeacher.dao.tableName + " ct ");
		sql.append("left join " + Office.dao.tableName + " major on major.id = ct.major_id and (major.is_del = ? or major.is_del is null) ");
		param.add(DEL_NO);
		sql.append("left join " + Office.dao.tableName + " institute on institute.id = ct.institute_id and (institute.is_del = ? or institute.is_del is null) ");
		param.add(DEL_NO);
		/*
		 * #5421 按照张淑峰要求，增加user表关联，增加office表关联，然后按照当前用户的部门id来处理
		 */
		sql.append("left join " + User.dao.tableName + " cu on cu.id = ct.id and cu.is_del = ? ");
		param.add(DEL_NO);
		sql.append("left join " + Office.dao.tableName + " office on office.id = cu.department and office.is_del = ? ");
		param.add(DEL_NO);
		sql.append("left join " + OfficePath.dao.tableName + " officePath on office.id = officePath.id ");
		
		sql.append("where ct.is_del = ? ");
		param.add(DEL_NO);
		if(majorId != null){
			sql.append("and ct.major_id = ? ");
			param.add(majorId);
		}
		if(instituteId != null){
			sql.append("and ct.institute_id = ? ");
			param.add(instituteId);
		}
		if(StrKit.notBlank(name)){
			sql.append("and ct.name like '%" + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		if(StrKit.notBlank(code)){
			sql.append("and ct.code like '%" + StringEscapeUtils.escapeSql(code) + "%' ");
		}
		if(majorId == null && instituteId == null) {
			sql.append("and officePath.office_ids like '%,"+ StringEscapeUtils.escapeSql(officeId.toString()) +",%' ");
		}
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			sql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		} else {
			sql.append("order by ct.name desc ");
		}
		
		return CcTeacher.dao.paginate(pageable, "select ct.*, major.name majorName, institute.name instituteName ", sql.toString(), param.toArray());
	}

	/**
	 * 某个部门下已经是专业认证教师的教师列表
	 * @param pageable
	 * @param majorId
	 * @param name
	 * 			教师名字
	 * @param majorName
	 * 			专业名称
	 * @param code
	 * 			教师职工号
	 * @param inDepartmentOffice
	 * 			查询这个部门及以下数据
	 * @param majorIds
	 * 			上述部门（inDepartmentOffice）下的专业编号列表，如果上述部门是专业，则此处为空
	 * @return
	 * @author SY 
	 * @version 编辑时间：2016年11月29日 下午3:50:48（只是修改了注释，其他没变） 
	 */
	public Page<CcTeacher> page(Pageable pageable, Long majorId, String name, String majorName, String code,  Office inDepartmentOffice, Long[] majorIds) {
		List<Object> params = Lists.newArrayList();
		StringBuilder selectString = new StringBuilder("select ct.*, major.name major_name, institute.name institute_name, school.name schoolName ");
		StringBuilder exceptSql = new StringBuilder("from " + CcTeacher.dao.tableName + " ct ");
		exceptSql.append("left join " + Office.dao.tableName + " major on major.id = ct.major_id ");
		exceptSql.append("left join " + Office.dao.tableName + " institute on institute.id = ct.institute_id " );
		exceptSql.append("left join " + Office.dao.tableName + " school on school.id = ct.school_id ");
		exceptSql.append("left join " + CcMajorTeacher.dao.tableName + " cmt on cmt.teacher_id = ct.id and cmt.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("left join " + CcVersion.dao.tableName + " cv on cmt.version_id = cv.id and cv.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("where ct.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and (major.is_del = ? or major.is_del is null) ");
		params.add(Boolean.FALSE);
		exceptSql.append("and (institute.is_del = ? or institute.is_del is null) ");
		params.add(Boolean.FALSE);
		exceptSql.append("and (school.is_del = ? or school.is_del is null ) ");
		params.add(Boolean.FALSE);
		
		// 数据权限过滤
		if (inDepartmentOffice != null) {
			String departmentType = inDepartmentOffice.getStr("type");
			if (Office.TYPE_SCHOOL.equals(departmentType)) {
				//学校
				exceptSql.append("and (ct.school_id = ?) ");
				params.add(inDepartmentOffice.get("id"));
			} else if (Office.TYPE_BRANCH.equals(departmentType)) {
				if(majorIds == null || majorIds.length == 0){
					//学院
					exceptSql.append("and (ct.institute_id = ?) ");
					params.add(inDepartmentOffice.get("id"));
				}else{
					//学院
					exceptSql.append("and (");
						exceptSql.append("(ct.institute_id = ?)");
						params.add(inDepartmentOffice.get("id"));
						exceptSql.append("or ( false ");
						for(Long id : majorIds) {
							exceptSql.append("or (( cv.major_id = ? AND cmt.is_del = ? ) or ( ct.major_id = ? and (cmt.is_del = ? or cmt.is_del is null)))");
							params.add(id);
							params.add(Boolean.FALSE);
							params.add(id);
							params.add(Boolean.FALSE);
						}
						exceptSql.append(")");
					exceptSql.append(")");
				}
	
			} else if (Office.TYPE_MAJOR.equals(departmentType)) {
				if(majorIds == null || majorIds.length == 0){
					//专业
					/*	从
					 *  exceptSql.append("and (((cv.major_id != ? or cv.major_id is null) and cmt.is_del = ? ) or ( ct.major_id = ? and (cmt.is_del = ? or cmt.is_del is null)))");
					 *  变化过来。
					 *  因为发现上述代码导致，只能查询出专业下教师，所以废弃
					 */
					exceptSql.append("and (( cv.major_id = ? AND cmt.is_del = ? ) or ( ct.major_id = ? and (cmt.is_del = ? or cmt.is_del is null)))");
					params.add(inDepartmentOffice.get("id"));
					params.add(Boolean.FALSE);
					params.add(inDepartmentOffice.get("id"));
					params.add(Boolean.FALSE);
				}else{
					exceptSql.append("and (");
					exceptSql.append("(( cv.major_id = ? AND cmt.is_del = ? ) or ( ct.major_id = ? and (cmt.is_del = ? or cmt.is_del is null)))");
						params.add(inDepartmentOffice.get("id"));
						params.add(Boolean.FALSE);
						params.add(inDepartmentOffice.get("id"));
						params.add(Boolean.FALSE);
						exceptSql.append("or ( false ");
						for(Long id : majorIds) {
							exceptSql.append("or (( cv.major_id = ? AND cmt.is_del = ? ) or ( ct.major_id = ? and (cmt.is_del = ? or cmt.is_del is null)))");
							params.add(id);
							params.add(Boolean.FALSE);
							params.add(id);
							params.add(Boolean.FALSE);
						}
						exceptSql.append(")");
					exceptSql.append(")");
				}

			}
		}
		// 删选条件
		if(majorId != null) {
			exceptSql.append("and ct.major_id = ? ");
			params.add(majorId);
		}
		if(StrKit.notBlank(name)) {
			exceptSql.append("and ct.name like '%" + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		if(StrKit.notBlank(majorName)) {
			exceptSql.append("and major.name like '%" + StringEscapeUtils.escapeSql(majorName) + "%' ");
		}
		if (StrKit.notBlank(code)) {
			exceptSql.append("and ct.code like '%" + StringEscapeUtils.escapeSql(code) + "%' ");
		}
		
		exceptSql.append("group by ct.id ");
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcTeacher.dao.paginate(pageable, selectString.toString(), exceptSql.toString(), params.toArray());
	}

	/**
	 * 根据教师编号和专业编号判断教师是否存在与专业下
	 * 
	 * @param teacherId
	 * @param majorId
	 * @return
	 */
	public boolean isExistedTeacherUnderMajorById(Long teacherId, Long majorId) {
		return Db.queryLong("select count(1) from " + tableName + " where id = ? and major_id = ? and is_del = ?", teacherId, majorId, DEL_NO) > 0;
	}

	public List<CcTeacher> findNameTeacher(String name,Long schoolId){
		StringBuilder sql = new StringBuilder("select * from " + tableName + " where code=? and is_del=0 and school_id=?");
		return find(sql.toString(),name,schoolId);
	}

}
