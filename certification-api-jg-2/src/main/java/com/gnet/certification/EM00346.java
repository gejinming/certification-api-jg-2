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

/**
 * 培养计划学年学期学期类型唯一性验证
 * 
 * @author xzl
 *
 * @date 2016年11月11日19:38:01
 * 
 */
@Service("EM00346")
@Transactional(readOnly=true)
public class EM00346 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		if(param.containsKey("id") && id == null) {
			return renderFAIL("1009", response, header, "id的参数值非法");
		}
		Integer year = paramsIntegerFilter(param.get("year"));
		Integer term = paramsIntegerFilter(param.get("term"));
	    Integer termType = paramsIntegerFilter(param.get("termType"));
		Long planId = paramsLongFilter(param.get("planId"));

		if (year == null) {
			return renderFAIL("0353", response, header);
		}
		if (term == null) {
			return renderFAIL("0355", response, header);
		}
		if(termType == null){
			return renderFAIL("0356", response, header);
		}
		if (planId == null) {
			return renderFAIL("0357", response, header);
		}
		
		Map<String, Object> result = Maps.newHashMap();
		result.put("isExists", CcPlanTerm.dao.isExists(year, term, termType, id, planId));
		return renderSUC(result, response, header);
	}

}
