package com.gnet.object;

/**
 * 成绩组成元素明细学生关联类型枚举
 * @author xzl
 * @date 2017年10月16日
 */
public enum CcCourseGradeComposeStudetailOrderType implements OrderType{

	STUDENT_NAME("studentName", "cs.name"),
	SCORE("score", "ccgs.score"),
	STUDENT_NO("studentNo", "cs.student_no");

	private String key;
	private String value;

	private CcCourseGradeComposeStudetailOrderType(String key, String value) {
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