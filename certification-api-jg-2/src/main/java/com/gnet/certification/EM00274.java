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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 批量删除指标点课程关系表
 *
 * @author xzl
 *
 * @date 2017年11月20日11:13:50
 *
 */
@Service("EM00274")
@Transactional(readOnly=false)
public class EM00274 extends BaseApi implements IApi{

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String,Boolean> result = new HashMap<>();

		Date date = new Date();

		// 强制删除的标志，是否必须通过，当存在关联表数据使用到本条数据id的时候，一起删除
		Boolean isMustPassAndDel = paramsBooleanFilter(param.get("isMustPassAndDel"));
		List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		Long[] idsArray = ids.toArray(new Long[ids.size()]);

		List<CcCourseTargetIndication> ccCourseTargetIndications = CcCourseTargetIndication.dao.findByColumnIn("indication_course_id", idsArray);
		// 记录下课程目标ID，用于接下去判断是否不再被使用。
		List<Long> indicationIds = new ArrayList<>();
		// 强制删除本条相关的：课程目标与指标点与课程关系的关系表
		if(isMustPassAndDel != null && isMustPassAndDel) {
			// 如果有数据，则删除表数据
			if(!ccCourseTargetIndications.isEmpty()){
				for(CcCourseTargetIndication temp : ccCourseTargetIndications) {
					Long indicationId = temp.getLong("indicatoin_id");
					// 如果这个id不存在，则加进去
					if(!indicationIds.contains(indicationId)) {
						indicationIds.add(indicationId);
					}
				}
				//  强制删除课程目标与指标点与课程关系的关系表
				if(!CcCourseTargetIndication.dao.deleteAllByColumn("indication_course_id", idsArray, date)){
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					result.put("isSuccess", false);
					return renderSUC(result, response, header);
				}
			}
		} else {
			// 如果存在关联，则不删除
			if(!ccCourseTargetIndications.isEmpty()){
				return renderFAIL("1100", response, header);
			}
		}

		// 删除CcIndicationCourse
		if(!CcIndicationCourse.dao.deleteAll(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		// 本次删除涉及到的课程目标，如果没有其它链接，则课程目标也删除。Edit By SY 2019年12月5日01:34:21
		List<CcCourseTargetIndication> ccCourseTargetIndicationsCheck = CcCourseTargetIndication.dao.findByColumnIn("indicatoin_id", indicationIds.toArray(new Long[indicationIds.size()]));
		if(!ccCourseTargetIndicationsCheck.isEmpty()) {
			// 还存在记录的，则从indicationIds里面去掉。等下这个List用于删除课程目标
			for(CcCourseTargetIndication temp : ccCourseTargetIndicationsCheck) {
				Long indicationId = temp.getLong("indicatoin_id");
				indicationIds.remove(indicationId);
			}
		}
		if(!indicationIds.isEmpty()) {
			if(!CcIndication.dao.deleteAll(indicationIds.toArray(new Long[indicationIds.size()]), date)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}

		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}	
	
