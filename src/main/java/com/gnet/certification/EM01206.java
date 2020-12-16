package com.gnet.certification;

import cn.jpush.api.common.connection.IHttpClient;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.gnet.pager.Pageable;
import com.gnet.utils.DateUtil;
import com.gnet.utils.PriceUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;
import org.omg.CORBA.IRObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 获取导出达成度评价与持续改进报告的数据
 * 
 * @author GJM
 * @Date 2020年8月13日14:09:05
 */
@Service("EM01206")
@Transactional(readOnly=true)
public class EM01206 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 教学班id
        // 导出类型 1导出报告2导出评价表
        Long exportType = paramsLongFilter(params.get("exporterType"));
		Long classId = paramsLongFilter(params.get("classId"));
		if (classId==null || exportType==null){
			return renderFAIL("0500", response, header);
		}
		//查询教学班学生所属行政班名称
        List<CcStudent> classNameList = CcStudent.dao.findClassName(classId);
		String className="";
		for (int i=0;i<classNameList.size();i++){
            CcStudent ccStudent = classNameList.get(i);
            if (i==0){
                className=ccStudent.get("className");
            }else {
                className=className+"、"+ccStudent.get("className");
            }
        }

		//教学班及课程信息
        Map<String, Object> educlassCourseInfo = new HashMap<>();
		//获取教学班及课程信息
		CcEduclass educlassInfo = CcEduclass.dao.findEduclassById(classId);
        Long courseId = educlassInfo.getLong("courseId");
        Long teacherCourseId = educlassInfo.getLong("teacherCourseId");
        //专业
        Long majorId = educlassInfo.getLong("major_id");
        if (!majorId.equals("") && majorId != null){
            Pageable pageable = new Pageable(null, null);
            Page<CcTeacher> majorPage = CcTeacher.dao.page(pageable, majorId, null, null, null, null, null, null, null, null, "162168");
            List<CcTeacher> ccTeacherList = majorPage.getList();
           if (ccTeacherList.size()>0){
               String majorName = ccTeacherList.get(0).getStr("name");
               educlassCourseInfo.put("majorName",majorName);
           }
        }
        //该课程的所有课程目标
        ArrayList<Object> courseAllIndicationList = new ArrayList<>();
        List<CcIndication> courseIndicationReport = CcIndication.dao.findCourseIndicationReport(courseId);
        for (CcIndication temp : courseIndicationReport ){
            HashMap<Object, Object> map = new HashMap<>();
            Long indicationId = temp.getLong("id");
            map.put("indicationId",indicationId);
            map.put("sort",temp.get("sort"));
            map.put("content",temp.get("content"));
            CcEdupointEachAimsAchieve classIndicationAchieve = CcEdupointEachAimsAchieve.dao.findClassIndicationAchieve(classId, indicationId);
            if (classIndicationAchieve !=null ){
                map.put("achieveValue",PriceUtils.currency(classIndicationAchieve.getBigDecimal("achieve_value")));
            }else{
               // map.put("achieveValue","");
            }
            //课程目标分析
            List<CcEduclassIndicationAnalyze> assessIndicationAnalyze = CcEduclassIndicationAnalyze.dao.findAssessIndicationAnalyze(classId, indicationId);
            if (assessIndicationAnalyze.size() !=0){
                //只有一个
                map.put("indicationAnalyze",assessIndicationAnalyze.get(0).getStr("indication_analyze"));
            }else {
                map.put("indicationAnalyze","");
            }

            courseAllIndicationList.add(map);

        }


        //学时/学分
		String hoursAndcredit=educlassInfo.get("all_hours")+"/"+educlassInfo.get("credit");
		//开课学期
		String courseTerm=educlassInfo.get("start_year")+"~"+educlassInfo.get("end_year")+"-"+educlassInfo.get("term");
        educlassCourseInfo.put("courseName",educlassInfo.getStr("courseName"));
        educlassCourseInfo.put("courseCode",educlassInfo.getStr("courseCode"));
        educlassCourseInfo.put("hoursAndcredit",hoursAndcredit);
        educlassCourseInfo.put("educlassName",educlassInfo.getStr("educlass_name"));
        educlassCourseInfo.put("studentNum",educlassInfo.get("studentNum"));
        educlassCourseInfo.put("teacherName",educlassInfo.getStr("teacherName"));
        educlassCourseInfo.put("courseTerm",courseTerm);
        educlassCourseInfo.put("allHours",educlassInfo.get("all_hours"));
        educlassCourseInfo.put("credit",educlassInfo.get("credit"));
        //课程负责人先取课程里的团队负责人
        educlassCourseInfo.put("teamLeader",educlassInfo.getStr("team_leader"));

        List<CcCourseGradecomposeIndication> finalIndictionScoreList = CcCourseGradecomposeIndication.dao.findFinalIndictionScore(teacherCourseId);
        //判断是否包含期末成绩
        if(finalIndictionScoreList==null){
            return renderFAIL("0085", response, header);
        }
        ArrayList<HashMap<String, Object>> gradecomposeList = new ArrayList<>();
        //成绩组成下的课程目标
        List<CcCourseGradecomposeIndication> courseGradecomposeIndications = CcCourseGradecomposeIndication.dao.findByTeacherCourseIds(Lists.newArrayList(teacherCourseId));
        ArrayList<Object> gradeconposeIndicationList = new ArrayList<>();
        for (CcCourseGradecomposeIndication temp : courseGradecomposeIndications){
            HashMap<String, Object> gradecomposeMap = new HashMap<>();
            Long gradecomposeId = temp.getLong("gradecomposeId");
            Long indicationId = temp.getLong("indication_id");
            HashMap<Object, Object> map = new HashMap<>();
            map.put("indicationId",temp.getLong("indication_id"));
            map.put("gradecomposeId",gradecomposeId);
            map.put("gradecomposeName",temp.getStr("name"));
            gradecomposeMap.put("courseGradecomposeId",temp.getLong("courseGradecomposeId"));
            gradecomposeMap.put("gradecomposeName",temp.getStr("name"));
            if (!gradecomposeList.contains(gradecomposeMap)){
                gradecomposeList.add(gradecomposeMap);
            }
            //细化的预期学习成果及实施准则和观测点
            CcEduclassIndicationResult indicationResult = CcEduclassIndicationResult.dao.findIndicationResult(classId, indicationId, gradecomposeId);
            if (indicationResult !=null){
                String learnContent = indicationResult.getStr("learn_content");
                String observeContent = indicationResult.getStr("observe_content");
                map.put("learnContent",learnContent);
                map.put("observeContent",observeContent);
            }
            gradeconposeIndicationList.add(map);
        }
        //期末成绩的总分
        BigDecimal allIndicationScore = new BigDecimal("0");
        //期末成绩包含的课程目标和其占比
        ArrayList<Object> endTermIndicationList = new ArrayList<>();
        //组成各个课程目标占比的一句话
        String indicationMarkUpString="";
        //求出各课程目标的占比
        BigDecimal indicationMaxScore = new BigDecimal("0");
        int is=0;
        for (CcCourseGradecomposeIndication temps: finalIndictionScoreList){

            HashMap<Object, Object> map = new HashMap<>();
            //首次统计
            if (is==0) {
                for (CcCourseGradecomposeIndication temp : finalIndictionScoreList) {
                    //求出期末成绩总分
                    BigDecimal maxScore = temp.getBigDecimal("max_score");
                    if (maxScore !=null){
                        allIndicationScore = allIndicationScore.add(maxScore);
                    }


                }
            }
            Long indicationId = temps.getLong("indicationId");
            //课程目标总分
            BigDecimal maxScore = temps.getBigDecimal("max_score");
            //课程目标占比
            BigDecimal max = new BigDecimal("100");
            BigDecimal indicatMakeUp=new BigDecimal("0");
            if (exportType==1 && maxScore !=null){
                //求出百分比  课程目标总分/期末总分*100
                indicatMakeUp= PriceUtils.currency(maxScore.divide(allIndicationScore,4).multiply(max));
            }
            //去百分比后面的0
            String indicatMakeUps = indicatMakeUp.stripTrailingZeros().toPlainString();
            map.put("indicationId",indicationId);
            map.put("indicationMaxScore",maxScore);
            map.put("sort",temps.get("sort"));
            map.put("content",temps.get("content"));
            map.put("indicatMakeUp",indicatMakeUps);
            //行为/技能（分数/题数）下的数据
            List<CcEduclassIndicationAnalyze> assessIndicationAnalyze = CcEduclassIndicationAnalyze.dao.findAssessIndicationAnalyze(classId, indicationId);
            if (assessIndicationAnalyze.size()>0){
                //分数
                map.put("oneContent",assessIndicationAnalyze.get(0).get("title_one_num"));
                map.put("twoContent",assessIndicationAnalyze.get(0).get("title_two_num"));
                //题数
                //记忆题数
                map.put("memoryNum",assessIndicationAnalyze.get(0).get("memory_num"));
                //理解题数
                map.put("understandNum",assessIndicationAnalyze.get(0).get("understand_num"));
                //应用题数
                map.put("applyNum",assessIndicationAnalyze.get(0).get("apply_num"));
                //评价题数
                map.put("assessNum",assessIndicationAnalyze.get(0).get("assess_num"));
                //创造题数
                map.put("createNum",assessIndicationAnalyze.get(0).get("create_num"));
                //考核内容
                map.put("checkContent",assessIndicationAnalyze.get(0).get("check_content"));

            }
            endTermIndicationList.add(map);
            //不等于0
            if (maxScore != null && !PriceUtils.eqThan(maxScore,new BigDecimal("0"))){
                if (is==0){
                    indicationMarkUpString="目标"+temps.getInt("sort")+"分数占"+indicatMakeUps+"%";
                }else {
                    indicationMarkUpString=indicationMarkUpString+","+"目标"+temps.getInt("sort")+"分数占"+indicatMakeUps+"%";
                }
            }
            is++;

        }
        //期末试卷设计行为/技能（分数）下的标题
        ArrayList<Object> endTermTitleList = new ArrayList<>();
        List<CcEduclassIndicationTitle> indicationTitleList = CcEduclassIndicationTitle.dao.findIndicationTitleList(classId, null);
        for (CcEduclassIndicationTitle temp: indicationTitleList){
            HashMap<Object, Object> map = new HashMap<>();
            map.put("titleNo",temp.getInt("title_no"));
            map.put("titleName",temp.getStr("title_name"));
            endTermTitleList.add(map);
        }


        //指标点与课程目标
        ArrayList indicationPointList = new ArrayList();
        ArrayList<Map<Object, Object>> indicationList = new ArrayList();
        List<CcIndicatorPoint> indicationAndPoint = CcIndicatorPoint.dao.findIndicationAndPoint(courseId, classId);

        Long inporintId=0l;
        //达成数量
        int achieveNum=0;
        //相同指标点的数量,用于合并行
        int indicationPointNum=1;
        //序号
        int num=0;

        //上一个指标点的权重
        BigDecimal oldweight=new BigDecimal("0");
        //上一个课程目标的达成度
        BigDecimal result = new BigDecimal("0");
        for (int i=0;i<indicationAndPoint.size();i++){
            CcIndicatorPoint temp = indicationAndPoint.get(i);
            Long pointId = temp.getLong("pointId");
            //指标点权重
            BigDecimal weight = temp.getBigDecimal("weight");
            //课程目标达成度achieveValue
            BigDecimal achieveValue = PriceUtils.currency(temp.getBigDecimal("achieveValue"));
            BigDecimal expected_value = temp.getBigDecimal("expected_value");

            //去重指标点，因为一个指标点下有多个课程目标
            if (!inporintId.equals(pointId)){
                num=num+1;
                HashMap<Object, Object> map = new HashMap<>();
                map.put("pointId",temp.getLong("pointId"));
                map.put("graduateIndexNum",temp.get("graduateIndexNum"));
                map.put("index_num",temp.get("index_num"));
                map.put("pointCont",temp.get("pointCont"));
                map.put("num",weight);
                map.put("num",num);

                inporintId=pointId;
                indicationPointList.add(map);
                indicationPointNum=1;
            }else {
                // 如果前者更小，用前者
                result = result.compareTo(new BigDecimal(0)) == 0 ? achieveValue : result.compareTo(achieveValue) == -1 ? result : achieveValue;
                indicationPointNum+=1;
            }
            Long indicationId1 = temp.getLong("indicationId");
           /* //课程目标总分
            BigDecimal indicationMaxScore = new BigDecimal("0");
            for (CcCourseGradecomposeIndication temps: finalIndictionScoreList){
                Long indicationId = temps.getLong("indicationId");
                BigDecimal maxScore = temps.getBigDecimal("max_score");
                if (indicationId1.equals(indicationId)){
                    indicationMaxScore=maxScore;
                }

            }
            //课程目标占比
            BigDecimal max = new BigDecimal("100");
            BigDecimal indicatMakeUp=new BigDecimal("0");

            if (exportType==1){
                indicatMakeUp= PriceUtils.currency(indicationMaxScore.divide(allIndicationScore,4).multiply(max));
            }

            String indicatMakeUps = indicatMakeUp.stripTrailingZeros().toPlainString();*/
            HashMap<Object, Object> indicationMap = new HashMap<>();
            Long indicationId = temp.getLong("indicationId");
            //课程目标的达成途径、评价方法、评价依据
            List<CcEduclassAssessReport> assessReportList = CcEduclassAssessReport.dao.findAssessReport(classId, indicationId,pointId);
            if (assessReportList !=null){
                for (CcEduclassAssessReport assessReport: assessReportList){
                    indicationMap.put("reachWay", assessReport.getStr("reach_way"));
                    indicationMap.put("assessGist", assessReport.getStr("assess_gist"));
                    indicationMap.put("assessMethod", assessReport.getStr("assess_method"));
                    indicationMap.put("indicationAnalyze", assessReport.getStr("indication_analyze"));
                }
            }
            indicationMap.put("pointId",temp.getLong("pointId"));
            indicationMap.put("id", temp.getLong("indicationId"));
            indicationMap.put("sort", temp.getInt("sort"));
            indicationMap.put("indicationName","目标"+temp.getInt("sort"));
            indicationMap.put("content", temp.getStr("indicationCont"));
            //课程目标期望值
            indicationMap.put("expected_value",PriceUtils.currency(temp.getBigDecimal("expected_value")));
            //课程目标达成度
            indicationMap.put("achieveValue",PriceUtils.currency(temp.get("achieveValue")));
            //剔除学生之后的达成度
            indicationMap.put("exceptAchieveValue",PriceUtils.currency(temp.get("exceptAchieveValue")));

            //指标点权重
            indicationMap.put("weight",weight);
           /* //课程目标总分
            indicationMap.put("indicationMaxScore",indicationMaxScore);
            //课程目标占比
            indicationMap.put("indicatMakeUp",indicatMakeUps);*/
            //是否达成


            boolean b = PriceUtils.lessThan(achieveValue, new BigDecimal(0.6));
            if (!b){
                indicationMap.put("isAchieve","是");
                achieveNum+=1;
            }else{
                indicationMap.put("isAchieve","否");
            }
            int a=i+1;
            indicationMap.put("num",a);
            indicationMap.put("indicationPointNum",indicationPointNum);
            indicationList.add(indicationMap);



        }
        //处理合并相同指标点数量及指标点课程目标中最小的达成度
        ArrayList indicationPointAndAceieveList = new ArrayList();
        List<CcIndicatorPoint> indicationAndPointNum = CcIndicatorPoint.dao.findIndicationAndPointNum(courseId, classId);
        if (indicationAndPointNum != null){
           for (CcIndicatorPoint temp:indicationAndPointNum){
               BigDecimal achieve = PriceUtils.currency(temp.getBigDecimal("achieve"));
               BigDecimal weigth = temp.getBigDecimal("weight");
               Map<Object, Object> map = new HashMap<>();
               map.put("pointId",temp.getLong("pointId"));
               map.put("weight",temp.getBigDecimal("weight"));
               map.put("achieve",PriceUtils.currency(temp.getBigDecimal("achieve")));
               //指标点达成度
               BigDecimal pointAchieve = PriceUtils.currency(achieve.multiply(weigth));
               map.put("pointAchieve",pointAchieve);
               map.put("megeNum",temp.get("megeNum"));
               indicationPointAndAceieveList.add(map);
           }
        }


        String year=DateUtil.getToYear()+"";
        String month=DateUtil.getToMonth()+"";
        String day=DateUtil.getToday()+"";
        //持续报告和评价表的一些数据
        HashMap<Object, Object> achieveReportInfo = new HashMap<>();
        CcEduclassAchieveReport achieveReport = CcEduclassAchieveReport.dao.findAchieveReport(classId);
        if (achieveReport !=null){
            achieveReportInfo.put("targetRequire", achieveReport.getStr("target_require"));
            achieveReportInfo.put("achieveAnalysis", achieveReport.getStr("achieve_analysis"));
            achieveReportInfo.put("teachDocument", achieveReport.getStr("teach_document"));
            achieveReportInfo.put("teachModified", achieveReport.getStr("teach_modified"));
            achieveReportInfo.put("problemModified", achieveReport.getStr("problem_modified"));
            achieveReportInfo.put("achieveResult",achieveReport.getStr("achieve_result"));
            achieveReportInfo.put("assessDocunment",achieveReport.getStr("assess_docunment"));
            achieveReportInfo.put("courseModified",achieveReport.getStr("course_modified"));
            achieveReportInfo.put("courseImprovement",achieveReport.getStr("course_improvement"));
            achieveReportInfo.put("assessRequire",achieveReport.getStr("assess_require"));
            achieveReportInfo.put("endPaper",achieveReport.getStr("end_paper"));
            //需要反馈的问题
            achieveReportInfo.put("problemContent",achieveReport.getStr("problem_content"));
            //课程简介
            achieveReportInfo.put("courseInfo",achieveReport.getStr("course_info"));
            //教学方法
            achieveReportInfo.put("teacherMothed",achieveReport.getStr("teacher_mothed"));
            //评价方法
            achieveReportInfo.put("assessMothed",achieveReport.getStr("assess_mothed"));
            //试卷分析的一段文字
            achieveReportInfo.put("testAnalysis",achieveReport.getStr("test_analysis"));
            //细化可测评的课程学习目标（预期学习成果）
            achieveReportInfo.put("courseLearTarget",achieveReport.getStr("course_lear_target"));
            //针对个体的达成度评价分析
            achieveReportInfo.put("personAchieveAnalyze",achieveReport.getStr("person_achieve_analyze"));
            //持续报告的时间

            String reportDate = achieveReport.getStr("report_date");
            //评价报告的时间
            String assessDate = achieveReport.getStr("assess_date");
            if (exportType ==1){
                if (reportDate !=null && !reportDate.equals("")){
                    Date date = DateUtil.stringtoDate(reportDate, "yyyy-MM-dd");
                     year = DateUtil.getYear(date)+"";
                     month=DateUtil.getMonth(date)+"";
                     day =DateUtil.getDay(date)+"";
                }
            }else{
                if (assessDate !=null && !assessDate.equals("")){
                    Date date = DateUtil.stringtoDate(assessDate, "yyyy-MM-dd");
                    year = DateUtil.getYear(date)+"";
                    month=DateUtil.getMonth(date)+"";
                    day =DateUtil.getDay(date)+"";
                }
            }

        }
        //持续报告的一些图片B64
        List<CccEduclassImages> educlassImages = CccEduclassImages.dao.findEduclassImages(classId);
        ArrayList<Object> imageList = new ArrayList<>();
        for (int i=0 ; i<educlassImages.size() ; i++){
            HashMap<Object, Object> map = new HashMap<>();
            CccEduclassImages temp = educlassImages.get(i);
            String imageName = temp.getStr("image_name");
            String imageB64 = temp.getStr("imageB64");
            //输出到word中，排个序
            String imageUrl ="rId"+(10+i);
            String imageNameNo= "media/image"+i+".jpeg";
            map.put("imageName",imageName);
            map.put("imageB64",imageB64);
            map.put("imageUrl",imageUrl);
            map.put("imageNameNo",imageNameNo);
            map.put("type",temp.getInt("type"));
            imageList.add(map);

        }
        returnMap.put("endTermTitleList",endTermTitleList);
        returnMap.put("courseAllIndication",courseAllIndicationList);
        returnMap.put("achieveReportInfo", achieveReportInfo);
        returnMap.put("educlassCourseInfo", educlassCourseInfo);
        returnMap.put("indicationList",indicationList);
        returnMap.put("indicationPointList",indicationPointList);
        returnMap.put("className",className);
        returnMap.put("achieveNum",achieveNum);
        returnMap.put("indicationMarkUpString",indicationMarkUpString);
        returnMap.put("year", year);
        returnMap.put("month", month);
        returnMap.put("day", day);
        returnMap.put("indicationPointAndAceieveList",indicationPointAndAceieveList);
        returnMap.put("endTermIndicationList",endTermIndicationList);
        returnMap.put("gradeconposeIndicationList",gradeconposeIndicationList);
        returnMap.put("gradecomposeList",gradecomposeList);
        returnMap.put("imageList",imageList);
		// 结果返回
		return renderSUC(returnMap, response, header);
	}
}
