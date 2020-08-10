package com.gnet.plugin.quartz;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.TriggerListener;
import org.quartz.impl.matchers.KeyMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;

import com.gnet.plugin.quartz.callback.ITaskMonitor;
import com.gnet.plugin.quartz.exception.ExistedJobException;
import com.gnet.plugin.quartz.exception.ExistedTriggerException;
import com.gnet.plugin.quartz.exception.NeedUnCompleteTriggerException;
import com.gnet.plugin.quartz.exception.NoExistedJobException;
import com.gnet.plugin.quartz.exception.NoExistedTriggerException;
import com.gnet.plugin.quartz.exception.NoInstanceSchedulerException;

/**
 * 任务调度工具类
 * 
 * @author xuq
 * @date 2015年11月12日
 * @version 2.0
 * @changelog 优化任务的相关操作功能
 */
public class QuartzKit {

	private static final Logger logger = Logger.getLogger(QuartzKit.class);
	
	static Scheduler scheduler;

	/**
	 * 获取触发器状态（所对应的工作线程的工作状态）
	 * 
	 * @param group 分组名
	 * @param triggerName 触发器名
	 * @return 触发器状态
	 * @throws NoExistedTriggerException 不存在触发器异常
	 * @throws SchedulerException 调度器异常
	 */
	public static TriggerState getTriggerState(String group, String triggerName) throws NoExistedTriggerException, SchedulerException {
		if (!checkTriggerExists(group, triggerName)) {
			throw new NoExistedTriggerException();
		}
		
		TriggerKey triggerKey = new TriggerKey(triggerName, group);
		return getScheduler().getTriggerState(triggerKey);
	}
	
	/**
	 * 获取触发器
	 * 
	 * @param group 分组
	 * @param triggerName 触发器名
	 * @return 触发器；如果没有这个触发器抛出触发器不存在异常，否则返回触发器实例
	 * @throws NoExistedTriggerException 不存在触发器异常
	 * @throws SchedulerException 调度器异常
	 */
	public static Trigger getTrigger(String group, String triggerName) throws NoExistedTriggerException, SchedulerException {
		if (!checkTriggerExists(group, triggerName)) {
			throw new NoExistedTriggerException();
		}
		
		TriggerKey triggerKey = new TriggerKey(triggerName, group);
		return getScheduler().getTrigger(triggerKey);
	}
	
	/**
	 * 获取工作
	 * 
	 * @param group 分组
	 * @param jobName 工作名
	 * @return 工作实例；如果没有这个工作抛出工作不存在异常，否则返回工作实例
	 * @throws NoExistedJobException 不存在工作异常
	 * @throws SchedulerException 调度器异常
	 */
	public static JobDetail getJobDetail(String group, String jobName) throws NoExistedJobException, SchedulerException {
		if (!checkJobExists(group, jobName)) {
			throw new NoExistedJobException();
		}
		
		JobKey jobKey = new JobKey(jobName, group);
		return getScheduler().getJobDetail(jobKey);
	}
	
	/**
	 * 创建工作
	 * 
	 * @see #createJob(String, String, Class, Map)
	 */
	public static JobDetail createJob(String name, Class<? extends Job> jobClass, Map<String, Object> jobData) {
		return createJob(name, null, jobClass, jobData);
	}
	
	/**
	 * 创建工作
	 * 
	 * @param name
	 *            工作名
	 * @param group
	 *            分组名
	 * @param jobClass
	 *            任务实现类
	 * @return 任务
	 */
	public static JobDetail createJob(String name, String group, Class<? extends Job> jobClass, Map<String, Object> jobData) {
		JobBuilder builder = JobBuilder.newJob(jobClass).withIdentity(name, group);
		if (jobData != null) {
			builder.usingJobData(new JobDataMap(jobData));
		}

		return builder.build();
	}

	/**
	 * 创建即时触发的触发器
	 * 
	 * @see #createTrigger(String, String, String, Date, ScheduleBuilder)
	 */
	public static <T extends Trigger> Trigger createTriggerStartNow(String name, String jobName, ScheduleBuilder<T> scheduleBuilder) {
		return createTrigger(name, jobName, new Date(), scheduleBuilder);
	}
	
	/**
	 * 创建触发器
	 * 
	 * @see #createTrigger(String, String, String, Date, ScheduleBuilder)
	 */
	public static <T extends Trigger> Trigger createTrigger(String name, String jobName, Date triggerDateTime, ScheduleBuilder<T> scheduleBuilder) {
		return createTrigger(name, null, jobName, triggerDateTime, scheduleBuilder);
	}

	/**
	 * 创建触发器
	 * 
	 * @param name
	 *            触发器名
	 * @parma group 分组名
	 * @param jobName
	 *            任务名
	 * @param triggerDateTime
	 *            开始触发日期（如果非现在开始）
	 * @param scheduleBuilder
	 *            触发规则
	 * @return 触发器
	 */
	public static <T extends Trigger> Trigger createTrigger(String name, String group, String jobName, Date triggerDateTime, ScheduleBuilder<T> scheduleBuilder) {
		TriggerBuilder<T> builder = TriggerBuilder.newTrigger().forJob(jobName).withIdentity(name, group).withSchedule(scheduleBuilder).startAt(triggerDateTime);
		return builder.build();
	}

	/**
	 * <p>
	 * 创建任务后立即开始触发
	 * </p>
	 * 
	 * @see #createTask(String, String, String, Class, Map, Date, Boolean, ScheduleBuilder, ITaskMonitor)
	 */
	public static <T extends Trigger> void createTaskStartNow(String name, Class<? extends Job> jobClass, Map<String, Object> jobData, ScheduleBuilder<T> scheduleBuilder, ITaskMonitor taskMonitor) throws SchedulerException {
		createTask(name + "_job", name + "_trigger", jobClass, jobData, new Date(), Boolean.FALSE, scheduleBuilder, taskMonitor);
	}

	/**
	 * @see #createTask(String, String, String, Class, Map, Date, Boolean, ScheduleBuilder, ITaskMonitor)
	 */
	public static <T extends Trigger> void createTask(String name, Class<? extends Job> jobClass, Map<String, Object> jobData, Date triggerDateTime, ScheduleBuilder<T> scheduleBuilder, ITaskMonitor taskMonitor) throws SchedulerException {
		createTask(name + "_job", name + "_trigger", jobClass, jobData, triggerDateTime, Boolean.FALSE, scheduleBuilder, taskMonitor);
	}
	
	/**
	 * <p>
	 * 创建任务后立即开始触发
	 * </p>
	 * 
	 * @see #createTask(String, String, String, Class, Map, Date, Boolean, ScheduleBuilder, ITaskMonitor)
	 */
	public static <T extends Trigger> void createTaskStartNow(String name, Class<? extends Job> jobClass, Map<String, Object> jobData, Boolean isOverride, ScheduleBuilder<T> scheduleBuilder, ITaskMonitor taskMonitor) throws SchedulerException {
		createTask(name + "_job", name + "_trigger", jobClass, jobData, new Date(), isOverride, scheduleBuilder, taskMonitor);
	}

	/**
	 * @see #createTask(String, String, String, Class, Map, Date, Boolean, ScheduleBuilder, ITaskMonitor)
	 * @version 2.0
	 */
	public static <T extends Trigger> void createTask(String name, Class<? extends Job> jobClass, Map<String, Object> jobData, Date triggerDateTime, Boolean isOverride, ScheduleBuilder<T> scheduleBuilder, ITaskMonitor taskMonitor) throws SchedulerException {
		createTask(name + "_job", name + "_trigger", jobClass, jobData, triggerDateTime, isOverride, scheduleBuilder, taskMonitor);
	}
	
	/**
	 * @see #createTask(String, String, String, Class, Map, Date, Boolean, ScheduleBuilder, ITaskMonitor)
	 * @version 2.0
	 */
	public static <T extends Trigger> void createTask(final String jobName, final String triggerName, final Class<? extends Job> jobClass, final Map<String, Object> jobData, final Date triggerDateTime, final Boolean isOverride, final ScheduleBuilder<T> scheduleBuilder, final ITaskMonitor taskMonitor) throws SchedulerException {
		createTask(jobName, triggerName, null, jobClass, jobData, triggerDateTime, isOverride, scheduleBuilder, taskMonitor);
	}
	
	/**
	 * <p>
	 * 创建任务<br/>
	 * 如果存在工作内容：<br/>
	 * <ol>
	 * <li>覆盖任务：重新创建工作内容，删除之前的工作内容</li>
	 * <li>不覆盖：获取之前创建的工作内容</li>
	 * </ol>
	 * 如果存在触发器：<br/>
	 * <ol>
	 * <li>如果存在对应的job，且工作内容不一致，则抛出{@link ExistedTriggerException}异常，不允许覆盖绑定其他工作内容的触发器</li>
	 * <li>如果存在对应的job，且工作内容所有一致，则对该触发器进行更新操作</li>
	 * <li>如果不存在对应的job，则对该触发器进行更新操作，并绑定至本工作内容</li>
	 * </ol>
	 * </p>
	 * 
	 * @param jobName
	 *            工作名
	 * @param triggerName
	 *            触发器名
	 * @param jobClass
	 *            任务关联类
	 * @param jobData
	 *            任务数据
	 * @param triggerDateTime
	 *            触发日期
	 * @param isOverride
	 *            是否覆盖已存在的任务
	 * @param scheduleBuilder
	 *            触发规则
	 * @throws SchedulerException
	 *             创建异常
	 * @version 2.0
	 * @changelog 优化创建策略
	 */
	public static <T extends Trigger> void createTask(final String jobName, final String triggerName, final String group, final Class<? extends Job> jobClass, final Map<String, Object> jobData, final Date triggerDateTime, final Boolean isOverride, final ScheduleBuilder<T> scheduleBuilder, final ITaskMonitor taskMonitor) throws SchedulerException {
		JobDetail jobDetail;
		Trigger trigger;
		
		if (checkJobExists(group, jobName)) {
			if (logger.isDebugEnabled()) {
				logger.debug("已经存在名为：" + jobName + "的job数据在" + group + "的分组中");
			}
			// is override
			if (isOverride) {
				// 删除之前的工作内容
				deleteJob(group, jobName);
				jobDetail = createJob(jobName, group, jobClass, jobData);
			} else {
				jobDetail = getScheduler().getJobDetail(new JobKey(jobName, group));
			}
		} else {
			jobDetail = createJob(jobName, group, jobClass, jobData);
		}
		if (checkTriggerExists(group, triggerName)) {
			if (logger.isDebugEnabled()) {
				logger.debug("已经存在名为：" + triggerName + "的触发器数据在" + group + "的分组中");
			}
			trigger = getScheduler().getTrigger(new TriggerKey(triggerName, group));
			if (trigger.getJobKey() != null) {
				JobDetail oldMappingJob = getScheduler().getJobDetail(trigger.getJobKey());
				if (oldMappingJob != null) {
					if (!oldMappingJob.equals(jobDetail)) {
						// 已经存在绑定其他任务的触发器，无法进行强行覆盖，抛出异常
						throw new ExistedTriggerException();
					} else {
						// 存在相同的触发器，删除已存在的，进行伪更新操作
						deleteTrigger(group, triggerName);
						trigger = createTrigger(triggerName, group, jobName, triggerDateTime, scheduleBuilder);
					}
				} else {
					// 存在未关联工作的Trigger，删除已存在的，进行伪更新操作
					deleteTrigger(group, triggerName);
					trigger = createTrigger(triggerName, group, jobName, triggerDateTime, scheduleBuilder);
				}
			}
		} else {
			trigger = createTrigger(triggerName, group, jobName, triggerDateTime, scheduleBuilder);
		}
		
		// TRIGGER 监听
		getScheduler().getListenerManager().addTriggerListener(new TriggerListener() {
			
			@Override
			public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
				return false;
			}
			
			@Override
			public void triggerMisfired(Trigger trigger) {
				if (taskMonitor != null) {
					taskMonitor.taskFail();
				}
			}
			
			@Override
			public void triggerFired(Trigger trigger, JobExecutionContext context) {
			}
			
			@Override
			public void triggerComplete(Trigger trigger, JobExecutionContext context, CompletedExecutionInstruction triggerInstructionCode) {
			}
			
			@Override
			public String getName() {
				return triggerName + "_listener";
			}
			
		}, KeyMatcher.keyEquals(new TriggerKey(triggerName, group)));
		
		// JOB 监听
		getScheduler().getListenerManager().addJobListener(new JobListener() {
			
			@Override
			public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
				if (taskMonitor != null) {
					taskMonitor.taskFinish();
				}
			}
			
			@Override
			public void jobToBeExecuted(JobExecutionContext context) {
				if (taskMonitor != null) {
					taskMonitor.taskStart();
				}
			}
			
			@Override
			public void jobExecutionVetoed(JobExecutionContext context) {
			}
			
			@Override
			public String getName() {
				return jobName + "_listener";
			}
			
		}, KeyMatcher.keyEquals(trigger.getJobKey()));
		
		getScheduler().scheduleJob(jobDetail, trigger);
	}
	
	/**
	 * 暂停触发器
	 * 
	 * @param group 分组名
	 * @param triggerName 触发器名
	 * @throws NoExistedTriggerException 不存在改触发器错误
	 * @throws SchedulerException 调度器错误
	 */
	public static void pauseTrigger(String group, String triggerName) throws NoExistedTriggerException, SchedulerException {
		if (!checkTriggerExists(group, triggerName)) {
			throw new NoExistedTriggerException();
		}
		
		TriggerKey triggerKey = new TriggerKey(triggerName, group);
		getScheduler().pauseTrigger(triggerKey);
	}
	
	/**
	 * 暂停工作
	 * 
	 * @param group 分组名
	 * @param jobName 工作名
	 * @throws NoExistedTriggerException 不存在改触发器错误
	 * @throws SchedulerException 调度器异常
	 */
	@SuppressWarnings("unchecked")
	public static void pauseJob(String group, String jobName) throws NoExistedJobException, SchedulerException {
		if (!checkJobExists(group, jobName)) {
			throw new NoExistedJobException();
		}
		
		JobKey jobKey = new JobKey(jobName, group);
		List<Trigger> triggers = (List<Trigger>) getScheduler().getTriggersOfJob(jobKey);
		if (triggers.size() > 0) {
			// 停止触发器
			for (Trigger trigger : triggers) {
				TriggerKey triggerKey = trigger.getKey();
				getScheduler().pauseTrigger(triggerKey);
			}
		}
		getScheduler().pauseJob(jobKey);
	}
	
	/**
	 * 恢复触发器工作
	 * 
	 * @param group 分组名
	 * @param triggerName 触发器名
	 * @throws NoExistedTriggerException 不存在触发器异常
	 * @throws SchedulerException 调度器异常
	 */
	public static void resumeTrigger(String group, String triggerName) throws NoExistedTriggerException, SchedulerException {
		if (!checkTriggerExists(group, triggerName)) {
			throw new NoExistedTriggerException();
		}
		
		TriggerKey triggerKey = new TriggerKey(triggerName, group);
		getScheduler().resumeTrigger(triggerKey);
	}
	
	/**
	 * 立刻恢复执行该工作，之后按照正常Trigger触发进行
	 * 
	 * @param group 分组
	 * @param jobName 工作名
	 * @throws NoExistedJobException 不存在该工作异常
	 * @throws SchedulerException 调度器异常
	 */
	public static void resumeJob(String group, String jobName) throws NoExistedJobException, SchedulerException {
		if (!checkJobExists(group, jobName)) {
			throw new NoExistedJobException();
		}
		
		JobKey jobKey = new JobKey(jobName, group);
		getScheduler().resumeJob(jobKey);
	}
	
	/**
	 * 更新简单触发器
	 * 
	 * @param group 分组
	 * @param triggerName 触发器名
	 * @param startTime 开始时间
	 * @param nextFireTime 下次触发时间
	 * @param priority 优先级
	 * @param repeatCount 重复次数
	 * @param repeatInterval 重复间隔
	 * @return 是否更新成功
	 * @throws NoExistedTriggerException 不存在该触发器异常
	 * @throws NeedUnCompleteTriggerException 需要未完成的触发器异常
	 * @throws SchedulerException 调度器异常
	 */
	public static boolean updateSimpleTrigger(String group, String triggerName, Date startTime, Date nextFireTime, Integer priority, Integer repeatCount, Integer repeatInterval) throws NoExistedTriggerException, NeedUnCompleteTriggerException, SchedulerException {
		// validate update info
		if (startTime == null && nextFireTime == null && priority == null && repeatCount == null && repeatInterval == null) {
			return false;
		}
		
		if (!checkTriggerExists(group, triggerName)) {
			throw new NoExistedTriggerException();
		}
		
		TriggerKey triggerKey = new TriggerKey(triggerName, group);
		Trigger trigger = getScheduler().getTrigger(triggerKey);
		TriggerState triggerState = getScheduler().getTriggerState(triggerKey);
		
		if (TriggerState.COMPLETE.equals(triggerState) || TriggerState.ERROR.equals(triggerState)) {
			throw new NeedUnCompleteTriggerException();
		}
		
		if (TriggerState.BLOCKED.equals(triggerState) || TriggerState.NORMAL.equals(triggerState)) {
			getScheduler().pauseTrigger(triggerKey);
		}
		
		if (trigger instanceof SimpleTriggerImpl) {
			SimpleTriggerImpl simpleTriggerImpl = (SimpleTriggerImpl) trigger;
			if (nextFireTime != null) {
				simpleTriggerImpl.setNextFireTime(nextFireTime);
			}
			if (priority != null) {
				simpleTriggerImpl.setPriority(priority);
			}
			if (repeatCount != null) {
				simpleTriggerImpl.setRepeatCount(repeatCount);
			}
			if (repeatInterval != null) {
				simpleTriggerImpl.setRepeatInterval(repeatInterval);
			}
			if (startTime != null) {
				simpleTriggerImpl.setStartTime(startTime);
			}
			
			// 重新关联job，并恢复运行状态
			getScheduler().rescheduleJob(triggerKey, simpleTriggerImpl);
			
			return true;
		}
		throw new NoExistedTriggerException();
	}
	
	/**
	 * 更新Cron表达式触发器
	 * 
	 * @param group 分组
	 * @param triggerName 触发器名
	 * @param cronExpression 表达式
	 * @return 是否更新成功
	 * @throws NoExistedTriggerException 不存在触发器异常
	 * @throws NeedUnCompleteTriggerException 需要未完成触发器异常
	 * @throws SchedulerException 调度器异常
	 * @throws ParseException 表达式解析异常
	 */
	public static boolean updateCronTrigger(String group, String triggerName, String cronExpression) throws NoExistedTriggerException, NeedUnCompleteTriggerException, SchedulerException, ParseException {
		// validate update info
		if (cronExpression == null || cronExpression.trim() == "") {
			return false;
		}
		
		if (!checkTriggerExists(group, triggerName)) {
			throw new NoExistedTriggerException();
		}
		
		TriggerKey triggerKey = new TriggerKey(triggerName, group);
		Trigger trigger = getScheduler().getTrigger(triggerKey);
		TriggerState triggerState = getScheduler().getTriggerState(triggerKey);
		
		if (TriggerState.COMPLETE.equals(triggerState) || TriggerState.ERROR.equals(triggerState)) {
			throw new NeedUnCompleteTriggerException();
		}
		
		if (TriggerState.BLOCKED.equals(triggerState) || TriggerState.NORMAL.equals(triggerState)) {
			getScheduler().pauseTrigger(triggerKey);
		}
		
		if (trigger instanceof CronTriggerImpl) {
			CronTriggerImpl cronTriggerImpl = (CronTriggerImpl) trigger;
			cronTriggerImpl.setCronExpression(cronExpression);
			
			// 重新关联job，并恢复运行状态
			getScheduler().rescheduleJob(triggerKey, cronTriggerImpl);
			
			return true;
		}
		throw new NoExistedTriggerException();
	}
	
	/**
	 * 删除触发器
	 * 
	 * @param group 分组
	 * @param triggerName 触发器名
	 * @throws NoExistedTriggerException 不存在该触发器异常
	 * @throws SchedulerException 调度器异常
	 */
	public static void deleteTrigger(String group, String triggerName) throws NoExistedTriggerException, SchedulerException {
		if (!checkTriggerExists(group, triggerName)) {
			throw new NoExistedTriggerException();
		}
		
		TriggerKey triggerKey = new TriggerKey(triggerName, group);
		getScheduler().pauseTrigger(triggerKey);
		getScheduler().unscheduleJob(triggerKey);
	}
	
	/**
	 * 删除工作<br/>
	 * 将会删除与此工作相关联的所有触发器<br/>
	 * 在删除之前，会停止触发器和工作的执行<br/>
	 * 在删除过程中，可能出现所对应的触发器部分删除失败的情况，会抛出异常
	 * 
	 * @param group 分组名
	 * @param jobName 工作名
	 * @throws NoExistedJobException 不存在该工作异常
	 * @throws SchedulerException 调度器异常
	 */
	@SuppressWarnings("unchecked")
	public static void deleteJob(String group, String jobName) throws NoExistedJobException, SchedulerException {
		if (!checkJobExists(group, jobName)) {
			throw new NoExistedJobException();
		}
		
		JobKey jobKey = new JobKey(jobName, group);
		List<Trigger> triggers = (List<Trigger>) getScheduler().getTriggersOfJob(jobKey);
		if (triggers.size() > 0) {
			// 停止触发器
			for (Trigger trigger : triggers) {
				TriggerKey triggerKey = trigger.getKey();
				getScheduler().pauseTrigger(triggerKey);
				getScheduler().unscheduleJob(triggerKey);
			}
		}
		
		getScheduler().deleteJob(jobKey);
	}

	/**
	 * 清理调度器
	 * 
	 * @throws SchedulerException
	 *             调度器异常
	 */
	public static void clearAll() throws SchedulerException {
		getScheduler().clear();
	}
	
	/**
	 * @see #checkJobExists(String, String)
	 */
	public static boolean checkJobExists(String jobName) throws SchedulerException {
		return checkJobExists(null, jobName);
	}
	
	/**
	 * 检查工作是否存在
	 * 
	 * @param jobName
	 *            工作名
	 * @return 是否存在
	 * @param group
	 * 			     组名
	 * @throws SchedulerException
	 *             调度器异常
	 */
	public static boolean checkJobExists(String group, String jobName) throws SchedulerException {
		return getScheduler().checkExists(new JobKey(jobName, group));
	}
	
	/**
	 * @see #checkTriggerExists(String, String)
	 */
	public static boolean checkTriggerExists(String triggerName) throws SchedulerException {
		return checkTriggerExists(null, triggerName);
	}

	/**
	 * 检查触发器是否存在
	 * 
	 * @param triggerName
	 *            触发器名
	 * @param group
	 * 			     组名
	 * @return 是否存在
	 * @throws SchedulerException
	 *             调度器异常
	 */
	public static boolean checkTriggerExists(String group, String triggerName) throws SchedulerException {
		return getScheduler().checkExists(new TriggerKey(triggerName, group));
	}
	
	/**
	 * 检查任务是否存在
	 * 
	 * @param group 组名
	 * @param taskName 任务名
	 * @return 是否存在
	 * @throws ExistedJobException 已经存在工作内容
	 * @throws ExistedTriggerException 已经存在触发器
	 * @throws SchedulerException 调度器异常
	 * @version 2.0
	 */
	public static boolean checkTaskExists(String group, String taskName) throws ExistedJobException, ExistedTriggerException, SchedulerException {
		if (checkTriggerExists(group, taskName + "_trigger")) {
			throw new ExistedTriggerException();
		}
		if (checkJobExists(group, taskName + "_job")) {
			throw new ExistedJobException();
		}
		return true;
	}
	
	/**
	 * 获得调度器
	 * 
	 * @return 调度器
	 * @throws NoInstanceSchedulerException
	 *             未实例化异常
	 */
	public static Scheduler getScheduler() throws NoInstanceSchedulerException {
		if (scheduler == null) {
			throw new NoInstanceSchedulerException();
		}
		return scheduler;
	}

	private QuartzKit() {
	}

}
