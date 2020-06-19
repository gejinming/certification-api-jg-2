package com.gnet.object;

/**
 * 等级制度明细表类型枚举
 * @author SY
 * @date 2019年12月16日17:06:14
 */
public enum CcLevelDetailOrderType implements OrderType{
	
	INDICATION_ID("indicationId", "cld.indication_id");
	
	private String key;
	private String value;
	
	private CcLevelDetailOrderType(String key, String value) {
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