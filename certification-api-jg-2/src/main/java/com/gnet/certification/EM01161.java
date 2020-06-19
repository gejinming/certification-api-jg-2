package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcLevelDetail;

/**
 * 查看等级制度明细某条信息
 * 
 * @author SY
 * @Date 2019年12月9日13:50:14
 */
@Service("EM01161")
public class EM01161 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据：id
		Long id = paramsLongFilter(params.get("id"));
		// id不能为空过滤
		if (id == null) {
			return renderFAIL("0100", response, header);
		}
		// 通过id获取这条记录
		CcLevelDetail temp = CcLevelDetail.dao.findFilteredById(id);
		if(temp == null) {
			return renderFAIL("0101", response, header);
		}
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("id", temp.getLong("id"));
		map.put("createDate", temp.getDate("create_date"));
		map.put("modifyDate", temp.getDate("modify_date"));
		map.put("name", temp.getStr("name"));
		map.put("value", temp.getInt("value"));
		map.put("levelId", temp.getLong("level_id"));
		map.put("remark", temp.getStr("remark"));
		
		return renderSUC(map, response, header);
	}
	
}
