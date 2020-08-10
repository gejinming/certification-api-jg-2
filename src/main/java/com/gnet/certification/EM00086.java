package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import com.gnet.api.kit.UserCacheKit;
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
@Service("EM00086")
public class EM00086 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long majorId  = paramsLongFilter(params.get("id"));
		String name = paramsStringFilter(params.get("name"));
		Long schoolId = UserCacheKit.getSchoolId(request.getHeader().getToken());

		if(schoolId == null){
			return renderFAIL("0084", response, header);
		}

		if (StrKit.isBlank(name)) {
			return renderFAIL("0150", response, header);
		}
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		result.put("isExisted", CcMajor.dao.isExisted(schoolId, name, majorId));
		return renderSUC(result, response, header);
	}

}
