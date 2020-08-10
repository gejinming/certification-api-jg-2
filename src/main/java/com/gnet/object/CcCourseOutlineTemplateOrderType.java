package com.gnet.object;

/**
 * 教学大纲模板类型枚举
 * @author xzl
 * @date 2017年8月2日
 */
public enum CcCourseOutlineTemplateOrderType implements OrderType{

	NAME("name", "name");

	private String key;
	private String value;

	private CcCourseOutlineTemplateOrderType(String key, String value) {
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