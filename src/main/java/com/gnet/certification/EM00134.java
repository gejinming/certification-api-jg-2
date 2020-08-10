package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;

/**
 * 批量删除School
 * 
 * @author zsf
 * 
 * @date 2016年06月25日 18:39:35
 *
 */
@Service("EM00134")
@Transactional(readOnly=false)
public class EM00134 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String,Boolean> result = new HashMap<String, Boolean>();
		
		Date date = new Date();
		List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		Long[] idsArray = ids.toArray(new Long[ids.size()]);

		//判断是否还有大纲类型再使用
		List<CcCourseOutlineType> ccCourseOutlineTypes = CcCourseOutlineType.dao.findFilteredByColumnIn("school_id", idsArray);
		if(!ccCourseOutlineTypes.isEmpty()){
			return renderFAIL("0896", response, header);
		}

		// 判断是否还有教师表在使用
		List<CcTeacher> ccTeachers = CcTeacher.dao.findFilteredByColumnIn("school_id", idsArray);
		if(!ccTeachers.isEmpty()) {
			return renderFAIL("0206", response, header);
		}
		// 判断是否还有学年学期表表在使用
		List<CcTerm> ccTerms = CcTerm.dao.findFilteredByColumnIn("school_id", idsArray);
		if(!ccTerms.isEmpty()) {
			return renderFAIL("0207", response, header);
		}
		
		// 删除School和School所对应的office
		if(!School.dao.deleteAll(idsArray, date) || !Office.dao.deleteAll(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}	
	
