package com.gnet.certification;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradecomposeBatchIndication;
import com.gnet.model.admin.CcCourseGradecomposeIndication;
import com.gnet.model.admin.CcTeacherCourse;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.response.ServiceResponse;
import com.gnet.service.CcCourseGradecompBatchService;
import com.gnet.service.CcstudentRaningLeveService;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.kit.PathKit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

/**
 * 批次成绩直接录入形式添加课程目标
 * 
 * @author GJM
 * @Date 2020年08月26日
 */
@Service("EM01212")
public class EM01212 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		HashMap<Object, Object> result = new HashMap<>();
		Long courseGradeComposeId = paramsLongFilter(param.get("courseGradeComposeId"));
		JSONArray scoreStuIndigradeArray = paramsJSONArrayFilter(param.get("batchIndigrade"));
		//Long indicationId = paramsLongFilter(param.get("indicationId"));
		//BigDecimal indicationScore = paramsBigDecimalFilter(param.get("score"));
		if(param.containsKey("courseGradeComposeId") && courseGradeComposeId == null) {
			return renderFAIL("1009", response, header, "courseGradeComposeId的参数值非法");
		}
		if (scoreStuIndigradeArray == null){
			return renderFAIL("2572", response, header);
		}
		CcTeacherCourse teacherCourse = CcTeacherCourse.dao.findByCourseGradeComposeId(courseGradeComposeId);
		//达成度计算类型
		Integer resultType = teacherCourse.getInt("result_type");
		Integer inputType = teacherCourse.getInt("input_score_type");
		Long educlassIds = teacherCourse.getLong("educlassId");
		Date date = new Date();
		ArrayList<Long> batchIds = new ArrayList<>();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		List<CcCourseGradecomposeBatchIndication> batchIndigradeAddList = new ArrayList<>();
		List<CcCourseGradecomposeBatchIndication> batchIndigradeUpdateList = new ArrayList<>();
		for (int i=0;i<scoreStuIndigradeArray.size();i++){
			CcCourseGradecomposeBatchIndication batchIndication = new CcCourseGradecomposeBatchIndication();
			JSONObject map = (JSONObject) scoreStuIndigradeArray.get(i);
			Long batchId = map.getLong("batchId");
			Long indicationId = map.getLong("indicationId");
			Object scoreS = map.get("score");
			Object weightS = map.get("weight");
			if (batchId==null || indicationId== null){
				return renderFAIL("2572", response, header);
			}
			if (!batchIds.contains(batchId)){
				batchIds.add(batchId);
			}
			batchIndication.set("create_date",date);
			batchIndication.set("modify_date",date);
			batchIndication.set("indication_id",indicationId);
			batchIndication.set("batch_id",batchId);
			//判断成绩组成是否关联课程目标
			CcCourseGradecomposeIndication gradecomposeIndication = CcCourseGradecomposeIndication.dao.findGradecomposeIndication(courseGradeComposeId, indicationId);
			if (gradecomposeIndication ==null ){
				return renderFAIL("2573", response, header);
			}
			if (scoreS=="" || scoreS == null || scoreS.equals("")){
				batchIndication.set("score",new BigDecimal("0"));
				batchIndication.set("is_del",Boolean.TRUE);
			}else {

				BigDecimal score = map.getBigDecimal("score");
				//评分表分析法 score就是比例系数
				if (resultType==2){
					batchIndication.set("scale_factor",score);
				}else{
					batchIndication.set("score",score);
				}

				batchIndication.set("is_del",Boolean.FALSE);
			}
			if ( weightS != null && !weightS.equals("")){

				batchIndication.set("weight",map.getBigDecimal("weight"));
			}else{
				batchIndication.set("weight",map.getBigDecimal("0"));
			}

			//判断是否存在
			List<CcCourseGradecomposeBatchIndication> batchIndicationList = CcCourseGradecomposeBatchIndication.dao.findBatchIndicationList(batchId, indicationId);
			if (batchIndicationList.size() !=0){
				batchIndication.put("id",batchIndicationList.get(0).getLong("id"));
				batchIndigradeUpdateList.add(batchIndication);
			}else {
				batchIndication.put("id",idGenerate.getNextValue());
				batchIndigradeAddList.add(batchIndication);
			}
		}
		if (!batchIndigradeAddList.isEmpty()) {
			if (!CcCourseGradecomposeBatchIndication.dao.batchSave(batchIndigradeAddList)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				result.put("isSuccess", Boolean.FALSE);
				return renderSUC(result, response, header);
			}
		}
		if (!batchIndigradeUpdateList.isEmpty()){

			if (!CcCourseGradecomposeBatchIndication.dao.batchUpdate(batchIndigradeUpdateList,"modify_date,score,weight,is_del,scale_factor")){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				result.put("isSuccess", Boolean.FALSE);
				return renderSUC(result, response, header);
			}
		}

		//统计成绩组成的课程目标总分
		CcCourseGradecompBatchService courseGradecomposeDetailService = SpringContextHolder.getBean(CcCourseGradecompBatchService.class);
		if (!courseGradecomposeDetailService.batchIndicationScore(courseGradeComposeId)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", Boolean.FALSE);
			return renderSUC(result, response, header);
		}
		//评分表分析法
		if (resultType==2){
			for (int i=0;i<batchIds.size();i++){
				Long batchId = batchIds.get(i);
				//更新了比例就要重新计算成绩
				CcstudentRaningLeveService cstudentRaningLeveService = SpringContextHolder.getBean(CcstudentRaningLeveService.class);
				ServiceResponse serviceResponse = cstudentRaningLeveService.mangeRaningLeveScore(courseGradeComposeId,inputType,educlassIds,batchId);
				if(!serviceResponse.isSucc()){
					return renderFAIL("0804", response, header, serviceResponse.getContent());
				}
			}

		}

		result.put("isSuccess", Boolean.TRUE);
		return renderSUC(result, response, header);
	}


}
