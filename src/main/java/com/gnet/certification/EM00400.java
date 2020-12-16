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
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;

/**
 * 考评点成绩录入接口
 * 
 * @author wct
 * @date 2016年9月4日
 */
@Transactional(readOnly = false)
@Service("EM00400")
public class EM00400 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long courseGradecomposeId = paramsLongFilter(params.get("courseGradecomposeId"));
		Long evaluteId = paramsLongFilter(params.get("evaluteId"));
		Long studentId = paramsLongFilter(params.get("studentId"));
		// 等级明细编号
		Long levelId = paramsLongFilter(params.get("levelId"));
		//批次Id
		Long batchId = paramsLongFilter(params.get("batchId"));
		// 课程考评点编号不能为空过滤
		if (evaluteId == null) {
			return renderFAIL("0370", response, header);
		}
		if( courseGradecomposeId == null) {
			return renderFAIL("1009", response, header, "courseGradecomposeId的参数值非法");
		}
		// 学生编号不能为空过滤
		if (studentId == null) {
			return renderFAIL("0330", response, header);
		}
		
		Map<String, Object> searchParams = Maps.newHashMap();
		searchParams.put("course_gradecompose_id", courseGradecomposeId);
		searchParams.put("student_id", studentId);
		if(batchId!=null){
			searchParams.put("batch_id", batchId);
		}
		CcStudentEvalute ccStudentEvalute = CcStudentEvalute.dao.findFirstByColumn(searchParams, Boolean.TRUE);
		
		boolean isSuccess = false;
		Date date = new Date();
		if (ccStudentEvalute == null) {
			IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
			ccStudentEvalute = new CcStudentEvalute();
			ccStudentEvalute.set("evalute_id", evaluteId);
			ccStudentEvalute.set("student_id", studentId);
			//ccStudentEvalute.set("level_id", levelId);
			ccStudentEvalute.set("create_date", date);
			ccStudentEvalute.set("modify_date", date);
			ccStudentEvalute.set("course_gradecompose_id", courseGradecomposeId);
			ccStudentEvalute.set("batch_id", batchId);
			ccStudentEvalute.set("is_del", CcStudentEvalute.DEL_NO);
			ccStudentEvalute.set("id", idGenerate.getNextValue());
			isSuccess = ccStudentEvalute.save();
		} else {
			ccStudentEvalute.set("evalute_id", evaluteId);
			ccStudentEvalute.set("modify_date", date);
			isSuccess = ccStudentEvalute.update();
		}
		
		// 返回结果
		Map<String, Object> result = Maps.newHashMap();
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}

}
