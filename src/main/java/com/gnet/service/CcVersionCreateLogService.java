package com.gnet.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.springframework.stereotype.Component;

import com.gnet.job.impl.VersionCreateLogJobImpl;
import com.gnet.job.impl.VersionFinishLogJobImpl;
import com.gnet.job.impl.VersionStatueUpdateForErrorJobImpl;
import com.gnet.job.impl.VersionStatueUpdateJobImpl;
import com.gnet.model.admin.CcVersionCreateLog;
import com.gnet.plugin.quartz.QuartzKit;

/**
 * @author SY
 * @date 2016年7月20日16:11:20
 */
@Component("ccVersionCreateLogService")
public class CcVersionCreateLogService {


	/**
	 * 创建：专业认证持续改进方案版本创建日志
	 * @param copyKey
	 * @param userId
	 * @param versionCreateLogId
	 * @param majorId 
	 * @return
	 */
	public Boolean createJob(String copyKey, Long userId, Long versionCreateLogId, Long majorId) {
		try{
			final String key = "copyVersionLog" + new Date().getTime();
			
			Map<String, Object> dataMap = new HashMap<>();
								dataMap.put("key", copyKey);
								dataMap.put("userId", userId);
								dataMap.put("versionCreateLogId", versionCreateLogId);
								dataMap.put("majorId", majorId);
			
			QuartzKit.createTaskStartNow(key, VersionCreateLogJobImpl.class, dataMap, SimpleScheduleBuilder.simpleSchedule(), null);
		} catch (SchedulerException e) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * @param versionId
	 * @return
	 */
	public Boolean finishJob(Long versionCreateLogId) {
		try{
			final String key = "copyVersionLog" + new Date().getTime();
			
			Map<String, Object> dataMap = new HashMap<>();
								dataMap.put("versionCreateLogId", versionCreateLogId);
			
			QuartzKit.createTaskStartNow(key, VersionFinishLogJobImpl.class, dataMap, SimpleScheduleBuilder.simpleSchedule(), null);
		} catch (SchedulerException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 创建专业认证持续改进方案版本日志
	 * @param key
	 * @param userId
	 * @param versionCreateLogId
	 * @param majorId 
	 * @return
	 */
	public Boolean create(String key, Long userId, Long versionCreateLogId, Long majorId) {
		Date date = new Date();
		CcVersionCreateLog ccVersionCreateLog = new CcVersionCreateLog();
		ccVersionCreateLog.set("id", versionCreateLogId);
		ccVersionCreateLog.set("create_date", date);
		ccVersionCreateLog.set("modify_date", date);
		ccVersionCreateLog.set("schedule_key", key);
		ccVersionCreateLog.set("create_step", CcVersionCreateLog.STEP_NOT_START);
		ccVersionCreateLog.set("create_message", "刚开始创建！");
		ccVersionCreateLog.set("create_status", CcVersionCreateLog.TYPE_STATUS_NOT_START);
		ccVersionCreateLog.set("create_person_id", userId);
		ccVersionCreateLog.set("major_id", majorId);
		if(ccVersionCreateLog.save()) {
			return true;
		}
		return false;
	}

	/**
	 * 更新：专业认证持续该警方按版本创建日志
	 * @param versionCreateLogId
	 * @param createMessage
	 * 			(String)
	 * @param createStep
	 */
	public Boolean changeStepJob(Long versionCreateLogId, String createMessage, Integer createStep) {
		try{
			final String key = "copyStepLog" + new Date().getTime();
			
			Map<String, Object> dataMap = new HashMap<>();
								dataMap.put("createMessage", createMessage);
								dataMap.put("createStep", createStep);
								dataMap.put("versionCreateLogId", versionCreateLogId);
			
			QuartzKit.createTaskStartNow(key, VersionStatueUpdateJobImpl.class, dataMap, SimpleScheduleBuilder.simpleSchedule(), null);
		} catch (SchedulerException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 复制操作的时候，出错了，修改步骤内容
	 * @param versionCreateLogId
	 * @param createMessage
	 * @param createStep
	 */
	public Boolean changeStepJobForError(Long versionCreateLogId, String createMessage, Integer createStep) {
		try{
			final String key = "copyStepErrorLog" + new Date().getTime();
			
			Map<String, Object> dataMap = new HashMap<>();
								dataMap.put("createMessage", createMessage);
								dataMap.put("createStep", createStep);
								dataMap.put("versionCreateLogId", versionCreateLogId);
			
			QuartzKit.createTaskStartNow(key, VersionStatueUpdateForErrorJobImpl.class, dataMap, SimpleScheduleBuilder.simpleSchedule(), null);
		} catch (SchedulerException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 修改步骤内容
	 * @param id
	 * @param createMessage
	 * @param createStep
	 */
	public Boolean changeStep(Long id, String createMessage, Integer createStep) {
		Date date = new Date();
		return CcVersionCreateLog.dao.updateStep(id, date, createMessage, createStep);
	}
	
	/**
	 * 复制操作的时候，出错了，修改步骤内容
	 * @param id
	 * @param createMessage
	 * @param createStep
	 */
	public Boolean changeStepForError(Long id, String createMessage, Integer createStep) {
		Date date = new Date();
		createMessage = "----步骤在：“" + createMessage + "”时候失败了！";
		return CcVersionCreateLog.dao.updateStepForError(id, date, createMessage, createStep);
	}


	
	/**
	 * 版本创建成功设置日志的为成功
	 * @param versionCreateLogId
	 * @return
	 */
	public Boolean finish(Long versionCreateLogId) {
		Date date = new Date();
		return CcVersionCreateLog.dao.updateStepSuccess(versionCreateLogId, date);
		
	}

	
}
