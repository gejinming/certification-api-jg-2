package com.gnet.certification;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradecomposeStudetail;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 查看学生的指定试卷的成绩组成元素明细接口
 * 
 * @author sll
 *
 */
@Service("EM00415")
@Transactional(readOnly=false)
public class EM00415 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Long studentId = paramsLongFilter(param.get("studentId"));
		Long courseGradecomposeId = paramsLongFilter(param.get("courseGradecomposeId"));
		
		if (studentId == null) {
			return renderFAIL("0462", response, header);
		}
		if (courseGradecomposeId == null) {
			return renderFAIL("0475", response, header);
		}
		
		List<CcCourseGradecomposeStudetail> ccCourseGradecomposeStudetailList = CcCourseGradecomposeStudetail.dao.findDetails(studentId, courseGradecomposeId);
		
		List<CcCourseGradecomposeStudetail> ccCourseGradecomposeStudetails = Lists.newArrayList();
		for (CcCourseGradecomposeStudetail temp: ccCourseGradecomposeStudetailList) {
			CcCourseGradecomposeStudetail ccCourseGradecomposeStudetail = new CcCourseGradecomposeStudetail();
			ccCourseGradecomposeStudetail.put("id", temp.get("id"));
			ccCourseGradecomposeStudetail.put("createDate", temp.get("create_date"));
			ccCourseGradecomposeStudetail.put("modifyDate", temp.get("modify_date"));
			ccCourseGradecomposeStudetail.put("studentId", temp.get("student_id"));
			ccCourseGradecomposeStudetail.put("detailId", temp.get("detail_id"));
			ccCourseGradecomposeStudetail.put("score", temp.get("score"));
			ccCourseGradecomposeStudetail.put("remark", temp.get("remark"));
			ccCourseGradecomposeStudetail.put("detailName", temp.get("detail_name"));
			ccCourseGradecomposeStudetail.put("detailScore", temp.get("detail_score"));
			ccCourseGradecomposeStudetail.put("detailContent", temp.get("detail_content"));
			ccCourseGradecomposeStudetail.put("detailRemark", temp.get("detail_remark"));
			ccCourseGradecomposeStudetails.add(ccCourseGradecomposeStudetail);
		}
		
		Map<String, Object> result = Maps.newHashMap();
		result.put("list", ccCourseGradecomposeStudetails);
		
		return renderSUC(result, response, header);
	}

}
