package com.gnet.certification;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcEvalute;
import com.gnet.utils.PriceUtils;
import com.jfinal.kit.StrKit;

/**
 * 编辑教师课程考评点
 * 
 * @author sll
 * 
 * @date 2016年07月04日 15:58:01
 *
 */
@Service("EM00363")
@Transactional(readOnly=false)
public class EM00363 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		Long indicationId = paramsLongFilter(param.get("indicationId"));
		Long teacherCourseId = paramsLongFilter(param.get("teacherCourseId"));
		String content = paramsStringFilter(param.get("content"));
		Long evaluteTypeId = paramsLongFilter(param.get("evaluteTypeId"));
		BigDecimal weight = paramsBigDecimalFilter(param.get("weight"));
		String remark = paramsStringFilter(param.get("remark"));
		
		if (id == null) {
			return renderFAIL("0370", response, header);
		}
		if (indicationId == null) {
			return renderFAIL("0372", response, header);
		}
		if (teacherCourseId == null) {
			return renderFAIL("0373", response, header);
		}
		if (StrKit.isBlank(content)) {
			return renderFAIL("0374", response, header);
		}
		if (weight == null) {
			return renderFAIL("0375", response, header);
		}
		if (evaluteTypeId == null) {
			return renderFAIL("0900", response, header);
		}
		
		Date date = new Date();
		CcEvalute ccEvalute = CcEvalute.dao.findFilteredById(id);
		if(ccEvalute == null) {
			return renderFAIL("0371", response, header);
		}
		if(PriceUtils.greaterThan(weight, CcEvalute.MAX_WEIGHT)){
			return renderFAIL("0439", response, header);
		}
		
		if(weight.equals(CcEvalute.MIN_WEIGHT) || PriceUtils.greaterThan(CcEvalute.MIN_WEIGHT, weight)){
			return renderFAIL("0376", response, header);
		}
		BigDecimal exsitWeight = CcEvalute.dao.findIndicationExistWeight(indicationId, id, teacherCourseId, evaluteTypeId);
		if(exsitWeight != null && PriceUtils.greaterThan(PriceUtils._add(exsitWeight, weight), CcEvalute.MAX_WEIGHT)){
			BigDecimal restWeight = PriceUtils.sub(CcEvalute.MAX_WEIGHT, exsitWeight);
			return renderFAIL("0772", response, header,  "同一指标点下考评点比例系数和不能大于1,该指标点剩余比例系数为 "+ restWeight);
		}
		
		ccEvalute.set("modify_date", date);
		ccEvalute.set("indication_id", indicationId);
		ccEvalute.set("teacher_course_id", teacherCourseId);
		ccEvalute.set("content", content);
		ccEvalute.set("weight", weight);
		ccEvalute.set("remark", remark);
		
		Map<String, Boolean> result = new HashMap<>();
		result.put("isSuccess", ccEvalute.update());
		return renderSUC(result, response, header);
	}
	
}
