package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcStudent;
import com.jfinal.kit.StrKit;

/**
 * 检查学号是否唯一
 * 
 * @author wct
 * @Date 2016年06月30日 23:40:26
 */
@Service("EM00205")
public class EM00205 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		String studentNo = paramsStringFilter(params.get("studentNo"));
		String originValue = paramsStringFilter(params.get("originValue"));
		Long schoolId = UserCacheKit.getSchoolId(request.getHeader().getToken());
		
		// 学号不能为空过滤
		if (StrKit.isBlank(studentNo)) {
			return renderFAIL("0332", response, header);
		}
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		result.put("isExisted", CcStudent.dao.isExisted(studentNo, originValue, schoolId));
		return renderSUC(result, response, header);
	}

}
