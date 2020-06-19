package com.gnet.certification;

import java.util.Date;
import java.util.Map;

import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.plugin.quartz.monitor.IQuartzMonitor;
import com.gnet.plugin.quartz.monitor.JFinalQuartzMonitorImpl;
import com.gnet.plugin.quartz.obj.TriggerWrapper;
import com.jfinal.kit.StrKit;

/**
 * 编辑普通的任务
 * @author wct
 * @Date 2016年6月5日
 */
@Service("EM00028")
public class EM00028 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		String name = paramsStringFilter(params.get("name"));
		String group = paramsStringFilter(params.get("group"));
		Date nextFireTime = paramsDateFilter(params.get("nextFireTime"));
		Integer priority = paramsIntegerFilter(params.get("priority"));
		Integer repeatCount = paramsIntegerFilter(params.get("repeatCount"));
		Integer repeatInterval = paramsIntegerFilter(params.get("repeatInterval"));
		// 任务名称不能为空过滤
		if (StrKit.isBlank(name)) {
			return renderFAIL("0040", response, header);
		}
		// 任务组别不能为空过滤
		if (StrKit.isBlank(group)) {
			return renderFAIL("0041", response, header);
		}
		
		// 更新参数处理
		IQuartzMonitor monitor = new JFinalQuartzMonitorImpl();
		TriggerWrapper triggerWrapper = monitor.getTrigger(group, name);
		// 任务为空返回
		if (triggerWrapper == null) {
			return renderFAIL("0042", response, header);
		}
		Trigger trigger = triggerWrapper.getTrigger();
		// 任务不是普通任务
		if (!(trigger instanceof SimpleTrigger)) {
			return renderFAIL("0043", response, header);
		}
		
		boolean successFlag = monitor.updateSimpleTrigger(group, name, null, nextFireTime, priority, repeatCount, repeatInterval);
		
		JSONObject result = new JSONObject();
		result.put("isSuccess", successFlag);
		return renderSUC(result, response, header);
	}

}
