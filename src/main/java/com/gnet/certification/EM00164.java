package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 批量删除指标点
 *
 * @author xzl
 *
 * @date 2017年11月17日15:56:52
 *
 */
@Service("EM00164")
@Transactional(readOnly=false)
public class EM00164 extends BaseApi implements IApi{

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String, Boolean> result = new HashMap<>();

		Date date = new Date();
		List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		Long[] idsArray = ids.toArray(new Long[ids.size()]);

		if(ids.isEmpty()){
			return renderFAIL("0243", response, header);
		}

		// 判断是否还有指标点与课程关系表在使用
		List<CcIndicationCourse> ccIndicationCourses = CcIndicationCourse.dao.findFilteredByColumnIn("indication_id", idsArray);
		if(!ccIndicationCourses.isEmpty()) {
			return renderFAIL("0236", response, header);
		}

		// 判断是否还有开课课程成绩组成元素指标点关联表在使用
		List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndications = CcCourseGradecomposeIndication.dao.findFilteredByColumnIn("indication_id", idsArray);
		if(!ccCourseGradecomposeIndications.isEmpty()) {
			return renderFAIL("0237", response, header);
		}

		// 判断是否还有成绩组成元素明细指标点关联表在使用
		List<CcCourseGradecomposeDetailIndication> ccCourseGradecomposeDetailIndications = CcCourseGradecomposeDetailIndication.dao.findFilteredByColumnIn("indication_id", idsArray);
		if(!ccCourseGradecomposeDetailIndications.isEmpty()) {
			return renderFAIL("0238", response, header);
		}
		// 判断是否还有考评点得分层次表在使用
		List<CcEvaluteLevel> ccEvaluteLevels = CcEvaluteLevel.dao.findFilteredByColumnIn("indication_id", idsArray);
		if(!ccEvaluteLevels.isEmpty()) {
			return renderFAIL("0239", response, header);
		}
		// 判断是否还有考评点表在使用
		List<CcEvalute> ccEvalutes = CcEvalute.dao.findFilteredByColumnIn("indication_id", idsArray);
		if(!ccEvalutes.isEmpty()) {
			return renderFAIL("0240", response, header);
		}
		// 删除指标点达成度统计表
		if(!CcReportPersonalIndication.dao.deleteAllByColumn("indication_id", idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		// 删除指标点报表数据统计表
		if(!CcReportMajor.dao.deleteAllByColumn("indication_id", idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		//所有的毕业要求指点属于同一个毕业要求
		CcIndicatorPoint indication = CcIndicatorPoint.dao.findFilteredById(ids.get(0));

		// 删除CcIndication
		if(!CcIndicatorPoint.dao.deleteAll(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		//获取某个毕业要求还未删除的指标点，并且按序号从小到大排序
		List<CcIndicatorPoint> indicationList = CcIndicatorPoint.dao.findAll(indication.getLong("graduate_id"));
		if(!indicationList.isEmpty()){
			for(int i = 0; i< indicationList.size(); i++){
				indicationList.get(i).set("modify_date", date);
				indicationList.get(i).set("index_num", i + 1 );
			}

			if(!CcIndicatorPoint.dao.batchUpdate(indicationList, "modify_date, index_num")){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}

		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}
	
