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
import com.gnet.model.admin.CcCourseHierarchy;
import com.jfinal.kit.StrKit;

/**
 * 新增课程层次某条信息
 * 
 * @author SY
 * @Date 2016年6月20日14:09:05
 */
@Service("EM00052")
public class EM00052 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 各种其他信息
		String name = paramsStringFilter(params.get("name"));
		String remark = paramsStringFilter(params.get("remark"));
        Long planId = paramsLongFilter(params.get("planId"));
		
		// planId不能为空信息的过滤
		if (planId == null) {
			return renderFAIL("0140", response, header);
		}
		// name不能为空信息的过滤
		if (StrKit.isBlank(name)) {
			return renderFAIL("0102", response, header);
		}
		// name不能重复的过滤
		if (CcCourseHierarchy.dao.isExisted(name, null, planId)) {
			return renderFAIL("0103", response, header);
		}
		
		Date date = new Date();
		CcCourseHierarchy ccCourseHierarchy = new CcCourseHierarchy();
		ccCourseHierarchy.set("create_date", date);
		ccCourseHierarchy.set("modify_date", date);
		ccCourseHierarchy.set("plan_id", planId);
		ccCourseHierarchy.set("name", name);
		ccCourseHierarchy.set("remark", remark);
		ccCourseHierarchy.set("is_del", Boolean.FALSE);
		
		// 保存这个信息
		Boolean saveResult = ccCourseHierarchy.save();
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		// 返回操作是否成功
		result.put("isSuccess", saveResult);
		return renderSUC(result, response, header);
	}
	
}
