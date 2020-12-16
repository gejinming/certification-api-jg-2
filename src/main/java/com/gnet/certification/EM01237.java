package com.gnet.certification;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.response.ServiceResponse;
import com.gnet.service.CcstudentRaningLeveService;
import com.gnet.utils.PriceUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.*;

/**
 * @program: certification-api-jg-2
 * @description: 评分表分析法成绩分散到各个课程目标里
 * @author: Gjm
 * @create: 2020-12-09 15:15
 **/
@Service("EM01237")
public class EM01237 extends BaseApi implements IApi {
    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        HashMap<Object, Object> result = new HashMap<>();
        Map<String, Object> param = request.getData();
        Long courseGradecomposeId = paramsLongFilter(param.get("courseGradecomposeId"));
        Long eduClassId = paramsLongFilter(param.get("eduClassId"));
        Long majorId = paramsLongFilter(param.get("majorId"));
        Long batchId = paramsLongFilter(param.get("batchId"));
        if(courseGradecomposeId == null){
            return renderFAIL("0475", response, header);
        }
        if (eduClassId == null) {
            return renderFAIL("0500", response, header);
        }
        List<CcCourseGradecompose> courseGradecomposes = CcCourseGradecompose.dao.findFilteredByColumn("id", courseGradecomposeId);
        if (courseGradecomposes.size()==0){
            return renderFAIL("0471", response, header);
        }
        //成绩录入类型
        Integer inputScoreType = courseGradecomposes.get(0).getInt("input_score_type");
        //等级制
       /* Integer hierarchyLevel = courseGradecomposes.get(0).getInt("hierarchy_level");
         //更新成绩组成关联的课程目标的满分值，满分值算法等级制最大分*当前课程目标的比例系数
        CcRankingLevel ccRankingLevel = CcRankingLevel.dao.finLevelMaxScore(majorId, hierarchyLevel);
       if (ccRankingLevel == null){
            return renderFAIL("2584", response, header);
        }*/
        //Date date = new Date();
        //等级制最大分
        //BigDecimal score = ccRankingLevel.getBigDecimal("score");
        //计算课程目标的满分
        /*for (CcCourseGradecomposeIndication temps : ccCourseGradecomposeIndications){
            //比例系数
            BigDecimal scaleFactor = temps.getBigDecimal("scale_factor");
            temps.set("max_score", PriceUtils.mul(score,scaleFactor,2));
            temps.set("modify_date",date);
        }
        //更新课程目标的满分值
        if (!CcCourseGradecomposeIndication.dao.batchUpdate(ccCourseGradecomposeIndications,"max_score,modify_date")){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("isSuccess", false);
            return renderSUC(result, response, header);
        }*/
        CcstudentRaningLeveService cstudentRaningLeveService = SpringContextHolder.getBean(CcstudentRaningLeveService.class);
        ServiceResponse serviceResponse = cstudentRaningLeveService.mangeRaningLeveScore(courseGradecomposeId,inputScoreType,eduClassId,batchId);
        if(!serviceResponse.isSucc()){
            return renderFAIL("0804", response, header, serviceResponse.getContent());
        }
        result.put("isSuccess", true);

        return renderSUC(result, response, header);
    }
}
