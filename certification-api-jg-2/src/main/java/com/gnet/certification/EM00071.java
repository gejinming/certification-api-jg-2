package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcGradecompose;

/**
 * 查看成绩组成某条信息
 * 
 * @author SY
 * @Date 2016年6月20日14:09:05
 */
@Service("EM00071")
public class EM00071 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据：id
		Long gradecomposeId = paramsLongFilter(params.get("id"));
		// gradecomposeId不能为空过滤
		if (gradecomposeId == null) {
			return renderFAIL("0120", response, header);
		}
		// 通过id获取这条记录
		CcGradecompose ccGradecompose = CcGradecompose.dao.findFilteredById(gradecomposeId);
		if(ccGradecompose == null) {
			return renderFAIL("0122", response, header);
		}
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		result.put("id", ccGradecompose.get("id"));
		result.put("createDate", ccGradecompose.get("create_date"));
		result.put("modifyDate", ccGradecompose.get("modify_date"));
		result.put("majorId", ccGradecompose.get("major_id"));
		result.put("name", ccGradecompose.get("name"));
		result.put("remark", ccGradecompose.get("remark"));
		return renderSUC(result, response, header);
	}
	
}
