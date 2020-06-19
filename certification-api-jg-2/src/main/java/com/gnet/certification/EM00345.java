package com.gnet.certification;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcPlanTerm;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;

/**
 * 培养计划学年学期唯一性验证
 * 
 * @author sll
 *
 * @date 2016年7月29日09:35:54
 * 
 */
@Service("EM00345")
@Transactional(readOnly=true)
public class EM00345 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Integer year = paramsIntegerFilter(param.get("year"));
		Integer term = paramsIntegerFilter(param.get("term"));
		String yearName = paramsStringFilter(param.get("yearName"));
		String termName = paramsStringFilter(param.get("termName"));
		Long planId = paramsLongFilter(param.get("planId"));
		Long planTermOriginValueId = paramsLongFilter(param.get("planTermOriginValueId"));
		
		if (year == null) {
			return renderFAIL("0353", response, header);
		}
		
		if (StrKit.notBlank(termName)) {
			return renderFAIL("0352", response, header);
		}
		
		if (term == null) {
			return renderFAIL("0355", response, header);
		}
		
		if (planId == null) {
			return renderFAIL("0357", response, header);
		}
		
		Map<String, Object> result = Maps.newHashMap();
		result.put("isExists", CcPlanTerm.dao.isExists(year, term, yearName, termName, planId, planTermOriginValueId));
		return renderSUC(result, response, header);
	}

}
