package com.gnet.model.admin;

import java.util.ArrayList;
import java.util.List;

import com.gnet.utils.CollectionKit;
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
 * @type model
 * @description 教师开课课程表操作，包括对数据的增删改查与列表
 * @table cc_teacher_course
 * @author SY
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_teacher_course")
public class CcTeacherCourse extends DbModel<CcTeacherCourse> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcTeacherCourse dao = new CcTeacherCourse();
	
	/**
	 * 达成度计算类型：考核分析法
	 */
	public static final Integer RESULT_TYPE_SCORE = 1;
	/**
	 * 达成度计算类型：评分表分析法
	 */
	public static final Integer RESULT_TYPE_EVALUATE = 2;
	/**
	 * 达成度计算类型：财经大学考核成绩分析法
	 */
	public static final Integer RESULT_TYPE_SCORE2 = 3;

	/**
	 * 达成度计算类型：考核成绩分析法
	 */
	public static final String TYPE_SCORE = "考核成绩分析法";
	/**
	 * 达成度计算类型：考核成绩分析法
	 */
	public static final String TYPE_SCORE2 = "财经大学考核成绩分析法";

	/**
	 * 达成度计算类型：评分表成绩分析法
	 */
    public static final String TYPE_EVALUATE = "评分表分析法";

	/**
	 * 验证教师+课程+学期+年级 不能重复
	 * @param courseId
	 * @param teacherId
	 * @param termId
	 * @param grade
	 * @param excludeTeacherCourseId
	 *        教师开课课程本身编号
	 * @return
	 */
	public boolean isExisted(Long courseId, Long teacherId, Long termId, Integer grade, Long excludeTeacherCourseId){
		if(excludeTeacherCourseId != null){
			return Db.queryLong("select count(1) from " + tableName + " where course_id = ? and teacher_id = ? and term_id = ? and grade = ? and id != ? and is_del = ? ", courseId, teacherId, termId, grade, excludeTeacherCourseId, Boolean.FALSE) > 0;
		}else{
			return Db.queryLong("select count(1) from " + tableName + " where course_id = ? and teacher_id = ? and term_id = ? and grade = ? and is_del = ? ", courseId, teacherId, termId, grade, Boolean.FALSE) > 0;
		}
	}

	/**
	 * 是否存在分享人
	 * @param courseId
	 * @param termId
	 * 			学期编号
	 * @param grade
	 * 			年级
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年10月19日 下午2:53:17 
	 */
	public Boolean isExistSharer(Long courseId, Long termId, Integer grade) {
		List<Object> params = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		sb.append("select count(1) from " + tableName + " ctc ");
		sb.append("where ctc.course_id = ? ");
		params.add(courseId);
		sb.append("and ctc.term_id = ? ");
		params.add(termId);	
		sb.append("and ctc.grade = ? ");
		params.add(grade);	
		sb.append("and ctc.is_sharer = ? ");
		params.add(Boolean.TRUE);	
		sb.append("and ctc.is_del = ? ");
		params.add(Boolean.FALSE);
		
		return Db.queryLong(sb.toString(), params.toArray()) > 0;
	}
	
	/**
	 * 获得教师开课课程列表(不分页)
	 * @param pageable 
	 * @param courseId
	 * @param teacherId
	 * @param termId
	 * @param courseName 
	 * @param teacherName 
	 * @param planId 
	 * @return
	 */
	public List<CcTeacherCourse> findAll(Pageable pageable, Long courseId, Long teacherId, Long termId, Long planId, String teacherName, String courseName) {
		StringBuilder exceptSql = new StringBuilder("select ctc.*, ct.name as teacher_name, cc.name as course_name, ctm.start_year term_start_year, ctm.end_year term_end_year, ctm.term term_num, ctm.term_type term_type, cc.plan_id plan_id, so.name major_name from " + CcTeacherCourse.dao.tableName + " ctc ");
		exceptSql.append("left join " + CcTeacher.dao.tableName + " ct on ct.id=ctc.teacher_id ");
		exceptSql.append("left join " + CcCourse.dao.tableName + " cc on cc.id=ctc.course_id ");
		exceptSql.append("inner join " + CcVersion.dao.tableName + " cv on cv.id = cc.plan_id ");
		exceptSql.append("inner join " + Office.dao.tableName + " so on so.id = cv.major_id ");
		exceptSql.append("left join " + CcTerm.dao.tableName + " ctm on ctm.id=ctc.term_id ");
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append("where ctc.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cc.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and ct.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and ctm.is_del = ? ");
		params.add(Boolean.FALSE);
		// 可选择条件
		if (courseId != null) {
			exceptSql.append("and ctc.course_id = ? ");
			params.add(courseId);
		}
		
		if (planId != null) {
			exceptSql.append("and cc.plan_id = ? ");
			params.add(planId);
		}
		
		if (teacherId != null) {
			exceptSql.append("and ctc.teacher_id = ? ");
			params.add(teacherId);
		}
		if (termId != null) {
			exceptSql.append("and ctc.term_id = ? ");
			params.add(termId);
		}
		if (StrKit.notBlank(courseName)) {
			exceptSql.append("and cc.name like '" + StringEscapeUtils.escapeSql(courseName) + "%' ");
		}
		if (StrKit.notBlank(teacherName)) {
			exceptSql.append("and ct.name like '" + StringEscapeUtils.escapeSql(teacherName) + "%' ");
		}
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return find(exceptSql.toString(), params.toArray());
	}
	
	/**
	 * 通过教学班获得教师开课课程
	 * 
	 * @param classId
	 * @return
	 */
	public CcTeacherCourse findCourseByClassId(Long classId) {
		StringBuilder sql = new StringBuilder("select ctc.*, cc.plan_id version_id,ce.educlass_name,cc.name,ct.start_year,ct.end_year  from " + tableName + " ctc ");
		sql.append("inner join cc_course cc on cc.id = ctc.course_id ");
		sql.append("left join cc_educlass ce on ce.teacher_course_id = ctc.id ");
		sql.append("left join cc_term ct on ct.id =ctc.term_id ");
		sql.append("where ce.id = ? and ctc.is_del = ?");
		return findFirst(sql.toString(), classId, DEL_NO);
	}

	/**
	 * 教师查看个人课程列表(分页)
	 * @param pageable
	 * @param teacherId
	 * @param name
	 * @return
	 */
	public Page<CcTeacherCourse> page(Pageable pageable, Long teacherId, String name) {
		StringBuilder exceptSql = new StringBuilder(" from " + CcTeacherCourse.dao.tableName + " ctc ");	
		List<Object> params = Lists.newArrayList();
		exceptSql.append("left join " + CcCourse.dao.tableName + " cc on cc.id = ctc.course_id ");
		exceptSql.append("left join " + CcCourseModule.dao.tableName + "  ccm on ccm.id = cc.module_id ");
		exceptSql.append("left join " + CcCourseHierarchy.dao.tableName + " cch on  cch.id = cc.hierarchy_id ");
		exceptSql.append("left join " + CcCourseProperty.dao.tableName + "  ccp on ccp.id = cc.property_id " );
		exceptSql.append("left join " + CcMajorDirection.dao.tableName + " cmd on cmd.id = cc.direction_id ");
		exceptSql.append("left join " + CcVersion.dao.tableName + " cv on cv.id = cc.plan_id ");
		exceptSql.append("where ctc.is_del = ? and cc.is_del = ? ");
		exceptSql.append("and ((cv.type = ? and cv.max_minor_version= ?) ");
		exceptSql.append("or (cv.type = ? and cv.minor_version = cv.max_minor_version) ) ");
		exceptSql.append("and ctc.teacher_id = ? ");
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		params.add(CcVersion.TYPE_MAJOR_VERSION);
		params.add(CcVersion.MINOR_VERSION_NULL);
		params.add(CcVersion.TYPE_MINOR_VERSION);
		params.add(teacherId);
		
		// 删选条件
		if (StrKit.notBlank(name)) {
			exceptSql.append("and cc.name = ? ");
			params.add(name);
		}
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcTeacherCourse.dao.paginate(pageable, "select cc.*,ctc.id teacher_course_id, ccm.module_name module_name,cch.name hierarchy_name,ccp.property_name property_name,cmd.name direction_name, cc.name courseName ", exceptSql.toString(), params.toArray());
	}


	public CcTeacherCourse findTeacherNameAndCourseName(Long teacherCourseId) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select cc.name courseName, ct.name teacherName from " + tableName + " ctc ");
		sql.append("left join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.teacher_course_id = ctc.id ");
		sql.append("left join " + CcCourse.dao.tableName + " cc on cc.id = ctc.course_id ");
		sql.append("left join " + CcTeacher.dao.tableName + " ct on ct.id = ctc.teacher_id ");
		
		sql.append("where ctc.id = ? ");
		param.add(teacherCourseId);
		
		sql.append("and ccg.is_del = ? ");
		param.add(Boolean.FALSE);
		
		sql.append("and ctc.is_del = ? ");
		param.add(Boolean.FALSE);
		
		sql.append("and cc.is_del = ? ");
	    param.add(Boolean.FALSE);
	    
	    sql.append("and ct.is_del = ? ");
	    param.add(Boolean.FALSE);
		
		return findFirst(sql.toString(), param.toArray());
	}
	
	/* (non-Javadoc)
	 * 根据编号查找教师开课课程详情
	 * 
	 * @see com.gnet.model.DbModel#findFilteredById(java.lang.Long)
	 */
	public CcTeacherCourse findFilteredById(Long id){
		
		StringBuilder exceptSql = new StringBuilder("select ctc.*,ct.name teacher_name, cc.type, cc.name course_name, cc.code course_code,ctm.start_year, ctm.end_year, ctm.term, ctm.term_type term_type, cv.major_id, " +
				"cc.plan_id, cch.name hierarchyName, ccm.module_name moduleName from " + CcTeacherCourse.dao.tableName + " ctc ");
		exceptSql.append("inner join " + CcTeacher.dao.tableName + " ct on ct.id = ctc.teacher_id  ");
		exceptSql.append("inner join " + CcCourse.dao.tableName + " cc on cc.id = ctc.course_id ");
		exceptSql.append("inner join " + CcVersion.dao.tableName + " cv on cv.id = cc.plan_id ");
		exceptSql.append("inner join " + CcTerm.dao.tableName + " ctm on ctm.id = ctc.term_id ");
		exceptSql.append("left join " + CcCourseHierarchy.dao.tableName + " cch on cch.id = cc.hierarchy_id ");
		exceptSql.append("left join " + CcCourseModule.dao.tableName + " ccm on ccm.id = cc.module_id ");
		exceptSql.append("where ctc.is_del = ? and cc.is_del = ? and ct.is_del = ? and cv.is_del = ? ");
		exceptSql.append("and ctc.id = ? ");
		exceptSql.append("and (cch.is_del = ? or cch.is_del is null ) ");
		exceptSql.append("and (ccm.is_del = ? or ccm.is_del is null ) ");

		List<Object> params = Lists.newArrayList();
		params.add(DEL_NO);
		params.add(DEL_NO);
		params.add(DEL_NO);
		params.add(DEL_NO);
		params.add(id);
		params.add(DEL_NO);
		params.add(DEL_NO);
		return findFirst(exceptSql.toString(), params.toArray());
		
	}
	
	/**
	 * 获得开课课程附带版本编号
	 * 
	 * @param id 开课课程编号
	 * @return
	 */
	public CcTeacherCourse findFilteredByIdWithVersion(Long id) {
		StringBuilder sql = new StringBuilder("select ctc.*, cc.plan_id version_id from " + tableName + " ctc ");
		sql.append("inner join cc_course cc on cc.id = ctc.course_id ");
		sql.append("where cc.is_del = ? and ctc.id = ?");
		return findFirst(sql.toString(), DEL_NO, id);
	}

	/**
	 * 返回某个版本下最大的年级
	 * @param planId
	 * @return
	 */
	public CcTeacherCourse findMaxGrade(Long planId) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select ctc.grade from " + tableName + " ctc ");
		sql.append("left join " + CcCourse.dao.tableName + " cc on cc.id = ctc.course_id ");
		sql.append("where cc.plan_id = ? ");
		param.add(planId);
		sql.append("and cc.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ctc.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("order by ctc.grade desc ");
		
		return findFirst(sql.toString(), param.toArray());
	}

	
	/**
	 * 通过未删除的开课课程返回最大的启用年级
	 * @param majorId 
	 * @return
	 */
	public CcTeacherCourse findMaxGradeTeacherCourse(Long majorId) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select tc.grade from " + tableName + " tc ");
		sql.append("left join " + CcCourse.dao.tableName + " c on c.id = tc.course_id ");
		sql.append("left join " + CcVersion.dao.tableName + " v on v.id = c.plan_id ");
		sql.append("where v.major_id = ? ");
		param.add(majorId);
		sql.append("and tc.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and c.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and v.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("order by tc.grade desc ");
		return findFirst(sql.toString(), param.toArray());
	}


	/**
	 * 获取专业编号下的排课列表
	 * @param majorId
	 * @return
	 * @author SY 
	 * @version 创建时间：2016年11月7日 下午8:20:25 
	 */
	public List<CcTeacherCourse> findByMajorId(Long majorId) {
		StringBuilder exceptSql = new StringBuilder("select ctc.* ");
		exceptSql.append("from " + CcTeacherCourse.dao.tableName + " ctc ");
		
		List<Object> params = Lists.newArrayList();
		exceptSql.append("left join " + CcCourse.dao.tableName + " cc on cc.id = ctc.course_id ");
		exceptSql.append("left join " + CcVersion.dao.tableName + " cv on cv.id = cc.plan_id and cv.minor_version = cv.max_minor_version ");
		exceptSql.append("where ctc.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cc.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cv.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cv.major_id = ? ");
		params.add(majorId);
		exceptSql.append("order by ctc.grade ");
		
		return find(exceptSql.toString(), params.toArray());
	}


	/**
	 * 获取某个版本的排课列表
	 * @param versionId
	 * @return
	 */
	public List<CcTeacherCourse> findByVersionId(Long versionId) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select ctc.* from " + tableName + " ctc ");
		sql.append("left join " + CcCourse.dao.tableName + " cc on cc.id = ctc.course_id ");
		sql.append("left join " + CcVersion.dao.tableName + " cv on cv.id = cc.plan_id ");
		sql.append("where ctc.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and cc.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and cv.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and cv.id = ? ");
		param.add(versionId);
		sql.append("order by ctc.grade ");
		
		return find(sql.toString(), param.toArray());
	}


	/**
	 * 返回某专业下的排课信息年级列表
	 * @param majorId
	 * @return
	 * @author SY 
	 * @version 创建时间：2016年12月30日 下午4:12:58 
	 */
	public List<CcTeacherCourse> findGradeByMajorId(Long majorId) {
		StringBuilder exceptSql = new StringBuilder("select ctc.grade ");
		exceptSql.append("from " + CcTeacherCourse.dao.tableName + " ctc ");
		
		List<Object> params = Lists.newArrayList();
		exceptSql.append("left join " + CcCourse.dao.tableName + " cc on cc.id = ctc.course_id ");
		exceptSql.append("left join " + CcVersion.dao.tableName + " cv on cv.id = cc.plan_id and cv.minor_version = cv.max_minor_version ");
		exceptSql.append("where ctc.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cc.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cv.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cv.major_id = ? ");
		params.add(majorId);
		exceptSql.append("group by ctc.grade ");
		exceptSql.append("order by ctc.grade ");
		
		return find(exceptSql.toString(), params.toArray());
	}

	/**
	 * 查询特定专业特定开年年级下的教师开课
	 * @param maxGrade
	 * @param minGrade
	 * @param excelMajorIds
	 * @return
	 */
	public List<CcTeacherCourse> findByGradeAndMjorId(Integer maxGrade, Integer minGrade, Long[] excelMajorIds) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select ctc.*, cc.code, ces.student_id, ce.educlass_name, ct.start_year, ct.end_year, ct.term, ct.term_type, cm.name majorName, ce.id classId from " + tableName + " ctc ");
		sql.append("inner join " + CcCourse.dao.tableName + " cc on cc.id = ctc.course_id and cc.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join " + CcVersion.dao.tableName + " cv on cv.id = cc.plan_id and cv.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join " + Office.dao.tableName + " cm on cm.id = cv.major_id and cm.is_del = ? and cm.id in (" + CollectionKit.convert(excelMajorIds, ",") + ") ");
		param.add(DEL_NO);
		sql.append("inner join " + CcTerm.dao.tableName + " ct on ct.id = ctc.term_id and ct.is_del = ? ");
		param.add(DEL_NO);
		sql.append("left join " + CcEduclass.dao.tableName + " ce on ce.teacher_course_id = ctc.id and (ce.is_del = ? or ce.is_del is null ) ");
		param.add(DEL_NO);
		sql.append("left join " + CcEduclassStudent.dao.tableName + " ces on ces.class_id = ce.id and (ces.is_del = ? or ces.is_del is null ) ");
		param.add(DEL_NO);
		sql.append("where ctc.is_del = ? and ctc.grade >= ? and ctc.grade <= ? ");
		param.add(DEL_NO);
		param.add(minGrade);
		param.add(maxGrade);
		return find(sql.toString(), param.toArray());
	}

	/**
	 * 找到当前课程的分享人的课程
	 * @param courseId
	 * @param termId
	 * 			学期编号
	 * @param grade 
	 * 			年级
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年10月19日 下午2:59:53 
	 */
	public CcTeacherCourse findSharer(Long courseId, Long termId, Integer grade) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder();
		sql.append("select ctc.* from " + tableName + " ctc ");
		sql.append("where ctc.course_id = ? ");
		param.add(courseId);
		sql.append("and ctc.term_id = ? ");
		param.add(termId);	
		sql.append("and ctc.grade = ? ");
		param.add(grade);	
		sql.append("and ctc.is_sharer = ? ");
		param.add(Boolean.TRUE);
		sql.append("and ctc.is_del = ? ");
		param.add(Boolean.FALSE);
		
		return findFirst(sql.toString(), param.toArray());
	}

	/**
	 * 查看教师开课课程列表
	 * 
	 * @param courseId
	 * @param termId
	 * @param teacherCourseId
	 * 			需要排除的当前教师开课编号
	 * @param grade 
	 * 			年级
	 * @return
	 */
	public List<CcTeacherCourse> findList(Long courseId, Long termId, Long teacherCourseId, Integer grade) {
		List<Object> params = Lists.newArrayList();
		StringBuilder selectSql = new StringBuilder("select ctc.* ");
		selectSql.append(", ct.name as teacher_name, cc.name as course_name ");
		selectSql.append(", ctm.start_year term_start_year, ctm.end_year term_end_year, ctm.term term_num, ctm.term_type term_type");
		selectSql.append(", cc.plan_id plan_id, so.name major_name, ce.educlass_name, ce.id educlassId, cc.code, cv.major_id ");
		StringBuilder exceptSql = new StringBuilder("from " + CcTeacherCourse.dao.tableName + " ctc ");
		exceptSql.append("left join " + CcTeacher.dao.tableName + " ct on ct.id=ctc.teacher_id ");
		exceptSql.append("left join " + CcCourse.dao.tableName + " cc on cc.id=ctc.course_id ");
		exceptSql.append("left join " + CcEduclass.dao.tableName + " ce on ce.teacher_course_id = ctc.id and ( ce.is_del = ? or ce.is_del is null ) ");
		params.add(Boolean.FALSE);
		exceptSql.append("inner join " + CcVersion.dao.tableName + " cv on cv.id = cc.plan_id ");
		exceptSql.append("inner join " + Office.dao.tableName + " so on so.id = cv.major_id ");
		exceptSql.append("left join " + CcTerm.dao.tableName + " ctm on ctm.id=ctc.term_id ");
		
		exceptSql.append("where ctc.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cc.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and ct.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and ctm.is_del = ? ");
		params.add(Boolean.FALSE);
		
		// pcf-start：这里的代码是为了在创建修订版本以后，对于重复的教师课程，只取最新的
		exceptSql.append("and cv.minor_version = cv.max_minor_version ");
		// pcf-end
		
		// 删选条件
		exceptSql.append("and ctc.course_id = ? ");
		params.add(courseId);
		
		exceptSql.append("and ctc.term_id = ? ");
		params.add(termId);
		
		exceptSql.append("and ctc.grade = ? ");
		params.add(grade);
		
		exceptSql.append("and ctc.id != ? ");
		params.add(teacherCourseId);
		
		exceptSql.append("order by ctc.modify_date desc ");
		
		return CcTeacherCourse.dao.find(selectSql.toString() + exceptSql.toString(), params.toArray());
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public List<CcTeacherCourse> findByTeacherCourseId(Long id) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select ccg.id courseGradecomposeId, ccg.gradecompose_id gradecomposeId, cic.indication_id indicationId, ccgi.id gradecomposeIndicationId from " + tableName + " ctc ");
		sql.append("inner join cc_course_gradecompose ccg on ccg.teacher_course_id = ctc.id and ctc.id = ? and ccg.is_del = ? ");
		param.add(id);
		param.add(DEL_NO);
		sql.append("inner join cc_course cc on cc.id = ctc.course_id and cc.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_indication_course cic on cic.course_id = cc.id and cic.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_course_gradecompose_indication ccgi on ccgi.course_gradecompose_id = ccg.id and ccgi.indication_id = cic.indication_id and ccgi.is_del = ? ");
		param.add(DEL_NO);
		sql.append("where ctc.is_del = ? ");
		param.add(DEL_NO);
		return find(sql.toString(), param.toArray());
	}

	/**
	 * 相同课程相同学期的教学班
	 * @param eduClassId
	 * @return
	 */
	public List<CcTeacherCourse> findListCourseByClassId(Long eduClassId) {
		StringBuilder sql = new StringBuilder("select ces.id from " + tableName +  " ctc ");
		sql.append("inner join cc_educlass ce on ce.teacher_course_id = ctc.id and ce.is_del = ? and ce.id= ? ");
		sql.append("inner join cc_teacher_course ctce on ctce.term_id = ctc.term_id and ctce.course_id = ctc.course_id and ctce.is_del = ? ");
		sql.append("inner join cc_educlass ces on ces.teacher_course_id = ctce.id and ces.is_del = ? ");
		sql.append("where ctc.is_del = ? ");
        return find(sql.toString(), DEL_NO, eduClassId, DEL_NO, DEL_NO, DEL_NO);
	}

	/**
	 * 某个学期某个课程是否存在传入学生
	 * @param educlassId
	 * @param studentIds
	 * @return
	 */
	public List<CcTeacherCourse> findExistCourseClassStudent(Long educlassId, Long[] studentIds) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select ces.educlass_name, cs.name, cs.student_no from cc_teacher_course ctc ");
		sql.append("inner join cc_educlass ce on ce.teacher_course_id = ctc.id and ce.id = ? ");
		param.add(educlassId);
		sql.append("inner join cc_teacher_course ctce on ctce.course_id = ctc.course_id and ctce.term_id = ctc.term_id and ctce.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_educlass ces on ces.teacher_course_id = ctce.id and ces.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_educlass_student cest on cest.class_id = ces.id and cest.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_student cs on cs.id = cest.student_id and cs.is_del = ? and cs.id in (" + CollectionKit.convert(studentIds, ",") + " ) ");
		param.add(DEL_NO);
		sql.append("where ctc.is_del = ? ");
		param.add(DEL_NO);
		return find(sql.toString(), param.toArray());
	}

	/**
	 * 通过开课课程成绩组成查找开课课程信息
	 * @param courseGradeComposeId
	 * @return
	 */
	public CcTeacherCourse findByCourseGradeComposeId(Long courseGradeComposeId) {
		StringBuilder sql = new StringBuilder("select ctc.*, ccg.input_score_type,cc.name,ce.educlass_name,ccg.hierarchy_level,ce.id educlassId from " + tableName + " ctc ");
		sql.append("inner join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.teacher_course_id = ctc.id and ccg.is_del = ? and ccg.id = ? ");
		sql.append("inner join cc_course cc on cc.id = ctc.course_id ");
		sql.append("left join cc_educlass ce on ce.teacher_course_id = ctc.id ");
		sql.append("where ctc.is_del = ? ");
		return findFirst(sql.toString(), DEL_NO, courseGradeComposeId, DEL_NO);
	}
	/*
	 * @param courseId
	 * @return com.gnet.model.admin.CcTeacherCourse
	 * @author Gejm
	 * @description: 通过课程id查找开课课程的达成度计算方式，因为一个课程可能有多个计算方式，我目前只取第一个计算方式计算达成度
	 * @date 2020/10/30 11:48
	 */
	public CcTeacherCourse findCourseResultType(Long courseId){
		StringBuilder sql = new StringBuilder("select * from cc_teacher_course a where  a.is_del=0 and course_id=? LIMIT 1");
		return findFirst(sql.toString(), courseId);
	}
}
