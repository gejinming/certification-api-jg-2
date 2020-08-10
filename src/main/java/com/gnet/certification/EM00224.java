package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradecompose;
import com.gnet.model.admin.CcEduclass;
import com.gnet.model.admin.CcEvalute;
import com.gnet.model.admin.CcEvaluteLevel;
import com.gnet.model.admin.CcTeacherCourse;

/**
 * 批量删除教师开课课程
 * 
 * @author SY
 * 
 * @date 2016年06月29日 14:41:41
 *
 */
@Service("EM00224")
@Transactional(readOnly=false)
public class EM00224 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String,Boolean> result = new HashMap<>();
		
		Date date = new Date();
		
		List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		// 判断是否已经删除教学班表
		List<CcEduclass> ccEduclasss = CcEduclass.dao.findFilteredByColumnIn("teacher_course_id", idsArray);
		if(!ccEduclasss.isEmpty()) {
			return renderFAIL("0317", response, header);
		}
		
		// 判断是否已经删除开课课程成绩组成元素表
		List<CcCourseGradecompose> ccCourseGradecomposes = CcCourseGradecompose.dao.findFilteredByColumnIn("teacher_course_id", idsArray);
		if(!ccCourseGradecomposes.isEmpty()) {
			return renderFAIL("0318", response, header);
		}
		
		// 判断是否已经删除考评点表
		List<CcEvalute> cEvalutes = CcEvalute.dao.findFilteredByColumnIn("teacher_course_id", idsArray);
		if(!cEvalutes.isEmpty()) {
			return renderFAIL("0319", response, header);
		}
		
		// 判断是否已经删除考评点得分层次表
		List<CcEvaluteLevel> cEvaluteLevels = CcEvaluteLevel.dao.findFilteredByColumnIn("teacher_course_id", idsArray);
		if(!cEvaluteLevels.isEmpty()) {
			return renderFAIL("0320", response, header);
		}
				
		// 删除CcTeacherCourse
		if(!CcTeacherCourse.dao.deleteAll(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}	
	
