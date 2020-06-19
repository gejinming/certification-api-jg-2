package com.gnet.plugin.quartz.monitor;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.ScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;

import com.gnet.plugin.quartz.obj.PageInfo;
import com.gnet.plugin.quartz.obj.TriggerWrapper;

/**
 * Quartz监控接口
 * 
 * @author xuq
 * @date 2015年12月18日
 * @version 1.0
 */
public interface IQuartzMonitor {
	
	static final String TABLE_TRIGGERS = "qrtz_triggers";
	static final String TABLE_JOB_DETAILS = "qrtz_job_details";
	
	static final String TABLE_BLOB_TRIGGERS = "qrtz_blob_triggers";
	static final String TABLE_CRON_TRIGGERS = "qrtz_cron_triggers";
	static final String TABLE_SIMPLE_TRIGGERS = "qrtz_simple_triggers";
	static final String TABLE_SIMPROP_TRIGGERS = "qrtz_simprop_triggers";
	
	static final String TABLE_FIRED_TRIGGERS = "qrtz_fired_triggers";
	static final String TABLE_PASUED_TRIGGER_GRPS = "qrtz_pasued_trigger_grps";
	
	static final String TABLE_SCHEDULER_STATE = "qrtz_scheduler_state";
	
	public TriggerWrapper getTrigger(String group, String triggerName);
	
	public JobDetail getJobDetail(String group, String jobName);
	
	public List<TriggerWrapper> getTriggers(PageInfo pageInfo, String triggerName, Date startDate, TriggerState state);
	
	public List<JobDetail> getJobDetails(Integer pageNumber, Integer pageSize);
	
	public Trigger addTrigger(String name, String group, String jobName, Date triggerDateTime, ScheduleBuilder<Trigger> scheduleBuilder);
	
	public JobDetail addJobDetail(String name, String group, Class<? extends Job> jobClass, Map<String, Object> jobData);
	
	public boolean pause(String group, String taskName);
	
	public boolean resume(String group, String taskName);
	
	public boolean remove(String group, String taskName);
	
	public boolean removeTrigger(String triggerName);
	
	public boolean removeJobDetail(String jobName);
	
	public boolean updateSimpleTrigger(String group, String triggerName, Date startTime, Date nextFireTime, Integer priority, Integer repeatCount, Integer repeatInterval);
	
	public boolean updateCronTrigger(String group, String triggerName, String cronExpression);
	
}
