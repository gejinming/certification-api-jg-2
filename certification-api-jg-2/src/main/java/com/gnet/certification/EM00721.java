package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseOutlineTemplate;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 大纲模板名称是否重复
 * 
 * @author xzl
 * @Date 2017年9月7日14:58:21
 */
@Service("EM00721")
public class EM00721 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long id = paramsLongFilter(params.get("id"));
		Long courseOutlineTypeId = paramsLongFilter(params.get("courseOutlineTypeId"));
		String templateName = paramsStringFilter(params.get("templateName"));

		if(courseOutlineTypeId == null){
			return  renderFAIL("0892", response, header);
		}

		if (StrKit.isBlank(templateName)) {
			return renderFAIL("0862", response, header);
		}

		// 结果返回
		Map<String, Object> result = Maps.newHashMap();
		result.put("isRepeat", CcCourseOutlineTemplate.dao.isExisted(templateName, id, courseOutlineTypeId));
		return renderSUC(result, response, header);
	}
	
}
