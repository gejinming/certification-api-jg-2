package com.gnet.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;

/**
 * 课程
 * 
 * @author SY
 * 
 * @date 2016年7月18日18:31:04
 */
@Component("ccCourseService")
public class CcCourseService {

	/**
	 * 验证以下的课程是否都可以删除
	 * @param courseIds
	 * @return
	 */
	public Record valiudateDelete(Long[] courseIds) {
		Record record = new Record();
		record.set("isSuccess", Boolean.TRUE);
		record.set("reason", "验证通过");
		/*
		 *  1. teacherCourse里面不存在数据
		 *  2. 判断课程组
		 */
		// 1. teacherCourse里面不存在数据
		List<CcTeacherCourse> ccTeacherCourses = CcTeacherCourse.dao.findFilteredByColumnIn("course_id", courseIds);
		if(!ccTeacherCourses.isEmpty()) {
			Long courseId = ccTeacherCourses.get(0).getLong("course_id");
			CcCourse course = CcCourse.dao.findFilteredById(courseId);
			record.set("isSuccess", Boolean.FALSE);
			record.set("reason", "课程名为：《" + course.getStr("name") + "》，课程代码为：" + course.getStr("code") + "，的课程。存在教师开课课程，在删除前不能进行删除本课程操作！");
			return record;
		}
		
		// 2. 判断课程组
		CcCourse ccCourse = CcCourse.dao.findByCourseHaveGroup(courseIds);
		if (ccCourse != null) {
			record.set("isSuccess", Boolean.FALSE);
			record.set("reason", "课程名为：《" + ccCourse.getStr("name") + "》，课程代码为：" + ccCourse.getStr("code") + "，的课程。存在课程组，在退出课程组前不能进行删除本课程操作！");
			return record;
		}
 		return record;
	}
	
	/**
	 * 验证年级的是否符合要求，即年级大于等于当前版本的启用年级，小于下一个版本的启用年级（如果下一个版本不存在，则无穷大）
	 * @param grade
	 * 			年级
	 * @param courseId
	 * 			课程编号
	 * @return
	 */
	public boolean validateGrade(Integer grade, Long courseId) {
		/*
		 * 1. 获取当前课程所属的版本
		 * 2. 获取版本的启用年级A
		 * 3. 获取下一个版本的启用年级（可能存在可能不存在）B
		 * 4. 要求A 《= grade 《 B（可能∞+）
		 */
		
		//  1. 获取当前课程所属的版本
		CcCourse myCourse = CcCourse.dao.findById(courseId);
		if(myCourse == null) {
			return false;
		}
		Long myPlanId = myCourse.getLong("plan_id");
		CcVersion myVersion = CcVersion.dao.findById(myPlanId);
		if(myVersion == null) {
			return false;
		}
		
		// 2. 获取版本的启用年级A
		Integer myGrade = myVersion.getInt("enable_grade");
		Integer myMajorVersion = myVersion.getInt("major_version");
		Long majorId = myVersion.getLong("major_id");
		if(myGrade > grade) {
			return false;
		}
		
		// 3. 获取下一个版本的启用年级（可能存在可能不存在）B
		CcVersionService ccVersionService = SpringContextHolder.getBean(CcVersionService.class);
		CcVersion myNextVersion = ccVersionService.findLastVersion(majorId, myMajorVersion + 1);
		if(myNextVersion != null) {
			Integer myNextGrade = myNextVersion.getInt("enable_grade");
			if(grade >= myNextGrade) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * 验证理论导入是否正确
	 * @param courseList
	 * @param planId
	 * @author SY
	 * @version 创建时间：2018年1月12日 上午1:08:38
	 */
	public boolean validateImportTheoryList(List<Record> courseList, Long planId) {
		// 验证数据需要数据结构准备
		List<Record> errorList = Lists.newArrayList();
		// 验证excel中课程代码是否重复数据结构准备
		List<String> courseCodeNoInExcel = Lists.newArrayList();
		// 验证课程代码是否在数据库中重复数据结构准备
		String[] courseCodes = new String[courseList.size()];

		// 课程代码是否在数据库中重复
		List<CcCourse> repeatInDB = CcCourse.dao.findFilteredByColumn("plan_id",  planId);
		List<String> codeInDb = new ArrayList<>();
		for(CcCourse temp : repeatInDB) {
			String code = temp.getStr("code");
			codeInDb.add(code);
		}

		// 找到所有的课程层次
		List<CcCourseHierarchy> ccCourseHierarchies = CcCourseHierarchy.dao.findFilteredByColumn("plan_id", planId);
		Map<String, Long> ccCourseHierarchiesMap = new HashMap<>();
		for(CcCourseHierarchy temp : ccCourseHierarchies) {
			String name = temp.getStr("name");
			Long id = temp.getLong("id");
			ccCourseHierarchiesMap.put(name, id);
		}
		//找到所有次要课程层次
		List<CcCourseHierarchySecondary> ccCourseHierarchySecondarys = CcCourseHierarchySecondary.dao.findFilteredByColumn("plan_id", planId);
		Map<String, Long> ccCourseHierarchySecondarysMap = new HashMap<>();
		for(CcCourseHierarchySecondary temp : ccCourseHierarchySecondarys) {
			String name = temp.getStr("name");
			Long id = temp.getLong("id");
			ccCourseHierarchySecondarysMap.put(name, id);
		}
		// 找到所有的课程性质
		List<CcCourseProperty> ccCourseProperties = CcCourseProperty.dao.findFilteredByColumn("plan_id", planId);
		Map<String, Long> ccCoursePropertiesMap = new HashMap<>();
		for(CcCourseProperty temp : ccCourseProperties) {
			String name = temp.getStr("property_name");
			Long id = temp.getLong("id");
			ccCoursePropertiesMap.put(name, id);
		}
		//找到所有的次要课程性质
		List<CcCoursePropertySecondary> ccCoursePropertiesSecondary = CcCoursePropertySecondary.dao.findFilteredByColumn("plan_id", planId);
		Map<String, Long> ccCoursePropertiesSecondaryMap = new HashMap<>();
		for(CcCoursePropertySecondary temp : ccCoursePropertiesSecondary) {
			String name = temp.getStr("property_name");
			Long id = temp.getLong("id");
			ccCoursePropertiesSecondaryMap.put(name, id);
		}
		// 找到所有的开课学期
		List<CcPlanTerm> ccPlanTermsClass = CcPlanTerm.dao.findByPlanId(planId);
		Map<String, Long> ccPlanTermsClassMap = new HashMap<>();
		for(CcPlanTerm temp : ccPlanTermsClass) {
			String yearName = temp.getStr("yearName");
			String termName = temp.getStr("termName");
			Long id = temp.getLong("planTermId");
			// 用中文冒号分开，因为excel中是这个
			ccPlanTermsClassMap.put(yearName + "：" + termName, id);
		}
		// 找到所有的考试学期
		List<CcPlanTerm> ccPlanTermsExam = CcPlanTerm.dao.findByPlanId(planId);
		Map<String, Long> ccPlanTermsExamMap = new HashMap<>();
		for(CcPlanTerm temp : ccPlanTermsExam) {
			String yearName = temp.getStr("yearName");
			String termName = temp.getStr("termName");
			Long id = temp.getLong("planTermId");
			// 用中文冒号分开，因为excel中是这个
			ccPlanTermsExamMap.put(yearName + "：" + termName, id);
		}

		for (int i = 0; i < courseList.size(); i++) {
			Record course = courseList.get(i);
			// 初始化isError为False
			course.set("plan_id", planId);
			course.set("isError", Boolean.FALSE);
			course.set("reasons", new ArrayList<String>());
			// 验证非空字段是否都存在
			if (StrKit.isBlank(course.getStr("code")) || StrKit.isBlank(course.getStr("name"))
					|| StrKit.isBlank(course.getStr("credit")) || StrKit.isBlank(course.getStr("weekHour"))
                    || StrKit.isBlank(course.getStr("planTermClassNames")) || StrKit.isBlank(course.getStr("planTermExamNames"))
                ) {
				errorList.add(course);
				course.set("isError", Boolean.TRUE);
				List<String> reasons = course.get("reasons");
				reasons.add("该课程缺少课程代码、课程中文名、学分、周学时、开课学期、考试学期这几个必填字段的其中之一。");
			}
			if (StrKit.notBlank(course.getStr("code"))) {
				courseCodes[i] = course.getStr("code");
				// 验证excel中学号是否重复
				if (!courseCodeNoInExcel.contains(courseCodes[i])) {
					courseCodeNoInExcel.add(courseCodes[i]);
				} else {
					errorList.add(course);
					course.set("isError", Boolean.TRUE);
					List<String> reasons = course.get("reasons");
					reasons.add("该课程代码在excel中有重复。");
				}

				// 验证是否在数据库中
				if(codeInDb.contains(courseCodes[i])) {
					errorList.add(course);
					course.set("isError", Boolean.TRUE);
					List<String> reasons = course.get("reasons");
					reasons.add("该课程代码在系统中已存在。");
				}
			}

			// 验证并找到课程层次
			String hierarchyName = course.getStr("hierarchyName");
			Long hierarchyId = ccCourseHierarchiesMap.get(hierarchyName);
			if(hierarchyId == null) {
				errorList.add(course);
				course.set("isError", Boolean.TRUE);
				List<String> reasons = course.get("reasons");
				reasons.add("该课程层次在系统中不存在。");
			} else {
				// 如果存在，设置进去
				course.set("hierarchyId", hierarchyId);
			}
			// 验证并找到次要课程层次
			String seconderyHierarchyName = course.getStr("seconderyHierarchyName");
			if (StrKit.notBlank(course.getStr("seconderyHierarchyName"))&& seconderyHierarchyName!=null && seconderyHierarchyName!=""){
				Long hierarchySecondId = ccCourseHierarchySecondarysMap.get(seconderyHierarchyName);
				if(hierarchySecondId == null) {
					errorList.add(course);
					course.set("isError", Boolean.TRUE);
					List<String> reasons = course.get("reasons");
					reasons.add("该次要课程层次在系统中不存在。");
				} else {
					// 如果存在，设置进去
					course.set("hierarchySecondId", hierarchySecondId);
				}
			}
			// 验证并找到课程性质
			String propertyName = course.getStr("propertyName");
			Long propertyId = ccCoursePropertiesMap.get(propertyName);
			if(StrKit.isBlank(propertyName)){
				errorList.add(course);
				course.set("isError", Boolean.TRUE);
				List<String> reasons = course.get("reasons");
				reasons.add("该课程性质为空。");
			}else {
				if(propertyId == null) {
					errorList.add(course);
					course.set("isError", Boolean.TRUE);
					List<String> reasons = course.get("reasons");
					reasons.add("该课程性质在系统中不存在。");
				} else {
					// 如果存在，设置进去
					course.set("propertyId", propertyId);
				}
			}
			//验证次要课程性质
			String propertySecondaryName = course.getStr("propertySecondaryName");
			if (StrKit.notBlank(course.getStr("propertySecondaryName")) && propertySecondaryName!=null && propertySecondaryName!=""){

				Long propertySecondaryId = ccCoursePropertiesSecondaryMap.get(propertySecondaryName);
				if(propertySecondaryId == null) {
					errorList.add(course);
					course.set("isError", Boolean.TRUE);
					List<String> reasons = course.get("reasons");
					reasons.add("该次要课程性质在系统中不存在。");
				} else {
					// 如果存在，设置进去
					course.set("propertySecondaryId", propertySecondaryId);
				}
			}
			// 验证并找到开课学期
			String planTermClassNames = course.getStr("planTermClassNames");
			if(planTermClassNames != null) {
				String []planTermClassName = planTermClassNames.split("、");
				List<Long> planTermClassIds = new ArrayList<>();
				for(String name : planTermClassName) {
					Long planTermClassId = ccPlanTermsClassMap.get(name);
					if(planTermClassId == null) {
						errorList.add(course);
						course.set("isError", Boolean.TRUE);
						List<String> reasons = course.get("reasons");
						reasons.add("开课学期：“" + name + "”在系统中不存在。");
					} else {
						planTermClassIds.add(planTermClassId);
					}
				}
				String planTermClassIdsStr = "";
				for(Long id : planTermClassIds) {
					planTermClassIdsStr = planTermClassIdsStr + id + ",";
				}
				course.set("planTermClassIds", planTermClassIdsStr.length() < 2 ? null : planTermClassIdsStr.subSequence(0, planTermClassIdsStr.length() - 1));
			}
			// 验证并找到考试学期
			String planTermExamNames = course.getStr("planTermExamNames");
			if(planTermExamNames != null) {
				String []planTermExamName = planTermExamNames.split("、");
				List<Long> planTermExamIds = new ArrayList<>();
				for(String name : planTermExamName) {
					Long planTermExamId = ccPlanTermsExamMap.get(name);
					if(planTermExamId == null) {
						errorList.add(course);
						course.set("isError", Boolean.TRUE);
						List<String> reasons = course.get("reasons");
						reasons.add("考试学期：“" + name + "”在系统中有不存在。");
					} else {
						planTermExamIds.add(planTermExamId);
					}
				}
				String planTermExamIdsStr = "";
				for(Long id : planTermExamIds) {
					planTermExamIdsStr = planTermExamIdsStr + id + ",";
				}
				course.set("planTermExamIds", planTermExamIdsStr.length() < 2 ? null : planTermExamIdsStr.subSequence(0, planTermExamIdsStr.length() - 1));
			}
		}

		return errorList.isEmpty();
	}

	/**
	 * 验证实践导入是否正确
	 * @param courseList
	 * @param planId
	 * @author SY
	 * @version 创建时间：2018年1月12日 上午1:08:38
	 */
	public boolean validateImportPracticeist(List<Record> courseList, Long planId) {
		// 验证数据需要数据结构准备
		List<Record> errorList = Lists.newArrayList();
		// 验证excel中课程代码是否重复数据结构准备
		List<String> courseCodeNoInExcel = Lists.newArrayList();
		// 验证课程代码是否在数据库中重复数据结构准备
		String[] courseCodes = new String[courseList.size()];

		// 课程代码是否在数据库中重复
		List<CcCourse> repeatInDB = CcCourse.dao.findFilteredByColumn("plan_id",  planId);
		List<String> codeInDb = new ArrayList<>();
		for(CcCourse temp : repeatInDB) {
			String code = temp.getStr("code");
			codeInDb.add(code);
		}

		// 找到所有的专业方向
		List<CcMajorDirection> ccMajorDirections = CcMajorDirection.dao.findFilteredByColumn("plan_id", planId);
		Map<String, Long> ccMajorDirectionsMap = new HashMap<>();
		for(CcMajorDirection temp : ccMajorDirections) {
			String name = temp.getStr("name");
			Long id = temp.getLong("id");
			ccMajorDirectionsMap.put(name, id);
		}
		// 找到所有的所属模块
		List<CcCourseModule> ccCourseModules = CcCourseModule.dao.findFilteredByColumn("plan_id", planId);
		Map<String, Long> ccCourseModulesMap = new HashMap<>();
		for(CcCourseModule temp : ccCourseModules) {
			String name = temp.getStr("module_name");
			Long id = temp.getLong("id");
			ccCourseModulesMap.put(name, id);
		}
		// 找到所有的开课学期
		List<CcPlanTerm> ccPlanTermsClass = CcPlanTerm.dao.findByPlanId(planId);
		Map<String, Long> ccPlanTermsClassMap = new HashMap<>();
		for(CcPlanTerm temp : ccPlanTermsClass) {
			String yearName = temp.getStr("yearName");
			String termName = temp.getStr("termName");
			Long id = temp.getLong("planTermId");
			// 用中文冒号分开，因为excel中是这个
			ccPlanTermsClassMap.put(yearName + "：" + termName, id);
		}

		for (int i = 0; i < courseList.size(); i++) {
			Record course = courseList.get(i);
			// 初始化isError为False
			course.set("plan_id", planId);
			course.set("isError", Boolean.FALSE);
			course.set("reasons", new ArrayList<String>());
			// 验证非空字段是否都存在
			if (StrKit.isBlank(course.getStr("code")) || StrKit.isBlank(course.getStr("name"))
					|| StrKit.isBlank(course.getStr("credit")) || StrKit.isBlank(course.getStr("weekHour"))
                    || StrKit.isBlank(course.getStr("planTermClassNames"))
                ) {
				errorList.add(course);
				course.set("isError", Boolean.TRUE);
				List<String> reasons = course.get("reasons");
				reasons.add("该课程缺少课程代码、课程中文名、学分、周学时、开课学期这几个必填字段的其中之一。");
			}
			if (StrKit.notBlank(course.getStr("code"))) {
				courseCodes[i] = course.getStr("code");
				// 验证excel中学号是否重复
				if (!courseCodeNoInExcel.contains(courseCodes[i])) {
					courseCodeNoInExcel.add(courseCodes[i]);
				} else {
					errorList.add(course);
					course.set("isError", Boolean.TRUE);
					List<String> reasons = course.get("reasons");
					reasons.add("该课程代码在excel中有重复。");
				}

				// 验证是否在数据库中
				if(codeInDb.contains(courseCodes[i])) {
					errorList.add(course);
					course.set("isError", Boolean.TRUE);
					List<String> reasons = course.get("reasons");
					reasons.add("该课程代码在系统中已存在。");
				}
			}

			// 验证并找到专业方向
			String directionName = course.getStr("directionName");
			if(directionName != null) {
				Long directionId = ccMajorDirectionsMap.get(directionName);
				if(directionId == null) {
					errorList.add(course);
					course.set("isError", Boolean.TRUE);
					List<String> reasons = course.get("reasons");
					reasons.add("该专业方向在系统中不存在。");
				} else {
					// 如果存在，设置进去
					course.set("directionId", directionId);
				}
			}
			// 验证兵找到所属模块
			String moduleName = course.getStr("moduleName");
			Long moduleId = ccCourseModulesMap.get(moduleName);
			if(StrKit.isBlank(moduleName)){
				errorList.add(course);
				course.set("isError", Boolean.TRUE);
				List<String> reasons = course.get("reasons");
				reasons.add("该所属模块为空。");
			}else {
				if(moduleId == null) {
					errorList.add(course);
					course.set("isError", Boolean.TRUE);
					List<String> reasons = course.get("reasons");
					reasons.add("该所属模块在系统中不存在。");
				} else {
					// 如果存在，设置进去
					course.set("moduleId", moduleId);
				}
			}

			// 验证并找到开课学期
			String planTermClassNames = course.getStr("planTermClassNames");
			if(planTermClassNames != null) {
				String []planTermClassName = planTermClassNames.split("、");
				List<Long> planTermClassIds = new ArrayList<>();
				for(String name : planTermClassName) {
					Long planTermClassId = ccPlanTermsClassMap.get(name);
					if(planTermClassId == null) {
						errorList.add(course);
						course.set("isError", Boolean.TRUE);
						List<String> reasons = course.get("reasons");
						reasons.add("开课学期：“" + name + "”在系统中不存在。");
					} else {
						planTermClassIds.add(planTermClassId);
					}
				}
				String planTermClassIdsStr = "";
				for(Long id : planTermClassIds) {
					planTermClassIdsStr = planTermClassIdsStr + id + ",";
				}
				course.set("planTermClassIds", planTermClassIdsStr.length() < 2 ? null : planTermClassIdsStr.subSequence(0, planTermClassIdsStr.length() - 1));
			}
		}

		return errorList.isEmpty();
	}

	/**
	 * 导入后的理论课程数据批量保存
	 * @param courseList
	 * @return
	 * @author SY
	 * @version 创建时间：2018年1月12日 上午3:27:02
	 */
	public boolean saveTheoryCourses(List<Record> courseList) {
		Boolean result = Boolean.FALSE;
		/*
		 * 1. 课程保存
		 * 2. 培养计划课程学期详情表保存
		 */
		Date date = new Date();
		List<CcCourse> ccCourseSaveList = new ArrayList<>();
		List<CcPlanTermCourse> ccPlanTermCourseSaveList = new ArrayList<>();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		for(Record record : courseList) {
			CcCourse course = new CcCourse();
			Long courseId = idGenerate.getNextValue();
			course.set("id", courseId);
			course.set("create_date", date);
			course.set("modify_date", date);
			course.set("type", CcCourse.TYPE_THEORY);
			course.set("hierarchy_id", record.getLong("hierarchyId"));
			course.set("property_id", record.getLong("propertyId"));
			course.set("hierarchy_secondary_id",record.getLong("hierarchySecondId"));
			course.set("property_secondary_id",record.getLong("propertySecondaryId"));
			course.set("code", record.getStr("code"));
			course.set("name", record.getStr("name"));
			course.set("english_name", record.getStr("englishName"));
			course.set("credit", record.getStr("credit"));
			course.set("all_hours", record.getStr("allHours"));
			course.set("theory_hours", record.getStr("theoryHours"));
			course.set("experiment_hours", record.getStr("experimentHours"));
			course.set("practice_hours", record.getStr("practiceHours"));
			course.set("operate_computer_hours", record.getStr("operateComputerHours"));
			course.set("exercises_hours", record.getStr("exercisesHours"));
			course.set("dicuss_hours", record.getStr("dicussHours"));
			course.set("extracurricular_hours", record.getStr("extracurricularHours"));
			course.set("week_hour", record.getStr("weekHour"));
			course.set("plan_id", record.getLong("planId"));
			course.set("is_del", Boolean.FALSE);
			ccCourseSaveList.add(course);


			String planTermClassIdsStr = record.getStr("planTermClassIds");
			String planTermExamIdsStr = record.getStr("planTermExamIds");
			String[] planTermClassIds = planTermClassIdsStr.split(",");
			String[] planTermExamIds = planTermExamIdsStr.split(",");

			// 建立培养计划课程学期详情表
			if(planTermExamIds != null && planTermExamIds.length > 0) {
				for(String id : planTermExamIds) {
					CcPlanTermCourse temp = new CcPlanTermCourse();
					temp.set("id", idGenerate.getNextValue());
					temp.set("create_date", date);
					temp.set("modify_date", date);
					temp.set("plan_term_id", id);
					temp.set("course_id", courseId);
					temp.set("type", CcPlanTermCourse.TYPE_EXAM);
					temp.set("is_del", Boolean.FALSE);

					ccPlanTermCourseSaveList.add(temp);
				}
			}
			if(planTermClassIds != null && planTermClassIds.length > 0) {
				for(String id : planTermClassIds) {
					CcPlanTermCourse temp = new CcPlanTermCourse();
					temp.set("id", idGenerate.getNextValue());
					temp.set("create_date", date);
					temp.set("modify_date", date);
					temp.set("plan_term_id", id);
					temp.set("course_id", courseId);
					temp.set("type", CcPlanTermCourse.TYPE_CLASS);
					temp.set("is_del", Boolean.FALSE);

					ccPlanTermCourseSaveList.add(temp);
				}
			}
		}
		result = CcPlanTermCourse.dao.batchSave(ccPlanTermCourseSaveList);
		if(!result) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return result;
		}
		result = CcCourse.dao.batchSave(ccCourseSaveList);
		if(!result) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return result;
		}

		return result;
	}

	/**
	 * 导入后的实践课程数据批量保存
	 * @param courseList
	 * @return
	 * @author SY
	 * @version 创建时间：2018年1月12日 上午3:27:02
	 */
	public boolean savePracticeCourses(List<Record> courseList) {
		Boolean result = Boolean.FALSE;
		/*
		 * 1. 课程保存
		 * 2. 培养计划课程学期详情表保存
		 */
		Date date = new Date();
		List<CcCourse> ccCourseSaveList = new ArrayList<>();
		List<CcPlanTermCourse> ccPlanTermCourseSaveList = new ArrayList<>();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		for(Record record : courseList) {
			CcCourse course = new CcCourse();
			Long courseId = idGenerate.getNextValue();
			course.set("id", courseId);
			course.set("create_date", date);
			course.set("modify_date", date);
			course.set("type", CcCourse.TYPE_PRACTICE);
			course.set("module_id", record.getLong("moduleId"));
			course.set("direction_id", record.getLong("directionId"));
			course.set("code", record.getStr("code"));
			course.set("name", record.getStr("name"));
			course.set("english_name", record.getStr("englishName"));
			course.set("credit", record.getStr("credit"));
			course.set("all_hours", record.getStr("allHours"));
			course.set("week_hour", record.getStr("weekHour"));
			course.set("plan_id", record.getLong("planId"));
			course.set("is_del", Boolean.FALSE);
			ccCourseSaveList.add(course);


			String planTermClassIdsStr = record.getStr("planTermClassIds");
			String[] planTermClassIds = planTermClassIdsStr.split(",");

			if(planTermClassIds != null && planTermClassIds.length > 0) {
				for(String id : planTermClassIds) {
					CcPlanTermCourse temp = new CcPlanTermCourse();
					temp.set("id", idGenerate.getNextValue());
					temp.set("create_date", date);
					temp.set("modify_date", date);
					temp.set("plan_term_id", id);
					temp.set("course_id", courseId);
					temp.set("type", CcPlanTermCourse.TYPE_CLASS);
					temp.set("is_del", Boolean.FALSE);

					ccPlanTermCourseSaveList.add(temp);
				}
			}
		}
		result = CcPlanTermCourse.dao.batchSave(ccPlanTermCourseSaveList);
		if(!result) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return result;
		}
		result = CcCourse.dao.batchSave(ccCourseSaveList);
		if(!result) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return result;
		}

		return result;
	}

}
