package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseHierarchy;

/**
 * 查看课程层次某条信息
 * 
 * @author SY
 * @Date 2016年6月20日14:09:05
 */
@Service("EM00051")
public class EM00051 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据：id
		Long hierarchyId = paramsLongFilter(params.get("id"));
		// hierarchyId不能为空过滤
		if (hierarchyId == null) {
			return renderFAIL("0100", response, header);
		}
		// 通过id获取这条记录
		CcCourseHierarchy temp = CcCourseHierarchy.dao.findFilteredById(hierarchyId);
		if(temp == null) {
			return renderFAIL("0101", response, header);
		}
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("id", temp.getLong("id"));
		map.put("createDate", temp.getDate("create_date"));
		map.put("modifyDate", temp.getDate("modify_date"));
		map.put("name", temp.getStr("name"));
		map.put("planId", temp.getLong("plan_id"));
		map.put("remark", temp.getStr("remark"));
		
		return renderSUC(map, response, header);
	}
	
}
