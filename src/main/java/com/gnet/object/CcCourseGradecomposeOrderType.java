package com.gnet.object;
/**
 * 开课课程组成元素排序
 * @author xzl
 * @Date 2016年7月7日
 */
public enum CcCourseGradecomposeOrderType implements OrderType{
	
	NAME("name", "cg.name");
	
	private String key;
	private String value;
	
	private CcCourseGradecomposeOrderType(String key, String value) {
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
