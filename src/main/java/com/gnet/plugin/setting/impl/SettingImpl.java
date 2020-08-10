package com.gnet.plugin.setting.impl;

import com.gnet.plugin.setting.ISetting;

/**
 * 配置通用实现
 * 
 * @author xuq
 * @date 2015年12月14日
 * @version 1.0
 */
public abstract class SettingImpl implements ISetting {
	
	private Boolean 	isGlobal;		// 是否为全局配置
	private Long 		userId;			// 用户编号
	
	protected SettingImpl(Boolean isGlobal, Long userId) {
		// validate
		if ((isGlobal && userId != null) || (!isGlobal && userId == null)) {
			throw new RuntimeException("既不是全局配置级别也不是用户配置级别");
		}
		
		this.isGlobal = isGlobal;
		this.userId = userId;
	}

	/**
	 * 是否全局配置
	 * 
	 * @return
	 */
	protected boolean isGlobal() {
		return this.isGlobal;
	}
	
	/**
	 * 获取用户编号
	 * 
	 * @return
	 */
	protected Long getUserId() {
		return this.userId;
	}

}
