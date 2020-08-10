package com.gnet.plugin.echart;

/**
 * 
 * EChart图的类型枚举
 * 
 * @author wct
 * @date 2016年8月9日
 */
public enum SeriesType {
	
	LINE("line"), 
	BAR("bar"), 
	SCATTER("scatter"), 
	K("k"), 
	PIE("pie"), 
	RADAR("radar"), 
	CHORD("chord");
	
	String type;
	
	private SeriesType(String type) {
		this.type = type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}
