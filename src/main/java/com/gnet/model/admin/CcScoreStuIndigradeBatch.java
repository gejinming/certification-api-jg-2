package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


/**
 * @type model
 * @description 多批次指标点成绩直接录入表
 * @table cc_score_stu_indigrade_batch
 * @author GJM
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_score_stu_indigrade_batch")
public class CcScoreStuIndigradeBatch extends DbModel<CcScoreStuIndigradeBatch> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcScoreStuIndigradeBatch dao = new CcScoreStuIndigradeBatch();
	/*
	 * @param gradecomposeIndicationId
		 * @param batchId
	 * @return java.util.List<com.gnet.model.admin.CcScoreStuIndigradeBatch>
	 * @author Gejm
	 * @description: 根据开课课程成绩组成元素与课程目标关联id与批次id查询成绩
	 * @date 2020/8/11 10:30
	 */
	public List<CcScoreStuIndigradeBatch> findBatchScoreList(Long gradecomposeIndicationId,Long batchId){
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select * from cc_score_stu_indigrade_batch where is_del =0 ");
		if (gradecomposeIndicationId != null){
			sql.append("and gradecompose_indication_id=? ");
			params.add(gradecomposeIndicationId);
		}
		if (batchId != null){
			sql.append("and batch_id=? ");
			params.add(batchId);
		}

		return  find(sql.toString(),params.toArray());

	}
	/**
	 * 获得教学班下某个批次课程目标的成绩
	 * @param educlassId
	 *            教学班编号
	 * @param courseGradecomposeId
	 *            开课课程成绩组成元素编号
	 * @return
	 */
	public List<CcScoreStuIndigradeBatch> findDetailByClassIdAndCourseGradecomposeId(Long educlassId, Long courseGradecomposeId,Long batchId) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select cssig.*,cgbi.id gradecomposeIndicationId, cs.student_no student_no, cs.name student_name from " + tableName + " cssig ");
		sql.append("left join cc_course_gradecompose_batch_indication cgbi on  cssig.gradecompose_indication_id=cgbi.id and cgbi.is_del=0 ");
		sql.append("left join cc_course_gradecompose_batch cgb on cgb.id= cssig.batch_id and cgb.is_del=0 ");
		//sql.append("left join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.id = cssig.gradecompose_indication_id ");
		sql.append("left join " + CcEduclassStudent.dao.tableName +  " ces on ces.student_id = cssig.student_id ");
		sql.append("left join " + CcStudent.dao.tableName + " cs on cs.id = cssig.student_id ");
		sql.append("where ces.class_id = ? and cssig.batch_id=? ");
		param.add(educlassId);
		param.add(batchId);
		sql.append("and cgb.course_gradecompose_id = ? ");
		param.add(courseGradecomposeId);
		sql.append("and ces.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and cssig.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and cs.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("order by cs.student_no asc");
		return find(sql.toString(), param.toArray());
	}
	/**
	 * 通过列表删除（根据studenti_id和gradecompose_indication_id）
	 * @param scoreStuIndigradeAddList
	 * @return
	 * @author SY
	 * @version 创建时间：2017年10月18日 上午11:42:15
	 */
	public boolean deleteByModel(List<CcScoreStuIndigradeBatch> scoreStuIndigradeAddList) {
		if(scoreStuIndigradeAddList == null || scoreStuIndigradeAddList.size() == 0) {
			return true;
		}
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("update " + tableName + " cssi ");
		sql.append("set cssi.is_del = ? where 1 = 1 and (");
		params.add(Boolean.TRUE);
		for(int i = 0; i < scoreStuIndigradeAddList.size(); i++) {
			CcScoreStuIndigradeBatch scoreStuIndigrade = scoreStuIndigradeAddList.get(i);
			if(i != 0) {
				sql.append("or ");
			}
			sql.append("(cssi.gradecompose_indication_id = ? ");
			params.add(scoreStuIndigrade.getLong("gradecompose_indication_id"));
			sql.append("and cssi.batch_id=? ");
			params.add(scoreStuIndigrade.getLong("batch_id"));
			sql.append("and cssi.student_id = ? )");
			params.add(scoreStuIndigrade.getLong("student_id"));
		}
		sql.append(")");
		return Db.update(sql.toString(), params.toArray()) >= 0;
	}
	/*
	 * @param courseGradeComposeId
	 * @return java.util.List<com.gnet.model.admin.CcScoreStuIndigradeBatch>
	 * @author Gejm
	 * @description: 根据成绩组成元素，学生编号统计成绩
	 * @date 2020/8/11 16:20
	 */
	public List<CcScoreStuIndigradeBatch> sumScoreStuIndigrade(Long courseGradeComposeId){
		List<Object> params = Lists.newArrayList();
		/*StringBuilder sql = new StringBuilder("select student_id,gradecompose_indication_id,sum(grade) grade from cc_score_stu_indigrade_batch ");
		sql.append("where  batch_id in (" + CollectionKit.convert(batchIds, ",") + ")  and is_del=0 ");
		sql.append("group by student_id,gradecompose_indication_id ");*/
		StringBuilder sql = new StringBuilder("select student_id,sum(grade) grade,d.id gradecompose_indication_id,c.indication_id from cc_score_stu_indigrade_batch a ");
		sql.append("inner join  cc_course_gradecompose_batch b on b.id=a.batch_id and b.is_del=0 ");
		sql.append("inner join cc_course_gradecompose_batch_indication c on a.gradecompose_indication_id=c.id and c.is_del=0 ");
		sql.append("inner join  cc_course_gradecompose_indication d on d.indication_id=c.indication_id and d.course_gradecompose_id=b.course_gradecompose_id ");
		sql.append("where a.is_del=0 and b.course_gradecompose_id=? ");
		params.add(courseGradeComposeId);
		sql.append("group by student_id,d.id ");
		return  find(sql.toString(),params.toArray());

	}
}
