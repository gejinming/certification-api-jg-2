package com.gnet.certification;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcEvaluteLevel;
import com.google.common.collect.Maps;

/**
 * 开课课程考评点层次名称唯一性验证
 *
 * @author wct
 * @date 2016年9月3日
 */
@Transactional(readOnly = true)
@Service("EM00395")
public class EM00395 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long teacherCourseId = paramsLongFilter(params.get("teacherCourseId"));
		Long indicationId = paramsLongFilter(params.get("indicationId"));
		String levelName = paramsStringFilter(params.get("levelName"));
		String originValue = paramsStringFilter(params.get("originValue"));
		
		// 开课课程不能为空过滤
		if (teacherCourseId == null) {
			return renderFAIL("0435", response, header);
		}
		
		// 指标点不能为空过滤
		if (indicationId == null) {
			return renderFAIL("0436", response, header);
		}
		
		// 层次名称不能为空
		if (levelName == null) {
			return renderFAIL("0432", response, header);
		}
		
		boolean isSuccess = CcEvaluteLevel.dao.isExisted(teacherCourseId, indicationId, levelName, originValue);
		// 返回结果
		Map<String, Object> result = Maps.newHashMap();
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}

}
