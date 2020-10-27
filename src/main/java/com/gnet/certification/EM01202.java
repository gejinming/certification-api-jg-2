package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.*;
import com.gnet.object.CcCourseGradeComposeDetailOrderType;
import com.gnet.pager.Pageable;
import com.gnet.service.CcEduindicationStuScoreService;
import com.gnet.utils.ParamSceneUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: certification-api-jg-2
 * @description: 多批次管理查询列表
 * @author: Gjm
 * @create: 2020-07-02 15:02
 **/
@Transactional(readOnly = false)
@Service("EM01202")
public class EM01202 extends BaseApi implements IApi {


    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        Map<String, Object> result = new HashMap();
        Map<String, Object> param = request.getData();
        Long courseGradeComposeId = paramsLongFilter(param.get("courseGradeComposeId"));
        Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
        Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
        String orderProperty = paramsStringFilter(param.get("orderProperty"));
        String orderDirection = paramsStringFilter(param.get("orderDirection"));
        if(param.containsKey("courseGradeComposeId") && courseGradeComposeId == null) {
            return renderFAIL("1009", response, header, "courseGradeComposeId的参数值非法");
        }
        // 进行分页
        Pageable pageable = new Pageable(pageNumber, pageSize);
        // 排序处理
        try {
            ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcCourseGradeComposeDetailOrderType.class);
        } catch (NotFoundOrderPropertyException e) {
            return renderFAIL("0085", response, header);
        } catch (NotFoundOrderDirectionException e) {
            return renderFAIL("0086", response, header);
        }
        Map<String, Object> ccCourseGradeComposebatchsMap = Maps.newHashMap();
        Page<CcCourseGradecomposeBatch> ccCourseGradecomposeBatchPage = CcCourseGradecomposeBatch.dao.page(pageable, courseGradeComposeId);
        List<CcCourseGradecomposeBatch> batchsList = ccCourseGradecomposeBatchPage.getList();
        // 判断是否分页
        if(pageable.isPaging()){
            ccCourseGradeComposebatchsMap.put("totalRow", ccCourseGradecomposeBatchPage.getTotalRow());
            ccCourseGradeComposebatchsMap.put("totalPage", ccCourseGradecomposeBatchPage.getTotalPage());
            ccCourseGradeComposebatchsMap.put("pageSize", ccCourseGradecomposeBatchPage.getPageSize());
            ccCourseGradeComposebatchsMap.put("pageNumber", ccCourseGradecomposeBatchPage.getPageNumber());
        }


        List<Map<String, Object>> list = new ArrayList<>();

        for (CcCourseGradecomposeBatch temp: batchsList){
            //批次id List 用于统计题目各个课程目标点成绩
            ArrayList<Long> batchIdList = new ArrayList<>();
            Map<String, Object> ccCourseGradeComposeBatch = new HashMap<>();
            Integer inputType = temp.getInt("input_type");
            Long batchId = temp.getLong("id");
            ccCourseGradeComposeBatch.put("id", temp.get("id"));
            ccCourseGradeComposeBatch.put("name",temp.get("name"));
            ccCourseGradeComposeBatch.put("score",temp.get("score"));
            ccCourseGradeComposeBatch.put("inputType",temp.get("input_type"));
            ccCourseGradeComposeBatch.put("remark",temp.get("remark"));
            batchIdList.add(temp.getLong("id"));
            //统计题目各个课程目标点成绩
            if (inputType==3){
                List<CcCourseGradecomposeDetailIndication> indictionScore = CcCourseGradecomposeDetailIndication.dao.getIndictionScore(batchIdList);
                ccCourseGradeComposeBatch.put("indictionScore",indictionScore);
            }else{
                List<CcCourseGradecomposeBatchIndication> batchIndicationList = CcCourseGradecomposeBatchIndication.dao.findBatchIndicationList(batchId, null);
                ccCourseGradeComposeBatch.put("indictionScore",batchIndicationList);
            }


            list.add(ccCourseGradeComposeBatch);

        }



        ccCourseGradeComposebatchsMap.put("list",list);
        return renderSUC(ccCourseGradeComposebatchsMap, response, header);
    }
}