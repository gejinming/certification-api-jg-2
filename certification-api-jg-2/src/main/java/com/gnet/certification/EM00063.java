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
import com.jfinal.kit.StrKit;

/**
 * 修改课程性质某条信息
 * 
 * @author SY
 * @Date 2016年6月20日14:09:05
 */
@Service("EM00063")
public class EM00063 extends BaseApi implements IApi {

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
		CcCourseProperty ccCourseProperty = CcCourseProperty.dao.findById(propertyId);
		// name不能为空信息的过滤
		if (ccCourseProperty == null) {
			return renderFAIL("0112", response, header);
		}
		// name不能为重复信息的过滤
		if (CcCourseProperty.dao.isExisted(propertyName, ccCourseProperty.getStr("property_name"), ccCourseProperty.getLong("plan_id"))) {
			return renderFAIL("0113", response, header);
		}
		ccCourseProperty.set("modify_date", date);
		ccCourseProperty.set("property_name", propertyName);
		ccCourseProperty.set("remark", remark);
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		// 返回操作是否成功
		result.put("isSuccess", ccCourseProperty.update());
		return renderSUC(result, response, header);
	}
	
}
