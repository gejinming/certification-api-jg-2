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
 * 根据课程编号和指标点编号删除课程指标点
 *
 * @author xzl
 *
 * @date 2017年11月20日11:22:14
 *
 */
@Service("EM00277")
@Transactional(readOnly=false)
public class EM00277 extends BaseApi implements IApi{

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String,Boolean> result = new HashMap<>();

		Date date = new Date();

		// 强制删除的标志，是否必须通过，当存在关联表数据使用到本条数据id的时候，一起删除
		Boolean isMustPassAndDel = paramsBooleanFilter(param.get("isMustPassAndDel"));
		Long courseId = paramsLongFilter(param.get("courseId"));
		Long indicationId = paramsLongFilter(param.get("indicationId"));
		Long idsArray[] = new Long[1];

		if(courseId == null){
			return renderFAIL("0250", response, header);
		}

		if(indicationId == null){
			return renderFAIL("0230", response, header);
		}

		CcIndicationCourse ccIndicationCourse = CcIndicationCourse.dao.findByCourseIdAndIndicationId(courseId, indicationId);
		if(ccIndicationCourse != null){
			idsArray[0] = ccIndicationCourse.getLong("id");
			if(isMustPassAndDel != null && isMustPassAndDel) {
				// TODO SY 学长说这里下次做，先预留一下。用于删除对应的表
			} else {
				   List<CcCourseTargetIndication> ccCourseTargetIndicationList = CcCourseTargetIndication.dao.findByColumnIn("indication_course_id", idsArray);
				   if(!ccCourseTargetIndicationList.isEmpty()){
					   return renderFAIL("1100", response, header);
				   }
			}

			// 删除CcIndicationCourse
			if(!CcIndicationCourse.dao.deleteAll(idsArray, date)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}

		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}	
	
