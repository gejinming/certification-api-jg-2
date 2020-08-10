package com.gnet.object;

/**
 * School类型枚举
 * @author zsf
 * @date 2016年06月25日 18:39:35
 */
public enum SchoolOrderType implements OrderType{
	
	NAME("name", "so.name"),
	LOGIN_NAME("loginName", "su.loginName"),
	CODE("code", "so.code");
	
	private String key;
	private String value;
	
	private SchoolOrderType(String key, String value) {
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