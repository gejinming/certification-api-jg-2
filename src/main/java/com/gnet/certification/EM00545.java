package com.gnet.certification;

import java.math.BigDecimal;

import java.util.HashMap;

import java.util.Map;



import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;


import com.gnet.model.admin.CcCourseGradecomposeIndication;
import com.gnet.utils.PriceUtils;


/**
 * 通过指标点和开课课程编号得到个指标点在某门课程中剩余权重
 * 
 * @author XZL
 * 
 * @date 2016年7月18日
 * 
 */
@Service("EM00545")
@Transactional(readOnly=true)
public class EM00545 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		//开课课程编号
		Long teacherCourseId = paramsLongFilter(param.get("teacherCourseId"));
		//指标点编号
		Long indicationId = paramsLongFilter(param.get("indicationId"));
		
		//开课课程成绩组成元素指标点关联编号（用于编辑的时候验证）
		Long courseGradecomposeIndicationId = paramsLongFilter(param.get("courseGradecomposeIndicationId"));
		
		if(teacherCourseId == null){
			return renderFAIL("0310", response, header);
		}
		
		if (indicationId == null) {
			return renderFAIL("0230", response, header);
		}
		
		//同一课程同一指标点下成绩组成元素权重和
		BigDecimal existWeight = CcCourseGradecomposeIndication.dao.calculateSumWeights(teacherCourseId, indicationId);
		
		BigDecimal restWeight = PriceUtils.sub(CcCourseGradecomposeIndication.MAX_WEIGHT, existWeight);
		if(courseGradecomposeIndicationId != null){
			CcCourseGradecomposeIndication courseGradecomposeIndication = CcCourseGradecomposeIndication.dao.findFilteredById(courseGradecomposeIndicationId);
		    if(courseGradecomposeIndication == null){
		    	return renderFAIL("0497", response, header);
		    }
		    restWeight = PriceUtils.sub(restWeight, courseGradecomposeIndication.getBigDecimal("weight"));
		}
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("restWeight", restWeight);
		return renderSUC(map, response, header);
	}
	
	
}
