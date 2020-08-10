package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcTerm;

/**
 * 学年学期唯一性验证接口
 * 
 * @author sll
 *
 */
@Service("EM00355")
public class EM00355 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Integer startYear = paramsIntegerFilter(param.get("startYear"));
		Integer endYear = paramsIntegerFilter(param.get("endYear"));
		Integer term = paramsIntegerFilter(param.get("term"));
		Integer termType = paramsIntegerFilter(param.get("termType"));
		Long schoolId = paramsLongFilter(param.get("schoolId"));
		Long originValueId = paramsLongFilter(param.get("originValueId"));
		
		if (schoolId == null) {
			schoolId = UserCacheKit.getSchoolId(request.getHeader().getToken());
		}
		
		if (startYear == null) {
			return renderFAIL("0392", response, header);
		}
		if (endYear == null) {
			return renderFAIL("0393", response, header);
		}
		if (term == null) {
			return renderFAIL("0394", response, header);
		}
		if (termType == null) {
			return renderFAIL("0398", response, header);
		}
		
		if (schoolId == null) {
			return renderFAIL("0084", response, header);
		}
		
		Boolean isExists = CcTerm.dao.isExists(startYear, endYear, term, termType, schoolId, originValueId);
		
		Map<String, Object> result = new HashMap<>();
		result.put("isExists", isExists);
		
		return renderSUC(result, response, header);
	}

}
