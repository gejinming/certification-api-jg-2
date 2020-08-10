package com.gnet.certification;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.plugin.quartz.monitor.IQuartzMonitor;
import com.gnet.plugin.quartz.monitor.JFinalQuartzMonitorImpl;
import com.jfinal.kit.StrKit;

/**
 * 启动任务的接口
 * @author wct
 * @Date 2016年6月5日
 */
@Service("EM00031")
public class EM00031 extends BaseApi implements IApi {

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
		// 任务组名不能为空过滤
		if (StrKit.isBlank(group)) {
			return renderFAIL("0041", response, header);
		}
		
		if (name.indexOf("_") > -1) {
			name = name.split("_")[0] + "_job";
		}
		
		// 编辑参数处理
		IQuartzMonitor monitor = new JFinalQuartzMonitorImpl();

		boolean successFlag = monitor.resume(group, name);
		
		JSONObject result = new JSONObject();
		result.put("isSuccess", successFlag);
		return renderSUC(result, response, header);
	}

}
