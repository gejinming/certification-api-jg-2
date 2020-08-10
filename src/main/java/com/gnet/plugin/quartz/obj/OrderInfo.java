package com.gnet.plugin.quartz.obj;

import java.util.Arrays;
import java.util.List;

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
