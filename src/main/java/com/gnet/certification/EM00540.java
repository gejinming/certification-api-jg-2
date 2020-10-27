package com.gnet.certification;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.CcTeacherCourse;
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
import com.gnet.response.ServiceResponse;
import com.gnet.service.CcEduindicationStuScoreService;
import com.gnet.service.CcGradecomposeIndicationScoreRemarkService;
import com.gnet.utils.PriceUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;


/**
 * 增加开课课程成绩组成元素指标点关联表
 *
 * updated: 增加开课课程成绩组成元素和课程目标关联
 * 
 * @author XZL
 * 
 * @date 2016年7月29日
 *
 */
@Service("EM00540")
@Transactional(readOnly=false)
public class EM00540 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		//课程目标编号
		Long indicationId = paramsLongFilter(param.get("indicationId"));
		Long courseGradecomposeId = paramsLongFilter(param.get("courseGradecomposeId"));
		BigDecimal weight = paramsBigDecimalFilter(param.get("weight"));
		BigDecimal maxScore = paramsBigDecimalFilter(param.get("maxScore"));
		String remark = paramsStringFilter(param.get("remark"));
		List<HashMap> scoreSectionRemarks = paramsJSONArrayFilter(param.get("scoreSectionRemarks"), HashMap.class);
		CcTeacherCourse teacherCourse = CcTeacherCourse.dao.findByCourseGradeComposeId(courseGradecomposeId);
		//达成度计算类型
		Integer resultType = teacherCourse.getInt("result_type");
		if(indicationId == null){
			return renderFAIL("1111", response, header);
		}
		
		if(courseGradecomposeId == null){
			return renderFAIL("0490", response, header);
		}
		
		if(weight == null){
			return renderFAIL("0491", response, header);
		}
		//不是财经大学的算法需要验证
		if (!resultType.equals(CcTeacherCourse.RESULT_TYPE_SCORE2)){
			if(weight.equals(CcCourseGradecomposeIndication.MIN_WEIGHT) || PriceUtils.greaterThan(CcCourseGradecomposeIndication.MIN_WEIGHT, weight)){
				return renderFAIL("0763", response, header);
			}
		}

		//单个权重不能超过1
		if(PriceUtils.greaterThan(weight, CcCourseGradecomposeIndication.MAX_WEIGHT)){
			return renderFAIL("0494", response, header);
		}
		//(保存之前先验证一下)同一课程同一指标点下的所有成绩权重之和不能超过1
		CcCourseGradecompose courseGradecompose = CcCourseGradecompose.dao.findFilteredById(courseGradecomposeId);
		if(CcCourseGradecompose.DIRECT_INPUT_SCORE.equals(courseGradecompose.getInt("input_score_type")) && maxScore == null){
			return renderFAIL("0803", response, header);
		}
		//通过开课课程成绩组成元素编号和指标点编号得到同一课程同一指标点下所有成绩组成信息和权重
		BigDecimal allWeight = CcCourseGradecomposeIndication.dao.calculateSumWeights(courseGradecompose.getLong("teacher_course_id"), indicationId);
		//同一开课课程的同一指标点下成绩组成元素不能重复
		//数据库已经存在的成绩组成编号
		List<Long> existGradecomposeIds = Lists.newArrayList();
		List<CcCourseGradecomposeIndication> courseGradecomposeIndications = CcCourseGradecomposeIndication.dao.findByTeacherCousreIdAndIndicationId(courseGradecompose.getLong("teacher_course_id"), indicationId);
		if(!courseGradecomposeIndications.isEmpty()){
			for(CcCourseGradecomposeIndication temp : courseGradecomposeIndications){
				existGradecomposeIds.add(temp.getLong("course_gradecompose_id"));
			}
		}

		if(existGradecomposeIds.contains(courseGradecomposeId)){
			return renderFAIL("0492", response, header);
		}
		
		//判断新增加的开课课程成绩组成元素编号和数据库已经存在的是否属于同一门课程
		existGradecomposeIds.add(courseGradecomposeId);
		//判断开课课程成绩组成元素编号的课程编号是否一致
		Long[] gradecomposeId = existGradecomposeIds.toArray(new Long[existGradecomposeIds.size()]);
		//查找开课课程成绩组成是否属于同一门课程，如果不是则返回错误
		if(CcCourseGradecompose.dao.isBelongSameCouse(gradecomposeId)){
			return renderFAIL("0495", response, header);
		}
		//数据库已经存在的权重和 和需要增加的权重和不能大于1
		allWeight = PriceUtils._add(allWeight, weight);
		if(PriceUtils.greaterThan(allWeight, CcCourseGradecomposeIndication.MAX_WEIGHT)){
			return renderFAIL("0493", response, header);
		}
		Date date = new Date();
		CcCourseGradecomposeIndication ccCourseGradecomposeIndication = new CcCourseGradecomposeIndication();
		ccCourseGradecomposeIndication.set("create_date", date);
		ccCourseGradecomposeIndication.set("modify_date", date);
		ccCourseGradecomposeIndication.set("indication_id", indicationId);
		ccCourseGradecomposeIndication.set("course_gradecompose_id", courseGradecomposeId);
		ccCourseGradecomposeIndication.set("weight", weight);
		ccCourseGradecomposeIndication.set("max_score", maxScore);
		ccCourseGradecomposeIndication.set("remark", remark);
		ccCourseGradecomposeIndication.set("is_del", Boolean.FALSE);
		
		Map<String, Object> result = new HashMap<>();
		if(!ccCourseGradecomposeIndication.save()){
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		if(scoreSectionRemarks != null && !scoreSectionRemarks.isEmpty()){
			CcGradecomposeIndicationScoreRemarkService ccGradecomposeIndicationScoreRemarkService = SpringContextHolder.getBean(CcGradecomposeIndicationScoreRemarkService.class);
			ServiceResponse serviceResponse = ccGradecomposeIndicationScoreRemarkService.batchSave(scoreSectionRemarks, ccCourseGradecomposeIndication.getLong("id"));
			if(!serviceResponse.isSucc()){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return renderFAIL("0960", response, header, serviceResponse.getContent());
			}
		}

		// 初始化开课课程下的教学班在改成绩组成课程目标下的平均分和总分
		Long teacherCourseId = courseGradecompose.getLong("teacher_course_id");
		CcEduindicationStuScoreService ccEduindicationStuScoreService = SpringContextHolder.getBean(CcEduindicationStuScoreService.class);

		if (!ccEduindicationStuScoreService.initEduClassGrade(teacherCourseId, ccCourseGradecomposeIndication.getLong("id"))) {
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		/***************************************** 重新计算学生达成度报表  Start *************************************/
		List<Long> educlassIdList = Lists.newArrayList();
		List<CcEduclass> ccEduclasses = CcEduclass.dao.findFilteredByColumn("teacher_course_id", teacherCourseId);
		for(CcEduclass temp : ccEduclasses) {
			Long educlassId = temp.getLong("id");
			educlassIdList.add(educlassId);
		}
 
		if (!educlassIdList.isEmpty()) {
			if (!ccEduindicationStuScoreService.calculate(educlassIdList.toArray(new Long[educlassIdList.size()]), courseGradecomposeId)) {
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}
		/***************************************** 重新计算学生达成度报表  End *************************************/
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}
}
