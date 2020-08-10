package com.gnet.object;

/**
 * 开课课程成绩组成元素指标点关联表类型枚举
 * @author XZL
 * @date 2016年07月07日 20:45:05
 */
public enum CcCourseGradecomposeIndicationOrderType implements OrderType{
	
	WEIGHT("weight", "ccgi.weight");


	private String key;
	private String value;
	
	private CcCourseGradecomposeIndicationOrderType(String key, String value) {
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