package com.gnet.model.admin;

import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.gnet.pager.Pageable;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * 
 * @type model
 * @table cc_course_gradecompose_detail
 * @author sll
 * @version 1.0
 * @date 2016年07月06日 14:37:10
 *
 */
@TableBind(tableName = "cc_course_gradecompose_detail")
public class CcCourseGradeComposeDetail extends DbModel<CcCourseGradeComposeDetail> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcCourseGradeComposeDetail dao = new CcCourseGradeComposeDetail();
	
	/**
	 * 查看成绩组成元素明细表列表分页
	 * 
	 * @param name
	 * @param detail
	 * @return
	 */
	public Page<CcCourseGradeComposeDetail> page(Pageable pageable, String name, String detail, Long courseGradeComposeId,Long batchId) {
		StringBuilder exceptSql = new StringBuilder("from " + CcCourseGradeComposeDetail.dao.tableName + " ");
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append("where is_del = ? ");
		params.add(Boolean.FALSE);
		
		// 删选条件
		if (!StrKit.isBlank(name)) {
			exceptSql.append("and name like '" + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		if (!StrKit.isBlank(detail)) {
			exceptSql.append("and detail like '" + StringEscapeUtils.escapeSql(detail) + "%' ");
		}
		if (courseGradeComposeId != null) {
			exceptSql.append("and course_gradecompose_id = ? ");
			params.add(courseGradeComposeId);
		}
		if (batchId != null){
			exceptSql.append("and batch_id = ? ");
			params.add(batchId);
		}
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcCourseGradeComposeDetail.dao.paginate(pageable, "select * ", exceptSql.toString(), params.toArray());
	}

	
	/**
	 * 检查同一成绩组成下的成绩组成明细题号是否重复
	 * 
	 * @param name
	 * @param originValue
	 * @return
	 */
	public Boolean isExisted(String name, String originValue, Long courseGradecomposeId,Long batchId){
		if (StrKit.notBlank(originValue)) {
			if (batchId !=null ){
				return Db.queryLong("select count(1) from " + tableName + " where is_del = ? and name = ? and name!= ? and course_gradecompose_id = ? and batch_id = ? ",Boolean.FALSE, name, originValue, courseGradecomposeId,batchId) > 0;
			}

			return Db.queryLong("select count(1) from " + tableName + " where is_del = ? and name = ? and name!= ? and course_gradecompose_id = ? ",Boolean.FALSE, name, originValue, courseGradecomposeId) > 0;
		} else {
			if (batchId !=null ){
				return Db.queryLong("select count(1) from " + tableName + " where is_del = ? and name = ? and course_gradecompose_id = ? and batch_id = ? ",Boolean.FALSE, name, courseGradecomposeId,batchId) > 0;
			}
			return Db.queryLong("select count(1) from " + tableName + " where is_del = ? and name = ? and course_gradecompose_id = ? ",Boolean.FALSE, name, courseGradecomposeId) > 0;
		}
	}
	
	/**
	 * @param name
	 * @param courseGradecomposeId
	 * @return
	 */
	public Boolean isExisted(String name, Long courseGradecomposeId,Long batchId) {
		return isExisted(name, null, courseGradecomposeId,batchId);
	}
	
	
	/**
	 * 根据成绩组成元素明细编号查找详细
	 * 
	 * @param id
	 * @return
	 */
	public CcCourseGradeComposeDetail findDetailById(Long id){
		
		StringBuilder exceptSql = new StringBuilder("select ccgd.id, cc.id course_id,cc.name course_name,cg.id grade_compose_id,cg.name grade_compose_name,ct.id teacher_id,ct.name teacher_name,ctm.id term_id,ctm.term,ctm.start_year,ctm.end_year ");
		exceptSql.append(" from " + CcCourseGradeComposeDetail.dao.tableName + " ccgd  ");
		exceptSql.append(" left join " + CcCourseGradecompose.dao.tableName + " ccg on ccgd.course_gradecompose_id = ccg.id ");
		exceptSql.append(" left join " + CcGradecompose.dao.tableName + " cg on cg.id = ccg.gradecompose_id ");
		exceptSql.append(" left join " + CcTeacherCourse.dao.tableName + " ctc on ctc.id = ccg.teacher_course_id ");
		exceptSql.append(" left join " + CcTerm.dao.tableName + " ctm on ctm.id = ctc.term_id "); 
		exceptSql.append(" left join " + CcTeacher.dao.tableName + " ct on ct.id = ctc.teacher_id ");
		exceptSql.append(" left join " + CcCourse.dao.tableName + " cc on ctc.course_id = cc.id ");
		exceptSql.append(" where ccgd.id = ? ");
		exceptSql.append(" and ccgd.is_del = ? and ccg.is_del = ? and ctc.is_del = ? ");
		
		List<Object> params = Lists.newArrayList();
		params.add(id);
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		
		return findFirst(exceptSql.toString(), params.toArray());
	}

	/**
	 * 该开课课程组成下是否存在题目
	 * @param courseGradecomposeId
	 * @return
	 */
	public boolean isExist(Long courseGradecomposeId) {
		return Db.queryLong("select count(1) from " + tableName + " where course_gradecompose_id = ? and is_del = ? " , courseGradecomposeId, Boolean.FALSE) > 0 ;
	}

	/**
	 * 通过开课课程成绩组成编号下的题目分数得出合计分值以及对应指标点的满分值(EM00528，EM00549)
	 *
	 * updated: 通过开课成绩组成编号 获取 该成绩组成编号下 对应的每个课程目标下 题目汇总的总分值和满分值
	 * @param courseGradecomposeId 				开课成绩组成ID
	 * @param indicationIds 					课程目标ID集合
	 * @param courseGradecomposeDetailId 		开课成绩组成明细ID
	 * @return
	 */
	public List<CcCourseGradeComposeDetail> findByCourseGradeComposeId(Long courseGradecomposeId, Long[] indicationIds, Long courseGradecomposeDetailId) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select ccgd.course_gradecompose_id, ccgdi.indication_id, sum(ccgd.score) allScore, ccgi.max_score, "
				+ " cg.name gradecomposeName, ci.sort indicationSort, ci.content from " + tableName + " ccgd ");
		sql.append("left join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.id = ccgd.course_gradecompose_id ");
		sql.append("left join "  + CcCourseGradecomposeDetailIndication.dao.tableName + " ccgdi on ccgdi.course_gradecompose_detail_id = ccgd.id ");
		sql.append("left join " + CcGradecompose.dao.tableName + " cg on cg.id = ccg.gradecompose_id ");
		sql.append("left join " + CcIndication.dao.tableName + " ci on ci.id = ccgdi.indication_id ");
		sql.append("left join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on (ccgi.course_gradecompose_id = ? and ccgi.indication_id = ccgdi.indication_id) ");
		param.add(courseGradecomposeId);
		sql.append("where ccgd.course_gradecompose_id = ? ");
		param.add(courseGradecomposeId);
		sql.append("and ccgd.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ccgdi.is_del = ? ");
		param.add(Boolean.FALSE);
		if(indicationIds != null && indicationIds.length > 0){
			sql.append("and ccgdi.indication_id in (" +  CollectionKit.convert(indicationIds, ",") + ") ");
		}
		if(courseGradecomposeDetailId != null){
			sql.append("and ccgd.id != ? ");
			param.add(courseGradecomposeDetailId);
		}
		sql.append("and ccg.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and cg.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ci.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ccgi.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("group by ccgdi.indication_id ");
		return find(sql.toString(), param.toArray());
	}
	/*
	 * @param courseGradecomposeId
	 * @return int
	 * @author Gejm
	 * @description: 获取成绩组成下的题目
	 * @date 2020/7/3 18:49
	 */
	public List<CcCourseGradeComposeDetail> topicList(Long courseGradecomposeId,Long batchId){
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select * from cc_course_gradecompose_detail where  is_del =? ");

		param.add(Boolean.FALSE);
		if (courseGradecomposeId !=null){
			sql.append("and course_gradecompose_id = ? ");
			param.add(courseGradecomposeId);
		}
		if (batchId != null){
			sql.append("and batch_id=? ");
			param.add(batchId);
		}
		return find(sql.toString(), param.toArray());

	}
}
