package com.gnet.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.SimpleFormatter;

import com.gnet.model.admin.*;
import com.gnet.utils.DateUtil;
import org.apache.bcel.generic.IF_ACMPEQ;
import org.apache.commons.lang.text.StrBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.PriceUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Record;

/**
 * 达成度报表统计服务
 * 用于统计计算教学班、课程、专业、个人的达成度, 管理各个报表生成的状态
 *
 * @author wct
 * @date 2016年7月21日
 */

@Component("ccResultStatisticsService")
public class CcResultStatisticsService {

	private static final Logger logger = Logger.getLogger(CcResultStatisticsService.class);

	/**
	 * 统计教学班数据-考核分析法-建工
	 *
	 * @param eduClassId 教学班编号
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean statisticsEduclassResult(Long eduClassId) {
		// 调用，更新整个教学班下的数据接口
		List<Long> eduClassIds = new ArrayList<>();
		eduClassIds.add(eduClassId);
		CcEdupointEachAimsAchieveService ccEdupointEachAimsAchieveService = SpringContextHolder.getBean(CcEdupointEachAimsAchieveService.class);
		return ccEdupointEachAimsAchieveService.statisticsEdupointEachAimsAchieve(eduClassId);
//		CcEduindicationStuScoreService ccEduindicationStuScoreService = SpringContextHolder.getBean(CcEduindicationStuScoreService.class);
//		ccEduindicationStuScoreService.calculate(eduClassIds, null);
//		Boolean result = Boolean.TRUE;
//		return result;
	}
	/**
	 * 统计教学班数据(考核分析法和考评点分析法)
	 *
	 * @param eduClassId 教学班编号
	 * @param resultType 达成度计算类型
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean statisticsEduclassResult(Long eduClassId, Integer resultType) {
		//查找历史数据已经删除的也找出来
		List<CcReportEduclassGrade> historyReportEduclassGrades = CcReportEduclassGrade.dao.findByColumn("educlass_id", eduClassId);
		Map<Long, CcReportEduclassGrade> historyReportEduclassGradesMap = Maps.newHashMap();
		for(CcReportEduclassGrade temp : historyReportEduclassGrades){
			historyReportEduclassGradesMap.put(temp.getLong("gradecompose_indication_id"), temp);
		}
		// BUG 252 因为要求增加剔除学生功能，所以计数方式修改 SY 2018年1月30日
//		Long studentSize = CcEduclassStudent.dao.countFiltered("class_id", eduClassId);
		Map<String, Object> params = new HashMap<>();
		params.put("class_id", eduClassId);
		params.put("is_del", Boolean.FALSE);
		params.put("is_caculate", Boolean.TRUE);
		Long studentSize = CcEduclassStudent.dao.count(params);
		// 班级没有人存在时返回错误信息
		if (studentSize == 0L) {
			logger.info(new StringBuilder("达成度计算：教学班(编号为")
					.append(eduClassId.toString()).append(")下的学生数为0")
					.toString());

			return true;
		}

		if (CcTeacherCourse.RESULT_TYPE_SCORE.equals(resultType)) {
			// 考核成绩分析法
			List<CcScoreStuIndigrade> ccScoreStuIndigrades = CcScoreStuIndigrade.dao.findByClassIdAndIsCaculate(eduClassId, Boolean.TRUE);
			List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndications = CcCourseGradecomposeIndication.dao.findByEduClassId(eduClassId);
			if (ccCourseGradecomposeIndications.isEmpty()) {
				logger.info(new StringBuilder("达成度计算：教学班(编号为")
						.append(eduClassId.toString()).append(")下的对应课程的成绩组成元素不存在")
						.toString());

				return true;
			}

			// 学生成绩累加
			Map<Long, BigDecimal> idToAllScoreMap = Maps.newHashMap();
			for (CcScoreStuIndigrade ccScoreStuIndigrade : ccScoreStuIndigrades) {
				Long gradeComposeIndicationId = ccScoreStuIndigrade.getLong("gradecompose_indication_id");
				BigDecimal grade = ccScoreStuIndigrade.getBigDecimal("grade") == null ? new BigDecimal(0) : ccScoreStuIndigrade.getBigDecimal("grade") ;
				if (idToAllScoreMap.get(gradeComposeIndicationId) == null) {
					idToAllScoreMap.put(gradeComposeIndicationId, grade);
				} else {
					idToAllScoreMap.put(gradeComposeIndicationId, PriceUtils._add(idToAllScoreMap.get(gradeComposeIndicationId), grade));
				}
			}

			// 将数据保存到考核分析法报表表中
			Date date = new Date();
			IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
			List<CcReportEduclassGrade> ccReportEduclazzGrades = Lists.newArrayList();
			List<CcReportEduclassGrade> updateCcReportEduclassGrades = Lists.newArrayList();
			//开课课程成绩组成元素与指标点关联编号相同的数据
			List<CcReportEduclassGrade> sameCcReportEduclassGrades = Lists.newArrayList();
			for (CcCourseGradecomposeIndication ccCourseGradecomposeIndication : ccCourseGradecomposeIndications) {
				Long gradecomposeIndicationId = ccCourseGradecomposeIndication.getLong("id");
				BigDecimal allScore = idToAllScoreMap.get(ccCourseGradecomposeIndication.getLong("id"));
				if (allScore == null) {
					continue;
				}
				allScore = allScore.divide(BigDecimal.valueOf(studentSize), 3, RoundingMode.HALF_UP);
				CcReportEduclassGrade historyCcReportEduclassGrade = historyReportEduclassGradesMap.get(gradecomposeIndicationId);
				if(historyCcReportEduclassGrade == null){
					CcReportEduclassGrade ccReportEduclazzGrade = new CcReportEduclassGrade();
					ccReportEduclazzGrade.set("educlass_id", eduClassId);
					ccReportEduclazzGrade.set("gradecompose_indication_id", gradecomposeIndicationId);
					ccReportEduclazzGrade.set("weight", ccCourseGradecomposeIndication.getBigDecimal("weight"));
					ccReportEduclazzGrade.set("max_score", ccCourseGradecomposeIndication.getBigDecimal("max_score"));
					ccReportEduclazzGrade.set("size", Integer.parseInt(studentSize.toString()));
					ccReportEduclazzGrade.set("statistics_date", date);
					ccReportEduclazzGrade.set("create_date", date);
					ccReportEduclazzGrade.set("modify_date", date);
					ccReportEduclazzGrade.set("is_del", CcReportEduclassGrade.DEL_NO);
					ccReportEduclazzGrade.set("id", idGenerate.getNextValue());
					// 计算平均分
					ccReportEduclazzGrade.set("result", allScore);
					ccReportEduclazzGrades.add(ccReportEduclazzGrade);
				}else{
					//原本是计算结果不一样的才更新，但是会导致一个问题：基础数据（比如成绩组成指标点等）有更新但是统计的达成度结果没有变化，会一直提示有更新
					historyCcReportEduclassGrade.set("result", allScore);
					historyCcReportEduclassGrade.set("weight", ccCourseGradecomposeIndication.getBigDecimal("weight"));
					historyCcReportEduclassGrade.set("max_score", ccCourseGradecomposeIndication.getBigDecimal("max_score"));
					historyCcReportEduclassGrade.set("size", Integer.parseInt(studentSize.toString()));
					historyCcReportEduclassGrade.set("modify_date", date);
					historyCcReportEduclassGrade.set("statistics_date", date);
					historyCcReportEduclassGrade.set("is_del", CcReportEduclassGrade.DEL_NO);
					updateCcReportEduclassGrades.add(historyCcReportEduclassGrade);
					sameCcReportEduclassGrades.add(historyCcReportEduclassGrade);
				}

			}

			// 保存新的报表计算数据
			if (!ccReportEduclazzGrades.isEmpty() && !CcReportEduclassGrade.dao.batchSave(ccReportEduclazzGrades)) {
				logger.error(new StringBuilder("达成度计算：教学班(编号为")
						.append(eduClassId.toString())
						.append(")新增数据失败，更新数据包括(").append(ccReportEduclazzGrades.toString()).append(")")
						.toString());

				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}

			//更新新的报表数据
			if(!updateCcReportEduclassGrades.isEmpty() && !CcReportEduclassGrade.dao.batchUpdate(updateCcReportEduclassGrades, "result, modify_date, statistics_date, max_score, weight, size, is_del")){
				logger.error(new StringBuilder("达成度计算：教学班(编号为")
						.append(eduClassId.toString())
						.append(")更新数据失败，新增数据包括(").append(updateCcReportEduclassGrades.toString()).append(")")
						.toString());

				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}

			//需要删除的数据
			List<Long> reportEduclassGradeIds = Lists.newArrayList();
			if(!historyReportEduclassGrades.isEmpty()){
				if(!sameCcReportEduclassGrades.isEmpty()){
					historyReportEduclassGrades.removeAll(sameCcReportEduclassGrades);
				}
				if(!historyReportEduclassGrades.isEmpty()){
					for(CcReportEduclassGrade temp : historyReportEduclassGrades){
						reportEduclassGradeIds.add(temp.getLong("id"));
					}
					if(!CcReportEduclassGrade.dao.deleteAll(reportEduclassGradeIds.toArray(new Long[reportEduclassGradeIds.size()]), date)){
						logger.error(new StringBuilder("达成度计算：教学班(编号为")
								.append(eduClassId.toString())
								.append(")删除数据失败，删除数据包括(").append(historyReportEduclassGrades.toString()).append(")")
								.toString());

						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return false;
					}
				}
			}

			logger.info(new StringBuilder("达成度计算：教学班(编号为").append(eduClassId.toString()).append(")统计完成").toString());

			return true;

		} else if (CcTeacherCourse.RESULT_TYPE_EVALUATE.equals(resultType)) {
//			//历史数据
//			List<CcReportEduclassEvalute> historyCcReportEduclassEvalutes = CcReportEduclassEvalute.dao.findByColumn("educlass_id", eduClassId);
//			Map<Long, CcReportEduclassEvalute> historyCcReportEduclassEvaluteMap = Maps.newHashMap();
//			for(CcReportEduclassEvalute temp : historyCcReportEduclassEvalutes){
//				historyCcReportEduclassEvaluteMap.put(temp.getLong("evalute_id"), temp);
//			}
//			// 评分表分析法
//			List<CcStudentEvalute> ccStudentEvalutes = CcStudentEvalute.dao.findAllByClassIdAndIsCaculate(eduClassId, Boolean.TRUE);
//			List<CcEvalute> ccEvalutes = CcEvalute.dao.findAllByEduClass(eduClassId);
//			if (ccEvalutes.isEmpty()) {
//				logger.info(new StringBuilder("达成度计算：教学班(编号为")
//						.append(eduClassId.toString()).append(")下的对应课程的考评点不存在")
//						.toString());
//
//				return true;
//			}
//
//			// 学生成绩累加
//			Map<Long, BigDecimal> idToStuEvaluteMap = Maps.newHashMap();
//			for (CcStudentEvalute ccStudentEvalute : ccStudentEvalutes) {
//				Long evaluteId = ccStudentEvalute.getLong("evalute_id");
//				BigDecimal score = ccStudentEvalute.getBigDecimal("score") == null ? new BigDecimal(0) : ccStudentEvalute.getBigDecimal("score") ;
//				if (idToStuEvaluteMap.get(evaluteId) == null) {
//					idToStuEvaluteMap.put(evaluteId, score);
//				} else {
//					idToStuEvaluteMap.put(evaluteId, PriceUtils._add(idToStuEvaluteMap.get(evaluteId), score));
//				}
//			}
//			// TODO SY 更新数据会走这里。
//			// 将数据保存到考评统计表表中
//			Date date = new Date();
//			IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
//			List<CcReportEduclassEvalute>  ccReportEduclassEvalutes = Lists.newArrayList();
//			List<CcReportEduclassEvalute> updateCcReportEduclassEvalutes = Lists.newArrayList();
//			List<CcReportEduclassEvalute> sameCcReportEduclassEvalutes = Lists.newArrayList();
//
//			for (CcEvalute ccEvalute : ccEvalutes) {
//				Long evaluteId = ccEvalute.getLong("id");
//				BigDecimal allScore = idToStuEvaluteMap.get(ccEvalute.getLong("id"));
//				if (allScore == null) {
//					continue;
//				}
//
//				allScore = allScore.divide(BigDecimal.valueOf(studentSize), 3, RoundingMode.HALF_UP);
//				CcReportEduclassEvalute historyCcReportEduclassEvalute = historyCcReportEduclassEvaluteMap.get(evaluteId);
//				if(historyCcReportEduclassEvalute == null){
//					CcReportEduclassEvalute ccReportEduclassEvalute = new CcReportEduclassEvalute();
//					ccReportEduclassEvalute.set("educlass_id", eduClassId);
//					ccReportEduclassEvalute.set("evalute_id", evaluteId);
//					ccReportEduclassEvalute.set("weight", ccEvalute.getBigDecimal("weight"));
//					ccReportEduclassEvalute.set("size", Integer.parseInt(studentSize.toString()));
//					ccReportEduclassEvalute.set("statistics_date", date);
//					ccReportEduclassEvalute.set("create_date", date);
//					ccReportEduclassEvalute.set("modify_date", date);
//					ccReportEduclassEvalute.set("is_del", CcReportEduclassGrade.DEL_NO);
//					ccReportEduclassEvalute.set("id", idGenerate.getNextValue());
//					// 计算平均分
//					ccReportEduclassEvalute.set("result", allScore);
//					ccReportEduclassEvalutes.add(ccReportEduclassEvalute);
//				}else{
//					//原本是计算结果不一样的才更新，但是会导致一个问题：基础数据（比如考评点等）有更新但是统计的达成度结果没有变化，会一直提示有更新
//					historyCcReportEduclassEvalute.set("modify_date", date);
//					historyCcReportEduclassEvalute.set("weight", ccEvalute.getBigDecimal("weight"));
//					historyCcReportEduclassEvalute.set("size", Integer.parseInt(studentSize.toString()));
//					historyCcReportEduclassEvalute.set("result", allScore);
//					historyCcReportEduclassEvalute.set("statistics_date", date);
//					historyCcReportEduclassEvalute.set("is_del", CcReportEduclassGrade.DEL_NO);
//					updateCcReportEduclassEvalutes.add(historyCcReportEduclassEvalute);
//					sameCcReportEduclassEvalutes.add(historyCcReportEduclassEvalute);
//				}
//
//			}
//
//
//			// 保存新的报表计算数据
//			if (!ccReportEduclassEvalutes.isEmpty() && !CcReportEduclassEvalute.dao.batchSave(ccReportEduclassEvalutes)) {
//				logger.error(new StringBuilder("达成度计算：教学班(编号为")
//						.append(eduClassId.toString())
//						.append(")新增数据失败，新增数据包括(").append(ccReportEduclassEvalutes.toString()).append(")")
//						.toString());
//
//				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//				return false;
//			}
//
//
//			if(!updateCcReportEduclassEvalutes.isEmpty() && !CcReportEduclassEvalute.dao.batchUpdate(updateCcReportEduclassEvalutes, "result, modify_date, statistics_date, weight, size, is_del")){
//				logger.error(new StringBuilder("达成度计算：教学班(编号为")
//						.append(eduClassId.toString())
//						.append(")更新数据失败，更新数据包括(").append(updateCcReportEduclassEvalutes.toString()).append(")")
//						.toString());
//
//				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//				return false;
//			}
//
//			//需要删除的数据
//			List<Long> reportEduclassEvaluteIds = Lists.newArrayList();
//			if(!historyCcReportEduclassEvalutes.isEmpty()){
//				if(!historyCcReportEduclassEvalutes.isEmpty()){
//					historyCcReportEduclassEvalutes.removeAll(sameCcReportEduclassEvalutes);
//				}
//				if(!historyCcReportEduclassEvalutes.isEmpty()){
//					for(CcReportEduclassEvalute temp : historyCcReportEduclassEvalutes){
//						reportEduclassEvaluteIds.add(temp.getLong("id"));
//					}
//					if(!CcReportEduclassEvalute.dao.deleteAll(reportEduclassEvaluteIds.toArray(new Long[reportEduclassEvaluteIds.size()]), date)){
//						logger.error(new StringBuilder("达成度计算：教学班(编号为")
//								.append(eduClassId.toString())
//								.append(")删除数据失败，删除数据包括(").append(historyCcReportEduclassEvalutes.toString()).append(")")
//								.toString());
//
//						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//						return false;
//					}
//				}
//			}
//
//			logger.info(new StringBuilder("达成度计算：教学班(编号为").append(eduClassId.toString()).append(")统计完成").toString());
//
//			return true;
			logger.info(new StringBuilder("达成度计算：教学班(编号为").append(eduClassId.toString()).append(")统计不予执行，以为评分表分析法计算，建工学院版本不适配。").toString());
			return false;

		} else {
			logger.info(new StringBuilder("达成度计算：教学班(编号为").append(eduClassId.toString()).append(")达成度统计类型")
					.append(resultType == null ? "null" : resultType).append("无法解析")
					.toString());

			return false;
		}
	}


	/**
	 * 判断并更新教学班数据(考核分析法)
	 *
	 * @param grade
	 * @param versionId
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean updateEduclassStatisticsScore(Integer grade, Long versionId) {
		List<CcScoreStuIndigrade> needUpdateccScoreStuIndigrades = CcScoreStuIndigrade.dao.findNeedToUpdate(versionId, grade);
		// 无更新不进行数据重新统计
		if (needUpdateccScoreStuIndigrades.isEmpty()) {
			logger.info(new StringBuilder("达成度统计：专业认证版本编号为").append(versionId.toString()).append(",")
					.append(grade.toString()).append("年级下的所有使用考核分析法的教学班学生成绩都为最新，无需进行更新")
					.toString());

			return true;
		}

		Long[] eduClassIds = new Long[needUpdateccScoreStuIndigrades.size()];
		for (int i = 0; i < needUpdateccScoreStuIndigrades.size(); i ++) {
			eduClassIds[i] = needUpdateccScoreStuIndigrades.get(i).getLong("educlass_id");
		}

		List<CcScoreStuIndigrade> ccScoreStuIndigrades = CcScoreStuIndigrade.dao.findByEduclassIds(eduClassIds);
		// 教学班学生数信息
		List<CcEduclassStudent> educlassStudentSizes = CcEduclassStudent.dao.findByEduClassIds(eduClassIds);
		Map<Long, Long> eduClassStudentSizeMap = Maps.newHashMap();
		for (CcEduclassStudent ccEduclassStudent : educlassStudentSizes) {
			eduClassStudentSizeMap.put(ccEduclassStudent.getLong("class_id"), ccEduclassStudent.getLong("student_size"));
		}

		// 结果计算并保存
		Date date = new Date();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		List<CcReportEduclassGrade> newCcReportEduclassGrades = Lists.newArrayList();
		// 每个班级学生个指标点下成绩累加
		Map<Long, Map<Long, BigDecimal>> idToAllScoreMap = Maps.newHashMap();
		for (CcScoreStuIndigrade ccScoreStuIndigrade : ccScoreStuIndigrades) {
			Long eduClassId = ccScoreStuIndigrade.getLong("educlass_id");
			Map<Long, BigDecimal> scoreMap = null;
			if (idToAllScoreMap.get(eduClassId) == null) {
				scoreMap = Maps.newHashMap();
				idToAllScoreMap.put(eduClassId, scoreMap);
			} else {
				scoreMap = idToAllScoreMap.get(eduClassId);
			}

			Long gradecomposeIndicationId = ccScoreStuIndigrade.getLong("gradecompose_indication_id");
			BigDecimal score = ccScoreStuIndigrade.getBigDecimal("grade") == null ? new BigDecimal(0) : ccScoreStuIndigrade.getBigDecimal("grade");
			if (scoreMap.get(gradecomposeIndicationId) == null) {
				scoreMap.put(gradecomposeIndicationId, score);
				// 每个成绩项的基本数据创建
				CcReportEduclassGrade ccReportEduclassGrade = new CcReportEduclassGrade();
				ccReportEduclassGrade.set("gradecompose_indication_id", gradecomposeIndicationId);
				ccReportEduclassGrade.set("educlass_id", ccScoreStuIndigrade.getLong("educlass_id"));
				ccReportEduclassGrade.set("weight", ccScoreStuIndigrade.getBigDecimal("weight"));
				ccReportEduclassGrade.set("max_score", ccScoreStuIndigrade.getBigDecimal("max_score"));
				ccReportEduclassGrade.set("statistics_date", date);
				ccReportEduclassGrade.set("create_date", date);
				ccReportEduclassGrade.set("modify_date", date);
				ccReportEduclassGrade.set("is_del", CcReportEduclassGrade.DEL_NO);
				ccReportEduclassGrade.set("id", idGenerate.getNextValue());
				if (eduClassStudentSizeMap.get(ccScoreStuIndigrade.getLong("educlass_id")) != null) {
					ccReportEduclassGrade.set("size", Integer.valueOf(eduClassStudentSizeMap.get(ccScoreStuIndigrade.getLong("educlass_id")).toString()));
				}

				newCcReportEduclassGrades.add(ccReportEduclassGrade);
			} else {
				scoreMap.put(gradecomposeIndicationId, PriceUtils._add(scoreMap.get(gradecomposeIndicationId), score));
			}

		}

		// 计算结果加入报表数据中
		Map<String, CcReportEduclassGrade> newCcReportEduclassGradeMap = Maps.newHashMap();
		for (CcReportEduclassGrade ccReportEduclassGrade : newCcReportEduclassGrades) {
			Long eduClassId = ccReportEduclassGrade.getLong("educlass_id");
			Long gradecomposeIndicationId = ccReportEduclassGrade.getLong("gradecompose_indication_id");
			String key = new StringBuilder(eduClassId.toString()).append(",").append(gradecomposeIndicationId.toString()).toString();
			BigDecimal averageScore = null;
			if (eduClassStudentSizeMap.get(eduClassId) == null || eduClassStudentSizeMap.get(eduClassId) == 0) {
				averageScore = new BigDecimal(0);
			} else {
				averageScore = idToAllScoreMap.get(eduClassId).get(ccReportEduclassGrade.getLong("gradecompose_indication_id")).divide(BigDecimal.valueOf(eduClassStudentSizeMap.get(eduClassId)), 3, RoundingMode.HALF_UP);
			}

			ccReportEduclassGrade.put("result", averageScore);
			if(newCcReportEduclassGradeMap.get(key) == null){
				newCcReportEduclassGradeMap.put(key, ccReportEduclassGrade);
			}
		}

		//得到教学班考核分析法历史数据
		List<CcReportEduclassGrade> historyCcReportEduclassGrades = CcReportEduclassGrade.dao.findByColumnIn("educlass_id", eduClassIds);
		Map<String, CcReportEduclassGrade> historyCcReportEduclassGradeMap = Maps.newHashMap();
		for(CcReportEduclassGrade ccReportEduclassGrade : historyCcReportEduclassGrades){
			String key = new StrBuilder(ccReportEduclassGrade.getLong("educlass_id").toString())
					.append(",").append(ccReportEduclassGrade.getLong("gradecompose_indication_id").toString())
					.toString();
			historyCcReportEduclassGradeMap.put(key, ccReportEduclassGrade);
		}

		List<CcReportEduclassGrade> addCcReportEduclassGrades = Lists.newArrayList();
		List<CcReportEduclassGrade> updateCcReportEduclassGrades = Lists.newArrayList();
		for(Map.Entry<String, CcReportEduclassGrade> entry : newCcReportEduclassGradeMap.entrySet()){
			BigDecimal result = entry.getValue().getBigDecimal("result");
			CcReportEduclassGrade historyCcReportEduclassGrade = historyCcReportEduclassGradeMap.get(entry.getKey());
			if(historyCcReportEduclassGrade == null){
				addCcReportEduclassGrades.add(entry.getValue());
			}else{
				historyCcReportEduclassGrade.set("result", result);
				historyCcReportEduclassGrade.set("modify_date", date);
				historyCcReportEduclassGrade.set("statistics_date", date);
				historyCcReportEduclassGrade.set("weight", entry.getValue().getBigDecimal("weight"));
				historyCcReportEduclassGrade.set("max_score", entry.getValue().getBigDecimal("max_score"));
				historyCcReportEduclassGrade.set("is_del", CcReportEduclassGrade.DEL_NO);
				updateCcReportEduclassGrades.add(historyCcReportEduclassGrade);
			}
		}

		//需要删除的教学班考核分析法
		Long[] ccReportEduclassGradeIds = {};
		if(!historyCcReportEduclassGrades.isEmpty()){
			if(!updateCcReportEduclassGrades.isEmpty()){
				historyCcReportEduclassGrades.removeAll(updateCcReportEduclassGrades);
			}
			if(!historyCcReportEduclassGrades.isEmpty()){
				ccReportEduclassGradeIds = new Long[historyCcReportEduclassGrades.size()];
				for(int i = 0; i<historyCcReportEduclassGrades.size(); i++){
					ccReportEduclassGradeIds[i] = historyCcReportEduclassGrades.get(i).getLong("id");
				}
				if(!CcReportEduclassGrade.dao.deleteAll(ccReportEduclassGradeIds, date)){
					logger.error(new StringBuilder("达成度统计：教学班考核分析法达成度统计表(").append(CcReportEduclassGrade.dao.tableName).append(")")
							.append("批量删除记录失败").append(", 删除的内容包括(")
							.append(historyCcReportEduclassGrades.toString()).append(")")
							.toString());
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
			}
		}

		if (!addCcReportEduclassGrades.isEmpty() && !CcReportEduclassGrade.dao.batchSave(addCcReportEduclassGrades)) {
			logger.error(new StringBuilder("达成度统计：教学班考核分析法统计表(").append(CcReportEduclassGrade.dao.tableName).append(")")
					.append("批量增加的统计记录失败，增加记录包括(").append(addCcReportEduclassGrades.toString()).append(")")
					.toString());

			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		if(!updateCcReportEduclassGrades.isEmpty() && !CcReportEduclassGrade.dao.batchUpdate(updateCcReportEduclassGrades, "result, modify_date, statistics_date, weight, max_score, is_del")){
			logger.error(new StringBuilder("达成度统计：教学班考核分析法统计表(").append(CcReportEduclassGrade.dao.tableName).append(")")
					.append("批量更新的统计记录失败，增加记录包括(").append(updateCcReportEduclassGrades.toString()).append(")")
					.toString());

			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		return true;
	}

	/**
	 * 判断并更新教学班数据(评分表分析法)
	 *
	 * @param grade
	 * @param versionId
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean updateEduclassStatisticsEvalute(Integer grade, Long versionId) {
		List<CcStudentEvalute> needUpdateccStudentEvalutes = CcStudentEvalute.dao.findNeedToUpdate(versionId, grade);
		// 无更新不进行数据重新统计
		if (needUpdateccStudentEvalutes.isEmpty()){
			logger.info(new StringBuilder("达成度统计：专业认证版本编号为").append(versionId.toString()).append(",")
					.append(grade.toString()).append("年级下的所有使用考评点分析法的教学班学生成绩都为最新，无需进行更新")
					.toString());

			return true;
		}
		Long[] eduClassIds = new Long[needUpdateccStudentEvalutes.size()];
		for (int i = 0; i < needUpdateccStudentEvalutes.size(); i ++) {
			eduClassIds[i] = needUpdateccStudentEvalutes.get(i).getLong("educlass_id");
		}

		List<CcStudentEvalute> ccStudentEvalutes = CcStudentEvalute.dao.findAllByEduClasses(eduClassIds);
		// 教学班学生数信息
		List<CcEduclassStudent> educlassStudentSizes = CcEduclassStudent.dao.findByEduClassIds(eduClassIds);
		Map<Long, Long> eduClassStudentSizeMap = Maps.newHashMap();
		for (CcEduclassStudent ccEduclassStudent : educlassStudentSizes) {
			eduClassStudentSizeMap.put(ccEduclassStudent.getLong("class_id"), ccEduclassStudent.getLong("student_size"));
		}

		// 结果计算并保存
		Date date = new Date();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		List<CcReportEduclassEvalute> newCcReportEduclassEvalutes = Lists.newArrayList();
		// 每个班级学生个指标点下成绩累加
		Map<Long, Map<Long, BigDecimal>> idToAllScoreMap = Maps.newHashMap();
		for (CcStudentEvalute ccStudentEvalute : ccStudentEvalutes) {
			Long eduClassId = ccStudentEvalute.getLong("educlass_id");
			Map<Long, BigDecimal> scoreMap = null;
			if (idToAllScoreMap.get(eduClassId) == null) {
				scoreMap = Maps.newHashMap();
				idToAllScoreMap.put(eduClassId, scoreMap);
			} else {
				scoreMap = idToAllScoreMap.get(eduClassId);
			}
			Long evaluteId = ccStudentEvalute.getLong("evalute_id");
			BigDecimal score = ccStudentEvalute.getBigDecimal("score") == null ? new BigDecimal(0) : ccStudentEvalute.getBigDecimal("score");
			if (scoreMap.get(evaluteId) == null) {
				scoreMap.put(evaluteId, score);
				// 每个成绩项的基本数据创建
				CcReportEduclassEvalute ccReportEduclassEvalute = new CcReportEduclassEvalute();
				ccReportEduclassEvalute.set("educlass_id", ccStudentEvalute.getLong("educlass_id"));
				ccReportEduclassEvalute.set("evalute_id", ccStudentEvalute.getLong("evalute_id"));
				ccReportEduclassEvalute.set("weight", ccStudentEvalute.getBigDecimal("weight"));
				ccReportEduclassEvalute.set("statistics_date", date);
				ccReportEduclassEvalute.set("create_date", date);
				ccReportEduclassEvalute.set("modify_date", date);
				ccReportEduclassEvalute.set("is_del", CcReportEduclassGrade.DEL_NO);
				ccReportEduclassEvalute.set("id", idGenerate.getNextValue());
				if (eduClassStudentSizeMap.get(ccStudentEvalute.getLong("educlass_id")) != null) {
					ccReportEduclassEvalute.set("size", Integer.valueOf(eduClassStudentSizeMap.get(ccStudentEvalute.getLong("educlass_id")).toString()));
				}

				newCcReportEduclassEvalutes.add(ccReportEduclassEvalute);
			} else {
				scoreMap.put(evaluteId, PriceUtils._add(scoreMap.get(evaluteId), score));
			}
		}

		// 结果计算并保存
		Map<String, CcReportEduclassEvalute> newCcReportEduclassEvaluteMap = Maps.newHashMap();
		for (CcReportEduclassEvalute ccReportEduclassEvalute : newCcReportEduclassEvalutes) {
			Long eduClassId = ccReportEduclassEvalute.getLong("educlass_id");
			Long evaluteId = ccReportEduclassEvalute.getLong("evalute_id");
			String key = new StringBuilder(eduClassId.toString()).append(",").append(evaluteId.toString()).toString();
			BigDecimal averageScore = null;
			if (eduClassStudentSizeMap.get(eduClassId) == null || eduClassStudentSizeMap.get(eduClassId) == 0) {
				averageScore = new BigDecimal(0);
			} else {
				averageScore = idToAllScoreMap.get(eduClassId).get(ccReportEduclassEvalute.getLong("evalute_id")).divide(BigDecimal.valueOf(eduClassStudentSizeMap.get(eduClassId)), 3, RoundingMode.HALF_UP);
			}
			ccReportEduclassEvalute.set("result", averageScore);
			if(newCcReportEduclassEvaluteMap.get(key) == null){
				newCcReportEduclassEvaluteMap.put(key, ccReportEduclassEvalute);
			}
		}

		//得到考评点分析法的历史数据
		List<CcReportEduclassEvalute> historyCcReportEduclassEvalutes = CcReportEduclassEvalute.dao.findByColumnIn("educlass_id", eduClassIds);
		Map<String, CcReportEduclassEvalute> historyhistoryCcReportEduclassEvaluteMap = Maps.newHashMap();
		for(CcReportEduclassEvalute ccReportEduclassEvalute : historyCcReportEduclassEvalutes){
			String key = new StringBuilder(ccReportEduclassEvalute.getLong("educlass_id").toString())
					.append(",").append(ccReportEduclassEvalute.getLong("evalute_id").toString()).toString();
			historyhistoryCcReportEduclassEvaluteMap.put(key, ccReportEduclassEvalute);
		}

		List<CcReportEduclassEvalute> addCcReportEduclassEvalutes = Lists.newArrayList();
		List<CcReportEduclassEvalute> updateReportEduclassEvalutes = Lists.newArrayList();
		for(Map.Entry<String, CcReportEduclassEvalute> entry : newCcReportEduclassEvaluteMap.entrySet()){
			BigDecimal result = entry.getValue().getBigDecimal("result");
			CcReportEduclassEvalute historyCcReportEduclassEvalute = historyhistoryCcReportEduclassEvaluteMap.get(entry.getKey());
			if(historyCcReportEduclassEvalute == null){
				addCcReportEduclassEvalutes.add(entry.getValue());
			}else{
				historyCcReportEduclassEvalute.set("result", result);
				historyCcReportEduclassEvalute.set("modify_date", date);
				historyCcReportEduclassEvalute.set("statistics_date", date);
				historyCcReportEduclassEvalute.set("weight", entry.getValue().getBigDecimal("weight"));
				historyCcReportEduclassEvalute.set("is_del", CcReportEduclassGrade.DEL_NO);
				updateReportEduclassEvalutes.add(historyCcReportEduclassEvalute);
			}

		}

		//需要删除的教学班考核分析法
		Long[] ccReportEduclassEvaluteId = {};
		if(!historyCcReportEduclassEvalutes.isEmpty()){
			if(!updateReportEduclassEvalutes.isEmpty()){
				historyCcReportEduclassEvalutes.removeAll(updateReportEduclassEvalutes);
			}
			if(!historyCcReportEduclassEvalutes.isEmpty()){
				ccReportEduclassEvaluteId = new Long[historyCcReportEduclassEvalutes.size()];
				for(int i=0; i<historyCcReportEduclassEvalutes.size(); i++){
					ccReportEduclassEvaluteId[i] = historyCcReportEduclassEvalutes.get(i).getLong("id");
				}
				if(!CcReportEduclassEvalute.dao.deleteAll(ccReportEduclassEvaluteId, date)){
					logger.error(new StringBuilder("达成度统计：教学班考评点分析法达成度统计表(").append(CcReportEduclassEvalute.dao.tableName).append(")")
							.append("批量删除记录失败").append(", 删除的内容包括(")
							.append(historyCcReportEduclassEvalutes.toString()).append(")")
							.toString());
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
			}
		}

		if (!addCcReportEduclassEvalutes.isEmpty() && !CcReportEduclassEvalute.dao.batchSave(addCcReportEduclassEvalutes)) {
			logger.error(new StringBuilder("达成度统计：教学班考评点分析法统计表(").append(CcReportEduclassGrade.dao.tableName).append(")")
					.append("批量增加的统计记录失败，增加记录包括(").append(addCcReportEduclassEvalutes.toString()).append(")")
					.toString());

			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		if(!updateReportEduclassEvalutes.isEmpty() && !CcReportEduclassEvalute.dao.batchUpdate(updateReportEduclassEvalutes, "result, modify_date, statistics_date, weight, is_del")){
			logger.error(new StringBuilder("达成度统计：教学班考评点分析法统计表(").append(CcReportEduclassGrade.dao.tableName).append(")")
					.append("批量更新的统计记录失败，增加记录包括(").append(updateReportEduclassEvalutes.toString()).append(")")
					.toString());

			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		return true;
	}

	/**
	 * 教学班指标点权重map
	 * @param versionId
	 * @param grade
	 * @return
	 */
	private Map<Long, Map<Long, BigDecimal>> educlassIndicationWeightMap (Long versionId, Integer grade){
		//教学班对应的指标点的成绩组成权重和
		List<CcReportEduclassGrade> reportEduclassGradeList = CcReportEduclassGrade.dao.findEduclassIndicationWeight(versionId, grade);
		Map<Long, Map<Long, BigDecimal>> educlassIndicationMap = Maps.newHashMap();
		for(CcReportEduclassGrade ccReportEduclassGrade : reportEduclassGradeList){
			Long educlassId = ccReportEduclassGrade.getLong("educlass_id");
			Long indicationId = ccReportEduclassGrade.getLong("indication_id");
			BigDecimal weight = ccReportEduclassGrade.getBigDecimal("weight");
			Map<Long, BigDecimal> indicationMap = Maps.newHashMap();
			indicationMap.put(indicationId, weight);
			if(educlassIndicationMap.get(educlassId) == null){
				educlassIndicationMap.put(educlassId, indicationMap);
			}else {
				educlassIndicationMap.get(educlassId).put(indicationId, weight);
			}
		}
		return  educlassIndicationMap;
	}


	/**
	 * 统计教学班的报表，生成课程达成度报表数据
	 * 注意：这里如果一个教师课程，有两个教学班，计算课程达成度报表数据的时候，直接把每个学生加起来，除以总学生数。而不是两个教学班的报表加一下除以二。
	 * @param grade
	 * @param versionId
	 * @return 这个与statisticsCourseResultForJGExcept 区别 是包含统计所有的学生（剔除和不剔除）
	 */

	public boolean statisticsCourseResultForJG(Integer grade, Long versionId) {
		/*
		 * 1.  获取当前年级和版本下的所有课程
		 * 2.1  获取对应课程的教学班Map
		 * 2.2  获取课程对应的指标点Map
		 * 3.  获取所有教学班对应的平均分，还有人数。
		 * 4.  获取这些课程目标下的学生总分和人数
		 * 5.  设置数据，保存
		 */
		// 1. 获取当前年级和版本下排除评分表算法的所有课程
		List<CcCourse> ccCourseList = CcCourse.dao.findAllByGradeAndVersion(grade, versionId, null, null);
		List<Long> courseIdList = new ArrayList<>();

		for(CcCourse temp : ccCourseList) {
			Long id = temp.getLong("id");
			courseIdList.add(id);
		}


		// 2.1 获取对应课程的教学班Map
		// <courseId, List<educlassId>>
		Map<Long, List<Long>> courseEduclassIdMap = new HashMap<>();
		// 一个教学班属于哪个课程  教学班与课程id绑定
		Map<Long, Long> educlassCourseIdMap = new HashMap<>();
		// 因为课程列表里课程id可能有不同年级在用，那么这个教学班列表包含了其他年级的  这里需要加个年级条件
		List<CcEduclass> ccEduclasseList = CcEduclass.dao.findAllByCourseIds(courseIdList,grade);
		List<Long> ccEduclassIdList = new ArrayList<>();
		for(CcEduclass temp : ccEduclasseList) {
			Long courseId = temp.getLong("course_id");
			Long educlassId = temp.getLong("id");

			List<Long> educlassIdList = courseEduclassIdMap.get(courseId);
			if(educlassIdList == null || educlassIdList.isEmpty()) {
				educlassIdList = new ArrayList<>();
				courseEduclassIdMap.put(courseId,educlassIdList);
			}

			educlassIdList.add(educlassId);

			// 是否加过，没加过则加进去（应该是每次都是唯一，可以加的，但是为了排除万一）
			if(!ccEduclassIdList.contains(educlassId)) {
				ccEduclassIdList.add(educlassId);
			}


			educlassCourseIdMap.put(educlassId, courseId);
		}




		// 3. 找到个个教学班下，有多少个学生 。
		//这个与statisticsCourseResultForJGExcept区别的地方就是这个 这个是统计教学班的人数
		List<CcEduclassStudent> ccEduclassStudentList = CcEduclassStudent.dao.countStudentNum(ccEduclassIdList);
		Map<Long, Long> educlassStudentMap = new HashMap<>();
		Map<Long, Long> courseStudentMap = new HashMap<>();
		for(CcEduclassStudent tempCcEduclassStudent : ccEduclassStudentList) {

			Long eduClassId = tempCcEduclassStudent.getLong("eduClassId");
			Long studentNum = tempCcEduclassStudent.getLong("studentNum");
			educlassStudentMap.put(eduClassId, studentNum);

			Long coaurseId = educlassCourseIdMap.get(eduClassId);
			Long courseStudentNum  = courseStudentMap.get(coaurseId);
			courseStudentNum = courseStudentNum == null ? 0L : courseStudentNum;
			courseStudentNum = courseStudentNum + studentNum;
			courseStudentMap.put(coaurseId, courseStudentNum);
		}
		// 4.  获取这些课程目标下的学生总分和人数。注意：因为课程目标在课程下，所以不需要找到课程下的课程目标
		Map<Long, BigDecimal> gradecomposeIndicationIdTotalScore = new HashMap<>();
		Map<Long, BigDecimal> gradecomposeIndicationIdMaxScore = new HashMap<>();
		Map<Long, BigDecimal> gradecomposeIndicationIdWeight = new HashMap<>();
		Map<Long, Long> gradecomposeIndicationIdcourseId = new HashMap<>();
		Map<Long, Long> gradecomposeIdMap = new HashMap<>();

		//TODO 一个课程一个老师下面有多个教学班这里会出现问题，取其中一个教学班id就可以统计所有的了，所以去除多余的
		//找出老师下面多个教学班其中的
		List<CcEduclass> educlassIds = CcEduclass.dao.findEduclassIds(courseIdList);
		List<Long> ccEduclassIdList1 = new ArrayList<>();
		for (CcEduclass temp:educlassIds){

			Long classId = temp.getLong("id");
			// 是否加过，没加过则加进去（应该是每次都是唯一，可以加的，但是为了排除万一）
			if(!ccEduclassIdList1.contains(classId)) {
				ccEduclassIdList1.add(classId);
			}
		}

		// 3. 找到个个教学班下，总分有多少，（平均分有误，因为学生没有分数的，他无法计算）
		//TODO 批次成绩加上之后会出现不同的课程的满分不相同，所以这个统计需要改动
		List<CcScoreStuIndigrade> ccScoreStuIndigrades = CcScoreStuIndigrade.dao.findAllClassGradeByEduclassIds(ccEduclassIdList1);
		//List<CcScoreStuIndigrade> ccScoreStuIndigrades = CcScoreStuIndigrade.dao.findCourseGradecomposeIdsGrade(ccEduclassIdList,null,null);

		for(CcScoreStuIndigrade tempCcScoreStuIndigrade : ccScoreStuIndigrades) {

			Long gradecomposeIndicationId = tempCcScoreStuIndigrade.getLong("gradecomposeIndicationId");
			BigDecimal weight = tempCcScoreStuIndigrade.getBigDecimal("weight");
			//成绩总分
			BigDecimal totalGrade = tempCcScoreStuIndigrade.getBigDecimal("totalGrade");
			//满分
			//BigDecimal maxScore = tempCcScoreStuIndigrade.getBigDecimal("maxScore");
			gradecomposeIndicationIdTotalScore.put(gradecomposeIndicationId, totalGrade);
			//gradecomposeIndicationIdMaxScore.put(gradecomposeIndicationId, maxScore);
			gradecomposeIndicationIdWeight.put(gradecomposeIndicationId, weight);
			gradecomposeIndicationIdcourseId.put(gradecomposeIndicationId,tempCcScoreStuIndigrade.getLong("course_id"));
			gradecomposeIdMap.put(gradecomposeIndicationId,tempCcScoreStuIndigrade.getLong("gradecompose_id"));
		}
		// 课程下包含了那些 gradecomposeIndicationId。 通过课程目标关联过去
		List<CcIndication> ccIndicationList = CcIndication.dao.findEachIndicationCourseAndGradecomposeIndicationByCourseIds(courseIdList);
		Map<Long, CcIndication> ccIndicationMap = new HashMap<>();
		// <courseId, Map<indication_course_id, Map<indicationId, List<gradecomposeIndicationId>>>>
		//courseIndicationCourseIdIndicationIdGradecomposeIndicationIdMap主要是用于课程与课程目标关联
		Map<Long, Map<Long,  Map<Long, List<Long>>>> courseIndicationCourseIdIndicationIdGradecomposeIndicationIdMap = new HashMap<>();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		for(CcIndication tempCcIndication : ccIndicationList) {
			Long courseId = tempCcIndication.getLong("course_id");
			Long indicationCourseId = tempCcIndication.getLong("indicationCourseId");
			Long indicationId = tempCcIndication.getLong("id");
			Long gradecomposeIndicationId = tempCcIndication.getLong("gradecomposeIndicationId");
			//indicationId课程目标id
			ccIndicationMap.put(indicationId, tempCcIndication);
			Map<Long,  Map<Long, List<Long>>> indicationCourseIdAndIndicationIdAndGradecomposeIndicationIdMap = courseIndicationCourseIdIndicationIdGradecomposeIndicationIdMap.get(courseId);
			if(indicationCourseIdAndIndicationIdAndGradecomposeIndicationIdMap == null || indicationCourseIdAndIndicationIdAndGradecomposeIndicationIdMap.isEmpty()) {
				indicationCourseIdAndIndicationIdAndGradecomposeIndicationIdMap = new HashMap<>();
				courseIndicationCourseIdIndicationIdGradecomposeIndicationIdMap.put(courseId, indicationCourseIdAndIndicationIdAndGradecomposeIndicationIdMap);
			}
			Map<Long, List<Long>> indicationIdAndGradecomposeIndicationIdMap = indicationCourseIdAndIndicationIdAndGradecomposeIndicationIdMap.get(indicationCourseId);
			if(indicationIdAndGradecomposeIndicationIdMap == null || indicationIdAndGradecomposeIndicationIdMap.isEmpty()) {
				indicationIdAndGradecomposeIndicationIdMap = new HashMap<>();
				indicationCourseIdAndIndicationIdAndGradecomposeIndicationIdMap.put(indicationCourseId, indicationIdAndGradecomposeIndicationIdMap);
			}
			List<Long> gradecomposeIndicationIdList = indicationIdAndGradecomposeIndicationIdMap.get(indicationId);
			if(gradecomposeIndicationIdList == null || gradecomposeIndicationIdList.isEmpty()) {
				gradecomposeIndicationIdList = new ArrayList<>();
				indicationIdAndGradecomposeIndicationIdMap.put(indicationId, gradecomposeIndicationIdList);
			}
			gradecomposeIndicationIdList.add(gradecomposeIndicationId);
		}
		Boolean resultResult = Boolean.TRUE;
		List<CcReportCourse> saveCcReportCourseList = new ArrayList<>();
		// 获取老的数据，用于保存原有记录
		List<CcReportCourse> oldCcReportCourseList = CcReportCourse.dao.findAllByGradeAndVersion(grade, versionId);
		// Map<"grade,indicationCourseId", CcReportCourse>
		Map<String, CcReportCourse> oldCcReportCourseMap = new HashMap<>();
		for(CcReportCourse temp : oldCcReportCourseList) {
			Long indicationCourseId = temp.getLong("indication_course_id");
			String key = grade + "," + indicationCourseId;
			oldCcReportCourseMap.put(key, temp);
		}
		List<Long> indicationCourseIds = new ArrayList<>();
		// 准备指标点达成度数据，需要考虑专业方向
		Map<String, BigDecimal> majorReportResultMap = Maps.newLinkedHashMap();
		//多选一
		Map<String, Map<Long, BigDecimal>> courseGroupResultMap = Maps.newHashMap();
		// 准备一份map，用于数据获取
		List<CcIndicationCourse> ccIndicationCourses = CcIndicationCourse.dao.findByVersionId(versionId,1);
		Map<Long, CcIndicationCourse> indicationCourseMap = new HashMap<>();
		for(CcIndicationCourse temp : ccIndicationCourses) {
			Long indicationCourseId = temp.getLong("id");
			indicationCourseMap.put(indicationCourseId, temp);
		}


		Date date = new Date();
		// 遍历整个课程，设置数据
		for(Long courseId : courseIdList) {
			CcTeacherCourse courseResultType = CcTeacherCourse.dao.findCourseResultType(courseId);
			//通过课程id查找开课课程的达成度计算方式，因为一个课程可能有多个计算方式，我目前只取第一个计算方式计算这个课程的达成度
			Integer resultType = courseResultType.getInt("result_type");
			if (resultType==3){
				System.out.println("ss");
			}
			//开课id
			Long teacherCourseId = courseResultType.getLong("id");
			BigDecimal studentNum = new BigDecimal(courseStudentMap.get(courseId) == null ? 0 : courseStudentMap.get(courseId));
			if (!PriceUtils.greaterThan(studentNum,new BigDecimal("0"))){
				continue;
			}
			Map<Long,  Map<Long, List<Long>>> indicationCourseIdAndIndicationIdAndGradecomposeIndicationIdMap = courseIndicationCourseIdIndicationIdGradecomposeIndicationIdMap.get(courseId);
			// 没加过指标点
			if(indicationCourseIdAndIndicationIdAndGradecomposeIndicationIdMap == null) {
				continue;
			}
			// 遍历指标点
			for(Entry<Long, Map<Long, List<Long>>> indicationCourseIdAndIndicationIdAndGradecomposeIndicationIdEntry : indicationCourseIdAndIndicationIdAndGradecomposeIndicationIdMap.entrySet()) {
				Long indicationCourseId = indicationCourseIdAndIndicationIdAndGradecomposeIndicationIdEntry.getKey();
				Map<Long, List<Long>> indicationIdAndgradecomposeIndicationIdMap = indicationCourseIdAndIndicationIdAndGradecomposeIndicationIdEntry.getValue();
				BigDecimal result = new BigDecimal(0);
				// 遍历课程目标
				for(Entry<Long, List<Long>> indicationIdAndGradecomposeIndicationIdEntry : indicationIdAndgradecomposeIndicationIdMap.entrySet()) {



					BigDecimal resultTemp = new BigDecimal(0);
					BigDecimal mom = new BigDecimal(0);
					BigDecimal son = new BigDecimal(0);
					BigDecimal A = new BigDecimal(0);
					// 课程目标
					Long indicationId = indicationIdAndGradecomposeIndicationIdEntry.getKey();
					List<Long> gradecomposeIndicationIdList = indicationIdAndGradecomposeIndicationIdEntry.getValue();
					//所有成绩组成这个课程目标的总权重 如果一个课程不同老师的权重是不同的那这里就需要改动。因为我这里是按照每个开课老师对这个课程的权重设置都是一样的
					BigDecimal allWeight = CcCourseGradecomposeIndication.dao.calculateSumWeights(teacherCourseId, indicationId);
					// 遍历每个 课程目标的成绩组成
					for(Long gradecomposeIndicatoinId : gradecomposeIndicationIdList) {
						BigDecimal totalGrade = gradecomposeIndicationIdTotalScore.get(gradecomposeIndicatoinId);
						if(totalGrade == null) {
							continue;
						}
						BigDecimal maxScore = gradecomposeIndicationIdMaxScore.get(gradecomposeIndicatoinId);
						//权重
						BigDecimal weight = gradecomposeIndicationIdWeight.get(gradecomposeIndicatoinId);
						//平均分

						BigDecimal avgGrade = totalGrade.divide(studentNum, 5, RoundingMode.HALF_UP);
						// 当不存在数据的时候

						//TODO 2020/09/07 一个课程多个教学班满分计算方式改为(A班学生数*A班的CO1的满分+B班学生数*B班的CO1的满分)/总学生数，
						// 权重应该是相同的，不然这个还要改

						Long courseIds = gradecomposeIndicationIdcourseId.get(gradecomposeIndicatoinId);
						//成绩组成id
						Long gradecomposeId = gradecomposeIdMap.get(gradecomposeIndicatoinId);
						//(A班学生数*A班的CO1的满分+B班学生数*B班的CO1的满分)
						BigDecimal maxScores = new BigDecimal("0");
						//统计每个教学班的学生个数
						List<CcEduclassStudent> ccEduclassStudents = CcEduclassStudent.dao.countStudentNums(courseIds);
						for (CcEduclassStudent educlassStudent : ccEduclassStudents){
							Long edClassId = educlassStudent.getLong("id");

							Object studentNums = educlassStudent.get("studentNum");
							if (studentNums==null || studentNums.equals("")){
								continue;
							}
							int classStudentNum = Integer.parseInt(educlassStudent.get("studentNum")+"");
							BigDecimal studentNumBig = new BigDecimal(classStudentNum);

							CcCourseGradecomposeIndication maxscoreAndWeight = CcCourseGradecomposeIndication.dao.findGradecomposeMaxscoreAndWeight(edClassId, indicationId, gradecomposeId);
							if (maxscoreAndWeight==null){
								continue;
							}
							//(A班学生数*A班的CO1的满分+B班学生数*B班的CO1的满分)
							BigDecimal indicationMaxScore = maxscoreAndWeight.getBigDecimal("max_score");
							maxScores=maxScores.add(indicationMaxScore.multiply(studentNumBig));

						}
						//平均分满分
						BigDecimal avgMaxScore=maxScores.divide(studentNum,2);
						//浙江科技学院的达成度算法
						if (resultType.equals(CcTeacherCourse.RESULT_TYPE_SCORE)){
							/*
							 *  注意：
							 *  这里如果一个教师课程，有两个教学班，计算课程达成度报表数据的时候，直接把每个学生加起来，除以总学生数。
							 *  而不是两个教学班的报表加一下除以二。
							 *  所以不能通过cc_edupoint_aims_achieve的数据处理
							 *  要重新计算所有学生的总分 和 总人数，算出平均分，然后处理 （即：【课程下的教学班们】 交集 ）
							 *  result1 = mom1/(son1)
							 *  result2 = mom2/(son2)
							 *  result3 = mom3/(son3)
							 *  result  = 指标点权重 * (result1 , result2, result3)min
							 *  这里的算法没有改
							 *  指标点达成度应该为 课程目标大达成度的最小值*指标点权重 也就是result
							 *  result1、result2、result3 应该为课程达成度
							 *  课程达成度计算公式(平均分1*权重1+平均分2*权重2+平均分3*权重3……)/(总分1*权重1+总分2*权重2+总分3*权重3……)
							 *  (平均分1*权重1+平均分2*权重2+平均分3*权重3……)相当于mom
							 *  总分1*权重1+总分2*权重2+总分3*权重3……)相当于son
							 *  下面说的满分即使总分
							 */

							//平均分*权重
							mom = mom.add(avgGrade.multiply(weight));


							//(A班学生数*A班的CO1的满分+B班学生数*B班的CO1的满分)/总人数*权重
							son = son.add(maxScores.divide(studentNum,2).multiply(weight));
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
							 * mon=平均分S1/S1总分
							 * son=成绩CO1S1的权重/CO1权重的总和
							 *  A=mom*son
							 */
							mom=avgGrade.divide(avgMaxScore,5);
							son=weight.divide(allWeight,5);
							A = A.add(mom.multiply(son));

						}

					}
					CcIndication ccIndication = ccIndicationMap.get(indicationId);
					if(ccIndication == null) {
						continue;
					}
					BigDecimal expectedValue = ccIndication.getBigDecimal("expected_value");
					if(son.compareTo(new BigDecimal(0)) == 0) {
						continue;
					}
					if (resultType.equals(CcTeacherCourse.RESULT_TYPE_SCORE)){
						//课程目标达成度算法(平均分1*权重1+平均分2*权重2+平均分3*权重3……)/(总分1*权重1+总分2*权重2+总分3*权重3……) mom/son
						resultTemp = mom.divide(son, 5, RoundingMode.HALF_UP);
					}else{
						//财经大学  A=mom*son  A=（平均分S1/S1总分）*（成绩CO1S1的权重/CO1权重的总和）+（平均分S2/S2总分）*（成绩CO1S2的权重/CO1权重的总和）+....
						resultTemp = A;
					}

					//期望值
					//resultTemp = resultTemp.divide(expectedValue, 5, RoundingMode.HALF_UP);
					// 如果前者更小，用前者
					result = result.compareTo(new BigDecimal(0)) == 0 ? resultTemp : result.compareTo(resultTemp) == -1 ? result : resultTemp;
				}
				CcIndicationCourse ccIndicationCourse = indicationCourseMap.get(indicationCourseId);
				if(ccIndicationCourse == null) {
					continue;
				}
				BigDecimal weight = ccIndicationCourse.getBigDecimal("weight");
				//这个应该是指标点达成度吧
				result = result.multiply(weight);

				String key = grade + "," + indicationCourseId;
				CcReportCourse oldCcReportCourse = oldCcReportCourseMap.get(key);

				CcReportCourse ccReportCourse = new CcReportCourse();
				ccReportCourse.set("grade", grade);
				ccReportCourse.set("indication_course_id", indicationCourseId);

				if(oldCcReportCourse != null) {
					BigDecimal result1 = oldCcReportCourse.getBigDecimal("result");
					if (result1 != null){
						ccReportCourse.set("result", PriceUtils.currency(oldCcReportCourse.getBigDecimal("result")));
					}

				}
				ccReportCourse.set("except_result", PriceUtils.currency(result));
				ccReportCourse.set("is_del", CcReportCourse.DEL_NO);
				ccReportCourse.set("statistics_date", date);
				ccReportCourse.set("create_date", date);
				ccReportCourse.set("modify_date", date);
				ccReportCourse.set("id", idGenerate.getNextValue());
				saveCcReportCourseList.add(ccReportCourse);

				// 记录课程指标点编号
				indicationCourseIds.add(indicationCourseId);

				// 记录专业及其每个方向上的指标点达成度
				Long majorDirectionId = ccIndicationCourse.getLong("direction_id");
				Long courseGroupId = ccIndicationCourse.getLong("course_group_id");
				//教学分组编号
				Long teachGroupId = ccIndicationCourse.getLong("teach_group_id");
				if (courseGroupId != null || teachGroupId !=null ) {
					System.out.println("ssss");
				}
				// 无专业方向，赋予专业方向编号无专业方向编号
				if (majorDirectionId == null) {
					majorDirectionId = CcReportMajor.NO_MAJORDIRECTION_ID;
				}

				Long indicatorPointId = ccIndicationCourse.getLong("indication_id");
				// 指标点编号,专业方向编号
				String indicationMajorResultKey = indicatorPointId + "," + majorDirectionId;

				if (majorReportResultMap.get(indicationMajorResultKey) == null) {
					majorReportResultMap.put(indicationMajorResultKey, new BigDecimal(0));
				}

				// 多选一编号 分级教学编号   这里先不加分级教学的数据,
				if (teachGroupId == null){


					if (courseGroupId == null ) {
						majorReportResultMap.put(indicationMajorResultKey
								, PriceUtils._add(PriceUtils.currency(majorReportResultMap.get(indicationMajorResultKey))
										, PriceUtils.currency(result)));

					} else {

						Map<Long, BigDecimal> courseGroupResult = null;
						if (courseGroupResultMap.get(indicationMajorResultKey) == null) {
							courseGroupResult = Maps.newHashMap();
							courseGroupResultMap.put(indicationMajorResultKey, courseGroupResult);
						} else {
							courseGroupResult = courseGroupResultMap.get(indicationMajorResultKey);
						}
						BigDecimal minScore = courseGroupResult.get(courseGroupId);
						// 当课程组中该门课成绩更小或是之前课程组未有成绩时，课程组成绩为该门课成绩
						if (minScore == null || PriceUtils.greaterThan(minScore, result)) {
							minScore = result;
						}
						courseGroupResult.put(courseGroupId, minScore);

					}

				}
			}
		}

		// 删除之前旧课程达成度记录
		if (!indicationCourseIds.isEmpty() && !CcReportCourse.dao.deleteAllNoDel(grade, indicationCourseIds)) {
			logger.error(new StringBuilder("达成度统计：课程达成度统计表(").append(CcReportCourse.dao.tableName).append(")")
					.append("批量删除旧的统计记录失败, 删除的年级为").append(grade.toString()).append(", 删除的课程指标点编号包括(")
					.append(indicationCourseIds.toString()).append(")")
					.toString());

			return false;
		}

		// 保存新的课程达成度记录
		if (!saveCcReportCourseList.isEmpty() && !CcReportCourse.dao.batchSave(saveCcReportCourseList)) {
			logger.error(new StringBuilder("达成度统计：课程达成度统计表(").append(CcReportCourse.dao.tableName).append(")")
					.append("批量增加新的统计记录失败，新增的记录包括(").append(saveCcReportCourseList.toString()).append(")")
					.toString());

			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		logger.info("达成度统计：课程达成度统计完成，准备进行指标点达成度统计");

		// 指标点达成度的数据汇总与保存
		List<CcReportMajor> ccReportMajors = Lists.newArrayList();
		// 获取老的数据，用于保存原有记录
		List<CcReportMajor> oldCcReportMajorList = CcReportMajor.dao.findAllByGradeAndVersion(grade, versionId);
		// Map<"grade,indication_id", CcReportMajor>
		Map<String, CcReportMajor> oldCcReportMajorMap = new HashMap<>();
		for(CcReportMajor temp : oldCcReportMajorList) {
			Long indicationId = temp.getLong("indication_id");
			String key = grade + "," + indicationId;
			oldCcReportMajorMap.put(key, temp);
		}
		List<Long> indicationIds = Lists.newArrayList();
		for (Map.Entry<String, BigDecimal> entry : majorReportResultMap.entrySet()) {
			String[] keySplit = entry.getKey().split(",");
			// 指标点编号
			Long indicationId = Long.valueOf(keySplit[0]);
			// 专业方向编号
			Long majorDirectionId = Long.valueOf(keySplit[1]);
			majorDirectionId = CcReportMajor.NO_MAJORDIRECTION_ID.equals(majorDirectionId) ? null : majorDirectionId;
			// 指标点得分
			BigDecimal indicationScore = PriceUtils.currency(entry.getValue());
			String noMajorDirectionIndicationKey = indicationId + "," + CcReportMajor.NO_MAJORDIRECTION_ID;
			// 当专业认证处于方向上时，需加上方向上的达成度
			if (majorDirectionId != null && majorReportResultMap.get(noMajorDirectionIndicationKey) != null) {
				indicationScore = PriceUtils._add(indicationScore, PriceUtils.currency(majorReportResultMap.get(noMajorDirectionIndicationKey)));
			}

			// 无方向
			Map<Long, BigDecimal> courseGroupScoreMapTemp = null;
			Map<Long, BigDecimal> courseGroupScoreMap = courseGroupResultMap.get(entry.getKey());
			if(majorDirectionId == null){
				courseGroupScoreMapTemp = courseGroupScoreMap;
			}else{
				//有方向
				Map<Long, BigDecimal> noDirectionCourseGroupScoreMap = courseGroupResultMap.get(noMajorDirectionIndicationKey);
				if(noDirectionCourseGroupScoreMap == null){
					noDirectionCourseGroupScoreMap = Maps.newHashMap();
				}
				if(courseGroupScoreMap != null){
					for (Map.Entry<Long, BigDecimal> courseGroupScoreEntry : courseGroupScoreMap.entrySet()) {
						BigDecimal dirValue = courseGroupScoreEntry.getValue();
						Long dirKey = courseGroupScoreEntry.getKey();
						if (dirValue != null ) {
							BigDecimal noDirValue = noDirectionCourseGroupScoreMap.get(dirKey);
							if(noDirValue == null){
								noDirectionCourseGroupScoreMap.put(dirKey, dirValue);
							}else{
								noDirectionCourseGroupScoreMap.put(dirKey, PriceUtils.lessThan(dirValue, noDirValue) ? dirValue : noDirValue);
							}
						}
					}
				}
				courseGroupScoreMapTemp = noDirectionCourseGroupScoreMap;
			}
			if (courseGroupScoreMapTemp != null) {
				for (Map.Entry<Long, BigDecimal> courseGroupScoreEntry : courseGroupScoreMapTemp.entrySet()) {
					if (courseGroupScoreEntry.getValue() != null ) {
						indicationScore = PriceUtils._add(indicationScore, courseGroupScoreEntry.getValue());
					}
				}
			}

			String key = grade + "," + indicationId;
			CcReportMajor oldCcReportMajor = oldCcReportMajorMap.get(key);

			CcReportMajor ccReportMajor = new CcReportMajor();
			ccReportMajor.set("indication_id", indicationId);
			ccReportMajor.set("grade", grade);
			ccReportMajor.set("major_direction_id", majorDirectionId);
			BigDecimal maxResult = new BigDecimal(1);

			//TODO 因为分级教学 只要课程分组中合计最小的值加入达成度中
			BigDecimal minTeacherResult = new BigDecimal("0");
			List<CcIndicationCourse> teachIndicationCourses = CcIndicationCourse.dao.sumTeachGroupResult(versionId, indicationId, null);

			for (CcIndicationCourse temp: teachIndicationCourses){
				BigDecimal teachMaxResult = temp.getBigDecimal("teachMaxResult");
				if (teachMaxResult !=null){
					minTeacherResult=minTeacherResult.add(teachMaxResult);
				}

				System.out.println(teachMaxResult);

			}
			if(oldCcReportMajor != null && oldCcReportMajor.getBigDecimal("except_result") !=null) {
				BigDecimal exceptResult = oldCcReportMajor.getBigDecimal("except_result");
				System.out.println(exceptResult);
				//数据加上分级教学中分组最小值
				BigDecimal subResult = oldCcReportMajor.getBigDecimal("except_result").add(minTeacherResult);

				//如果值超过1就设置为1
				if(PriceUtils.greaterThan(PriceUtils.currency(subResult),maxResult))
				{
					ccReportMajor.set("except_result", PriceUtils.currency(maxResult));
				}else{
					ccReportMajor.set("except_result", PriceUtils.currency(subResult));
				}


			}

			//如果值超过1就设置为1
			indicationScore=indicationScore.add(minTeacherResult);
			if (PriceUtils.greaterThan(indicationScore,maxResult)){
				ccReportMajor.set("result", maxResult);
			}else {
				ccReportMajor.set("result", indicationScore);
			}
			ccReportMajor.set("is_del", CcReportMajor.DEL_NO);
			ccReportMajor.set("statistics_date", date);
			ccReportMajor.set("create_date", date);
			ccReportMajor.set("modify_date", date);
			ccReportMajor.set("id", idGenerate.getNextValue());
			ccReportMajors.add(ccReportMajor);
			indicationIds.add(indicationId);
		}

		// 删除旧的指标点达成度的记录
		if (!indicationIds.isEmpty() && !CcReportMajor.dao.deleteAllNoDel(grade, indicationIds)) {
			logger.error(new StringBuilder("达成度统计：指标点达成度统计表(").append(CcReportMajor.dao.tableName).append(")")
					.append("批量删除旧的统计记录失败, 删除的年级为").append(grade.toString()).append(", 删除的指标点编号包括(")
					.append(indicationCourseIds.toString()).append(")")
					.toString());

			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		// 保存新的指标点达成度的记录
		if (!ccReportMajors.isEmpty() && !CcReportMajor.dao.batchSave(ccReportMajors)) {
			logger.error(new StringBuilder("达成度统计：指标点达成度统计表(").append(CcReportCourse.dao.tableName).append(")")
					.append("批量增加新的统计记录失败，新增的记录包括(").append(ccReportMajors.toString()).append(")")
					.toString());

			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		logger.info("达成度统计：指标点达成度统计完成");
		return resultResult;
	}

	/**
	 * 统计教学班的报表，生成课程达成度报表数据
	 * 注意：这里如果一个教师课程，有两个教学班，计算课程达成度报表数据的时候，直接把每个学生加起来，除以总学生数。而不是两个教学班的报表加一下除以二。
	 * @param grade
	 * @param versionId
	 * @return  这个是不包含剔除学生的-----
	 */
	public boolean statisticsCourseResultForJGExcept(Integer grade, Long versionId) {
		/*
		 * 1.  获取当前年级和版本下的所有课程
		 * 2.1  获取对应课程的教学班Map
		 * 2.2  获取课程对应的指标点Map
		 * 3.  获取所有教学班对应的平均分，还有人数。
		 * 4.  获取这些课程目标下的学生总分和人数
		 * 5.  设置数据，保存
		 */
		// 1. 获取当前年级和版本下的所有课程
		List<CcCourse> ccCourseList = CcCourse.dao.findAllByGradeAndVersion(grade, versionId, null, null);
		List<Long> courseIdList = new ArrayList<>();
		List<Long> courseTeacherIdList=new ArrayList<>();
		for(CcCourse temp : ccCourseList) {
			Long id = temp.getLong("id");
			courseIdList.add(id);
			Long teacherCourseId = temp.getLong("teacherCourseId");
			courseTeacherIdList.add(teacherCourseId);
		}
		// 2.1 获取对应课程的教学班Map
		// <courseId, List<educlassId>>
		Map<Long, List<Long>> courseEduclassIdMap = new HashMap<>();
		// 一个教学班属于哪个课程  //2020/06/16加了grade条件
		Map<Long, Long> educlassCourseIdMap = new HashMap<>();
		List<CcEduclass> ccEduclasseList = CcEduclass.dao.findAllByCourseIds(courseIdList,grade);
		List<Long> ccEduclassIdList = new ArrayList<>();
		for(CcEduclass temp : ccEduclasseList) {
			Long courseId = temp.getLong("course_id");
			Long educlassId = temp.getLong("id");

			List<Long> educlassIdList = courseEduclassIdMap.get(courseId);
			if(educlassIdList == null || educlassIdList.isEmpty()) {
				educlassIdList = new ArrayList<>();
			}
			educlassIdList.add(educlassId);

			// 是否加过，没加过则加进去（应该是每次都是唯一，可以加的，但是为了排除万一）
			if(!ccEduclassIdList.contains(educlassId)) {
				ccEduclassIdList.add(educlassId);
			}

			educlassCourseIdMap.put(educlassId, courseId);
		}

		// 3. 找到个个教学班下，有多少个学生 。 课程下有多少学生 统计剔除学生后的教学班学生数量

		List<CcEduclassStudent> ccEduclassStudentList = CcEduclassStudent.dao.countStudentNum(ccEduclassIdList, Boolean.TRUE);
		Map<Long, Long> educlassStudentMap = new HashMap<>();
		Map<Long, Long> courseStudentMap = new HashMap<>();
		for(CcEduclassStudent tempCcEduclassStudent : ccEduclassStudentList) {

			Long eduClassId = tempCcEduclassStudent.getLong("eduClassId");
			Long studentNum = tempCcEduclassStudent.getLong("studentNum");
			educlassStudentMap.put(eduClassId, studentNum);

			Long coaurseId = educlassCourseIdMap.get(eduClassId);
			Long courseStudentNum  = courseStudentMap.get(coaurseId);
			courseStudentNum = courseStudentNum == null ? 0L : courseStudentNum;
			courseStudentNum = courseStudentNum + studentNum;
			courseStudentMap.put(coaurseId, courseStudentNum);
		}
		// 4.  获取这些课程目标下的学生总分和人数。注意：因为课程目标在课程下，所以不需要找到课程下的课程目标
		Map<Long, BigDecimal> gradecomposeIndicationIdTotalScore = new HashMap<>();
		Map<Long, BigDecimal> gradecomposeIndicationIdMaxScore = new HashMap<>();
		Map<Long, BigDecimal> gradecomposeIndicationIdWeight = new HashMap<>();
		Map<Long, Long> gradecomposeIndicationIdcourseId = new HashMap<>();
		Map<Long, Long> gradecomposeIdMap = new HashMap<>();

		//TODO 一个课程一个老师下面有多个教学班这里会出现问题，取其中一个教学班id就可以统计所有的了，所以去除多余的
		//找出老师下面多个教学班其中的
		List<CcEduclass> educlassIds = CcEduclass.dao.findEduclassIds(courseIdList);
		List<Long> ccEduclassIdList1 = new ArrayList<>();
		for (CcEduclass temp:educlassIds){

			Long classId = temp.getLong("id");
			// 是否加过，没加过则加进去（应该是每次都是唯一，可以加的，但是为了排除万一）
			if(!ccEduclassIdList1.contains(classId)) {
				ccEduclassIdList1.add(classId);
			}
		}

		// 3. 找到个个教学班下，总分有多少，（平均分有误，因为学生没有分数的，他无法计算）
		//TODO 批次成绩加上之后会出现不同的课程的满分不相同，所以这个统计需要改动
		//如果剔除学生的达成度不够，那么就是这个sql问题，因为统计时剔除的也统计里了。 目前没有发现问题。
		List<CcScoreStuIndigrade> ccScoreStuIndigrades = CcScoreStuIndigrade.dao.findAllClassGradeByEduclassIds(ccEduclassIdList1);
		//List<CcScoreStuIndigrade> ccScoreStuIndigrades = CcScoreStuIndigrade.dao.findCourseGradecomposeIdsGrade(ccEduclassIdList,null,null);

		for(CcScoreStuIndigrade tempCcScoreStuIndigrade : ccScoreStuIndigrades) {

			Long gradecomposeIndicationId = tempCcScoreStuIndigrade.getLong("gradecomposeIndicationId");
			BigDecimal weight = tempCcScoreStuIndigrade.getBigDecimal("weight");
			//成绩总分
			BigDecimal totalGrade = tempCcScoreStuIndigrade.getBigDecimal("totalGrade");
			//满分
			//BigDecimal maxScore = tempCcScoreStuIndigrade.getBigDecimal("maxScore");
			gradecomposeIndicationIdTotalScore.put(gradecomposeIndicationId, totalGrade);
			//gradecomposeIndicationIdMaxScore.put(gradecomposeIndicationId, maxScore);
			gradecomposeIndicationIdWeight.put(gradecomposeIndicationId, weight);
			gradecomposeIndicationIdcourseId.put(gradecomposeIndicationId,tempCcScoreStuIndigrade.getLong("course_id"));
			gradecomposeIdMap.put(gradecomposeIndicationId,tempCcScoreStuIndigrade.getLong("gradecompose_id"));
		}
		// 课程下包含了那些 gradecomposeIndicationId。 通过课程目标关联过去
		List<CcIndication> ccIndicationList = CcIndication.dao.findEachIndicationCourseAndGradecomposeIndicationByCourseIds(courseIdList);
		Map<Long, CcIndication> ccIndicationMap = new HashMap<>();
		// <courseId, Map<indication_course_id, Map<indicationId, List<gradecomposeIndicationId>>>>
		//courseIndicationCourseIdIndicationIdGradecomposeIndicationIdMap主要是用于课程与课程目标关联
		Map<Long, Map<Long,  Map<Long, List<Long>>>> courseIndicationCourseIdIndicationIdGradecomposeIndicationIdMap = new HashMap<>();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		for(CcIndication tempCcIndication : ccIndicationList) {
			Long courseId = tempCcIndication.getLong("course_id");
			Long indicationCourseId = tempCcIndication.getLong("indicationCourseId");
			Long indicationId = tempCcIndication.getLong("id");
			Long gradecomposeIndicationId = tempCcIndication.getLong("gradecomposeIndicationId");
			//indicationId课程目标id
			ccIndicationMap.put(indicationId, tempCcIndication);
			Map<Long,  Map<Long, List<Long>>> indicationCourseIdAndIndicationIdAndGradecomposeIndicationIdMap = courseIndicationCourseIdIndicationIdGradecomposeIndicationIdMap.get(courseId);
			if(indicationCourseIdAndIndicationIdAndGradecomposeIndicationIdMap == null || indicationCourseIdAndIndicationIdAndGradecomposeIndicationIdMap.isEmpty()) {
				indicationCourseIdAndIndicationIdAndGradecomposeIndicationIdMap = new HashMap<>();
				courseIndicationCourseIdIndicationIdGradecomposeIndicationIdMap.put(courseId, indicationCourseIdAndIndicationIdAndGradecomposeIndicationIdMap);
			}
			Map<Long, List<Long>> indicationIdAndGradecomposeIndicationIdMap = indicationCourseIdAndIndicationIdAndGradecomposeIndicationIdMap.get(indicationCourseId);
			if(indicationIdAndGradecomposeIndicationIdMap == null || indicationIdAndGradecomposeIndicationIdMap.isEmpty()) {
				indicationIdAndGradecomposeIndicationIdMap = new HashMap<>();
				indicationCourseIdAndIndicationIdAndGradecomposeIndicationIdMap.put(indicationCourseId, indicationIdAndGradecomposeIndicationIdMap);
			}
			List<Long> gradecomposeIndicationIdList = indicationIdAndGradecomposeIndicationIdMap.get(indicationId);
			if(gradecomposeIndicationIdList == null || gradecomposeIndicationIdList.isEmpty()) {
				gradecomposeIndicationIdList = new ArrayList<>();
				indicationIdAndGradecomposeIndicationIdMap.put(indicationId, gradecomposeIndicationIdList);
			}
			gradecomposeIndicationIdList.add(gradecomposeIndicationId);
		}
		Boolean resultResult = Boolean.TRUE;
		List<CcReportCourse> saveCcReportCourseList = new ArrayList<>();
		// 获取老的数据，用于保存原有记录
		List<CcReportCourse> oldCcReportCourseList = CcReportCourse.dao.findAllByGradeAndVersion(grade, versionId);
		// Map<"grade,indicationCourseId", CcReportCourse>
		Map<String, CcReportCourse> oldCcReportCourseMap = new HashMap<>();
		for(CcReportCourse temp : oldCcReportCourseList) {
			Long indicationCourseId = temp.getLong("indication_course_id");
			String key = grade + "," + indicationCourseId;
			oldCcReportCourseMap.put(key, temp);
		}
		List<Long> indicationCourseIds = new ArrayList<>();
		// 准备指标点达成度数据，需要考虑专业方向
		Map<String, BigDecimal> majorReportResultMap = Maps.newLinkedHashMap();
		Map<String, Map<Long, BigDecimal>> courseGroupResultMap = Maps.newHashMap();
		// 准备一份map，用于数据获取
		List<CcIndicationCourse> ccIndicationCourses = CcIndicationCourse.dao.findByVersionId(versionId,1);
		Map<Long, CcIndicationCourse> indicationCourseMap = new HashMap<>();
		for(CcIndicationCourse temp : ccIndicationCourses) {
			Long indicationCourseId = temp.getLong("id");
			indicationCourseMap.put(indicationCourseId, temp);
		}
		Date date = new Date();
		// 遍历整个课程，设置数据
		for(Long courseId : courseIdList) {
			CcTeacherCourse courseResultType = CcTeacherCourse.dao.findCourseResultType(courseId);
			//通过课程id查找开课课程的达成度计算方式，因为一个课程可能有多个计算方式，我目前用第一个开课老师计算方式计算这个课程的达成度
			Integer resultType = courseResultType.getInt("result_type");
			//开课id
			Long teacherCourseId = courseResultType.getLong("id");
			BigDecimal studentNum = new BigDecimal(courseStudentMap.get(courseId) == null ? 0 : courseStudentMap.get(courseId));
			if (!PriceUtils.greaterThan(studentNum,new BigDecimal("0"))){
				continue;
			}
			Map<Long,  Map<Long, List<Long>>> indicationCourseIdAndIndicationIdAndGradecomposeIndicationIdMap = courseIndicationCourseIdIndicationIdGradecomposeIndicationIdMap.get(courseId);
			// 没加过指标点
			if(indicationCourseIdAndIndicationIdAndGradecomposeIndicationIdMap == null) {
				continue;
			}
			// 遍历指标点
			for(Entry<Long, Map<Long, List<Long>>> indicationCourseIdAndIndicationIdAndGradecomposeIndicationIdEntry : indicationCourseIdAndIndicationIdAndGradecomposeIndicationIdMap.entrySet()) {
				Long indicationCourseId = indicationCourseIdAndIndicationIdAndGradecomposeIndicationIdEntry.getKey();
				Map<Long, List<Long>> indicationIdAndgradecomposeIndicationIdMap = indicationCourseIdAndIndicationIdAndGradecomposeIndicationIdEntry.getValue();
				BigDecimal result = new BigDecimal(0);
				// 遍历课程目标
				for(Entry<Long, List<Long>> indicationIdAndGradecomposeIndicationIdEntry : indicationIdAndgradecomposeIndicationIdMap.entrySet()) {

					BigDecimal resultTemp = new BigDecimal(0);
					BigDecimal mom = new BigDecimal(0);
					BigDecimal son = new BigDecimal(0);
					BigDecimal A = new BigDecimal(0);
					// 课程目标
					Long indicationId = indicationIdAndGradecomposeIndicationIdEntry.getKey();
					List<Long> gradecomposeIndicationIdList = indicationIdAndGradecomposeIndicationIdEntry.getValue();
					//所有成绩组成这个课程目标的总权重 如果一个课程不同老师的权重是不同的那这里就需要改动。因为我这里是按照每个开课老师对这个课程的权重设置都是一样的
					BigDecimal allWeight = CcCourseGradecomposeIndication.dao.calculateSumWeights(teacherCourseId, indicationId);
					// 遍历每个 课程目标的成绩组成
					for(Long gradecomposeIndicatoinId : gradecomposeIndicationIdList) {
						BigDecimal totalGrade = gradecomposeIndicationIdTotalScore.get(gradecomposeIndicatoinId);
						BigDecimal maxScore = gradecomposeIndicationIdMaxScore.get(gradecomposeIndicatoinId);
						// 当不存在数据的时候
						if(totalGrade == null) {
							continue;
						}
						//权重
						BigDecimal weight = gradecomposeIndicationIdWeight.get(gradecomposeIndicatoinId);
						//平均分
						BigDecimal avgGrade = totalGrade.divide(studentNum, 5, RoundingMode.HALF_UP);
						//平均分*权重
						mom = mom.add(avgGrade.multiply(weight));

						//TODO 2020/09/07 一个课程多个教学班满分计算方式改为(A班学生数*A班的CO1的满分+B班学生数*B班的CO1的满分)/总学生数，
						// 权重应该是相同的，不然这个还要改

						Long courseIds = gradecomposeIndicationIdcourseId.get(gradecomposeIndicatoinId);
						//成绩组成id
						Long gradecomposeId = gradecomposeIdMap.get(gradecomposeIndicatoinId);
						//(A班学生数*A班的CO1的满分+B班学生数*B班的CO1的满分)
						BigDecimal maxScores = new BigDecimal("0");
						//统计每个教学班的学生个数
						List<CcEduclassStudent> ccEduclassStudents = CcEduclassStudent.dao.countStudentNums(courseIds);
						for (CcEduclassStudent educlassStudent : ccEduclassStudents){
							Long edClassId = educlassStudent.getLong("id");

							Object studentNums = educlassStudent.get("studentNum");
							if (studentNums==null || studentNums.equals("")){
								continue;
							}
							int classStudentNum = Integer.parseInt(educlassStudent.get("studentNum")+"");
							BigDecimal studentNumBig = new BigDecimal(classStudentNum);

							CcCourseGradecomposeIndication maxscoreAndWeight = CcCourseGradecomposeIndication.dao.findGradecomposeMaxscoreAndWeight(edClassId, indicationId, gradecomposeId);
							if (maxscoreAndWeight==null){
								continue;
							}
							//(A班学生数*A班的CO1的满分+B班学生数*B班的CO1的满分)
							BigDecimal indicationMaxScore = maxscoreAndWeight.getBigDecimal("max_score");
							maxScores=maxScores.add(indicationMaxScore.multiply(studentNumBig));

						}
						//平均分满分
						BigDecimal avgMaxScore=maxScores.divide(studentNum,5);
						//浙江科技学院的达成度算法
						if (resultType.equals(CcTeacherCourse.RESULT_TYPE_SCORE)){
							/*
							 *  注意：
							 *  这里如果一个教师课程，有两个教学班，计算课程达成度报表数据的时候，直接把每个学生加起来，除以总学生数。
							 *  而不是两个教学班的报表加一下除以二。
							 *  所以不能通过cc_edupoint_aims_achieve的数据处理
							 *  要重新计算所有学生的总分 和 总人数，算出平均分，然后处理 （即：【课程下的教学班们】 交集 ）
							 *  result1 = mom1/(son1)
							 *  result2 = mom2/(son2)
							 *  result3 = mom3/(son3)
							 *  result  = 指标点权重 * (result1 , result2, result3)min
							 *  这里的算法没有改
							 *  指标点达成度应该为 课程目标大达成度的最小值*指标点权重 也就是result
							 *  result1、result2、result3 应该为课程达成度
							 *  课程达成度计算公式(平均分1*权重1+平均分2*权重2+平均分3*权重3……)/(总分1*权重1+总分2*权重2+总分3*权重3……)
							 *  (平均分1*权重1+平均分2*权重2+平均分3*权重3……)相当于mom
							 *  总分1*权重1+总分2*权重2+总分3*权重3……)相当于son
							 *  下面说的满分即使总分
							 */

							//平均分*权重
							mom = mom.add(avgGrade.multiply(weight));


							//(A班学生数*A班的CO1的满分+B班学生数*B班的CO1的满分)/总人数*权重
							son = son.add(maxScores.divide(studentNum,2).multiply(weight));
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
							 * mon=平均分S1/S1总分
							 * son=成绩CO1S1的权重/CO1权重的总和
							 *  A=mom*son
							 */
							mom=avgGrade.divide(avgMaxScore,5);
							son=weight.divide(allWeight,5);
							A = A.add(mom.multiply(son));

						}

					}
					CcIndication ccIndication = ccIndicationMap.get(indicationId);
					if(ccIndication == null) {
						continue;
					}
					BigDecimal expectedValue = ccIndication.getBigDecimal("expected_value");
					if(son.compareTo(new BigDecimal(0)) == 0) {
						continue;
					}
					if (resultType.equals(CcTeacherCourse.RESULT_TYPE_SCORE)){
						//课程目标达成度算法(平均分1*权重1+平均分2*权重2+平均分3*权重3……)/(总分1*权重1+总分2*权重2+总分3*权重3……) mom/son
						resultTemp = mom.divide(son, 5, RoundingMode.HALF_UP);
					}else{
						//财经大学  A=mom*son  A=（平均分S1/S1总分）*（成绩CO1S1的权重/CO1权重的总和）+（平均分S2/S2总分）*（成绩CO1S2的权重/CO1权重的总和）+....
						resultTemp = A;
					}
					//期望值
					//resultTemp = resultTemp.divide(expectedValue, 5, RoundingMode.HALF_UP);
					// 如果前者更小，用前者
					result = result.compareTo(new BigDecimal(0)) == 0 ? resultTemp : result.compareTo(resultTemp) == -1 ? result : resultTemp;
				}
				CcIndicationCourse ccIndicationCourse = indicationCourseMap.get(indicationCourseId);
				if(ccIndicationCourse == null) {
					continue;
				}
				BigDecimal weight = ccIndicationCourse.getBigDecimal("weight");
				//这个应该是指标点达成度吧
				result = result.multiply(weight);

				String key = grade + "," + indicationCourseId;
				CcReportCourse oldCcReportCourse = oldCcReportCourseMap.get(key);

				CcReportCourse ccReportCourse = new CcReportCourse();
				ccReportCourse.set("grade", grade);
				ccReportCourse.set("indication_course_id", indicationCourseId);
				if(oldCcReportCourse != null) {
					BigDecimal result1 = oldCcReportCourse.getBigDecimal("result");

					if (result1 != null){
						ccReportCourse.set("result", PriceUtils.currency(oldCcReportCourse.getBigDecimal("result")));
					}
				}
				ccReportCourse.set("except_result", PriceUtils.currency(result));
				ccReportCourse.set("is_del", CcReportCourse.DEL_NO);
				ccReportCourse.set("statistics_date", date);
				ccReportCourse.set("create_date", date);
				ccReportCourse.set("modify_date", date);
				ccReportCourse.set("id", idGenerate.getNextValue());
				saveCcReportCourseList.add(ccReportCourse);

				// 记录课程指标点编号
				indicationCourseIds.add(indicationCourseId);

				// 记录专业及其每个方向上的指标点达成度
				Long majorDirectionId = ccIndicationCourse.getLong("direction_id");
				Long courseGroupId = ccIndicationCourse.getLong("course_group_id");
				Long teachGroupId = ccIndicationCourse.getLong("teach_group_id");
				// 无专业方向，赋予专业方向编号无专业方向编号
				if (majorDirectionId == null) {
					majorDirectionId = CcReportMajor.NO_MAJORDIRECTION_ID;
				}

				Long indicatorPointId = ccIndicationCourse.getLong("indication_id");
				// 指标点编号,专业方向编号
				String indicationMajorResultKey = indicatorPointId + "," + majorDirectionId;

				if (majorReportResultMap.get(indicationMajorResultKey) == null) {
					majorReportResultMap.put(indicationMajorResultKey, new BigDecimal(0));
				}


				//多选一编号 分级教学编号  先剔除分级教学的课程达成度 在下面统计专业指标点达成度再加上 2020/09/09
				if (teachGroupId == null){
					if (courseGroupId == null ) {
						majorReportResultMap.put(indicationMajorResultKey
								, PriceUtils._add(PriceUtils.currency(majorReportResultMap.get(indicationMajorResultKey))
										, PriceUtils.currency(result)));

					} else {

						Map<Long, BigDecimal> courseGroupResult = null;
						if (courseGroupResultMap.get(indicationMajorResultKey) == null) {
							courseGroupResult = Maps.newHashMap();
							courseGroupResultMap.put(indicationMajorResultKey, courseGroupResult);
						} else {
							courseGroupResult = courseGroupResultMap.get(indicationMajorResultKey);
						}
						BigDecimal minScore = courseGroupResult.get(courseGroupId);
						// 当课程组中该门课成绩更小或是之前课程组未有成绩时，课程组成绩为该门课成绩
						if (minScore == null || PriceUtils.greaterThan(minScore, result)) {
							minScore = result;
						}
						courseGroupResult.put(courseGroupId, minScore);

					}
				}
			}
		}

		// 删除之前旧课程达成度记录
		if (!indicationCourseIds.isEmpty() && !CcReportCourse.dao.deleteAllNoDel(grade, indicationCourseIds)) {
			logger.error(new StringBuilder("达成度统计：课程达成度统计表(").append(CcReportCourse.dao.tableName).append(")")
					.append("批量删除旧的统计记录失败, 删除的年级为").append(grade.toString()).append(", 删除的课程指标点编号包括(")
					.append(indicationCourseIds.toString()).append(")")
					.toString());

			return false;
		}

		// 保存新的课程达成度记录
		if (!saveCcReportCourseList.isEmpty() && !CcReportCourse.dao.batchSave(saveCcReportCourseList)) {
			logger.error(new StringBuilder("达成度统计：课程达成度统计表(").append(CcReportCourse.dao.tableName).append(")")
					.append("批量增加新的统计记录失败，新增的记录包括(").append(saveCcReportCourseList.toString()).append(")")
					.toString());

			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		logger.info("达成度统计：课程达成度统计完成，准备进行指标点达成度统计");

		// 指标点达成度的数据汇总与保存
		List<CcReportMajor> ccReportMajors = Lists.newArrayList();
		// 获取老的数据，用于保存原有记录
		List<CcReportMajor> oldCcReportMajorList = CcReportMajor.dao.findAllByGradeAndVersion(grade, versionId);
		// Map<"grade,indication_id", CcReportMajor>
		Map<String, CcReportMajor> oldCcReportMajorMap = new HashMap<>();
		for(CcReportMajor temp : oldCcReportMajorList) {
			Long indicationId = temp.getLong("indication_id");
			String key = grade + "," + indicationId;
			oldCcReportMajorMap.put(key, temp);
		}
		List<Long> indicationIds = Lists.newArrayList();
		for (Map.Entry<String, BigDecimal> entry : majorReportResultMap.entrySet()) {
			String[] keySplit = entry.getKey().split(",");
			// 指标点编号
			Long indicationId = Long.valueOf(keySplit[0]);
			// 专业方向编号
			Long majorDirectionId = Long.valueOf(keySplit[1]);
			majorDirectionId = CcReportMajor.NO_MAJORDIRECTION_ID.equals(majorDirectionId) ? null : majorDirectionId;
			// 指标点得分
			BigDecimal indicationScore = PriceUtils.currency(entry.getValue());
			String noMajorDirectionIndicationKey = indicationId + "," + CcReportMajor.NO_MAJORDIRECTION_ID;
			// 当专业认证处于方向上时，需加上方向上的达成度
			if (majorDirectionId != null && majorReportResultMap.get(noMajorDirectionIndicationKey) != null) {
				indicationScore = PriceUtils._add(indicationScore,PriceUtils.currency( majorReportResultMap.get(noMajorDirectionIndicationKey)));
			}

			// 无方向
			Map<Long, BigDecimal> courseGroupScoreMapTemp = null;
			Map<Long, BigDecimal> courseGroupScoreMap = courseGroupResultMap.get(entry.getKey());
			if(majorDirectionId == null){
				courseGroupScoreMapTemp = courseGroupScoreMap;
			}else{
				//有方向
				Map<Long, BigDecimal> noDirectionCourseGroupScoreMap = courseGroupResultMap.get(noMajorDirectionIndicationKey);
				if(noDirectionCourseGroupScoreMap == null){
					noDirectionCourseGroupScoreMap = Maps.newHashMap();
				}
				if(courseGroupScoreMap != null){
					for (Map.Entry<Long, BigDecimal> courseGroupScoreEntry : courseGroupScoreMap.entrySet()) {
						BigDecimal dirValue = courseGroupScoreEntry.getValue();
						Long dirKey = courseGroupScoreEntry.getKey();
						if (dirValue != null ) {
							BigDecimal noDirValue = noDirectionCourseGroupScoreMap.get(dirKey);
							if(noDirValue == null){
								noDirectionCourseGroupScoreMap.put(dirKey, dirValue);
							}else{
								noDirectionCourseGroupScoreMap.put(dirKey, PriceUtils.lessThan(dirValue, noDirValue) ? dirValue : noDirValue);
							}
						}
					}
				}
				courseGroupScoreMapTemp = noDirectionCourseGroupScoreMap;
			}
			if (courseGroupScoreMapTemp != null) {
				for (Map.Entry<Long, BigDecimal> courseGroupScoreEntry : courseGroupScoreMapTemp.entrySet()) {
					if (courseGroupScoreEntry.getValue() != null ) {
						indicationScore = PriceUtils._add(indicationScore, courseGroupScoreEntry.getValue());
					}
				}
			}

			String key = grade + "," + indicationId;
			CcReportMajor oldCcReportMajor = oldCcReportMajorMap.get(key);
			BigDecimal maxResult = new BigDecimal(1);
			CcReportMajor ccReportMajor = new CcReportMajor();
			ccReportMajor.set("indication_id", indicationId);
			ccReportMajor.set("grade", grade);
			ccReportMajor.set("major_direction_id", majorDirectionId);

			//TODO 2020/09/09因为分级教学 只要课程分组中合计最小的值加入
			BigDecimal minTeacherResult = new BigDecimal("0");
			List<CcIndicationCourse> teachIndicationCourses = CcIndicationCourse.dao.sumTeachGroupResult(versionId, indicationId, null);
			for (CcIndicationCourse temp: teachIndicationCourses){
				BigDecimal teachMaxResult = temp.getBigDecimal("teachMaxResult");
				if (teachMaxResult !=null){
					minTeacherResult=minTeacherResult.add(teachMaxResult);
				}

				System.out.println(teachMaxResult);

			}

			if(oldCcReportMajor != null) {
				BigDecimal subResult = oldCcReportMajor.getBigDecimal("result").add(minTeacherResult);
				//如果值超过1就设置为1
				if(PriceUtils.greaterThan(PriceUtils.currency(subResult),maxResult))
				{
					ccReportMajor.set("result", PriceUtils.currency(maxResult));
				}else{
					ccReportMajor.set("result", PriceUtils.currency(subResult));
				}


			}
			indicationScore=indicationScore.add(minTeacherResult);
			//如果值超过1就设置为1
			if (PriceUtils.greaterThan(indicationScore,maxResult)){
				ccReportMajor.set("except_result", maxResult);
			}else {
				ccReportMajor.set("except_result", indicationScore);
			}

			ccReportMajor.set("is_del", CcReportMajor.DEL_NO);
			ccReportMajor.set("statistics_date", date);
			ccReportMajor.set("create_date", date);
			ccReportMajor.set("modify_date", date);
			ccReportMajor.set("id", idGenerate.getNextValue());
			ccReportMajors.add(ccReportMajor);
			indicationIds.add(indicationId);
		}

		// 删除旧的指标点达成度的记录
		if (!indicationIds.isEmpty() && !CcReportMajor.dao.deleteAllNoDel(grade, indicationIds)) {
			logger.error(new StringBuilder("达成度统计：指标点达成度统计表(").append(CcReportMajor.dao.tableName).append(")")
					.append("批量删除旧的统计记录失败, 删除的年级为").append(grade.toString()).append(", 删除的指标点编号包括(")
					.append(indicationCourseIds.toString()).append(")")
					.toString());

			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		// 保存新的指标点达成度的记录
		if (!ccReportMajors.isEmpty() && !CcReportMajor.dao.batchSave(ccReportMajors)) {
			logger.error(new StringBuilder("达成度统计：指标点达成度统计表(").append(CcReportCourse.dao.tableName).append(")")
					.append("批量增加新的统计记录失败，新增的记录包括(").append(ccReportMajors.toString()).append(")")
					.toString());

			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		logger.info("达成度统计：指标点达成度统计完成");
		return resultResult;
	}
	/**
	 * 统计教学班的报表，生成课程达成度报表数据
	 * 注意：这里如果一个教师课程，有两个教学班，计算课程达成度报表数据的时候，直接把两个教学班的报表加一下除以二。而不是每个学生加起来，除以总学生数
	 * @param grade
	 * @param versionId
	 * @return -----------(这个方法好像没有其他地方用到)
	 */
	@Transactional(readOnly = false)
	@Deprecated
	public boolean statisticsCourseResult(Integer grade, Long versionId) {
		/*
		 *  这个方法的代码，动一下就出错，而且还搞不清数据是什么，需求是什么。动手修改需要大量时间。
		 *  对此，之前的人在这里浪费了总共时间：27h。
		 *  每当有人来改，请更新这个时间
		 */
		List<CcReportEduclassGrade> ccReportEduclassGrades = CcReportEduclassGrade.dao.getByVersionAndGrade(versionId, grade);
		List<CcReportEduclassEvalute> ccReportEduclassEvalutes = CcReportEduclassEvalute.dao.getByVersionAndGrade(versionId, grade);

		// 课程编号的课程组成与考评点的map
		List<Long> courseIds = Lists.newArrayList();
		Map<Long, List<Record>> gradeAndEvaluteRecords = Maps.newHashMap();
		for (CcReportEduclassGrade ccReportEduclassGrade : ccReportEduclassGrades) {
			Long courseId = ccReportEduclassGrade.getLong("course_id");
			if (gradeAndEvaluteRecords.get(courseId) == null) {
				courseIds.add(courseId);
				gradeAndEvaluteRecords.put(courseId, new ArrayList<Record>());
			}
			Record record = new Record();
			record.set("educlass_id", ccReportEduclassGrade.getLong("educlass_id"));
			record.set("indication_id", ccReportEduclassGrade.getLong("indication_id"));
			record.set("teacher_course_id", ccReportEduclassGrade.getLong("teacher_course_id"));
			record.set("weight", ccReportEduclassGrade.getBigDecimal("weight"));
			record.set("max_score", ccReportEduclassGrade.getBigDecimal("max_score"));
			record.set("result", ccReportEduclassGrade.getBigDecimal("result"));
			record.set("result_type", ccReportEduclassGrade.getInt("result_type"));
//			// 这里是可能要改的。先写了，注释掉，看结果。 FIXME SY
//			record.set("percentage", ccReportEduclassGrade.getInt("percentage"));
			gradeAndEvaluteRecords.get(courseId).add(record);
		}

		for (CcReportEduclassEvalute ccReportEduclassEvalute : ccReportEduclassEvalutes) {
			Long courseId = ccReportEduclassEvalute.getLong("course_id");
			if (gradeAndEvaluteRecords.get(courseId) == null) {
				courseIds.add(courseId);
				gradeAndEvaluteRecords.put(courseId, new ArrayList<Record>());
			}
			Record record = new Record();
			record.set("educlass_id", ccReportEduclassEvalute.getLong("educlass_id"));
			record.set("indication_id", ccReportEduclassEvalute.getLong("indication_id"));
			record.set("teacher_course_id", ccReportEduclassEvalute.getLong("teacher_course_id"));
			record.set("weight", ccReportEduclassEvalute.getBigDecimal("weight"));
			record.set("result", ccReportEduclassEvalute.getBigDecimal("result"));
			record.set("percentage", ccReportEduclassEvalute.getInt("percentage"));
			record.set("result_type", ccReportEduclassEvalute.getInt("result_type"));
			record.set("evalute_type", ccReportEduclassEvalute.getInt("evalute_type"));
			gradeAndEvaluteRecords.get(courseId).add(record);
		}

		// 课程-教师课程map
		Map<Long, Map<Long, Map<Long, BigDecimal>>> courseTeacherCourseMap = Maps.newHashMap();
		// 某课程指标点的考评点类型占比之和 key: teacherCourseId,indication.    value:BigDecimal
		Map<String, BigDecimal> oneTeacherCoursePercentageBigMap = Maps.newHashMap();
		// 用于计算是否已经加过 key ： teacherCourseId,indication,type
		Map<String, Boolean> oneTeacherCoursePercentageBigIsAddMap = Maps.newHashMap();
		// 用于减少计算量，记录一下，指标点，对应有的课程，是那些。<indication, List<courseId>>
		Map<Long, List<Long>> indicationCourseIdList = Maps.newHashMap();
		for (Long courseId : courseIds) {
			// 教师课程指标点map
			Map<Long, Map<Long, BigDecimal>> teacherCourseIndicationMap = Maps.newHashMap();
			courseTeacherCourseMap.put(courseId, teacherCourseIndicationMap);
			// 遍历该课程中考核分析法的班级
			if (gradeAndEvaluteRecords.get(courseId) != null) {
				Long prevEduClassId = gradeAndEvaluteRecords.get(courseId).get(0).getLong("educlass_id");
				Long prevIndicationId = gradeAndEvaluteRecords.get(courseId).get(0).getLong("indication_id");

				// 只有考评表分析法才有用。 考评点类型
				Integer preEvaluteType = gradeAndEvaluteRecords.get(courseId).get(0).getInt("evalute_type");
				BigDecimal evaluteTypeAllWeight = new BigDecimal(0);
				// 【指标点达成度算法】分数【2】~~【3.2】的数值
				BigDecimal score = new BigDecimal(0);
				// 【2】的分数
				// 一个考点类型的单个考评点的计算
				BigDecimal eachEvaluteTypeScore = new BigDecimal(0);
//				Long eduClassSize = 1L;
				// 记录下当前课程下某个教师的课程，有几个教学班
				Map<Long, Integer> teacherCourseEduClassNumMap = Maps.newHashMap();
				BigDecimal percentageBig = new BigDecimal(0);
				// 用于记录下最后一次他的数值
				Long teacherCourseId = null;
				// 用于记录下上一次他的数值
				Long preTeacherCourseId = null;
				// 用于记录下最后一次对应教师课程对应指标点的评价值
				Map<Long, BigDecimal> indicationMap = Maps.newHashMap();
				// 用于记录下最后一个类型
				Integer evaluteType = null;
				// 上一次ResultType
				Integer preResultType = null;

				// 遍历考评点
				for (Record record : gradeAndEvaluteRecords.get(courseId)) {
					Long eduClassId = record.getLong("educlass_id");
					Long indicationId = record.getLong("indication_id");

					// 记录一下当前指标点存在于此课程
					List<Long> courseIdList = indicationCourseIdList.get(indicationId);
					if(courseIdList == null || courseIdList.isEmpty()) {
						courseIdList = new ArrayList<>();
					}
					courseIdList.remove(courseId);
					courseIdList.add(courseId);
					indicationCourseIdList.put(indicationId, courseIdList);

					// 当前考评点的考评点类型的占比
					Integer percentage = record.getInt("percentage");
					evaluteType = record.getInt("evalute_type");
					Integer resultType = record.getInt("result_type");
					// 当课程发现是不同的时候，关闭percentage增加的入口，发现是一样的，则是开启状态。
					teacherCourseId = record.getLong("teacher_course_id");
					if(preTeacherCourseId == null) {
						preTeacherCourseId = teacherCourseId;
					}
					// 初始化指标点Map
//					indicationMap = teacherCourseIndicationMap.get(teacherCourseId);
					indicationMap = teacherCourseIndicationMap.get(preTeacherCourseId);
					// 需要考虑到一个课程多个教师，每个教师多门课程的计算。由于每个教师课程的同一个指标点，构成不同，所以分开计算
					if(indicationMap == null) {
						indicationMap = Maps.newHashMap();
					}
//					teacherCourseIndicationMap.put(teacherCourseId, indicationMap);
					teacherCourseIndicationMap.put(preTeacherCourseId, indicationMap);

					// 指标点发生改变，直接存入评价值结果
					if (!indicationId.equals(prevIndicationId)) {
						// 考虑到，当切换指标点的时候，如果考评点类型未发生改变，还是要计算，并且清空一下计数数据
						if (CcTeacherCourse.RESULT_TYPE_EVALUATE.equals(resultType) && !CcTeacherCourse.RESULT_TYPE_SCORE.equals(preResultType)) {
							// 计算
							// 【3.2】
							eachEvaluteTypeScore = eachEvaluteTypeScore.divide(evaluteTypeAllWeight, 4, RoundingMode.HALF_UP);
							score = PriceUtils._add(score, eachEvaluteTypeScore);

							// 考评点类型比例系数和清空
							evaluteTypeAllWeight = new BigDecimal(0);
							preEvaluteType = evaluteType;
							eachEvaluteTypeScore = new BigDecimal(0);
						}

						Integer num = teacherCourseEduClassNumMap.get(preTeacherCourseId);
						num = num == null ? 1 : num;
						if(num > 1) {
							// 如果这不是第一个教学班，则找到以前的，然后再来计算
							BigDecimal oldScore = indicationMap.get(prevIndicationId) == null ? new BigDecimal(0) : indicationMap.get(prevIndicationId);
							oldScore = oldScore.multiply(new BigDecimal(num - 1));
							score = PriceUtils._add(oldScore, score);
						}
						// 计算当前指标点的评价值
						indicationMap.put(prevIndicationId, score.divide(BigDecimal.valueOf(num), 3, RoundingMode.HALF_UP));
						score = new BigDecimal(0);
						prevIndicationId = indicationId;
					} else if (CcTeacherCourse.RESULT_TYPE_EVALUATE.equals(resultType) && !CcTeacherCourse.RESULT_TYPE_SCORE.equals(preResultType)  && evaluteType != preEvaluteType) {
//						// 不换指标点，不需要计算 221 cert 2018年1月16日09:48:39---2018年1月19日18:16:16
//						// 如果考评点类型发生改变，则需要除以考评点类型的比例系数之和【3.2】
//						// 计算
//						// 【3.2】
//						eachEvaluteTypeScore = eachEvaluteTypeScore.divide(evaluteTypeAllWeight, 4, RoundingMode.HALF_UP);
//						score = PriceUtils._add(score, eachEvaluteTypeScore);
//
//						// 考评点类型比例系数和清空
//						evaluteTypeAllWeight = new BigDecimal(0);
//						preEvaluteType = evaluteType;
//						eachEvaluteTypeScore = new BigDecimal(0);
					}

					// 本班级的本指标点还在继续计算中
					if (CcTeacherCourse.RESULT_TYPE_SCORE.equals(resultType)) {
						score = PriceUtils._add(score, PriceUtils._mul(record.getBigDecimal("result").divide(PriceUtils._mul(record.getBigDecimal("max_score"), educlassIndicationWeightMap(versionId, grade).get(eduClassId).get(indicationId)), 4, RoundingMode.HALF_UP), record.getBigDecimal("weight")));
					} else if (CcTeacherCourse.RESULT_TYPE_EVALUATE.equals(resultType)) {

						// Edit By SY Date:2017-9-7
						// 某一个考评点分数乘以当前考评点的比例系数(以前数据库叫做权重)。查看ctrl + f ：【指标点达成度算法】的【2.】
						BigDecimal weight = record.getBigDecimal("weight");
						BigDecimal evaluteScore = PriceUtils._mul(record.getBigDecimal("result"), weight);
						// 再乘以考评点类型的占比【3.1】
						percentageBig = new BigDecimal(percentage == null ? 0 : percentage).divide(new BigDecimal(100));
						evaluteScore = PriceUtils._mul(evaluteScore, percentageBig);

						eachEvaluteTypeScore = PriceUtils._add(eachEvaluteTypeScore, evaluteScore);
						// 该考评点类型 的 比例系数之和 加上这一次的
						evaluteTypeAllWeight = evaluteTypeAllWeight.add(weight);

						// 计算一下考评点类型的占比集合
						String teacherCourseKey = teacherCourseId+ "," + indicationId;
						String teacherCourseIsAddKey = teacherCourseId+ "," + indicationId + "," + evaluteType;
						BigDecimal oneIndicationPercentageBig = oneTeacherCoursePercentageBigMap.get(teacherCourseKey);
						Boolean oneIndicationPercentageBigIsAdd = oneTeacherCoursePercentageBigIsAddMap.get(teacherCourseIsAddKey);
						if(oneIndicationPercentageBigIsAdd == null || !oneIndicationPercentageBigIsAdd) {
							oneIndicationPercentageBig = oneIndicationPercentageBig == null ? new BigDecimal(0) : oneIndicationPercentageBig;
							oneIndicationPercentageBig = oneIndicationPercentageBig.add(percentageBig);
							oneTeacherCoursePercentageBigMap.put(teacherCourseKey, oneIndicationPercentageBig);
							oneTeacherCoursePercentageBigIsAddMap.put(teacherCourseIsAddKey, Boolean.TRUE);
						}
						preEvaluteType = evaluteType;
					} else {
						logger.warn(new StringBuilder("达成度分析课程达成度统计分析法类型为")
								.append(record.getInt("result_type") != null ? "空" : record.getInt("result_type"))
								.append(",无法解析为具体分析法类型")
								.toString());

						return false;
					}

					// 记录下上一次的类型是什么。
					preResultType = resultType;
					// 若班级编号发生改变加1
					if (!prevEduClassId.equals(eduClassId)) {
//						eduClassSize ++;
						prevEduClassId = eduClassId;
						Integer num = teacherCourseEduClassNumMap.get(preTeacherCourseId);
						num = num == null ? 0 : num;
						num++;
						// 计数增加
						teacherCourseEduClassNumMap.put(preTeacherCourseId, num);
					}
					// 记录下上一次的教师课程是什么。
					preTeacherCourseId = teacherCourseId;
				}

				// 加入最后一个
				Integer num = teacherCourseEduClassNumMap.get(preTeacherCourseId);
				num = num == null ? 0 : num;
				num++;
				if(num > 1) {
					// 如果这不是第一个教学班，则找到以前的，然后再来计算
					BigDecimal oldScore = indicationMap.get(prevIndicationId) == null ? new BigDecimal(0) : indicationMap.get(prevIndicationId);
					oldScore = oldScore.multiply(new BigDecimal(num - 1));
					score = PriceUtils._add(oldScore, score);
				}

				// 如果考评点类型发生改变，则需要除以考评点类型的比例系数之和【3.2】
				if (CcTeacherCourse.RESULT_TYPE_EVALUATE.equals(gradeAndEvaluteRecords.get(courseId).get(gradeAndEvaluteRecords.get(courseId).size() - 1).getInt("result_type"))) {
					// 计算
					// 【3.2】
					eachEvaluteTypeScore = eachEvaluteTypeScore.divide(evaluteTypeAllWeight, 4, BigDecimal.ROUND_UP);
					score = PriceUtils._add(score, eachEvaluteTypeScore);

					// 考评点类型比例系数和清空
					evaluteTypeAllWeight = new BigDecimal(0);
					eachEvaluteTypeScore = new BigDecimal(0);
				}
				indicationMap.put(prevIndicationId, score.divide(BigDecimal.valueOf(num), 4, RoundingMode.HALF_UP));
			}
		}

		List<CcIndicationCourse> ccIndicationCourses = CcIndicationCourse.dao.findByVersionId(versionId,1);

		// 保存课程达成度记录
		Date date = new Date();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		List<CcReportCourse> ccReportCourses = Lists.newArrayList();
		List<Long> indicationCourseIds = Lists.newArrayList();
		// 准备指标点达成度数据，需要考虑专业方向
		Map<String, BigDecimal> majorReportResultMap = Maps.newLinkedHashMap();
		Map<String, Map<Long, BigDecimal>> courseGroupResultMap = Maps.newHashMap();
		for (CcIndicationCourse ccIndicationCourse : ccIndicationCourses) {
			Long indicationId = ccIndicationCourse.getLong("indication_id");
			Long courseId = ccIndicationCourse.getLong("course_id");
			// 平均所有课程之后，计算该课程该指标点平均
			BigDecimal result = new BigDecimal(0);
			// 有多少教师门课被计算了，用于充当求平均值的除数。
			Integer teacherCourseNum = 0;

			for(Map.Entry<Long, Map<Long,  Map<Long, BigDecimal>>> entry : courseTeacherCourseMap.entrySet()) {
				Map<Long,  Map<Long, BigDecimal>> teacherCourseIndicationMap = entry.getValue();
				Long thisCourseId = entry.getKey();
				if(!thisCourseId.equals(courseId)) {
					// 如果courseId 不相等，先跳过，等用到时候再说
					continue;
				}
				// 记录一下当前指标点存在于此课程
				List<Long> courseIdList = indicationCourseIdList.get(indicationId);
				if(courseIdList == null || !courseIdList.contains(courseId)) {
					// 如果该指标点不存在于此课程，简约时间，先跳过
					continue;
				}

				for(Map.Entry<Long, Map<Long, BigDecimal>> secondEntry : teacherCourseIndicationMap.entrySet()) {
					Long teacherCourseId = secondEntry.getKey();
					Map<Long, BigDecimal> indicationMap = secondEntry.getValue();
					// 只有当前指标点，才计算
					for(Map.Entry<Long, BigDecimal> thirdEntry : indicationMap.entrySet()) {
						Long thisIndicationId = thirdEntry.getKey();
						if(!thisIndicationId.equals(indicationId)) {
							continue;
						}
						// 当前指标点的【2.】~【3.2】
						BigDecimal oneTeacherCourseIndicationScore = thirdEntry.getValue();
						// 获取占比之和
						// 某课程指标点的考评点类型占比之和 key: teacherCourseId,indication.    value:BigDecimal
						String key = teacherCourseId + "," + indicationId;
						BigDecimal oneTeacherCourseIndicationPercentageBig = oneTeacherCoursePercentageBigMap.get(key);
						// 遍历教师课程，获取对应的(X考评点类型占比 + Y考评点类型占比 + ...)，并计算出当前教师课程当前指标点的评价值，然后求平均
						BigDecimal oneTeacherOneIndicationResult = new BigDecimal(0);
						if(oneTeacherCourseIndicationPercentageBig == null) {
							// 如果不存在，则表示是考核成绩分析法
							oneTeacherOneIndicationResult = PriceUtils._mul(oneTeacherCourseIndicationScore, ccIndicationCourse.getBigDecimal("weight"));
							result = result.add(oneTeacherOneIndicationResult);
						} else {
							// 否则是评分表分析法
							/*
							 * Edit By SY Date : 2017-9-7
							 * 【指标点达成度算法】：解释指标点达成度：
							 * 	【(考评点平均分 * 考评点类型占比)】 * 指标点的权重
							 * (考评点平均分 *  )  / 考评点占比之和
							 * （下面result可以带入：X = 平时成绩。 Y = 期末成绩。 A = 考评点一。 B = 考评点二。 C = 考评点三）
							 * 1. result（指标点达成度） =
							 * 	 	{
							 * 			[
							 * 2.				(X考评点类型 的 A考评点平均分 * A考评点比例系数) +  (X考评点类型 的 B考评点平均分 * B考评点比例系数) + ..
							 * 3.1			] * X考评点类型占比
							 * 3.2				÷ （A考评点比例系数 + B考评点比例系数 +...）
							 * 			+
							 *  		[
							 *  			(Y考评点类型 的 C考评点平均分 * C考评点比例系数) + ..
							 *  		]* Y考评点类型占比 ÷ （C考评点比例系数 + ...）
							 *  		...
							 * 		}
							 * 4.	 * 指标点权重
							 * 5.  	 ÷ (X考评点类型占比 + Y考评点类型占比 + ...)
							 */
							// result = weight * ( (考评点平均分 * 考评点类型占比) + (考评点平均分 * 考评点类型占比 ) + ...)
							// 4. result = weight * {...}
							// 由于存在一门课有多个教师课程，所以这里不能简单直接获取，应该分批计算每个教师课程的平均值
							oneTeacherOneIndicationResult = PriceUtils._mul(oneTeacherCourseIndicationScore, ccIndicationCourse.getBigDecimal("weight"));
							// 5. ÷ (X考评点类型占比 + Y考评点类型占比 + ...)
							oneTeacherOneIndicationResult = oneTeacherOneIndicationResult.divide(oneTeacherCourseIndicationPercentageBig, 4, RoundingMode.HALF_UP);
							result = result.add(oneTeacherOneIndicationResult);
						}
					}
					teacherCourseNum++;
				}
			}

			if(!result.equals(0) && teacherCourseNum != 0) {
				result = result.divide(new BigDecimal(teacherCourseNum), 4, RoundingMode.HALF_UP);
			}

			CcReportCourse ccReportCourse = new CcReportCourse();
			ccReportCourse.set("grade", grade);
			ccReportCourse.set("indication_course_id", ccIndicationCourse.getLong("id"));
			ccReportCourse.set("result", result.setScale(2, RoundingMode.HALF_UP));
			ccReportCourse.set("is_del", CcReportCourse.DEL_NO);
			ccReportCourse.set("statistics_date", date);
			ccReportCourse.set("create_date", date);
			ccReportCourse.set("modify_date", date);
			ccReportCourse.set("id", idGenerate.getNextValue());
			ccReportCourses.add(ccReportCourse);
			// 记录课程指标点编号
			indicationCourseIds.add(ccIndicationCourse.getLong("id"));

			// 记录专业及其每个方向上的指标点达成度
			Long majorDirectionId = ccIndicationCourse.getLong("direction_id");
			Long courseGroupId = ccIndicationCourse.getLong("course_group_id");

			// 无专业方向，赋予专业方向编号无专业方向编号
			if (majorDirectionId == null) {
				majorDirectionId = CcReportMajor.NO_MAJORDIRECTION_ID;
			}

			// 指标点编号,专业方向编号
			String indicationMajorResultKey = indicationId + "," + majorDirectionId;

			if (majorReportResultMap.get(indicationMajorResultKey) == null) {
				majorReportResultMap.put(indicationMajorResultKey, new BigDecimal(0));
			}

			// 课程组编号
			if (courseGroupId == null) {
				majorReportResultMap.put(indicationMajorResultKey
						, PriceUtils._add(majorReportResultMap.get(indicationMajorResultKey)
								, result));

			} else {
				Map<Long, BigDecimal> courseGroupResult = null;
				if (courseGroupResultMap.get(indicationMajorResultKey) == null) {
					courseGroupResult = Maps.newHashMap();
					courseGroupResultMap.put(indicationMajorResultKey, courseGroupResult);
				} else {
					courseGroupResult = courseGroupResultMap.get(indicationMajorResultKey);
				}

				BigDecimal minScore = courseGroupResult.get(courseGroupId);
				// 当课程组中该门课成绩更小或是之前课程组未有成绩时，课程组成绩为该门课成绩
				if (minScore == null || PriceUtils.greaterThan(minScore, result)) {
					minScore = result;
				}

				courseGroupResult.put(courseGroupId, minScore);
			}

		}

		//考核分析法下没有学生成绩的课程指标点
		List<CcIndicationCourse> gradeIndicationCourses = CcIndicationCourse.dao.findNotExistStudentGrade(versionId, grade);
		//考评点分析法下没有学生成绩的课程指标点
		List<CcIndicationCourse> evaluteIndicationCourses = CcIndicationCourse.dao.findNotExistStudentEvalute(versionId, grade);
		if(!gradeIndicationCourses.isEmpty()){
			for(CcIndicationCourse temp : gradeIndicationCourses){
				indicationCourseIds.add(temp.getLong("id"));
			}
		}
		if(!evaluteIndicationCourses.isEmpty()){
			for(CcIndicationCourse temp : evaluteIndicationCourses){
				indicationCourseIds.add(temp.getLong("id"));
			}
		}


		// 删除之前旧课程达成度记录
		if (!indicationCourseIds.isEmpty() && !CcReportCourse.dao.deleteAllNoDel(grade, indicationCourseIds)) {
			logger.error(new StringBuilder("达成度统计：课程达成度统计表(").append(CcReportCourse.dao.tableName).append(")")
					.append("批量删除旧的统计记录失败, 删除的年级为").append(grade.toString()).append(", 删除的课程指标点编号包括(")
					.append(indicationCourseIds.toString()).append(")")
					.toString());

			return false;
		}

		// 保存新的课程达成度记录
		if (!ccReportCourses.isEmpty() && !CcReportCourse.dao.batchSave(ccReportCourses)) {
			logger.error(new StringBuilder("达成度统计：课程达成度统计表(").append(CcReportCourse.dao.tableName).append(")")
					.append("批量增加新的统计记录失败，新增的记录包括(").append(ccReportCourses.toString()).append(")")
					.toString());

			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		logger.info("达成度统计：课程达成度统计完成，准备进行指标点达成度统计");

		// 指标点达成度的数据汇总与保存
		List<CcReportMajor> ccReportMajors = Lists.newArrayList();
		List<Long> indicationIds = Lists.newArrayList();
		for (Map.Entry<String, BigDecimal> entry : majorReportResultMap.entrySet()) {
			String[] keySplit = entry.getKey().split(",");
			// 指标点编号
			Long indicationId = Long.valueOf(keySplit[0]);
			// 专业方向编号
			Long majorDirectionId = Long.valueOf(keySplit[1]);
			majorDirectionId = CcReportMajor.NO_MAJORDIRECTION_ID.equals(majorDirectionId) ? null : majorDirectionId;
			// 指标点得分
			BigDecimal indicationScore = PriceUtils.currency(entry.getValue());
			String noMajorDirectionIndicationKey = indicationId + "," + CcReportMajor.NO_MAJORDIRECTION_ID;
			// 当专业认证处于方向上时，需加上方向上的达成度
			if (majorDirectionId != null && majorReportResultMap.get(noMajorDirectionIndicationKey) != null) {
				indicationScore = PriceUtils._add(indicationScore, PriceUtils.currency(majorReportResultMap.get(noMajorDirectionIndicationKey)));
			}

			// 无方向
			Map<Long, BigDecimal> courseGroupScoreMapTemp = null;
			Map<Long, BigDecimal> courseGroupScoreMap = courseGroupResultMap.get(entry.getKey());
			if(majorDirectionId == null){
				courseGroupScoreMapTemp = courseGroupScoreMap;
			}else{
				//有方向
				Map<Long, BigDecimal> noDirectionCourseGroupScoreMap = courseGroupResultMap.get(noMajorDirectionIndicationKey);
				if(noDirectionCourseGroupScoreMap == null){
					noDirectionCourseGroupScoreMap = Maps.newHashMap();
				}
				if(courseGroupScoreMap != null){
					for (Map.Entry<Long, BigDecimal> courseGroupScoreEntry : courseGroupScoreMap.entrySet()) {
						BigDecimal dirValue = courseGroupScoreEntry.getValue();
						Long dirKey = courseGroupScoreEntry.getKey();
						if (dirValue != null ) {
							BigDecimal noDirValue = noDirectionCourseGroupScoreMap.get(dirKey);
							if(noDirValue == null){
								noDirectionCourseGroupScoreMap.put(dirKey, dirValue);
							}else{
								noDirectionCourseGroupScoreMap.put(dirKey, PriceUtils.lessThan(dirValue, noDirValue) ? dirValue : noDirValue);
							}
						}
					}
				}
				courseGroupScoreMapTemp = noDirectionCourseGroupScoreMap;
			}
			if (courseGroupScoreMapTemp != null) {
				for (Map.Entry<Long, BigDecimal> courseGroupScoreEntry : courseGroupScoreMapTemp.entrySet()) {
					if (courseGroupScoreEntry.getValue() != null ) {
						indicationScore = PriceUtils._add(indicationScore, PriceUtils.currency(courseGroupScoreEntry.getValue()));
					}
				}
			}
			BigDecimal maxResult = new BigDecimal(1);
			CcReportMajor ccReportMajor = new CcReportMajor();
			ccReportMajor.set("indication_id", indicationId);
			ccReportMajor.set("grade", grade);
			ccReportMajor.set("major_direction_id", majorDirectionId);
			//如果值大于1就设为1
			if (PriceUtils.greaterThan(indicationScore,maxResult)){
				ccReportMajor.set("result", maxResult);
			}else{
				ccReportMajor.set("result", indicationScore);
			}
			ccReportMajor.set("is_del", CcReportMajor.DEL_NO);
			ccReportMajor.set("statistics_date", date);
			ccReportMajor.set("create_date", date);
			ccReportMajor.set("modify_date", date);
			ccReportMajor.set("id", idGenerate.getNextValue());
			ccReportMajors.add(ccReportMajor);
			indicationIds.add(indicationId);
		}

		// 删除旧的指标点达成度的记录
		if (!indicationIds.isEmpty() && !CcReportMajor.dao.deleteAllNoDel(grade, indicationIds)) {
			logger.error(new StringBuilder("达成度统计：指标点达成度统计表(").append(CcReportMajor.dao.tableName).append(")")
					.append("批量删除旧的统计记录失败, 删除的年级为").append(grade.toString()).append(", 删除的指标点编号包括(")
					.append(indicationCourseIds.toString()).append(")")
					.toString());

			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		// 保存新的指标点达成度的记录
		if (!ccReportMajors.isEmpty() && !CcReportMajor.dao.batchSave(ccReportMajors)) {
			logger.error(new StringBuilder("达成度统计：指标点达成度统计表(").append(CcReportCourse.dao.tableName).append(")")
					.append("批量增加新的统计记录失败，新增的记录包括(").append(ccReportMajors.toString()).append(")")
					.toString());

			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		logger.info("达成度统计：指标点达成度统计完成");

		return true;
	}

	/**
	 * 统计某个年级下所有学生的课程达成度以及指标点达成度（去掉考评点分析法）
	 *
	 * @param versionId 版本编号
	 * @param grade 学生年级
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean statisticsPersonalCourseAndIndication(Long versionId, Integer grade) {
		/*
		 *  这个方法的代码，动一下就出错，而且还搞不清数据是什么，需求是什么。动手修改需要大量时间。
		 *  对此，之前的人在这里浪费了总共时间：10h。
		 *  每当有人来改，请更新这个时间
		 */
		// 获得过去计算的达成度报表统计表
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date5 = new Date();
		String format5 = sdf.format(date5);

		List<CcReportPersonalCourse> historyCcReportPersonalCourses = CcReportPersonalCourse.dao.findAllByVersionAndGrade(versionId, grade);
		Date date6 = new Date();
		String format6 = sdf.format(date6);
		logger.info("获得过去计算的达成度报表统计表："+DateUtil.timeSub(format5, format6)+"秒");
		Map<String, CcReportPersonalCourse> historyStudentAndIndicationCourseToReportMap = Maps.newHashMap();
		for (CcReportPersonalCourse ccReportPersonalCourse : historyCcReportPersonalCourses) {
			// key为学生编号，课程指标点编号
			String key = new StringBuilder(ccReportPersonalCourse.getLong("student_id").toString())
					.append(",").append(ccReportPersonalCourse.getLong("indication_course_id").toString())
					.toString();

			historyStudentAndIndicationCourseToReportMap.put(key, ccReportPersonalCourse);

		}

		// 获得年级下使用考核分析法的教学班的每个学生的成绩情况  这里耗时较长
		Date date3 = new Date();
		String format = sdf.format(date3);
		List<CcEduclassStudent> ccEduclassStudentsScore = CcEduclassStudent.dao.findAllStudentScore(versionId, grade);
		Date date4 = new Date();
		String format1 = sdf.format(date4);
		long timeSubs = DateUtil.timeSub(format, format1);

		logger.info("获得年级下使用考核分析法的教学班的每个学生的成绩情况共用时间："+timeSubs+"秒");
//		// 获得年级下使用考评点分析法的教学班的每个学生的成绩情况
//		List<CcEduclassStudent> ccEduclassStudentsEvalute = CcEduclassStudent.dao.findAllStudentEvalute(versionId, grade);
//		ccEduclassStudentsScore.addAll(ccEduclassStudentsEvalute);
//		// TODO SY 测试
//		// TEST
//		List<CcEduclassStudent> test = new ArrayList<>();
//		for(CcEduclassStudent ccEduclassStudent : ccEduclassStudentsEvalute) {
//			// key为学生编号，课程指标点编号
//			String key = new StringBuilder(ccEduclassStudent.getLong("student_id").toString())
//					.append(",").append(ccEduclassStudent.getLong("indication_course_id").toString())
//					.toString();
////			if(key.contains("286155,282778")) {
//			if(key.equals("283680,282788")) {
//				BigDecimal score = ccEduclassStudent.getBigDecimal("score") == null ? null
//						: PriceUtils._mul(PriceUtils._mul(
//								ccEduclassStudent.getBigDecimal("score")
//								, ccEduclassStudent.getBigDecimal("score_weight")
//						), ccEduclassStudent.getBigDecimal("indication_weight"));
//				// EDIT BY SY 新增考评点类型，他的占比需要呈上去
//				Integer percentage = ccEduclassStudent.getInt("percentage");
//				percentage = percentage == null ? 0 : percentage;
//				score = PriceUtils._mul(new BigDecimal(percentage), score).divide(new BigDecimal(100));
//
//				ccEduclassStudent.put("score_caculate", score.toString());
//				test.add(ccEduclassStudent);
//			}
//		}
		// 开始计算成绩 key为学生编号student_id，课程指标点编号indication_course_id 【C】
		Map<String, BigDecimal> studentAndIndicationCourseToMap = Maps.newHashMap();
		// 开始计算成绩 key为学生编号student_id，课程指标点编号indication_course_id ,课程目标编号 indication_id【C】
		Map<String, BigDecimal> studentAndIndicationCourseToMapMom = Maps.newHashMap();
		Map<String, BigDecimal> studentAndIndicationCourseToMapSon = Maps.newHashMap();
//		TODO SY 原先由于未除以最后成绩组成之和，所以这里补除以一下。但是答案突然对了，这里代码先废弃。随后再说。【BUG-6377】
//		// 某个学生，某个指标点的成绩组成权重之和
//		Map<String, BigDecimal> studentAndIndicationCourseAllWeightToMap = Maps.newHashMap();
//		// 某个teacherCourse的课程的考评点分数未除以考评点类型占比之和 Map<'student_id,indication_course_id,teacher_course_id',分数> 【A-2】
//		Map<String, BigDecimal> teacherCourseIndicationEvaluteNotDiviMaps = Maps.newHashMap();
//		// 某个teacherCourse的课程的考评点分数 Map<'student_id,indication_course_id,teacher_course_id,evaluteTypeId',分数> 【A】
//		Map<String, BigDecimal> teacherCourseIndicationEvaluteMaps = Maps.newHashMap();
//		// 某个teacherCourse的课程的占比之和	 Map<'student_id,indication_course_id,teacher_course_id,evaluteTypeId',占比和> 【B】
//		Map<String, BigDecimal> teacherCourseIndicationEvaluteWeightMaps = Maps.newHashMap();
//		// 用于记录，是否计算过占比之和   Map<'student_id,indication_course_id,teacher_course_id,evaluteId', Boolean> 【B-2】
//		Map<String, Boolean> teacherCourseIndicationEvaluteIsUsedMaps = Maps.newHashMap();
//		// 某个teacherCourse的课程的indicationCourse的某个evaluteType的百分比 Map<'indication_course_id,teacher_course_id',分数> 【D】
//		Map<String, BigDecimal> teacherCourseIndicationPercentageMaps = Maps.newHashMap();
//		// indicationCourse的某个evaluteType的百分比是否已经增加过 Map<'indication_course_id,teacher_course_id,evaluteTypeId',Boolean> 【D-2】
//		Map<String, Boolean> teacherCourseIndicationEvalutePercentageIsUsedMaps = Maps.newHashMap();
//		// 记录student_id,indication_course_id,teacher_course_id,evaluteTypeId 对应的是哪个 student_id,indication_course_id,teacher_course_id
//		Map<String, String> relationshipMaps = Maps.newHashMap();

//		 for(学生每个分数) {
//			获取每个学生的分数 * 比例系数 * 考评点类型占比 * 指标点权重； 【A】
//			比例系数相加；【B】
//		}
//		for(A) {
//			studentAndIndicationCourseToMap = SUM(a / B);【C】
//		}

		// 开始计算成绩 key为学生编号student_id，课程指标点编号indication_course_id
		Map<String, CcEduclassStudent> studentAndIndicationCourseGradeMap = Maps.newHashMap();
//		// 开始计算成绩 key为学生编号student_id，课程指标点编号indication_course_id ,课程目标编号 indication_id
//		Map<String, CcEduclassStudent> studentAndIndicationCourseGradeMap = Maps.newHashMap();
		Map<Long, Map<Long, BigDecimal>> educlassIndicationMap = educlassIndicationWeightMap(versionId, grade);
		// beforeXXX用于排除同一个学生在不同学期上同一门课导致的错误 Edit By SY 2016年12月12日17:55:14
		String beforeClass = "";
		String newClass = "";
		String beforeKey = "";
		for (CcEduclassStudent ccEduclassStudent : ccEduclassStudentsScore) {
//			Long educlassId = ccEduclassStudent.getLong("educlass_id");
//			Long indicatorPointId = ccEduclassStudent.getLong("indicatorPointId");
			Long indicationId = ccEduclassStudent.getLong("indication_id");
//			Integer resultType = ccEduclassStudent.getInt("result_type");

			Long studentId = ccEduclassStudent.getLong("student_id");
			Long indicationCourseId = ccEduclassStudent.getLong("indication_course_id");
//			Long teacherCourseId = ccEduclassStudent.getLong("teacher_course_id");
			String studentIndicationKey = new StringBuilder(studentId.toString())
					.append(",").append(indicationCourseId.toString())
					.toString();
			// key为学生编号student_id，课程指标点编号indication_course_id, 课程目标indication_id
			String key = new StringBuilder(studentIndicationKey)
					.append(",").append(indicationId.toString())
					.toString();
			// 用于排除同一个学生在不同学期上同一门课导致的错误 Edit By SY 2016年12月12日17:55:14
			newClass = key + "," + ccEduclassStudent.getInt("start_year") + "," + ccEduclassStudent.getInt("term_type") + "," + ccEduclassStudent.getInt("term");
			// 如果发现是同一个课程指标点，但是不同的学期（不同的教学班），则跳过，按照老的来，因为我查询出来的时候，排序是依据上课时间倒序的
			if(key.equals(beforeKey) && !newClass.equals(beforeClass)) {
				continue;
			} else {
				beforeClass = newClass;
				beforeKey = key;
			}

			// 当计算项为最新开课课程年级时进行计算, 当计算项比年级比之前的计算项年级小时跳过计算
			if (studentAndIndicationCourseGradeMap.get(studentIndicationKey) == null || ccEduclassStudent.getInt("course_grade") > studentAndIndicationCourseGradeMap.get(studentIndicationKey).getInt("course_grade")) {
				studentAndIndicationCourseToMap.put(studentIndicationKey, null);
				studentAndIndicationCourseGradeMap.put(studentIndicationKey, ccEduclassStudent);
			} else {
				if (ccEduclassStudent.getInt("course_grade") < studentAndIndicationCourseGradeMap.get(studentIndicationKey).getInt("course_grade")) {
					continue;
				}
			}
//			// TEST
//			if(key.contains("301335,")) {
//			if(key.equals("283680,282788")) {
//				int a = 0;
//				a = 1;
//			}
			// 学生课程成绩项求达成度
			/*
			 *  score = (co1,co2...)min * indicationWeight (由于我们计算时候，已经获取了indicationWeight,所以在mom这边，就已经计算了)
			 *  {
			 *  	co1 = mom1 / (son1 * 期望) (由于我们计算时候，已经获取了期望,所以在son这边，就已经计算了)
			 *  	{
			 *  		mom = C11 * P11 + C12* P12 ...
			 *  		son = U11 * P11 + U12* P12 ...
			 *  	}
			 *  }
			 */
			// TODO SY 期望值没算，然后，最后关头score需要计算一下最小值（mom/son）
//			BigDecimal score = null;
			BigDecimal mom = null;
			BigDecimal son = null;
//			if (CcTeacherCourse.RESULT_TYPE_SCORE.equals(resultType)) {
			BigDecimal C = ccEduclassStudent.getBigDecimal("score");
			BigDecimal P = ccEduclassStudent.getBigDecimal("score_weight");
			BigDecimal U = ccEduclassStudent.getBigDecimal("max_score");
			BigDecimal expectedValue = ccEduclassStudent.getBigDecimal("expected_value");
			BigDecimal indicationWeight = ccEduclassStudent.getBigDecimal("indication_weight");
			mom = C == null ? null : PriceUtils._mul(PriceUtils._mul(C, P), indicationWeight);
			son = C == null ? null : PriceUtils._mul(PriceUtils._mul(U, P), expectedValue);
//				score = ccEduclassStudent.getBigDecimal("score") == null ? null
//						: PriceUtils._mul(PriceUtils._mul(
//								ccEduclassStudent.getBigDecimal("score").divide(
//										PriceUtils._mul(ccEduclassStudent.getBigDecimal("max_score"),
//										educlassIndicationMap.get(educlassId).get(indicatorPointId))  , 5, RoundingMode.HALF_UP)
//								, ccEduclassStudent.getBigDecimal("score_weight")
//						), ccEduclassStudent.getBigDecimal("indication_weight"));
////				TODO SY 原先由于未除以最后成绩组成之和，所以这里补除以一下。但是答案突然对了，这里代码先废弃。随后再说。【BUG-6377】
////				// 由于这里是按照学生+指标点课程来，所以不会重复的。直接加就好
////				BigDecimal allWeight = studentAndIndicationCourseAllWeightToMap.get(key);
////				allWeight = allWeight == null ? new BigDecimal(0) : allWeight;
////				allWeight = allWeight.add(ccEduclassStudent.getBigDecimal("score_weight"));
////				studentAndIndicationCourseAllWeightToMap.put(key, allWeight);

			// 学生达成度相加
			if (C != null) {
				// 当前这个学生的课程指标点的课程目标存在
				if (studentAndIndicationCourseToMapMom.get(key) == null) {
					studentAndIndicationCourseToMapMom.put(key, mom);
					studentAndIndicationCourseToMapSon.put(key, son);
				} else {
//						studentAndIndicationCourseToMap.put(key, PriceUtils._add(studentAndIndicationCourseToMap.get(key), score));
					mom = PriceUtils._add(studentAndIndicationCourseToMapMom.get(key), mom);
					son = PriceUtils._add(studentAndIndicationCourseToMapSon.get(key), son);
					studentAndIndicationCourseToMapMom.put(key, mom);
					studentAndIndicationCourseToMapSon.put(key, son);
				}
			}
//			} else if (CcTeacherCourse.RESULT_TYPE_EVALUATE.equals(resultType)) {
//				score = ccEduclassStudent.getBigDecimal("score") == null ? null
//						: PriceUtils._mul(PriceUtils._mul(
//								ccEduclassStudent.getBigDecimal("score")
//								, ccEduclassStudent.getBigDecimal("score_weight")
//						), ccEduclassStudent.getBigDecimal("indication_weight"));
//				// EDIT BY SY 新增考评点类型，他的占比需要呈上去
//				Integer percentage = ccEduclassStudent.getInt("percentage");
//				percentage = percentage == null ? 0 : percentage;
//				BigDecimal percentageBig = new BigDecimal(percentage).divide(new BigDecimal(100), 5, RoundingMode.HALF_UP);
//				score = PriceUtils._mul(percentageBig, score);
//
//				Long evaluteTypeId = ccEduclassStudent.getLong("evalute_type_id");
//				Long evaluteId = ccEduclassStudent.getLong("evalute_id");
//				String studentIndicationCourseEvaluteType = key + "," + teacherCourseId + "," + evaluteTypeId;
//				// 关系建立
//				relationshipMaps.put(studentIndicationCourseEvaluteType, key + "," + teacherCourseId);
//				// 学生达成度相加
//				if (score != null) {
//					if (teacherCourseIndicationEvaluteMaps.get(studentIndicationCourseEvaluteType) == null) {
//						teacherCourseIndicationEvaluteMaps.put(studentIndicationCourseEvaluteType, score);
//					} else {
//						teacherCourseIndicationEvaluteMaps.put(studentIndicationCourseEvaluteType, PriceUtils._add(teacherCourseIndicationEvaluteMaps.get(studentIndicationCourseEvaluteType), score));
//					}
//				}
//				// 用于记录，是否计算过占比之和
//				String isUsedKey = key + "," + teacherCourseId + "," + evaluteId;
//				if(teacherCourseIndicationEvaluteIsUsedMaps.get(isUsedKey) == null) {
//					// 设置为已经加入
//					teacherCourseIndicationEvaluteIsUsedMaps.put(isUsedKey, Boolean.TRUE);
//					// 分数增加
//					BigDecimal weight = teacherCourseIndicationEvaluteWeightMaps.get(studentIndicationCourseEvaluteType);
//					weight = weight == null ? new BigDecimal(0) : weight;
//					weight = weight.add(ccEduclassStudent.getBigDecimal("score_weight"));
//					teacherCourseIndicationEvaluteWeightMaps.put(studentIndicationCourseEvaluteType, weight);
//				}
//				String isAddKey = indicationCourseId + ","  + teacherCourseId + "," + evaluteTypeId;
//				String teacherCourseIndicationPercentageKey = indicationCourseId + ","  + teacherCourseId;
//				if(teacherCourseIndicationEvalutePercentageIsUsedMaps.get(isAddKey) == null) {
//					// 设置为已经加入
//					teacherCourseIndicationEvalutePercentageIsUsedMaps.put(isAddKey, Boolean.TRUE);
//					// 百分比增加
//					BigDecimal percentageBigBefore = teacherCourseIndicationPercentageMaps.get(teacherCourseIndicationPercentageKey);
//					percentageBigBefore = percentageBigBefore == null ? new BigDecimal(0) : percentageBigBefore;
//					percentageBigBefore = percentageBigBefore.add(percentageBig);
//					teacherCourseIndicationPercentageMaps.put(teacherCourseIndicationPercentageKey, percentageBigBefore);
//				}
//			}
		}
		// 计算学生课程达成度
		for(Entry<String, BigDecimal> entrystudentAndIndicationCourseToMapMom : studentAndIndicationCourseToMapMom.entrySet()) {
			String key = entrystudentAndIndicationCourseToMapMom.getKey();
			String kes[] = key.split(",");
			Long studentId = Long.valueOf(kes[0]);
			Long indicationCourseId = Long.valueOf(kes[1]);
//			Long indicationId = Long.valueOf(kes[2]);
			BigDecimal mom = entrystudentAndIndicationCourseToMapMom.getValue();
			BigDecimal son = studentAndIndicationCourseToMapSon.get(key);
			String studentIndicationKey = studentId+","+indicationCourseId;
			BigDecimal newScore = mom.divide(son, 5, RoundingMode.HALF_UP);
			BigDecimal score = studentAndIndicationCourseToMap.get(studentIndicationKey);
			if(score == null) {
				studentAndIndicationCourseToMap.put(studentIndicationKey, newScore);
			} else if(newScore.compareTo(score) == -1){
				studentAndIndicationCourseToMap.put(studentIndicationKey, newScore);
			}
		}

//		/*
//		 * Edit By SY Date : 2017-9-19
//		 * 【指标点达成度个人算法】：解释指标点达成度：
//		 * 	【(考评点平均分 * 考评点类型占比)】 * 指标点的权重
//		 * (考评点平均分 *  )  / 考评点占比之和
//		 * （下面result可以带入：X = 平时成绩。 Y = 期末成绩。 A = 考评点一。 B = 考评点二。 C = 考评点三）
//		 * 1. result（指标点达成度） =
//		 * 	 	{
//		 * 			[
//		 * 2.				(X考评点类型 的 A考评点分 * A考评点比例系数) +  (X考评点类型 的 B考评点分 * B考评点比例系数) + ..
//		 * 3.1			] * X考评点类型占比
//		 * 3.2				÷ （A考评点比例系数 + B考评点比例系数 +...）
//		 * 			+
//		 *  		[
//		 *  			(Y考评点类型 的 C考评点分 * C考评点比例系数) + ..
//		 *  		]* Y考评点类型占比 ÷ （C考评点比例系数 + ...）
//		 *  		...
//		 * 		}
//		 * 4.	 * 指标点权重
//		 * 5.  	 ÷ (X考评点类型占比 + Y考评点类型占比 + ...)
//		 */
//		for(Map.Entry<String, BigDecimal> entry : teacherCourseIndicationEvaluteMaps.entrySet()) {
//			String key = entry.getKey();
//			BigDecimal score = entry.getValue();
//			BigDecimal weight = teacherCourseIndicationEvaluteWeightMaps.get(key);
//			score = score.divide(weight, 5, RoundingMode.HALF_UP);
//			String studentIndicationCourseKey = relationshipMaps.get(key);
////			// TEST
////			if(studentIndicationCourseKey.equals("286155,282788")) {
////			if(key.equals("283680,282788")) {
////				int i = 1;
////				i = 2;
////			}
//			BigDecimal oldScore = teacherCourseIndicationEvaluteNotDiviMaps.get(studentIndicationCourseKey);
//			oldScore = oldScore == null ? new BigDecimal(0) : oldScore;
//			// 最终，这里的分数 除了【5】，都完成了
//			teacherCourseIndicationEvaluteNotDiviMaps.put(studentIndicationCourseKey, oldScore.add(score));
//		}
//		// 执行第【5】步
//		for(Map.Entry<String, BigDecimal> entry : teacherCourseIndicationEvaluteNotDiviMaps.entrySet()) {
//			String key = entry.getKey();
//			BigDecimal score = entry.getValue();
//
//			// TEST
////			if(key.contains("286155,")) {
////			if(key.equals("283680,282788")) {
////				int i = 1;
////				i = 2;
////			}
//
//			String [] keys = key.split(",");
//			Long studentId = Long.valueOf(keys[0]);
//			Long indicationCourseId = Long.valueOf(keys[1]);
//			Long teacherCourseId = Long.valueOf(keys[2]);
//			String teacherCourseIndicationPercentageKey = indicationCourseId + "," +teacherCourseId;
//			String studentIndicationCourseKey = studentId + "," + indicationCourseId;
//			score = score.divide(teacherCourseIndicationPercentageMaps.get(teacherCourseIndicationPercentageKey), 5, RoundingMode.HALF_UP);
//			// 最终，这里的分数 除了【5】，都完成了
//			BigDecimal oldScore = studentAndIndicationCourseToMap.get(studentIndicationCourseKey);
//			oldScore = oldScore == null ? new BigDecimal(0) : oldScore;
//			if(oldScore.compareTo(score) == -1) {
//				studentAndIndicationCourseToMap.put(studentIndicationCourseKey, score);
//			}
//		}
		logger.info("达成度统计：学生个人课程记录达成度计算完成，准备进行学生专业方向记录保存");

		Date date = new Date();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		// 判断学生所属专业方向
		// 1.判断该专业改版本是否设置了专业方向
		boolean haveMajorDirection = CcMajorDirection.dao.countFiltered("plan_id", versionId) > 0;

		Map<Long, Long> studentMajorDirectionMap = null;
		List<CcMajorStudent> newCcMajorStudents = Lists.newArrayList();
		if (haveMajorDirection) {
			studentMajorDirectionMap = Maps.newHashMap();
			Long prevStudentId = null;
			Date date9 = new Date();
			List<CcEduclassStudent> studentDirectionCourse = CcEduclassStudent.dao.countStudentDirectionCourse(versionId, grade);
			Date date10 = new Date();
			logger.info("获得学生课程方向上的课程数共用时间"+DateUtil.timeSub(sdf.format(date9),sdf.format(date10))+"秒");
			for (CcEduclassStudent ccEduclassStudent : studentDirectionCourse) {
				Long studentId = ccEduclassStudent.getLong("student_id");
				// 已经排过排，某个专业方向上课程数最多的方向即为学生的专业方向，当学生的专业方向已经指定时就不在重新指定方向（只能通过手动更改来更换学生的专业方向）
				if (!studentId.equals(prevStudentId)) {
					Long studentMajorDirectionId = ccEduclassStudent.getLong("student_direction_id");
					// 保存新增专业方向的学生的专业方向
					if (studentMajorDirectionId == null && ccEduclassStudent.getLong("direction_id") != null) {
						CcMajorStudent ccMajorStudent = new CcMajorStudent();
						ccMajorStudent.set("student_id", studentId);
						ccMajorStudent.set("version_id", versionId);
						ccMajorStudent.set("major_direction_id", ccEduclassStudent.getLong("direction_id"));
						ccMajorStudent.set("create_date", date);
						ccMajorStudent.set("modify_date", date);
						ccMajorStudent.set("id", idGenerate.getNextValue());
						newCcMajorStudents.add(ccMajorStudent);
						studentMajorDirectionId = ccEduclassStudent.getLong("direction_id");
					}
					studentMajorDirectionMap.put(studentId, studentMajorDirectionId);
				}
				prevStudentId = studentId;
			}
		}

		// 保存学生所属方向
		if (!newCcMajorStudents.isEmpty() && !CcMajorStudent.dao.batchSave(newCcMajorStudents)) {
			logger.error(new StringBuilder("达成度统计：批量保存学生专业方向失败, 保存的年级为")
					.append(grade.toString()).append(", 保存的学生记录包括(")
					.append(newCcMajorStudents.toString()).append(")")
					.toString());

			return false;
		}

		logger.info("达成度统计：学生专业方向信息保存完毕，准备进行学生个人课程记录达成度保存");

		// 保存学生课程达成度的记录
		List<CcReportPersonalCourse> updateCcReportPersonalCourses = Lists.newArrayList();
		List<CcReportPersonalCourse> newCcReportPersonalCourses = Lists.newArrayList();
		//课程指标点相同的学生课程达成度
		List<CcReportPersonalCourse> sameCcReportPersonalCourses = Lists.newArrayList();
		// 计算指标点达成度
		Map<String, BigDecimal> studentIndicationMap = Maps.newHashMap();
		List<Long> reportIds=new ArrayList<>();
		// 学生指标点课程组另外处理
		Map<String, Map<Long, BigDecimal>> studentIndicationCourseGroupMap = Maps.newHashMap();
		for (Map.Entry<String, BigDecimal> entry : studentAndIndicationCourseToMap.entrySet()) {
			if (entry.getValue() == null) {
				continue;
			}

			String key = entry.getKey();
			String[] keySplit = key.split(",");
			// 学生编号
			Long studentId = Long.valueOf(keySplit[0]);
			// 课程指标点编号
			Long courseIndicationId = Long.valueOf(keySplit[1]);
			BigDecimal studentScore = entry.getValue().setScale(5, RoundingMode.HALF_UP);
//			TODO SY 原先由于未除以最后成绩组成之和，所以这里补除以一下。但是答案突然对了，这里代码先废弃。随后再说。【BUG-6377】
//			BigDecimal allWeight = studentAndIndicationCourseAllWeightToMap.get(key);
//			studentScore = studentScore.divide(allWeight, 3, RoundingMode.HALF_UP);
			CcReportPersonalCourse historyCcReportPersonalCourse = historyStudentAndIndicationCourseToReportMap.get(entry.getKey());
			if (historyCcReportPersonalCourse == null) {
				// 新增的记录项
				CcReportPersonalCourse newCcReportPersonalCourse = new CcReportPersonalCourse();
				newCcReportPersonalCourse.set("student_id", studentId);
				newCcReportPersonalCourse.set("indication_course_id", courseIndicationId);
				newCcReportPersonalCourse.set("result", studentScore);
				newCcReportPersonalCourse.set("is_del", CcReportPersonalCourse.DEL_NO);
				newCcReportPersonalCourse.set("statistics_date", date);
				newCcReportPersonalCourse.set("modify_date", date);
				newCcReportPersonalCourse.set("create_date", date);
				newCcReportPersonalCourse.set("id", idGenerate.getNextValue());
				newCcReportPersonalCourses.add(newCcReportPersonalCourse);
			} else {
				//原本是计算结果不一样的才更新，但是会导致一个问题：基础数据（比如成绩组成指标点权重等）有更新但是统计的达成度结果没有变化，会一直提示有更新
				historyCcReportPersonalCourse.set("result", studentScore);
				historyCcReportPersonalCourse.set("modify_date", date);
				historyCcReportPersonalCourse.set("statistics_date", date);
				historyCcReportPersonalCourse.set("is_del", Boolean.FALSE);
				updateCcReportPersonalCourses.add(historyCcReportPersonalCourse);
				sameCcReportPersonalCourses.add(historyCcReportPersonalCourse);

			}

			CcEduclassStudent ccEduclassStudent = studentAndIndicationCourseGradeMap.get(entry.getKey());
			if(ccEduclassStudent == null) {
				continue;
			}
			// 计算指标点成绩时过滤掉不是所属专业方向的课程
			Long courseMajorDirectionId = ccEduclassStudent.getLong("course_direction_id");
			if (!haveMajorDirection || courseMajorDirectionId == null || courseMajorDirectionId.equals(studentMajorDirectionMap.get(studentId))) {
				Long indicatorPointId = ccEduclassStudent.getLong("indicatorPointId");
				// 学生编号 , 指标点编号  ‘studentId,indicatorPointId’
				String indicatorPointIdKey = new StringBuilder(studentId.toString())
						.append(",").append(indicatorPointId.toString())
						.toString();
				// TEST
//				if(indicationKey.equals("301335,282759")) {
//					int a = 1;
//					a= 2;
//				}
				//  存在课程组编号时则另外计算分数
				Long courseGroupId = ccEduclassStudent.getLong("course_group_id");
				if (courseGroupId != null) {
					Map<Long, BigDecimal> studentCourseGroupMap = null;
					if (studentIndicationCourseGroupMap.get(indicatorPointIdKey) == null || studentIndicationCourseGroupMap.get(indicatorPointIdKey).get(courseGroupId) == null) {
						studentCourseGroupMap = Maps.newHashMap();
						studentCourseGroupMap.put(courseGroupId, studentScore);
						studentIndicationCourseGroupMap.put(indicatorPointIdKey, studentCourseGroupMap);
					} else {
						studentCourseGroupMap = studentIndicationCourseGroupMap.get(indicatorPointIdKey);
						if (PriceUtils.greaterThan(studentCourseGroupMap.get(courseGroupId), entry.getValue())) {
							studentCourseGroupMap.put(courseGroupId, studentScore);
						}

					}

					if (studentIndicationMap.get(indicatorPointIdKey) == null) {
						studentIndicationMap.put(indicatorPointIdKey, new BigDecimal(0L));
					}

				} else {
					if (studentIndicationMap.get(indicatorPointIdKey) == null) {
						studentIndicationMap.put(indicatorPointIdKey, studentScore);
					} else {
						studentIndicationMap.put(indicatorPointIdKey, PriceUtils._add(studentIndicationMap.get(indicatorPointIdKey), studentScore));
					}

				}

			}

		}

		// 更新之前的学生课程成绩记录

		if (!updateCcReportPersonalCourses.isEmpty() ) {
			Date date1 = new Date();

			String dateS = sdf.format(date1);
			boolean updateState = CcReportPersonalCourse.dao.batchUpdate(updateCcReportPersonalCourses, "result, modify_date, statistics_date, is_del");
			Date date2 = new Date();
			String dateS2 = sdf.format(date2);
			long timeSub = DateUtil.timeSub(dateS, dateS2);

			logger.info("统计达个人课程达成度统计表共用时间："+timeSub+"秒");
			if(!updateState) {
				logger.error(new StringBuilder("达成度统计：个人课程达成度统计表(").append(CcReportPersonalCourse.dao.tableName).append(")")
						.append("批量更新失败, 更新的年级为").append(grade.toString()).append(", 更新的内容包括(")
						.append(updateCcReportPersonalCourses.toString()).append(")")
						.toString());

				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
		}
		// 新增学生课程成绩记录
		if (!newCcReportPersonalCourses.isEmpty() && !CcReportPersonalCourse.dao.batchSave(newCcReportPersonalCourses)) {
			logger.error(new StringBuilder("达成度统计：个人课程达成度统计表(").append(CcReportPersonalCourse.dao.tableName).append(")")
					.append("批量新增记录失败, 新增记录所属的年级为").append(grade.toString()).append(", 新增的内容包括(")
					.append(newCcReportPersonalCourses.toString()).append(")")
					.toString());

			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		//需要删除的课程指标点
		List<Long> reportPersonalCoursesIds = Lists.newArrayList();
		if(!historyCcReportPersonalCourses.isEmpty()){
			if(!sameCcReportPersonalCourses.isEmpty()){
				historyCcReportPersonalCourses.removeAll(sameCcReportPersonalCourses);
			}
			if(!historyCcReportPersonalCourses.isEmpty()){
				for(CcReportPersonalCourse temp : historyCcReportPersonalCourses){
					reportPersonalCoursesIds.add(temp.getLong("id"));
				}
				if(!CcReportPersonalCourse.dao.deleteAll(reportPersonalCoursesIds.toArray(new Long[reportPersonalCoursesIds.size()]), date)){
					logger.error(new StringBuilder("达成度统计：个人课程达成度统计表(").append(CcReportPersonalCourse.dao.tableName).append(")")
							.append("批量删除记录失败, 新增记录所属的年级为").append(grade.toString()).append(", 删除的内容包括(")
							.append(historyCcReportPersonalCourses.toString()).append(")")
							.toString());
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
			}
		}

		logger.info("达成度统计：学生个人课程记录达成度保存完成，准备进行学生个人指标点达成度保存");

		// 遍历学生指标点Map
		List<CcReportPersonalIndication> newCcReportPersonalIndications = Lists.newArrayList();
		// Map 的   key  ：  学生编号 , 指标点编号  ‘studentId,indicatorPointId’
		for (Map.Entry<String, BigDecimal> entry : studentIndicationMap.entrySet()) {
			String[] indicatorPointIdKeySplit = entry.getKey().split(",");
			// 学生编号
			Long studentId = Long.valueOf(indicatorPointIdKeySplit[0]);
			// 指标点编号
			Long indicatorPointId = Long.valueOf(indicatorPointIdKeySplit[1]);
			// 指标点课程组成绩
			BigDecimal fiStudentScore = entry.getValue();
			if (studentIndicationCourseGroupMap.get(entry.getKey()) != null) {
				for (Map.Entry<Long, BigDecimal> courseGroupEntry : studentIndicationCourseGroupMap.get(entry.getKey()).entrySet()) {
					if (fiStudentScore == null && courseGroupEntry.getValue() != null) {
						fiStudentScore = new BigDecimal(0);
					}
					fiStudentScore = PriceUtils._add(fiStudentScore, courseGroupEntry.getValue());
				}
			}

			// 这里是个人达成度报表显示
			fiStudentScore = fiStudentScore == null ? null : fiStudentScore.setScale(3, RoundingMode.HALF_UP);
			CcReportPersonalIndication ccReportPersonalIndication = new CcReportPersonalIndication();
			ccReportPersonalIndication.set("indication_id", indicatorPointId);
			ccReportPersonalIndication.set("student_id", studentId);
			ccReportPersonalIndication.set("result", fiStudentScore);
			ccReportPersonalIndication.set("is_del", CcReportPersonalIndication.DEL_NO);
			ccReportPersonalIndication.set("statistics_date", date);
			ccReportPersonalIndication.set("create_date", date);
			ccReportPersonalIndication.set("modify_date", date);
			ccReportPersonalIndication.set("id", idGenerate.getNextValue());
			newCcReportPersonalIndications.add(ccReportPersonalIndication);
		}

		// 删除旧的学生指标点成绩
		if (!CcReportPersonalIndication.dao.deleteAllNoDel(grade)) {
			logger.error(new StringBuilder("达成度统计：个人指标点达成度统计表(").append(CcReportPersonalIndication.dao.tableName).append(")")
					.append("批量删除旧的记录失败, 删除记录所属的年级为").append(grade.toString())
					.toString());

			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		// 新增学生指标点成绩
		if (!newCcReportPersonalIndications.isEmpty() && !CcReportPersonalIndication.dao.batchSave(newCcReportPersonalIndications)) {
			logger.error(new StringBuilder("达成度统计：个人指标点达成度统计表(").append(CcReportPersonalIndication.dao.tableName).append(")")
					.append("批量新增记录失败, 新增记录所属的年级为").append(grade.toString()).append(", 新增记录包括(")
					.append(newCcReportPersonalIndications.toString()).append(")")
					.toString());

			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		logger.info("达成度统计：学生个人课程记录达成度保存，学生个人达成度统计完成");

		return true;
	}

	/**
	 * 新建报表生成任务记录
	 *
	 * @param name 任务名称
	 * @param reportType 报表类型
	 * @param versionId 版本编号
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean createReportBuildRecord(String name, Integer reportType, Long versionId) {
		Date date = new Date();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		CcReportBuildStatus ccReportBuildStatus = new CcReportBuildStatus();
		ccReportBuildStatus.set("report_build_key", name);
		ccReportBuildStatus.set("report_build_status", CcReportBuildStatus.STATUS_TASK_UNCREATE);
		ccReportBuildStatus.set("report_type", reportType);
		ccReportBuildStatus.set("version_id", versionId);
		ccReportBuildStatus.set("is_build_finish", Boolean.FALSE);
		ccReportBuildStatus.set("create_date", date);
		ccReportBuildStatus.set("modify_date", date);
		ccReportBuildStatus.set("id", idGenerate.getNextValue());
		return ccReportBuildStatus.save();
	}


	/**
	 * 更新报表生成任务记录
	 *
	 * @param name 任务名称
	 * @param buildType 生成类型
	 * @param reportStatue 报表任务状态
	 * @param isFinish 报表任务是否已经完成
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean updateReportBuildRecord(String name, Integer buildType, Integer reportStatue, Boolean isFinish, Long elapseTime) {
		Date date = new Date();
		CcReportBuildStatus ccReportBuildStatus = CcReportBuildStatus.dao.getBuildStatusRecord(buildType, name);
		if (ccReportBuildStatus == null) {
			logger.error(new StringBuilder("未找到").append(name).append("任务记录").toString());
			return false;
		}
		ccReportBuildStatus.set("report_build_status", reportStatue);
		ccReportBuildStatus.set("report_build_elapse_time", elapseTime);
		ccReportBuildStatus.set("is_build_finish", isFinish == null ? Boolean.FALSE : isFinish);
		ccReportBuildStatus.set("modify_date", date);
		return ccReportBuildStatus.update();
	}


}








