package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcEvalute;

/**
 * 课程考评点的序号唯一性验证
 * 
 * @author sll
 * 
 * 经过和xzl确认，这里应该改成某个课程下唯一，又因为新增了区分的类型，所以改成：某个课程的某个指标点的某个类型下唯一
 * @Edit SY 
 * @Edit Date 2017年8月9日15:47:11
 *
 */
@Service("EM00365")
public class EM00365 extends BaseApi implements IApi {
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		Integer indexNum = paramsIntegerFilter(params.get("indexNum"));
		Long evaluteTypeId = paramsLongFilter(params.get("evaluteTypeId"));
		Long indicationId = paramsLongFilter(params.get("indicationId"));
		Long teacherCourseId = paramsLongFilter(params.get("teacherCourseId"));
		Integer originValue = paramsIntegerFilter(params.get("originValue"));
		if(params.containsKey("originValue") && originValue == null) {
			return renderFAIL("1009", response, header, "originValue的参数值非法");
		}
			
		// 序号不能为空
		if (indicationId == null) {
			return renderFAIL("0372", response, header);
		}
		if (indexNum == null) {
			return renderFAIL("0376", response, header);
		}
		if (teacherCourseId == null) {
			return renderFAIL("0373", response, header);
		}
		if (evaluteTypeId == null) {
			return renderFAIL("0900", response, header);
		}
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		result.put("isExisted", CcEvalute.dao.isExisted(indexNum, originValue, teacherCourseId, indicationId, evaluteTypeId));
		return renderSUC(result, response, header);
	}

}
