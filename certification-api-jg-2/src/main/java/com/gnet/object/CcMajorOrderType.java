package com.gnet.object;

/**
 * 专业排序类型枚举
 * @author SY
 * @Date 2016年6月21日22:16:53
 */
public enum CcMajorOrderType implements OrderType{
	
	EDUCATION_LENGTH("educationLength", "cm.education_length"),
	MAJOR_NAME("majorName", "sozy.name"),
	CODE("code", "sozy.code"),
	INSTITUTE_NAME("instituteName", "soxy.name"),
	USERNAME("userName", "su.name");
	
	private String key;
	private String value;
	
	private CcMajorOrderType(String key, String value) {
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