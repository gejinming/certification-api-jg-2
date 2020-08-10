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
import com.gnet.model.admin.CcInstitute;
import com.gnet.model.admin.CcTeacher;
import com.gnet.model.admin.Office;

/**
 * 批量删除学院
 * 
 * @author zsf
 * 
 * @date 2016年06月26日 18:57:47
 *
 */
@Service("EM00144")
@Transactional(readOnly=false)
public class EM00144 extends BaseApi implements IApi{

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String,Boolean> result = new HashMap<String, Boolean>();
		
		Date date = new Date();
		List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		// 判断是否还有教师表在使用
		List<CcTeacher> ccTeachers = CcTeacher.dao.findFilteredByColumnIn("institute_id", idsArray);
		if(!ccTeachers.isEmpty()) {
			return renderFAIL("218", response, header);
		}
		
		// 删除学院和学院对应的部门
		if(!CcInstitute.dao.deleteAll(idsArray, date) || !Office.dao.deleteAll(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}	
	
