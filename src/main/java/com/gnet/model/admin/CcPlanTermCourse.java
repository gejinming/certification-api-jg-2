package com.gnet.model.admin;

import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;


/**
 * 培养计划课程学期详情表
 * @type model
 * @table cc_plan_term_course
 * @author SY
 * @version 1.0
 * @date 2016年7月15日16:02:14
 *
 */
@TableBind(tableName = "cc_plan_term_course")
public class CcPlanTermCourse extends DbModel<CcPlanTermCourse> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcPlanTermCourse dao = new CcPlanTermCourse();
	
	/**
	 * 类型--上课学期
	 */
	public static final Integer TYPE_CLASS = 1;
	/**
	 * 类型--考试学期
	 */
	public static final Integer TYPE_EXAM = 2;
	
	/**
	 * 通过课程id编号数组获取对应的所有培养计划课程学期详情表，以及表对应的培养学期信息
	 * @param courseIds
	 * @return
	 */
	public List<CcPlanTermCourse> findWithPlanTermByCourseId(Long[] courseIds) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sb = new StringBuilder("select cptc.*, cpt.term_type termType, cpt.year, cpt.term, cpt.week_nums weekNums, cpt.plan_id planId, cpt.year_name yearName, cpt.term_name termName ");
		sb.append("from " + tableName + " cptc ");
		sb.append("left join " + CcPlanTerm.dao.tableName + " cpt on cpt.id = cptc.plan_term_id ");
		sb.append("where cpt." + CcPlanTerm.IS_DEL_LABEL + " = ? and cptc.is_del = ? ");
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		sb.append("and cptc.course_id in(" + CollectionKit.convert(courseIds, ",") + ")");
		return find(sb.toString(), params.toArray());
	}
	
	/**
	 * 通过课程id编号数组获取对应的所有培养计划课程学期详情表，以及表对应的培养学期信息
	 * @param courseId
	 * @return
	 */
	public List<CcPlanTermCourse> findWithPlanTermByCourseId(Long courseId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sb = new StringBuilder("select cptc.*, cpt.term_type termType, cpt.year, cpt.term, cpt.week_nums weekNums, cpt.plan_id planId, cpt.year_name yearName, cpt.term_name termName ");
		sb.append("from " + tableName + " cptc ");
		sb.append("left join " + CcPlanTerm.dao.tableName + " cpt on cpt.id = cptc.plan_term_id ");
		sb.append("where cpt." + CcPlanTerm.IS_DEL_LABEL + " = ? ");
		params.add(Boolean.FALSE);
		sb.append("and cptc.course_id = ? ");
		params.add(courseId);
		return find(sb.toString(), params.toArray());
	}
	
	/**
	 * 根据字段硬删除记录
	 * @param column
	 * @param id
	 * @return
	 */
	public boolean deleteAllByColumnHard(String column, Long value) {
		return Db.update("delete from " + tableName + " where " + column + "=? ", value) >= 0;
	}

	/**
	 * 找到当前版本下的开课学期
	 * @param planId
	 * 			学期编号
	 * @param type
	 * 			类型
	 * @return
	 * @author SY 
	 * @version 创建时间：2018年1月12日 上午2:34:19 
	 */
	public List<CcPlanTermCourse> findByPlanId(Long planId, Integer type) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sb = new StringBuilder("select cptc.*, cpt.term_name termName, cpt.year_name yearName, cpt.id planTermId ");
		sb.append("from " + tableName + " cptc ");
		sb.append("left join " + CcPlanTerm.dao.tableName + " cpt on cpt.id = cptc.plan_term_id and cpt.is_del = ? ");
		params.add(Boolean.FALSE);
		sb.append("where cpt.plan_id = ? ");
		params.add(planId);
		sb.append("and cptc.is_del = ? ");
		params.add(Boolean.FALSE);
		sb.append("and cptc.type = ? ");
		params.add(type);
		return find(sb.toString(), params.toArray());
	}
}
