package com.gnet.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.gnet.model.admin.*;
import com.gnet.utils.PriceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.jfinal.log.Logger;

/**
 * @author SY
 * @date 2017年11月23日
 */
@Component("ccEdupointEachAimsAchieveService")
public class CcEdupointEachAimsAchieveService {

private static final Logger logger = Logger.getLogger(CcEdupointEachAimsAchieveService.class);
	
	/**
	 * 统计教学班下各个课程目标的达成度(考核分析法)
	 * 作用：把学生数据统计成为课程目标达成度
	 * @param eduClassId 教学班编号
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean statisticsEdupointEachAimsAchieve(Long eduClassId) {

		CcTeacherCourse courseByClassId = CcTeacherCourse.dao.findCourseByClassId(eduClassId);
		//达成度计算类型
		Integer resultType = courseByClassId.getInt("result_type");
		Date date = new Date();
		// bugfix252 先记录以前的数据
		List<CcEdupointEachAimsAchieve> ccEdupointEachAimsAchieveOldList = CcEdupointEachAimsAchieve.dao.findByColumn("educlass_id", eduClassId);
		// Map<"educlass_id,indication_id", CcEdupointEachAimsAchieve>
		Map<String, CcEdupointEachAimsAchieve> ccEdupointEachAimsAchieveOldMap = new HashMap<>();
		for(CcEdupointEachAimsAchieve temp : ccEdupointEachAimsAchieveOldList) {
			String key = temp.getLong("educlass_id")+","+temp.getLong("indication_id");
			ccEdupointEachAimsAchieveOldMap.put(key, temp);
		}
		// 如果存在以前的数据，则先删除。
		if(!CcEdupointEachAimsAchieve.dao.deleteAllByColumn("educlass_id", eduClassId, date)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			logger.info(new StringBuilder("达成度计算：教学班(编号为").append(eduClassId.toString()).append(")统计失败。因为删除老数据失败。").toString());
			return false;
		}
		List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndications = CcCourseGradecomposeIndication.dao.findByEduClassId(eduClassId);
		if (ccCourseGradecomposeIndications.isEmpty()) {
			logger.info(new StringBuilder("达成度计算：教学班(编号为")
					.append(eduClassId.toString()).append(")下的对应课程的成绩组成元素不存在")
					.toString());

			return true;
		}
		List<Long> ccCourseGradecomposeIndicationIdList = new ArrayList<>();
		Map<Long, CcCourseGradecomposeIndication> ccCourseGradecomposeIndicationMap = new HashMap<>();
		// 开课课程成绩组成元素与课程目标关联表  循环 获取id
		for(CcCourseGradecomposeIndication temp : ccCourseGradecomposeIndications) {
			Long ccCourseGradecomposeIndicationId = temp.getLong("id");
			ccCourseGradecomposeIndicationIdList.add(ccCourseGradecomposeIndicationId);

			ccCourseGradecomposeIndicationMap.put(ccCourseGradecomposeIndicationId, temp);
		}
		// 将数据保存到考核分析法报表表中
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		// 需要新增的课程目标达成度数据
		List<CcEdupointEachAimsAchieve> ccEdupointEachAimsAchieves = Lists.newArrayList();

		// Map<课程目标编号(indicationId)， Map<成绩组成编号(courseGradecomposeId), Map<平均分/总分/权重（averageScore/allScore/weight）, 数值>>>
		Map<Long, Map<Long, Map<String, BigDecimal>>> indicationIdCourseGradecomposeIdAddMap = new HashMap<>();
		List<CcEduindicationStuScore> ccEduindicationStuScores = CcEduindicationStuScore.dao.findByColumnIn("gradecompose_indication_id", ccCourseGradecomposeIndicationIdList.toArray(new Long[ccCourseGradecomposeIndicationIdList.size()]));
		// 由于查询出来学生成绩是所有教学班的，所以现在限制教学班
		List<CcEduindicationStuScore> ccEduindicationStuScoresTemp = new ArrayList<>();
		ccEduindicationStuScoresTemp.addAll(ccEduindicationStuScores);
		for(CcEduindicationStuScore temp : ccEduindicationStuScoresTemp) {
			Long educlasssId = temp.getLong("educlass_id");
			if(!educlasssId.equals(eduClassId)) {
				ccEduindicationStuScores.remove(temp);
			}
		}
		for(CcEduindicationStuScore temp : ccEduindicationStuScores) {
			// 教学班下课程目标成绩组成学生分数
			Long gradecomposeIndicationId = temp.getLong("gradecompose_indication_id");

			BigDecimal averageScore = PriceUtils.currency(temp.getBigDecimal("avg_score"));
			// 开课课程成绩组成元素与课程目标关联表
			CcCourseGradecomposeIndication ccCourseGradecomposeIndication = ccCourseGradecomposeIndicationMap.get(gradecomposeIndicationId);
			BigDecimal allScore = ccCourseGradecomposeIndication.getBigDecimal("max_score");
			Long indicationId = ccCourseGradecomposeIndication.getLong("indication_id");
			Long courseGradecomposeId = ccCourseGradecomposeIndication.getLong("course_gradecompose_id");
			BigDecimal weight = ccCourseGradecomposeIndication.getBigDecimal("weight");

			Map<Long, Map<String, BigDecimal>> courseGradecomposeIdAddMap = indicationIdCourseGradecomposeIdAddMap.get(indicationId);
			if(courseGradecomposeIdAddMap == null) {
				courseGradecomposeIdAddMap = new HashMap<>();
				indicationIdCourseGradecomposeIdAddMap.put(indicationId, courseGradecomposeIdAddMap);
			}
			// 因为在一个开课目标下，同一个成绩组成只出现一遍，所以，这里一定是新增
			Map<String, BigDecimal> valueMap = new HashMap<>();
			courseGradecomposeIdAddMap.put(courseGradecomposeId, valueMap);
			valueMap.put("averageScore", averageScore);
			valueMap.put("allScore", allScore);
			valueMap.put("weight", weight);
		}
		//财经大学的算法与浙江科技学院的不一样
		if (!resultType.equals(CcTeacherCourse.RESULT_TYPE_SCORE2)){
			/*
			 * 浙江科技学院算法：
			 * 名词：{
			 * 	a : 课程目标达成度
			 * 	CO ： 课程目标
			 * 	S : 成绩组成
			 * }
			 * 课程目标达成度a=A/B
			 * A = CO1.S1.学生们.平均分 * CO1S1.权重(开课课程成绩组成元素与课程目标关联表.权重) + CO1.S2.学生们.平均分 * CO1S2.权重 + ...
			 * 废弃1--- B = CO1.期望值 * （CO1S1.总分 + CO1S2.总分 + ...）----废弃
			 * 废弃2--- B = （CO1S1.总分 + CO1S2.总分 + ...） 现在要求存真的，不存期望值后的数据 --- 废弃
			 * Edit By SY 2019年11月5日02:58:31。 这个废弃2的B少乘了权重，下面计算里面有，但是这里没写，所以修改
			 * B = （CO1S1.总分 * CO1S1.权重 + CO1S2.总分 * CO1S2.权重 + ...） 现在要求存真的，不存期望值后的数据
			 * *
			 */

			// 通过map 和计算公式，得出数据，然后批量保存

			for(Entry<Long, Map<Long, Map<String, BigDecimal>>> entry : indicationIdCourseGradecomposeIdAddMap.entrySet()) {
				// 课程目标
				Long indicationId = entry.getKey();
				Map<Long, Map<String, BigDecimal>> courseGradecomposeIdAddMap = entry.getValue();
				BigDecimal A = new BigDecimal(0);
				BigDecimal B = new BigDecimal(0);
				for(Entry<Long, Map<String, BigDecimal>> secondEntry : courseGradecomposeIdAddMap.entrySet()) {
					Map<String, BigDecimal> valueMap = secondEntry.getValue();
					BigDecimal averageScore = PriceUtils.currency(valueMap.get("averageScore"));
					BigDecimal allScore = valueMap.get("allScore");
					BigDecimal weight = valueMap.get("weight");
					if(averageScore == null || allScore == null || weight == null) {
						continue;
					}
					A = A.add(averageScore.multiply(weight));
					B = B.add(allScore.multiply(weight));
				}
				if(B.equals(new BigDecimal(0))) {
					continue;
				}

				// 以前的值
				String key = eduClassId + "," + indicationId;
				CcEdupointEachAimsAchieve ccEdupointEachAimsAchieveOld = ccEdupointEachAimsAchieveOldMap.get(key);

				// 期望值
//				BigDecimal expectedValue = indicationMap.get(indicationId).getBigDecimal("expected_value");
				BigDecimal resultScore = A.compareTo(new BigDecimal(0)) == 0 ? A : A.divide(B, 5, RoundingMode.HALF_UP);
				CcEdupointEachAimsAchieve ccEdupointEachAimsAchieveTemp = new CcEdupointEachAimsAchieve();
				ccEdupointEachAimsAchieveTemp.put("id", idGenerate.getNextValue());
				ccEdupointEachAimsAchieveTemp.put("create_date", date);
				ccEdupointEachAimsAchieveTemp.put("modify_date", date);
				ccEdupointEachAimsAchieveTemp.put("educlass_id", eduClassId);
				ccEdupointEachAimsAchieveTemp.put("indication_id", indicationId);
				if(ccEdupointEachAimsAchieveOld != null) {
					// 老的数据放进去
					ccEdupointEachAimsAchieveTemp.set("except_achieve_value", ccEdupointEachAimsAchieveOld.getBigDecimal("except_achieve_value"));
				}
				// 新的数据更新
				ccEdupointEachAimsAchieveTemp.put("achieve_value", resultScore);
				ccEdupointEachAimsAchieves.add(ccEdupointEachAimsAchieveTemp);


			}

			// 保存数据
			if(!CcEdupointEachAimsAchieve.dao.batchSave(ccEdupointEachAimsAchieves)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}

		}else{
			/*
			 * 财经大学课程目标算法：
			 * 名词：{
			 * 	A : 课程目标达成度
			 * 	CO ： 课程目标
			 * 	S : 成绩组成
			 * }
			 * CO1课程目标达成度
			 * A=（平均分S1/S1总分）*（成绩CO1S1的权重/CO1权重的总和）+（平均分S2/S2总分）*（成绩CO1S2的权重/CO1权重的总和）+....
			  B=平均分S1/S1总分
			  C=成绩CO1S1的权重/CO1权重的总和
			 * * A=B*C
			 */
			// indicationIdCourseGradecomposeIdAddMap 值意义
			// Map<课程目标编号(indicationId)， Map<成绩组成编号(courseGradecomposeId), Map<平均分/总分/权重（averageScore/allScore/weight）, 数值>>>
			for(Entry<Long, Map<Long, Map<String, BigDecimal>>> entry : indicationIdCourseGradecomposeIdAddMap.entrySet()) {
				// 课程目标编号(indicationId)
				Long indicationId = entry.getKey();
				//Map<成绩组成编号(courseGradecomposeId)
				Map<Long, Map<String, BigDecimal>> courseGradecomposeIdAddMap = entry.getValue();
				BigDecimal A = new BigDecimal(0);
				BigDecimal B = new BigDecimal(0);
				BigDecimal C = new BigDecimal(0);
				//遍历的是成绩组成的平均分/总分/权重（averageScore/allScore/weight）
				for(Entry<Long, Map<String, BigDecimal>> secondEntry : courseGradecomposeIdAddMap.entrySet()) {
					Long courseGradecomposeId = secondEntry.getKey();
					CcCourseGradecompose courseGradecompose = CcCourseGradecompose.dao.findFilteredById(courseGradecomposeId);
					//所有成绩组成这个课程目标的总权重
					BigDecimal allWeight = CcCourseGradecomposeIndication.dao.calculateSumWeights(courseGradecompose.getLong("teacher_course_id"), indicationId);
					if(allWeight.equals(new BigDecimal(0))) {
						continue;
					}
					Map<String, BigDecimal> valueMap = secondEntry.getValue();
					BigDecimal averageScore = PriceUtils.currency(valueMap.get("averageScore"));
					BigDecimal allScore = valueMap.get("allScore");
					BigDecimal weight = valueMap.get("weight");
					if(averageScore == null || allScore == null || weight == null) {
						continue;
					}
					// A=（平均分S1/S1总分）*（成绩CO1S1的权重/CO1权重的总和）+（平均分S2/S2总分）*（成绩CO1S2的权重/CO1权重的总和）+....
					B = PriceUtils.div(averageScore,allScore,5);
					C = PriceUtils.div(weight,allWeight,5);
					A = A.add(B.multiply(C));

				}
				if(A.equals(new BigDecimal(0))) {
					continue;
				}

				// 以前的值
				String key = eduClassId + "," + indicationId;
				CcEdupointEachAimsAchieve ccEdupointEachAimsAchieveOld = ccEdupointEachAimsAchieveOldMap.get(key);

				// 期望值
//				BigDecimal expectedValue = indicationMap.get(indicationId).getBigDecimal("expected_value");
				BigDecimal resultScore = A;
				CcEdupointEachAimsAchieve ccEdupointEachAimsAchieveTemp = new CcEdupointEachAimsAchieve();
				ccEdupointEachAimsAchieveTemp.put("id", idGenerate.getNextValue());
				ccEdupointEachAimsAchieveTemp.put("create_date", date);
				ccEdupointEachAimsAchieveTemp.put("modify_date", date);
				ccEdupointEachAimsAchieveTemp.put("educlass_id", eduClassId);
				ccEdupointEachAimsAchieveTemp.put("indication_id", indicationId);
				if(ccEdupointEachAimsAchieveOld != null) {
					// 老的数据放进去
					ccEdupointEachAimsAchieveTemp.set("except_achieve_value", ccEdupointEachAimsAchieveOld.getBigDecimal("except_achieve_value"));
				}
				// 新的数据更新
				ccEdupointEachAimsAchieveTemp.put("achieve_value", resultScore);
				ccEdupointEachAimsAchieves.add(ccEdupointEachAimsAchieveTemp);
			}

			// 保存数据
			if(!CcEdupointEachAimsAchieve.dao.batchSave(ccEdupointEachAimsAchieves)) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}

		}


			
			logger.info(new StringBuilder("达成度计算：教学班(编号为").append(eduClassId.toString()).append(")统计完成").toString());
			
			return true;
			

	}
	
	/**
	 * 统计教学班下各个课程目标的达成度(考核分析法-剔除部分学生之后)
	 * 作用：把学生数据统计成为课程目标达成度
	 * @param eduClassId 教学班编号
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean statisticsEdupointEachAimsAchieveExcept(Long eduClassId) {

		CcTeacherCourse courseByClassId = CcTeacherCourse.dao.findCourseByClassId(eduClassId);
		//达成度计算类型
		Integer resultType = courseByClassId.getInt("result_type");
		Date date = new Date();
		// bugfix252 先记录以前的数据
		List<CcEdupointEachAimsAchieve> ccEdupointEachAimsAchieveOldList = CcEdupointEachAimsAchieve.dao.findByColumn("educlass_id", eduClassId);
		// Map<"educlass_id,indication_id", CcEdupointEachAimsAchieve>
		Map<String, CcEdupointEachAimsAchieve> ccEdupointEachAimsAchieveOldMap = new HashMap<>();
		for (CcEdupointEachAimsAchieve temp : ccEdupointEachAimsAchieveOldList) {
			String key = temp.getLong("educlass_id") + "," + temp.getLong("indication_id");
			ccEdupointEachAimsAchieveOldMap.put(key, temp);
		}
		// 如果存在以前的数据，则先删除。
		if (!CcEdupointEachAimsAchieve.dao.deleteAllByColumn("educlass_id", eduClassId, date)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			logger.info(new StringBuilder("达成度计算：教学班(编号为").append(eduClassId.toString()).append(")统计失败。因为删除老数据失败。").toString());
			return false;
		}

//		if (CcTeacherCourse.RESULT_TYPE_SCORE.equals(resultType)) {
		// 考核成绩分析法
//			List<CcScoreStuIndigrade> ccScoreStuIndigrades = CcScoreStuIndigrade.dao.findByClassId(eduClassId);
		List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndications = CcCourseGradecomposeIndication.dao.findByEduClassId(eduClassId);
		if (ccCourseGradecomposeIndications.isEmpty()) {
			logger.info(new StringBuilder("达成度计算：教学班(编号为")
					.append(eduClassId.toString()).append(")下的对应课程的成绩组成元素不存在")
					.toString());

			return true;
		}
		List<Long> ccCourseGradecomposeIndicationIdList = new ArrayList<>();
		Map<Long, CcCourseGradecomposeIndication> ccCourseGradecomposeIndicationMap = new HashMap<>();
		// 开课课程成绩组成元素与课程目标关联表  循环 获取id
		for (CcCourseGradecomposeIndication temp : ccCourseGradecomposeIndications) {
			Long ccCourseGradecomposeIndicationId = temp.getLong("id");
			ccCourseGradecomposeIndicationIdList.add(ccCourseGradecomposeIndicationId);

			ccCourseGradecomposeIndicationMap.put(ccCourseGradecomposeIndicationId, temp);
		}

		// 将数据保存到考核分析法报表表中
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		// 需要新增的课程目标达成度数据
		List<CcEdupointEachAimsAchieve> ccEdupointEachAimsAchieves = Lists.newArrayList();

		// Map<课程目标编号(indicationId)， Map<成绩组成编号(courseGradecomposeId), Map<平均分/总分/权重（averageScore/allScore/weight）, 数值>>>
		Map<Long, Map<Long, Map<String, BigDecimal>>> indicationIdCourseGradecomposeIdAddMap = new HashMap<>();
		List<CcEduindicationStuScore> ccEduindicationStuScores = CcEduindicationStuScore.dao.findByColumnIn("gradecompose_indication_id", ccCourseGradecomposeIndicationIdList.toArray(new Long[ccCourseGradecomposeIndicationIdList.size()]));
		// 由于查询出来学生还是成绩是所有教学班的，所以现在限制教学班
		List<CcEduindicationStuScore> ccEduindicationStuScoresTemp = new ArrayList<>();
		ccEduindicationStuScoresTemp.addAll(ccEduindicationStuScores);
		for (CcEduindicationStuScore temp : ccEduindicationStuScoresTemp) {
			Long educlasssId = temp.getLong("educlass_id");
			if (!educlasssId.equals(eduClassId)) {
				ccEduindicationStuScores.remove(temp);
			}
		}
		for (CcEduindicationStuScore temp : ccEduindicationStuScores) {
			// 教学班下课程目标成绩组成学生分数
			Long gradecomposeIndicationId = temp.getLong("gradecompose_indication_id");
//			BigDecimal averageScore = temp.getBigDecimal("avg_score");
			BigDecimal exceptAverageScore = PriceUtils.currency(temp.getBigDecimal("except_avg_score"));
			// 开课课程成绩组成元素与课程目标关联表
			CcCourseGradecomposeIndication ccCourseGradecomposeIndication = ccCourseGradecomposeIndicationMap.get(gradecomposeIndicationId);
			BigDecimal allScore = ccCourseGradecomposeIndication.getBigDecimal("max_score");
			Long indicationId = ccCourseGradecomposeIndication.getLong("indication_id");
			Long courseGradecomposeId = ccCourseGradecomposeIndication.getLong("course_gradecompose_id");
			BigDecimal weight = ccCourseGradecomposeIndication.getBigDecimal("weight");

			Map<Long, Map<String, BigDecimal>> courseGradecomposeIdAddMap = indicationIdCourseGradecomposeIdAddMap.get(indicationId);
			if (courseGradecomposeIdAddMap == null) {
				courseGradecomposeIdAddMap = new HashMap<>();
				indicationIdCourseGradecomposeIdAddMap.put(indicationId, courseGradecomposeIdAddMap);
			}
			// 因为在一个开课目标下，同一个成绩组成只出现一遍，所以，这里一定是新增
			Map<String, BigDecimal> valueMap = new HashMap<>();
			courseGradecomposeIdAddMap.put(courseGradecomposeId, valueMap);
			valueMap.put("exceptAverageScore", exceptAverageScore);
			valueMap.put("allScore", allScore);
			valueMap.put("weight", weight);
		}
		if (!resultType.equals(CcTeacherCourse.RESULT_TYPE_SCORE2)) {
			/*
			 * 浙江科技学院算法：
			 * 名词：{
			 * 	a : 课程目标达成度
			 * 	CO ： 课程目标
			 * 	S : 成绩组成
			 * }
			 * 课程目标达成度a=A/B
			 * A = CO1.S1.学生们.平均分 * CO1S1.权重(开课课程成绩组成元素与课程目标关联表.权重) + CO1.S2.学生们.平均分 * CO1S2.权重 + ...
			 * --- B = CO1.期望值 * （CO1S1.总分 + CO1S2.总分 + ...）----废弃
			 * 废弃2--- B = （CO1S1.总分 + CO1S2.总分 + ...） 现在要求存真的，不存期望值后的数据 --- 废弃
			 * Edit By SY 2019年11月5日02:58:31。 这个废弃2的B少乘了权重，下面计算里面有，但是这里没写，所以修改
			 * B = （CO1S1.总分 * CO1S1.权重 + CO1S2.总分 * CO1S2.权重 + ...） 现在要求存真的，不存期望值后的数据
			 */
			// 通过map 和计算公式，得出数据，然后批量保存
			for (Entry<Long, Map<Long, Map<String, BigDecimal>>> entry : indicationIdCourseGradecomposeIdAddMap.entrySet()) {
				// 课程目标
				Long indicationId = entry.getKey();
				Map<Long, Map<String, BigDecimal>> courseGradecomposeIdAddMap = entry.getValue();
				BigDecimal A = new BigDecimal(0);
				BigDecimal B = new BigDecimal(0);
				for (Entry<Long, Map<String, BigDecimal>> secondEntry : courseGradecomposeIdAddMap.entrySet()) {
					Map<String, BigDecimal> valueMap = secondEntry.getValue();
					BigDecimal exceptAverageScore = PriceUtils.currency(valueMap.get("exceptAverageScore"));
					BigDecimal allScore = valueMap.get("allScore");
					BigDecimal weight = valueMap.get("weight");
					if (exceptAverageScore == null || allScore == null || weight == null) {
						continue;
					}
					A = A.add(exceptAverageScore.multiply(weight));
					B = B.add(allScore.multiply(weight));
				}
				if (B.equals(new BigDecimal(0))) {
					continue;
				}

				// 以前的值
				String key = eduClassId + "," + indicationId;
				CcEdupointEachAimsAchieve ccEdupointEachAimsAchieveOld = ccEdupointEachAimsAchieveOldMap.get(key);

				// 期望值
//				BigDecimal expectedValue = indicationMap.get(indicationId).getBigDecimal("expected_value");
				BigDecimal resultScore = A.compareTo(new BigDecimal(0)) == 0 ? A : A.divide(B, 5, RoundingMode.HALF_UP);
				CcEdupointEachAimsAchieve ccEdupointEachAimsAchieveTemp = new CcEdupointEachAimsAchieve();
				ccEdupointEachAimsAchieveTemp.put("id", idGenerate.getNextValue());
				ccEdupointEachAimsAchieveTemp.put("create_date", date);
				ccEdupointEachAimsAchieveTemp.put("modify_date", date);
				ccEdupointEachAimsAchieveTemp.put("educlass_id", eduClassId);
				ccEdupointEachAimsAchieveTemp.put("indication_id", indicationId);
				if (ccEdupointEachAimsAchieveOld != null) {
					// 老的数据放进去
					ccEdupointEachAimsAchieveTemp.set("achieve_value", ccEdupointEachAimsAchieveOld.getBigDecimal("achieve_value"));
					ccEdupointEachAimsAchieveTemp.set("except_achieve_value", ccEdupointEachAimsAchieveOld.getBigDecimal("except_achieve_value"));
				}
				// 新的数据更新
				ccEdupointEachAimsAchieveTemp.put("except_achieve_value", resultScore);
				ccEdupointEachAimsAchieves.add(ccEdupointEachAimsAchieveTemp);
			}
		}else{
			/*
			 * 财经大学课程目标算法：
			 * 名词：{
			 * 	A : 课程目标达成度
			 * 	CO ： 课程目标
			 * 	S : 成绩组成
			 * }
			 * CO1课程目标达成度
			 * A=（平均分S1/S1总分）*（成绩CO1S1的权重/CO1权重的总和）+（平均分S2/S2总分）*（成绩CO1S2的权重/CO1权重的总和）+....
			  B=平均分S1/S1总分
			  C=成绩CO1S1的权重/CO1权重的总和
			 * * A=B*C
			 */
			// indicationIdCourseGradecomposeIdAddMap 值意义
			// Map<课程目标编号(indicationId)， Map<成绩组成编号(courseGradecomposeId), Map<平均分/总分/权重（averageScore/allScore/weight）, 数值>>>
			for(Entry<Long, Map<Long, Map<String, BigDecimal>>> entry : indicationIdCourseGradecomposeIdAddMap.entrySet()) {
				// 课程目标编号(indicationId)
				Long indicationId = entry.getKey();
				//Map<成绩组成编号(courseGradecomposeId)
				Map<Long, Map<String, BigDecimal>> courseGradecomposeIdAddMap = entry.getValue();
				BigDecimal A = new BigDecimal(0);
				BigDecimal B = new BigDecimal(0);
				BigDecimal C = new BigDecimal(0);
				//遍历的是成绩组成的平均分/总分/权重（averageScore/allScore/weight）
				for(Entry<Long, Map<String, BigDecimal>> secondEntry : courseGradecomposeIdAddMap.entrySet()) {
					Long courseGradecomposeId = secondEntry.getKey();
					CcCourseGradecompose courseGradecompose = CcCourseGradecompose.dao.findFilteredById(courseGradecomposeId);
					//所有成绩组成这个课程目标的总权重
					BigDecimal allWeight = CcCourseGradecomposeIndication.dao.calculateSumWeights(courseGradecompose.getLong("teacher_course_id"), indicationId);
					if(allWeight.equals(new BigDecimal(0))) {
						continue;
					}
					Map<String, BigDecimal> valueMap = secondEntry.getValue();
					BigDecimal averageScore = PriceUtils.currency(valueMap.get("exceptAverageScore"));
					BigDecimal allScore = valueMap.get("allScore");
					BigDecimal weight = valueMap.get("weight");
					if(averageScore == null || allScore == null || weight == null) {
						continue;
					}
					// A=（平均分S1/S1总分）*（成绩CO1S1的权重/CO1权重的总和）+（平均分S2/S2总分）*（成绩CO1S2的权重/CO1权重的总和）+....
					B = PriceUtils.div(averageScore,allScore,5);
					C = PriceUtils.div(weight,allWeight,5);
					A = A.add(B.multiply(C));

				}
				if(A.equals(new BigDecimal(0))) {
					continue;
				}

				// 以前的值
				String key = eduClassId + "," + indicationId;
				CcEdupointEachAimsAchieve ccEdupointEachAimsAchieveOld = ccEdupointEachAimsAchieveOldMap.get(key);

				// 期望值
//				BigDecimal expectedValue = indicationMap.get(indicationId).getBigDecimal("expected_value");
				BigDecimal resultScore = A;
				CcEdupointEachAimsAchieve ccEdupointEachAimsAchieveTemp = new CcEdupointEachAimsAchieve();
				ccEdupointEachAimsAchieveTemp.put("id", idGenerate.getNextValue());
				ccEdupointEachAimsAchieveTemp.put("create_date", date);
				ccEdupointEachAimsAchieveTemp.put("modify_date", date);
				ccEdupointEachAimsAchieveTemp.put("educlass_id", eduClassId);
				ccEdupointEachAimsAchieveTemp.put("indication_id", indicationId);
				if (ccEdupointEachAimsAchieveOld != null) {
					// 老的数据放进去
					ccEdupointEachAimsAchieveTemp.set("achieve_value", ccEdupointEachAimsAchieveOld.getBigDecimal("achieve_value"));
					ccEdupointEachAimsAchieveTemp.set("except_achieve_value", ccEdupointEachAimsAchieveOld.getBigDecimal("except_achieve_value"));
				}
				// 新的数据更新
				ccEdupointEachAimsAchieveTemp.put("except_achieve_value", resultScore);
				ccEdupointEachAimsAchieves.add(ccEdupointEachAimsAchieveTemp);
			}
		}
		// 保存数据
		if (!CcEdupointEachAimsAchieve.dao.batchSave(ccEdupointEachAimsAchieves)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		logger.info(new StringBuilder("达成度计算：教学班(编号为").append(eduClassId.toString()).append(")统计完成").toString());
		
		return true;
		
//		} else if (CcTeacherCourse.RESULT_TYPE_EVALUATE.equals(resultType)) {
//			logger.info(new StringBuilder("达成度计算：教学班(编号为").append(eduClassId.toString()).append(")统计不予执行，以为评分表分析法计算，建工学院版本不适配。").toString());
//			return false;
//			
//		} else {
//			logger.info(new StringBuilder("达成度计算：教学班(编号为").append(eduClassId.toString()).append(")达成度统计类型")
//					.append(resultType == null ? "null" : resultType).append("无法解析")
//					.toString());
//			
//			return false;
//		}
	}
}
