package com.gnet.certification;

import com.alibaba.fastjson.JSON;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.response.ServiceResponse;
import com.gnet.service.CcCourseOutlineModuleService;
import com.gnet.service.CcCourseOutlineService;
import com.gnet.service.CcCourseOutlineTemplateInfoService;
import com.gnet.service.CcCourseOutlineTemplateModuleService;
import com.gnet.utils.DateUtil;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

/**
 * 负责人保存大纲模板
 *
 * @author xzl
 * @date 2018年1月30日15:11:39
 */
@Service("EM00731")
@Transactional(readOnly = false)
public class EM00731 extends BaseApi implements IApi {

    private static final Logger logger = Logger.getLogger(EM00731.class);

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        // 返回结果
        Map<String, Object> result = Maps.newHashMap();
        Map<String, Object> param = request.getData();
        Long id = paramsLongFilter(param.get("id"));
        Long courseOutlineTypeId = paramsLongFilter(param.get("courseOutlineTypeId"));
        String templateName = paramsStringFilter(param.get("templateName"));
        List<LinkedHashMap> courseInfoMap = paramsJSONArrayFilter(param.get("courseInfos"), LinkedHashMap.class);
        List<LinkedHashMap> moduleMap = paramsJSONArrayFilter(param.get("modules"), LinkedHashMap.class);
        User user  = UserCacheKit.getUser(request.getHeader().getToken());

        if(user == null ){
            return renderFAIL("0536", response, header);
        }

        //完善大纲信息
        if (moduleMap.isEmpty()) {
            return renderFAIL("0865", response, header);
        }

        //完善大纲与课程相关信息(也可自定义)
        if (courseInfoMap.isEmpty()) {
            return renderFAIL("0864", response, header);
        }

        if(courseOutlineTypeId == null){
            return  renderFAIL("0892", response, header);
        }

        if (StrKit.isBlank(templateName)) {
            return renderFAIL("0862", response, header);
        }

        if (CcCourseOutlineTemplate.dao.isExisted(templateName,id, courseOutlineTypeId)) {
            return renderFAIL("0863", response, header);
        }

        Date date = new Date();
        CcCourseOutlineTemplate ccCourseOutlineTemplate = null;
        if(id != null){
            ccCourseOutlineTemplate = CcCourseOutlineTemplate.dao.findFilteredById(id);
            if(ccCourseOutlineTemplate == null){
                return renderFAIL("0889", response, header);
            }

            ccCourseOutlineTemplate.set("modify_date", date);
            ccCourseOutlineTemplate.set("name", templateName);

            if(!ccCourseOutlineTemplate.update()){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.put("isSuccess", false);
                return renderSUC(result, response, header);
            }
        }else{
            IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
            ccCourseOutlineTemplate = new CcCourseOutlineTemplate();
            ccCourseOutlineTemplate.set("id", idGenerate.getNextValue());
            ccCourseOutlineTemplate.set("create_date", date);
            ccCourseOutlineTemplate.set("modify_date", date);
            ccCourseOutlineTemplate.set("name", templateName);
            ccCourseOutlineTemplate.set("is_del", Boolean.FALSE);
            ccCourseOutlineTemplate.set("outline_type_id", courseOutlineTypeId);
            if (!ccCourseOutlineTemplate.save()) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.put("isSuccess", false);
                return renderSUC(result, response, header);
            }
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

        //保存模板基本信息
        CcCourseOutlineTemplateInfoService ccCourseOutlineTemplateInfoService = SpringContextHolder.getBean(CcCourseOutlineTemplateInfoService.class);
        ServiceResponse infoResponse = ccCourseOutlineTemplateInfoService.saveTemplateInfo(courseInfoMap, ccCourseOutlineTemplate.getLong("id"));
        if(!infoResponse.isSucc()){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return renderFAIL("0866", response, header, infoResponse.getContent());
        }

        //保存模板模块
        CcCourseOutlineTemplateModuleService ccCourseOutlineTemplateModuleService = SpringContextHolder.getBean(CcCourseOutlineTemplateModuleService.class);
        ServiceResponse templateResponse = ccCourseOutlineTemplateModuleService.saveTemplateLists(moduleMap, ccCourseOutlineTemplate.getLong("id"));
        if(!templateResponse.isSucc()){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return renderFAIL("0866", response, header, templateResponse.getContent());
        }

        //保存大纲模板操作历史
        CcCourseOutlineTemplateHistory ccCourseOutlineTemplateHistory = new CcCourseOutlineTemplateHistory();
        ccCourseOutlineTemplateHistory.set("create_date", date);
        ccCourseOutlineTemplateHistory.set("modify_date", date);
        ccCourseOutlineTemplateHistory.set("outline_template_id", ccCourseOutlineTemplate.getLong("id"));
        ccCourseOutlineTemplateHistory.set("trigger_id", user.getLong("id"));
        ccCourseOutlineTemplateHistory.set("event", String.format("负责人(编号：%s)%s%s了【%s(编号：%s)】的课程大纲模板", user.getLong("id"), user.getStr("name"), id == null ? "创建":"修改", templateName, ccCourseOutlineTemplate.getLong("id")));
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
