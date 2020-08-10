package com.gnet.certification;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.object.QrtzTriggersType;
import com.gnet.pager.Pageable;
import com.gnet.plugin.quartz.monitor.IQuartzMonitor;
import com.gnet.plugin.quartz.monitor.JFinalQuartzMonitorImpl;
import com.gnet.plugin.quartz.obj.OrderInfo;
import com.gnet.plugin.quartz.obj.PageInfo;
import com.gnet.plugin.quartz.obj.TriggerWrapper;
import com.jfinal.kit.StrKit;

/**
 * 查看任务列表接口
 * @author wct
 * @Date 2016年6月4日
 */
@Service("EM00026")
public class EM00026 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		String triggerName = paramsStringFilter(params.get("triggerName"));
		Date startDate = paramsDateFilter(params.get("startDate"));
		String state = paramsStringFilter(params.get("state"));
		Integer pageNumber = paramsIntegerFilter(params.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(params.get("pageSize"));
		String orderProperty = paramsStringFilter(params.get("orderProperty"));
		String orderDirection = paramsStringFilter(params.get("orderDirection"));
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		TriggerState triggerState = null;
		if (StrKit.notBlank(state)) {
			triggerState = TriggerState.valueOf(state);
		}
		
		// 任务查询
		IQuartzMonitor monitor = new JFinalQuartzMonitorImpl();
		PageInfo pageInfo = null;
		// 判断是否分页
		if (pageable.isPaging()) {
			if (StrKit.notBlank(orderProperty)) {
				orderProperty = getRealOrderProperty(orderProperty);
				if (orderProperty == null) {
					return renderFAIL("0082", response, header);
				}
				OrderInfo orderInfo = OrderInfo.ORDER_ENUM.desc.toString().equals(orderDirection) ? OrderInfo.desc(orderProperty) : OrderInfo.asc(orderProperty);
				pageInfo = new PageInfo(pageable.getPageNumber(), pageable.getPageSize(), orderInfo);
			} else {
				pageInfo = new PageInfo(pageable.getPageNumber(), pageable.getPageSize());
			}
		}
		
		List<TriggerWrapper> triggers  = monitor.getTriggers(pageInfo, triggerName, startDate, triggerState);
		
		// 结果返回
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();
		for (TriggerWrapper triggerWrapper : triggers) {
			JSONObject task = new JSONObject();
			Trigger trigger = triggerWrapper.getTrigger();
			Map<String, Object> attrs = triggerWrapper.getAttrs();
			// 主键集
			JSONObject key = new JSONObject();
			key.put("name", trigger.getKey().getName());
			key.put("group", trigger.getKey().getGroup());
			task.put("key", key);
			// 触发属性
			task.put("triggerNextFireTime", trigger.getNextFireTime());
			task.put("triggerPreviousFireTime", trigger.getPreviousFireTime());
			task.put("triggerStartTime", trigger.getStartTime());
			task.put("triggerEndTime", trigger.getEndTime());
			task.put("triggerPriority", trigger.getPriority());
			if (trigger instanceof SimpleTrigger) {
				task.put("triggerTimesTriggered", ((SimpleTrigger) trigger).getTimesTriggered());
			}
			
			task.putAll(attrs);
			array.add(task);
		}
		result.put("tasks", array);
		result.put("searchTriggerName", triggerName);
		result.put("searchStartDate", startDate);
		result.put("searchState", state);
		if (pageable.isPaging()) {
			result.put("pageNumber", pageInfo.getPageNumber());
			result.put("pageSize", pageInfo.getPageSize());
			result.put("totalPage", pageInfo.getTotalPage());
		}
		
		return renderSUC(result, response, header);
	}
	
	/**
	 * 获得排序参数
	 * @param orderProperty
	 * @return
	 */
	private String getRealOrderProperty(String orderProperty) {
		QrtzTriggersType[] values = QrtzTriggersType.values();
		for (QrtzTriggersType value : values) {
			if (value.getKey().equals(orderProperty)) {
				return value.getValue();
			}
		}
		return null;
	}

}
