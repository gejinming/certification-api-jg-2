package com.gnet.plugin.pushMonitor.obj;

import java.util.Arrays;
import java.util.List;

/**
 * 排序
 * 
 * @author xuq
 * @date 2015年12月30日
 * @version 1.1
 */
public class OrderInfo {

	public enum ORDER_ENUM {
		desc,
		asc
	}
	
	private String by;
	private ORDER_ENUM order;
	
	private OrderInfo(String by, ORDER_ENUM order) {
		this.by = by;
		this.order = order;
	}
	
	public static OrderInfo get(String by, String order) {
		return new OrderInfo(by, ORDER_ENUM.valueOf(order));
	}
	
	public static OrderInfo desc(String by) {
		return new OrderInfo(by, ORDER_ENUM.desc);
	}
	
	public static OrderInfo asc(String by) {
		return new OrderInfo(by, ORDER_ENUM.asc);
	}
	
	public static List<OrderInfo> orders(OrderInfo... orderInfos) {
		return Arrays.asList(orderInfos);
	}

	public String getBy() {
		return by;
	}

	public ORDER_ENUM getOrder() {
		return order;
	}
	
}
