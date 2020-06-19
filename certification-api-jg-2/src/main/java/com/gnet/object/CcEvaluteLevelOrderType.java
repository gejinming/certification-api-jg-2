package com.gnet.object;

/**
 * 考评点得分层次关系表类型枚举
 * @author sll
 * @date 2016年07月05日 18:29:45
 */
public enum CcEvaluteLevelOrderType implements OrderType{
	
	LEVEL_NAME("levelName", "level_name");
	
	private String key;
	private String value;
	
	private CcEvaluteLevelOrderType(String key, String value) {
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