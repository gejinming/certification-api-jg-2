package com.gnet.certification;

import com.alibaba.fastjson.JSON;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.gnet.response.ServiceResponse;
import com.gnet.service.CcCourseOutlineModuleService;
import com.gnet.service.CcCourseOutlineService;
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
 * 教师编辑或提交课程大纲
 *
 * @author xzl
 * @date 2017年8月3日
 */
@Service("EM00702")
@Transactional(readOnly = false)
public class EM00702 extends BaseApi implements IApi {

    private static final Logger logger = Logger.getLogger(EM00702.class);

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        // 返回结果
        Map<String, Object> result = Maps.newHashMap();
        Map<String, Object> param = request.getData();
        Long courseOutlineId = paramsLongFilter(param.get("courseOutlineId"));
        String name = paramsStringFilter(param.get("name"));
        //是提交还是保存
        Boolean isSubmit = paramsBooleanFilter(param.get("isSubmit"));
        List<LinkedHashMap> courseInfoMap = paramsJSONArrayFilter(param.get("courseInfos"), LinkedHashMap.class);
        List<LinkedHashMap> moduleMap = paramsJSONArrayFilter(param.get("modules"), LinkedHashMap.class);
        User user  = UserCacheKit.getUser(request.getHeader().getToken());

        logger.info(String.format("--------------------courseOutlineId:%s-------------courseOutlineName:%s---------------%s-------------",  courseOutlineId, name, DateUtil.dateToString(new Date(), DateUtil.FORMAT_ONE)));
        logger.info(String.format("%s %s", "courseInfo", JSON.toJSONString(courseInfoMap)));
        logger.info(String.format("%s %s", "module", JSON.toJSONString(moduleMap)));

        if(user == null ){
            return renderFAIL("0536", response, header);
        }

        if (courseOutlineId == null) {
            return renderFAIL("0531", response, header);
        }

        if (StrKit.isBlank(name)) {
            return renderFAIL("0860", response, header);
        }

        if(isSubmit == null){
            return  renderFAIL("0870", response, header);
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


        CcCourseOutline ccCourseOutline = CcCourseOutline.dao.findById(courseOutlineId);
        if(ccCourseOutline == null){
            return renderFAIL("0537", response, header);
        }
        if (ccCourseOutline.getInt("status") != CcCourseOutline.STATUS_AUDIT_DISMISSED && ccCourseOutline.getInt("status") > CcCourseOutline.STATUS_NOT_SUBMIT) {
            return renderFAIL("0861", response, header);
        }


        //保存大纲名称
        Date date = new Date();
        ccCourseOutline.set("modify_date", date);
        ccCourseOutline.set("name", name);
        ccCourseOutline.set("status", isSubmit ? CcCourseOutline.STATUS_PENDING_AUDIT : CcCourseOutline.STATUS_NOT_SUBMIT);

        //记录操作历史
        CcCourseOutlineHistory courseOutlineHistory = new CcCourseOutlineHistory();
        courseOutlineHistory.set("create_date", date);
        courseOutlineHistory.set("modify_date", date);
        courseOutlineHistory.set("outline_id", courseOutlineId);
        courseOutlineHistory.set("trigger_id", user.getLong("id"));
        courseOutlineHistory.set("event", String.format("教师(编号：%s)%s编写%s了【%s(编号：%s)】的课程大纲", user.getLong("id"), user.getStr("name"), isSubmit ? "保存" : "提交", course.getStr("name"), course.getLong("id")));
        courseOutlineHistory.set("event_type", CcCourseOutlineHistory.TYPE_COMPILE);
        courseOutlineHistory.set("is_del", Boolean.FALSE);

        if(!courseOutlineHistory.save()){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("isSuccess", false);
            return renderSUC(result, response, header);
        }

        //先删除教学大纲旧数据
        Long[] outlineIds = new Long[]{courseOutlineId};
        CcCourseOutlineService ccCourseOutlineService = SpringContextHolder.getBean(CcCourseOutlineService.class);
        if(!ccCourseOutlineService.deleteCourseOutline(outlineIds, date)){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("isSuccess", false);
            return renderSUC(result, response, header);
        }

        //保存与课程相关信息
        ServiceResponse serviceResponse = ccCourseOutlineService.saveCourseInfo(courseInfoMap, courseOutlineId);
        if(!serviceResponse.isSucc()){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return renderFAIL("0864", response, header, serviceResponse.getContent());
        }

        //保存大纲模块
        CcCourseOutlineModuleService ccCourseOutlineModuleService = SpringContextHolder.getBean(CcCourseOutlineModuleService.class);
        ServiceResponse moduleResponse = ccCourseOutlineModuleService.saveModules(moduleMap, course, isSubmit, ccCourseOutline);
        if(!moduleResponse.isSucc()){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return renderFAIL("0866", response, header, moduleResponse.getContent());
        }

        result.put("isSuccess", true);
        return renderSUC(result, response, header);
    }

}
