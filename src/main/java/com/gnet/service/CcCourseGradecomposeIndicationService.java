package com.gnet.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.gnet.model.admin.CcCourseGradecompose;
import com.gnet.model.admin.CcCourseGradecomposeIndication;
import com.gnet.model.admin.CcGradecompose;
import com.gnet.model.admin.CcTeacherCourse;

/**
 * 开课课程成绩组成元素指标点关联表
 * 
 * @author SY
 * 
 * @date 2017年9月8日
 */
@Component("ccCourseGradecomposeIndicationService")
public class CcCourseGradecomposeIndicationService {
	
	/**
	 * 返回验证是否通过,验证是否所有教师开课的成绩组成相同 
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年12月20日 下午5:29:06 
	 */
	public boolean validatorSameGradecomposeIndication(List<CcTeacherCourse> ccTeacherCourseList) {
		/*
		 * 1. 验证开课类型是否一样
		 * 2. 验证开课成绩组成是否一样。
		 * 3. 验证开课成绩组成和指标点关联是否一样。
		 */
		// 1. 验证开课类型是否一样
		List<Long> teacherCourseIdList = new ArrayList<>();
		Integer lastType = null;
		for(CcTeacherCourse temp : ccTeacherCourseList) {
			Long teacherCourseId = temp.getLong("id"); 
			Integer type = temp.getInt("result_type"); 
			teacherCourseIdList.add(teacherCourseId);
			if(lastType != null && !type.equals(lastType)) {
				// 当不同教师开课的考评类型不同，则无法计算，直接返回错误
				return false;
			}
			lastType = type;
		}
		
		// 2. 验证开课成绩组成是否一样。
		List<Long> lastGradecomposeIdList = new ArrayList<>();
		List<Long> courseGradecomposeIdList = new ArrayList<>();
		List<CcGradecompose> ccGradecomposeList = CcGradecompose.dao.findByTeacherCourseIds(teacherCourseIdList.toArray(new Long[teacherCourseIdList.size()]));
		// teacherCourseId, List<gradecomposeId>
		Map<Long, List<Long>> gradecomposeIdMap = new HashMap<>();
		// courseGradecomposeId, List<gradecomposeId>
		Map<Long, List<Long>> courseGradecomposeIdMap = new HashMap<>();
		for(CcGradecompose temp : ccGradecomposeList) {
			Long gradecomposeId = temp.getLong("id");
			Long teacherCourseId = temp.getLong("teacherCourseId");
			List<Long> thisGradecomposeIdList = gradecomposeIdMap.get(teacherCourseId);
			if(thisGradecomposeIdList == null || thisGradecomposeIdList.isEmpty()) {
				thisGradecomposeIdList = new ArrayList<>();
				gradecomposeIdMap.put(teacherCourseId, thisGradecomposeIdList);
			}
			if(!thisGradecomposeIdList.contains(gradecomposeId)) {
				// 防止重复添加
				thisGradecomposeIdList.add(gradecomposeId);
			}

			Long courseGradecomposeId = temp.getLong("courseGradecomposeId");
			List<Long> thisCourseGradecomposeIdList = courseGradecomposeIdMap.get(teacherCourseId);
			if(thisCourseGradecomposeIdList == null || thisCourseGradecomposeIdList.isEmpty()) {
				thisCourseGradecomposeIdList = new ArrayList<>();
				courseGradecomposeIdMap.put(teacherCourseId, thisCourseGradecomposeIdList);
			}
			thisCourseGradecomposeIdList.add(courseGradecomposeId);
			courseGradecomposeIdList.add(courseGradecomposeId);
		}
		for(CcTeacherCourse temp : ccTeacherCourseList) {
			Long teacherCourseId = temp.getLong("id");
			List<Long> thisGradecomposeIdList = gradecomposeIdMap.get(teacherCourseId);
			if(!lastGradecomposeIdList.isEmpty()) {
				// 当非空时候，验证是否和上次的成绩组成相同
				if(thisGradecomposeIdList.size() != lastGradecomposeIdList.size()) {
					// 当不同教师开课的成绩组成数量不同，则无法计算，直接返回错误
					return false;
				}
				if(!lastGradecomposeIdList.containsAll(thisGradecomposeIdList)) {
					// 当不同教师开课的成绩组成对象不同，则无法计算，直接返回错误
					return false;
				}
			} else {
				lastGradecomposeIdList = thisGradecomposeIdList;
			}
		}
		
		// 3. 验证开课成绩组成和指标点关联是否一样。
		// Map<teacherCourse, Map<'indicationId,gradecomposeId',CcCourseGradecomposeIndication>>
		Map<Long, Map<String, CcCourseGradecomposeIndication>> teacherCourseCcCourseGradecomposeIndicationMap = new HashMap<>();
		List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndications = CcCourseGradecomposeIndication.dao.findByCourseGradecomposeIds(courseGradecomposeIdList.toArray(new Long[courseGradecomposeIdList.size()]));
		for(CcCourseGradecomposeIndication temp : ccCourseGradecomposeIndications) {
			Long indicationId = temp.getLong("indication_id");
			Long teacherCourseId = temp.getLong("teacherCourseId");
			Long gradecomposeId = temp.getLong("gradecomposeId");
			
			Map<String, CcCourseGradecomposeIndication> ccCourseGradecomposeIndicationMap = teacherCourseCcCourseGradecomposeIndicationMap.get(teacherCourseId);
			if(ccCourseGradecomposeIndicationMap == null || ccCourseGradecomposeIndicationMap.isEmpty()) {
				ccCourseGradecomposeIndicationMap = new HashMap<>();
				teacherCourseCcCourseGradecomposeIndicationMap.put(teacherCourseId, ccCourseGradecomposeIndicationMap);
			}
			String indicationIdAndGradecomposeId = indicationId + "," + gradecomposeId;
			ccCourseGradecomposeIndicationMap.put(indicationIdAndGradecomposeId, temp);
		}
		// 判断每个教师开课的Map<'indicationId,gradecomposeId',CcCourseGradecomposeIndication>中间，是否同样成绩组成和指标点下的：权重、满分一样
		Map<String, CcCourseGradecomposeIndication> lastCcCourseGradecomposeIndicationMap = new HashMap<>();
		for(Entry<Long, Map<String, CcCourseGradecomposeIndication>> entry : teacherCourseCcCourseGradecomposeIndicationMap.entrySet()) {
			Map<String, CcCourseGradecomposeIndication> ccCourseGradecomposeIndicationMap = entry.getValue();
			if(lastCcCourseGradecomposeIndicationMap.isEmpty()) {
				// 如果是第一次，赋值
				lastCcCourseGradecomposeIndicationMap = ccCourseGradecomposeIndicationMap; 
			} else {
				// 否则比较是否权重、满分一样，并且包含了所有的内容
				if(ccCourseGradecomposeIndicationMap.size() != lastCcCourseGradecomposeIndicationMap.size()) {
					// 判断个数是否相同
					return false;
				}
				for(Entry<String, CcCourseGradecomposeIndication> ccCourseGradecomposeIndicationEntry : ccCourseGradecomposeIndicationMap.entrySet()) {
					String key = ccCourseGradecomposeIndicationEntry.getKey();
					CcCourseGradecomposeIndication newCcCourseGradecomposeIndication = ccCourseGradecomposeIndicationEntry.getValue();
					CcCourseGradecomposeIndication oldCcCourseGradecomposeIndication = lastCcCourseGradecomposeIndicationMap.get(key);
					if(oldCcCourseGradecomposeIndication == null) {
						// 如果不存在，直接返回错误
						return false;
					} else {
						// 否则比较权重和满分是否一样
						BigDecimal newWeight = newCcCourseGradecomposeIndication.getBigDecimal("weight");
						BigDecimal newMaxScore = newCcCourseGradecomposeIndication.getBigDecimal("max_score");
						BigDecimal oldWeight = oldCcCourseGradecomposeIndication.getBigDecimal("weight");
						BigDecimal oldMaxScore = oldCcCourseGradecomposeIndication.getBigDecimal("max_score");
						newWeight = newWeight.setScale(3, RoundingMode.HALF_UP);
						newMaxScore = newMaxScore.setScale(3, RoundingMode.HALF_UP);
						oldWeight = oldWeight.setScale(3, RoundingMode.HALF_UP);
						oldMaxScore = oldMaxScore.setScale(3, RoundingMode.HALF_UP);
						if(newWeight.compareTo(oldWeight) != 0) {
							// 如果权重不一样
							return false;
						}
						if(newMaxScore != null && oldMaxScore != null && newMaxScore.compareTo(oldMaxScore) != 0) {
							// 如果不为空，并且不一样
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * 通过开课课程成绩组成元素编号，获取该开课课程成绩组成元素，的指标点分数之和 。（指标点的分数之和  = 满分 - 其他分数）
	 * @param courseGradecompostId
	 * 			开课课程成绩组成元素编号
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年9月8日 下午3:20:44 
	 */
	public BigDecimal caculateCourseGradecomposeScoreButOtherScore(Long courseGradecompostId) {
		BigDecimal returnScore = new BigDecimal(0);
		List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndications = CcCourseGradecomposeIndication.dao.findFilteredByColumn("course_gradecompose_id", courseGradecompostId);
		for(CcCourseGradecomposeIndication temp : ccCourseGradecomposeIndications) {
			BigDecimal maxScore = temp.getBigDecimal("max_score") == null ? new BigDecimal(0) : temp.getBigDecimal("max_score");
			returnScore = returnScore.add(maxScore);
		}
		return returnScore;
	}
	
	/**
	 * 通过开课课程成绩组成元素编号，获取该开课课程成绩组成元素，的满分分值。（满分 = 其他分数 + 指标点的分数之和 ）
	 * @param courseGradecompostId
	 * 			开课课程成绩组成元素编号
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年9月8日 下午3:20:44 
	 */
	public BigDecimal caculateCourseGradecomposeScoreScore(Long courseGradecompostId) {
		BigDecimal returnScore = new BigDecimal(0);
		CcCourseGradecompose ccCourseGradecompose = CcCourseGradecompose.dao.findFilteredById(courseGradecompostId);
		BigDecimal otherScore = ccCourseGradecompose.getBigDecimal("other_score");
		List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndications = CcCourseGradecomposeIndication.dao.findFilteredByColumn("course_gradecompose_id", courseGradecompostId);
		for(CcCourseGradecomposeIndication temp : ccCourseGradecomposeIndications) {
			BigDecimal maxScore = temp.getBigDecimal("max_score");
			returnScore = returnScore.add(maxScore);
		}
		returnScore = returnScore.add(otherScore);
		return returnScore;
	}

	/**
	 * 通过教学班课程编号，获取当前教学班课程的所有  各自指标点的分数之和，并以map返回
	 * @param teacherCourseId
	 * @return
	 * 		map<couseGradecomposeId, fullScore>
	 * @author SY 
	 * @version 创建时间：2017年9月8日 下午4:13:04 
	 */
	public Map<Long, BigDecimal> caculateCourseGradecomposeScoreToMap(Long teacherCourseId) {
		Map<Long, BigDecimal> returnMap = new HashMap<>();
		List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndications = CcCourseGradecomposeIndication.dao.findByTeacherCourseId(teacherCourseId);
		for(CcCourseGradecomposeIndication temp : ccCourseGradecomposeIndications) {
			Long couseGradecomposeId = temp.getLong("course_gradecompose_id");
			BigDecimal maxScore = temp.getBigDecimal("max_score") == null ? new BigDecimal(0) : temp.getBigDecimal("max_score");
			BigDecimal oldMaxScore = returnMap.get(couseGradecomposeId);
			if(oldMaxScore == null) {
				oldMaxScore = new BigDecimal(0);
			}
			oldMaxScore = oldMaxScore.add(maxScore);
			returnMap.put(couseGradecomposeId, oldMaxScore);
		}
		return returnMap;
	}
}
