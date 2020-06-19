package com.gnet.certification;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.CcCourseGradecomposeIndication;
import com.gnet.service.CcEduindicationStuScoreService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradecomposeStudetail;
import com.gnet.service.CcCourseGradecomposeDetailService;
import com.gnet.utils.SpringContextHolder;

/**
 * 题目分数编辑
 * 
 * @author sll
 *
 */
@Service("EM00613")
@Transactional(readOnly=false)
public class EM00613 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		BigDecimal score = paramsBigDecimalFilter(param.get("score"));
		Long courseGradecomposeId = paramsLongFilter(param.get("courseGradecomposeId"));
		String remark = paramsStringFilter(param.get("remark"));
		Long educlassId = paramsLongFilter(param.get("educlassId"));
		
		if (id == null) {
			return renderFAIL("0461", response, header);
		}
		if (score == null) {
			return renderFAIL("0460", response, header);
		}
		if(courseGradecomposeId == null){
			return  renderFAIL("0475", response, header);
		}
		if (educlassId == null) {
			return renderFAIL("0380", response, header);
		}
		
		Date date = new Date();
		CcCourseGradecomposeStudetail ccCourseGradecomposeStudetail = CcCourseGradecomposeStudetail.dao.findFilteredById(id);
		if(ccCourseGradecomposeStudetail == null) {
			return renderFAIL("0463", response, header);
		}
		ccCourseGradecomposeStudetail.set("modify_date", date);
		ccCourseGradecomposeStudetail.set("score", score);
		ccCourseGradecomposeStudetail.set("remark", remark);
		
		Map<String, Boolean> result = new HashMap<>();
		result.put("isSuccess", ccCourseGradecomposeStudetail.update());

		Long[] studentIds = new Long[]{ccCourseGradecomposeStudetail.getLong("student_id")};
		Long[] detailIds = new Long[]{ccCourseGradecomposeStudetail.getLong("detail_id")};

		List<CcCourseGradecomposeIndication> courseGradecomposeIndicationList = CcCourseGradecomposeIndication.dao.findByDetailIdAndCourseGradecomposeId(courseGradecomposeId, detailIds);
		Long courseGradecomposeIndicationIds[] = new Long[courseGradecomposeIndicationList.size()];
		if(!courseGradecomposeIndicationList.isEmpty()){
			for(int i=0; i<courseGradecomposeIndicationList.size(); i++ ){
				courseGradecomposeIndicationIds[i] = courseGradecomposeIndicationList.get(i).getLong("id");
			}
		}

		CcCourseGradecomposeDetailService courseGradecomposeDetailService = SpringContextHolder.getBean(CcCourseGradecomposeDetailService.class);
		if(!courseGradecomposeDetailService.updateStudentGrade(courseGradecomposeIndicationIds, studentIds, detailIds)){
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		List<Long> educlassIdList = Lists.newArrayList();
		educlassIdList.add(educlassId);

		if (!educlassIdList.isEmpty()) {

			CcEduindicationStuScoreService ccEduindicationStuScoreService = SpringContextHolder.getBean(CcEduindicationStuScoreService.class);

			if (!ccEduindicationStuScoreService.calculate(educlassIdList.toArray(new Long[educlassIdList.size()]), courseGradecomposeId)) {
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}

		result.put("isSuccess", true);

		return renderSUC(result, response, header);
	}

}
