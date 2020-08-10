package com.gnet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import com.gnet.api.kit.CacheKit;
import com.gnet.handle.CORSHandler;
import com.gnet.handle.SessionIdHandler;
import com.gnet.model.DbModel;
import com.gnet.plugin.configLoader.ConfigLoader;
import com.gnet.plugin.configLoader.ConfigUtils;
import com.gnet.plugin.poi.ExcelPlugin;
import com.gnet.plugin.quartz.QuartzPlugin;
import com.gnet.plugin.route.AutoBindRoutes;
import com.gnet.plugin.setting.SettingPlugin;
import com.gnet.plugin.tablebind.AutoTableBindPlugin;
import com.gnet.plugin.tablebind.SimpleNameStyles;
import com.gnet.plugin.validate.Verifiable;
import com.gnet.utils.DictUtils;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.SqlReporter;
import com.jfinal.plugin.spring.SpringPlugin;
import com.jfinal.render.FreeMarkerRender;
import com.jfinal.render.JsonRender;

import jodd.util.PropertiesUtil;

/**
 * 项目主要配置部分
 */
public class JFApiConfig extends JFinalConfig {

	/**
	 * 常量配置
	 */
	@Override
	public void configConstant(Constants me) {
		// 载入配置
		ConfigLoader.load("classpath:config");
		// 开发模式
		me.setDevMode(ConfigUtils.getBoolean("global", "isDev"));
		// 增加JSON解析层数到15层
		JsonRender.setConvertDepth(15);
	}

	/**
	 * 路由配置
	 */
	@Override
	public void configRoute(Routes me) {
		me.add(new AutoBindRoutes().autoScan(false));
	}

	/**
	 * 全局拦截器
	 */
	@Override
	public void configInterceptor(Interceptors me) {
	}

	/**
	 * 配置处理器
	 */
	@Override
	public void configHandler(Handlers me) {
		me.add(new SessionIdHandler());
		if (ConfigUtils.getBoolean("cors", "open")) {
			me.add(new CORSHandler());
		}
	}

	/**
	 * 配置插件
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void configPlugin(Plugins me) {
		// 默认在 WEB-INF/application.xml
		SpringPlugin springPlugin = new SpringPlugin();
		springPlugin.start();
		AutoTableBindPlugin atbp = new AutoTableBindPlugin("mysql", (com.alibaba.druid.pool.DruidDataSource) SpringContextHolder.getBean("dataSource"), SimpleNameStyles.LOWER_UNDERLINE);
		atbp.addExcludeClasses(DbModel.class);
		atbp.addExcludeClasses(Verifiable.class);
		atbp.setShowSql(true);
		me.add(atbp);
		// sql记录
		SqlReporter.setLogger(true, 3);
		
		// setting plugin
		SettingPlugin settingPlugin = new SettingPlugin();
		me.add(settingPlugin);
		
		// quartz plugin
		QuartzPlugin quartzPlugin = new QuartzPlugin();
		me.add(quartzPlugin);
		
		// excel plugin
		ExcelPlugin excelPlugin = new ExcelPlugin();
		me.add(excelPlugin);
	}

	@Override
	public void afterJFinalStart() {
		DictUtils.init("certification.xml");
		FreeMarkerRender.setProperties(getProperties("freemarker.properties"));
		// 初始化缓存中的登录Mapping
		CacheKit.set("userinfo", "mapping_queue", new HashMap<Long, String>());
	}

	private Properties getProperties(String fileName) {
		try {
			return PropertiesUtil.createFromFile(PathKit.getRootClassPath() + File.separator + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
