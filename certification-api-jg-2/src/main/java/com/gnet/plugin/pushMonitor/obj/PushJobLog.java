package com.gnet.plugin.pushMonitor.obj;

import java.util.Date;

/**
 * 推送事件记录
 * 
 * @author xuq
 * @date 2015年12月30日
 * @version 1.1
 */
public class PushJobLog extends PushObj {

	private static final long serialVersionUID = -1197157528668668756L;

	private Long pushJobId;
	private Integer eventType;
	private Date eventTime;
	
	private PushJobError pushJobError;
	
	public Long getPushJobId() {
		return pushJobId;
	}
	public void setPushJobId(Long pushJobId) {
		this.pushJobId = pushJobId;
	}
	public Integer getEventType() {
		return eventType;
	}
	public void setEventType(Integer eventType) {
		this.eventType = eventType;
	}
	public Date getEventTime() {
		return eventTime;
	}
	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}
	public PushJobError getPushJobError() {
		return pushJobError;
	}
	public void setPushJobError(PushJobError pushJobError) {
		this.pushJobError = pushJobError;
	}
	
}
