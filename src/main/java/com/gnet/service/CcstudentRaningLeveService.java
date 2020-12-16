package com.gnet.service;

import com.gnet.model.admin.*;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.response.ServiceResponse;
import com.gnet.utils.PriceUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.*;

/**
 * @program: certification-api-jg-2
 * @description: 处理评分表分析法的成绩
 * @author: Gjm
 * @create: 2020-12-09 17:40
 **/
@Component("CcstudentRaningLeveService")
public class CcstudentRaningLeveService {


    public ServiceResponse mangeRaningLeveScore(Long courseGradecomposeId,Integer inputScoreType,Long eduClassId,Long batchId) {
        IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
        Date date = new Date();
        ArrayList<CcScoreStuIndigrade> addCcScoreStuIndigradeList = new ArrayList<>();
        Set<Long> studentIds = new HashSet<>();
        List<Long> gradecomposeIndicationIdList = new ArrayList<>();
        ArrayList<CcCourseGradecomposeStudetail> addList = new ArrayList<>();
        ArrayList<Long> detailIds = new ArrayList<>();
        ArrayList<CcScoreStuIndigradeBatch> addScoreStuIndigradeBatches = new ArrayList<>();
        //当前成绩组成的学生评分成绩
        List<CcStudentEvalute> studentEvaluteGradeList = CcStudentEvalute.dao.findEvaluteGradeByEduClazzIdAndIndicationId(courseGradecomposeId, eduClassId,batchId);
        for (CcStudentEvalute temps : studentEvaluteGradeList) {
            //学生成绩
            BigDecimal studentScore = temps.getBigDecimal("score");
            Long studentId = temps.getLong("student_id");
            studentIds.add(studentId);
            //单批次直接录入方式直接汇总到成绩表里
            if (inputScoreType == 1) {
                List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndications = CcCourseGradecomposeIndication.dao.findcCourseGradecomposeIndicationList(courseGradecomposeId);
                for (CcCourseGradecomposeIndication temp : ccCourseGradecomposeIndications){
                    CcScoreStuIndigrade scoreStuIndigrade = new CcScoreStuIndigrade();
                    Long id = temp.getLong("id");
                    gradecomposeIndicationIdList.add(id);
                    BigDecimal scaleFactor = temp.getBigDecimal("scale_factor");
                    //根据比例系数把成绩分散到每个开课课程成绩组成元素与课程目标关联的成绩值，也就是这个成绩组成对应课程目标的成绩值
                    BigDecimal indicationScore = PriceUtils.mul(studentScore, scaleFactor,2);

                        scoreStuIndigrade.set("id",idGenerate.getNextValue());
                        scoreStuIndigrade.set("create_date",date);
                        scoreStuIndigrade.set("modify_date",date);
                        scoreStuIndigrade.set("gradecompose_indication_id",id);
                        scoreStuIndigrade.set("student_id",studentId);
                        scoreStuIndigrade.set("grade",indicationScore);
                        scoreStuIndigrade.set("type",2);
                        scoreStuIndigrade.set("is_del",Boolean.FALSE);
                        addCcScoreStuIndigradeList.add(scoreStuIndigrade);

                }


            }
            //单批次题目录入方式
            if (inputScoreType == 2 ||inputScoreType == 3) {


                //获取题目信息
                List<CcCourseGradeComposeDetail> courseGradeComposeDetails = CcCourseGradeComposeDetail.dao.topicList0(courseGradecomposeId, batchId);
                for (CcCourseGradeComposeDetail temp : courseGradeComposeDetails){
                    CcCourseGradecomposeStudetail ccCourseGradecomposeStudetail = new CcCourseGradecomposeStudetail();
                    Long detailid = temp.getLong("id");
                    detailIds.add(detailid);
                    //题目的比例系数
                    BigDecimal scaleFactor = temp.getBigDecimal("scale_factor");
                    //题目成绩=比例系数*评分等级分数
                    BigDecimal Score = PriceUtils.mul(studentScore, scaleFactor,2);
                        ccCourseGradecomposeStudetail.set("id",idGenerate.getNextValue());
                        ccCourseGradecomposeStudetail.set("create_date",date);
                        ccCourseGradecomposeStudetail.set("modify_date",date);
                        ccCourseGradecomposeStudetail.set("student_id",studentId);
                        ccCourseGradecomposeStudetail.set("detail_id",detailid);
                        ccCourseGradecomposeStudetail.set("score",Score);
                        ccCourseGradecomposeStudetail.set("is_del",Boolean.FALSE);
                        addList.add(ccCourseGradecomposeStudetail);


                }


            }
            //多批次直接录入方式
            if (inputScoreType == 4) {
                List<CcCourseGradecomposeBatchIndication> batchIndicationList = CcCourseGradecomposeBatchIndication.dao.findBatchIndicationList(batchId, null);
                for (CcCourseGradecomposeBatchIndication temp : batchIndicationList){
                    Long id = temp.getLong("id");
                    //当前批次的比例系数
                    BigDecimal scaleFactor = temp.getBigDecimal("scale_factor");
                    //评分成绩*比例系数
                    BigDecimal Score = PriceUtils.mul(studentScore, scaleFactor,2);
                    //把分数写入批次直接录入表里cc_score_stu_indigrade_batch
                    CcScoreStuIndigradeBatch cScoreStuIndigradeBatch = new CcScoreStuIndigradeBatch();
                    cScoreStuIndigradeBatch.set("id", idGenerate.getNextValue());
                    cScoreStuIndigradeBatch.set("create_date", date);
                    cScoreStuIndigradeBatch.set("modify_date", date);
                    cScoreStuIndigradeBatch.set("student_id", studentId);
                    cScoreStuIndigradeBatch.set("grade", Score);
                    cScoreStuIndigradeBatch.set("is_del", Boolean.FALSE);
                    cScoreStuIndigradeBatch.set("batch_id",batchId);
                    cScoreStuIndigradeBatch.set("type",2);
                    cScoreStuIndigradeBatch.set("gradecompose_indication_id",id );
                    addScoreStuIndigradeBatches.add(cScoreStuIndigradeBatch);
                }
            }

        }
       if (inputScoreType==1 && addCcScoreStuIndigradeList.size()>0){
           //先删除所有的当前成绩组成的课程目标成绩再添加
           if (!CcScoreStuIndigrade.dao.deleAllGradecomposeScore(gradecomposeIndicationIdList)){
               TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
               return ServiceResponse.error("批量删除成绩失败 ");
           }
           if (addCcScoreStuIndigradeList.size()!=0){
               Boolean isSuccess = CcScoreStuIndigrade.dao.batchSave(addCcScoreStuIndigradeList);
               if(!isSuccess) {
                   TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                   return ServiceResponse.error("批量更新成绩失败 ");
               }
           }
       }

        if ((inputScoreType == 2 ||inputScoreType == 3) && addList.size()>0) {
            //先删除所有再添加
            if (!CcCourseGradecomposeStudetail.dao.deleAllGradecomposeDetailScore(detailIds)){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServiceResponse.error("批量删除成绩失败 ");
            }
            if (addList.size()!=0){
                Boolean isSuccess = CcCourseGradecomposeStudetail.dao.batchSave(addList);
                if(!isSuccess) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ServiceResponse.error("批量更新成绩失败 ");
                }
            }
            //把题目成绩汇总到scoreStuIndigrade表
            List<CcCourseGradecomposeIndication> courseGradecomposeIndicationList = CcCourseGradecomposeIndication.dao.findByDetailIdAndCourseGradecomposeId(courseGradecomposeId, detailIds.toArray(new Long[detailIds.size()]));
            //可能存在重复
            Set<Long> courseGradecomposeIndicationIds = new HashSet<>();
            if (!courseGradecomposeIndicationList.isEmpty()) {
                for (int i = 0; i < courseGradecomposeIndicationList.size(); i++) {
                    courseGradecomposeIndicationIds.add(courseGradecomposeIndicationList.get(i).getLong("id"));
                }
            }

            CcCourseGradecomposeDetailService courseGradecomposeDetailService = SpringContextHolder.getBean(CcCourseGradecomposeDetailService.class);
            if (!courseGradecomposeDetailService.batchUpdateStudentGrade(courseGradecomposeIndicationIds.toArray(new Long[courseGradecomposeIndicationIds.size()]), studentIds.toArray(new Long[studentIds.size()]), detailIds.toArray(new Long[detailIds.size()]), courseGradecomposeId)) {
                return ServiceResponse.error("更新题目成绩失败 ");
            }
        }
        if (inputScoreType==4 && addScoreStuIndigradeBatches.size()>0){
            if (batchId !=null ){
                // 删除该课程下的学生成绩
                if(!CcScoreStuIndigradeBatch.dao.deleteByModel(addScoreStuIndigradeBatches)) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ServiceResponse.error("批量删除成绩失败 ");
                }

                Boolean isSuccess = CcScoreStuIndigradeBatch.dao.batchSave(addScoreStuIndigradeBatches);
                if(!isSuccess) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ServiceResponse.error("更新批次成绩失败 ");
                }

                //处理当前成绩组成的批次成绩
                CcCourseGradecompBatchService  ccCourseGradecompBatchService=SpringContextHolder.getBean(CcCourseGradecompBatchService.class);
                boolean saveState = ccCourseGradecompBatchService.saveScoreIndiction(courseGradecomposeId);
                if (!saveState){
                    return ServiceResponse.error("更新批次成绩汇总失败 ");
                }

            }
        }
        return ServiceResponse.succ(true);
    }

}
