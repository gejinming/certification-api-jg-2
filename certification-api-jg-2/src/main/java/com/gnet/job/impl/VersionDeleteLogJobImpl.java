package com.gnet.job.impl;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.gnet.service.CcVersionDeleteLogService;
import com.gnet.utils.SpringContextHolder;

/**
 * 废弃版本日志的新建状态
 * @author SY
 * @date 2016年8月18日14:07:11
 *
 */
public class VersionDeleteLogJobImpl implements Job {

	@Override
	public void execute(JobExecutionContext jobexecutioncontext) throws JobExecutionException {
		JobDataMap dataMap = jobexecutioncontext.getJobDetail().getJobDataMap();
		String key = dataMap.getString("key");
		Long userId = dataMap.getLong("userId");
		Long id = dataMap.getLong("id");
		Long majorId = dataMap.getLong("majorId");
		
		CcVersionDeleteLogService ccVersionDeleteLogService = SpringContextHolder.getBean(CcVersionDeleteLogService.class);
		ccVersionDeleteLogService.create(key, userId, id, majorId);
	}

}
