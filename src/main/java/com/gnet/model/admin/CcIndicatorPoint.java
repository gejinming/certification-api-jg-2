package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;


import java.util.ArrayList;
import java.util.List;

/**
 * 指标点【建工】
 * @type model
 * @table cc_indicator_point
 * @author xzl
 * @version 1.0
 * @date 2017年11月17日
 *
 */
@TableBind(tableName = "cc_indicator_point")
public class CcIndicatorPoint extends DbModel<CcIndicatorPoint> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcIndicatorPoint dao = new CcIndicatorPoint();

	/**
	 * 查找本毕业要求编号下最新的一条数据（indexNum最大的）
	 * @param graduateId
	 * 			毕业要求编号
	 * @return
	 */
	public CcIndicatorPoint findLastByGraduateId(Long graduateId) {
		return findFirst("select * from " + tableName + " where graduate_id = ? and is_del = ? order by index_num desc ", graduateId, Boolean.FALSE);
	}

	/**
	 * 返回某个毕业要求下未删除的指标点
	 * @param graduateId
	 * @return
	 */
	public List<CcIndicatorPoint> findAll(Long graduateId) {
		return find("select * from " + tableName + " where graduate_id = ? and is_del = ? order by index_num asc ", graduateId, Boolean.FALSE);
	}

	/**
	 * 查找本毕业要求编号下最新的一条数据（indexNum最大的）
	 * @param propertyName
	 *
	 * @param graduateId
	 * 			毕业要求编号
	 * @return
	 */
	public List<CcIndicatorPoint> findOrderFilteredByColumnIn(String propertyName, Long graduateId[]) {
		return find("select * from " + tableName + " where " + propertyName + " in (" + CollectionKit.convert(graduateId, ",") + ") and is_del = ? order by index_num ", Boolean.FALSE);
	}

	/**
	 * 通过课程和id来获取,当存在指标点编号的时候，直接忽略课程编号的要求。否则按照课程编号查找下面所有的指标点
	 * @param courseId
	 * @param indicatorPointId
	 * 			指标点编号，不可以为空
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年11月29日 上午10:23:46 
	 */
	public List<CcIndicatorPoint> findAllByCourseIdAndId(Long courseId, Long indicatorPointId) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select cip.*, cge.index_num graduateIndexNum, cic.weight indicationWeight ");
		sql.append("from " + tableName + " cip ");
		sql.append("inner join " + CcIndicationCourse.dao.tableName + " cic on cic.indication_id = cip.id and cic.is_del = ? ");
		sql.append("inner join " + CcGraduate.dao.tableName + " cge on cge.id = cip.graduate_id ");
		params.add(DEL_NO);
		sql.append("where cic.course_id = ? ");
		params.add(courseId);
		sql.append("and cip.is_del = ? ");
		params.add(DEL_NO);
		sql.append("and cip.id = ? ");
		params.add(indicatorPointId);
		sql.append("order by cip.index_num ");
		return find(sql.toString(), params.toArray());
	}
	
	/**
	 * 通过课程和id来获取，否则按照课程编号查找下面所有的指标点
	 * @param courseId
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年11月29日 上午10:23:46 
	 */
	public List<CcIndicatorPoint> findAllByCourseId(Long courseId) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select cip.*, cge.index_num graduateIndexNum, cic.weight indicationWeight ");
		sql.append("from " + tableName + " cip ");
		sql.append("inner join " + CcIndicationCourse.dao.tableName + " cic on cic.indication_id = cip.id and cic.is_del = ? ");
		sql.append("inner join " + CcGraduate.dao.tableName + " cge on cge.id = cip.graduate_id ");
		params.add(DEL_NO);
		sql.append("where cic.course_id = ? ");
		params.add(courseId);
		sql.append("and cip.is_del = ? ");
		params.add(DEL_NO);
		sql.append("order by cip.index_num ");
		return find(sql.toString(), params.toArray());
	}
	
	/**
	 * 通过课程和id来获取，否则按照课程编号查找下面所有的指标点
	 * @param indicatorPointId
	 * 			指标点编号
	 * @param courseId
	 * 			课程编号
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年11月29日 上午10:23:46 
	 */
	public CcIndicatorPoint findAllByIdAndCourseId(Long indicatorPointId, Long courseId) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select cip.*, cge.index_num graduateIndexNum, cic.weight indicationWeight ");
		sql.append("from " + tableName + " cip ");
		sql.append("inner join " + CcIndicationCourse.dao.tableName + " cic on cic.indication_id = cip.id and cic.is_del = ? and cic.course_id = ? ");
		params.add(DEL_NO);
		// Edit by SY 2019年11月6日 我发现页面显示的指标点权重有错误。然后这里查出来四条，选中第一条不是正确答案，所以加了个courseId的筛选条件。
		params.add(courseId);
		sql.append("inner join " + CcGraduate.dao.tableName + " cge on cge.id = cip.graduate_id ");
		sql.append("where cip.id = ? ");
		params.add(indicatorPointId);
		sql.append("and cip.is_del = ? ");
		params.add(DEL_NO);
		sql.append("order by cip.index_num ");
		return findFirst(sql.toString(), params.toArray());
	}
	/*
	 * @param courseId
		 * @param educlassId
	 * @return java.util.List<com.gnet.model.admin.CcIndicatorPoint>
	 * @author Gejm
	 * @description: 根据课程及教学班id查询毕业指标点、课程目标、期望值、课程目标达成度
	 * @date 2020/8/14 18:13
	 */
	public List<CcIndicatorPoint> findIndicationAndPoint(Long courseId,Long educlassId){
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select cip.id pointId,cic.weight,cg.index_num graduateIndexNum,cip.index_num,cip.content pointCont,ci.sort,ci.id indicationId,ci.content indicationCont,");
		sql.append(" ci.expected_value,ceeaa.achieve_value achieveValue,ceeaa.except_achieve_value exceptAchieveValue  ");
		sql.append("from cc_indicator_point cip ");
		sql.append("left join cc_indication_course cic on cic.indication_id=cip.id and cic.is_del=0 ");
		sql.append("left join cc_course_target_indication  cti on cti.indication_course_id=cic.id ");
		sql.append("left join cc_indication ci on ci.id=cti.indication_id and ci.is_del=0 ");
		sql.append("left JOIN cc_graduate cg ON cg.id = cip.graduate_id ");
		sql.append("INNER JOIN cc_edupoint_each_aims_achieve ceeaa ON ceeaa.indication_id = ci.id ");
		sql.append("where cic.course_id=? and ceeaa.educlass_id=? order by cg.index_num,cip.index_num,ci.sort");
		params.add(courseId);
		params.add(educlassId);
		return find(sql.toString(), params.toArray());
	}
	/*
	 * @param courseId
		 * @param educlassId
	 * @return java.util.List<com.gnet.model.admin.CcIndicatorPoint>
	 * @author Gejm
	 * @description: 统计指标点合并行数、最小课程目标达成度，权重
	 * @date 2020/8/20 16:22
	 */
	public List<CcIndicatorPoint> findIndicationAndPointNum(Long courseId,Long educlassId){
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select cip.id pointId,count(ci.id) megeNum,min(ceeaa.achieve_value) achieve,cic.weight ");
		sql.append("from cc_indicator_point cip ");
		sql.append("left join cc_indication_course cic on cic.indication_id=cip.id and cic.is_del=0 ");
		sql.append("left join cc_course_target_indication  cti on cti.indication_course_id=cic.id ");
		sql.append("left join cc_indication ci on ci.id=cti.indication_id and ci.is_del=0 ");
		sql.append("left JOIN cc_graduate cg ON cg.id = cip.graduate_id ");
		sql.append("INNER JOIN cc_edupoint_each_aims_achieve ceeaa ON ceeaa.indication_id = ci.id ");
		sql.append("where cic.course_id=? and ceeaa.educlass_id=? ");
		params.add(courseId);
		params.add(educlassId);
		sql.append("group by cip.id,cic.weight ");
		return find(sql.toString(), params.toArray());
	}
}
