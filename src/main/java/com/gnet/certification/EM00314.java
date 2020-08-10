package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.CcEduindicationStuScore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcEduclass;
import com.gnet.model.admin.CcEduclassStudent;

/**
 * 批量删除教学班
 * 
 * @author SY
 * 
 * @date 2016年07月01日 14:41:11
 *
 */
@Service("EM00314")
@Transactional(readOnly=false)
public class EM00314 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String,Boolean> resultMap = new HashMap<>();
		
		Date date = new Date();
		
		List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		// 验证是否可以删除。
		// 教学班内是否已经不存在学生
		List<CcEduclassStudent> educlassStudents = CcEduclassStudent.dao.findFilteredByColumnIn("class_id", idsArray);
		if(!educlassStudents.isEmpty()) {
			return renderFAIL("0384", response, header);
		}
		
		// 删除CcEduclass
		if(!CcEduclass.dao.deleteAll(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			resultMap.put("isSuccess", false);
			return renderSUC(resultMap, response, header);
		}

		// 删除教学班下 cc_eduindication_stu_score 表的数据
		if (!CcEduindicationStuScore.dao.deleteAllByColumn("educlass_id", idsArray, date)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			resultMap.put("isSuccess", false);
			return renderSUC(resultMap, response, header);
		}

		resultMap.put("isSuccess", true);
		return renderSUC(resultMap, response, header);
	}

}	
	
