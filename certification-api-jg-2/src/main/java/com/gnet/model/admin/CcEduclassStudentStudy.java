package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;

/**
 * @type model
 * @description 学生教学班学习情况表操作，包括对数据的增删改查与列表
 * @table cc_educlass_student_study
 * @author SY
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_educlass_student_study")
public class CcEduclassStudentStudy extends DbModel<CcEduclassStudentStudy> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcEduclassStudentStudy dao = new CcEduclassStudentStudy();
	
	/**
	 * 备注：无
	 */
	public static Integer REMARK_NOTHING = 0; 
	
	/**
	 * 备注：缺考
	 */
	public static Integer REMARK_EXAM_MISS = 1; 
	
	/**
	 * 备注：补考
	 */
	public static Integer REMARK_EXAM_RESIT = 2; 
	
	/**
	 * 重修标记：否
	 */
	public static Boolean RETAKE_FALSE = Boolean.FALSE; 
	
	/**
	 * 重修标记：是
	 */
	public static Boolean RETAKE_TRUE = Boolean.TRUE;
}
