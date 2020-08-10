package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradecompose;

import com.gnet.utils.DictUtils;

/**
 * 通过开课课程成绩组成元素查看开课课程与成绩组件的联系详情 
 * 
 * @author xzl
 *
 */
@Service("EM00525")
@Transactional(readOnly=true)
public class EM00525 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		if (id == null) {
			return renderFAIL("0475", response, header);
		}
		
		CcCourseGradecompose courseGradecompose = CcCourseGradecompose.dao.findDetailById(id);
		
		if (courseGradecompose == null) {
			return renderFAIL("0476", response, header);
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("id", courseGradecompose.getLong("id"));
		map.put("courseName", courseGradecompose.getStr("courseName"));
		map.put("resultType", courseGradecompose.getInt("resultType"));
		map.put("resultTypeName", DictUtils.findLabelByTypeAndKey("courseResultType", courseGradecompose.getInt("resultType")));
		map.put("startYear", courseGradecompose.getInt("startYear"));
		map.put("endYear", courseGradecompose.getInt("endYear"));
		map.put("term", courseGradecompose.getInt("term"));
		map.put("termName", DictUtils.findLabelByTypeAndKey("term", courseGradecompose.getInt("term")));
		map.put("termType", courseGradecompose.getInt("termType"));
		map.put("termTypeName", DictUtils.findLabelByTypeAndKey("termType", courseGradecompose.getInt("termType")));
		map.put("grade", courseGradecompose.getInt("grade"));
		map.put("gradecomposeName", courseGradecompose.getStr("gradecomposeName"));
		map.put("teacherName", courseGradecompose.getStr("teacherName"));
		
		return renderSUC(map, response, header);
	}

}




