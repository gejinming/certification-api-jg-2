package com.gnet.certification;

import java.util.Map;

import org.springframework.stereotype.Service;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcGraduate;
import com.google.common.collect.Maps;

/**
 * 检查毕业要求序号是否唯一
 * 
 * @author SY
 * @Date 2016年06月24日 20:55:57
 */
@Service("EM00125")
public class EM00125 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		Long graduateVerId = paramsLongFilter(params.get("graduateVerId"));
		Integer indexNum = paramsIntegerFilter(params.get("indexNum"));
		Integer originValue = paramsIntegerFilter(params.get("originValue"));
		
		// 毕业要求序号不能为空过滤
		if (indexNum == null) {
			return renderFAIL("0182", response, header);
		}
		
		// 结果返回
		Map<String, Object> result = Maps.newHashMap();
		result.put("isExisted", CcGraduate.dao.isExisted(graduateVerId, indexNum, originValue));
		return renderSUC(result, response, header);
	}

}
