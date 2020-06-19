package com.gnet.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.model.admin.CcCourseGradeComposeDetail;
import com.gnet.model.admin.CcCourseGradecompose;
import com.gnet.model.admin.CcCourseGradecomposeDetailIndication;
import com.gnet.model.admin.CcCourseGradecomposeIndication;
import com.gnet.model.admin.CcCourseGradecomposeStudetail;
import com.gnet.model.admin.CcEvalute;
import com.gnet.model.admin.CcEvaluteLevel;
import com.gnet.model.admin.CcEvaluteType;
import com.gnet.model.admin.CcGradecomposeIndicationScoreRemark;
import com.gnet.model.admin.CcScoreStuIndigrade;
import com.gnet.model.admin.CcStudentEvalute;
import com.gnet.model.admin.CcTeacherCourse;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.DictUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;

/**
 * 教师课程
 * 
 * @author SY
 * 
 * @date 2017年10月19日
 */
/**
 * @author SY
 *
 */
@Component("ccTeacherCourseService")
public class CcTeacherCourseService {

	/**
	 * 验证某个课程是否存在分享人
	 * @param courseId
	 * @return
	 */
	public Boolean isExistSharerByTeacherCourseId(Long teacherCourseId) {
		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findFilteredById(teacherCourseId);
		if(ccTeacherCourse == null) {
			return false;
		}
		Long courseId = ccTeacherCourse.getLong("course_id");
		Long termId = ccTeacherCourse.getLong("term_id");
		Integer grade = ccTeacherCourse.getInt("grade");
 		return isExistSharer(courseId, termId, grade);
	}
	
	/**
	 * 验证某个课程在某个学期下是否存在分享人
	 * @param courseId
	 * @param termId
	 * 			学期编号(可以为空)
	 * @param grade
	 * 			年级 
	 * @return
	 */
	public Boolean isExistSharer(Long courseId, Long termId, Integer grade) {
		return CcTeacherCourse.dao.isExistSharer(courseId, termId, grade);
	}
	
	/**
	 * 找到当前课程的分享人的课程
	 * @param courseId
	 * 			课程编号
	 * @param termId
	 * 			学期编号
	 * @param grade 
	 * 			年级
	 * @return
	 */
	public CcTeacherCourse findSharer(Long courseId, Long termId, Integer grade) {
		return CcTeacherCourse.dao.findSharer(courseId, termId, grade);
	}
	
	/**
	 * 找到当前课程的分享人的课程
	 * @param teacherCourseId
	 * 			课程编号
	 * @return
	 */
	public CcTeacherCourse findSharerByTeacherCourseId(Long teacherCourseId) {
		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findFilteredById(teacherCourseId);
		if(ccTeacherCourse == null) {
			return null;
		}
		Long courseId = ccTeacherCourse.getLong("course_id");
		Long termId = ccTeacherCourse.getLong("term_id");
		Integer grade = ccTeacherCourse.getInt("grade");
 		return findSharer(courseId, termId, grade);
	}
	
	/**
	 * 通过教师开课编号获取这个教师分享的教师列表
	 * @param teacherCourseId
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年11月2日 下午2:06:06 
	 */
	public List<CcTeacherCourse> findSharedTeacherCourse(Long teacherCourseId) {
		if(teacherCourseId == null) {
			return new ArrayList<>();
		}
		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findFilteredById(teacherCourseId);
		if(ccTeacherCourse == null) {
			return new ArrayList<>();
		}
		return findSharedTeacherCourse(ccTeacherCourse);
	}
	/**
	 * 通过教师开课获取这个教师分享的教师列表
	 * @param ccTeacherCourse
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年11月2日 下午2:06:06 
	 */
	public List<CcTeacherCourse> findSharedTeacherCourse(CcTeacherCourse ccTeacherCourse) {
		
		if(ccTeacherCourse == null) {
			return new ArrayList<>();
		}
		List<CcTeacherCourse> list = new ArrayList<>();
		
		Long teacherCourseId = ccTeacherCourse.getLong("id");
		Long courseId = ccTeacherCourse.getLong("course_id");
		Long termId = ccTeacherCourse.getLong("term_id");
		Integer grade = ccTeacherCourse.getInt("grade");
		// 排除掉重复的课程（同一课程在不同版本中），只留下最新的
		List<CcTeacherCourse> ccTeacherCourseList = CcTeacherCourse.dao.findList(courseId, termId, teacherCourseId, grade);
		
		// 获取所有课程的分享者map
		List<CcTeacherCourse> sharedTeacherCourse = CcTeacherCourse.dao.findFilteredByColumn("is_sharer", Boolean.TRUE);
		// Map<courseId, List<termId>>
		Map<Long , List<Long>> sharedCourseIdAndTermIdMap = Maps.newHashMap();
		for(CcTeacherCourse temp : sharedTeacherCourse) {
			Long thisCourseId = temp.getLong("course_id");
			Long thisTermeId = temp.getLong("term_id");
			List<Long> thisTermIdList = sharedCourseIdAndTermIdMap.get(thisCourseId);
			if(thisTermIdList == null) {
				thisTermIdList = new ArrayList<>();
				sharedCourseIdAndTermIdMap.put(thisCourseId, thisTermIdList);
			}
			thisTermIdList.add(thisTermeId);
		}
		
		Map<Long, CcTeacherCourse> map = Maps.newHashMap();
		for (CcTeacherCourse temp : ccTeacherCourseList) {
			Long educlassId = temp.getLong("educlassId");
			String educlassName = temp.getStr("educlass_name");
			Long id = temp.getLong("id");
            if(map.get(id) == null){
            	List<Map<String, Object>> educlasses = Lists.newArrayList();
            	if(StrKit.notBlank(educlassName) && educlassId != null){
            		Map<String, Object> eduMap = Maps.newHashMap();
            		eduMap.put("id", educlassId);
            		eduMap.put("name", educlassName);
            		educlasses.add(eduMap);
            	}
            	
    			CcTeacherCourse ccTeacherCourseTemp = new CcTeacherCourse();
    			Long thisCourseId = temp.getLong("course_id");
    			Long thisTermId = temp.getLong("term_id");
    			ccTeacherCourseTemp.put("id", id);
    			ccTeacherCourseTemp.put("createDate", temp.get("create_date"));
    			ccTeacherCourseTemp.put("modifyDate", temp.get("modify_date"));
    			ccTeacherCourseTemp.put("courseId", thisCourseId);
    			ccTeacherCourseTemp.put("courseName", temp.get("course_name"));
    			ccTeacherCourseTemp.put("teacherId", temp.get("teacher_id"));
    			ccTeacherCourseTemp.put("teacherName", temp.get("teacher_name"));
    			ccTeacherCourseTemp.put("termId", thisTermId);
    			ccTeacherCourseTemp.put("termStartYear", temp.get("term_start_year"));
    			ccTeacherCourseTemp.put("termEndYear", temp.get("term_end_year"));
    			ccTeacherCourseTemp.put("termNum", temp.get("term_num"));
    			ccTeacherCourseTemp.put("termTypeName", DictUtils.findLabelByTypeAndKey("termType", temp.getInt("term_type")));
    			ccTeacherCourseTemp.put("resultType", temp.get("result_type"));
    			ccTeacherCourseTemp.put("resultTypeName", DictUtils.findLabelByTypeAndKey("courseResultType", temp.getInt("result_type")));
    			ccTeacherCourseTemp.put("planId", temp.get("plan_id"));
    			ccTeacherCourseTemp.put("majorName", temp.get("major_name"));
    			ccTeacherCourseTemp.put("grade", temp.get("grade"));
    			ccTeacherCourseTemp.put("isSharer", temp.get("is_sharer"));
    			ccTeacherCourseTemp.put("isShared", sharedCourseIdAndTermIdMap.get(thisCourseId) == null ? Boolean.FALSE : sharedCourseIdAndTermIdMap.get(thisCourseId).contains(thisTermId) ? Boolean.TRUE : Boolean.FALSE);
    			ccTeacherCourseTemp.put("educlasses", educlasses);
    			list.add(ccTeacherCourseTemp);
    			temp.put("index", list.size()-1);
    			map.put(id, temp);
            }else{
            	
            	CcTeacherCourse techerCourse = list.get(map.get(id).getInt("index"));
            	List<Map<String, Object>> educlasses = techerCourse.get("educlasses");
            	if(StrKit.notBlank(educlassName) && educlassId != null){
            		Map<String, Object> eduMap = Maps.newHashMap();
            		eduMap.put("id", educlassId);
            		eduMap.put("name", educlassName);
            		educlasses.add(eduMap);
            		techerCourse.put("educlasses", educlasses);
            	}
            	
            }
		}
		return list;
	}
	
	/**
	 * 强制重置开课课程下的考评点信息或者成绩组成信息。
	 * @param teacherCourseId
	 * @author SY 
	 * @version 创建时间：2017年11月9日
	 * @return Map<String, Object> {
	 * 				"isSuccess" : Boolean,
	 * 				"errorMessage" String
	 * 			}
	 */
	public Map<String, Object> forceResetGradecomposeAndEvalute(Long teacherCourseId) {
		Map<String, Object> returnMap = new HashMap<>();
		Boolean result = Boolean.TRUE;
		returnMap.put("isSuccess", result);
		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findFilteredById(teacherCourseId);
		if(ccTeacherCourse == null) {
			returnMap.put("isSuccess", Boolean.FALSE);
			returnMap.put("errorMessage", "不存在当前开课课程。");
			return returnMap;
		}
		
		Integer type = ccTeacherCourse.getInt("result_type");
		if(type == CcTeacherCourse.RESULT_TYPE_SCORE) {
			// 考核分析法的重置
			return forceResetScore(teacherCourseId);
		} else if(type == CcTeacherCourse.RESULT_TYPE_EVALUATE) {
			// 考评点分析法的重置
			return forceResetEvalute(teacherCourseId);
		} else {
			returnMap.put("isSuccess", result);
			return returnMap;
		}
		
	}
	
	/**
	 * 重置开课课程下的考评点信息或者成绩组成信息。
	 * @param teacherCourseId
	 * @author SY 
	 * @version 创建时间：2017年10月19日 下午4:48:31 
	 * @return Map<String, Object> {
	 * 				"isSuccess" : Boolean,
	 * 				"errorMessage" String
	 * 			}
	 */
	public Map<String, Object> resetGradecomposeAndEvalute(Long teacherCourseId) {
		Map<String, Object> returnMap = new HashMap<>();
		Boolean result = Boolean.TRUE;
		returnMap.put("isSuccess", result);
		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findFilteredById(teacherCourseId);
		if(ccTeacherCourse == null) {
			returnMap.put("isSuccess", Boolean.FALSE);
			returnMap.put("errorMessage", "不存在当前开课课程。");
			return returnMap;
		}
		
		Integer type = ccTeacherCourse.getInt("result_type");
		if(type == CcTeacherCourse.RESULT_TYPE_SCORE) {
			// 考核分析法的重置
			return resetScore(teacherCourseId);
		} else if(type == CcTeacherCourse.RESULT_TYPE_EVALUATE) {
			// 考评点分析法的重置
			return resetEvalute(teacherCourseId);
		} else {
			returnMap.put("isSuccess", result);
			return returnMap;
		}
		
	}

	/**
	 * 重置考评点分析法的考评点信息
	 * @param teacherCourseId
	 * @return Map<String, Object> {
	 * 				"isSuccess" : Boolean,
	 * 				"errorMessage" String
	 * 			}
	 * @author SY 
	 * @version 创建时间：2017年10月19日 下午4:53:19 
	 */
	public Map<String, Object> resetEvalute(Long teacherCourseId) {
		Map<String, Object> returnMap = new HashMap<>();
		Boolean result = Boolean.TRUE;
		returnMap.put("isSuccess", result);
		Date date = new Date();
		
		// 获取考评点类型
		List<CcEvaluteType> ccEvaluteTypeList = CcEvaluteType.dao.findFilteredByColumn("teacher_course_id", teacherCourseId);
		Long[] ccEvaluteTypeIds = new Long[ccEvaluteTypeList.size()];
		for(int i = 0; i < ccEvaluteTypeList.size(); i++) {
			CcEvaluteType temp = ccEvaluteTypeList.get(i);
			ccEvaluteTypeIds[i] = temp.getLong("id");
		}
		
		if(ccEvaluteTypeList == null || ccEvaluteTypeList.isEmpty()) {
			return returnMap;
		}
		
		// 获取考评点层次
		List<CcEvaluteLevel> ccEvaluteLevelList = CcEvaluteLevel.dao.findFilteredByColumn("teacher_course_id", teacherCourseId);
		Long[] ccEvaluteLevelIds = new Long[ccEvaluteLevelList.size()];
		for(int i = 0; i < ccEvaluteLevelList.size(); i++) {
			CcEvaluteLevel temp = ccEvaluteLevelList.get(i);
			ccEvaluteLevelIds[i] = temp.getLong("id");
		}
		
		// 获取考评点
		List<CcEvalute> ccEvaluteList = CcEvalute.dao.findFilteredByColumn("teacher_course_id", teacherCourseId);
		Long[] ccEvaluteIds = new Long[ccEvaluteList.size()];
		for(int i = 0; i < ccEvaluteList.size(); i++) {
			CcEvalute temp = ccEvaluteList.get(i);
			ccEvaluteIds[i] = temp.getLong("id");
		}
		
		if(ccEvaluteIds == null || ccEvaluteIds.length == 0) {
			// 检查是否存在学生已经有分数了
			List<CcStudentEvalute> ccStudentEvaluteList = CcStudentEvalute.dao.findFilteredByColumnIn("evalute_id", ccEvaluteIds);
			if(ccStudentEvaluteList != null && !ccStudentEvaluteList.isEmpty()) {
				returnMap.put("isSuccess", Boolean.FALSE);
				returnMap.put("errorMessage", "考评点下面存在学生成绩。");
				return returnMap;
			}
		}
		
		
		// 全部删掉！
		// 删除CcEvaluteType
		if(!CcEvaluteType.dao.deleteAll(ccEvaluteTypeIds, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			returnMap.put("errorMessage", "指标点类型删除失败。");
			returnMap.put("isSuccess", false);
			return returnMap;
		}
		// 删除CcEvaluteLevel
		if(!CcEvaluteLevel.dao.deleteAll(ccEvaluteLevelIds, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			returnMap.put("errorMessage", "指标点层次删除失败。");
			returnMap.put("isSuccess", false);
			return returnMap;
		}
		// 删除CcEvalute
		if(!CcEvalute.dao.deleteAll(ccEvaluteIds, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			returnMap.put("errorMessage", "指标点删除失败。");
			returnMap.put("isSuccess", false);
			return returnMap;
		}
		
		return returnMap;
	}

	/**
	 * 重置考核分析法的成绩组成信息
	 * @param teacherCourseId
	 * @return Map<String, Object> {
	 * 				"isSuccess" : Boolean,
	 * 				"errorMessage" String
	 * 			}
	 * @author SY 
	 * @version 创建时间：2017年10月19日 下午4:52:50 
	 */
	public Map<String, Object> resetScore(Long teacherCourseId) {
		Map<String, Object> returnMap = new HashMap<>();
		Boolean result = Boolean.TRUE;
		returnMap.put("isSuccess", result);
		Date date = new Date();
		
		// 获取所有的成绩组成信息
		List<CcCourseGradecompose> courseGradecomposes = CcCourseGradecompose.dao.findFilteredByColumn("teacher_course_id", teacherCourseId);
		List<Long> courseGradecomposeIdList = new ArrayList<>();
		for(CcCourseGradecompose temp : courseGradecomposes) {
			Long courseGradecomposeId = temp.getLong("id");
			courseGradecomposeIdList.add(courseGradecomposeId);
		}
		Long[] courseGradecomposeIds= courseGradecomposeIdList.toArray(new Long[courseGradecomposeIdList.size()]);
		if(courseGradecomposeIds == null || courseGradecomposeIds.length == 0) {
			return returnMap;
		}
		
		Boolean isExist = CcScoreStuIndigrade.dao.isExistStudentGrade(courseGradecomposeIdList);
		if(isExist) {
			returnMap.put("isSuccess", Boolean.FALSE);
			returnMap.put("errorMessage", "成绩组成下面存在学生成绩。");
			return returnMap;
		}
		// 获取每个成绩组成的和开课目标的关系 
		List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndications = CcCourseGradecomposeIndication.dao.findByCourseGradecomposeIds(courseGradecomposeIds);
		Long[] ccCourseGradecomposeIndicationIds = new Long[ccCourseGradecomposeIndications.size()];
		for(int i = 0; i < ccCourseGradecomposeIndications.size(); i++) {
			CcCourseGradecomposeIndication temp = ccCourseGradecomposeIndications.get(i);
			ccCourseGradecomposeIndicationIds[i] = temp.getLong("id");
		}
		
		// 获取每个开课课程成绩组成元素指标点关系的分数范围备注
		if(ccCourseGradecomposeIndicationIds != null && ccCourseGradecomposeIndicationIds.length > 0) {
			// 删除CcGradecomposeIndicationScoreRemark
			if(!CcGradecomposeIndicationScoreRemark.dao.deleteAllByColumn("gradecompose_indication_id", ccCourseGradecomposeIndicationIds, date)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				returnMap.put("errorMessage", "开课课程成绩组成元素指标点关系的分数范围备注删除失败。");
				returnMap.put("isSuccess", false);
				return returnMap;
			}
		}
		
		// 获取每个成绩组成和成绩组成明细的关系（题目）
		List<CcCourseGradeComposeDetail> ccCourseGradeComposeDetailList = CcCourseGradeComposeDetail.dao.findFilteredByColumnIn("course_gradecompose_id", courseGradecomposeIds);
		Long[] ccCourseGradeComposeDetailIds = new Long[ccCourseGradeComposeDetailList.size()];
		for(int i = 0; i< ccCourseGradeComposeDetailList.size(); i++) {
			CcCourseGradeComposeDetail temp = ccCourseGradeComposeDetailList.get(i);
			ccCourseGradeComposeDetailIds[i] = temp.getLong("id");
		}
		
		// 防止不存在数据
		if(ccCourseGradeComposeDetailIds != null && ccCourseGradeComposeDetailIds.length > 0) {
			// 检查是否有学生
			List<CcCourseGradecomposeStudetail> ccCourseGradecomposeStudetailList = CcCourseGradecomposeStudetail.dao.findFilteredByColumnIn("detail_id", ccCourseGradeComposeDetailIds);
			if(ccCourseGradecomposeStudetailList != null && !ccCourseGradecomposeStudetailList.isEmpty()) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				returnMap.put("isSuccess", Boolean.FALSE);
				returnMap.put("errorMessage", "成绩组成元素明细下面存在学生成绩。");
				return returnMap;
			}
			
			// 获取成绩组成明细指标点关系表
			List<CcCourseGradecomposeDetailIndication> ccCourseGradecomposeDetailIndicationList = CcCourseGradecomposeDetailIndication.dao.findFilteredByColumnIn("course_gradecompose_detail_id", ccCourseGradeComposeDetailIds);
			Long []ccCourseGradecomposeDetailIndicationIds = new Long[ccCourseGradecomposeDetailIndicationList.size()];
			for(int i = 0; i< ccCourseGradecomposeDetailIndicationList.size(); i++) {
				CcCourseGradecomposeDetailIndication temp = ccCourseGradecomposeDetailIndicationList.get(i);
				ccCourseGradecomposeDetailIndicationIds[i] = temp.getLong("id");
			}
			
			// 删除CcCourseGradecomposeDetailIndication
			if(!CcCourseGradecomposeDetailIndication.dao.deleteAll(ccCourseGradecomposeDetailIndicationIds, date)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				returnMap.put("errorMessage", "成绩组成元素明细指标点关联删除失败。");
				returnMap.put("isSuccess", false);
				return returnMap;
			}
			
			// 删除CcCourseGradeComposeDetail
			if(!CcCourseGradeComposeDetail.dao.deleteAll(ccCourseGradeComposeDetailIds, date)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				returnMap.put("errorMessage", "成绩组成元素明细删除失败。");
				returnMap.put("isSuccess", false);
				return returnMap;
			}
		}
		
		
		
		// 全部删掉！
		// 删除CcCourseGradecompose
		if(!CcCourseGradecompose.dao.deleteAll(courseGradecomposeIds, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			returnMap.put("errorMessage", "成绩组成删除失败。");
			returnMap.put("isSuccess", false);
			return returnMap;
		}
		// 删除CcCourseGradecomposeIndication
		if(!CcCourseGradecomposeIndication.dao.deleteAll(ccCourseGradecomposeIndicationIds, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			returnMap.put("errorMessage", "成绩组成元素指标点关联表删除失败。");
			returnMap.put("isSuccess", false);
			return returnMap;
		}
		
		return returnMap;
	}
	
	
	/**
	 * 强制重置考评点分析法的考评点信息
	 * @param teacherCourseId
	 * @return Map<String, Object> {
	 * 				"isSuccess" : Boolean,
	 * 				"errorMessage" String
	 * 			}
	 * @author SY 
	 * @version 创建时间：2017年10月19日 下午4:53:19 
	 */
	public Map<String, Object> forceResetEvalute(Long teacherCourseId) {
		Map<String, Object> returnMap = new HashMap<>();
		Boolean result = Boolean.TRUE;
		returnMap.put("isSuccess", result);
		Date date = new Date();
		
		// 获取考评点类型
		List<CcEvaluteType> ccEvaluteTypeList = CcEvaluteType.dao.findFilteredByColumn("teacher_course_id", teacherCourseId);
		Long[] ccEvaluteTypeIds = new Long[ccEvaluteTypeList.size()];
		for(int i = 0; i < ccEvaluteTypeList.size(); i++) {
			CcEvaluteType temp = ccEvaluteTypeList.get(i);
			ccEvaluteTypeIds[i] = temp.getLong("id");
		}
		
		if(ccEvaluteTypeList == null || ccEvaluteTypeList.isEmpty()) {
			return returnMap;
		}
		
		// 获取考评点层次
		List<CcEvaluteLevel> ccEvaluteLevelList = CcEvaluteLevel.dao.findFilteredByColumn("teacher_course_id", teacherCourseId);
		Long[] ccEvaluteLevelIds = new Long[ccEvaluteLevelList.size()];
		for(int i = 0; i < ccEvaluteLevelList.size(); i++) {
			CcEvaluteLevel temp = ccEvaluteLevelList.get(i);
			ccEvaluteLevelIds[i] = temp.getLong("id");
		}
		
		// 获取考评点
		List<CcEvalute> ccEvaluteList = CcEvalute.dao.findFilteredByColumn("teacher_course_id", teacherCourseId);
		Long[] ccEvaluteIds = new Long[ccEvaluteList.size()];
		for(int i = 0; i < ccEvaluteList.size(); i++) {
			CcEvalute temp = ccEvaluteList.get(i);
			ccEvaluteIds[i] = temp.getLong("id");
		}
		
		if(ccEvaluteIds.length != 0) {
			// 检查是否存在学生已经有分数了
			List<CcStudentEvalute> ccStudentEvaluteList = CcStudentEvalute.dao.findFilteredByColumnIn("evalute_id", ccEvaluteIds);
			if(ccStudentEvaluteList != null && !ccStudentEvaluteList.isEmpty()) {
				Boolean isSuccess = CcStudentEvalute.dao.deleteAllByColumn("evalute_id", ccEvaluteIds, date);
				if(!isSuccess) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					returnMap.put("isSuccess", Boolean.FALSE);
					returnMap.put("errorMessage", "考评点下面存在学生成绩删除失败。");
					return returnMap;
				}
			}
		}
		
		
		// 全部删掉！
		// 删除CcEvaluteType
		if(!CcEvaluteType.dao.deleteAll(ccEvaluteTypeIds, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			returnMap.put("errorMessage", "指标点类型删除失败。");
			returnMap.put("isSuccess", false);
			return returnMap;
		}
		// 删除CcEvaluteLevel
		if(!CcEvaluteLevel.dao.deleteAll(ccEvaluteLevelIds, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			returnMap.put("errorMessage", "指标点层次删除失败。");
			returnMap.put("isSuccess", false);
			return returnMap;
		}
		// 删除CcEvalute
		if(ccEvaluteIds.length != 0) {
			if(!CcEvalute.dao.deleteAll(ccEvaluteIds, date)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				returnMap.put("errorMessage", "指标点删除失败。");
				returnMap.put("isSuccess", false);
				return returnMap;
			}			
		}
		
		return returnMap;
	}
	
	/**
	 * 强制重置考核分析法的成绩组成信息
	 * @param teacherCourseId
	 * @return Map<String, Object> {
	 * 				"isSuccess" : Boolean,
	 * 				"errorMessage" String
	 * 			}
	 * @author SY 
	 * @version 创建时间：2017年10月19日 下午4:52:50 
	 */
	public Map<String, Object> forceResetScore(Long teacherCourseId) {
		Map<String, Object> returnMap = new HashMap<>();
		Boolean result = Boolean.TRUE;
		returnMap.put("isSuccess", result);
		Date date = new Date();
		
		// 获取所有的成绩组成信息
		List<CcCourseGradecompose> courseGradecomposes = CcCourseGradecompose.dao.findFilteredByColumn("teacher_course_id", teacherCourseId);
		List<Long> courseGradecomposeIdList = new ArrayList<>();
		for(CcCourseGradecompose temp : courseGradecomposes) {
			Long courseGradecomposeId = temp.getLong("id");
			courseGradecomposeIdList.add(courseGradecomposeId);
		}
		Long[] courseGradecomposeIds= courseGradecomposeIdList.toArray(new Long[courseGradecomposeIdList.size()]);
		if(courseGradecomposeIds == null || courseGradecomposeIds.length == 0) {
			return returnMap;
		}
		
		Boolean isExist = CcScoreStuIndigrade.dao.isExistStudentGrade(courseGradecomposeIdList);
		if(isExist) {
			Boolean isSuccess = CcScoreStuIndigrade.dao.deleteByCourseGradecomposeIds(courseGradecomposeIdList, date);
			if(!isSuccess) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				returnMap.put("errorMessage", "成绩组成元素明细下面存在学生成绩删除失败。");
				returnMap.put("isSuccess", false);
				return returnMap;
			}
		}
		// 获取每个成绩组成的和开课目标的关系 
		List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndications = CcCourseGradecomposeIndication.dao.findByCourseGradecomposeIds(courseGradecomposeIds);
		Long[] ccCourseGradecomposeIndicationIds = new Long[ccCourseGradecomposeIndications.size()];
		for(int i = 0; i < ccCourseGradecomposeIndications.size(); i++) {
			CcCourseGradecomposeIndication temp = ccCourseGradecomposeIndications.get(i);
			ccCourseGradecomposeIndicationIds[i] = temp.getLong("id");
		}
		
		// 获取每个开课课程成绩组成元素指标点关系的分数范围备注
		if(ccCourseGradecomposeIndicationIds != null && ccCourseGradecomposeIndicationIds.length > 0) {
			// 删除CcGradecomposeIndicationScoreRemark
			if(!CcGradecomposeIndicationScoreRemark.dao.deleteAllByColumn("gradecompose_indication_id", ccCourseGradecomposeIndicationIds, date)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				returnMap.put("errorMessage", "开课课程成绩组成元素指标点关系的分数范围备注删除失败。");
				returnMap.put("isSuccess", false);
				return returnMap;
			}
		}
		
		// 获取每个成绩组成和成绩组成明细的关系（题目）
		List<CcCourseGradeComposeDetail> ccCourseGradeComposeDetailList = CcCourseGradeComposeDetail.dao.findFilteredByColumnIn("course_gradecompose_id", courseGradecomposeIds);
		Long[] ccCourseGradeComposeDetailIds = new Long[ccCourseGradeComposeDetailList.size()];
		for(int i = 0; i< ccCourseGradeComposeDetailList.size(); i++) {
			CcCourseGradeComposeDetail temp = ccCourseGradeComposeDetailList.get(i);
			ccCourseGradeComposeDetailIds[i] = temp.getLong("id");
		}
		
		// 防止不存在数据
		if(ccCourseGradeComposeDetailIds != null && ccCourseGradeComposeDetailIds.length > 0) {
			// 检查是否有学生
			List<CcCourseGradecomposeStudetail> ccCourseGradecomposeStudetailList = CcCourseGradecomposeStudetail.dao.findFilteredByColumnIn("detail_id", ccCourseGradeComposeDetailIds);
			if(ccCourseGradecomposeStudetailList != null && !ccCourseGradecomposeStudetailList.isEmpty()) {
				Boolean isSuccess = CcCourseGradecomposeStudetail.dao.deleteAllByColumn("detail_id", ccCourseGradeComposeDetailIds, date);
				if(!isSuccess) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					returnMap.put("isSuccess", Boolean.FALSE);
					returnMap.put("errorMessage", "成绩组成元素明细下面存在学生成绩删除失败。");
					return returnMap;
				}
			}
			
			// 获取成绩组成明细指标点关系表
			List<CcCourseGradecomposeDetailIndication> ccCourseGradecomposeDetailIndicationList = CcCourseGradecomposeDetailIndication.dao.findFilteredByColumnIn("course_gradecompose_detail_id", ccCourseGradeComposeDetailIds);
			Long []ccCourseGradecomposeDetailIndicationIds = new Long[ccCourseGradecomposeDetailIndicationList.size()];
			for(int i = 0; i< ccCourseGradecomposeDetailIndicationList.size(); i++) {
				CcCourseGradecomposeDetailIndication temp = ccCourseGradecomposeDetailIndicationList.get(i);
				ccCourseGradecomposeDetailIndicationIds[i] = temp.getLong("id");
			}
			
			// 删除CcCourseGradecomposeDetailIndication
			if(!CcCourseGradecomposeDetailIndication.dao.deleteAll(ccCourseGradecomposeDetailIndicationIds, date)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				returnMap.put("errorMessage", "成绩组成元素明细指标点关联删除失败。");
				returnMap.put("isSuccess", false);
				return returnMap;
			}
			
			// 删除CcCourseGradeComposeDetail
			if(!CcCourseGradeComposeDetail.dao.deleteAll(ccCourseGradeComposeDetailIds, date)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				returnMap.put("errorMessage", "成绩组成元素明细删除失败。");
				returnMap.put("isSuccess", false);
				return returnMap;
			}
		}
		
		
		
		// 全部删掉！
		// 删除CcCourseGradecompose
		if(!CcCourseGradecompose.dao.deleteAll(courseGradecomposeIds, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			returnMap.put("errorMessage", "成绩组成删除失败。");
			returnMap.put("isSuccess", false);
			return returnMap;
		}
		// 删除CcCourseGradecomposeIndication
		if(!CcCourseGradecomposeIndication.dao.deleteAll(ccCourseGradecomposeIndicationIds, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			returnMap.put("errorMessage", "成绩组成元素指标点关联表删除失败。");
			returnMap.put("isSuccess", false);
			return returnMap;
		}
		
		return returnMap;
	}
	
	/**
	 * 接收别人的考评点或者成绩组成的分享
	 * @param teacherCourseId
	 * 			接收人的教师开课编号
	 * @param sharerTeacherCourseId
	 * 			分享人的教师开课编号
	 * @return Map<String, Object> {
	 * 				"isSuccess" : Boolean,
	 * 				"errorMessage" String
	 * 			}
	 * @author SY 
	 * @version 创建时间：2017年10月20日 上午11:40:07 
	 */
	public Map<String, Object> copyComposeGradeAndEvalute(Long teacherCourseId, Long sharerTeacherCourseId) {
		return copyComposeGradeAndEvalute(teacherCourseId, sharerTeacherCourseId, null);
	}

	/**
	 * 接收别人的考评点或者成绩组成的分享
	 * @param teacherCourseId
	 * 			接收人的教师开课编号
	 * @param sharerTeacherCourseId
	 * 			分享人的教师开课编号
	 * @param teacherCourse
	 * 			分享人的教师开课信息
	 * @return Map<String, Object> {
	 * 				"isSuccess" : Boolean,
	 * 				"errorMessage" String
	 * 			}
	 * @author SY 
	 * @version 创建时间：2017年10月20日 上午11:40:07 
	 */
	public Map<String, Object> copyComposeGradeAndEvalute(Long teacherCourseId, Long sharerTeacherCourseId, CcTeacherCourse ccTeacherCourse) {
		Map<String, Object> returnMap = new HashMap<>();
		Boolean result = Boolean.TRUE;
		returnMap.put("isSuccess", result);
		if(ccTeacherCourse == null) {
			ccTeacherCourse = CcTeacherCourse.dao.findFilteredById(teacherCourseId);
			if(ccTeacherCourse == null) {
				returnMap.put("isSuccess", Boolean.FALSE);
				returnMap.put("errorMessage", "不存在接收人的开课课程。");
				return returnMap;
			}
		}
		
		CcTeacherCourse ccTeacherCourseSharer = CcTeacherCourse.dao.findFilteredById(sharerTeacherCourseId);
		if(ccTeacherCourseSharer == null) {
			returnMap.put("isSuccess", Boolean.FALSE);
			returnMap.put("errorMessage", "不存在分享人的开课课程。");
			return returnMap;
		}
		
		Integer type = ccTeacherCourse.getInt("result_type");
		Integer sharerType = ccTeacherCourseSharer.getInt("result_type");
		if(type != sharerType) {
			returnMap.put("isSuccess", Boolean.FALSE);
			returnMap.put("errorMessage", "接收人和分享人的开课课程的达成度计算类型不一致。");
			return returnMap;
		} else if(type == CcTeacherCourse.RESULT_TYPE_SCORE) {
			// 考核分析法的复制
			return copyScore(teacherCourseId, sharerTeacherCourseId);
		} else if(type == CcTeacherCourse.RESULT_TYPE_EVALUATE) {
			// 考评点分析法的复制
			return copyEvalute(teacherCourseId, sharerTeacherCourseId);
		} else {
			returnMap.put("isSuccess", result);
			return returnMap;
		}
	}
	
	/**
	 * 拷贝考评点分析法的考评点信息
	 * @param teacherCourseId 
	 * @param sharerTeacherCourseId
	 * @return Map<String, Object> {
	 * 				"isSuccess" : Boolean,
	 * 				"errorMessage" String
	 * 			}
	 * @author SY 
	 * @version 创建时间：2017年10月20日11:54:04 
	 */
	public Map<String, Object> copyEvalute(Long teacherCourseId, Long sharerTeacherCourseId) {
		Map<String, Object> returnMap = new HashMap<>();
		Boolean result = Boolean.TRUE;
		returnMap.put("isSuccess", result);
		Date date = new Date();
		
		// 关系map
		// 指标点编号 和 考评点类型编号
		Map<Long, Long> evaluteIdAndEvaluteTypeIdMap = new HashMap<>();
				
		// 获取考评点类型
		List<CcEvaluteType> ccEvaluteTypeList = CcEvaluteType.dao.findFilteredByColumnIn("teacher_course_id", sharerTeacherCourseId);
		
		// 获取考评点层次
		List<CcEvaluteLevel> ccEvaluteLevelList = CcEvaluteLevel.dao.findFilteredByColumnIn("teacher_course_id", sharerTeacherCourseId);
		
		// 获取考评点
		List<CcEvalute> ccEvaluteList = CcEvalute.dao.findFilteredByColumnIn("teacher_course_id", sharerTeacherCourseId);
		for(int i = 0; i < ccEvaluteList.size(); i++) {
			CcEvalute temp = ccEvaluteList.get(i);
			Long evaluteId = temp.getLong("id");
			Long evaluteTypeId = temp.getLong("evalute_type_id");
			evaluteIdAndEvaluteTypeIdMap.put(evaluteId, evaluteTypeId);
		}
		
		// 新的map关系
		// 考评点类型编号map
		Map<Long, Long> evaluteTypeIdOldAndNew = new HashMap<>();
		
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		
		// 复制考评点类型
		for(CcEvaluteType temp : ccEvaluteTypeList) {
			
			Long oldEvaluteTypeId = temp.getLong("id");
					
			Long newEvaluteTypeId = idGenerate.getNextValue();
			temp.set("id", newEvaluteTypeId);
			temp.set("create_date", date);
			temp.set("modify_date", date);
			temp.set("teacher_course_id", teacherCourseId);
			
			evaluteTypeIdOldAndNew.put(oldEvaluteTypeId, newEvaluteTypeId);
		}
		
		// 复制考评点
		for(CcEvalute temp : ccEvaluteList) {
			Long evaluteId = idGenerate.getNextValue();
			temp.set("id", evaluteId);
			temp.set("create_date", date);
			temp.set("modify_date", date);
			temp.set("teacher_course_id", teacherCourseId);
			temp.set("evalute_type_id", evaluteTypeIdOldAndNew.get(temp.getLong("evalute_type_id")));
		}
		
		// 复制考评点层次
		for(CcEvaluteLevel temp : ccEvaluteLevelList) {
			Long levelId = idGenerate.getNextValue();
			temp.set("id", levelId);
			temp.set("create_date", date);
			temp.set("modify_date", date);
			temp.set("teacher_course_id", teacherCourseId);
		}
		
		// 全部保存
		// 保存CcEvaluteType
		if(!CcEvaluteType.dao.batchSave(ccEvaluteTypeList)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			returnMap.put("errorMessage", "指标点类型保存失败。");
			returnMap.put("isSuccess", false);
			return returnMap;
		}
		// 保存CcEvaluteLevel
		if(!CcEvaluteLevel.dao.batchSave(ccEvaluteLevelList)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			returnMap.put("errorMessage", "指标点层次保存失败。");
			returnMap.put("isSuccess", false);
			return returnMap;
		}
		// 保存CcEvalute
		if(!CcEvalute.dao.batchSave(ccEvaluteList)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			returnMap.put("errorMessage", "指标点保存失败。");
			returnMap.put("isSuccess", false);
			return returnMap;
		}
		
		return returnMap;
	}

	/**
	 * 拷贝考核分析法的成绩组成信息
	 * @param teacherCourseId 
	 * @param sharerTeacherCourseId
	 * @return Map<String, Object> {
	 * 				"isSuccess" : Boolean,
	 * 				"errorMessage" String
	 * 			}
	 * @author SY 
	 * @version 创建时间：2017年10月20日11:54:04 
	 */
	public Map<String, Object> copyScore(Long teacherCourseId, Long sharerTeacherCourseId) {
		Map<String, Object> returnMap = new HashMap<>();
		Boolean result = Boolean.TRUE;
		returnMap.put("isSuccess", result);
		Date date = new Date();
		
		// 获取所有的成绩组成信息
		List<CcCourseGradecompose> courseGradecomposes = CcCourseGradecompose.dao.findFilteredByColumn("teacher_course_id", sharerTeacherCourseId);
		List<Long> courseGradecomposeIdList = new ArrayList<>();
		for(CcCourseGradecompose temp : courseGradecomposes) {
			Long courseGradecomposeId = temp.getLong("id");
			courseGradecomposeIdList.add(courseGradecomposeId);
		}
		if(courseGradecomposeIdList.isEmpty()) {
			return returnMap;
		}
		Long[] courseGradecomposeIds= courseGradecomposeIdList.toArray(new Long[courseGradecomposeIdList.size()]);
		
		// 获取每个成绩组成的和开课目标的关系 
		List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndications = CcCourseGradecomposeIndication.dao.findByCourseGradecomposeIds(courseGradecomposeIds);
		Long[] ccCourseGradecomposeIndicationIds = new Long[ccCourseGradecomposeIndications.size()];
		for(int i = 0; i< ccCourseGradecomposeIndications.size(); i++) {
			CcCourseGradecomposeIndication temp = ccCourseGradecomposeIndications.get(i);
			ccCourseGradecomposeIndicationIds[i] = temp.getLong("id");
		}
				
		// 获取每个成绩组成和成绩组成明细的关系（题目）
		List<CcCourseGradeComposeDetail> ccCourseGradeComposeDetailList = CcCourseGradeComposeDetail.dao.findFilteredByColumnIn("course_gradecompose_id", courseGradecomposeIds);
		Long[] ccCourseGradeComposeDetailIds = new Long[ccCourseGradeComposeDetailList.size()];
		for(int i = 0; i< ccCourseGradeComposeDetailList.size(); i++) {
			CcCourseGradeComposeDetail temp = ccCourseGradeComposeDetailList.get(i);
			ccCourseGradeComposeDetailIds[i] = temp.getLong("id");
		}
		
		// CcCourseGradecompose
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		// Map的映射
		Map<Long, Long> courseGradecomposeIdOldAndNew = new HashMap<>();
		Map<Long, Long> CcCourseGradeComposeDetailIdOldAndNew = new HashMap<>();
		Map<Long, Long> CcCourseGradeComposeIndicationIdOldAndNew = new HashMap<>();
		// CcCourseGradecompose
		for(CcCourseGradecompose temp : courseGradecomposes) {
			Long courseGradecomposeId = temp.getLong("id");
			Long id = idGenerate.getNextValue();
			temp.set("id", id);
			temp.set("create_date", date);
			temp.set("modify_date", date);
			temp.set("teacher_course_id", teacherCourseId);
			
			courseGradecomposeIdOldAndNew.put(courseGradecomposeId, id);
		}
		// CcCourseGradecomposeIndication
		for(CcCourseGradecomposeIndication temp : ccCourseGradecomposeIndications) {
			Long courseGradecomposeIndicationId = temp.getLong("id");
			Long courseGradecomposeId = temp.getLong("course_gradecompose_id");
			Long id = idGenerate.getNextValue();
			temp.set("id", id);
			temp.set("create_date", date);
			temp.set("modify_date", date);
			temp.set("course_gradecompose_id", courseGradecomposeIdOldAndNew.get(courseGradecomposeId));
			
			CcCourseGradeComposeIndicationIdOldAndNew.put(courseGradecomposeIndicationId, id);
		}
		// CcCourseGradeComposeDetail
		for(CcCourseGradeComposeDetail temp : ccCourseGradeComposeDetailList) {
			Long oldCourseGradeComposeDetailId = temp.getLong("id");
			Long courseGradecomposeId = temp.getLong("course_gradecompose_id");
			Long id = idGenerate.getNextValue();
			temp.set("id", id);
			temp.set("create_date", date);
			temp.set("modify_date", date);
			temp.set("course_gradecompose_id", courseGradecomposeIdOldAndNew.get(courseGradecomposeId));
			
			CcCourseGradeComposeDetailIdOldAndNew.put(oldCourseGradeComposeDetailId, id);
		}

		// 获取每个开课课程成绩组成元素指标点关系的分数范围备注
		if(ccCourseGradecomposeIndicationIds != null && ccCourseGradecomposeIndicationIds.length > 0) {
			// 获取CcGradecomposeIndicationScoreRemark
			List<CcGradecomposeIndicationScoreRemark> ccGradecomposeIndicationScoreRemarkList = CcGradecomposeIndicationScoreRemark.dao.findByColumnIn("gradecompose_indication_id", ccCourseGradecomposeIndicationIds);
			
			// CcGradecomposeIndicationScoreRemark
			for(CcGradecomposeIndicationScoreRemark temp : ccGradecomposeIndicationScoreRemarkList) {
				Long courseGradecomposeIndicationlId = temp.getLong("gradecompose_indication_id");
				Long id = idGenerate.getNextValue();
				temp.set("id", id);
				temp.set("create_date", date);
				temp.set("modify_date", date);
				temp.set("gradecompose_indication_id", CcCourseGradeComposeIndicationIdOldAndNew.get(courseGradecomposeIndicationlId));
			}
			// 保存CcGradecomposeIndicationScoreRemark
			if(!CcGradecomposeIndicationScoreRemark.dao.batchSave(ccGradecomposeIndicationScoreRemarkList)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				returnMap.put("errorMessage", "开课课程成绩组成元素指标点关系的分数范围备注保存失败。");
				returnMap.put("isSuccess", false);
				return returnMap;
			}
		}
				
		if(ccCourseGradeComposeDetailIds != null && ccCourseGradeComposeDetailIds.length > 0) {
			// 获取成绩组成明细指标点关系表
			List<CcCourseGradecomposeDetailIndication> ccCourseGradecomposeDetailIndicationList = CcCourseGradecomposeDetailIndication.dao.findFilteredByColumnIn("course_gradecompose_detail_id", ccCourseGradeComposeDetailIds);
			
			// CcCourseGradecomposeDetailIndication
			for(CcCourseGradecomposeDetailIndication temp : ccCourseGradecomposeDetailIndicationList) {
				Long courseGradecomposeDetailId = temp.getLong("course_gradecompose_detail_id");
				Long id = idGenerate.getNextValue();
				temp.set("id", id);
				temp.set("create_date", date);
				temp.set("modify_date", date);
				temp.set("course_gradecompose_detail_id", CcCourseGradeComposeDetailIdOldAndNew.get(courseGradecomposeDetailId));
			}
			// 保存CcCourseGradecomposeDetailIndication
			if(!CcCourseGradecomposeDetailIndication.dao.batchSave(ccCourseGradecomposeDetailIndicationList)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				returnMap.put("errorMessage", "成绩组成元素明细指标点关联保存失败。");
				returnMap.put("isSuccess", false);
				return returnMap;
			}
		}
		
		// 全部增加！
		// 保存CcCourseGradecompose
		if(!CcCourseGradecompose.dao.batchSave(courseGradecomposes)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			returnMap.put("errorMessage", "成绩组成保存失败。");
			returnMap.put("isSuccess", false);
			return returnMap;
		}
		// 保存CcCourseGradecomposeIndication
		if(!CcCourseGradecomposeIndication.dao.batchSave(ccCourseGradecomposeIndications)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			returnMap.put("errorMessage", "成绩组成元素指标点关联表保存失败。");
			returnMap.put("isSuccess", false);
			return returnMap;
		}
		// 保存CcCourseGradeComposeDetail
		if(!CcCourseGradeComposeDetail.dao.batchSave(ccCourseGradeComposeDetailList)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			returnMap.put("errorMessage", "成绩组成元素明细保存失败。");
			returnMap.put("isSuccess", false);
			return returnMap;
		}
		
		return returnMap;
	}

}
