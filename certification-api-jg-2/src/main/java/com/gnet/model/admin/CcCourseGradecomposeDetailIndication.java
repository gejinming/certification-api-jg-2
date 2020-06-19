package com.gnet.model.admin;

import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;

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
	
}
