package com.gnet.certification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.PermissionHaveApi;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.Permission;

/**
 * 查看权限详细
 *
 * @author sll
 *
 * @date 2016年6月7日15:08:07
 *
 */
@Service("EM00018")
@Transactional(readOnly=true)
public class EM00018 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {

		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();

		Long id = paramsLongFilter(param.get("id"));

		if (id == null) {
			return renderFAIL("0027", response, header);
		}

		Permission temp = Permission.dao.findById(id);

		Map<String, Object> permission = new HashMap<>();
		permission.put("id", temp.getLong("id"));
		permission.put("createDate", temp.getDate("create_date"));
		permission.put("modifyDate", temp.getDate("modify_date"));
		permission.put("code", temp.getStr("code"));
		permission.put("name", temp.getStr("name"));
		permission.put("pname", temp.getStr("pname"));
		permission.put("description", temp.getStr("description"));
		permission.put("isSystem", temp.getBoolean("is_system"));

		//查找权限下已关联的接口
		List<PermissionHaveApi> permissionHaveApiList = PermissionHaveApi.dao.findByPermissionId(id);
		List<Map<String, Object>> permissionHaveApis = Lists.newArrayList();
		for(PermissionHaveApi permissionHaveApi : permissionHaveApiList){
			Map<String, Object> map = new HashMap<>();
			map.put("id",  permissionHaveApi.getLong("id"));
			map.put("code",  permissionHaveApi.getStr("code"));
			map.put("name",  permissionHaveApi.getStr("name"));
			map.put("description", permissionHaveApi.getStr("description"));
			permissionHaveApis.add(map);
		}

		permission.put("permissionHaveApis", permissionHaveApis);

		return renderSUC(permission, response, header);
	}

}
