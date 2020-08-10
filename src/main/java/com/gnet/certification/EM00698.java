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
import com.gnet.utils.ConvertUtils;
import com.gnet.utils.DateUtil;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.*;

/**
 * 教师根据大纲模板创建课程大纲
 *
 * @author xzl
 * @date 2017年8月1日
 */
@Service("EM00698")
@Transactional(readOnly = false)
public class EM00698 extends BaseApi implements IApi {

    private static final Logger logger = Logger.getLogger(EM00698.class);

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
        // 返回结果
        Map<String, Object> result = Maps.newHashMap();
        Map<String, Object> param = request.getData();
        Long courseOutlineId = paramsLongFilter(param.get("courseOutlineId"));
        String name = paramsStringFilter(param.get("name"));
        List<LinkedHashMap> courseInfoMap = paramsJSONArrayFilter(param.get("courseInfos"), LinkedHashMap.class);
        List<LinkedHashMap> moduleMap = paramsJSONArrayFilter(param.get("modules"), LinkedHashMap.class);
        User user  = UserCacheKit.getUser(request.getHeader().getToken());
        Long outlineTemplateId =  paramsLongFilter(param.get("outlineTemplateId"));

        if(user == null ){
            return renderFAIL("0536", response, header);
        }

        if (StrKit.isBlank(name)) {
            return renderFAIL("0860", response, header);
        }

        if(outlineTemplateId == null){
            return renderFAIL("0869", response, header);
        }

        //完善大纲信息
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

        //保存前验证该模板是否合理
        BigDecimal theoryHours = course.getBigDecimal("theory_hours"),
                operateComputerHours = course.getBigDecimal("operate_computer_hours"),
                experimentHours = course.getBigDecimal("experiment_hours"),
                practiceHours = course.getBigDecimal("practice_hours"),
                dicussHours = course.getBigDecimal("dicuss_hours"),
                exercisesHours = course.getBigDecimal("exercises_hours");

        List<CcCourseOutlineTemplateHeader> ccCourseOutlineTemplateHeaders = CcCourseOutlineTemplateHeader.dao.findFilteredByColumn("course_outline_template_id", outlineTemplateId);
        //大纲模板包含的表头类型
        List<Integer> hourTypes = Lists.newArrayList();
        for(CcCourseOutlineTemplateHeader ccCourseOutlineTemplateHeader : ccCourseOutlineTemplateHeaders){
            hourTypes.add(ccCourseOutlineTemplateHeader.getInt("hours_type"));
        }

        CcCourseOutline ccCourseOutline = CcCourseOutline.dao.findById(courseOutlineId);
        //如果大纲属于教学大纲，则在负责人发布任务时则可以验证大纲表格表头是否合理
        if(CcCourseOutlineType.NAME.equals(ccCourseOutline.getStr("outlineTypeName"))){
            if(CcCourse.TYPE_PRACTICE.equals(course.getInt("type"))){
                Boolean isExistHoursType = hourTypes.contains(CcCourseOutlineHeader.CURRICULAR_HOURS) || hourTypes.contains(CcCourseOutlineHeader.DAYS) || hourTypes.contains(CcCourseOutlineHeader.WEEKS);
                if(hourTypes == null || !isExistHoursType){
                    return renderFAIL("0872", response, header, "教学大纲的表格的表头没有关联天数、课内学时、周数其中的一种");
                }
            }else{
                if(theoryHours != null && !hourTypes.contains(CcCourseOutlineHeader.THEORY_HOURS)){
                    return renderFAIL("0872", response, header, "课程存在理论学时，教学大纲的表格的表头没有关联理论学时");
                }
                if(operateComputerHours != null && !hourTypes.contains(CcCourseOutlineHeader.OPERATECOMPUTER_HOURS)){
                    return renderFAIL("0872", response, header, "课程存在上机学时，教学大纲的表格的表头没有关联上机学时");
                }
                if(experimentHours != null && !hourTypes.contains(CcCourseOutlineHeader.EXPERIMENT_HOURS)){
                    return renderFAIL("0872", response, header, "课程存在实验学时，教学大纲的表格的表头没有关联实验学时");
                }
                if(practiceHours != null && !hourTypes.contains(CcCourseOutlineHeader.PRACTICE_HOURS)){
                    return renderFAIL("0872", response, header, "课程存在实践学时，教学大纲的表格的表头没有关联实践学时");
                }
                if(dicussHours != null && !hourTypes.contains(CcCourseOutlineHeader.DICUSS_HOURS)){
                    return renderFAIL("0872", response, header, "课程存在研讨学时，教学大纲的表格的表头没有关联研讨学时");
                }
                if(exercisesHours != null && !hourTypes.contains(CcCourseOutlineHeader.EXERCISES_HOURS)){
                    return renderFAIL("0872", response, header, "课程存在习题学时，教学大纲的表格的表头没有关联习题学时");
                }
            }
        }

        Date date = new Date();
        Long originOutlineTemplateId = ccCourseOutline.getLong("outline_template_id");
        Long courseOutId = ccCourseOutline.getLong("id");

        if(!name.equals(ccCourseOutline.getStr("name")) || !outlineTemplateId.equals(originOutlineTemplateId)){
            ccCourseOutline.set("modify_date", date);
            ccCourseOutline.set("outline_template_id", outlineTemplateId);
            if (!ccCourseOutline.update()) {
                result.put("isSuccess", false);
                return renderSUC(result, response, header);
            }
        }

        logger.info(String.format("--------------------courseOutlineId:%s-------------courseOutlineName:%s---------------%s-------------",  courseOutlineId, name, DateUtil.dateToString(new Date(), DateUtil.FORMAT_ONE)));
        logger.info(String.format("%s %s", "courseInfo", JSON.toJSONString(courseInfoMap)));
        logger.info(String.format("%s %s", "module", JSON.toJSONString(moduleMap)));


        if(!outlineTemplateId.equals(originOutlineTemplateId)){
            //先删除教学大纲旧数据
            if(!CcCourseOutlineCourseInfo.dao.deleteAllByColumn(CcCourseOutline.COURSE_OUTLINE_ID, courseOutlineId, date)){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.put("isSuccess", false);
                return renderSUC(result, response, header);
            }

            if(!CcCourseOutlineModule.dao.deleteAllByColumn(CcCourseOutline.COURSE_OUTLINE_ID, courseOutlineId, date)){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.put("isSuccess", false);
                return renderSUC(result, response, header);
            }

            if(!CcCourseOutlineTableName.dao.deleteAllByColumn(CcCourseOutline.COURSE_OUTLINE_ID, courseOutlineId, date)){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.put("isSuccess", false);
                return renderSUC(result, response, header);
            }

            if(!CcCourseOutlineHeader.dao.deleteAllByColumn(CcCourseOutline.COURSE_OUTLINE_ID, courseOutlineId, date)){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.put("isSuccess", false);
                return renderSUC(result, response, header);
            }
            //保存大纲模板里的教学内容和次要内容到课程大纲教学内容、次要内容中
            //模块教学内容
            List<CcCourseOutlineTeachingContent> ccCourseOutlineTeachingContentList = Lists.newArrayList();
            //模块次要内容
            List<CcCourseOutlineSecondaryContent> ccCourseOutlineSecondaryContentList = Lists.newArrayList();
            for(int i=1 ; i<= moduleMap.size(); i++){
                Map<String, Object> map = moduleMap.get(i-1);
                //教学内容
                List<HashMap> teachingContentMap = map.containsKey("teachingContents") ? ConvertUtils.convert(map.get("teachingContents"),List.class) : new ArrayList<HashMap>();
                //次要内容
                List<HashMap> secondaryContentMap = map.containsKey("secondaryContents") ? ConvertUtils.convert(map.get("secondaryContents"),List.class) : new ArrayList<HashMap>();
                //教学内容
                if(!teachingContentMap.isEmpty()){
                    for(int k=1; k<= teachingContentMap.size(); k++){

                        Map<String, Object> temp = teachingContentMap.get(k-1);
                        String teachingContent = temp.containsKey("teachingContent") ? ConvertUtils.convert(temp.get("teachingContent"), String.class) : null;
                        String basicRequirement = temp.containsKey("basicRequirement") ? ConvertUtils.convert(temp.get("basicRequirement"), String.class) : null;


                        CcCourseOutlineTeachingContent ccCourseOutlineTeachingContent = new CcCourseOutlineTeachingContent();
                        ccCourseOutlineTeachingContent.set("id", idGenerate.getNextValue());
                        ccCourseOutlineTeachingContent.set("create_date", date);
                        ccCourseOutlineTeachingContent.set("modify_date", date);
                        ccCourseOutlineTeachingContent.set("indexes", i);
                        ccCourseOutlineTeachingContent.set("teaching_content", teachingContent);

                        ccCourseOutlineTeachingContent.set("basic_requirement", basicRequirement);

                        ccCourseOutlineTeachingContent.set(CcCourseOutline.COURSE_OUTLINE_ID, courseOutlineId);
                        ccCourseOutlineTeachingContent.set("is_del", Boolean.FALSE);

                        ccCourseOutlineTeachingContentList.add(ccCourseOutlineTeachingContent);

                    }
                }
                //次要内容
                if(!secondaryContentMap.isEmpty()){
                    for(int k=1; k<= secondaryContentMap.size(); k++ ){
                        Map<String, Object> temp = secondaryContentMap.get(k-1);
                        //String contentTitle = temp.containsKey("title") ? ConvertUtils.convert(temp.get("title"), String.class) : null;
                        String content = temp.containsKey("content") ? ConvertUtils.convert(temp.get("content"), String.class) : null;

                        CcCourseOutlineSecondaryContent ccCourseOutlineSecondaryContent = new CcCourseOutlineSecondaryContent();
                        ccCourseOutlineSecondaryContent.set("id", idGenerate.getNextValue());
                        ccCourseOutlineSecondaryContent.set("create_date", date);
                        ccCourseOutlineSecondaryContent.set("modify_date", date);
                        ccCourseOutlineSecondaryContent.set("indexes", i);
                        //ccCourseOutlineSecondaryContent.set("title", contentTitle);
                        ccCourseOutlineSecondaryContent.set("content", content);
                       // ccCourseOutlineSecondaryContent.set("indications", indications);
                        ccCourseOutlineSecondaryContent.set(CcCourseOutline.COURSE_OUTLINE_ID, courseOutlineId);
                        ccCourseOutlineSecondaryContent.set("is_del", Boolean.FALSE);

                        ccCourseOutlineSecondaryContentList.add(ccCourseOutlineSecondaryContent);
                    }
                }
            }
            if(!ccCourseOutlineTeachingContentList.isEmpty()){
                if(!CcCourseOutlineTeachingContent.dao.batchSave(ccCourseOutlineTeachingContentList)){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

                }
            }

            if(!ccCourseOutlineSecondaryContentList.isEmpty()){
                if(!CcCourseOutlineSecondaryContent.dao.batchSave(ccCourseOutlineSecondaryContentList)){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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


            CcCourseOutlineHistory courseOutlineHistory = new CcCourseOutlineHistory();
            courseOutlineHistory.set("create_date", date);
            courseOutlineHistory.set("modify_date", date);
            courseOutlineHistory.set("outline_id", courseOutlineId);
            courseOutlineHistory.set("trigger_id", user.getLong("id"));
            courseOutlineHistory.set("event", String.format("负责人(编号：%s)%s%s根据【%s(编号：%s)】创建了%s课程的%s的课程大纲", user.getLong("id"), user.getStr("name"), "创建", ccCourseOutline.getStr("outlineTemplateName"), ccCourseOutline.getLong("outline_template_id"), course.getStr("name"), course.getStr("outlineTypeName")));
            courseOutlineHistory.set("event_type", CcCourseOutlineHistory.TYPE_CREATE);
            courseOutlineHistory.set("is_del", Boolean.FALSE);

            if(!courseOutlineHistory.save()){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.put("isSuccess", false);
                return renderSUC(result, response, header);
            }
        }

        result.put("isSuccess", true);
        return renderSUC(result, response, header);
    }

}
