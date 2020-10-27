package com.gnet.service;

import com.gnet.model.admin.*;
import com.gnet.pager.Pageable;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.PriceUtils;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.plugin.activerecord.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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
    /*
     * @param batchId
     * @return boolean
     * @author Gejm
     * @description: 多批次直接录入方式成绩处理
     * 处理方式，先合并整个成绩组成的所有批次成绩（根据开课课程成绩组成元素编号合并）
     * ，然后合并后的成绩除以这个成绩组成的批次数量，保存到cc_score_stu_indigrade表中
     * @date 2020/8/11 15:52
     */
    public boolean saveScoreIndiction(Long courseGradeComposeId){
        IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
        Boolean isSuccess=true;
        Date date = new Date();

        //统计成绩（先合并整个成绩组成的所有批次成绩（根据开课课程成绩组成元素编号合并））
        List<CcScoreStuIndigradeBatch> batchStudentScore = CcScoreStuIndigradeBatch.dao.sumScoreStuIndigrade(courseGradeComposeId);
        List<CcScoreStuIndigrade> scoreStuIndigradeAddList = new ArrayList<>();

        if (batchStudentScore !=null){
            for (CcScoreStuIndigradeBatch temp:batchStudentScore ){
                Long indicationId = temp.getLong("indication_id");
                //TODO 2020/9/2改为除以批次数量
                // 进行分页
                Pageable pageable = new Pageable(null, null);
                Page<CcCourseGradecomposeBatch> ccCourseGradecomposeBatchPage = CcCourseGradecomposeBatch.dao.page(pageable, courseGradeComposeId);
                List<CcCourseGradecomposeBatch> batchsList = ccCourseGradecomposeBatchPage.getList();
                //查询包含此课程目标的批次数量
                //List<CcCourseGradecomposeBatchIndication> ccCourseGradecomposeBatchIndications = CcCourseGradecomposeBatchIndication.dao.indicationBatchList(courseGradeComposeId, indicationId);
                int batchNum=batchsList.size();
                if (batchNum==0){
                    continue;
                }
                BigDecimal batchNums = BigDecimal.valueOf(batchNum);

                //处理成绩（合并后的成绩除以这个成绩组成的批次数量，保存到cc_score_stu_indigrade表中）
                BigDecimal grades = temp.getBigDecimal("grade");
                BigDecimal grade = PriceUtils.div(grades, batchNums, 2);
                CcScoreStuIndigrade scoreStuIndigrade = new CcScoreStuIndigrade();
                scoreStuIndigrade.set("id", idGenerate.getNextValue());
                scoreStuIndigrade.set("create_date", date);
                scoreStuIndigrade.set("modify_date", date);
                scoreStuIndigrade.set("gradecompose_indication_id",  temp.getLong("gradecompose_indication_id"));
                scoreStuIndigrade.set("student_id", temp.getLong("student_id"));
                scoreStuIndigrade.set("grade", grade);
                scoreStuIndigrade.set("is_del", Boolean.FALSE);
                scoreStuIndigradeAddList.add(scoreStuIndigrade);
            }
            //先删除再添加
            // 删除该课程下的学生成绩
            if(!CcScoreStuIndigrade.dao.deleteByModel(scoreStuIndigradeAddList)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }

             isSuccess = CcScoreStuIndigrade.dao.batchSave(scoreStuIndigradeAddList);


        }

        return isSuccess;
    }
    /*
     * @param courseGradeComposeId
     * @return boolean
     * @author Gejm
     * @description: 批次直接录入方式汇总课程目标总分
     * 批次汇总后的课程目标总分除以批次数量即是此成绩组成此课程目标的总分
     * @date 2020/8/27 11:17
     */
    public boolean batchIndicationScore(Long courseGradeComposeId){
        IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
        //查询成绩组成所有批次课程目标的总分
        List<CcCourseGradecomposeBatchIndication> batchIndicationScore = CcCourseGradecomposeBatchIndication.dao.indicationSumScore(courseGradeComposeId);
        List<CcCourseGradecomposeIndication> updateList = new ArrayList<>();
        List<CcCourseGradecomposeIndication> addList = new ArrayList<>();
        Date date = new Date();
        for (CcCourseGradecomposeBatchIndication temp:batchIndicationScore){
            Long indicationId = temp.getLong("indication_id");
            BigDecimal score = temp.getBigDecimal("score");
            //TODO 2020/9/2改为除以批次数量
            // 进行分页
            Pageable pageable = new Pageable(null, null);
            Page<CcCourseGradecomposeBatch> ccCourseGradecomposeBatchPage = CcCourseGradecomposeBatch.dao.page(pageable, courseGradeComposeId);
            List<CcCourseGradecomposeBatch> batchsList = ccCourseGradecomposeBatchPage.getList();
            //查询包含此课程目标的批次数量
           // List<CcCourseGradecomposeBatchIndication> ccCourseGradecomposeBatchIndications = CcCourseGradecomposeBatchIndication.dao.indicationBatchList(courseGradeComposeId, indicationId);
            int batchNum=batchsList.size();
            if (batchNum !=0 && !score.equals(null) && !score.equals("")) {
                //成绩组成的课程目标总分=score/batchNum
                BigDecimal number = new BigDecimal(batchNum);
                BigDecimal divideScore = PriceUtils.div(score, number, 2);
                CcCourseGradecomposeIndication ccCourseGradecomposeIndication = new CcCourseGradecomposeIndication();
                ccCourseGradecomposeIndication.put("create_date",date);
                ccCourseGradecomposeIndication.put("modify_date",date);
                ccCourseGradecomposeIndication.put("indication_id",indicationId);
                ccCourseGradecomposeIndication.put("course_gradecompose_id",courseGradeComposeId);
                ccCourseGradecomposeIndication.put("max_score",divideScore);

                //判断是否存在
                List<CcCourseGradecomposeIndication> gradecomposeIndication = CcCourseGradecomposeIndication.dao.findGradecomposeIndication(courseGradeComposeId, indicationId);
                if (gradecomposeIndication.size() !=0){
                    ccCourseGradecomposeIndication.put("id",gradecomposeIndication.get(0).getLong("id"));
                    updateList.add(ccCourseGradecomposeIndication);
                }else {
                    ccCourseGradecomposeIndication.put("is_del",Boolean.FALSE);
                    ccCourseGradecomposeIndication.put("id",idGenerate.getNextValue());
                    addList.add(ccCourseGradecomposeIndication);
                }
            }

        }
        if (!addList.isEmpty()) {
            if (!CcCourseGradecomposeIndication.dao.batchSave(addList)) {

                return false;
            }
        }
        if (!updateList.isEmpty()){

            if (!CcCourseGradecomposeIndication.dao.batchUpdate(updateList,"modify_date,max_score")){

                return false;
            }
        }

        return true;
    }
}
