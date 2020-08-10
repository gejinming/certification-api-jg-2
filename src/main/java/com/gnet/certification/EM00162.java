package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcIndication;
import com.gnet.model.admin.CcIndicatorPoint;
import com.jfinal.kit.StrKit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *增加指标点
 *
 * @author xzl
 *
 * @date 2017年11月17日
 *
 */
@Service("EM00162")
@Transactional(readOnly=false)
public class EM00162 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {

		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();

		Long graduateId = paramsLongFilter(param.get("graduateId"));
		String content = paramsStringFilter(param.get("content"));
		String remark = paramsStringFilter(param.get("remark"));
		if (graduateId == null) {
			return renderFAIL("0232", response, header);
		}
		if (StrKit.isBlank(content)) {
			return renderFAIL("0234", response, header);
		}
		Date date = new Date();

		CcIndicatorPoint beforeIndication = CcIndicatorPoint.dao.findLastByGraduateId(graduateId);
		Integer indexNum = beforeIndication == null ? 1 : (beforeIndication.getInt("index_num") + 1);
		CcIndicatorPoint ccIndication = new CcIndicatorPoint();

		ccIndication.set("create_date", date);
		ccIndication.set("modify_date", date);
		ccIndication.set("graduate_id", graduateId);
		ccIndication.set("index_num",indexNum);
		ccIndication.set("content", content);
		ccIndication.set("remark", remark);
		ccIndication.set("is_del", Boolean.FALSE);

		Map<String, Object> result = new HashMap<>();
		result.put("isSuccess", ccIndication.save());

		return renderSUC(result, response, header);
	}
}
