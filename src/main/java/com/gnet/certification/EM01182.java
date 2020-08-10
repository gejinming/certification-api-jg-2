package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseProperty;
import com.gnet.model.admin.CcCourseType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 查看课程类别某条信息
 * 
 * @author gjm
 * @Date 2020年5月25日14:09:05
 */
@Service("EM01182")
public class EM01182 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据：id
		Long id = paramsLongFilter(params.get("id"));
		// propertyId不能为空过滤
		if (id == null) {
			return renderFAIL("0110", response, header);
		}
		// 通过id获取这条记录
		CcCourseType ccCourseType = CcCourseType.dao.findFilteredById(id);
		if(ccCourseType == null) {
			return renderFAIL("0112", response, header);
		}
		
		// 结果返回
		Map<String, Object> map = new HashMap<>();
		map.put("id", ccCourseType.getLong("id"));
		map.put("planId", ccCourseType.getLong("plan_id"));
		map.put("typeValue", ccCourseType.getStr("type_value"));
		map.put("typeName", ccCourseType.getStr("type_name"));
		return renderSUC(map, response, header);
	}
	
}
