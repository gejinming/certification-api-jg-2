package com.gnet.certification;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.Office;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;

/**
 * 部门代码是否存在判断接口
 * 
 * @author wct
 * @Date 2016年6月29日
 */
@Service("EM00036")
public class EM00036 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		String code = paramsStringFilter(params.get("code"));
		String originValue = paramsStringFilter(params.get("originValue"));
		
		// 编码为空过滤
		if (StrKit.isBlank(code)) {
			return renderFAIL("0050", response, header);
		}
		
		//结果返回
		Map<String, Object> result = Maps.newHashMap();
		result.put("isExisted", isExisted(code, originValue));
		return renderSUC(result, response, header);
	}
	
	/**
	 * 部门代码是否存在
	 * 
	 * @param code
	 * @return
	 */
	private boolean isExisted(String code, String originValue) {
		return Office.dao.isExisted(code, originValue);
	}

}
