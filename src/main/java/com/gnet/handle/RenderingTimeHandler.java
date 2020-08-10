package com.gnet.handle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gnet.plugin.configLoader.ConfigUtils;
import com.jfinal.handler.Handler;
import com.jfinal.log.Logger;

public class RenderingTimeHandler extends Handler {

    protected final Logger logger = Logger.getLogger(getClass());

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
    	if(ConfigUtils.getBoolean("global", "isDev")){
    		long start = System.currentTimeMillis();
            nextHandler.handle(target, request, response, isHandled);
            long end = System.currentTimeMillis();
            logger.info("URL:["+ target + "]\tTRENDING TIME:\t[" + (end - start) + "]ms");
    	}else{
    		nextHandler.handle(target, request, response, isHandled);
    	}
        
    }

}
