package com.gnet.api;

import java.io.Serializable;

public class RequestHeader implements Serializable {

	private static final long serialVersionUID = -5498639102916146580L;

	private String token;//报文发送的允许认证
	private String trdate;//报文发送日期 格式 yyyy-MM-dd hh:mm:ss
	private String trcode;//请求方请求的交易码
	private String truser;//请求用户ID
	private String appseq;//流水号（随机唯一值，与请求报文流水号对应）
	private String version;//版本号

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTrdate() {
		return trdate;
	}

	public void setTrdate(String trdate) {
		this.trdate = trdate;
	}

	public String getTrcode() {
		return trcode;
	}

	public void setTrcode(String trcode) {
		this.trcode = trcode;
	}

	public String getTruser() {
		return truser;
	}

	public void setTruser(String truser) {
		this.truser = truser;
	}

	public String getAppseq() {
		return appseq;
	}

	public void setAppseq(String appseq) {
		this.appseq = appseq;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
