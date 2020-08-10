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
 * 检查课程英文名字是否唯一
 * 
 * @author SY
 * @Date 2016年06月28日 14:26:40
 */
@Service("EM00177")
public class EM00177 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		Long planId = paramsLongFilter(params.get("planId"));
		String englishName = paramsStringFilter(params.get("englishName"));
		Long id = paramsLongFilter(params.get("id"));
		
		// 课程代码不能为空过滤
		if (StrKit.isBlank(englishName)) {
			return renderFAIL("0255", response, header);
		}
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		result.put("isExisted", CcCourse.dao.isExisted("english_name", englishName, planId, id));
		return renderSUC(result, response, header);
	}

}
