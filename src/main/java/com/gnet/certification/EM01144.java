package com.gnet.certification;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcLevel;
import com.gnet.model.admin.CcLevelDetail;
import com.google.common.collect.Maps;

/**
 * 删除等级制度某条信息
 * 
 * @author SY
 * @Date 2019年12月9日13:51:55
 */
@Service("EM01144")
public class EM01144 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		// 获取数据
		Date date = new Date();
		
		List<Long> ids = paramsJSONArrayFilter(params.get("ids"), Long.class);
		// id不能为空信息的过滤
		if (ids == null) {
			return renderFAIL("0100", response, header);
		}
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		// 判断是否还有等级制度详细表在使用
		List<CcLevelDetail> ccLevelDetails = CcLevelDetail.dao.findFilteredByColumnIn("level_id", idsArray);
		if(!ccLevelDetails.isEmpty()) {
			return renderFAIL("0708", response, header);
		}
		
		Map<String, Object> result = Maps.newHashMap();
		// 返回操作是否成功
		result.put("isSuccess", CcLevel.dao.deleteAll(idsArray, date));
		return renderSUC(result, response, header);
	}
	
}
