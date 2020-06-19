package com.gnet.generate.template;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSONArray;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.${moduleName};
import com.gnet.plugin.quartz.QuartzKit;
import com.gnet.plugin.quartz.callback.ITaskMonitor;
import com.jfinal.kit.JsonKit;

/**
 * 批量删除${moduleCName}
 * 
 * @author ${author}
 * 
 * @date ${currentDate?string("yyyy年MM月dd日 HH:mm:ss")}
 *
 */
@Service("${API_DELETE}")
@Transactional(readOnly=false)
public class ${API_DELETE} extends BaseApi implements IApi{
	
	private static final Logger logger = Logger.getLogger(${API_DELETE}.class);

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String,Boolean> result = new HashMap<>();
		
		Date date = new Date();
		
		List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		// 删除${moduleName}
		if(!${moduleName}.dao.deleteAll(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}	
	
	