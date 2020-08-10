package com.gnet.excel;

import com.gnet.plugin.poi.ExcelField;
import com.gnet.plugin.poi.ExcelModel;

/**
 * 大纲excel类
 * @author xzl
 * @Date 2017年9月30日17:17:52
 */
@ExcelModel(name = "courseOutlineExcel", rowCount = "1")
public class CourseOutlineExcel {
	
	@ExcelField(fieldName = "majorName", index = "0", title = "所属专业", type = ExcelField.CELL_TYPE_STRING)
	String majorName;

	@ExcelField(fieldName = "code", index = "1", title = "课程代码", type = ExcelField.CELL_TYPE_STRING)
	String code;

	@ExcelField(fieldName = "courseName", index = "2", title = "课程名称", type = ExcelField.CELL_TYPE_STRING)
	String courseName;

	@ExcelField(fieldName = "name", index = "3", title = "大纲名称", type = ExcelField.CELL_TYPE_STRING)
	String name;
	
	@ExcelField(fieldName = "outlineTypeName", index = "4", title = "大纲类型", type = ExcelField.CELL_TYPE_STRING)
	String outlineTypeName;
	
	@ExcelField(fieldName = "authorName", index = "5", title = "执笔人", type = ExcelField.CELL_TYPE_STRING)
	String authorName;

	@ExcelField(fieldName = "auditorName", index = "6", title = "审核人", type = ExcelField.CELL_TYPE_STRING)
	String auditorName;

	@ExcelField(fieldName = "status", index = "7", title = "大纲状态", type = ExcelField.CELL_TYPE_STRING, convert = {"0:未分配", "1:已分配未确认", "2:未开始", "3:未提交", "4:待审核", "5:审核通过", "6:审核驳回"})
	String status;

}
