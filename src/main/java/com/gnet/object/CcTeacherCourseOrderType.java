package com.gnet.object;

/**
 * 教师开课课程类型枚举
 * @author SY
 * @date 2016年06月29日 14:41:41
 */
public enum CcTeacherCourseOrderType implements OrderType{
	
	COURSE_ID("courseId", "ctc.course_id"),
	TEACHER_ID("teacherId", "ctc.teacher_id"),
	TERM_ID("termId", "ctc.term_id"),
	COURSE_NAME("courseName", "cc.name"),
	TEACHER_NAME("teacherName", "ct.name"),
	START_YEAR("startYear", "ctm.start_year"),
	END_YEAR("endYear", "ctm.end_year"),
	TERM("term", "ctm.term"),
	RESULT_TYPE("resultType", "ctc.result_type"),
	MAJOR_NAME("majorName", "so.name"),
	GRADE("grade", "ctc.grade");
	
	private String key;
	private String value;
	
	private CcTeacherCourseOrderType(String key, String value) {
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