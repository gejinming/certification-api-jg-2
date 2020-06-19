package com.gnet.certification;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcEvalute;

/**
 * 考评点排序接口
 * 
 * @author sll
 *
 */
@Service("EM00366")
@Transactional(readOnly=false)
public class EM00366 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> params = request.getData();
		
		JSONArray indexNumArray = paramsJSONArrayFilter(params.get("indexNumArray"));
		List<CcEvalute> ccEvaluteUpdates = new ArrayList<>();
		
		Date date = new Date();
		for(int i = 0; i < indexNumArray.size(); i++) {
			CcEvalute temp = new CcEvalute();
			JSONObject map = (JSONObject) indexNumArray.get(i); 
			temp.set("id", map.get("id"));
			temp.set("modify_date", date);
			temp.set("index_num", map.get("indexNum"));
			ccEvaluteUpdates.add(temp);
		}
		
		Boolean isSuccess = CcEvalute.dao.batchUpdate(ccEvaluteUpdates, "modify_date,index_num");
		Map<String, Object> result = new HashMap<>();
		result.put("isSuccess", isSuccess);
		
		return renderSUC(result, response, header);
	}

}
