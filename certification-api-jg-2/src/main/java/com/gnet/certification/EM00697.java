package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.gnet.response.ServiceResponse;
import com.gnet.service.*;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

/**
 * 教师根据大纲模板创建或修改课程大纲
 *
 * @author xzl
 * @date 2017年7月31日
 */
@Deprecated
@Service("EM00697")
@Transactional(readOnly = false)
public class EM00697 extends BaseApi implements IApi {

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        // 返回结果
        Map<String, Object> result = Maps.newHashMap();
        Map<String, Object> param = request.getData();
        //课程教学大纲文本编号
        Long courseOutlineId = paramsLongFilter(param.get("courseOutlineId"));
        String name = paramsStringFilter(param.get("name"));
        String templateName = paramsStringFilter(param.get("templateName"));
        Boolean isCreateTemplate = paramsBooleanFilter(param.get("isCreateTemplate"));
        List<LinkedHashMap> courseInfoMap = paramsJSONArrayFilter(param.get("courseInfos"), LinkedHashMap.class);
        List<LinkedHashMap> moduleMap = paramsJSONArrayFilter(param.get("modules"), LinkedHashMap.class);
        Long courseOutlineTypeId = paramsLongFilter(param.get("courseOutlineTypeId"));
        User user  = UserCacheKit.getUser(request.getHeader().getToken());

        if(user == null ){
            return renderFAIL("0536", response, header);
        }

        if (courseOutlineId == null) {
            return renderFAIL("0531", response, header);
        }

        if (StrKit.isBlank(name)) {
            return renderFAIL("0860", response, header);
        }

        if(isCreateTemplate == null){
            return renderFAIL("0868", response, header);
        }

        //完善大纲和模板模块信息
        if (moduleMap.isEmpty()) {
            return renderFAIL("0865", response, header);
        }

        //完善大纲与课程相关信息(也可自定义)
        if (courseInfoMap.isEmpty()) {
            return renderFAIL("0864", response, header);
        }

        CcCourse course = CcCourse.dao.findByCourseOutlineId(courseOutlineId);
        if(course == null){
            return renderFAIL("0251", response, header);
        }

        CcCourseOutline ccCourseOutline = CcCourseOutline.dao.findFilteredById(courseOutlineId);
        if (ccCourseOutline.getInt("status") > CcCourseOutline.STATUS_NOT_START) {
            return renderFAIL("0861", response, header);
        }

        //保存大纲名称
        Date date = new Date();
        if (ccCourseOutline == null) {
            return renderFAIL("0537", response, header);
        }
        ccCourseOutline.set("modify_date", date);
        ccCourseOutline.set("name", name);
        if (!ccCourseOutline.update()) {
            result.put("isSuccess", false);
            return renderSUC(result, response, header);
        }

        //保存大纲模板
        if(isCreateTemplate){

            if (StrKit.isBlank(templateName)) {
                return renderFAIL("0862", response, header);
            }

            if (CcCourseOutlineTemplate.dao.isExisted(templateName, courseOutlineTypeId, courseOutlineTypeId)) {
                return renderFAIL("0863", response, header);
            }

            CcCourseOutlineTemplate ccCourseOutlineTemplate = new CcCourseOutlineTemplate();
            ccCourseOutlineTemplate.set("create_date", date);
            ccCourseOutlineTemplate.set("modify_date", date);
            ccCourseOutlineTemplate.set("name", templateName);
            ccCourseOutlineTemplate.set("is_del", Boolean.FALSE);
            if (!ccCourseOutlineTemplate.save()) {
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
        }


        //保存与课程相关信息
        CcCourseOutlineService ccCourseOutlineService = SpringContextHolder.getBean(CcCourseOutlineService.class);
        ServiceResponse serviceResponse = ccCourseOutlineService.saveCourseInfo(courseInfoMap, courseOutlineId);
        if(!serviceResponse.isSucc()){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return renderFAIL("0864", response, header, serviceResponse.getContent());
        }

        //保存大纲模块
        CcCourseOutlineModuleService ccCourseOutlineModuleService = SpringContextHolder.getBean(CcCourseOutlineModuleService.class);
        ServiceResponse moduleResponse = ccCourseOutlineModuleService.saveModuleLists(moduleMap, courseOutlineId);
        if(!moduleResponse.isSucc()){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return renderFAIL("0866", response, header, moduleResponse.getContent());
        }

        //记录操作历史
        CcCourseOutlineHistory courseOutlineHistory = new CcCourseOutlineHistory();
        courseOutlineHistory.set("create_date", date);
        courseOutlineHistory.set("modify_date", date);
        courseOutlineHistory.set("outline_id", courseOutlineId);
        courseOutlineHistory.set("trigger_id", user.getLong("id"));
        courseOutlineHistory.set("event", String.format("负责人(编号：%s)%s创建了【%s(编号：%s)】的课程大纲", user.getLong("id"), user.getStr("name"), course.getStr("name"), course.getLong("id")));
        courseOutlineHistory.set("event_type", CcCourseOutlineHistory.TYPE_CREATE);
        courseOutlineHistory.set("is_del", Boolean.FALSE);

        if(!courseOutlineHistory.save()){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("isSuccess", false);
            return renderSUC(result, response, header);
        }

        result.put("isSuccess", true);
        return renderSUC(result, response, header);
    }

}
