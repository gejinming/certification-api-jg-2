package com.gnet.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.*;
import org.junit.experimental.theories.FromDataPoints;
import org.springframework.stereotype.Component;

import com.gnet.plugin.poi.RowDefinition;
import com.gnet.plugin.poi.RowDefinition.GroupColumnDefinition;
import com.gnet.utils.DateUtil;
import com.gnet.utils.DictUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;

/**
 * 学生相关操作
 *
 * @author wct
 * @date 2016年11月24日
 */
@Component("ccStudentService")
public class CcStudentService {

	/**
	 * 导入数据验证 1.验证非空字段是否数据都存在 2.验证学号是否存在 3.添加班级是否存在
	 * 
	 * @param ccStudents
	 * @param schoolId
	 * @param userDepartmentId 
	 * @return
	 */
	public boolean validateImportList(List<Record> ccStudents, Long schoolId, Long userDepartmentId) {
		// 验证数据需要数据结构准备
		List<Record> errorList = Lists.newArrayList();
		// 验证excel中学号是否重复数据结构准备
		List<String> studentNoInExcel = Lists.newArrayList();
		// 验证学号是否在数据库中重复数据结构准备
		String[] studentNos = new String[ccStudents.size()];
		Map<String, Record> studentNoToStudentMap = Maps.newHashMap();
		// 验证班级是否存在验证数据结构准备
		Map<String, List<Record>> classMap = Maps.newHashMap();

		for (int i = 0; i < ccStudents.size(); i++) {
			Record student = ccStudents.get(i);
			// 初始化isError为False
			student.set("isError", Boolean.FALSE);
			student.set("reasons", new ArrayList<String>());
			// 验证非空字段是否都存在
			if (StrKit.isBlank(student.getStr("studentNo")) || StrKit.isBlank(student.getStr("name"))
					|| StrKit.isBlank(student.getStr("sex")) || StrKit.isBlank(student.getStr("matriculateDate"))
					|| StrKit.isBlank(student.getStr("status"))) {
				errorList.add(student);
				student.set("isError", Boolean.TRUE);
				List<String> reasons = student.get("reasons");
				reasons.add("该学生缺少学号、姓名、性别、入学时间必填字段的其中之一。");
			}
			// 验证excel中学号是否重复
			if (StrKit.notBlank(student.getStr("studentNo"))) {
				studentNos[i] = student.getStr("studentNo");
				if (!studentNoInExcel.contains(studentNos[i])) {
					studentNoInExcel.add(studentNos[i]);
					// 当学号不能为空且excel中不存在时，将对象存入map
					studentNoToStudentMap.put(student.getStr("studentNo"), student);
				} else {
					errorList.add(student);
					student.set("isError", Boolean.TRUE);
					List<String> reasons = student.get("reasons");
					reasons.add("该学生学号在excel中有重复。");
				}
			}
			// 班级map
			String className = student.getStr("className");
			if (StrKit.notBlank(className)) {
				if (classMap.get(className) == null) {
					classMap.put(className, new ArrayList<Record>());
				}
				classMap.get(className).add(student);
			}
			// 判断入学时间是否符合日期规则
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		    format.setLenient(false);  
		    try {
				format.parse(student.getStr("matriculateDate"));
			} catch (ParseException e) {
				errorList.add(student);
				student.set("isError", Boolean.TRUE);
				List<String> reasons = student.get("reasons");
				reasons.add("该学生的入学时间格式不符合规则。");
			}  

		}

		// 学号是否在数据库中重复
		List<Record> repeatInDB = CcStudent.dao.existedStudents(studentNos, schoolId);
		for (Record record : repeatInDB) {
			Record student = studentNoToStudentMap.get(record.getStr("student_no"));
			errorList.add(student);
			student.set("isError", Boolean.TRUE);
			List<String> reasons = student.get("reasons");
			reasons.add("该学生学号在系统中已存在。");
		}

		// 班级是否存在验证
		List<CcClass> ccClasses = CcClass.dao.getClassBySchool(schoolId);
		List<String> classNameInDB = Lists.newArrayList();
		Map<String, Integer> classNameYearMap = Maps.newHashMap();
		for (CcClass ccClass : ccClasses) {
			String className = ccClass.getStr("name");
			classNameInDB.add(className);
			if (classNameYearMap.get(className) == null) {
				classNameYearMap.put(className, ccClass.getInt("grade"));
			}
		}
		
		// 用户所在部门及以下的部门(用于验证当前操作人增加某个部门以下的教师时候，是否有权限)
		List<Office> userclasses = CcSchool.dao.findByDepartment(userDepartmentId);
		List<String> classNameInUserDB = Lists.newArrayList();
		for (Office office : userclasses) {
			classNameInUserDB.add(office.getStr("name"));
		}
		
		for (String className : classMap.keySet()) {
			if (!classNameInDB.contains(className)) {
				for (Record student : classMap.get(className)) {
					errorList.add(student);
					student.set("isError", Boolean.TRUE);
					List<String> reasons = student.get("reasons");
					reasons.add("该学生的行政班不存在于系统中。");
				}
			} else if(!classNameInUserDB.contains(className)) {
				// 当前操作人增加某个教学班以下的学生时候，如果没有权限，则提示其错误
				for (Record student : classMap.get(className)) {
					errorList.add(student);
					student.set("isError", Boolean.TRUE);
					List<String> reasons = student.get("reasons");
					reasons.add("该学生的教学班您没有权限操作！");
				}
			} else {
				for (Record student : classMap.get(className)) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date matriculateDate = null;
					try {
						matriculateDate = sdf.parse(student.getStr("matriculateDate"));
					} catch (ParseException e) {
						e.printStackTrace();
						errorList.add(student);
						student.set("isError", Boolean.TRUE);
						List<String> reasons = student.get("reasons");
						reasons.add("该学生入学日期格式不正确。");
					}

					if (!classNameYearMap.get(className).equals(DateUtil.getYear(matriculateDate))) {
						errorList.add(student);
						student.set("isError", Boolean.TRUE);
						List<String> reasons = student.get("reasons");
						reasons.add("该学生的年级" + DateUtil.getYear(matriculateDate) + "和行政班年级 "
								+ classNameYearMap.get(className) + "不一致。");
					}
				}
			}
		}

		return errorList.isEmpty();
	}

	/**
	 * 获得某一个成绩组成下的考核分析法的层次结构
	 * @param ccTeacherCourse
	 * @param courseGradeComposeId
	 * @param startNaturalColumnIndex
	 * @return
	 */
	public RowDefinition getSingleScoreDefinition(CcTeacherCourse ccTeacherCourse, Long courseGradeComposeId, Integer startNaturalColumnIndex,Long batchId) {
		List<Long> courseGradeComposeIds = Lists.newArrayList();
		courseGradeComposeIds.add(courseGradeComposeId);
		return getScoreDefinition(ccTeacherCourse, courseGradeComposeIds, startNaturalColumnIndex,batchId);
	}

	/**
	 * 获得某一些成绩组成下的考核分析法的层次结构
	 * @param ccTeacherCourse
	 * 			教师开课信息
	 * @param courseGradeComposeIds
	 * 			开课课程成绩组成元素编号列表（如果是空，则表示不限制）
	 * @param startNaturalColumnIndex 
	 * 			第几列开始
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年10月12日 下午2:40:50 
	 */
	public RowDefinition getScoreDefinition(CcTeacherCourse ccTeacherCourse, List<Long> courseGradeComposeIds, Integer startNaturalColumnIndex,Long batchId) {
		//------------------------------------  数据准备 ----------------------------------------
		
		Long teacherCourseId = ccTeacherCourse.getLong("id");
		
		// Map<开课课程成绩组成元素编号, 成绩组成名称>
		Map<Long, String> courseGradecomposeMap = new HashMap<>();
		// Map<开课课程成绩组成元素编号, 课程目标名称或题号>
		// TODO 2020/11/04 GJM 需求要求一个教学班成绩整体导入。一次性导入所有成绩，所以增加了一种适应于所有成绩组成的导入模板
		Map<Long, List<String>> ccCourseGradecomposeIndicationMap = new HashMap<>();
		Map<Long, Integer> ccCourseGradecomposeInputTypeMap = new HashMap<>();
		// Map<开课课程成绩组成元素编号, 课程目标排序>
		Map<Long, List<String>> ccCourseGradecomposeIndicationScortMap = new HashMap<>();
		ArrayList<Long> batchCourseGradecomposeId = new ArrayList<>();
		ArrayList<Long> batchIds = new ArrayList<>();
		// 获取教师开课的成绩组成
		List<CcCourseGradecompose>	courseGradecomposeList = CcCourseGradecompose.dao.findByTeacherCourseIdAndCourseGradeComposeIdsOrderBySort(teacherCourseId, courseGradeComposeIds,batchId);

		String[] gradecomposes = new String[courseGradecomposeList.size()];
		for(int i = 0; i < courseGradecomposeList.size(); i++) {
			CcCourseGradecompose temp = courseGradecomposeList.get(i);
			Long courseGradecomposeId = temp.getLong("id");
			Long batchId0=null;
			String batchName=null;
			//这次是新增一次性导入各个成绩组成成绩的模板，要兼容之前单个批次导入的模板设置，排除有批次的，特殊处理
			if (batchId ==null){
				 batchId0 = temp.getLong("batchId");
				 batchName = temp.getStr("batchName");
			}

			if (batchName == null ){
				gradecomposes[i] = temp.getStr("name");
				ccCourseGradecomposeIndicationMap.put(courseGradecomposeId, new ArrayList<String>());
			}else {
				gradecomposes[i] = temp.getStr("name")+"-"+batchName;
				//含有批次的成绩组成
				if (!batchCourseGradecomposeId.contains(courseGradecomposeId)){
					batchCourseGradecomposeId.add(courseGradecomposeId);
				}
				if (!batchIds.contains(batchId0)) {
					batchIds.add(batchId0);
				}
				//含有批次的Key courseGradecomposeId+batchId0
				ccCourseGradecomposeIndicationMap.put(courseGradecomposeId+batchId0, new ArrayList<String>());
				courseGradeComposeIds.remove(courseGradecomposeId);

			}
			courseGradecomposeMap.put(courseGradecomposeId, gradecomposes[i]);
			if (ccCourseGradecomposeInputTypeMap.get(courseGradecomposeId)==null){
				//输入类型
				ccCourseGradecomposeInputTypeMap.put(courseGradecomposeId,temp.getInt("input_score_type"));
			}

		}
		//这个是单个批次导入制作模板场景的使用
		if (batchId !=null){
			//批次直接录入形式，每个批次包含不同的课程目标
			List<CcCourseGradecomposeBatchIndication> courseBatchGradecomposeIds = CcCourseGradecomposeBatchIndication.dao.findByCourseBatchGradecomposeIds0(courseGradeComposeIds, batchId,null);
			for (CcCourseGradecomposeBatchIndication temp : courseBatchGradecomposeIds){
				Long courseGradecomposeId = temp.getLong("course_gradecompose_id");
				String content = temp.getInt("sort")+":"+temp.getStr("content");
				//String sort = temp.getInt("sort").toString();
				List<String> list = ccCourseGradecomposeIndicationMap.get(courseGradecomposeId);
				list.add(content);
			}
		}else {
			//含有直接录入批次的成绩组成
			if (batchCourseGradecomposeId.size()>0){
				List<CcCourseGradecomposeBatchIndication> courseBatchGradecomposeIds = CcCourseGradecomposeBatchIndication.dao.findByCourseBatchGradecomposeIds(batchCourseGradecomposeId, batchIds,null);
				for (CcCourseGradecomposeBatchIndication temp : courseBatchGradecomposeIds){
					Long courseGradecomposeId = temp.getLong("course_gradecompose_id");
					Long batchId1 = temp.getLong("batch_id");
					String content = temp.getInt("sort")+":"+temp.getStr("content");
					//String sort = temp.getInt("sort").toString();
					List<String> list = ccCourseGradecomposeIndicationMap.get(courseGradecomposeId+batchId1);
					list.add(content);
					//剔除直接录入批次的，就会剩下题目批次成绩组成的了
					batchCourseGradecomposeId.remove(courseGradecomposeId);
					batchIds.remove(batchId1);
				}

			}
			//剔除直接录入批次之后 只有题目批次成绩组成的
			if (batchCourseGradecomposeId.size()>0){
				List<CcCourseGradeComposeDetail> ccCourseGradeComposeDetails = CcCourseGradeComposeDetail.dao.topicList(batchCourseGradecomposeId, batchIds);
				for (CcCourseGradeComposeDetail temps: ccCourseGradeComposeDetails){
					Long courseGradecomposeId = temps.getLong("course_gradecompose_id");
					Long batchId1 = temps.getLong("batch_id");
					List<String> list = ccCourseGradecomposeIndicationMap.get(courseGradecomposeId+batchId1);
					//题号
					String name = temps.getStr("name");
					//最高分
					BigDecimal score = temps.getBigDecimal("score");
					String detailInfo=name+"("+score+")";
					//上面ccCourseGradecomposeIndicationList里题目类型的成绩组成包含多个课程目标，会再便利一次所以要题目去重
					if (!list.contains(detailInfo)){
						list.add(detailInfo);
					}

				}
			}
			List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndicationList = CcCourseGradecomposeIndication.dao.findDetailByTeacherCourseIdAndCourseGradeComposeIds(teacherCourseId, courseGradeComposeIds);
			for(CcCourseGradecomposeIndication temp : ccCourseGradecomposeIndicationList) {
				Long courseGradecomposeId = temp.getLong("course_gradecompose_id");
				String content = temp.getInt("sort") + ":" + temp.getStr("content");
				//String sort = temp.getInt("sort").toString();
				//录入成绩类型,1:单批次指标点成绩直接输入,2单批次题目明细计算获得，3：多批次由题目明细计算得，4：多批次指标点成绩直接输入
				Integer inputType = ccCourseGradecomposeInputTypeMap.get(courseGradecomposeId);

				if (inputType == 1) {
					List<String> list = ccCourseGradecomposeIndicationMap.get(courseGradecomposeId);
					//直接输入类型的
					list.add(content);
				}else if (inputType == 2){
					List<String> list = ccCourseGradecomposeIndicationMap.get(courseGradecomposeId);
					//题目列表
					List<CcCourseGradeComposeDetail> ccCourseGradeComposeDetails = CcCourseGradeComposeDetail.dao.topicList0(courseGradecomposeId, null);
					for (CcCourseGradeComposeDetail temps: ccCourseGradeComposeDetails){
						//题号
						String name = temps.getStr("name");
						//最高分
						BigDecimal score = temps.getBigDecimal("score");
						String detailInfo=name+"("+score+")";
						//上面ccCourseGradecomposeIndicationList里题目类型的成绩组成包含多个课程目标，会再便利一次所以要题目去重
						if (!list.contains(detailInfo)){
							list.add(detailInfo);
						}

					}
				}


				//list.add(sort);
			}
		}

		//这个值应该是标题合并的行数+3 columnNum + 3
        int columnNum = 0;
        for (CcCourseGradecompose temp : courseGradecomposeList) {
            Long courseGradecomposeId = temp.getLong("id");
			Long batchId0 = temp.getLong("batchId");
			List<String> list;
			if (batchId0 !=null && batchId == null){
				list= ccCourseGradecomposeIndicationMap.get(courseGradecomposeId+batchId0);
			}else {
				 list = ccCourseGradecomposeIndicationMap.get(courseGradecomposeId);
			}

            int size = list.size();
            if (size == 0) {
                list.add("");
                columnNum++;
                continue;
            }
            columnNum += size;
        }
		
		// ----------------------------------------- 处理head -----------------------------------
		RowDefinition rowDefinition = new RowDefinition(columnNum + 3);

        RowDefinition.ColumnDefinition no = RowDefinition.ColumnDefinition.createCommonColumn(0, "序号");
        RowDefinition.ColumnDefinition studentno = RowDefinition.ColumnDefinition.createCommonColumn(1, "学号");
        RowDefinition.ColumnDefinition name = RowDefinition.ColumnDefinition.createCommonColumn(2, "姓名");
        RowDefinition.ColumnDefinition clazzname = RowDefinition.ColumnDefinition.createCommonColumn(3, "教学班名称");

        RowDefinition.ValidateDefinition header2validate = new RowDefinition.ValidateDefinition(gradecomposes);
        
        rowDefinition.addColumn(no);
        rowDefinition.addColumn(studentno);
        rowDefinition.addColumn(name);
        rowDefinition.addColumn(clazzname);
        
        // 第二层设置
        Integer firstNaturalColumnIndex = startNaturalColumnIndex;
		Integer secondNaturalColumnIndex = startNaturalColumnIndex;
		for(int i = 0; i < courseGradecomposeList.size(); i++) {
			// sencondMap1 赋值
			CcCourseGradecompose temp = courseGradecomposeList.get(i);
			Long courseGradecomposeId = temp.getLong("id");
			String batchName = temp.getStr("batchName");
			Long batchId1 = temp.getLong("batchId");
			String gradecomposeName;
			List<String> indicationList;
			if (batchName != null && batchId==null && batchId1 != null){
				gradecomposeName = temp.getStr("name")+"-"+batchName;
				//课程目标或者题号
				 indicationList = ccCourseGradecomposeIndicationMap.get(courseGradecomposeId+batchId1);
			}else {
				gradecomposeName = temp.getStr("name");
				//课程目标或者题号
				indicationList=ccCourseGradecomposeIndicationMap.get(courseGradecomposeId);
			}
			Integer inputType = ccCourseGradecomposeInputTypeMap.get(courseGradecomposeId);

			RowDefinition.ValidateDefinition subGrade2Validate = new RowDefinition.ValidateDefinition(indicationList.toArray(new String[indicationList.size()]));
			
			Integer number = indicationList.size();

            if (number == 0) {
                number = 1;
            }

            RowDefinition.ColumnDefinition gradecompose = RowDefinition.ColumnDefinition
	                .createGroupColumn(firstNaturalColumnIndex - 1, firstNaturalColumnIndex - 2 + number, gradecomposeName)
	                .setIndexs(rowDefinition.getIndexs())
	                .setValidateDefinition(header2validate);
			firstNaturalColumnIndex = firstNaturalColumnIndex + number;
			
			for(int indicationIndex = 0;secondNaturalColumnIndex != firstNaturalColumnIndex; indicationIndex++) {
				RowDefinition.ColumnDefinition cloumn;
				// sencondMap2 赋值
				//直接录入方式，批次直接录入方式
				if (inputType==1 || inputType==4){
					 cloumn = RowDefinition.ColumnDefinition
							.createCommonColumn(secondNaturalColumnIndex - 1, "CO" + indicationList.get(indicationIndex))
							.setValidateDefinition(subGrade2Validate)
							;
				}else{
					cloumn = RowDefinition.ColumnDefinition
							.createCommonColumn(secondNaturalColumnIndex - 1,indicationList.get(indicationIndex))
							.setValidateDefinition(subGrade2Validate)
							;
				}

				((GroupColumnDefinition) gradecompose).addColumn(cloumn);
				secondNaturalColumnIndex++;
			}
			rowDefinition.addColumn(gradecompose);
		}
		
		return rowDefinition;
	}

	/**
	 * 获得考核分析法的层次结构
	 * @param ccTeacherCourse
	 * 			教师开课信息
	 * @param startNaturalColumnIndex 
	 * 			第几列开始
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年10月12日 下午2:40:50 
	 */
	public RowDefinition getScoreDefinition(CcTeacherCourse ccTeacherCourse, Integer startNaturalColumnIndex) {
		return getScoreDefinition(ccTeacherCourse, null, startNaturalColumnIndex,null);
	}


	/**
	 * 获得考评点分析法的层次结构
	 * @param ccTeacherCourse
	 * 			教师开课信息
	 * @param startNaturalColumnIndex 
	 * 			第几列开始
	 * @author SY 
	 * @version 创建时间：2017年10月10日 上午11:15:38
	 * return 
	 * 	Map<第几行，Map<第几列,Map<当前成绩组成细啊有几个考评点/考评点名称数组, 对应的数值>>> 
	 */
	public RowDefinition getEvaluateDefinition(CcTeacherCourse ccTeacherCourse, Integer startNaturalColumnIndex) {
		
		Long teacherCourseId = ccTeacherCourse.getLong("id");
		Long courseId = ccTeacherCourse.getLong("course_id");
		
		List<String> evaluteTypeNameList = new ArrayList<>();
		// Map<考评点类型编号, name>
		Map<Long, String> evaluteTypeMap = new HashMap<>();
		// 获取考评点类型
		List<CcEvaluteType> ccEvaluteTypes = CcEvaluteType.dao.findByTeacherCourseId(teacherCourseId);
		for(CcEvaluteType temp : ccEvaluteTypes) {
			Integer type = temp.getInt("type");
			Long ccEvaluteTypeId = temp.getLong("id");
			String name = DictUtils.findLabelByTypeAndKey("evaluteType", type);
			evaluteTypeNameList.add(name);
			evaluteTypeMap.put(ccEvaluteTypeId, name);
		}
		
		// 获取指标点
		// Map<指标点编号，指标点名称>
		Map<Long, String> ccIndicationMap = new HashMap<>();
		List<Long> ccIndicationSortList = new ArrayList<>();
		List<CcIndicationCourse> ccIndicationCourses = CcIndicationCourse.dao.findDetailByCourseId(courseId);
		for(CcIndicationCourse temp : ccIndicationCourses) {
			Long indicationId = temp.getLong("indication_id");
			String content = "指标点" + temp.getInt("graduateIndexNum") + "-" + temp.getInt("index_num");
			ccIndicationMap.put(indicationId, content);
			
			ccIndicationSortList.add(indicationId);
		}
		
		// 获取考评点
		// Map<考评点类型编号, List<CcEvalute>>
		Map<Long, List<CcEvalute>> ccEvaluteTypeAndEvaluteMap = new HashMap<>();
		// Map<考评点编号， List<indicationName>>
		Map<Long, List<String>> ccIndicationAndEvaluteMap = new HashMap<>();
		// Map<考评点类型编号-指标点编号， List<CcEvaluteName>>
		Map<String, List<String>> ccTypeAndIndicationMap = new HashMap<>();
//		List<CcEvalute> ccEvalutes = CcEvalute.dao.findFilteredByColumn("teacher_course_id", teacherCourseId);
		// 需要先排序一下
		List<CcEvalute> ccEvalutes = CcEvalute.dao.findByTeacherCourse(teacherCourseId);
		for(CcEvalute temp : ccEvalutes) {
			Long evaluteTypeId = temp.getLong("evalute_type_id");
			String content = temp.getStr("content");
			
			// 考评点类型关系
			List<CcEvalute> ccEvaluteTypeTemp = ccEvaluteTypeAndEvaluteMap.get(evaluteTypeId);
			if(ccEvaluteTypeTemp == null || ccEvaluteTypeTemp.isEmpty()) {
				ccEvaluteTypeTemp = new ArrayList<>();
			}
			ccEvaluteTypeTemp.add(temp);
			ccEvaluteTypeAndEvaluteMap.put(evaluteTypeId, ccEvaluteTypeTemp);
			
			// 指标点关系
			Long indicationId = temp.getLong("indication_id");
			
			List<String> ccEvaluteIndicationTemp = ccIndicationAndEvaluteMap.get(evaluteTypeId);
			if(ccEvaluteIndicationTemp == null || ccEvaluteIndicationTemp.isEmpty()) {
				ccEvaluteIndicationTemp = new ArrayList<>();
			}
			String indicationName = ccIndicationMap.get(indicationId);
			ccEvaluteIndicationTemp.remove(indicationName);
			ccEvaluteIndicationTemp.add(indicationName);
			ccIndicationAndEvaluteMap.put(evaluteTypeId, ccEvaluteIndicationTemp);
			
			// 考评点类型And指标点关系
			String key = evaluteTypeId + "-" + indicationId;
			List<String> ccEvaluteTypeAndIndicationTemp = ccTypeAndIndicationMap.get(key);
			if(ccEvaluteTypeAndIndicationTemp == null || ccEvaluteTypeAndIndicationTemp.isEmpty()) {
				ccEvaluteTypeAndIndicationTemp = new ArrayList<>();
			}
			ccEvaluteTypeAndIndicationTemp.add(content);
			ccTypeAndIndicationMap.put(key, ccEvaluteTypeAndIndicationTemp);
		}
		
		// ----------------------------------------- 处理head -----------------------------------
		RowDefinition rowDefinition = new RowDefinition(ccEvalutes.size() + 3);

        RowDefinition.ColumnDefinition no = RowDefinition.ColumnDefinition.createCommonColumn(0, "序号");
        RowDefinition.ColumnDefinition studentno = RowDefinition.ColumnDefinition.createCommonColumn(1, "学号");
        RowDefinition.ColumnDefinition name = RowDefinition.ColumnDefinition.createCommonColumn(2, "姓名");
        RowDefinition.ColumnDefinition clazzname = RowDefinition.ColumnDefinition.createCommonColumn(3, "教学班名称");

        RowDefinition.ValidateDefinition header3validate = new RowDefinition.ValidateDefinition(evaluteTypeNameList.toArray(new String[evaluteTypeNameList.size()]));
        
        rowDefinition.addColumn(no);
        rowDefinition.addColumn(studentno);
        rowDefinition.addColumn(name);
        rowDefinition.addColumn(clazzname);
		
		Integer firstNaturalColumnIndex = startNaturalColumnIndex;
		Integer secondNaturalColumnIndex = startNaturalColumnIndex;
		Integer thirdNaturalColumnIndex = startNaturalColumnIndex;
		List<Long> evaluteTypeAdded = new ArrayList<>();
		// 遍历考评点类型
		for(CcEvaluteType temp : ccEvaluteTypes) {
			Integer type = temp.getInt("type");
			Long ccEvaluteTypeId = temp.getLong("id");
			
			// 此考评点类型下的所有考评点
			Integer number = 0;
			List<CcEvalute> ccEvaluteTypeTemp = ccEvaluteTypeAndEvaluteMap.get(ccEvaluteTypeId);
			String evaluteTypeName = evaluteTypeMap.get(ccEvaluteTypeId);
			number = ccEvaluteTypeTemp.size();
			
			RowDefinition.ColumnDefinition evaluteType = RowDefinition.ColumnDefinition
	                .createGroupColumn(firstNaturalColumnIndex - 1, firstNaturalColumnIndex - 2 + number, evaluteTypeName)
	                .setIndexs(rowDefinition.getIndexs())
	                .setValidateDefinition(header3validate);
			rowDefinition.addColumn(evaluteType);
			firstNaturalColumnIndex = firstNaturalColumnIndex + number;
			
			// 指标点也是按照次序增加，循环这个是为了排序
			for(Long thisIndicationId : ccIndicationSortList) {
				// 上一个指标点
				// 遍历考评点类型-指标点
				List<String> ccEvalutesTemp = ccTypeAndIndicationMap.get(ccEvaluteTypeId+"-"+thisIndicationId);
				if(ccEvalutesTemp == null || ccEvalutesTemp.isEmpty()) {
					continue;
				}
//				for(Map.Entry<String, List<String>> entry : ccTypeAndIndicationMap.entrySet()) {
//					String key = entry.getKey();
//					String keys [] = key.split("-");
//					Long evaluteTypeId = Long.valueOf(keys[0]);
//					Long indicationId = Long.valueOf(keys[1]);
//					
//					// 如果不是本次考评点类型的，节略过
//					if(!ccEvaluteTypeId.equals(evaluteTypeId)) {
//						continue;
//					} 
//					// 如果不是本次指标点，节略过
//					if(!indicationId.equals(thisIndicationId)) {
//						continue;
//					}
					
					// 当前考评点类型和指标点下，所有的考评点
//					List<String> ccEvalutesTemp = entry.getValue();
					
//					for(int indicationIndex = 0; secondNaturalColumnIndex != firstNaturalColumnIndex; indicationIndex++) {
						// sencondMap2 赋值	
						// 数量是当前考评点类型和指标点下的个数
						number = ccEvalutesTemp.size();
						List<String> evaluteTypeIdAndIndicationNameList = ccIndicationAndEvaluteMap.get(ccEvaluteTypeId);
						RowDefinition.ValidateDefinition subGrade2Validate = new RowDefinition.ValidateDefinition(evaluteTypeIdAndIndicationNameList.toArray(new String[evaluteTypeIdAndIndicationNameList.size()]));
						
						RowDefinition.ColumnDefinition cloumn = RowDefinition.ColumnDefinition
							.createGroupColumn(secondNaturalColumnIndex - 1, secondNaturalColumnIndex - 2 + number, ccIndicationMap.get(thisIndicationId))
							.setIndexs(rowDefinition.getIndexs())
							.setValidateDefinition(subGrade2Validate)
							;
						((GroupColumnDefinition) evaluteType).addColumn(cloumn);
						
						secondNaturalColumnIndex = secondNaturalColumnIndex + number;
						for(int evaluteIndex = 0; secondNaturalColumnIndex != thirdNaturalColumnIndex; evaluteIndex++) {
							// sencondMap3 赋值	
//							Map<String, Object> thirdMap3 = new HashMap<>();
//							thirdMap3.put("value", ccEvalutesTemp.toArray(new String[ccEvalutesTemp.size()]));
//							thirdMap3.put("number", 1);
//							sencondMap3.put(thirdNaturalColumnIndex, thirdMap3);
							
							RowDefinition.ValidateDefinition subHeader2Validate = new RowDefinition.ValidateDefinition(ccEvalutesTemp.toArray(new String[ccEvalutesTemp.size()]));
							
							RowDefinition.ColumnDefinition evalute = RowDefinition.ColumnDefinition
								.createCommonColumn(thirdNaturalColumnIndex - 1, ccEvalutesTemp.get(evaluteIndex))
								.setValidateDefinition(subHeader2Validate)
								;
							((GroupColumnDefinition) cloumn).addColumn(evalute);
							thirdNaturalColumnIndex++;
						}
//					}
//				}
			}
		}
		
		return rowDefinition;
	}
	

	/**
	 * 获得考评点分析法的层次结构
	 * @param ccTeacherCourse
	 * 			教师开课信息
	 * @author SY 
	 * @version 创建时间：2017年10月10日 上午11:15:38
	 * return 
	 * 	Map<第几行，Map<第几列,Map<当前成绩组成细啊有几个考评点/考评点名称数组, 对应的数值>>> 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getEvaluateMap(CcTeacherCourse ccTeacherCourse) {
		
		Map<String, Object> returnMap = new HashMap<>();
		Map<String, Object> returnHeadMap = new HashMap<>();
		
		// 返回的额外Id参数Map
		Map<String, Object> reutnrHeadIdMap = new HashMap<>();
		// 《类型名称，Map<指标点名字，Map<考评点名称，考评点编号>>》
		Map<String, Map<String, Map<String, Long>>> returnHeadEvaluteIdMap = new HashMap<>();
		// 《类型名称，Map<指标点名字，指标点编号>》
		Map<String, Map<String, Long>> returnHeadIndicationIdMap = new HashMap<>();
		reutnrHeadIdMap.put("returnHeadEvaluteIdMap", returnHeadEvaluteIdMap);
		reutnrHeadIdMap.put("returnHeadIndicationIdMap", returnHeadIndicationIdMap);
		
//		Map<String, Long> headOne = new HashMap<>();
//		Map<String, Long> headTow = new HashMap<>();
//		Map<String, Long> headThree = new HashMap<>();
//		returnHeadIdMap.put(1, headOne);
//		returnHeadIdMap.put(2, headTow);
//		returnHeadIdMap.put(3, headThree);
		
		Long teacherCourseId = ccTeacherCourse.getLong("id");
		Long courseId = ccTeacherCourse.getLong("course_id");
		// TODO 准备放入多个考评点类型 <指标点名称，List《考评点名称》>
//		Map<String, List<String>> qimo3 = new HashMap<>();
		
		List<String> evaluteTypeNameList = new ArrayList<>();
		// Map<考评点类型编号, name>
		Map<Long, String> evaluteTypeMap = new HashMap<>();
//		// Map<name, 考评点类型编号>
//		Map<String, Long> evaluteTypeNameMap = new HashMap<>();
		// 获取考评点类型
		List<CcEvaluteType> ccEvaluteTypes = CcEvaluteType.dao.findByTeacherCourseId(teacherCourseId);
		for(CcEvaluteType temp : ccEvaluteTypes) {
			Integer type = temp.getInt("type");
			Long ccEvaluteTypeId = temp.getLong("id");
			String name = DictUtils.findLabelByTypeAndKey("evaluteType", type);
			evaluteTypeNameList.add(name);
			evaluteTypeMap.put(ccEvaluteTypeId, name);
			
			Map<String, List<String>> evaluteTypeReturnMap = new HashMap<>();
			returnHeadMap.put(name, evaluteTypeReturnMap);
			
			// id 的 树
			Map<String, Map<String, Long>> typeMap = new HashMap<>();
			returnHeadEvaluteIdMap.put(name, typeMap);
			
			// id 的 树
			Map<String, Long> indicationMap = new HashMap<>();
			returnHeadIndicationIdMap.put(name, indicationMap);
		}
		
		// 获取指标点
		// Map<考评点类型编号， 指标点中文列表>
//		Map<Long, List<String>> ccIndicationMap = new HashMap<>();
		// Map<指标点编号，指标点名称>
		Map<Long, String> ccIndicationMap = new HashMap<>();
		List<CcIndicationCourse> ccIndicationCourses = CcIndicationCourse.dao.findDetailByCourseId(courseId);
		for(CcIndicationCourse temp : ccIndicationCourses) {
			Long indicationId = temp.getLong("indication_id");
			String content = "指标点" + temp.getInt("graduateIndexNum") + "-" + temp.getInt("index_num");
			ccIndicationMap.put(indicationId, content);
		}
		
//		// 获取考评点
//		// Map<考评点类型编号, List<CcEvalute>>
//		Map<Long, List<CcEvalute>> ccEvaluteTypeAndEvaluteMap = new HashMap<>();
//		// Map<考评点编号， List<indicationName>>
//		Map<Long, List<String>> ccIndicationAndEvaluteMap = new HashMap<>();
//		// Map<考评点类型编号-指标点编号， List<CcEvaluteName>>
//		Map<String, List<String>> ccTypeAndIndicationMap = new HashMap<>();
		List<CcEvalute> ccEvalutes = CcEvalute.dao.findFilteredByColumn("teacher_course_id", teacherCourseId);
		for(CcEvalute temp : ccEvalutes) {
			Long evaluteTypeId = temp.getLong("evalute_type_id");
			Long evaluteId = temp.getLong("id");
			String content = temp.getStr("content");
			
			// 考评点类型关系
//			List<CcEvalute> ccEvaluteTypeTemp = ccEvaluteTypeAndEvaluteMap.get(evaluteTypeId);
//			if(ccEvaluteTypeTemp == null || ccEvaluteTypeTemp.isEmpty()) {
//				ccEvaluteTypeTemp = new ArrayList<>();
//			}
//			ccEvaluteTypeTemp.add(temp);
//			ccEvaluteTypeAndEvaluteMap.put(evaluteTypeId, ccEvaluteTypeTemp);
			
			// 指标点关系
			Long indicationId = temp.getLong("indication_id");
			// TODO 这个可能需要排个序
//			List<String> ccEvaluteIndicationTemp = ccIndicationAndEvaluteMap.get(evaluteTypeId);
//			if(ccEvaluteIndicationTemp == null || ccEvaluteIndicationTemp.isEmpty()) {
//				ccEvaluteIndicationTemp = new ArrayList<>();
//			}
			String indicationName = ccIndicationMap.get(indicationId);
//			ccEvaluteIndicationTemp.remove(indicationName);
//			ccEvaluteIndicationTemp.add(indicationName);
//			ccIndicationAndEvaluteMap.put(evaluteTypeId, ccEvaluteIndicationTemp);
			
			// 考评点类型And指标点关系
//			String key = evaluteTypeId + "-" + indicationId;
			String name = evaluteTypeMap.get(evaluteTypeId);
			Map<String, List<String>> evaluteTypeReturnMap = (Map<String, List<String>>) returnHeadMap.get(name);
			List<String> indicationReturnList = evaluteTypeReturnMap.get(indicationName);
			
			// 指标点map
			Map<String, Map<String, Long>> indicationMap = returnHeadEvaluteIdMap.get(name);
			// 考评点Map
			Map<String, Long> kpdHeadIdMap = indicationMap.get(indicationName);
			if(kpdHeadIdMap == null) {
				kpdHeadIdMap = new HashMap<>();
				indicationMap.put(indicationName, kpdHeadIdMap);
			}
			kpdHeadIdMap.put(content, evaluteId);
			
			// 指标点map
			Map<String, Long> indicationNameMap = returnHeadIndicationIdMap.get(name);
			// 考评点Map设置id
			indicationNameMap.put(indicationName, indicationId);
			
			if(indicationReturnList == null) {
				indicationReturnList = new ArrayList<>();
				evaluteTypeReturnMap.put(indicationName, indicationReturnList);
			}
			indicationReturnList.add(content);
//			
//			List<String> ccEvaluteTypeAndIndicationTemp = ccTypeAndIndicationMap.get(key);
//			if(ccEvaluteTypeAndIndicationTemp == null || ccEvaluteTypeAndIndicationTemp.isEmpty()) {
//				ccEvaluteTypeAndIndicationTemp = new ArrayList<>();
//			}
//			ccEvaluteTypeAndIndicationTemp.add(content);
//			ccTypeAndIndicationMap.put(key, ccEvaluteTypeAndIndicationTemp);
		}
		
//		Integer firstNaturalColumnIndex = startNaturalColumnIndex;
//		Integer secondNaturalColumnIndex = startNaturalColumnIndex;
//		Integer thirdNaturalColumnIndex = startNaturalColumnIndex;
//		List<Long> evaluteTypeAdded = new ArrayList<>();
//		for(Map.Entry<String, List<String>> entry : ccTypeAndIndicationMap.entrySet()) {
//			String key = entry.getKey();
//			String keys [] = key.split("-");
//			Long evaluteTypeId = Long.valueOf(keys[0]);
//			Long indicationId = Long.valueOf(keys[1]);
//			// 当前考评点类型和指标点下，所有的考评点
//			List<String> ccEvalutesTemp = entry.getValue();
//			Integer number = 0;
//			// 此考评点类型下的所有考评点
//			List<CcEvalute> ccEvaluteTypeTemp = ccEvaluteTypeAndEvaluteMap.get(evaluteTypeId);
//			number = ccEvaluteTypeTemp.size();
////			Map<String, Object> thirdMap1 = new HashMap<>();
////			thirdMap1.put("value", evaluteTypeNameList.toArray(new String[evaluteTypeNameList.size()]));
////			thirdMap1.put("number", number);
////			sencondMap1.put(firstNaturalColumnIndex, thirdMap1);
//			// 因为只有第二层，所以直接加上去就好
//			firstNaturalColumnIndex = firstNaturalColumnIndex + number;
//			evaluteTypeAdded.add(evaluteTypeId);
////			if(!evaluteTypeAdded.contains(evaluteTypeId)) {
////				// 此考评点类型下的所有考评点
////				List<CcEvalute> ccEvaluteTypeTemp = ccEvaluteTypeAndEvaluteMap.get(evaluteTypeId);
////				number = ccEvaluteTypeTemp.size();
////				Map<String, Object> thirdMap1 = new HashMap<>();
////				thirdMap1.put("value", evaluteTypeNameList.toArray(new String[evaluteTypeNameList.size()]));
////				thirdMap1.put("number", number);
////				sencondMap1.put(firstNaturalColumnIndex, thirdMap1);
////				// 因为只有第二层，所以直接加上去就好
////				firstNaturalColumnIndex = firstNaturalColumnIndex + number;
////				evaluteTypeAdded.add(evaluteTypeId);
////			}
//			
//			
//			for(;secondNaturalColumnIndex != firstNaturalColumnIndex;) {
//				// sencondMap2 赋值	
//				// 数量是当前考评点类型和指标点下的个数
//				number = ccEvalutesTemp.size();
//				List<String> evaluteTypeIdAndIndicationNameList = ccIndicationAndEvaluteMap.get(evaluteTypeId);
//				Map<String, Object> thirdMap2 = new HashMap<>();
//				thirdMap2.put("value", evaluteTypeIdAndIndicationNameList.toArray(new String[evaluteTypeIdAndIndicationNameList.size()]));
//				thirdMap2.put("number", number);
//				sencondMap2.put(secondNaturalColumnIndex, thirdMap2);
//				secondNaturalColumnIndex = secondNaturalColumnIndex + number;
//				for(;secondNaturalColumnIndex != thirdNaturalColumnIndex;) {
//					// sencondMap3 赋值	
//					Map<String, Object> thirdMap3 = new HashMap<>();
//					thirdMap3.put("value", ccEvalutesTemp.toArray(new String[ccEvalutesTemp.size()]));
//					thirdMap3.put("number", 1);
//					sencondMap3.put(thirdNaturalColumnIndex, thirdMap3);
//					thirdNaturalColumnIndex++;
//				}
//			}
//		}
//		return returnHeadMap;
		returnMap.put("head", returnHeadMap);
		returnMap.put("headId", reutnrHeadIdMap);
		return returnMap;
	}
	
	/**
	 * 获得考核分析法的层次结构
	 * @param ccTeacherCourse
	 * 			教师开课信息
	 * @param courseGradeComposeId
	 * 			开课成绩组成编号（为空，则不限制）
	 * @author SY 
	 * @version 创建时间：2017年10月10日 上午11:15:38
	 * return 
	 * 	Map<成绩组成名字, List<指标点名字>> 
	 */
	public Map<String, Object> getScoreMap(CcTeacherCourse ccTeacherCourse, Long courseGradeComposeId,Long batchId) {

		Map<String, Object> returnMap = new HashMap<>();
		Map<String, Object> returnHeadMap = new HashMap<>();
		
		Map<String, Object> returnHeadIdMap = new HashMap<>();
		// 《成绩组成名称，Map<课程目标名字，课程目标编号>》
		Map<String, Map<String, Long>> returnHeadGradecomposeIndicationIdMap = new HashMap<>();
		// 《成绩组成名称，Map<课程目标名字，courseGradecomposeIndicationId>
		Map<String, Map<String, Long>> courseGradecomposeIndicationIdMap = new HashMap<>();
		// 《成绩组成名称，Map<课程目标名字，课程目标满分数值>
		Map<String, Map<String, BigDecimal>> courseGradecomposeIndicationFullScoreMap = new HashMap<>();
		returnHeadIdMap.put("returnHeadGradecomposeIndicationIdMap", returnHeadGradecomposeIndicationIdMap);
		returnHeadIdMap.put("courseGradecomposeIndicationIdMap", courseGradecomposeIndicationIdMap);
		returnHeadIdMap.put("courseGradecomposeIndicationFullScoreMap", courseGradecomposeIndicationFullScoreMap);
		
		Long teacherCourseId = ccTeacherCourse.getLong("id");
		
		// Map<开课课程成绩组成元素编号, 成绩组成名称>
		Map<Long, String> courseGradecomposeMap = new HashMap<>();
		List<Long> courseGradecomposeIds = new ArrayList<>();
		// Map<开课课程成绩组成元素编号, 课程目标名称>
		Map<Long, List<String>> ccCourseGradecomposeIndicationMap = new HashMap<>();
				
		// 获取教师开课的成绩组成
//		Page<CcCourseGradecompose> pageCcCourseGradecompose = CcCourseGradecompose.dao.page(pageable, teacherCourseId);
//		List<CcCourseGradecompose> courseGradecomposeList = pageCcCourseGradecompose.getList(); 
		List<CcCourseGradecompose> courseGradecomposeList = CcCourseGradecompose.dao.findByTeacherCourseIdOrderBySort(teacherCourseId);

		for(int i = 0; i < courseGradecomposeList.size(); i++) {
			CcCourseGradecompose temp = courseGradecomposeList.get(i);
			String name = temp.getStr("name");
			Long courseGradecomposeId = temp.getLong("id");
			// 当存在且不是指定开课成绩组成元素的时候，跳过
			if(courseGradeComposeId != null && !courseGradeComposeId.equals(courseGradecomposeId)) {
				continue;
			}
			courseGradecomposeIds.add(courseGradecomposeId);
			courseGradecomposeMap.put(courseGradecomposeId, name);
			ccCourseGradecomposeIndicationMap.put(courseGradecomposeId, new ArrayList<String>());
			
			Map<String, Long> ccCourseGradecomposeMap = new HashMap<>();
			returnHeadGradecomposeIndicationIdMap.put(name, ccCourseGradecomposeMap);
			Map<String, Long> ccCourseGradecomposeIndicationNameMap = new HashMap<>();
			courseGradecomposeIndicationIdMap.put(name, ccCourseGradecomposeIndicationNameMap);
			Map<String, BigDecimal> ccCourseGradecomposeIndicationNameScoreMap = new HashMap<>();
			courseGradecomposeIndicationFullScoreMap.put(name, ccCourseGradecomposeIndicationNameScoreMap);
		}

		if (batchId != null){
			ArrayList<Long> courseGradeComposeIds = new ArrayList<>();
			courseGradeComposeIds.add(courseGradeComposeId);
			//批次直接录入形式，每个批次包含不同的课程目标
			List<CcCourseGradecomposeBatchIndication> courseBatchGradecomposeIds = CcCourseGradecomposeBatchIndication.dao.findByCourseBatchGradecomposeIds0(courseGradeComposeIds, batchId,null);
			for (CcCourseGradecomposeBatchIndication temp : courseBatchGradecomposeIds){
				Long courseGradecomposeIndicationId = temp.getLong("courseGradecomposeIndicationId");
				Long courseGradecomposeId = temp.getLong("course_gradecompose_id");
				String content = temp.getInt("sort")+":"+temp.getStr("content");

				//String sort = temp.getInt("sort").toString();
				// 当存在且不是指定开课成绩组成元素的时候，跳过
				if(courseGradeComposeId != null && !courseGradeComposeId.equals(courseGradecomposeId)) {
					continue;
				}
				List<String> list = ccCourseGradecomposeIndicationMap.get(courseGradecomposeId);
				list.add(content);
				Long indicationId = temp.getLong("indicationId");
				String coureGradecomposeName = courseGradecomposeMap.get(courseGradecomposeId);
				Map<String, Long> indicationNameAndId = returnHeadGradecomposeIndicationIdMap.get(coureGradecomposeName);
				indicationNameAndId.put(content, indicationId);

				Map<String, Long> indicationNameAndCoursegradecomposeId = courseGradecomposeIndicationIdMap.get(coureGradecomposeName);
				indicationNameAndCoursegradecomposeId.put(content, courseGradecomposeIndicationId);

				BigDecimal maxScore = temp.getBigDecimal("maxScore");
				Map<String, BigDecimal> indicationNameAndCoursegradecomposeFullScore = courseGradecomposeIndicationFullScoreMap.get(coureGradecomposeName);
				indicationNameAndCoursegradecomposeFullScore.put(content, maxScore);
			}

		}else{

			List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndicationList = CcCourseGradecomposeIndication.dao.findDetailByTeacherCourseId(teacherCourseId);
			for(CcCourseGradecomposeIndication temp : ccCourseGradecomposeIndicationList) {
				Long courseGradecomposeIndicationId = temp.getLong("id");
				Long courseGradecomposeId = temp.getLong("course_gradecompose_id");
				// 当存在且不是指定开课成绩组成元素的时候，跳过
				if(courseGradeComposeId != null && !courseGradeComposeId.equals(courseGradecomposeId)) {
					continue;
				}
				// 课程目标名称 2020/06/30 TODO GJM 课程目标名称改为课程目标序号+名称
				String content = temp.getInt("sort")+":"+temp.getStr("content");
				List<String> list = ccCourseGradecomposeIndicationMap.get(courseGradecomposeId);
				list.add(content);

				Long indicationId = temp.getLong("indication_id");
				String coureGradecomposeName = courseGradecomposeMap.get(courseGradecomposeId);
				Map<String, Long> indicationNameAndId = returnHeadGradecomposeIndicationIdMap.get(coureGradecomposeName);
				indicationNameAndId.put(content, indicationId);

				Map<String, Long> indicationNameAndCoursegradecomposeId = courseGradecomposeIndicationIdMap.get(coureGradecomposeName);
				indicationNameAndCoursegradecomposeId.put(content, courseGradecomposeIndicationId);

				BigDecimal maxScore = temp.getBigDecimal("max_score");
				Map<String, BigDecimal> indicationNameAndCoursegradecomposeFullScore = courseGradecomposeIndicationFullScoreMap.get(coureGradecomposeName);
				indicationNameAndCoursegradecomposeFullScore.put(content, maxScore);
			}
		}
		for(Long id : courseGradecomposeIds) {
			// 当存在且不是指定开课成绩组成元素的时候，跳过
			if(courseGradeComposeId != null && !courseGradeComposeId.equals(id)) {
				continue;
			}
			String key = courseGradecomposeMap.get(id);
			List<String> value = ccCourseGradecomposeIndicationMap.get(id);
			List<String> coValue = new ArrayList<>();
			//TODO GJM 不能使用自增的，要真实数据库里的目标序号
			for(Integer i = 1; i <= value.size(); i++) {
				//coValue.add("CO" + i + ":" + value.get(i - 1));
				coValue.add("CO" + value.get(i - 1));
			}
			returnHeadMap.put(key, coValue);
		}
		
		// 获取这些成绩组成的课程目标
		returnMap.put("head", returnHeadMap);
		returnMap.put("headId", returnHeadIdMap);
		return returnMap;
	}
	/**
	 * 获得考核分析法的层次结构
	 * @param ccTeacherCourse
	 * 			教师开课信息
	 * @param courseGradeComposeId
	 * 			开课成绩组成编号（为空，则不限制）
	 * @author SY
	 * @version 创建时间：2017年10月10日 上午11:15:38
	 * return
	 * 	Map<成绩组成名字, List<指标点名字>>
	 */
	public Map<String, Object> getScoreMap2(CcTeacherCourse ccTeacherCourse, Long courseGradeComposeId,Long batchId) {
		ArrayList<Long> courseGradeComposeIdList = new ArrayList<>();
		courseGradeComposeIdList.add(courseGradeComposeId);
		Map<String, Object> returnMap = new HashMap<>();
		Map<String, Object> returnHeadMap = new HashMap<>();

		Map<String, Object> returnHeadIdMap = new HashMap<>();
		// 《成绩组成名称，Map<课程目标名字，课程目标编号>》
		Map<String, Map<String, Long>> returnHeadGradecomposeIndicationIdMap = new HashMap<>();
		// 《成绩组成名称，Map<课程目标名字，courseGradecomposeIndicationId>
		Map<String, Map<String, Long>> courseGradecomposeIndicationIdMap = new HashMap<>();
		// 《成绩组成名称，Map<课程目标名字，课程目标满分数值>
		Map<String, Map<String, BigDecimal>> courseGradecomposeIndicationFullScoreMap = new HashMap<>();
		returnHeadIdMap.put("returnHeadGradecomposeIndicationIdMap", returnHeadGradecomposeIndicationIdMap);
		returnHeadIdMap.put("courseGradecomposeIndicationIdMap", courseGradecomposeIndicationIdMap);
		returnHeadIdMap.put("courseGradecomposeIndicationFullScoreMap", courseGradecomposeIndicationFullScoreMap);

		Long teacherCourseId = ccTeacherCourse.getLong("id");

		// Map<开课课程成绩组成元素编号, 成绩组成名称>
		Map<Long, String> courseGradecomposeMap = new HashMap<>();
		List<Long> courseGradecomposeIds = new ArrayList<>();
		// Map<开课课程成绩组成元素编号, 课程目标名称>
		Map<Long, List<String>> ccCourseGradecomposeIndicationMap = new HashMap<>();

		// 获取教师开课的成绩组成
//		Page<CcCourseGradecompose> pageCcCourseGradecompose = CcCourseGradecompose.dao.page(pageable, teacherCourseId);
//		List<CcCourseGradecompose> courseGradecomposeList = pageCcCourseGradecompose.getList();
		List<CcCourseGradecompose> courseGradecomposeList = CcCourseGradecompose.dao.findByTeacherCourseIdAndCourseGradeComposeIdsOrderBySort(teacherCourseId,courseGradeComposeIdList,batchId);

		for(int i = 0; i < courseGradecomposeList.size(); i++) {
			CcCourseGradecompose temp = courseGradecomposeList.get(i);
			String name = temp.getStr("name");
			Long courseGradecomposeId = temp.getLong("id");
			String batchName = temp.getStr("batchName");
			if (batchName !=null){
				name=name+"-"+batchName;
			}
			// 当存在且不是指定开课成绩组成元素的时候，跳过
			if(courseGradeComposeId != null && !courseGradeComposeId.equals(courseGradecomposeId)) {
				continue;
			}
			courseGradecomposeIds.add(courseGradecomposeId);
			courseGradecomposeMap.put(courseGradecomposeId, name);
			ccCourseGradecomposeIndicationMap.put(courseGradecomposeId, new ArrayList<String>());

			Map<String, Long> ccCourseGradecomposeMap = new HashMap<>();
			returnHeadGradecomposeIndicationIdMap.put(name, ccCourseGradecomposeMap);
			Map<String, Long> ccCourseGradecomposeIndicationNameMap = new HashMap<>();
			courseGradecomposeIndicationIdMap.put(name, ccCourseGradecomposeIndicationNameMap);
			Map<String, BigDecimal> ccCourseGradecomposeIndicationNameScoreMap = new HashMap<>();
			courseGradecomposeIndicationFullScoreMap.put(name, ccCourseGradecomposeIndicationNameScoreMap);
		}

		if (batchId != null){
			ArrayList<Long> courseGradeComposeIds = new ArrayList<>();
			courseGradeComposeIds.add(courseGradeComposeId);
			//批次直接录入形式，每个批次包含不同的课程目标
			List<CcCourseGradecomposeBatchIndication> courseBatchGradecomposeIds = CcCourseGradecomposeBatchIndication.dao.findByCourseBatchGradecomposeIds0(courseGradeComposeIds, batchId,null);
			for (CcCourseGradecomposeBatchIndication temp : courseBatchGradecomposeIds){
				Long courseGradecomposeIndicationId = temp.getLong("courseGradecomposeIndicationId");
				Long courseGradecomposeId = temp.getLong("course_gradecompose_id");
				String content = temp.getInt("sort")+":"+temp.getStr("content");

				//String sort = temp.getInt("sort").toString();
				// 当存在且不是指定开课成绩组成元素的时候，跳过
				if(courseGradeComposeId != null && !courseGradeComposeId.equals(courseGradecomposeId)) {
					continue;
				}
				List<String> list = ccCourseGradecomposeIndicationMap.get(courseGradecomposeId);
				list.add(content);
				Long indicationId = temp.getLong("indicationId");
				String coureGradecomposeName = courseGradecomposeMap.get(courseGradecomposeId);
				Map<String, Long> indicationNameAndId = returnHeadGradecomposeIndicationIdMap.get(coureGradecomposeName);
				indicationNameAndId.put(content, indicationId);

				Map<String, Long> indicationNameAndCoursegradecomposeId = courseGradecomposeIndicationIdMap.get(coureGradecomposeName);
				indicationNameAndCoursegradecomposeId.put(content, courseGradecomposeIndicationId);

				BigDecimal maxScore = temp.getBigDecimal("maxScore");
				Map<String, BigDecimal> indicationNameAndCoursegradecomposeFullScore = courseGradecomposeIndicationFullScoreMap.get(coureGradecomposeName);
				indicationNameAndCoursegradecomposeFullScore.put(content, maxScore);
			}

		}else{

			List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndicationList = CcCourseGradecomposeIndication.dao.findDetailByTeacherCourseId(teacherCourseId);
			for(CcCourseGradecomposeIndication temp : ccCourseGradecomposeIndicationList) {
				Long courseGradecomposeIndicationId = temp.getLong("id");
				Long courseGradecomposeId = temp.getLong("course_gradecompose_id");
				// 当存在且不是指定开课成绩组成元素的时候，跳过
				if(courseGradeComposeId != null && !courseGradeComposeId.equals(courseGradecomposeId)) {
					continue;
				}
				// 课程目标名称 2020/06/30 TODO GJM 课程目标名称改为课程目标序号+名称
				String content = temp.getInt("sort")+":"+temp.getStr("content");
				List<String> list = ccCourseGradecomposeIndicationMap.get(courseGradecomposeId);
				list.add(content);

				Long indicationId = temp.getLong("indication_id");
				String coureGradecomposeName = courseGradecomposeMap.get(courseGradecomposeId);
				Map<String, Long> indicationNameAndId = returnHeadGradecomposeIndicationIdMap.get(coureGradecomposeName);
				indicationNameAndId.put(content, indicationId);

				Map<String, Long> indicationNameAndCoursegradecomposeId = courseGradecomposeIndicationIdMap.get(coureGradecomposeName);
				indicationNameAndCoursegradecomposeId.put(content, courseGradecomposeIndicationId);

				BigDecimal maxScore = temp.getBigDecimal("max_score");
				Map<String, BigDecimal> indicationNameAndCoursegradecomposeFullScore = courseGradecomposeIndicationFullScoreMap.get(coureGradecomposeName);
				indicationNameAndCoursegradecomposeFullScore.put(content, maxScore);
			}
		}
		for(Long id : courseGradecomposeIds) {
			// 当存在且不是指定开课成绩组成元素的时候，跳过
			if(courseGradeComposeId != null && !courseGradeComposeId.equals(id)) {
				continue;
			}
			String key = courseGradecomposeMap.get(id);
			List<String> value = ccCourseGradecomposeIndicationMap.get(id);
			List<String> coValue = new ArrayList<>();
			//TODO GJM 不能使用自增的，要真实数据库里的目标序号
			for(Integer i = 1; i <= value.size(); i++) {
				//coValue.add("CO" + i + ":" + value.get(i - 1));
				coValue.add("CO" + value.get(i - 1));
			}
			returnHeadMap.put(key, coValue);
		}

		// 获取这些成绩组成的课程目标
		returnMap.put("head", returnHeadMap);
		returnMap.put("headId", returnHeadIdMap);
		return returnMap;
	}
}
