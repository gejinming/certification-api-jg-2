package com.gnet.object;

/**
 * 学生专业方向排序
 * @author xzl
 * @Date 2016年12月8日
 */
public enum CcMajorStudentOrderType implements OrderType{
	
    NAME("name", "cs.name"),
    STUDENT_NO("studentNo", "cs.student_no"),
    SEX("sex", "cs.sex"),
    CLASSNAME("className", "class.name"),
    MAJOR_DIRECTION_NAME("majorDirectionName", "cmd.name");
	
	private String key;
	private String value;
	
	private CcMajorStudentOrderType(String key, String value) {
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