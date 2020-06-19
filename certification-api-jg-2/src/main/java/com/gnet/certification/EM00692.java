package com.gnet.certification;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 根据专业编号或学院编号获取教师列表
 * 
 * @author xzl
 * @Date 2017年1月10日
 */
@Service("EM00692")
public class EM00692 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Long majorId = paramsLongFilter(param.get("majorId"));
		if(param.containsKey("majorId") && majorId == null) {
		    return renderFAIL("1009", response, header, "majorId的参数值非法");
		}
		Long instituteId = paramsLongFilter(param.get("instituteId"));
		if(param.containsKey("instituteId") && instituteId == null) {
		    return renderFAIL("1009", response, header, "instituteId的参数值非法");
		}
	
		String name = paramsStringFilter(param.get("name"));
		String code = paramsStringFilter(param.get("code"));
		
		String token = request.getHeader().getToken();
		Office departmentOffice = UserCacheKit.getDepartmentOffice(token);
		Long officeId = null;
		
		if(majorId == null && instituteId == null){
			// 如果都为空， 找到部门id，放到查询条件中去
			officeId = departmentOffice.getLong("id");
		}
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
			
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
		Page<CcTeacher> majorPage = CcTeacher.dao.page(pageable, majorId, instituteId, officeId, code, name);
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
		for (CcTeacher temp : ccTeacherList) {
			
			Map<String, Object> teacher = Maps.newHashMap();
			teacher.put("id", temp.getLong("id"));
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
			teacher.put("majorName", temp.get("majorName"));
			teacher.put("instituteId", temp.get("institute_id"));
			teacher.put("instituteName", temp.get("instituteName"));
			teacher.put("schoolId", temp.get("school_id"));
			list.add(teacher);
			
		}
		
		returnMap.put("list", list);
		
		// 结果返回
		return renderSUC(returnMap, response, header);
	}

	


}
