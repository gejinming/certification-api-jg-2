package com.gnet.plugin.quartz.exception;

import org.quartz.SchedulerException;

/**
 * 需要未完成的触发器
 * 
 * @author xuq
 * @date 2015年12月22日
 * @version 1.0
 */
public class NeedUnCompleteTriggerException extends SchedulerException {

	private static final long serialVersionUID = 2422336959266316528L;

	public NeedUnCompleteTriggerException() {
		super("需要未完成的触发器，已经触发完成的无法更新触发规则");
	}
	
}
