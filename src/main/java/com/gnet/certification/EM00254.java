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
import com.gnet.model.admin.CcClass;
import com.gnet.model.admin.CcStudent;
import com.gnet.service.OfficeService;
import com.gnet.utils.SpringContextHolder;

/**
 * 批量删除行政班
 * 
 * @author sll
 * 
 * @date 2016年06月29日 17:46:25
 *
 */
@Service("EM00254")
@Transactional(readOnly=false)
public class EM00254 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String, Object> result = new HashMap<>();
		
		Date date = new Date();
		
		List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		// 判断是否还有学生表在使用
		List<CcStudent> ccStudents = CcStudent.dao.findFilteredByColumnIn("class_id", idsArray);
		if(!ccStudents.isEmpty()) {
			return renderFAIL("0301", response, header);
		}
		 		
		// 删除CcClass
		if(!CcClass.dao.deleteAll(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		//删除Office和officePath
		OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
		if(!officeService.delete(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}	
	
