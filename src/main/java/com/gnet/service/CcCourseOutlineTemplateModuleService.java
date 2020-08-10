package com.gnet.service;

import com.gnet.model.admin.*;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.response.ServiceResponse;
import com.gnet.utils.ConvertUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

/**
 * 课程教学大纲模板模块
 *
 * @author xzl
 * @date 2017年8月1日
 */
@Component("ccCourseOutlineTemplateModuleService")
public class CcCourseOutlineTemplateModuleService {


    public ServiceResponse saveTemplateLists(List<LinkedHashMap> moduleMap, Long courseOutlineTemplateId) {
        Date date = new Date();
        IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
        List<CcCourseOutlineTemplateModule> ccCourseOutlineTemplateModuleList = Lists.newArrayList();
        List<CcCourseOutlineTemplateHeader> ccCourseOutlineTemplateHeaderList = Lists.newArrayList();
        List<CcCourseOutlineTemplateTableName> ccCourseOutlineTemplateTableNameList = Lists.newArrayList();

        for(int i=1; i<=moduleMap.size(); i++){
             Map<String, Object> map = moduleMap.get(i-1);
            String title = ConvertUtils.convert(map.get("title"), String.class);
            Boolean isExistMainContent = ConvertUtils.convert(map.get("isExistMainContent"), Boolean.class);
            Boolean isExistSecondaryContent = ConvertUtils.convert(map.get("isExistSecondaryContent"), Boolean.class);
            Boolean isExistTeachingContent = ConvertUtils.convert(map.get("isExistTeachingContent"), Boolean.class);
            Boolean isExistTable = ConvertUtils.convert(map.get("isExistTable"), Boolean.class);
            Boolean isMainContentSupport = ConvertUtils.convert(map.get("isMainContentSupport"), Boolean.class);
            Boolean isSecondaryContentSupport = ConvertUtils.convert(map.get("isSecondaryContentSupport"), Boolean.class);
            Boolean isTeachingContentSupport = ConvertUtils.convert(map.get("isTeachingContentSupport"), Boolean.class);
            Object secondaryContents = map.get("secondaryContents");
            //教学内容
            List<HashMap> teachingContentMap = map.containsKey("teachingContents") ? ConvertUtils.convert(map.get("teachingContents"),List.class) : new ArrayList<HashMap>();
            //次要内容
            List<HashMap> secondaryContentMap = map.containsKey("secondaryContents") ? ConvertUtils.convert(map.get("secondaryContents"),List.class) : new ArrayList<HashMap>();
            String mainContent = ConvertUtils.convert(map.get("mainContent"), String.class);
            String secondaryCountent=null;
            String teachingContent=null;
            if(!secondaryContentMap.isEmpty()){

                Map<String, Object> temp = secondaryContentMap.get(0);
                 secondaryCountent = temp.containsKey("content") ? ConvertUtils.convert(temp.get("content"), String.class) : null;
            }
            if (!teachingContentMap.isEmpty()){
                Map<String, Object> temp = teachingContentMap.get(0);
                teachingContent = temp.containsKey("basicRequirement") ? ConvertUtils.convert(temp.get("basicRequirement"), String.class) : null;

            }


            if (StringUtils.isBlank(title)) {
                return ServiceResponse.error(String.format("第%s个课程大纲模块的标题不能为空", i));
            }

            if(title.length() > 128){
                return ServiceResponse.error(String.format("第%s个课程大纲模块的标题长度不能大于128", i));
            }

            if(isExistMainContent == null){
                return  ServiceResponse.error(String.format("第%s个课程大纲模块是否存在主要内容不能为空", i));
            }
            if(isExistSecondaryContent == null){
                return  ServiceResponse.error(String.format("第%s个课程大纲模块是否存在次要内容不能为空", i));
            }
            if(isExistTeachingContent == null){
                return  ServiceResponse.error(String.format("第%s个课程大纲模块是否存在教学内容不能为空", i));
            }
            if(isExistTable == null){
                return  ServiceResponse.error(String.format("第%s个课程大纲模块是否存在表格不能为空", i));
            }
            if(isMainContentSupport == null){
                return  ServiceResponse.error(String.format("第%s个课程大纲模块主要内容是否支持课程目标不能为空", i));
            }
            if(isSecondaryContentSupport == null){
                return  ServiceResponse.error(String.format("第%s个课程大纲模块次要内容是否支持课程目标不能为空", i));
            }
            if(isTeachingContentSupport == null){
                return  ServiceResponse.error(String.format("第%s个课程大纲模块教学内容是否支持课程目标不能为空", i));
            }

            CcCourseOutlineTemplateModule ccCourseOutlineTemplateModule = new CcCourseOutlineTemplateModule();
            ccCourseOutlineTemplateModule.set("id", idGenerate.getNextValue());
            ccCourseOutlineTemplateModule.set("create_date", date);
            ccCourseOutlineTemplateModule.set("modify_date", date);
            ccCourseOutlineTemplateModule.set("course_outline_template_id", courseOutlineTemplateId);
            ccCourseOutlineTemplateModule.set("indexes",i);
            ccCourseOutlineTemplateModule.set("title", title);
            ccCourseOutlineTemplateModule.set("is_exist_main_content", isExistMainContent);
            ccCourseOutlineTemplateModule.set("is_exist_secondary_content", isExistSecondaryContent);
            ccCourseOutlineTemplateModule.set("is_exist_teaching_content", isExistTeachingContent);
            ccCourseOutlineTemplateModule.set("is_exist_table", isExistTable);
            ccCourseOutlineTemplateModule.set("is_main_content_support", isMainContentSupport);
            ccCourseOutlineTemplateModule.set("is_secondary_content_support", isSecondaryContentSupport);
            ccCourseOutlineTemplateModule.set("is_teaching_content_support", isTeachingContentSupport);
            ccCourseOutlineTemplateModule.set("is_del", Boolean.FALSE);
            ccCourseOutlineTemplateModule.set("main_content",mainContent);
            ccCourseOutlineTemplateModule.set("secondary_countent",secondaryCountent);
            ccCourseOutlineTemplateModule.set("teaching_content",teachingContent);
            ccCourseOutlineTemplateModuleList.add(ccCourseOutlineTemplateModule);

            List<HashMap> tables = map.containsKey("tables") ? ConvertUtils.convert(map.get("tables"), List.class) : new ArrayList<HashMap>();
            //如果存在表格但是没有表格信息则返回错误
            if(isExistTable && tables.isEmpty()){
                return  ServiceResponse.error(String.format("第%s个课程大纲模块存在表格但却没有表格信息", i));
            }
            if(isExistTable && !tables.isEmpty()){
                for(int j=1; j<=tables.size(); j++){
                    //表名
                    Map<String, Object> tableMap = tables.get(j-1);
                    String tableName = tableMap.containsKey("tableName") ? ConvertUtils.convert(tableMap.get("tableName"), String.class) : null;
                    String tableTopDetail = tableMap.containsKey("tableTopDetail") ? ConvertUtils.convert(tableMap.get("tableTopDetail"), String.class) : null;
                    String tableBottomDetail = tableMap.containsKey("tableBottomDetail") ? ConvertUtils.convert(tableMap.get("tableBottomDetail"), String.class) : null;
                    if(StrKit.notBlank(tableName)){
                        if(tableName.length() > 128){
                            return ServiceResponse.error(String.format("第%s个课程大纲模块的第%s的表格的表名长度不能大于128", i, j));
                        }
                        CcCourseOutlineTemplateTableName ccCourseOutlineTemplateTableName = new CcCourseOutlineTemplateTableName();
                        ccCourseOutlineTemplateTableName.set("id", idGenerate.getNextValue());
                        ccCourseOutlineTemplateTableName.set("create_date", date);
                        ccCourseOutlineTemplateTableName.set("modify_date", date);
                        ccCourseOutlineTemplateTableName.set("course_outline_template_id", courseOutlineTemplateId);
                        ccCourseOutlineTemplateTableName.set("indexes", i);
                        ccCourseOutlineTemplateTableName.set("number", j);
                        ccCourseOutlineTemplateTableName.set("table_name", tableName);
                        ccCourseOutlineTemplateTableName.set("table_top_detail",tableTopDetail);
                        ccCourseOutlineTemplateTableName.set("table_bottom_detail",tableBottomDetail);
                        ccCourseOutlineTemplateTableName.set("is_del", Boolean.FALSE);

                        ccCourseOutlineTemplateTableNameList.add(ccCourseOutlineTemplateTableName);
                    }

                    //保存模块表头信息
                    List<HashMap> headerMap = tableMap.containsKey("headers") ? ConvertUtils.convert(tableMap.get("headers"),List.class) : new ArrayList<HashMap>();
                    if(!headerMap.isEmpty()){
                        for(int n=1; n<=headerMap.size(); n++){
                            Map<String, Object> temp = headerMap.get(n-1);
                            String headerName = ConvertUtils.convert(temp.get("name"), String.class);
                            Integer type = ConvertUtils.convert(temp.get("type"), Integer.class);

                            if(StrKit.isBlank(headerName)){
                                return ServiceResponse.error(String.format("第%s个课程大纲模的第%s个表格的表头名称不能为空", i, j));
                            }

                            if(headerName.length() > 50){
                                return ServiceResponse.error(String.format("第%s个课程大纲模的第%s个表格的表头名称长度不能大于50", i, j));
                            }

                            if(type == null){
                                return ServiceResponse.error(String.format("第%s个课程大纲模的第%s个表格的表头类型不能为空", i, j));
                            }

                            CcCourseOutlineTemplateHeader ccCourseOutlineTemplateHeader = new CcCourseOutlineTemplateHeader();
                            ccCourseOutlineTemplateHeader.set("id", idGenerate.getNextValue());
                            ccCourseOutlineTemplateHeader.set("create_date", date);
                            ccCourseOutlineTemplateHeader.set("modify_date", date);
                            ccCourseOutlineTemplateHeader.set("indexes", i);
                            ccCourseOutlineTemplateHeader.set("number", j);
                            ccCourseOutlineTemplateHeader.set("name", headerName);
                            ccCourseOutlineTemplateHeader.set("type", type);
                            ccCourseOutlineTemplateHeader.set("column_ordinal", n);
                            ccCourseOutlineTemplateHeader.set("course_outline_template_id", courseOutlineTemplateId);
                            ccCourseOutlineTemplateHeader.set("hours_type", temp.containsKey("hoursType") ? ConvertUtils.convert(temp.get("hoursType"), Integer.class) : null);
                            ccCourseOutlineTemplateHeader.set("total_column", temp.containsKey("totalColumn") ? ConvertUtils.convert(temp.get("totalColumn"), String.class) : null);
                            ccCourseOutlineTemplateHeader.set("is_del", Boolean.FALSE);

                            ccCourseOutlineTemplateHeaderList.add(ccCourseOutlineTemplateHeader);
                        }
                    }
                }
            }

        }

        if (!CcCourseOutlineTemplateModule.dao.batchSave(ccCourseOutlineTemplateModuleList)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServiceResponse.error("保存课程大纲失败");
        }
        if(!ccCourseOutlineTemplateTableNameList.isEmpty()){
            if(!CcCourseOutlineTemplateTableName.dao.batchSave(ccCourseOutlineTemplateTableNameList)){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServiceResponse.error("保存课程大纲失败");
            }
        }
        if(!ccCourseOutlineTemplateHeaderList.isEmpty()){
           if(!CcCourseOutlineTemplateHeader.dao.batchSave(ccCourseOutlineTemplateHeaderList)){
               TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
               return ServiceResponse.error("保存课程大纲失败");
           }
        }

        return ServiceResponse.succ(true);
    }
}
