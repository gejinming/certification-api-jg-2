package com.gnet.object;

/**
 * 学生转入转出表
 * @author xzl
 * @date 2016年7月27日
 */
public enum CcStudentTransferOrderType implements OrderType{
	
	GRADE("grade", "cst.grade"),
	YEAR("year", "cst.year"),
	STUDENT_NO("studentNo", "cst.student_no"),
	STUDENT_NAME("studentName", "cst.student_name"),
	STUDENT_SEX("studentSex", "cst.student_sex"),
	TRANSFER_IN_MAJOR_NAME("transferInMajorName", "cst.transfer_in_major_name"),
	TRANSFER_OUT_MAJOR_NAME("transferOutMajorName", "cst.transfer_out_major_name");
	
	private String key;
	private String value;
	
	private CcStudentTransferOrderType(String key, String value) {
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