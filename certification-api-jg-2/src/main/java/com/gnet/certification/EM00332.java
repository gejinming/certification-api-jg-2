package com.gnet.certification;

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
/**
 * 查看所属模块的详情
 * 
 * @author xzl
 * 
 * @date 2016年7月4日
 *
 */
@Service("EM00332")
@Transactional(readOnly=true)
public class EM00332 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		
		if (id == null) {
			return renderFAIL("0402", response, header);
		}
		
		CcCourseModule temp = CcCourseModule.dao.findFilteredById(id);
		if(temp == null) {
			return renderFAIL("0403", response, header);
		}
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("id", temp.getLong("id"));
		map.put("createDate", temp.getDate("create_date"));
		map.put("modifyDate", temp.getDate("modify_date"));
		map.put("moduleName", temp.getStr("module_name"));
		map.put("planId", temp.getLong("plan_id"));
        map.put("sumGroupId", temp.getLong("sum_group_id"));
		map.put("remark", temp.getStr("remark"));
		
		return renderSUC(map, response, header);
	}

}
