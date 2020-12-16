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
import com.gnet.model.admin.CcEduclassStudentStudy;;

/**
 * 新增或者修改学生教学班学习情况接口
 * @author SY
 * 
 * @date 2017年8月14日09:52:31
 * 
 */
@Service("EM00281")
@Transactional(readOnly=false)
public class EM00281 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Map<String, Object> map = new HashMap<>();
		
		Long eduClassId = paramsLongFilter(param.get("eduClassId"));
		Long studentId = paramsLongFilter(param.get("studentId"));
		Integer remark = paramsIntegerFilter(param.get("remark"));
		Boolean isRetake = paramsBooleanFilter(param.get("isRetake"));
		Long courseGradecomposeId = paramsLongFilter(param.get("courseGradecomposeId"));
		Long batchId = paramsLongFilter(param.get("batchId"));
		if(eduClassId == null){
			return renderFAIL("0380", response, header);
		}
		
		if(studentId == null){
			return renderFAIL("0330", response, header);
		}
		if( courseGradecomposeId == null) {
			return renderFAIL("1009", response, header, "courseGradecomposeId的参数值非法");
		}
		Boolean isSuccess = Boolean.TRUE;
		Date date = new Date();
		Map<String, Object> paras = new HashMap<>();
		paras.put("class_id", eduClassId);
		paras.put("student_id", studentId);
		paras.put("course_gradecompose_id", courseGradecomposeId);
		if (batchId!=null){
			paras.put("batch_id", batchId);
		}
		CcEduclassStudentStudy ccEduclassStudentStudy = CcEduclassStudentStudy.dao.findFirstByColumn(paras, Boolean.TRUE);
		if(ccEduclassStudentStudy == null) {
			// 新增
			remark = remark == null ? CcEduclassStudentStudy.REMARK_NOTHING : remark;
			isRetake = isRetake == null ? CcEduclassStudentStudy.RETAKE_FALSE : isRetake;
			
			ccEduclassStudentStudy = new CcEduclassStudentStudy();
			ccEduclassStudentStudy.set("create_date", date);
			ccEduclassStudentStudy.set("modify_date", date);
			ccEduclassStudentStudy.set("student_id", studentId);
			ccEduclassStudentStudy.set("class_id", eduClassId);
			ccEduclassStudentStudy.set("course_gradecompose_id", courseGradecomposeId);
			ccEduclassStudentStudy.set("batch_id", batchId);
			ccEduclassStudentStudy.set("remark", remark);
			ccEduclassStudentStudy.set("is_retake", isRetake);
			ccEduclassStudentStudy.set("is_del", CcEduclassStudentStudy.DEL_NO);
			isSuccess = ccEduclassStudentStudy.save();
		} else {
			// 更新
			ccEduclassStudentStudy.set("modify_date", date);
			if(remark != null) {
				ccEduclassStudentStudy.set("remark", remark);	
			}
			if(isRetake != null) {
				ccEduclassStudentStudy.set("is_retake", isRetake);
			}
			ccEduclassStudentStudy.set("is_del", CcEduclassStudentStudy.DEL_NO);
			isSuccess = ccEduclassStudentStudy.update();
		}
		
		map.put("isSuccess", isSuccess);
		return renderSUC(map, response, header);
	}
	
}
