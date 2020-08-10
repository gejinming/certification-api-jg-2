package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程大纲下是否存在内容
 * 
 * @author xzl
 * 
 * @date 2018年1月31日15:47:02
 *
 */
@Service("EM00732")
@Transactional(readOnly=false)
public class EM00732 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {

		Map<String, Object> param = request.getData();
		Map<String, Boolean> result = new HashMap<>();
		Long courseOutlineId = paramsLongFilter(param.get("courseOutlineId"));
		if (courseOutlineId == null) {
			return renderFAIL("0531", response, header);
		}

		Boolean isExistContent = !CcCourseOutlineModule.dao.findFilteredByColumn("course_outline_id", courseOutlineId).isEmpty()
				|| !CcCourseOutlineIndications.dao.findFilteredByColumn("course_outline_id", courseOutlineId).isEmpty()
				|| !CcCourseOutlineTeachingContent.dao.findFilteredByColumn("course_outline_id", courseOutlineId).isEmpty()
				|| !CcCourseOutlineSecondaryContent.dao.findFilteredByColumn("course_outline_id", courseOutlineId).isEmpty()
				|| !CcCourseOutlineTableName.dao.findFilteredByColumn("course_outline_id", courseOutlineId).isEmpty()
				|| !CcCourseOutlineHeader.dao.findFilteredByColumn("course_outline_id", courseOutlineId).isEmpty()
				|| !CcCourseOutlineTableDetail.dao.findFilteredByColumn("course_outline_id", courseOutlineId).isEmpty();

		result.put("isExistContent", isExistContent);
		return renderSUC(result, response, header);
	}

}
