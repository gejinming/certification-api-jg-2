package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.service.CcEduindicationStuScoreService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
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
import com.gnet.model.admin.CcScoreStuIndigrade;
import com.gnet.model.admin.CcStudentEvalute;

/**
 * 批量删除教学班学生
 * 
 * @author xzl
 * 
 * @date 2016年7月4日
 *
 */
@Service("EM00381")
@Transactional(readOnly=false)
public class EM00381 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String,Boolean> result = new HashMap<>();
		
		Date date = new Date();
		List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		if(ids.isEmpty()){
			result.put("isSuccess", true);
			return renderSUC(result, response, header);
		}
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		CcEduclassStudent ccEduclassStudent = CcEduclassStudent.dao.findFilteredById(idsArray[0]);
		if(ccEduclassStudent == null){
			return renderFAIL("0445", response, header);
		}
		CcEduclass ccEduclass = CcEduclass.dao.findFilteredById(ccEduclassStudent.getLong("class_id"));
		if(ccEduclass == null){
			return renderFAIL("0381", response, header);
		}
		
		// 验证是否学生的考核分析法成绩已经删除干净
		if(CcScoreStuIndigrade.dao.existScore(idsArray)) {
			return renderFAIL("0442", response, header);
		}
		
		// 验证是否学生的考评点分析法成绩已经删除干净
		if(CcStudentEvalute.dao.existScore(idsArray)){
			return renderFAIL("0444", response, header);
		}
		
		// 删除CcEduclassStudent
		if(!CcEduclassStudent.dao.deleteAll(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		ccEduclass.set("modify_date", date);
		ccEduclass.set("student_num_change_date", date);
		if(!ccEduclass.update()){
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}


		List<Long> educlassIdList = Lists.newArrayList();
		educlassIdList.add(ccEduclass.getLong("id"));

		if (!educlassIdList.isEmpty()) {

			CcEduindicationStuScoreService ccEduindicationStuScoreService = SpringContextHolder.getBean(CcEduindicationStuScoreService.class);

			if (!ccEduindicationStuScoreService.calculate(educlassIdList, Lists.<Long>newArrayList())) {
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}
		
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}	
	
