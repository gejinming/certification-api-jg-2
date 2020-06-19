package com.gnet.job;

import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.gnet.api.kit.UserCacheKit;
import com.gnet.model.admin.User;

/**
 * 异步更新已登陆用户的缓存
 * 
 * @author xuq
 * @date 2016年6月7日
 * @version 1.0
 */
public class AsyncUpdateUserCache implements Job {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataMap = context.getMergedJobDataMap();
		Map<String, Object> userinfo = (Map<String, Object>) dataMap.get("userinfo");
		
		String token = (String) userinfo.get("token");
		User user = (User) userinfo.get("user");
		List<String> permissions = (List<String>) userinfo.get("permissions");
		List<String> roles = (List<String>) userinfo.get("roles");
		
		UserCacheKit.update(token, user, permissions, roles);
	}

}
