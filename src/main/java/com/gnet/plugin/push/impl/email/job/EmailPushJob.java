package com.gnet.plugin.push.impl.email.job;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSONObject;
import com.gnet.plugin.push.IPushMonitor;
import com.gnet.plugin.push.Push;
import com.gnet.plugin.push.PushModel;
import com.gnet.plugin.push.db.JFinalJdbcPushImpl;
import com.gnet.plugin.push.impl.email.Email;
import com.gnet.plugin.push.impl.email.EmailPushImpl;
import com.gnet.plugin.setting.SettingKit;

/**
 * 邮箱推送任务
 * 
 * @author xuq
 * @date 2015年11月19日
 * @version 1.0
 */
public class EmailPushJob extends JFinalJdbcPushImpl {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		
		// 获取认证信息
		String address = SettingKit.getStr(cn.setting.Email.REGION, cn.setting.Email.EMAIL_ENUM.EMAIL_ADDRESS.getValue());
		String passwd = SettingKit.getStr(cn.setting.Email.REGION, cn.setting.Email.EMAIL_ENUM.EMAIL_PASSWD.getValue());
		
		Push push;
		Object emailObj = jobDataMap.get("email");
		if (emailObj instanceof List) {
			push = new EmailPushImpl((List<Email>) emailObj, address, passwd);
		} else {
			push = new EmailPushImpl((Email) emailObj, address, passwd);
		}
		final Long pushJobId = recordCreate(JSONObject.toJSONString(emailObj), new Date());
		
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
