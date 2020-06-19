package com.gnet.certification;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcStudentEvalute;
import com.google.common.collect.Maps;

/**
 * 考评点成绩删除接口
 * 
 * @author SY
 * @date 2016年12月21日15:20:22
 */
@Transactional(readOnly = false)
@Service("EM00402")
public class EM00402 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long evaluteId = paramsLongFilter(params.get("evaluteId"));
		Long studentId = paramsLongFilter(params.get("studentId"));
		// 课程考评点编号不能为空过滤
		if (evaluteId == null) {
			return renderFAIL("0370", response, header);
		}
		
		// 学生编号不能为空过滤
		if (studentId == null) {
			return renderFAIL("0330", response, header);
		}
		
		Date date = new Date();
		
		Boolean isSuccess = CcStudentEvalute.dao.deleteAllByEvaluteIdAndStuedntId(evaluteId, studentId, date);
		
		// 返回结果
		Map<String, Object> result = Maps.newHashMap();
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}

}
