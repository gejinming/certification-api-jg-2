package com.gnet.job.impl;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.gnet.service.CcVersionCreateLogService;
import com.gnet.utils.SpringContextHolder;

/**
 * 创建版本日志的新建状态
 * @author SY
 * @date 2016年7月22日09:47:54
 *
 */
public class VersionCreateLogJobImpl implements Job {

	@Override
	public void execute(JobExecutionContext jobexecutioncontext) throws JobExecutionException {
		JobDataMap dataMap = jobexecutioncontext.getJobDetail().getJobDataMap();
		String key = dataMap.getString("key");
		Long userId = dataMap.getLong("userId");
		Long versionCreateLogId = dataMap.getLong("versionCreateLogId");
		Long majorId = dataMap.getLong("majorId");
		
		CcVersionCreateLogService ccVersionCreateLogService = SpringContextHolder.getBean(CcVersionCreateLogService.class);
		ccVersionCreateLogService.create(key, userId, versionCreateLogId, majorId);
	}

}
