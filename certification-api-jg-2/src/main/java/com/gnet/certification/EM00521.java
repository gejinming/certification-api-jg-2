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
import com.gnet.model.admin.CcCourseGradeComposeDetail;
import com.gnet.model.admin.CcCourseGradecompose;
import com.gnet.model.admin.CcCourseGradecomposeIndication;

/**
 * 批量删除开课课程成绩组成元素
 * 
 * @author xzl
 * 
 * @date 2016年7月7日
 *
 */
@Service("EM00521")
@Transactional(readOnly=false)
public class EM00521 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String,Boolean> result = new HashMap<>();
		
		Date date = new Date();
		
		List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		List<CcCourseGradeComposeDetail> courseGradeComposeDetails = CcCourseGradeComposeDetail.dao.findFilteredByColumnIn("course_gradecompose_id", idsArray);
		List<CcCourseGradecomposeIndication> courseGradecomposeIndications = CcCourseGradecomposeIndication.dao.findFilteredByColumnIn("course_gradecompose_id", idsArray);
		if(!courseGradeComposeDetails.isEmpty()){
			return renderFAIL("0473", response, header);
		}
		if(!courseGradecomposeIndications.isEmpty()){
			return renderFAIL("0472", response, header);
		}
			
		// 删除CcCourseGradecompose
		if(!CcCourseGradecompose.dao.deleteAll(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}	
	
