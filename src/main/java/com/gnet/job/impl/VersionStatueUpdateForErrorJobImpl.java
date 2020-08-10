package com.gnet.job.impl;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.gnet.service.CcVersionCreateLogService;
import com.gnet.utils.SpringContextHolder;

/**
 * 更新版本新建状态为错误
 * @author SY
 *
 */
public class VersionStatueUpdateForErrorJobImpl implements Job {

	@Override
	public void execute(JobExecutionContext jobexecutioncontext) throws JobExecutionException {
		JobDataMap dataMap = jobexecutioncontext.getJobDetail().getJobDataMap();
		String createMessage = dataMap.getString("createMessage");
		Long versionCreateLogId = dataMap.getLong("versionCreateLogId");
		Integer createStep = dataMap.getInt("createStep");
		
		CcVersionCreateLogService ccVersionCreateLogService = SpringContextHolder.getBean(CcVersionCreateLogService.class);
		ccVersionCreateLogService.changeStepForError(versionCreateLogId, createMessage, createStep);
	}

}
