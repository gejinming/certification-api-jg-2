package com.gnet.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.springframework.stereotype.Component;

import com.gnet.job.impl.VersionCloseStatueUpdateForErrorJobImpl;
import com.gnet.job.impl.VersionCloseStatueUpdateJobImpl;
import com.gnet.job.impl.VersionDeleteLogJobImpl;
import com.gnet.model.admin.CcVersionCreateLog;
import com.gnet.model.admin.CcVersionDeleteLog;
import com.gnet.plugin.quartz.QuartzKit;

/**
 * @author SY
 * @date 2016年8月18日14:07:17
 */
@Component("ccVersionDeleteLogService")
public class CcVersionDeleteLogService {


	/**
	 * 创建：专业认证持续改进方案版本创建日志
	 * @param copyKey
	 * @param userId
	 * @param versionDeleteLogId
	 * @param majorId 
	 * @return
	 */
	public Boolean createJob(String copyKey, Long userId, Long versionDeleteLogId, Long majorId) {
		try{
			final String key = "closeVersionLog" + new Date().getTime();
			
			Map<String, Object> dataMap = new HashMap<>();
								dataMap.put("key", copyKey);
								dataMap.put("userId", userId);
								dataMap.put("id", versionDeleteLogId);
								dataMap.put("majorId", majorId);
			
			QuartzKit.createTaskStartNow(key, VersionDeleteLogJobImpl.class, dataMap, SimpleScheduleBuilder.simpleSchedule(), null);
		} catch (SchedulerException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 创建专业认证持续改进方案版本日志
	 * @param key
	 * @param userId
	 * @param id
	 * @param majorId 
	 * @return
	 */
	public Boolean create(String key, Long userId, Long id, Long majorId) {
		Date date = new Date();
		CcVersionDeleteLog ccVersionDeleteLog = new CcVersionDeleteLog();
		ccVersionDeleteLog.set("id", id);
		ccVersionDeleteLog.set("create_date", date);
		ccVersionDeleteLog.set("modify_date", date);
		ccVersionDeleteLog.set("schedule_key", key);
		ccVersionDeleteLog.set("delete_step", CcVersionCreateLog.STEP_NOT_START);
		ccVersionDeleteLog.set("delete_message", "刚开始删除！");
		ccVersionDeleteLog.set("delete_status", CcVersionDeleteLog.TYPE_STATUS_NOT_START);
		ccVersionDeleteLog.set("delete_person_id", userId);
		ccVersionDeleteLog.set("major_id", majorId);
		if(ccVersionDeleteLog.save()) {
			return true;
		}
		return false;
	}

	/**
	 * 更新：专业认证持续该警方按版本创建日志
	 * @param versionDeleteLogId
	 * @param deleteMessage
	 * 			(String)
	 * @param deleteStep
	 * @param isFinish 
	 */
	public Boolean changeStepJob(Long versionDeleteLogId, String deleteMessage, Integer deleteStep, Boolean isFinish) {
		try{
			final String key = "closeStepLog" + new Date().getTime();
			
			Map<String, Object> dataMap = new HashMap<>();
								dataMap.put("deleteMessage", deleteMessage);
								dataMap.put("deleteStep", deleteStep);
								dataMap.put("versionDeleteLogId", versionDeleteLogId);
								dataMap.put("isFinish", isFinish);
			
			QuartzKit.createTaskStartNow(key, VersionCloseStatueUpdateJobImpl.class, dataMap, SimpleScheduleBuilder.simpleSchedule(), null);
		} catch (SchedulerException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 复制操作的时候，出错了，修改步骤内容
	 * @param versionDeleteLogId
	 * @param deleteMessage
	 * @param deleteStep
	 */
	public Boolean changeStepJobForError(Long versionDeleteLogId, String deleteMessage, Integer deleteStep) {
		try{
			final String key = "closeStepErrorLog" + new Date().getTime();
			
			Map<String, Object> dataMap = new HashMap<>();
								dataMap.put("deleteMessage", deleteMessage);
								dataMap.put("deleteStep", deleteStep);
								dataMap.put("versionDeleteLogId", versionDeleteLogId);
			
			QuartzKit.createTaskStartNow(key, VersionCloseStatueUpdateForErrorJobImpl.class, dataMap, SimpleScheduleBuilder.simpleSchedule(), null);
		} catch (SchedulerException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 修改步骤内容
	 * @param id
	 * @param deleteMessage
	 * @param deleteStep
	 * @param isFinish 
	 */
	public Boolean changeStep(Long id, String deleteMessage, Integer deleteStep, Boolean isFinish) {
		Date date = new Date();
		if(isFinish){
			return CcVersionDeleteLog.dao.finishStep(id, date, deleteMessage, deleteStep);
		}else{
			return CcVersionDeleteLog.dao.updateStep(id, date, deleteMessage, deleteStep);
		}
	}
	
	/**
	 * 废弃操作的时候，出错了，修改步骤内容
	 * @param id
	 * @param deleteMessage
	 * @param deleteStep
	 */
	public Boolean changeStepForError(Long id, String deleteMessage, Integer deleteStep) {
		Date date = new Date();
		deleteMessage = "----步骤在：“" + deleteMessage + "”时候失败了！";
		return CcVersionDeleteLog.dao.updateStepForError(id, date, deleteMessage, deleteStep);
	}
	
}
