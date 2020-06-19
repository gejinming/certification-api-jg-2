package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradecompose;
import com.gnet.model.admin.CcGradecompose;

/**
 * 删除成绩组成某条信息
 * 
 * @author SY
 * @Date 2016年6月20日14:09:05
 */
@Service("EM00074")
public class EM00074 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据
		Date date = new Date();
		
		List<Long> ids = paramsJSONArrayFilter(params.get("ids"), Long.class);
		if (ids == null || ids.isEmpty()) {
			return renderFAIL("0120", response, header);
		}
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		// 判断是否还有开课课程成绩组成元素表在使用
		List<CcCourseGradecompose> ccCourseGradecomposes = CcCourseGradecompose.dao.findFilteredByColumnIn("gradecompose_id", idsArray);
		if(!ccCourseGradecomposes.isEmpty()) {
			return renderFAIL("0707", response, header);
		}
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		// 返回操作是否成功
		result.put("isSuccess", CcGradecompose.dao.deleteAll(idsArray, date));
		return renderSUC(result, response, header);
	}
	
}
