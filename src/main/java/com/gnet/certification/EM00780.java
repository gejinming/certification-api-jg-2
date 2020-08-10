package com.gnet.certification;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcTeacherCourse;
import com.google.common.collect.Maps;

/**
 * 分享考评点或者成绩组成
 * 
 * @author SY
 * @Date 2017年10月19日
 */
@Service("EM00780")
public class EM00780 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long teacherCourseId = paramsLongFilter(params.get("teacherCourseId"));

		if (teacherCourseId == null) {
			return renderFAIL("0310", response, header);
		}

		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findFilteredById(teacherCourseId);
		if(ccTeacherCourse == null) {
			return renderFAIL("0311", response, header);
		}
		ccTeacherCourse.set("modify_date", new Date());
		ccTeacherCourse.set("is_shared", Boolean.TRUE);
		Boolean isSuccess = ccTeacherCourse.update();
		// 结果返回
		Map<String, Object> result = Maps.newHashMap();
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}
	
}
