package com.gnet.certification;

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
import com.gnet.model.admin.Office;
import com.gnet.service.CcTeacherService;
import com.gnet.utils.DictUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;

/**
 * 批量保存教师信息
 * 
 * @author SY
 * @date 2017年1月19日
 */
@Transactional(readOnly = false)
@Service("EM00694")
public class EM00694 extends BaseApi implements IApi{
	
	@Autowired
	private CcTeacherService ccTeacherService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> params = request.getData();
		
		List<HashMap> teachersMap = paramsJSONArrayFilter(params.get("teachers"), HashMap.class);
		
		String token = request.getHeader().getToken();
		Long schoolId = UserCacheKit.getSchoolId(token);
		Office office = UserCacheKit.getDepartmentOffice(token);
		// 学校不存在过滤
		if (schoolId == null) {
			return renderFAIL("0084", response, header);
		}
		// 部门不存在过滤
		if (office == null) {
			return renderFAIL("0057", response, header);
		}
		
		if (teachersMap.isEmpty()) {
			return renderFAIL("0336", response, header);
		}
		
		List<Record> teachers = Lists.newArrayList();
		for (Map<String, String> ccTeacher : teachersMap) {
			Record record = new Record();
			record.set("name", ccTeacher.get("name"));
			record.set("code", ccTeacher.get("code"));
			record.set("sex", ccTeacher.get("sex"));
			record.set("isEnabled", ccTeacher.get("isEnabled"));
			record.set("departmentName", ccTeacher.get("departmentName"));
			record.set("isLeave", ccTeacher.get("isLeave"));
			teachers.add(record);
		}
		
		Map<String, Object> result = Maps.newHashMap();
		if (!ccTeacherService.validateImportList(teachers, schoolId, office.getLong("id"))) {
			// 返回结果
			List<Map<String, Object>> errorList = Lists.newArrayList();
			for (Record record : teachers) {
				Map<String, Object> object = Maps.newHashMap();
				String sexName = StrKit.isBlank(record.getStr("sex")) ? null : DictUtils.findLabelByTypeAndKey("sex", Integer.parseInt(record.getStr("sex")));
				object.put("name", record.getStr("name"));
				object.put("code", record.getStr("code"));
				object.put("sex", record.getStr("sex"));
				object.put("sexName", sexName);
				object.put("isEnabled", record.getStr("isEnabled"));
				object.put("departmentName", record.getStr("departmentName"));
				object.put("departmentId", record.getLong("departmentId"));
				object.put("isLeave", record.getStr("isLeave"));
				object.put("reasons", record.get("reasons"));
				object.put("isError", record.getBoolean("isError"));
				errorList.add(object);
			}
			result.put("isSuccess", Boolean.FALSE);
			result.put("failRecords", errorList);
		} else {
			// 返回结果
			boolean isSuccess = ccTeacherService.saveTeachers(teachers, schoolId);
			result.put("isSuccess", isSuccess);
		}
		return renderSUC(result, response, header);
	}
	
}
