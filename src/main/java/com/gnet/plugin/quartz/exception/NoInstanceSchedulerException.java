package com.gnet.plugin.quartz.exception;

import org.quartz.SchedulerException;

/**
 * 调度器未实例化异常
 * 
 * @author xuq
 * @date 2015年11月12日
 * @version 1.0
 */
public class NoInstanceSchedulerException extends SchedulerException {

	private static final long serialVersionUID = -8245720127392795532L;
	
	public NoInstanceSchedulerException() {
		super("调度器未实例化");
	}

}
