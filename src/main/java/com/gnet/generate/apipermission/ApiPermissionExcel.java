package com.gnet.generate.apipermission;

import com.gnet.plugin.poi.ExcelField;
import com.gnet.plugin.poi.ExcelModel;

/**
 * 教学班导入学生模板
 * 
 * @author wct
 * @Date 2016年7月6日
 */
@ExcelModel(name = "apiPermissionExcel", rowCount = "1")
public class ApiPermissionExcel {
	
	@ExcelField(fieldName = "moduleName", index = "0", title = "模块名称", type = ExcelField.CELL_TYPE_STRING)
	String moduleName;
	
	@ExcelField(fieldName = "name", index = "1", title = "功能权限名称", type = ExcelField.CELL_TYPE_STRING)
	String name;
	
	@ExcelField(fieldName = "permission", index = "2", title = "功能权限编码", type = ExcelField.CELL_TYPE_STRING)
	String permissions;
	
	@ExcelField(fieldName = "apipermissions", index = "3", title = "api权限编码集", type = ExcelField.CELL_TYPE_STRING)
	String apipermissions;
}