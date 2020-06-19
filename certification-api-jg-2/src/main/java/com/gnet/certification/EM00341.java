package com.gnet.certification;

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

/**
 * 查看培养计划学年学期详情
 * 
 * @author sll
 * 
 * @date 2016年07月04日 08:30:41
 *
 */
@Service("EM00341")
@Transactional(readOnly=true)
public class EM00341 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		
		if (id == null) {
			return renderFAIL("0350", response, header);
		}
		
		CcPlanTerm temp = CcPlanTerm.dao.findFilteredById(id);
		if(temp == null) {
			return renderFAIL("0351", response, header);
		}
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("id", temp.get("id"));
		map.put("createDate", temp.get("create_date"));
		map.put("modifyDate", temp.get("modify_date"));
		map.put("yearName", temp.get("year_name"));
		map.put("year", temp.get("year"));
		map.put("termName", temp.get("term_name"));
		map.put("term", temp.get("term"));
		map.put("termType", temp.get("term_type"));
		map.put("planId", temp.get("plan_id"));
		map.put("weekNums", temp.get("week_nums"));
		map.put("remark", temp.get("remark"));
		
		return renderSUC(map, response, header);
	}

}
