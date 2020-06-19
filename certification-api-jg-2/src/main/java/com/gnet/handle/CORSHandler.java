package com.gnet.handle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gnet.plugin.configLoader.ConfigUtils;
import com.jfinal.handler.Handler;

/**
 * 跨域请求Handler
 * 
 * @author xuq
 * @date 2016年6月3日
 * @version 1.0
 */
public class CORSHandler extends Handler {

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		response.setHeader("Access-Control-Allow-Origin", ConfigUtils.getStr("cors", "allowOrigin"));
        response.setHeader("Access-Control-Allow-Methods", ConfigUtils.getStr("cors", "allowMethods"));
        response.setHeader("Access-Control-Max-Age", ConfigUtils.getStr("cors", "maxAge"));
        response.setHeader("Access-Control-Allow-Headers", ConfigUtils.getStr("cors", "allowHeaders"));
        nextHandler.handle(target, request, response, isHandled);
	}
	
}
