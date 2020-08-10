package com.gnet.object;
/**
 * 所属模块排序类型枚举
 * @author xzl
 * @Date 2016年7月2日
 */
public enum CcCourseModuleOrderType implements OrderType{
	
	MODULE_NAME("moduleName", "ccm.module_name");
	
	private String key;
	private String value;
	
	private CcCourseModuleOrderType(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {	
		return key;
	}
	
	public String getValue() {
		return value;
	}

	
}
