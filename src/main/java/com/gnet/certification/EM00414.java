package com.gnet.certification;

import java.math.BigDecimal;
import java.util.*;

import com.gnet.model.admin.*;
import com.gnet.service.CcCourseGradecompBatchService;
import com.gnet.utils.PriceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.response.ServiceResponse;
import com.gnet.service.CcCourseGradecomposeDetailService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;

/**
 * 批量删除成绩组成元素明细表
 * 
 * @author sll
 * 
 * @date 2016年07月06日 14:37:10
 *
 */
@Service("EM00414")
@Transactional(readOnly=false)
public class EM00414 extends BaseApi implements IApi{
	@Autowired
	private CcCourseGradecompBatchService ccCourseGradecompBatchService;

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String,Boolean> result = new HashMap<>();
		
		Date date = new Date();
		
		List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		Long batchId = paramsLongFilter(param.get("batchId"));
		// 判断是否已经删除成绩组成元素明细学生关联表
		List<CcCourseGradecomposeStudetail> ccCourseGradecomposeStudetails = CcCourseGradecomposeStudetail.dao.findFilteredByColumnIn("detail_id", idsArray);
		if(!ccCourseGradecomposeStudetails.isEmpty()) {
			return renderFAIL("0456", response, header);
		}

		// 只允许单个题目的删除
		if(idsArray != null && idsArray.length > 0){
			Long courseGradeComposeDetailId = idsArray[0];
			CcCourseGradeComposeDetail courseGradeComposeDetail = CcCourseGradeComposeDetail.dao.findFilteredById(courseGradeComposeDetailId);
			if(courseGradeComposeDetail == null){
				return renderFAIL("0451", response, header);
			}
			//与该题目有关联的开课课程成绩组成元素指标点关联数据
			List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndications = CcCourseGradecomposeIndication.dao.findByCourseGradeComposeDetailId(courseGradeComposeDetailId);
			//题目删除时开课课程成绩组成编号的题目分数得到对应指标点的满分值需要更新
			for(CcCourseGradecomposeIndication ccCourseGradecomposeIndication : ccCourseGradecomposeIndications){
				BigDecimal resetScore = PriceUtils._sub(ccCourseGradecomposeIndication.getBigDecimal("max_score"), courseGradeComposeDetail.getBigDecimal("score"));
				ccCourseGradecomposeIndication.set("modify_date", date);
				ccCourseGradecomposeIndication.set("max_score", PriceUtils.isZero(resetScore) ? null : resetScore);
			}
			if(!ccCourseGradecomposeIndications.isEmpty()) {
				if (!CcCourseGradecomposeIndication.dao.batchUpdate(ccCourseGradecomposeIndications, "modify_date, max_score")) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					result.put("isSuccess", false);
					return renderSUC(result, response, header);
				}
			}
		}

		// 判断是否已经删除成绩组成元素明细指标点关联表
		List<CcCourseGradecomposeDetailIndication> ccCourseGradecomposeDetailIndications = CcCourseGradecomposeDetailIndication.dao.findFilteredByColumnIn("course_gradecompose_detail_id", idsArray);
		if(!ccCourseGradecomposeDetailIndications.isEmpty()) {
			List<Long> idList = Lists.newArrayList();
			for(CcCourseGradecomposeDetailIndication temp : ccCourseGradecomposeDetailIndications){
				idList.add(temp.getLong("id"));
			}
			if(!CcCourseGradecomposeDetailIndication.dao.deleteAll(idList.toArray(new Long[idList.size()]), date)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}

		// 删除CcCourseGradeComposeDetail
		if(!CcCourseGradeComposeDetail.dao.deleteAll(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		//更新批次的总分
		if (batchId != null){
			boolean updateBatchScoreState = ccCourseGradecompBatchService.updateBatchScore(batchId);
			if (!updateBatchScoreState){
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}

		}
		
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}	
	
