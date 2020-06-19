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
import com.gnet.model.admin.CcTerm;
import com.gnet.utils.DictUtils;

/**
 * 查看学期表详情
 * 
 * @author sll
 * 
 * @date 2016年07月03日 17:31:09
 *
 */
@Service("EM00351")
@Transactional(readOnly=true)
public class EM00351 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		
		if (id == null) {
			return renderFAIL("0390", response, header);
		}
		
		CcTerm temp = CcTerm.dao.findFilteredById(id);
		if(temp == null) {
			return renderFAIL("0391", response, header);
		}
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("id", temp.get("id"));
		map.put("createDate", temp.get("create_date"));
		map.put("modifyDate", temp.get("modify_date"));
		map.put("startYear", temp.get("start_year"));
		map.put("endYear", temp.get("end_year"));
		map.put("term", temp.get("term"));
		map.put("termType", temp.get("term_type"));
		map.put("termTypeName", DictUtils.findLabelByTypeAndKey("termType", temp.getInt("term_type")));
		map.put("schoolId", temp.get("school_id"));
		map.put("sort", temp.get("sort"));
		map.put("remark", temp.get("remark"));
		
		return renderSUC(map, response, header);
	}

}
