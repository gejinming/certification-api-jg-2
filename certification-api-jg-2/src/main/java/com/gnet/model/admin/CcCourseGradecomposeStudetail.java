package com.gnet.model.admin;

import java.util.List;

import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;
import org.apache.commons.lang3.StringUtils;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

@TableBind(tableName = "cc_course_gradecompose_studetail")
public class CcCourseGradecomposeStudetail extends DbModel<CcCourseGradecomposeStudetail> {

	private static final long serialVersionUID = 5144422528895697100L;
	public final static CcCourseGradecomposeStudetail dao = new CcCourseGradecomposeStudetail();
	
	/**
	 * 根据学生编号和课程成绩组成编号查找某一个学生的某一份试卷的成绩
	 * 
	 * @param studentId
	 * 			学生编号
	 * @param courseGradecomposeId
	 * 			成绩组成编号
	 * @return
	 */
	public List<CcCourseGradecomposeStudetail> findDetails(Long studentId, Long courseGradecomposeId) {
		
		StringBuilder sql = new StringBuilder("select ccgcs.*, ccgcd.name detail_name, ccgcd.score datail_score, ccgcd.detail datailContent, ccgcd.remark detail_remark from " + tableName + " ccgcs ");
		sql.append("left join " + CcCourseGradeComposeDetail.dao.tableName + " ccgcd on ccgcd.id = ccgcs.detail_id ");
		sql.append("where ccgcs.student_id = ? and course_gradecompose_id = ? ");
		sql.append("and ccgcs.is_del = ? and ccgcd.is_del = ? ");
		
		List<Object> params = Lists.newArrayList();
		params.add(studentId);
		params.add(courseGradecomposeId);
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		
		return find(sql.toString(), params.toArray());
	}

	/**
	 * 统计一个班级所有学生一个指标点的总分
	 * 
	 * @return
	 */
	public List<CcCourseGradecomposeStudetail> sumOfScore (Long indicationId, Long classId){
		
		StringBuilder exceptSql = new StringBuilder("select sum(cgs.score) sum,cgi.id gradecompose_indication_id, cs.id student_id from " + CcCourseGradecomposeStudetail.dao.tableName + " cgs ");
		
		exceptSql.append("left join " + CcCourseGradeComposeDetail.dao.tableName + " cgd on cgs.detail_id = cgd.id ");
		exceptSql.append("left join " + CcCourseGradecompose.dao.tableName + " cg on cgd.course_gradecompose_id = cg.id ");
		exceptSql.append("left join " + CcCourseGradecomposeIndication.dao.tableName + " cgi on cgi.course_gradecompose_id = cg.id ");
		exceptSql.append("left join " + CcStudent.dao.tableName + " cs on cs.id = cgs.student_id ");
		exceptSql.append("left join " + CcEduclassStudent.dao.tableName + " ces on ces.student_id = cs.id " );
		
		exceptSql.append("where cgd.is_del = ? ");
		List<Object> params = Lists.newArrayList();
		params.add(Boolean.FALSE);
		
		exceptSql.append("and cgs.is_del = ? ");
		params.add(Boolean.FALSE);
		
		exceptSql.append("and cgi.indication_id = ? ");
		params.add(indicationId);
		
		exceptSql.append("and ces.class_id = ? ");
		params.add(classId);
		
		return find(exceptSql.toString(), params.toArray());
	}

	/**
	 * 返回教学班学生在各个指标点下的成绩组成的成绩
	 * @param courseGradecomposeId
	 * @param educlassId
	 * @return
	 */
	public List<CcCourseGradecomposeStudetail> findByEduclassIdAndCourseGradecomposeId(Long courseGradecomposeId, Long educlassId) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select sum(ccgs.score) score, ccgs.student_id studentId, ccgi.id courseGradecomposeIndicationId from " + tableName + " ccgs ");
		sql.append("left join " + CcCourseGradeComposeDetail.dao.tableName + " ccgd on ccgd.id = ccgs.detail_id ");
		sql.append("left join " + CcCourseGradecomposeDetailIndication.dao.tableName + " ccgdi on ccgdi.course_gradecompose_detail_id = ccgs.detail_id ");
		sql.append("left join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on (ccgi.course_gradecompose_id = ccgd.course_gradecompose_id and ccgdi.indication_id = ccgi.indication_id) ");
		sql.append("left join " + CcEduclassStudent.dao.tableName + " ces on (ces.student_id = ccgs.student_id and ces.class_id = ?) ");
		param.add(educlassId);
		sql.append("where ccgd.course_gradecompose_id = ? ");
		param.add(courseGradecomposeId);
		sql.append("and ccgs.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ccgd.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ccgdi.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ces.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("group by ccgs.student_id, ccgdi.indication_id ");
		return find(sql.toString(), param.toArray());
	}

	/**
	 * 返回学生在特定指标点下的成绩组成的成绩
	 * @param courseGradecomposeId
	 * @param indaicationIds
	 * @return
	 */
	public List<CcCourseGradecomposeStudetail> findByIndicationIdsAndCourseGradecomposeId(Long courseGradecomposeId) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select sum(ccgs.score) score, ccgs.student_id studentId, ccgi.id courseGradecomposeIndicationId, ccgi.indication_id from " + tableName + " ccgs ");
		sql.append("inner join " + CcCourseGradeComposeDetail.dao.tableName + " ccgd on ccgd.id = ccgs.detail_id ");
		sql.append("inner join " + CcCourseGradecomposeDetailIndication.dao.tableName + " ccgdi on ccgdi.course_gradecompose_detail_id = ccgs.detail_id ");
		sql.append("inner join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on (ccgi.course_gradecompose_id = ccgd.course_gradecompose_id and ccgdi.indication_id = ccgi.indication_id ) ");
		sql.append("where ccgd.course_gradecompose_id = ? ");
		param.add(courseGradecomposeId);
		sql.append("and ccgs.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ccgd.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ccgdi.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and ccgi.is_del = ? ");
		param.add(DEL_NO);
		sql.append("group by ccgs.student_id, ccgi.indication_id ");
		sql.append("order by ccgs.student_id, ccgi.indication_id ");
		return find(sql.toString(), param.toArray());
	}


	/**
	 * 根据学生题目分数得出学生成绩
	 *
	 * @param studentIds
	 * @param detailIds
	 * @param courseGradecomposeIndicationIds
	 * @return
	 */
	public List<CcCourseGradecomposeStudetail> findByStudentIdsAndDetailIds(Long[] studentIds, Long[] detailIds, Long[] courseGradecomposeIndicationIds) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select ccgs.score, ccgs.student_id studentId, ccgi.id courseGradecomposeIndicationId, ccgi.indication_id, ccgs.detail_id from " + tableName + " ccgs ");
		sql.append("inner join cc_course_gradecompose_detail ccgd on ccgd.id = ccgs.detail_id and ccgd.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_course_gradecompose_detail_indication ccgdi on ccgdi.course_gradecompose_detail_id = ccgd.id and ccgdi.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_course_gradecompose_detail_indication ccgdi_target on ccgdi_target.indication_id = ccgdi.indication_id and ccgdi_target.course_gradecompose_detail_id in ( " + CollectionKit.convert(detailIds, ",") + " ) and ccgdi_target.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_course_gradecompose_indication ccgi on ccgi.course_gradecompose_id = ccgd.course_gradecompose_id and ccgdi.indication_id = ccgi.indication_id and ccgi.is_del = ? ");
		if(courseGradecomposeIndicationIds != null && courseGradecomposeIndicationIds.length > 0){
			sql.append("and ccgi.id in ( " + CollectionKit.convert(courseGradecomposeIndicationIds, ",") +  ") ");
		}
		param.add(DEL_NO);
		sql.append("where ccgs.is_del = ? ");
		param.add(DEL_NO);
		sql.append("and ccgs.student_id in (" + CollectionKit.convert(studentIds, ",") + ") ");
		sql.append("group by ccgs.student_id, ccgi.id, ccgs.detail_id ");
		sql.append("order by ccgs.student_id, ccgi.id ");
		return find(sql.toString(), param.toArray());
	}

	/**
	 * 根据学生题目分数得出学生成绩
	 *
	 * @param studentIds
	 * @param detailIds
	 * @param courseGradecomposeIndicationIds
	 * @return
	 */
	public List<CcCourseGradecomposeStudetail> findByStudentIdAndDetailId(Long[] studentIds, Long[] detailIds, Long[] courseGradecomposeIndicationIds) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select sum(ccgs.score) score, ccgs.student_id studentId, ccgi.id courseGradecomposeIndicationId, ccgi.indication_id from " + tableName + " ccgs ");
		sql.append("inner join cc_course_gradecompose_detail ccgd on ccgd.id = ccgs.detail_id and ccgd.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_course_gradecompose_detail_indication ccgdi on ccgdi.course_gradecompose_detail_id = ccgd.id and ccgdi.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_course_gradecompose_detail_indication ccgdi_target on ccgdi_target.indication_id = ccgdi.indication_id and ccgdi_target.course_gradecompose_detail_id in ( " + CollectionKit.convert(detailIds, ",") + " ) and ccgdi_target.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join cc_course_gradecompose_indication ccgi on ccgi.course_gradecompose_id = ccgd.course_gradecompose_id and ccgdi.indication_id = ccgi.indication_id and ccgi.is_del = ? ");
		if(courseGradecomposeIndicationIds != null && courseGradecomposeIndicationIds.length > 0){
			sql.append("and ccgi.id in ( " + CollectionKit.convert(courseGradecomposeIndicationIds, ",") +  ") ");
		}
		param.add(DEL_NO);
		sql.append("where ccgs.is_del = ? ");
		param.add(DEL_NO);
		sql.append("and ccgs.student_id in (" + CollectionKit.convert(studentIds, ",") + ") ");
		sql.append("group by ccgs.student_id, ccgi.id, ccgi.indication_id ");
		return find(sql.toString(), param.toArray());
	}

	/**
	 * 判断学生该题目是否已经录过分数了
	 * @param studentId
	 * @param detailId
	 * @return
	 */
	public boolean isExistStudentGrade(Long studentId, Long detailId) {
		String sql = "select count(1) from " + tableName + " where student_id = ? and detail_id = ? and is_del = ? ";
		return Db.queryLong(sql, studentId, detailId, DEL_NO) > 0;
	}

	/**
	 * 某道题目学生最大得分
	 * @param id
	 * @return
	 */
	public CcCourseGradecomposeStudetail findMaxScoreStudent(Long id) {
		String sql = "select * from " + tableName + " where detail_id = ? and is_del = ? order by score desc ";
		return findFirst(sql, id, DEL_NO);
	}
	
	/**
	 * 某道题目所有输入成绩的学生列表
	 * @param pageable
	 * @param id
	 * @return
	 */
	public Page<CcCourseGradecomposeStudetail> page(Pageable pageable, Long id) {
		List<Object> param = Lists.newArrayList();
		StringBuilder headSql = new StringBuilder("select ccgs.score, ccgs.student_id, cs.name, cs.student_no, ccgs.id, ccgs.detail_id ");
		StringBuilder sql  = new StringBuilder("from " + tableName + " ccgs ");
		sql.append("inner join " + CcStudent.dao.tableName + " cs on cs.id = ccgs.student_id and cs.is_del = ? ");
		sql.append("where ccgs.detail_id = ? and ccgs.is_del = ? ");
		param.add(DEL_NO);
		param.add(id);
		param.add(DEL_NO);
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			sql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}else{
			sql.append("order by ccgs.score desc ");
		}
		return CcCourseGradecomposeStudetail.dao.paginate(pageable, headSql.toString(), sql.toString(), param.toArray());
	}


	/**
	 * 教学班下在某个成绩组成下已存在的学生题目
	 * @param eduClassId
	 * @param courseGradeComposeId
	 * @return
	 */
	public List<CcCourseGradecomposeStudetail> findStudetail(Long eduClassId, Long courseGradeComposeId) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select ccgs.id, ccgc.name, ccgc.score, cs.student_no, ccgs.student_id, ccgs.detail_id from " + tableName + " ccgs ");
		sql.append("inner join " + CcEduclassStudent.dao.tableName + " ces on ces.student_id = ccgs.student_id and ces.class_id = ? and ces.is_del = ? ");
		param.add(eduClassId);
		param.add(DEL_NO);
		sql.append("inner join " + CcStudent.dao.tableName + " cs on cs.id = ces.student_id and cs.is_del = ? ");
		param.add(DEL_NO);
		sql.append("inner join " + CcCourseGradeComposeDetail.dao.tableName + " ccgc on ccgc.id = ccgs.detail_id and ccgc.course_gradecompose_id = ? and ccgc.is_del = ? ");
        param.add(courseGradeComposeId);
        param.add(DEL_NO);
        sql.append("where ccgs.is_del = ? ");
        param.add(DEL_NO);
		return  find(sql.toString(), param.toArray());
	}
}
