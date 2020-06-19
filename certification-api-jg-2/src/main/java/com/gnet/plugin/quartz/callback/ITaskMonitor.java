package com.gnet.plugin.quartz.callback;

/**
 * Job监听
 * 
 * @author xuq
 * @date 2015年11月17日
 * @version 1.0
 */
public interface ITaskMonitor {

	/**
	 * 任务开始
	 */
	public void taskStart();
	
	/**
	 * 任务失败
	 */
	public void taskFail();
	
	/**
	 * 任务完成
	 */
	public void taskFinish();
	
}
