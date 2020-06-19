package com.gnet.certification;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcReportBuildStatus;
import com.gnet.model.admin.CcVersion;
import com.google.common.collect.Maps;

/**
 * 培养计划生成状态获取接口
 * 
 * @author wct
 * @date 2016年8月9日
 */
@Service("EM00660")
public class EM00660 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long planId = paramsLongFilter(params.get("planId"));
		Long majorId = paramsLongFilter(params.get("majorId"));
		if(params.containsKey("majorId") && majorId == null){
			return renderFAIL("1009", response, header, "majorId的参数值非法");
		}
		Integer grade = paramsIntegerFilter(params.get("grade"));
		if(params.containsKey("grade") && grade == null){
			return renderFAIL("1009", response, header, "grade的参数值非法");
		}
		if(majorId != null && grade != null){
			planId = CcVersion.dao.findNewestVersion(majorId, grade);
			if (planId == null) {
				return renderFAIL("0671", response, header);
			}
		}
		// 培养计划编号不能为空
		if (planId == null) {
			return renderFAIL("0660", response, header);
		}
		
		final String missionKey = planId.toString() + "异步统计培养计划数据";
		
		// 找到最新创建的任务状态记录
		CcReportBuildStatus ccReportBuildStatus = CcReportBuildStatus.dao.getBuildStatusRecord(CcReportBuildStatus.TYPE_PLANREPORT, missionKey);
		if (ccReportBuildStatus == null) {
			return renderFAIL("0663", response, header);
		}
		// 返回结果
		Map<String, Object> result = Maps.newHashMap();
		result.put("reportStatus", ccReportBuildStatus.getInt("report_build_status"));
		result.put("reportType", ccReportBuildStatus.getInt("report_type"));
		result.put("isBuildFinish", ccReportBuildStatus.getBoolean("is_build_finish"));
		result.put("isSuccess", Boolean.FALSE);
		if (CcReportBuildStatus.STATUS_TASK_SUCCESS.equals(ccReportBuildStatus.getInt("report_build_status"))) {
			result.put("isSuccess", Boolean.TRUE);
		} else {
			result.put("errorMsg", ccReportBuildStatus.getStr("report_msg"));
		}
		
		return renderSUC(result, response, header);
	}

}
