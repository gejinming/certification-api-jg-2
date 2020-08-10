package com.gnet.job;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.gnet.api.kit.UserCacheKit;

/**
 * 异步更新已登陆用户的缓存
 * 
 * @author xuq
 * @date 2016年6月7日
 * @version 1.0
 */
public class AsyncDeleteUserCache implements Job {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataMap = context.getMergedJobDataMap();
		List<String> tokens = (List<String>) dataMap.get("tokens");
		// 逐个将其登出
		for (String token : tokens) {
			UserCacheKit.logout(token);
		}
	}

}
