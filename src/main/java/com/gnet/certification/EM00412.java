package com.gnet.certification;

import java.math.BigDecimal;
import java.util.*;

import com.gnet.model.admin.*;
import com.gnet.service.CcCourseGradecompBatchService;
import com.gnet.service.CcstudentRaningLeveService;
import com.gnet.utils.PriceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.response.ServiceResponse;
import com.gnet.service.CcCourseGradecomposeDetailService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;

/**
 * 增加成绩组成元素明细表
 * 
 * @author sll
 * 
 * @date 2016年07月06日 14:37:10
 *
 */
@Service("EM00412")
@Transactional(readOnly=false)
public class EM00412 extends BaseApi implements IApi{

	@Autowired
	private CcCourseGradecompBatchService ccCourseGradecompBatchService;

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Map<String, Object> result = new HashMap<>();
		
		String name = paramsStringFilter(param.get("name"));
		BigDecimal score = paramsBigDecimalFilter(param.get("score"));
		String detail = paramsStringFilter(param.get("detail"));
		String remark = paramsStringFilter(param.get("remark"));
		Long courseGradecomposeId = paramsLongFilter(param.get("courseGradecomposeId"));
		List<Long> indicationIds = paramsJSONArrayFilter(param.get("indicationIdArray"), Long.class);
		//TODO 2020/10/21增加题目权重适用于浙江财经学院的达成度计算
		BigDecimal weight = paramsBigDecimalFilter(param.get("weight"));
		//	TODO 2020/07/07 gjm 增加了批次题目成绩
		Long batchId = paramsLongFilter(param.get("batchId"));
		// TODO 2020.12.10 评分表分析法也采用题目方式，增加比例系数
		BigDecimal scaleFactor = paramsBigDecimalFilter(param.get("scaleFactor"));
		if (StrKit.isBlank(name)) {
			return renderFAIL("0452", response, header);
		}
		if (score == null) {
			return renderFAIL("0453", response, header);
		}

		if(PriceUtils.isZero(score)){
			return renderFAIL("1130", response, header);
		}

		if (courseGradecomposeId == null) {
			return renderFAIL("0455", response, header);
		}
		//验证权重
		if(weight != null){
			if(weight.equals(CcCourseGradecomposeIndication.MIN_WEIGHT) || PriceUtils.greaterThan(CcCourseGradecomposeIndication.MIN_WEIGHT, weight)){
				return renderFAIL("0763", response, header);
			}
			//单个权重不能超过1
			if(PriceUtils.greaterThan(weight, CcCourseGradecomposeIndication.MAX_WEIGHT)){
				return renderFAIL("0494", response, header);
			}

		}
		CcTeacherCourse teacherCourse = CcTeacherCourse.dao.findByCourseGradeComposeId(courseGradecomposeId);
		//达成度计算类型
		Integer resultType = teacherCourse.getInt("result_type");
		Integer inputType = teacherCourse.getInt("input_score_type");
		Long educlassIds = teacherCourse.getLong("educlassId");
		CcCourseGradecompose courseGradecompose = CcCourseGradecompose.dao.findFilteredById(courseGradecomposeId);
		if(courseGradecompose == null){
			return renderFAIL("0471", response, header);
		}
		CcCourseGradeComposeDetail ccCourseGradeComposeDetail = new CcCourseGradeComposeDetail();
		//判断题号是否已经存在
		if (ccCourseGradeComposeDetail.isExisted(name,courseGradecomposeId,batchId)){
			return renderFAIL("2555", response, header);
		}

		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		Long courseGradeComposeDetailId = idGenerate.getNextValue();
		//成绩组成元素明细保存
		Date date = new Date();
		ccCourseGradeComposeDetail.set("id", courseGradeComposeDetailId);
		ccCourseGradeComposeDetail.set("create_date", date);
		ccCourseGradeComposeDetail.set("modify_date", date);
		ccCourseGradeComposeDetail.set("name", name);
		ccCourseGradeComposeDetail.set("score", score);
		ccCourseGradeComposeDetail.set("detail", detail);
		ccCourseGradeComposeDetail.set("remark", remark);
		ccCourseGradeComposeDetail.set("weight", weight);
		ccCourseGradeComposeDetail.set("course_gradecompose_id", courseGradecomposeId);
		//评分表分析法
		if (resultType==2){
			if (scaleFactor == null) {
				return renderFAIL("0375", response, header);
			}
			//判断比例系数是否大于0小于1
			if(PriceUtils.greaterThan(CcCourseGradecomposeIndication.MIN_WEIGHT, scaleFactor) || PriceUtils.greaterThan(scaleFactor, CcCourseGradecomposeIndication.MAX_WEIGHT)){
				return renderFAIL("0376", response, header);
			}
			ccCourseGradeComposeDetail.set("scale_factor", scaleFactor);
		}

		//batchId说明是批次的题目
		if (batchId !=null){
			ccCourseGradeComposeDetail.set("batch_id", batchId);
		}
		ccCourseGradeComposeDetail.set("is_del", Boolean.FALSE);
		
		//题目关联指标点批量保存
		List<CcCourseGradecomposeDetailIndication> saveList = Lists.newArrayList();
		//新增成绩组成与课程目标关联的权重
		ArrayList<CcCourseGradecomposeIndication> addGradecomposeIndications = new ArrayList<>();
		//修改
		ArrayList<CcCourseGradecomposeIndication> updateGradecomposeIndications = new ArrayList<>();
		if(!indicationIds.isEmpty()){
			//指标点可以增加的数据
			List<Long> addList = Lists.newArrayList();
			//判断指标点编号本次重复数据
			List<Long> repeatList = Lists.newArrayList();
			for(Long indicationId : indicationIds){

				if(!addList.contains(indicationId)){
					addList.add(indicationId);

					//验证权重并更新课程目标的权重
					if(weight != null){
						//(保存之前先验证一下)同一课程同一指标点下的所有成绩权重之和不能超过1
						//通过开课课程成绩组成元素编号和指标点编号得到同一课程同一指标点下所有成绩组成信息和权重
						BigDecimal allWeight = CcCourseGradecomposeIndication.dao.calculateSumWeights(courseGradecompose.getLong("teacher_course_id"), indicationId);
						//数据库已经存在的权重和 和需要增加的权重和不能大于1
						allWeight = PriceUtils._add(allWeight, weight);
						if(PriceUtils.greaterThan(allWeight, CcCourseGradecomposeIndication.MAX_WEIGHT)){
							return renderFAIL("0493", response, header);
						}
						//判断是否存在
						CcCourseGradecomposeIndication gradecomposeIndication = CcCourseGradecomposeIndication.dao.findGradecomposeIndication(courseGradecomposeId, indicationId);
						if (gradecomposeIndication==null){
							CcCourseGradecomposeIndication ccCourseGradecomposeIndication = new CcCourseGradecomposeIndication();
							ccCourseGradecomposeIndication.set("id", idGenerate.getNextValue());
							ccCourseGradecomposeIndication.set("create_date", date);
							ccCourseGradecomposeIndication.set("modify_date", date);
							ccCourseGradecomposeIndication.set("indication_id", indicationId);
							ccCourseGradecomposeIndication.set("course_gradecompose_id", courseGradecomposeId);
							ccCourseGradecomposeIndication.set("weight", weight);
							ccCourseGradecomposeIndication.set("remark", remark);
							ccCourseGradecomposeIndication.set("is_del", Boolean.FALSE);
							addGradecomposeIndications.add(ccCourseGradecomposeIndication);
						}else{
							//修改
							BigDecimal newWeight=gradecomposeIndication.getBigDecimal("weight").add(weight);
							gradecomposeIndication.set("modify_date", date);
							gradecomposeIndication.set("weight",newWeight);
							updateGradecomposeIndications.add(gradecomposeIndication);
						}

					}
				}else{
					repeatList.add(indicationId);
				}
					
			}
			if(!repeatList.isEmpty()){
				return renderFAIL("0464", response, header);
			}
			
			//如果直接输入指标点成绩需要进行数据验证
			if(CcCourseGradecompose.DIRECT_INPUT_SCORE.equals(courseGradecompose.getInt("input_score_type"))){
				CcCourseGradecomposeDetailService courseGradecomposeDetailService = SpringContextHolder.getBean(CcCourseGradecomposeDetailService.class);
				List<String> errorMessageList = courseGradecomposeDetailService.getErrorMessage(score, courseGradecomposeId, indicationIds, null, name);
				if(!errorMessageList.isEmpty()){
					return renderFAIL("0802", response, header, errorMessageList);
				}
			}
				
			for(Long indicationId: indicationIds){
				CcCourseGradecomposeDetailIndication temp =  new CcCourseGradecomposeDetailIndication();
				temp.set("id", idGenerate.getNextValue());
				temp.set("create_date", date);
				temp.set("modify_date", date);
				temp.set("course_gradecompose_detail_id", courseGradeComposeDetailId);
				temp.set("indication_id", indicationId);
				temp.set("is_del", Boolean.FALSE);
				saveList.add(temp);
			}		
		}
		if (addGradecomposeIndications.size()!=0){
			if(!CcCourseGradecomposeIndication.dao.batchSave(addGradecomposeIndications)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}
		if (updateGradecomposeIndications.size()!=0){
			if(!CcCourseGradecomposeIndication.dao.batchUpdate(updateGradecomposeIndications,"modify_date,weight")){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}
		if(!ccCourseGradeComposeDetail.save()){
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		if(!saveList.isEmpty()){
			if(!CcCourseGradecomposeDetailIndication.dao.batchSave(saveList)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}
		
		CcCourseGradecomposeDetailService courseGradecomposeDetailService = SpringContextHolder.getBean(CcCourseGradecomposeDetailService.class);
		ServiceResponse serviceResponse = courseGradecomposeDetailService.getServiceResponse(courseGradecomposeId);
		if(!serviceResponse.isSucc()){
			return renderFAIL("0804", response, header, serviceResponse.getContent());
		}
		//更新批次的总分
		if (batchId != null){
			boolean updateBatchScoreState = ccCourseGradecompBatchService.updateBatchScore(batchId);
			if (!updateBatchScoreState){
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}

		}
		//评分表分析法，新增题目按题目比例更新成绩
		if (resultType==2){
			//更新了比例就要重新计算成绩
			CcstudentRaningLeveService cstudentRaningLeveService = SpringContextHolder.getBean(CcstudentRaningLeveService.class);
			ServiceResponse serviceResponses = cstudentRaningLeveService.mangeRaningLeveScore(courseGradecomposeId,inputType,educlassIds,batchId);
			if(!serviceResponses.isSucc()){
				return renderFAIL("0804", response, header, serviceResponse.getContent());
			}
		}
		result.put("isSuccess", true);
		result.put("id", courseGradeComposeDetailId);
		return renderSUC(result, response, header);
	}
}
