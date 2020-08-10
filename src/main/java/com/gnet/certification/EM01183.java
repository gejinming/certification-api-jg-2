package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseProperty;
import com.gnet.model.admin.CcCourseType;
import com.jfinal.kit.StrKit;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 修改课程类别某条信息
 * 
 * @author SY
 * @Date 2016年6月20日14:09:05
 */
@Service("EM01183")
public class EM01183 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据
		Long id = paramsLongFilter(params.get("id"));
		String typeValue = paramsStringFilter(params.get("typeValue"));
		String typeName = paramsStringFilter(params.get("typeName"));
		// hierarchyId不能为空信息的过滤
		if (id == null) {
			return renderFAIL("2500", response, header);
		}
		// typeValue/typeName
		if (StrKit.isBlank(typeValue) || StrKit.isBlank(typeName)) {
			return renderFAIL("2501", response, header);
		}
		

		// 保存这个信息
		CcCourseType ccCourseType = CcCourseType.dao.findById(id);
		// name不能为空信息的过滤
		if (ccCourseType == null) {
			return renderFAIL("2502", response, header);
		}
		// name不能为重复信息的过滤
		if (CcCourseType.dao.isExistedName(typeName,ccCourseType.getStr("type_name"), ccCourseType.getLong("plan_id"))) {
			return renderFAIL("2503", response, header);
		}
		// 编号不能为重复信息的过滤
		if (CcCourseType.dao.isExisted(typeValue, ccCourseType.getStr("type_value"), ccCourseType.getLong("plan_id"))) {
			return renderFAIL("2504", response, header);
		}

		ccCourseType.set("type_name", typeName);
		ccCourseType.set("type_value", typeValue);
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		// 返回操作是否成功
		result.put("isSuccess", ccCourseType.update());
		return renderSUC(result, response, header);
	}
	
}
