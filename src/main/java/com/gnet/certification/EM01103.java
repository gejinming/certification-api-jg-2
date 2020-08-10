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
import com.gnet.model.admin.CcCoursePropertySecondary;
import com.jfinal.kit.StrKit;

/**
 * 修改次要课程性质某条信息
 * 
 * @author SY
 * @Date 2019年12月9日13:37:33
 */
@Service("EM01103")
public class EM01103 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据
		Long propertyId = paramsLongFilter(params.get("id"));
		String propertyName = paramsStringFilter(params.get("propertyName"));
		String remark = paramsStringFilter(params.get("remark"));
		// hierarchyId不能为空信息的过滤
		if (propertyId == null) {
			return renderFAIL("0110", response, header);
		}
		// name不能为空信息的过滤
		if (StrKit.isBlank(propertyName)) {
			return renderFAIL("0111", response, header);
		}
		
		Date date = new Date();
		// 保存这个信息
		CcCoursePropertySecondary ccCoursePropertySecondary = CcCoursePropertySecondary.dao.findById(propertyId);
		// name不能为空信息的过滤
		if (ccCoursePropertySecondary == null) {
			return renderFAIL("0112", response, header);
		}
		// name不能为重复信息的过滤
		if (CcCoursePropertySecondary.dao.isExisted(propertyName, ccCoursePropertySecondary.getStr("property_name"), ccCoursePropertySecondary.getLong("plan_id"))) {
			return renderFAIL("0113", response, header);
		}
		ccCoursePropertySecondary.set("modify_date", date);
		ccCoursePropertySecondary.set("property_name", propertyName);
		ccCoursePropertySecondary.set("remark", remark);
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		// 返回操作是否成功
		result.put("isSuccess", ccCoursePropertySecondary.update());
		return renderSUC(result, response, header);
	}
	
}
