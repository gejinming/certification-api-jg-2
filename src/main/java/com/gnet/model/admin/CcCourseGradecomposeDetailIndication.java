package com.gnet.model.admin;

import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;

/**
 * @type model
 * @description 考核成绩分析法学生课程目标成绩
 * @table cc_course_gradecompose_detail_indication
 * @author xzl
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course_gradecompose_detail_indication")
public class CcCourseGradecomposeDetailIndication extends DbModel<CcCourseGradecomposeDetailIndication> {

	private static final long serialVersionUID = 8489536854459982746L;
	public final static CcCourseGradecomposeDetailIndication dao = new CcCourseGradecomposeDetailIndication();
	
	/**
	 * 通过指标点编号和成绩组成明细编号返回成绩组成元素明细指标点关联信息
	 * @param id
	 *         成绩组成元素明细编号
	 * @param array
	 *        指标点编号
	 * @return
	 */
	public List<CcCourseGradecomposeDetailIndication> findDeatil(Long id, Long[] array) {
		StringBuffer sql = new StringBuffer("select * from " + tableName + " where course_gradecompose_detail_id = ? ");
		sql.append("and indication_id in ( " + CollectionKit.convert(array, ",") + " ) ");
		sql.append("and is_del = ? ");
		return find(sql.toString(), id, DEL_NO);
	}
	/*
	 * @param batchIds
	 * @return java.util.List<com.gnet.model.admin.CcCourseGradecomposeIndication>
	 * @author Gejm
	 * @description: 统计批次每个课程目标的总成绩
	 * @date 2020/7/6 18:36
	 */
	public  List<CcCourseGradecomposeDetailIndication> getIndictionScore(List<Long> batchIds){
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select sum(b.score) score,a.indication_id,batch_id,c.content indicationContent,c.sort indicationSort from " + tableName + " a ");
		sql.append("inner join cc_course_gradecompose_detail b on b.id=a.course_gradecompose_detail_id and b.is_del=? ");
		param.add(DEL_NO);
		sql.append("inner join cc_indication c on c.id=a.indication_id and c.is_del=? ");
		param.add(DEL_NO);
		sql.append("where b. batch_id in (" + CollectionKit.convert(batchIds, ",") + ")  and a.is_del=? ");
		param.add(DEL_NO);
		sql.append("group by a.indication_id,batch_id ");
		sql.append("order by sort");
		return find(sql.toString(), param.toArray());
	}

	/*
	 * @param batchIds
	 * @return java.util.List<com.gnet.model.admin.CcCourseGradecomposeIndication>
	 * @author Gejm
	 * @description: 每道题目支持的课程目标
	 * @date 2020/7/6 18:36
	 */
	public  List<CcCourseGradecomposeDetailIndication> getIndictionList(Long detailId,Long indicationId){
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select a.indication_id,sort from " + tableName + " a ");
		sql.append("inner join cc_course_gradecompose_detail b on b.id=a.course_gradecompose_detail_id and b.is_del=? ");
		param.add(DEL_NO);
		sql.append("inner join cc_indication c on c.id=a.indication_id and c.is_del=? ");
		param.add(DEL_NO);
		sql.append("where b.id = ?  and a.is_del=? ");
		param.add(detailId);
		param.add(DEL_NO);
		if (indicationId != null){
			sql.append("and a.indication_id=? ");
			param.add("indicationId");
		}
		sql.append("group by a.indication_id ");
		sql.append("order by sort");
		return find(sql.toString(), param.toArray());
	}
	/*
	 * @param indicationId
		 * @param courseGradeComposeId
	 * @return java.util.List<com.gnet.model.admin.CcCourseGradecomposeDetailIndication>
	 * @author Gejm
	 * @description: 查找包含指定课程目标的批次id
	 * @date 2020/7/10 15:11
	 */
	public List<CcCourseGradecomposeDetailIndication> findBatchList(Long indicationId,Long courseGradeComposeId){
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select b.batch_id from cc_course_gradecompose_detail_indication a ");
		sql.append("left join cc_course_gradecompose_detail b on a.course_gradecompose_detail_id=b.id ");
		sql.append("where  a.indication_id=? and b.course_gradecompose_id=? and a.is_del=0 and b.is_del=0 ");
		param.add(indicationId);
		param.add(courseGradeComposeId);
		sql.append("group by b.batch_id ");
		return find(sql.toString(), param.toArray());
	}

}
