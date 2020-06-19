package com.gnet.object;

/**
 * CcInstitute类型枚举
 * @author zsf
 * @date 2016年06月26日 18:57:47
 */
public enum CcInstituteOrderType implements OrderType{
	
	CODE("code", "so.code"),
	NAME("name", "so.name");
	
	private String key;
	private String value;
	
	private CcInstituteOrderType(String key, String value) {
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