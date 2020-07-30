package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.service.CcEduindicationStuScoreService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.jfinal.log.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @program: certification-api-jg-2
 * @description: 多批次管理新增
 * @author: Gjm
 * @create: 2020-07-02 15:02
 **/
@Transactional(readOnly = false)
@Service("EM01201")
public class EM01201 extends BaseApi implements IApi {
    private static final Logger logger = Logger.getLogger(EM01201.class);

    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        Map<String, Object> result = new HashMap();
        Map<String, Object> param = request.getData();
        Long courseGradeComposeId = paramsLongFilter(param.get("courseGradeComposeId"));
        String name = paramsStringFilter(param.get("name"));
        BigDecimal score = paramsBigDecimalFilter(param.get("score"));
        IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
        Date date = new Date();
        if(param.containsKey("courseGradeComposeId") && courseGradeComposeId == null) {
            return renderFAIL("1009", response, header, "courseGradeComposeId的参数值非法");
        }
        if (name == null){
            return renderFAIL("2557", response, header);
        }
        CcCourseGradecomposeBatch ccCourseGradecomposeBatch = new CcCourseGradecomposeBatch();
        ccCourseGradecomposeBatch.set("id",idGenerate.getNextValue());
        ccCourseGradecomposeBatch.set("create_date",date);
        ccCourseGradecomposeBatch.set("modify_date",date);
        ccCourseGradecomposeBatch.set("name",name);
        ccCourseGradecomposeBatch.set("score",score);
        ccCourseGradecomposeBatch.set("course_gradecompose_id",courseGradeComposeId);
        ccCourseGradecomposeBatch.set("is_del", Boolean.FALSE);

        if(!ccCourseGradecomposeBatch.save()){
            result.put("isSuccess", false);
            return renderSUC(result, response, header);
        }

        result.put("isSuccess", Boolean.TRUE);
        return renderSUC(result, response, header);
    }
}