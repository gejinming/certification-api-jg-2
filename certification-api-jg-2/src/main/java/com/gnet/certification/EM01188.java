package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourse;
import com.gnet.model.admin.CcCourseGroup;
import com.gnet.model.admin.CcCourseGroupMange;
import com.gnet.model.admin.CcCourseGroupMangeGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查看课程分组--限选表详情
 * 
 * @author GJM
 * 
 * @date 2016年07月14日 11:10:53
 *
 */
@Service("EM01188")
@Transactional(readOnly=true)
public class EM01188 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		
		if (id == null) {
			return renderFAIL("0560", response, header);
		}

		CcCourseGroupMange temp = CcCourseGroupMange.dao.findFilteredById(id);
		if(temp == null) {
			return renderFAIL("0561", response, header);
		}
		
		List<CcCourse> courseList = CcCourse.dao.coursesLists(id);
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("id", temp.get("id"));
		map.put("createDate", temp.get("create_date"));
		map.put("modifyDate", temp.get("modify_date"));
		map.put("credit", temp.get("credit"));
		map.put("typeId", temp.get("type"));
		map.put("allHours", temp.get("all_hours"));
		map.put("planId", temp.get("plan_id"));
		map.put("remark", temp.get("remark"));
		map.put("groupName",temp.get("group_name"));
		// 获取course中的数据
		List<CcCourse> courseListReturn = new ArrayList<>();
		for(CcCourse course : courseList) {
			CcCourse courseTemp = new CcCourse();
			courseTemp.set("id", course.getLong("id"));
			courseTemp.set("name", course.getStr("name"));
			//courseTemp.set("code", course.getStr("code"));
			courseListReturn.add(courseTemp);
		}
		map.put("courseList", courseListReturn);
		
		return renderSUC(map, response, header);
	}

}
