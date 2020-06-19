package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcVersion;
import com.gnet.response.ServiceResponse;
import com.gnet.service.CcVersionService;
import com.gnet.utils.SpringContextHolder;

/**
 * 验证新建版本启用年级是否符合要求
 * 
 * @author SY
 * @Date 2016年7月2日13:42:05
 */
@Service("EM00108")
public class EM00108 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		Integer type = paramsIntegerFilter(params.get("type"));
		Integer enableGrade = paramsIntegerFilter(params.get("enableGrade"));
		Long majorId = paramsLongFilter(params.get("majorId"));
		Long parentId = paramsLongFilter(params.get("parentId"));
		
		if (type == null) {
			return renderFAIL("0144", response, header);
		}
		if (enableGrade == null) {
			return renderFAIL("0154", response, header);
		}
		if (majorId == null) {
			return renderFAIL("0146", response, header);
		}
		
		
		CcVersionService ccVersionService = SpringContextHolder.getBean(CcVersionService.class);
		// 验证版本的起始年级是否符合要求
		ServiceResponse responseResult = ccVersionService.validateSaveEnableGrade(type, enableGrade, majorId, parentId);
		if(!responseResult.isSucc()) {
			if(CcVersion.TYPE_MAJOR_VERSION.equals(type)) {
				return renderFAIL("0155", response, header, responseResult.getContent());
			}
			return renderFAIL("0156", response, header);
		}
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		result.put("isSuccess", responseResult.isSucc());
		return renderSUC(result, response, header);
	}

}
