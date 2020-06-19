package com.gnet.object;

/**
 * 指标点课程关系表类型枚举
 * @author sll
 * @date 2016年07月04日 15:58:01
 */
public enum CcEvaluteOrderType implements OrderType{
	
	INDICATION_ID("indicationId", "ce.indication_id");
	
	private String key;
	private String value;
	
	private CcEvaluteOrderType(String key, String value) {
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