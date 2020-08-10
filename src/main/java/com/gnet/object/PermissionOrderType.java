package com.gnet.object;

/**
 * 权限类型枚举
 * @author xzl
 * @date 2016年11月8日10:20:30
 */
public enum PermissionOrderType implements OrderType{
	
	NAME("name", "name"),
	CODE("code", "code"),
	PNAME("pname", "pname");
	
	private String key;
	private String value;
	
	private PermissionOrderType(String key, String value) {
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