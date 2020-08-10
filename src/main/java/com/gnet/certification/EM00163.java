package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcIndication;
import com.gnet.model.admin.CcIndicatorPoint;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * 修改指标点
 *
 * @author xzl
 *
 * @date 2017年11月17日15:49:10
 *
 */
@Service("EM00163")
@Transactional(readOnly=false)
public class EM00163 extends BaseApi implements IApi{

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {

		Map<String, Object> param = request.getData();

		Long id = paramsLongFilter(param.get("id"));
		String content = paramsStringFilter(param.get("content"));
		String remark = paramsStringFilter(param.get("remark"));

		if (id == null) {
			return renderFAIL("0230", response, header);
		}
		if (StrKit.isBlank(content)) {
			return renderFAIL("0234", response, header);
		}

		Date date = new Date();
		CcIndicatorPoint ccIndication = CcIndicatorPoint.dao.findFilteredById(id);
		if(ccIndication == null) {
			return renderFAIL("0231", response, header);
		}
		ccIndication.set("modify_date", date);
		ccIndication.set("content", content);
		ccIndication.set("remark", remark);

		Map<String, Object> result = Maps.newHashMap();
		result.put("isSuccess", ccIndication.update());
		return renderSUC(result, response, header);
	}
}
