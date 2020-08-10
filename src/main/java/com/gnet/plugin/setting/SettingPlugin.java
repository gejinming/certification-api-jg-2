package com.gnet.plugin.setting;

import com.jfinal.plugin.IPlugin;

/**
 * 配置插件<br/>
 * 载入全局配置
 * 
 * @author xuq
 * @date 2015年12月14日
 * @version 1.0
 */
public class SettingPlugin implements IPlugin {

	@Override
	public boolean start() {
		return SettingKit.init();
	}

	@Override
	public boolean stop() {
		return true;
	}

}
