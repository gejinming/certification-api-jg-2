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
 * 通过课程大纲编号获得课程大纲内容
 * 
 * @author xzl
 * 
 * @date 2016年8月12日
 *
 */
@Deprecated
@Service("EM00572")
@Transactional(readOnly=false)
public class EM00572 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		
		//课程大纲编号
		Long courseOutlineId = paramsLongFilter(param.get("courseOutlineId"));
		
		if(courseOutlineId == null){
			return renderFAIL("0531", response, header);
		}
		
		CcCourseOutline courseOutline = CcCourseOutline.dao.findFilteredById(courseOutlineId);
		
		if(courseOutline == null){
			return renderFAIL("0537", response, header);
		}
		Map<String, Object> map = Maps.newHashMap();
		map.put("courseOutlineId", courseOutline.getLong("id"));
		map.put("courseId", courseOutline.getLong("course_id"));
		map.put("createDate", courseOutline.getDate("create_date"));
		map.put("modifyDate", courseOutline.getDate("modify_date"));
		map.put("status", courseOutline.getInt("status"));
		map.put("statusName", DictUtils.findLabelByTypeAndKey("courseOutlineStatus", courseOutline.getInt("status")));
		map.put("content", courseOutline.getStr("content"));
		map.put("auditComment", courseOutline.getStr("auditComment"));
		
		return renderSUC(map, response, header);
		
	}
	
}
