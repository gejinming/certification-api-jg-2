package com.gnet.object;

/**
 * 课程层次排序类型枚举
 * @author SY
 * @Date 2016年6月21日22:16:53
 */
public enum CcCourseHierarchySecondaryOrderType implements OrderType{
	
	NAME("name", "cchs.name");
	
	private String key;
	private String value;
	
	private CcCourseHierarchySecondaryOrderType(String key, String value) {
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