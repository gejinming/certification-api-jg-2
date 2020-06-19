package com.gnet.job.impl;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.gnet.service.CcVersionDeleteLogService;
import com.gnet.utils.SpringContextHolder;

/**
 * 更新版本废弃状态为错误
 * @author SY
 *
 */
public class VersionCloseStatueUpdateForErrorJobImpl implements Job {

	@Override
	public void execute(JobExecutionContext jobexecutioncontext) throws JobExecutionException {
		JobDataMap dataMap = jobexecutioncontext.getJobDetail().getJobDataMap();
		String deleteMessage = dataMap.getString("deleteMessage");
		Long versionDeleteLogId = dataMap.getLong("versionDeleteLogId");
		Integer deleteStep = dataMap.getInt("deleteStep");
		
		CcVersionDeleteLogService ccVersionDeleteLogService = SpringContextHolder.getBean(CcVersionDeleteLogService.class);
		ccVersionDeleteLogService.changeStepForError(versionDeleteLogId, deleteMessage, deleteStep);
	}

}
