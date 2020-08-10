package com.gnet.object;

/**
 * 课程组表类型枚举
 * @author SY
 * @date 2016年07月14日 11:10:53
 */
public enum CcCourseGroupOrderType implements OrderType{
	
	REMARK("remark", "remark");
	
	private String key;
	private String value;
	
	private CcCourseGroupOrderType(String key, String value) {
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