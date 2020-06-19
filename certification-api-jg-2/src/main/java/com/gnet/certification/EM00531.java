package com.gnet.certification;

import java.math.BigDecimal;
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
import com.gnet.model.admin.CcEduclassStudentGradecompose;


/**
 * 教学班学生成绩组成增加或更新其他分数
 * 
 * @author SY
 * 
 * @date 2017年8月15日
 *
 */
@Service("EM00531")
@Transactional(readOnly=false)
public class EM00531 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Map<String, Object> result = new HashMap<>();
		
		// 教学班编号
		Long eduClassId = paramsLongFilter(param.get("eduClassId"));
		
		// 开课课程成绩组成元素编号
		Long courseGradecomposeId = paramsLongFilter(param.get("courseGradecomposeId"));
		
		// 学生编号
		Long studentId = paramsLongFilter(param.get("studentId"));
		
		// 其他分数
		BigDecimal otherScore = paramsBigDecimalFilter(param.get("otherScore"));
		
		if(eduClassId == null){
			return renderFAIL("0380", response, header);
		}
	
		if (courseGradecomposeId == null) {
			return renderFAIL("0490", response, header);
		}
		
		if (studentId == null) {
			return renderFAIL("0330", response, header);
		}
		
		Map<String, Object> paras = new HashMap<>();
		paras.put("student_id", studentId);
		paras.put("course_gradecompose_id", courseGradecomposeId);
		paras.put("class_id", eduClassId);
		CcEduclassStudentGradecompose ccEduclassStudentGradecompose = CcEduclassStudentGradecompose.dao.findFirstByColumn(paras, Boolean.TRUE);
		if(ccEduclassStudentGradecompose == null){
			return renderFAIL("0930", response, header);
		}
		
		ccEduclassStudentGradecompose.set("modify_date", new Date());
		ccEduclassStudentGradecompose.set("other_score", otherScore);
		Boolean isSuccess = ccEduclassStudentGradecompose.update();
		
		result.put("isSuccess", isSuccess);	
		return renderSUC(result, response, header);
	}
}
