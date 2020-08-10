package com.gnet.object;

/**
 * 毕业生就业情况表类型枚举
 * @author sll
 * @date 2016年07月20日 21:54:24
 */
public enum CcGraduateEmploymentOrderType implements OrderType{
	
	YEAR("year", "cge.year"),
	MAJORID("majorId", "major_id"),
	GRADUATENUMS("graduateNums", String.format("%s%s", NUMBER_ORDER, "graduate_nums")),
	GRADUATERATIO("graduateRatio", String.format("%s%s", NUMBER_ORDER, "graduate_ratio")),
	GETDEGREERATIO("getDegreeRatio", String.format("%s%s", NUMBER_ORDER, "get_degree_ratio")),
	FIRSTTIMEEMPLOYEDRADIO("firsttimeEmployedRatio", String.format("%s%s", NUMBER_ORDER, "firsttime_employed_ratio")),
	MASTERANDGOABROAD("masterAndGoabroadRatio", String.format("%s%s", NUMBER_ORDER, "master_and_goabroad_ratio")),
	NATIONANDINSTITUTIONRATIO("nationAndInstitutionRatio", String.format("%s%s", NUMBER_ORDER, "nation_and_institution_ratio")),
	OTHERENTERPRISERATIO("otherEnterpriseRatio", String.format("%s%s", NUMBER_ORDER, "other_enterprise_ratio"));
	
	private String key;
	private String value;
	
	private CcGraduateEmploymentOrderType(String key, String value) {
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