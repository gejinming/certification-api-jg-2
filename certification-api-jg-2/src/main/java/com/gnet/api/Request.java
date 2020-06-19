package com.gnet.api;

import java.io.Serializable;
import java.util.Map;

@SuppressWarnings({"serial", "rawtypes"})
public class Request implements Serializable {

	public static final String POST = "POST";
	public static final String GET = "GET";
	
	private RequestHeader header;
	private Map data;
	private String requestIp;

	public RequestHeader getHeader() {
		return header;
	}

	public void setHeader(RequestHeader header) {
		this.header = header;
	}

	public Map getData() {
		return data;
	}

	public void setData(Map data) {
		this.data = data;
	}

	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

}
