package com.gnet.certification;

import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradecomposeStudetail;
import com.google.common.collect.Maps;

/**
 * 某道题目学生的最大得分
 * 
 * @author xzl
 * @Date 2016年12月26日
 */
@Transactional(readOnly = false)
@Service("EM00687")
public class EM00687 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String, Object> result = Maps.newHashMap();
		
		Long id = paramsLongFilter(param.get("id"));
		
		if(id == null){
			return renderFAIL("0450", response, header);
		}
	
	    CcCourseGradecomposeStudetail courseGradecomposeStudetail  = CcCourseGradecomposeStudetail.dao.findMaxScoreStudent(id);
		if(courseGradecomposeStudetail != null){
			result.put("maxScore", courseGradecomposeStudetail.getBigDecimal("score"));
		}
		return renderSUC(result, response, header);
	}
	
}
