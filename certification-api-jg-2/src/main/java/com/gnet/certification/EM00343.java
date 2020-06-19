package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcPlanTerm;
import com.jfinal.kit.StrKit;

/**
 * 编辑培养计划学年学期表
 * 
 * @author sll
 * 
 * @date 2016年07月04日 08:30:41
 *
 */
@Service("EM00343")
@Transactional(readOnly=false)
public class EM00343 extends BaseApi implements IApi{
	

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		String yearName = paramsStringFilter(param.get("yearName"));
		Integer year = paramsIntegerFilter(param.get("year"));
		String termName = paramsStringFilter(param.get("termName"));
		Integer term = paramsIntegerFilter(param.get("term"));
		Integer termType = paramsIntegerFilter(param.get("termType"));
		Long planId = paramsLongFilter(param.get("planId"));
		Integer weekNums = paramsIntegerFilter(param.get("weekNums"));
		String remark = paramsStringFilter(param.get("remark"));
		
		if (id == null) {
			return renderFAIL("0350", response, header);
		}
		if (StrKit.isBlank(yearName)) {
			return renderFAIL("0352", response, header);
		}
		if (year == null) {
			return renderFAIL("0353", response, header);
		}
		if (StrKit.isBlank(termName)) {
			return renderFAIL("0354", response, header);
		}
		if (term == null) {
			return renderFAIL("0355", response, header);
		}
		if (termType == null) {
			return renderFAIL("0356", response, header);
		}
		if (planId == null) {
			return renderFAIL("0357", response, header);
		}
		
		Date date = new Date();
		CcPlanTerm ccPlanTerm = CcPlanTerm.dao.findFilteredById(id);
		if(ccPlanTerm == null) {
			return renderFAIL("0341", response, header);
		}
		
		if (CcPlanTerm.dao.isExists(year, term, termType, id, planId) || CcPlanTerm.dao.isExists(yearName, termName, termType, id, planId)) {
			return renderFAIL("0780", response, header);
		}
		
		ccPlanTerm.set("modify_date", date);
		ccPlanTerm.set("year_name", yearName);
		ccPlanTerm.set("year", year);
		ccPlanTerm.set("term_name", termName);
		ccPlanTerm.set("term", term);
		ccPlanTerm.set("term_type", termType);
		ccPlanTerm.set("plan_id", planId);
		ccPlanTerm.set("week_nums", weekNums);
		ccPlanTerm.set("remark", remark);
		
		Map<String, Object> result = new HashMap<>();
		result.put("isSuccess", ccPlanTerm.update());
		return renderSUC(result, response, header);
	}
	
}
