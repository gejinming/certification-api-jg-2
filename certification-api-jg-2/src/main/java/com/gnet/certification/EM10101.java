package com.gnet.certification;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.job.impl.AdminVersionCreateAndCopyJobImpl;
import com.gnet.model.admin.CcVersion;
import com.gnet.model.admin.CcVersionCreateLog;
import com.gnet.model.admin.CcVersionDeleteLog;
import com.gnet.model.admin.User;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.plugin.quartz.QuartzKit;
import com.gnet.plugin.quartz.callback.ITaskMonitor;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Record;

/**
 * 后台调用专用，根据父级版本，新增大版本，而且是增加以前的大版本
 * 
 * @author SY
 * @Date 2017年11月9日
 */
@Service("EM10101")
public class EM10101 extends BaseApi implements IApi {

	private static final Logger logger = Logger.getLogger(EM10101.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		// 各种其他信息
		String description = paramsStringFilter(params.get("description"));
		String name = paramsStringFilter(params.get("name"));
		String remark = paramsStringFilter(params.get("remark"));
		Long majorId = paramsLongFilter(params.get("majorId"));
		Integer enableGrade = paramsIntegerFilter(params.get("enableGrade"));
		Long parentId = paramsLongFilter(params.get("parentId"));
		// 另外两张关联表
		String planName = paramsStringFilter(params.get("planName"));
		String applyGrade = paramsStringFilter(params.get("applyGrade"));
		String planCourseVersionName = paramsStringFilter(params.get("planCourseVersionName"));
		String graduateName = paramsStringFilter(params.get("graduateName"));
		String graduateIndicationVersionName = paramsStringFilter(params.get("graduateIndicationVersionName"));
		BigDecimal pass = paramsBigDecimalFilter(params.get("pass"));
		
		String token = request.getHeader().getToken();
		User user = UserCacheKit.getUser(token);
		Long userId = user.getLong("id");
		
		if(enableGrade == null){
			return renderFAIL("0154", response, header);
		}
		if (majorId == null) {
			return renderFAIL("0146", response, header);
		}
		if (StrKit.isBlank(name)) {
			return renderFAIL("0147", response, header);
		}
		if (pass == null) {
			return renderFAIL("0158", response, header);
		}
		if (StrKit.isBlank(planName)) {
			return renderFAIL("0159", response, header);
		}
		if (StrKit.isBlank(graduateName)) {
			return renderFAIL("0157", response, header);
		}
		
		Integer majorVersion = -1;
		
//		CcVersionService ccVersionService = SpringContextHolder.getBean(CcVersionService.class);
//		
//		// 验证版本的起始年级是否符合要求
//		ServiceResponse responseResult = ccVersionService.validateSaveEnableGrade(CcVersion.TYPE_MAJOR_VERSION, enableGrade, majorId, parentId);
//		if(!responseResult.isSucc()) {
//			return renderFAIL("0155", response, header, responseResult.getContent());
//		}
		
		// 验证是否有可以有当前专业的version在新增
		Boolean returnResult = CcVersionCreateLog.dao.validateSavingByMajorId(majorId);
		if(returnResult) {
			return renderFAIL("0749", response, header);
		}
		// 验证是否有可以有当前专业的version在删除
		returnResult = CcVersionDeleteLog.dao.validateDeletingByMajorId(majorId);
		if(returnResult) {
			return renderFAIL("0748", response, header);
		}
		
		//专业认证持续改进方案版本保存
		Date date = new Date();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		Long id = idGenerate.getNextValue();
		CcVersion ccVersion = new CcVersion();
		CcVersionCreateLog ccVersionCreateLog = new CcVersionCreateLog();
		final String key = "copyVersion" + date.getTime();
		ccVersionCreateLog.set("id", id);
		ccVersionCreateLog.set("create_date", date);
		ccVersionCreateLog.set("modify_date", date);
		ccVersionCreateLog.set("schedule_key", key);
		ccVersionCreateLog.set("create_step", CcVersionCreateLog.STEP_NOT_START);
		ccVersionCreateLog.set("create_message", "准备开始创建！");
		ccVersionCreateLog.set("create_status", CcVersionCreateLog.TYPE_STATUS_NOT_START);
		ccVersionCreateLog.set("create_person_id", userId);
		ccVersionCreateLog.set("major_id", majorId);
		ccVersionCreateLog.set("parent_id", parentId);
		ccVersionCreateLog.set("total_step", parentId == null ? CcVersionCreateLog.STEP_ALL_NUM :  CcVersionCreateLog.COPY_STEP_ALL_NUM);
		if(!ccVersionCreateLog.save()){
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		ccVersion.set("id", id);
		ccVersion.set("create_date", date);
		ccVersion.set("modify_date", date);
		ccVersion.set("is_use", Boolean.TRUE);
		ccVersion.set("is_del", Boolean.FALSE);
		ccVersion.set("description", description);
		ccVersion.set("enable_grade", enableGrade);
		ccVersion.set("apply_grade", applyGrade);
		ccVersion.set("name", name);
		ccVersion.set("remark", remark);
		ccVersion.set("major_id", majorId);
		ccVersion.set("parent_id", parentId);
		ccVersion.set("state", CcVersion.STATUE_EDIT);
		ccVersion.set("type", CcVersion.TYPE_MAJOR_VERSION);
		ccVersion.set("minor_version", CcVersion.INITIAL_MINOR);
		ccVersion.set("major_version", majorVersion);
		ccVersion.set("max_minor_version", CcVersion.MINOR_VERSION_NULL);
		
		
		try{
			Record allMessage = new Record();
			allMessage.set("description", description);
			allMessage.set("name", name);
			allMessage.set("remark", remark);
			allMessage.set("majorId", majorId);
			allMessage.set("enableGrade", enableGrade);
			allMessage.set("newVersionId", id);
			allMessage.set("oldVersionId", parentId);
			allMessage.set("parentId", parentId);
			allMessage.set("planName", planName);
			allMessage.set("planCourseVersionName", planCourseVersionName);
			allMessage.set("graduateName", graduateName);
			allMessage.set("graduateIndicationVersionName", graduateIndicationVersionName);
			allMessage.set("pass", pass);
			allMessage.set("date", date);
			allMessage.set("key", key);
			allMessage.set("userId", userId);
			allMessage.set("isCopyAchievement", Boolean.FALSE);
			
			Map<String, Object> dataMap = new HashMap<String, Object>();
								dataMap.put("ccVersion", ccVersion);
								dataMap.put("allMessage", allMessage);
			
			QuartzKit.createTaskStartNow(key, AdminVersionCreateAndCopyJobImpl.class, dataMap, SimpleScheduleBuilder.simpleSchedule(), new ITaskMonitor() {
	
				public void taskStart() {
				}
	
				public void taskFinish() {
				}
	
				public void taskFail() {
				}
	
			});
		} catch (SchedulerException e) {
			if (logger.isErrorEnabled()) {
				logger.error("复制版本失败~！", e);
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}
		
		// 返回操作是否成功
		result.put("isSuccess", true);
		result.put("versionId", id);
		return renderSUC(result, response, header);
	}
	
}
