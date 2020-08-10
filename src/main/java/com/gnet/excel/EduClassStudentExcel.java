package com.gnet.excel;

import com.gnet.plugin.poi.ExcelField;
import com.gnet.plugin.poi.ExcelModel;

/**
 * 教学班导入学生模板
 * 
 * @author wct
 * @Date 2016年7月6日
 */
@ExcelModel(name = "eduClassStudentExcel", rowCount = "2")
public class EduClassStudentExcel {
	
	@ExcelField(fieldName = "student_no", index = "0", title = "学号", type = ExcelField.CELL_TYPE_STRING)
	String studentNo;
	
	@ExcelField(fieldName = "name", index = "1", title = "学生姓名", type = ExcelField.CELL_TYPE_STRING)
	String name;
	
	@ExcelField(fieldName = "class_name", index = "2", title = "行政班名称", type = ExcelField.CELL_TYPE_STRING)
	String className;
	
	@ExcelField(fieldName = "edu_class_name", index = "3", title = "教学班名称", type = ExcelField.CELL_TYPE_STRING)
	String eduClassName;
}
