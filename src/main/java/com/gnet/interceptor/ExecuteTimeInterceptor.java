package com.gnet.interceptor;

import java.util.Date;

import com.gnet.plugin.configLoader.ConfigUtils;
import com.jfinal.core.ActionInvocation;
import com.jfinal.log.Logger;

public class ExecuteTimeInterceptor implements com.jfinal.aop.Interceptor {

	private static final Logger logger = Logger.getLogger("Execute Time Slow Output");

	@Override
	public void intercept(ActionInvocation ai) {
		if (ConfigUtils.getBoolean("global", "isDev")) {
			Long a = new Date().getTime();
			ai.invoke();
			Long b = new Date().getTime();
			Long internel = b - a;
			if (internel > 1000 && logger.isWarnEnabled()) {
				logger.warn("SLOW EXECUTE TIME: url[" + ai.getActionKey() + "]    executeTime[" + internel + "ms]");
			}
		} else {
			ai.invoke();
		}

	}

}
