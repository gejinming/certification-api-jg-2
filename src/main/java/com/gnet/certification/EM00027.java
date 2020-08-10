package com.gnet.certification;

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
 * 获取任务详情
 * @author wct
 * @Date 2016年6月5日
 */
@Service("EM00027")
public class EM00027 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		String name = paramsStringFilter(params.get("name"));
		String group = paramsStringFilter(params.get("group"));
		// 任务名称不能为空过滤
		if (StrKit.isBlank(name)) {
			return renderFAIL("0040", response, header);
		}
		// 任务分组不能为空过滤
		if (StrKit.isBlank(group)) {
			return renderFAIL("0041", response, header);
		}
		
		IQuartzMonitor monitor = new JFinalQuartzMonitorImpl();
		TriggerWrapper triggerWrapper = monitor.getTrigger(group, name);
		if (triggerWrapper == null) {
			return renderFAIL("0042", response, header);
		}
		
		//结果返回
		JSONObject result = new JSONObject();
		Trigger trigger = triggerWrapper.getTrigger();
		// 主键集
		JSONObject key = new JSONObject();
		key.put("name", trigger.getKey().getName());
		key.put("group", trigger.getKey().getGroup());
		result.put("key", key);
		// 触发属性
		result.put("triggerNextFireTime", trigger.getNextFireTime());
		result.put("triggerPreviousFireTime", trigger.getPreviousFireTime());
		result.put("triggerStartTime", trigger.getStartTime());
		result.put("triggerEndTime", trigger.getEndTime());
		result.put("triggerPriority", trigger.getPriority());
		if (trigger instanceof SimpleTrigger) {
			result.put("triggerTimesTriggered", ((SimpleTrigger) trigger).getTimesTriggered());
		}
		// 额外的属性
		result.putAll(triggerWrapper.getAttrs());
		
		return renderSUC(result, response, header);
	}

}
