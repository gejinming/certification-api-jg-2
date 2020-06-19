package com.gnet.certification;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradecomposeIndication;
import com.gnet.model.admin.CcCourseTargetIndication;
import com.gnet.model.admin.CcEduclassStudent;
import com.gnet.model.admin.CcEdupointAimsAchieve;
import com.gnet.model.admin.CcEvalute;
import com.gnet.model.admin.CcIndication;
import com.gnet.model.admin.CcIndicatorPoint;
import com.gnet.model.admin.CcReportEduclassEvalute;
import com.gnet.model.admin.CcReportEduclassGrade;
import com.gnet.model.admin.CcScoreStuIndigrade;
import com.gnet.model.admin.CcStudentEvalute;
import com.gnet.model.admin.CcTeacherCourse;
import com.gnet.utils.DictUtils;
import com.gnet.utils.PriceUtils;
import com.google.common.collect.Maps;

/**
 * 教学班报表显示
 * 
 * @author wct
 * @date 2016年7月11日
 * @edit date 2017年11月28日 SY
 */
@Service("EM00552")
public class EM00552 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long eduClassId = paramsLongFilter(params.get("eduClassId"));
		Long indicatorPointId = paramsLongFilter(params.get("indicatorPointId"));
		// 是否剔除部分学生的
		Boolean isCaculate = paramsBooleanFilter(params.get("isCaculate"));
		// 默认是的
		isCaculate = isCaculate == null ? Boolean.TRUE : isCaculate;
		if(params.containsKey("indicatorPointId") && indicatorPointId == null) {
		    return renderFAIL("1009", response, header, "indicatorPointId的参数值非法");
		}
		// 教学班编号不能为空过滤
		if (eduClassId == null) {
			return renderFAIL("0500", response, header);
		}
		CcTeacherCourse ccTeacherCourse = findByClassId(eduClassId);
		// 教师开课课程为空过滤
		if (ccTeacherCourse == null) {
			return renderFAIL("0501", response, header);
		}
		// 分析法判断
		Map<String, Object> studentInfo = null;
		Map<Long, Object> indicatorPointInfo = null;
//		Date statisticsDate = null;
		Boolean needUpdate = null;
		
		if (CcTeacherCourse.RESULT_TYPE_SCORE.equals(ccTeacherCourse.getInt("result_type"))) {
			// 考核分析法数据获取
			studentInfo = getStudentInfoScore(eduClassId, isCaculate);
			// 如果是剔除
			if(isCaculate) {
				indicatorPointInfo = getIndicationInfoScoreExcept(ccTeacherCourse.getLong("course_id"), eduClassId, indicatorPointId);				
			} else {
				indicatorPointInfo = getIndicationInfoScore(ccTeacherCourse.getLong("course_id"), eduClassId, indicatorPointId);
			}
			// 最近一次统计时间
//			statisticsDate = getLastStatisticsDateScore(eduClassId);
			// 有记录变动建议重新生成报表
//			needUpdate = statisticsDate == null ? CcScoreStuIndigrade.dao.isExistStudentGrades(eduClassId, indicationId) : needToUpdateScore(eduClassId);
			needUpdate = needToUpdateScore(eduClassId);
			
		} else if (CcTeacherCourse.RESULT_TYPE_EVALUATE.equals(ccTeacherCourse.getInt("result_type"))) {
//			// 评分表分析法数据获取
//			studentInfo = getStudentInfoEvalute(eduClassId);
//			indicationInfo = getIndicationInfoEvalute(ccTeacherCourse.getLong("course_id"), eduClassId, indicationId);
//			// 最近一次统计时间
//			statisticsDate = getLastStatisticsDateEvalute(eduClassId);
//			// 有记录变动建议重新生成报表
//			needUpdate = statisticsDate == null ? CcStudentEvalute.dao.isExistStudentGrades(eduClassId, indicationId) : needToUpdateEvalute(eduClassId);
			return renderFAIL("0502", response, header);
		} else {
			return renderFAIL("0502", response, header);
		}
		
		// 返回结果
		Map<String, Object> result = Maps.newHashMap();
		result.put("studentInfo", studentInfo);
		result.put("indicatorPointInfo", indicatorPointInfo);
//		result.put("indicationInfo", indicationInfo);
//		result.put("statisticsDate", statisticsDate);
		result.put("needUpdate", needUpdate);
		result.put("resultType", ccTeacherCourse.getInt("result_type"));
		result.put("resultTypeName", DictUtils.findLabelByTypeAndKey("courseResultType", ccTeacherCourse.getInt("result_type")));
		return renderSUC(result, response, header);
	}
	
	/**
	 * 根据教师班级获取教师开课课程
	 * 
	 * @param eduClassId
	 * @return
	 */
	private CcTeacherCourse findByClassId(Long eduClassId) {
		return CcTeacherCourse.dao.findCourseByClassId(eduClassId);
	}
	
	/**
	 * 获得学生所有成绩项
	 * 
	 * @param eduClassId
	 * @param isCaculate
	 * 			是否计算
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getStudentInfoScore(Long eduClassId, Boolean isCaculate) {
		// 学生每个开课课程成绩组成元素与课程目标关联数据
		List<CcEduclassStudent> ccEduclassStudents = CcEduclassStudent.dao.findScoreDetailByClassId(eduClassId, isCaculate);
		Map<String, Object> result = Maps.newLinkedHashMap();
//		// Map<studentNo, Map<indicationId, Map<gradecomposeId, Map<名称, 数值>>>>
//		Map<String, Map<Long, Map<Long, Map<String, Object>>>> studentIndicationGradecomposeScoreMap = new HashMap<>();
		
//		// 遍历，获取每个学生的成绩
//		for (CcEduclassStudent ccEduclassStudent : ccEduclassStudents) {
//			String studentNo = ccEduclassStudent.getStr("student_no");
//			Long indicationId = ccEduclassStudent.getLong("indicationId");
//			Long courseGradecomposeId = ccEduclassStudent.getLong("courseGradecomposeId");
//			// 课程目标
//			Map<Long, Map<Long, Map<String, Object>>> scoreMap = (Map<Long, Map<Long, Map<String, Object>>>) studentIndicationGradecomposeScoreMap.get(studentNo);
//			if(scoreMap == null || scoreMap.isEmpty()) {
//				scoreMap = new HashMap<>();
//				studentIndicationGradecomposeScoreMap.put(studentNo, scoreMap);
//			}
//			// 成绩组成。
//			Map<Long, Map<String, Object>> gradecomposeMap = scoreMap.get(indicationId);
//			if(gradecomposeMap == null || gradecomposeMap.isEmpty()) {
//				gradecomposeMap = new HashMap<>();
//				scoreMap.put(indicationId, gradecomposeMap);
//			}
//			// 分数。因为一个课程目标下面成绩组成都是唯一的，所以直接new
//			Map<String, Object> scoreItem = Maps.newHashMap();
//			scoreItem.put("score", ccEduclassStudent.getBigDecimal("grade"));
//			gradecomposeMap.put(courseGradecomposeId, scoreItem);
//		}
//		
//		// 遍历，按照学生，放到返回数据中
//		for (CcEduclassStudent ccEduclassStudent : ccEduclassStudents) {
//			String studentNo = ccEduclassStudent.getStr("student_no");
//			Map<String, Object> studentInfo = null;
//			// 获取某个学生，加入他的数据
//			if (result.get(studentNo) == null) {
//				studentInfo = Maps.newHashMap();
//				// 学生基本信息
//				studentInfo.put("id", ccEduclassStudent.getLong("student_id"));
//				studentInfo.put("studentNo", studentNo);
//				studentInfo.put("studentName", ccEduclassStudent.getStr("student_name"));
//				// 学生成绩项保存
//				studentInfo.put("scoreMap", studentIndicationGradecomposeScoreMap.get(studentNo));
//				result.put(studentNo, studentInfo);
//			}
//		}
		
		for (CcEduclassStudent ccEduclassStudent : ccEduclassStudents) {
			String studentNo = ccEduclassStudent.getStr("student_no");
			Map<String, Object> studentInfo = null;
			if (result.get(studentNo) == null) {
				studentInfo = Maps.newHashMap();
				// 学生基本信息
				studentInfo.put("id", ccEduclassStudent.getLong("student_id"));
				studentInfo.put("studentNo", studentNo);
				studentInfo.put("studentName", ccEduclassStudent.getStr("student_name"));
				// 学生成绩项保存
				studentInfo.put("scoreMap", new HashMap<Long, Map<Long, Map<String, Object>>>());
				result.put(studentNo, studentInfo);
			} else{
				studentInfo = (Map<String, Object>) result.get(studentNo);
			}
			Map<Long, Map<String, Object>> scoreMap = (Map<Long, Map<String, Object>>) studentInfo.get("scoreMap");
			Map<String, Object> scoreItem = Maps.newHashMap();
			scoreItem.put("score", ccEduclassStudent.getBigDecimal("grade"));
			scoreMap.put(ccEduclassStudent.getLong("gradecomposeIndicationId"), scoreItem);
		}
		return result;
	}
	
	/**
	 * 获得指标点以及指标点下的成绩组成
	 *
	 * @param eduClassId
	 * @param courseId
	 * @param indicatorPointId
	 * 			指标点编号（非必填）
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<Long, Object> getIndicationInfoScore(Long courseId, Long eduClassId, Long indicatorPointId) {
		/*
		 * 最终获取数据结构 ：
		 * Map<指标点编号，Map<指标点参数名，指标点参数值>>
		 * 指标点参数值中的一个 indicationInfo是课程目标，它的结构是 Map<课程目标id，Map<课程目标参数，课程目标参数值>>
		 * 课程目标参数值中的一个 gradecomposeList是成绩组成列表，它的结构是List<Map<成绩组成参数，成绩组成参数值>>
		 * 即：
		 * "indicatorPointInfo": {	
				"90001": {
					"indicatorPointId": 1,
					"indicatorPointIndexNum": 1,
					"indicatorPointWeight": 0.300,
					"indicatorPointValue": 0.10740000,
					"indicatorPointContent": "新的指标点",
					"indicationInfo": {
						"80001": {
							"indicationIndexNum": 1,
							"indicationWeight": 0.300,
							"indicationValue": 0.10740000,
							"indicationContent": "课程目标1",
							"gradecomposeList": [{
								"gradecomposeName": "平时分",
								"maxScore": 24.00,
								"gradecomposeAverage": 8.62,
								"gradecomposeWeight": 0.600,
								"gradecomposeIndicationId": 10001
							},
							{
								"gradecomposeName": "实验分",
								"maxScore": 40.00,
								"gradecomposeAverage": 13.04,
								"gradecomposeWeight": 0.200,
								"gradecomposeIndicationId": 10002
							},
							{
								"gradecomposeName": "考试分",
								"maxScore": 70.00,
								"gradecomposeAverage": 26.80,
								"gradecomposeWeight": 0.200,
								"gradecomposeIndicationId": 10003
							}],
							"indicationId": 10001
						},
						"80002": {
							课程目标下信息...
						} 
					}
				},
				"90002": {
					指标点下的信息...
				}
			}
		 */
//		// 当不存在指定指标点的时候
//		if(indicatorPointId == null) {
//			return getIndicationInfoScore(courseId, eduClassId);
//		}
//		// 当存在指定指标点的时候
		
		//某个教学班某个指标点下的所有的开课课程成绩组成指标点关系
		List<CcCourseGradecomposeIndication> courseGradecomposeIndicationList = CcCourseGradecomposeIndication.dao.findByEduClassIdAndIndicationId(courseId, eduClassId, indicatorPointId);
		// 返回的指标点对象
		Map<Long, Object> result = Maps.newLinkedHashMap();
		// 指标点对象创建
		Map<String, Object> indicatorPointInfo = null;
		// 课程目标信息
		List<Object> indicationInfoList = null;
		// 成绩组成元素详细信息
		List<Object> gradecomposeList = null;
		//指标点下成绩组成权重和
		BigDecimal allWeight = new BigDecimal(0);
		
		for(CcCourseGradecomposeIndication temp : courseGradecomposeIndicationList){
			allWeight = PriceUtils.add(allWeight, temp.getBigDecimal("weight"));
		}
		
		// 1. 获取指标点
		List<CcIndicatorPoint> ccIndicatorPoints = new ArrayList<>();
		if(indicatorPointId == null) {
			ccIndicatorPoints = CcIndicatorPoint.dao.findAllByCourseId(courseId);
		} else {
			CcIndicatorPoint ccIndicatorPoint = CcIndicatorPoint.dao.findAllByIdAndCourseId(indicatorPointId, courseId);
			ccIndicatorPoints.add(ccIndicatorPoint);
		}
		// 获取指标点和课程目标关系
		List<CcCourseTargetIndication> ccCourseTargetIndications = CcCourseTargetIndication.dao.findByCourseId(courseId);
		Map<Long, List<Long>> indicatorIdAndIndicationIdListMap = new HashMap<>();
		for(CcCourseTargetIndication temp : ccCourseTargetIndications) {
			Long indicationId = temp.getLong("indication_id");
			Long keyIndicatorPointId = temp.getLong("indicatorPointId");
			List<Long> indicationIdList = indicatorIdAndIndicationIdListMap.get(keyIndicatorPointId);
			if(indicationIdList == null || indicationIdList.isEmpty()) {
				indicationIdList = new ArrayList<>();
				indicatorIdAndIndicationIdListMap.put(keyIndicatorPointId, indicationIdList);
			}
			indicationIdList.add(indicationId);
		}
		// 找到指标点的达成度
		List<CcEdupointAimsAchieve> ccEdupointAimsAchieves = CcEdupointAimsAchieve.dao.findByEduclassId(eduClassId);
		// 指标点对应达成度的map
		Map<Long, BigDecimal> indicatorPointValueMap = new HashMap<>();
		for(CcEdupointAimsAchieve temp : ccEdupointAimsAchieves) {
			Long thisIndicatorPointId = temp.getLong("indicatorPointId");
			BigDecimal achieveValue = temp.getBigDecimal("achieve_value");
			indicatorPointValueMap.put(thisIndicatorPointId, achieveValue);
		}
		// 2. 作为筛选条件，获取当前要求指标点对应的课程目标
		List<CcIndication> ccIndications = CcIndication.dao.findAllByCourseId(courseId);
		List<CcIndication> ccTempIndications = new ArrayList<>();
		ccTempIndications.addAll(ccIndications);
		List<Long> indicationIdList = new ArrayList<>();
		for(CcIndication temp : ccTempIndications) {
			Long indicationid = temp.getLong("id");
			Long educlassId = temp.getLong("educlassId");
			if(educlassId != null && educlassId.equals(eduClassId) && !indicationIdList.contains(indicationid)) {
				indicationIdList.add(indicationid);
			} else {
				// 课程目标单纯留念当前教学班
				ccIndications.remove(temp);
			}
		}
		// 3. 找到成绩组成
		List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndications = CcCourseGradecomposeIndication.dao.findByIndicationIds(indicationIdList);
		/*
		 *  第一层，放入指标点
		 *  第二层，放入课程目标
		 *  第三层，放入成绩组成
		 */
		for(CcIndicatorPoint tempIndicatorPoint : ccIndicatorPoints) {
			Long keyIndicatorPointId = tempIndicatorPoint.getLong("id");
			indicatorPointInfo = Maps.newHashMap();
			indicationInfoList = new ArrayList<>();
			indicatorPointInfo.put("indicatorPointId", keyIndicatorPointId);
			indicatorPointInfo.put("indicatorPointIndexNum", tempIndicatorPoint.getInt("index_num"));
			indicatorPointInfo.put("graduateIndexNum", tempIndicatorPoint.getInt("graduateIndexNum"));
			indicatorPointInfo.put("indicatorPointContent", tempIndicatorPoint.getStr("content"));
			indicatorPointInfo.put("indicatorPointRemark", tempIndicatorPoint.getStr("remark"));
			indicatorPointInfo.put("indicatorPointWeight", tempIndicatorPoint.getBigDecimal("indicationWeight"));
			indicatorPointInfo.put("indicatorPointValue", indicatorPointValueMap.get(keyIndicatorPointId));
			indicatorPointInfo.put("indicationInfo", indicationInfoList);
			List<Long> thisIndicationIdList = indicatorIdAndIndicationIdListMap.get(keyIndicatorPointId);
			// 遍历课程目标
			for(CcIndication tempIndication : ccIndications) {
				Long indicationId = tempIndication.getLong("id");
				if(thisIndicationIdList.contains(indicationId)) {
					BigDecimal expectedValue = tempIndication.getBigDecimal("expected_value");
					BigDecimal achieveValue = tempIndication.getBigDecimal("achieveValue");
					Integer sort = tempIndication.getInt("sort");
					String content = tempIndication.getStr("content");
					
					// 如果当前指标点存在这个课程目标
					gradecomposeList = new ArrayList<>();
					Map<String, Object> indicationInfo = Maps.newHashMap();
					indicationInfo.put("indicationId", indicationId);
					indicationInfo.put("indicationIndexNum", sort);
					indicationInfo.put("indicationExpectedValue", expectedValue);
					indicationInfo.put("indicationAchieveValue", achieveValue == null ? achieveValue : achieveValue.divide(expectedValue,5, RoundingMode.HALF_UP));
					indicationInfo.put("indicationContent", content);
					indicationInfo.put("gradecomposeList", gradecomposeList);
					indicationInfoList.add(indicationInfo);
					
					// 遍历成绩组成，把属于的加进去
					for(CcCourseGradecomposeIndication tempCcCourseGradecomposeIndication : ccCourseGradecomposeIndications) {
						Long thisIndicationId = tempCcCourseGradecomposeIndication.getLong("indication_id");
						Long educlassId = tempCcCourseGradecomposeIndication.getLong("educlassId");
						if(thisIndicationId.equals(indicationId) && educlassId.equals(eduClassId)) {
							// 如果这个成绩组成和当前的指标点有关系，并且是当前教学班的时候，就加进去
							String gradecomposeName = tempCcCourseGradecomposeIndication.getStr("gradecomposeName");
							Long gradecomposeIndicationId = tempCcCourseGradecomposeIndication.getLong("id");
							BigDecimal avgScore = tempCcCourseGradecomposeIndication.getBigDecimal("avgScore");
							BigDecimal weight = tempCcCourseGradecomposeIndication.getBigDecimal("weight");
							BigDecimal maxScore = tempCcCourseGradecomposeIndication.getBigDecimal("max_score");
							Map<String, Object> gradecomposeDetail = Maps.newHashMap();
							gradecomposeDetail.put("gradecomposeIndicationId", gradecomposeIndicationId);
							gradecomposeDetail.put("gradecomposeName", gradecomposeName);
							gradecomposeDetail.put("gradecomposeAverage", avgScore);
							gradecomposeDetail.put("gradecomposeWeight", weight);
							gradecomposeDetail.put("maxScore", maxScore);
							gradecomposeList.add(gradecomposeDetail);
						}
					}
				}
			}
			result.put(keyIndicatorPointId, indicatorPointInfo);
		}
		return result;
	}
	
	/**
	 * 获得指标点以及指标点下的成绩组成
	 *
	 * @param eduClassId
	 * @param courseId
	 * @param indicatorPointId
	 * 			指标点编号（非必填）
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<Long, Object> getIndicationInfoScoreExcept(Long courseId, Long eduClassId, Long indicatorPointId) {
		/*
		 * 最终获取数据结构 ：
		 * Map<指标点编号，Map<指标点参数名，指标点参数值>>
		 * 指标点参数值中的一个 indicationInfo是课程目标，它的结构是 Map<课程目标id，Map<课程目标参数，课程目标参数值>>
		 * 课程目标参数值中的一个 gradecomposeList是成绩组成列表，它的结构是List<Map<成绩组成参数，成绩组成参数值>>
		 * 即：
		 * "indicatorPointInfo": {	
				"90001": {
					"indicatorPointId": 1,
					"indicatorPointIndexNum": 1,
					"indicatorPointWeight": 0.300,
					"indicatorPointValue": 0.10740000,
					"indicatorPointContent": "新的指标点",
					"indicationInfo": {
						"80001": {
							"indicationIndexNum": 1,
							"indicationWeight": 0.300,
							"indicationValue": 0.10740000,
							"indicationContent": "课程目标1",
							"gradecomposeList": [{
								"gradecomposeName": "平时分",
								"maxScore": 24.00,
								"gradecomposeAverage": 8.62,
								"gradecomposeWeight": 0.600,
								"gradecomposeIndicationId": 10001
							},
							{
								"gradecomposeName": "实验分",
								"maxScore": 40.00,
								"gradecomposeAverage": 13.04,
								"gradecomposeWeight": 0.200,
								"gradecomposeIndicationId": 10002
							},
							{
								"gradecomposeName": "考试分",
								"maxScore": 70.00,
								"gradecomposeAverage": 26.80,
								"gradecomposeWeight": 0.200,
								"gradecomposeIndicationId": 10003
							}],
							"indicationId": 10001
						},
						"80002": {
							课程目标下信息...
						} 
					}
				},
				"90002": {
					指标点下的信息...
				}
			}
		 */
//		// 当不存在指定指标点的时候
//		if(indicatorPointId == null) {
//			return getIndicationInfoScore(courseId, eduClassId);
//		}
//		// 当存在指定指标点的时候
		
		//某个教学班某个指标点下的所有的开课课程成绩组成指标点关系
		List<CcCourseGradecomposeIndication> courseGradecomposeIndicationList = CcCourseGradecomposeIndication.dao.findByEduClassIdAndIndicationId(courseId, eduClassId, indicatorPointId);
		// 返回的指标点对象
		Map<Long, Object> result = Maps.newLinkedHashMap();
		// 指标点对象创建
		Map<String, Object> indicatorPointInfo = null;
		// 课程目标信息
		List<Object> indicationInfoList = null;
		// 成绩组成元素详细信息
		List<Object> gradecomposeList = null;
		//指标点下成绩组成权重和
		BigDecimal allWeight = new BigDecimal(0);
		
		for(CcCourseGradecomposeIndication temp : courseGradecomposeIndicationList){
			allWeight = PriceUtils.add(allWeight, temp.getBigDecimal("weight"));
		}
		
		// 1. 获取指标点
		List<CcIndicatorPoint> ccIndicatorPoints = new ArrayList<>();
		if(indicatorPointId == null) {
			ccIndicatorPoints = CcIndicatorPoint.dao.findAllByCourseId(courseId);
		} else {
			CcIndicatorPoint ccIndicatorPoint = CcIndicatorPoint.dao.findAllByIdAndCourseId(indicatorPointId, courseId);
			ccIndicatorPoints.add(ccIndicatorPoint);
		}
		// 获取指标点和课程目标关系
		List<CcCourseTargetIndication> ccCourseTargetIndications = CcCourseTargetIndication.dao.findByCourseId(courseId);
		Map<Long, List<Long>> indicatorIdAndIndicationIdListMap = new HashMap<>();
		for(CcCourseTargetIndication temp : ccCourseTargetIndications) {
			Long indicationId = temp.getLong("indication_id");
			Long keyIndicatorPointId = temp.getLong("indicatorPointId");
			List<Long> indicationIdList = indicatorIdAndIndicationIdListMap.get(keyIndicatorPointId);
			if(indicationIdList == null || indicationIdList.isEmpty()) {
				indicationIdList = new ArrayList<>();
				indicatorIdAndIndicationIdListMap.put(keyIndicatorPointId, indicationIdList);
			}
			indicationIdList.add(indicationId);
		}
		// 找到指标点的达成度
		List<CcEdupointAimsAchieve> ccEdupointAimsAchieves = CcEdupointAimsAchieve.dao.findByEduclassId(eduClassId);
		// 指标点对应提出学生后的达成度的map
		Map<Long, BigDecimal> indicatorPointExceptValueMap = new HashMap<>();
		for(CcEdupointAimsAchieve temp : ccEdupointAimsAchieves) {
			Long thisIndicatorPointId = temp.getLong("indicatorPointId");
			
			BigDecimal exceptAchieveValue = temp.getBigDecimal("except_achieve_value");
			indicatorPointExceptValueMap.put(thisIndicatorPointId, exceptAchieveValue);
		}
		// 2. 作为筛选条件，获取当前要求指标点对应的课程目标
		List<CcIndication> ccIndications = CcIndication.dao.findAllByCourseId(courseId);
		List<CcIndication> ccTempIndications = new ArrayList<>();
		ccTempIndications.addAll(ccIndications);
		List<Long> indicationIdList = new ArrayList<>();
		for(CcIndication temp : ccTempIndications) {
			Long indicationid = temp.getLong("id");
			Long educlassId = temp.getLong("educlassId");
			if(educlassId != null && educlassId.equals(eduClassId) && !indicationIdList.contains(indicationid)) {
				indicationIdList.add(indicationid);
			} else {
				// 课程目标单纯留念当前教学班
				ccIndications.remove(temp);
			}
		}
		// 3. 找到成绩组成
		List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndications = CcCourseGradecomposeIndication.dao.findByIndicationIds(indicationIdList);
		/*
		 *  第一层，放入指标点
		 *  第二层，放入课程目标
		 *  第三层，放入成绩组成
		 */
		for(CcIndicatorPoint tempIndicatorPoint : ccIndicatorPoints) {
			Long keyIndicatorPointId = tempIndicatorPoint.getLong("id");
			indicatorPointInfo = Maps.newHashMap();
			indicationInfoList = new ArrayList<>();
			indicatorPointInfo.put("indicatorPointId", keyIndicatorPointId);
			indicatorPointInfo.put("indicatorPointIndexNum", tempIndicatorPoint.getInt("index_num"));
			indicatorPointInfo.put("graduateIndexNum", tempIndicatorPoint.getInt("graduateIndexNum"));
			indicatorPointInfo.put("indicatorPointContent", tempIndicatorPoint.getStr("content"));
			indicatorPointInfo.put("indicatorPointRemark", tempIndicatorPoint.getStr("remark"));
			indicatorPointInfo.put("indicatorPointWeight", tempIndicatorPoint.getBigDecimal("indicationWeight"));
			indicatorPointInfo.put("indicatorPointValue", indicatorPointExceptValueMap.get(keyIndicatorPointId));
			indicatorPointInfo.put("indicationInfo", indicationInfoList);
			List<Long> thisIndicationIdList = indicatorIdAndIndicationIdListMap.get(keyIndicatorPointId);
			// 遍历课程目标
			for(CcIndication tempIndication : ccIndications) {
				Long indicationId = tempIndication.getLong("id");
				if(thisIndicationIdList.contains(indicationId)) {
					BigDecimal expectedValue = tempIndication.getBigDecimal("expected_value");
					BigDecimal exceptAchieveValue = tempIndication.getBigDecimal("exceptAchieveValue");
					Integer sort = tempIndication.getInt("sort");
					String content = tempIndication.getStr("content");
					
					// 如果当前指标点存在这个课程目标
					gradecomposeList = new ArrayList<>();
					Map<String, Object> indicationInfo = Maps.newHashMap();
					indicationInfo.put("indicationId", indicationId);
					indicationInfo.put("indicationIndexNum", sort);
					indicationInfo.put("indicationExpectedValue", expectedValue);
					indicationInfo.put("indicationAchieveValue", exceptAchieveValue == null ? exceptAchieveValue : exceptAchieveValue.divide(expectedValue,5, RoundingMode.HALF_UP));
					indicationInfo.put("indicationContent", content);
					indicationInfo.put("gradecomposeList", gradecomposeList);
					indicationInfoList.add(indicationInfo);
					
					// 遍历成绩组成，把属于的加进去
					for(CcCourseGradecomposeIndication tempCcCourseGradecomposeIndication : ccCourseGradecomposeIndications) {
						Long thisIndicationId = tempCcCourseGradecomposeIndication.getLong("indication_id");
						Long educlassId = tempCcCourseGradecomposeIndication.getLong("educlassId");
						if(thisIndicationId.equals(indicationId) && educlassId.equals(eduClassId)) {
							// 如果这个成绩组成和当前的指标点有关系，并且是当前教学班的时候，就加进去
							String gradecomposeName = tempCcCourseGradecomposeIndication.getStr("gradecomposeName");
							Long gradecomposeIndicationId = tempCcCourseGradecomposeIndication.getLong("id");
							BigDecimal exceptAvgScore = tempCcCourseGradecomposeIndication.getBigDecimal("exceptAvgScore");
							BigDecimal weight = tempCcCourseGradecomposeIndication.getBigDecimal("weight");
							BigDecimal maxScore = tempCcCourseGradecomposeIndication.getBigDecimal("max_score");
							Map<String, Object> gradecomposeDetail = Maps.newHashMap();
							gradecomposeDetail.put("gradecomposeIndicationId", gradecomposeIndicationId);
							gradecomposeDetail.put("gradecomposeName", gradecomposeName);
							gradecomposeDetail.put("gradecomposeAverage", exceptAvgScore);
							gradecomposeDetail.put("gradecomposeWeight", weight);
							gradecomposeDetail.put("maxScore", maxScore);
							gradecomposeList.add(gradecomposeDetail);
						}
					}
				}
			}
			result.put(keyIndicatorPointId, indicatorPointInfo);
		}
		return result;
	}
	
//	/**
//	 * 获得当前课程的当前教学班下，所有指标点以及指标点下的课程目标以及成绩组成
//	 *
//	 * @param eduClassId
//	 * @param courseId
//	 * @return
//	 */
//	private Map<Long, Object> getIndicationInfoScore(Long courseId, Long eduClassId) {
////		// 教学班下课程目标成绩组成学生分数
////		List<CcEduindicationStuScore> ccEduindicationStuScores = CcEduindicationStuScore.dao.findAllByEduclassId(eduClassId);
//		//某个教学班某个指标点下的所有的开课课程成绩组成指标点关系
//		List<CcCourseGradecomposeIndication> courseGradecomposeIndicationList = CcCourseGradecomposeIndication.dao.findByEduClassIdAndIndicationId(courseId, eduClassId, null);
//		// 返回的指标点对象
//		Map<Long, Object> result = Maps.newLinkedHashMap();
//		// 指标点对象创建
//		Map<String, Object> indicatorPointInfo = null;
//		// 课程目标信息
//		List<Object> indicationInfoList = null;
//		// 成绩组成元素详细信息
//		List<Object> gradecomposeList = null;
//		//指标点下成绩组成权重和
//		BigDecimal allWeight = new BigDecimal(0);
//		
//		for(CcCourseGradecomposeIndication temp : courseGradecomposeIndicationList){
//			allWeight = PriceUtils.add(allWeight, temp.getBigDecimal("weight"));
//		}
//		
//		// 1. 获取指标点
//		List<CcIndicatorPoint> ccIndicatorPoints = CcIndicatorPoint.dao.findAllByCourseId(courseId);
//		// 获取指标点和课程目标关系
//		List<CcCourseTargetIndication> ccCourseTargetIndications = CcCourseTargetIndication.dao.findByCourseId(courseId);
//		Map<Long, List<Long>> indicatorIdAndIndicationIdListMap = new HashMap<>();
//		for(CcCourseTargetIndication temp : ccCourseTargetIndications) {
//			Long indicationId = temp.getLong("indication_id");
//			Long keyIndicatorPointId = temp.getLong("indicatorPointId");
//			List<Long> indicationIdList = indicatorIdAndIndicationIdListMap.get(keyIndicatorPointId);
//			if(indicationIdList == null || indicationIdList.isEmpty()) {
//				indicationIdList = new ArrayList<>();
//				indicatorIdAndIndicationIdListMap.put(keyIndicatorPointId, indicationIdList);
//			}
//			indicationIdList.add(indicationId);
//		}
//		// 找到指标点的达成度
//		List<CcEdupointAimsAchieve> ccEdupointAimsAchieves = CcEdupointAimsAchieve.dao.findByEduclassId(eduClassId);
//		// 指标点对应达成度的map
//		Map<Long, BigDecimal> indicatorPointValueMap = new HashMap<>();
//		for(CcEdupointAimsAchieve temp : ccEdupointAimsAchieves) {
//			Long thisIndicatorPointId = temp.getLong("indicatorPointId");
//			BigDecimal achieveValue = temp.getBigDecimal("achieve_value");
//			indicatorPointValueMap.put(thisIndicatorPointId, achieveValue);
//		}
//		// 2. 作为筛选条件，获取当前要求指标点对应的课程目标
//		List<CcIndication> ccIndications = CcIndication.dao.findAllByCourseId(courseId);
//		List<Long> indicationIdList = new ArrayList<>();
//		for(CcIndication temp : ccIndications) {
//			Long indicationid = temp.getLong("id");
//			if(!indicationIdList.contains(indicationid)) {
//				indicationIdList.add(indicationid);
//			}
//		}
//		// 3. 找到成绩组成
//		List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndications = CcCourseGradecomposeIndication.dao.findByIndicationIds(indicationIdList);
//		/*
//		 *  第一层，放入指标点
//		 *  第二层，放入课程目标
//		 *  第三层，放入成绩组成
//		 */
//		for(CcIndicatorPoint tempIndicatorPoint : ccIndicatorPoints) {
//			Long keyIndicatorPointId = tempIndicatorPoint.getLong("id");
//			indicatorPointInfo = Maps.newHashMap();
//			indicationInfoList = new ArrayList<>();
//			indicatorPointInfo.put("indicatorPointId", keyIndicatorPointId);
//			indicatorPointInfo.put("indicatorPointIndexNum", tempIndicatorPoint.getInt("index_num"));
//			indicatorPointInfo.put("graduateIndexNum", tempIndicatorPoint.getInt("graduateIndexNum"));
//			indicatorPointInfo.put("indicatorPointContent", tempIndicatorPoint.getStr("content"));
//			indicatorPointInfo.put("indicatorPointRemark", tempIndicatorPoint.getStr("remark"));
//			indicatorPointInfo.put("indicatorPointWeight", tempIndicatorPoint.getBigDecimal("indicationWeight"));
//			indicatorPointInfo.put("indicatorPointValue", indicatorPointValueMap.get(keyIndicatorPointId));
//			indicatorPointInfo.put("indicationInfo", indicationInfoList);
//			List<Long> thisIndicationIdList = indicatorIdAndIndicationIdListMap.get(keyIndicatorPointId);
//			// 遍历课程目标
//			for(CcIndication tempIndication : ccIndications) {
//				Long indicationId = tempIndication.getLong("id");
//				if(thisIndicationIdList.contains(indicationId)) {
//					BigDecimal expectedValue = tempIndication.getBigDecimal("expected_value");
//					BigDecimal achieveValue = tempIndication.getBigDecimal("achieveValue");
//					Integer sort = tempIndication.getInt("sort");
//					String content = tempIndication.getStr("content");
//					
//					// 如果当前指标点存在这个课程目标
//					gradecomposeList = new ArrayList<>();
//					Map<String, Object> indicationInfo = Maps.newHashMap();
//					indicationInfo.put("indicationId", indicationId);
//					indicationInfo.put("indicationIndexNum", sort);
//					indicationInfo.put("indicationExpectedValue", expectedValue);
//					indicationInfo.put("indicationAchieveValue", achieveValue);
//					indicationInfo.put("indicationContent", content);
//					indicationInfo.put("gradecomposeList", gradecomposeList);
//					indicationInfoList.add(indicationInfo);
//					
//					// 遍历成绩组成，把属于的加进去
//					for(CcCourseGradecomposeIndication tempCcCourseGradecomposeIndication : ccCourseGradecomposeIndications) {
//						Long thisIndicationId = tempCcCourseGradecomposeIndication.getLong("indication_id");
//						if(thisIndicationId.equals(indicationId)) {
//							// 如果这个成绩组成和当前的指标点有关系，就加进去
//							String gradecomposeName = tempCcCourseGradecomposeIndication.getStr("gradecomposeName");
//							Long gradecomposeIndicationId = tempCcCourseGradecomposeIndication.getLong("id");
//							BigDecimal avgScore = tempCcCourseGradecomposeIndication.getBigDecimal("avgScore");
//							BigDecimal weight = tempCcCourseGradecomposeIndication.getBigDecimal("weight");
//							BigDecimal maxScore = tempCcCourseGradecomposeIndication.getBigDecimal("max_score");
//							Map<String, Object> gradecomposeDetail = Maps.newHashMap();
//							gradecomposeDetail.put("gradecomposeIndicationId", gradecomposeIndicationId);
//							gradecomposeDetail.put("gradecomposeName", gradecomposeName);
//							gradecomposeDetail.put("gradecomposeAverage", avgScore);
//							gradecomposeDetail.put("gradecomposeWeight", weight);
//							gradecomposeDetail.put("maxScore", maxScore);
//							gradecomposeList.add(gradecomposeDetail);
//						}
//					}
//				}
//			}
//			result.put(keyIndicatorPointId, indicatorPointInfo);
//		}
//		return result;
//	}

//	/**
//	 * 获得指标点以及指标点下的成绩组成
//	 *
//	 * @param eduClassId
//	 * @param courseId
//	 * @param indicatorPointId
//	 * 			指标点编号（非必填）
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	private Map<Long, Object> getIndicationInfoScore(Long courseId, Long eduClassId, Long indicatorPointId) {
//		// 教学班下课程目标成绩组成学生分数
////		List<CcEduindicationStuScore> ccEduindicationStuScores = CcEduindicationStuScore.dao.findByColumn("educlass_id", eduClassId);
//		List<CcEduindicationStuScore> ccEduindicationStuScores = CcEduindicationStuScore.dao.findAllByEduclassId(eduClassId);
////		List<CcReportEduclassGrade> ccReportEduclassGrades = CcReportEduclassGrade.dao.findAllByCourseId(courseId, eduClassId, targetIndicationId);
//		// 某个教学班某个指标点下的所有的开课课程成绩组成课程目标关系
//		List<CcCourseGradecomposeIndication> courseGradecomposeIndicationList = CcCourseGradecomposeIndication.dao.findByEduClassIdAndIndicationId(courseId, eduClassId, indicatorPointId);
//		Map<Long, Object> reportEduclassGradesMap = Maps.newHashMap();
//		// Map<indicationId, Object(主要是成绩组成元素)>
//		Map<Long, Object> result = Maps.newLinkedHashMap();
//		// 课程目标的达成度 Map<indicationId, value>
//		Map<Long, BigDecimal> indicationValueMap = Maps.newHashMap();
//		// 课程目标对象创建
//		Map<String, Object> indicationInfo = null;
//		// 成绩组成元素详细信息
//		List<Object> gradecomposeMap = null;
//		//课程目标下成绩组成权重和
//		BigDecimal allWeight = new BigDecimal(0);
//		
//		// 组装课程目标达成度map
//		List<CcEdupointEachAimsAchieve> ccEdupointEachAimsAchieves = CcEdupointEachAimsAchieve.dao.findByColumn("educlass_id", eduClassId);
//		for(CcEdupointEachAimsAchieve temp : ccEdupointEachAimsAchieves) {
//			Long indicationid = temp.getLong("indication_id");
//			BigDecimal achieveValue = temp.getBigDecimal("achieve_value");
//			indicationValueMap.put(indicationid, achieveValue);
//		}
//		
//		for(CcCourseGradecomposeIndication temp : courseGradecomposeIndicationList){
//			allWeight = PriceUtils.add(allWeight, temp.getBigDecimal("weight"));
//		}
//		// 没有输入任何学生成绩的时候
//		if(!courseGradecomposeIndicationList.isEmpty() && ccEduindicationStuScores.isEmpty()){
//			for(CcCourseGradecomposeIndication temp : courseGradecomposeIndicationList){
//				Long indicationId = temp.getLong("indication_id");
//				indicationInfo = Maps.newHashMap();
//				indicationInfo.put("indicationId", indicationId);
//				indicationInfo.put("indicationIndexNum", temp.getInt("sort"));
//				indicationInfo.put("indicationContent", temp.getStr("content"));
//				indicationInfo.put("indicationWeight", temp.getBigDecimal("indication_weight"));
//				indicationInfo.put("indicationValue", indicationValueMap.get(indicationId).setScale(3, RoundingMode.HALF_UP));
//				result.put(indicationId, indicationInfo);
//			}
//		}
//		
//		// 对于 教学班下-课程目标-成绩组成，存在数据，进行加入
//		for (CcEduindicationStuScore ccEduindicationStuScore : ccEduindicationStuScores) {
//			Long indicationId = ccEduindicationStuScore.getLong("indication_id");
//			Long gradecomposeIndicationId = ccEduindicationStuScore.getLong("gradecompose_indication_id");
//			// 某个课程目标下的数据
//			indicationInfo = (Map<String, Object>) result.get(indicationId);
//			reportEduclassGradesMap.put(gradecomposeIndicationId, ccEduindicationStuScore);
//			// 如果还未生成。全部设置为0
//			if(indicationInfo == null) {
//				indicationInfo = Maps.newHashMap();
//				indicationInfo.put("indicationId", ccEduindicationStuScore.getLong("indication_id"));
//				indicationInfo.put("indicationIndexNum", ccEduindicationStuScore.getInt("sort"));
//				indicationInfo.put("indicationContent", ccEduindicationStuScore.getStr("content"));
//				indicationInfo.put("indicationWeight", ccEduindicationStuScore.getBigDecimal("indication_weight"));
//				indicationInfo.put("indicationValue", indicationValueMap.get(indicationId).setScale(3, RoundingMode.HALF_UP));
//				indicationInfo.put("gradecomposeList", new ArrayList<Object>());
//				result.put(indicationId, indicationInfo);
//			} else {
//				indicationInfo = (Map<String, Object>) result.get(indicationId);
//			}
//			// 成绩组成列表设置
//			gradecomposeMap = (List<Object>) indicationInfo.get("gradecomposeList");
//			Map<String, Object> gradecomposeDetail = Maps.newHashMap();
//			gradecomposeDetail.put("gradecomposeIndicationId", gradecomposeIndicationId);
//			gradecomposeDetail.put("gradecomposeName", ccEduindicationStuScore.getStr("gradecompose_name"));
//			gradecomposeDetail.put("gradecomposeAverage", ccEduindicationStuScore.getBigDecimal("avg_score"));
//			gradecomposeDetail.put("gradecomposeWeight", ccEduindicationStuScore.getBigDecimal("weight"));
//			gradecomposeDetail.put("maxScore", ccEduindicationStuScore.getBigDecimal("max_score"));
//			gradecomposeMap.add(gradecomposeDetail);
//		}
////		for (CcEduindicationStuScore ccEduindicationStuScore : ccEduindicationStuScores) {
////			Long indicationId = ccEduindicationStuScore.getLong("indication_id");
////			Long gradecomposeIndicationId = ccEduindicationStuScore.getLong("gradecompose_indication_id");
////			reportEduclassGradesMap.put(gradecomposeIndicationId, ccEduindicationStuScore);
////			if (result.get(indicationId) == null) {
////				indicationInfo = Maps.newHashMap();
////				indicationInfo.put("indicationId", ccEduindicationStuScore.getLong("indication_id"));
////				indicationInfo.put("indicationIndexNum", ccEduindicationStuScore.getInt("sort"));
//////				indicationInfo.put("graduateIndexNum", ccReportEduclassGrade.getInt("graduateIndexNum"));
////				indicationInfo.put("indicationContent", ccEduindicationStuScore.getStr("content"));
//////				indicationInfo.put("indicationRemark", ccReportEduclassGrade.getStr("remark"));
////				indicationInfo.put("indicationWeight", ccEduindicationStuScore.getBigDecimal("indication_weight"));
////				indicationInfo.put("gradecomposeList", new ArrayList<Object>());
////				indicationValueMap.put(indicationId, new BigDecimal(0));
////				result.put(indicationId, indicationInfo);
////			} else {
////				indicationInfo = (Map<String, Object>) result.get(indicationId);
////			}
////		    gradecomposeMap = (List<Object>) indicationInfo.get("gradecomposeList");
////			Map<String, Object> gradecomposeDetail = Maps.newHashMap();
////			gradecomposeDetail.put("gradecomposeIndicationId", gradecomposeIndicationId);
////			gradecomposeDetail.put("gradecomposeName", ccEduindicationStuScore.getStr("gradecompose_name"));
////			gradecomposeDetail.put("gradecomposeAverage", ccEduindicationStuScore.getBigDecimal("result"));
////			gradecomposeDetail.put("gradecomposeWeight", ccEduindicationStuScore.getBigDecimal("weight"));
////			gradecomposeDetail.put("maxScore", ccEduindicationStuScore.getBigDecimal("max_score"));
////			gradecomposeMap.add(gradecomposeDetail);
////			// 计算成绩组成评价值项
////			BigDecimal gradecomposeValue = PriceUtils.isZero(ccEduindicationStuScore.getBigDecimal("max_score")) ? new BigDecimal(0) : PriceUtils._mul(ccEduindicationStuScore.getBigDecimal("result").divide(PriceUtils._mul(ccEduindicationStuScore.getBigDecimal("max_score"), allWeight),4, RoundingMode.HALF_UP), ccEduindicationStuScore.getBigDecimal("weight"));
////			indicationValueMap.put(indicationId, PriceUtils._add(indicationValueMap.get(indicationId), PriceUtils._mul(gradecomposeValue, ccEduindicationStuScore.getBigDecimal("indication_weight"))));
////		}
////		
//		//放入学生所有成绩为空的课程目标下的开课课程成绩组成
//		if(courseGradecomposeIndicationList.size() > ccEduindicationStuScores.size()){
//			if(indicationInfo.get("gradecomposeList") == null){
//				indicationInfo.put("gradecomposeList", new ArrayList<Object>());
//			}
//			gradecomposeMap = (List<Object>) indicationInfo.get("gradecomposeList");
//			for(CcCourseGradecomposeIndication temp : courseGradecomposeIndicationList){
//				Long gradecomposeIndicationId = temp.getLong("id");
//				if(reportEduclassGradesMap.get(gradecomposeIndicationId) == null){
//					Map<String, Object> gradecomposeDetail = Maps.newHashMap();
//					gradecomposeDetail.put("gradecomposeIndicationId", gradecomposeIndicationId);
//					gradecomposeDetail.put("gradecomposeName", temp.getStr("gradecomposeName"));
//					gradecomposeDetail.put("gradecomposeWeight",temp.getBigDecimal("weight"));
//					gradecomposeDetail.put("maxScore", temp.getBigDecimal("max_score"));
//					gradecomposeMap.add(gradecomposeDetail);
//				}		
//			}
//		}		
////		// 将课程目标评价值放入返回结果中
////		if(!indicationValueMap.isEmpty()){
////			for (Map.Entry<Long, Object> entry : result.entrySet()) {
////				((Map<String, Object>) entry.getValue()).put("indicationValue", indicationValueMap.get(entry.getKey()).setScale(3, RoundingMode.HALF_UP));
////			}
////		}
//		return result;
//	}
	
	/**
	 * 获得统计时间(考核分析法)
	 * 
	 * @param eduClassId
	 * @return
	 */
	private Date getLastStatisticsDateScore(Long eduClassId) {
		List<CcReportEduclassGrade>  ccReportEduclassGrades = CcReportEduclassGrade.dao.findFilteredByColumn("educlass_id", eduClassId);
		if (ccReportEduclassGrades.isEmpty()) {
			return null;
		}
		return ccReportEduclassGrades.get(0).getDate("statistics_date");
	}
	
	/**
	 * 存在新的更新(考核分析法)
	 * 
	 * @param eduClassId
	 * @return
	 */
	private boolean needToUpdateScore(Long eduClassId) {
		return CcScoreStuIndigrade.dao.needToUpdate(eduClassId);
	}
	
	/**
	 * 获得学生所有评分表成绩项
	 * 
	 * @param eduClassId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getStudentInfoEvalute(Long eduClassId) {
		List<CcEduclassStudent> ccEduclassStudents = CcEduclassStudent.dao.findEvaluteDetailByClassId(eduClassId);
		Map<String, Object> result = Maps.newLinkedHashMap();
		for (CcEduclassStudent ccEduclassStudent : ccEduclassStudents) {
			String studentNo = ccEduclassStudent.getStr("student_no");
			Map<String, Object> studentInfo = null;
			if (result.get(studentNo) == null) {
				studentInfo = Maps.newHashMap();
				// 学生基本信息
				studentInfo.put("id", ccEduclassStudent.getLong("student_id"));
				studentInfo.put("studentNo", studentNo);
				studentInfo.put("studentName", ccEduclassStudent.getStr("student_name"));
				// 学生成绩项保存
				studentInfo.put("scoreMap", new HashMap<Long, Object>());
				result.put(studentNo, studentInfo);
			} else {
				studentInfo = (Map<String, Object>) result.get(studentNo);
			}
			Map<Long, Object> scoreMap = (Map<Long, Object>) studentInfo.get("scoreMap");
			Map<String, Object> scoreItem = Maps.newHashMap();
			scoreItem.put("id", ccEduclassStudent.getLong("level_id"));
			scoreItem.put("name", ccEduclassStudent.getStr("level_name"));
			scoreItem.put("score", ccEduclassStudent.getBigDecimal("level_score"));
			scoreMap.put(ccEduclassStudent.getLong("evalute_id"), scoreItem);
		}
		return result;
	}
	
	/**
	 * 获得指标点以及考评点
	 * 
	 * @param courseId
	 * @param eduClassId
	 * @param targetIndicationId
	 * @return
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	private Map<Long, Object> getIndicationInfoEvalute(Long courseId, Long eduClassId, Long targetIndicationId) {
		List<CcReportEduclassEvalute> ccReportEduclassEvalutes = CcReportEduclassEvalute.dao.findAllByCourseId(courseId, eduClassId, targetIndicationId);
		List<CcEvalute> ccEvalutes = CcEvalute.dao.findByEduClassIdAndIndicationId(courseId, eduClassId, targetIndicationId);
		Map<Long, Object> reportEduclassEvalutesMap = Maps.newHashMap();
		Map<Long, Object> result = Maps.newLinkedHashMap();
		Map<String, Object> indicationInfo = null;
		List<Object> evaluteList = null;
		Map<Long, BigDecimal> indicationValueMap = Maps.newHashMap();
		//当没有任何学生考评点成绩时
		if(!ccEvalutes.isEmpty() && ccReportEduclassEvalutes.isEmpty()){
			for(CcEvalute temp : ccEvalutes){
				Long indicationId = temp.getLong("indication_id");
				indicationInfo = Maps.newHashMap();
				indicationInfo.put("indicationId", temp.getLong("indication_id"));
				indicationInfo.put("indicationIndexNum", temp.getInt("index_num"));
				indicationInfo.put("graduateIndexNum", temp.getInt("graduateIndexNum"));
				indicationInfo.put("indicationContent", temp.getStr("content"));
				indicationInfo.put("indicationRemark", temp.getStr("remark"));
				indicationInfo.put("indicationWeight", temp.getBigDecimal("indication_weight"));
				indicationInfo.put("evaluteList", new ArrayList<Object>());
				result.put(indicationId, indicationInfo);
			}
		}
		
		for (CcReportEduclassEvalute ccReportEduclassEvalute : ccReportEduclassEvalutes) {
			Long indicationId = ccReportEduclassEvalute.getLong("indication_id");
			Long evaluteId = ccReportEduclassEvalute.getLong("evalute_id");
			reportEduclassEvalutesMap.put(evaluteId, ccReportEduclassEvalute);
			if (result.get(indicationId) == null) {
				indicationInfo = Maps.newHashMap();
				indicationInfo.put("indicationId", ccReportEduclassEvalute.getLong("indication_id"));
				indicationInfo.put("indicationIndexNum", ccReportEduclassEvalute.getInt("index_num"));
				indicationInfo.put("graduateIndexNum", ccReportEduclassEvalute.getInt("graduateIndexNum"));
				indicationInfo.put("indicationContent", ccReportEduclassEvalute.getStr("content"));
				indicationInfo.put("indicationRemark", ccReportEduclassEvalute.getStr("remark"));
				indicationInfo.put("indicationWeight", ccReportEduclassEvalute.getBigDecimal("indication_weight"));
				indicationInfo.put("evaluteList", new ArrayList<Object>());
				result.put(indicationId, indicationInfo);
				indicationValueMap.put(indicationId, new BigDecimal(0));
			} else {
				indicationInfo = (Map<String, Object>) result.get(indicationId);
			}
			// 考评点详细信息
			evaluteList = (List<Object>) indicationInfo.get("evaluteList");
			Map<String, Object> evaluteDetail = Maps.newHashMap();
			evaluteDetail.put("evaluteId", evaluteId);
			evaluteDetail.put("evaluteIndexNum", ccReportEduclassEvalute.getInt("evalute_index_num"));
			evaluteDetail.put("evaluteContent", ccReportEduclassEvalute.getStr("evalute_content"));
			evaluteDetail.put("evaluteRemark", ccReportEduclassEvalute.getStr("evalute_remark"));
			evaluteDetail.put("evaluteAverage", ccReportEduclassEvalute.getBigDecimal("result"));
			evaluteDetail.put("evaluteWeight", ccReportEduclassEvalute.getBigDecimal("weight"));
			evaluteDetail.put("evaluteTypePercentage", ccReportEduclassEvalute.getInt("evalute_type_percentage"));
			Integer evaluteType = ccReportEduclassEvalute.getInt("evalute_type");
			evaluteDetail.put("evaluteType", evaluteType);
			evaluteDetail.put("evaluteTypeName", DictUtils.findLabelByTypeAndKey("evaluteType", evaluteType));
			evaluteList.add(evaluteDetail);
			// 计算考评点评价值项
			BigDecimal evaluteValue = PriceUtils._mul(ccReportEduclassEvalute.getBigDecimal("result"), ccReportEduclassEvalute.getBigDecimal("weight"));
			indicationValueMap.put(indicationId, PriceUtils._add(indicationValueMap.get(indicationId), PriceUtils._mul(evaluteValue, ccReportEduclassEvalute.getBigDecimal("indication_weight"))));
		}
		
		//某个考评下所有学生成绩为空也显示那一列
		if(ccEvalutes.size() > ccReportEduclassEvalutes.size()){
			if(indicationInfo.get("evaluteList") == null){
				indicationInfo.put("evaluteList", new ArrayList<Object>());
			}	
			evaluteList = (List<Object>) indicationInfo.get("evaluteList");
			for(CcEvalute temp : ccEvalutes){
				Long evaluteId = temp.getLong("evalute_id");
				if(reportEduclassEvalutesMap.get(evaluteId) == null){
					Map<String, Object> evaluteDetail = Maps.newHashMap();
					evaluteDetail.put("evaluteId", evaluteId);
					evaluteDetail.put("evaluteIndexNum", temp.getInt("evalute_index_num"));
					evaluteDetail.put("evaluteContent", temp.getStr("evalute_content"));
					evaluteDetail.put("evaluteRemark", temp.getStr("evalute_remark"));
					evaluteDetail.put("evaluteWeight", temp.getBigDecimal("weight"));
					evaluteDetail.put("evaluteTypePercentage", temp.getInt("evalute_type_percentage"));
					Integer evaluteType = temp.getInt("evalute_type");
					evaluteDetail.put("evaluteType", evaluteType);
					evaluteDetail.put("evaluteTypeName", DictUtils.findLabelByTypeAndKey("evaluteType", evaluteType));
					evaluteList.add(evaluteDetail);
				}
			}
		}
		
		// 将指标点评价值放入返回结果中
		if(!indicationValueMap.isEmpty()){
			for (Map.Entry<Long, Object> entry : result.entrySet()) {
				((Map<String, Object>) entry.getValue()).put("indicationValue", indicationValueMap.get(entry.getKey()).setScale(3, RoundingMode.HALF_UP));
			}
		}
		return result;
	}
	

	/**
	 * 获得统计时间(评分点分析法)
	 * 
	 * @param eduClassId
	 * @return
	 */
	private Date getLastStatisticsDateEvalute(Long eduClassId) {
		List<CcReportEduclassEvalute> ccReportEduclassEvalutes = CcReportEduclassEvalute.dao.findFilteredByColumn("educlass_id", eduClassId);
		if (ccReportEduclassEvalutes.isEmpty()) {
			return null;
		}
		return ccReportEduclassEvalutes.get(0).getDate("statistics_date");
	}
	
	/**
	 * 存在新的更新(评分点分析法)
	 * 
	 * @param eduClassId
	 * @param statisticsDate
	 * @return
	 */
	private boolean needToUpdateEvalute(Long eduClassId) {
		return CcStudentEvalute.dao.needToUpdate(eduClassId);
	} 

}
