package com.gnet.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gnet.model.admin.CcEvaluteLevel;
import com.gnet.model.admin.CcTeacherCourse;
import com.gnet.utils.DictUtils;

/**
 * 考评点类型
 * 
 * @author SY
 * 
 * @date 2017年10月17日
 */
@Component("ccEvaluteLevelService")
public class CcEvaluteLevelService {

	/**
	 * 是否是数字
	 * @param str
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年10月18日 下午2:52:03 
	 */
	public static boolean isNum(String str) {
		try {
			new BigDecimal(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 计算用户的分数属于哪个分段
	 * @return
	 * @author SY
	 * @version 创建时间：2017年8月10日 下午5:38:51
	 */
	public String calculateToName(BigDecimal score, Integer level) {
		score = score.divide(new BigDecimal(1), 2, BigDecimal.ROUND_HALF_UP);
		String name = "";
		if(CcEvaluteLevel.LEVEL_TOW.equals(level)) {
			if(score.compareTo(new BigDecimal(0.5).setScale(2, BigDecimal.ROUND_HALF_UP)) == 1) {
				name = DictUtils.findLabelByTypeAndKey("evaluteLevelTow", CcEvaluteLevel.LEVEL_TOW_A);
			} else {
				name = DictUtils.findLabelByTypeAndKey("evaluteLevelTow", CcEvaluteLevel.LEVEL_TOW_B);
			}
		} else if(CcEvaluteLevel.LEVEL_FIVE.equals(level)) {
			if(score.compareTo(new BigDecimal(0.8).setScale(2, BigDecimal.ROUND_HALF_UP)) == 1) {
				name = DictUtils.findLabelByTypeAndKey("evaluteLevelFive", CcEvaluteLevel.LEVEL_FIVE_A);
			} else if(score.compareTo(new BigDecimal(0.6).setScale(2, BigDecimal.ROUND_HALF_UP)) == 1) {
				name = DictUtils.findLabelByTypeAndKey("evaluteLevelFive", CcEvaluteLevel.LEVEL_FIVE_B);
			} else if(score.compareTo(new BigDecimal(0.4).setScale(2, BigDecimal.ROUND_HALF_UP)) == 1) {
				name = DictUtils.findLabelByTypeAndKey("evaluteLevelFive", CcEvaluteLevel.LEVEL_FIVE_C);
			} else if(score.compareTo(new BigDecimal(0.2).setScale(2, BigDecimal.ROUND_HALF_UP)) == 1) {
				name = DictUtils.findLabelByTypeAndKey("evaluteLevelFive", CcEvaluteLevel.LEVEL_FIVE_D);
			} else {
				name = DictUtils.findLabelByTypeAndKey("evaluteLevelFive", CcEvaluteLevel.LEVEL_FIVE_E);
			}
		} else {
			name = "错误数据！";
		}
		return name;
	}
	
	/**
	 * 计算用户的分数属于哪个分段
	 * @return
	 * @author SY
	 * @version 创建时间：2017年8月10日 下午5:38:51
	 */
	public String calculateToNameValue(BigDecimal score, Integer level) {
		score = score.divide(new BigDecimal(1), 2, BigDecimal.ROUND_HALF_UP);
		String name = "";
		if(CcEvaluteLevel.LEVEL_TOW.equals(level)) {
			if(score.compareTo(new BigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_UP)) == 1) {
				name = "错误数据！";
			} else if(score.compareTo(new BigDecimal(0.5).setScale(2, BigDecimal.ROUND_HALF_UP)) == 1) {
				name = DictUtils.findLabelByTypeAndKey("evaluteLevelTowValue", CcEvaluteLevel.LEVEL_TOW_A);
			} else {
				name = DictUtils.findLabelByTypeAndKey("evaluteLevelTowValue", CcEvaluteLevel.LEVEL_TOW_B);
			}
		} else if(CcEvaluteLevel.LEVEL_FIVE.equals(level)) {
			if(score.compareTo(new BigDecimal(1).setScale(2, BigDecimal.ROUND_HALF_UP)) == 1) {
				name = "错误数据！";
			} else if(score.compareTo(new BigDecimal(0.8).setScale(2, BigDecimal.ROUND_HALF_UP)) == 1) {
				name = DictUtils.findLabelByTypeAndKey("evaluteLevelFiveValue", CcEvaluteLevel.LEVEL_FIVE_A);
			} else if(score.compareTo(new BigDecimal(0.6).setScale(2, BigDecimal.ROUND_HALF_UP)) == 1) {
				name = DictUtils.findLabelByTypeAndKey("evaluteLevelFiveValue", CcEvaluteLevel.LEVEL_FIVE_B);
			} else if(score.compareTo(new BigDecimal(0.4).setScale(2, BigDecimal.ROUND_HALF_UP)) == 1) {
				name = DictUtils.findLabelByTypeAndKey("evaluteLevelFiveValue", CcEvaluteLevel.LEVEL_FIVE_C);
			} else if(score.compareTo(new BigDecimal(0.2).setScale(2, BigDecimal.ROUND_HALF_UP)) == 1) {
				name = DictUtils.findLabelByTypeAndKey("evaluteLevelFiveValue", CcEvaluteLevel.LEVEL_FIVE_D);
			} else {
				name = DictUtils.findLabelByTypeAndKey("evaluteLevelFiveValue", CcEvaluteLevel.LEVEL_FIVE_E);
			}
		} else {
			name = "错误数据！";
		}
		return name;
	}
	
	/**
	 * 通过中文获取分数应该是多少
	 * @param valueString
	 * @param level
	 * 			层次类型
	 * @author SY 
	 * @version 创建时间：2017年10月23日 上午10:19:48 
	 */
	public BigDecimal caculateToScore(String valueString, Integer level) {
		BigDecimal score = null;
		valueString = valueString.trim();
		if(CcEvaluteLevel.LEVEL_TOW.equals(level)) {
			if(valueString.equals(DictUtils.findLabelByTypeAndKey("evaluteLevelTowValue", CcEvaluteLevel.LEVEL_TOW_A))) {
				score = CcEvaluteLevel.LEVEL_TOW_A_VALUE;
			} else if(valueString.equals(DictUtils.findLabelByTypeAndKey("evaluteLevelTowValue", CcEvaluteLevel.LEVEL_TOW_B))) {
				score = CcEvaluteLevel.LEVEL_TOW_B_VALUE;
			}
		} else if(CcEvaluteLevel.LEVEL_FIVE.equals(level)) {
			if(valueString.equals(DictUtils.findLabelByTypeAndKey("evaluteLevelFiveValue", CcEvaluteLevel.LEVEL_FIVE_A))) {
				score = CcEvaluteLevel.LEVEL_FIVE_A_VALUE;
			} else if(valueString.equals(DictUtils.findLabelByTypeAndKey("evaluteLevelFiveValue", CcEvaluteLevel.LEVEL_FIVE_B))) {
				score = CcEvaluteLevel.LEVEL_FIVE_B_VALUE;
			} else if(valueString.equals(DictUtils.findLabelByTypeAndKey("evaluteLevelFiveValue", CcEvaluteLevel.LEVEL_FIVE_C))) {
				score = CcEvaluteLevel.LEVEL_FIVE_C_VALUE;
			} else if(valueString.equals(DictUtils.findLabelByTypeAndKey("evaluteLevelFiveValue", CcEvaluteLevel.LEVEL_FIVE_D))) {
				score = CcEvaluteLevel.LEVEL_FIVE_D_VALUE;
			} else if(valueString.equals(DictUtils.findLabelByTypeAndKey("evaluteLevelFiveValue", CcEvaluteLevel.LEVEL_FIVE_E))) {
				score = CcEvaluteLevel.LEVEL_FIVE_E_VALUE;
			}
		}
		
		return score;
		
	}
	
	/**
	 * 根据上传的数据，返回id，数据可能是：分数、中文。
	 * @param value
	 * 			excel的数据
	 * @param evaluteLevelMap
	 * 			考评点等级map<indicationId, Map<分数，levelId>>
	 * @param indicationId
	 * 			指标点  
	 * @return Map {
	 * 				isSuccess : booolean,
	 * 				levelId : Long
	 * 				failMessage: string
	 * 			}
	 * @author SY 
	 * @version 创建时间：2017年10月22日 下午4:13:16
	 */
	public Map<String, Object> calculateToLevelId(Object value, Map<Long, Map<BigDecimal, Long>> evaluteLevelMap, Long indicationId) {
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("isSuccess", Boolean.TRUE);
		returnMap.put("levelId", null);
		Long levelId = null;
		BigDecimal score = null;
		String name = null;
		
		if(value == null) {
			returnMap.put("isSuccess", Boolean.FALSE);
			returnMap.put("failMessage", "数值不得为空！");
			return returnMap;
		}
		String valueString = value.toString();
		
		Map<BigDecimal, Long> evaluteScoreAndLevelIdMap = evaluteLevelMap.get(indicationId);
		if(evaluteScoreAndLevelIdMap == null || evaluteScoreAndLevelIdMap.isEmpty()) {
			returnMap.put("isSuccess", Boolean.FALSE);
			returnMap.put("failMessage", "不存在考评点编号为"+indicationId+"下的考评点层次！");
			return returnMap;
		}
		if(evaluteScoreAndLevelIdMap.size() == 2) {
			if(!isNum(valueString)) {
				score = caculateToScore(valueString, CcEvaluteLevel.LEVEL_TOW);
				if(score == null) {
					returnMap.put("isSuccess", Boolean.FALSE);
					returnMap.put("failMessage", "不存在当前的考评点层次！");
					return returnMap;
				}
			} else {
				name = calculateToNameValue(new BigDecimal(valueString), CcEvaluteLevel.LEVEL_TOW);
				score = caculateToScore(name, CcEvaluteLevel.LEVEL_TOW);
			}
		} else if(evaluteScoreAndLevelIdMap.size() == 5) {
			if(!isNum(valueString)) {
				score = caculateToScore(valueString, CcEvaluteLevel.LEVEL_FIVE);
				if(score == null) {
					returnMap.put("isSuccess", Boolean.FALSE);
					returnMap.put("failMessage", "不存在当前的考评点层次！");
					return returnMap;
				}
			} else {
				name = calculateToNameValue(new BigDecimal(valueString), CcEvaluteLevel.LEVEL_FIVE);
				score = caculateToScore(name, CcEvaluteLevel.LEVEL_FIVE);
			}
		} else {
			returnMap.put("isSuccess", Boolean.FALSE);
			returnMap.put("failMessage", "考评点编号为"+indicationId+"下的考评点层次数量不符合设置！");
			return returnMap;
		}
		
		if(score == null) {
			returnMap.put("isSuccess", Boolean.FALSE);
			returnMap.put("failMessage", "考评点编号为"+indicationId+"下的考评点层次不符合设置！");
			return returnMap;
		}
		score = score.setScale(3);
		levelId = evaluteLevelMap.get(indicationId).get(score);
		if(levelId == null) {
			returnMap.put("isSuccess", Boolean.FALSE);
			returnMap.put("failMessage", "考评点编号为"+indicationId+"下的考评点层次不符合设置！");
			return returnMap;
		}
		returnMap.put("levelId", levelId);
		return returnMap;
	}
	
	/**
	 * @param eduClassId
	 * @return
	 * 			 Map<indicationId, Map<分数, levelId>>
	 * @author SY 
	 * @version 创建时间：2017年10月17日 下午2:22:50 
	 */
	public Map<Long, Map<BigDecimal, Long>> getIndicationAndEvaluteIdMap(Long eduClassId) {
		Map<Long, Map<BigDecimal, Long>> reutrnMap = new HashMap<>();
		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findCourseByClassId(eduClassId);
		List<CcEvaluteLevel> ccEvaluteLevels = CcEvaluteLevel.dao.findFilteredByColumn("teacher_course_id", ccTeacherCourse.getLong("id"));
		
		for(CcEvaluteLevel ccEvaluteLevel : ccEvaluteLevels) {
			Long indicationId = ccEvaluteLevel.getLong("indication_id");
			Long levelId = ccEvaluteLevel.getLong("id");
			BigDecimal score = ccEvaluteLevel.getBigDecimal("score");
			
			Map<BigDecimal, Long> indicationMap = reutrnMap.get(indicationId);
			if(indicationMap == null) {
				indicationMap = new HashMap<>();
				reutrnMap.put(indicationId, indicationMap);
			}
			indicationMap.put(score, levelId);
		}
		
		return reutrnMap;
	}


}
