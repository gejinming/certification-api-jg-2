package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


/**
 * @type model
 * @description 多批次课程目标
 * @table cc_course_gradecompose_batch_indication
 * @author GJM
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course_gradecompose_batch_indication")
public class CcCourseGradecomposeBatchIndication extends DbModel<CcCourseGradecomposeBatchIndication> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcCourseGradecomposeBatchIndication dao = new CcCourseGradecomposeBatchIndication();

	/**
	 *
	 * @param batchId
	 * @param indicationId
	 * @return 根据批次id查询关联的课程目标及成绩
	 */
	public List<CcCourseGradecomposeBatchIndication> findBatchIndicationList(Long batchId,Long indicationId){
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select a.*,b.name,c.content from cc_course_gradecompose_batch_indication a ");
		sql.append("left join cc_course_gradecompose_batch b on a.batch_id=b.id and b.is_del=0 ");
		sql.append("left join cc_indication c on c.id=a.indication_id and c.is_del=0 ");
		sql.append("where a.batch_id=? and a.is_del=0 ");
		params.add(batchId);
		if (indicationId !=null){
			sql.append("and a.indication_id=? ");
			params.add(indicationId);
		}
		sql.append("order by a.indication_id");
		return find(sql.toString(),params.toArray());
	}
	/*
	 * @param courseGradeComposeId
	 * @return java.util.List<com.gnet.model.admin.CcCourseGradecomposeBatchIndication>
	 * @author Gejm
	 * @description: 批次直接录入方式 统计成绩组成的课程目标总分
	 * @date 2020/8/27 12:02
	 */
	public List<CcCourseGradecomposeBatchIndication> indicationSumScore(Long courseGradeComposeId){
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("SELECT a.indication_id,sum(a.score) score FROM cc_course_gradecompose_batch_indication a ");
		sql.append("inner join cc_course_gradecompose_batch b on a.batch_id=b.id and b.is_del=0 ");
		sql.append("where a.is_del=0 and b.course_gradecompose_id=? ");
		params.add(courseGradeComposeId);
		sql.append("group by a.indication_id ");
		return find(sql.toString(),params.toArray());
	}
	/*
	 * @param courseGradeComposeId
		 * @param indicationId
	 * @return java.util.List<com.gnet.model.admin.CcCourseGradecomposeBatchIndication>
	 * @author Gejm
	 * @description: 查询成绩组成中含有指定课程目标的批次
	 * @date 2020/8/27 14:34
	 */
	public List<CcCourseGradecomposeBatchIndication> indicationBatchList(Long courseGradeComposeId,Long indicationId){
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select a.batch_id,b.name from cc_course_gradecompose_batch_indication a ");
		sql.append("inner join cc_course_gradecompose_batch b on a.batch_id=b.id and b.is_del=0 ");
		sql.append("where a.is_del=0 and b.course_gradecompose_id=?  and a.indication_id=? ");
		params.add(courseGradeComposeId);
		params.add(indicationId);
		sql.append("group by a.batch_id ");
		return find(sql.toString(),params.toArray());
	}
	/*
	 * @param courseGradecomposeId
	 * @return java.util.List<com.gnet.model.admin.CcCourseGradecomposeIndication>
	 * @author Gejm
	 * @description: 批次直接录入方式查询关联的课程目标
	 * @date 2020/8/27 16:38
	 */
	public List<CcCourseGradecomposeBatchIndication> findByCourseBatchGradecomposeId(Long courseGradecomposeId,Long batchId) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select ci.content indicationContent, ci.sort indicationSort, ci.id indicationId, "
				+ " cgbi.id courseGradecomposeIndicationId, cgbi.score maxScore from " + tableName + " cgbi ");
		sql.append("inner join " + CcIndication.dao.tableName + " ci on ci.id =  cgbi.indication_id ");
		sql.append("inner join cc_course_gradecompose_batch cgb on cgbi.batch_id=cgb.id and cgb.is_del=0 ");
		//sql.append("INNER JOIN cc_course_gradecompose_indication ccgi on ccgi.course_gradecompose_id = cgb.course_gradecompose_id ");
		sql.append("where cgb.course_gradecompose_id = ?  and cgbi.batch_id=? and cgbi.is_del=0 ");
		param.add(courseGradecomposeId);
		param.add(batchId);
		//sql.append("and ccgi.is_del = ? ");
		sql.append("and ci.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("order by ci.sort asc ");
		return find(sql.toString(), param.toArray());
	}
	/*
	 * @param courseGradecomposeId
	 * @return java.util.List<com.gnet.model.admin.CcCourseGradecomposeIndication>
	 * @author Gejm
	 * @description: 批次直接录入方式查询关联的课程目标
	 * @date 2020/8/27 16:38
	 */
	public List<CcCourseGradecomposeBatchIndication> findByCourseBatchGradecomposeIds(List<Long> courseGradeComposeIds,Long batchId,Long gradeIndicationId) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select ci.content , ci.sort , ci.id indicationId, cgb.course_gradecompose_id,"
				+ " cgbi.id courseGradecomposeIndicationId, cgbi.score maxScore,ccgi.id from " + tableName + " cgbi ");
		sql.append("inner join " + CcIndication.dao.tableName + " ci on ci.id =  cgbi.indication_id ");
		sql.append("inner join cc_course_gradecompose_batch cgb on cgbi.batch_id=cgb.id and cgb.is_del=0 ");
		sql.append("INNER JOIN cc_course_gradecompose_indication ccgi on ccgi.course_gradecompose_id = cgb.course_gradecompose_id and cgbi.indication_id=ccgi.indication_id and  ccgi.is_del=0 ");
		sql.append("where  cgbi.is_del=0  and cgbi.batch_id=?  ");
		param.add(batchId);
		if(courseGradeComposeIds != null && !courseGradeComposeIds.isEmpty()) {
			sql.append("and cgb.course_gradecompose_id in ("+ CollectionKit.convert(courseGradeComposeIds, ",")+") ");
		}
		sql.append("and ci.is_del = ? ");
		if (gradeIndicationId !=null){
			sql.append("and ccgi.id=? ");
			param.add(gradeIndicationId);
		}
		param.add(Boolean.FALSE);
		sql.append("order by ci.sort asc ");
		return find(sql.toString(), param.toArray());
	}
}
