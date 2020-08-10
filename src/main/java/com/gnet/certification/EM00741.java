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
import com.gnet.plugin.poi.Header3ExcelParser;
import com.gnet.plugin.poi.RowDefinition;
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
@Deprecated
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
		
		Long eduClassId = paramsLongFilter(param.get("eduClassId"));
		// 教学班编号为空过滤
		if (eduClassId == null) {
			return renderFAIL("0500", response, header);
		}
		// 获得教师开课课程
		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findCourseByClassId(eduClassId);
		if (ccTeacherCourse == null) {
			return renderFAIL("0501", response, header);
		}
		
		try {
			Map<String, Object> returnMap = ccStudentService.getEvaluateMap(ccTeacherCourse);
			Map<String, Object> header3 = (Map<String, Object>) returnMap.get("head");
			Map<String, Object> headerIdMap = (Map<String, Object>) returnMap.get("headId");

			String url = fileInfo.getTempUrl();
			File file = new File(url);
	        InputStream inputStream = new FileInputStream(file); 
	        ExcelParser excelParser = new Header3ExcelParser();
	        ExcelDefinition excelDefinition = excelParser.parse(inputStream, header3, 3);
	        
	        // 把excelDefinition数据传递要页面。
	        Map<String, Object> returnMapList = getReturnList(excelDefinition, headerIdMap, eduClassId);
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
	 * @param headerEvaluteIdMap
	 * 			head对应的考评点编号树
	 * @param eduClassId 
	 * 			教学班编号
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年10月16日 下午6:07:32 
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getReturnList(ExcelDefinition excelDefinition, Map<String, Object> headerIdMap, Long eduClassId) {
		// Map<head/body, Object>
		Map<String, Object> returnMap = new HashMap<>();
		// 一个个学生信息 List<Map<第几行， Map<字段名，字段数据>>>
		List<Map<Integer, Map<String, Object>>> body = new ArrayList<>();
		returnMap.put("isSuccess", Boolean.TRUE);
		
		// 《类型名称，Map<指标点名字，Map<考评点名称，考评点编号>>》
		Map<String, Map<String, Map<String, Long>>> headerEvaluteIdMap = (Map<String, Map<String, Map<String, Long>>>) headerIdMap.get("returnHeadEvaluteIdMap");
		// 《类型名称，Map<指标点名字，指标点编号>》
		Map<String, Map<String, Long>> returnHeadIndicationIdMap = (Map<String, Map<String, Long>>) headerIdMap.get("returnHeadIndicationIdMap");
		
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
		Map<Integer, Long> evaluteIdMap = new HashMap<>();
		// 第几个格子是什么属于哪个的ID
		Map<Integer, Long> indicationIdMap = new HashMap<>();
		// 当前考评点是属于哪个考评点类型和那个指标点 map<evalutId, Map<evaluteTypeName/indicationName/evaluteName, name>>
		Map<Long, Map<String, String>> evaluteIdAndPreName = new HashMap<>();
		RowDefinition prHeaderOne = excelDefinition.getHeader();
		GroupColumnDefinition prHeaderTow = prHeaderOne.getGroupColumnDefinition();
		// 我这里是平时成绩，下面应该还有一层指标点。但是这里只有getColumns,没有getGroupColumnDefinition
		List<ColumnDefinition> preHeader = prHeaderTow.getColumns();
		for(int i = 0 ; i < preHeader.size(); i++) {
			ColumnDefinition rowDefinition = preHeader.get(i);
			Integer index = rowDefinition.getIndex();
			if(index < 4) {
				continue;
			}
			// 考评点类型名字
			String evaluteTypeName = rowDefinition.getName();
			// 指标点列表
			List<ColumnDefinition> indicationHeader = ((GroupColumnDefinition) rowDefinition).getColumns();
			for(ColumnDefinition indication : indicationHeader) {
				String indicationName = indication.getName();
			
				List<ColumnDefinition> evaluteHeader = ((GroupColumnDefinition) indication).getColumns();
				for(ColumnDefinition evalute : evaluteHeader) {
					index = evalute.getIndex();
					String evaluteName = evalute.getName();
					Long evaluteId = headerEvaluteIdMap.get(evaluteTypeName).get(indicationName).get(evaluteName);
					evaluteIdMap.put(index, evaluteId);
					
					Map<String, String> preName = new HashMap<>();
					preName.put("evaluteTypeName", evaluteTypeName);
					preName.put("indicationName", indicationName);
					preName.put("evaluteName", evaluteName);
					evaluteIdAndPreName.put(evaluteId, preName);
					
					Long indicationId = returnHeadIndicationIdMap.get(evaluteTypeName).get(indicationName);
					indicationIdMap.put(index, indicationId);
				}
			}
		}
		
		// 准备考评点的层次数据
		CcEvaluteLevelService ccEvaluteLevelService = SpringContextHolder.getBean(CcEvaluteLevelService.class);
		// Map<indicationId, Map<分数, levelId>>
		Map<Long, Map<BigDecimal, Long>> evaluteLevelMap = ccEvaluteLevelService.getIndicationAndEvaluteIdMap(eduClassId);
		if(evaluteLevelMap == null || evaluteLevelMap.isEmpty()) {
			returnMap.put("isSuccess", Boolean.FALSE);
			returnMap.put("failMessage", "该教学班不存在考评点层次数据！");
			return returnMap;
		}
		
		// 所有的学生数据
		// 学生导入的错误数据，《第几行。 List<错误信息>》
		Map<Integer, List<String>> studentErrorMessage = new HashMap<>();
		List<RowDefinition> preBody = excelDefinition.getBody();
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
					cell.put("studentId", studentNoAndIdMap.get(studentNo));
					cell.put("data", "studentNo");
				} else if(columnIndex == 2) {
					cell.put("data", "name");
				} else if(columnIndex == 3) {
					cell.put("data", "educlassName");
				} else if(columnIndex > 3) {
					// 由于前四个都是固定的，所以第个，即i=4开始输入考评点编号和层次编号
					Long evaluteId = evaluteIdMap.get(columnIndex);
					cell.put("evaluteId", evaluteId);
					Long indicationId = indicationIdMap.get(columnIndex);
//					Integer trueColumnIndex = columnIndex + 1;
//					Integer trueRowIndex = rowIndex + 5;
//					Boolean isNum = isNum(value.toString());
//					if(!isNum) {
//						List<String>  errorMessage = studentErrorMessage.get(rowIndex);
//						if(errorMessage == null) {
//							errorMessage = new ArrayList<>();
//							studentErrorMessage.put(rowIndex, errorMessage);
//						}
//						errorMessage.add("第"+trueRowIndex+"行第"+trueColumnIndex+"列，当前分值为："+value+"，并非是符合要求的数据。");
//					} else {
						// 根据上传的数据，返回id，数据可能是：分数、中文。
						Map<String, Object> caculateMap = ccEvaluteLevelService.calculateToLevelId(value, evaluteLevelMap, indicationId);
						Boolean isCaculate = (Boolean) caculateMap.get("isSuccess");
						Long levelId = (Long) caculateMap.get("levelId");
						if(!isCaculate || levelId == null) {
							List<String>  errorMessage = studentErrorMessage.get(rowIndex);
							if(errorMessage == null) {
								errorMessage = new ArrayList<>();
								studentErrorMessage.put(rowIndex, errorMessage);
							}
							// 提示变成 那个考评点类型下的那个指标点下的那个考评点
							String indicationName = evaluteIdAndPreName.get(evaluteId).get("indicationName");
							String evaluteTypeName = evaluteIdAndPreName.get(evaluteId).get("evaluteTypeName");
							String evaluteName = evaluteIdAndPreName.get(evaluteId).get("evaluteName");
							errorMessage.add(evaluteTypeName+"的"+indicationName+"的"+evaluteName+"的数据当前为："+value+"，不符合考评点层次分值。");
//							errorMessage.add("第"+trueRowIndex+"行第"+trueColumnIndex+"列，当前为："+value+"，不符合考评点层次分值。");
						} else {
							cell.put("levelId", levelId);							
						}
//					}
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