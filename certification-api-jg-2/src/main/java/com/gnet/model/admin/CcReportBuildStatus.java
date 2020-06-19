package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;

/**
 * 报表生成状态记录表
 * 
 * @author wct
 * @date 2016年7月22日
 */
@TableBind(tableName = "cc_report_build_status")
public class CcReportBuildStatus extends DbModel<CcReportBuildStatus>{

	private static final long serialVersionUID = -7005850968383631298L;
	public static final CcReportBuildStatus dao = new CcReportBuildStatus();
	
	/**
	 * 教学班报表类型，代码为1
	 */
	public static final Integer TYPE_EDUCLASS = 1;
	
	/**
	 * 年级报表类型， 代码为2
	 */
	public static final Integer TYPE_COURSEINDICATION = 2;
	
	/**
	 * 学生个人报表类型，代码为3
	 */
	public static final Integer TYPE_STUDENT = 3;
	
	/**
	 * 培养计划报表类型，代码为4
	 */
	public static final Integer TYPE_PLANREPORT = 4;
	
	/**
	 * 开课课程下的教学班报表类型，代码为5
	 */
	public static final Integer TYPE_TEACHERCOURSE_EDUCLASS = 5;
	
	
	/**
	 * 任务未创建状态，代码为0
	 */
	public static final Integer STATUS_TASK_UNCREATE = 0;
	
	/**
	 * 任务创建失败，代码为1
	 */
	public static final Integer STATUS_TASK_CREATEFAIL = 1;
	
	/**
	 * 任务创建成功，代码为2
	 */
	public static final Integer STATUS_TASK_CREATESUCCESS = 2; 
	
	/**
	 * 任务已经开始，代码为3
	 */
	public static final Integer STATUS_TASK_START = 3;
	
	/**
	 * 报表生成失败，代码为4
	 */
	public static final Integer STATUS_TASK_FAIL = 4;
	
	/**
	 * 报表生成成功，代码为5
	 */
	public static final Integer STATUS_TASK_SUCCESS = 5;
	
	/**
	 * 根据报表生成类型
	 * 
	 * @param buildType 生成类型
	 * @param key 任务名称
	 * @return
	 */
	public CcReportBuildStatus getBuildStatusRecord(Integer buildType, String key) {
		return findFirst("select * from " + tableName + " where report_type = ? and report_build_key = ? order by create_date desc", buildType, key);
	}
}