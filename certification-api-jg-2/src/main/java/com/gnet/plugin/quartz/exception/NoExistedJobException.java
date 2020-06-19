package com.gnet.plugin.quartz.exception;

import org.quartz.SchedulerException;

/**
 * 无作业异常
 * 
 * @author xuq
 * @date 2015年12月21日
 * @version 1.0
 */
public class NoExistedJobException extends SchedulerException {

	private static final long serialVersionUID = 2422336959266316528L;

	public NoExistedJobException() {
		super("没有作业存在");
	}
	
}
