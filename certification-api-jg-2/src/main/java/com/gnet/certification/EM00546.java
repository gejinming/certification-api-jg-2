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
 * 验证开课课程下的成绩组成与指标点的关系是否可以保存 
 * 
 * @author XZL
 * 
 * @date 2016年7月19日
 * 
 */
@Service("EM00546")
@Transactional(readOnly=true)
public class EM00546 extends BaseApi implements IApi {
	
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
		//需要增加或修改的权重
		BigDecimal weight = paramsBigDecimalFilter(param.get("weight"));
		
		if(teacherCourseId == null){
			return renderFAIL("0310", response, header);
		}
		
		if (indicationId == null) {
			return renderFAIL("0230", response, header);
		}
		
		if(weight == null){
			return renderFAIL("0491", response, header);
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
		//要保存的权重和
		BigDecimal saveWeight = PriceUtils._add(restWeight, weight);
		map.put("cantSave", PriceUtils.greaterThan(saveWeight, CcCourseGradecomposeIndication.MAX_WEIGHT));
		return renderSUC(map, response, header);
	}
	
	
}
