package com.gnet.object;

/**
 * 抽象学期表类型枚举
 * @author sll
 * @date 2016年07月04日 08:30:41
 */
public enum CcPlanTermOrderType implements OrderType{
	
	YEAR("year", "year"),
	YEAR_NAME("yearName", "year_name"),
	TERM("term", "term"),
	TERM_NAME("termName", "term_name"),
	TERM_TYPE("termType", "term_type"),
	WEEK_NUMS("weekNums", String.format("%s%s", NUMBER_ORDER, "week_nums"));
	
	private String key;
	private String value;
	
	private CcPlanTermOrderType(String key, String value) {
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