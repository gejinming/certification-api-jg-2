package com.gnet.model.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;

/**
 * 课程达成度统计表表
 * 
 * @author wct
 * @date 2016年7月15日
 */
@TableBind(tableName = "cc_report_course")
public class CcReportCourse extends DbModel<CcReportCourse> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5967160296854738908L;
	public static final CcReportCourse dao = new CcReportCourse();

	/**
	 * 根据课程指标点编号删除所有未删除的记录项
	 * 
	 * @param indicationCourseIds
	 * @return
	 */
	public boolean deleteAllNoDel(Integer grade, List<Long> indicationCourseIds) {
		return Db.update("update " + tableName + " set is_del=?, modify_date=? where is_del=? and grade = ? and indication_course_id in (" + CollectionKit.convert(indicationCourseIds, ",") + ")", DEL_YES, new Date(), DEL_NO, grade) >= 0;
	}
	
	/**
	 * 根据年级和版本编号获得课程统计表集合
	 * 
	 * @param grade 年级 
	 * @param versionId 培养计划版本编号
	 * @return
	 */
	public List<CcReportCourse> findAllByGradeAndVersion(Integer grade, Long versionId) {
		return findAllByGradeAndVersion(grade, versionId, null, null);
	}
	
	/**
	 * 根据年级和版本编号获得课程统计表集合
	 * 
	 * @param grade 年级 
	 * @param versionId 培养计划版本编号
	 * @return
	 */
	public List<CcReportCourse> findAllByGradeAndVersion(Integer grade, Long versionId, Long graduateId, Long majorDirectionId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select crc.*, cic.course_id course_id, ci.id indication_id"
				+ ", cic.indication_id indication_id, cic.weight weight, cg.index_num indication_first_num"
				+ ", ci.index_num indication_last_num, crm.result indication_result, crm.except_result indication_except_result "
				+ "from " + CcIndicationCourse.dao.tableName + " cic ");
		sql.append("left join " + tableName + " crc ON cic.id = crc.indication_course_id and crc.grade = ? and crc.is_del = ? ");
		sql.append("left join " + CcIndicatorPoint.dao.tableName  + " ci on ci.id = cic.indication_id and ci.is_del = ? ");
		sql.append("inner join cc_graduate cg on cg.is_del = ? and cg.id = ci.graduate_id ");
		params.add(grade);
		params.add(DEL_NO);
		params.add(DEL_NO);
		params.add(DEL_NO);
		// 毕业要求是否为空
		if (graduateId != null) {
			sql.append("and cg.id = ? ");
			params.add(graduateId);
		}

		sql.append("inner join cc_course cc on cc.plan_id = ? and cc.is_del = ? and cc.id = cic.course_id ");
		params.add(versionId);
		params.add(DEL_NO);
		// 判断专业方向是否为空
		if (majorDirectionId != null) {
			sql.append("and (cc.direction_id = ? or cc.direction_id is null) ");
			params.add(majorDirectionId);
		} else {
			sql.append("and cc.direction_id is null ");
		}

		sql.append("left join cc_report_major crm on crm.is_del = ? and crm.grade = ? and crm.indication_id = ci.id ");
		params.add(DEL_NO);
		params.add(grade);
		// 判断专业方向是否为空
		if (majorDirectionId != null) {
			sql.append("and (crm.major_direction_id = ? or crm.major_direction_id is null) ");
			params.add(majorDirectionId);
		} else {
			sql.append("and crm.major_direction_id is null ");
		}

		sql.append("where cic.is_del = ? ");
		params.add(DEL_NO);
		sql.append("order by cg.index_num asc, ci.index_num asc, crm.major_direction_id desc");
		return find(sql.toString(), params.toArray());
	}
	
	/**
	 * 获得某年级报表中最新更新的一条记录
	 * 
	 * @param grade 年级
	 * @return
	 */
	public CcReportCourse getNewStatisticsDateRecord(Integer grade) {
		return findFirst("select * from " + tableName + " where grade = ? and is_del = ? order by statistics_date desc", grade, DEL_NO);
	}
	
	/**
	 * 通过课程编号获得各个年级指标点专业达成度
	 * 
	 * @param courseCode 课程代码
	 * @param majorId 
	 * @return
	 */
	public List<CcReportCourse> findByCourseCode(String courseCode, Long majorId) {
		StringBuilder sql = new StringBuilder("select crc.*, cic.weight weight from " + tableName + " crc ");
		sql.append("inner join cc_indication_course cic on cic.id = crc.indication_course_id ");
		sql.append("inner join cc_course cc on cc.id = cic.course_id ");
		sql.append("inner join cc_version cv on (cv.max_minor_version = ? or cv.minor_version = cv.max_minor_version) and cv.id = cc.plan_id and cv.major_id = ? ");
		sql.append("where cc.code = ? and crc.is_del = ? ");
		sql.append("order by crc.grade asc");
		return find(sql.toString(), CcVersion.MINOR_VERSION_NULL, majorId, courseCode, DEL_NO);
	}

	/**
	 * 判断某个版本某个年级教学班信息是否有修改，若有则判断为需要更新
	 * 
	 * @param versionId 版本编号
	 * @param grade 成绩
	 * @return
	 * @date 2016年12月16日17:31:54
	 * @author SY
	 */
	public Boolean isNeedToUpdateByReportEduclassGrade(Long versionId, Integer grade) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select count(creg.id) from cc_report_educlass_grade creg ");
		sql.append("left join cc_educlass ce on ce.id = creg.educlass_id and ce.is_del = ? ");
		params.add(DEL_NO);
		sql.append("left join cc_teacher_course ctc on ce.teacher_course_id = ctc.id and ctc.grade = ? and ctc.is_del = ? ");
		params.add(grade);
		params.add(DEL_NO);
		sql.append("left join cc_course cc on  ctc.course_id = cc.id and cc.is_del = ? and cc.plan_id = ?  ");
		params.add(DEL_NO);
		params.add(versionId);
		sql.append("left join cc_indication_course cic on cic.course_id = cc.id and cic.is_del = ? ");
		params.add(DEL_NO);
		sql.append("left join cc_report_course crc on crc.indication_course_id = cic.id and crc.is_del = ? and crc.grade = ? ");
		params.add(DEL_NO);
		params.add(grade);
		// #5237 少了这个inner join 会导致 ccgi.indication_id ！= cic.indication_id ，然后查出的数据有点问题。 2016年12月27日18:22:07 by SY
		sql.append("inner join cc_course_gradecompose_indication ccgi on ccgi.id = creg.gradecompose_indication_id and ccgi.indication_id = cic.indication_id "); 
		sql.append("where creg.is_del = ? ");
		params.add(DEL_NO);
		sql.append("and (creg.statistics_date > crc.statistics_date or crc.id is null or ce.student_num_change_date > creg.statistics_date or ce.student_num_change_date > crc.statistics_date or cc.direction_change_date > crc.statistics_date ) ");
		sql.append("and cc.plan_id = ? ");
		params.add(versionId);
		sql.append("limit 1");
		return Db.queryLong(sql.toString(), params.toArray()) > 0;
	}

	/**
	 * 判断某个版本某个年级教学班信息是否有修改，若有则判断为需要更新
	 * @param versionId
	 * @param grade
	 * @return
	 */
	public boolean isNeedToUpdateByReportEduclassEvalute(Long versionId, Integer grade) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select count(cree.id) from cc_report_educlass_evalute cree ");
		sql.append("left join cc_educlass ce on ce.id = cree.educlass_id and ce.is_del = ? ");
		param.add(DEL_NO);
		sql.append("left join cc_teacher_course ctc on ce.teacher_course_id = ctc.id and ctc.grade = ? and ctc.is_del = ? ");
		param.add(grade);
		param.add(DEL_NO);
		sql.append("left join cc_course cc on ctc.course_id = cc.id and cc.is_del = ? and cc.plan_id = ? ");
		param.add(DEL_NO);
		param.add(versionId);
		sql.append("left join cc_evalute cee on cee.teacher_course_id = ctc.id and cee.id = cree.evalute_id and cee.is_del = ? ");
		param.add(DEL_NO);
		sql.append("left join cc_indication_course cic on cic.course_id = cc.id and cic.indication_id = cee.indication_id and cee.is_del = ? ");
		param.add(DEL_NO);
		sql.append("left join cc_report_course crc on crc.indication_course_id = cic.id and crc.is_del = ? and crc.grade = ? ");
		param.add(DEL_NO);
		param.add(grade);
		sql.append("where cree.is_del = ? ");
		param.add(DEL_NO);
		sql.append("and (cree.statistics_date > crc.statistics_date or crc.id is null or ce.student_num_change_date > cree.statistics_date or ce.student_num_change_date > crc.statistics_date or cc.direction_change_date > crc.statistics_date ) ");
		sql.append("and cc.plan_id = ? ");
		param.add(versionId);
		sql.append("limit 1");
		return Db.queryLong(sql.toString(), param.toArray()) > 0;
	}

	/**
	 * 判断某个版本某个年级某个课程的考评点分析法成绩是否有修改
	 * @param versionId
	 * @param grade
	 * @return
	 */
	public boolean isNeedToUpdateByStudentEvalute(Long versionId, Integer grade) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select count(crc.id) from cc_report_course crc ");
		sql.append("inner join cc_indication_course cic on cic.id = crc.indication_course_id and cic.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_course cc on cc.id = cic.course_id and cc.plan_id = ? and cc.is_del = ? ");
		param.add(versionId);
		param.add(DEL_NO);
		sql.append("inner join cc_teacher_course ctc on ctc.course_id = cc.id and ctc.grade = ? and ctc.is_del = ? ");
		param.add(grade);
		param.add(DEL_NO);
		sql.append("inner join cc_evalute ce on ce.indication_id = cic.indication_id and ce.teacher_course_id = ctc.id and ce.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_student_evalute cse on cse.evalute_id = ce.id ");
		sql.append("where crc.grade = ? and crc.is_del = ? ");
		param.add(grade);
		param.add(DEL_NO);
		sql.append("and( cse.modify_date > crc.statistics_date ) limit 1 ");
		
		return Db.queryLong(sql.toString(), param.toArray()) > 0;
	}

	/**
	 * 判断某个版本某个年级某个课程的考核分析法成绩是否有修改
	 * @param versionId
	 * @param grade
	 * @return
	 */
	public boolean isNeedToUpdateByScoreStuIndigrade(Long versionId, Integer grade) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select count(crc.id) from cc_report_course crc ");
		sql.append("inner join cc_indication_course cic on cic.id = crc.indication_course_id and cic.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_course cc on cc.id = cic.course_id and cc.plan_id = ? and cc.is_del = ? ");
		param.add(versionId);
		param.add(DEL_NO);
		sql.append("inner join cc_teacher_course ctc on ctc.course_id = cc.id and ctc.grade = ? and ctc.is_del = ? ");
		param.add(grade);
		param.add(DEL_NO);
		sql.append("inner join cc_course_gradecompose ccg on ccg.teacher_course_id = ctc.id and ccg.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_course_gradecompose_indication ccgi on ccgi.course_gradecompose_id = ccg.id and ccgi.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_score_stu_indigrade cssi on cssi.gradecompose_indication_id = ccgi.id ");
		sql.append("where crc.grade = ? and crc.is_del = ? ");
		param.add(grade);
		param.add(DEL_NO);
		sql.append("and( cssi.modify_date > crc.statistics_date ) limit 1 ");
		return Db.queryLong(sql.toString(), param.toArray()) > 0;
	}

	/**
	 * 同一课程代码的课程的教学班信息是否有修改，若有则判断为需要更新
	 * @param courseCode
	 * @param majorId 
	 * @return
	 */
	public boolean isNeedToUpdateByReportEduclassGradeAndCourseCode(String courseCode, Long majorId) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select count(creg.id) from cc_report_educlass_grade creg ");
		sql.append(returnReportEduclassGradeStrB(courseCode, majorId));
		params.add(DEL_NO);
		params.add(DEL_NO);
		params.add(DEL_NO);
		params.add(courseCode);
		params.add(DEL_NO);
		params.add(majorId);
		params.add(DEL_NO);
		params.add(DEL_NO);
		params.add(DEL_NO);
		return Db.queryLong(sql.toString(), params.toArray()) > 0;
	}
	
	public List<CcVersion> getByReportEduclassGradeAndCourseCode(String courseCode, Long majorId){
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select cv.id versionId, ctc.grade from cc_report_educlass_grade creg ");
		sql.append(returnReportEduclassGradeStrB(courseCode, majorId));
		params.add(DEL_NO);
		params.add(DEL_NO);
		params.add(DEL_NO);
		params.add(courseCode);
		params.add(DEL_NO);
		params.add(majorId);
		params.add(DEL_NO);
		params.add(DEL_NO);
		params.add(DEL_NO);
		return CcVersion.dao.find(sql.toString(), params.toArray());
	}
	
	private StringBuilder returnReportEduclassGradeStrB(String courseCode, Long majorId){
		StringBuilder sql = new StringBuilder("left join cc_educlass ce on ce.id = creg.educlass_id and ce.is_del = ? ");
		sql.append("left join cc_teacher_course ctc on ce.teacher_course_id = ctc.id and ctc.is_del = ? ");
		sql.append("left join cc_course cc on  ctc.course_id = cc.id and cc.is_del = ? and cc.code = ? ");
		sql.append("inner join cc_version cv on cv.minor_version = cv.max_minor_version and cv.is_del = ? and cv.major_id = ? and cv.id = cc.plan_id ");
		sql.append("left join cc_indication_course cic on cic.course_id = cc.id and cic.is_del = ? ");
		sql.append("left join cc_report_course crc on crc.indication_course_id = cic.id and crc.is_del = ? and ctc.grade = crc.grade ");
		sql.append("inner join cc_course_gradecompose_indication ccgi on ccgi.id = creg.gradecompose_indication_id and ccgi.indication_id = cic.indication_id "); 
		sql.append("where creg.is_del = ? ");
		sql.append("and (creg.statistics_date > crc.statistics_date or crc.id is null or ce.student_num_change_date > creg.statistics_date or ce.student_num_change_date > crc.statistics_date or cc.direction_change_date > crc.statistics_date ) ");
		return sql;
	}
	

	/**
	 * 同一课程代码的课程的教学班信息是否有修改，若有则判断为需要更新
	 * @param courseCode
	 * @param majorId 
	 * @return
	 */
	public boolean isNeedToUpdateByReportEduclassEvaluteAndCourseCode(String courseCode, Long majorId) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select count(cree.id) from cc_report_educlass_evalute cree ");
		sql.append(returnReportEduclassEvaluteStrB(courseCode, majorId));
		param.add(DEL_NO);
		param.add(DEL_NO);
		param.add(DEL_NO);
		param.add(courseCode);
		param.add(DEL_NO);
		param.add(majorId);
		param.add(DEL_NO);
		param.add(DEL_NO);
		param.add(DEL_NO);
		param.add(DEL_NO);
		return Db.queryLong(sql.toString(), param.toArray()) > 0;
	}
	
	public List<CcVersion> getByReportEduclassEvaluteAndCourseCode(String courseCode, Long majorId){
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select cv.id versionId, ctc.grade from cc_report_educlass_evalute cree ");
		sql.append(returnReportEduclassEvaluteStrB(courseCode, majorId));
		param.add(DEL_NO);
		param.add(DEL_NO);
		param.add(DEL_NO);
		param.add(courseCode);
		param.add(DEL_NO);
		param.add(majorId);
		param.add(DEL_NO);
		param.add(DEL_NO);
		param.add(DEL_NO);
		param.add(DEL_NO);
		return CcVersion.dao.find(sql.toString(), param.toArray());
	}
	
	private StringBuilder returnReportEduclassEvaluteStrB(String courseCode, Long majorId){
		StringBuilder sql = new StringBuilder("left join cc_educlass ce on ce.id = cree.educlass_id and ce.is_del = ? ");
		sql.append("left join cc_teacher_course ctc on ce.teacher_course_id = ctc.id and ctc.is_del = ? ");
		sql.append("left join cc_course cc on ctc.course_id = cc.id and cc.is_del = ? and cc.code = ? ");
        sql.append("inner join cc_version cv on cv.minor_version = cv.max_minor_version and cv.is_del = ? and cv.major_id = ? and cv.id = cc.plan_id ");
		sql.append("left join cc_evalute cee on cee.teacher_course_id = ctc.id and cee.id = cree.evalute_id and cee.is_del = ? ");
		sql.append("left join cc_indication_course cic on cic.course_id = cc.id and cic.indication_id = cee.indication_id and cee.is_del = ? ");
		sql.append("left join cc_report_course crc on crc.indication_course_id = cic.id and crc.is_del = ? and ctc.grade = crc.grade ");	
		sql.append("where cree.is_del = ? ");	
		sql.append("and (cree.statistics_date > crc.statistics_date or crc.id is null or ce.student_num_change_date > cree.statistics_date or ce.student_num_change_date > crc.statistics_date or cc.direction_change_date > crc.statistics_date ) ");
		return sql;
	}
	

	/**
	 * 同一课程代码的课程的考评点分析法成绩是否有修改
	 * @param courseCode
	 * @return
	 */
	public boolean isNeedToUpdateByStudentEvaluteAndCourseCode(String courseCode, Long majorId) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select count(crc.id) from cc_report_course crc ");
		sql.append(returnStudentEvaluteStrB(courseCode, majorId));
		param.add(DEL_NO);
		param.add(DEL_NO);
		param.add(courseCode);	
		param.add(DEL_NO);
		param.add(majorId);
		param.add(DEL_NO);
		param.add(DEL_NO);
		param.add(DEL_NO);	
		return Db.queryLong(sql.toString(), param.toArray()) > 0;
	}

	public List<CcVersion> getByStudentEvaluteAndCourseCode(String courseCode, Long majorId){
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select cv.id versionId, ctc.grade from cc_report_course crc ");
		sql.append(returnStudentEvaluteStrB(courseCode, majorId));
		param.add(DEL_NO);
		param.add(DEL_NO);
		param.add(courseCode);	
		param.add(DEL_NO);
		param.add(majorId);
		param.add(DEL_NO);
		param.add(DEL_NO);
		param.add(DEL_NO);
		return CcVersion.dao.find(sql.toString(), param.toArray());
	}
	
	private StringBuilder returnStudentEvaluteStrB(String courseCode, Long majorId){
		StringBuilder sql = new StringBuilder("inner join cc_indication_course cic on cic.id = crc.indication_course_id and cic.is_del = ? ");
		sql.append("inner join cc_course cc on cc.id = cic.course_id and cc.is_del = ? and cc.code = ? ");
		sql.append("inner join cc_version cv on cv.minor_version = cv.max_minor_version and cv.is_del = ? and cv.major_id = ? and cv.id = cc.plan_id ");
		sql.append("inner join cc_teacher_course ctc on ctc.course_id = cc.id and ctc.is_del = ? and crc.grade = ctc.grade ");
		sql.append("inner join cc_evalute ce on ce.indication_id = cic.indication_id and ce.teacher_course_id = ctc.id and ce.is_del = ? ");
		sql.append("inner join cc_student_evalute cse on cse.evalute_id = ce.id ");
		sql.append("where crc.is_del = ? ");
		sql.append("and( cse.modify_date > crc.statistics_date ) ");
		return sql;
	}
	
	
	/**
	 * 同一课程代码的课程的考核分析法成绩是否有修改
	 * @param courseCode
	 * @return
	 */
	public boolean isNeedToUpdateByScoreStuIndigradeAndCourseCode(String courseCode, Long majorId) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select count(crc.id) from cc_report_course crc ");
		sql.append(returnScoreStuIndigradeStrB(courseCode, majorId));
		param.add(DEL_NO);
		param.add(DEL_NO);
		param.add(courseCode);
		param.add(DEL_NO);
		param.add(majorId);
		param.add(DEL_NO);
		param.add(DEL_NO);
		param.add(DEL_NO);
		param.add(DEL_NO);
		return Db.queryLong(sql.toString(), param.toArray()) > 0;
	}
	
	public List<CcVersion> getByScoreStuIndigradeAndCourseCode(String courseCode, Long majorId){
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select cv.id versionId, ctc.grade from cc_report_course crc ");
		sql.append(returnScoreStuIndigradeStrB(courseCode, majorId));
		param.add(DEL_NO);
		param.add(DEL_NO);
		param.add(courseCode);
		param.add(DEL_NO);
		param.add(majorId);
		param.add(DEL_NO);
		param.add(DEL_NO);
		param.add(DEL_NO);
		param.add(DEL_NO);
		return CcVersion.dao.find(sql.toString(), param.toArray());
	}

	private StringBuilder returnScoreStuIndigradeStrB(String courseCode, Long majorId){
		StringBuilder sql = new StringBuilder("inner join cc_indication_course cic on cic.id = crc.indication_course_id and cic.is_del = ? ");
		sql.append("inner join cc_course cc on cc.id = cic.course_id and cc.is_del = ? and cc.code = ? ");
		sql.append("inner join cc_version cv on cv.minor_version = cv.max_minor_version and cv.is_del = ? and cv.major_id = ? and cv.id = cc.plan_id ");
		sql.append("inner join cc_teacher_course ctc on ctc.course_id = cc.id and ctc.is_del = ? and crc.grade = ctc.grade ");
		sql.append("inner join cc_course_gradecompose ccg on ccg.teacher_course_id = ctc.id and ccg.is_del = ? ");
		sql.append("inner join cc_course_gradecompose_indication ccgi on ccgi.course_gradecompose_id = ccg.id and ccgi.is_del = ? ");
		sql.append("inner join cc_score_stu_indigrade cssi on cssi.gradecompose_indication_id = ccgi.id ");
		sql.append("where crc.is_del = ? ");
		sql.append("and( cssi.modify_date > crc.statistics_date ) ");
		return sql;
	}

}
