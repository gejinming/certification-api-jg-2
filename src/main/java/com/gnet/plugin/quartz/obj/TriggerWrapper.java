package com.gnet.plugin.quartz.obj;

import java.util.HashMap;
import java.util.Map;

import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;

/**
 * Trigger容器
 * 
 * @author xuq
 * @date 2015年12月18日
 * @version 1.0
 */
public class TriggerWrapper {

	private Trigger trigger;
	private TriggerState triggerState;
	private Map<String, Object> attrs;
	
	public TriggerWrapper(Trigger trigger, TriggerState triggerState) {
		this.trigger = trigger;
		this.triggerState = triggerState;
		
		// set attrs
		this.attrs = new HashMap<String, Object>();
		this.attrs.put("triggerStateName", getTriggerStateName(triggerState));
		this.attrs.put("schedule", getTriggerSchedule(trigger));
		this.attrs.put("isInit", TriggerState.NONE.equals(triggerState));
		this.attrs.put("isPaused", TriggerState.PAUSED.equals(triggerState));
		this.attrs.put("isRunning", TriggerState.NORMAL.equals(triggerState));
		this.attrs.put("isBlocked", TriggerState.BLOCKED.equals(triggerState));
		this.attrs.put("isError", TriggerState.ERROR.equals(triggerState));
	}

	public Trigger getTrigger() {
		return trigger;
	}

	public TriggerState getTriggerState() {
		return triggerState;
	}
	
	public Map<String, Object> getAttrs() {
		return attrs;
	}
	
	private String getTriggerStateName(TriggerState triggerState) {
		if (TriggerState.NONE.equals(triggerState)) {
			return "初始化";
		} else if (TriggerState.NORMAL.equals(triggerState)) {
			return "运行中";
		} else if (TriggerState.PAUSED.equals(triggerState)) {
			return "等待中";
		} else if (TriggerState.BLOCKED.equals(triggerState)) {
			return "阻塞中";
		} else if (TriggerState.COMPLETE.equals(triggerState)) {
			return "已完成";
		} else {
			// error
			return "发生错误";
		}
	}
	
	private String getTriggerSchedule(Trigger trigger) {
		StringBuilder result = new StringBuilder();
		if (trigger instanceof SimpleTriggerImpl) {
			SimpleTriggerImpl simpleTriggerImpl = (SimpleTriggerImpl) trigger;
			
			result.append(simpleTriggerImpl.getRepeatCount() == -1 ? "无限循环，" : "循环" + simpleTriggerImpl.getRepeatCount() + "次，");
			result.append("间隔" + simpleTriggerImpl.getRepeatInterval() + "毫秒");
		} else if (trigger instanceof CronTriggerImpl) {
			CronTriggerImpl cronTriggerImpl = (CronTriggerImpl) trigger;
			
			result.append(cronTriggerImpl.getCronExpression());
		} else {
			throw new RuntimeException("暂不支持该触发器类型规则转化");
		}
		return result.toString();
	}

}
