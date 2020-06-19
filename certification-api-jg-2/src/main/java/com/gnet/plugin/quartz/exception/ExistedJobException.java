package com.gnet.plugin.quartz.exception;

import org.quartz.SchedulerException;

/**
 * 工作内容已经存在异常
 * 
 * @author xuq
 * @date 2016年1月21日
 * @version 2.0
 */
public class ExistedJobException extends SchedulerException {

	private static final long serialVersionUID = -4770133695049658493L;

	public ExistedJobException() {
		super("工作内容已经存在");
	}
	
}
