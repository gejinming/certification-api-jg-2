package com.gnet.model.admin;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

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
 * 学生信息表
 * 
 * @author wct
 * @Date 2016年6月29日
 */
@TableBind(tableName = "cc_student")
public class CcStudent extends DbModel<CcStudent> {

	private static final long serialVersionUID = -2187101643295882356L;
	
	public static final CcStudent dao = new CcStudent();
	

	/**
	 * 获得学生列表（分页）
	 * 
	 * @param pageable
	 * @param name
	 * @param studentNo
	 * @param idCard
	 * @param className
	 * @param statue
	 * @param majorIds 
	 * @param classId 
	 * @param grade 
	 * @param majorId 
	 * @return
	 */
	public Page<CcStudent> page(Pageable pageable, String name, String studentNo, String idCard, String className, Integer statue, Long[] majorIds, Long majorId, Integer grade, Long classId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("from " + CcStudent.dao.tableName + " cs ");
		sql.append("left join " +  CcClass.dao.tableName + " cc on cc.id = cs.class_id ");
		sql.append("left join " + Office.dao.tableName + " clazz on clazz.id = cc.id ");
		sql.append("left join " + Office.dao.tableName +  " major on major.id = clazz.parentid ");
		sql.append("where cs.is_del=? ");
		params.add(CcStudent.DEL_NO);	
		
		if(classId != null){
			sql.append("and cc.id = ? ");
			params.add(classId);
		}
		
		if(grade != null){
			sql.append("and cc.grade = ? ");
			params.add(grade);
		}
		
		if(majorId != null){
			sql.append("and clazz.parentid = ? ");
			params.add(majorId);
		}else if(majorIds.length > 0) {
			sql.append("and clazz.parentid in (" + CollectionKit.convert(majorIds, ",") + ") ");
		}
		// 行政班查询
		if (StrKit.notBlank(className)) {
			sql.append("and clazz.name like '%" + StringEscapeUtils.escapeSql(className) + "%' ");
		}
		// 学生姓名查询
		if (StrKit.notBlank(name)) {
			sql.append("and cs.name like '%" + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		// 学生学号查询
		if (StrKit.notBlank(studentNo)) {
			sql.append("and cs.student_no like '%" + StringEscapeUtils.escapeSql(studentNo) + "%' ");
		}
		// 学生身份证号查询
		if (StrKit.notBlank(idCard)) {
			sql.append("and cs.id_card like '%" + StringEscapeUtils.escapeSql(idCard) + "%' ");
		}
		// 学生学籍状态查询
		if (statue != null) {
			sql.append("and cs.statue=? ");
			params.add(statue);
		}
		
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			sql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcStudent.dao.paginate(pageable, "select cs.*, clazz.name className, major.id major_id, major.name major_name ", sql.toString(), params.toArray());
	}

	/**
	 * 获取学校下学号存在的学生
	 * 
	 * @param studentNos
	 * @param schoolId
	 * @return
	 */
	public List<Record> existedStudents(String[] studentNos, Long schoolId) {
		StringBuilder sql = new StringBuilder("select cs.*, so.name class_name from " + tableName + " cs ");
		sql.append("left join sys_office so on so.id = cs.class_id ");
		sql.append("left join sys_office_path sop on sop.id = so.id ");
		sql.append("where sop.office_ids like '," + schoolId.toString() + "%' ");
		sql.append("and cs.student_no in (" + CollectionKit.convert(studentNos, ",", true) + ") ");
		sql.append("and cs." + IS_DEL_LABEL + "=?");
		return Db.find(sql.toString(), DEL_NO);
	}
	
	/**
	 * 学号是否存在判断
	 * 
	 * @param studentNo
	 * @param originValue  排除值，一般用于编辑校验
	 * @return
	 */
	public boolean isExisted(String studentNo, String originValue, Long schoolId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select count(1) from cc_student cs ");
		sql.append("left join sys_office so on so.id = cs.class_id ");
		sql.append("left join sys_office_path sop on sop.id = so.id ");
		sql.append("where cs.student_no = ? and cs.is_del = ? ");
		params.add(studentNo);
		params.add(DEL_NO);
		if (schoolId != null) {
			sql.append("and sop.office_ids like '," + schoolId.toString() + "%' ");
		}
		if (StrKit.notBlank(originValue)) {
			sql.append("and cs.student_no != ?");
			params.add(originValue);
		}
		return Db.queryLong(sql.toString(), params.toArray()) > 0;
	}
	
	/**
	 * 
	 * 是否存在此学号
	 * 
	 * @description 根据学号查询是否存在该CcStudent
	 * @sql select count(1) from cc_student where studentNo=?
	 * @version 1.0
	 * @param studentNo
	 * @return
	 */
	public boolean isExisted(String studentNo, Long schoolId) {
		return isExisted(studentNo, null, schoolId);
	}

	/**
	 * 获得学生列表（分页）
	 * 
	 * @param classId
	 * @return
	 */
	public Page<CcStudent> page(Pageable pageable, Long classId, String name, String studentNo) {
		
		List<Object> params = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("from " + CcStudent.dao.tableName + " cs ");
		sql.append("left join " + CcEduclassStudent.dao.tableName + " ces on ces.student_id = cs.id " );
		sql.append("left join " + Office.dao.tableName + " clazz on clazz.id = cs.class_id ");
		sql.append("left join " + Office.dao.tableName + " major on major.id = clazz.parentid ");
		sql.append("where ces.class_id = ? and cs.is_del = ? and ces.is_del = ? and clazz.is_del = ? and major.is_del = ? ");
		params.add(classId);
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		
		// 学生姓名查询
		if (StrKit.notBlank(name)) {
			sql.append("and cs.name like '%" + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		// 学生学号查询
		if (StrKit.notBlank(studentNo)) {
			sql.append("and cs.student_no like '%" + StringEscapeUtils.escapeSql(studentNo) + "%' ");
		}
		
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			sql.append("order by cs." + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		} else {
			sql.append("order by cs.student_no ");
		}
		
		return CcStudent.dao.paginate(pageable, "select cs.*, ces.id classStudentId, ces.is_caculate isCaculate, clazz.name className, clazz.id classId, major.name majorName, major.id majorId ", sql.toString(), params.toArray());
	}

	public List<CcStudent> findByClassIds(Long[] classIdsArray) {
		String sql = "select * from " + tableName + " where class_id in (" + CollectionKit.convert(classIdsArray, ",") + ")";
		return find(sql);
	}
	
	/**
	 * 根据学生列表获得所有学生
	 * 
	 * @param studentsNo
	 * @param schoolId
	 * @return
	 */
	public List<CcStudent> findByStudentsNo(String[] studentsNo, Long schoolId) {
		StringBuilder sql = new StringBuilder("select cs.* from " + tableName + " cs ");
		sql.append("left join cc_educlass_student ces on ces.student_id = cs.id ");
		sql.append("left join sys_office_path sop on sop.id = cs.class_id ");
		sql.append("where cs.student_no in (" + CollectionKit.convert(studentsNo, ",", true) + ") ");
		sql.append("and cs.is_del=? and sop.office_ids like '," + schoolId.toString() + "%' ");
		return CcStudent.dao.find(sql.toString(), DEL_NO);
	}


	/**
	 * 某个学校下所有的学生
	 * @param schoolId
	 * @return
	 */
    public List<CcStudent> findBySchoolId(Long schoolId) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select cs.id studentId, cs.name, cs.student_no studentNo, class.name className, major.name majorName from " + tableName + " cs ");
        sql.append("inner join " + Office.dao.tableName + " class on class.id = cs.class_id and class.is_del = ? ");
        param.add(DEL_NO);
        sql.append("inner join " + Office.dao.tableName + " major on major.id = class.parentid and major.is_del = ? ");
        param.add(DEL_NO);
		sql.append("inner join sys_office_path sop on sop.id = cs.class_id ");
		sql.append("where cs.is_del = ? and sop.office_ids like '," + schoolId.toString() + "%' ");
		param.add(DEL_NO);
        return find(sql.toString(), param.toArray());

	}
	
	/**
	 * 某个教学班下的学生
	 * @param educlassId
	 *           教学班编号
	 * @return
	 */
	public List<CcStudent> findByEduclassId(Long educlassId) {
		StringBuffer sql = new StringBuffer("select cs.* from " + tableName + " cs ");
		sql.append("left join " + CcEduclassStudent.dao.tableName + " ces on ces.student_id = cs.id ");
		sql.append("where ces.class_id = ? and cs.is_del = ? and ces.is_del = ? ");
		return find(sql.toString(), educlassId, CcStudent.DEL_NO, Boolean.FALSE);
	}
	
	/**
	 * 某个教学班下的学生
	 * @param educlassId
	 *           教学班编号
	 * @author SY
	 * @return
	 * @date 2017年10月27日
	 */
	public List<CcStudent> findByEduclassIdOrderByStudentNo(Long educlassId) {
		StringBuffer sql = new StringBuffer("select cs.* from " + tableName + " cs ");
		sql.append("left join " + CcEduclassStudent.dao.tableName + " ces on ces.student_id = cs.id ");
		sql.append("where ces.class_id = ? and cs.is_del = ? and ces.is_del = ? ");
		sql.append("order by cs.student_no asc ");
		return find(sql.toString(), educlassId, CcStudent.DEL_NO, Boolean.FALSE);
	}
	
	/**
	 * 获得学生信息（包括专业方向信息）
	 * @param studentId 学生编号
	 * @param versionid 培养计划版本编号
	 * @return
	 */
	public CcStudent findByIdWithMajorDirection(Long studentId, Long versionId) {
		StringBuilder sql = new StringBuilder("select cs.*, cms.major_direction_id major_direction_id from " + tableName + " cs ");
		sql.append("left join cc_major_student cms on cms.version_id = ? and cms.student_id = cs.id ");
		sql.append("where cs.id = ? and cs.is_del = ? ");
		return findFirst(sql.toString(), versionId, studentId, DEL_NO);
	}

	/**
	 * 返回在某个行政班不在某个教学班的学生列表信息接口
	 * @param pageable
	 * @param classId
	 *         行政班编号
	 * @param eduClassId
	 *         教学班编号
	 * @param name
	 * @param studentNo
	 * @return
	 * @author SY
	 * @date 2016年10月29日18:51:26
	 */
	public Page<CcStudent> pageInClassIdNotEduClassId(Pageable pageable, Long classId, Long eduClassId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder selectString = new StringBuilder("select cs.*, clazz.name className ");
		StringBuilder sql = new StringBuilder(" from " + tableName + " cs ");
		sql.append("left join " + Office.dao.tableName + " clazz on (clazz.id = cs.class_id and clazz.is_del = ?) ");
		params.add(DEL_NO);
		sql.append("left join " + CcEduclassStudent.dao.tableName + " ces on (ces.student_id = cs.id and ces.is_del = ? and ces.class_id = ? ) ");
		params.add(DEL_NO);
		params.add(eduClassId);
		
		sql.append("where cs.class_id = ? and cs.is_del = ? ");
		params.add(classId);
		params.add(DEL_NO);
		// 不属于这个
		sql.append("and ces.id is null ");
		
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			sql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		return CcStudent.dao.paginate(pageable, selectString.toString(), sql.toString(), params.toArray());
	}
	
	/**
	 * 通过行政班编号和教学班编号获取不在相同课程、相同学期排课课程教学班中的指定行政班的学生
	 * @param pageable
	 * @param classId
	 * @param educlassIds
	 * @return
	 */
	public Page<CcStudent> pageInClassIdNotSameTermTeacherCourse(Pageable pageable, Long classId, Long[] educlassIds) {
		List<Object> params = Lists.newArrayList();
		StringBuilder selectString = new StringBuilder("select cs.*, clazz.name className ");
		StringBuilder sql = new StringBuilder(" from " + tableName + " cs ");
		sql.append("left join " + Office.dao.tableName + " clazz on (clazz.id = cs.class_id and clazz.is_del = ?) ");
		params.add(DEL_NO);
		sql.append("left join " + CcEduclassStudent.dao.tableName + " ces on (ces.student_id = cs.id and ces.is_del = ? and ces.class_id in (" +  CollectionKit.convert(educlassIds, ",") + " ) ) ");
		params.add(DEL_NO);
		
		sql.append("where cs.class_id = ? and cs.is_del = ? ");
		params.add(classId);
		params.add(DEL_NO);
		// 不属于这个
		sql.append("and ces.id is null ");
		
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			sql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		return CcStudent.dao.paginate(pageable, selectString.toString(), sql.toString(), params.toArray());
	}
	
	/**
	 * 返回某个行政班学生信息列表
	 * @param pageable
	 * @param classId
	 *         行政班编号
	 * @param name
	 * @param studentNo
	 * @return
	 */
	public Page<CcStudent> findDetailByClassId(Pageable pageable, Long classId, String name, String studentNo) {
		List<Object> params = Lists.newArrayList();
		StringBuffer selectString = new StringBuffer("select cs.*, clazz.name className ");
		StringBuilder sql = new StringBuilder("from " + tableName + " cs ");
		sql.append("left join " + Office.dao.tableName + " clazz on (clazz.id = cs.class_id and clazz.is_del = ?) ");
		params.add(DEL_NO);
		sql.append("where cs.class_id = ? and cs.is_del = ? ");
		params.add(classId);
		params.add(DEL_NO);
		// 学生姓名查询
		if (StrKit.notBlank(name)) {
			sql.append("and cs.name like '%" + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		// 学生学号查询
		if (StrKit.notBlank(studentNo)) {
			sql.append("and cs.student_no like '%" + StringEscapeUtils.escapeSql(studentNo) + "%' ");
		}
		
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			sql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		return CcStudent.dao.paginate(pageable, selectString.toString(), sql.toString(), params.toArray());
	}

	/**
	 * 学生信息包含行政班信息
	 * @param id
	 *         学生编号
	 * @return
	 */
	public CcStudent findDetailById(Long id) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select cs.*, so.name className,sop.office_ids from " + tableName + " cs ");
		sql.append("left join " + Office.dao.tableName + " so on so.id = cs.class_id ");
		sql.append("left join sys_office_path sop on sop.id = so.id ");
		sql.append("where cs.id = ? ");
		param.add(id);
		sql.append("and cs.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and so.is_del = ? ");
		param.add(Boolean.FALSE);
		
		return findFirst(sql.toString(), param.toArray());
	}

	/**
	 * 通过专业编号查找学生年级列表
	 * @param majorId
	 * @return
	 */
	public List<CcStudent> findGradeListByMajorId(Long majorId) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select cs.grade from " + tableName + " cs ");
	    sql.append("left join " + Office.dao.tableName + " class on class.id = cs.class_id ");
	    sql.append("left join " + Office.dao.tableName + " major on major.id = class.parentid ");
	    sql.append("where major.id = ? ");
	    param.add(majorId);
	    sql.append("and cs.is_del = ? ");
	    param.add(Boolean.FALSE);
	    sql.append("and class.is_del = ? ");
	    param.add(Boolean.FALSE);
	    sql.append("and major.is_del = ? ");
	    param.add(Boolean.FALSE);
	    sql.append("group by cs.grade ");
		return find(sql.toString(), param.toArray());
	}
	/**
	 * 学生登录验证
	 *
	 * @param studentNos
	 * @param schoolId
	 * @return
	 */
	public CcStudent existedStudents(String studentNos, String password,String schoolId) {
		List<Object> param = Lists.newArrayList();

		StringBuilder sql = new StringBuilder("select cs.*, so.name class_name from " + tableName + " cs ");
		sql.append("left join sys_office so on so.id = cs.class_id ");
		sql.append("left join sys_office_path sop on sop.id = so.id ");
		sql.append("where sop.office_ids like '," + schoolId + "%' ");
		sql.append("and cs.student_no = ? and password=? and cs.is_del=? and statue=1 ");
		param.add(studentNos);
		param.add(password);
		param.add(Boolean.FALSE);
		return findFirst(sql.toString(), param.toArray());
	}
	/*
	 * @param edclassId
	 * @return java.util.List<com.gnet.model.admin.CcStudent>
	 * @author Gejm
	 * @description: 根据教学班id查询其中学生所属的行政班级名称
	 * @date 2020/8/17 11:01
	 */
	public List<CcStudent> findClassName(Long edclassId){
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("SELECT clazz.NAME className FROM cc_student cs ");
		sql.append("INNER JOIN cc_class cc ON cc.id = cs.class_id and cc.is_del=0 ");
		sql.append("INNER JOIN sys_office clazz ON clazz.id = cc.id and clazz.is_del=0 ");
		sql.append("INNER JOIN sys_office major ON major.id = clazz.parentid and   major.is_del=0 ");
		sql.append("INNER JOIN cc_educlass_student ces on ces.student_id=cs.id and ces.is_del=0 ");
		sql.append("WHERE cs.is_del =0 and ces.class_id=? ");
		param.add(edclassId);
		sql.append("group by clazz.NAME order by  clazz.NAME ");
		return find(sql.toString(), param.toArray());
	}

}