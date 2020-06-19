package com.gnet.certification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.object.CcGraduateOrderType;
import com.gnet.object.RoleOrderType;
import com.gnet.utils.ParamSceneUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.Permission;
import com.gnet.model.admin.Role;
import com.gnet.pager.Pageable;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;


/**
 * 查看角色列表
 * 
 * @author sll
 * 
 * @date 2016年6月6日15:11:59
 *
 */
@Service("EM00008")
@Transactional(readOnly=true)
public class EM00008 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		String name = paramsStringFilter(param.get("name"));
		Pageable pageable = new Pageable(pageNumber, pageSize);


		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, RoleOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}



		Map<String, Object> rolesMap = Maps.newHashMap();
		Page<Role> rolePage = Role.dao.page(pageable, name);
		List<Role> roleList = rolePage.getList();
		
		// 判断是否分页
		if(pageable.isPaging()){
			rolesMap.put("totalRow", rolePage.getTotalRow());
			rolesMap.put("totalPage", rolePage.getTotalPage());
			rolesMap.put("pageSize", rolePage.getPageSize());
			rolesMap.put("pageNumber", rolePage.getPageNumber());
		}
	
		List<Role> list = new ArrayList<Role>();
		Map<String, Permission> permissionsMap = getPermissionsMap();
		for (Role temp: roleList) {
			Role role = new Role();
			role.put("id", temp.get("id"));
			role.put("name", temp.get("name"));
			role.put("isSystem", temp.get("is_system"));
			role.put("description", temp.get("description"));
			role.put("createDate", temp.get("create_date"));
			role.put("permissionCodes", temp.getStr("permissions"));
			// 权限信息获取
			JSONArray permissions = new JSONArray();
			if (StrKit.notBlank(temp.getStr("permissions"))) {
				for (String code : temp.getStr("permissions").split(",")) {
					JSONObject permissionInfo = new JSONObject();
					Permission permission = permissionsMap.get(code);
					if (permission != null) {
						permissionInfo.put("id", permission.getLong("id"));
						permissionInfo.put("code", code);
						permissionInfo.put("name", permission.getStr("name"));
						permissions.add(permissionInfo);
					}
				}
			}
			role.put("permissions", permissions);
			list.add(role);
		}
		
		rolesMap.put("list", list);
		
		return renderSUC(rolesMap, response, header);
	}
		
	/**
	 * 获得所有权限表
	 * 
	 * @return
	 */
	private Map<String, Permission> getPermissionsMap() {
		Map<String, Permission> result = Maps.newHashMap();
		List<Permission> permissions = Permission.dao.findAll();
		for (Permission permission : permissions) {
			result.put(permission.getStr("code"), permission);
		}
		
		return result;
	}

}
