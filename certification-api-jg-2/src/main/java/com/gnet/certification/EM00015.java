package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.PermissionHaveApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.Permission;
import com.gnet.model.admin.RolePermission;

/**
 * 删除权限
 * 
 * @author sll
 * 
 * @date 2016年6月6日15:17:16
 *
 */
@Service("EM00015")
@Transactional(readOnly=false)
public class EM00015 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		if (ids == null || ids.isEmpty()) {
			return renderFAIL("0027", response, header);
		}
		
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		Date date = new Date();
		// 已经存在权限于角色关联中
		if (RolePermission.dao.hasPermissions(idsArray)) {
			return renderFAIL("0026", response, header);
		}
		//是否已有接口关联
		if(PermissionHaveApi.dao.isExist(null, ids.get(0))){
			return renderFAIL("1166", response, header);
		}
		// 批量删除权限
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("isSuccess", Permission.dao.deleteAll(idsArray, date));
		
		return renderSUC(result, response, header);
	}
	
}
