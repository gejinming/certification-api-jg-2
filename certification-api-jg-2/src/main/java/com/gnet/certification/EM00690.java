package com.gnet.certification;

import java.math.BigDecimal;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcIndicationCourse;
import com.google.common.collect.Maps;

/**
 * 获取指定课程的指标点权重和
 * 
 * @author xzl
 * 
 * @date 2016年12月29日
 * 
 */
@Service("EM00690")
@Transactional(readOnly=true)
public class EM00690 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
        Long courseId = paramsLongFilter(param.get("courseId"));
		if(courseId == null){
			return renderFAIL("0250", response, header);
		}
        
		BigDecimal allWeight = CcIndicationCourse.dao.getCourseWeight(courseId);
        
		Map<String, Object> map = Maps.newHashMap();
		map.put("courseId", courseId);
		map.put("allWeight", allWeight);
		return renderSUC(map, response, header);
	}
}
