package com.gnet.certification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcIndicationCourse;
import com.gnet.service.CcIndicationCourseService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;

/**
 * 获取指定指标点的总权重
 * 
 * @author xzl
 * 
 * @date 2016年12月29日18:23:48
 * 
 */
@Service("EM00689")
@Transactional(readOnly=true)
public class EM00689 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Long indicationId = paramsLongFilter(param.get("indicationId"));
		Long directionId = paramsLongFilter(param.get("directionId"));
		if(param.containsKey("directionId") && directionId == null){
			return renderFAIL("1009", response, header, "directionId的参数值非法");
		}
		
		if(indicationId == null){
			return renderFAIL("0230", response, header);
		}
		
		List<CcIndicationCourse> indicationCourseList = CcIndicationCourse.dao.findByIndicationIdAndDirectionId(indicationId, directionId);
		CcIndicationCourseService ccIndicationCourseService = SpringContextHolder.getBean(CcIndicationCourseService.class);
		Map<Long, BigDecimal> indicationWeightMap = ccIndicationCourseService.getIndicationWeight(indicationCourseList);
		
	    Map<String, Object> map = Maps.newHashMap();
		map.put("indicationId", indicationId);
		map.put("allWeight", indicationWeightMap.get(indicationId));
		map.put("directionId", directionId);
		
		return renderSUC(map, response, header);
	}
}
