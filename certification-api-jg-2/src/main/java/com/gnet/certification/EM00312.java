package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradecomposeIndication;
import com.gnet.model.admin.CcEduclass;
import com.gnet.model.admin.CcEduindicationStuScore;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.service.CcEduindicationStuScoreService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 增加教学班
 * 
 * @author SY
 * 
 * @date 2016年07月01日 14:41:11
 *
 */
@Service("EM00312")
@Transactional(readOnly=false)
public class EM00312 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		String educlassName = paramsStringFilter(param.get("educlassName"));
		Long teacherCourseId = paramsLongFilter(param.get("teacherCourseId"));
		if (StrKit.isBlank(educlassName)) {
			return renderFAIL("0382", response, header);
		}
		if (teacherCourseId == null) {
			return renderFAIL("0383", response, header);
		}

		if(CcEduclass.dao.isExisted(null, teacherCourseId, educlassName)){
			return renderFAIL("0387", response, header);
		}

		Date date = new Date();
		
		CcEduclass ccEduclass = new CcEduclass();
		
		ccEduclass.set("create_date", date);
		ccEduclass.set("modify_date", date);
		ccEduclass.set("educlass_name", educlassName);
		ccEduclass.set("teacher_course_id", teacherCourseId);
		ccEduclass.set("is_del", Boolean.FALSE);

		Map<String, Object> resultMap = new HashMap<>();

		if (!ccEduclass.save()) {
			resultMap.put("isSuccess", Boolean.FALSE);
			return renderSUC(resultMap, response, header);
		}

		List<Long> eduClassIds = Lists.newArrayList(ccEduclass.getLong("id"));

		if (eduClassIds.isEmpty()) {
			resultMap.put("isSuccess", Boolean.FALSE);
			return renderSUC("0388", response, header);
		}

		CcEduindicationStuScoreService ccEduindicationStuScoreService = SpringContextHolder.getBean(CcEduindicationStuScoreService.class);

		if (!ccEduindicationStuScoreService.initEduClassGrade(eduClassIds, teacherCourseId)) {
			resultMap.put("isSuccess", Boolean.FALSE);
			return renderSUC(resultMap, response, header);
		}

		resultMap.put("isSuccess", Boolean.TRUE);
		resultMap.put("id", ccEduclass.getLong("id"));
		
		return renderSUC(resultMap, response, header);
	}
}
