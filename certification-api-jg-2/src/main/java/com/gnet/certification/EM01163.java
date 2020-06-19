package com.gnet.certification;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradecomposeIndication;
import com.gnet.model.admin.CcLevelDetail;
import com.gnet.model.admin.CcScoreStuIndigrade;
import com.gnet.service.CcEduindicationStuScoreService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;

/**
 * 修改等级制度明细某条信息
 * 
 * @author SY
 * @Date 2019年12月9日13:51:21
 */
@Service("EM01163")
public class EM01163 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据
		Long id = paramsLongFilter(params.get("id"));
		String name = paramsStringFilter(params.get("name"));
		BigDecimal value = paramsBigDecimalFilter(params.get("value"));
		String remark = paramsStringFilter(params.get("remark"));
		
		// id不能为空信息的过滤
		if (id == null) {
			return renderFAIL("0100", response, header);
		}
		
		// value不能为空信息的过滤
		if (value == null) {
			return renderFAIL("0102", response, header);
		}
		
		// name不能为空信息的过滤
		if (StrKit.isBlank(name)) {
			return renderFAIL("0102", response, header);
		}
				
		CcLevelDetail ccLevelDetail = CcLevelDetail.dao.findFilteredById(id);
		if(ccLevelDetail == null) {
			return renderFAIL("0101", response, header);
		}
		
		Date date = new Date();
		// 保存这个信息
		ccLevelDetail.set("modify_date", date);
		ccLevelDetail.set("name", name);
		ccLevelDetail.set("value", value);
		ccLevelDetail.set("remark", remark);
		Boolean isSuccess = ccLevelDetail.update();
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		if(!isSuccess) {
			// 返回操作是否成功
			result.put("isSuccess", isSuccess);
			return renderSUC(result, response, header);
		}
		
		// 查询是否有相关的成绩
		CcScoreStuIndigrade ccScoreStuIndigrade = CcScoreStuIndigrade.dao.findFirstByColumn("level_detail_id", id);
		if(ccScoreStuIndigrade != null) {
			// 更新所有和这个相关的学生成绩。
			isSuccess = CcScoreStuIndigrade.dao.batchUpdateStudentGrade(value, id) > 0 ? Boolean.TRUE : Boolean.FALSE;
			if(!isSuccess) {
				// 更新失败，直接返回
				result.put("isSuccess", isSuccess);
				return renderSUC(result, response, header);
			}
			
			// 更新所有报表。
			List<Long> educlassIdList = Lists.newArrayList();
			Long gradecomposeIndicationId = ccScoreStuIndigrade.getLong("gradecompose_indication_id");
			CcCourseGradecomposeIndication courseGradecomposeIndication = CcCourseGradecomposeIndication.dao.findFilteredById(gradecomposeIndicationId);
			if(courseGradecomposeIndication == null){
				result.put("isSuccess", isSuccess);
				return renderSUC(result, response, header);
			}
			// 找到所有的教学班
			List<CcScoreStuIndigrade> CcScoreStuIndigradeList = CcScoreStuIndigrade.dao.findByLevelDetailId(id);
			for(CcScoreStuIndigrade temp : CcScoreStuIndigradeList) {
				Long educlassId = temp.getLong("educlassId");
				if(!educlassIdList.contains(educlassId)) {
					educlassIdList.add(educlassId);
				}
			}

			if (!educlassIdList.isEmpty()) {
				CcEduindicationStuScoreService ccEduindicationStuScoreService = SpringContextHolder.getBean(CcEduindicationStuScoreService.class);
				if (!ccEduindicationStuScoreService.calculate(educlassIdList.toArray(new Long[educlassIdList.size()]), courseGradecomposeIndication.getLong("course_gradecompose_id"))) {
					result.put("isSuccess", false);
					return renderSUC(result, response, header);
				}
			}
		}
		
		// 返回操作是否成功
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}
	
}
