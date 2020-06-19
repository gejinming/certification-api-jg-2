package com.gnet.certification;


import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseOutline;
import com.gnet.utils.DictUtils;
import com.google.common.collect.Maps;


/**
 * 通过课程编号获得课程大纲内容
 * 
 * @author xzl
 * 
 * @date 2016年11月15日
 *
 */
@Service("EM00585")
@Transactional(readOnly=false)
public class EM00585 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		
		//课程大纲编号
		Long courseId = paramsLongFilter(param.get("courseId"));
		
		if(courseId == null){
			return renderFAIL("0250", response, header);
		}
		
		CcCourseOutline courseOutline = CcCourseOutline.dao.findByCourseId(courseId);
		
		Map<String, Object> map = Maps.newHashMap();
		
		if(courseOutline != null){
			map.put("courseOutlineId", courseOutline.getLong("id"));
			map.put("courseId", courseOutline.getLong("course_id"));
			map.put("createDate", courseOutline.getDate("create_date"));
			map.put("modifyDate", courseOutline.getDate("modify_date"));
			map.put("status", courseOutline.getInt("status"));
			map.put("statusName", DictUtils.findLabelByTypeAndKey("courseOutlineStatus", courseOutline.getInt("status")));
			map.put("content", courseOutline.getStr("content"));
			map.put("auditComment", courseOutline.getStr("auditComment"));
		}
		
		return renderSUC(map, response, header);
		
	}
	
}
