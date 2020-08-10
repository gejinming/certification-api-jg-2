package com.gnet.certification;

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
import com.gnet.model.admin.CcReportBuildStatus;
import com.gnet.service.CcReportBuildStatusService;
import com.google.common.collect.Maps;

/**
 * 获取课程达成度多个年级版本的报表生成状态
 * 
 * @author SY
 * @date 2017年1月22日09:31:22
 */
@Transactional(readOnly = true)
@Service("EM00696")
public class EM00696 extends BaseApi implements IApi {

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		/*
		 * params [	
		 * 	{
		 * 		grade : 11,
		 * 		versionId : 218264
		 * 	},
		 * 	{
		 * 		grade : 12,
		 * 		versionId : 226253
		 * 	}
		 * ]
		 */
		List<HashMap> paramsMap = paramsJSONArrayFilter(params.get("params"), HashMap.class);
		
		if (paramsMap.isEmpty()) {
			return renderFAIL("0336", response, header);
		}
		
		// 返回结果
		Map<String, Object> result = Maps.newHashMap();
		
		// 找到最新创建的任务状态记录
		Boolean isSuccess = Boolean.TRUE;
		Boolean isBuildFinish = Boolean.TRUE;
		for (Map<String, Object> gradeAndVersionId : paramsMap) {
			Integer grade = Integer.valueOf(gradeAndVersionId.get("grade").toString());
			Long versionId = Long.valueOf(gradeAndVersionId.get("versionId").toString());
			String key = CcReportBuildStatusService.getReportBuildKeyForCourse(grade, versionId);
			
			CcReportBuildStatus temp = CcReportBuildStatus.dao.getBuildStatusRecord(CcReportBuildStatus.TYPE_COURSEINDICATION, key);
			if (temp == null) {
				return renderFAIL("0528", response, header);
			}
			isSuccess = CcReportBuildStatus.STATUS_TASK_SUCCESS.equals(temp.getInt("report_build_status"));
			isBuildFinish = temp.getBoolean("is_build_finish");
			
			if(!isBuildFinish || !isSuccess) {
				break;
			}
		}
				
		result.put("isSuccess", isSuccess);
		result.put("isBuildFinish", isBuildFinish);
		return renderSUC(result, response, header);
	}

}
