package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcGradecompose;
import com.jfinal.kit.StrKit;

/**
 * 检查成绩组成名称唯一性验证接口
 * 
 * @author SY
 * @Date 2016年7月13日20:59:17
 */
@Service("EM00075")
public class EM00075 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		String name = paramsStringFilter(params.get("name"));
		String originValue = paramsStringFilter(params.get("originValue"));
		Long majorId = paramsLongFilter(params.get("majorId"));
		
		// 课程层次名称不能为空
		if (StrKit.isBlank(name)) {
			return renderFAIL("0121", response, header);
		}
		// majorId不能为空信息的过滤
		if (majorId == null) {
			return renderFAIL("0130", response, header);
		}
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		result.put("isExisted", CcGradecompose.dao.isExisted(name, originValue, majorId));
		return renderSUC(result, response, header);
	}

}
