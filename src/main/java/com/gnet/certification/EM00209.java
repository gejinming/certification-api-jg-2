package com.gnet.certification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcClass;
import com.gnet.model.admin.CcStudent;
import com.gnet.model.admin.Office;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.response.ServiceResponse;
import com.gnet.service.CcStudentService;
import com.gnet.utils.DateUtil;
import com.gnet.utils.DictUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;

/**
 * 批量保存学生信息
 * 
 * @author wct
 * @date 2016年11月24日
 */
@Transactional(readOnly = false)
@Service("EM00209")
public class EM00209 extends BaseApi implements IApi{
	
	@Autowired
	private CcStudentService ccStudentService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> params = request.getData();
		
		List<HashMap> studentsMap = paramsJSONArrayFilter(params.get("students"), HashMap.class);
		
		Long appointSchoolId = paramsLongFilter(params.get("appointSchoolId"));
		
		String token = request.getHeader().getToken();
		Long schoolId = UserCacheKit.getSchoolId(token);
		Office office = UserCacheKit.getDepartmentOffice(token);
		
		// 优先选择指定学校编号
		if (appointSchoolId != null) {
			schoolId = appointSchoolId;
		}
		// 学校不存在过滤
		if (schoolId == null) {
			return renderFAIL("0084", response, header);
		}
		
		if (studentsMap.isEmpty()) {
			return renderFAIL("0336", response, header);
		}
		
		List<Record> students = Lists.newArrayList();
		for (Map<String, String> ccStudent : studentsMap) {
			Record record = new Record();
			record.set("name", ccStudent.get("name"));
			record.set("studentNo", ccStudent.get("studentNo"));
			record.set("sex", ccStudent.get("sex"));
			record.set("idCard", ccStudent.get("idCard"));
			record.set("className", ccStudent.get("className"));
			record.set("matriculateDate", ccStudent.get("matriculateDate"));
			record.set("status", ccStudent.get("status"));
			students.add(record);
		}
		
		Map<String, Object> result = Maps.newHashMap();
		if (!ccStudentService.validateImportList(students, schoolId, office.getLong("id"))) {
			// 返回结果
			List<Map<String, Object>> errorList = Lists.newArrayList();
			for (Record record : students) {
				Map<String, Object> object = Maps.newHashMap();
				String sex = StrKit.isBlank(record.getStr("sex")) ? null : DictUtils.findLabelByTypeAndKey("sex", Integer.parseInt(record.getStr("sex")));
				String status = StrKit.isBlank(record.getStr("status")) ? null : DictUtils.findLabelByTypeAndKey("studentStatue", Integer.parseInt(record.getStr("status")));
				object.put("name", record.getStr("name"));
				object.put("studentNo", record.getStr("studentNo"));
				object.put("sex", record.getStr("status"));
				object.put("sexName", sex);
				object.put("idCard", record.getStr("idCard"));
				object.put("className", record.getStr("className"));
				object.put("matriculateDate", record.getStr("matriculateDate"));
				object.put("status", record.getStr("status"));
				object.put("statusName", status);
				object.put("reasons", record.get("reasons"));
				object.put("isError", record.getBoolean("isError"));
				errorList.add(object);
			}
			result.put("isSuccess", Boolean.FALSE);
			result.put("failRecords", errorList);
		} else {
			// 返回结果
		    if(!isSaveStudentsSuccess(students, schoolId).isSucc()){
		    	return renderFAIL("0305", response, header, isSaveStudentsSuccess(students, schoolId).getContent());
		    }
		    result.put("isSuccess", true);
		}
		
		return renderSUC(result, response, header);
	}
	
	/**
	 * 增加学生信息
	 * 
	 * @param list
	 * @return
	 */
	private ServiceResponse isSaveStudentsSuccess(List<Record> list, Long schoolId) {
		Date date = new Date();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		
		// 根据班级名称获得班级编号
		List<String> classNames = Lists.newArrayList();
		for (Record record : list) {
			String className = record.getStr("className");
			if (StrKit.notBlank(className) && !classNames.contains(className)) {
				classNames.add(className);
			}
		}
		List<CcClass> ccClasses = CcClass.dao.getClassByNames(classNames.toArray(new String[classNames.size()]), schoolId);
		Map<String, Long> classNameToId = Maps.newHashMap();
		Map<String, Integer> classNameToYear = Maps.newHashMap();
		for (CcClass ccClass : ccClasses) {
			classNameToId.put(ccClass.getStr("name"), ccClass.getLong("id"));
			classNameToYear.put(ccClass.getStr("name"), ccClass.getInt("grade"));
		}
		// 根据学生信息添加
		List<CcStudent> ccStudents = Lists.newArrayList();
		for (Record record : list) {
			CcStudent ccStudent = new CcStudent();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date matriculateDate = null;
			try {
				matriculateDate = sdf.parse(record.getStr("matriculateDate"));
			} catch (ParseException e) {
				e.printStackTrace();
				return ServiceResponse.error("学号为" +record.getStr("studentNo")+"学生的入学日期格式不正确");
			}	
			ccStudent.set("id", idGenerate.getNextValue());
			ccStudent.set("create_date", date);
			ccStudent.set("modify_date", date);
			ccStudent.set("student_no", record.getStr("studentNo"));
			ccStudent.set("name", record.getStr("name"));
			ccStudent.set("sex", Integer.parseInt(record.getStr("sex")));
			ccStudent.set("id_card", record.getStr("idCard"));
			ccStudent.set("statue", Integer.parseInt(record.getStr("status")));
			ccStudent.set("matriculate_date", matriculateDate);
			ccStudent.set("grade", DateUtil.getYear(ccStudent.getDate("matriculate_date")));
			ccStudent.set(CcStudent.IS_DEL_LABEL, CcStudent.DEL_NO);
			if (StrKit.notBlank(record.getStr("className"))) {
				ccStudent.set("class_id", classNameToId.get(record.getStr("className")));
				if(!classNameToYear.get(record.getStr("className")).equals(DateUtil.getYear(matriculateDate))){
					return ServiceResponse.error("学号为" +record.getStr("studentNo")+"学生的入学年级" +classNameToYear.get(record.getStr("className") + "和行政班"+record.getStr("className")+"的年级"+classNameToYear.get(record.getStr("className"))+"不一致"));
				}
				
			}
			ccStudents.add(ccStudent);
		}
		if(!CcStudent.dao.batchSave(ccStudents)){
			return ServiceResponse.error("学生批量加入行政班失败");
		}
		return ServiceResponse.succ(true);
	}

}
