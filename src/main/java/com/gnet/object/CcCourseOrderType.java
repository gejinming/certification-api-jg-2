package com.gnet.object;

/**
 * 课程表类型枚举
 * @author SY
 * @date 2016年06月28日 14:26:40
 */
public enum CcCourseOrderType implements OrderType{
	
	CODE("code", "cc.code"),
	NAME("courseName", "cc.name"),
	CREDIT("credit", String.format("%s%s", NUMBER_ORDER, "cc.credit")),
	PROPERTY_NAME("propertyName", "ccp.property_name"),
	HIERARCHY_NAME("hierarchyName", "cch.name"),
	MODULE_NAME("moduleName", "ccm.module_name"),
	ALL_HOURS("allHours", String.format("%s%s", NUMBER_ORDER, "cc.all_hours")),
	VERSION_NAME("versionName", "cv.name"),
	MAJOR_NAME("majorName", "so.name"),
	TEACHER_NAME("teacherName", "ct.name"),
	STATUS_NAME("status", "cco.status"),
	TYPE("type", "cc.type"),
	COURSE_OUTLINE_NAME("courseOutlineName", "cco.name"),
	PLAN_ID("planId", "cc.plan_id");
	
	private String key;
	private String value;
	
	private CcCourseOrderType(String key, String value) {
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