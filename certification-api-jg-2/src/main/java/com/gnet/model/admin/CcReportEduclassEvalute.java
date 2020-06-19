package com.gnet.model.admin;

import java.util.Date;
import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;

/**
 * 教学班达成度报表统计表(评分表分析法)
 * 
 * @author wct
 * @date 2016年7月10日
 */
@TableBind(tableName = "cc_report_educlass_evalute")
public class CcReportEduclassEvalute extends DbModel<CcReportEduclassEvalute> {

	private static final long serialVersionUID = -7247316725440679966L;
	public static final CcReportEduclassEvalute dao = new CcReportEduclassEvalute();
	
	/**
	 * 获取课程下的所有指标点与考评点信息(EM00552)
	 * 
	 * @param courseId
	 * @param eduClassId
	 * @param indicationId
	 * @return
	 */
	public List<CcReportEduclassEvalute> findAllByCourseId(Long courseId, Long eduClassId, Long indicationId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select cree.*, cet.percentage evalute_type_percentage, cet.type evalute_type, cge.index_num graduateIndexNum, ci.id indication_id, ci.index_num index_num, ci.content content, ci.remark remark, cic.weight indication_weight, ce.index_num evalute_index_num, ce.content evalute_content, ce.remark evalute_remark from " + tableName + " cree ");
		sql.append("inner join cc_evalute ce on ce.id = cree.evalute_id and ce.is_del = ? ");
		sql.append("inner join " + CcEvaluteType.dao.tableName + " cet on cet.id = ce.evalute_type_id and cet.is_del = ? ");
		sql.append("inner join cc_educlass ces on ces.teacher_course_id = ce.teacher_course_id and ces.id = ? ");
		sql.append("inner join cc_indication ci on ci.id = ce.indication_id ");
		sql.append("left join " + CcGraduate.dao.tableName + " cge on cge.id = ci.graduate_id ");
		sql.append("left join cc_indication_course cic on cic.indication_id = ci.id and cic.course_id = ? ");
		sql.append("where cree.educlass_id = ? and cree.is_del = ? ");
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
		sql.append("order by ci.index_num asc, ce.index_num asc");
		
		return find(sql.toString(), params.toArray());
	}
	
	/**
	 * 根据课程编号删除所有未删除的报表项
	 * 
	 * @param teacherCourseId
	 * @return
	 */
	public boolean deleteAllNoDelByCourseId(Long teacherCourseId) {
		StringBuilder sql = new StringBuilder("update " + tableName + " cree ");
		sql.append("left join cc_educlass ce on ce.id = cree.educlass_id ");
		sql.append("set cree.is_del=?, cree.modify_date=? ");
		sql.append("where cree.is_del=? and ce.teacher_course_id=?");
		return Db.update(sql.toString(), DEL_YES, new Date(), DEL_NO, teacherCourseId) >= 0;
	}
	
	/**
	 * 根据教学班编号删除所有未删除的报表项
	 * 
	 * @param eduClassId
	 * @return
	 */
	public boolean deleteAllNoDel(Long eduClassId) {
		return Db.update("update " + tableName + " set is_del=?, modify_date=? where is_del=? and educlass_id=?", DEL_YES, new Date(), DEL_NO, eduClassId) >= 0;
	}
	
	/**
	 * 获得该版本某一级学生所有课程的所有教学班的考评点
	 * 
	 * @param grade
	 * @param versionId
	 * @return
	 */
	public List<CcReportEduclassEvalute> getByVersionAndGrade(Long versionId, Integer grade) {
		StringBuilder sql = new StringBuilder("select cree.*, ctc.id teacher_course_id, cet.percentage, cet.type evalute_type, cev.indication_id indication_id, cc.id course_id, ctc.result_type result_type from " + tableName + " cree ");
		sql.append("left join cc_evalute cev on cev.id = cree.evalute_id ");
		sql.append("left join cc_evalute_type cet on cet.id = cev.evalute_type_id ");
		sql.append("left join cc_educlass ce on ce.id = cree.educlass_id ");
		sql.append("left join cc_teacher_course ctc on ctc.id = ce.teacher_course_id and ctc.result_type = ? ");
		sql.append("left join cc_course cc on cc.id = ctc.course_id ");
		sql.append("where cc.plan_id = ? and ctc.grade = ? and cree.is_del = ? ");
		sql.append("order by ce.id asc, cev.indication_id asc, cet.type asc ");
		return find(sql.toString(), CcTeacherCourse.RESULT_TYPE_EVALUATE, versionId, grade, DEL_NO);
	}
	
	/**
	 * 删除所有未删除的报表项
	 * 
	 * @param eduClassId
	 * @return
	 */
	public boolean deleteAllNoDelByEduclassIds(Long[] eduClassIds) {
		StringBuilder sql = new StringBuilder("update " + tableName + " cree ");
		sql.append("set cree.is_del=?, cree.modify_date=? ");
		sql.append("where cree.is_del=? and cree.educlass_id in (" + CollectionKit.convert(eduClassIds, ",") + ")");
		return Db.update(sql.toString(), DEL_YES, new Date(), DEL_NO) >= 0;
	}
	
	/**
	 * 删除教学班考评点达成度是否成功
	 * 
	 * @param ids 编号
	 * @param date 时间
	 * @return
	 */
	public boolean deleteAll(Long[] ids, Date date) {
		String sql = "update "  + tableName + " set is_del = ?, modify_date = ?, statistics_date = ? where id in (" + CollectionKit.convert(ids, ",") + ")";
		return Db.update(sql, DEL_YES, date, date) >= 0;
	}
	
}
