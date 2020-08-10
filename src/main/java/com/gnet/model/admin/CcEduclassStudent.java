package com.gnet.model.admin;

import java.util.ArrayList;
import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;

/**
 * @type model
 * @description 教学班学生表操作，包括对数据的增删改查与列表
 * @table cc_educlass_student
 * @author SY
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_educlass_student")
public class CcEduclassStudent extends DbModel<CcEduclassStudent> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcEduclassStudent dao = new CcEduclassStudent();

	/**
	 * 获取教学班下学生信息
	 *
	 * @param eduClassId
	 * @return
	 */
	public List<CcEduclassStudent> findStuInfoByClassId(Long eduClassId) {
		StringBuilder sql = new StringBuilder("select cecs.*, cs.student_no student_no from " + tableName + " cecs ");
		sql.append("left join cc_student cs on cs.id = cecs.student_id ");
		sql.append("where cecs.class_id = ? and cecs.is_del=0");
		return find(sql.toString(), eduClassId);
	}

	/**
	 * 学生教学班关联表
	 *
	 * @param eduClassIds
	 * @return
	 */
	public List<CcEduclassStudent> findByEduClassIds(Long[] eduClassIds) {
		StringBuilder sql = new StringBuilder("select class_id, count(student_id) student_size from " + tableName + " ");
		sql.append("where class_id in (" + CollectionKit.convert(eduClassIds, ",") + ") and is_del = ? ");
		sql.append("group by class_id");
		return CcEduclassStudent.dao.find(sql.toString(), DEL_NO);
	}

	/**
	 * 获取某个年级学生参与过课程的分数情况（考核课程）
	 * @param versionId 版本编号
	 * @param grade 年级
	 * @return
	 */
	public List<CcEduclassStudent> findAllStudentScore(Long versionId, Integer grade) {
		StringBuilder sql = new StringBuilder("select ci.expected_value, ct.start_year, ccgi.course_gradecompose_id, ccgi.indication_id, "
				+ "ctc.id teacher_course_id, ct.end_year, ct.term, ct.term_type, cic.indication_id indicatorPointId, "
				+ "cic.id indication_course_id, cs.id student_id, cssi.grade score, ccgi.weight score_weight, "
				+ "ccgi.max_score max_score, cic.weight indication_weight, ctc.result_type result_type, "
				+ "ctc.grade course_grade, cc.direction_id course_direction_id, cc.course_group_id course_group_id, "
				+ "ce.id educlass_id from " + tableName + " ces ");
		sql.append("inner join cc_student cs on cs.is_del = ? and cs.grade = ? and ces.student_id = cs.id ");
		sql.append("inner join cc_educlass ce on ce.is_del = ? and ce.id = ces.class_id ");
		sql.append("inner join cc_teacher_course ctc on ctc.result_type = ? and ctc.id = ce.teacher_course_id ");
		sql.append("inner join cc_term ct on ct.id = ctc.term_id ");
		sql.append("inner join cc_course cc on cc.plan_id = ? and cc.id = ctc.course_id ");
		sql.append("left join cc_course_gradecompose ccg on ccg.teacher_course_id = ctc.id ");
		sql.append("left join cc_course_gradecompose_indication ccgi on ccgi.course_gradecompose_id = ccg.id ");
		sql.append("inner join " + CcIndication.dao.tableName + " ci on ci.id = ccgi.indication_id ");
		sql.append("inner join " + CcCourseTargetIndication.dao.tableName + " ccti on ccti.indication_id = ci.id ");
		sql.append("inner join cc_indication_course cic on cic.id = ccti.indication_course_id and cic.course_id = ctc.course_id and cic.is_del = ? ");
		sql.append("inner join cc_score_stu_indigrade cssi on cssi.is_del = ? and cssi.student_id = ces.student_id and cssi.gradecompose_indication_id = ccgi.id ");
		sql.append("where ces.is_del = ? ");
		sql.append("order by cs.id asc, cic.id asc, ctc.grade desc ");
		// 排序，原先的排序基础上，如果是同一门课的不同班级，增加按照优先：上课学年最近，短学期，学期学期最近
		sql.append(", ct.start_year desc, ct.term_type desc, ct.term desc ");
		return find(sql.toString(), DEL_NO, grade, DEL_NO, CcTeacherCourse.RESULT_TYPE_SCORE, versionId, DEL_NO, DEL_NO, DEL_NO);
	}

	/**
	 * 获取某个年级学生参与过课程的分数情况（考评点课程）
	 *
	 * @param versionId 版本编号
	 * @param grade	年级
	 * @return
	 */
	public List<CcEduclassStudent> findAllStudentEvalute(Long versionId, Integer grade) {
		StringBuilder sql = new StringBuilder("select cic.indication_id indication_id, ctc.id teacher_course_id,cee.id evalute_id, cet.id evalute_type_id, cet.percentage, cic.id indication_course_id, cs.id student_id, cel.score score, cee.weight score_weight, cic.weight indication_weight, ctc.result_type result_type, ctc.grade course_grade, cc.direction_id course_direction_id, " +
				"cc.course_group_id course_group_id, ce.id educlass_id from " + tableName + " ces ");
		sql.append("inner join cc_student cs on cs.is_del = ? and cs.grade = ? and ces.student_id = cs.id ");
		sql.append("inner join cc_educlass ce on ce.is_del = ? and ce.id = ces.class_id ");
		sql.append("inner join cc_teacher_course ctc on ctc.result_type = ? and ctc.id = ce.teacher_course_id ");
		sql.append("inner join cc_course cc on cc.plan_id = ? and cc.id = ctc.course_id ");
		sql.append("left join cc_evalute cee on cee.teacher_course_id = ctc.id ");
		sql.append("left join " + CcEvaluteType.dao.tableName + " cet on cee.evalute_type_id = cet.id ");
		sql.append("left join cc_student_evalute cse on cse.is_del = ? and cse.evalute_id = cee.id and cse.student_id = cs.id ");
		sql.append("inner join cc_indication_course cic on cic.indication_id = cee.indication_id and cic.course_id = ctc.course_id and cic.is_del = ? ");
		sql.append("inner join cc_evalute_level cel on cel.id = cse.level_id ");
		sql.append("where ces.is_del = ? ");
		sql.append("order by cs.id asc, cic.id asc, ctc.grade desc ");
		return find(sql.toString(), DEL_NO, grade, DEL_NO, CcTeacherCourse.RESULT_TYPE_EVALUATE, versionId, DEL_NO, DEL_NO, DEL_NO);
	}

	/**
	 * 获得学生课程方向上的课程数
	 *
	 * @param versionId 版本编号
	 * @param grade 年级
	 * @return
	 */
	public List<CcEduclassStudent> countStudentDirectionCourse(Long versionId, Integer grade) {
		StringBuilder sql = new StringBuilder("select ces.student_id student_id, cc.direction_id direction_id, count(cc.direction_id) course_count, cms.major_direction_id student_direction_id from " + tableName + " ces ");
		sql.append("inner join cc_student cs on cs.is_del = ? and cs.grade = ? and cs.id = ces.student_id ");
		sql.append("left join cc_major_student cms on cms.version_id = ? and cms.student_id = cs.id ");
		sql.append("inner join cc_educlass ce on ce.is_del = ? and ce.id = ces.class_id ");
		sql.append("inner join cc_teacher_course ctc on ctc.id = ce.teacher_course_id ");
		sql.append("inner join cc_course cc on cc.plan_id = ? and cc.direction_id is not null and cc.id = ctc.course_id ");
		sql.append("group by ces.student_id, cc.direction_id ");
		sql.append("order by ces.student_id asc, course_count desc, cc.direction_id asc");
		return find(sql.toString(), DEL_NO, grade, versionId, DEL_NO, versionId);
	}

	/**
	 * 获取学生成绩项(剔除掉一些不用计算的学生)
	 *
	 * @editor SY
	 * @param eduClassId
	 * 			教学班编号
	 * @param isCaculate
	 * 			是否计算
	 * @return
	 */
	public List<CcEduclassStudent> findScoreDetailByClassId(Long eduClassId, Boolean isCaculate) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select ccgi.id gradecomposeIndicationId, ccgi.indication_id indicationId, ccgi.course_gradecompose_id courseGradecomposeId, cs.student_no student_no, cs.name student_name, cssig.* from " + tableName + " ces ");
		sql.append("inner join " + CcStudent.dao.tableName + " cs on cs.id = ces.student_id and cs.is_del = ? ");
		params.add(DEL_NO);
		sql.append("inner join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi ");
		sql.append("inner join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.id = ccgi.course_gradecompose_id and ccg.is_del = ? ");
		params.add(DEL_NO);
		sql.append("inner join " + CcTeacherCourse.dao.tableName + " ctc on ctc.id = ccg.teacher_course_id and ctc.is_del = ? ");
		params.add(DEL_NO);
		sql.append("inner join " + CcEduclass.dao.tableName + " ce on ce.teacher_course_id = ctc.id and ce.id = ? ");
		params.add(eduClassId);
		sql.append("inner join " + CcScoreStuIndigrade.dao.tableName + " cssig on cssig.student_id = ces.student_id and cssig.gradecompose_indication_id = ccgi.id and cssig.is_del = ? ");
		params.add(DEL_NO);
		sql.append("where ces.class_id = ? and ces.is_del = ? ");
		params.add(eduClassId);
		params.add(DEL_NO);
		if(isCaculate != null) {
			sql.append("and ces.is_caculate = ? ");
			params.add(isCaculate);
		}
		sql.append("order by cs.student_no asc ");
		return find(sql.toString(), params.toArray());
	}

	/**
	 * 获取学生成绩项(剔除掉一些不用计算的学生)
	 *
	 * @param eduClassId
	 * 			教学班编号
	 * @param isCaculate
	 * 			是否计算
	 * @return
	 */
	public List<CcEduclassStudent> findScoreDetailWithTypeAndIndicationByClassId(Long eduClassId, Boolean isCaculate) {
		StringBuilder sql = new StringBuilder("select ccgi.id gradecomposeIndicationId, ccgi.indication_id, ccgi.max_score, ccg.other_score gradecompose_other_score, ccg.percentage, cesg.other_score, ccgi.course_gradecompose_id, cess.remark student_study_remark, cess.is_retake, cs.student_no student_no, cs.name student_name, cssig.* from " + tableName + " ces ");
		sql.append("inner join cc_student cs on cs.id = ces.student_id and cs.is_del = ? ");
		sql.append("inner join cc_course_gradecompose_indication ccgi ");
		sql.append("inner join cc_course_gradecompose ccg on ccg.id = ccgi.course_gradecompose_id and ccg.is_del = ? ");
		sql.append("inner join cc_teacher_course ctc on ctc.id = ccg.teacher_course_id and ctc.is_del = ? ");
		sql.append("inner join cc_educlass ce on ce.teacher_course_id = ctc.id and ce.id = ? ");
		sql.append("left join cc_score_stu_indigrade cssig on cssig.student_id = ces.student_id and cssig.gradecompose_indication_id = ccgi.id and cssig.is_del = ? ");
		sql.append("left join "+ CcEduclassStudentStudy.dao.tableName +" cess on cess.student_id = ces.student_id and cess.class_id = ces.class_id and cess.is_del = ? ");
		sql.append("left join "+ CcEduclassStudentGradecompose.dao.tableName +" cesg on cesg.student_id = ces.student_id and cesg.class_id = ces.class_id and cesg.course_gradecompose_id = ccg.id and cesg.is_del = ? ");
		sql.append("where ces.class_id = ? and ces.is_del = ? ");
		sql.append("and ces.is_caculate = ?  ");
		sql.append("order by cs.student_no asc ");
		return find(sql.toString(), DEL_NO, DEL_NO, DEL_NO, eduClassId, DEL_NO, DEL_NO, DEL_NO, eduClassId, DEL_NO, isCaculate);
	}

	/**
	 * 获得学生评价成绩项
	 *
	 * @param eduClassId
	 * @return
	 */
	public List<CcEduclassStudent> findEvaluteDetailByClassId(Long eduClassId) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select cse.*, cs.student_no student_no, cs.name student_name, cel.id level_id, cel.score level_score, cel.level_name level_name from " + CcStudent.dao.tableName + " cs ");
		sql.append("inner join cc_educlass_student ces on ces.student_id = cs.id and ces.is_del = ? and ces.class_id = ? ");
		sql.append("inner join cc_evalute cee  ");
		sql.append("inner join cc_teacher_course ctc on ctc.id = cee.teacher_course_id and ctc.is_del = ? ");
		sql.append("inner join cc_educlass ce on ce.teacher_course_id = ctc.id and ce.id = ? and ce.is_del = ? ");
		sql.append("left join cc_student_evalute cse on cse.evalute_id = cee.id and cse.student_id = ces.student_id and cse.is_del = ? ");
		sql.append("left join cc_evalute_level cel on cel.id = cse.level_id and cel.is_del = ? ");
		sql.append("where cs.is_del = ? ");
		sql.append("order by cs.student_no asc ");
		param.add(DEL_NO);
		param.add(eduClassId);
		param.add(DEL_NO);
		param.add(eduClassId);
		param.add(DEL_NO);
		param.add(DEL_NO);
		param.add(DEL_NO);
		param.add(DEL_NO);
		return  find(sql.toString(), param.toArray());
	}
	
	/**
	 * 获取学生成绩项(剔除掉一些不用计算的学生)
	 *
	 * @param teacherCourseIdList
	 * @param isCaculate
	 * 			是否计算
	 * @return
	 */
	public List<CcEduclassStudent> findScoreDetailByTeacherCourseId(List<Long> teacherCourseIdList, Boolean isCaculate) {
		if(teacherCourseIdList == null || teacherCourseIdList.isEmpty()) {
			return new ArrayList<>();
		}
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select ccgi.id gradecomposeIndicationId, cs.student_no student_no, "
				+ "cs.name student_name, ccg.gradecompose_id gradecomposeId, cic.indication_id indicatorPointId, "
				+ "ci.id indicationId,cssig.* from " + tableName + " ces ");
		sql.append("inner join " + CcStudent.dao.tableName + " cs on cs.id = ces.student_id and cs.is_del = ? ");
		params.add(DEL_NO);
		sql.append("inner join " + CcEduclass.dao.tableName + " ce on ce.id = ces.class_id and ce.is_del = ? ");
		params.add(DEL_NO);
		sql.append("inner join " + CcTeacherCourse.dao.tableName + " ctc on ctc.id = ce.teacher_course_id and ctc.is_del = ? ");
		params.add(DEL_NO);
		sql.append("inner join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.teacher_course_id = ctc.id and ccg.is_del = ? ");
		params.add(DEL_NO);
		sql.append("inner join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.course_gradecompose_id = ccg.id and ccgi.is_del = ? ");
		params.add(DEL_NO);
		sql.append("inner join " + CcIndication.dao.tableName + " ci on ci.id = ccgi.indication_id and ci.is_del = ? ");
		params.add(DEL_NO);
		sql.append("inner join " + CcCourseTargetIndication.dao.tableName + " ccti on ci.id = ccti.indication_id ");
		sql.append("inner join " + CcIndicationCourse.dao.tableName + " cic on cic.id = ccti.indication_course_id and cic.is_del = ? ");
		params.add(DEL_NO);
		sql.append("left join " + CcScoreStuIndigrade.dao.tableName + " cssig on cssig.student_id = ces.student_id and cssig.gradecompose_indication_id = ccgi.id and cssig.is_del = ? ");
		params.add(DEL_NO);
		sql.append("where ctc.id in (" + CollectionKit.convert(teacherCourseIdList, ",") + ") and ces.is_del = ? ");
		params.add(DEL_NO);
		if(isCaculate != null) {
			sql.append("and ces.is_caculate = ? ");
			params.add(isCaculate);			
		}
		sql.append("order by cs.student_no asc ");
		return find(sql.toString(), params.toArray());
	}

	/**
	 * 获得学生评价成绩项
	 *
	 * @param teacherCourseIdList
	 * @return
	 */
	public List<CcEduclassStudent> findEvaluteDetailByTeacherCourseId(List<Long> teacherCourseIdList) {
		if(teacherCourseIdList == null || teacherCourseIdList.isEmpty()) {
			return new ArrayList<>();
		}
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select cse.*, cs.student_no student_no, cs.name student_name, cel.id level_id, cel.score level_score, cel.level_name level_name from " + CcStudent.dao.tableName + " cs ");
		sql.append("inner join cc_educlass_student ces on ces.student_id = cs.id and ces.is_del = ? and ces.class_id = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_evalute cee  ");
		sql.append("inner join cc_teacher_course ctc on ctc.id = cee.teacher_course_id and ctc.is_del = ? and cee.teacher_course_id in ("+ CollectionKit.convert(teacherCourseIdList, ",")+") ");
		param.add(DEL_NO);
		sql.append("inner join cc_educlass ce on ce.teacher_course_id = ctc.id  and ce.is_del = ? ");
		param.add(DEL_NO);
		sql.append("left join cc_student_evalute cse on cse.evalute_id = cee.id and cse.student_id = ces.student_id and cse.is_del = ? ");
		param.add(DEL_NO);
		sql.append("left join cc_evalute_level cel on cel.id = cse.level_id and cel.is_del = ? ");
		param.add(DEL_NO);
		sql.append("where cs.is_del = ? ");
		param.add(DEL_NO);
		sql.append("order by cs.student_no asc ");
		return  find(sql.toString(), param.toArray());
	}
	
	/**
	 * 获得学生评价成绩项，用于显示平时成绩、期中成绩、期末成绩、实验成绩。
	 *
	 * @param eduClassId
	 * @return
	 * @author SY
	 * @version 创建时间：2017年8月11日 上午10:00:29 
	 */
	public List<CcEduclassStudent> findEvaluteDetailWithTypeAndIndicationByClassId(Long eduClassId) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select cse.*, cess.remark student_study_remark, cess.is_retake,cel.level, cee.indication_id, cet.type evalute_type, cs.student_no student_no, cs.name student_name, cel.id level_id, cel.score level_score, cel.level_name level_name from " + CcStudent.dao.tableName + " cs ");
		sql.append("inner join cc_educlass_student ces on ces.student_id = cs.id and cs.is_del = ? and ces.class_id = ? ");
		param.add(DEL_NO);
		param.add(eduClassId);
		sql.append("inner join cc_evalute cee  ");
		sql.append("inner join "+ CcEvaluteType.dao.tableName +" cet on cet.id = cee.evalute_type_id and cet.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_teacher_course ctc on ctc.id = cee.teacher_course_id and ctc.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_educlass ce on ce.teacher_course_id = ctc.id and ce.id = ? and ce.is_del = ? ");
		param.add(eduClassId);
		param.add(DEL_NO);
		sql.append("left join cc_student_evalute cse on cse.evalute_id = cee.id and cse.student_id = ces.student_id ");
		sql.append("and cse.is_del = ? ");
		param.add(DEL_NO);
		sql.append("left join cc_evalute_level cel on cel.id = cse.level_id and cel.is_del = ? ");
		param.add(DEL_NO);
		sql.append("left join "+ CcEduclassStudentStudy.dao.tableName +" cess on cess.student_id = ces.student_id and cess.class_id = ces.class_id and cess.is_del = ? ");
		param.add(DEL_NO);
		sql.append("where cs.is_del = ? ");
		param.add(DEL_NO);
		sql.append("order by cs.student_no asc, cet.type asc ");
		
		return  find(sql.toString(), param.toArray());
	}

	/**
	 * 教师开课课程编号
	 * @param teacherCourseId
	 * @param isCludeSum
	 *         是否求学生成绩组成的和
	 * @return
	 */
	public List<CcEduclassStudent> findByTeacherCourseId(Long teacherCourseId, Boolean isCludeSum) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select cs.student_no studentNo, cs.name studentName, class.name className, ce.educlass_name educlassName, ccgi.id gradecomposeIndicationId, cssig.grade," +
				" ccg.id courseGradecomposeId, ccg.gradecompose_id gradecomposeId, cic.indication_id indicationId ");
		if(isCludeSum){
			sql.append(", sum(cssig.grade) totalScore");
		}
		sql.append(" from cc_educlass_student ces ");
		sql.append("inner join cc_educlass ce on ce.id = ces.class_id and ce.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_student cs on cs.id = ces.student_id and cs.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join sys_office class on class.id = cs.class_id and class.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_course_gradecompose ccg on ccg.teacher_course_id = ce.teacher_course_id and ccg.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_teacher_course ctc on ctc.id = ? ");
		param.add(teacherCourseId);
		sql.append("inner join cc_course cc on cc.id = ctc.course_id and cc.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_indication_course cic on cic.course_id = cc.id and cic.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_course_gradecompose_indication ccgi on ccgi.course_gradecompose_id = ccg.id  and ccgi.indication_id = cic.indication_id and ccgi.is_del = ? ");
		param.add(DEL_NO);
		sql.append("left join cc_score_stu_indigrade cssig on cssig.gradecompose_indication_id = ccgi.id and cssig.student_id = ces.student_id and (cssig.is_del is null or cssig.is_del = ?) ");
		param.add(DEL_NO);
		sql.append("where ces.is_del = ? ");
		param.add(DEL_NO);
		if(isCludeSum){
			sql.append("group by ccg.id, cs.student_no ");
		}
		sql.append("order by ccg.id, cs.student_no ");

		return find(sql.toString(), param.toArray());
	}

	/**
	 * 对应开课课程指标点下学生总成绩
	 * @param teacherCourseId
	 * @return
	 */
	public List<CcEduclassStudent> findStudentScores(Long teacherCourseId) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select ccgi.id gradecomposeIndicationId, ccg.gradecompose_id gradecomposeId, cic.indication_id indicationId, sum(cssig.grade) totalScore from cc_educlass_student ces ");
		sql.append("inner join cc_educlass ce on ce.id = ces.class_id and ce.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_student cs on cs.id = ces.student_id and cs.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join sys_office class on class.id = cs.class_id and class.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_course_gradecompose ccg on ccg.teacher_course_id = ce.teacher_course_id and ccg.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_teacher_course ctc on ctc.id = ? ");
		param.add(teacherCourseId);
		sql.append("inner join cc_course cc on cc.id = ctc.course_id and cc.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_indication_course cic on cic.course_id = cc.id and cic.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_course_gradecompose_indication ccgi on ccgi.course_gradecompose_id = ccg.id and ccgi.indication_id = cic.indication_id and ccgi.is_del = ? ");
		param.add(DEL_NO);
		sql.append("left join cc_score_stu_indigrade cssig on cssig.gradecompose_indication_id = ccgi.id and cssig.student_id = ces.student_id and (cssig.is_del is null or cssig.is_del = ?) ");
		param.add(DEL_NO);
		sql.append("where ces.is_del = ? ");
		param.add(DEL_NO);
		sql.append("group by ccgi.id ");

		return find(sql.toString(), param.toArray());
	}

	/**
	 * 获取某个教师开课课程下的所有的学生数量
	 * @param teacherCourseIds
	 * @return
	 */
	public Long findStudentCounts(Long[] teacherCourseIds) {
		return findStudentCounts(teacherCourseIds, null);
	}
	
	/**
	 * // BUG 252 因为要求增加剔除学生功能，所以计数方式修改 SY 2018年1月30日
	 * 获取某个教师开课课程下的所有的学生数量
	 * @param teacherCourseIds
	 * @param isCaculate
	 * 			是否计算
	 * @return
	 */
	public Long findStudentCounts(Long[] teacherCourseIds, Boolean isCaculate) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select count(1) from " + tableName + " ces ");
		sql.append("inner join cc_educlass ce on ce.id = ces.class_id and ce.is_del = false and ce.teacher_course_id in (" + CollectionKit.convert(teacherCourseIds, ",") + ") ");
		sql.append("inner join cc_student cs on cs.id = ces.student_id and cs.is_del = ? ");
		params.add(DEL_NO);
		sql.append("where ces.is_del = ? ");
		params.add(DEL_NO);
		if(isCaculate != null) {
			sql.append("and ces.is_caculate = ? ");
			params.add(isCaculate);			
		}
		return Db.queryLong(sql.toString(), params.toArray());
	}

	/**
	 * // BUG 252 因为要求增加剔除学生功能，所以计数方式修改 SY 2018年1月30日
	 * 通过教学班id列表，获取每个教学班下，有多少个学生
	 * @param ccEduclassIdList
	 * @param isCaculate
	 * 			是否计算
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年12月1日 上午11:05:16 
	 */
	public List<CcEduclassStudent> countStudentNum(List<Long> ccEduclassIdList, Boolean isCaculate) {
		if(ccEduclassIdList == null || ccEduclassIdList.isEmpty()) {
			return new ArrayList<>();
		}
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select count(ces.id) studentNum, ces.class_id eduClassId from " + tableName + " ces ");
		sql.append("inner join cc_educlass ce on ce.id = ces.class_id and ce.is_del = ? ");
		params.add(DEL_NO);
		sql.append("inner join cc_student cs on cs.id = ces.student_id and cs.is_del = ? ");
		params.add(DEL_NO);
		sql.append("where ces.is_del = ? ");
		params.add(DEL_NO);
		sql.append("and ces.class_id in ( " + CollectionKit.convert(ccEduclassIdList, ",") + " ) ");
		if(isCaculate != null) {
			sql.append("and ces.is_caculate = ? ");
			params.add(isCaculate);
		}
		sql.append("group by ces.class_id ");
		return find(sql.toString(), params.toArray());
	}
	
	/**
	 * 通过教学班id列表，获取每个教学班下，有多少个学生
	 * @param ccEduclassIdList
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年12月1日 上午11:05:16 
	 */
	public List<CcEduclassStudent> countStudentNum(List<Long> ccEduclassIdList) {
		return countStudentNum(ccEduclassIdList, null);
	}
}
