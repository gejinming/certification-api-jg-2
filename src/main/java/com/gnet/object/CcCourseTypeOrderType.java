package com.gnet.object;

import sun.management.Agent;

/**
 * 课程类型枚举
 * @author SY
 * @Date 2016年6月21日22:16:53
 */
public enum CcCourseTypeOrderType implements OrderType{

	NAME("typeValue", "cct.type_value");


	private String key;
	private String value;

	private CcCourseTypeOrderType(String key, String value) {
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