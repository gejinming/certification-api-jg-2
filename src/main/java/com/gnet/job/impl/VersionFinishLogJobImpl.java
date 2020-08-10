package com.gnet.job.impl;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.gnet.service.CcVersionCreateLogService;
import com.gnet.utils.SpringContextHolder;

/**
 * 创建版本日志的创建完成状态
 * @author xzl
 * @date 2016年10月19日
 *
 */
public class VersionFinishLogJobImpl implements Job {

	@Override
	public void execute(JobExecutionContext jobexecutioncontext) throws JobExecutionException {
		JobDataMap dataMap = jobexecutioncontext.getJobDetail().getJobDataMap();
		Long versionCreateLogId = dataMap.getLong("versionCreateLogId");
		
		CcVersionCreateLogService ccVersionCreateLogService = SpringContextHolder.getBean(CcVersionCreateLogService.class);
		ccVersionCreateLogService.finish(versionCreateLogId);
	}

}
