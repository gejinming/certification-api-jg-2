package com.gnet.certification;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcTeacherCourse;
import com.gnet.model.admin.CcTerm;

/**
 * 批量删除学期表
 * 
 * @author sll
 * 
 * @date 2016年07月03日 17:31:09
 *
 */
@Service("EM00354")
@Transactional(readOnly=false)
public class EM00354 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String,Boolean> result = new HashMap<>();
		
		Date date = new Date();
		
		List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		// 判断是否还教师课程表在使用
		List<CcTeacherCourse> ccTeacherCourses = CcTeacherCourse.dao.findFilteredByColumnIn("term_id", idsArray);
		if(!ccTeacherCourses.isEmpty()) {
			return renderFAIL("0399", response, header);
		}
		
		// 删除CcTerm
		if(!CcTerm.dao.deleteAll(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}	
	
