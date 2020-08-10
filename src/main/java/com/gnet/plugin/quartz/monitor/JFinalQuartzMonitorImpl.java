package com.gnet.plugin.quartz.monitor;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.ScheduleBuilder;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;

import com.gnet.plugin.quartz.QuartzKit;
import com.gnet.plugin.quartz.exception.NeedUnCompleteTriggerException;
import com.gnet.plugin.quartz.exception.NoExistedJobException;
import com.gnet.plugin.quartz.exception.NoExistedTriggerException;
import com.gnet.plugin.quartz.obj.OrderInfo;
import com.gnet.plugin.quartz.obj.PageInfo;
import com.gnet.plugin.quartz.obj.TriggerWrapper;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * <p>
 * JFinal Quartz 任务监控实现（非集群）<br/>
 * 通过缓存中的JOB进行监控，只能监控当前服务器上的Quartz数据，无法进行HTTP或者SOCKETS等等协议监控。<br/>
 * 在更新过程中，会通过Quartz自动持久化到数据库，非本工具进行数据库的实时操作。
 * </p>
 * 
 * @author xuq
 * @date 2015年12月18日
 * @version 1.0
 */
public class JFinalQuartzMonitorImpl implements IQuartzMonitor {

	private static final Logger logger = Logger.getLogger(JFinalQuartzMonitorImpl.class);
	
	@Override
	public TriggerWrapper getTrigger(String group, String triggerName) {
		try {
			Trigger trigger = QuartzKit.getTrigger(group, triggerName);
			TriggerState triggerState = QuartzKit.getTriggerState(group, triggerName);
			
			return new TriggerWrapper(trigger, triggerState);
		} catch (NoExistedTriggerException e) {
			if (logger.isErrorEnabled()) {
				logger.error(e.getMessage() + "\r\n返回NULL", e);
			}
			return null;
		} catch (SchedulerException e) {
			if (logger.isErrorEnabled()) {
				logger.error("获取触发器错误", e);
			}
			return null;
		}
	}

	@Override
	public JobDetail getJobDetail(String group, String jobName) {
		try {
			return QuartzKit.getJobDetail(group, jobName);
		} catch (NoExistedJobException e) {
			if (logger.isErrorEnabled()) {
				logger.error(e.getMessage() + "\r\n返回NULL", e);
			}
			return null;
		} catch (SchedulerException e) {
			if (logger.isErrorEnabled()) {
				logger.error("获取工作错误", e);
			}
			return null;
		}
	}

	@Override
	public List<TriggerWrapper> getTriggers(PageInfo pageInfo, String triggerName, Date startDate, TriggerState state) {
		List<TriggerWrapper> triggerWraps = new ArrayList<TriggerWrapper>();
		try {
			List<Object> params = new ArrayList<Object>();
			StringBuilder count = new StringBuilder("select count(1) ");
			StringBuilder list = new StringBuilder("select TRIGGER_NAME, TRIGGER_GROUP ");
			StringBuilder sql = new StringBuilder("from " + TABLE_TRIGGERS + " where SCHED_NAME=? ");
			params.add(QuartzKit.getScheduler().getSchedulerName());
			if (!StrKit.isBlank(triggerName)) {
				sql.append("and TRIGGER_NAME like '%" + triggerName + "%' ");
			}
			if (startDate != null) {
				sql.append("and START_TIME >= ? ");
				params.add(startDate.getTime());
			}
			if (state != null) {
				sql.append("and TRIGGER_STATE = ? ");
				params.add(state);
			}
			if (pageInfo != null) {
				OrderInfo[] orderInfos = pageInfo.getOrderInfos();
				if (orderInfos.length > 0) {
					sql.append("order by ");
					for (int i = 0; i < orderInfos.length; i++) {
						OrderInfo orderInfo = orderInfos[i];
						sql.append(orderInfo.getBy() + " " + orderInfo.getOrder().toString() + " ");
						if ((i + 1) != orderInfos.length) {
							sql.append(",");
						}
					}
				}
			}
			
			count.append(sql);
			list.append(sql);
			if (pageInfo != null) {
				list.append("limit " + pageInfo.getPageSize() * (pageInfo.getPageNumber() - 1) + ", " + pageInfo.getPageSize() + " ");
			}
			
			Long countNum = Db.queryLong(count.toString(), params.toArray());
			List<Record> simpleTriggers = Db.find(list.toString(), params.toArray());
			for (Record simpleTrigger : simpleTriggers) {
				TriggerWrapper triggerWrapper = getTrigger(simpleTrigger.getStr("TRIGGER_GROUP"), simpleTrigger.getStr("TRIGGER_NAME"));
				triggerWraps.add(triggerWrapper);
			}
			
			if (pageInfo != null) {
				Integer left = (int) (countNum % pageInfo.getPageSize());
				Integer totalPage = (int) (countNum / pageInfo.getPageSize()) + (left != 0 ? 1 : 0);
				pageInfo.setTotalPage(totalPage);
			}
		} catch (SchedulerException e) {
			if (logger.isErrorEnabled()) {
				logger.error("无法获取当前调度器的名字", e);
			}
		}
		return triggerWraps;
	}

	@Override
	public List<JobDetail> getJobDetails(Integer pageNumber, Integer pageSize) {
		List<JobDetail> jobDetails = new ArrayList<JobDetail>();
		try {
			List<Record> simpleJobDetails = Db.query("select JOB_NAME, JOB_GROUP from " + TABLE_JOB_DETAILS + " where SCHED_NAME=? ", QuartzKit.getScheduler().getSchedulerName());
			for (Record simpleJobDetail : simpleJobDetails) {
				jobDetails.add(getJobDetail(simpleJobDetail.getStr("JOB_GROUP"), simpleJobDetail.getStr("JOB_NAME")));
			}
		} catch (SchedulerException e) {
			if (logger.isErrorEnabled()) {
				logger.error("无法获取当前调度器的名字", e);
			}
		}
		return jobDetails;
	}

	@Override
	public Trigger addTrigger(String name, String group, String jobName, Date triggerDateTime, ScheduleBuilder<Trigger> scheduleBuilder) {
		Trigger trigger = QuartzKit.createTrigger(name, group, jobName, triggerDateTime, scheduleBuilder);
		return trigger;
	}

	@Override
	public JobDetail addJobDetail(String name, String group, Class<? extends Job> jobClass, Map<String, Object> jobData) {
		return QuartzKit.createJob(name, group, jobClass, jobData);
	}

	@Override
	public boolean pause(String group, String taskName) {
		try {
			QuartzKit.pauseJob(group, taskName);
			return true;
		} catch (NoExistedJobException e) {
			if (logger.isErrorEnabled()) {
				logger.error("任务暂停：" + e.getMessage(), e);
			}
			return false;
		} catch (SchedulerException e) {
			if (logger.isErrorEnabled()) {
				logger.error("任务暂停：" + e.getMessage(), e);
			}
			return false;
		}
	}
	
	@Override
	public boolean resume(String group, String taskName) {
		try {
			QuartzKit.resumeJob(group, taskName);
			return true;
		} catch (NoExistedTriggerException e) {
			if (logger.isErrorEnabled()) {
				logger.error("任务恢复：" + e.getMessage(), e);
			}
			return false;
		} catch (SchedulerException e) {
			if (logger.isErrorEnabled()) {
				logger.error("任务恢复：" + e.getMessage(), e);
			}
			return false;
		}
	}

	@Override
	public boolean remove(String group, String taskName) {
		try {
			QuartzKit.deleteJob(group, taskName + "_job");
			return true;
		} catch (NoExistedJobException e) {
			if (logger.isErrorEnabled()) {
				logger.error("任务删除：" + e.getMessage(), e);
			}
			return false;
		} catch (SchedulerException e) {
			if (logger.isErrorEnabled()) {
				logger.error("任务删除：" + e.getMessage(), e);
			}
			return false;
		}
	}

	@Override
	public boolean removeTrigger(String triggerName) {
		return false;
	}

	@Override
	public boolean removeJobDetail(String jobName) {
		return false;
	}

	@Override
	public boolean updateSimpleTrigger(String group, String triggerName, Date startTime, Date nextFireTime, Integer priority, Integer repeatCount, Integer repeatInterval) {
		try {
			return QuartzKit.updateSimpleTrigger(group, triggerName, startTime, nextFireTime, priority, repeatCount, repeatInterval);
		} catch (NoExistedTriggerException e) {
			if (logger.isErrorEnabled()) {
				logger.error("任务更新：" + e.getMessage(), e);
			}
			return false;
		} catch (NeedUnCompleteTriggerException e) {
			if (logger.isErrorEnabled()) {
				logger.error("任务更新：" + e.getMessage(), e);
			}
			return false;
		} catch (SchedulerException e) {
			if (logger.isErrorEnabled()) {
				logger.error("任务更新：" + e.getMessage(), e);
			}
			return false;
		}
	}
	
	@Override
	public boolean updateCronTrigger(String group, String triggerName, String cronExpression) {
		try {
			return QuartzKit.updateCronTrigger(group, triggerName, cronExpression);
		} catch (NoExistedTriggerException e) {
			if (logger.isErrorEnabled()) {
				logger.error("任务更新：" + e.getMessage(), e);
			}
			return false;
		} catch (NeedUnCompleteTriggerException e) {
			if (logger.isErrorEnabled()) {
				logger.error("任务更新：" + e.getMessage(), e);
			}
			return false;
		} catch (SchedulerException e) {
			if (logger.isErrorEnabled()) {
				logger.error("任务更新：" + e.getMessage(), e);
			}
			return false;
		} catch (ParseException e) {
			if (logger.isErrorEnabled()) {
				logger.error("任务更新：" + e.getMessage(), e);
			}
			return false;
		}
	}

}
