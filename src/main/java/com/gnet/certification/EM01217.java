package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcSelfreport;
import com.gnet.model.admin.CcVersion;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.SpringContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 自评报告信息
 * 
 * @author GJM
 * @Date 2020年09月10日
 */
@Service("EM01217")
public class EM01217 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		HashMap<Object, Object> result = new HashMap<>();

		Long id = paramsLongFilter(params.get("id"));

		if ( id ==null){
			return renderFAIL("2574", response, header);
		}

		CcSelfreport ccSelfreport = CcSelfreport.dao.findSelfReport(id);

		result.put("ccSelfreport", ccSelfreport);
		return renderSUC(result, response, header);
	}


}
