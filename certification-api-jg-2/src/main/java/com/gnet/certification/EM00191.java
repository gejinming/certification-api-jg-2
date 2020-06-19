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
import com.gnet.model.admin.CcMajorDirection;

/**
 * 查看专业方向详情
 * 
 * @author sll
 * 
 * @date 2016年06月28日 17:57:45
 *
 */
@Service("EM00191")
@Transactional(readOnly=true)
public class EM00191 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		
		if (id == null) {
			return renderFAIL("0280", response, header);
		}
		
		CcMajorDirection temp = CcMajorDirection.dao.findFilteredById(id);
		if (temp == null) {
			return renderFAIL("0281", response, header);
		}
		
		Map<String, Object> map = new HashMap<String,Object>();
		
		map.put("id", temp.getLong("id"));
		map.put("createDate", temp.getDate("create_date"));
		map.put("modifyDate", temp.getDate("modify_date"));
		map.put("planId", temp.getLong("plan_id"));
		map.put("name", temp.getStr("name"));
		map.put("remark", temp.getStr("remark"));
		
		return renderSUC(map, response, header);
	}

}
