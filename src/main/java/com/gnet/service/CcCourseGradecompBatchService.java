package com.gnet.service;

import com.gnet.model.admin.CcCourseGradecomposeBatch;
import com.gnet.model.admin.CcCourseGradecomposeDetailIndication;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: certification-api-jg-2
 * @description: 批次服务类
 * @author: Gjm
 * @create: 2020-07-07 17:21
 **/
@Component("ccCourseGradecompBatchService")
public class CcCourseGradecompBatchService {

    public boolean updateBatchScore(Long batchId){
        //更新批次的总分
        Date date = new Date();
        CcCourseGradecomposeBatch ccCourseGradecomposeBatch = new CcCourseGradecomposeBatch();
        BigDecimal sumScore= new BigDecimal("0");
        ArrayList<Long> batchIdList = new ArrayList<>();
        batchIdList.add(batchId);
        //统计题目各个课程目标点成绩
        List<CcCourseGradecomposeDetailIndication> indictionScore = CcCourseGradecomposeDetailIndication.dao.getIndictionScore(batchIdList);
        //总分
        for (CcCourseGradecomposeDetailIndication temps: indictionScore){
            BigDecimal scores = temps.getBigDecimal("score");
            sumScore=sumScore.add(scores);
        }
        ccCourseGradecomposeBatch.set("id",batchId);
        ccCourseGradecomposeBatch.set("modify_date",date);
        ccCourseGradecomposeBatch.set("score",sumScore);
        boolean update = ccCourseGradecomposeBatch.update();

        return  update;
    }
}
