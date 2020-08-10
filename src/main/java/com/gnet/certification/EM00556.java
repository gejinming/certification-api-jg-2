package com.gnet.certification;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcReportBuildStatus;
import com.gnet.service.CcReportBuildStatusService;
import com.google.common.collect.Maps;

/**
 * 获取课程达成度报表生成状态
 * 
 * @author wct
 * @date 2016年7月24日
 */
@Transactional(readOnly = true)
@Service("EM00556")
public class EM00556 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Integer grade = paramsIntegerFilter(params.get("grade"));
		Long versionId = paramsLongFilter(params.get("versionId"));
		
		// 年级为空
		if (grade == null) {
			return renderFAIL("0521", response, header);
		}
		
		// 版本编号不能为空
		if (versionId == null) {
			return renderFAIL("0522", response, header);
		}
		
		String key = CcReportBuildStatusService.getReportBuildKeyForCourse(grade, versionId);
		// 找到最新创建的任务状态记录
		CcReportBuildStatus ccReportBuildStatus = CcReportBuildStatus.dao.getBuildStatusRecord(CcReportBuildStatus.TYPE_COURSEINDICATION, key);
		if (ccReportBuildStatus == null) {
			return renderFAIL("0528", response, header);
		}
		
		// 返回结果
		Map<String, Object> result = Maps.newHashMap();
		result.put("reportStatus", ccReportBuildStatus.getInt("report_build_status"));
		result.put("reportType", ccReportBuildStatus.getInt("report_type"));
		result.put("isBuildFinish", ccReportBuildStatus.getBoolean("is_build_finish"));
		result.put("isSuccess", Boolean.FALSE);
		if (CcReportBuildStatus.STATUS_TASK_SUCCESS.equals( ccReportBuildStatus.getInt("report_build_status"))) {
			result.put("isSuccess", Boolean.TRUE);
		}
		return renderSUC(result, response, header);
	}

}
