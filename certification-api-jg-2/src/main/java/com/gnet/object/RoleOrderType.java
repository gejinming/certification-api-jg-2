package com.gnet.object;

/**
 * 角色类型枚举
 * @author xzl
 * @date 2017年10月17日
 */
public enum RoleOrderType implements OrderType{

	NAME("name", "sr.name");


	private String key;
	private String value;

	private RoleOrderType(String key, String value) {
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