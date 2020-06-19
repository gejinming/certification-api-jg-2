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
import com.gnet.model.admin.CcEvaluteLevel;
import com.gnet.model.admin.CcStudentEvalute;

/**
 * 批量删除考评点得分层次关系表
 * 
 * @author sll
 * 
 * @date 2016年07月05日 18:29:45
 *
 */
@Service("EM00394")
@Transactional(readOnly=false)
@Deprecated
public class EM00394 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String,Boolean> result = new HashMap<>();
		
		Date date = new Date();
		
		List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		if (ids.isEmpty()) {
			return renderFAIL("0370", response, header);
		}
		
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		if (CcStudentEvalute.dao.isInUseEvaluteLevel(idsArray)) {
			return renderFAIL("0518", response, header);
		}
		// 删除CcEvaluteLevel
		if(!CcEvaluteLevel.dao.deleteAll(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}	
	
