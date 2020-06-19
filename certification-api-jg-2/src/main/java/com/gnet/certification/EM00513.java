package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradecomposeStudetail;
import com.gnet.model.admin.CcScoreStuIndigrade;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;

/**
 * 统计指标点成绩接口
 * 
 * @author sll
 *
 */
@Service("EM00513")
@Transactional(readOnly=false)
public class EM00513 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		//指标点编号
		Long indicationId = paramsLongFilter(param.get("indicationId"));
		//教学班编号
		Long educlassId = paramsLongFilter(param.get("educlassId"));
		
		if (indicationId == null) {
			return renderFAIL("0362", response, header);
		}
		if (educlassId == null) {
			return renderFAIL("0380", response, header);
		}
		
		//统计某一班级所有学生某一个开课指标点的分数和
		List<CcCourseGradecomposeStudetail> ccCourseGradecomposeStudetails = CcCourseGradecomposeStudetail.dao.sumOfScore(indicationId, educlassId);
		if (ccCourseGradecomposeStudetails.isEmpty()) {
			return renderFAIL("0463", response, header);
		}
		
		Map<String, Boolean> result = new HashMap<>();
		
		//删除该班级下所有学生该指标点的成绩
		if (!CcScoreStuIndigrade.dao.deleteByClassIdAndIndicationId(educlassId, indicationId)){
			TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		//批量保存学生的成绩
		List<CcScoreStuIndigrade> ccScoreStuIndigrades = Lists.newArrayList();
		Date date = new Date();
		for (CcCourseGradecomposeStudetail temp: ccCourseGradecomposeStudetails) {
			CcScoreStuIndigrade ccScoreStuIndigrade = new CcScoreStuIndigrade();
			ccScoreStuIndigrade.set("id", SpringContextHolder.getBean(IdGenerate.class).getNextValue());
			ccScoreStuIndigrade.set("create_date", date);
			ccScoreStuIndigrade.set("modify_date", date);
			ccScoreStuIndigrade.set("gradecompose_indication_id", temp.getLong("gradecompose_indication_id") );
			ccScoreStuIndigrade.set("student_id", temp.getLong("student_id"));
			ccScoreStuIndigrade.set("grade", temp.getBigDecimal("sum"));
			ccScoreStuIndigrade.set("is_del", Boolean.FALSE);
			ccScoreStuIndigrades.add(ccScoreStuIndigrade);
		}
		
		if (!CcScoreStuIndigrade.dao.batchSave(ccScoreStuIndigrades)){
			TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		} 
		
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}
