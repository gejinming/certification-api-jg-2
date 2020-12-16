package com.gnet.model.admin;

import java.util.ArrayList;
import java.util.List;

import com.gnet.utils.CollectionKit;
import com.jfinal.plugin.activerecord.Db;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

/**
 * @type model
 * @description 教学班表操作，包括对数据的增删改查与列表
 * @table cc_educlass
 * @author SY
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_educlass")
public class CcEduclass extends DbModel<CcEduclass> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcEduclass dao = new CcEduclass();

	/**
	 * 获得教学班列表(不分页)
	 * @param educlassId
	 * @return
	 */
	public CcEduclass findEduclassById(Long educlassId) {
		StringBuilder exceptSql = new StringBuilder("select ctc.id teacherCourseId,cv.id versionId, cv.major_id majorId,cv.enable_grade, ce.*, count(ces.id) studentNum,cc.id courseId, cc.name courseName, ct.name teacherName,ct.major_id, cc.code courseCode, cc.id courseId, ctc.result_type, ctc.grade grade ," +
				"cc.credit, cc.all_hours,ctm.start_year,ctm.end_year,ctm.term,ctm.term_type,cc.team_leader from " + CcEduclass.dao.tableName + " ce ");
		exceptSql.append("left join " + CcTeacherCourse.dao.tableName + " ctc on ctc.id = ce.teacher_course_id ");
		exceptSql.append("left join " + CcTeacher.dao.tableName + " ct on ct.id = ctc.teacher_id ");
		exceptSql.append("left join " + CcCourse.dao.tableName + " cc on cc.id = ctc.course_id ");
		exceptSql.append("left join " + CcEduclassStudent.dao.tableName + " ces on ces.class_id = ce.id and ces.is_del=0 ");
		exceptSql.append("left join " + CcVersion.dao.tableName + " cv on cv.id = cc.plan_id ");
		exceptSql.append("LEFT JOIN cc_term ctm ON ctm.id = ctc.term_id ");
		
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append("where ce.is_del = ? ");
		params.add(Boolean.FALSE);
		
		exceptSql.append("and ce.id = ? ");
		params.add(educlassId);
		exceptSql.append("and ctc.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cc.is_del = ? ");
		params.add(Boolean.FALSE);
		
		// 增加合并,用于计算学生数量
		exceptSql.append("group by ce.id ");
		
		return findFirst(exceptSql.toString(), params.toArray());
	}
	
	/**
	 * 查看教学班列表分页
	 * 
	 * @param courseName
	 * @return
	 */
	public Page<CcEduclass> page(Pageable pageable, String courseName, Long teacherCourseId) {
		StringBuilder exceptSql = new StringBuilder("from " + CcEduclass.dao.tableName + " ce ");
		exceptSql.append("left join " + CcTeacherCourse.dao.tableName + " ctc on ctc.id = ce.teacher_course_id ");
		exceptSql.append("left join " + CcCourse.dao.tableName + " cc on cc.id = ctc.course_id ");
		exceptSql.append("left join " + CcEduclassStudent.dao.tableName + " ces on ces.class_id = ce.id ");
		exceptSql.append("left join " + CcTeacher.dao.tableName + " ct on ct.id = ctc.teacher_id ");
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append("where ce.is_del = ? ");
		params.add(Boolean.FALSE);
		
		// 删选条件
		if (!StrKit.isBlank(courseName)) {
			exceptSql.append("and cc.name like '%" + StringEscapeUtils.escapeSql(courseName) + "%' ");
		}
		if (teacherCourseId != null) {
			exceptSql.append(" and ctc.id = ? ");
			params.add(teacherCourseId);
		}
		
		// 增加合并,用于计算学生数量
		exceptSql.append("group by ce.id ");
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		return CcEduclass.dao.paginate(pageable, "select ce.*,ct.id teacher_id, ct.name teacher_name, count(ces.id) studentNum, cc.name courseName, cc.code courseCode, cc.id courseId, ctc.result_type resultType ", exceptSql.toString(), params.toArray());
	}


	/**
	 * 某个版本下教师开课下的班级列表
	 * @return
	 */
	public List<CcEduclass> findByPlanId(Long majorId, Long[] majorIds){
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select ce.*, ctc.id teacherCourseId from " + CcEduclass.dao.tableName + " ce ");
		sql.append("inner join " + CcTeacherCourse.dao.tableName + " ctc on ctc.id = ce.teacher_course_id and ctc.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join " + CcCourse.dao.tableName + " cc on cc.id = ctc.course_id and cc.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join " + CcVersion.dao.tableName + " cv on cv.id = cc.plan_id and cv.is_del = ? and cv.minor_version = cv.max_minor_version ");
        param.add(DEL_NO);
		if (majorId != null) {
			sql.append("and cv.major_id = ? ");
			param.add(majorId);
		}else{
			sql.append("and cv.major_id in (" + CollectionKit.convert(majorIds, ",") + ") ");
		}
		sql.append("inner join " + Office.dao.tableName + " so on so.id = cv.major_id and so.is_del = ? ");
		param.add(DEL_NO);
		sql.append("where ce.is_del = ? ");
		param.add(DEL_NO);
		return find(sql.toString(), param.toArray());
	}

	/**
	 * 查看教学班列表分页
	 * 
	 * @param versionId
	 * @param grade
	 * @return
	 */
	public Page<CcEduclass> page(Pageable pageable, Long versionId, Integer grade) {
		StringBuilder exceptSql = new StringBuilder("from " + CcEduclass.dao.tableName + " ce ");
		exceptSql.append("left join " + CcTeacherCourse.dao.tableName + " ctc on ctc.id = ce.teacher_course_id ");
		exceptSql.append("left join " + CcCourse.dao.tableName + " cc on cc.id = ctc.course_id ");
		exceptSql.append("left join " + CcEduclassStudent.dao.tableName + " ces on ces.class_id = ce.id ");
		exceptSql.append("left join " + CcTeacher.dao.tableName + " ct on ct.id = ctc.teacher_id ");
		
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append("where ce.is_del = ? ");
		params.add(Boolean.FALSE);
		
		// 删选条件
		if (grade != null) {
			exceptSql.append("and ctc.grade = ? ");
			params.add(grade);
		}
		if (versionId != null) {
			exceptSql.append("and cc.plan_id = ? ");
			params.add(versionId);
		}
		
		// 增加合并,用于计算学生数量
		exceptSql.append("group by ce.id ");
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcEduclass.dao.paginate(pageable, "select ce.*,ct.id teacher_id, ct.name teacher_name, count(ces.id) studentNum, cc.name courseName, cc.code courseCode, cc.id courseId ", exceptSql.toString(), params.toArray());
	}

	
	/**
	 * 根据教师开课课程编号获得所有教学班
	 * 
	 * @param teacherCourseId
	 * @return
	 */
	public List<CcEduclass> findAllByCourseId(Long teacherCourseId) {
		StringBuilder sql = new StringBuilder("select cec.* from " + tableName + " cec ");
		sql.append("inner join " + CcTeacherCourse.dao.tableName + " ctc on ctc.id = cec.teacher_course_id ");
		sql.append("where cec.teacher_course_id = ? and cec.is_del = ? and ctc.is_del = ? ");
		return find(sql.toString(), teacherCourseId, DEL_NO, DEL_NO);
	}
	
	/**
	 * 根据课程编号，获取教学班列表
	 * 
	 * @param courseIdList
	 * @return
	 */
	public List<CcEduclass> findAllByCourseIds(List<Long> courseIdList,Integer grade) {
		if(courseIdList == null || courseIdList.isEmpty()) {
			return new ArrayList<>();
		}


		StringBuilder sql = new StringBuilder("select cec.*, ctc.course_id, count(ces.id) studentNum from " + tableName + " cec ");
		sql.append("inner join " + CcTeacherCourse.dao.tableName + " ctc on ctc.id = cec.teacher_course_id ");
		sql.append("left join " + CcEduclassStudent.dao.tableName +" ces on ces.class_id = cec.id ");
		sql.append("where cec.is_del = ? and ctc.is_del = ? and ctc.grade=? ");
		sql.append("and ctc.course_id in (" + CollectionKit.convert(courseIdList, ",") + ")");
		sql.append("group by cec.id, ctc.course_id ");
		//sql.append("group by teacher_course_id, ctc.course_id ");



		return find(sql.toString(), DEL_NO, DEL_NO,grade);
	}

	public List<CcEduclass> findAllByEduclass(Long courseId,Integer startYear,Long teacherId) {


		StringBuilder sql = new StringBuilder("select cec.* from " + tableName + " cec ");
		sql.append("inner join " + CcTeacherCourse.dao.tableName + " ctc on ctc.id = cec.teacher_course_id ");
		sql.append("inner join cc_term ct on ct.id =ctc.term_id ");
		sql.append("where cec.is_del = ? and ctc.is_del = ? and ct.start_year=? ");
		sql.append("and ctc.course_id =? and ctc.teacher_id=? ");
		sql.append("group by cec.id ");
		//sql.append("group by teacher_course_id, ctc.course_id ");

		return find(sql.toString(), DEL_NO, DEL_NO,startYear,courseId,teacherId);
	}

	/**
	 * 查看教学班列表分页
	 * 
	 * @param majorId
	 * @param courseName 
	 * @param grade 
	 * @return
	 */
	public Page<CcEduclass> page(Pageable pageable, Long majorId, Long versionId, String courseName, Integer grade) {
		List<Object> params = Lists.newArrayList();
		StringBuilder exceptSql = new StringBuilder("from " + CcEduclass.dao.tableName + " ce ");
		exceptSql.append("left join " + CcTeacherCourse.dao.tableName + " ctc on ctc.id = ce.teacher_course_id ");
		if(grade != null) {
			exceptSql.append("and ctc.grade = ? ");
			params.add(grade);
		}
		exceptSql.append("left join " + CcCourse.dao.tableName + " cc on cc.id = ctc.course_id ");
		exceptSql.append("left join " + CcTeacher.dao.tableName + " ct on ct.id = ctc.teacher_id ");
		exceptSql.append("left join " + CcVersion.dao.tableName + " cv on cv.id = cc.plan_id ");
		exceptSql.append("where ");
		exceptSql.append("ce.is_del = ? ");
		params.add(Boolean.FALSE);
		if(majorId != null) {
			exceptSql.append("and cv.major_id = ? ");
			params.add(majorId);			
		}
		exceptSql.append("and ctc.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cc.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and ct.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cv.is_del = ? ");
		params.add(Boolean.FALSE);
	    
		if(versionId != null){
			exceptSql.append("and cv.id = ? ");
			params.add(versionId);
		}
		
		if(StrKit.notBlank(courseName)){
			exceptSql.append("and cc.name like '%" + StringEscapeUtils.escapeSql(courseName) + "%' ");
		}
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcEduclass.dao.paginate(pageable, "select ce.*,ct.id teacher_id, ct.name teacher_name, cc.name courseName, cc.code courseCode, cc.id courseId, ctc.result_type resultType ", exceptSql.toString(), params.toArray());
	}


	/**
	 * 教学班名称是否重复
	 * @param id
	 *         教学班编号
	 * @param teacherCourseId
	 *         教师开课课程编号
	 * @param educlassName
	 *         教学班名称
	 * @return
	 */
    public boolean isExisted(Long id, Long teacherCourseId, String educlassName) {
		if (id != null) {
			return Db.queryLong("select count(1) from " + tableName + " where educlass_name = ? and id != ? and teacher_course_id = ? and is_del = ? ",  educlassName, id, teacherCourseId , Boolean.FALSE) > 0;
		} else {
			return Db.queryLong("select count(1) from " + tableName + " where educlass_name = ? and teacher_course_id = ? and is_del = ? ",  educlassName, teacherCourseId, Boolean.FALSE) > 0;
		}
    }

	/**
	 * 查找开课课程下已关联成绩组成的课程目标
	 *
	 * @param indicationCourseId
	 * @param eduClassId
	 * @return
	 */
	public List<CcEduclass> findByEduclassId(Long eduClassId, Long indicationCourseId) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select ci.*, ceeaa.achieve_value from " + tableName + " ce ");
		sql.append("inner join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.teacher_course_id = ce.teacher_course_id and ccg.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.course_gradecompose_id = ccg.id and ccgi.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join " + CcIndication.dao.tableName + " ci on ci.id = ccgi.indication_id and ci.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join " + CcCourseTargetIndication.dao.tableName + " ccti on ccti.indication_id = ci.id ");
		sql.append("inner join " + CcIndicationCourse.dao.tableName + " cic on cic.id = ccti.indication_course_id and cic.id = ? ");
        param.add(indicationCourseId);
		sql.append("left join " + CcEdupointEachAimsAchieve.dao.tableName + " ceeaa on ceeaa.indication_id = ccgi.indication_id and ceeaa.educlass_id = ? ");
		param.add(eduClassId);
		sql.append("where ce.is_del = ? and ce.id = ? ");
		param.add(DEL_NO);
		param.add(eduClassId);
		sql.append("group by ci.id ");
		sql.append("order by ci.sort ");
        return find(sql.toString(), param.toArray());
	}
	/*
	 * @param educlassIds
	 * @return java.util.List<com.gnet.model.admin.CcEduclass>
	 * @author Gejm
	 * @description: 根据课程统计教学班id,这里的group by 去除了相同课程中相同老师下的教学班list只取其中一个教学班id
	 * @date 2020/7/27 14:41
	 */
	public List<CcEduclass> findEduclassIds(List<Long> courseIds){
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select ce.id from cc_educlass ce inner join cc_teacher_course ctc on ce.teacher_course_id=ctc.id  and ctc.is_del=0 ");
		sql.append("where  ce.is_del=? and ctc.course_id in(" + CollectionKit.convert(courseIds, ",") + ") ");
		param.add(DEL_NO);
		sql.append("group by ctc.id  ");
		return find(sql.toString(), param.toArray());
	}

	public CcEduclass findEduclassCourse(Long edclassId){
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select ce.teacher_course_id,ce.educlass_name,cc.name courseName,cc.code,cc.credit,cc.all_hours,ct.name teacherName,");
		sql.append("count(ces.student_id) studentNum,cte.start_year,cte.end_year,cte.term,cc.plan_id,cc.id courseId  ");
		sql.append("from cc_educlass ce ");
		sql.append("left join cc_teacher_course ctc on ce.teacher_course_id=ctc.id and ctc.is_del=0 ");
		sql.append("left join cc_course cc on cc.id=ctc.course_id and cc.is_del=0 ");
		sql.append("left join cc_teacher ct on ct.id=ctc.teacher_id and ct.is_del=0 ");
		sql.append("left join cc_educlass_student ces on ces.class_id=ce.id and ces.is_del=0 ");
		sql.append("left join cc_term cte on cte.id=ctc.term_id and cte.is_del=0 ");
		sql.append("where ce.id=? and ce.is_del=0 ");
		param.add(edclassId);
		return  findFirst(sql.toString(),param.toArray());
	}

}
