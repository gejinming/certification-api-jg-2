package com.gnet.certification;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcVersion;
import com.gnet.model.admin.CcVersionCreateLog;
import com.gnet.model.admin.User;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.service.CcVersionCreateLogService;
import com.gnet.service.CcVersionService;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;

/**
 * 后台调用专用，单纯新增大版本，而且是增加以前的大版本
 * 
 * @author SY
 * @Date 2017年11月9日
 */
@Service("EM10102")
@Transactional(readOnly=false)
public class EM10102 extends BaseApi implements IApi {

	private static final Logger logger = Logger.getLogger(EM10102.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		// 各种其他信息
		String description = paramsStringFilter(params.get("description"));
		String applyGrade = paramsStringFilter(params.get("applyGrade"));
		String name = paramsStringFilter(params.get("name"));
		String remark = paramsStringFilter(params.get("remark"));
		Long majorId = paramsLongFilter(params.get("majorId"));
		Integer enableGrade = paramsIntegerFilter(params.get("enableGrade"));
//		Long parentId = paramsLongFilter(params.get("parentId"));
		Long parentId = null;
		// 另外两张关联表
		String planName = paramsStringFilter(params.get("planName"));
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
		
		CcVersionService ccVersionService = SpringContextHolder.getBean(CcVersionService.class);
		
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
			return renderFAIL("500", response, header, "保存日志表初始化状态失败");
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
		
		
		CcVersionCreateLogService ccVersionCreateLogService = SpringContextHolder.getBean(CcVersionCreateLogService.class);
		Boolean saveResult;
		//设置任务状态为创建中
		saveResult = ccVersionCreateLogService.changeStepJob(id, "创建中！", CcVersionCreateLog.STEP_CREATING);
		if(!saveResult){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return renderFAIL("500", response, header, "保存日志表创建中状态失败");
		}
		
		// 保存这个信息
		saveResult = ccVersion.save();
		if(!saveResult) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return renderFAIL("500", response, header, "保存版本创建失败");
		}
		
		saveResult = ccVersionService.saveLink(ccVersion, planName, planCourseVersionName, graduateName, graduateIndicationVersionName, pass);
		if(!saveResult) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			// 返回操作是否成功
			return renderFAIL("500", response, header, "保存版本关联表失败");
		}
	
		saveResult = ccVersionCreateLogService.finishJob(id);
		if(!saveResult) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return renderFAIL("500", response, header, "保存日志表结束状态失败");
		}
		
		// 返回操作是否成功
		result.put("isSuccess", saveResult);
		result.put("versionId", id);
		return renderSUC(result, response, header);
	}
	
}
