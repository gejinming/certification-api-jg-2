package com.gnet.plugin.quartz.db;

/**
 * JDBC常量
 * 
 * @author xuq
 * @date 2015年12月30日
 * @version 1.1
 */
public interface IJdbcPushConstants {
	
	/* TABLE NAMES */
	static final String TABLE_PUSH = "push_job";
	static final String TABLE_PUSH_LOG = "push_job_log";
	static final String TABLE_PUSH_ERROR = "push_job_error";
	
	/* COMMON COLUMNS */
	static final String COL_ID = "id";
	static final String COL_CREATE_DATE = "create_date";
	static final String COL_MODIFY_DATE = "modify_date";
	static final String COL_PUSH_JOB_ID = "push_job_id";
	static final String COL_PUSH_JOB_LOG_ID = "push_job_log_id";
	
	/* PUSH_JOB COLUMNS */
	static final String COL_START_TIME = "start_time";
	static final String COL_END_TIME = "end_time";
	static final String COL_PROGRESS = "progress";
	static final String COL_JOB_CONTENT = "job_content";
	
	/* PUSH_JOB_ERROR COLUMNS */
	static final String COL_ERROR_MSG = "error_msg";
	
	/* PUSH_JOB_LOG COLUMNS */
	static final String COL_EVENT_TYPE = "event_type";
	static final String COL_EVENT_TIME = "event_time";
	
	/* EVENT_TYPE CONSTANTS */
	static final Integer EVENT_TYPE_START = 1;
	static final Integer EVENT_TYPE_FINISH = 2;
	static final Integer EVENT_TYPE_ERROR = 3;

}
