package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradeComposeDetail;
import com.jfinal.kit.StrKit;

/**
 * 检查成绩组成元素明细的题号是否重复
 * 
 * @author sll
 *
 */
@Service("EM00076")
public class EM00076 extends BaseApi implements IApi {

	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		@SuppressWarnings("unchecked")
		Map<String, Object> params = request.getData();
		
		String name = paramsStringFilter(params.get("name"));
		String originValue = paramsStringFilter(params.get("originValue"));
		Long courseGradecomposeId = paramsLongFilter(params.get("courseGradecomposeId"));
		
		if (StrKit.isBlank(name)) {
			return renderFAIL("0452", response, header);
		}
		if (courseGradecomposeId == null) {
			return renderFAIL("0475", response, header);
		}
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		result.put("isExisted", CcCourseGradeComposeDetail.dao.isExisted(name, originValue, courseGradecomposeId));
		return renderSUC(result, response, header);
	}
	
}
