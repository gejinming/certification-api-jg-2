package com.gnet.object;

/**
 * ${moduleCName}类型枚举
 * @author ${author}
 * @date ${currentDate?string("yyyy年MM月dd日 HH:mm:ss")}
 */
public enum ${moduleName}OrderType implements OrderType{
	
	<#list records as record>
	<#if record.isSearch>
	<#assign num = 0>
	${record.searchProps.NAME}("${record.searchProps.name}", "${record.COLUMN_NAME}")<#if num == (searches.size() - 1)>;<#else>,</#if>
	</#if>
	</#list>
	
	private String key;
	private String value;
	
	private ${moduleName}OrderType(String key, String value) {
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