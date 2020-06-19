package com.gnet.object;

/**
 * 教师排序类型枚举
 * @author SY
 * @Date 2016年6月21日22:16:53
 */
public enum CcTeacherOrderType implements OrderType{
	
	CODE("code", "ct.code"),
	NAME("name", "ct.name"),
	SEX("sex", "ct.sex"),
	MAJOR_NAME("majorName", "major.name"),
	INSTITUTE_NAME("instituteName", "institute.name"),
	JOB_TITLE("jobTitle", "ct.job_title"),
	DEGREE("highestDegrees", "ct.highest_degrees"),
	PHONE("mobilePhone", "ct.mobile_phone");
	
	private String key;
	private String value;
	
	private CcTeacherOrderType(String key, String value) {
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