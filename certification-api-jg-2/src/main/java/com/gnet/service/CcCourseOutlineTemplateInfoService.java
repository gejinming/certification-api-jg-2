package com.gnet.service;

import com.gnet.model.admin.CcCourseOutlineTemplateInfo;
import com.gnet.model.admin.CcCourseOutlineTemplateModule;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.response.ServiceResponse;
import com.gnet.utils.ConvertUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

/**
 * 课程教学大纲模板基本信息
 *
 * @author xzl
 * @date 2017年8月2日
 */
@Component("ccCourseOutlineTemplateInfoService")
public class CcCourseOutlineTemplateInfoService {


    public ServiceResponse saveTemplateInfo(List<LinkedHashMap> courseInfoMap, Long courseOutlineTemplateId) {
        Date date = new Date();
        IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
        List<CcCourseOutlineTemplateInfo> ccCourseOutlineTemplateInfoList = Lists.newArrayList();

        for(int i=1; i<=courseInfoMap.size(); i++){
            Map<String, Object> map = courseInfoMap.get(i-1);
            CcCourseOutlineTemplateInfo ccCourseOutlineTemplateInfo = new CcCourseOutlineTemplateInfo();
            String courseInfoName = ConvertUtils.convert(map.get("name"), String.class);
            String databaseField = ConvertUtils.convert(map.get("databaseField"), String.class);

            if (StrKit.isBlank(courseInfoName)) {
                return ServiceResponse.error(String.format("第%s个课程大纲中与课程相关的课程基本信息名称不能为空", i));
            }
            if(courseInfoName.length() > 50){
                return ServiceResponse.error(String.format("第%s个课程大纲中与课程相关的课程基本信息名称长度不能大于50",i));
            }

            ccCourseOutlineTemplateInfo.set("id", idGenerate.getNextValue());
            ccCourseOutlineTemplateInfo.set("create_date", date);
            ccCourseOutlineTemplateInfo.set("modify_date", date);
            ccCourseOutlineTemplateInfo.set("course_outline_template_id", courseOutlineTemplateId);
            ccCourseOutlineTemplateInfo.set("name", courseInfoName);
            ccCourseOutlineTemplateInfo.set("database_field", databaseField);
            ccCourseOutlineTemplateInfo.set("is_del", Boolean.FALSE);

            ccCourseOutlineTemplateInfoList.add(ccCourseOutlineTemplateInfo);
        }

        if (!CcCourseOutlineTemplateInfo.dao.batchSave(ccCourseOutlineTemplateInfoList)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServiceResponse.error("保存课程大纲模板失败");
        }

        return ServiceResponse.succ(true);
    }
}
