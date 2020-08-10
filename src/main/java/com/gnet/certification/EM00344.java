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
import com.gnet.model.admin.CcPlanTerm;
import com.gnet.model.admin.CcPlanTermCourse;

/**
 * 批量培养计划学年学期表
 * 
 * @author sll
 * 
 * @date 2016年07月04日 08:30:41
 *
 */
@Service("EM00344")
@Transactional(readOnly=false)
public class EM00344 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String,Boolean> result = new HashMap<>();
		
		Date date = new Date();
		
		List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		// 判断是否还有培养计划课程学期详情表在使用
		List<CcPlanTermCourse> ccPlanTermCourses = CcPlanTermCourse.dao.findFilteredByColumnIn("plan_term_id", idsArray);
		if(!ccPlanTermCourses.isEmpty()) {
			return renderFAIL("0349", response, header);
		}
		
		// 删除CcPlanTerm
		if(!CcPlanTerm.dao.deleteAll(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}	
	
