package com.gnet.model.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @type model
 * @description 课程目标表
 * @table cc_indication
 * @author xzl
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_indication")
public class CcIndication extends DbModel<CcIndication> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcIndication dao = new CcIndication();

	/**
	 * 通过教师开课获取当前课程下的，开课目标，按照sort排序
	 * @param teacherCourseId
	 * 			教师开课编号
	 * @return
	 * @author SY
	 * @version 创建时间：2017年11月22日 下午4:09:33
	 */
	public List<CcIndication> findByTeacherCourseIdOrderBySort(Long teacherCourseId) {
		List<Object> params = new ArrayList<>();
		StringBuilder sb = new StringBuilder("select ci.* from "  + tableName + " ci ");
		sb.append("inner join " + CcTeacherCourse.dao.tableName + " ctc on ctc.course_id = ci.course_id and cc.is_del = ? ");
		params.add(DEL_NO);
		sb.append("where ci.is_del = ? ");
		params.add(DEL_NO);
		sb.append("and ctc.id = ? ");
		params.add(teacherCourseId);

		sb.append("order by ci.sort ");
		return null;
	}

	/**
	 * 查询课程目标
	 * @param pageable
	 * @param courseId
	 * @return
	 */
	public Page<CcIndication> page(Pageable pageable, Long courseId) {
		StringBuilder exceptSql = new StringBuilder("from " + tableName + " ci ");
		exceptSql.append("inner join " + CcCourse.dao.tableName + " cc on cc.id = ci.course_id ");
		List<Object> params = Lists.newArrayList();

		exceptSql.append("and cc.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and ci.is_del = ? ");
		params.add(Boolean.FALSE);

		if (courseId != null) {
			exceptSql.append("and ci.course_id = ? ");
			params.add(courseId);
		}

		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}else{
			exceptSql.append("order by ci.sort asc ");
		}

		return CcIndication.dao.paginate(pageable, "select ci.*, cc.name courseName ", exceptSql.toString(), params.toArray());
	}

	/**
	 * 课程目标默认期望值
	 */
	public final static BigDecimal DEFAULT_EXPECTED_VALUE = BigDecimal.valueOf(1);

	/**
	 * 已关联课程目标的成绩组成
	 * @param deleteIndicationIds
	 * @return
	 */
	public List<CcIndication> findByIndicationIds(List<Long> deleteIndicationIds) {
		StringBuilder sql = new StringBuilder("select ci.sort from " + tableName + " ci ");
		sql.append("inner join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.indication_id = ci.id and ccgi.is_del = ? ");
		sql.append("where ci.id in (" + CollectionKit.convert(deleteIndicationIds, ",") + ") and ci.is_del = ? ");
		sql.append("group by ci.sort ");
		return find(sql.toString(), DEL_NO, DEL_NO);
	}


	/**
	 * 课程下已关联课程目标的成绩组成
	 * @param courseId
	 * @return
	 */
	public List<CcIndication> findByCourseId(Long courseId) {
		StringBuilder sql = new StringBuilder("select ci.* from " + tableName + " ci ");
		sql.append("inner join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.indication_id = ci.id and ccgi.is_del = ? ");
		sql.append("where ci.is_del = ? and ci.course_id = ? ");
		sql.append("group by ci.sort ");
		return find(sql.toString(), DEL_NO, DEL_NO, courseId);
	}
	
	/**
	 * 版本下与课程指标点关联的课程目标
	 * @param planId
	 * @return
	 */
	public List<CcIndication> finfByPlanId(Long planId) {
		//List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select ci.content, ci.sort, cic.id from " + tableName + " ci ");
		sql.append("inner join " +  CcCourseTargetIndication.dao.tableName  + " ccti on ccti.indication_id = ci.id ");
		sql.append("inner join " + CcIndicationCourse.dao.tableName + " cic on cic.id = ccti.indication_course_id and cic.is_del = 0 ");
		//param.add(Boolean.FALSE);
		sql.append("inner join " + CcCourse.dao.tableName + " cc on cc.id = cic.course_id and cc.is_del = 0 ");
		//param.add(Boolean.FALSE);
		sql.append("where ci.is_del = 0 ");
		//param.add(Boolean.FALSE);
		if (planId!=null){
			sql.append("  and cc.plan_id = "+planId);

		}

		return find(sql.toString());
    }

	/**
	 * 获取某课程的某个指标点下的数据
	 * @param courseId
	 * @param indicatorPointId
	 *			指标点编号 (不可以为空)
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年11月28日 下午9:27:09 
	 */
	public List<CcIndication> findByCourseIdAndIndicatorPointId(Long courseId, Long indicatorPointId) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select ci.* from " + tableName + " ci ");
		sql.append("inner join " + CcCourseTargetIndication.dao.tableName + " ccti on ccti.indication_id = ci.id ");
		sql.append("inner join " + CcIndicationCourse.dao.tableName + " cic on cic.id = ccti.indication_course_id and cic.is_del = ? ");
		params.add(DEL_NO);
		sql.append("where ci.is_del = ? and ci.course_id = ? ");
		params.add(DEL_NO);
		params.add(courseId);
		sql.append("and cic.indication_id = ? ");
		params.add(indicatorPointId);
		sql.append("order by ci.sort ");
		return find(sql.toString(), params.toArray());
	}
	
	/**
	 * 获取某课程下的数据，包括达成度信息(存在多个教学班)
	 * @param courseId
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年11月28日 下午9:27:09 
	 */
	public List<CcIndication> findAllByCourseId(Long courseId) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select ci.*, ceeaa.educlass_id educlassId, ceeaa.achieve_value achieveValue, ceeaa.except_achieve_value exceptAchieveValue from " + tableName + " ci ");
		sql.append("left join " + CcEdupointEachAimsAchieve.dao.tableName + " ceeaa on ceeaa.indication_id = ci.id ");
		sql.append("where ci.is_del = ? and ci.course_id = ? ");
		params.add(DEL_NO);
		params.add(courseId);
		sql.append("order by ci.sort ");
		return find(sql.toString(), params.toArray());
	}

	/**
	 * 获取一些课程下的数据，包括达成度信息
	 * @param courseIdList
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年12月1日 下午2:11:14 
	 */
	public List<CcIndication> findEachIndicationCourseAndGradecomposeIndicationByCourseIds(List<Long> courseIdList) {
		if(courseIdList == null || courseIdList.isEmpty()) {
			return new ArrayList<>();
		}
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select ci.*, ccgi.id gradecomposeIndicationId, ccti.indication_course_id indicationCourseId from " + tableName + " ci ");
		sql.append("inner join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.indication_id = ci.id and ccgi.is_del = ? ");
		sql.append("inner join " + CcCourseTargetIndication.dao.tableName + " ccti on ccti.indication_id = ci.id ");
		params.add(DEL_NO);
		sql.append("where ci.is_del = ? and ci.course_id in ("+CollectionKit.convert(courseIdList, ",")+") ");
		params.add(DEL_NO);
		sql.append("order by ci.sort ");
		return find(sql.toString(), params.toArray());
	}

	/**
	 * 开课课程成绩组成关联的课程目标
	 * @param courseGradeComposeId
	 * @return
	 */
	public List<CcIndication> findCourseGradeComposeId(Long courseGradeComposeId) {
		StringBuilder sql = new StringBuilder("select ci.* from " + tableName + " ci ");
		sql.append("inner join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.indication_id = ci.id and ccgi.course_gradecompose_id = ? and ccgi.is_del = ? ");
		sql.append("where ci.is_del = ? ");
		sql.append("order by ci.sort ");
		return find(sql.toString(), courseGradeComposeId, DEL_NO, DEL_NO);
	}
}
