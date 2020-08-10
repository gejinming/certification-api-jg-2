package com.gnet.object;

/**
 * 成绩组成排序类型枚举
 * @author SY
 * @Date 2016年6月21日22:16:53
 */
public enum CcGradecomposeOrderType implements OrderType{
	
	NAME("name", "cg.name");
	
	private String key;
	private String value;
	
	private CcGradecomposeOrderType(String key, String value) {
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