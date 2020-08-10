package com.gnet.plugin.quartz.exception;

import org.quartz.SchedulerException;

/**
 * 无作业异常
 * 
 * @author xuq
 * @date 2015年12月21日
 * @version 1.0
 */
public class NoExistedTriggerException extends SchedulerException {

	private static final long serialVersionUID = 8858239506053397256L;

	public NoExistedTriggerException() {
		super("没有触发器存在");
	}
	
}
