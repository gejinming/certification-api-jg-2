package com.gnet.object;

/**
 * 成绩组成元素明细表类型枚举
 * @author sll
 * @date 2016年07月06日 14:37:10
 */
public enum CcCourseGradeComposeDetailOrderType implements OrderType{
	
	NAME("name", "name"),
	DETAIL("detail", "detail");
	
	private String key;
	private String value;
	
	private CcCourseGradeComposeDetailOrderType(String key, String value) {
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