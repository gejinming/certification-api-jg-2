package com.gnet.object;

/**
 * 排序方向枚举
 * @author wct
 * @Date 2016年5月12日
 */
public enum OrderDirectionType {
	
	ASC("asc"),
	
	DESC("desc");
	
	private String value;
	
	private OrderDirectionType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
