package com.gnet.object;

/**
 * 培养计划类型枚举
 * @author xzl
 * @Date 2017年10月17日
 */
public enum CcPlanVersionOrderType implements OrderType{

	NAME("name", "cpv.name"),
	VERSION_NAME("versionName", "cv.name"),
	VERSION_STATE("versionState", "cv.state"),
	MAJOR_VERSION("majorVersion", "cv.major_version"),
	MINOR_VERSION("minorVersion", "cv.major_version"),
	ENABLE_GRADE("enableGrade", "cv.enable_grade"),
	IS_USE("isUse", "cv.is_use");

	private String key;
	private String value;

	private CcPlanVersionOrderType(String key, String value) {
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