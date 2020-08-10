package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcMajor;
import com.jfinal.kit.StrKit;

/**
 * 检查专业名字是否唯一
 * 
 * @author SY
 * @Date 2016年6月29日22:17:55
 */
@Service("EM00087")
public class EM00087 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long instituteId = paramsLongFilter(params.get("instituteId"));
		String code = paramsStringFilter(params.get("code"));
		
		// 毕业要求序号不能为空过滤
		if (instituteId == null) {
			return renderFAIL("0138", response, header);
		}
		if (StrKit.isBlank(code)) {
			return renderFAIL("0151", response, header);
		}
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		result.put("isExisted", false);
		return renderSUC(result, response, header);
	}

}
