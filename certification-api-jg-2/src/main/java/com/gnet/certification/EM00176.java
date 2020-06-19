package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourse;
import com.jfinal.kit.StrKit;

/**
 * 检查课程名字是否唯一
 * 
 * @author SY
 * @Date 2016年06月28日 14:26:40
 */
@Service("EM00176")
public class EM00176 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		Long planId = paramsLongFilter(params.get("planId"));
		String name = paramsStringFilter(params.get("name"));
		Long id = paramsLongFilter(params.get("id"));
		
		// 课程代码不能为空过滤
		if (StrKit.isBlank(name)) {
			return renderFAIL("0254", response, header);
		}
				
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		result.put("isExisted", CcCourse.dao.isExisted("name", name, planId, id));
		return renderSUC(result, response, header);
	}

}
