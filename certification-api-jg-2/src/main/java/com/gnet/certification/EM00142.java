package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.gnet.utils.RandomUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcInstitute;
import com.gnet.model.admin.Office;
import com.gnet.model.admin.OfficePath;
import com.gnet.model.admin.User;
import com.gnet.service.OfficeService;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.kit.StrKit;

/**
 * 增加学院
 * 
 * @author zsf
 * 
 * @date 2016年06月26日 18:57:47
 *
 */
@Service("EM00142")
@Transactional(readOnly=false)
public class EM00142 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();

		String name = paramsStringFilter(param.get("name"));
		String description = paramsStringFilter(param.get("description"));
		String token = request.getHeader().getToken();
		
		if (StrKit.isBlank(name)) {
			return renderFAIL("0212", response, header);
		}

		User user = UserCacheKit.getUser(token);
		OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
		Office school = officeService.getSchoolByUserId(user.getLong("id"));
		Long schoolId = school.getLong("id");
		
		if (CcInstitute.dao.isExisted(schoolId, name)) {
			return renderFAIL("0214", response, header);
		}
		
		Date date = new Date();
		
		Office office = new Office();
		office.set("create_date", date);
		office.set("modify_date", date);
		office.set("parentid", schoolId);
		office.set("code", RandomUtils.randomString(Office.TYPE_BRANCH));
		office.set("name", name);
		office.set("is_del", Boolean.FALSE);
		office.set("description", description);
		office.set("type", Office.TYPE_BRANCH);
		office.set("is_system", Boolean.FALSE);
		Boolean isSuccess = office.save();
		
		Map<String, Object> result = new HashMap<>();
		
		if(!isSuccess) {
			result.put("isSuccess", Boolean.FALSE);
			return renderSUC(result, response, header);
		}
		
		CcInstitute institute = new CcInstitute();
		institute.set("id", office.get("id"));
		institute.set("create_date", date);
		institute.set("modify_date", date);
		institute.set("is_del", Boolean.FALSE);
		
		isSuccess = institute.save();
		
		if(!isSuccess) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", Boolean.FALSE);
			return renderSUC(result, response, header);
		}
		
		OfficePath officePath = new OfficePath();
		officePath.set("id", office.getLong("id"));
		officePath.set("create_date", date);
		officePath.set("modify_date", date);
		officePath.set("office_ids", "," + schoolId + ",," + office.getLong("id") + ",");
		
		isSuccess = officePath.save();
		
		if(!isSuccess) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}
}
