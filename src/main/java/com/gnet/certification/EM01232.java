package com.gnet.certification;

import com.gnet.FileInfo;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.api.sign.Result;
import com.gnet.model.admin.*;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.service.CcEduindicationStuScoreService;
import com.gnet.service.CcStudentService;
import com.gnet.service.FileService;
import com.gnet.utils.ExcelUtil;
import com.gnet.utils.PriceUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Record;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


/**
 * 教学班所有成绩导入
 *
 * @author GJM
 * @Date 2020年11月5日
 */
@Transactional(readOnly = false)
@Service("EM01232")
@Slf4j
public class EM01232 extends BaseApi implements IApi {

	@Autowired
	private CcStudentService ccStudentService;
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		HashMap<Object, Object> result = new HashMap<>();
		Map<String, Object> param = request.getData();
		Object fileInfoObject = param.get("fileInfo");
		Long educlassId = paramsLongFilter(param.get("educlassId"));
		String token = request.getHeader().getToken();
		Long schoolId = UserCacheKit.getSchoolId(token);
		// fileInfo合法性验证
		if (fileInfoObject == null || !(fileInfoObject instanceof FileInfo)) {
			return renderFAIL("0087", response, header);
		}
		FileInfo fileInfo = (FileInfo) fileInfoObject;
		FileService fileService = SpringContextHolder.getBean(FileService.class);
		// 上传失败验证
		if (!fileService.upload(fileInfo, Boolean.FALSE)) {
			return renderFAIL("0088", response, header);
		}
		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findCourseByClassId(educlassId);
		if (ccTeacherCourse==null){
			return renderFAIL("0311", response, header);
		}
		Long teacherCourseId = ccTeacherCourse.getLong("id");
		log.info("-----------准备这个课程的成绩组成数据--------------------");
		List<CcCourseGradecompose> courseGradecomposeList= CcCourseGradecompose.dao.findByTeacherCourseIdAndCourseGradeComposeIdsOrderBySort(teacherCourseId, Lists.newArrayList(),null);
		//直接输入课程目标形式的
		ArrayList<Map<String,Object>> gradecomposeIndicationInfoList = new ArrayList<>();
		//题目形式的录入
		ArrayList<Map<String,Map<String, Map<String, Object>>>> subjectInfoList = new ArrayList<>();
		//《成绩组成名称，录入方式》
		Map<String,Integer> gradeComposeInputType = new HashMap<>();
		//<成绩组成名称，开课成绩组成id/批次id>
		HashMap<String, Long> gradecompseMap = new HashMap<>();
		for (CcCourseGradecompose temp : courseGradecomposeList){
			Long courseGradeComposeId = temp.getLong("id");
			String gradecomposeName = temp.getStr("name");
			Integer inputScoreType = temp.getInt("input_score_type");
			String batchName = temp.getStr("batchName");
			Long batchId = temp.getLong("batchId");
			String name="";
			if (batchId !=null){
				name=gradecomposeName+"-"+batchName;
			}else{
				name=gradecomposeName;
			}
			gradeComposeInputType.put(name,inputScoreType);
			if (inputScoreType==1 || inputScoreType==4){

				//多批次直接录入方式
				Map<String, Object> returnMap = ccStudentService.getScoreMap2(ccTeacherCourse, courseGradeComposeId,batchId);
				Map<String, Object> headerIdMap = (Map<String, Object>) returnMap.get("headId");
				if (inputScoreType==1){
					gradecompseMap.put(gradecomposeName,courseGradeComposeId);
				}else{
					gradecompseMap.put(gradecomposeName+"-"+batchName,batchId);
				}
				gradecomposeIndicationInfoList.add(headerIdMap);

			}else if (inputScoreType==2 || inputScoreType==3){
				//单批次题目 和多批次题目
				List<CcCourseGradeComposeDetail> ccCourseGradeComposeDetails = CcCourseGradeComposeDetail.dao.topicList0(courseGradeComposeId, batchId);
				if(ccCourseGradeComposeDetails.isEmpty()){
					return renderFAIL("2105", response, header);
				}
				Map<String,Map<String, Map<String, Object>>> gradecomposeSubjectInfo = new HashMap<>();
				Map<String, Map<String, Object>> subjectInfo = new HashMap<>();
				//题目key题号(分数)value编号map

				Map<String, Object> subjectMap = new HashMap<>();
				Map<String, Object> subjectScoreMap = new HashMap<>();
				for(CcCourseGradeComposeDetail ccCourseGradeComposeDetail : ccCourseGradeComposeDetails){
					String detail = String.format("%s(%s)", ccCourseGradeComposeDetail.getStr("name"), ccCourseGradeComposeDetail.getBigDecimal("score"));
					subjectMap.put(detail, ccCourseGradeComposeDetail.getLong("id"));
					subjectScoreMap.put(detail, ccCourseGradeComposeDetail.getBigDecimal("score"));
				}


				subjectInfo.put("subjectMap",subjectMap);
				subjectInfo.put("subjectScoreMap",subjectScoreMap);
				if (inputScoreType==2){
					gradecompseMap.put(gradecomposeName,courseGradeComposeId);
					gradecomposeSubjectInfo.put(gradecomposeName,subjectInfo);
				}else {
					//多批次的题目录入方式
					gradecompseMap.put(gradecomposeName+"-"+batchName,batchId);
					gradecomposeSubjectInfo.put(gradecomposeName+"-"+batchName,subjectInfo);
				}

				subjectInfoList.add(gradecomposeSubjectInfo);
			}
			//gradecomposeList.add(gradecompseMap);
		}
		log.info("--------------准备数据成功，开始读取excel-----------------");
		Result readRresult = readExcel(PathKit.getWebRootPath() + fileInfo.getTargetUrl(), gradecompseMap,educlassId,gradeComposeInputType,gradecomposeIndicationInfoList,subjectInfoList);
		if (!readRresult.getFlag()){

			result.put("isSuccess",false);
			result.put("message",readRresult.getMessage());
			return renderSUC(result, response, header);
		}
		result.put("isSuccess",true);
		result.put("body",readRresult.getDataMap());



		return renderSUC(result, response, header);
	}

	/*
	 * @param path
		 * @param edClassId
	 * @return com.gnet.api.sign.Result
	 * @author Gejm
	 * @description: 读取Excel 并验证数据有效性
	 * @date 2020/10/22 9:45
	 */
	public Result readExcel(String path,Map<String,Long> gradecomposeMap,Long educlassId,Map<String,Integer> gradeComposeInputType,
							List<Map<String,Object>> gradecomposeIndicationInfoList,List<Map<String,Map<String, Map<String, Object>>>> subjectInfoList) {
		//表头行
		ArrayList<Map<String, Object>> headerList = new ArrayList<>();
		HashMap<Object, Object> body = new HashMap<>();
		//教学班的所有学生,<学号，学生id>
		HashMap<Object, Object> educlassAllStudent = new HashMap<>();
		List<CcEduclassStudent> stuInfoByClassList = CcEduclassStudent.dao.findStuInfoByClassId(educlassId);
		if (stuInfoByClassList.size()==0){
			return Result.error("错误:该教学班不存在学生！");
		}
		for ( CcEduclassStudent temp: stuInfoByClassList){
			educlassAllStudent.put(temp.get("student_no"),temp.get("student_id"));
		}
		//解析后的学生数据
		ArrayList<Object> studentInfoLists = new ArrayList<>();
		try {
			Workbook wb = WorkbookFactory.create(new File(path));
			Sheet sheet = wb.getSheetAt(0);
			//从第2行开始
			for (int i = 1; i < sheet.getLastRowNum()  + 1; i++) {
				Row row = sheet.getRow(i);
				HashMap<String, Object> headerGradecomposeMap = new HashMap<>();
				//一行数据一个list
				ArrayList<Object> studentIofoList = new ArrayList<>();
				//错误信息
				HashMap<Object, Object> errMessAgeMap = new HashMap<>();
				//状态
				HashMap<Object, Object> dataMessageMap = new HashMap<>();
				ArrayList<Object> errMessageList = new ArrayList<>();
				for (Cell c : row) {
					//学生的每一列数据
					HashMap<Object, Object> studentInfo = new HashMap<>();
					int index = c.getColumnIndex();
					String returnStr ;
					boolean isMerge = ExcelUtil.isMergedRegion(sheet, i, c.getColumnIndex());
					//判断是否具有合并单元格
					if (isMerge) {
						String rs = ExcelUtil.getMergedRegionValue(sheet, row.getRowNum(), c.getColumnIndex());
						returnStr = rs;
					} else {
						returnStr = ExcelUtil.getCellValue(c);
					}
					if (returnStr==null ||returnStr.equals("")){
						continue;
					}
					//处理表头,以及验证数据有效性，合并列从上往下找
					if (i==1){

						HashMap<String, Object> headerMap = new HashMap<>();
						if (index<=3){
							//前四列分别是序号、学号、姓名、教学班固定列 判断模板是否被更改
							String[] columnString=new String[]{"序号","学号","姓名","教学班名称"};
							if (!returnStr.equals(columnString[index])){
								return Result.error("错误：导入模板有问题，请重新下载");
							}
							headerMap.put("name",returnStr);
							headerMap.put("index",index);
							headerList.add(headerMap);


						}else{
							//成绩组成下的课程目标或题号
							ArrayList<Object> indicationList = new ArrayList<>();
							if (returnStr==null ||returnStr.equals("")){
								continue;
							}

							if (headerGradecomposeMap.get(returnStr)==null){
								if (gradecomposeMap.get(returnStr)==null){
									return Result.error("错误：导入模板有问题，该教学班不存在此"+returnStr+"成绩组成，请重新下载");
								}

								headerGradecomposeMap.put(returnStr,returnStr);
								Row row1 = sheet.getRow(2);
								for (Cell c1: row1){
									Integer isExist=0;

									//c1.getColumnIndex()>4 前4列是学生信息列
									if (c1.getColumnIndex()>3){
										HashMap<Object, Object> indicationMap = new HashMap<>();
										//第三行是课程目标或题号
										String indication =ExcelUtil.isMeargeStringValue(sheet, 2,c1.getColumnIndex());
										String indicationName="";
										if(indication.contains("CO")) {
											indicationName=indication.replace("CO","");
										}
										//第二行的成绩组成
										String gradcomposeName =ExcelUtil.isMeargeStringValue(sheet, 1,c1.getColumnIndex());
										if(gradcomposeName.equals(returnStr)){
											//课程目标或题号
											indicationMap.put("index",c1.getColumnIndex());
											indicationMap.put("name",indication);
											indicationList.add(indicationMap);
										}
										Integer inputType2 = gradeComposeInputType.get(gradcomposeName);
										if (gradecomposeMap.get(gradcomposeName)==null){
											return Result.error("错误：导入模板有问题，该教学班不存在此"+gradcomposeName+"成绩组成，请重新下载");
										}
										if (inputType2==2 || inputType2==3){

											//题目输入方式的题号验证是否存在
											for (int q=0; q<subjectInfoList.size();q++){
												Map<String, Map<String, Map<String, Object>>> subjectMaps = subjectInfoList.get(q);
												//成绩组成下的
												Map<String, Map<String, Object>> gradecomposeSubject = subjectMaps.get(gradcomposeName);
												if(gradecomposeSubject!=null){
													//<题号，题目id>
													Map<String, Object> subjectMap = gradecomposeSubject.get("subjectMap");
													Object subjectId = subjectMap.get(indication);
													if (subjectId != null){
														isExist=1;
														break;
													}


												}
											}
										}else{
											//课程目标验证
											for (int q=0 ; q<gradecomposeIndicationInfoList.size(); q++) {
												Map<String, Object> indicationInfos = gradecomposeIndicationInfoList.get(q);
												// 《成绩组成名称，Map<课程目标名字，课程目标编号>》
												Map<String, Map<String, Long>> headerGradecomposeIndicationIdMap = (Map<String, Map<String, Long>>) indicationInfos.get("returnHeadGradecomposeIndicationIdMap");

												if (headerGradecomposeIndicationIdMap.get(gradcomposeName) != null) {
													Long indicationId = headerGradecomposeIndicationIdMap.get(gradcomposeName).get(indicationName);
													if (indicationId != null) {
														isExist=1;
														break;
													}
												}

											}

										}
										if (isExist==0){
											return Result.error("错误：导入模板有问题，该"+gradcomposeName+"成绩组成下不存在此课程目标或题号："+indication+"，请重新下载");
										}
									}

								}



								//后面这些列是成绩组成的
								headerMap.put("name",returnStr);
								headerMap.put("index",index);
								headerMap.put("indicationList",indicationList);
								headerList.add(headerMap);

							}


						}


					}
					//录入数据从第四行开始读取
					if (i>=3){
						//表头名称
						String topName =ExcelUtil.isMeargeStringValue(sheet, 1,index);
						//学生信息
						if (index<=3){
							studentInfo.put("name",topName);
							studentInfo.put("index",index);
							studentInfo.put("value",returnStr);
							if (topName.equals("序号")){
								studentInfo.put("data","index");
							}
							else if (topName.equals("学号")){
								studentInfo.put("data","studentNo");
								studentInfo.put("studentId",educlassAllStudent.get(returnStr));
								if (educlassAllStudent.get(returnStr)==null){
									errMessageList.add("此教学班没有这个学生");
								}
							}
							else if (topName.equals("姓名")){
								studentInfo.put("data","name");
							}
							else if (topName.equals("教学班名称")){
								studentInfo.put("data","educlassName");
							}

						}else{
							//题号/课程目标
							String indication =ExcelUtil.isMeargeStringValue(sheet, 2,index);
							Integer inputType = gradeComposeInputType.get(topName);
							String indicationName="";
							if(indication.contains("CO")) {
								indicationName=indication.replace("CO","");
							}
							if (inputType==1 ||inputType==4){
								//gradecomposeIndicationInfoList是验证课程目标的值是否超过最大值
								for (int q=0 ; q<gradecomposeIndicationInfoList.size(); q++){
									Map<String, Object> indicationInfos = gradecomposeIndicationInfoList.get(q);
									// 《成绩组成名称，Map<课程目标名字，课程目标编号>》
									Map<String, Map<String, Long>> headerGradecomposeIndicationIdMap = (Map<String, Map<String, Long>>) indicationInfos.get("returnHeadGradecomposeIndicationIdMap");
									// 《成绩组成名称，Map<课程目标名字，courseGradecomposeIndicationId>
									Map<String, Map<String, Long>> courseGradecomposeIndicationIdMap = (Map<String, Map<String, Long>>) indicationInfos.get("courseGradecomposeIndicationIdMap");
									// 《成绩组成名称，Map<课程目标名字，课程目标满分数值>
									Map<String, Map<String, BigDecimal>> courseGradecomposeIndicationFullScoreMap = (Map<String, Map<String, BigDecimal>>) indicationInfos.get("courseGradecomposeIndicationFullScoreMap");
									if (headerGradecomposeIndicationIdMap.get(topName)!=null){
										Long indicationId = headerGradecomposeIndicationIdMap.get(topName).get(indicationName);
										if (indicationId !=null){
											studentInfo.put("data","grade");
											studentInfo.put("name",indication);
											studentInfo.put("index",index);
											studentInfo.put("indicationId",indicationId);
											studentInfo.put("inputScoreType",inputType);
											studentInfo.put("batchIdOrGradecomposeId",gradecomposeMap.get(topName));
										}
									}
									if (courseGradecomposeIndicationIdMap.get(topName)!=null){
										Long courseGradecomposeIndicationId = courseGradecomposeIndicationIdMap.get(topName).get(indicationName);
										if (courseGradecomposeIndicationId !=null){
											studentInfo.put("courseGradecomposeIndicationId",courseGradecomposeIndicationId);
										}
									}
									if (courseGradecomposeIndicationFullScoreMap.get(topName)!=null){
										//验证分数
										BigDecimal maxScore = courseGradecomposeIndicationFullScoreMap.get(topName).get(indicationName);
										//名字太长，截取：前的，CO1...
										String indicationNames = indication.substring(0, indication.indexOf(":"));
										if (maxScore!=null){
											//转换值
											try {
												BigDecimal score = new BigDecimal(returnStr);
												if(PriceUtils.greaterThan(score,maxScore)){
													errMessageList.add(topName+"成绩组成下"+indicationNames+"的值："+score+"超过最大值："+maxScore);
												}
												if (PriceUtils.greaterThan(CcScoreStuIndigrade.MIN_SCORE, score)){
													errMessageList.add(topName+"成绩组成下"+indicationNames+"的值："+score+"小于0");
												}
												studentInfo.put("value",score);
											} catch (Exception e) {
												errMessageList.add(indication+"的值格式填写错误！");
												e.printStackTrace();
											}
										}
									}



								}
							}else{
								//题目输入方式的验证数据
								for (int q=0; q<subjectInfoList.size();q++){
									Map<String, Map<String, Map<String, Object>>> subjectMaps = subjectInfoList.get(q);
									//成绩组成下的
									Map<String, Map<String, Object>> gradecomposeSubject = subjectMaps.get(topName);
									if(gradecomposeSubject!=null){
										//<题号，题目id>
										Map<String, Object> subjectMap = gradecomposeSubject.get("subjectMap");
										//<题号，成绩最大值>
										Map<String, Object> subjectScoreMap = gradecomposeSubject.get("subjectScoreMap");
										Object subjectId = subjectMap.get(indication);
										Object maxScoreO = subjectScoreMap.get(indication);
										if (subjectId != null){
											studentInfo.put("data","grade");
											studentInfo.put("indicationId",subjectId);
											studentInfo.put("index",index);
											studentInfo.put("name",indication);
											studentInfo.put("courseGradecomposeIndicationId","");
											studentInfo.put("inputScoreType",inputType);
											studentInfo.put("batchIdOrGradecomposeId",gradecomposeMap.get(topName));
										}
										if (maxScoreO != null){
											BigDecimal maxScore = new BigDecimal(maxScoreO.toString());
											try {
												BigDecimal score = new BigDecimal(returnStr);
												if(PriceUtils.greaterThan(score,maxScore)){
													errMessageList.add(topName+"成绩组成下"+indication+"的值："+score+"超过最大值："+maxScore);
												}
												if (PriceUtils.greaterThan(CcScoreStuIndigrade.MIN_SCORE, score)){
													errMessageList.add(topName+"成绩组成下"+indication+"的值："+score+"小于0");
												}
												studentInfo.put("value",score);
											} catch (Exception e) {
												errMessageList.add(indication+"的值格式填写错误！");
												e.printStackTrace();
											}

										}

									}
								}
							}
						}

						studentIofoList.add(studentInfo);

					}

				}
				if (i>=3){
					errMessAgeMap.put("data","errorMessage");
                    //errMessAgeMap.put("name","失败原因");
					errMessAgeMap.put("errMeageList",errMessageList);
					studentIofoList.add(errMessAgeMap);
					dataMessageMap.put("data","dataMessage");
					if (errMessageList.size()>0){
						dataMessageMap.put("value",false);
					}else {
						dataMessageMap.put("value",true);
					}
					studentIofoList.add(dataMessageMap);
					if (studentIofoList.size()>0){
						studentInfoLists.add(studentIofoList);
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		log.info("-----数据解析成功------------");
		body.put("headerList",headerList);
		body.put("studentInfoLists",studentInfoLists);
		return  Result.ok(body);
	}

}