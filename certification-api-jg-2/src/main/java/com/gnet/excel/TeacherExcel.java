package com.gnet.excel;

import com.gnet.plugin.poi.ExcelField;
import com.gnet.plugin.poi.ExcelModel;

/**
 * 教师信息excel类
 * 
 * @author SY
 * @Date 2016年7月12日14:53:25
 */
@ExcelModel(name = "teacherExcel", rowCount = "2")
public class TeacherExcel {
	
	@ExcelField(fieldName = "code", index = "0", title = "工号", type = ExcelField.CELL_TYPE_STRING)
	String code;

	@ExcelField(fieldName = "name", index = "1", title = "姓名", type = ExcelField.CELL_TYPE_STRING)
	String name;

	@ExcelField(fieldName = "sex", index = "2", title = "性别", type = ExcelField.CELL_TYPE_STRING, convert = { "0:男", "1:女"})
	String sex;

	@ExcelField(fieldName = "isEnabled", index = "3", title = "是否可用", type = ExcelField.CELL_TYPE_STRING, convert = { "0:不可用", "1:可用"})
	String is_enabled;
	
	@ExcelField(fieldName = "isLeave", index = "4", title = "是否离职", type = ExcelField.CELL_TYPE_STRING, convert = { "0:在职", "1:离职"})
	String is_leave;
	
	@ExcelField(fieldName = "departmentName", index = "5", title = "部门名称", type = ExcelField.CELL_TYPE_STRING)
	String department;
	
	
}
