package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcEduclass;
import com.jfinal.kit.StrKit;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 教学班名称是否重复接口
 * 
 * @author xzl
 * @Date 2017年10月8日
 */
@Service("EM00316")
public class EM00316 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long id = paramsLongFilter(params.get("id"));
        Long teacherCourseId = paramsLongFilter(params.get("teacherCourseId"));
        String educlassName = paramsStringFilter(params.get("educlassName"));
		if (StrKit.isBlank(educlassName)) {
			return renderFAIL("0382", response, header);
		}
		if (teacherCourseId == null) {
			return renderFAIL("0383", response, header);
		}

		// 结果返回
		Map<String, Object> result = new HashMap<>();
		result.put("isExisted", CcEduclass.dao.isExisted(id, teacherCourseId, educlassName));
		return renderSUC(result, response, header);
	}

}
