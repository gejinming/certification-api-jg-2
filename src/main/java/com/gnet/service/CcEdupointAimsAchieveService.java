package com.gnet.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.model.admin.CcCourseTargetIndication;
import com.gnet.model.admin.CcEduclass;
import com.gnet.model.admin.CcEduclassStudent;
import com.gnet.model.admin.CcEdupointAimsAchieve;
import com.gnet.model.admin.CcEdupointEachAimsAchieve;
import com.gnet.model.admin.CcIndication;
import com.gnet.model.admin.CcIndicationCourse;
import com.gnet.model.admin.CcTeacherCourse;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.log.Logger;

/**
 * 教学班指标点下课程目标达成度
 * @author SY
 * @date 2017年11月23日
 */
@Component("ccEdupointAimsAchieveService")
public class CcEdupointAimsAchieveService {

	private static final Logger logger = Logger.getLogger(CcEdupointAimsAchieveService.class);
	
	/**
	 * 统计教学班&&课程指标点数据下的课程目标达成度(考核分析法)
	 * 
	 * @param eduClassId 教学班编号
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean statisticsEdupointAimsAchieve(Long eduClassId) {
		/*
		 * 计算：
		 * 1. 删掉以前的教学班指标点下课程目标达成度（由于存在剔除学生成绩，所以先获取全部用于计算）
		 * 2. 获取课程的指标点，对每个指标点遍历
		 * 3. 获取教学班各个课程目标达成度达成度数据，然后取最小值
		 * 4. 获取指标点的权重
		 * 5. 最小值乘以权重
		 */
		Date date = new Date();
		List<CcEdupointAimsAchieve> CcEdupointAimsAchieveOldList = CcEdupointAimsAchieve.dao.findByColumn("educlass_id", eduClassId);
		// Map<"educlass_id,indication_course_id", CcEdupointAimsAchieve>
		Map<String, CcEdupointAimsAchieve> CcEdupointAimsAchieveOldMap = new HashMap<>();
        for(CcEdupointAimsAchieve temp : CcEdupointAimsAchieveOldList) {
        	String key = temp.getLong("educlass_id")+","+temp.getLong("indication_course_id");
        	CcEdupointAimsAchieveOldMap.put(key, temp);
        }
        
		// 1. 删除以前的数据
		if(!CcEdupointAimsAchieve.dao.deleteAllByColumn("educlass_id", eduClassId, date)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			logger.info(new StringBuilder("达成度计算：教学班(编号为").append(eduClassId.toString()).append(")统计失败。因为删除老数据失败。").toString());
		}
		Long studentSize = CcEduclassStudent.dao.countFiltered("class_id", eduClassId);
		// 班级没有人存在时返回错误信息
		if (studentSize == 0L) {
			logger.info(new StringBuilder("达成度计算：教学班(编号为")
					.append(eduClassId.toString()).append(")下的学生数为0")
					.toString());
			
			return true;
		}
		
		CcEduclass ccEduclass = CcEduclass.dao.findFilteredById(eduClassId);
		Long teacherCourseId = ccEduclass.getLong("teacher_course_id");
		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findFilteredById(teacherCourseId);
		Long courseId = ccTeacherCourse.getLong("course_id");
		// 指标点与课程关系表
		List<CcIndicationCourse> ccIndicationCourses = CcIndicationCourse.dao.findFilteredByColumn("course_id", courseId);
		List<Long> indicationCourseIdList = new ArrayList<>();
		Map<Long, CcIndicationCourse> ccIndicationCourseMap = new HashMap<>();
		for(CcIndicationCourse indicationCourseTemp : ccIndicationCourses) {
			Long ccIndicationCourseId = indicationCourseTemp.getLong("id");
			indicationCourseIdList.add(ccIndicationCourseId);
			
			ccIndicationCourseMap.put(ccIndicationCourseId, indicationCourseTemp);
		}
		// 获取课程目标与指标点与课程关系的关系表
		List<CcCourseTargetIndication> ccCourseTargetIndications = CcCourseTargetIndication.dao.findByColumnIn("indication_course_id", indicationCourseIdList.toArray(new Long[indicationCourseIdList.size()]));
		// 获取课程目标
		List<CcIndication> ccIndicationList = CcIndication.dao.findByColumn("course_id", courseId);
		Map<Long, CcIndication> ccIndicationMap = new HashMap<>();
		for(CcIndication tempIndication : ccIndicationList) {
			Long indicationId = tempIndication.getLong("id");
			ccIndicationMap.put(indicationId, tempIndication);
		}
		// 获取map《指标点课程编号（ccIndicationCourses.id），List<课程目标id>》
		Map<Long, List<Long>> indicatorPointAndIndicationMap = new HashMap<>(); 
		// 遍历关系关系表，填充Map
		for(CcCourseTargetIndication ccCourseTargetIndicationTemp : ccCourseTargetIndications) {
			Long indicationId = ccCourseTargetIndicationTemp.getLong("indication_id");
			Long indicationCourseId = ccCourseTargetIndicationTemp.getLong("indication_course_id");
			List<Long> indicationIdList = indicatorPointAndIndicationMap.get(indicationCourseId);
			if(indicationIdList == null || indicationIdList.isEmpty()) {
				indicationIdList = new ArrayList<>();
				indicatorPointAndIndicationMap.put(indicationCourseId, indicationIdList);
			}
			indicationIdList.add(indicationId);
		}
		// 获取已经生成完毕的每个课程目标
		List<CcEdupointEachAimsAchieve> ccEdupointEachAimsAchieves = CcEdupointEachAimsAchieve.dao.findByColumn("educlass_id", eduClassId);
		
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		// 用于保存的教学班指标点下课程目标达成度
		List<CcEdupointAimsAchieve> ccEdupointAimsAchievesSave = new ArrayList<>();
		// 遍历每个课程指标点
		for(Entry<Long, List<Long>> entry : indicatorPointAndIndicationMap.entrySet()) {
			// 课程指标点编号
			Long indicationCourseId = entry.getKey();
			List<Long> indicationList = entry.getValue();
			// 达成度结果
			BigDecimal achieveValue = new BigDecimal(0);
			// 遍历每个教学办下的课程目标达成度
			for(CcEdupointEachAimsAchieve ccEdupointEachAimsAchieveTemp : ccEdupointEachAimsAchieves) {
				BigDecimal achieveValueEach = ccEdupointEachAimsAchieveTemp.getBigDecimal("achieve_value");
				Long indicationId = ccEdupointEachAimsAchieveTemp.getLong("indication_id");
				// 现在的达成度是没有除以期望的，所以现在再除以期望
				BigDecimal expectedValue = ccIndicationMap.get(indicationId).getBigDecimal("expected_value");
				achieveValueEach = achieveValueEach.divide(expectedValue, 3, RoundingMode.HALF_UP);
				// 如果当前课程指标点包含这个课程达成度，并且达成度更加小，则用更加小的
				if(indicationList.contains(indicationId) && achieveValueEach.compareTo(new BigDecimal(0)) != 0 && (achieveValueEach.compareTo(achieveValue) == -1 || achieveValue.compareTo(new BigDecimal(0)) == 0)) {
					// 如果更加小，则替代
					achieveValue = achieveValueEach;
				}
			}
			
			// 5. 最小值乘以权重
			BigDecimal weight = ccIndicationCourseMap.get(indicationCourseId).getBigDecimal("weight");
			achieveValue = achieveValue.multiply(weight);
			
			// 以前的值
			String key = eduClassId + "," + indicationCourseId;
			CcEdupointAimsAchieve ccEdupointEachAimsAchieveOld = CcEdupointAimsAchieveOldMap.get(key);
			
			
			CcEdupointAimsAchieve saveTemp = new CcEdupointAimsAchieve();
			saveTemp.set("id", idGenerate.getNextValue());
			saveTemp.set("create_date", date);
			saveTemp.set("modify_date", date);
			saveTemp.set("educlass_id", eduClassId);
			saveTemp.set("indication_course_id", indicationCourseId);
			if(ccEdupointEachAimsAchieveOld != null) {
				// 老的数据放进去
				saveTemp.set("achieve_value", ccEdupointEachAimsAchieveOld.getBigDecimal("achieve_value"));
				saveTemp.set("except_achieve_value", ccEdupointEachAimsAchieveOld.getBigDecimal("except_achieve_value"));            	
			}
			// 新的数据更新
			saveTemp.set("achieve_value", achieveValue);
			
			ccEdupointAimsAchievesSave.add(saveTemp);
		}
		
		// 保存数据
		if(!CcEdupointAimsAchieve.dao.batchSave(ccEdupointAimsAchievesSave)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		
		return true;
	}
	
	/**
	 * 统计教学班&&课程指标点数据下的课程目标达成度(考核分析法-剔除部分学生之后)
	 * 
	 * @param eduClassId 教学班编号
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean statisticsEdupointAimsAchieveExcept(Long eduClassId) {
		/*
		 * 计算：
		 * 1. 删掉以前的教学班指标点下课程目标达成度（由于存在剔除学生成绩，所以先获取全部用于计算）
		 * 2. 获取课程的指标点，对每个指标点遍历
		 * 3. 获取教学班各个课程目标达成度达成度数据，然后取最小值
		 * 4. 获取指标点的权重
		 * 5. 最小值乘以权重
		 */
		Date date = new Date();
		List<CcEdupointAimsAchieve> CcEdupointAimsAchieveOldList = CcEdupointAimsAchieve.dao.findByColumn("educlass_id", eduClassId);
		// Map<"educlass_id,indication_course_id", CcEdupointAimsAchieve>
		Map<String, CcEdupointAimsAchieve> CcEdupointAimsAchieveOldMap = new HashMap<>();
		for(CcEdupointAimsAchieve temp : CcEdupointAimsAchieveOldList) {
			String key = temp.getLong("educlass_id")+","+temp.getLong("indication_course_id");
			CcEdupointAimsAchieveOldMap.put(key, temp);
		}
		
		// 1. 删除以前的数据
		if(!CcEdupointAimsAchieve.dao.deleteAllByColumn("educlass_id", eduClassId, date)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			logger.info(new StringBuilder("达成度计算：教学班(编号为").append(eduClassId.toString()).append(")统计失败。因为删除老数据失败。").toString());
		}
		Long studentSize = CcEduclassStudent.dao.countFiltered("class_id", eduClassId);
		// 班级没有人存在时返回错误信息
		if (studentSize == 0L) {
			logger.info(new StringBuilder("达成度计算：教学班(编号为")
					.append(eduClassId.toString()).append(")下的学生数为0")
					.toString());
			
			return true;
		}
		
		CcEduclass ccEduclass = CcEduclass.dao.findFilteredById(eduClassId);
		Long teacherCourseId = ccEduclass.getLong("teacher_course_id");
		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findFilteredById(teacherCourseId);
		Long courseId = ccTeacherCourse.getLong("course_id");
		// 指标点与课程关系表
		List<CcIndicationCourse> ccIndicationCourses = CcIndicationCourse.dao.findFilteredByColumn("course_id", courseId);
		List<Long> indicationCourseIdList = new ArrayList<>();
		Map<Long, CcIndicationCourse> ccIndicationCourseMap = new HashMap<>();
		for(CcIndicationCourse indicationCourseTemp : ccIndicationCourses) {
			Long ccIndicationCourseId = indicationCourseTemp.getLong("id");
			indicationCourseIdList.add(ccIndicationCourseId);
			
			ccIndicationCourseMap.put(ccIndicationCourseId, indicationCourseTemp);
		}
		// 获取课程目标与指标点与课程关系的关系表
		List<CcCourseTargetIndication> ccCourseTargetIndications = CcCourseTargetIndication.dao.findByColumnIn("indication_course_id", indicationCourseIdList.toArray(new Long[indicationCourseIdList.size()]));
		// 获取课程目标
		List<CcIndication> ccIndicationList = CcIndication.dao.findByColumn("course_id", courseId);
		Map<Long, CcIndication> ccIndicationMap = new HashMap<>();
		for(CcIndication tempIndication : ccIndicationList) {
			Long indicationId = tempIndication.getLong("id");
			ccIndicationMap.put(indicationId, tempIndication);
		}
		// 获取map《指标点课程编号（ccIndicationCourses.id），List<课程目标id>》
		Map<Long, List<Long>> indicatorPointAndIndicationMap = new HashMap<>(); 
		// 遍历关系关系表，填充Map
		for(CcCourseTargetIndication ccCourseTargetIndicationTemp : ccCourseTargetIndications) {
			Long indicationId = ccCourseTargetIndicationTemp.getLong("indication_id");
			Long indicationCourseId = ccCourseTargetIndicationTemp.getLong("indication_course_id");
			List<Long> indicationIdList = indicatorPointAndIndicationMap.get(indicationCourseId);
			if(indicationIdList == null || indicationIdList.isEmpty()) {
				indicationIdList = new ArrayList<>();
				indicatorPointAndIndicationMap.put(indicationCourseId, indicationIdList);
			}
			indicationIdList.add(indicationId);
		}
		// 获取已经生成完毕的每个课程目标
		List<CcEdupointEachAimsAchieve> ccEdupointEachAimsAchieves = CcEdupointEachAimsAchieve.dao.findByColumn("educlass_id", eduClassId);
		
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		// 用于保存的教学班指标点下课程目标达成度
		List<CcEdupointAimsAchieve> ccEdupointAimsAchievesSave = new ArrayList<>();
		// 遍历每个课程指标点
		for(Entry<Long, List<Long>> entry : indicatorPointAndIndicationMap.entrySet()) {
			// 课程指标点编号
			Long indicationCourseId = entry.getKey();
			List<Long> indicationList = entry.getValue();
			// 达成度结果
			BigDecimal achieveValue = new BigDecimal(0);
			// 遍历每个教学办下的课程目标达成度
			for(CcEdupointEachAimsAchieve ccEdupointEachAimsAchieveTemp : ccEdupointEachAimsAchieves) {
				BigDecimal exceptAchieveValueEach = ccEdupointEachAimsAchieveTemp.getBigDecimal("except_achieve_value");
				Long indicationId = ccEdupointEachAimsAchieveTemp.getLong("indication_id");
				// 现在的达成度是没有除以期望的，所以现在再除以期望
				BigDecimal expectedValue = ccIndicationMap.get(indicationId).getBigDecimal("expected_value");
				exceptAchieveValueEach = exceptAchieveValueEach.divide(expectedValue, 3, RoundingMode.HALF_UP);
				// 如果当前课程指标点包含这个课程达成度，并且达成度更加小，则用更加小的
				if(indicationList.contains(indicationId) && exceptAchieveValueEach.compareTo(new BigDecimal(0)) != 0 && (exceptAchieveValueEach.compareTo(achieveValue) == -1 || achieveValue.compareTo(new BigDecimal(0)) == 0)) {
					// 如果更加小，则替代
					achieveValue = exceptAchieveValueEach;
				}
			}
			
			// 5. 最小值乘以权重
			BigDecimal weight = ccIndicationCourseMap.get(indicationCourseId).getBigDecimal("weight");
			achieveValue = achieveValue.multiply(weight);
			
			// 以前的值
			String key = eduClassId + "," + indicationCourseId;
			CcEdupointAimsAchieve ccEdupointEachAimsAchieveOld = CcEdupointAimsAchieveOldMap.get(key);
			
			
			CcEdupointAimsAchieve saveTemp = new CcEdupointAimsAchieve();
			saveTemp.set("id", idGenerate.getNextValue());
			saveTemp.set("create_date", date);
			saveTemp.set("modify_date", date);
			saveTemp.set("educlass_id", eduClassId);
			saveTemp.set("indication_course_id", indicationCourseId);
			if(ccEdupointEachAimsAchieveOld != null) {
				// 老的数据放进去
				saveTemp.set("achieve_value", ccEdupointEachAimsAchieveOld.getBigDecimal("achieve_value"));
				saveTemp.set("except_achieve_value", ccEdupointEachAimsAchieveOld.getBigDecimal("except_achieve_value"));            	
			}
			// 新的数据更新
			saveTemp.set("except_achieve_value", achieveValue);
			
			ccEdupointAimsAchievesSave.add(saveTemp);
		}
		
		// 保存数据
		if(!CcEdupointAimsAchieve.dao.batchSave(ccEdupointAimsAchievesSave)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		
		return true;
	}
}
