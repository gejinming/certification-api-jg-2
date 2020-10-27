package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcSelfreport;
import com.gnet.model.admin.CcSelfreportContent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自评报告导出内容
 * 
 * @author GJM
 * @Date 2020年09月10日
 */
@Service("EM01221")
public class EM01221 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		HashMap<Object, Object> result = new HashMap<>();

		Long id = paramsLongFilter(params.get("id"));

		if ( id ==null){
			return renderFAIL("2574", response, header);
		}

		List<CcSelfreportContent> selfReportContentList = CcSelfreportContent.dao.findSelfReportContentList(id);
		ArrayList<Object> ccSelfreportList = new ArrayList<>();
		for (CcSelfreportContent temp : selfReportContentList){
			HashMap<Object, Object> map = new HashMap<>();
			Long selfId = temp.getLong("id");
			Object titleNo = temp.get("title_no");
			String titleName = temp.getStr("title_name");
			String content = temp.getStr("content");
			map.put("selfId",selfId);
			map.put("titleNo",titleNo);
			map.put("titleName",titleName);
			map.put("content",content);
			ccSelfreportList.add(map);
		}

		result.put("ccSelfreportList", ccSelfreportList);
		return renderSUC(result, response, header);
	}


}
