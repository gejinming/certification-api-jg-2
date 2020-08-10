package com.gnet.plugin.pushMonitor.obj;

/**
 * 推送错误记录
 * 
 * @author xuq
 * @date 2015年12月30日
 * @version 1.0
 */
public class PushJobError extends PushObj {

	private static final long serialVersionUID = -7061033228812824615L;
	
	private Long pushJobId;
	private Long pushJobLogId;
	private String errorMsg;
	
	public Long getPushJobId() {
		return pushJobId;
	}
	public void setPushJobId(Long pushJobId) {
		this.pushJobId = pushJobId;
	}
	public Long getPushJobLogId() {
		return pushJobLogId;
	}
	public void setPushJobLogId(Long pushJobLogId) {
		this.pushJobLogId = pushJobLogId;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
}
