package com.gnet.object;

/**
 * 行政班类型枚举
 * @author sll
 * @date 2016年06月29日 18:38:27
 */
public enum OfficeOrderType implements OrderType{
	
	NAME("name", "office.name"),
	MAJOR_NAME("majorName", "major.name"),
	INSTITUTE("instituteName", "institute.name"),
	CLASS_LEADER("classLeader", "cc.class_leader"),
	GRADE("grade", "cc.grade");
	
	private String key;
	private String value;
	
	private OfficeOrderType(String key, String value) {
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