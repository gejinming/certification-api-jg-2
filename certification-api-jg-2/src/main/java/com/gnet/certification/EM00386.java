package com.gnet.certification;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.FileInfo;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcEduclassStudent;
import com.gnet.model.admin.CcStudent;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.plugin.poi.ExcelHelper;
import com.gnet.service.FileService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Record;


/**
 * 教学班学生导入
 * 
 * @author wct
 * @Date 2016年7月6日
 */
@Transactional(readOnly = false)
@Service("EM00386")
public class EM00386 extends BaseApi implements IApi {
	
	private static final Logger logger = Logger.getLogger(EM00386.class);

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Object fileInfoObject = params.get("fileInfo");
		Long eduClassId = paramsLongFilter(params.get("eduClassId"));
		Long appointSchoolId = paramsLongFilter(params.get("appointSchoolId"));
		Long schoolId = UserCacheKit.getSchoolId(request.getHeader().getToken());
		if (appointSchoolId != null) {
			schoolId = appointSchoolId;
		}
		// 学校编号是否为空过滤
		if (schoolId == null) {
			return renderFAIL("0084", response, header);
		}
		// 教学班编号是否为空过滤
		if (eduClassId == null) {
			return renderFAIL("0380", response, header);
		}
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
		// 获得上传的文件
		File file = new File(PathKit.getWebRootPath() + fileInfo.getTargetUrl());
		List<Record> studentList = null;
		try {
			studentList = SpringContextHolder.getBean(ExcelHelper.class).importToRecordList("eduClassStudentExcel", file);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(e.getMessage());
			}
			return renderFAIL("0088", response, header);
		}
		// 上传文件内容为空过滤
		if (studentList.isEmpty()) {
			return renderFAIL("0441", response, header);
		}
		Map<String, Object> result = Maps.newHashMap();
		if (!validateImportList(studentList, schoolId, eduClassId)) {
			// 返回结果
			List<Map<String, Object>> errorList = Lists.newArrayList();
			for (Record record : studentList) {
				Map<String, Object> object = Maps.newHashMap();
				object.put("name", record.getStr("name"));
				object.put("studentNo", record.getStr("student_no"));
				object.put("className", record.getStr("class_name"));
				object.put("eduClassName", record.getStr("edu_class_name"));
				object.put("reasons", record.get("reasons"));
				object.put("isError", record.getBoolean("isError"));
				errorList.add(object);
			}
			result.put("isSuccess", Boolean.FALSE);
			result.put("failRecords", errorList);
		} else {
			boolean isSuccess = saveStudents(studentList, schoolId, eduClassId);
			
			result.put("isSuccess", isSuccess);
		}
		return renderSUC(result, response, header);
	}
	
	/**
	 * 验证导入列表
	 * 
	 * @param list
	 * @param schoolId
	 * @return
	 */
	private boolean validateImportList(List<Record> list, Long schoolId, Long eduClassId) {
		// 验证数据需要数据结构准备
		List<Record> errorList = Lists.newArrayList();
		// 验证excel中学号是否重复数据结构准备
		List<String> studentNoInExcel = Lists.newArrayList();
		List<Record> studentList = Lists.newArrayList();
		for (Record record : list) {
			String studentNo = record.getStr("student_no");
			record.set("reasons", new ArrayList<String>());
			if (!studentNoInExcel.contains(studentNo)) {
				studentNoInExcel.add(studentNo);
				studentList.add(record);
			} else {
				List<String> reasons = record.get("reasons");
				reasons.add("学生学号在excel中有重复，请检查excel");
				record.set("isError", Boolean.TRUE);
				errorList.add(record);
			}
			record.set("isError", Boolean.FALSE);
		}
		// 查询学生是否存在与数据库中
		List<String> studentNoInDB = Lists.newArrayList();
		List<CcStudent> ccStudents = CcStudent.dao.findByStudentsNo(studentNoInExcel.toArray(new String[studentNoInExcel.size()]), schoolId);
		for (CcStudent ccStudent : ccStudents) {
			studentNoInDB.add(ccStudent.getStr("student_no"));
		}
		for (Record record : studentList) {
			if (!studentNoInDB.contains(record.getStr("student_no"))) {
				List<String> reasons = record.get("reasons");
				reasons.add("学生学号不存在与系统中，请检查excel");
				record.set("isError", Boolean.TRUE);
				errorList.add(record);
			}
		}
		// 查询学生是否已经存在于该教学班中
		List<CcEduclassStudent> ccEduclassStudents = CcEduclassStudent.dao.findStuInfoByClassId(eduClassId);
		studentNoInDB.clear();
		for (CcEduclassStudent ccEduclassStudent : ccEduclassStudents) {
			studentNoInDB.add(ccEduclassStudent.getStr("student_no"));
		}
		for (Record record : studentList) {
			if (studentNoInDB.contains(record.getStr("student_no"))) {
				List<String> reasons = record.get("reasons");
				reasons.add("学生学号已存在该教学班中，请检查excel");
				record.set("isError", Boolean.TRUE);
				errorList.add(record);
			}
		}

		return errorList.isEmpty();
	}
	
	/**
	 * 教学班学生保存
	 * 
	 * @param list
	 * @param schoolId
	 * @param eduClassId
	 * @return
	 */
	private boolean saveStudents(List<Record> list, Long schoolId, Long eduClassId) {
		Date date = new Date();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		String[] studentNos = new String[list.size()];
		for (int i = 0; i < list.size(); i ++) {
			studentNos[i] = list.get(i).getStr("student_no");
		}
		List<CcStudent> ccStudents = CcStudent.dao.findByStudentsNo(studentNos, schoolId);
		List<CcEduclassStudent> ccEduclassStudents = Lists.newArrayList();
		for (CcStudent ccStudent : ccStudents) {
			CcEduclassStudent ccEduclassStudent = new CcEduclassStudent();
			ccEduclassStudent.set("id", idGenerate.getNextValue());
			ccEduclassStudent.set("create_date", date);
			ccEduclassStudent.set("modify_date", date);
			ccEduclassStudent.set("student_id", ccStudent.getLong("id"));
			ccEduclassStudent.set("class_id", eduClassId);
			ccEduclassStudents.add(ccEduclassStudent);
		}
		
		return CcEduclassStudent.dao.batchSave(ccEduclassStudents);
		
	}

}
