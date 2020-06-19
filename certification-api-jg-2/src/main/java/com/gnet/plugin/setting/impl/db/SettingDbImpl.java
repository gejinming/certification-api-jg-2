package com.gnet.plugin.setting.impl.db;

import com.gnet.plugin.setting.impl.SettingImpl;

/**
 * 数据库配置实现抽象类
 * 
 * @author xuq
 * @date 2015年12月14日
 * @version 1.0
 */
public abstract class SettingDbImpl extends SettingImpl implements ISettingDb {
	
	protected SettingDbImpl(Boolean isGlobal, Long userId) {
		super(isGlobal, userId);
	}

	/**
	 * 获得表名
	 */
	protected String getTableName() {
		if (isGlobal()) {
			return TABLE_GLOBAL;
		} else {
			return TABLE_USER;
		}
	}
	
}
