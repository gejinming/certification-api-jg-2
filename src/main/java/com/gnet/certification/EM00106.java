package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.job.impl.VersionCloseJobImpl;
import com.gnet.model.admin.CcVersion;
import com.gnet.model.admin.CcVersionCreateLog;
import com.gnet.model.admin.CcVersionDeleteLog;
import com.gnet.model.admin.User;
import com.gnet.plugin.quartz.QuartzKit;
import com.gnet.service.CcVersionService;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Record;

/**
 * 废弃版本
 * 
 * @author SY
 * @Date 2016年6月22日18:44:23
 */
@Service("EM00106")
@Transactional
public class EM00106 extends BaseApi implements IApi {

	private static final Logger logger = Logger.getLogger(EM00106.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		// 获取数据
		Long versionId = paramsLongFilter(params.get("id"));
		String token = request.getHeader().getToken();
		User user = UserCacheKit.getUser(token);
		Long userId = user.getLong("id");
		
		if (versionId == null) {
			return renderFAIL("0140", response, header);
		}
		
		// 保存这个信息
		CcVersion version = CcVersion.dao.findById(versionId);
		if(version == null){
			return renderFAIL("0141", response, header);
		 	
		}
	    
		//验证是否可以删除版本
		CcVersionService ccVersionService = SpringContextHolder.getBean(CcVersionService.class);
		if(!ccVersionService.isDeletable(version).isSucc()){
			return renderFAIL("0821", response, header,ccVersionService.isDeletable(version).getContent());
		}
		
		Long majorId = version.getLong("major_id");
		// 验证是否有可以有当前专业的version在新增
		Boolean returnResult = CcVersionCreateLog.dao.validateSavingByMajorId(majorId);
		if(returnResult) {
			return renderFAIL("0747", response, header);
		}
		
		Date date = new Date();
		final String key = "closeVersion" + date.getTime();
		
		CcVersionDeleteLog ccVersionDeleteLog = CcVersionDeleteLog.dao.findById(versionId);
		if(ccVersionDeleteLog == null) {
			// 如果是第一次
			ccVersionDeleteLog = new CcVersionDeleteLog();
			ccVersionDeleteLog.set("id", versionId);
			ccVersionDeleteLog.set("create_date", date);
			ccVersionDeleteLog.set("modify_date", date);
			ccVersionDeleteLog.set("schedule_key", key);
			ccVersionDeleteLog.set("delete_step", CcVersionCreateLog.STEP_NOT_START);
			ccVersionDeleteLog.set("delete_message", "准备开始删除！");
			ccVersionDeleteLog.set("delete_status", CcVersionDeleteLog.TYPE_STATUS_NOT_START);
			ccVersionDeleteLog.set("delete_person_id", userId);
			ccVersionDeleteLog.set("major_id", version.getLong("major_id"));
	        if(!ccVersionDeleteLog.save()){
	        	result.put("isSuccess", false);
	        	return renderSUC(result, response, header);
	        }
		} else {
			// 如果之前已经删除过
			ccVersionDeleteLog.set("create_date", date);
			ccVersionDeleteLog.set("modify_date", date);
			ccVersionDeleteLog.set("schedule_key", key);
			ccVersionDeleteLog.set("delete_step", CcVersionCreateLog.STEP_NOT_START);
			ccVersionDeleteLog.set("delete_message", "准备开始删除！");
			ccVersionDeleteLog.set("delete_status", CcVersionDeleteLog.TYPE_STATUS_NOT_START);
			ccVersionDeleteLog.set("delete_person_id", userId);
			ccVersionDeleteLog.set("major_id", version.getLong("major_id"));
			if(!ccVersionDeleteLog.update()){
	        	result.put("isSuccess", false);
	        	return renderSUC(result, response, header);
	        }
		}
        
		try{
			Record allMessage = new Record();
			allMessage.set("date", date);
			allMessage.set("key", key);
			allMessage.set("version", version);
			allMessage.set("versionId", versionId);
			allMessage.set("userId", userId);
			 
			
			Map<String, Object> dataMap = new HashMap<String, Object>();
								dataMap.put("allMessage", allMessage);
			
			QuartzKit.createTaskStartNow(key, VersionCloseJobImpl.class, dataMap, SimpleScheduleBuilder.simpleSchedule(), null);
		} catch (SchedulerException e) {
			if (logger.isErrorEnabled()) {
				logger.error("废弃版本失败~！", e);
			}
		}
		
		// 返回操作是否成功
		result.put("isSuccess", true);
		result.put("message", "已经提交了废弃版本请求，正在进行废弃版本操作！");
		return renderSUC(result, response, header);
	}
	
}
