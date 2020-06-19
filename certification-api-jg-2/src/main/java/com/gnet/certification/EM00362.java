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
 * 增加教师课程考评点关系表
 * 
 * @author sll
 * 
 * @date 2016年07月04日 15:58:01
 *
 */
@Service("EM00362")
@Transactional(readOnly=false)
public class EM00362 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Long evaluteTypeId = paramsLongFilter(param.get("evaluteTypeId"));
		Long indicationId = paramsLongFilter(param.get("indicationId"));
		Long teacherCourseId = paramsLongFilter(param.get("teacherCourseId"));
		String content = paramsStringFilter(param.get("content"));
		BigDecimal weight = paramsBigDecimalFilter(param.get("weight"));
		String remark = paramsStringFilter(param.get("remark"));
		
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
		
		if(weight.equals(CcEvalute.MIN_WEIGHT) || PriceUtils.greaterThan(CcEvalute.MIN_WEIGHT, weight)){
			return renderFAIL("0376", response, header);
		}
		
		if(PriceUtils.greaterThan(weight, CcEvalute.MAX_WEIGHT)){
			return renderFAIL("0439", response, header);
		}
		BigDecimal exsitWeight = CcEvalute.dao.findIndicationExistWeight(indicationId, null, teacherCourseId, evaluteTypeId);
		if(exsitWeight != null && PriceUtils.greaterThan(PriceUtils._add(exsitWeight, weight), CcEvalute.MAX_WEIGHT)){
			BigDecimal restWeight = PriceUtils.sub(CcEvalute.MAX_WEIGHT, exsitWeight);
			return renderFAIL("0772", response, header,  "同一指标点下考评点比例系数和不能大于1,该指标点剩余比例系数为 "+ restWeight);
		}
		
		//找到最大的indexNum
		Integer indexNum = CcEvalute.dao.findMaxIndexNum(teacherCourseId, indicationId, evaluteTypeId);
		
		CcEvalute ccEvalute = new CcEvalute();
		
		ccEvalute.set("create_date", date);
		ccEvalute.set("modify_date", date);
		ccEvalute.set("indication_id", indicationId);
		ccEvalute.set("teacher_course_id", teacherCourseId);
		ccEvalute.set("index_num", indexNum == null ? 1 : ++ indexNum);
		ccEvalute.set("content", content);
		ccEvalute.set("evalute_type_id", evaluteTypeId);
		ccEvalute.set("weight", weight);
		ccEvalute.set("remark", remark);
		ccEvalute.set("is_del", Boolean.FALSE);
		
		Map<String, Object> result = new HashMap<>();
		result.put("isSuccess", ccEvalute.save());
		result.put("id", ccEvalute.getLong("id"));
		
		return renderSUC(result, response, header);
	}
}
