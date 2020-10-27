package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcEduclassAchieveReport;
import com.gnet.model.admin.CcEduclassAssessReport;
import com.gnet.model.admin.CcEduclassIndicationAnalyze;
import com.gnet.model.admin.CcEduclassIndicationTitle;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.ConvertUtils;
import com.gnet.utils.SpringContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: certification-api-jg-2
 * @description: 保存持续改进方案和评价表
 * @author: Gjm
 * @create: 2020-08-25 09:42
 **/
@Service("EM01210")
public class EM01210 extends BaseApi implements IApi {

    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        Map<String, Object> param = request.getData();
        HashMap<Object, Object> result = new HashMap<>();
        Long edclassId = paramsLongFilter(param.get("classId"));
        //1.持续报告，2.评价表
        Integer inputType = paramsIntegerFilter(param.get("inputType"));
        //共有
        List<Map<String, Object>> indigradeReportList = ConvertUtils.convert(param.get("indigradeReportList"), List.class);
        //----------持续改进报告录入内容
        String targetRequire = paramsStringFilter(param.get("targetRequire"));
        String achieveAnalysis = paramsStringFilter(param.get("achieveAnalysis"));
        String teachDocument = paramsStringFilter(param.get("teachDocument"));
        String teachModified = paramsStringFilter(param.get("teachModified"));
        String problemModified = paramsStringFilter(param.get("problemModified"));
        String reportDate = paramsStringFilter(param.get("reportDate"));
        String endPaper = paramsStringFilter(param.get("endPaper"));
        //持续报告里期末试卷设计行为/技能（分数）下的标题
        List<Map<String, Object>> endTermList = ConvertUtils.convert(param.get("endTermList"), List.class);
        List<Map<String, Object>> endTermIndicationDataList = ConvertUtils.convert(param.get("endTermIndicationDataList"), List.class);
        //课程目标分析
        List<Map<String, Object>> indicationAnalyzeList = ConvertUtils.convert(param.get("indicationAnalyzeList"), List.class);
        if (inputType==1 ){
            if (targetRequire==null){
                return renderFAIL("2561", response, header);
            }
            if (achieveAnalysis==null){
                return renderFAIL("2562", response, header);
            }
            if (teachDocument==null){
                return renderFAIL("2563", response, header);
            }
            if (teachModified==null){
                return renderFAIL("2564", response, header);
            }
            if (problemModified==null){
                return renderFAIL("2565", response, header);
            }
        }
        //---------评价表录入内容
        String achieveResult = paramsStringFilter(param.get("achieveResult"));
        String assessDocunment = paramsStringFilter(param.get("assessDocunment"));
        String courseModified = paramsStringFilter(param.get("courseModified"));
        String courseImprovement = paramsStringFilter(param.get("courseImprovement"));
        String assessRequire = paramsStringFilter(param.get("assessRequire"));
        String  assessDate = paramsStringFilter(param.get("assessDate"));
        if (inputType==2 ){
            if (achieveResult==null){
                return renderFAIL("2566", response, header);
            }
            if (assessDocunment==null){
                return renderFAIL("2567", response, header);
            }
            if (courseModified==null){
                return renderFAIL("2568", response, header);
            }
            if (courseImprovement==null){
                return renderFAIL("2569", response, header);
            }
            if (assessRequire==null){
                return renderFAIL("2570", response, header);
            }
        }
        //处理课程目标的达成途径、方法、评价依据、课程目标分析
        if (indigradeReportList ==null ||indigradeReportList.size()==0){
            return renderFAIL("2571", response, header);

        }
        //处理持续报告和评价表的文字数据
        IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
        ArrayList<CcEduclassAchieveReport> updateEduclassAchieveReports = new ArrayList<>();
        CcEduclassAchieveReport ccEduclassAchieveReport = new CcEduclassAchieveReport();
        //判断是否存在
        CcEduclassAchieveReport achieveReport = CcEduclassAchieveReport.dao.findAchieveReport(edclassId);
        Integer isExists=0;
        if (achieveReport==null){
            ccEduclassAchieveReport.set("id",idGenerate.getNextValue());
            ccEduclassAchieveReport.set("class_id",edclassId);
            ccEduclassAchieveReport.set("target_require", targetRequire);
            ccEduclassAchieveReport.set("achieve_analysis", achieveAnalysis);
            ccEduclassAchieveReport.set("teach_document", teachDocument);
            ccEduclassAchieveReport.set("teach_modified", teachModified);
            ccEduclassAchieveReport.set("end_paper", endPaper);
            ccEduclassAchieveReport.set("problem_modified", problemModified);
            ccEduclassAchieveReport.set("achieve_result", achieveResult);
            ccEduclassAchieveReport.set("assess_docunment", assessDocunment);
            ccEduclassAchieveReport.set("course_modified", courseModified);
            ccEduclassAchieveReport.set("course_improvement", courseImprovement);
            ccEduclassAchieveReport.set("assess_require", assessRequire);
            ccEduclassAchieveReport.set("assess_date", assessDate);
            ccEduclassAchieveReport.set("report_date", reportDate);
            ccEduclassAchieveReport.set("is_del", Boolean.FALSE);
            isExists=1;
        }else {
           //修改的数据要分类，避免被覆盖
            if (inputType == 1) {
                achieveReport.set("target_require", targetRequire);
                achieveReport.set("achieve_analysis", achieveAnalysis);
                achieveReport.set("teach_document", teachDocument);
                achieveReport.set("end_paper", endPaper);
                achieveReport.set("teach_modified", teachModified);
                achieveReport.set("problem_modified", problemModified);
                achieveReport.set("report_date", reportDate);

            } else {
                achieveReport.set("achieve_result", achieveResult);
                achieveReport.set("assess_docunment", assessDocunment);
                achieveReport.set("course_modified", courseModified);
                achieveReport.set("course_improvement", courseImprovement);
                achieveReport.set("assess_require", assessRequire);
                achieveReport.set("assess_date", assessDate);
            }
        }
        //新增
        if (isExists==1){
            boolean save = ccEduclassAchieveReport.save();
            if (!save){
                result.put("isSuccess", Boolean.FALSE);
                return renderSUC(result, response, header);
            }
        }else {

            boolean update = achieveReport.update();
            if (!update){
                result.put("isSuccess", Boolean.FALSE);
                return renderSUC(result, response, header);
            }
        }
        //以下是课程目标的达成途径、依据、方法的保存和修改
        ArrayList<CcEduclassAssessReport> ccEduclassAssessReportAddList = new ArrayList<>();
        ArrayList<CcEduclassAssessReport> ccEduclassAssessReportUpdateList = new ArrayList<>();
        for (int i=0; i<indigradeReportList.size();i++){
            CcEduclassAssessReport ccEduclassAssessReport = new CcEduclassAssessReport();
            Map<String, Object> indicationReport = indigradeReportList.get(i);
            Long indicationId = Long.valueOf(String.valueOf(indicationReport.get("indicationId")));
            Long indicatorPortId = Long.valueOf(String.valueOf(indicationReport.get("indicatorPortId")));
            //课程目标id
            ccEduclassAssessReport.put("indication_id",indicationId);
            //指标点id
            ccEduclassAssessReport.put("indicator_port_id",indicatorPortId);
            //达成途径
            ccEduclassAssessReport.put("reach_way",indicationReport.get("reachWay"));
            //评价依据
            ccEduclassAssessReport.put("assess_gist",indicationReport.get("assessGist"));
            //评价方法
            ccEduclassAssessReport.put("assess_method",indicationReport.get("assessMethod"));
            //课程目标分析
            ccEduclassAssessReport.put("indication_analyze",indicationReport.get("indicationAnalyze"));
            ccEduclassAssessReport.put("is_del",Boolean.FALSE);
            ccEduclassAssessReport.put("class_id",edclassId);
            //判断是否存在
            List<CcEduclassAssessReport> assessReportList = CcEduclassAssessReport.dao.findAssessReport(edclassId, indicationId,indicatorPortId);
            if (assessReportList.size()==0){
                ccEduclassAssessReport.put("id",idGenerate.getNextValue());
                ccEduclassAssessReportAddList.add(ccEduclassAssessReport);
            }else {
                ccEduclassAssessReport.put("id",assessReportList.get(0).getLong("id"));
                ccEduclassAssessReportUpdateList.add(ccEduclassAssessReport);
            }
        }
        if(!ccEduclassAssessReportAddList.isEmpty() && !CcEduclassAssessReport.dao.batchSave(ccEduclassAssessReportAddList)){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("isSuccess", false);
            return renderSUC(result, response, header);
        }

        if(!ccEduclassAssessReportUpdateList.isEmpty() && !CcEduclassAssessReport.dao.batchUpdate(ccEduclassAssessReportUpdateList, "reach_way, assess_gist,assess_method,indication_analyze")){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("isSuccess", false);
            return renderSUC(result, response, header);
        }
        //----课程目标分析数据的保存和修改------
        ArrayList<CcEduclassIndicationAnalyze> updatAnalyze = new ArrayList<>();
        ArrayList<CcEduclassIndicationAnalyze> addAnalyze = new ArrayList<>();
        if (indicationAnalyzeList !=null){
            for (int i=0; i<indicationAnalyzeList.size();i++){
                Map<String, Object> indicationAnalyze = indicationAnalyzeList.get(i);
                CcEduclassIndicationAnalyze ccEduclassIndicationAnalyze = new CcEduclassIndicationAnalyze();
                Long indicationId = Long.valueOf(String.valueOf(indicationAnalyze.get("indicationId")));
                //判断是否存在
                List<CcEduclassIndicationAnalyze> assessIndicationAnalyze = CcEduclassIndicationAnalyze.dao.findAssessIndicationAnalyze(edclassId, indicationId);
                ccEduclassIndicationAnalyze.put("indication_id",indicationId);
                ccEduclassIndicationAnalyze.put("class_id",edclassId);
                ccEduclassIndicationAnalyze.put("indication_analyze",indicationAnalyze.get("indicationAnalyze"));
                ccEduclassIndicationAnalyze.put("is_del",Boolean.FALSE);
                if (assessIndicationAnalyze.size()==0){
                    ccEduclassIndicationAnalyze.put("id",idGenerate.getNextValue());
                    addAnalyze.add(ccEduclassIndicationAnalyze);
                }else{
                    ccEduclassIndicationAnalyze.put("id",assessIndicationAnalyze.get(0).get("id"));
                    updatAnalyze.add(ccEduclassIndicationAnalyze);
                }

            }
        }
        if(!addAnalyze.isEmpty() && !CcEduclassIndicationAnalyze.dao.batchSave(addAnalyze)){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("isSuccess", false);
            return renderSUC(result, response, header);
        }

        if(!updatAnalyze.isEmpty() && !CcEduclassIndicationAnalyze.dao.batchUpdate(updatAnalyze, "indication_analyze")){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("isSuccess", false);
            return renderSUC(result, response, header);
        }
        //---------期末试卷设计行为/技能（分数）下的标题列--------------
        ArrayList<CcEduclassIndicationTitle> updatTitle = new ArrayList<>();
        ArrayList<CcEduclassIndicationTitle> addTitle = new ArrayList<>();
        if (endTermList !=null){
            for (int i=0;i<endTermList.size();i++){
                CcEduclassIndicationTitle ccEduclassIndicationTitle = new CcEduclassIndicationTitle();
                Map<String, Object> endTermTitle = endTermList.get(i);
                int titleNo = Integer.parseInt(endTermTitle.get("id").toString());
                Object titleName = endTermTitle.get("title");
                ccEduclassIndicationTitle.put("title_name",titleName);
                ccEduclassIndicationTitle.put("title_no",titleNo);
                ccEduclassIndicationTitle.put("class_id",edclassId);
                ccEduclassIndicationTitle.put("is_del",Boolean.FALSE);
                //判断是否存在
                List<CcEduclassIndicationTitle> indicationTitleList = CcEduclassIndicationTitle.dao.findIndicationTitleList(edclassId, titleNo);
                if (indicationTitleList.size()>0){
                    ccEduclassIndicationTitle.put("id",indicationTitleList.get(0).getLong("id"));
                    updatTitle.add(ccEduclassIndicationTitle);
                }else{
                    ccEduclassIndicationTitle.put("id",idGenerate.getNextValue());
                    addTitle.add(ccEduclassIndicationTitle);
                }

            }
        }
        if(!addTitle.isEmpty() && !CcEduclassIndicationTitle.dao.batchSave(addTitle)){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("isSuccess", false);
            return renderSUC(result, response, header);
        }

        if(!updatTitle.isEmpty() && !CcEduclassIndicationTitle.dao.batchUpdate(updatTitle, "title_name")){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("isSuccess", false);
            return renderSUC(result, response, header);
        }
        //------期末试卷设计行为/技能（分数）下的数据列------
        ArrayList<CcEduclassIndicationAnalyze> updatIndicationTitleData = new ArrayList<>();
        ArrayList<CcEduclassIndicationAnalyze> addIndicationTitleData = new ArrayList<>();
        if (endTermIndicationDataList !=null){
            for (int i=0; i<endTermIndicationDataList.size();i++){
                Map<String, Object> indicationTitleData = endTermIndicationDataList.get(i);
                CcEduclassIndicationAnalyze ccEduclassIndicationAnalyze = new CcEduclassIndicationAnalyze();
                Long indicationId = Long.valueOf(String.valueOf(indicationTitleData.get("indicationId")));
                //判断是否存在
                List<CcEduclassIndicationAnalyze> assessIndicationAnalyze = CcEduclassIndicationAnalyze.dao.findAssessIndicationAnalyze(edclassId, indicationId);
                ccEduclassIndicationAnalyze.put("indication_id",indicationId);
                ccEduclassIndicationAnalyze.put("class_id",edclassId);
                ccEduclassIndicationAnalyze.put("is_del",Boolean.FALSE);
                String oneContent = indicationTitleData.get("oneContent")+"";
                String twoContent = indicationTitleData.get("twoContent")+"";
                if (oneContent!="" && oneContent !=null && !"".equals(oneContent)){
                    ccEduclassIndicationAnalyze.put("title_one_num",indicationTitleData.get("oneContent"));
                }
                if (twoContent!="" && twoContent !=null && !"".equals(twoContent)){
                    ccEduclassIndicationAnalyze.put("title_two_num",indicationTitleData.get("twoContent"));
                }


                if (assessIndicationAnalyze.size()==0){
                    ccEduclassIndicationAnalyze.put("id",idGenerate.getNextValue());
                    addIndicationTitleData.add(ccEduclassIndicationAnalyze);
                }else{
                    ccEduclassIndicationAnalyze.put("id",assessIndicationAnalyze.get(0).get("id"));
                    updatIndicationTitleData.add(ccEduclassIndicationAnalyze);
                }

            }
        }
        if(!addIndicationTitleData.isEmpty() && !CcEduclassIndicationAnalyze.dao.batchSave(addIndicationTitleData)){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("isSuccess", false);
            return renderSUC(result, response, header);
        }

        if(!updatIndicationTitleData.isEmpty() && !CcEduclassIndicationAnalyze.dao.batchUpdate(updatIndicationTitleData, "title_one_num,title_two_num")){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("isSuccess", false);
            return renderSUC(result, response, header);
        }
        result.put("isSuccess", Boolean.TRUE);
        return renderSUC(result, response, header);
    }
}
