package com.gnet.certification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcTeacher;
import com.gnet.model.admin.CcVersion;
import com.gnet.model.admin.Office;
import com.gnet.object.CcTeacherOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 某个专业某个版本某个专业的还不是专业认证教师的教师列表
 * 
 * @author xzl
 * 
 * @date 2016年10月25日14:44:41
 * 
 */
@Service("EM00297")
@Transactional(readOnly=true)
public class EM00297 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		//通过token获取专业编号
		String token = request.getHeader().getToken();
		Long schoolId = UserCacheKit.getSchoolId(token);
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		Long versionId = paramsLongFilter(param.get("versionId"));
		String name = paramsStringFilter(param.get("name"));
		String code = paramsStringFilter(param.get("code"));
		Long departmentId = paramsLongFilter(param.get("departmentId"));
		
		if(schoolId == null){
			return renderFAIL("0395", response, header);
		}
		
		if(versionId == null){
			return renderFAIL("0140", response, header);
		}
		
		// 获得专业编号
		CcVersion version = CcVersion.dao.findById(versionId);
		if (version == null) {
			return renderFAIL("0740", response, header);
		}
		
		Long majorId = version.getLong("major_id");
		
		List<Long> officeIds = Lists.newArrayList();
		if (departmentId != null) {
			Office office = Office.dao.findByIdWithPath(departmentId);
			// 获得该部门下所有下级部门
			List<Office> offices = Office.dao.findUnderOfficeByPath(office.getStr("office_path"));
			for (Office childOffice : offices) {
				officeIds.add(childOffice.getLong("id"));
			}
		}
		
		// 进行分页
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcTeacherOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		Map<String, Object> ccMajorTeachersMap = Maps.newHashMap();
		// 判断是否分页
		Page<CcTeacher> ccMajorTeacherPage = CcTeacher.dao.page(pageable, versionId, schoolId, majorId, code, name, officeIds);
		List<CcTeacher> ccMajorTeacherList = ccMajorTeacherPage.getList();
		if(pageable.isPaging()){
			ccMajorTeachersMap.put("totalRow", ccMajorTeacherPage.getTotalRow());
			ccMajorTeachersMap.put("totalPage", ccMajorTeacherPage.getTotalPage());
			ccMajorTeachersMap.put("pageSize", ccMajorTeacherPage.getPageSize());
			ccMajorTeachersMap.put("pageNumber", ccMajorTeacherPage.getPageNumber());
		}

		Map<Long, Boolean> teacherMap = new HashMap<>();
		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (CcTeacher temp : ccMajorTeacherList) {
			Map<String, Object> ccMajorTeacher =  Maps.newHashMap();
			Boolean isMajorTeacher = temp.getBoolean("isMajorTeacher");
			Long teacherId = temp.getLong("id");
			ccMajorTeacher.put("id", teacherId);
			ccMajorTeacher.put("code", temp.getStr("code"));
			ccMajorTeacher.put("name", temp.getStr("name"));
			ccMajorTeacher.put("sex", temp.getInt("sex"));
			ccMajorTeacher.put("sexName", DictUtils.findLabelByTypeAndKey("sex", temp.getInt("sex")));
			ccMajorTeacher.put("majorId", temp.getLong("major_id"));
			ccMajorTeacher.put("majorName", temp.getStr("majorName"));
			ccMajorTeacher.put("instituteId", temp.getLong("institute_id"));
			ccMajorTeacher.put("instituteName", temp.getStr("instituteName"));
			ccMajorTeacher.put("birthday", temp.getDate("birthday"));
			ccMajorTeacher.put("nativePlace", temp.getStr("native_place"));
			ccMajorTeacher.put("nation", temp.getStr("nation"));
			ccMajorTeacher.put("politics", temp.getStr("politics"));
			ccMajorTeacher.put("country", temp.getStr("country"));
			ccMajorTeacher.put("idCard", temp.getStr("id_card"));
			ccMajorTeacher.put("highestEducation", temp.getStr("highest_education"));
			ccMajorTeacher.put("highestDegrees", temp.getInt("highest_degrees"));
			ccMajorTeacher.put("highestDegreesName", DictUtils.findLabelByTypeAndKey("highestDegrees", temp.getInt("highest_degrees")));
			ccMajorTeacher.put("bachelorSchool", temp.getStr("bachelor_school"));
			ccMajorTeacher.put("bachelorMajor", temp.getStr("bachelor_major"));
			ccMajorTeacher.put("masterSchool", temp.getStr("master_school"));
			ccMajorTeacher.put("masterMajor", temp.getStr("master_major"));
			ccMajorTeacher.put("doctorateSchool", temp.getStr("doctorate_school"));
			ccMajorTeacher.put("doctorateMajor", temp.getStr("doctorate_major"));
			ccMajorTeacher.put("comeSchoolTime", temp.getDate("come_school_time"));
			ccMajorTeacher.put("startEducationYear", temp.getDate("start_education_year"));
			ccMajorTeacher.put("jobTitle", temp.getInt("job_title"));
			ccMajorTeacher.put("jobTitleName", DictUtils.findLabelByTypeAndKey("jobTitle", temp.getInt("job_title")));
			ccMajorTeacher.put("administrative", temp.getStr("administrative"));
			ccMajorTeacher.put("mobilePhone", temp.getStr("mobile_phone"));
			ccMajorTeacher.put("mobilePhoneSec", temp.getStr("mobile_phone_sec"));
			ccMajorTeacher.put("officePhone", temp.getStr("office_phone"));
			ccMajorTeacher.put("officePhoneSec", temp.getStr("office_phone_sec"));
			ccMajorTeacher.put("qq", temp.getStr("qq"));
			ccMajorTeacher.put("wechat", temp.getStr("wechat"));
			ccMajorTeacher.put("email", temp.getStr("email"));
			ccMajorTeacher.put("officeAddress", temp.getStr("office_address"));
			ccMajorTeacher.put("photo", temp.getStr("photo"));
			if(isMajorTeacher == null){
				list.add(ccMajorTeacher);
			}else{
				if(isMajorTeacher){
					list.add(ccMajorTeacher);
				}
				if(teacherMap.get(teacherId) == null){
					teacherMap.put(teacherId, isMajorTeacher);
				}else{
					list.remove(ccMajorTeacher);
				}
			}
		}
		
		ccMajorTeachersMap.put("list", list);
		
		return renderSUC(ccMajorTeachersMap, response, header);
	}
}
