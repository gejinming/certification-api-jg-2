package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseOutline;
import com.gnet.model.admin.CcCourseOutlineType;
import com.gnet.model.admin.User;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 负责人是否可以操作大纲
 * 
 * @author xzl
 * @Date 2017-8-24 11:15:16
 */
@Service("EM00714")
public class EM00714 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
        Long courseId = paramsLongFilter(params.get("courseId"));
        Long courseOutlineTypeId = paramsLongFilter(params.get("courseOutlineTypeId"));

        if(courseId == null){
			return renderFAIL("0250", response, header);
		}

		if(courseOutlineTypeId == null){
			return  renderFAIL("0892", response, header);
		}

		// 结果返回
		Map<String, Object> result = Maps.newHashMap();
		Map<String, Object> param = new HashMap<>();
		params.put("course_id", courseId);
		params.put("outline_type_id", courseOutlineTypeId);
		CcCourseOutline ccCourseOutline = CcCourseOutline.dao.findFirstByColumn(param, true);

        if(ccCourseOutline == null || (ccCourseOutline!= null && ccCourseOutline.getInt("status") < CcCourseOutline.STATUS_NOT_SUBMIT)){
			result.put("canOperate", true);
			return renderSUC(result, response, header);
		}


		result.put("canOperate", false);
		return renderSUC(result, response, header);
	}
	
}
