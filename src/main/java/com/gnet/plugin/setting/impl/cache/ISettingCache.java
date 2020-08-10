package com.gnet.plugin.setting.impl.cache;

import com.gnet.plugin.setting.ISetting;

/**
 * 缓存配置接口
 * 
 * @author xuq
 * @date 2015年12月14日
 * @version 1.0
 */
public interface ISettingCache extends ISetting {

	static final String REGION = "setting";
	
	static final String REGION_SPLIT = "-";
	
}
