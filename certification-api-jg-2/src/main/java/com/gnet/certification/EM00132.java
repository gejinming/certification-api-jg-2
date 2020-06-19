package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.*;
import com.gnet.service.CcCourseOutlineTypeService;
import com.gnet.utils.RandomUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.druid.util.StringUtils;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.plugin.configLoader.ConfigUtils;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.service.UserService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;

/**
 * 增加学校
 * 
 * @author zsf
 * 
 * @date 2016年06月25日 18:39:35
 *
 */
@Service("EM00132")
@Transactional(readOnly=false)
public class EM00132 extends BaseApi implements IApi{
	
	private static final Logger logger  = Logger.getLogger(EM00132.class);

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		String name = paramsStringFilter(param.get("name"));
		String description = paramsStringFilter(param.get("description"));
		String loginName = paramsStringFilter(param.get("loginName"));
		String password = paramsStringFilter(param.get("password"));
		
		if(StrKit.isBlank(loginName)){
			return renderFAIL("0208", response, header);
		}
		
		if (StringUtils.isEmpty(name)) {
			return renderFAIL("0202", response, header);
		}

		if (School.dao.isExisted(name)) {
			return renderFAIL("0204", response, header);
		}

		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		Long userId = idGenerate.getNextValue();
	    User user = new User();
	    user.set("id", userId);
		
		Date date = new Date();
		
		School school = new School();
		school.set("create_date", date);
		school.set("modify_date", date);
		school.set("admin_id", userId);
		school.set("is_del", Boolean.FALSE);
		
		Boolean isSuccess = school.save();
		Long schoolId = school.getLong("id");
		Map<String, Object> result = new HashMap<>();
		
		if(!isSuccess) {
			result.put("isSuccess", Boolean.FALSE);
			return renderSUC(result, response, header);
		}

		CcCourseOutlineTypeService ccCourseOutlineTypeService = SpringContextHolder.getBean(CcCourseOutlineTypeService.class);
		if(!ccCourseOutlineTypeService.saveOutlineType(CcCourseOutlineType.NAME, schoolId)){
			result.put("isSuccess", Boolean.FALSE);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return renderSUC(result, response, header);
		}

		Office office = new Office();
		office.set("id", schoolId);
		office.set("create_date", date);
		office.set("modify_date", date);
		office.set("type", Office.TYPE_SCHOOL);
		office.set("code", RandomUtils.randomString(Office.TYPE_SCHOOL));
		office.set("name", name);
		office.set("parentid", 0l);
		office.set("is_system", Boolean.FALSE);
		office.set("is_del", Boolean.FALSE);
		office.set("description", description);
		
		isSuccess = office.save();
		
		if(!isSuccess) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", Boolean.FALSE);
			
			return renderSUC(result, response, header);
		}
		
		//保存学校对应的部门路径
		OfficePath path = new OfficePath();
		path.set("id", office.getLong("id"));
		path.set("create_date", date);
		path.set("modify_date", date);
		path.set("office_ids", "," + office.getLong("id") + ",");
		
		isSuccess = path.save();
		
		if(!isSuccess) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", Boolean.FALSE);
			
			return renderSUC(result, response, header);
		}
		
		
		if(StrKit.isBlank(password)){
			password = User.DEFAULT_PASSWORD;
		}
		if (User.dao.isExisted(schoolId + "-" + loginName)) {
			return renderFAIL("0016", response, header);
		}
		Long roleId = ConfigUtils.getLong("global", "role.school_admin_id");
		if(roleId == null){
			logger.error("需要在global文件中初始化学校管理员角色 role.school_admin_id 的值");
			return renderFAIL("0209", response, header);
		}
		List<Long> roleIds = Lists.newArrayList();
		roleIds.add(roleId);
		UserService userService = SpringContextHolder.getBean(UserService.class);
		isSuccess = userService.save(user, loginName, password, String.valueOf(schoolId), roleIds, name + "管理员", true, null, schoolId);
		if(!isSuccess) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", Boolean.FALSE);
			
			return renderSUC(result, response, header);
		}
		
		result.put("isSuccess", isSuccess);
		
		return renderSUC(result, response, header);
	}
}
