package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcStudentTransfer;

/**
 * 验证同一个人从A专业转入到B专业（或同一个人从A专业转出到B专业），不能录入2次
 * 
 * @author xzl
 * @Date 2016年7月27日
 */
@Service("EM00635")
public class EM00635 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		
		Long majorId = paramsLongFilter(param.get("majorId"));
		Integer type = paramsIntegerFilter(param.get("type"));
		String studentNo = paramsStringFilter(param.get("studentNo"));
		String originValue = paramsStringFilter(param.get("originValue"));
		
		if(majorId == null){
			return renderFAIL("0640", response, header);
		}
		
		if(type == null){
			return renderFAIL("0643", response, header);
		}
		
		if(studentNo == null){
			return renderFAIL("0332", response, header);
		}
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		result.put("isRepeat", CcStudentTransfer.dao.isRepeat(majorId, type, studentNo, originValue));
		return renderSUC(result, response, header);
	}

}
