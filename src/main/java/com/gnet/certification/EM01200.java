package com.gnet.certification;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradecomposeIndication;
import com.gnet.model.admin.CcCourseGradecomposeStudetail;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.service.CcCourseGradecomposeDetailService;
import com.gnet.service.CcEduindicationStuScoreService;
import com.gnet.utils.ConvertUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.*;

/**
 * @program: certification-api-jg-2
 * @description: 批量更新题目成绩，替换了原来的单个题目录入，EM00612、613
 * @author: Gjm
 * @create: 2020-07-02 17:24
 **/
@Transactional(readOnly = false)
@Service("EM01200")
public class EM01200 extends BaseApi implements IApi {

    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        Map<String, Object> result = new HashMap();
        Map<String, Object> param = request.getData();
        Long eduClassId = paramsLongFilter(param.get("eduClassId"));
        List<Map<String, Object>> scoreStuIndigrade = ConvertUtils.convert(param.get("scoreStuIndigrade"), List.class);
        Long courseGradeComposeId = paramsLongFilter(param.get("courseGradeComposeId"));
        IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
        Long batchId = paramsLongFilter(param.get("batchId"));

        if(courseGradeComposeId == null){
            return  renderFAIL("0475", response, header);
        }
        if (eduClassId == null) {
            return renderFAIL("0380", response, header);
        }
        if (scoreStuIndigrade == null) {
            return renderFAIL("0460", response, header);
        }
        //需要新增的学生题目分数
        List<CcCourseGradecomposeStudetail> addList = Lists.newArrayList();
        //需要更新的学生题目分数
        List<CcCourseGradecomposeStudetail> editList = Lists.newArrayList();
        Date date = new Date();
        Set<Long> detailIds = new HashSet<>();
        Set<Long> studentIds = new HashSet<>();
        for (int i=0;i<scoreStuIndigrade.size();i++){
            JSONObject map = (JSONObject) scoreStuIndigrade.get(i);
            Long studentId = map.getLong("studentId");
            Long detailId = map.getLong("detailId");
            BigDecimal score = map.getBigDecimal("grade");
            studentIds.add(studentId);
            detailIds.add(detailId);
            Long id = map.getLong("id");
            //判断是否已经录入
            boolean existStudentGrade = CcCourseGradecomposeStudetail.dao.isExistStudentGrade(studentId, detailId);
            //修改
            if (existStudentGrade){
                CcCourseGradecomposeStudetail ccCourseGradecomposeStudetail = new CcCourseGradecomposeStudetail();
                ccCourseGradecomposeStudetail.set("id", id);
                ccCourseGradecomposeStudetail.set("modify_date", date);
                ccCourseGradecomposeStudetail.set("score", score);
                if (score==null){
                    ccCourseGradecomposeStudetail.set("is_del", true);
                }else{
                    ccCourseGradecomposeStudetail.set("is_del", false);
                }
                editList.add(ccCourseGradecomposeStudetail);
            }else {
                CcCourseGradecomposeStudetail ccCourseGradecomposeStudetail = new CcCourseGradecomposeStudetail();
                ccCourseGradecomposeStudetail.set("id", idGenerate.getNextValue());
                ccCourseGradecomposeStudetail.set("create_date", date);
                ccCourseGradecomposeStudetail.set("modify_date", date);
                ccCourseGradecomposeStudetail.set("student_id", studentId);
                ccCourseGradecomposeStudetail.set("detail_id", detailId);
                ccCourseGradecomposeStudetail.set("score", score);
                ccCourseGradecomposeStudetail.set("is_del", false);
                addList.add(ccCourseGradecomposeStudetail);
            }

        }
        if(!addList.isEmpty() && !CcCourseGradecomposeStudetail.dao.batchSave(addList)){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("isSuccess", false);
            return renderSUC(result, response, header);
        }

        if(!editList.isEmpty() && !CcCourseGradecomposeStudetail.dao.batchUpdate(editList, "modify_date, score,is_del")){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("isSuccess", false);
            return renderSUC(result, response, header);
        }

        List<CcCourseGradecomposeIndication> courseGradecomposeIndicationList = CcCourseGradecomposeIndication.dao.findByDetailIdAndCourseGradecomposeId(courseGradeComposeId, detailIds.toArray(new Long[detailIds.size()]));
        //可能存在重复

        Set<Long> courseGradecomposeIndicationIds = new HashSet<>();
        if(!courseGradecomposeIndicationList.isEmpty()){
            for(int i=0; i<courseGradecomposeIndicationList.size(); i++ ){
                courseGradecomposeIndicationIds.add(courseGradecomposeIndicationList.get(i).getLong("id"));
            }
        }

        CcCourseGradecomposeDetailService courseGradecomposeDetailService = SpringContextHolder.getBean(CcCourseGradecomposeDetailService.class);
        if(!courseGradecomposeDetailService.batchUpdateStudentGrade(courseGradecomposeIndicationIds.toArray(new Long[courseGradecomposeIndicationIds.size()]), studentIds.toArray(new Long[studentIds.size()]), detailIds.toArray(new Long[detailIds.size()]),courseGradeComposeId)){
            result.put("isSuccess", false);
            return renderSUC(result, response, header);
        }
        //手动更新达成度 EM01199
        /*List<Long> educlassIdList = Lists.newArrayList();
        educlassIdList.add(eduClassId);

        if (!educlassIdList.isEmpty()) {

            CcEduindicationStuScoreService ccEduindicationStuScoreService = SpringContextHolder.getBean(CcEduindicationStuScoreService.class);

            if (!ccEduindicationStuScoreService.calculate(educlassIdList.toArray(new Long[educlassIdList.size()]), courseGradeComposeId)) {
                result.put("isSuccess", false);
                return renderSUC(result, response, header);
            }
        }*/

        result.put("isSuccess", Boolean.TRUE);
        return renderSUC(result, response, header);
    }
}
