package com.gnet.certification;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import com.gnet.service.CcEduindicationStuScoreService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;


import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradecompose;
import com.gnet.model.admin.CcCourseGradecomposeIndication;
import com.gnet.model.admin.CcEduclass;
import com.gnet.model.admin.CcScoreStuIndigrade;


/**
 * 批量删除开课课程成绩组成元素指标点关联表
 * 
 * @author XZL
 * 
 * @date 2016年07月07日 20:45:05
 *
 */
@Service("EM00543")
@Transactional(readOnly=false)
public class EM00543 extends BaseApi implements IApi{
	

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String,Boolean> result = new HashMap<>();
		
		Date date = new Date();
		
		List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		List<CcScoreStuIndigrade> scoreStuIndigradeLists = CcScoreStuIndigrade.dao.findFilteredByColumnIn("gradecompose_indication_id", idsArray);
		if(!scoreStuIndigradeLists.isEmpty()){
			return renderFAIL("0478", response, header);
		}

		//如果存在题目与课程指标点的关联则不允许删除
        List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndications = CcCourseGradecomposeIndication.dao.getByIds(idsArray);
		if(!ccCourseGradecomposeIndications.isEmpty()){
			return renderFAIL("0328", response, header);
		}
	
		CcEduindicationStuScoreService ccEduindicationStuScoreService = SpringContextHolder.getBean(CcEduindicationStuScoreService.class);
		
		/***************************************** 重新计算学生达成度报表  Start *************************************/
		List<Long> educlassIdList = Lists.newArrayList();
		List<Long> courseGradecomposeIdList = Lists.newArrayList();
		List<Long> teacherCourseIdList = Lists.newArrayList();
		List<CcCourseGradecomposeIndication> courseGradecomposeIndication = CcCourseGradecomposeIndication.dao.findFilteredByColumnIn("id", idsArray);
		for(CcCourseGradecomposeIndication temp : courseGradecomposeIndication) {
			Long courseGradecomposeId = temp.getLong("course_gradecompose_id");
			if(!courseGradecomposeIdList.contains(courseGradecomposeId)) {
				courseGradecomposeIdList.add(courseGradecomposeId);
			}
		}
		List<CcCourseGradecompose> ccCourseGradecomposes = CcCourseGradecompose.dao.findFilteredByColumnIn("id", courseGradecomposeIdList.toArray(new Long[courseGradecomposeIdList.size()]));
		for(CcCourseGradecompose temp : ccCourseGradecomposes) {
			Long teacherCourseId = temp.getLong("teacher_course_id");
			if(!teacherCourseIdList.contains(teacherCourseId)) {
				teacherCourseIdList.add(teacherCourseId);
			}
		}
		List<CcEduclass> ccEduclasses = CcEduclass.dao.findFilteredByColumnIn("teacher_course_id", teacherCourseIdList.toArray(new Long[teacherCourseIdList.size()]));
		for(CcEduclass temp : ccEduclasses) {
			Long educlassId = temp.getLong("id");
			educlassIdList.add(educlassId);
		}
 
		if (!educlassIdList.isEmpty()) {

//			CcEduindicationStuScoreService ccEduindicationStuScoreService = SpringContextHolder.getBean(CcEduindicationStuScoreService.class);

			if (!ccEduindicationStuScoreService.calculate(educlassIdList, courseGradecomposeIdList)) {
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}
		/***************************************** 重新计算学生达成度报表  End *************************************/
		
		
		// 删除CcCourseGradecomposeIndication
		if(!CcCourseGradecomposeIndication.dao.deleteAll(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		if (!ccEduindicationStuScoreService.deleteByGradecomposeIndicationIds(idsArray)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}	
	
