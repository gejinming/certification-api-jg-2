package com.gnet.object;

/**
 * 教学班类型枚举
 * @author SY
 * @date 2016年07月01日 14:41:11
 */
public enum CcEduclassOrderType implements OrderType{
	
	EDUCLASS_NAME("educlassName", "ce.educlass_name"),
	COURSE_NAME("courseName", "cc.name"),
	COURSE_CODE("courseCode", "cc.code");
	
	private String key;
	private String value;
	
	private CcEduclassOrderType(String key, String value) {
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