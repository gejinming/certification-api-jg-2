package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.gnet.service.CcCourseOutlineTemplateInfoService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 删除模板接口
 * 
 * @author xzl
 * 
 * @date 2017年8月2日
 *
 */
@Service("EM00701")
@Transactional(readOnly=false)
public class EM00701 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		Map<String, Object> result = new HashMap<>();
		Long id = paramsLongFilter(param.get("id"));
		User user  = UserCacheKit.getUser(request.getHeader().getToken());
		
		if (id == null) {
			return renderFAIL("0869", response, header);
		}

		CcCourseOutlineTemplate ccCourseOutlineTemplate = CcCourseOutlineTemplate.dao.findFilteredById(id);
		if(ccCourseOutlineTemplate == null){
			return renderFAIL("0889", response, header);
		}

		//如果模板已被教师教师引用不允许删除
		Long useCount = CcCourseOutline.dao.count("outline_template_id", id, true);
        if(useCount > 0){
        	return renderFAIL("0897", response, header);
		}

		Date date = new Date();
        if(!CcCourseOutlineTemplate.dao.deleteAllById(id, date)){
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
	    }

	    //删除基本信息
		 if(!CcCourseOutlineTemplateInfo.dao.deleteAllByColumn("course_outline_template_id", id, date)){
			 TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			 result.put("isSuccess", false);
			 return renderSUC(result, response, header);
		 }

	    //删除模块
	    if(!CcCourseOutlineTemplateModule.dao.deleteAllByColumn("course_outline_template_id", id, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		//删除表名
		if(!CcCourseOutlineTemplateTableName.dao.deleteAllByColumn("course_outline_template_id", id, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		//删除表头
		if(!CcCourseOutlineTemplateHeader.dao.deleteAllByColumn("course_outline_template_id", id, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		//保存大纲模板操作历史
		CcCourseOutlineTemplateHistory ccCourseOutlineTemplateHistory = new CcCourseOutlineTemplateHistory();
		ccCourseOutlineTemplateHistory.set("create_date", date);
		ccCourseOutlineTemplateHistory.set("modify_date", date);
		ccCourseOutlineTemplateHistory.set("outline_template_id", id);
		ccCourseOutlineTemplateHistory.set("trigger_id", user.getLong("id"));
		ccCourseOutlineTemplateHistory.set("event", String.format("负责人(编号：%s)%s%s了【%s(编号：%s)】的课程大纲模板", user.getLong("id"), user.getStr("name"), "删除", ccCourseOutlineTemplate.getStr("name"), ccCourseOutlineTemplate.getLong("id")));
		ccCourseOutlineTemplateHistory.set("is_del", Boolean.FALSE);

		if(!ccCourseOutlineTemplateHistory.save()){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}


		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}
