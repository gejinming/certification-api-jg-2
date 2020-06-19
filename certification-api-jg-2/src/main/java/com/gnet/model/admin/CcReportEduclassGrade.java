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
 * 教学班达成度报表统计表(考核成绩分析法)
 * 
 * @author wct
 * @date 2016年7月8日
 */
// 已废弃，由cc_eduindication_stu_score代替。2017年12月3日
@Deprecated
@TableBind(tableName = "cc_report_educlass_grade")
public class CcReportEduclassGrade extends DbModel<CcReportEduclassGrade> {
	
	private static final long serialVersionUID = 3891540411758437977L;
	public static final CcReportEduclassGrade dao = new CcReportEduclassGrade();

	/**
	 * 获取课程下的所有指标点与课程元素的关联信息(EM00552)
	 * 
	 * @param indicationId
	 * @param eduClassId
	 * @param courseId
	 * @return
	 */
	public List<CcReportEduclassGrade> findAllByCourseId(Long courseId, Long eduClassId, Long indicationId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select creg.*, cge.index_num graduateIndexNum, ci.id indication_id, ci.index_num index_num, ci.content content, ci.remark remark, cic.weight indication_weight, cg.name gradecompose_name from " + tableName + " creg ");
		sql.append("inner join cc_course_gradecompose_indication ccgci on ccgci.id = creg.gradecompose_indication_id and ccgci.is_del = ? ");
		sql.append("inner join cc_course_gradecompose ccg on ccg.id = ccgci.course_gradecompose_id and ccg.is_del = ? ");
		sql.append("inner join cc_gradecompose cg on cg.id = ccg.gradecompose_id and cg.is_del = ? ");
		sql.append("inner join cc_educlass ce on ce.teacher_course_id = ccg.teacher_course_id and ce.id = ? ");
		sql.append("inner join cc_indication ci on ci.id = ccgci.indication_id ");
		sql.append("left join " + CcGraduate.dao.tableName + " cge on cge.id = ci.graduate_id ");
		sql.append("left join cc_indication_course cic on cic.indication_id = ci.id and cic.course_id = ? ");
		sql.append("where creg.educlass_id = ? and creg.is_del = ? ");
		params.add(DEL_NO);
		params.add(DEL_NO);
		params.add(DEL_NO);
		params.add(eduClassId);
		params.add(courseId);
		params.add(eduClassId);
		params.add(DEL_NO);
		if (indicationId != null) {
			sql.append("and ci.id = ? ");
			params.add(indicationId);
		}
		sql.append("order by ci.index_num asc, ccg.sort desc, ccg.create_date asc, ccg.id asc");
		return find(sql.toString(), params.toArray());
	}
	
	/**
	 * 删除所有未删除的报表项
	 * 
	 * @param teacherCourseId
	 * @return
	 */
	public boolean deleteAllNoDelByCourseId(Long teacherCourseId) {
		StringBuilder sql = new StringBuilder("update " + tableName + " creg ");
		sql.append("left join cc_educlass ce on ce.id = creg.educlass_id ");
		sql.append("set creg.is_del=?, creg.modify_date=? ");
		sql.append("where creg.is_del=? and ce.teacher_course_id=?");
		return Db.update(sql.toString(), DEL_YES, new Date(), DEL_NO, teacherCourseId) >= 0;
	}
	
	/**
	 * 删除所有未删除的报表项
	 * 
	 * @param eduClassId
	 * @return
	 */
	public boolean deleteAllNoDel(Long eduClassId) {
		return Db.update("update " + tableName + " set is_del=?, modify_date=? where is_del=? and educlass_id=?", DEL_YES, new Date(), DEL_NO, eduClassId) >= 0;
	}
	
	
	/**
	 * 返回班级的人数
	 * 
	 * @param reportIds
	 * @return
	 */
	public List<CcReportEduclassGrade> getStudentSizeByIds(Long[] reportIds) {
		StringBuilder sql = new StringBuilder("select ces.class_id, count(ces.student_id) student_size from " + tableName + " creg ");
		sql.append("left join cc_educlass_student ces on ces.class_id = ce.id ");
		sql.append("where creg.id in (" + CollectionKit.convert(reportIds, ",") + ")");
		return find(sql.toString());
	}
	
	/**
	 * 删除所有未删除的报表项
	 * 
	 * @param eduClassIds
	 * @return
	 */
	public boolean deleteAllNoDelByEduclassIds(Long[] eduClassIds) {
		StringBuilder sql = new StringBuilder("update " + tableName + " creg ");
		sql.append("set creg.is_del=?, creg.modify_date=? ");
		sql.append("where creg.is_del=? and creg.educlass_id in (" + CollectionKit.convert(eduClassIds, ",") + ")");
		return Db.update(sql.toString(), DEL_YES, new Date(), DEL_NO) >= 0;
	}
	
	/**
	 * 获得该版本某一级学生所有课程的所有教学班的成绩组成成绩
	 * 
	 * @param versionId
	 * @param grade
	 * @return
	 */
	public List<CcReportEduclassGrade> getByVersionAndGrade(Long versionId, Integer grade) {
//		// 这里是可能要改的。先写了，注释掉，看结果。 FIXME SY
//		StringBuilder sql = new StringBuilder("select creg.*, ccg.percentage, ccgi.indication_id indication_id, cc.id course_id, ctc.result_type result_type from " + tableName + " creg ");
//		sql.append("left join cc_course_gradecompose_indication ccgi on ccgi.id = creg.gradecompose_indication_id ");
//		sql.append("left join cc_course_gradecompose ccg on ccgi.course_gradecompose_id = ccg.id ");
		StringBuilder sql = new StringBuilder("select creg.*, ctc.id teacher_course_id, ccgi.indication_id indication_id, cc.id course_id, ctc.result_type result_type from " + tableName + " creg ");
		sql.append("left join cc_course_gradecompose_indication ccgi on ccgi.id = creg.gradecompose_indication_id ");
		
		sql.append("left join cc_educlass ce on ce.id = creg.educlass_id ");
		sql.append("left join cc_teacher_course ctc on ctc.id = ce.teacher_course_id and ctc.result_type = ? ");
		sql.append("left join cc_course cc on cc.id = ctc.course_id ");
		sql.append("where cc.plan_id = ? and ctc.grade = ? and creg.is_del = ? ");
		sql.append("order by ce.id asc, indication_id asc");
		return find(sql.toString(), CcTeacherCourse.RESULT_TYPE_SCORE, versionId, grade, DEL_NO);
	}
	
	/**
	 * 删除教学班考核分析法是否成功
	 * 
	 * @param ids 编号
	 * @param date 时间
	 * @return
	 */
	public boolean deleteAll(Long[] ids, Date date) {
		String sql = "update "  + tableName + " set is_del = ?, modify_date = ?, statistics_date = ? where id in (" + CollectionKit.convert(ids, ",") + ")";
		return Db.update(sql, DEL_YES, date, date) >= 0;
	}

	/**
	 * 考核分析法教学班对应的指标点权重和
	 * @param versionId
	 * @param grade
	 * @return
	 */
	public List<CcReportEduclassGrade> findEduclassIndicationWeight(Long versionId, Integer grade) {
		StringBuffer sql = new StringBuffer("select creg.educlass_id, ccgi.indication_id indication_id, sum(creg.weight) weight from " + tableName + " creg ");
		sql.append("inner join cc_course_gradecompose_indication ccgi ON ccgi.id = creg.gradecompose_indication_id ");
		sql.append("inner join cc_educlass ce ON ce.id = creg.educlass_id ");
		sql.append("inner join cc_teacher_course ctc ON ctc.id = ce.teacher_course_id and ctc.result_type = ? ");
		sql.append("inner join cc_course cc ON cc.id = ctc.course_id ");
		sql.append("where cc.plan_id = ? and ctc.grade = ? and creg.is_del = ? ");
		sql.append("group by educlass_id, indication_id ");
		sql.append("order by ce.id asc, indication_id asc ");
		return find(sql.toString(), CcTeacherCourse.RESULT_TYPE_SCORE, versionId, grade, DEL_NO);
	}
	
	/**
	 * 计算指定教师开课下面的 总的 指标点成绩组成 总分
	 * @param teacherCourseIdList
	 * @param indicationId
	 * 			指标点（可为空）
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年12月21日 上午11:45:04 
	 */
	public List<CcReportEduclassGrade> caculateSumInSameGradecomposeAndIndication(List<Long> teacherCourseIdList, Long indicationId) {
		List<Object> params = new ArrayList<>();
		StringBuffer sql = new StringBuffer("select ccgi.indication_id indicationId, ccg.gradecompose_id gradecomposeId "
				+ ",ccg.teacher_course_id teacherCourseId,(creg.size * creg.result ) as totalScore from " + tableName + " creg ");
		sql.append("inner join cc_course_gradecompose_indication ccgi ON ccgi.id = creg.gradecompose_indication_id and ccgi.is_del = ? ");
		params.add(DEL_NO);
		sql.append("inner join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.id = ccgi.course_gradecompose_id and ccg.is_del = ? ");
		params.add(DEL_NO);
		sql.append("where creg.is_del = ? ");
		params.add(DEL_NO);
		if(indicationId != null) {
			sql.append("and ccgi.indication_id = ? ");
			params.add(indicationId);	
		}
		
		sql.append("group by ccgi.indication_id, ccg.gradecompose_id, ccg.teacher_course_id ");
		sql.append("order by ccgi.indication_id asc ");
		return find(sql.toString(), params.toArray());
	}
}
