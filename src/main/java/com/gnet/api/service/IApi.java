package com.gnet.api.service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import javax.servlet.http.HttpServletRequest;


/**
 * @author cwledit
 *
 */
public interface IApi {
  /**
   * @param request
   * @param response
   * @param header
   * @param method
   * @return
   */
  public Response excute(Request request, Response response, ResponseHeader header, String method);
}
