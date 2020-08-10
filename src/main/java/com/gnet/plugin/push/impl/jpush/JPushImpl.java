package com.gnet.plugin.push.impl.jpush;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.gnet.plugin.configLoader.ConfigUtils;
import com.gnet.plugin.push.IPushMonitor;
import com.gnet.plugin.push.Push;
import com.gnet.plugin.push.impl.jpush.exception.PushFailException;
import com.gnet.plugin.push.kit.DecimalKit;
import com.jfinal.log.Logger;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;

/**
 * JPush推送到手机端具体实现
 * 
 * @author xuq
 * @date 2015年11月17日
 * @version 1.0
 */
public class JPushImpl extends Push {
	
	private static final Logger logger = Logger.getLogger(JPushImpl.class);
	
	private Properties 		jpushConfig;		// JPUSH全局配置
	
	private List<JPush> 	jPushs;				// 推送信息
	
	public JPushImpl(JPush jPush) {
		this(Arrays.asList(jPush));
	}
	
	public JPushImpl(List<JPush> jPushs) {
		super();
		this.jpushConfig = ConfigUtils.getProps("pushjpush");
		
		this.jPushs = jPushs;
	}

	@Override
	public void push(IPushMonitor monitor) {
		if (monitor != null) {
			monitor.pushStart();
		}
		JPushClient jpushClient = new JPushClient(jpushConfig.getProperty("appSecret"), jpushConfig.getProperty("appKey"), Integer.valueOf(jpushConfig.getProperty("push.maxRetry")));
		
		Integer delivered = 0;
		Integer allToDelivered = jPushs.size();
		for (JPush jPush : jPushs) {
			PushResult result;
			try {
				result = jpushClient.sendPush(jPush.getPushPayload());
			} catch (APIConnectionException | APIRequestException e) {
				if (logger.isErrorEnabled()) {
					logger.error(e.getMessage(), e);
				}
				if (monitor != null) {
					monitor.pushFail(jPush, e);
				}
				return;
			}
			if (result.getResponseCode() != 200) {
				// 存在错误
				if (monitor != null) {
					monitor.pushFail(jPush, new PushFailException("推送失败"));
				}
			}
			if (monitor != null) {
				monitor.pushProgress(jPush, DecimalKit.div(new BigDecimal(delivered), new BigDecimal(allToDelivered)));
			}
		}
		
		if (monitor != null) {
			monitor.pushFinish();
		}
	}
	
}
