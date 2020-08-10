package com.gnet.certification;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradecompose;
import com.gnet.service.CcCourseGradecomposeIndicationService;
import com.gnet.utils.SpringContextHolder;


/**
 * 开课课程成绩组成元素更新占比和其他分值
 * 
 * @author SY
 * 
 * @date 2017年8月15日
 *
 */
@Service("EM00530")
@Transactional(readOnly=false)
public class EM00530 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Map<String, Object> result = new HashMap<>();
		
		//开课课程编号
		Long teacherCourseId = paramsLongFilter(param.get("teacherCourseId"));
		
		//成绩组成元素编号
		Long gradecomposeId = paramsLongFilter(param.get("gradecomposeId"));
		
		// 满分
		BigDecimal fullScore = paramsBigDecimalFilter(param.get("fullScore"));
		
		// 占比
		Integer percentage = paramsIntegerFilter(param.get("percentage"));
		
		if(gradecomposeId == null){
			return renderFAIL("0455", response, header);
		}
	
		if (teacherCourseId == null) {
			return renderFAIL("0310", response, header);
		}

		// 验证占比是否小于等于0或大于100
		if(percentage != null && (percentage > 100 || percentage <= 0)) {
			return renderFAIL("0904", response, header);
		}
		List<CcCourseGradecompose> ccCourseGradecomposeOld = CcCourseGradecompose.dao.findFilteredByColumn("teacher_course_id", teacherCourseId);
		Integer allPercentage = percentage ==  null ? 0 : percentage;
		for(CcCourseGradecompose temp : ccCourseGradecomposeOld) {
			if(gradecomposeId.equals(temp.getLong("gradecompose_id"))) {
				// 如果是自己。就直接跳过，因为按照新的数值来
				continue;
			}
			Integer tempPercentage = temp.getInt("percentage");
			tempPercentage = tempPercentage == null ? 0 : tempPercentage;
			allPercentage = allPercentage + tempPercentage;
			if(allPercentage > 100) {
				return renderFAIL("0904", response, header);	
			}
		}
		
		Map<String, Object> paras = new HashMap<>();
		paras.put("teacher_course_id", teacherCourseId);
		paras.put("gradecompose_id", gradecomposeId);
		CcCourseGradecompose ccCourseGradecompose = CcCourseGradecompose.dao.findFirstByColumn(paras, Boolean.TRUE);
		if(ccCourseGradecompose == null){
			return renderFAIL("0451", response, header);
		}
		
		ccCourseGradecompose.set("modify_date", new Date());
		if(fullScore != null) {
			// 计算一下当前成绩组成的所有指标点之和，1：小于等于满分，则保存之和，否则，保存满分
			Long courseGradecompostId = ccCourseGradecompose.getLong("id");
			CcCourseGradecomposeIndicationService ccCourseGradecomposeIndicationService = SpringContextHolder.getBean(CcCourseGradecomposeIndicationService.class);
			BigDecimal oldFullScore = ccCourseGradecomposeIndicationService.caculateCourseGradecomposeScoreButOtherScore(courseGradecompostId);
			Integer compare = fullScore.compareTo(oldFullScore);
			if(compare == 1) {
				// 当满分大于指标点之和的时候，按照满分减去指标点之和来作为其他分数
				ccCourseGradecompose.set("other_score", fullScore.subtract(oldFullScore));		
			} else {
				// 当满分 小于等于 指标点之和的时候， 按照指标点之和来。所以是0
				ccCourseGradecompose.set("other_score", new BigDecimal(0));		
			}
		}
		if(percentage != null) {
			ccCourseGradecompose.set("percentage", percentage);
		}
		
		Boolean isSuccess = ccCourseGradecompose.update();
		
		result.put("isSuccess", isSuccess);	
		return renderSUC(result, response, header);
	}
}
