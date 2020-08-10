package com.gnet.excel;

import com.gnet.plugin.poi.ExcelField;
import com.gnet.plugin.poi.ExcelModel;

/**
 * 理论课程excel类
 * 
 * @author SY
 * @Date 2018年1月12日
 */
@ExcelModel(name = "theoryCourseExcel", rowCount = "2")
public class TheoryCourseExcel {
	
	@ExcelField(fieldName = "hierarchyName", index = "0", title = "课程层次", type = ExcelField.CELL_TYPE_STRING)
	String hierarchyName;

	@ExcelField(fieldName = "seconderyHierarchyName", index = "1", title = "次要课程层次", type = ExcelField.CELL_TYPE_STRING)
	String seconderyHierarchyName;

	@ExcelField(fieldName = "propertyName", index = "2", title = "课程性质", type = ExcelField.CELL_TYPE_STRING)
	String propertyName;

	@ExcelField(fieldName = "propertySecondaryName", index = "3", title = "次要课程性质", type = ExcelField.CELL_TYPE_STRING)
	String propertySecondaryName;
	
	@ExcelField(fieldName = "code", index = "4", title = "课程代码", type = ExcelField.CELL_TYPE_STRING)
	String code;
	
	@ExcelField(fieldName = "name", index = "5", title = "课程中文名", type = ExcelField.CELL_TYPE_STRING)
	String name;
	
	@ExcelField(fieldName = "englishName", index = "6", title = "课程英文名", type = ExcelField.CELL_TYPE_STRING)
	String englishName;
	
	@ExcelField(fieldName = "credit", index = "7", title = "学分", type = ExcelField.CELL_TYPE_STRING)
	String credit;
	
	@ExcelField(fieldName = "allHours", index = "8", title = "总学时", type = ExcelField.CELL_TYPE_STRING)
	String allHours;
	
	@ExcelField(fieldName = "theoryHours", index = "9", title = "理论学时", type = ExcelField.CELL_TYPE_STRING)
	String theoryHours;
	
	@ExcelField(fieldName = "experimentHours", index = "10", title = "实验学时", type = ExcelField.CELL_TYPE_STRING)
	String experimentHours;
	
	@ExcelField(fieldName = "practiceHours", index = "11", title = "实践学时", type = ExcelField.CELL_TYPE_STRING)
	String practiceHours;
	
	@ExcelField(fieldName = "operateComputerHours", index = "12", title = "上机学时", type = ExcelField.CELL_TYPE_STRING)
	String operateComputerHours;
	
	@ExcelField(fieldName = "exercisesHours", index = "13", title = "习题学时", type = ExcelField.CELL_TYPE_STRING)
	String exercisesHours;
	
	@ExcelField(fieldName = "dicussHours", index = "14", title = "研讨学时", type = ExcelField.CELL_TYPE_STRING)
	String dicussHours;
	
	@ExcelField(fieldName = "extracurricularHours", index = "15", title = "课外学时", type = ExcelField.CELL_TYPE_STRING)
	String extracurricularHours;
	
	@ExcelField(fieldName = "weekHour", index = "16", title = "周学时", type = ExcelField.CELL_TYPE_STRING)
	String weekHour;
	
	@ExcelField(fieldName = "planTermClassNames", index = "17", title = "开课学期", type = ExcelField.CELL_TYPE_STRING)
	String planTermClassNames;
	
	@ExcelField(fieldName = "planTermExamNames", index = "18", title = "考试学期", type = ExcelField.CELL_TYPE_STRING)
	String planTermExamNames;
	
}
