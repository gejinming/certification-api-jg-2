package com.gnet.certification;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

import com.gnet.model.admin.*;
import com.gnet.plugin.poi.*;
import com.gnet.utils.DictUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.FileInfo;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.plugin.poi.RowDefinition.ColumnDefinition;
import com.gnet.plugin.poi.RowDefinition.GroupColumnDefinition;
import com.gnet.service.CcEvaluteLevelService;
import com.gnet.service.CcStudentService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;
import com.jfinal.log.Logger;


/**
 * 考评点分析法教学班学生批量成绩录入
 * 
 * @author SY
 * @Date 2017年10月6日
 */
@Transactional(readOnly = false)
@Service("EM00741")
public class EM00741 extends BaseApi implements IApi {
	
	private static final Logger logger = Logger.getLogger(EM00741.class);
	
	@Autowired
	private CcStudentService ccStudentService;
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Object fileInfoObject = param.get("fileInfo");
		FileInfo fileInfo = (FileInfo) fileInfoObject;
		Map<String, Object> result = Maps.newHashMap();
		// 课程成绩组成编号列表
		List<Long> courseGradeComposeIds = paramsJSONArrayFilter(param.get("courseGradeComposeIds"), Long.class);
		Long eduClassId = paramsLongFilter(param.get("eduClassId"));
		Long batchId = paramsLongFilter(param.get("batchId"));
		// 教学班编号为空过滤
		if (eduClassId == null) {
			return renderFAIL("0500", response, header);
		}
		// 课程成绩组成为空过滤
		if (courseGradeComposeIds == null || courseGradeComposeIds.isEmpty()) {
			return renderFAIL("0490", response, header);
		}
		// 获得教师开课课程
		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findCourseByClassId(eduClassId);
		if (ccTeacherCourse == null) {
			return renderFAIL("0501", response, header);
		}
		
		try {
			Map<String, Object> header2 = new HashMap<>();
			String url = fileInfo.getTempUrl();
			File file = new File(url);
	        InputStream inputStream = new FileInputStream(file);
			ExcelParser excelParser = new Header2ExcelParser();
			ExcelDefinition excelDefinition = excelParser.parse(inputStream, header2, 1);
			//header中的数据直接返回
			RowDefinition prHeaderOne = excelDefinition.getHeader();
			GroupColumnDefinition prHeaderTow = prHeaderOne.getGroupColumnDefinition();
			//解析hear中的数据验证是否合理
			List<ColumnDefinition> preHeader = prHeaderTow.getColumns();
			String index = DictUtils.findLabelByTypeAndKey("subjectScoreImport", 1);
			String no =  DictUtils.findLabelByTypeAndKey("subjectScoreImport", 2);
			String name =  DictUtils.findLabelByTypeAndKey("subjectScoreImport", 3);
			String className =  DictUtils.findLabelByTypeAndKey("subjectScoreImport", 4);
			ColumnDefinition first = preHeader.get(0);
			ColumnDefinition second = preHeader.get(1);
			ColumnDefinition third  = preHeader.get(2);
			ColumnDefinition fourth  = preHeader.get(3);

			if(!index.equals(first.getName())){
				return renderFAIL("2103", response, header, String.format("excel中的第1列列名不是%s,请检查", index));
			}
			if(!no.equals(second.getName())){
				return renderFAIL("2103", response, header, String.format("excel中的第2列列名不是%s,请检查", no));
			}
			if(!name.equals(third.getName())){
				return renderFAIL("2103", response, header, String.format("excel中的第3列列名不是%s,请检查", name));
			}
			if(!className.equals(fourth.getName())){
				return renderFAIL("2103", response, header, String.format("excel中的第4列列名不是%s,请检查", className));
			}
			List<String> gradecomposeNameList = new ArrayList<>();
			//等级级别
			int hierarchyLevel=0;
			//获取成绩组成以及批次名称
			List<CcCourseGradecompose> ccCourseGradecomposes = CcCourseGradecompose.dao.findGradecomposeBatch(courseGradeComposeIds,batchId);
			//<名称，批次id>
			HashMap<String, Long> batchIdMap = new HashMap<>();
			//<名称，成绩组成id>
			HashMap<String, Long> courseGradecomposeIdMap = new HashMap<>();
			for(CcCourseGradecompose temp : ccCourseGradecomposes) {
				String gradecomposename;
				gradecomposename = temp.getStr("name");
				String batchName = temp.getStr("batchName");
				Long id = temp.getLong("id");
				//所有的成绩组成的等级都是同步的
				hierarchyLevel=temp.getInt("hierarchy_level");
				if (batchName !=null){
					gradecomposename=gradecomposename+"-"+batchName;
					Long batchId1 = temp.getLong("batchId");
					batchIdMap.put(gradecomposename,batchId1);
				}
				courseGradecomposeIdMap.put(gradecomposename,id);

				if (!gradecomposeNameList.contains(gradecomposename)){
					gradecomposeNameList.add(gradecomposename);
				}

			}
			CcTeacherCourse teacherCourse = CcTeacherCourse.dao.findByCourseGradeComposeId(courseGradeComposeIds.get(0));
			CcCourse ccCourse = CcCourse.dao.findCourseMajor(teacherCourse.getLong("course_id"));
			Long majorId = ccCourse.getLong("major_id");
			List<CcRankingLevel> levelList = CcRankingLevel.dao.findLevelList(majorId, hierarchyLevel);
			HashMap<String, Long> levelLists = new HashMap<>();
			for (CcRankingLevel temps: levelList){
				String levelName = temps.getStr("levelName");
				Long id = temps.getLong("id");
				levelLists.put(levelName,id);
			}
			if (levelList.size()==0){
				return renderFAIL("2586", response, header);
			}
			if(preHeader.size() < 5){
				return renderFAIL("2106", response, header);
			}else{
				for (int i=5;i<preHeader.size();i++){
					ColumnDefinition columnName  = preHeader.get(i);
					if (courseGradecomposeIdMap.get(columnName.getName())==null){
						return renderFAIL("403", response, header, "请检查模板的是否正确。包括：1.是否存在重复。2.是否选错班级。3.是否修改过结构。解决方案：重新下载一份。");
					}
				}
			}
	        
	        // 把excelDefinition数据传递要页面。
	        Map<String, Object> returnMapList = getReturnList(excelDefinition, courseGradecomposeIdMap, eduClassId,levelLists, batchIdMap);
//	        result.put("excelDefinition", excelDefinition);
	        Boolean isSuccess = (Boolean) returnMapList.get("isSuccess");
	        if(!isSuccess) {
	        	return renderFAIL("403", response, header, returnMapList.get("failMessage"));
	        }
	        result.put("isSuccess", isSuccess);
	        result.put("excelDefinition", returnMapList);
		} catch (Exception e) {
			return renderFAIL("403", response, header, "请检查模板的是否正确。包括：1.是否存在重复。2.是否选错班级。3.是否修改过结构。解决方案：重新下载一份。");
//			return renderFAIL("403", response, header, e+"。并请检查模板的数据结构是否和导入班级一样，且不重复。");
		}

		return renderSUC(result, response, header);
	}

	/**
	 * 根据数据，得到返回的列表
	 * @param excelDefinition
	 * 			excel获取后封装的对象
	 * 			head对应的考评点编号树
	 * @param eduClassId 
	 * 			教学班编号
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年10月16日 下午6:07:32 
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getReturnList(ExcelDefinition excelDefinition, Map<String, Long> coursegradecomposeIdMap, Long eduClassId,HashMap<String, Long> levelList,HashMap<String, Long> batchIdMap) {
		// Map<head/body, Object>
		Map<String, Object> returnMap = new HashMap<>();
		// 一个个学生信息 List<Map<第几行， Map<字段名，字段数据>>>
		List<Map<Integer, Map<String, Object>>> body = new ArrayList<>();
		returnMap.put("isSuccess", Boolean.TRUE);

		// 准备学生的数据 
		//Map<StudentNo, studentId>
		Map<String, Long> studentNoAndIdMap = new HashMap<>();
		List<CcStudent> ccStudents = CcStudent.dao.findByEduclassId(eduClassId);
		for(CcStudent temp : ccStudents) {
			String studentNo = temp.getStr("student_no");
			Long studentId = temp.getLong("id");
			studentNoAndIdMap.put(studentNo, studentId);
		}

		RowDefinition prHeaderOne = excelDefinition.getHeader();
		GroupColumnDefinition prHeaderTow = prHeaderOne.getGroupColumnDefinition();
		// 我这里是平时成绩，下面应该还有一层指标点。但是这里只有getColumns,没有getGroupColumnDefinition
		List<ColumnDefinition> preHeader = prHeaderTow.getColumns();

		List<RowDefinition> studentInfoList = excelDefinition.getBody();
		for (int i=1;i<studentInfoList.size();i++){
			//《序号，《字段，数据》》
			Map<Integer, Map<String, Object>> indexStudent = new HashMap<>();
			Map<String, Object> studentInfos = new HashMap<>();
			RowDefinition studentInfo = studentInfoList.get(i);
			Map<Integer, ColumnDefinition> indexColumn = studentInfo.getIndexs();
			//Iterator<Map.Entry<Integer, ColumnDefinition>> it = indexColumn.entrySet().iterator();
			//录入的成绩
			ArrayList<Object> courseGradecomposeList = new ArrayList<>();
			String errMess="";
			for (Map.Entry<Integer, ColumnDefinition> info : indexColumn.entrySet()){
				HashMap<Object, Object> courseGradecomposeMap = new HashMap<>();
				ColumnDefinition infoValue = info.getValue();
				String name = infoValue.getName();
				String value = infoValue.getValue()+"";
				int index = infoValue.getIndex();

				if(name.equals("学号")){
					Long studentId = studentNoAndIdMap.get(value);
					if (studentNoAndIdMap.get(value)==null){
						errMess="该教学班不存在此学号。";
					}
					studentInfos.put("studentId",studentId);
				}
				if (index>=4){

					//boolean contains = name.contains("-");
					Long courseGradecomposeId = coursegradecomposeIdMap.get(name);
					Long batchId = batchIdMap.get(name);
					courseGradecomposeMap.put(name,value);
					courseGradecomposeMap.put("courseGradecomposeId",courseGradecomposeId);
					courseGradecomposeMap.put("batchId",batchId);
					if (value.equals("")){
						errMess=errMess+name+"考评点成绩不能为空。";
					}else{
						///---courseGradecomposeIdOrbatchId
						Long levelId = levelList.get(value);
						if (levelList.get(value)==null){
							errMess=errMess+name+"考评点成绩不符合等级制设置。";
						}


						courseGradecomposeMap.put("levelId",levelId);
						courseGradecomposeList.add(courseGradecomposeMap);
					}

				}else{
					studentInfos.put(name,value);
				}




			}
			studentInfos.put("courseGradecomposeList",courseGradecomposeList);
			studentInfos.put("失败原因",errMess);
			if (errMess.equals("")){
				studentInfos.put("数据检查",true);
			}else{
				studentInfos.put("数据检查",false);
			}
			indexStudent.put(i,studentInfos);
			body.add(indexStudent);
		}


		returnMap.put("body", body);
		returnMap.put("header", prHeaderOne);
		return returnMap;
	}

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
	
}