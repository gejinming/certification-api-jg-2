package com.gnet.service;

import org.springframework.stereotype.Component;

/**
 * @author SY
 * @date 2017年1月22日15:18:10
 */
@Component("ccReportBuildStatusService")
public class CcReportBuildStatusService {

	/**
	 * 通过年级和版本编号获取课程达成度的--报表生成键值
	 * 
	 * 教学班达成度报表键值规则：
	 * 		教学班编号+"异步统计教学班达成度数据"
	 * 		年级达成度报表键值规则：年级+版本编号+"异步统计课程达成度数据"
	 * 		个人达成度报表键值规则：年级+版本编号+"异步统计个人达成度数据"
	 * 		培养计划报表键值规则：培养计划编号+"异步统计培养计划数据"
	 * 		开课课程教学班达成度报表键值规则：开课课程编号+"异步统计教师开课课程下的教学班达成度数据"
	 * @param grade
	 * @param versionId
	 * @author SY 
	 * @version 创建时间：2017年1月22日 下午3:19:24 
	 */
	public static String getReportBuildKeyForCourse(Integer grade, Long versionId) {
		StringBuilder sb = new StringBuilder();
		return sb.append(grade).append(",").append(versionId).append("异步统计课程达成度数据").toString();
	}


	
}
