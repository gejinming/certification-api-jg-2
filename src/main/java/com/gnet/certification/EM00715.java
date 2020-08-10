package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseOutline;
import com.gnet.model.admin.User;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 教师是否可以编写大纲
 * 
 * @author xzl
 * @Date 2017-8-24 13:54:46
 */
@Service("EM00715")
public class EM00715 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long courseOutlineId = paramsLongFilter(params.get("courseOutlineId"));
		User user  = UserCacheKit.getUser(request.getHeader().getToken());

        if(courseOutlineId == null){
        	return renderFAIL("0531", response, header);
		}

		// 结果返回
		Map<String, Object> result = Maps.newHashMap();
		CcCourseOutline ccCourseOutline = CcCourseOutline.dao.findFilteredById(courseOutlineId);

        if(ccCourseOutline != null && user.getLong("id").equals(ccCourseOutline.getLong("author_id")) && (ccCourseOutline.getInt("status").equals(CcCourseOutline.STATUS_NOT_SUBMIT) || ccCourseOutline.getInt("status").equals(CcCourseOutline.STATUS_AUDIT_DISMISSED))){
            result.put("canWrite", true);
			return renderSUC(result, response, header);
		}

		result.put("canWrite", false);
		return renderSUC(result, response, header);
	}
	
}
