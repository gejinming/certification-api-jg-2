package com.gnet.plugin.pushMonitor;

import java.util.Date;
import java.util.List;

import com.gnet.plugin.pushMonitor.obj.PageInfo;
import com.gnet.plugin.pushMonitor.obj.PushJob;
import com.gnet.plugin.pushMonitor.obj.PushJobError;
import com.gnet.plugin.pushMonitor.obj.PushJobLog;

/**
 * 推送数据监控
 * 
 * @author xuq
 * @date 2015年12月30日
 * @version 1.1
 */
public interface IPushDbMonitor {

	/**
	 * 监控数据分页
	 */
	public List<PushJob> watch(PageInfo pageInfo, Date startTime, Date endTime);

	/**
	 * 查看事件日志
	 */
	public List<PushJobLog> watchLog(Long pushJobId);
	
	/**
	 * 查看事件错误日志
	 */
	public List<PushJobError> watchError(Long pushJobId);
}
