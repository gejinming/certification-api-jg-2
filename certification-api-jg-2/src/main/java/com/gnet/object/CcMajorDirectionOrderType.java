package com.gnet.object;

import com.gnet.object.OrderType;

/**
 * 专业方向类型枚举
 * @author sll
 * @date 2016年06月28日 17:57:45
 */
public enum CcMajorDirectionOrderType implements OrderType{
	
	NAME("name", "cmd.name");
	
	private String key;
	private String value;
	
	private CcMajorDirectionOrderType(String key, String value) {
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