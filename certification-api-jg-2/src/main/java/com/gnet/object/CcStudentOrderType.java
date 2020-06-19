package com.gnet.object;

/**
 * 学生排序枚举
 * 
 * @author wct
 * @Date 2016年6月29日
 */
public enum CcStudentOrderType implements OrderType{
	
	CREATE_DATE("createDate", "cs.create_date"),
	GRADUATE_DATE("graduateDate", "cs.graduate_date"),
	CLASSID("classId", "cs.class_id"),
	MATRICULATE_DATE("matriculateDate", "cs.matriculate_date"),
	NAME("name", "cs.name"),
	SEX("sex", "cs.sex"),
	STUDENT_NO("studentNo", "cs.student_no"),
	MAJOR_NAME("majorName", "major.name"),
	CLASS_NAME("className", "clazz.name"),
	BIRTHDAY("birthday", "cs.birthday"),
	GRADE("grade", "cs.grade"),
	MOBILE_PHONE("mobilePhone", "cs.mobile_phone");
	
	private String key;
	private String value;
	
	private CcStudentOrderType(String key, String value) {
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
