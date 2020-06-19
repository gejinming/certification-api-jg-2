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
 * 开课课程下教学班任务状态获取接口
 * 
 * @author wct
 * @date 2016年8月11日
 */
@Transactional(readOnly = true)
@Service("EM00664")
public class EM00664 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long teacherCourseId = paramsLongFilter(params.get("teacherCourseId"));
		
		// 开课课程编号不能为空
		if (teacherCourseId == null) {
			return renderFAIL("0506", response, header);
		}
		
		String key = teacherCourseId.toString() + "异步统计教师开课课程下的教学班达成度数据";
		
		// 找到最新创建的任务状态记录
		CcReportBuildStatus ccReportBuildStatus = CcReportBuildStatus.dao.getBuildStatusRecord(CcReportBuildStatus.TYPE_TEACHERCOURSE_EDUCLASS, key);
		if (ccReportBuildStatus == null) {
			return renderFAIL("0509", response, header);
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
