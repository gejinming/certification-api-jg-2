package com.gnet.handle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

/**
 * 对于没有cookie的时候会传递url会带上sessionId导致action跳入404
 * <url>/sign_in;jsessionid=7ba49c313a84295770fecbd01e86f116166sc5feg5yhzwis9zayzx492</url>
 */
public class SessionIdHandler extends Handler {

    @Override
    public void handle(String target, HttpServletRequest request,
            HttpServletResponse response, boolean[] isHandled) {
        int place = target.indexOf(";");
        if (place != -1) {
            target = target.substring(0, place);
        }
        nextHandler.handle(target, request, response, isHandled);
    }
}
