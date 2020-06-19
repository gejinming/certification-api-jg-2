package com.gnet.object;

/**
 * 课程性质排序类型枚举
 * @author SY
 * @Date 2016年6月21日22:16:53
 */
public enum CcCoursePropertySecondaryOrderType implements OrderType{
	
	PROPERTY_NAME("propertyName", "ccps.property_name");
	
	private String key;
	private String value;
	
	private CcCoursePropertySecondaryOrderType(String key, String value) {
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