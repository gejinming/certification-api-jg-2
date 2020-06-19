package com.gnet.plugin.push;

import java.math.BigDecimal;

/**
 * 推送
 * 
 * @author xuq
 * @date 2015年11月16日
 * @version 1.0
 */
public interface IPushMonitor {
	
	/**
	 * 开始推送
	 */
	public void pushStart();
	
	/**
	 * 推送进度监控
	 * 
	 * @param email 邮件
	 * @param percentage 百分比
	 */
	public void pushProgress(PushModel pushModel, BigDecimal percentage);
	
	/**
	 * 推送失败
	 * 
	 * @param email 邮件
	 * @param t 异常信息
	 */
	public void pushFail(PushModel pushModel, Throwable t);
	
	/**
	 * 推送部分成功
	 * 
	 */
	public void pushPartily(PushModel pushModel);
	
	/**
	 * 推送完成
	 */
	public void pushFinish();
	
}
