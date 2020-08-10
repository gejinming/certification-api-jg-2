package com.gnet.excel;

import com.gnet.plugin.poi.ExcelField;
import com.gnet.plugin.poi.ExcelModel;

/**
 * 实践课程excel类
 * 
 * @author SY
 * @Date 2018年1月12日
 */
@ExcelModel(name = "practiceCourseCourseExcel", rowCount = "2")
public class PracticeCourseCourseExcel {
	
	@ExcelField(fieldName = "directionName", index = "0", title = "专业方向", type = ExcelField.CELL_TYPE_STRING)
	String directionName;
	
	@ExcelField(fieldName = "moduleName", index = "1", title = "所属模块", type = ExcelField.CELL_TYPE_STRING)
	String moduleName;
	
	@ExcelField(fieldName = "code", index = "2", title = "课程代码", type = ExcelField.CELL_TYPE_STRING)
	String code;
	
	@ExcelField(fieldName = "name", index = "3", title = "课程中文名", type = ExcelField.CELL_TYPE_STRING)
	String name;
	
	@ExcelField(fieldName = "englishName", index = "4", title = "课程英文名", type = ExcelField.CELL_TYPE_STRING)
	String englishName;
	
	@ExcelField(fieldName = "credit", index = "5", title = "学分", type = ExcelField.CELL_TYPE_STRING)
	String credit;
	
	@ExcelField(fieldName = "weekHour", index = "6", title = "周或者周学时", type = ExcelField.CELL_TYPE_STRING)
	String weekHour;
	
	@ExcelField(fieldName = "allHours", index = "7", title = "周或总学时", type = ExcelField.CELL_TYPE_STRING)
	String allHours;
	
	@ExcelField(fieldName = "planTermClassNames", index = "8", title = "开课学期", type = ExcelField.CELL_TYPE_STRING)
	String planTermClassNames;
	
}
