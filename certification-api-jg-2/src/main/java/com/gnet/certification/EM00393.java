package com.gnet.certification;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcEvaluteLevel;
import com.jfinal.kit.StrKit;

/**
 * 编辑考评点得分层次关系表
 * 
 * @author sll
 * 
 * @date 2016年07月05日 18:29:45
 *
 */
@Service("EM00393")
@Transactional(readOnly=false)
@Deprecated
public class EM00393 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		String levelName = paramsStringFilter(param.get("levelName"));
		BigDecimal score = paramsBigDecimalFilter(param.get("score"));
		String requirement = paramsStringFilter(param.get("requirement"));
		Long teacherCourseId = paramsLongFilter(param.get("teacherCourseId"));
		Long indicationId = paramsLongFilter(param.get("indicationId"));
		
		if (id == null) {
			return renderFAIL("0430", response, header);
		}
		if (StrKit.isBlank(levelName)) {
			return renderFAIL("0432", response, header);
		}
		if (score == null) {
			return renderFAIL("0433", response, header);
		}
		if (teacherCourseId == null) {
			return renderFAIL("0435", response, header);
		}
		if (indicationId == null) {
			return renderFAIL("0436", response, header);
		}
		
		Date date = new Date();
		CcEvaluteLevel ccEvaluteLevel = CcEvaluteLevel.dao.findFilteredById(id);
		if(ccEvaluteLevel == null) {
			return renderFAIL("0431", response, header);
		}
		ccEvaluteLevel.set("modify_date", date);
		ccEvaluteLevel.set("level_name", levelName);
		ccEvaluteLevel.set("score", score);
		ccEvaluteLevel.set("requirement", requirement);
		ccEvaluteLevel.set("teacher_course_id", teacherCourseId);
		ccEvaluteLevel.set("indication_id", indicationId);
		
		Map<String, Object> result = new HashMap<>();
		result.put("isSuccess", ccEvaluteLevel.update());
		return renderSUC(result, response, header);
	}
	
}
