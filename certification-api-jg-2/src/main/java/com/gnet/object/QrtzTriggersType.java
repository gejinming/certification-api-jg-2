package com.gnet.object;

/**
 * 任务监控排序
 * @author wct
 * @Date 2016年6月4日
 */
public enum QrtzTriggersType implements OrderType{
	
	START_TIME("startTime", "START_TIME"),
	
	END_TIME("endTime", "END_TIME"),
	
	TRIGGER_STATE("triggerState", "TRIGGER_STATE"),
	
	TRIGGER_TYPE("triggerType", "TRIGGER_TYPE");
	
	private String key;
	private String value;
	
	private QrtzTriggersType(String key, String value) {
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
