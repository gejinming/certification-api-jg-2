package com.gnet.plugin.quartz;

import java.util.Properties;

import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;

import com.gnet.plugin.configLoader.ConfigUtils;
import com.jfinal.plugin.IPlugin;

public class QuartzPlugin implements IPlugin {
	
	public QuartzPlugin() {}

	@Override
	public boolean start() {
		try {
			Properties properties = ConfigUtils.getProps("quartz");
			SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory(properties);
			QuartzKit.scheduler = schedFact.getScheduler();
			QuartzKit.scheduler.start();
		} catch (SchedulerException e) {
			return false;
		}
		
		return true;
	}

	@Override
	public boolean stop() {
		try {
			QuartzKit.scheduler.shutdown(true);
		} catch (SchedulerException e) {
			try {
				while (QuartzKit.scheduler.isStarted()) {
					QuartzKit.scheduler.shutdown(true);
				}
			} catch (SchedulerException e1) {
				return false;
			}
		}
		return true;
	}

}
