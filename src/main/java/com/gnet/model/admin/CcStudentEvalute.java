package com.gnet.model.admin;

import java.util.Date;
import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;

/**
 * @type model
 * @description 学生考评点成绩表操作，包括对数据的增删改查与列表
 * @table cc_student_evalute
 * @author SY
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_student_evalute")
public class CcStudentEvalute extends DbModel<CcStudentEvalute> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcStudentEvalute dao = new CcStudentEvalute();
	
	public Boolean deleteByEvaluteId(Long[] evaluteIds, Date date) {
		int result;
		if (getTable().hasColumnLabel(IS_DEL_LABEL)) {
			result = Db.update("update " + tableName + " set is_del=?, modify_date=? where evalute_id in (" + CollectionKit.convert(evaluteIds, ",") + ")", Boolean.TRUE, date);
		} else {
			result = Db.update("delete from " + tableName + " where evalute_id in (" + CollectionKit.convert(evaluteIds, ",") + ") ");
		}
		return result >= 0;
	}
	
	
	/**
	 * 最近的统计之后有新的更新
	 * 
	 * @param eduClassId
	 * @param date
	 * @return
	 */
	public Boolean needToUpdate(Long eduClassId) {
		StringBuilder sql = new StringBuilder("select count(cse.id) from " + tableName + " cse ");
		sql.append("inner join cc_evalute ce on ce.id = cse.evalute_id ");
		sql.append("inner join cc_evalute_level cel on cel.id = cse.level_id ");
		sql.append("inner join cc_teacher_course ctc on ctc.id = ce.teacher_course_id ");
		sql.append("inner join cc_educlass cec on cec.teacher_course_id = ctc.id and cec.id = ? ");
		sql.append("inner join cc_educlass_student ces on ces.class_id = cec.id and ces.student_id = cse.student_id ");
		sql.append("left join cc_report_educlass_evalute cree on cree.evalute_id = ce.id and cree.educlass_id = ? ");
		sql.append("where cse.modify_date > cree.statistics_date or ce.modify_date > cree.statistics_date or ( cree.statistics_date is null and cse.is_del = ? ) or cec.student_num_change_date > cree.statistics_date ");
		sql.append("limit 1");
		return Db.queryLong(sql.toString(), eduClassId, eduClassId, DEL_NO) > 0;
	}
	
	/**
	 * 获得考评点统计信息需要更新的班级编号
	 * 
	 * @param versionId
	 * @param grade
	 * @return
	 */
	public List<CcStudentEvalute> findNeedToUpdate(Long versionId, Integer grade) {
		StringBuilder sql = new StringBuilder("select cec.id educlass_id from " + tableName + " cse ");
		sql.append("inner join cc_evalute ce on ce.id = cse.evalute_id ");
		sql.append("inner join cc_evalute_level cel on cel.id = cse.level_id ");
		sql.append("inner join cc_teacher_course ctc on ctc.id = ce.teacher_course_id and ctc.grade = ? ");
		sql.append("inner join cc_course cc on cc.plan_id = ? and cc.id = ctc.course_id ");
		sql.append("inner join cc_educlass cec on cec.teacher_course_id = ctc.id ");
		sql.append("inner join cc_educlass_student ces on ces.class_id = cec.id and ces.student_id = cse.student_id ");
		sql.append("left join cc_report_educlass_evalute cree on cree.is_del = ? and cree.evalute_id = ce.id ");
		sql.append("where cse.modify_date > cree.statistics_date or cel.modify_date > cree.statistics_date or ce.weight != cree.weight or cree.id is null ");
		sql.append("group by cec.id");
		return find(sql.toString(), grade, versionId, DEL_NO);
	}
	
	/**
	 * 判断某个版本某个年级考评点相关记录是否有修改，若有则判断为需要更新
	 * 
	 * @param versionId
	 * @param grade
	 * @return
	 */
	public Boolean isNeedToUpdateByVersionAndGrade(Long versionId, Integer grade) {
		StringBuilder sql = new StringBuilder("select count(cse.id) from " + tableName + " cse ");
		sql.append("inner join cc_evalute ce on ce.id = cse.evalute_id ");
		sql.append("inner join cc_evalute_level cel on cel.id = cse.level_id ");
		sql.append("inner join cc_teacher_course ctc on ctc.id = ce.teacher_course_id and ctc.grade = ? ");
		sql.append("inner join cc_course cc on cc.plan_id = ? and cc.id = ctc.course_id ");
		sql.append("inner join cc_educlass cec on cec.teacher_course_id = ctc.id ");
		sql.append("inner join cc_educlass_student ces on ces.class_id = cec.id and ces.student_id = cse.student_id ");
		sql.append("left join cc_report_educlass_evalute cree on cree.evalute_id = ce.id ");
		sql.append("where (cse.modify_date > cree.statistics_date or cel.modify_date > cree.statistics_date or ce.weight != cree.weight or cree.id is null or cec.student_num_change_date > cree.statistics_date ) ");
		sql.append("limit 1");
		return Db.queryLong(sql.toString(), grade, versionId) > 0;
	}
	
	/**
	 * 判断个人报表是否需要更新（考评点分析法）
	 * 
	 * @param studentId 学生编号
	 * @param majorDirectionId 专业方向编号
	 * @param grade 
	 * @param versionId 
	 * @return
	 */
	public Boolean needToUpdateByStudentId(Long studentId, Long majorDirectionId, Integer grade, Long versionId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select count(cse.id) from " + tableName + " cse ");
		sql.append("inner join cc_educlass_student ces on ces.student_id = cse.student_id and ces.is_del = ? ");
		sql.append("inner join cc_student cs on cs.is_del = ? and cs.grade = ? and ces.student_id = cs.id ");
		sql.append("inner join cc_educlass cec on cec.id = ces.class_id and cec.is_del = ? ");
		sql.append("inner join cc_teacher_course ctc on ctc.id = cec.teacher_course_id and ctc.is_del = ? ");
		sql.append("inner join cc_course cc on cc.id = ctc.course_id and cc.is_del = ? and cc.plan_id = ? ");
		sql.append("inner join cc_evalute ce on ce.id = cse.evalute_id and cec.teacher_course_id = ce.teacher_course_id ");
		sql.append("inner join cc_evalute_level cel on cel.id = cse.level_id and cec.teacher_course_id = ce.teacher_course_id ");
		sql.append("inner join cc_indication_course cic on cic.indication_id = ce.indication_id and cic.course_id = cc.id and cic.is_del = ? ");
		sql.append("left join cc_report_personal_course crpc on crpc.indication_course_id = cic.id and crpc.student_id = cse.student_id  ");
		sql.append("where cse.student_id = ? and (cse.modify_date > crpc.statistics_date or ce.modify_date > crpc.statistics_date or cel.modify_date > crpc.statistics_date or crpc.statistics_date is null) ");
		params.add(DEL_NO);
		params.add(DEL_NO);
		params.add(grade);
		params.add(DEL_NO);
		params.add(DEL_NO);
		params.add(DEL_NO);
		params.add(versionId);
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
	 * 根据教师开课课程编号获得所有学生考评点
	 * 
	 * @param teacherCourseId
	 * @return
	 */
	public List<CcStudentEvalute> findAllByCourseId(Long teacherCourseId) {
		StringBuilder sql = new StringBuilder("select cse.*, ce.id educlass_id, cel.score score from " + tableName + " cse ");
		sql.append("left join cc_educlass_student ces on ces.student_id = cse.student_id ");
		sql.append("left join cc_evalute_level cel on cel.id = cse.level_id ");
		sql.append("left join cc_educlass ce on ce.id = ces.class_id ");
		sql.append("where ce.teacher_course_id=? and cse.is_del=?");
		return find(sql.toString(), teacherCourseId, DEL_NO);
	}
	
	/**
	 * 获得班级下的所有学生考评点成绩
	 * 
	 * @param eduClassId
	 * @return
	 */
	public List<CcStudentEvalute> findAllByEduClass(Long eduClassId) {
		StringBuilder sql = new StringBuilder("select cse.*, cel.score score from " + tableName + " cse ");
		sql.append("inner join cc_educlass_student ces on ces.class_id = ? and ces.is_del = ? and ces.student_id = cse.student_id ");
		sql.append("inner join cc_educlass ce on ce.is_del = ? and ce.id = ces.class_id ");
		sql.append("inner join cc_teacher_course ctc on ctc.is_del = ? and ctc.id = ce.teacher_course_id ");
		sql.append("inner join cc_evalute cee on cee.is_del = ? and cee.teacher_course_id = ctc.id and cee.id = cse.evalute_id ");
		sql.append("inner join cc_evalute_level cel on cel.is_del = ? and cel.id = cse.level_id ");
		sql.append("where cse.is_del = ?");
		return find(sql.toString(), eduClassId, DEL_NO, DEL_NO, DEL_NO, DEL_NO, DEL_NO, DEL_NO);
	}
	
	/**
	 * 获得班级下的所有学生考评点成绩(剔除掉一些不用计算的学生)
	 * 
	 * @author SY
	 * @param eduClassId
	 * 			教学班编号
	 * @param isCaculate
	 * 			是否计算
	 * @return
	 */
	public List<CcStudentEvalute> findAllByClassIdAndIsCaculate(Long eduClassId, Boolean isCaculate) {
		StringBuilder sql = new StringBuilder("select cse.*, cel.score score from " + tableName + " cse ");
		sql.append("inner join cc_educlass_student ces on ces.class_id = ? and ces.is_del = ? and ces.student_id = cse.student_id and ces.is_caculate = ? ");
		sql.append("inner join cc_educlass ce on ce.is_del = ? and ce.id = ces.class_id ");
		sql.append("inner join cc_teacher_course ctc on ctc.is_del = ? and ctc.id = ce.teacher_course_id ");
		sql.append("inner join cc_evalute cee on cee.is_del = ? and cee.teacher_course_id = ctc.id and cee.id = cse.evalute_id ");
		sql.append("inner join cc_evalute_level cel on cel.is_del = ? and cel.id = cse.level_id ");
		sql.append("where cse.is_del = ?");
		return find(sql.toString(), eduClassId, DEL_NO, isCaculate, DEL_NO, DEL_NO, DEL_NO, DEL_NO, DEL_NO);
	}
	
	/**
	 * 获得班级下的所有学生考评点成绩(EM00746)
	 * 
	 * @param eduClassId
	 * @return
	 */
	public List<CcStudentEvalute> findAllByEduClassOrderByIndex(Long eduClassId) {
		StringBuilder sql = new StringBuilder("select cse.*, cel.score score, cel.level_name, cs.student_no from " + tableName + " cse ");
		sql.append("inner join cc_educlass_student ces on ces.class_id = ? and ces.is_del = ? and ces.student_id = cse.student_id ");
		sql.append("inner join cc_educlass ce on ce.is_del = ? and ce.id = ces.class_id ");
		sql.append("inner join cc_teacher_course ctc on ctc.is_del = ? and ctc.id = ce.teacher_course_id ");
		sql.append("inner join cc_evalute cee on cee.is_del = ? and cee.teacher_course_id = ctc.id and cee.id = cse.evalute_id ");
		sql.append("inner join cc_evalute_level cel on cel.is_del = ? and cel.id = cse.level_id ");
		sql.append("inner join " + CcIndication.dao.tableName + " ci on ci.id = cee.indication_id ");
		sql.append("inner join " + CcGraduate.dao.tableName + " cg on cg.id = ci.graduate_id ");
		sql.append("inner join " + CcStudent.dao.tableName + " cs on cs.id = cse.student_id ");
		sql.append("where cse.is_del = ? ");
		sql.append("order by cg.index_num, ci.index_num ");
		return find(sql.toString(), eduClassId, DEL_NO, DEL_NO, DEL_NO, DEL_NO, DEL_NO, DEL_NO);
	}
	
	/**
	 * 通过一组编号获得班级的所有学生考评点成绩
	 * 
	 * @param eduClassIds
	 * @return
	 */
	public List<CcStudentEvalute> findAllByEduClasses(Long[] eduClassIds) {
		StringBuilder sql = new StringBuilder("select cse.*, ce.weight weight, cel.score score, ces.class_id educlass_id  from " + tableName + " cse " );
		sql.append("inner join cc_educlass_student ces on ces.class_id in (" + CollectionKit.convert(eduClassIds, ",") + ") and ces.is_del = ? and ces.student_id = cse.student_id ");
		sql.append("inner join cc_educlass cec on cec.is_del = ? and cec.id = ces.class_id ");
		sql.append("inner join cc_teacher_course ctc on ctc.is_del = ? and ctc.id = cec.teacher_course_id ");
		sql.append("inner join cc_evalute ce on ce.id = cse.evalute_id and ce.teacher_course_id = ctc.id and ce.is_del = ? ");
		sql.append("inner join cc_evalute_level cel on cel.id = cse.level_id and cel.teacher_course_id = ctc.id and cel.is_del = ? ");
		sql.append("where cse.is_del = ?");
		return find(sql.toString(), DEL_NO, DEL_NO, DEL_NO, DEL_NO, DEL_NO, DEL_NO);
	}
	
	/**
	 * 统计
	 * 
	 * @param idsArray
	 * @return
	 */
	public Boolean countStudentEvalute(Long[] idsArray){
		StringBuilder sb = new StringBuilder("select count(1) from " + CcStudentEvalute.dao.tableName + " cse ");
		sb.append(" where is_del = ? ");
		sb.append(" and evalute_id in (" + CollectionKit.convert(idsArray, ",") + ")" );
		
		List<Object> params = Lists.newArrayList();
		params.add(Boolean.FALSE);
		
		return Db.queryLong(sb.toString(), params.toArray()) > 0;
	}
	
	/**
	 * 判断考评点评分层次是否在使用中
	 * 
	 * @param ids 考评点评分层次编号
	 * @return
	 */
	public Boolean isInUseEvaluteLevel(Long[] ids) {
		StringBuilder sql = new StringBuilder("select count(1) from " + tableName + " ");
		sql.append("where is_del = ? and level_id in (" + CollectionKit.convert(ids, ",") + ")");
		return Db.queryLong(sql.toString(), DEL_NO) > 0;
	}
	
	/**
	 * 根据教学班编号和指标点编号查找学生考评点成绩
	 *
	 * @param courseGradecomposeId
	 * @param eduClassId
	 * @return
	 */
	public List<CcStudentEvalute> findEvaluteGradeByEduClazzIdAndIndicationId(Long courseGradecomposeId,Long eduClassId,Long batchId){
		List<Object> params = Lists.newArrayList();
		//TODO 2020.12.09改变考评点成绩得关联方式
		StringBuilder sql = new StringBuilder("select cs.id student_id, cess.remark , cess.is_retake, cs.student_no, cs.name student_name, cs.sex, cs.id_card, cs.address,crl.id levelId,crl.level_name ,crl.level,crl.score  from " + CcStudentEvalute.dao.tableName +" cse ");
		sql.append("inner join " + CcStudent.dao.tableName + " cs on cs.id = cse.student_id and cs.is_del = ? ");
		sql.append("inner join " + CcEduclassStudent.dao.tableName + " ces on ces.student_id = cs.id and ces.is_del = ? and ces.class_id = ? ");
		sql.append("left join " + CcEduclassStudentStudy.dao.tableName + " cess on cess.student_id = cs.id and cess.is_del = ? and cess.course_gradecompose_id = ? ");
		sql.append("inner join " +CcRankingLevel.dao.tableName + " crl on cse.evalute_id= crl.id ");
		/*sql.append("inner join " + CcEduclass.dao.tableName + " ce on ce.id = ces.class_id and ce.is_del = ? ");
		sql.append("inner join " + CcEvalute.dao.tableName + " cee on cee.id = cse.evalute_id and cee.is_del = ? and cee.indication_id = ? ");
		sql.append("inner join " + CcEvaluteLevel.dao.tableName + " cel on cel.id = cse.level_id and cel.is_del = ? and cel.indication_id = ? ");*/
		sql.append("where cse.is_del = ? and cse.course_gradecompose_id=? ");

		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		params.add(eduClassId);
		params.add(Boolean.FALSE);
		params.add(courseGradecomposeId);
		params.add(Boolean.FALSE);
		params.add(courseGradecomposeId);
		if (batchId != null){
			sql.append(" and cse.batch_id=? ");
			params.add(batchId);
		}
		return find(sql.toString(), params.toArray());
	}
	
	/**
	 * 某个教学班学生在某个指标点是否存在考评点成绩
	 * @param educlassId
	 * @param indicationId
	 * @return
	 */
	public boolean isExistStudentGrades(Long educlassId, Long indicationId){
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select count(1) from " + CcStudentEvalute.dao.tableName +" cse ");
		sql.append("inner join " + CcEduclassStudent.dao.tableName + " ces on ces.student_id = cse.student_id and ces.is_del = ? and ces.class_id = ? ");
		sql.append("inner join " + CcEduclass.dao.tableName + " ce on ce.id = ces.class_id and ce.is_del = ? ");
		sql.append("inner join " + CcEvalute.dao.tableName + " cee on cee.id = cse.evalute_id and cee.is_del = ? and cee.indication_id = ? ");
		sql.append("inner join " + CcEvaluteLevel.dao.tableName + " cel on cel.id = cse.level_id and cel.is_del = ? and cel.indication_id = ? ");
		sql.append("where cse.is_del = ? ");
		params.add(Boolean.FALSE);
		params.add(educlassId);
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		params.add(indicationId);
		params.add(Boolean.FALSE);
		params.add(indicationId);
		params.add(Boolean.FALSE);
		return Db.queryLong(sql.toString(), params.toArray()) > 0;
	}


	/**
	 * 通过考评点编号和学生编号删除记录
	 * @param evaluteId
	 * @param studentId
	 * @param date
	 * @return
	 * @author SY 
	 * @version 创建时间：2016年12月21日 下午4:12:53 
	 */
	public Boolean deleteAllByEvaluteIdAndStuedntId(Long courseGradecomposeId, Long studentId, Date date,Long batchId) {
		int result;
		if (getTable().hasColumnLabel(IS_DEL_LABEL)) {
			if (batchId == null){
				result = Db.update("update " + tableName + " set is_del=?, modify_date=? where course_gradecompose_id = ? and student_id = ? ", Boolean.TRUE, date, courseGradecomposeId, studentId);
			}else {
				result = Db.update("update " + tableName + " set is_del=?, modify_date=? where course_gradecompose_id = ? and student_id = ? and batch_id=?  ", Boolean.TRUE, date, courseGradecomposeId, studentId,batchId);
			}

		} else {
			if (batchId == null) {
				result = Db.update("delete from " + tableName + " where course_gradecompose_id = ? and student_id = ? ", courseGradecomposeId, studentId);
			}else{
				result = Db.update("delete from " + tableName + " where course_gradecompose_id = ? and student_id = ? and batch_id=? ", courseGradecomposeId, studentId,batchId);
			}
			}
		return result >= 0;
	}


	/**
	 * 教学班学生是否存在考评点成绩
	 * @param idsArray
	 * @return
	 */
	public boolean existScore(Long[] idsArray) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select count(1) from " + CcStudentEvalute.dao.tableName +" cse ");
		sql.append("inner join " + CcEduclassStudent.dao.tableName + " ces on ces.student_id = cse.student_id and ces.id in ( " + CollectionKit.convert(idsArray, ",") + " ) ");
		sql.append("inner join " + CcEduclass.dao.tableName + " ce on ce.id = ces.class_id and ce.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join " + CcTeacherCourse.dao.tableName + " ctc on ctc.id = ce.teacher_course_id and ctc.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join " + CcEvalute.dao.tableName + " cee on cee.teacher_course_id = ctc.id and cee.is_del = ? and cse.evalute_id = cee.id ");
		param.add(DEL_NO);
		sql.append("where cse.is_del = ? ");
		param.add(DEL_NO);
		return Db.queryLong(sql.toString(), param.toArray()) > 0;
	}


	/**
	 * 同一课程代码的课程在各个版本的年级考评点相关记录是否有修改，若有则判断为需要更新
	 * @param courseCode
	 * @param majorId 
	 * @return
	 */
	public boolean isNeedToUpdateByCourseCode(String courseCode, Long majorId) {
		StringBuilder sql = new StringBuilder("select count(cse.id) from " + tableName + " cse ");
		sql.append(returnStrB(courseCode, majorId));
		return Db.queryLong(sql.toString(), courseCode, DEL_NO, DEL_NO, majorId, DEL_NO) > 0;
	}
	
	/**
	 * 通过courseCode和majorId得到版本编号和年级
	 * @param courseCode
	 * @param majorId
	 * @return
	 */
	public List<CcVersion> getVersionAndGrade(String courseCode, Long majorId){
		StringBuilder sql = new StringBuilder("select cv.id versionId, ctc.grade from " + tableName + " cse ");
		sql.append(returnStrB(courseCode, majorId));
		return CcVersion.dao.find(sql.toString(), courseCode, DEL_NO, DEL_NO, majorId, DEL_NO);
	}
	
	private StringBuilder returnStrB(String courseCode, Long majorId){
		StringBuilder sql = new StringBuilder("inner join cc_evalute ce on ce.id = cse.evalute_id ");
		sql.append("inner join cc_evalute_level cel on cel.id = cse.level_id ");
		sql.append("inner join cc_teacher_course ctc on ctc.id = ce.teacher_course_id ");
		sql.append("inner join cc_course cc on cc.id = ctc.course_id and cc.code = ? and cc.is_del = ? ");
		sql.append("inner join cc_version cv on cv.minor_version = cv.max_minor_version and cv.is_del = ? and cv.major_id = ? and cv.id = cc.plan_id ");
		sql.append("inner join cc_educlass cec on cec.teacher_course_id = ctc.id ");
		sql.append("inner join cc_educlass_student ces on ces.class_id = cec.id and ces.student_id = cse.student_id and ces.is_del = ? ");
		sql.append("left join cc_report_educlass_evalute cree on cree.evalute_id = ce.id ");
		sql.append("where (cse.modify_date > cree.statistics_date or cel.modify_date > cree.statistics_date or ce.weight != cree.weight or cree.id is null or cec.student_num_change_date > cree.statistics_date ) ");
		return sql;
	}
	//---------------TODO 2020.12.07 以上方法都失效了，更改了评分表分析法的录入模式和保存格式

	public List<CcStudentEvalute> findCourseGradecomposeList(List<Long> courseGradeComposeIds){
		StringBuilder sql = new StringBuilder("select * from " + tableName + " where is_del=0 and course_gradecompose_id in (" + CollectionKit.convert(courseGradeComposeIds, ",") + ") ");
		return find(sql.toString());

	}
}
