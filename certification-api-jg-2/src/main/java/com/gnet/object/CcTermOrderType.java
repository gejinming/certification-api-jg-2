package com.gnet.object;

/**
 * 学期表类型枚举
 * @author sll
 * @date 2016年07月03日 17:31:09
 */
public enum CcTermOrderType implements OrderType{
	
	START_YEAR("startYear", "start_year"),
	END_YEAR("endYear", "end_year"),
	TERM("term", String.format("%s%s", NUMBER_ORDER, "term")),
	TERM_TYPE("termType", "term_type"),
	SORT("sort", "sort");
	
	private String key;
	private String value;
	
	private CcTermOrderType(String key, String value) {
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