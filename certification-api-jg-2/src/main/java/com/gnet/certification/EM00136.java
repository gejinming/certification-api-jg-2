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
 * 检查学校编码是否唯一
 * 
 * @author zsf
 * @Date 2016年06月25日 18:39:35
 */
@Service("EM00136")
public class EM00136 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		String code = paramsStringFilter(params.get("code"));
		String originValue = paramsStringFilter(params.get("originValue"));
		
		// 学校名不能为空过滤
		if(StringUtils.isEmpty(code)) {
			return renderFAIL("0203", response, header);
		}
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		result.put("isExisted", School.dao.isExistedOnCode(code, originValue));
		return renderSUC(result, response, header);
	}

}
