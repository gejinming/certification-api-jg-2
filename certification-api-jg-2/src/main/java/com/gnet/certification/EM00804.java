package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcIndication;
import com.gnet.model.admin.CcTeacherCourse;
import com.gnet.service.CcIndicationCourseService;
import com.gnet.service.CcIndicationService;
import com.gnet.service.CcTeacherCourseService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程目标期望值修改
 * 
 * @author xzl
 * @Date 2017年12月1日
 */
@Service("EM00804")
@Transactional(readOnly=false)
public class EM00804 extends BaseApi implements IApi {

	@Autowired
	private CcIndicationService ccIndicationService;

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long id = paramsLongFilter(params.get("id"));
		BigDecimal expectedValue = paramsBigDecimalFilter(params.get("expectedValue"));
		Long eduClassId = paramsLongFilter(params.get("eduClassId"));

		Map<String, Object> result = new HashMap<>();

		if(id == null){
			return renderFAIL("1111", response, header);
		}

		if(expectedValue == null){
			return renderFAIL("1112", response, header);
		}

		if(eduClassId == null){
			return renderFAIL("0380", response, header);
		}

		CcIndication ccIndication = CcIndication.dao.findFilteredById(id);
		if(ccIndication == null){
			return renderFAIL("1113", response, header);
		}

		if(!ccIndicationService.statisticsAllForJGByEduClassId(eduClassId)){
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		ccIndication.set("modify_date", new Date());
		ccIndication.set("expected_value", expectedValue);
		if(!ccIndication.update()){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}
	
}
