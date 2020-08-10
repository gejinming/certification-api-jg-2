package com.gnet.object;

/**
 * 用户枚举
 * @author xzl
 * @date 2016年06月29日 18:38:27
 */
public enum UserOrderType implements OrderType{
	
	NAME("name", "su.name"),
	LOGIN_NAME("loginName", "su.loginName"),
	EMAIL("email", "su.email"),
	IS_ENABLED("isEnabled", "su.is_enabled"),
	DEPARTMENT_NAME("departmentName", "so.name");
	
	private String key;
	private String value;
	
	private UserOrderType(String key, String value) {
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