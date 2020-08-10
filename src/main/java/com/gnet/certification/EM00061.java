package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseProperty;

/**
 * 查看课程性质某条信息
 * 
 * @author SY
 * @Date 2016年6月20日14:09:05
 */
@Service("EM00061")
public class EM00061 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据：id
		Long propertyId = paramsLongFilter(params.get("id"));
		// propertyId不能为空过滤
		if (propertyId == null) {
			return renderFAIL("0110", response, header);
		}
		// 通过id获取这条记录
		CcCourseProperty ccCourseProperty = CcCourseProperty.dao.findFilteredById(propertyId);
		if(ccCourseProperty == null) {
			return renderFAIL("0112", response, header);
		}
		
		// 结果返回
		Map<String, Object> map = new HashMap<>();
		map.put("id", ccCourseProperty.getLong("id"));
		map.put("planId", ccCourseProperty.getLong("plan_id"));
		map.put("propertyName", ccCourseProperty.getStr("property_name"));
		map.put("createDate", ccCourseProperty.getDate("create_date"));
		map.put("modifyDate", ccCourseProperty.getDate("modify_date"));
		map.put("remark", ccCourseProperty.getStr("remark"));
		return renderSUC(map, response, header);
	}
	
}
