package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcTerm;

/**
 * 编辑学期表
 * 
 * @author sll
 * 
 * @date 2016年07月03日 17:31:09
 *
 */
@Service("EM00353")
@Transactional(readOnly=false)
public class EM00353 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		Integer startYear = paramsIntegerFilter(param.get("startYear"));
		Integer endYear = paramsIntegerFilter(param.get("endYear"));
		Integer term = paramsIntegerFilter(param.get("term"));
		Integer termType = paramsIntegerFilter(param.get("termType"));
		String token = request.getHeader().getToken();
		Long schoolId = UserCacheKit.getSchoolId(token);
		Integer sort = paramsIntegerFilter(param.get("sort"));
		String remark = paramsStringFilter(param.get("remark"));
		
		if (id == null) {
			return renderFAIL("0390", response, header);
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
			return renderFAIL("0395", response, header);
		}
		if(startYear > endYear){
			return renderFAIL("0397", response, header);
		}
		
		Date date = new Date();
		CcTerm ccTerm = CcTerm.dao.findFilteredById(id);
		if(ccTerm == null) {
			return renderFAIL("0391", response, header);
		}
		
		
		// 验证学年学期是否为空
		if (CcTerm.dao.isExists(startYear, endYear, term, termType, schoolId, id)) {
			return renderFAIL("0396", response, header);
		}
		
		ccTerm.set("modify_date", date);
		ccTerm.set("start_year", startYear);
		ccTerm.set("end_year", endYear);
		ccTerm.set("term", term);
		ccTerm.set("term_type", termType);
		ccTerm.set("school_id", schoolId);
		ccTerm.set("sort", sort);
		ccTerm.set("remark", remark);
		
		Map<String, Boolean> result = new HashMap<>();
		result.put("isSuccess", ccTerm.update());
		return renderSUC(result, response, header);
	}
	
}
