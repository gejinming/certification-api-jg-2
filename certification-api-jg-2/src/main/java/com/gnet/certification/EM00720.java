package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseOutline;
import com.gnet.model.admin.Role;
import com.gnet.model.admin.RolePermission;
import com.gnet.model.admin.UserRole;
import com.gnet.service.CcCourseOutlineService;
import com.gnet.utils.SpringContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 负责人废弃大纲
 * 
 * @author xzl
 * 
 * @date 2017年8月30日18:10:02
 *
 */
@Service("EM00720")
@Transactional(readOnly=false)
public class EM00720 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Map<String,Object> result = new HashMap<String, Object>();
		
		List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		if (ids == null || ids.isEmpty()) {
			return renderFAIL("0531", response, header);
		}
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		Date date = new Date();
		if(!CcCourseOutline.dao.deleteAll(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		CcCourseOutlineService ccCourseOutlineService = SpringContextHolder.getBean(CcCourseOutlineService.class);
		if(!ccCourseOutlineService.deleteCourseOutline(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}
