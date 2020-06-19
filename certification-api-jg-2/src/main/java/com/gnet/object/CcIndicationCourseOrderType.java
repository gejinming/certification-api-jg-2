package com.gnet.object;

/**
 * 指标点课程关系表类型枚举
 * @author SY
 * @date 2016年06月30日 21:17:12
 */
public enum CcIndicationCourseOrderType implements OrderType{
	
	INDICATION_CONTENT("indicationContent", "ci.content"),
	WEIGHT("weight", "cic.weight"),
	EDU_AIM("eduAim", "cic.edu_aim"),
	GRADUATE_INDICATION_INDEX_NUM("graduateIndicationIndexNum", "graduateIndicationIndexNum"),
	WAY("way", "cic.way"),
	COURSE_NAME("courseName", "cc.name");
	
	private String key;
	private String value;
	
	private CcIndicationCourseOrderType(String key, String value) {
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