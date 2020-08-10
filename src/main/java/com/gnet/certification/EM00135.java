package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.School;

/**
 * 检查学校名是否唯一
 * 
 * @author zsf
 * @Date 2016年06月25日 18:39:35
 */
@Service("EM00135")
public class EM00135 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		String name = paramsStringFilter(params.get("name"));
		String originValue = paramsStringFilter(params.get("originValue"));
		
		// 学校名不能为空过滤
		if(StringUtils.isEmpty(name)) {
			return renderFAIL("0202", response, header);
		}
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		result.put("isExisted", School.dao.isExisted(name, originValue));
		return renderSUC(result, response, header);
	}

}
