package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;

import java.util.ArrayList;
import java.util.List;

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


	public List<CcEduclassStudentStudy> findCourseGradecomposeStud(Long courseGradecomposeId, Long classId, Long batchId){
		ArrayList<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select * from " + tableName + " where course_gradecompose_id=? and class_id=? ");
		params.add(courseGradecomposeId);
		params.add(classId);
		if (batchId != null){
			sql.append("and batch_id = ?");
			params.add(batchId);
		}

		return find(sql.toString(),params.toArray());
	}
}
