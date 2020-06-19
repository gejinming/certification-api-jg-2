package com.gnet.certification;

import com.gnet.FileInfo;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.gnet.plugin.configLoader.ConfigUtils;
import com.gnet.plugin.poi.ExcelHelper;
import com.gnet.service.CcEduclassStudentService;
import com.gnet.service.FileService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
import java.util.*;


/**
 * 教学班学生excel上传解析接口
 * 
 * @author xzl
 * @Date 2017年10月8日
 */
@Transactional(readOnly = false)
@Service("EM00761")
public class EM00761 extends BaseApi implements IApi {

	private static final Logger logger = Logger.getLogger(EM00761.class);

	@Autowired
	private CcEduclassStudentService ccEduclassStudentService;
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Long appointSchoolId = paramsLongFilter(param.get("appointSchoolId"));
		
		Object fileInfoObject = param.get("fileInfo");
		String token = request.getHeader().getToken();
		Long schoolId = UserCacheKit.getSchoolId(token);

		// 优先选择指定学校编号
		if (appointSchoolId != null) {
			schoolId = appointSchoolId;
		}
		// 学校不存在过滤
		if (schoolId == null) {
			return renderFAIL("0084", response, header);
		}

		//导入教学班学生时是否需要验证学生是否已录入系统
		Boolean isValidateImportStudent = ConfigUtils.getBoolean("global", "isValidateImportStudent");
		if(isValidateImportStudent == null){
			return renderFAIL("0954",response, header);
		}

		//学校下的学生
		List<CcStudent> ccStudents = CcStudent.dao.findBySchoolId(schoolId);
		if(isValidateImportStudent && ccStudents.isEmpty()){
			return renderFAIL("0950", response, header);
		}
		//学校下的专业
		List<CcMajor> ccMajors = CcMajor.dao.findBySchoolId(schoolId);
		if(ccMajors.isEmpty()){
			return renderFAIL("0951", response, header);
		}
		//学校下的行政班
		List<CcClass> ccClasses = CcClass.dao.getClassBySchool(schoolId);
		//学校下的所有的教师
		List<CcTeacher> ccTeachers = CcTeacher.dao.findFilteredByColumn("school_id", schoolId);
		if(ccTeachers.isEmpty()){
			return renderFAIL("0952", response, header);
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
		List<Record> students;
		try {
			students = SpringContextHolder.getBean(ExcelHelper.class).importToRecordList("studentEduClassExcel", file);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error(e.getMessage());
			}
			return renderFAIL("0088", response, header);
		}
		// 上传文件内容为空过滤
		if (students.isEmpty()) {
			return renderFAIL("0336", response, header);
		}

		//还未建立教学班需要加入系统的学生
		Map<String, List<CcStudent>> studentListMap = new HashMap<>();
		//已经建立教学班需要加入系统的学生
		List<CcStudent> ccStudentList = Lists.newArrayList();
		//系统中已经存在教学班，直接增加学生和教学班关联
        List<CcEduclassStudent> classStudents = Lists.newArrayList();
		//系统中已经存在排课但是还未建立教学班，先建立教学班在增加关联
        Map<String, List<CcEduclassStudent>> classStudentMap = new HashMap<>();
        //教师开课课程编号+教学班姓名的字符串
		Set<String> teacherCourseAndClassNames = new HashSet<>();
		//key为专业名称value为专业编号
		Map<String, Long> majorMap = new HashMap<>();

        // 验证导入数据的唯一性和合法性
        ccEduclassStudentService.validateImportList(students, ccStudents, ccMajors, ccClasses, ccTeachers, classStudents, classStudentMap, teacherCourseAndClassNames, isValidateImportStudent, ccStudentList, studentListMap, majorMap);
		Map<String, Object> result = Maps.newHashMap();
		List<Map<String, Object>> returnStudentList = Lists.newArrayList();
		// 返回结果
		for (Record student : students) {
			Map<String, Object> map = Maps.newHashMap();
            map.put("courseMajor", student.getStr("courseMajor"));
            map.put("grade", student.getStr("grade"));
            map.put("teacherNo", student.getStr("teacherNo"));
            map.put("courseCode", student.getStr("courseCode"));
            map.put("term", student.getStr("term"));
            map.put("eduClassName", student.getStr("eduClassName"));
            map.put("type", student.getStr("type"));
            map.put("studentNo", student.getStr("studentNo"));
            map.put("studentName", student.getStr("studentName"));
            map.put("major", student.getStr("major"));
            map.put("classes", student.getStr("classes"));
			map.put("reasons", student.get("reasons"));
			map.put("isError", student.getBoolean("isError"));
			returnStudentList.add(map);
		}
		
		result.put("returnStudentList", returnStudentList);
		
		return renderSUC(result, response, header);
	}

}