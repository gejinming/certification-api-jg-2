package com.gnet.certification;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcIndicatorPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 *  对某个毕业要求点的指标点列表进行重新排序
 *
 * @author xzl
 *
 * @date 2017年11月17日15:27:08
 *
 */
@Service("EM00160")
@Transactional(readOnly=false)
public class EM00160 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {

		Map<String, Object> param = request.getData();

		JSONArray sortArray = paramsJSONArrayFilter(param.get("sortArray"));
		List<CcIndicatorPoint> ccIndicatorPoints = new ArrayList<>();

		Date date = new Date();
		for(int i = 0; i < sortArray.size(); i++) {
			CcIndicatorPoint temp = new CcIndicatorPoint();
			JSONObject map = (JSONObject) sortArray.get(i);
			temp.set("id", map.get("id"));
			temp.set("modify_date", date);
			temp.set("index_num", map.get("indexNum"));
			ccIndicatorPoints.add(temp);
		}
		Boolean isSuccess = CcIndicatorPoint.dao.batchUpdate(ccIndicatorPoints, "modify_date,index_num");
		Map<String, Object> result = new HashMap<>();
		result.put("isSuccess", isSuccess);

		return renderSUC(result, response, header);
	}
}
