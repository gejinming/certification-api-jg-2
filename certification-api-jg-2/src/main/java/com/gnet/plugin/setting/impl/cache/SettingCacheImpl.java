package com.gnet.plugin.setting.impl.cache;

import com.gnet.plugin.setting.impl.SettingImpl;

/**
 * 通用缓存配置实现
 * 
 * @author xuq
 * @date 2015年12月14日
 * @version 1.0
 */
public abstract class SettingCacheImpl extends SettingImpl implements ISettingCache {

	protected SettingCacheImpl(Boolean isGlobal, Long userId) {
		super(isGlobal, userId);
	}

	/**
	 * 获取缓存的key
	 * 
	 * @param region
	 * @param key
	 * @return
	 */
	protected String getKey(String region, String key) {
		if (isGlobal()) {
			return region + REGION_SPLIT + key;
		} else {
			return region + REGION_SPLIT + getUserId() + REGION_SPLIT + key;
		}
	}
	
}
