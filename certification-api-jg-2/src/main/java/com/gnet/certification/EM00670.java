package com.gnet.certification;

import java.util.ArrayList;
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
import com.gnet.model.admin.CcVersionCreateLog;
import com.gnet.model.admin.CcVersionDeleteLog;

/**
 * 查看当前拷贝版本任务状态
 * 
 * @author SY
 * 
 * @date 2016年8月17日18:59:37
 *
 */
@Service("EM00670")
@Transactional
public class EM00670 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long majorId = paramsLongFilter(param.get("majorId"));
		
		if (majorId == null) {
			return renderFAIL("0130", response, header);
		}
		
		
		Map<String, Object> map = new HashMap<>();
		
		List<CcVersionCreateLog> ccVersionCreateLog = CcVersionCreateLog.dao.findAndReadByMajorId(majorId);
		List<CcVersionCreateLog> ccVersionCreateLogSave = new ArrayList<>();
		if(ccVersionCreateLog != null && !ccVersionCreateLog.isEmpty()) {
			for(CcVersionCreateLog createLog : ccVersionCreateLog) {
				CcVersionCreateLog newTemp = new CcVersionCreateLog();
				newTemp.put("id", createLog.get("id"));
				newTemp.put("createDate", createLog.get("create_date"));
				newTemp.put("modifyDate", createLog.get("modify_date"));
				newTemp.put("scheduleKey", createLog.get("schedule_key"));
				newTemp.put("createStep", createLog.get("create_step"));
				newTemp.put("createStatus", createLog.get("create_status"));
				newTemp.put("createPersonId", createLog.get("create_person_id"));
				newTemp.put("createMessage", createLog.get("create_message"));
				newTemp.put("majorId", createLog.get("major_id"));
				newTemp.put("parentId", createLog.get("parentId"));
				newTemp.put("totalStep", createLog.getInt("total_step"));
				
				ccVersionCreateLogSave.add(newTemp);
			}
			
		}
		
		List<CcVersionDeleteLog> ccVersionDeleteLog = CcVersionDeleteLog.dao.findAndReadByMajorId(majorId);
		List<CcVersionDeleteLog> ccVersionDeleteLogSave = new ArrayList<>();
		if(ccVersionDeleteLog != null && !ccVersionDeleteLog.isEmpty()) {
			for(CcVersionDeleteLog deleteLog : ccVersionDeleteLog) {
				CcVersionDeleteLog newTemp = new CcVersionDeleteLog();
				newTemp.put("id", deleteLog.get("id"));
				newTemp.put("createDate", deleteLog.get("create_date"));
				newTemp.put("modifyDate", deleteLog.get("modify_date"));
				newTemp.put("scheduleKey", deleteLog.get("schedule_key"));
				newTemp.put("deleteStep", deleteLog.get("delete_step"));
				newTemp.put("deleteStatus", deleteLog.get("delete_status"));
				newTemp.put("deletePersonId", deleteLog.get("delete_person_id"));
				newTemp.put("deleteMessage", deleteLog.get("delete_message"));
				newTemp.put("totalStep", CcVersionDeleteLog.STEP_ALL_NUM);
				
				ccVersionDeleteLogSave.add(newTemp);
			}
			
		}
		
		Map<String, Object> params = new HashMap<>();
		params.put("stepAllNum", CcVersionCreateLog.STEP_ALL_NUM);
		
		map.put("params", params);
		map.put("versionCreateLogList", ccVersionCreateLogSave);
		map.put("versionDeleteLogList", ccVersionDeleteLogSave);
		return renderSUC(map, response, header);
	}

}
