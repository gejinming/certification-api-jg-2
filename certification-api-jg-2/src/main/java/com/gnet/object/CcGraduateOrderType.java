package com.gnet.object;

/**
 * 毕业要求类型枚举
 * @author SY
 * @date 2016年06月24日 19:54:33
 */
public enum CcGraduateOrderType implements OrderType{

	INDEX_NUM("indexNum",  String.format("%s%s",NUMBER_ORDER, "cg.index_num")),
	CONTENT("content", "cg.content");
	
	private String key;
	private String value;
	
	private CcGraduateOrderType(String key, String value) {
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