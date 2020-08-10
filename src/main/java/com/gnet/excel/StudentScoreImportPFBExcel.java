package com.gnet.excel;

import com.gnet.plugin.poi.ExcelField;
import com.gnet.plugin.poi.ExcelModel;

/**
 * 学生成绩-评分表分析法信息excel类
 * 
 * @author SY
 * @Date 2016年7月12日14:53:25
 */
//@ExcelModel(name = "pfbImportExcel", rowCount = "2")
public class StudentScoreImportPFBExcel {
	/*
	 * 从第二行开始，第四行后面几个是指标点编号，需要记住。 
	 * 列：
	 * 1. 序号	2.学号	3.姓名	4.教学班名称	5.成绩组成id或者空或者指标点id 6.重复第5步……
	 */
	
	/**
	 * 评分表分析法名字
	 */
	public static final String NAME = "pfb";
	/**
	 * 头部所占用的行数
	 */
	public static final Integer COLS_COUNT = 2;
	/**
	 * 头部所占用的列数
	 */
	public static final Integer COLUMN_COUNT = 1;
	
//	@ExcelField(fieldName = "index", index = "1", title = "序号", type = ExcelField.CELL_TYPE_STRING)
//	String index;
//	
//	@ExcelField(fieldName = "studentNo", index = "2", title = "学号", type = ExcelField.CELL_TYPE_STRING)
//	String studentNo;
//	
//	@ExcelField(fieldName = "name", index = "3", title = "姓名", type = ExcelField.CELL_TYPE_STRING)
//	String name;
//	
//	@ExcelField(fieldName = "eduClassName", index = "4", title = "教学班名称", type = ExcelField.CELL_TYPE_STRING)
//	String eduClassName;
}
