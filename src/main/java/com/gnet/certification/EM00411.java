package com.gnet.certification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradeComposeDetail;
import com.gnet.model.admin.CcCourseGradecomposeDetailIndication;

/**
 * 查看成绩组成元素明细表详情
 * 
 * @author sll
 * 
 * @date 2016年07月06日 14:37:10
 *
 */
@Service("EM00411")
@Transactional(readOnly=true)
public class EM00411 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		
		if (id == null) {
			return renderFAIL("0450", response, header);
		}
		
		CcCourseGradeComposeDetail temp = CcCourseGradeComposeDetail.dao.findFilteredById(id);
		if(temp == null) {
			return renderFAIL("0451", response, header);
		}
		
	    List<CcCourseGradecomposeDetailIndication> courseGradecomposeDetailIndicationLists = CcCourseGradecomposeDetailIndication.dao.findFilteredByColumn("course_gradecompose_detail_id", id);
	    //返回内容过滤
	    List<Long> indicationIdList = new ArrayList<>();
	    for(CcCourseGradecomposeDetailIndication courseGradecomposeDetailIndication : courseGradecomposeDetailIndicationLists){
	    	indicationIdList.add(courseGradecomposeDetailIndication.getLong("indication_id"));
		}
	    
		Map<String, Object> map = new HashMap<>();
		
		map.put("id", temp.get("id"));
		map.put("createDate", temp.get("create_date"));
		map.put("modifyDate", temp.get("modify_date"));
		map.put("name", temp.get("name"));
		map.put("weight", temp.get("weight"));
		map.put("score", temp.get("score"));
		map.put("detail", temp.get("detail"));
		map.put("remark", temp.get("remark"));
		map.put("scaleFactor", temp.get("scale_factor"));
		map.put("courseGradecomposeId", temp.get("course_gradecompose_id"));
		map.put("indicationIdArray", indicationIdList.toArray(new Long[indicationIdList.size()]));
		
		return renderSUC(map, response, header);
	}

}
