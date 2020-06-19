package com.gnet.certification;

import java.util.Map;

import org.quartz.CronTrigger;
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
 * 编辑表达式任务的接口
 * @author wct
 * @Date 2016年6月5日
 */
@Service("EM00029")
public class EM00029 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		String name = paramsStringFilter(params.get("name"));
		String group = paramsStringFilter(params.get("group"));
		String cronExpression = paramsStringFilter(params.get("cronExpression"));
		// 任务名称不能为空过滤
		if (StrKit.isBlank(name)) {
			return renderFAIL("0040", response, header);
		}
		// 任务组名不能为空过滤
		if (StrKit.isBlank(group)) {
			return renderFAIL("0041", response, header);
		}
		
		// 编辑参数处理
		IQuartzMonitor monitor = new JFinalQuartzMonitorImpl();
		TriggerWrapper triggerWrapper = monitor.getTrigger(group, name);
		// 任务不存在过滤
		if (triggerWrapper == null) {
			return renderFAIL("0042", response, header);
		}
		Trigger trigger = triggerWrapper.getTrigger();
		// 任务不是表达式任务过滤
		if (!(trigger instanceof CronTrigger)) {
			return renderFAIL("0043", response, header);
		}
		CronTrigger cronTrigger = (CronTrigger) trigger;
		if (StrKit.isBlank(cronExpression)) {
			cronExpression = cronTrigger.getCronExpression();
		}
		
		boolean successFlag = monitor.updateCronTrigger(group, name, cronExpression);
		
		JSONObject result = new JSONObject();
		result.put("isSuccess", successFlag);
		return renderSUC(result, response, header);
	}

}
