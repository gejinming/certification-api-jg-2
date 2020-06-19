package com.gnet.certification;


import java.math.BigDecimal;
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
import com.gnet.model.admin.CcCourseGradecomposeIndication;
import com.google.common.collect.Maps;


/**
 * 根据开课课程成绩组成元素编号更新其支持指标点的满分值
 * 
 * @author xzl
 * 
 * @date 2016年11月14日
 *
 */
@Service("EM00528")
@Transactional(readOnly=false)
public class EM00528 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Map<String, Object> result = new HashMap<>();
		
		Long courseGradecomposeId = paramsLongFilter(param.get("courseGradecomposeId"));
		if(courseGradecomposeId == null){
			return renderFAIL("0490", response, header);
		}
		
		//根据开课课程成绩组成编号的题目分数得到对应指标点的满分值
		List<CcCourseGradeComposeDetail> courseGradeComposeDetailList = CcCourseGradeComposeDetail.dao.findByCourseGradeComposeId(courseGradecomposeId, null, null);
		//开课课程成绩组成支持的指标点
		List<CcCourseGradecomposeIndication> courseGradecomposeIndicationList = CcCourseGradecomposeIndication.dao.findFilteredByColumn("course_gradecompose_id", courseGradecomposeId);
		
		if(courseGradeComposeDetailList.isEmpty()){
			result.put("isSuccess", true);
			return renderSUC(result, response, header);
		}
		
		if(!courseGradeComposeDetailList.isEmpty() && courseGradecomposeIndicationList.isEmpty()){
			return renderFAIL("0482", response, header);
		}
		
		Map<Long, BigDecimal> scoreMap = Maps.newHashMap();
		for(CcCourseGradeComposeDetail temp: courseGradeComposeDetailList){
			scoreMap.put(temp.getLong("indication_id"), temp.getBigDecimal("allScore"));
		}
		Date date = new Date();
		for(CcCourseGradecomposeIndication temp : courseGradecomposeIndicationList){
			temp.set("modify_date", date);
			temp.set("max_score", scoreMap.get(temp.getLong("indication_id")));
		}
		
		if(!CcCourseGradecomposeIndication.dao.batchUpdate(courseGradecomposeIndicationList, "modify_date, max_score")){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}	
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}
}
