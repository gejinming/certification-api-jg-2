package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @type model
 * @description 开课课程成绩组成元素课程目标关联的分数范围备注
 * @table cc_gradecompose_indication_score_remark
 * @author xzl
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_gradecompose_indication_score_remark")
public class CcGradecomposeIndicationScoreRemark extends DbModel<CcGradecomposeIndicationScoreRemark> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcGradecomposeIndicationScoreRemark dao = new CcGradecomposeIndicationScoreRemark();


	/**************************** 非建工专业代码 start ****************************/
	/**
	 * 开课课程下分数范围备注
	 * @param teacherCourseId
	 * @return
	 */
	public List<CcGradecomposeIndicationScoreRemark> findByTeacherCourseId(Long teacherCourseId) {
		StringBuilder sql = new StringBuilder("select ccg.id courseGradecomposeId, cgisr.*, ccgi.indication_id indicationId from " + tableName + " cgisr ");
		sql.append("inner join cc_course_gradecompose_indication ccgi on ccgi.id = cgisr.gradecompose_indication_id and ccgi.is_del = ? ");
		sql.append("inner join cc_course_gradecompose ccg on ccg.id = ccgi.course_gradecompose_id and ccg.teacher_course_id = ? and ccg.is_del = ? ");
		return find(sql.toString(), DEL_NO, teacherCourseId, DEL_NO);
	}
	/**************************** 非建工专业代码 end ****************************/
}
