package com.gnet.object;

/**
 * 专业排序类型枚举
 * @author SY
 * @Date 2016年6月21日22:16:53
 */
public enum CcVersionOrderType implements OrderType{
	
	NAME("name", "cv.name"),
	STATE("state", "cv.state"),
	PLAN_COURSE_VERSION_NAME("planCourseVersionName", "cpv.course_version_name"),
	GRADE("enableGrade", "cv.enable_grade"),
	GRADUATE_NAME("graduateName", "cgv.name"),
	INDICATION_NAME("graduateIndicationVersionName", "cgv.indication_version_name"),
	PLAN_NAME("planName", "cpv.name");
	
	private String key;
	private String value;
	
	private CcVersionOrderType(String key, String value) {
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