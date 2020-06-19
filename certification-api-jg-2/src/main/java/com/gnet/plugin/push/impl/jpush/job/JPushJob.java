package com.gnet.plugin.push.impl.jpush.job;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.gnet.plugin.push.IPushMonitor;
import com.gnet.plugin.push.Push;
import com.gnet.plugin.push.PushModel;
import com.gnet.plugin.push.db.JFinalJdbcPushImpl;
import com.gnet.plugin.push.impl.jpush.JPush;
import com.gnet.plugin.push.impl.jpush.JPushImpl;

/**
 * 邮箱推送任务
 * 
 * @author xuq
 * @date 2015年11月19日
 * @version 1.0
 */
public class JPushJob extends JFinalJdbcPushImpl {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		
		Push push;
		Object jPushObj = jobDataMap.get("jPush");
		if (jPushObj instanceof List) {
			push = new JPushImpl((List<JPush>) jPushObj);
		} else {
			push = new JPushImpl((JPush) jPushObj);
		}
		final Long pushJobId = recordCreate(jPushObj.toString(), new Date());
		
		push.push(new IPushMonitor() {
			
			@Override
			public void pushStart() {
				recordStart(pushJobId, new Date());
			}
			
			@Override
			public void pushProgress(PushModel pushModel, BigDecimal percentage) {
				updateProgress(pushJobId, percentage, new Date());
			}
			
			@Override
			public void pushPartily(PushModel pushModel) {
				recordFail(pushJobId, new Exception("部分完成"), new Date());
			}
			
			@Override
			public void pushFinish() {
				recordFinish(pushJobId, new Date());
			}
			
			@Override
			public void pushFail(PushModel pushModel, Throwable t) {
				recordFail(pushJobId, t, new Date());
			}
			
		});
	}

}
