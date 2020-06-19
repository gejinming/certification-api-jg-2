package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.CcIndicatorPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcGraduate;
import com.gnet.model.admin.CcIndication;

/**
 * 批量删除毕业要求
 * 
 * @author SY
 * 
 * @date 2016年06月24日 20:55:57
 *
 */
@Service("EM00124")
@Transactional(readOnly=false)
public class EM00124 extends BaseApi implements IApi{

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String, Boolean> result = new HashMap<>();
		
		Date date = new Date();
		List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		// 判断是否还有指标点表在使用
		List<CcIndicatorPoint> ccIndications = CcIndicatorPoint.dao.findFilteredByColumnIn("graduate_id", idsArray);
		if(!ccIndications.isEmpty()) {
			return renderFAIL("0185", response, header);
		}
		
		// 删除CcGraduate
		if(!CcGraduate.dao.deleteAll(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}	
	
