package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseOutline;
import com.gnet.model.admin.CcCourseOutlineType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 可以指定执笔人和审核人的大纲列表
 * 
 * @author xzl
 * @Date 2017-8-23 15:15:17
 */
@Service("EM00710")
public class EM00710 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
        Long courseId = paramsLongFilter(params.get("courseId"));
		Long schoolId = UserCacheKit.getSchoolId(request.getHeader().getToken());

        if(courseId == null){
			return renderFAIL("0250", response, header);
		}

		if(schoolId == null){
			return renderFAIL("0084", response, header);
		}

		List<Map<String, Object>> list = Lists.newArrayList();

        List<CcCourseOutlineType> ccCourseOutlineTypeList = CcCourseOutlineType.dao.findByCourseId(courseId, schoolId);
        for(CcCourseOutlineType temp : ccCourseOutlineTypeList){
        	Integer status = temp.getInt("status");
			Map<String, Object> map = new HashMap<>();
			map.put("id", temp.getLong("id"));
			map.put("name", temp.getStr("name"));
			map.put("outlineTypeName", temp.getStr("outlineTypeName"));
			map.put("authorId", temp.getLong("author_id"));
			map.put("auditorId", temp.getLong("auditor_id"));
			map.put("authorName", temp.getStr("authorName"));
			map.put("auditorName", temp.getStr("auditorName"));
            map.put("canOperate", status == null || status < 3);
			list.add(map);
		}

		// 结果返回
		Map<String, Object> result = Maps.newHashMap();
        result.put("list", list);

		return renderSUC(result, response, header);
	}
	
}
