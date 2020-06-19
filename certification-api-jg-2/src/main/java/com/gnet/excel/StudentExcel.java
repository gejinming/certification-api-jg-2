package com.gnet.excel;

import com.gnet.plugin.poi.ExcelField;
import com.gnet.plugin.poi.ExcelModel;

/**
 * 学生信息excel类
 * 
 * @author wct
 * @Date 2016年7月1日
 */
@ExcelModel(name = "studentExcel", rowCount = "2")
public class StudentExcel {
	
	@ExcelField(fieldName = "studentNo", index = "0", title = "学号", type = ExcelField.CELL_TYPE_STRING)
	String studentNo;
	
	@ExcelField(fieldName = "name", index = "1", title = "姓名", type = ExcelField.CELL_TYPE_STRING)
	String name;
	
	@ExcelField(fieldName = "sex", index = "2", title = "性别", type = ExcelField.CELL_TYPE_STRING, convert = {"0:男", "1:女"})
	String sex;
	
	@ExcelField(fieldName = "idCard", index = "3", title = "身份证号", type = ExcelField.CELL_TYPE_STRING)
	String idCard;
	
	@ExcelField(fieldName = "status", index = "4", title = "学籍状态", type = ExcelField.CELL_TYPE_STRING, convert = {"1:在读", "2:毕业", "3:休学", "4:转学", "5:退学"})
	String status;
	
	@ExcelField(fieldName = "matriculateDate", index = "5", title = "入学时间", type = ExcelField.CELL_TYPE_STRING)
	String matriculateDate;
	
	@ExcelField(fieldName = "className", index = "6", title = "行政班名称", type = ExcelField.CELL_TYPE_STRING)
	String className;
	
}
