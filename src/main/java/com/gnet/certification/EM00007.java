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
import com.gnet.model.admin.CcTeacher;
import com.gnet.model.admin.User;
import com.gnet.model.admin.UserRole;
import com.gnet.service.UserService;
import com.gnet.utils.SpringContextHolder;

/**
 * 批量删除用户
 * 
 * @author sll
 * 
 * @date 2016年6月6日15:11:19
 *
 */
@Service("EM00007")
@Transactional(readOnly=false)
public class EM00007 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		Map<String, Boolean> result = new HashMap<>();
		Date date = new Date();
		
		List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		// 判断是否还有教师表在使用
		List<CcTeacher> ccTeachers = CcTeacher.dao.findFilteredByColumnIn("id", idsArray);
		if(!ccTeachers.isEmpty()) {
			return renderFAIL("0712", response, header);
		}
		
		// 删除用户，删除对应的角色联系
		if(!User.dao.deleteAll(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		if(!UserRole.dao.deleteAll(idsArray, date)){
	    	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	    	result.put("isSuccess", false);
	    	return renderSUC(result, response, header);
	    }
		
		
		UserService userService = SpringContextHolder.getBean(UserService.class);
		
		userService.deleteCache(idsArray);
		
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}	
	
	