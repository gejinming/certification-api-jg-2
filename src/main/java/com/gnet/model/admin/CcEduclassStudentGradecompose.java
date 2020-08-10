package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;

/**
 * @type model
 * @description 教学班学生成绩组成情况表操作，包括对数据的增删改查与列表
 * @table cc_educlass_student_gradecompose
 * @author SY
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_educlass_student_gradecompose")
public class CcEduclassStudentGradecompose extends DbModel<CcEduclassStudentGradecompose> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcEduclassStudentGradecompose dao = new CcEduclassStudentGradecompose();
}
