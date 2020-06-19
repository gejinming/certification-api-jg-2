package com.gnet.excel;

import com.gnet.plugin.poi.ExcelField;
import com.gnet.plugin.poi.ExcelModel;

/**
 * 毕业要求excel类
 * 
 * @author SY
 * @Date 2018年1月10日
 */
@ExcelModel(name = "graduateExcel", rowCount = "2")
public class GraduateExcel {
	
	@ExcelField(fieldName = "graduateIndexAndContent", index = "0", title = "毕业要求序号以及名称", type = ExcelField.CELL_TYPE_STRING)
	String graduateIndexAndContent;
	
	@ExcelField(fieldName = "indName", index = "1", title = "指标点名称", type = ExcelField.CELL_TYPE_STRING)
	String indName;
	
	@ExcelField(fieldName = "courseName", index = "2", title = "课程名称", type = ExcelField.CELL_TYPE_STRING)
	String courseName;
	
	@ExcelField(fieldName = "weight", index = "3", title = "权重", type = ExcelField.CELL_TYPE_STRING)
	String weight;
	
}
