package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseOutline;
import com.gnet.model.admin.User;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 负责人可开始任务的大纲列表
 * 
 * @author xzl
 * @Date 2017-08-24 19:56:15
 */
@Service("EM00719")
public class EM00719 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
        Long courseId = paramsLongFilter(params.get("courseId"));

        if(courseId == null){
			return renderFAIL("0250", response, header);
		}

		List<CcCourseOutline> ccCourseOutlineList =  CcCourseOutline.dao.findListByCourseId(courseId, CcCourseOutline.STATUS_NOT_SUBMIT, null, true);
		List<Map<String, Object>> list = Lists.newArrayList();

		for(CcCourseOutline temp : ccCourseOutlineList){
			Map<String, Object> map = new HashMap<>();
			map.put("id", temp.getLong("id"));
			map.put("createDate", temp.getDate("create_date"));
			map.put("modifyDate", temp.getDate("modify_date"));
			map.put("name", temp.getStr("name"));
			map.put("outlineTypeName", temp.getStr("outlineTypeName"));
            map.put("outlineTypeId", temp.getLong("outline_type_id"));
			list.add(map);
		}

		Map<String, Object> result = new HashMap<>();
		result.put("list", list);
		return renderSUC(result, response, header);
	}
	
}
