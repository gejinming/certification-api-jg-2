package com.gnet.model.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;

import java.lang.String;

/**
 * @type model
 * @description 考核成绩分析法学生课程目标成绩
 * @table cc_score_stu_indigrade
 * @author SY
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_score_stu_indigrade")
public class CcScoreStuIndigrade extends DbModel<CcScoreStuIndigrade> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcScoreStuIndigrade dao = new CcScoreStuIndigrade();

	/**
	 * 字符串常量
	 */
	public static final String split = "-";

	
	/**
	 * 学生最小成绩
	 */
	public static final BigDecimal MIN_SCORE = BigDecimal.valueOf(0);
	
	/**
	 * 最近的统计之后有新的更新
	 * 
	 * @param eduClassId
	 * @param date
	 * @return
	 */
	public Boolean needToUpdate(Long eduClassId) {
		StringBuilder sql = new StringBuilder("select count(cssig.id) from " + tableName + " cssig ");
		sql.append("inner join cc_course_gradecompose_indication ccgi on ccgi.id = cssig.gradecompose_indication_id ");
		sql.append("inner join cc_course_gradecompose ccg on ccg.id = ccgi.course_gradecompose_id ");
		sql.append("inner join cc_teacher_course ctc on ctc.id = ccg.teacher_course_id ");
		sql.append("inner join cc_educlass ce on ce.teacher_course_id = ctc.id and ce.id = ? ");
		sql.append("inner join cc_educlass_student ces on ces.student_id = cssig.student_id and ces.class_id = ce.id ");
		// Edit by SY 2019年11月7日19:01:34 删除和cc_report_educlass_grade标的关联，因为已经废弃，重新加入其他表
//		sql.append("left join cc_report_educlass_grade creg on creg.gradecompose_indication_id = cssig.gradecompose_indication_id and creg.educlass_id = ? ");
//		sql.append("where cssig.modify_date > creg.statistics_date or ccgi.modify_date > creg.statistics_date or ( creg.statistics_date is null and cssig.is_del = ? ) or ce.student_num_change_date > creg.statistics_date ");
		sql.append("left join cc_eduindication_stu_score cess on cess.gradecompose_indication_id = cssig.gradecompose_indication_id and cess.educlass_id = ? ");
		sql.append("where cssig.modify_date > cess.modify_date or ccgi.modify_date > cess.modify_date or ( cess.modify_date is null and cssig.is_del = ? ) or ce.student_num_change_date > cess.modify_date ");
		sql.append("limit 1");
		return Db.queryLong(sql.toString(), eduClassId, eduClassId, DEL_NO) > 0;
	}
	
	/**
	 * 返回需要更新的统计报表项
	 * 
	 * @param versionId
	 * @param grade
	 * @return
	 */
	public List<CcScoreStuIndigrade> findNeedToUpdate(Long versionId, Integer grade) {
		StringBuilder sql = new StringBuilder("select ce.id educlass_id from " + tableName + " cssig ");
		sql.append("inner join cc_course_gradecompose_indication ccgi on ccgi.id = cssig.gradecompose_indication_id ");
		sql.append("inner join cc_course_gradecompose ccg on ccg.id = ccgi.course_gradecompose_id ");
		sql.append("inner join cc_teacher_course ctc on ctc.id = ccg.teacher_course_id and ctc.grade = ? ");
		sql.append("inner join cc_course cc on cc.plan_id = ? and cc.id = ctc.course_id ");
		sql.append("inner join cc_educlass ce on ce.teacher_course_id = ctc.id ");
		sql.append("inner join cc_educlass_student ces on ces.student_id = cssig.student_id and ces.class_id = ce.id ");
		// Edit by SY 2019年11月7日19:01:34 删除和cc_report_educlass_grade标的关联，因为已经废弃，重新加入其他表
//		sql.append("left join cc_report_educlass_grade creg on creg.is_del = ? and creg.gradecompose_indication_id = cssig.gradecompose_indication_id and creg.educlass_id = ce.id ");
//		sql.append("where cssig.modify_date > creg.statistics_date or ccgi.weight != creg.weight or ccgi.max_score != creg.max_score or creg.id is null ");
		sql.append("left join cc_eduindication_stu_score cess on cess.gradecompose_indication_id = cssig.gradecompose_indication_id and cess.educlass_id = ? ");
		sql.append("where cssig.modify_date > cess.modify_date or ccgi.modify_date > cess.modify_date or ( cess.modify_date is null and cssig.is_del = ? ) or ce.student_num_change_date > cess.modify_date ");
		sql.append("group by ce.id");
		return find(sql.toString(), grade, versionId, DEL_NO);
	}
	
	/**
	 * 判断某个版本某个年级成绩组成相关记录是否有修改，若有则判断为需要更新
	 * 
	 * @param versionId 版本编号
	 * @param grade 成绩
	 * @return
	 */
	public Boolean isNeedToUpdateByVersionAndGrade(Long versionId, Integer grade) {
		StringBuilder sql = new StringBuilder("select count(cssig.id) from " + tableName + " cssig ");
		sql.append("inner join cc_course_gradecompose_indication ccgi on ccgi.id = cssig.gradecompose_indication_id ");
		sql.append("inner join cc_course_gradecompose ccg on ccg.id = ccgi.course_gradecompose_id ");
		sql.append("inner join cc_teacher_course ctc on ctc.id = ccg.teacher_course_id and ctc.grade = ? ");
		sql.append("inner join cc_course cc on cc.plan_id = ? and cc.id = ctc.course_id ");
		sql.append("inner join cc_educlass ce on ce.teacher_course_id = ctc.id ");
		sql.append("inner join cc_educlass_student ces on ces.student_id = cssig.student_id and ces.class_id = ce.id ");
		sql.append("left join cc_report_educlass_grade creg on creg.gradecompose_indication_id = cssig.gradecompose_indication_id and creg.educlass_id = ce.id ");
		sql.append("where cssig.is_del = ? and (cssig.modify_date > creg.statistics_date or ccgi.weight != creg.weight or ccgi.max_score != creg.max_score or creg.id is null or ce.student_num_change_date > creg.statistics_date ) ");
		sql.append("limit 1");
		return Db.queryLong(sql.toString(), grade, versionId, DEL_NO) > 0;
	}
	
	/**
	 * 判断个人报表是否需要更新（考核分析法）
	 * 
	 * @param studentId 学生编号
	 * @param majorDirectionId 专业方向编号
	 * @param grade 
	 * @param versionId 
	 * @return
	 */
	public Boolean needToUpdateByStudentId(Long studentId, Long majorDirectionId, Integer grade, Long versionId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select count(cssig.id) from " + tableName + " cssig ");
		sql.append("inner join cc_educlass_student ces on ces.student_id = cssig.student_id and ces.is_del = ? ");
		sql.append("inner join cc_student cs on cs.is_del = ? and cs.grade = ? and ces.student_id = cs.id ");
		sql.append("inner join cc_educlass cec on cec.id = ces.class_id and cec.is_del = ? ");
		sql.append("inner join cc_teacher_course ctc on ctc.id = cec.teacher_course_id and ctc.is_del = ? ");
		sql.append("inner join cc_course cc on cc.id = ctc.course_id and cc.is_del = ? and cc.plan_id = ? ");
		sql.append("inner join cc_course_gradecompose ccg on ccg.teacher_course_id = ctc.id and ccg.is_del = ? ");
		sql.append("inner join cc_course_gradecompose_indication ccgi on ccgi.id = cssig.gradecompose_indication_id and ccgi.course_gradecompose_id = ccg.id and ccgi.is_del = ? ");
		sql.append("inner join cc_indication_course cic on cic.indication_id = ccgi.indication_id and cc.id = cic.course_id and cic.is_del = ? ");
		sql.append("left join cc_report_personal_course crpc on crpc.indication_course_id = cic.id and crpc.student_id = cssig.student_id ");
		sql.append("where cssig.student_id = ? and (cssig.modify_date > crpc.statistics_date or ccgi.modify_date > crpc.statistics_date or crpc.statistics_date is null) ");
		params.add(DEL_NO);
		params.add(DEL_NO);
		params.add(grade);
		params.add(DEL_NO);
		params.add(DEL_NO);
		params.add(DEL_NO);
		params.add(versionId);
		params.add(DEL_NO);
		params.add(DEL_NO);
		params.add(DEL_NO);
		params.add(studentId);
		if (majorDirectionId != null) {
			sql.append("and (cc.direction_id = ? or cc.direction_id is null) ");
			params.add(majorDirectionId);
		}
		
		sql.append("limit 1");
		return Db.queryLong(sql.toString(), params.toArray()) > 0;
	}
	
	/**
	 * 返回学生指标点成绩项
	 * 
	 * @param eduClassId
	 * @return
	 */
	public List<CcScoreStuIndigrade> findByClassId(Long eduClassId) {
		StringBuilder sql = new StringBuilder("select cssig.* from " + tableName + " cssig ");
		sql.append("inner join cc_educlass_student ces on ces.class_id = ? and ces.is_del = ? and ces.student_id = cssig.student_id  ");
		sql.append("inner join cc_educlass ce on ce.is_del = ? and ce.id = ces.class_id ");
		sql.append("inner join cc_teacher_course ctc on ctc.is_del = ? and ctc.id = ce.teacher_course_id ");
		sql.append("inner join cc_course_gradecompose ccg on ccg.is_del = ? and ccg.teacher_course_id = ctc.id ");
		sql.append("inner join cc_course_gradecompose_indication ccgi on ccgi.is_del = ? and ccgi.course_gradecompose_id = ccg.id and cssig.gradecompose_indication_id = ccgi.id ");
		sql.append("where cssig.is_del = ?");
		return find(sql.toString(), eduClassId, DEL_NO, DEL_NO, DEL_NO, DEL_NO, DEL_NO, DEL_NO);
	}

	/**
	 * 返回学生指标点成绩项(剔除掉一些不用计算的学生)
	 *
	 * @author SY
	 * @param eduClassId
	 * 			教学班编号
	 * @param isCaculate
	 * 			是否计算
	 * @return
	 */
	public List<CcScoreStuIndigrade> findByClassIdAndIsCaculate(Long eduClassId, Boolean isCaculate) {
		StringBuilder sql = new StringBuilder("select cssig.* from " + tableName + " cssig ");
		sql.append("inner join cc_educlass_student ces on ces.class_id = ? and ces.is_del = ? and ces.student_id = cssig.student_id and ces.is_caculate = ? ");
		sql.append("inner join cc_educlass ce on ce.is_del = ? and ce.id = ces.class_id ");
		sql.append("inner join cc_teacher_course ctc on ctc.is_del = ? and ctc.id = ce.teacher_course_id ");
		sql.append("inner join cc_course_gradecompose ccg on ccg.is_del = ? and ccg.teacher_course_id = ctc.id ");
		sql.append("inner join cc_course_gradecompose_indication ccgi on ccgi.is_del = ? and ccgi.course_gradecompose_id = ccg.id and cssig.gradecompose_indication_id = ccgi.id ");
		sql.append("where cssig.is_del = ?");
		return find(sql.toString(), eduClassId, DEL_NO, isCaculate, DEL_NO, DEL_NO, DEL_NO, DEL_NO, DEL_NO);
	}

	/**
	 * @description: 查询教学班的总分和平均分
	 * @param eduClassIds
	 * @return
	 * @author YHL
	 */
	public List<CcScoreStuIndigrade> findClassGradeByEduclassIds(Long[] eduClassIds) {
		StringBuilder sql = new StringBuilder("select sum(cssi.grade) totalGrade, avg(cssi.grade) avgGrade, ces.class_id eduClassId, cssi.gradecompose_indication_id gradecomposeIndicationId from " + CcScoreStuIndigrade.dao.tableName + " cssi ");
		sql.append("inner join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.id = cssi.gradecompose_indication_id and ccgi.is_del = ? ");
		sql.append("inner join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.id = ccgi.course_gradecompose_id and ccg.is_del = ? ");
		sql.append("inner join " + CcEduclassStudent.dao.tableName + " ces on ces.student_id = cssi.student_id and ces.class_id in (" + CollectionKit.convert(eduClassIds, ",") + ") and ces.is_del = ? ");
		sql.append("inner join " + CcEduclass.dao.tableName + " ce on ce.id = ces.class_id and ce.is_del = ? ");
		sql.append("where cssi.is_del = ? ");
		sql.append("group by ces.class_id, cssi.gradecompose_indication_id ");

		return find(sql.toString(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
	}

	/**
	 * @description: 查询这些教学班下每个课程目标对应的每个成绩组成的各自的总分（即，CO1和S11的数据，不区分教学班）
	 * @param eduClassIds
	 * @return
	 * @author SY
	 */
	public List<CcScoreStuIndigrade> findAllClassGradeByEduclassIds(List<Long> eduClassIds) {
		//TODO 2020/9/7 统计时去除按满分统计，因为一个课程的满分可能不一样
		//List<Long> eduClassIds0 = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select  sum(cssi.grade) totalGrade,ctc.course_id,ccg.gradecompose_id,ccgi.weight, cssi.gradecompose_indication_id gradecomposeIndicationId " +
				"from " + CcScoreStuIndigrade.dao.tableName + " cssi ");
		sql.append("inner join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.id = cssi.gradecompose_indication_id and ccgi.is_del = ? ");
		sql.append("inner join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.id = ccgi.course_gradecompose_id and ccg.is_del = ? ");
		sql.append("inner join " + CcTeacherCourse.dao.tableName +" ctc on ccg.teacher_course_id=ctc.id AND ctc.is_del = ?  "  );
		//sql.append("inner join " + CcEduclassStudent.dao.tableName + " ces on ces.student_id = cssi.student_id and ces.class_id in (" + CollectionKit.convert(eduClassIds, ",") + ") and ces.is_del = ? ");
		//2020/06/27 Gjm
		sql.append("inner join " + CcEduclass.dao.tableName + " ce on ce.teacher_course_id = ctc.id  and ce.is_del = ? " +
				" and ce.id in ("+ CollectionKit.convert(eduClassIds, ",") + ") ");
		sql.append("where cssi.is_del = ? ");
//		sql.append("group by cssi.gradecompose_indication_id, ccgi.max_score, ccgi.weight ");
		// BUGFIX cert 217 Edit By SY 2018年1月10日
		sql.append("group by ctc.course_id,ccgi.indication_id, ccg.gradecompose_id,ccgi.weight ");
		sql.append("order by ctc.course_id");
		return find(sql.toString(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
	}

	/**
	 * 返回开课课程下学生指标点成绩项(EM00746)
	 * 
	 * @param eduClassId
	 * @return
	 */
	public List<CcScoreStuIndigrade> findAllByEduClassOrderByIndex(Long eduClassId) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select cssig.*, cs.student_no from " + tableName + " cssig ");
		sql.append("inner join cc_educlass_student ces on ces.class_id = ? and ces.is_del = ? and ces.student_id = cssig.student_id  ");
		params.add(eduClassId);
		params.add(DEL_NO);
		sql.append("inner join cc_educlass ce on ce.is_del = ? and ce.id = ces.class_id ");
		params.add(DEL_NO);
		sql.append("inner join cc_teacher_course ctc on ctc.is_del = ? and ctc.id = ce.teacher_course_id ");
		params.add(DEL_NO);
		sql.append("inner join cc_course_gradecompose ccg on ccg.is_del = ? and ccg.teacher_course_id = ctc.id ");
		params.add(DEL_NO);
		sql.append("inner join cc_course_gradecompose_indication ccgi on ccgi.is_del = ? and ccgi.course_gradecompose_id = ccg.id and cssig.gradecompose_indication_id = ccgi.id ");
		params.add(DEL_NO);
		sql.append("inner join " + CcIndication.dao.tableName + " ci on ci.id = ccgi.indication_id ");
		sql.append("inner join " + CcStudent.dao.tableName + " cs on cs.id = cssig.student_id ");
		sql.append("where cssig.is_del = ? and ccgi.is_del = ? and ci.is_del = ? ");
		params.add(DEL_NO);
		params.add(DEL_NO);
		params.add(DEL_NO);
		sql.append("order by ci.sort ");
		return find(sql.toString(), params.toArray());
	}
	
	/**
	 * 根据教学班编号获取相关学生成绩项
	 * 
	 * @param reportIds
	 * @return
	 */
	public List<CcScoreStuIndigrade> findByEduclassIds(Long[] eduClassIds) {
		StringBuilder sql = new StringBuilder("select cssig.*, ccgi.weight weight, ccgi.max_score max_score, ces.class_id educlass_id from " + tableName + " cssig ");
		sql.append("inner join cc_educlass_student ces on ces.class_id in (" + CollectionKit.convert(eduClassIds, ",") + ") and ces.is_del = ? and ces.student_id = cssig.student_id  ");
		sql.append("inner join cc_educlass ce on ce.is_del = ? and ce.id = ces.class_id ");
		sql.append("inner join cc_teacher_course ctc on ctc.is_del = ? and ctc.id = ce.teacher_course_id ");
		sql.append("inner join cc_course_gradecompose ccg on ccg.is_del = ? and ccg.teacher_course_id = ctc.id ");
		sql.append("inner join cc_course_gradecompose_indication ccgi on ccgi.is_del = ? and ccgi.course_gradecompose_id = ccg.id and cssig.gradecompose_indication_id = ccgi.id ");
		sql.append("where cssig.is_del = ?");
		return find(sql.toString(), DEL_NO, DEL_NO, DEL_NO, DEL_NO, DEL_NO, DEL_NO);
	}
	
	
	/**
	 * 返回开课课程下学生指标点成绩项
	 * 
	 * @param teacherCourseId
	 * @return
	 */
	public List<CcScoreStuIndigrade> findByTeacherCourseId(Long teacherCourseId) {
		StringBuilder sql = new StringBuilder("select cssig.*, ce.id educlass_id from " + tableName + " cssig ");
		sql.append("left join cc_educlass_student ces on ces.student_id = cssig.student_id ");
		sql.append("left join cc_educlass ce on ce.id = ces.class_id ");
		sql.append("where ce.teacher_course_id=? and cssig.is_del=?");
		return find(sql.toString(), teacherCourseId, DEL_NO);
	}
	
	/**
	 * 根据班级编号和指标点编号
	 * 
	 * @param classId
	 * @param indicationId
	 * @return
	 */
	public Boolean deleteByClassIdAndIndicationId(Long classId, Long indicationId){
		
		StringBuilder sql = new StringBuilder("update " + tableName + " cssi ");
		sql.append("left join " + CcStudent.dao.tableName + " cs on cs.id = cssi.student_id ");
		sql.append("left join " + CcEduclassStudent.dao.tableName + " ces on ces.student_id = cs.id ");
		sql.append("left join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.id = cssi.gradecompose_indication_id ");
		sql.append("set cssi.is_del = ? where ces.class_id = ? and ccgi.indication_id = ? ");
		
		List<Object> params = Lists.newArrayList();
		params.add(Boolean.TRUE);
		params.add(classId);
		params.add(indicationId);
		
		return Db.update(sql.toString(), params.toArray()) >= 0;
	}
	
	/**
	 * 根据班级编号
	 * 
	 * @param classId
	 * @return
	 */
	public Boolean deleteByClassId(Long classId){
		
		StringBuilder sql = new StringBuilder("update " + tableName + " cssi ");
		sql.append("left join " + CcStudent.dao.tableName + " cs on cs.id = cssi.student_id ");
		sql.append("left join " + CcEduclassStudent.dao.tableName + " ces on ces.student_id = cs.id ");
		sql.append("set cssi.is_del = ? where ces.class_id = ? ");
		
		List<Object> params = Lists.newArrayList();
		params.add(Boolean.TRUE);
		params.add(classId);
		
		return Db.update(sql.toString(), params.toArray()) >= 0;
	}
	

	/**
	 * 获得教学班下某个指标点的成绩
	 * @param educlassId
	 *            教学班编号
	 * @param indicationId
	 *            指标点编号
	 * @return
	 */
	public List<CcScoreStuIndigrade> findDetailByClassIdAndIndicationId(Long educlassId, Long indicationId) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select cssig.*, cs.student_no student_no, cs.name student_name from " + tableName + " cssig ");
		sql.append("left join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.id = cssig.gradecompose_indication_id ");
		sql.append("left join " + CcEduclassStudent.dao.tableName +  " ces on ces.student_id = cssig.student_id ");
		sql.append("left join " + CcStudent.dao.tableName + " cs on cs.id = cssig.student_id ");
		sql.append("where ces.class_id = ? "); 
		param.add(educlassId);
		sql.append("and ccgi.indication_id = ? ");
		param.add(indicationId);
		sql.append("and ces.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and cssig.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ccgi.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and cs.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("order by cs.student_no asc");
		return find(sql.toString(), param.toArray());
	}
	
	/**
	 * 某个教学班下的学生在某个指标点下是否有考核分析法成绩
	 * @param educlassId
	 * @param indicationId
	 * @return
	 */
	public boolean isExistStudentGrades(Long educlassId, Long indicationId){
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select count(1) from " + tableName + " cssig ");
		sql.append("left join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.id = cssig.gradecompose_indication_id ");
		sql.append("left join " + CcEduclassStudent.dao.tableName +  " ces on ces.student_id = cssig.student_id ");
		sql.append("where ces.class_id = ? "); 
		param.add(educlassId);
		sql.append("and ccgi.indication_id = ? ");
		param.add(indicationId);
		sql.append("and ces.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and cssig.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ccgi.is_del = ? ");
		param.add(Boolean.FALSE);
		return Db.queryLong(sql.toString(), param.toArray()) > 0;
	}
	
	/**
	 * 获得教学班下某个指标点的成绩
	 * @param educlassId
	 *            教学班编号
	 * @param courseGradecomposeId
	 *            开课课程成绩组成元素编号
	 * @return
	 */
	public List<CcScoreStuIndigrade> findDetailByClassIdAndCourseGradecomposeId(Long educlassId, Long courseGradecomposeId) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select cssig.*, cs.student_no student_no, cs.name student_name from " + tableName + " cssig ");
		sql.append("left join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.id = cssig.gradecompose_indication_id ");
		sql.append("left join " + CcEduclassStudent.dao.tableName +  " ces on ces.student_id = cssig.student_id ");
		sql.append("left join " + CcStudent.dao.tableName + " cs on cs.id = cssig.student_id ");
		sql.append("where ces.class_id = ? "); 
		param.add(educlassId);
		sql.append("and ccgi.course_gradecompose_id = ? ");
		param.add(courseGradecomposeId);
		sql.append("and ces.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and cssig.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ccgi.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and cs.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("order by cs.student_no asc");
		return find(sql.toString(), param.toArray());
	}

	/**
	 * 根据教学班学生表ids，获取成绩
	 * @param educlassStudentIds
	 * 			教学班学生ids
	 * @return
	 * @author SY
	 * @date 2016年8月8日16:35:22
	 */
	public boolean existScore(Long[] educlassStudentIds) {
		List<Object> params = new ArrayList<>();
		StringBuilder sb = new StringBuilder("select count(1) from " + tableName + " cssi ");
		sb.append("left join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.id = cssi.gradecompose_indication_id ");
		sb.append("left join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.id = ccgi.course_gradecompose_id ");
		sb.append("left join " + CcEduclass.dao.tableName + " ce on ce.teacher_course_id = ccg.teacher_course_id ");
		sb.append("left join " + CcEduclassStudent.dao.tableName + " ces on ces.class_id = ce.id ");
		sb.append("where ces.student_id = cssi.student_id ");
		sb.append("and ces.id in (" + CollectionKit.convert(educlassStudentIds, ",") + ") ");
		sb.append("and cssi.is_del = ? ");
		params.add(Boolean.FALSE);
		return Db.queryLong(sb.toString(), params.toArray()) > 0;
	}
	
	/**
	 * 验证同一学生在开课课程成绩组成元素与指标点下有没有重复
	 * @param studentId
	 * @param gradecomposeIndicationId
	 * @return
	 */
	public boolean isExist(Long studentId, Long gradecomposeIndicationId){
		return Db.queryLong("select count(1) from " + tableName + " where gradecompose_indication_id = ? and student_id = ? and is_del = ? ", gradecomposeIndicationId, studentId, Boolean.FALSE) > 0;
	}

	/**该开课课程组成下是否存在学生成绩
	 * @param id
	 * @return
	 */
	public boolean isExistStudentGrade(Long courseGradecomposeId) {
		StringBuilder sql = new StringBuilder("select count(1) from " + tableName + " cssi ");
		sql.append("left join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.id = cssi.gradecompose_indication_id ");
		sql.append("where ccgi.course_gradecompose_id = ? ");
		sql.append("and cssi.is_del = ? ");
		sql.append("and ccgi.is_del = ? ");
		sql.append("and cssi.grade is not null ");
		return Db.queryLong(sql.toString(), courseGradecomposeId, Boolean.FALSE, Boolean.FALSE) > 0 ;
	}
	
	/**这些开课课程组成下是否存在学生成绩
	 * @param id
	 * @author SY
	 * @date 2017年10月19日
	 * @return
	 */
	public boolean isExistStudentGrade(List<Long> courseGradecomposeIds) {
		if(courseGradecomposeIds == null || courseGradecomposeIds.isEmpty()) {
			return false;
		}
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select count(1) from " + tableName + " cssi ");
		sql.append("left join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.id = cssi.gradecompose_indication_id ");
		sql.append("where ccgi.course_gradecompose_id in (" + CollectionKit.convert(courseGradecomposeIds, ",") + ") ");
		sql.append("and cssi.is_del = ? ");
		params.add(Boolean.FALSE);
		sql.append("and ccgi.is_del = ? ");
		params.add(Boolean.FALSE);
		sql.append("and cssi.grade is not null ");
		return Db.queryLong(sql.toString(), params.toArray()) > 0 ;
	}

	/**
	 * 某已开课课程成绩组成下的学生成绩
	 * @param courseGradecomposeId
	 * @return
	 */
	public List<CcScoreStuIndigrade> findByIndicationIdsAndCourseGradecomposeId(Long courseGradecomposeId) {
		StringBuilder sql = new StringBuilder("select cssi.*, ccgi.indication_id from " + tableName + " cssi ");
		sql.append("inner join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.id = cssi.gradecompose_indication_id and ccgi.course_gradecompose_id = ? and ccgi.is_del = ? ");
		sql.append("where cssi.is_del = ? ");
		return find(sql.toString(), courseGradecomposeId, DEL_NO, DEL_NO);
	}

	/**
	 * 某已开课课程成绩组成特定指标点下的学生成绩
	 * @param courseGradecomposeId
	 * @param indaicationIds
	 * @return
	 */
	public List<CcScoreStuIndigrade> findByIndicationIdsAndCourseGradecomposeIdGroupByEduclassId(Long courseGradecomposeId, Long[] indaicationIds) {
		StringBuilder sql = new StringBuilder("select ifnull(sum(cssi.grade), 0) totalGrade, ifnull(avg(cssi.grade), 0) avgGrade, ccgi.id courseGradecomposeIndicationId, ces.class_id classId from " + tableName + " cssi ");
		sql.append("inner join " + CcEduclassStudent.dao.tableName + " ces on ces.id = cssi.student_id ");
		sql.append("inner join " + CcEduclass.dao.tableName + " ce on ces.class_id ");
		sql.append("inner join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.id = cssi.gradecompose_indication_id and ccgi.course_gradecompose_id = ? and ccgi.indication_id in ( " + CollectionKit.convert(indaicationIds, ",") + ")" + " and ccgi.is_del = ? ");
		sql.append("where cssi.is_del = ? and ces.is_del = ? and ce.is_del = ? group by ces.class_id, cssi.gradecompose_indication_id ");
		return find(sql.toString(), courseGradecomposeId, DEL_NO, DEL_NO, DEL_NO, DEL_NO);
	}

	/**
	 * 根据学生编号和成绩组成明细编号返回对应学生成绩
	 * @param studentIds
	 * @param detailIds
	 * @return
	 */
	public List<CcScoreStuIndigrade> findByStudentIdsAndDetailIds(Long[] studentIds, Long[] detailIds) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select cssi.*, ccgi.indication_id from " + tableName + " cssi ");
		sql.append("inner join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.id = cssi.gradecompose_indication_id and ccgi.is_del = ? and cssi.student_id in (" + CollectionKit.convert(studentIds, ",") + ") ");
		param.add(DEL_NO);
		sql.append("inner join " + CcCourseGradeComposeDetail.dao.tableName + " ccgd on ccgd.course_gradecompose_id = ccgi.course_gradecompose_id and ccgd.is_del = ? and ccgd.id in (" + CollectionKit.convert(detailIds, ",") + ") " );
		param.add(DEL_NO);
		sql.append("inner join " + CcCourseGradecomposeDetailIndication.dao.tableName + " ccgdi on ccgdi.course_gradecompose_detail_id = ccgd.id and ccgdi.indication_id = ccgi.indication_id and ccgdi.is_del = ? ");
		param.add(DEL_NO);
		sql.append("where cssi.is_del = ? ");
		param.add(DEL_NO);
		sql.append("group by cssi.gradecompose_indication_id, cssi.student_id ");
		return find(sql.toString(), param.toArray());
	}

	/**
	 * 根据学生编号和成绩组成明细编号返回对应学生成绩
	 * @param studentIds
	 * @param detailIds
	 * @return
	 */
	public List<CcScoreStuIndigrade> findByStudentIdAndDetailId(Long[] studentIds, Long[] detailIds) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select cssi.*, ccgi.indication_id from " + tableName + " cssi ");
		sql.append("inner join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.id = cssi.gradecompose_indication_id and ccgi.is_del = ? and cssi.student_id in (" + CollectionKit.convert(studentIds, ",") + ") ");
		param.add(DEL_NO);
		sql.append("inner join " + CcCourseGradeComposeDetail.dao.tableName + " ccgd on ccgd.course_gradecompose_id = ccgi.course_gradecompose_id and ccgd.is_del = ? and ccgd.id in (" + CollectionKit.convert(detailIds, ",") + ") " );
		param.add(DEL_NO);
        sql.append("inner join " + CcCourseGradecomposeDetailIndication.dao.tableName + " ccgdi on ccgdi.course_gradecompose_detail_id = ccgd.id and ccgdi.indication_id = ccgi.indication_id and ccgdi.is_del = ? ");
        param.add(DEL_NO);
        sql.append("where cssi.is_del = ? ");
        param.add(DEL_NO);
		return find(sql.toString(), param.toArray());
	}

	/**
	 * 同一课程代码的课程在各个版本的成绩组成相关记录是否有修改，若有则判断为需要更新
	 * @param courseCode
	 * @param majorId 
	 * @return
	 */
	public boolean isNeedToUpdateByCourseCode(String courseCode, Long majorId) {
		StringBuilder sql = new StringBuilder("select count(cssig.id) from " + tableName + " cssig ");
		sql.append(returnStrB(courseCode,majorId));
		return Db.queryLong(sql.toString(), courseCode, DEL_NO, DEL_NO, majorId, DEL_NO, DEL_NO) > 0;
	}
	
	/**
	 * 通过courseCode和majorId得到版本编号和年级
	 * @param courseCode
	 * @param majorId
	 * @return
	 */
	public List<CcVersion> getVersionAndGrade(String courseCode, Long majorId){
		StringBuilder sql = new StringBuilder("select cv.id versionId, ctc.grade from " + tableName + " cssig ");
		sql.append(returnStrB(courseCode,majorId));
		return CcVersion.dao.find(sql.toString(), courseCode, DEL_NO, DEL_NO, majorId, DEL_NO, DEL_NO);
	}

	private StringBuilder returnStrB(String courseCode, Long majorId){
		StringBuilder sql = new StringBuilder("inner join cc_course_gradecompose_indication ccgi on ccgi.id = cssig.gradecompose_indication_id ");
		sql.append("inner join cc_course_gradecompose ccg on ccg.id = ccgi.course_gradecompose_id ");
		sql.append("inner join cc_teacher_course ctc on ctc.id = ccg.teacher_course_id ");
		sql.append("inner join cc_course cc on cc.id = ctc.course_id and cc.code = ? and cc.is_del = ? ");
		sql.append("inner join cc_version cv on cv.minor_version = cv.max_minor_version and cv.is_del = ? and cv.major_id = ? and cv.id = cc.plan_id ");
		sql.append("inner join cc_educlass ce on ce.teacher_course_id = ctc.id ");
		sql.append("inner join cc_educlass_student ces on ces.student_id = cssig.student_id and ces.class_id = ce.id and ces.is_del = ? ");
		sql.append("left join cc_report_educlass_grade creg on creg.gradecompose_indication_id = cssig.gradecompose_indication_id and creg.educlass_id = ce.id ");
		sql.append("where cssig.is_del = ? and (cssig.modify_date > creg.statistics_date or ccgi.weight != creg.weight or ccgi.max_score != creg.max_score or creg.id is null or ce.student_num_change_date > creg.statistics_date ) ");
		return sql;
	}

	/**
	 * 通过列表删除（根据studenti_id和gradecompose_indication_id）
	 * @param scoreStuIndigradeAddList
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年10月18日 上午11:42:15 
	 */
	public boolean deleteByModel(List<CcScoreStuIndigrade> scoreStuIndigradeAddList) {
		if(scoreStuIndigradeAddList == null || scoreStuIndigradeAddList.size() == 0) {
			return true;
		}
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("update " + tableName + " cssi ");
		sql.append("set cssi.is_del = ? where 1 = 1 and (");
		params.add(Boolean.TRUE);
		for(int i = 0; i < scoreStuIndigradeAddList.size(); i++) {
			CcScoreStuIndigrade scoreStuIndigrade = scoreStuIndigradeAddList.get(i);
			if(i != 0) {
				sql.append("or ");
			}
			sql.append("(cssi.gradecompose_indication_id = ? ");
			params.add(scoreStuIndigrade.getLong("gradecompose_indication_id"));
			
			sql.append("and cssi.student_id = ? )");
			params.add(scoreStuIndigrade.getLong("student_id"));
		}
		sql.append(")");
		return Db.update(sql.toString(), params.toArray()) >= 0;
	}
	/*
	 * @param gradecomposeIndicationIdList
	 * @return boolean
	 * @author Gejm
	 * @description: 根据开课课程成绩组成元素与课程目标关联清空成绩
	 * @date 2020/12/10 16:58
	 */
	public boolean deleAllGradecomposeScore(List<Long> gradecomposeIndicationIdList){
		if(gradecomposeIndicationIdList == null || gradecomposeIndicationIdList.isEmpty()) {
			return true;
		}
		StringBuilder sql = new StringBuilder("update " + tableName + " cssi ");
		sql.append("set cssi.is_del=1 where cssi.gradecompose_indication_id in (" + CollectionKit.convert(gradecomposeIndicationIdList, ",") + ") ");
		return Db.update(sql.toString()) >= 0;
	}

	/**
	 * 清空这些开课课程组成存在学生成绩
	 * @param id
	 * @author SY
	 * @param date 
	 * @date 2017年11月9日
	 * @return
	 */
	public boolean deleteByCourseGradecomposeIds(List<Long> courseGradecomposeIds, Date date) {
		if(courseGradecomposeIds == null || courseGradecomposeIds.isEmpty()) {
			return true;
		}
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("update " + tableName + " cssi ");
		sql.append("left join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.id = cssi.gradecompose_indication_id ");
		sql.append("set cssi.is_del = ? ");
		params.add(Boolean.TRUE);
		sql.append(",cssi.modify_date = ? ");
		params.add(date);
		sql.append("where ccgi.course_gradecompose_id in (" + CollectionKit.convert(courseGradecomposeIds, ",") + ") ");
		sql.append("and cssi.is_del = ? ");
		params.add(Boolean.FALSE);
		sql.append("and ccgi.is_del = ? ");
		params.add(Boolean.FALSE);
		sql.append("and cssi.grade is not null ");
		return Db.update(sql.toString(), params.toArray()) >= 0 ;
	}

	/**
	 * @description: 查询 某些教学班 在 某些开课成绩组成下的 总分和平均分
	 * @param eduClassIds    教学班ID集合  如果 ==null 或者 isEmpty 不做限制
	 * @param courseGradecomposeIds 开课成绩组成ID集合 如果 ==null 或者 isEmpty 不做限制
	 * @return
	 */
	public List<CcScoreStuIndigrade> findClassGradeByEduclassIdsAndCourseGradecomposeIds(List<Long> eduClassIds, List<Long> courseGradecomposeIds) {
		return findClassGradeByEduclassIdsAndCourseGradecomposeIds(eduClassIds, courseGradecomposeIds, null);
	}

	/**
	 * // BUG 252 因为要求增加剔除学生功能，所以计数方式修改 SY 2018年1月30日
	 * @description: 查询 某些教学班 在 某些开课成绩组成下的 总分和平均分(剔除掉一些不用计算的学生)
	 * @param eduClassIds    教学班ID集合  如果 ==null 或者 isEmpty 不做限制
	 * @param courseGradecomposeIds 开课成绩组成ID集合 如果 ==null 或者 isEmpty 不做限制
	 * @param isCaculate
	 * 			是否计算
	 * @return
	 */
	public List<CcScoreStuIndigrade> findClassGradeByEduclassIdsAndCourseGradecomposeIds(List<Long> eduClassIds, List<Long> courseGradecomposeIds, Boolean isCaculate) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select sum(studentScore.grade) totalScore, avg(studentScore.grade) avgScore, studentScore.eduClassId, studentScore.gradeComposeIndicationId ");
		sql.append("from ( ");
		sql.append("select ces.student_id studentId, ifnull(cssi.grade, 0) grade, ce.id eduClassId, ctc.id teacherCourseId, ccg.id courseComposeId, ccgi.id gradeComposeIndicationId from " + CcEduclassStudent.dao.tableName + " ces ");
		sql.append("inner join " + CcEduclass.dao.tableName + " ce on ce.id = ces.class_id and ce.is_del = ? ");
		params.add(DEL_NO);

		if (eduClassIds != null && !eduClassIds.isEmpty()) {
			sql.append("and ce.id in (" + CollectionKit.convert(eduClassIds, ",") + ") ");
		}

		sql.append("inner join " + CcTeacherCourse.dao.tableName + " ctc on ctc.id = ce.teacher_course_id and ctc.is_del = ? ");
		params.add(DEL_NO);
		sql.append("inner join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.teacher_course_id = ctc.id and ccg.is_del = ? ");
		params.add(DEL_NO);

		if (courseGradecomposeIds != null && !courseGradecomposeIds.isEmpty()) {
			sql.append("and ccg.id in (" + CollectionKit.convert(courseGradecomposeIds, ",") + ") " );
		}

		sql.append("inner join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.course_gradecompose_id = ccg.id and ccgi.is_del = ? ");
		params.add(DEL_NO);
		sql.append("left join " + CcScoreStuIndigrade.dao.tableName + " cssi on cssi.student_id = ces.student_id and cssi.gradecompose_indication_id = ccgi.id and cssi.is_del = ? ");
		params.add(DEL_NO);
		sql.append("where ces.is_del = ? ");
		params.add(DEL_NO);
		// BUG 252 因为要求增加剔除学生功能，所以计数方式修改 SY 2018年1月30日
		if(isCaculate != null) {
			sql.append("and ces.is_caculate = ? ");
			params.add(isCaculate);
		}
		sql.append(") as studentScore ");
		sql.append("group by studentScore.eduClassId, studentScore.gradeComposeIndicationId ");

		return find(sql.toString(), params.toArray());
	}

	/**
	 * 根据level_detail_id更新所有学生成绩。
	 * 因为一个培养计划下用一套，所以这里只会影响一个版本。
	 * @param grade
	 * 			分数
	 * @param levelDetailId
	 * 			等级明细编号
	 * @return
	 * @author shaye
	 * @date 2020年2月21日16:02:48
	 */
	public Integer batchUpdateStudentGrade(BigDecimal grade, Long levelDetailId) {
		if(grade == null || levelDetailId == null) {
			return 0;
		}
		Date date = new Date();
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("update " + tableName + " cssi ");
		sql.append("set cssi.grade = ? ");
		params.add(grade);
		sql.append(",cssi.modify_date = ? ");
		params.add(date);
		sql.append("where cssi.is_del = ? ");
		params.add(Boolean.FALSE);
		sql.append("and cssi.level_detail_id = ? ");
		params.add(levelDetailId);
		return Db.update(sql.toString(), params.toArray()) ;
	}

	/**
	 * 找到这等级明细编号影响的所有教学班编号
	 * @param levelDetailId
	 * @return
	 * @author shaye
	 * @date 2020年2月21日16:02:48
	 */
	public List<CcScoreStuIndigrade> findByLevelDetailId(Long levelDetailId) {
		StringBuilder sql = new StringBuilder("select cssi.*, ce.id educlassId from " + tableName + " cssi ");
		List<Object> params = new ArrayList<>();
		sql.append("left join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.id = cssi.gradecompose_indication_id and ccgi.is_del = ? ");
		params.add(Boolean.FALSE);
		sql.append("left join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.id = ccgi.course_gradecompose_id and ccg.is_del = ? ");
		params.add(Boolean.FALSE);
		sql.append("left join " + CcTeacherCourse.dao.tableName + " ctc on ctc.id = ccg.teacher_course_id and ctc.is_del = ? ");
		params.add(Boolean.FALSE);
		sql.append("inner join " + CcEduclass.dao.tableName + " ce on ce.teacher_course_id = ctc.id and ce.is_del = ? ");
		params.add(Boolean.FALSE);
		
		sql.append("where cssi.level_detail_id = ? ");
		params.add(levelDetailId);
		params.add("group by ce.id ");
		
		return find(sql.toString(),  params.toArray());
	}
	/*
	 * @param courseGradecomposeId
		 * @param edClassId
	 * @return java.util.List<com.gnet.model.admin.CcScoreStuIndigrade>
	 * @author Gejm
	 * @description: 找到这个班级这个成绩组成的成绩
	 * @date 2020/7/2 11:26
	 */
	public List<CcScoreStuIndigrade> findClassGrade(Long courseGradecomposeId,Long edClassId){
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select a.*,d.content from  " + tableName + " a ");
		sql.append("left join cc_course_gradecompose_indication b on a.gradecompose_indication_id=b.id ");
		sql.append("left join cc_educlass_student c on a.student_id=c.student_id and c.is_del=? " );
		params.add(Boolean.FALSE);
		sql.append("left join cc_indication d on b.indication_id=d.id ");
		sql.append("where course_gradecompose_id=? and c.class_id=? and a.is_del=?  ");
		params.add(courseGradecomposeId);
		params.add(edClassId);
		params.add(Boolean.FALSE);
		return find(sql.toString(),  params.toArray());

	}
	/*
	 * @param eduClassIds
		 * @param courseGradecomposeIds
		 * @param isCaculate
	 * @return java.util.List<com.gnet.model.admin.CcScoreStuIndigrade>
	 * @author Gejm
	 * @description: 统计每个GradecomposeId的总分
	 * @date 2020/7/27 10:53
	 */
	public List<CcScoreStuIndigrade> findCourseGradecomposeIdsGrade(List<Long> eduClassIds, List<Long> courseGradecomposeIds, Boolean isCaculate) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select sum(studentScore.grade) totalGrade, studentScore.gradecomposeIndicationId,studentScore.max_score maxScore,studentScore.weight ");
		sql.append("from ( ");
		sql.append("select ces.student_id studentId, ifnull(cssi.grade, 0) grade, ce.id eduClassId, ctc.id teacherCourseId, ccg.id courseComposeId, ccgi.id gradeComposeIndicationId,ccgi.max_score,ccgi.weight from " + CcEduclassStudent.dao.tableName + " ces ");
		sql.append("inner join " + CcEduclass.dao.tableName + " ce on ce.id = ces.class_id and ce.is_del = ? ");
		params.add(DEL_NO);

		if (eduClassIds != null && !eduClassIds.isEmpty()) {
			sql.append("and ce.id in (" + CollectionKit.convert(eduClassIds, ",") + ") ");
		}

		sql.append("inner join " + CcTeacherCourse.dao.tableName + " ctc on ctc.id = ce.teacher_course_id and ctc.is_del = ? ");
		params.add(DEL_NO);
		sql.append("inner join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.teacher_course_id = ctc.id and ccg.is_del = ? ");
		params.add(DEL_NO);

		if (courseGradecomposeIds != null && !courseGradecomposeIds.isEmpty()) {
			sql.append("and ccg.id in (" + CollectionKit.convert(courseGradecomposeIds, ",") + ") " );
		}

		sql.append("inner join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.course_gradecompose_id = ccg.id and ccgi.is_del = ? ");
		params.add(DEL_NO);
		sql.append("left join " + CcScoreStuIndigrade.dao.tableName + " cssi on cssi.student_id = ces.student_id and cssi.gradecompose_indication_id = ccgi.id and cssi.is_del = ? ");
		params.add(DEL_NO);
		sql.append("where ces.is_del = ? and cssi.grade is not null ");
		params.add(DEL_NO);
		// BUG 252 因为要求增加剔除学生功能，所以计数方式修改 SY 2018年1月30日
		if(isCaculate != null) {
			sql.append("and ces.is_caculate = ? ");
			params.add(isCaculate);
		}
		sql.append(") as studentScore ");
		sql.append("group by  studentScore.gradeComposeIndicationId,studentScore.max_score,studentScore.weight ");

		return find(sql.toString(), params.toArray());
	}


}
