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
import com.google.common.collect.Maps;

/**
 * 教学班任务状态获取接口
 * 
 * @author wct
 * @date 2016年8月11日
 */
@Transactional(readOnly = true)
@Service("EM00663")
public class EM00663 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long eduClassId = paramsLongFilter(params.get("eduClassId"));
		
		// 教学班编号不能为空
		if (eduClassId == null) {
			return renderFAIL("0500", response, header);
		}
		
		String key = eduClassId.toString() + "异步统计教学班达成度数据";
		// 找到最新创建的任务状态记录
		CcReportBuildStatus ccReportBuildStatus = CcReportBuildStatus.dao.getBuildStatusRecord(CcReportBuildStatus.TYPE_EDUCLASS, key);
		
		if (ccReportBuildStatus == null) {
			return renderFAIL("0508", response, header);
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
