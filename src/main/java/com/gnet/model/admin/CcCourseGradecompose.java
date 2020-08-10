package com.gnet.model.admin;

import java.util.ArrayList;
import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * @type model
 * @description 开课课程成绩组成元素表操作，包括对数据的增删改查与列表
 * @table cc_course_gradecompose
 * @author SY
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course_gradecompose")
public class CcCourseGradecompose extends DbModel<CcCourseGradecompose> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcCourseGradecompose dao = new CcCourseGradecompose();
	
	/**
	 * 单批次指标点成绩直接输入
	 */
	public static final Integer DIRECT_INPUT_SCORE = 1;
	
	/**
	 * 由单批次题目成绩汇总输入
	 */
	public static final Integer SUMMARY_INPUT_SCORE = 2;

	/**
	 * 由多批次题目成绩汇总输入
	 */
	public static final Integer SUMMARY_MANYINPUT_SCORE = 3;
	/**
	 * 由多批次指标点成绩直接输入
	 */
	public  static final Integer DIRECT_MANYINPUT_SCORE=4;
	/**
	 * 某门课程的开课课程组成
	 * @param pageable
	 * @param teacherCourseId
	 * @return
	 */
	public Page<CcCourseGradecompose> page(Pageable pageable, Long teacherCourseId) {
		List<Object> params = Lists.newArrayList();
		String selectString = "select ccg.*, cg.name ";
		StringBuilder exceptSql = new StringBuilder("from " + CcCourseGradecompose.dao.tableName + " ccg ");
		exceptSql.append("left join " + CcGradecompose.dao.tableName + " cg on cg.id =  ccg.gradecompose_id ");
		exceptSql.append("where ccg.teacher_course_id = ? ");
		params.add(teacherCourseId);
		// 增加条件，为非软删除的
		exceptSql.append("and cg.is_del=? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and ccg.is_del=? ");
		params.add(Boolean.FALSE);
		
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}else{
			exceptSql.append("order by ccg.sort desc, ccg.id desc");
		}
		
		return CcCourseGradecompose.dao.paginate(pageable, selectString, exceptSql.toString(), params.toArray());
	}
	
	/**
	 * 判断课程组成元素是否属于同一门课程
	 * @param gradecomposeId
	 * @return
	 */
	public boolean isBelongSameCouse(Long[] gradecomposeId) { 
		String sql = "select count(1) from " + tableName + " where id in (" + CollectionKit.convert(gradecomposeId, "," ) + ") group by teacher_course_id " ; 
		return Db.query(sql).size() > 1;
	}

	
	/**
	 * 通过开课课程成绩组成元素查看开课课程与成绩组件的详情
	 * @param id
	 *         开课课程成绩组成元素编号
	 * @return
	 */
	public CcCourseGradecompose findDetailById(Long id) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select ccg.id, cc.name courseName, ct.name teacherName, ctc.result_type resultType, ctm.start_year startYear, "
				+ "ctm.end_year endYear, ctm.term, ctm.term_type termType, ctc.grade, cg.name gradecomposeName from " + tableName + " ccg ");
		sql.append("left join " + CcGradecompose.dao.tableName + " cg on cg.id = ccg.gradecompose_id ");
		sql.append("left join " + CcTeacherCourse.dao.tableName + " ctc on ctc.id = ccg.teacher_course_id ");
		sql.append("left join " + CcCourse.dao.tableName + " cc on cc.id = ctc.course_id ");
		sql.append("left join " + CcTerm.dao.tableName + " ctm on ctm.id = ctc.term_id ");
		sql.append("left join " + CcTeacher.dao.tableName + " ct on ct.id = ctc.teacher_id ");
		sql.append("where ccg.id = ? ");
		param.add(id);
		sql.append("and ccg.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and cg.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ctc.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and cc.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ctm.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ct.is_del = ? ");
		param.add(Boolean.FALSE);
		
		return findFirst(sql.toString(), param.toArray());
	}

	/**
	 * 是否允许修改教师开课程年级
	 * @param teacherCourseId
	 * @return
	 */
	public Boolean isAllowEditGrade(Long teacherCourseId) {
		String sql = "select count(1) from " + tableName + " where teacher_course_id = ? and is_del = ? ";
		return Db.queryLong(sql, teacherCourseId, DEL_NO) <= 0 ;
	}

	/**
	 * 某门教师开课课程关联的成绩组成以及对应的比重
	 * @param teacherCourseId
	 * @return
	 */
	public List<CcCourseGradecompose> findByTeacherCourseId(Long teacherCourseId) {
		StringBuilder sql = new StringBuilder("select ccg.percentage, cg.name gradecomposeName, ccg.id gradecomposeId from " +  tableName + " ccg ");
		sql.append("inner join " + CcGradecompose.dao.tableName + " cg on cg.id = ccg.gradecompose_id and cg.is_del = ? ");
		sql.append("where ccg.teacher_course_id = ?  and ccg.is_del = ? ");
		return find(sql.toString(), DEL_NO, teacherCourseId, DEL_NO);
	}
	
	/**
	 * 某门教师开课课程关联的成绩组成以及对应的比重
	 * @param teacherCourseId
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年10月25日 下午3:45:41 
	 */
	public List<CcCourseGradecompose> findByTeacherCourseIdOrderBySort(Long teacherCourseId) {
		StringBuilder sql = new StringBuilder("select ccg.*, cg.name from " +  tableName + " ccg ");
		sql.append("inner join " + CcGradecompose.dao.tableName + " cg on cg.id = ccg.gradecompose_id and cg.is_del = ? ");
		sql.append("where ccg.teacher_course_id = ?  and ccg.is_del = ? ");
		sql.append("order by ccg.sort asc");
		return find(sql.toString(), DEL_NO, teacherCourseId, DEL_NO);
	}
	
	/**
	 * 某门教师开课课程关联的成绩组成以及对应的比重
	 * @param teacherCourseId
	 * @param courseGradeComposeIds
	 * 			指定开课课程成绩组成元素编号，如果为空，则不加限制
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年10月25日 下午3:45:41 
	 */

	public List<CcCourseGradecompose> findByTeacherCourseIdAndCourseGradeComposeIdsOrderBySort(Long teacherCourseId, List<Long> courseGradeComposeIds) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select ccg.*, cg.name from " +  tableName + " ccg ");
		sql.append("inner join " + CcGradecompose.dao.tableName + " cg on cg.id = ccg.gradecompose_id and cg.is_del = ? ");
		params.add(DEL_NO);
		sql.append("where ccg.teacher_course_id = ?  and ccg.is_del = ? ");
		params.add(teacherCourseId);
		params.add(DEL_NO);
		if(courseGradeComposeIds != null && !courseGradeComposeIds.isEmpty()) {
			sql.append("and ccg.id in ("+ CollectionKit.convert(courseGradeComposeIds, ",")+") ");
		}
		sql.append("order by ccg.sort asc");
		return find(sql.toString(), params.toArray());
	}

	/**
	 * 开课课程是否关联了成绩组成以及指标点
	 * @param teacherCourseId
	 * @return
	 */
	public Boolean isRelationGradecomposeAndIndication(Long teacherCourseId){
		StringBuilder sql = new StringBuilder("select count(1) from " + tableName + " ccg ");
		sql.append("inner join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.course_gradecompose_id = ccg.id and ccgi.is_del = ? ");
		sql.append("where ccg.teacher_course_id = ?  and ccg.is_del = ? ");
		return Db.queryLong(sql.toString(), DEL_NO, teacherCourseId, DEL_NO) > 0;
	}

	/**
	 * 查询教学班对应的开课课程的成绩组成
	 * @param eduClassIds
	 * @return
	 */
	public List<CcCourseGradecompose> findByEduClassIds(List<Long> eduClassIds) {
		StringBuilder sql = new StringBuilder("select ccg.* from " + CcCourseGradecompose.dao.tableName + " ccg ");
		sql.append("inner join " + CcTeacherCourse.dao.tableName + " ctc on ctc.id = ccg.teacher_course_id and ctc.is_del = ? ");
		sql.append("inner join " + CcEduclass.dao.tableName + " ce on ce.teacher_course_id = ctc.id and ce.is_del = ? and ce.id in (" + CollectionKit.convert(eduClassIds, ",") + ") ");
		sql.append("where ccg.is_del = ? ");
		return find(sql.toString(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
	}

}
