package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程目标与指标点与课程关系的关系表
 * @type model
 * @table cc_course_target_indication
 * @author xzl
 * @version 1.0
 * @date 2017年11月17日11:26:14
 *
 */
@TableBind(tableName = "cc_course_target_indication")
public class CcCourseTargetIndication extends DbModel<CcCourseTargetIndication> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcCourseTargetIndication dao = new CcCourseTargetIndication();

	/**
	 * 课程指标点下已关联的课程目标
	 * @param courseIndicationId
	 * @return
	 */
	public List<CcCourseTargetIndication> findByIndicationCourseId(Long courseIndicationId) {
		StringBuilder sql = new StringBuilder("select ci.* from " + tableName + " ccti ");
		sql.append("inner join " + CcIndication.dao.tableName + " ci on ci.id = ccti.indication_id ");
		sql.append("where ci.is_del = ? and ccti.indication_course_id = ? ");
		sql.append("order by ci.sort ");
		return find(sql.toString(), DEL_NO, courseIndicationId);
	}

	/**
	 * 课程指标点下已关联的课程目标
	 * @param courseId
	 * @return
	 */
	public List<CcCourseTargetIndication> findByCourseId(Long courseId) {
		List<Object> param = Lists.newArrayList();
		// TODO SY 我把这里代码和xzl代码合并了，xzl的indicationId都要改改成indicatorPointI，但是代码太多了，改不全，没时间……先这样吧
		StringBuilder sql = new StringBuilder("select ccti.*, ccti.id courseTargetIndicationId, cic.indication_id indicatorPointId, cip.id indicationId, cip.index_num indicationIndexNum, cip.content indicationContent, cg.index_num graduateIndexNum, cg.content graduateContent, " +
				"ci.sort, ci.content from " + tableName + " ccti "  );
		sql.append("inner join " + CcIndication.dao.tableName + " ci on ci.id = ccti.indication_id and ci.is_del = ? and ci.course_id = ? ");
		param.add(DEL_NO);
		param.add(courseId);
		sql.append("inner join " + CcIndicationCourse.dao.tableName + " cic on cic.id = ccti.indication_course_id and cic.is_del = ? and cic.course_id = ? ");
		param.add(DEL_NO);
		param.add(courseId);
		sql.append("inner join " + CcIndicatorPoint.dao.tableName + " cip on cip.id = cic.indication_id and cip.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join " + CcGraduate.dao.tableName + " cg on cg.id = cip.graduate_id and cg.is_del = ? ");
		param.add(DEL_NO);
		sql.append("order by cg.index_num, cip.index_num , ci.sort ");
		return find(sql.toString(), param.toArray());
	}
	
	/**
	 * 获取课程下的指标点和课程目标关系
	 * @param indicatorPointId
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年11月29日 上午11:29:22 
	 */
	public List<CcCourseTargetIndication> findByIndicatorPointId(Long indicatorPointId) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select ccti.*, cic.indication_id indicatorPointId from " + tableName + " ccti ");
		sql.append("inner join " + CcIndicationCourse.dao.tableName + " cic on cic.id = ccti.indication_course_id ");
		sql.append("where cic.is_del = ? and cic.indication_id = ? ");
		params.add(DEL_NO);
		params.add(indicatorPointId);
		return find(sql.toString(), params.toArray());
	}
}
