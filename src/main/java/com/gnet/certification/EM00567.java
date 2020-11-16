package com.gnet.certification;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


/**
 * 通过教学班编号和开课课程成绩组成元素编号得到课程指标下不同成绩组成的学生成绩
 * 
 * @author xzl
 * 
 * @date 2016年11月12日
 * 
 */
@Service("EM00567")
@Transactional(readOnly=true)
public class EM00567 extends BaseApi implements IApi {
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		Map<String, Object> result = new HashMap<>();
		
		//教学班编号
	    Long educlassId = paramsLongFilter(param.get("educlassId"));
	    //判断输入方式 3多批次题目录入，4多批次指标点直接录入
		Integer inputType =paramsIntegerFilter(param.get("inputType"));
		
	    //开课课程成绩组成编号
	    Long courseGradecomposeId = paramsLongFilter(param.get("courseGradecomposeId"));
		Long batchId = paramsLongFilter(param.get("batchId"));
		if(courseGradecomposeId == null){
			return renderFAIL("0475", response, header);
		}
		
		if(educlassId == null){
			return renderFAIL("380", response, header);
		}

		//根据教学班为条件获取
		List<CcEduclassStudentStudy> educlassStudentStudies = CcEduclassStudentStudy.dao.findFilteredByColumn("class_id", educlassId);
		List<Map<String, Object>> educlassStudentStudyList = new ArrayList<>();
		for(CcEduclassStudentStudy temp : educlassStudentStudies) {
			Map<String, Object> tempMap = Maps.newHashMap();
			tempMap.put("remark", temp.getInt("remark"));
			tempMap.put("studentId", temp.getLong("student_id"));
			tempMap.put("isRetake", temp.getBoolean("is_retake"));
			educlassStudentStudyList.add(tempMap);
		}
		//返回内容过滤
		List<Map<String, Object>> courseGradecomposeIndicationLists = Lists.newArrayList();
		//如果batchId不为空且是多批次题目录入方式则为查询批次的汇总信息
		if (batchId !=null && inputType==3){

				ArrayList<Long> batchIdList = new ArrayList<>();
				batchIdList.add(batchId);
				List<CcCourseGradecomposeDetailIndication> indictionScore = CcCourseGradecomposeDetailIndication.dao.getIndictionScore(batchIdList);
				//如果为空就直接返回
				if(indictionScore.isEmpty()){
					result.put("courseGradecomposeIndicationLists", indictionScore);
					return renderSUC(result, response, header);
				}

				for(CcCourseGradecomposeDetailIndication temp : indictionScore){
					Map<String, Object> courseGradecomposeIndication = Maps.newHashMap();
					courseGradecomposeIndication.put("courseGradecomposeIndicationId", temp.getLong("indication_id"));
					courseGradecomposeIndication.put("indicationSort", temp.getInt("indicationSort"));
					courseGradecomposeIndication.put("indicationContent", temp.getStr("indicationContent"));
					courseGradecomposeIndication.put("indicationId", temp.getLong("indication_id"));
					courseGradecomposeIndication.put("maxScore", temp.getBigDecimal("score"));
					courseGradecomposeIndicationLists.add(courseGradecomposeIndication);
				}


		}//多批次直接录入
		else if (batchId !=null && inputType==4){
			List<CcCourseGradecomposeBatchIndication> courseBatchGradecomposeId = CcCourseGradecomposeBatchIndication.dao.findByCourseBatchGradecomposeId(courseGradecomposeId,batchId);
			//如果为空就直接返回
			if(courseBatchGradecomposeId.isEmpty()){
				result.put("courseGradecomposeIndicationLists", courseBatchGradecomposeId);
				return renderSUC(result, response, header);
			}

			for(CcCourseGradecomposeBatchIndication temp : courseBatchGradecomposeId){
				Map<String, Object> courseGradecomposeIndication = Maps.newHashMap();
				courseGradecomposeIndication.put("courseGradecomposeIndicationId", temp.getLong("courseGradecomposeIndicationId"));
				courseGradecomposeIndication.put("indicationSort", temp.getInt("indicationSort"));
				courseGradecomposeIndication.put("indicationContent", temp.getStr("indicationContent"));
				courseGradecomposeIndication.put("indicationId", temp.getLong("indicationId"));
				courseGradecomposeIndication.put("maxScore", temp.getBigDecimal("maxScore"));
				courseGradecomposeIndicationLists.add(courseGradecomposeIndication);
			}
		}else{
			//开课课程成绩组成元素指标点关联list
			List<CcCourseGradecomposeIndication> courseGradecomposeIndications = CcCourseGradecomposeIndication.dao.findByCourseGradecomposeId(courseGradecomposeId);
			//如果为空就直接返回
			if(courseGradecomposeIndications.isEmpty()){
				result.put("courseGradecomposeIndicationLists", courseGradecomposeIndications);
				return renderSUC(result, response, header);
			}

			for(CcCourseGradecomposeIndication temp : courseGradecomposeIndications){
				Map<String, Object> courseGradecomposeIndication = Maps.newHashMap();
				courseGradecomposeIndication.put("courseGradecomposeIndicationId", temp.getLong("courseGradecomposeIndicationId"));
				courseGradecomposeIndication.put("indicationSort", temp.getInt("indicationSort"));
				courseGradecomposeIndication.put("indicationContent", temp.getStr("indicationContent"));
				courseGradecomposeIndication.put("indicationId", temp.getLong("indicationId"));
				courseGradecomposeIndication.put("maxScore", temp.getBigDecimal("maxScore"));
				courseGradecomposeIndicationLists.add(courseGradecomposeIndication);
			}
		}



		Map<String, Object> studentInScoreMap = Maps.newLinkedHashMap();
		//某个教学班的所有学生
		List<CcStudent> studentLits = CcStudent.dao.findByEduclassId(educlassId);
		if(studentLits.isEmpty()){
			result.put("studentInScoreMap", studentInScoreMap);
			return renderSUC(result, response, header);
		}


		//某个教学班在某一个开课课程成绩组成下的已经有成绩的学生
		if (batchId !=null && inputType==3){
			//多批次题目录入
			List<CcCourseGradecomposeStudetail> ccCourseGradecomposeStudetails = CcCourseGradecomposeStudetail.dao.sumBatchStudetail(batchId, courseGradecomposeId, educlassId);
			for (CcCourseGradecomposeStudetail ccScoreStuIndigrade:ccCourseGradecomposeStudetails){
				String studentNo = ccScoreStuIndigrade.getStr("student_no");
				Map<String, Object> studentInfo = null;
				if (studentInScoreMap.get(studentNo) == null) {
					studentInfo = Maps.newHashMap();
					// 学生基本信息
					studentInfo.put("id", ccScoreStuIndigrade.getLong("student_id"));
					studentInfo.put("studentNo", studentNo);
					studentInfo.put("studentName", ccScoreStuIndigrade.getStr("student_name"));
					// 学生成绩项保存
					studentInfo.put("scoreMap", new HashMap<Long, BigDecimal>());
					studentInScoreMap.put(studentNo, studentInfo);
				} else{
					studentInfo = (Map<String, Object>) studentInScoreMap.get(studentNo);
				}
				Map<Long, Object> scoreMap = (Map<Long, Object>) studentInfo.get("scoreMap");
				Map<String, Object> scoreItem = Maps.newHashMap();
				scoreItem.put("grade", ccScoreStuIndigrade.getBigDecimal("grade"));
				scoreItem.put("type", ccScoreStuIndigrade.get("type"));
				scoreItem.put("levelDetailId", ccScoreStuIndigrade.get("level_detail_id"));
				scoreItem.put("scoreStuIndigradeId", ccScoreStuIndigrade.get("id"));
				scoreMap.put(ccScoreStuIndigrade.getLong("gradecompose_indication_id"), scoreItem);

			}
			//多批次指标点直接录入
		}else if (batchId !=null && inputType==4){
			List<CcScoreStuIndigradeBatch> ccScoreStuIndigrades = CcScoreStuIndigradeBatch.dao.findDetailByClassIdAndCourseGradecomposeId(educlassId, courseGradecomposeId,batchId);
			for (CcScoreStuIndigradeBatch ccScoreStuIndigrade : ccScoreStuIndigrades) {
				String studentNo = ccScoreStuIndigrade.getStr("student_no");
				Map<String, Object> studentInfo = null;
				if (studentInScoreMap.get(studentNo) == null) {
					studentInfo = Maps.newHashMap();
					// 学生基本信息
					studentInfo.put("id", ccScoreStuIndigrade.getLong("student_id"));
					studentInfo.put("studentNo", studentNo);
					studentInfo.put("studentName", ccScoreStuIndigrade.getStr("student_name"));
					// 学生成绩项保存
					studentInfo.put("scoreMap", new HashMap<Long, BigDecimal>());
					studentInScoreMap.put(studentNo, studentInfo);
				} else{
					studentInfo = (Map<String, Object>) studentInScoreMap.get(studentNo);
				}
				Map<Long, Object> scoreMap = (Map<Long, Object>) studentInfo.get("scoreMap");
				Map<String, Object> scoreItem = Maps.newHashMap();
				scoreItem.put("grade", ccScoreStuIndigrade.getBigDecimal("grade"));
				scoreItem.put("type", ccScoreStuIndigrade.getInt("type"));
				scoreItem.put("levelDetailId", ccScoreStuIndigrade.getLong("level_detail_id"));
				scoreItem.put("scoreStuIndigradeId", ccScoreStuIndigrade.getLong("id"));
				scoreMap.put(ccScoreStuIndigrade.getLong("gradecomposeIndicationId"), scoreItem);
			}
		}
		else{
			//单批次指标点直接录入
			List<CcScoreStuIndigrade> ccScoreStuIndigrades = CcScoreStuIndigrade.dao.findDetailByClassIdAndCourseGradecomposeId(educlassId, courseGradecomposeId);
			for (CcScoreStuIndigrade ccScoreStuIndigrade : ccScoreStuIndigrades) {
				String studentNo = ccScoreStuIndigrade.getStr("student_no");
				Map<String, Object> studentInfo = null;
				if (studentInScoreMap.get(studentNo) == null) {
					studentInfo = Maps.newHashMap();
					// 学生基本信息
					studentInfo.put("id", ccScoreStuIndigrade.getLong("student_id"));
					studentInfo.put("studentNo", studentNo);
					studentInfo.put("studentName", ccScoreStuIndigrade.getStr("student_name"));
					// 学生成绩项保存
					studentInfo.put("scoreMap", new HashMap<Long, BigDecimal>());
					studentInScoreMap.put(studentNo, studentInfo);
				} else{
					studentInfo = (Map<String, Object>) studentInScoreMap.get(studentNo);
				}
				Map<Long, Object> scoreMap = (Map<Long, Object>) studentInfo.get("scoreMap");
				Map<String, Object> scoreItem = Maps.newHashMap();
				scoreItem.put("grade", ccScoreStuIndigrade.getBigDecimal("grade"));
				scoreItem.put("type", ccScoreStuIndigrade.getInt("type"));
				scoreItem.put("levelDetailId", ccScoreStuIndigrade.getLong("level_detail_id"));
				scoreItem.put("scoreStuIndigradeId", ccScoreStuIndigrade.getLong("id"));
				scoreMap.put(ccScoreStuIndigrade.getLong("gradecompose_indication_id"), scoreItem);
			}
		}

		


		
		//对于没有录入成绩的学生也放入studentInScoreMap
		for(CcStudent student : studentLits){
			Map<String, Object> studentInfo = Maps.newHashMap();
			String studentNo = student.getStr("student_no");
			if(studentInScoreMap.get(studentNo) == null){
				studentInfo.put("id", student.getLong("id"));
				studentInfo.put("studentNo", studentNo);
				studentInfo.put("studentName", student.getStr("name"));
				// 学生成绩项保存
				studentInfo.put("scoreMap", new HashMap<Long, BigDecimal>());
				studentInScoreMap.put(studentNo, studentInfo);
			}
		}
		result.put("courseGradecomposeIndicationLists", courseGradecomposeIndicationLists);
		result.put("studentInScoreMap", studentInScoreMap);
		result.put("educlassStudentStudyList", educlassStudentStudyList);
		 
	    return renderSUC(result, response, header);
	
	}
}
