package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseModule;
import com.jfinal.kit.StrKit;

/**
 * 编辑所属模块信息
 * 
 * @author xzl
 * 
 * @date 2016年7月4日
 *
 */
@Service("EM00333")
@Transactional(readOnly=false)
public class EM00333 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		String moduleName = paramsStringFilter(param.get("moduleName"));
		String remark = paramsStringFilter(param.get("remark"));
		Long sumGroupId = paramsLongFilter(param.get("sumGroupId"));
     
 		//moduleName不能为空的消息过滤
 		if(StrKit.isBlank(moduleName)){
 			return renderFAIL("0400", response, header);
 		}
		
		Date date = new Date();
		CcCourseModule courseModule = CcCourseModule.dao.findFilteredById(id);
		if(courseModule == null) {
			return renderFAIL("0403", response, header);
		}
		
		if(CcCourseModule.dao.isExisted(moduleName, courseModule.getLong("plan_id"), courseModule.getStr("module_name"))){
			return renderFAIL("0401", response, header);
		}
		
		courseModule.set("modify_date", date);
		courseModule.set("module_name", moduleName);
		courseModule.set("sum_group_id", sumGroupId);
		courseModule.set("remark", remark);
		
		Map<String, Object> result = new HashMap<>();
		result.put("isSuccess", courseModule.update());
		return renderSUC(result, response, header);
	}
	
}
