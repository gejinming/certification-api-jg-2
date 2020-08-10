package com.gnet.certification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.CcStudent;
import com.gnet.plugin.configLoader.ConfigUtils;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.Office;
import com.gnet.model.admin.User;
import com.gnet.model.admin.UserRole;
import com.gnet.service.UserService;
import com.gnet.utils.DictUtils;
import com.jfinal.kit.StrKit;

/**
 * 查看用户详情
 * 
 * @author sll
 * 
 * @date 2016年6月7日08:43:54
 *
 */
@Service("EM00016")
@Transactional(readOnly=true)
public class EM00016 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		String token = request.getHeader().getToken();
		//判断是学生登录还是教师登录 1教师登录，2学生登录
		Integer userRoleId = paramsIntegerFilter(param.get("userRole"));
		if (id == null) {
			return renderFAIL("0017", response, header);
		}
        if (userRoleId == null) {
            return renderFAIL("0017", response, header);
        }
        User user = new User();
        String officeIds="";
        UserRole userRole = UserRole.dao.findFirstByColumn("user_id", id);
        List<String> roleList = Lists.newArrayList();
        if (userRole != null && StrKit.notBlank(userRole.getStr("roles"))) {
            roleList = Lists.newArrayList(CollectionKit.convert(userRole.getStr("roles"), ","));
        }
        if(userRoleId==1){
            User temp = User.dao.findFilteredById(id);
            officeIds = temp.getStr("office_ids");
            user.put("id", temp.getLong("id"));
            user.put("createDate", temp.getDate("create_date"));
            user.put("modifyDate", temp.getDate("modify_date"));
            user.put("name", temp.getStr("name"));
            user.put("loginName", UserService.handleLoginName(temp.getStr("loginName")));
            user.put("type", temp.getInt("type"));
            user.put("typeName", DictUtils.findLabelByTypeAndKey("type", temp.getInt("type")));
            user.put("email", temp.getStr("email"));
            user.put("lockedDate", temp.getDate("locked_date"));
            user.put("loginDate", temp.getDate("login_date"));
            user.put("loginFailureCount", temp.getInt("login_failure_count"));
            user.put("loginIp", temp.getStr("login_ip"));
            user.put("isBindEmail", temp.getBoolean("is_bind_email"));
            user.put("idBindMobile", temp.getBoolean("is_bind_mobile"));
            user.put("isEnabled", temp.getBoolean("is_enabled"));
            user.put("idLocked", temp.getBoolean("is_locked"));
            //判断user是否是学校管理员
            user.put("isSchoolAdmin", roleList.contains(ConfigUtils.getStr("global", "role.school_admin_id")));
            user.put("isMajorDirector", roleList.contains(ConfigUtils.getStr("global", "role.major_director_id")));
        }else {
            CcStudent student = CcStudent.dao.findDetailById(id);
            if (student !=null ){
                user.put("id",student.getLong("id"));
                user.put("name",student.getStr("name"));
                user.put("loginName",student.getStr("student_no"));
                officeIds =student.getStr("office_ids");
            }
        }


		
		List<Map<String, Object>> departments = new ArrayList<>();
		List<Long> departmentIds = new ArrayList<>();
		String[] departmentArray = StringUtils.split(officeIds, ",");
		if (departmentArray != null) {
			for (String department : departmentArray) {
				departmentIds.add(Long.valueOf(department));
			}
			List<Office> offices = Office.dao.findByColumnIn("id", departmentIds.toArray(new Long[]{}));
			for (Office office : offices) {
				Map<String, Object> departmentMapping = new HashMap<>();
				departmentMapping.put("id", office.getLong("id"));
				departmentMapping.put("name", office.getStr("name"));
				departments.add(departmentMapping);
			}
			
			user.put("departments", departments);
		}
		
		if (userRole != null) {
			List<Long> roleIds = new ArrayList<Long>();
			String roles = userRole.getStr("roles");
			if (StrKit.notBlank(roles)) {
				for (String role : roles.split(",")) {
					roleIds.add(Long.valueOf(role.trim()));
				}
			}
			user.put("roleIds", roleIds);
		}
		
		List<String> permissions = UserCacheKit.getPermissions(token);
		user.put("permissions", permissions);
		
		return renderSUC(user, response, header);
	}

}
