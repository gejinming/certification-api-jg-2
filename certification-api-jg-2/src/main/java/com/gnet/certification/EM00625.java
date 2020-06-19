package com.gnet.certification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourse;

/**
 * 检查即将保存的课程是否已经是课程组
 * 
 * @author SY
 * @Date 2016年07月14日 11:10:53
 */
@Service("EM00625")
public class EM00625 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		List<Long> courseIds = paramsJSONArrayFilter(params.get("courseIds"), Long.class);
		Integer type = paramsIntegerFilter(params.get("typeId"));
		if(courseIds.isEmpty() || courseIds.size() < 2) {
			return renderFAIL("0567", response, header);
		}
		if(CcCourse.dao.isGroup(courseIds,type)) {
			return renderFAIL("0566", response, header);
		}
		
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		result.put("isExisted", CcCourse.dao.isGroup(courseIds,type));
		return renderSUC(result, response, header);
	}

}
