package com.gnet.plugin.push;

import java.util.Properties;

import com.gnet.plugin.configLoader.ConfigUtils;

/**
 * 推送
 * 
 * @author xuq
 * @date 2015年11月16日
 * @version 1.0
 */
public abstract class Push {

	protected Properties 	global;		// 全局推送配置
	
	protected Push() {
		this.global = ConfigUtils.getProps("push");
	}
	
	/**
	 * 推送
	 * 
	 * @param pushMonitor 推送监控
	 */
	public abstract void push(IPushMonitor pushMonitor);
	
}
