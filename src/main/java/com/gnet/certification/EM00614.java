package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.CcCourseGradecomposeIndication;
import com.gnet.service.CcEduindicationStuScoreService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradecomposeStudetail;
import com.gnet.service.CcCourseGradecomposeDetailService;
import com.gnet.utils.SpringContextHolder;

/**
 * 学生题目分数删除
 * 
 * @author xzl
 * 
 * @date 2016年12月23日
 *
 */
@Service("EM00614")
@Transactional(readOnly=false)
public class EM00614 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		Map<String,Boolean> result = new HashMap<>();

		Long id = paramsLongFilter(param.get("id"));
		Long educlassId = paramsLongFilter(param.get("educlassId"));
		if(id == null){
			return renderFAIL("0461", response, header);
		}
		if (educlassId == null) {
			return renderFAIL("0380", response, header);
		}

		Long courseGradecomposeId = paramsLongFilter(param.get("courseGradecomposeId"));
		if(courseGradecomposeId == null){
			return  renderFAIL("0475", response, header);
		}

		CcCourseGradecomposeStudetail courseGradecomposeStudetail = CcCourseGradecomposeStudetail.dao.findFilteredById(id);
		if(courseGradecomposeStudetail == null){
			return renderFAIL("0463", response, header);
		}
		
		Long[] idArray = {id};
		
		Date date = new Date();
		if(!CcCourseGradecomposeStudetail.dao.deleteAll(idArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		Long[] studentIds = new Long[]{courseGradecomposeStudetail.getLong("student_id")};
		Long[] detailIds = new Long[]{courseGradecomposeStudetail.getLong("detail_id")};

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
	
