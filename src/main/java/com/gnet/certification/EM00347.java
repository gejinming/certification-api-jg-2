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
 * 培养计划学年名称学期名称学期类型唯一性验证
 * 
 * @author xzl
 *
 * @date 2016年11月11日19:38:01
 * 
 */
@Service("EM00347")
@Transactional(readOnly=true)
public class EM00347 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		if(param.containsKey("id") && id == null) {
			return renderFAIL("1009", response, header, "id的参数值非法");
		}
	    String yearName = paramsStringFilter(param.get("yearName"));
	    String termName = paramsStringFilter(param.get("termName"));
	    Integer termType = paramsIntegerFilter(param.get("termType"));
	    Long planId = paramsLongFilter(param.get("planId"));
	    
		if (yearName == null) {
			return renderFAIL("0352", response, header);
		}
		if (termName == null) {
			return renderFAIL("0354", response, header);
		}
		if(termType == null){
			return renderFAIL("0356", response, header);
		}
		if (planId == null) {
			return renderFAIL("0357", response, header);
		}
		
		Map<String, Object> result = Maps.newHashMap();
		result.put("isExists", CcPlanTerm.dao.isExists(yearName, termName, termType, id, planId));
		return renderSUC(result, response, header);
	}

}
