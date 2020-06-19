package com.gnet.certification;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcTeacher;
import com.gnet.model.admin.User;
import com.gnet.plugin.configLoader.ConfigUtils;
import com.gnet.service.OfficeService;
import com.gnet.service.UserService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;


/**
 * 只根据教师工号、教师姓名、所属学院/专业添加教师信息
 * 
 * @author xzl
 * @Date 2016年11月16日
 */
@Transactional
@Service("EM00679")
public class EM00679 extends BaseApi implements IApi {
	
	private static final Logger logger  = Logger.getLogger(EM00679.class);

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 各种其他信息
		String code = paramsStringFilter(params.get("code"));
		String name = paramsStringFilter(params.get("name"));
		
		// user 需要的信息 userRole 需要的信息
		String loginName = code;
		Long roleId = ConfigUtils.getLong("global", "role.teacher_id");
		if(roleId == null){
			logger.error("需要在global文件中初始化教师角色 role.teacher_id 的值");
			return renderFAIL("0169", response, header);
		}
		List<Long> roleIds = Lists.newArrayList();
		roleIds.add(roleId);
		Long departmentId = paramsLongFilter(params.get("departmentId"));
		
		Long schoolId = null;
		Long instituteId = null;
		Long majorId = null;

		if (departmentId == null) {
			return renderFAIL("0015", response, header);
		}
		OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
		List<Long> departmentIds = officeService.getPathByOfficeId(departmentId);
		
	    if(departmentIds == null){
	    	return renderFAIL("0057", response, header);
	    }
	    
	  //部门路径表中会保存对应部门的路径树，对应级数为 学校--学院--专业
		if (departmentIds.size() == 1) {
			schoolId = departmentIds.get(0);
		} else if (departmentIds.size() == 2) {
			schoolId = departmentIds.get(0);
			instituteId = departmentIds.get(1);
		} else if (departmentIds.size() >= 3) {
			schoolId = departmentIds.get(0);
			instituteId = departmentIds.get(1);
			majorId = departmentIds.get(2);
		}
		
		if (User.dao.isExisted(schoolId + "-" + loginName)) {
			return renderFAIL("0016", response, header);
		}
		
		// 为空信息的过滤
		if (schoolId == null) {
			return renderFAIL("0084", response, header);
		}
		if (StrKit.isBlank(name)) {
			return renderFAIL("0164", response, header);
		}
		if (StrKit.isBlank(code)) {
			return renderFAIL("0165", response, header);
		}
		// 结果返回
		Map<String, Object> result = Maps.newHashMap();
		
		// 保存 User and UserRole
		UserService userService = SpringContextHolder.getBean(UserService.class);
		User user = new User();
		Boolean saveResult = userService.save(user, loginName, User.DEFAULT_PASSWORD, String.valueOf(departmentId), roleIds, name, Boolean.TRUE, null, schoolId);
		if(!saveResult) {
			// 返回操作是否成功
			result.put("isSuccess", saveResult);
			return renderSUC(result, response, header);
		}
				
		Date date = new Date();
		CcTeacher ccTeacher = new CcTeacher();
		Long userId = user.getLong("id");
		ccTeacher.set("id", userId);
		ccTeacher.set("create_date", date);
		ccTeacher.set("modify_date", date);
		ccTeacher.set("code", code);
		ccTeacher.set("name", name);
		ccTeacher.set("highest_degrees", CcTeacher.HIGHESTDEGREES_TYPE);
		ccTeacher.set("job_title", CcTeacher.JOBTITLE_TYPE);
		ccTeacher.set("sex",CcTeacher.SEX_MAN);
		ccTeacher.set("major_id", majorId);
		ccTeacher.set("institute_id", instituteId);
		ccTeacher.set("school_id", schoolId);
		ccTeacher.set("is_leave", Boolean.FALSE);
		ccTeacher.set("is_del", Boolean.FALSE);
		
		// 保存这个信息
		saveResult = ccTeacher.save();
		if(!saveResult) {
			// 返回操作是否成功
			result.put("isSuccess", saveResult);
			return renderSUC(result, response, header);
		}
		
		
		if(!saveResult) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}	
		// 返回操作是否成功
		result.put("isSuccess", saveResult);
		result.put("name", name);
		result.put("code", code);
		result.put("departmentId", departmentId);
		result.put("teacherId", userId);
		return renderSUC(result, response, header);
	}
	
}
