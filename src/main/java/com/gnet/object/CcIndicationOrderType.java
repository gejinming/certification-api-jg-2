package com.gnet.object;

/**
 * 课程目标排序
 * @author xzl
 * @date 2017年11月22日15:42:34
 */
public enum CcIndicationOrderType implements OrderType{

	CONTENT("content", "ci.content");

	private String key;
	private String value;

	private CcIndicationOrderType(String key, String value) {
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