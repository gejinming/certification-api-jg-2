package com.gnet.object;

/**
 * 教师进修经历表类型枚举
 * @author sll
 * @date 2016年07月21日 21:08:48
 */
public enum CcTeacherFurtherEducationOrderType implements OrderType{
	
	START_YEAR("startTime", "ctfe.start_time"),
	END_YEAR("endTime", "ctfe.end_time"),
	NAME("teacherName", "ct.name"),
	EDUCATION_TYPE("educationType", "ctfe.education_type"),
	CONTENT("content", "ctfe.content"),
	
	SITE("site", "ctfe.site");
	
	private String key;
	private String value;
	
	private CcTeacherFurtherEducationOrderType(String key, String value) {
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