package com.gnet.object;

/**
 * 招生情况表类型枚举
 * @author sll
 * @date 2016年07月19日 09:41:29
 */
public enum CcEnrollmentOrderType implements OrderType{
	
	YEAR("year", "ce.year"),
	MAJOR_ID("majorId", "ce.major_id"),
	ENROL_NUM("enrolNum", String.format("%s%s", NUMBER_ORDER, "ce.enrol_num")),
	PROVINCE_DIVISION("provinceDivision", String.format("%s%s", NUMBER_ORDER, "ce.province_division")),
	MAJOR_DIVISION("majorDivision", String.format("%s%s", NUMBER_ORDER, "ce.major_division")),
	LOWEST_LINE("lowestLine", String.format("%s%s", NUMBER_ORDER, "ce.lowest_line")),
	FIRST_VOLUNTARY_ENROLLMENT_RATIO("firstVoluntaryEnrollmentRatio", String.format("%s%s", NUMBER_ORDER, "ce.first_voluntary_enrollment_ratio"))
	;
	
	private String key;
	private String value;
	
	private CcEnrollmentOrderType(String key, String value) {
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