package com.gnet.plugin.pushMonitor.obj;

/**
 * 分页信息
 * 
 * @author xuq
 * @date 2015年12月30日
 * @version 1.1
 */
public class PageInfo {

	private Integer pageNumber;
	private Integer pageSize;
	private Integer totalPage;
	private OrderInfo[] orderInfos;
	
	public PageInfo(Integer pageNumber, Integer pageSize, OrderInfo...orderInfos) {
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.orderInfos = orderInfos;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public OrderInfo[] getOrderInfos() {
		return orderInfos;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	
}
