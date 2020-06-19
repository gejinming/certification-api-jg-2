package com.gnet.excel;

import com.gnet.plugin.poi.ExcelField;
import com.gnet.plugin.poi.ExcelModel;

/**
 * 教学班导入学生模板
 * 
 * @author xzl
 * @Date 2017年10月8日
 */
@ExcelModel(name = "studentEduClassExcel", rowCount = "2")
public class StudentEduClassExcel {

	@ExcelField(fieldName = "courseMajor", index = "0", title = "开课专业", type = ExcelField.CELL_TYPE_STRING)
	String courseMajor;

	@ExcelField(fieldName = "grade", index = "1", title = "开课年级", type = ExcelField.CELL_TYPE_STRING)
	String grade;

	@ExcelField(fieldName = "teacherNo", index = "2", title = "教师职工号", type = ExcelField.CELL_TYPE_STRING)
	String teacherCode;
	
	@ExcelField(fieldName = "courseCode", index = "3", title = "课程代码", type = ExcelField.CELL_TYPE_STRING)
	String courseCode;
	
	@ExcelField(fieldName = "term", index = "4", title = "开课学期", type = ExcelField.CELL_TYPE_STRING)
	String term;
	
	@ExcelField(fieldName = "eduClassName", index = "5", title = "教学班", type = ExcelField.CELL_TYPE_STRING)
	String eduClassName;

	@ExcelField(fieldName = "type", index = "6", title = "达成度计算类型", type = ExcelField.CELL_TYPE_STRING)
	String type;

	@ExcelField(fieldName = "studentNo", index = "7", title = "学号", type = ExcelField.CELL_TYPE_STRING)
	String studentNo;

	@ExcelField(fieldName = "studentName", index = "8", title = "姓名", type = ExcelField.CELL_TYPE_STRING)
	String studentName;

	@ExcelField(fieldName = "major", index = "9", title = "专业", type = ExcelField.CELL_TYPE_STRING)
	String major;

	@ExcelField(fieldName = "classes", index = "10", title = "行政班", type = ExcelField.CELL_TYPE_STRING)
	String classes;

}
