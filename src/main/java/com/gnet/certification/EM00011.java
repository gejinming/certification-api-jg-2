package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
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
import com.gnet.model.admin.Role;
import com.gnet.model.admin.RolePermission;
import com.gnet.model.admin.UserRole;

/**
 * 删除角色
 * 
 * @author sll
 * 
 * @date 2016年6月6日15:14:16
 *
 */
@Service("EM00011")
@Transactional(readOnly=false)
public class EM00011 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Map<String,Object> result = new HashMap<String, Object>();
		
		List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		if (ids == null || ids.isEmpty()) {
			return renderFAIL("0020", response, header);
		}
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		Date date = new Date();
		// 存在用户与角色的关联
		if (UserRole.dao.hasRoles(idsArray)) {
			return renderFAIL("0021", response, header);
		}
		// 批量删除角色,删除角色与权限的关联
		if(!RolePermission.dao.deleteAll(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		if (!Role.dao.deleteAll(idsArray, date)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}
