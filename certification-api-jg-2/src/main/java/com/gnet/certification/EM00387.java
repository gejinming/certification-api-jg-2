package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcEduclass;
import com.gnet.model.admin.CcEduclassStudent;
import com.gnet.model.admin.CcTeacherCourse;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.service.CcEduindicationStuScoreService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 教学班学生导入更新接口
 * 
 * @author sll
 *
 */
@Transactional(readOnly = false)
@Service("EM00387")
public class EM00387 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {

		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Map<String, Object> result = new HashMap<>();

		Long educlassId = paramsLongFilter(param.get("educlassId"));
		List<Long> studentIds = paramsJSONArrayFilter(param.get("studentIds"), Long.class);
		List<Long> addList = Lists.newArrayList();
		List<Long> errorList = Lists.newArrayList();
		if(null == studentIds || studentIds.size() == 0){
			return renderFAIL("0330", response, header);
		}
		if (educlassId == null) {
			return renderFAIL("0380", response, header);
		}

		//重复学生编号
		List<Long> repeatStudentIds = Lists.newArrayList();
		//教学班已经存在学生编号
		List<Long> existStudentIds = Lists.newArrayList();
		List<CcEduclassStudent> educlassStudents = CcEduclassStudent.dao.findFilteredByColumn("class_id", educlassId);

		if (!educlassStudents.isEmpty()) {
			for (CcEduclassStudent educlassStudent : educlassStudents) {
				existStudentIds.add(educlassStudent.getLong("student_id"));
			}
			for (Long studentId : studentIds) {
				if (existStudentIds.contains(studentId)) {
					repeatStudentIds.add(studentId);
				}
				if(!addList.contains(studentId)){
					addList.add(studentId);
				}else{
					errorList.add(studentId);
				}

			}
		}

		if(!errorList.isEmpty()){
			return renderFAIL("0443", response, header);
		}

		if(!repeatStudentIds.isEmpty()){
			return renderFAIL("0440", response, header);
		}

		//验证学生是否在某个们课程某个学期中已经添加
		CcEduclass educlass = CcEduclass.dao.findFilteredById(educlassId);
		if(educlass == null){
			return renderFAIL("0381", response, header);
		}

		Date date = new Date();
		educlass.set("modify_date", date);
		educlass.set("student_num_change_date", date);
		if(!educlass.update()){
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		//学生是否在某个们课程某个学期中已经添加的数据
		List<CcTeacherCourse> existTeacherCourseClassStudent = CcTeacherCourse.dao.findExistCourseClassStudent(educlassId, studentIds.toArray(new Long[studentIds.size()]));

		if(!existTeacherCourseClassStudent.isEmpty()){
			StringBuilder stringBuilder = new StringBuilder();
			for(CcTeacherCourse student : existTeacherCourseClassStudent){
				stringBuilder.append("学号为"+ student.getStr("student_no") + "的学生" + student.getStr("name") + ",");
			}
			return renderFAIL("0440", response, header, stringBuilder.append("已经在相同学期、相同课程中存在了").toString());
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
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		List<Long> educlassIdList = Lists.newArrayList();
		educlassIdList.add(educlassId);

		if (!educlassIdList.isEmpty()) {

			CcEduindicationStuScoreService ccEduindicationStuScoreService = SpringContextHolder.getBean(CcEduindicationStuScoreService.class);

			if (!ccEduindicationStuScoreService.calculate(educlassIdList, Lists.<Long>newArrayList())) {
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}

		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}
