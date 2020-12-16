package com.gnet.certification;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcEduclassStudent;
import com.gnet.model.admin.CcEvalute;
import com.gnet.model.admin.CcGradecompose;
import com.gnet.model.admin.CcReportEduclassEvalute;
import com.gnet.model.admin.CcReportEduclassGrade;
import com.gnet.model.admin.CcScoreStuIndigrade;
import com.gnet.model.admin.CcStudentEvalute;
import com.gnet.model.admin.CcTeacherCourse;
import com.gnet.service.CcEvaluteLevelService;
import com.gnet.utils.DictUtils;
import com.google.common.collect.Maps;

/**
 * 教学班报表显示--显示优良等信息，而不是分数（在EM00552基础上，多做了一步计算）
 *
 * @author SY
 * @date 2017年8月10日17:20:24
 */
@Service("EM00553")
public class EM00553 extends BaseApi implements IApi {

	@Autowired
	private CcEvaluteLevelService ccEvaluteLevelService;
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long eduClassId = paramsLongFilter(params.get("eduClassId"));
		// 教学班编号不能为空过滤
		if (eduClassId == null) {
			return renderFAIL("0500", response, header);
		}
		CcTeacherCourse ccTeacherCourse = findByClassId(eduClassId);
		// 教师开课课程为空过滤
		if (ccTeacherCourse == null) {
			return renderFAIL("0501", response, header);
		}
		Long teacherCourseId = ccTeacherCourse.getLong("id");

		// 分析法判断
		Map<String, Object> studentInfo = null;
		Date statisticsDate = null;
		Boolean needUpdate = null;
		// 获取考评点的分类列表
		Map<Integer, Integer> evaluteTypes = new HashMap<>();
		// 返回结果
		Map<String, Object> result = Maps.newHashMap();
		//TODO 2020.12.11考核分析法和评分表分析法显示一样
		//if (CcTeacherCourse.RESULT_TYPE_SCORE.equals(ccTeacherCourse.getInt("result_type"))) {
			// 考核分析法数据获取
			Map<Long, Map<String, Object>> gradecomposeAndNames = new HashMap<>();
			List<CcGradecompose> ccGradecomposes = CcGradecompose.dao.findGradecomposeByTeacherCourseId(teacherCourseId);
			for(CcGradecompose temp : ccGradecomposes) {
				Map<String, Object> percentageAndName = new HashMap<>();
				Integer percentage = temp.getInt("percentage");
				Long courseGradecomposeId = temp.getLong("courseGradecomposeId");
				Long gradecomposeId = temp.getLong("id");
				String gradecomposeName = temp.getStr("name");
				percentageAndName.put("percentage", percentage);
				percentageAndName.put("gradecomposeName", gradecomposeName);
				percentageAndName.put("gradecomposeId", gradecomposeId);
				gradecomposeAndNames.put(courseGradecomposeId, percentageAndName);
			}
			// 表头
			result.put("gradecomposes", gradecomposeAndNames);

			/*
			 * courseGradecomposeIndicationMaxScoreMap
			 * 		某个课程成绩组成，的指标点满分(不包括了其他分数)
			 * 		 key:course_gradecompose_id,indication_id
			 * 		 value：max_score
			 */
			Map<Long, BigDecimal> courseGradecomposeMaxScoreMap = Maps.newLinkedHashMap();
			/*
			 * 某个成绩组成的其他分数
			 */
			Map<Long, BigDecimal> courseGradecomposeOtherScoreMap = Maps.newLinkedHashMap();
			studentInfo = getStudentInfoScore(eduClassId, courseGradecomposeMaxScoreMap, courseGradecomposeOtherScoreMap);
			calculateForCourseGradecompose(studentInfo, courseGradecomposeMaxScoreMap, courseGradecomposeOtherScoreMap);
			// 最近一次统计时间
			statisticsDate = getLastStatisticsDateScore(eduClassId);
			// 有记录变动建议重新生成报表
			needUpdate = needToUpdateScore(eduClassId);

		/*} else if (CcTeacherCourse.RESULT_TYPE_EVALUATE.equals(ccTeacherCourse.getInt("result_type"))) {
			// 评分表分析法数据获取
			Map<Integer, Map<String, Object>> evaluteTypeAndNames = new HashMap<>();
			List<CcEvalute> ccEvalutes = CcEvalute.dao.findAllByEduClass(eduClassId);
			for(CcEvalute temp : ccEvalutes) {
				Integer percentage = temp.getInt("type_percentage");
				percentage = percentage == null ? 0 : percentage;
				Integer type = temp.getInt("type");
				evaluteTypes.put(type, percentage);

				Map<String, Object> typeAndName = new HashMap<>();
				typeAndName.put("name", DictUtils.findLabelByTypeAndKey("evaluteType", type));
				typeAndName.put("percentage", percentage);
				evaluteTypeAndNames.put(type, typeAndName);
			}
			// 表头
			result.put("evaluteTypes", evaluteTypeAndNames);

			studentInfo = getStudentInfoEvalute(eduClassId);
			calculateForType(studentInfo, evaluteTypes);
			// 最近一次统计时间
			statisticsDate = getLastStatisticsDateEvalute(eduClassId);
			// 有记录变动建议重新生成报表
			needUpdate = needToUpdateEvalute(eduClassId);
		} else {
			return renderFAIL("0502", response, header);
		}*/

		result.put("studentInfo", studentInfo);
		result.put("statisticsDate", statisticsDate);
		result.put("needUpdate", needUpdate);
		result.put("resultType", ccTeacherCourse.getInt("result_type"));
		result.put("resultTypeName", DictUtils.findLabelByTypeAndKey("courseResultType", ccTeacherCourse.getInt("result_type")));
		return renderSUC(result, response, header);
	}

	/**
	 * 把学生通过不同成绩组成来分类成绩。
	 * @param studentInfo
	 * @param courseGradecomposeIndicationMaxScoreMap
	 * 		某个课程成绩组成，的指标点满分(不包括了其他分数)
	 * 		 key:course_gradecompose_id
	 * 		 value：max_score
	 * @param courseGradecomposeOtherScoreMap
	 * 		某个课程成绩组成的其他分数
	 * @author SY
	 * @version 创建时间：2017年8月16日 上午11:24:44
	 */
	@SuppressWarnings("unchecked")
	private void calculateForCourseGradecompose(Map<String, Object> studentInfo, Map<Long, BigDecimal> courseGradecomposeIndicationMaxScoreMap, Map<Long, BigDecimal> courseGradecomposeOtherScoreMap) {
		/*
		 * "studentInfo": {
				"jyxs002": {
					"studentNo": "jyxs002",
					"studentName": "九月学生2",
					// 总评成绩
					"generalScore": 76
					"scoreMap": {
						// 一个个成绩组成
						"210893": {
							"score": 0.600
						},
						"null": {

						}
					}
				}
			}
		 */
		// 遍历每个人
		for (Map.Entry<String, Object> entry : studentInfo.entrySet()) {
			// 获取这个人
			Map<String, Object> studentMessageMap = (Map<String, Object>) entry.getValue();
			// 获取这个人的得分情况
			Map<String, Object> scoreMap = (Map<String, Object>) studentMessageMap.get("scoreMap");
			Map<Long, Object> newScoreMap = new HashMap<>();
			// 当前学生这门课所有的成绩组成占比之和
			float allPercentage = 0;

			// 遍历这个人所有的指标点成绩
			for(Map.Entry<String, Object> gradecomposeIndicationMap : scoreMap.entrySet()) {
				// 获取指标点中的信息
				Map<String, Object> gradecomposeIndication = (Map<String, Object>) gradecomposeIndicationMap.getValue();
				Long courseGradecomposeId = (Long) gradecomposeIndication.get("courseGradecomposeId");
				// 所有加起来的分数
				BigDecimal addScore = (BigDecimal) gradecomposeIndication.get("score");
				// Tip 成绩组成的分值 = 学生个人此成绩组成分数 除以 指标点总分在满分中的占比 = addScore / （指标点的满分 / 满分） 即 成绩组成的分值 = addScore * 满分 / 指标点的满分  
				// 某个成绩组成的指标点满分
				BigDecimal gradecomposeAllIndicationMaxScore = courseGradecomposeIndicationMaxScoreMap.get(courseGradecomposeId);
				// 某个成绩组成的其他分数
				BigDecimal gradecomposeOtherScore = courseGradecomposeOtherScoreMap.get(courseGradecomposeId);
				gradecomposeOtherScore = gradecomposeOtherScore == null ? new BigDecimal(0) : gradecomposeOtherScore;
				// 某个成绩组成的满分
				BigDecimal gradecomposeOtherMaxScore = gradecomposeAllIndicationMaxScore.add(gradecomposeOtherScore);
				addScore = addScore == null ? new BigDecimal(0) : addScore;
				// 是否已经有了
				Map<String, Object> newCourseGradecomposeMap = (Map<String, Object>) newScoreMap.get(courseGradecomposeId);
				if(newCourseGradecomposeMap == null) {
					// 如果是第一次，新建，并且加入其它分数
					newCourseGradecomposeMap = new HashMap<>();
					BigDecimal otherScore = (BigDecimal) gradecomposeIndication.get("otherScore");
					BigDecimal otherAndThisScore = otherScore == null ? addScore : addScore.add(otherScore);
					Integer percentageInt = (Integer)(gradecomposeIndication.get("percentage") == null ? 0 : gradecomposeIndication.get("percentage"));
					allPercentage = allPercentage + percentageInt;
					newCourseGradecomposeMap.put("addScore", otherAndThisScore);
					newCourseGradecomposeMap.put("percentage", percentageInt);
					newCourseGradecomposeMap.put("score", otherAndThisScore.multiply(gradecomposeOtherMaxScore).divide(gradecomposeAllIndicationMaxScore , 5, BigDecimal.ROUND_HALF_UP));
				} else {
					BigDecimal newScore = ((BigDecimal) newCourseGradecomposeMap.get("addScore")).add(addScore);
					// newScore 还需要除以指标点总分在满分中的占比
					newCourseGradecomposeMap.put("addScore", newScore);
					newCourseGradecomposeMap.put("score", newScore.multiply(gradecomposeOtherMaxScore).divide(gradecomposeAllIndicationMaxScore, 5, BigDecimal.ROUND_HALF_UP));
				}
				DecimalFormat df = new DecimalFormat("0.00");
				Integer percentageInt = (Integer)(gradecomposeIndication.get("percentage") == null ? 0 : gradecomposeIndication.get("percentage"));
				float a = percentageInt;
				float b = 100;
				float newScore = a/b;
				BigDecimal percentage = new BigDecimal(df.format(newScore));
				addScore = addScore.multiply(percentage);
				newScoreMap.put(courseGradecomposeId, newCourseGradecomposeMap);
			}
			// 当前指标点下当前类型考评点总分多少了（用于作为求平均分的分子）
			BigDecimal allScore = new BigDecimal(0);
			// 计算总分
			for(Map.Entry<Long, Object> entrySencond : newScoreMap.entrySet()) {
				Map<Long, Object> newScore = (Map<Long, Object>) entrySencond.getValue();
				BigDecimal score = (BigDecimal) newScore.get("score");
				Integer percentage = (Integer)newScore.get("percentage");
				BigDecimal newPercentage = new BigDecimal(percentage).divide(new BigDecimal(100), 3, BigDecimal.ROUND_HALF_UP);
				score = score.multiply(newPercentage);
				allScore = allScore.add(score);
			}
			// 需要除以 成绩组成的 占比之和
			studentMessageMap.put("generalScore", allScore.divide(new BigDecimal(allPercentage), 3,  BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
			studentMessageMap.put("scoreMap", newScoreMap);
		}
	}

	/**
	 * 把学生的成绩通过type的不同，计算出一个数值，用于页面显示他的中文（优、良... / 及格、不及格）
	 * @param studentInfo
	 * @param evaluteTypes
	 * @author SY
	 * @version 创建时间：2017年8月10日 下午5:43:27
	 */
	@SuppressWarnings("unchecked")
	private void calculateForType(Map<String, Object> studentInfo, Map<Integer, Integer> evaluteTypes) {
		/*
		 * "studentInfo": {
				"jyxs002": {
					"studentNo": "jyxs002",
					"studentName": "九月学生2",
					"generalScore": "64.2",
					"generalScoreName": "及格",
					"scoreMapList":这里是一个list，用于吧scoreMap以List<Map<Long,Object>>形式存下来。
								       主要是应为遍历scoreMap的时候，是按照key的大小来遍历，不是按照存入顺序。导致数据不连续，从而产生错误数据
					"scoreMap": {
						"210893": {
							"id": 210897,
							"name": "良好",
							"score": 0.600,
							"allScoreAPart": 21
							"type": 0
						},
						"210895": {
							"id": 210898,
							"name": "优秀",
							"score": 1.000,
							"allScoreAPart": 21
							"type": 1
						},
						"null": {

						}
					}
				}
			}
		 */
		// 遍历指标点类型，得到他们一起的占比为多少
		float allTypePercentage = 0;
		for (Map.Entry<Integer, Integer> entry : evaluteTypes.entrySet()) {
			allTypePercentage = allTypePercentage + entry.getValue();
		}
		allTypePercentage = allTypePercentage / 100;
		// 遍历每个人
		for (Map.Entry<String, Object> entry : studentInfo.entrySet()) {
			// 获取这个人
			Map<String, Object> studentMessageMap = (Map<String, Object>) entry.getValue();
			// 获取这个人的得分情况
			List<Long> scoreMapList = (List<Long>) studentMessageMap.get("scoreMapList");
			Map<String, Object> scoreMap = (Map<String, Object>) studentMessageMap.get("scoreMap");
			Map<String, Object> newScoreMap = new HashMap<>();

			// 之前的type是多少
			Integer oldType = 0;
			Integer level = null;
			// 当前指标点下当前类型考评点计数到几个了（用于作为求品均分的分母）
			Integer num = 0;
			// 当前指标点下当前类型考评点总分多少了（用于作为求品均分的分子）
			BigDecimal allScore = new BigDecimal(0);
			// 遍历这个人所有的指考评点
			for(Long scoreMapKey : scoreMapList) {
//			for(Map.Entry<String, Object> evaluteMap : scoreMap.entrySet()) {
				// 获取指标点中的信息
				Map<String, Object> evalute = (Map<String, Object>)scoreMap.get(scoreMapKey);
//				Map<String, Object> evalute = (Map<String, Object>) evaluteMap.getValue();
				Integer type = (Integer) evalute.get("type");
				level = (Integer) evalute.get("level");
				// 当发现指标点的类型发生变化，并且个数不是0的时候
				if(!oldType.equals(type) && num != 0 && level != null) {
					Map<String, Object> newEvaluteMap = new HashMap<>();
					BigDecimal average = allScore.divide(new BigDecimal(num), 2, BigDecimal.ROUND_HALF_UP);
					newEvaluteMap.put("score", average);
					String name = ccEvaluteLevelService.calculateToNameValue(average, level);
//					String name = ccEvaluteLevelService.calculateToName(average, level);
					newEvaluteMap.put("name", name);
					// 平均分乘以百分比
					BigDecimal allScoreAPart = average.multiply(new BigDecimal(evaluteTypes.get(oldType)));
					newEvaluteMap.put("allScoreAPart", allScoreAPart);
					newScoreMap.put(oldType.toString(), newEvaluteMap);
					// 计数数据清空和重置数据
					num = 0;
					allScore = new BigDecimal(0);
				}
				BigDecimal score = (BigDecimal) evalute.get("score");
				score = score == null ? new BigDecimal(0) : score;
				allScore = allScore.add(score);
				oldType = type;
				num++;
			}
			// 循环内是每次更改type保存一次，最后一次的没保存，所以这里保存
			Map<String, Object> newEvaluteMap = new HashMap<>();
			if(num != 0) {
				BigDecimal average = allScore.divide(new BigDecimal(num), 2, BigDecimal.ROUND_HALF_UP);
				newEvaluteMap.put("score", average);
				String name = ccEvaluteLevelService.calculateToNameValue(average, level);
//				String name = ccEvaluteLevelService.calculateToName(average, level);
				newEvaluteMap.put("name", name);
				// 平均分乘以百分比
				BigDecimal allScoreAPart = average.multiply(new BigDecimal(evaluteTypes.get(oldType)));
				newEvaluteMap.put("allScoreAPart", allScoreAPart);
				newScoreMap.put(oldType.toString(), newEvaluteMap);
			}
			

			// 计算总评成绩
			BigDecimal generalScore = new BigDecimal(0);
			for(Map.Entry<String, Object> typeMap : newScoreMap.entrySet()) {
				Map<String, Object> evalute = (Map<String, Object>) typeMap.getValue();
				generalScore = generalScore.add((BigDecimal) evalute.get("allScoreAPart"));
			}
			// (总的成绩)除以（考评点类型占比之和）
			if(!generalScore.equals(new BigDecimal(0))) {
				generalScore = generalScore.divide(new BigDecimal(allTypePercentage), 3 ,BigDecimal.ROUND_HALF_UP);
				studentMessageMap.put("generalScore", generalScore);
				float generalScoreFirst = generalScore.floatValue();
				float generalScoreHun = 100;
				float generalScoreEnd = generalScoreFirst/generalScoreHun;
//				String generalScoreName = ccEvaluteLevelService.calculateToName(new BigDecimal(generalScoreEnd), level);
				String generalScoreName = ccEvaluteLevelService.calculateToNameValue(new BigDecimal(generalScoreEnd), level);
				studentMessageMap.put("generalScoreName", generalScoreName);
				studentMessageMap.put("scoreMap", newScoreMap);
			}
		}

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
	 * @param courseGradecomposeMaxScoreMap
	 * 		某个课程成绩组成，的指标点满分(不包括了其他分数)
	 * 		 key:course_gradecompose_id
	 * 		 value：max_score
	 * @param courseGradecomposeOtherScoreMap
	 * 		某个课程成绩组成的其他分数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getStudentInfoScore(Long eduClassId, Map<Long, BigDecimal> courseGradecomposeMaxScoreMap, Map<Long, BigDecimal> courseGradecomposeOtherScoreMap) {
		List<CcEduclassStudent> ccEduclassStudents = CcEduclassStudent.dao.findScoreDetailWithTypeAndIndicationByClassId(eduClassId, Boolean.TRUE);
		Map<String, Object> result = Maps.newLinkedHashMap();
		// 某个课程成绩组成的某个指标点，的满分(包括了其他分数) key:course_gradecompose_id,indication_id . value：max_score
		Map<String, BigDecimal> courseGradecomposeIndicationMaxScoreMap = Maps.newLinkedHashMap();
		for (CcEduclassStudent ccEduclassStudent : ccEduclassStudents) {
			String studentNo = ccEduclassStudent.getStr("student_no");
			Map<String, Object> studentInfo = null;
			if (result.get(studentNo) == null) {
				studentInfo = Maps.newHashMap();
				// 学生基本信息
				studentInfo.put("id", ccEduclassStudent.getLong("student_id"));
				studentInfo.put("studentNo", studentNo);
				studentInfo.put("studentName", ccEduclassStudent.getStr("student_name"));
				studentInfo.put("studentStudyRemark", DictUtils.findLabelByTypeAndKey("studentIndicationRemark" ,ccEduclassStudent.getInt("student_study_remark")));
				studentInfo.put("isRetake", ccEduclassStudent.getBoolean("is_retake"));
				// 学生成绩项保存
				studentInfo.put("scoreMap", new HashMap<Long, BigDecimal>());
				result.put(studentNo, studentInfo);
			} else{
				studentInfo = (Map<String, Object>) result.get(studentNo);
			}
			Map<Long, Object> scoreMap = (Map<Long, Object>) studentInfo.get("scoreMap");
			Map<String, Object> scoreItem = Maps.newHashMap();
			Long courseGradecomposeId = ccEduclassStudent.getLong("course_gradecompose_id");
			scoreItem.put("score", ccEduclassStudent.getBigDecimal("grade"));
			scoreItem.put("percentage", ccEduclassStudent.getInt("percentage"));
			scoreItem.put("otherScore", ccEduclassStudent.getBigDecimal("other_score"));
			scoreItem.put("courseGradecomposeId", courseGradecomposeId);
			scoreMap.put(ccEduclassStudent.getLong("gradecomposeIndicationId"), scoreItem);
			
			Long indicationId = ccEduclassStudent.getLong("indication_id");
			BigDecimal maxScore = ccEduclassStudent.getBigDecimal("max_score");
			String courseGradecomposeIndicationMaxScoreKey = courseGradecomposeId + "," + indicationId;
			courseGradecomposeIndicationMaxScoreMap.put(courseGradecomposeIndicationMaxScoreKey, maxScore);
			
			BigDecimal gradecomposeOtherScore = ccEduclassStudent.getBigDecimal("gradecompose_other_score");
			courseGradecomposeOtherScoreMap.put(courseGradecomposeId, gradecomposeOtherScore);
		}
		
		// 遍历课程组成指标点的满分，得到某个课程组成的指标点满分之和
		for(Map.Entry<String, BigDecimal> entry : courseGradecomposeIndicationMaxScoreMap.entrySet()) {
			String key = entry.getKey();
			String keys[] = key.split(",");
			Long courseGradecomposeId = Long.valueOf(keys[0]);
			BigDecimal maxScore = entry.getValue();
			BigDecimal maxScoreAll = courseGradecomposeMaxScoreMap.get(courseGradecomposeId);
			maxScoreAll = maxScoreAll == null ? new BigDecimal(0) : maxScoreAll;
			maxScoreAll = maxScoreAll.add(maxScore);
			courseGradecomposeMaxScoreMap.put(courseGradecomposeId, maxScoreAll);
		}
		return result;
	}

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
		List<CcEduclassStudent> ccEduclassStudents = CcEduclassStudent.dao.findEvaluteDetailWithTypeAndIndicationByClassId(eduClassId);
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
				studentInfo.put("studentStudyRemark",  DictUtils.findLabelByTypeAndKey("studentIndicationRemark" ,ccEduclassStudent.getInt("student_study_remark")));
				studentInfo.put("isRetake", ccEduclassStudent.getBoolean("is_retake"));
				// 学生成绩项保存
				studentInfo.put("scoreMap", new HashMap<Long, Object>());
				studentInfo.put("scoreMapList", new ArrayList<Map<Long, Object>>());
				result.put(studentNo, studentInfo);
			} else {
				studentInfo = (Map<String, Object>) result.get(studentNo);
			}
			List<Long> scoreMapList = (List<Long>) studentInfo.get("scoreMapList");
			Map<Long, Object> scoreMap = (Map<Long, Object>) studentInfo.get("scoreMap");
			Map<String, Object> scoreItem = Maps.newHashMap();
			scoreItem.put("id", ccEduclassStudent.getLong("level_id"));
			scoreItem.put("name", ccEduclassStudent.getStr("level_name"));
			scoreItem.put("score", ccEduclassStudent.getBigDecimal("level_score"));
			scoreItem.put("type", ccEduclassStudent.getInt("evalute_type"));
			scoreItem.put("level", ccEduclassStudent.getInt("level"));
			scoreItem.put("indication_id", ccEduclassStudent.getLong("indication_id"));
			if(ccEduclassStudent.getLong("evalute_id") != null) {
				scoreMap.put(ccEduclassStudent.getLong("evalute_id"), scoreItem);
				scoreMapList.add(ccEduclassStudent.getLong("evalute_id"));
			}
//			scoreMap.put(ccEduclassStudent.getLong("evalute_id"), scoreItem);
//			scoreMapList.add(ccEduclassStudent.getLong("evalute_id"));
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
	 * @return
	 */
	private boolean needToUpdateEvalute(Long eduClassId) {
		return CcStudentEvalute.dao.needToUpdate(eduClassId);
	}

}
