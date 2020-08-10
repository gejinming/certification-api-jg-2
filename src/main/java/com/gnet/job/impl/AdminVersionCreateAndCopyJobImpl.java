package com.gnet.job.impl;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.gnet.model.admin.CcVersion;
import com.gnet.service.CcVersionService;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.plugin.activerecord.Record;

/**
 * 1、拷贝以前大版本信息到新的大版本里面
 * @author SY
 *
 */
public class AdminVersionCreateAndCopyJobImpl implements Job {

	@Override
	public void execute(JobExecutionContext jobexecutioncontext) throws JobExecutionException {
		JobDataMap dataMap = jobexecutioncontext.getJobDetail().getJobDataMap();
		CcVersion ccVersion = (CcVersion) dataMap.get("ccVersion");
		Record allMessage = (Record) dataMap.get("allMessage");
		
		CcVersionService ccVersionService = SpringContextHolder.getBean(CcVersionService.class);
		ccVersionService.adminCreate(ccVersion, allMessage);
	}

}
