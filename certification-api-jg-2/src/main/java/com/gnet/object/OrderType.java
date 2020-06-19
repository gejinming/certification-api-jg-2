package com.gnet.object;

/**
 * 排序参数接口
 * @author wct
 * @Date 2016年5月12日
 */
public interface OrderType {
	
	public String getKey();
	
	public String getValue();

	/**
	 * 如果排序字段是number类型，则加上NUMBER_ORDER的前缀用于区分和以字母排序的字段
	 */
	public static final String NUMBER_ORDER = "numberOrder";

}
