package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradeComposeDetail;
import com.gnet.model.admin.CcCourseGradecompose;


/**
 * 设置开课程成绩组成录入类型为直接输入
 * 
 * @author xzl
 * 
 * @date 2016年11月12日
 *
 */
@Service("EM00526")
@Transactional(readOnly=false)
public class EM00526 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Map<String, Object> result = new HashMap<>();
		
		Long id = paramsLongFilter(param.get("id"));

		if(id == null){
			return renderFAIL("0475", response, header);
		}
		
		if(CcCourseGradeComposeDetail.dao.isExist(id)){
			return renderFAIL("0800", response, header);
		}
		Date date = new Date();
		CcCourseGradecompose courseGradecompose = CcCourseGradecompose.dao.findFilteredById(id);
		if(courseGradecompose == null){
			return renderFAIL("0471", response, header);
		}
		courseGradecompose.set("modify_date", date);
		courseGradecompose.set("input_score_type", CcCourseGradecompose.DIRECT_INPUT_SCORE);
		result.put("isSuccess", courseGradecompose.update());	
		return renderSUC(result, response, header);
	}
}
