package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCoursePropertySecondary;

/**
 * 查看次要课程性质某条信息
 * 
 * @author SY
 * @Date 2019年12月3日16:52:01
 */
@Service("EM01101")
public class EM01101 extends BaseApi implements IApi {

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
		CcCoursePropertySecondary ccCoursePropertySecondary = CcCoursePropertySecondary.dao.findFilteredById(propertyId);
		if(ccCoursePropertySecondary == null) {
			return renderFAIL("0112", response, header);
		}
		
		// 结果返回
		Map<String, Object> map = new HashMap<>();
		map.put("id", ccCoursePropertySecondary.getLong("id"));
		map.put("planId", ccCoursePropertySecondary.getLong("plan_id"));
		map.put("propertyName", ccCoursePropertySecondary.getStr("property_name"));
		map.put("createDate", ccCoursePropertySecondary.getDate("create_date"));
		map.put("modifyDate", ccCoursePropertySecondary.getDate("modify_date"));
		map.put("remark", ccCoursePropertySecondary.getStr("remark"));
		return renderSUC(map, response, header);
	}
	
}
