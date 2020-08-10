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
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * 题目分数录入
 * 
 * @author sll
 *
 */
@Service("EM00612")
@Transactional(readOnly=false)
public class EM00612 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Long studentId = paramsLongFilter(param.get("studentId"));
		Long detailId = paramsLongFilter(param.get("detailId"));
		Long courseGradecomposeId = paramsLongFilter(param.get("courseGradecomposeId"));
		BigDecimal score = paramsBigDecimalFilter(param.get("score"));
		String remark = paramsStringFilter(param.get("remark"));
		Long educlassId = paramsLongFilter(param.get("educlassId"));
		
		if (studentId == null) {
			return renderFAIL("0330", response, header);
		}
		if (detailId == null) {
			return renderFAIL("0450", response, header);
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
		
		if(CcCourseGradecomposeStudetail.dao.isExistStudentGrade(studentId, detailId)){
			return renderFAIL("0454", response, header);
		}
		
		Date date = new Date();
		
		CcCourseGradecomposeStudetail ccCourseGradecomposeStudetail = new CcCourseGradecomposeStudetail();
		
		ccCourseGradecomposeStudetail.set("create_date", date);
		ccCourseGradecomposeStudetail.set("modify_date", date);
		ccCourseGradecomposeStudetail.set("student_id", studentId);
		ccCourseGradecomposeStudetail.set("detail_id", detailId);
		ccCourseGradecomposeStudetail.set("score", score);
		ccCourseGradecomposeStudetail.set("remark", remark);
		ccCourseGradecomposeStudetail.set("is_del", Boolean.FALSE);
		
		Map<String, Object> result = new HashMap<>();
		if (!ccCourseGradecomposeStudetail.save()) {
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		result.put("id", ccCourseGradecomposeStudetail.getLong("id"));

		Long[] studentIds = new Long[]{studentId};
        Long[] detailIds = new Long[]{detailId};

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
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}

		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}
