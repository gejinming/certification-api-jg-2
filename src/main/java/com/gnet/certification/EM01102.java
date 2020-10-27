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
import com.gnet.model.admin.CcCourseProperty;
import com.gnet.model.admin.CcCoursePropertySecondary;
import com.jfinal.kit.StrKit;

/**
 * 新增次要课程性质某条信息
 * 
 * @author SY
 * @Date 2019年12月9日13:25:49
 */
@Service("EM01102")
public class EM01102 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 各种其他信息
		String propertyName = paramsStringFilter(params.get("propertyName"));
		String remark = paramsStringFilter(params.get("remark"));
        Long planId = paramsLongFilter(params.get("planId"));
		// name不能为空信息的过滤
		if (StrKit.isBlank(propertyName)) {
			return renderFAIL("0111", response, header);
		}
		// name不能为重复信息的过滤
		if (CcCoursePropertySecondary.dao.isExisted(propertyName,null, planId)) {
			return renderFAIL("0113", response, header);
		}
		// planId不能为空信息的过滤
		if (planId == null) {
			return renderFAIL("0140", response, header);
		}
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		
		Date date = new Date();
		CcCoursePropertySecondary ccCoursePropertySecondary = new CcCoursePropertySecondary();
		ccCoursePropertySecondary.set("create_date", date);
		ccCoursePropertySecondary.set("modify_date", date);
		ccCoursePropertySecondary.set("plan_id", planId);
		ccCoursePropertySecondary.set("property_name", propertyName);
		ccCoursePropertySecondary.set("remark", remark);
		ccCoursePropertySecondary.set("is_del", Boolean.FALSE);
		Boolean isSuccess = ccCoursePropertySecondary.save();
		// 返回操作是否成功
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}
	
}
