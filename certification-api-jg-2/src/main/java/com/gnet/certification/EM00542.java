package com.gnet.certification;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.gnet.model.admin.CcGradecomposeIndicationScoreRemark;
import com.gnet.response.ServiceResponse;
import com.gnet.service.CcEduindicationStuScoreService;
import com.gnet.service.CcGradecomposeIndicationScoreRemarkService;
import com.gnet.utils.PriceUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;


/**
 * 编辑开课课程成绩组成元素指标点关联表
 * 
 * @author XZL
 * 
 * @date 2016年07月07日 20:45:05
 *
 */
@Service("EM00542")
@Transactional(readOnly=false)
public class EM00542 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		//开课课程成绩组成元素指标点关联编号
		Long id = paramsLongFilter(param.get("id"));
		//指标点编号
		Long indicationId = paramsLongFilter(param.get("indicationId"));
		Long courseGradecomposeId = paramsLongFilter(param.get("courseGradecomposeId"));
		BigDecimal weight = paramsBigDecimalFilter(param.get("weight"));
		BigDecimal maxScore = paramsBigDecimalFilter(param.get("maxScore"));
		List<HashMap> scoreSectionRemarks = paramsJSONArrayFilter(param.get("scoreSectionRemarks"), HashMap.class);
		
		if(id == null){
			return renderFAIL("0496", response, header);
		}
		if(weight == null){
			return renderFAIL("0491", response, header);
		}
		if(indicationId == null){
			return renderFAIL("0230", response, header);
		}
		if(courseGradecomposeId == null){
			return renderFAIL("0475", response, header);
		}
		if(weight.equals(CcCourseGradecomposeIndication.MIN_WEIGHT) || PriceUtils.greaterThan(CcCourseGradecomposeIndication.MIN_WEIGHT, weight)){
			return renderFAIL("0763", response, header);
		}
		//单个权重不能超过1
		if(PriceUtils.greaterThan(weight, CcCourseGradecomposeIndication.MAX_WEIGHT)){
			return renderFAIL("0494", response, header);
		}
		CcCourseGradecomposeIndication courseGradecomposeIndication = CcCourseGradecomposeIndication.dao.findDetailById(id);
		if(courseGradecomposeIndication == null){
			return renderFAIL("0476", response, header);
		}
		
		CcCourseGradecompose courseGradecompose = CcCourseGradecompose.dao.findFilteredById(courseGradecomposeId);
		if(courseGradecompose == null){
			return renderFAIL("0471", response, header);
		}
		
		if(CcCourseGradecompose.DIRECT_INPUT_SCORE.equals(courseGradecompose.getInt("input_score_type")) && maxScore == null){
			return renderFAIL("0803", response, header);
		}
		
		//(保存之前先验证一下)同一课程同一指标点下的所有成绩权重之和不能超过1
		BigDecimal allWeight = new BigDecimal(0);
		//编辑对象原本的权重
		BigDecimal existWeight = new BigDecimal(0);
	    //如果相同则表明没有选择新的开课课程成绩组成编号，开课课程编号就不需要再查询数据库
	    if(courseGradecomposeId.equals(courseGradecomposeIndication.getLong("course_gradecompose_id"))){
	    	//同一课程同一指标点下的开课课程成绩组成元素指标点的权重和
	    	allWeight = CcCourseGradecomposeIndication.dao.calculateSumWeights(courseGradecomposeIndication.getLong("teacher_course_id"), indicationId);
	    }else{
			allWeight =  CcCourseGradecomposeIndication.dao.calculateSumWeights(courseGradecompose.getLong("teacher_course_id"), indicationId);
	    }
		
		if(courseGradecomposeId.equals(courseGradecomposeIndication.getLong("course_gradecompose_id")) && indicationId.equals(courseGradecomposeIndication.getLong("indication_id"))){
			existWeight = courseGradecomposeIndication.getBigDecimal("weight");
		}else{
			//因为courseGradecomposeId和indicationId在表中没有数据，所以existWeight可能为空
			existWeight = CcCourseGradecomposeIndication.dao.getWeightByIndicationIdAndCourseGradecomposeId(courseGradecomposeId, indicationId);
		}
		if(existWeight != null){
			//两者的差值
		    BigDecimal differenceWeight = PriceUtils._sub(allWeight, existWeight);
		    //修改后所有权重和  + 差值 是否大于1
		    BigDecimal newWeight = PriceUtils._add(weight, differenceWeight);
		    if(PriceUtils.greaterThan(newWeight, CcCourseGradecomposeIndication.MAX_WEIGHT)){
		    	return renderFAIL("493", response, header);
		    }
		}
		
	    Date date = new Date();
	    courseGradecomposeIndication.set("modify_date", date);
	    courseGradecomposeIndication.set("indication_id", indicationId);
	    courseGradecomposeIndication.set("course_gradecompose_id", courseGradecomposeId);
	    courseGradecomposeIndication.set("weight", weight);
	    courseGradecomposeIndication.set("max_score", maxScore);
	    
	    Map<String, Boolean> result = new HashMap<>();
		if(!courseGradecomposeIndication.update()){
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		//先删除之前存在的数据
		if(!CcGradecomposeIndicationScoreRemark.dao.deleteAllByColumn("gradecompose_indication_id", id, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		if(scoreSectionRemarks != null && !scoreSectionRemarks.isEmpty()){
			CcGradecomposeIndicationScoreRemarkService ccGradecomposeIndicationScoreRemarkService = SpringContextHolder.getBean(CcGradecomposeIndicationScoreRemarkService.class);
			ServiceResponse serviceResponse = ccGradecomposeIndicationScoreRemarkService.batchSave(scoreSectionRemarks, id);
			if(!serviceResponse.isSucc()){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return renderFAIL("0960", response, header, serviceResponse.getContent());
			}
		}

		/***************************************** 重新计算学生达成度报表  Start *************************************/
		List<Long> educlassIdList = Lists.newArrayList();
		List<Long> teacherCourseIdList = Lists.newArrayList();
		List<CcCourseGradecompose> ccCourseGradecomposes = CcCourseGradecompose.dao.findFilteredByColumn("id", courseGradecomposeId);
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

			CcEduindicationStuScoreService ccEduindicationStuScoreService = SpringContextHolder.getBean(CcEduindicationStuScoreService.class);

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
