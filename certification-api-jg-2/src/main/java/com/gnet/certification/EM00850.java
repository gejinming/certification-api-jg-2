package com.gnet.certification;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradecomposeIndication;
import com.gnet.model.admin.CcEduclassStudent;
import com.gnet.model.admin.CcEduindicationStuScore;
import com.gnet.model.admin.CcGradecompose;
import com.gnet.model.admin.CcIndication;
import com.gnet.model.admin.CcIndicationCourse;
import com.gnet.model.admin.CcTeacherCourse;
import com.gnet.service.CcCourseGradecomposeIndicationService;
import com.gnet.utils.DictUtils;
import com.gnet.utils.PriceUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;

/**
 * 教师报表显示
 * 
 * @author SY
 * @date 2017年12月19日
 */
@Service("EM00850")
public class EM00850 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long courseId = paramsLongFilter(params.get("courseId"));
		Long teacherId = paramsLongFilter(params.get("teacherId"));
		Integer grade = paramsIntegerFilter(params.get("grade"));
		// 是否剔除部分学生的
		Boolean isCaculate = paramsBooleanFilter(params.get("isCaculate"));
		// 默认是的
		isCaculate = isCaculate == null ? Boolean.TRUE : isCaculate;
		Long indicatorPointId = paramsLongFilter(params.get("indicatorPointId"));
		if(params.containsKey("indicatorPointId") && indicatorPointId == null) {
		    return renderFAIL("1009", response, header, "indicatorPointId的参数值非法");
		}
		if (grade == null) {
			return renderFAIL("0316", response, header);
		}
		// 教师编号不能为空过滤
		if (teacherId == null) {
			return renderFAIL("0160", response, header);
		}
		// 课程编号不能为空过滤
		if (courseId == null) {
			return renderFAIL("0250", response, header);
		}
		Map<String, Object> paras = new HashMap<>();
		paras.put("course_id", courseId);
		paras.put("teacher_id", teacherId);
		paras.put("grade", grade);
		paras.put("is_del", CcTeacherCourse.DEL_NO);
		List<CcTeacherCourse> ccTeacherCourseList = CcTeacherCourse.dao.findByColumn(paras);
		// 教师开课课程为空过滤
		if (ccTeacherCourseList == null) {
			return renderFAIL("0501", response, header);
		}
		/****************************************** 验证是否所有教师开课的成绩组成相同 Start ***********************************/
		CcCourseGradecomposeIndicationService ccCourseGradecomposeIndicationService = SpringContextHolder.getBean(CcCourseGradecomposeIndicationService.class); 
		if(!ccCourseGradecomposeIndicationService.validatorSameGradecomposeIndication(ccTeacherCourseList)) {
			return renderFAIL("0329", response, header);
		}
		/****************************************** 验证是否所有教师开课的成绩组成相同 End ***********************************/
		List<Long> teacherCourseIdList = new ArrayList<>();
		for(CcTeacherCourse temp : ccTeacherCourseList) {
			Long teacherCourseId = temp.getLong("id"); 
			teacherCourseIdList.add(teacherCourseId);
		}
		
		// 分析法判断
		Map<String, Object> studentInfo = null;
		Map<Long, Object> indicatorPointInfo = null;
		Integer type = ccTeacherCourseList.get(0).getInt("result_type");
		
		if (CcTeacherCourse.RESULT_TYPE_SCORE.equals(type)) {
			// 考核分析法数据获取
			studentInfo = getStudentInfoScore(teacherCourseIdList, indicatorPointId, isCaculate);
			// 如果是剔除
			if(isCaculate) {
				indicatorPointInfo = getIndicationInfoScoreExcept(teacherCourseIdList, indicatorPointId);				
			} else {
				indicatorPointInfo = getIndicationInfoScore(teacherCourseIdList, indicatorPointId);
			}
			
		} else if (CcTeacherCourse.RESULT_TYPE_EVALUATE.equals(type)) {
			return renderFAIL("0502", response, header);
		} else {
			return renderFAIL("0502", response, header);
		}
		
		// 返回结果
		Map<String, Object> result = Maps.newHashMap();
		result.put("studentInfo", studentInfo);
		result.put("indicatorPointInfo", indicatorPointInfo);
		result.put("resultType", type);
		result.put("resultTypeName", DictUtils.findLabelByTypeAndKey("courseResultType", type));
		return renderSUC(result, response, header);
	}

	/**
	 * 获得学生所有成绩项
	 * 
	 * @param teacherCourseIdList
	 * 			教师开课编号
	 * @param indicatorPointId 
	 * 			指标点编号（必填）
	 * @param isCaculate
	 * 			是否计算
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getStudentInfoScore(List<Long> teacherCourseIdList, Long indicatorPointId, Boolean isCaculate) {
		List<CcEduclassStudent> ccEduclassStudents = CcEduclassStudent.dao.findScoreDetailByTeacherCourseId(teacherCourseIdList, isCaculate);
		Map<String, Object> result = Maps.newLinkedHashMap();
		for (CcEduclassStudent ccEduclassStudent : ccEduclassStudents) {
			String studentNo = ccEduclassStudent.getStr("student_no");
			Long thisIndicationId = ccEduclassStudent.getLong("indicatorPointId");
			if(!thisIndicationId.equals(indicatorPointId)) {
				continue;
			}
			Map<String, Object> studentInfo = null;
			if (result.get(studentNo) == null) {
				studentInfo = Maps.newHashMap();
				// 学生基本信息
				studentInfo.put("id", ccEduclassStudent.getLong("student_id"));
				studentInfo.put("studentNo", studentNo);
				studentInfo.put("studentName", ccEduclassStudent.getStr("student_name"));
				// 学生成绩项保存
				studentInfo.put("scoreMap", new HashMap<String, BigDecimal>());
				result.put(studentNo, studentInfo);
			} else{
				studentInfo = (Map<String, Object>) result.get(studentNo);
			}
			Map<String, Object> scoreMap = (Map<String, Object>) studentInfo.get("scoreMap");
			Map<String, Object> scoreItem = Maps.newHashMap();
			scoreItem.put("score", ccEduclassStudent.getBigDecimal("grade"));
			String key = ccEduclassStudent.getLong("indicationId") + "-" + ccEduclassStudent.getLong("gradecomposeId");
			scoreMap.put(key, scoreItem);
		}
		return result;
	}
	
	/**
	 * 获得指标点以及指标点下的成绩组成
	 *
	 * @param teacherCourseIdList
	 * @param targetIndicatorPointId
	 * 			指标点（可以为空）
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<Long, Object> getIndicationInfoScore(List<Long> teacherCourseIdList, Long targetIndicatorPointId) {
		/*
		 * 1. 获取学生个数
		 * 2. 获取满分
		 * 3. 各个指标点成绩组成的权重
		 * 4. 获取总分与平均分
		 * 5.1 指标点权重
		 * 5.2 获取课程目标信息
		 * 6. 获取所有成绩组成
		 * 7. 准备返回数据：指标点信息，指标点下成绩组成信息
		 */
		// 1. 学生个数
		Long studentNum = CcEduclassStudent.dao.findStudentCounts(teacherCourseIdList.toArray(new Long[teacherCourseIdList.size()]), Boolean.TRUE);
		// 2. 满分 + 3. 权重,  由于每个教师开课的成绩组成都是一样的，所以随便获取一个就可以代表所有
		List<CcCourseGradecomposeIndication> courseGradecomposeIndicationList = CcCourseGradecomposeIndication.dao.findByTeacherCousreIdAndIndicationId(teacherCourseIdList.get(0), targetIndicatorPointId);
		// Map<indicatorPointId, Map<indicationId, Map<gradecomposeId, CcCourseGradecomposeIndication>>>
		Map<Long, Map<Long, Map<Long, CcCourseGradecomposeIndication>>> courseGradecomposeIndicationMap = new HashMap<>();
		// Map<indicatorPointId, allWeight>
		Map<Long, BigDecimal> ccGradecomposeAllWeightMap = new HashMap<>();
		for(CcCourseGradecomposeIndication temp : courseGradecomposeIndicationList) {
			Long indicationId = temp.getLong("indication_id");
			Long indicatorPointId = temp.getLong("indicatorPointId");
			Long gradecomposeId = temp.getLong("gradecomposeId");
			// 指标点
			Map<Long, Map<Long, CcCourseGradecomposeIndication>> ccIndicationCourseGradecomposeIndicationMap = courseGradecomposeIndicationMap.get(indicatorPointId);
			BigDecimal allWeight = ccGradecomposeAllWeightMap.get(indicatorPointId);
			allWeight = allWeight == null ? new BigDecimal(0) : allWeight;
			if(ccIndicationCourseGradecomposeIndicationMap == null || ccIndicationCourseGradecomposeIndicationMap.isEmpty()) {
				ccIndicationCourseGradecomposeIndicationMap = new HashMap<>();
				courseGradecomposeIndicationMap.put(indicatorPointId, ccIndicationCourseGradecomposeIndicationMap);
				
				ccGradecomposeAllWeightMap.put(indicatorPointId, new BigDecimal(0));
			}
			
			// 课程目标
			Map<Long, CcCourseGradecomposeIndication> ccCourseGradecomposeIndicationMap = ccIndicationCourseGradecomposeIndicationMap.get(indicationId);
			if(ccCourseGradecomposeIndicationMap == null || ccCourseGradecomposeIndicationMap.isEmpty()) {
				ccCourseGradecomposeIndicationMap = new HashMap<>();
				ccIndicationCourseGradecomposeIndicationMap.put(indicationId, ccCourseGradecomposeIndicationMap);
			}
			BigDecimal weight = temp.getBigDecimal("weight");
			ccGradecomposeAllWeightMap.put(indicatorPointId, allWeight.add(weight));
			
			ccCourseGradecomposeIndicationMap.put(gradecomposeId, temp);			
			ccIndicationCourseGradecomposeIndicationMap.put(indicationId, ccCourseGradecomposeIndicationMap);
		}
		// 4. 获取总分与平均分
		List<CcEduindicationStuScore> ccEduindicationStuScores = CcEduindicationStuScore.dao.caculateSumInSameGradecomposeAndIndication(teacherCourseIdList, targetIndicatorPointId);
		// Map<indicatorPointId, Map<indicationId, Map<gradecomposeId, totalScore>>>
		Map<Long, Map<Long, Map<Long, BigDecimal>>> ccIndicationGradecomposeReportEduclassGradeMap = new HashMap<>();
		for(CcEduindicationStuScore temp : ccEduindicationStuScores) {
			Long indicatorPointId = temp.getLong("indicatorPointId");
			Long indicationId = temp.getLong("indicationId");
			Long gradecomposeId = temp.getLong("gradecomposeId");
			BigDecimal totalScore = temp.getBigDecimal("totalScore");
			Map<Long, Map<Long, BigDecimal>> ccIndicationReportEduclassGradeMap = ccIndicationGradecomposeReportEduclassGradeMap.get(indicatorPointId);
			if(ccIndicationReportEduclassGradeMap == null || ccIndicationReportEduclassGradeMap.isEmpty()) {
				ccIndicationReportEduclassGradeMap = new HashMap<>();
				ccIndicationGradecomposeReportEduclassGradeMap.put(indicatorPointId, ccIndicationReportEduclassGradeMap);
			}
			Map<Long, BigDecimal> ccReportEduclassGradeMap = ccIndicationReportEduclassGradeMap.get(indicationId);
			if(ccReportEduclassGradeMap == null || ccReportEduclassGradeMap.isEmpty()) {
				ccReportEduclassGradeMap = new HashMap<>();
				ccIndicationReportEduclassGradeMap.put(indicationId, ccReportEduclassGradeMap);
			}
			BigDecimal oldTotalScore = ccReportEduclassGradeMap.get(gradecomposeId);
			oldTotalScore = oldTotalScore == null ? new BigDecimal(0) : oldTotalScore;
			ccReportEduclassGradeMap.put(gradecomposeId, totalScore.add(oldTotalScore));
		}
		// 5.1 指标点权重
		// Map<indicatorPointId, weight>
		Map<Long, BigDecimal> ccIndicationWeightMap = new HashMap<>();
		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findFilteredById(teacherCourseIdList.get(0));
		Long courseId = ccTeacherCourse.getLong("course_id");
		if(targetIndicatorPointId != null) {
			// 当存在目标指标点的时候
			CcIndicationCourse ccIndicationCourse = CcIndicationCourse.dao.findByCourseIdAndIndicationId(courseId, targetIndicatorPointId);
			BigDecimal weight = ccIndicationCourse.getBigDecimal("weight");
			ccIndicationWeightMap.put(targetIndicatorPointId, weight);
		} else {
			// 当不指定的时候
			List<CcIndicationCourse> ccIndicationCourseList = CcIndicationCourse.dao.findDetailByCourseId(courseId);
			for(CcIndicationCourse ccIndicationCourse : ccIndicationCourseList) {
				Long indicatorPointId = ccIndicationCourse.getLong("indication_id");
				BigDecimal weight = ccIndicationCourse.getBigDecimal("weight");
				ccIndicationWeightMap.put(indicatorPointId, weight);
			}
		}
		// 5.2 获取课程目标信息
		List<CcIndication> ccIndications = CcIndication.dao.findAllByCourseId(courseId);
		Map<Long, CcIndication> ccIndicationMap = new HashMap<>();
		List<Long> indicationIdList = new ArrayList<>();
		for(CcIndication temp : ccIndications) {
			Long indicationid = temp.getLong("id");
			if(!indicationIdList.contains(indicationid)) {
				indicationIdList.add(indicationid);
				ccIndicationMap.put(indicationid, temp);
			}
		}
		
		// 6. 获取所有成绩组成
		List<CcGradecompose> ccGradecomposeList = CcGradecompose.dao.findFilteredAll();
		Map<Long, CcGradecompose> ccGradecomposeMap = new HashMap<>();
		for(CcGradecompose temp : ccGradecomposeList) {
			Long gradecomposeId = temp.getLong("id");
			ccGradecomposeMap.put(gradecomposeId, temp);
		}
		
		// 7. 准备返回数据：指标点信息，指标点下成绩组成信息
		Map<Long, Object> result = Maps.newLinkedHashMap();
		// 指标点对象创建
		Map<String, Object> indicatorPointInfo = null;
		// 课程目标信息
		List<Object> indicationInfoList = null;
		// 成绩组成元素详细信息
		List<Object> gradecomposeList = null;
		Map<Long, BigDecimal> indicationValueMap = Maps.newHashMap();
		//某个教学班某个指标点下的所有的开课课程成绩组成指标点关系
		List<CcCourseGradecomposeIndication> courseGradecomposeIndications = CcCourseGradecomposeIndication.dao.findByEduClassIdAndIndicationId(courseId, null, null);
		/*
		 *  第一层，放入指标点
		 *  第二层，放入课程目标
		 *  第三层，放入成绩组成
		 */
		for(CcCourseGradecomposeIndication tempIndicatorPoint : courseGradecomposeIndications){
			Long indicatorPointId = tempIndicatorPoint.getLong("indicatorPointId");
			if(result.get(indicatorPointId) == null && (targetIndicatorPointId == null || targetIndicatorPointId.equals(indicatorPointId))) {
				indicatorPointInfo = Maps.newHashMap();
				indicationInfoList = new ArrayList<>();
//				indicatorPointInfo.put("indicationId", indicatorPointId);
//				indicatorPointInfo.put("indicationIndexNum", temp.getInt("index_num"));
//				indicatorPointInfo.put("graduateIndexNum", temp.getInt("graduateIndexNum"));
//				indicatorPointInfo.put("indicationContent", temp.getStr("content"));
//				indicatorPointInfo.put("indicationRemark", temp.getStr("indicationRemark"));
//				indicatorPointInfo.put("indicationWeight", temp.getBigDecimal("indication_weight"));
//				indicatorPointInfo.put("gradecomposeList", new ArrayList<Object>());
				
				indicatorPointInfo.put("indicatorPointId", indicatorPointId);
				indicatorPointInfo.put("indicatorPointIndexNum", tempIndicatorPoint.getInt("index_num"));
				indicatorPointInfo.put("graduateIndexNum", tempIndicatorPoint.getInt("graduateIndexNum"));
				indicatorPointInfo.put("indicatorPointContent", tempIndicatorPoint.getStr("content"));
				indicatorPointInfo.put("indicatorPointRemark", tempIndicatorPoint.getStr("remark"));
				indicatorPointInfo.put("indicatorPointWeight", tempIndicatorPoint.getBigDecimal("indicationWeight"));
				indicatorPointInfo.put("indicationInfo", indicationInfoList);
				result.put(indicatorPointId, indicatorPointInfo);
			}
		}
		// 课程目标放入
		// 成绩组成放入
		for(Entry<Long, Map<Long, Map<Long, CcCourseGradecomposeIndication>>> indicatorPointAndIndicationAndGradecomposeMap : courseGradecomposeIndicationMap.entrySet()) {
			Long indicatorPointId = indicatorPointAndIndicationAndGradecomposeMap.getKey();
			Map<Long, Map<Long, CcCourseGradecomposeIndication>> indicationAndGradecomposeCcCourseGradecomposeIndicationMap = indicatorPointAndIndicationAndGradecomposeMap.getValue();
			indicatorPointInfo = (Map<String, Object>) result.get(indicatorPointId);
			indicationInfoList = (List<Object>) indicatorPointInfo.get("indicationInfo");
			BigDecimal indicationWeight = ccIndicationWeightMap.get(indicatorPointId);
			// 课程目标放入
			for(Entry<Long, Map<Long, CcCourseGradecomposeIndication>> ccIndicatorPointIndicationCourseGradecomposeIndicationMap : indicationAndGradecomposeCcCourseGradecomposeIndicationMap.entrySet()) {
				Long indicationId = ccIndicatorPointIndicationCourseGradecomposeIndicationMap.getKey();
				Map<Long, CcCourseGradecomposeIndication> ccIndicationCourseGradecomposeIndicationMap = ccIndicatorPointIndicationCourseGradecomposeIndicationMap.getValue();
				
				CcIndication tempIndication = ccIndicationMap.get(indicationId);
				BigDecimal expectedValue = tempIndication.getBigDecimal("expected_value");
				BigDecimal achieveValue = new BigDecimal(0);
				Integer sort = tempIndication.getInt("sort");
				String content = tempIndication.getStr("content");
				// 如果当前指标点存在这个课程目标
				gradecomposeList = new ArrayList<>();
				Map<String, Object> indicationInfo = Maps.newHashMap();
				indicationInfo.put("indicationId", indicationId);
				indicationInfo.put("indicationIndexNum", sort);
				indicationInfo.put("indicationExpectedValue", expectedValue);
				indicationInfo.put("indicationContent", content);
				indicationInfo.put("gradecomposeList", gradecomposeList);
				indicationInfoList.add(indicationInfo);
				BigDecimal mom = new BigDecimal(0);
				BigDecimal son = new BigDecimal(0);
				// 成绩组成放入
				for(Entry<Long, CcCourseGradecomposeIndication> ccCourseGradecomposeIndicationMap : ccIndicationCourseGradecomposeIndicationMap.entrySet()) {
					Long gradecomposeId = ccCourseGradecomposeIndicationMap.getKey();
					CcCourseGradecomposeIndication ccCourseGradecomposeIndication = ccCourseGradecomposeIndicationMap.getValue();
					
					Map<Long, Map<Long, BigDecimal>> ccIndicationReportEduclassGradeMap = ccIndicationGradecomposeReportEduclassGradeMap.get(indicatorPointId);
					BigDecimal totalScore = new BigDecimal(0);
					if(ccIndicationReportEduclassGradeMap != null) {
						Map<Long, BigDecimal> ccReportEduclassGradeMap = ccIndicationReportEduclassGradeMap.get(indicationId);
						if(ccReportEduclassGradeMap != null) {
							totalScore = ccReportEduclassGradeMap.get(gradecomposeId);
						}
					}
					BigDecimal avgScore = studentNum == 0 ? new BigDecimal(0) : totalScore.divide(new BigDecimal(studentNum), 3, RoundingMode.HALF_UP);
					gradecomposeList = (List<Object>) indicationInfo.get("gradecomposeList");
					String gradecomposeName = ccGradecomposeMap.get(gradecomposeId).getStr("name");
					BigDecimal maxScore = ccCourseGradecomposeIndication.getBigDecimal("max_score");
					BigDecimal weight = ccCourseGradecomposeIndication.getBigDecimal("weight");
					
					Map<String, Object> gradecomposeDetail = Maps.newHashMap();
					gradecomposeDetail.put("gradecomposeId", gradecomposeId);
					gradecomposeDetail.put("gradecomposeName", gradecomposeName);
					gradecomposeDetail.put("gradecomposeAverage", avgScore);
					gradecomposeDetail.put("gradecomposeWeight", weight);
					gradecomposeDetail.put("maxScore", maxScore);
					gradecomposeList.add(gradecomposeDetail);
					// 计算成绩组成评价值项
					mom = mom.add(PriceUtils._mul(avgScore, weight));
					son = son.add(PriceUtils._mul(maxScore, weight));
				}
				achieveValue = mom.divide(son, 5, RoundingMode.HALF_UP);
				// 除以期望值
				achieveValue = achieveValue.divide(expectedValue,5, RoundingMode.HALF_UP);
				// 计算完毕成绩组成后，得到课程目标的达成度
				indicationInfo.put("indicationAchieveValue", achieveValue);
				
				// 计算指标点的达成度
				BigDecimal indicationAchieveValue = achieveValue;
				// 乘以权重
				indicationAchieveValue = indicationAchieveValue.multiply(indicationWeight);
				BigDecimal oldValue = indicationValueMap.get(indicatorPointId) == null ? new BigDecimal(0) : indicationValueMap.get(indicatorPointId);
				if(oldValue.compareTo(new BigDecimal(0)) == 0 || oldValue.compareTo(indicationAchieveValue) != -1) {
					// 如果现在的更小
					indicationValueMap.put(indicatorPointId, indicationAchieveValue);
				}
			}
			
		}
		
		// 将指标点评价值放入返回结果中
		if(!indicationValueMap.isEmpty()){
			for (Map.Entry<Long, Object> entry : result.entrySet()) {
				indicatorPointInfo = (Map<String, Object>) entry.getValue();
				indicatorPointInfo.put("indicatorPointValue", indicationValueMap.get(entry.getKey()) == null ? null : indicationValueMap.get(entry.getKey()).setScale(3, RoundingMode.HALF_UP));
			}
		}
		return result;
	}
	
	/**
	 * 获得指标点以及指标点下的成绩组成，剔除学生之后
	 *
	 * @param teacherCourseIdList
	 * @param targetIndicatorPointId
	 * 			指标点（可以为空）
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<Long, Object> getIndicationInfoScoreExcept(List<Long> teacherCourseIdList, Long targetIndicatorPointId) {
		/*
		 * 1. 获取学生个数
		 * 2. 获取满分
		 * 3. 各个指标点成绩组成的权重
		 * 4. 获取总分与平均分
		 * 5.1 指标点权重
		 * 5.2 获取课程目标信息
		 * 6. 获取所有成绩组成
		 * 7. 准备返回数据：指标点信息，指标点下成绩组成信息
		 */
		// 1. 学生个数
		Long studentNum = CcEduclassStudent.dao.findStudentCounts(teacherCourseIdList.toArray(new Long[teacherCourseIdList.size()]), Boolean.TRUE);
		// 2. 满分 + 3. 权重,  由于每个教师开课的成绩组成都是一样的，所以随便获取一个就可以代表所有
		List<CcCourseGradecomposeIndication> courseGradecomposeIndicationList = CcCourseGradecomposeIndication.dao.findByTeacherCousreIdAndIndicationId(teacherCourseIdList.get(0), targetIndicatorPointId);
		// Map<indicatorPointId, Map<indicationId, Map<gradecomposeId, CcCourseGradecomposeIndication>>>
		Map<Long, Map<Long, Map<Long, CcCourseGradecomposeIndication>>> courseGradecomposeIndicationMap = new HashMap<>();
		// Map<indicatorPointId, allWeight>
		Map<Long, BigDecimal> ccGradecomposeAllWeightMap = new HashMap<>();
		for(CcCourseGradecomposeIndication temp : courseGradecomposeIndicationList) {
			Long indicationId = temp.getLong("indication_id");
			Long indicatorPointId = temp.getLong("indicatorPointId");
			Long gradecomposeId = temp.getLong("gradecomposeId");
			// 指标点
			Map<Long, Map<Long, CcCourseGradecomposeIndication>> ccIndicationCourseGradecomposeIndicationMap = courseGradecomposeIndicationMap.get(indicatorPointId);
			BigDecimal allWeight = ccGradecomposeAllWeightMap.get(indicatorPointId);
			allWeight = allWeight == null ? new BigDecimal(0) : allWeight;
			if(ccIndicationCourseGradecomposeIndicationMap == null || ccIndicationCourseGradecomposeIndicationMap.isEmpty()) {
				ccIndicationCourseGradecomposeIndicationMap = new HashMap<>();
				courseGradecomposeIndicationMap.put(indicatorPointId, ccIndicationCourseGradecomposeIndicationMap);
				
				ccGradecomposeAllWeightMap.put(indicatorPointId, new BigDecimal(0));
			}
			
			// 课程目标
			Map<Long, CcCourseGradecomposeIndication> ccCourseGradecomposeIndicationMap = ccIndicationCourseGradecomposeIndicationMap.get(indicationId);
			if(ccCourseGradecomposeIndicationMap == null || ccCourseGradecomposeIndicationMap.isEmpty()) {
				ccCourseGradecomposeIndicationMap = new HashMap<>();
				ccIndicationCourseGradecomposeIndicationMap.put(indicationId, ccCourseGradecomposeIndicationMap);
			}
			BigDecimal weight = temp.getBigDecimal("weight");
			ccGradecomposeAllWeightMap.put(indicatorPointId, allWeight.add(weight));
			
			ccCourseGradecomposeIndicationMap.put(gradecomposeId, temp);			
			ccIndicationCourseGradecomposeIndicationMap.put(indicationId, ccCourseGradecomposeIndicationMap);
		}
		// 4. 获取总分与平均分
		List<CcEduindicationStuScore> ccEduindicationStuScores = CcEduindicationStuScore.dao.caculateSumInSameGradecomposeAndIndication(teacherCourseIdList, targetIndicatorPointId);
		// Map<indicatorPointId, Map<indicationId, Map<gradecomposeId, exceptTotalScore>>>
		Map<Long, Map<Long, Map<Long, BigDecimal>>> ccIndicationGradecomposeReportEduclassGradeExceptMap = new HashMap<>();
		for(CcEduindicationStuScore temp : ccEduindicationStuScores) {
			Long indicatorPointId = temp.getLong("indicatorPointId");
			Long indicationId = temp.getLong("indicationId");
			Long gradecomposeId = temp.getLong("gradecomposeId");
			
			// 剔除后的学生 Start
			BigDecimal exceptTotalScore = temp.getBigDecimal("exceptTotalScore");
			exceptTotalScore = exceptTotalScore == null ? new BigDecimal(0) : exceptTotalScore;
			Map<Long, Map<Long, BigDecimal>> ccIndicationReportEduclassGradeExceptMap = ccIndicationGradecomposeReportEduclassGradeExceptMap.get(indicatorPointId);
			if(ccIndicationReportEduclassGradeExceptMap == null || ccIndicationReportEduclassGradeExceptMap.isEmpty()) {
				ccIndicationReportEduclassGradeExceptMap = new HashMap<>();
				ccIndicationGradecomposeReportEduclassGradeExceptMap.put(indicatorPointId, ccIndicationReportEduclassGradeExceptMap);
			}
			Map<Long, BigDecimal> ccReportEduclassGradeExceptMap = ccIndicationReportEduclassGradeExceptMap.get(indicationId);
			if(ccReportEduclassGradeExceptMap == null || ccReportEduclassGradeExceptMap.isEmpty()) {
				ccReportEduclassGradeExceptMap = new HashMap<>();
				ccIndicationReportEduclassGradeExceptMap.put(indicationId, ccReportEduclassGradeExceptMap);
			}
			BigDecimal oldExceptTotalScore = ccReportEduclassGradeExceptMap.get(gradecomposeId);
			oldExceptTotalScore = oldExceptTotalScore == null ? new BigDecimal(0) : oldExceptTotalScore;
			ccReportEduclassGradeExceptMap.put(gradecomposeId, exceptTotalScore.add(oldExceptTotalScore));
			// 剔除后的学生 End
		}
		// 5.1 指标点权重
		// Map<indicatorPointId, weight>
		Map<Long, BigDecimal> ccIndicationWeightMap = new HashMap<>();
		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findFilteredById(teacherCourseIdList.get(0));
		Long courseId = ccTeacherCourse.getLong("course_id");
		if(targetIndicatorPointId != null) {
			// 当存在目标指标点的时候
			CcIndicationCourse ccIndicationCourse = CcIndicationCourse.dao.findByCourseIdAndIndicationId(courseId, targetIndicatorPointId);
			BigDecimal weight = ccIndicationCourse.getBigDecimal("weight");
			ccIndicationWeightMap.put(targetIndicatorPointId, weight);
		} else {
			// 当不指定的时候
			List<CcIndicationCourse> ccIndicationCourseList = CcIndicationCourse.dao.findDetailByCourseId(courseId);
			for(CcIndicationCourse ccIndicationCourse : ccIndicationCourseList) {
				Long indicatorPointId = ccIndicationCourse.getLong("indication_id");
				BigDecimal weight = ccIndicationCourse.getBigDecimal("weight");
				ccIndicationWeightMap.put(indicatorPointId, weight);
			}
		}
		// 5.2 获取课程目标信息
		List<CcIndication> ccIndications = CcIndication.dao.findAllByCourseId(courseId);
		Map<Long, CcIndication> ccIndicationMap = new HashMap<>();
		List<Long> indicationIdList = new ArrayList<>();
		for(CcIndication temp : ccIndications) {
			Long indicationid = temp.getLong("id");
			if(!indicationIdList.contains(indicationid)) {
				indicationIdList.add(indicationid);
				ccIndicationMap.put(indicationid, temp);
			}
		}
		
		// 6. 获取所有成绩组成
		List<CcGradecompose> ccGradecomposeList = CcGradecompose.dao.findFilteredAll();
		Map<Long, CcGradecompose> ccGradecomposeMap = new HashMap<>();
		for(CcGradecompose temp : ccGradecomposeList) {
			Long gradecomposeId = temp.getLong("id");
			ccGradecomposeMap.put(gradecomposeId, temp);
		}
		
		// 7. 准备返回数据：指标点信息，指标点下成绩组成信息
		Map<Long, Object> result = Maps.newLinkedHashMap();
		// 指标点对象创建
		Map<String, Object> indicatorPointInfo = null;
		// 课程目标信息
		List<Object> indicationInfoList = null;
		// 成绩组成元素详细信息
		List<Object> gradecomposeList = null;
		Map<Long, BigDecimal> indicationExceptValueMap = Maps.newHashMap();
		//某个教学班某个指标点下的所有的开课课程成绩组成指标点关系
		List<CcCourseGradecomposeIndication> courseGradecomposeIndications = CcCourseGradecomposeIndication.dao.findByEduClassIdAndIndicationId(courseId, null, null);
		/*
		 *  第一层，放入指标点
		 *  第二层，放入课程目标
		 *  第三层，放入成绩组成
		 */
		for(CcCourseGradecomposeIndication tempIndicatorPoint : courseGradecomposeIndications){
			Long indicatorPointId = tempIndicatorPoint.getLong("indicatorPointId");
			if(result.get(indicatorPointId) == null && (targetIndicatorPointId == null || targetIndicatorPointId.equals(indicatorPointId))) {
				indicatorPointInfo = Maps.newHashMap();
				indicationInfoList = new ArrayList<>();
//				indicatorPointInfo.put("indicationId", indicatorPointId);
//				indicatorPointInfo.put("indicationIndexNum", temp.getInt("index_num"));
//				indicatorPointInfo.put("graduateIndexNum", temp.getInt("graduateIndexNum"));
//				indicatorPointInfo.put("indicationContent", temp.getStr("content"));
//				indicatorPointInfo.put("indicationRemark", temp.getStr("indicationRemark"));
//				indicatorPointInfo.put("indicationWeight", temp.getBigDecimal("indication_weight"));
//				indicatorPointInfo.put("gradecomposeList", new ArrayList<Object>());
				
				indicatorPointInfo.put("indicatorPointId", indicatorPointId);
				indicatorPointInfo.put("indicatorPointIndexNum", tempIndicatorPoint.getInt("index_num"));
				indicatorPointInfo.put("graduateIndexNum", tempIndicatorPoint.getInt("graduateIndexNum"));
				indicatorPointInfo.put("indicatorPointContent", tempIndicatorPoint.getStr("content"));
				indicatorPointInfo.put("indicatorPointRemark", tempIndicatorPoint.getStr("remark"));
				indicatorPointInfo.put("indicatorPointWeight", tempIndicatorPoint.getBigDecimal("indicationWeight"));
				indicatorPointInfo.put("indicationInfo", indicationInfoList);
				result.put(indicatorPointId, indicatorPointInfo);
			}
		}
		// 课程目标放入
		// 成绩组成放入
		for(Entry<Long, Map<Long, Map<Long, CcCourseGradecomposeIndication>>> indicatorPointAndIndicationAndGradecomposeMap : courseGradecomposeIndicationMap.entrySet()) {
			Long indicatorPointId = indicatorPointAndIndicationAndGradecomposeMap.getKey();
			Map<Long, Map<Long, CcCourseGradecomposeIndication>> indicationAndGradecomposeCcCourseGradecomposeIndicationMap = indicatorPointAndIndicationAndGradecomposeMap.getValue();
			indicatorPointInfo = (Map<String, Object>) result.get(indicatorPointId);
			indicationInfoList = (List<Object>) indicatorPointInfo.get("indicationInfo");
			BigDecimal indicationWeight = ccIndicationWeightMap.get(indicatorPointId);
			// 课程目标放入
			for(Entry<Long, Map<Long, CcCourseGradecomposeIndication>> ccIndicatorPointIndicationCourseGradecomposeIndicationMap : indicationAndGradecomposeCcCourseGradecomposeIndicationMap.entrySet()) {
				Long indicationId = ccIndicatorPointIndicationCourseGradecomposeIndicationMap.getKey();
				Map<Long, CcCourseGradecomposeIndication> ccIndicationCourseGradecomposeIndicationMap = ccIndicatorPointIndicationCourseGradecomposeIndicationMap.getValue();
				
				CcIndication tempIndication = ccIndicationMap.get(indicationId);
				BigDecimal expectedValue = tempIndication.getBigDecimal("expected_value");
				BigDecimal exceptAchieveValue = new BigDecimal(0);
				Integer sort = tempIndication.getInt("sort");
				String content = tempIndication.getStr("content");
				// 如果当前指标点存在这个课程目标
				gradecomposeList = new ArrayList<>();
				Map<String, Object> indicationInfo = Maps.newHashMap();
				indicationInfo.put("indicationId", indicationId);
				indicationInfo.put("indicationIndexNum", sort);
				indicationInfo.put("indicationExpectedValue", expectedValue);
				indicationInfo.put("indicationContent", content);
				indicationInfo.put("gradecomposeList", gradecomposeList);
				indicationInfoList.add(indicationInfo);
				// 剔除学生后的成绩的mom
				BigDecimal exceptMom = new BigDecimal(0);
				BigDecimal son = new BigDecimal(0);
				// 成绩组成放入
				for(Entry<Long, CcCourseGradecomposeIndication> ccCourseGradecomposeIndicationMap : ccIndicationCourseGradecomposeIndicationMap.entrySet()) {
					Long gradecomposeId = ccCourseGradecomposeIndicationMap.getKey();
					CcCourseGradecomposeIndication ccCourseGradecomposeIndication = ccCourseGradecomposeIndicationMap.getValue();
					
					// 剔除学生之后的
					Map<Long, Map<Long, BigDecimal>> ccIndicationReportEduclassGradeExceptMap = ccIndicationGradecomposeReportEduclassGradeExceptMap.get(indicatorPointId);
					BigDecimal exceptTotalScore = new BigDecimal(0);
					if(ccIndicationReportEduclassGradeExceptMap != null) {
						Map<Long, BigDecimal> ccReportEduclassGradeMap = ccIndicationReportEduclassGradeExceptMap.get(indicationId);
						if(ccReportEduclassGradeMap != null) {
							exceptTotalScore = ccReportEduclassGradeMap.get(gradecomposeId);
						}
					}
					BigDecimal exceptAvgScore = studentNum == 0 ? new BigDecimal(0) : exceptTotalScore.divide(new BigDecimal(studentNum), 3, RoundingMode.HALF_UP);
					gradecomposeList = (List<Object>) indicationInfo.get("gradecomposeList");
					String gradecomposeName = ccGradecomposeMap.get(gradecomposeId).getStr("name");
					BigDecimal maxScore = ccCourseGradecomposeIndication.getBigDecimal("max_score");
					BigDecimal weight = ccCourseGradecomposeIndication.getBigDecimal("weight");
					
					Map<String, Object> gradecomposeDetail = Maps.newHashMap();
					gradecomposeDetail.put("gradecomposeId", gradecomposeId);
					gradecomposeDetail.put("gradecomposeName", gradecomposeName);
					gradecomposeDetail.put("gradecomposeAverage", exceptAvgScore);
					gradecomposeDetail.put("gradecomposeWeight", weight);
					gradecomposeDetail.put("maxScore", maxScore);
					gradecomposeList.add(gradecomposeDetail);
					// 计算成绩组成评价值项
					exceptMom = exceptMom.add(PriceUtils._mul(exceptAvgScore, weight));
					son = son.add(PriceUtils._mul(maxScore, weight));
				}
				exceptAchieveValue = exceptMom.divide(son, 5, RoundingMode.HALF_UP);
				// 除以期望值
				exceptAchieveValue = exceptAchieveValue.divide(expectedValue,5, RoundingMode.HALF_UP);
				// 计算完毕成绩组成后，得到课程目标的达成度
				indicationInfo.put("indicationAchieveValue", exceptAchieveValue);
				
				
				// 计算指标点的剔除学生后达成度
				BigDecimal indicationAchieveValue = exceptAchieveValue;
				// 乘以权重
				indicationAchieveValue = indicationAchieveValue.multiply(indicationWeight);
				BigDecimal oldExceptValue = indicationExceptValueMap.get(indicatorPointId) == null ? new BigDecimal(0) : indicationExceptValueMap.get(indicatorPointId);
				if(oldExceptValue.compareTo(new BigDecimal(0)) == 0 || oldExceptValue.compareTo(indicationAchieveValue) != -1) {
					// 如果现在的更小
					indicationExceptValueMap.put(indicatorPointId, indicationAchieveValue);
				}
			}
			
		}
		
		// 将指标点评价值放入返回结果中
		if(!indicationExceptValueMap.isEmpty()){
			for (Map.Entry<Long, Object> entry : result.entrySet()) {
				indicatorPointInfo = (Map<String, Object>) entry.getValue();
				indicatorPointInfo.put("indicatorPointValue", indicationExceptValueMap.get(entry.getKey()) == null ? null : indicationExceptValueMap.get(entry.getKey()).setScale(3, RoundingMode.HALF_UP));
			}
		}
		return result;
	}
}
