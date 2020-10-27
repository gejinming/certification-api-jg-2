package com.gnet.service;

import com.gnet.Constant;
import com.gnet.model.admin.*;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.DateUtil;
import com.gnet.utils.DictUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 教学班学生相关操作
 *
 * @author xzl
 * @date 2016年11月24日
 */
@Component("ccEduclassStudentService")
public class CcEduclassStudentService {


	/**
	 * 验证教学班学生导入列表
	 * @param studentList
	 * @param ccStudents
	 * @param ccMajors
	 * @param ccClasses
	 * @param ccTeachers
	 * @param classStudents
	 * @param classStudentMap
	 * @param teacherCourseAndClassNames
	 * @param isValidateImportStudent
	 * @param ccStudentList
	 * @param studentListMap @return
	 * @param majorMap          */
	public boolean validateImportList(List<Record> studentList, List<CcStudent> ccStudents, List<CcMajor> ccMajors, List<CcClass> ccClasses, List<CcTeacher> ccTeachers, List<CcEduclassStudent> classStudents, Map<String, List<CcEduclassStudent>> classStudentMap, Set<String> teacherCourseAndClassNames, Boolean isValidateImportStudent, List<CcStudent> ccStudentList, Map<String, List<CcStudent>> studentListMap, Map<String, Long> majorMap){
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		// 验证数据需要数据结构准备
		List<Record> errorList = Lists.newArrayList();
		//验证同一学期同一开课课程下学生是否重复
		List<String> studentNos = Lists.newArrayList();
		//同一学期同一开课课程下学生对应的excel的行数
		Map<String, String> sameClassStudentMap = new HashMap<>();

		Long majorIds[] = ccMajors.isEmpty() ? new Long[]{} : new Long[ccMajors.size()];
		for(int i=0; i<ccMajors.size(); i++){
			CcMajor ccMajor = ccMajors.get(i);
			Long majorId = ccMajor.getLong("id");
			majorMap.put(ccMajor.getStr("name"), majorId);
			majorIds[i] = majorId;
		}

		Long classIds[] = ccClasses.isEmpty() ? new Long[]{} : new Long[ccClasses.size()];
		//key为行政班姓名value为行政班编号
		Map<String, Long> classMap = new HashMap<>();
		//key为行政班编号，value为专业名称
		Map<Long, String> clazzMajorMap = new HashMap<>();
		for(int i=0; i< ccClasses.size(); i++){
			CcClass ccClass = ccClasses.get(i);
			Long classId = ccClass.getLong("id");
			classMap.put(ccClass.getStr("name"), classId);
			clazzMajorMap.put(classId, ccClass.getStr("majorName"));
			classIds[i] = classId;
		}
		//数据库已经存在的学生
		List<CcStudent> existStudents = ccClasses.isEmpty() ? new ArrayList<CcStudent>() : CcStudent.dao.findFilteredByColumnIn("class_id", classIds);
		Map<Long, List<String>> existStudentMap = new HashMap<>();
		for(CcStudent ccStudent : existStudents){
			Long classId = ccStudent.getLong("class_id");
			String studentNo = ccStudent.getStr("student_no");

			List<String> studentNoList = existStudentMap.get(classId);
			if(studentNoList == null){
				studentNoList = new ArrayList<>();
				existStudentMap.put(classId, studentNoList);
			}
			studentNoList.add(studentNo);
		}


		//key为教师职工号value为教师编号
		Map<String, Long> teacherMap = new HashMap<>();
		for(CcTeacher ccTeacher : ccTeachers){
			teacherMap.put(ccTeacher.getStr("code"), ccTeacher.getLong("id"));
		}

		//key为学号，value为学生编号+姓名+专业+行政班
		Map<String, String> studentMap = new HashMap<>();
		Map<String, Long> studentIdMap = new HashMap<>();
		for(CcStudent ccStudent : ccStudents){
			studentMap.put(ccStudent.getStr("studentNo"), String.format("%s-%s-%s-%s", ccStudent.getLong("studentId"), ccStudent.getStr("name"), ccStudent.getStr("majorName"), ccStudent.getStr("className")));
			studentIdMap.put(ccStudent.getStr("studentNo"), ccStudent.getLong("studentId"));
		}

		//查询专业下所有最新的版本
		List<CcVersion> versions = CcVersion.dao.findByMajorIds(majorIds);
		Long versionIds[] = versions.isEmpty() ? new Long[]{} : new Long[versions.size()];
		//专业编号+适用年级为key，版本编号为value的map
		Map<String, Long> majorGradeMap = new HashMap<>();
		for(int i=0; i<versions.size(); i++){
			CcVersion ccVersion = versions.get(i);
			versionIds[i] = ccVersion.getLong("id");
			majorGradeMap.put(String.format("%s-%s", ccVersion.getLong("major_id"), ccVersion.getStr("apply_grade")), ccVersion.getLong("id"));
		}

		//通过版本编号查找所有版本下的课程
		List<CcCourse> ccCourses = CcCourse.dao.findFilteredByColumnIn("plan_id", versionIds);
		Map<Long,List<String>> courseCodeMap = new HashMap<>();
		for(CcCourse ccCourse : ccCourses){
			Long planId = ccCourse.getLong("plan_id");
			String code = ccCourse.getStr("code");

			List<String> codes = courseCodeMap.get(planId);
			if(codes == null){
				codes = new ArrayList<>();
				courseCodeMap.put(planId, codes);
			}
			codes.add(code);
		}


		//第一次循环得到最大和最小的开课年级和专业,限定查询的数据范围
		List<Long> majorIdsArray = Lists.newArrayList();
		Integer maxGrade = 0, minGrade = 0;
		for(Record student : studentList){
			//判断excel中的年级是否为正整数
			if(isInt(student.getStr("grade"))){
				Integer grade = Integer.valueOf(student.getStr("grade"));
				if(grade > maxGrade){
					maxGrade = grade;
				}
				if(grade < minGrade){
					minGrade = grade;
				}
			}
			Long majorId = majorMap.get(student.getStr("courseMajor"));
			if(majorId != null && !majorIdsArray.contains(majorId)){
				majorIdsArray.add(majorId);
			}
		}

		Long excelMajorIds[] = majorIdsArray.toArray(new Long[majorIdsArray.size()]);

		//开课专业+开课年级+教师编号+课程代码+开课学期+达成度计算类型为key，教师开课编号为value
		Map<String, Long> teacherCourseMap = new HashMap<>();
		//教学班名称和编号的map
		Map<String, Long> classNameAndIdMap = new HashMap<>();
		//开课专业+开课年级+教师编号+课程代码+开课学期+达成度计算类型为key,学生学号集合为value
		Map<String, List<Long>> classStudentsMap = new HashMap<>();
		List<CcTeacherCourse> ccTeacherCourses = excelMajorIds !=null && excelMajorIds.length > 0 ? CcTeacherCourse.dao.findByGradeAndMjorId(maxGrade, minGrade, excelMajorIds) : new ArrayList<CcTeacherCourse>();
		for(CcTeacherCourse ccTeacherCourse : ccTeacherCourses){
			String className = ccTeacherCourse.getStr("educlass_name");
			Long classId = ccTeacherCourse.getLong("classId");
			Long studentId = ccTeacherCourse.getLong("student_id");
			//开课专业+开课年级+教师编号+课程代码+开课学期+达成度计算类型
			String teacherCourseCode = String.format("%s%s%s%s%s-%s-%s%s%s", ccTeacherCourse.getStr("majorName"), ccTeacherCourse.getInt("grade"), ccTeacherCourse.getLong("teacher_id"), ccTeacherCourse.getStr("code"), ccTeacherCourse.getInt("start_year"), ccTeacherCourse.getInt("end_year"), DictUtils.findLabelByTypeAndKey("termTypeName", ccTeacherCourse.getInt("term_type")), ccTeacherCourse.getInt("term"), DictUtils.findLabelByTypeAndKey("courseResultType", ccTeacherCourse.getInt("result_type")));
			//开课专业+开课年级+课程代码+开课学期
			String courseTermCode = String.format("%s%s%s%s-%s-%s%s", ccTeacherCourse.getStr("majorName"), ccTeacherCourse.getInt("grade"),  ccTeacherCourse.getStr("code"), ccTeacherCourse.getInt("start_year"), ccTeacherCourse.getInt("end_year"), DictUtils.findLabelByTypeAndKey("termTypeName", ccTeacherCourse.getInt("term_type")), ccTeacherCourse.getInt("term"));
			if(teacherCourseMap.get(teacherCourseCode) == null){
				teacherCourseMap.put(teacherCourseCode, ccTeacherCourse.getLong("id"));
			}

			//有可能已经排了课但还未建立教学班
			if(StrKit.notBlank(className)){
				String classCode = String.format("%s-%s", teacherCourseCode, className);
				if(classNameAndIdMap.get(classCode) == null){
					classNameAndIdMap.put(classCode, classId);
				}
			}

			//相同教师开课课程下的学生
			if(studentId != null){
				List<Long> studentIds = classStudentsMap.get(courseTermCode);
				if(studentIds == null){
					studentIds = new ArrayList<>();
					classStudentsMap.put(courseTermCode, studentIds);
				}
				studentIds.add(studentId);
			}

		}

		Map<String, String> noAndNameMap = new HashMap<>();
		Map<String, String> noAndIndexMap = new HashMap<>();
		//第二次循环验证每一行数据的正确性
		for(int i=0; i< studentList.size(); i++){
			Date date = new Date();
			Record student = studentList.get(i);
			student.set("isError", false);
			student.set("reasons", new ArrayList<String>());

			String courseMajor = student.getStr("courseMajor");
			String grade = student.getStr("grade");
			String teacherNo = student.getStr("teacherNo");
			String courseCode = student.getStr("courseCode");
			String term = student.getStr("term");
			String eduClassName = student.getStr("eduClassName");
			String type = student.getStr("type");
			String studentNo = student.getStr("studentNo");
			String studentName =  student.getStr("studentName");
			String major = student.getStr("major");
			String classes = student.getStr("classes");

			if(StrKit.isBlank(courseMajor) || StrKit.isBlank(grade) || StrKit.isBlank(teacherNo) || StrKit.isBlank(courseCode) || StrKit.isBlank(term) || StrKit.isBlank(eduClassName)
					|| StrKit.isBlank(type) || StrKit.isBlank(studentNo) || StrKit.isBlank(studentName) || StrKit.isBlank(major)
					|| StrKit.isBlank(classes)){
				errorList.add(student);
				student.set("isError", true);
				List<String> reasons = student.get("reasons");
				reasons.add(String.format("excel中第%s行缺少开课专业、开课年级、职工号、课程代码、开课学期、教学班、达成度计算类型、学号、姓名、专业、行政班中的一个或多个", i+3));
			}

			//验证开课年级必须是正整数
			if(!isInt(grade)){
				errorList.add(student);
				student.set("isError", true);
				List<String> reasons = student.get("reasons");
				reasons.add(String.format("excel中的第%s行的开课年级必须是正整数", i+3));
			}

			//验证开课学期格式正确性，正确格式：2017-2018-长1或2017-2018-短1
			String terms[] = term.split("-");
			if(terms == null || terms.length != 3){
				errorList.add(student);
				student.set("isError", true);
				List<String> reasons = student.get("reasons");
				reasons.add(String.format("excel中的第%s行的开课学期数据格式不正确", i+3));
				return  errorList.isEmpty();
			}
			String termType = terms[2].substring(0,1);
			String termNum = terms[2].substring(1,2);
			if(!isInt(terms[0]) || !isInt(terms[1]) || (!termType.equals("长") && !termType.equals("短")) || !isInt(termNum)){
				errorList.add(student);
				student.set("isError", true);
				List<String> reasons = student.get("reasons");
				reasons.add(String.format("excel中的第%s行的开课学期数据格式不正确", i+3));
			}

			//验证达成度类型是否正确
			if(!CcTeacherCourse.TYPE_EVALUATE.equals(type) && !CcTeacherCourse.TYPE_SCORE.equals(type) && !CcTeacherCourse.TYPE_SCORE2.equals(type)){
				errorList.add(student);
				student.set("isError", true);
				List<String> reasons = student.get("reasons");
				reasons.add(String.format("excel中的第%s行的达成度计算类型无法识别，达成度计算类型只有%s或%s", i+3, CcTeacherCourse.TYPE_EVALUATE, CcTeacherCourse.TYPE_SCORE,CcTeacherCourse.TYPE_SCORE2));
			}

			String studentNoStr = String.format("%s%s%s%s%s", courseMajor, grade, courseCode, term, studentNo);
			String column = sameClassStudentMap.get(studentNoStr);
			if(StrKit.isBlank(column)){
				sameClassStudentMap.put(studentNoStr, String.format("%s", i+3));
			}else{
				sameClassStudentMap.put(studentNoStr, String.format("%s,%s", column, i+3));
			}

			//验证学生是否重复
			if(!studentNos.contains(studentNoStr)){
				studentNos.add(studentNoStr);
			}else{
				errorList.add(student);
				student.set("isError", Boolean.TRUE);
				List<String> reasons = student.get("reasons");
				reasons.add(String.format("excel中的第%s行的学生，加入了同一学期同一门课程中了", sameClassStudentMap.get(studentNoStr)));
			}

			//学校下是否有该开课专业
			if(!majorMap.containsKey(courseMajor)){
				errorList.add(student);
				student.set("isError", Boolean.TRUE);
				List<String> reasons = student.get("reasons");
				reasons.add(String.format("学校下不存在excel中第%s行的开课专业", i+3));
			}

			//学校下是否有该教师职工号
			if(!teacherMap.containsKey(teacherNo)){
				errorList.add(student);
				student.set("isError", Boolean.TRUE);
				List<String> reasons = student.get("reasons");
				reasons.add(String.format("学校下不存在excel中第%s行的教师职工号", i+3));
			}

			//学校下是否存在该学生
			if(!studentMap.containsKey(studentNo)){
				//防止重复添加学生信息
				List<String> unRepeatStudents = Lists.newArrayList();
				//isValidateImportStudent为true时学生必须已存在系统中
				if(isValidateImportStudent){
					errorList.add(student);
					student.set("isError", Boolean.TRUE);
					List<String> reasons = student.get("reasons");
					reasons.add(String.format("学校下不存在excel中第%s行的学生学号", i+3));
				}else{
					//判断新增的学生专业是否存在
					if(!majorMap.containsKey(major)){
						errorList.add(student);
						student.set("isError", Boolean.TRUE);
						List<String> reasons = student.get("reasons");
						reasons.add(String.format("学校下不存在excel中第%s行的专业", i+3));
					}


					if(noAndNameMap.get(studentNo) == null){
						noAndNameMap.put(studentNo, studentName);
						noAndIndexMap.put(studentNo, String.format("%s", i+3));
					}else{
						if(!studentName.equals(noAndNameMap.get(studentNo))){
							noAndIndexMap.put(studentNo, String.format("%s,%s", noAndIndexMap.get(studentNo), i+3));
							errorList.add(student);
							student.set("isError", Boolean.TRUE);
							List<String> reasons = student.get("reasons");
							reasons.add(String.format("excel中第%s行的学号都为%s,姓名却不相同", noAndIndexMap.get(studentNo), studentNo));
						}
					}

					CcStudent ccStudent = new CcStudent();
					Long studentId = idGenerate.getNextValue();
					studentIdMap.put(studentNo, studentId);
					ccStudent.set("id", studentId);
					ccStudent.set("create_date", date);
					ccStudent.set("modify_date", date);
					ccStudent.set("student_no", studentNo);
					ccStudent.set("name", studentName);
					ccStudent.set("sex", Constant.SEX_MAN);
					ccStudent.set("matriculate_date", DateUtil.stringtoDate(String.format("%s-09-01", grade), DateUtil.LONG_DATE_FORMAT));
					ccStudent.set("grade", grade);
					ccStudent.set("is_del", false);

					//未加入系统的学生本身不重复
					if(!unRepeatStudents.contains(studentNo)){
						unRepeatStudents.add(studentNo);
						Long classId = classMap.get(classes);
						List<String> existStudentNos = existStudentMap.get(classId);
						if(classId != null){
							//验证已经存在的教学班是否在系统对应的专业下
							if(!major.equals(clazzMajorMap.get(classId))){
								errorList.add(student);
								student.set("isError", Boolean.TRUE);
								List<String> reasons = student.get("reasons");
								reasons.add(String.format("excel中第%s行的学生的行政班%s，对应的专业是%s系统中专业为%s", i+3, classes, major, clazzMajorMap.get(classId)));
							}
							if(existStudentNos ==null || existStudentNos.isEmpty() || !existStudentNos.contains(studentNo)){
								ccStudent.set("class_id", classId);
								ccStudentList.add(ccStudent);
							}
						}else{
							String key = String.format("%s-%s", major, classes);
							List<CcStudent> students = studentListMap.get(key);
							if(students == null){
								students = new ArrayList<>();
								studentListMap.put(key, students);
							}
							students.add(ccStudent);
						}
					}
				}
			}else{
				String[] studentValues = studentMap.get(studentNo).split("-");
				//学生学号与姓名与系统中是否一致
				if(!studentName.equals(studentValues[1])){
					errorList.add(student);
					student.set("isError", Boolean.TRUE);
					List<String> reasons = student.get("reasons");
					reasons.add(String.format("excel中第%s行的学号为%s的学生姓名为%s，系统中为%s", i+3, studentNo, studentName, studentValues[1]));
				}

				//学生专业和系统中是否一致
				if(!major.equals(studentValues[2])){
					errorList.add(student);
					student.set("isError", Boolean.TRUE);
					List<String> reasons = student.get("reasons");
					reasons.add(String.format("excel中第%s行的学号为%s的学生，专业为%s,系统中为%s", i+3, studentNo, major, studentValues[2]));
				}

				//学生行政班与系统中是否一致
				if(!classes.equals(studentValues[3])){
					errorList.add(student);
					student.set("isError", Boolean.TRUE);
					List<String> reasons = student.get("reasons");
					reasons.add(String.format("excel中第%s行的学号为%s的学生，行政班为%s,系统中为%s", i+3, studentNo, classes, studentValues[3]));
				}
			}

			//判断对应的开课专业和年级是否存在版本
			if(majorGradeMap == null || majorGradeMap.isEmpty()){
				errorList.add(student);
				student.set("isError", Boolean.TRUE);
				List<String> reasons = student.get("reasons");
				reasons.add(String.format("excel中第%s行开课专业为%s开课年级为%s的数据，在系统中找不到对应的持续改进版本", i+3, courseMajor, grade));
			}

			//对应开课专业和年级下是否存在版本
			Boolean isExistVersion = false;
			//对应版本下是否有该门课
			for(Map.Entry<String, Long> entry : majorGradeMap.entrySet()){
				String keys[] = entry.getKey().split("-");
				Long majorId = Long.valueOf(keys[0]);
				String applyGrade = keys[1];
				Long versionId = entry.getValue();

				if(majorId.equals(majorMap.get(courseMajor)) && isGradeCorrect(grade, applyGrade)){
					//判断对应版本下是否有该门课程
					if(!courseCodeMap.get(versionId).contains(courseCode)){
						errorList.add(student);
						student.set("isError", Boolean.TRUE);
						List<String> reasons = student.get("reasons");
						reasons.add(String.format("excel中第%s行开课专业为%s开课年级为%s数据，对应持续改进版本中没有课程代码为%s的课程", i+3, major, grade, courseCode));
					}
				}else{
					isExistVersion = true;
				}
			}

			//判断对应的开课专业和年级是否存在版本
			if(!isExistVersion){
				errorList.add(student);
				student.set("isError", Boolean.TRUE);
				List<String> reasons = student.get("reasons");
				reasons.add(String.format("excel中第%s行开课专业为%s开课年级为%s的数据，在系统中找不到对应的持续改进版本", i+3, major, grade));
			}

			//判断是否有对应教师开课信息
			String excelTeacherCourse = String.format("%s%s%s%s%s%s", courseMajor, grade, teacherMap.get(teacherNo), courseCode, term, type);
			String excelCourseTerm = String.format("%s%s%s%s", courseMajor, grade, courseCode, term );
			Long teacherCourseId = teacherCourseMap.get(excelTeacherCourse);

			if(teacherCourseId == null){
				errorList.add(student);
				student.set("isError", Boolean.TRUE);
				List<String> reasons = student.get("reasons");
				reasons.add(String.format("系统中没有excel中第%s行对应的教师开课数据", i+3));
			}else{
				//判断相同的开课信息下学生是否重复
				List<Long> studentIds = classStudentsMap.get(excelCourseTerm);
				Long studentId = studentIdMap.get(studentNo);
				if(studentIds != null && !studentIds.isEmpty()){
					if(studentIds.contains(studentId)){
						errorList.add(student);
						student.set("isError", Boolean.TRUE);
						List<String> reasons = student.get("reasons");
						reasons.add(String.format("excel第%s行学号为%s的学生，已经在系统中相同学期、相同的课程中存在了", i+3, studentNo));
					}
				}

				//判断excel中的开课教学班系统中是否已经存在
				String excelClassCode = String.format("%s-%s", excelTeacherCourse, eduClassName);
				Long classId = classNameAndIdMap.get(excelClassCode);
				if(classId != null){
					CcEduclassStudent ccEduclassStudent = new CcEduclassStudent();
					ccEduclassStudent.set("id", idGenerate.getNextValue());
					ccEduclassStudent.set("create_date", date);
					ccEduclassStudent.set("modify_date", date);
					ccEduclassStudent.set("class_id", classId);
					ccEduclassStudent.set("student_id", studentId);
					ccEduclassStudent.set("is_del", false);
					classStudents.add(ccEduclassStudent);
				}else{
					CcEduclassStudent ccEduclassStudent = new CcEduclassStudent();
					ccEduclassStudent.set("id", idGenerate.getNextValue());
					ccEduclassStudent.set("create_date", date);
					ccEduclassStudent.set("modify_date", date);
					ccEduclassStudent.set("student_id", studentId);
					ccEduclassStudent.set("is_del", false);
					String classKey = String.format("%s-%s", teacherCourseId, eduClassName);
					teacherCourseAndClassNames.add(classKey);
					List<CcEduclassStudent> ccEduclassStudents = classStudentMap.get(classKey);
					if(ccEduclassStudents == null){
						ccEduclassStudents = new ArrayList<>();
						classStudentMap.put(classKey, ccEduclassStudents);
					}
					ccEduclassStudents.add(ccEduclassStudent);
				}
			}
		}
		return errorList.isEmpty();
	}

	//某个专业下的开课年级是否正确
	public boolean isGradeCorrect(String grade, String applyGrade){
		//如果开课年级不是正整数直接返回错误
		if(!isInt(grade)){
			return false;
		}
		if(applyGrade.endsWith(CcVersion.GRADE_CHARACTER) && Integer.valueOf(grade) >= Integer.valueOf(applyGrade.substring(0, applyGrade.length()-1))){
			return true;
		}else{
			if(Arrays.asList(applyGrade.split(CcVersion.SPLIT)).contains(grade)){
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断字符串是否是正整数
	 * @return
	 */
	public boolean isInt(String number){
		String regex="^[1-9]+[0-9]*$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(number);
		return m.find();
	}

}
