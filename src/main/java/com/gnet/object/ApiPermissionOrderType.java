package com.gnet.object;

/**
 * 接口类型枚举
 * @author xzl
 * @Date 2018年1月9日14:09:55
 */
public enum ApiPermissionOrderType implements OrderType{

	CODE("code", "code"),
	NAME("name", "name"),
	DESCRIPTION("description", "description");

	private String key;
	private String value;

	private ApiPermissionOrderType(String key, String value) {
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