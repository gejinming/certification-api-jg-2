package com.gnet.plugin.quartz.exception;

import org.quartz.SchedulerException;

/**
 * 触发器已经存在异常
 * 
 * @author xuq
 * @date 2015年11月12日
 * @version 1.0
 */
public class ExistedTriggerException extends SchedulerException {

	private static final long serialVersionUID = -4770133695049658493L;

	public ExistedTriggerException() {
		super("触发器已经存在");
	}
	
}
