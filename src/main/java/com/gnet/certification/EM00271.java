package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcIndicationCourse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 查看指标点课程关系表详情
 *
 * @author xzl
 *
 * @date 2017年11月17日16:23:52
 *
 */
@Service("EM00271")
@Transactional(readOnly=true)
public class EM00271 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {

		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();

		Long id = paramsLongFilter(param.get("id"));

		if (id == null) {
			return renderFAIL("0360", response, header);
		}

		CcIndicationCourse temp = CcIndicationCourse.dao.findFilteredById(id);
		if(temp == null) {
			return renderFAIL("0361", response, header);
		}

		Map<String, Object> map = new HashMap<>();

		map.put("id", temp.get("id"));
		map.put("createDate", temp.get("create_date"));
		map.put("modifyDate", temp.get("modify_date"));
		map.put("indicationId", temp.get("indication_id"));
		map.put("indicationContent", temp.get("indicationContent"));
		map.put("indicationIndexNum", temp.get("indicationIndexNum"));
		map.put("graduateIndexNum", temp.get("graduateIndexNum"));
		map.put("graduateId", temp.getLong("graduateId"));
		map.put("graduateContent", temp.getStr("graduateContent"));
		map.put("courseName", temp.get("courseName"));
		map.put("courseId", temp.get("course_id"));
		map.put("weight", temp.get("weight"));
		map.put("eduAim", temp.get("edu_aim"));
		map.put("means", temp.get("means"));
		map.put("source", temp.get("source"));
		map.put("way", temp.get("way"));

		return renderSUC(map, response, header);
	}
}
