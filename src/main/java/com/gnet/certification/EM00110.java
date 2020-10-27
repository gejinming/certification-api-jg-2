package com.gnet.certification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.CcCourse;
import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcTeacher;
import com.gnet.model.admin.Office;
import com.gnet.object.CcTeacherOrderType;
import com.gnet.pager.Pageable;
import com.gnet.service.UserService;
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看教师表信息列表
 * 
 * @author SY
 * @Date 2016年6月23日10:21:33
 */
@Service("EM00110")
public class EM00110 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		//通过token获取专业编号
		String token = request.getHeader().getToken();
		Boolean isQuerySchoolTeacher = paramsBooleanFilter(params.get("isQuerySchoolTeacher"));
		Long courseId = paramsLongFilter(params.get("courseId"));
		Office departmentOffice = null;
		if(isQuerySchoolTeacher != null && isQuerySchoolTeacher){
			departmentOffice = UserCacheKit.getSchool(token);
		}else{
			departmentOffice = UserCacheKit.getDepartmentOffice(token);
		}

		String roleId = paramsStringFilter(params.get("roleId"));
		Integer pageNumber = paramsIntegerFilter(params.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(params.get("pageSize"));
		String orderProperty = paramsStringFilter(params.get("orderProperty"));
		String orderDirection = paramsStringFilter(params.get("orderDirection"));
		//教务员排课的时候筛选为专业负责人
		if (courseId != null){
			CcCourse byCourseId = CcCourse.dao.findByCourseId(courseId);
			//如果是毕业设计和工程实习类型的，则上课教师只能是专业负责人
			if (byCourseId !=null){
				Integer courseType = byCourseId.getInt("course_type");
				if (courseType !=null && (courseType ==2 || courseType ==3) ){
					roleId="162168";
				}
			}
		}
		// 查询字段
		Long majorId = paramsLongFilter(params.get("majorId"));
		if(params.containsKey("majorId") && majorId == null){
			return renderFAIL("1009", response, header, "majorId的参数值非法");
		}
		String majorName = paramsStringFilter(params.get("majorName"));
		String name = paramsStringFilter(params.get("name"));
		String code = paramsStringFilter(params.get("code"));
		String department = paramsStringFilter(params.get("department"));
		// User 查询字段
		String loginName = paramsStringFilter(params.get("loginName"));
		String email = paramsStringFilter(params.get("email"));
		//用于搜索不在某版本中的所有教师
		Long versionId = paramsLongFilter(params.get("versionId"));
		if(params.containsKey("versionId") && versionId == null){
			return renderFAIL("1009", response, header, "versionId的参数值非法");
		}
		
		Pageable pageable = new Pageable(pageNumber, pageSize);
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcTeacherOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		// 获取整个列表
		Page<CcTeacher> majorPage = CcTeacher.dao.page(pageable, majorId, name, loginName, email, majorName, code, department, versionId, departmentOffice, roleId);
		List<CcTeacher> ccTeacherList = majorPage.getList();
		Map<String, Object> returnMap = new HashMap<>();
		// 判断是否分页
		if (pageable.isPaging()) {
			// 分页信息返回
			returnMap.put("totalRow", majorPage.getTotalRow());
			returnMap.put("totalPage", majorPage.getTotalPage());
			returnMap.put("pageSize", majorPage.getPageSize());
			returnMap.put("pageNumber", majorPage.getPageNumber());
		}

		// 返回内容过滤
		List<Map<String, Object>> list = Lists.newArrayList();
		UserService userService = SpringContextHolder.getBean(UserService.class);
		for (CcTeacher temp : ccTeacherList) {
			Map<String, Object> teacher = Maps.newHashMap();
			
			teacher.put("id", temp.get("id"));
			teacher.put("createDate", temp.get("create_date"));
			teacher.put("modifyDate", temp.get("modify_date"));
			teacher.put("code", temp.get("code"));
			teacher.put("name", temp.get("name"));
			teacher.put("sex", temp.get("sex"));
			teacher.put("sexName", DictUtils.findLabelByTypeAndKey("sex", temp.getInt("sex")));
			teacher.put("birthday", temp.get("birthday"));
			teacher.put("nativePlace", temp.get("native_place"));
			teacher.put("nation", temp.get("nation"));
			teacher.put("politics", temp.get("politics"));
			teacher.put("country", temp.get("country"));
			teacher.put("idCard", temp.get("id_card"));
			teacher.put("highestEducation", temp.get("highest_education"));
			teacher.put("highestDegrees", temp.get("highest_degrees"));
			teacher.put("highestDegreesName", DictUtils.findLabelByTypeAndKey("highestDegrees", temp.getInt("highest_degrees")));
			teacher.put("bachelorSchool", temp.get("bachelor_school"));
			teacher.put("bachelorMajor", temp.get("bachelor_major"));
			teacher.put("masterSchool", temp.get("master_school"));
			teacher.put("masterMajor", temp.get("master_major"));
			teacher.put("doctorateSchool", temp.get("doctorate_school"));
			teacher.put("doctorateMajor", temp.get("doctorate_major"));
			teacher.put("comeSchoolTime", temp.get("come_school_time"));
			teacher.put("startEducationYear", temp.get("start_education_year"));
			teacher.put("jobTitle", temp.get("job_title"));
			teacher.put("jobTitleName", DictUtils.findLabelByTypeAndKey("jobTitle", temp.getInt("job_title")));
			teacher.put("administrative", temp.get("administrative"));
			teacher.put("mobilePhone", temp.get("mobile_phone"));
			teacher.put("mobilePhoneSec", temp.get("mobile_phone_sec"));
			teacher.put("officePhone", temp.get("office_phone"));
			teacher.put("officePhoneSec", temp.get("office_phone_sec"));
			teacher.put("qq", temp.get("qq"));
			teacher.put("wechat", temp.get("wechat"));
			teacher.put("email", temp.get("email"));
			teacher.put("officeAddress", temp.get("office_address"));
			teacher.put("photo", temp.get("photo"));
			teacher.put("isLeave", temp.get("is_leave"));
			teacher.put("majorId", temp.get("major_id"));
			teacher.put("majorName", temp.get("major_name"));
			teacher.put("instituteId", temp.get("institute_id"));
			teacher.put("instituteName", temp.get("institute_name"));
			teacher.put("schoolId", temp.get("school_id"));
			teacher.put("schoolName", temp.get("schoolName"));
			teacher.put("versionId", temp.get("version_id"));
			// user
			teacher.put("departmentId", temp.get("department"));
			teacher.put("departmentName", temp.get("department_name"));
			teacher.put("loginName", UserService.handleLoginName(temp.getStr("loginName")));
			teacher.put("type", temp.getInt("type"));
			teacher.put("roles", temp.getStr("roles"));
			teacher.put("typeName", DictUtils.findLabelByTypeAndKey("type", temp.getInt("type")));
			teacher.put("roles", userService.getUserRoles(temp.getStr("roles"), temp.getStr("name"), temp.getLong("id")));
			list.add(teacher);
		}
		
		returnMap.put("list", list);
		
		// 结果返回
		return renderSUC(returnMap, response, header);
	}

	


}
