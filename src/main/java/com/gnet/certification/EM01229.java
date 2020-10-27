package com.gnet.certification;

import com.gnet.FileInfo;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.api.sign.Result;
import com.gnet.model.admin.CcEduclass;
import com.gnet.model.admin.CcEduclassStudent;
import com.gnet.model.admin.CcStudent;
import com.gnet.model.admin.CcTeacherCourse;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.service.CcEduindicationStuScoreService;
import com.gnet.service.FileService;
import com.gnet.utils.ExcelUtil;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Record;
import com.microsoft.schemas.office.visio.x2012.main.CellType;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.omg.CORBA.IRObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 教学班学生导入
 *
 * @author GJM
 * @Date 2020年10月19日
 */
@Transactional(readOnly = false)
@Service("EM01229")
public class EM01229 extends BaseApi implements IApi {

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
		Result readRresult = readExcel(PathKit.getWebRootPath() + fileInfo.getTargetUrl(), schoolId,educlassId);
		if (!readRresult.getFlag()){

			result.put("isSuccess",false);
			result.put("message",readRresult.getMessage());
			return renderSUC(result, response, header);
		}

		Result saveResult = saveStudent(readRresult.getData(), educlassId);
		if (saveResult.getFlag()){
			result.put("isSuccess",true);
			result.put("message","导入成功！");
		}else {
			result.put("isSuccess",false);
			result.put("message",saveResult.getMessage());
		}


		return renderSUC(result, response, header);
	}

	/*
	 * @param path
		 * @param edClassId
	 * @return com.gnet.api.sign.Result
	 * @author Gejm
	 * @description: 读取Excel
	 * @date 2020/10/22 9:45
	 */
	public Result readExcel(String path,Long schoolId,Long educlassId) {
		List<Long> addList = Lists.newArrayList();
		try {
			Workbook wb = WorkbookFactory.create(new File(path));
			Sheet sheet = wb.getSheetAt(0);
			for (int i=1;i<=sheet.getLastRowNum();i++){
				Row row = sheet.getRow(i);
				//只获取第一列的学号
				//String studentNoS = row.getCell(0).getStringCellValue();
				if (row==null){
					return Result.error("学号不可为空，请检查！");
				}
				String studentNo = ExcelUtil.getCellValue(row.getCell(0));
				if (studentNo!=null || studentNo!=""){
					String[] studentNos = new String[1];
					studentNos[0]=studentNo;
					//判断是否存在此学生
					List<Record> studentList = CcStudent.dao.existedStudents(studentNos, schoolId);
					if (studentList.size()>1){
						return Result.error("此学号"+studentNo+"存在多个学生，请检查！");
					}else if (studentList.size()==0){
						return Result.error("此学号"+studentNo+"不存在学生，请检查！");
					}else{
						Long studentId = studentList.get(0).getLong("id");
						//教学班已经存在学生编号
						List<CcEduclassStudent> educlassStudents = CcEduclassStudent.dao.findFilteredByColumn("class_id", educlassId);

						if (!educlassStudents.isEmpty()) {
							for (CcEduclassStudent educlassStudent : educlassStudents) {
								Long studentId1 = educlassStudent.getLong("student_id");
								if (studentId.equals(studentId1)){
									return Result.error("此学号"+studentNo+"已在该教学班中，请检查！");
								}
							}

						}

						if (!addList.contains(studentId)) {
							addList.add(studentId);
						}else{
							return Result.error("此学号"+studentNo+"在导入数据中有重复的，请检查！");
						}
					}
				}else{
					return Result.error("学号不可为空，请检查！");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		return  Result.ok(addList);
	}
	/*
	 * @param
	 * @return com.gnet.api.sign.Result
	 * @author Gejm
	 * @description: 保存
	 * @date 2020/10/22 11:40
	 */
	public Result saveStudent(List<Long> studentIds,Long educlassId){
		if(null == studentIds || studentIds.size() == 0){
			return Result.error("添加学生数据为空，请检查！");
		}
		if (educlassId == null) {
			return Result.error("教学班Id为空，请重试！");
		}



		//验证学生是否在某个们课程某个学期中已经添加
		CcEduclass educlass = CcEduclass.dao.findFilteredById(educlassId);
		if(educlass == null){
			return Result.error("教学班信息为空，请重试！");
		}

		Date date = new Date();
		educlass.set("modify_date", date);
		educlass.set("student_num_change_date", date);
		if(!educlass.update()){
			return Result.error("修改教学班人数更改日期失败，请重试！");
		}

		//学生是否在某个们课程某个学期中已经添加的数据
		List<CcTeacherCourse> existTeacherCourseClassStudent = CcTeacherCourse.dao.findExistCourseClassStudent(educlassId, studentIds.toArray(new Long[studentIds.size()]));

		if(!existTeacherCourseClassStudent.isEmpty()){
			StringBuilder stringBuilder = new StringBuilder();
			for(CcTeacherCourse student : existTeacherCourseClassStudent){
				stringBuilder.append("学号为"+ student.getStr("student_no") + "的学生" + student.getStr("name") + ",");
			}
			return Result.error(stringBuilder.append("已经在相同学期、相同课程中存在了").toString());
		}

		List<CcEduclassStudent> newStudents  = Lists.newArrayList();
		for (Long studentId : studentIds) {
			IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
			Long id = idGenerate.getNextValue();
			CcEduclassStudent educlassStudent = new CcEduclassStudent();
			educlassStudent.set("id", id);
			educlassStudent.set("create_date", date);
			educlassStudent.set("modify_date", date);
			educlassStudent.set("class_id", educlassId);
			educlassStudent.set("student_id", studentId);
			educlassStudent.set("is_caculate", Boolean.TRUE);
			educlassStudent.set("is_del", Boolean.FALSE);
			newStudents.add(educlassStudent);
		}

		if(!CcEduclassStudent.dao.batchSave(newStudents)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Result.error("导入学生失败，请重试！");
		}

		List<Long> educlassIdList = Lists.newArrayList();
		educlassIdList.add(educlassId);

		if (!educlassIdList.isEmpty()) {

			CcEduindicationStuScoreService ccEduindicationStuScoreService = SpringContextHolder.getBean(CcEduindicationStuScoreService.class);

			if (!ccEduindicationStuScoreService.calculate(educlassIdList, Lists.<Long>newArrayList())) {
				return Result.error("更新课程成绩组成失败，请重试！");
			}
		}

		return Result.ok();
	}

}