package com.gnet.certification;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.service.CcTeacherCourseService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;

/**
 * 检查是否已经存在考评点或者成绩组成的分享人
 * 
 * @author SY
 * @Date 2017年10月19日
 */
@Service("EM00783")
public class EM00783 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long courseId = paramsLongFilter(params.get("courseId"));
		Long termId = paramsLongFilter(params.get("termId"));
		Integer grade = paramsIntegerFilter(params.get("grade"));

		if (courseId == null) {
			return renderFAIL("0250", response, header);
		}
		if (termId == null) {
			return renderFAIL("0350", response, header);
		}
		if (grade == null) {
			return renderFAIL("0316", response, header);
		}

		CcTeacherCourseService ccTeacherCourseService = SpringContextHolder.getBean(CcTeacherCourseService.class);
		// 是否存在分享人
		Boolean isExist = ccTeacherCourseService.isExistSharer(courseId, termId, grade);
		
		// 结果返回
		Map<String, Object> result = Maps.newHashMap();
		result.put("isExisted", isExist);
		return renderSUC(result, response, header);
	}
	
}
