package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;


import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;

import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcEnrollment;

/**
 * 检查招生情况年份唯一性
 * 
 * @author xzl
 * @Date 2016年8月24日
 */
@Service("EM00435")
public class EM00435 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		Integer year = paramsIntegerFilter(params.get("year"));
		Integer originValue = paramsIntegerFilter(params.get("originValue")); 
		Long majorId = paramsLongFilter(params.get("majorId"));
		if(params.containsKey("majorId") && majorId == null) {
		    return renderFAIL("1009", response, header, "majorId的参数值非法");
		}
		
		if(year == null){
			return renderFAIL("0582", response, header);
		}
		if(majorId == null){
			return renderFAIL("0146", response, header);
		}
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		
		result.put("isExisted", CcEnrollment.dao.isExists(year, originValue, majorId));
		return renderSUC(result, response, header);
	}

}
