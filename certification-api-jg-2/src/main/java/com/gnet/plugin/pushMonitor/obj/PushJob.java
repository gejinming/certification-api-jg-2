package com.gnet.plugin.pushMonitor.obj;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 推送记录
 * 
 * @author xuq
 * @date 2015年12月30日
 * @version 1.2
 */
@SuppressWarnings("rawtypes")
public class PushJob extends PushObj {

	private static final long serialVersionUID = 6240478700380579296L;
	
	private Date startTime;
	private Date endTime;
	private BigDecimal progress;
	private List<Map> jobContent;
	
	private List<PushJobLog> pushJobLogs;
	
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public BigDecimal getProgress() {
		return progress;
	}
	public void setProgress(BigDecimal progress) {
		this.progress = progress;
	}
	public List<Map> getJobContent() {
		return jobContent;
	}
	public void setJobContent(List<Map> jobContent) {
		this.jobContent = jobContent;
	}
	public List<PushJobLog> getPushJobLogs() {
		return pushJobLogs;
	}
	public void setPushJobLogs(List<PushJobLog> pushJobLogs) {
		this.pushJobLogs = pushJobLogs;
	}
	
}
