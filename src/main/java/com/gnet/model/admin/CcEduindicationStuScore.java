package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;

import java.util.ArrayList;
import java.util.List;

/**
 * @type model
 * @description 教学班下课程目标成绩组成学生分数
 * @table cc_eduindication_stu_score
 * @author xzl
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_eduindication_stu_score")
public class CcEduindicationStuScore extends DbModel<CcEduindicationStuScore> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcEduindicationStuScore dao = new CcEduindicationStuScore();
	
	/**
	 * 通过教学班编号，获取开课课程成绩组成元素与课程目标关联表 和 教学班下课程目标成绩组成学生分数
	 * @param eduClassId
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年11月28日 下午4:06:45 
	 */
	public List<CcEduindicationStuScore> findAllByEduclassId(Long eduClassId) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select cess.*, ci.id indication_id,"
				+ " ci.sort sort, ci.content content, cic.weight indication_weight,"
				+ " cg.name gradecompose_name, ccgci.weight, ccgci.max_score from " + tableName + " cess ");
		sql.append("inner join cc_course_gradecompose_indication ccgci on ccgci.id = cess.gradecompose_indication_id and ccgci.is_del = ? ");
		sql.append("inner join cc_course_gradecompose ccg on ccg.id = ccgci.course_gradecompose_id and ccg.is_del = ? ");
		sql.append("inner join cc_gradecompose cg on cg.id = ccg.gradecompose_id and cg.is_del = ? ");
		sql.append("inner join cc_educlass ce on ce.teacher_course_id = ccg.teacher_course_id and ce.id = ? ");
		sql.append("inner join cc_indication ci on ci.id = ccgci.indication_id ");
		sql.append("inner join cc_indication_course cic on cic.indication_id = ci.id ");
		sql.append("where cess.educlass_id = ?  ");
		params.add(DEL_NO);
		params.add(DEL_NO);
		params.add(DEL_NO);
		params.add(eduClassId);
		params.add(eduClassId);
		sql.append("order by ci.sort asc, ccg.sort desc, ccg.create_date asc, ccg.id asc");
		return find(sql.toString(), params.toArray());
	}

	/**
	 * @description: 通过教学班ID集合和开课成绩组成ID查询数据
	 * @param eduClassIds
	 * @param courseGradecomposeIds
	 * @return
	 */
	public List<CcEduindicationStuScore> findByEduclassIdsAndCourseGradecomposeIds(List<Long> eduClassIds, List<Long> courseGradecomposeIds) {
		StringBuilder sql = new StringBuilder("select cess.* from " + tableName + " cess ");
		sql.append("inner join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.id = cess.gradecompose_indication_id and ccgi.course_gradecompose_id in (" + CollectionKit.convert(courseGradecomposeIds, ",") + ") and ccgi.is_del = ? ");
		sql.append("inner join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.id = ccgi.course_gradecompose_id and ccg.is_del = ? ");
		sql.append("inner join " + CcEduclass.dao.tableName + " ce on ce.id = cess.educlass_id and cess.educlass_id in (" + CollectionKit.convert(eduClassIds, ",") + ") and ce.is_del = ? ");
		return find(sql.toString(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
	}
	/**
	 * @param eduClassIds
	 * @return java.util.List<com.gnet.model.admin.CcEduindicationStuScore>
	 * @author Gejm
	 * @description: 根据一门课程下所有的教学班id统计 总分 和获取权重、满分（根据指标点id，成绩组成id）
	 * @date 2020/6/16 14:24
	 */
	public List<CcEduindicationStuScore> findByEduclassIdsSumList(List<Long> eduClassIds) {
		StringBuilder sql = new StringBuilder("select sum(cesc.total_score) totalGrade,ccgi.max_score maxScore,ccgi.weight,ci.id indicationId," +
				"ci.content,cg.id gradecomposeId,cesc.gradecompose_indication_id gradecomposeIndicationId" +
				" from " + CcEduindicationStuScore.dao.tableName + " cesc ");
		//开课课程成绩组成元素与课程目标关联表取权重、满分
		sql.append(" left join cc_course_gradecompose_indication ccgi on cesc.gradecompose_indication_id=ccgi.id");
		//课程目标表
		sql.append(" left join cc_indication ci on ci.id=ccgi.indication_id ");
		//开课课程组成元素表
		sql.append(" left join cc_course_gradecompose ccg on ccg.id =ccgi.course_gradecompose_id");
		//成绩组成元素表
		sql.append(" left join cc_gradecompose cg on cg.id=ccg.gradecompose_id");
		sql.append(" where cesc.educlass_id in (" + CollectionKit.convert(eduClassIds, ",") + ")");
		sql.append(" group by ccgi.indication_id,ccg.gradecompose_id,ccgi.max_score,ccgi.weight  ");
		sql.append(" order by educlass_id");
		return find(sql.toString());
	}
	/**
	 * 教学班下已存在的学生分数
	 * @param eduClassId
	 * @return
	 */
	public List<CcEduindicationStuScore> findByEduclassId(Long eduClassId) {
		StringBuilder sql = new StringBuilder("select ces.*, ccgi.indication_id, ccgi.course_gradecompose_id from " +tableName + " ces ");
		sql.append("inner join " + CcCourseGradecomposeIndication.dao.tableName + " ccgi on ccgi.id = ces.gradecompose_indication_id and ccgi.is_del = ? ");
		sql.append("where ces.educlass_id = ? ");
		return find(sql.toString(), DEL_NO, eduClassId);
	}

	/**
	 * 计算指定教师开课下面的 总的 指标点成绩组成 总分
	 * @param teacherCourseIdList
	 * @param indicatorPointId
	 * 			指标点（可为空）
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年12月21日 上午11:45:04 
	 */
	public List<CcEduindicationStuScore> caculateSumInSameGradecomposeAndIndication(List<Long> teacherCourseIdList, Long indicatorPointId) {
		List<Object> params = new ArrayList<>();
		StringBuffer sql = new StringBuffer("select cic.indication_id indicatorPointId, ccg.gradecompose_id gradecomposeId "
				+ ",ccg.teacher_course_id teacherCourseId, cess.id eduindicationStuScoreId, ci.id indicationId "
				+ ",cess.total_score totalScore, cess.except_total_score exceptTotalScore from " + tableName + " cess ");
		sql.append("inner join cc_course_gradecompose_indication ccgi ON ccgi.id = cess.gradecompose_indication_id and ccgi.is_del = ? ");
		params.add(DEL_NO);
		sql.append("inner join " + CcCourseGradecompose.dao.tableName + " ccg on ccg.id = ccgi.course_gradecompose_id and ccg.is_del = ? ");
		params.add(DEL_NO);
		sql.append("inner join " + CcIndication.dao.tableName + " ci on ci.id = ccgi.indication_id and ci.is_del = ? ");
		params.add(DEL_NO);
		sql.append("inner join " + CcCourseTargetIndication.dao.tableName + " ccti on ci.id = ccti.indication_id ");
		sql.append("inner join " + CcIndicationCourse.dao.tableName + " cic on cic.id = ccti.indication_course_id and cic.is_del = ? ");
		params.add(DEL_NO);
		sql.append("where ccg.teacher_course_id in (" + CollectionKit.convert(teacherCourseIdList, ",") + ") ");
		if(indicatorPointId != null) {
			sql.append("and cic.indication_id = ? ");
			params.add(indicatorPointId);	
		}
		
		sql.append("group by cic.indication_id, ccg.gradecompose_id, ccg.teacher_course_id, cess.id ");
		sql.append("order by cic.indication_id asc ");
		return find(sql.toString(), params.toArray());
	}
}
