package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.CcCourseGradecomposeIndication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradecompose;
import com.gnet.model.admin.CcScoreStuIndigrade;
import org.springframework.transaction.interceptor.TransactionAspectSupport;


/**
 * 设置开课程成绩组成录入类型为由题目明细计算获得
 * 
 * @author xzl
 * 
 * @date 2016年11月12日
 *
 */
@Service("EM00527")
@Transactional(readOnly=false)
public class EM00527 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Map<String, Object> result = new HashMap<>();
		
		Long id = paramsLongFilter(param.get("id"));
		Long inputType = paramsLongFilter(param.get("inputType"));
		if(id == null){
			return renderFAIL("0475", response, header);
		}
		if (inputType == null){
			return renderFAIL("0475", response, header);
		}
		if(CcScoreStuIndigrade.dao.isExistStudentGrade(id)){
			return renderFAIL("0801", response, header);
		}
		
		Date date = new Date();
		CcCourseGradecompose courseGradecompose = CcCourseGradecompose.dao.findFilteredById(id);
		if(courseGradecompose == null){
			return renderFAIL("0471", response, header);
		}
		courseGradecompose.set("modify_date", date);
		//TODO 2020/07/06增加多批次题目录入
		if(inputType == 1){
			courseGradecompose.set("input_score_type", CcCourseGradecompose.SUMMARY_INPUT_SCORE);
		}else {
			courseGradecompose.set("input_score_type", CcCourseGradecompose.SUMMARY_MANYINPUT_SCORE);
		}

		courseGradecompose.set("other_score", null);
		if(!courseGradecompose.update()){
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

        List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndicationList = CcCourseGradecomposeIndication.dao.findFilteredByColumn("course_gradecompose_id", id);
        if(!ccCourseGradecomposeIndicationList.isEmpty()){
        	for(CcCourseGradecomposeIndication ccCourseGradecomposeIndication : ccCourseGradecomposeIndicationList){
				ccCourseGradecomposeIndication.set("modify_date", date);
				ccCourseGradecomposeIndication.set("max_score",null);
			}
			if(!CcCourseGradecomposeIndication.dao.batchUpdate(ccCourseGradecomposeIndicationList, "modify_date, max_score")){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}

		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}
}
