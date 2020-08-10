package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcReportPersonalIndication;
import com.gnet.model.admin.CcScoreStuIndigrade;
import com.gnet.model.admin.CcStudent;
import com.gnet.model.admin.CcStudentEvalute;
import com.gnet.model.admin.CcVersion;
import com.gnet.utils.PriceUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 个人专业达成度报表显示
 * 
 * @author wct
 * @date 2016年11月12日
 */
@Service("EM00665")
@Transactional(readOnly = true)
public class EM00665 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Integer grade = paramsIntegerFilter(params.get("grade"));
		if(params.containsKey("grade") && grade == null){
			return renderFAIL("1009", response, header, "grade的参数值非法");
		}
		Long studentId = paramsLongFilter(params.get("studentId"));
		Long versionId = paramsLongFilter(params.get("versionId"));
		Long majorId = paramsLongFilter(params.get("majorId"));
		// 年级不能为空过滤
		if (grade == null) {
			return renderFAIL("0521", response, header);
		}
		
		// 学生编号为空过滤
		if (studentId == null) {
			return renderFAIL("0526", response, header);
		}
		
		// 专业编号或版本编号必须有一个不能为空过滤
		if (majorId == null && versionId == null) {
			return renderFAIL("0524", response, header);
		}
		
		// 获得该年级使用的专业认证当前工作版本
		if (versionId == null) {
			versionId = CcVersion.dao.findNewestVersion(majorId, grade);
		}
		
		// 专业编号未找到一个版本
		if (versionId == null) {
			return renderSUC(new HashMap<String, Object>(), response, header);
		}
		
		CcStudent student = CcStudent.dao.findByIdWithMajorDirection(studentId, versionId);
		// 学生不存在过滤
		if (student == null) {
			return renderFAIL("0527", response, header);
		}
		
		List<Map<String, Object>> graduateInfos = findGraduateInfos(versionId, studentId);
//		// 获得最新的统计日期
//		Date statisticsDate = CcReportPersonalIndication.dao.getNewestStatisticsDate(studentId);
//		// 判断是否需要更新
//		boolean needUpdate = CcScoreStuIndigrade.dao.needToUpdateByStudentId(studentId, null, grade, versionId)
//				|| CcStudentEvalute.dao.needToUpdateByStudentId(studentId, null, grade, versionId);
		
		// 返回结果
		Map<String, Object> result = Maps.newHashMap();
		result.put("graduateInfos", graduateInfos);
//		result.put("statisticsDate", statisticsDate);
//		result.put("needUpdate", needUpdate);
		return renderSUC(result, response, header);
	}
	
	/**
	 * 获得个人专业达成度报表数据
	 * 
	 * @param versionId
	 * @param studentId
	 * @return
	 */
	private List<Map<String, Object>> findGraduateInfos(Long versionId, Long studentId) {
		List<Map<String, Object>> result = Lists.newArrayList();
		List<CcReportPersonalIndication> ccReportPersonalIndications = CcReportPersonalIndication.dao.findAllByStudentAndVersion(studentId, versionId);
		for (CcReportPersonalIndication ccReportPersonalIndication : ccReportPersonalIndications) {
			Map<String, Object> item = Maps.newHashMapWithExpectedSize(2);
			item.put("graduateTargetResult", ccReportPersonalIndication.get("target_value"));
			item.put("graduateNum", ccReportPersonalIndication.get("graduate_index_num"));
			item.put("graduateResult", ccReportPersonalIndication.get("indication_result"));
			item.put("isComplete", !PriceUtils.lessThan(ccReportPersonalIndication.getBigDecimal("indication_result"), ccReportPersonalIndication.getBigDecimal("target_value")));
			result.add(item);
		}
		
		return result;
	}

}
