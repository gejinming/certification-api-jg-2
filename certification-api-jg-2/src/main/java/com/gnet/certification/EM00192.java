package com.gnet.certification;

import java.util.Date;
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
import com.jfinal.kit.StrKit;

/**
 * 增加专业方向
 * 
 * @author sll
 * 
 * @date 2016年06月28日 17:57:45
 *
 */
@Service("EM00192")
@Transactional(readOnly=false)
public class EM00192 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		String name = paramsStringFilter(param.get("name"));
		String remark = paramsStringFilter(param.get("remark"));
        Long planId = paramsLongFilter(param.get("planId"));
		
        // planId不能为空信息的过滤
 		if (planId == null) {
 			return renderFAIL("0140", response, header);
 		}
 		
		if (StrKit.isBlank(name)) {
			return renderFAIL("0282", response, header);
		}
		
		Date date = new Date();
		
		CcMajorDirection ccMajorDirection = new CcMajorDirection();
		
		ccMajorDirection.set("create_date", date);
		ccMajorDirection.set("modify_date", date);
		ccMajorDirection.set("plan_id", planId);
		ccMajorDirection.set("name", name);
		ccMajorDirection.set("remark", remark);
		ccMajorDirection.set("is_del", Boolean.FALSE);

		Map<String, Object> result = new HashMap<>();
		result.put("isSuccess", ccMajorDirection.save());
		
		return renderSUC(result, response, header);
	}
}
