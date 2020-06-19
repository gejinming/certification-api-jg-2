package com.gnet.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.model.admin.CcCourse;
import com.gnet.model.admin.CcGraduate;
import com.gnet.model.admin.CcIndicationCourse;
import com.gnet.model.admin.CcIndicatorPoint;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;

/**
 * @author SY
 * @date 2018年1月11日
 */
@Component("ccGraduateService")
public class CcGraduateService {

	/**
	 * 验证即将导入的毕业要求数据是否正确
	 * @param graduateList
	 * 			内容
	 * @param graduateVerId
	 * 			毕业要求版本编号
	 * @author SY 
	 * @version 创建时间：2018年1月11日 下午2:25:06 
	 */
	public boolean validateImportList(List<Record> graduateList, Long graduateVerId) {
		// 验证数据需要数据结构准备
		List<Record> errorList = Lists.newArrayList();

		/*
		 *  1. 验证当前数据需要是否重复
		 *  2. 验证当前课程名称对应课程是否唯一
		 */
		// 找到当前毕业要求版本已经存在的数据，用于对比
		List<CcGraduate> ccGraduates = CcGraduate.dao.findFilteredByColumn("graduate_ver_id", graduateVerId);
		// 制作成为map
		Map<String, String> ccGraduateMap = new HashMap<>();
		// excel内的数据形成的map，用于检验当前excel是否重复
		Map<String, String> currentGraduateMap = new HashMap<>();
		for(CcGraduate temp : ccGraduates) {
			Integer indexNum = temp.getInt("index_num");
			String content = temp.getStr("content");
			ccGraduateMap.put(indexNum+"", content);
		}
		
		// 获取所有的课程名称，变成map，用于获取excel中课程名对应的课程编号
		List<CcCourse> ccCourses = CcCourse.dao.findFilteredByColumn("plan_id", graduateVerId);
		Map<String, Long> ccCourseMap = new HashMap<>();
		// 课程名称重复的map
		Map<String, Long> ccCourseManyMap = new HashMap<>();
		for(CcCourse temp : ccCourses) {
			// 注意，我这里的处理逻辑是，一旦发现重复的，就全部去掉，用于在下面验证课程名是属于哪个couseId的时候，直接说找不到或者重复。
			String courseName = temp.getStr("name");
			Long courseId = temp.getLong("id");
			if(ccCourseMap.containsKey(courseName)) {
				ccCourseMap.remove(courseName);
				ccCourseManyMap.put(courseName, courseId);
			} else {
				ccCourseMap.put(courseName, courseId);
			}
		}
		
		// 上一次的数据
		String preGraduateIndexNum = null;
		String preGraduateContent = null;
		// 上一次指标点的序号是多少
		Integer preIndIndexNum = 0;
		String preIndContent = null;
		// 2018年2月28日 检查 毕业要求的同一个指标点的权重之和，不能大于1. Map<'index,indName', weight>
		Map<String, BigDecimal> indexAndIndNameWeightMap = new HashMap<>();
		for (int i = 0; i < graduateList.size(); i++) {
			Record graduate = graduateList.get(i);
			// 初始化isError为False
			graduate.set("graduateVerId", graduateVerId);
			graduate.set("isError", Boolean.FALSE);
			graduate.set("reasons", new ArrayList<String>());
			
			// 毕业要求需要以及内容， 注意，这里可能是空的，就延续使用之前的
			String graduateContentAndIndex = graduate.getStr("graduateIndexAndContent");
			// 指标点内容， 注意，这里可能是空的，就延续使用之前的
			String indName = graduate.getStr("indName");
			// 课程名称
			String courseName = graduate.getStr("courseName");
			
			// 验证非空字段是否都存在
			if (StrKit.isBlank(graduate.getStr("courseName")) || StrKit.isBlank(graduate.getStr("weight"))) {
				errorList.add(graduate);
				graduate.set("isError", Boolean.TRUE);
				List<String> reasons = graduate.get("reasons");
				reasons.add("毕业要求版本对应课程缺少课程名称、权重必填字段的其中之一。");
			}
			
			// 如果第一次就发现毕业要求是空的或者指标是空的，返回错误
			if(graduateContentAndIndex == null && preGraduateContent == null) {
				errorList.add(graduate);
				graduate.set("isError", Boolean.TRUE);
				List<String> reasons = graduate.get("reasons");
				reasons.add("毕业要求内容为空。");
				continue;
			}
			
			/************************************ 验证毕业要求是否正确 Start *********************************************/
			// 毕业要求序号 
			String graduateIndexNum = null;
			// 毕业要求内容
			String graduateContent = null;
			// 如果是第一次，数据都有。
			if(graduateContentAndIndex != null) {
				graduateIndexNum = StringUtils.substringBefore(graduateContentAndIndex, "."); 
				graduateContent = StringUtils.substringAfter(graduateContentAndIndex, "."); 
				// 验证当前数据是否和excel重复
				if(currentGraduateMap.get(graduateIndexNum) != null) {
					errorList.add(graduate);
					graduate.set("isError", Boolean.TRUE);
					List<String> reasons = graduate.get("reasons");
					reasons.add("此序号的毕业要求版本在本Excel重复。");
				} else if(ccGraduateMap.get(graduateIndexNum) != null) {
					// 验证当前数据是否和数据库重复
					errorList.add(graduate);
					graduate.set("isError", Boolean.TRUE);
					List<String> reasons = graduate.get("reasons");
					reasons.add("该版本已存在此序号的毕业要求。");
					
					// 不重复于当前excel但是存在于数据库
					currentGraduateMap.put(graduateIndexNum, graduateContent);
				} else {
					// 不重复于Excel和数据库
					currentGraduateMap.put(graduateIndexNum, graduateContent);
//					ccGraduateMap.put(graduateIndexNum, graduateContent);
				}
				
				// 当重新有毕业要求的时候，等于指标点重新计数
				preIndIndexNum = 0;
				preGraduateIndexNum = graduateIndexNum;
				preGraduateContent = graduateContent;
			} else if(graduateContentAndIndex == null && preGraduateContent != null) {
				// 如果是第二次获取了
				graduateIndexNum = preGraduateIndexNum;
				graduateContent = preGraduateContent;
				if(ccGraduateMap.get(graduateIndexNum) != null) {
					// 验证当前数据是否和数据库重复
					errorList.add(graduate);
					graduate.set("isError", Boolean.TRUE);
					List<String> reasons = graduate.get("reasons");
					reasons.add("该版本已存在此序号的毕业要求。");
					
					// 不重复于当前excel但是存在于数据库
					currentGraduateMap.put(graduateIndexNum, graduateContent);
				}
			}
			graduate.set("graduateIndexNum", graduateIndexNum);
			graduate.set("graduateContent", graduateContent);
			/************************************ 验证毕业要求是否正确 End *********************************************/
			// 验证指标点否为空
			if(indName == null && preIndContent == null) {
				errorList.add(graduate);
				graduate.set("isError", Boolean.TRUE);
				List<String> reasons = graduate.get("reasons");
				reasons.add("指标点内容为空。");
				continue;
			} 
			
			// 设置指标点数据
			String indContent = null;
			if(indName == null) {
				indContent = preIndContent;
			} else {
				indContent = indName;
				preIndContent = indName;
				preIndIndexNum = preIndIndexNum + 1;
			}
			graduate.set("indContent", indContent);
			graduate.set("indIndexNum", preIndIndexNum.toString());
			
			/************************************ 验证指标点和是否大于1 Start *********************************************/
			String key = graduateIndexNum + "," + indContent; 
			BigDecimal sumWeight= indexAndIndNameWeightMap.get(key);
			sumWeight = sumWeight == null ? new BigDecimal(0) : sumWeight;
			try {
				BigDecimal thisWeight = new BigDecimal(graduate.getStr("weight"));
				sumWeight = sumWeight.add(thisWeight);
				if(sumWeight.compareTo(new BigDecimal(1)) == 1) {
					errorList.add(graduate);
					graduate.set("isError", Boolean.TRUE);
					List<String> reasons = graduate.get("reasons");
					reasons.add("同一个指标点下权重之和不能大于1。");
				}
				indexAndIndNameWeightMap.put(key, sumWeight);
			} catch (Exception e) {
				errorList.add(graduate);
				graduate.set("isError", Boolean.TRUE);
				List<String> reasons = graduate.get("reasons");
				reasons.add("权重必须是数字。");
			}
			/************************************ 验证指标点和是否大于1 End *********************************************/
			
			// 验证当前课程名称对应课程是否唯一 
			if(ccCourseMap.containsKey(courseName)) {
				// 设置指标点课程联系参数
				graduate.set("courseId", ccCourseMap.get(courseName));
			} else if(ccCourseManyMap.containsKey(courseName)) {
				// 重复数据
				errorList.add(graduate);
				graduate.set("isError", Boolean.TRUE);
				List<String> reasons = graduate.get("reasons");
				reasons.add("当前课程名称存在重复，无法识别正确课程。");
			} else {
				// 验证当前数据是否和数据库重复
				errorList.add(graduate);
				graduate.set("isError", Boolean.TRUE);
				List<String> reasons = graduate.get("reasons");
				reasons.add("当前课程名称不存在。");
			}
			

		}

		return errorList.isEmpty();
	}

	/**
	 * 保存毕业要求及相关信息
	 * @param graduateList
	 * 		{
	 * 			graduateIndexNum : 毕业要求序号
	 * 			graduateContent ：毕业要求内容
	 * 			graduateVerId ： 毕业要求编号
	 * 
	 * 			indIndexNum ： 指标点修好
	 *  		indContent ： 指标点内容
	 *  
	 *  		courseName ： 课程名称		
	 *  		courseId ： 课程编号	
	 *  		weight ： 权重
	 * 		}
	 * @return
	 * @author SY 
	 * @version 创建时间：2018年1月11日 下午9:14:31 
	 */
	public boolean saveGraduats(List<Record> graduateList) {
		/*
		 * 1. 毕业要求的保存
		 * 2. 指标点的保存
		 * 3. 指标点课程关系的保存
		 */
		Boolean result = Boolean.TRUE;
		Date date = new Date();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		
		// 用于保存的List
		List<CcGraduate> saveGraduates = new ArrayList<>();
		List<CcIndicatorPoint> saveIndicatorPoints = new ArrayList<>();
		List<CcIndicationCourse> saveIndicationCourses = new ArrayList<>();
		
		// 记录关系型的map， Map<graduateIndexNum ,graduateId>
		Map<Integer, Long> numGraduateMap = new HashMap<>();
		// 记录关系型的map， Map<indIndexNum ,indicationId>
		Map<Integer, Long> numIndMap = new HashMap<>();
		
		// 1. 毕业要求的保存
		Integer preGraduateIndexNum = null;
		Integer preIndIndexNum = 0;
		for(Record record : graduateList) {
			// 毕业要求参数
			String graduateIndexNumStr = record.getStr("graduateIndexNum");
			Integer graduateIndexNum = Integer.valueOf(graduateIndexNumStr);
			String graduateContent = record.getStr("graduateContent");
			Long graduateVerId = record.getLong("graduateVerId");
			// 指标点参数
			String indIndexNumStr = record.getStr("indIndexNum");
			Integer indIndexNum = Integer.valueOf(indIndexNumStr);
			String indContent = record.getStr("indContent");
			// 指标点课程联系表参数
//			String courseName = record.getStr("courseName");
			Long courseId = record.getLong("courseId");
			String weightStr = record.getStr("weight");
			BigDecimal weight = new BigDecimal(weightStr);
			
			// 因为会重复，所以当发现和之前的排序是一样的时候，等于加过了
			if(!graduateIndexNum.equals(preGraduateIndexNum)) {
				// 只有新的，才新建
				Long id = idGenerate.getNextValue();			
				CcGraduate ccGraduate = new CcGraduate();
				ccGraduate.set("id", id);
				ccGraduate.set("create_date", date);
				ccGraduate.set("modify_date", date);
				ccGraduate.set("graduate_ver_id", graduateVerId);
				ccGraduate.set("index_num", graduateIndexNum);
				ccGraduate.set("content", graduateContent);
				ccGraduate.set("is_del", Boolean.FALSE);
				saveGraduates.add(ccGraduate);
				
				numGraduateMap.put(graduateIndexNum, id);
				preGraduateIndexNum = graduateIndexNum;
			}
			// 因为会重复，所以当发现和之前的排序是一样的时候，等于加过了
			if(!indIndexNum.equals(preIndIndexNum)) {
				// 只有新的，才新建
				Long id = idGenerate.getNextValue();	
				Long graduateId = numGraduateMap.get(graduateIndexNum);
				CcIndicatorPoint ccIndicatorPoint = new CcIndicatorPoint();
				ccIndicatorPoint.set("id", id);
				ccIndicatorPoint.set("create_date", date);
				ccIndicatorPoint.set("modify_date", date);
				ccIndicatorPoint.set("graduate_id", graduateId);
				ccIndicatorPoint.set("index_num", indIndexNum);
				ccIndicatorPoint.set("content", indContent);
				ccIndicatorPoint.set("is_del", Boolean.FALSE);
				saveIndicatorPoints.add(ccIndicatorPoint);
				
				numIndMap.put(indIndexNum, id);	
				preIndIndexNum = indIndexNum;
			}
			
			// 新建指标点课程关联表
			Long id = idGenerate.getNextValue();	
			Long indicationId = numIndMap.get(indIndexNum);
			CcIndicationCourse ccIndicationCourse = new CcIndicationCourse();
			ccIndicationCourse.set("id", id);
			ccIndicationCourse.set("create_date", date);
			ccIndicationCourse.set("modify_date", date);
			ccIndicationCourse.set("indication_id", indicationId);
			ccIndicationCourse.set("course_id", courseId);
			ccIndicationCourse.set("weight", weight);
			ccIndicationCourse.set("is_del", Boolean.FALSE);
			saveIndicationCourses.add(ccIndicationCourse);
		}
		// 保存
		result = CcGraduate.dao.batchSave(saveGraduates);
		if(!result) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return result;
		}
		result = CcIndicatorPoint.dao.batchSave(saveIndicatorPoints);
		if(!result) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return result;
		}
		result = CcIndicationCourse.dao.batchSave(saveIndicationCourses);
		if(!result) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return result;
		}
		return result;
	}
	

}
