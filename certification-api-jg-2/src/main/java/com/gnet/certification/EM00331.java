package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseModule;
import com.jfinal.kit.StrKit;

/**
 * 新增所属模块的某条信息
 * 
 * @author xzl
 * @Date 2016年7月2日
 */
@Service("EM00331")
public class EM00331 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 各种其他信息
		String moduleName = paramsStringFilter(params.get("moduleName"));
		String remark = paramsStringFilter(params.get("remark"));
        Long planId = paramsLongFilter(params.get("planId"));
        Long sumGroupId = paramsLongFilter(params.get("sumGroupId"));
		
		// planId不能为空信息的过滤
		if (planId == null) {
			return renderFAIL("0140", response, header);
		}
		//moduleName不能为空的消息过滤
		if(StrKit.isBlank(moduleName)){
			return renderFAIL("0400", response, header);
		}
		
		Date date = new Date();
		CcCourseModule courseModule = new CcCourseModule();
		courseModule.set("create_date", date);
		courseModule.set("modify_date", date);
		courseModule.set("module_name", moduleName);
		courseModule.set("plan_id", planId);
		courseModule.set("sum_group_id", sumGroupId);
		courseModule.set("remark", remark);
		courseModule.set("is_del", Boolean.FALSE);
		
		Map<String, Object> result = new HashMap<>();
		// 返回操作是否成功
		result.put("isSuccess", courseModule.save());
		
		return renderSUC(result, response, header);
	}
	
}
