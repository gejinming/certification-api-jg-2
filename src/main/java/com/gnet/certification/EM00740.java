package com.gnet.certification;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.FileInfo;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcStudent;
import com.gnet.model.admin.CcTeacherCourse;
import com.gnet.plugin.poi.ExcelDefinition;
import com.gnet.plugin.poi.ExcelParser;
import com.gnet.plugin.poi.Header2ExcelParser;
import com.gnet.plugin.poi.RowDefinition;
import com.gnet.plugin.poi.RowDefinition.ColumnDefinition;
import com.gnet.plugin.poi.RowDefinition.GroupColumnDefinition;
import com.gnet.service.CcStudentService;
import com.google.common.collect.Maps;
import com.jfinal.log.Logger;


/**
 * 考核成绩分析法教学班学生批量成绩录入
 * 
 * @author SY
 * @Date 2017年10月6日
 */
@Transactional(readOnly = false)
@Service("EM00740")
public class EM00740 extends BaseApi implements IApi {
	
	private static final Logger logger = Logger.getLogger(EM00740.class);
	
	@Autowired
	private CcStudentService ccStudentService;
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		
		Object fileInfoObject = param.get("fileInfo");
		FileInfo fileInfo = (FileInfo) fileInfoObject;
		Map<String, Object> result = Maps.newHashMap();
		
		Long eduClassId = paramsLongFilter(param.get("eduClassId"));
		Long batchId = paramsLongFilter(param.get("batchId"));
		// 开课课程成绩组成元素编号
		Long courseGradeComposeId = paramsLongFilter(param.get("courseGradeComposeId"));
		// 教学班编号为空过滤
		if (eduClassId == null) {
			return renderFAIL("0500", response, header);
		}
		// 课程成绩组成为空过滤
		if (courseGradeComposeId == null) {
			return renderFAIL("0490", response, header);
		}
		// 获得教师开课课程
		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findCourseByClassId(eduClassId);
		if (ccTeacherCourse == null) {
			return renderFAIL("0501", response, header);
		}
		
		try {
			Map<String, Object> returnMap = ccStudentService.getScoreMap(ccTeacherCourse, courseGradeComposeId,batchId);
			Map<String, Object> header2 = (Map<String, Object>) returnMap.get("head");
			Map<String, Object> headerIdMap = (Map<String, Object>) returnMap.get("headId");

			String url = fileInfo.getTempUrl();
			File file = new File(url);
	        InputStream inputStream = new FileInputStream(file); 
	        ExcelParser excelParser = new Header2ExcelParser();
	        ExcelDefinition excelDefinition = excelParser.parse(inputStream, header2, 2);
	        
	        // 把excelDefinition数据传递要页面。
	        // 一个个学生
	        Map<String, Object> returnMapList = getReturnList(excelDefinition, headerIdMap, eduClassId);
			Boolean isSuccess = (Boolean) returnMapList.get("isSuccess");
	        if(!isSuccess) {
	        	return renderFAIL("400", response, header, returnMapList.get("failMessage"));
	        }
	        result.put("excelDefinition", returnMapList);
	        result.put("isSuccess", Boolean.TRUE);
		} catch (Exception e) {
//			result.put("failMessage", e+"。 并请检查模板的数据结构是否和导入班级一样，且不重复。");
//			result.put("isSuccess", Boolean.FALSE);
//			return renderSUC(result, response, header);
//			return renderFAIL("403", response, header, e+"。 并请检查模板的数据结构是否和导入班级一样，且不重复。");
			e.printStackTrace();
			if (logger.isErrorEnabled()) {
				logger.error("批量导入成绩失败", e);
			}
			return renderFAIL("400", response, header, "请检查模板的是否正确。包括：1.是否存在重复。2.是否选错班级。3.是否修改过结构。4.是否是正确的成绩组成。解决方案：重新下载一份。");
		}
		result.put("isSuccess", Boolean.TRUE);		
		return renderSUC(result, response, header);
	}

	/**
	 * 根据数据，得到返回的列表
	 * @param excelDefinition
	 * 			excel获取后封装的对象
	 * @param headerEvaluteIdMap
	 * 			head对应的考评点编号树
	 * @param eduClassId 
	 * 			教学班编号
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年10月16日 上午11:54:01 
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getReturnList(ExcelDefinition excelDefinition, Map<String, Object> headerIdMap,Long eduClassId) {
		// Map<head/body, Object>
		Map<String, Object> returnMap = new HashMap<>();
		// 一个个学生信息 List<Map<第几行， Map<字段名，字段数据>>>
		List<Map<Integer, Map<String, Object>>> body = new ArrayList<>();
		returnMap.put("isSuccess", Boolean.TRUE);
		
		// 《成绩组成名称，Map<课程目标名字，课程目标编号>》
		Map<String, Map<String, Long>> headerGradecomposeIndicationIdMap = (Map<String, Map<String, Long>>) headerIdMap.get("returnHeadGradecomposeIndicationIdMap");
		// 《成绩组成名称，Map<课程目标名字，courseGradecomposeIndicationId>
		Map<String, Map<String, Long>> courseGradecomposeIndicationIdMap = (Map<String, Map<String, Long>>) headerIdMap.get("courseGradecomposeIndicationIdMap");
		// 《成绩组成名称，Map<课程目标名字，课程目标满分数值>
		Map<String, Map<String, BigDecimal>> courseGradecomposeIndicationFullScoreMap = (Map<String, Map<String, BigDecimal>>) headerIdMap.get("courseGradecomposeIndicationFullScoreMap");
		for (int i=0;i<courseGradecomposeIndicationFullScoreMap.size();i++){

		}
		// 准备学生的数据 
		//Map<StudentNo, studentId>
		Map<String, Long> studentNoAndIdMap = new HashMap<>();
		List<CcStudent> ccStudents = CcStudent.dao.findByEduclassId(eduClassId);
		for(CcStudent temp : ccStudents) {
			String studentNo = temp.getStr("student_no");
			Long studentId = temp.getLong("id");
			studentNoAndIdMap.put(studentNo, studentId);
		}
		
		// 第几个格子是什么属于哪个的ID
		Map<Integer, Long> indicationIdMap = new HashMap<>();
		Map<Integer, Long> courseGradecomposeIndicationNoAndIdMap = new HashMap<>();
		// 当前课程目标是属于哪个成绩组成map<gradecomposeId, Map<gradecomposeName/indicationName, name>>
		Map<Long, Map<String, String>> gradecomposeAndPreName = new HashMap<>();
		Map<Integer, BigDecimal> courseGradecomposeIndicationNoAndFullScoreMap = new HashMap<>();
		RowDefinition prHeaderOne = excelDefinition.getHeader();
		GroupColumnDefinition prHeaderTow = prHeaderOne.getGroupColumnDefinition();
		List<ColumnDefinition> preHeader = prHeaderTow.getColumns();
		for(int i = 0 ; i < preHeader.size(); i++) {
			ColumnDefinition rowDefinition = preHeader.get(i);
			Integer index = rowDefinition.getIndex();
			if(index < 4) {
				continue;
			}
			// 成绩组成名字
			String gradecomposeName = rowDefinition.getName();
			// 课程目标列表
			List<ColumnDefinition> indicationHeader = ((GroupColumnDefinition) rowDefinition).getColumns();
			for(ColumnDefinition indication : indicationHeader) {
				index = indication.getIndex();
				String indicationName = indication.getName();
				indicationName = deleteCONumbers(indicationName);
				// 设置第几个格子是什么编号
				Long indicationId = headerGradecomposeIndicationIdMap.get(gradecomposeName).get(indicationName);
				indicationIdMap.put(index, indicationId);
				
				Map<String, String> preName = new HashMap<>();
				preName.put("indicationName", indicationName);
				preName.put("gradecomposeName", gradecomposeName);
				gradecomposeAndPreName.put(indicationId, preName);
				
				// 设置第几个格子是什么编号
				Long courseGradecomposeIndicationId = courseGradecomposeIndicationIdMap.get(gradecomposeName).get(indicationName);
				courseGradecomposeIndicationNoAndIdMap.put(index, courseGradecomposeIndicationId);
				
				// 设置第几个格子多少是满分
				BigDecimal courseGradecomposeIndicationFullScore = courseGradecomposeIndicationFullScoreMap.get(gradecomposeName).get(indicationName);
				courseGradecomposeIndicationNoAndFullScoreMap.put(index, courseGradecomposeIndicationFullScore);
			}
		}
		
		// 所有的学生数据
		List<RowDefinition> preBody = excelDefinition.getBody();
		// 学生导入的错误数据，《第几行。 List<错误信息>》
		Map<Integer, List<String>> studentErrorMessage = new HashMap<>();
		for(int rowIndex = 0; rowIndex < preBody.size(); rowIndex++) {
			RowDefinition rowDefinition = preBody.get(rowIndex);
			// 单个学生的，数据。格子需要是key
			Map<Integer, ColumnDefinition> indexMap = rowDefinition.getIndexs();
			Map<Integer, Map<String, Object>> student = new HashMap<>();
			Integer columnSize = indexMap.size();
			for(Map.Entry<Integer, ColumnDefinition> entry : indexMap.entrySet()) {
				// 应该是用不到的，但是还是排除一下
				ColumnDefinition column = entry.getValue();
				if(column == null) {
					continue;
				}
				Map<String, Object> cell = new HashMap<>();
				Object value = column.getValue();
				String name = column.getName();
				Integer columnIndex = column.getIndex();
				cell.put("index", columnIndex);
				cell.put("name", name);
				cell.put("value", value);
				if(columnIndex == 0) {
					cell.put("data", "index");
				} else if(columnIndex == 1) {
					String studentNo = value.toString();
					Long studentId = studentNoAndIdMap.get(studentNo);
					if(studentId == null) {
						List<String>  errorMessage = studentErrorMessage.get(rowIndex);
						if(errorMessage == null) {
							errorMessage = new ArrayList<>();
							studentErrorMessage.put(rowIndex, errorMessage);
						}
						errorMessage.add("该教学班不存在该学号的学生。");
					}
					cell.put("studentId", studentId);
					cell.put("data", "studentNo");
				} else if(columnIndex == 2) {
					cell.put("data", "name");
				} else if(columnIndex == 3) {
					cell.put("data", "educlassName");
				} else if(columnIndex > 3) {
					// 如果只课程目标，则输入课程目标的编号，由于前四个都是固定的，所以第五个，即i=4开始输入课程目标编号
					Long indicationId = indicationIdMap.get(columnIndex);
					cell.put("indicationId", indicationId);
					cell.put("gradecomposeIndicationId", courseGradecomposeIndicationNoAndIdMap.get(columnIndex));
//					Integer trueColumnIndex = columnIndex + 1;
//					Integer trueRowIndex = rowIndex + 4;
					String gradecomposeName = gradecomposeAndPreName.get(indicationId).get("gradecomposeName");
					String indicationName = gradecomposeAndPreName.get(indicationId).get("indicationName");
					Boolean isNum = isNum(value.toString());
					if(!isNum) {
						List<String>  errorMessage = studentErrorMessage.get(rowIndex);
						if(errorMessage == null) {
							errorMessage = new ArrayList<>();
							studentErrorMessage.put(rowIndex, errorMessage);
						}
						errorMessage.add(gradecomposeName+"的"+indicationName+"，当前分值为："+value+"，并非是符合要求的数据。");
//						errorMessage.add("第"+trueRowIndex+"行第"+trueColumnIndex+"列，当前分值为："+value+"，并非是符合要求的数据。");
					} else {
						BigDecimal score = new BigDecimal(value.toString());
						BigDecimal fullScore = courseGradecomposeIndicationNoAndFullScoreMap.get(columnIndex);
						if(fullScore == null) {
							List<String>  errorMessage = studentErrorMessage.get(rowIndex);
							if(errorMessage == null) {
								errorMessage = new ArrayList<>();
								studentErrorMessage.put(rowIndex, errorMessage);
							}
							errorMessage.add(gradecomposeName+"的"+indicationName+"，当前分值为："+value+"，但是满分值不存在。");
						}else if(fullScore.compareTo(score) == -1) {
							List<String>  errorMessage = studentErrorMessage.get(rowIndex);
							if(errorMessage == null) {
								errorMessage = new ArrayList<>();
								studentErrorMessage.put(rowIndex, errorMessage);
							}
//							errorMessage.add("第"+trueRowIndex+"行第"+trueColumnIndex+"列，当前分值为："+score+"，超过了满分值："+fullScore+"。");
							errorMessage.add(gradecomposeName+"的"+indicationName+"，当前分值为："+value+"，超过了满分值："+fullScore+"。");
						}
					}
					
					cell.put("data", "grade");
				}
				student.put(columnIndex, cell);
			}
			// 设置当前行的错误信息
			List<String> errorMessage = studentErrorMessage.get(rowIndex);
			if(errorMessage != null && !errorMessage.isEmpty()) {
				Map<String, Object> errorCell = new HashMap<>();
				errorCell.put("index", columnSize);
				errorCell.put("name", "错误信息");
				errorCell.put("value", errorMessage);
				errorCell.put("data", "errorMessage");
				student.put(columnSize, errorCell);
			}
			body.add(student);
		}
		
		// 解析header，放入data这个名字
		Map<Integer, ColumnDefinition> indexs = prHeaderOne.getIndexs();
		Map<String, Object> header = new HashMap<>();
		header.put("groupColumnDefinition", prHeaderTow);
		// Map<第几列, Map<属性名称，属性数据>>
		Map<Integer, Map<String, Object>> newIndexs = new HashMap<>();
		header.put("indexs", prHeaderTow);
		for(int i = 0; i < indexs.size(); i++) {
			ColumnDefinition columnDefinition = indexs.get(i);
			// 每个格子
			Map<String, Object> cell = new HashMap<>();
			cell.put("index", columnDefinition.getIndex());
			cell.put("name", columnDefinition.getName());
			cell.put("type", columnDefinition.getType());
			cell.put("data", "grade");
			
			newIndexs.put(i, cell);
		}
		
		returnMap.put("body", body);
		returnMap.put("header", header);
		return returnMap;
	}

	/**
	 * 去掉默认的CO
	 * @param indicationName
	 * @return
	 * @author GJM
	 * @version 创建时间：2020年6月28日 下午4:21:43
	 */
	private String deleteCONumbers(String indicationName) {
		for(int i = 1; i < 999999; i++) {
			if(indicationName.contains("CO")) {
				indicationName = indicationName.replace("CO", "");
			} else {
				System.out.println("CO"+i+":" + " is error！");
				break;
			}
		}
		return indicationName;
	}
	/**
	 * 去掉默认的CO?:
	 * @param indicationName
	 * @return
	 * @author sy
	 * @version 创建时间：2017年12月28日 下午4:21:43
	 */
	private String deleteCONumber(String indicationName) {
		for(int i = 1; i < 999999; i++) {
			if(indicationName.contains("CO")) {
				indicationName = indicationName.replace("CO"+i+":", "");
			} else {
				System.out.println("CO"+i+":" + " is error！");
				break;
			}
		}
		return indicationName;
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