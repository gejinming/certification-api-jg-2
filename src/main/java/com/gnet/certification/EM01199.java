package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.gnet.service.CcEduindicationStuScoreService;
import com.gnet.utils.DateUtil;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.jfinal.log.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: certification-api-jg-2
 * @description: 更新达成度计算
 * @author: Gjm
 * @create: 2020-07-02 15:02
 **/
@Transactional(readOnly = false)
@Service("EM01199")
public class EM01199 extends BaseApi implements IApi {
    private static final Logger logger = Logger.getLogger(EM01199.class);

    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        Map<String, Object> result = new HashMap();
        Map<String, Object> param = request.getData();
        Long eduClassId = paramsLongFilter(param.get("eduClassId"));
        Long inputType = paramsLongFilter(param.get("inputType"));
        Long courseGradeComposeId = paramsLongFilter(param.get("courseGradeComposeId"));
        Long batchId = paramsLongFilter(param.get("batchId"));
        List<Long> courseGradecomposeIds=new ArrayList<>() ;
        courseGradecomposeIds.add(courseGradeComposeId);

        //判段成绩是否全部录入
        //1获取教学班人数
        int studentNum = CcEduclassStudent.dao.findStuInfoByClassId(eduClassId).size();
        if (inputType==1){
            //直接录入的方式
            //2获取这个成绩组成的课程目标数量
            int indicationNum = CcIndication.dao.findCourseGradeComposeId(courseGradeComposeId).size();
            //3需要录入的数量=人数*课程目标数量
            int sumNum=studentNum*indicationNum;
            //已经录入的数量
            List<CcScoreStuIndigrade> studentGradeList = CcScoreStuIndigrade.dao.findDetailByClassIdAndCourseGradecomposeId(eduClassId, courseGradeComposeId);
            if (sumNum!=studentGradeList.size()){
                return renderFAIL("2556", response, header);
            }

        }else if (inputType==2 || inputType==3){
            //题目录入方式
            //题目数量
            List<CcCourseGradeComposeDetail> ccCourseGradeComposeDetails = CcCourseGradeComposeDetail.dao.topicList(courseGradeComposeId,batchId);
            int topicNum=ccCourseGradeComposeDetails.size();
            //需要录入的成绩数量
            int sumNum=studentNum*topicNum;
            //已经录入的数量
            int num = CcCourseGradecomposeStudetail.dao.sumStudentGradeNum(courseGradeComposeId,batchId).size();
            if (sumNum > num){
                return renderFAIL("2556", response, header);
            }
        }

        // 更新教学班的平均成绩和总成绩以及所有的达成度
		CcEduindicationStuScoreService ccEduindicationStuScoreService = SpringContextHolder.getBean(CcEduindicationStuScoreService.class);
		//如果courseGradecomposeIds设置为null的话就会计算这个班级的所有成绩组成的达成度
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateS = sdf.format(date);
        logger.info("开始统计达成度教学班"+eduClassId+"时间："+dateS);
        if (!ccEduindicationStuScoreService.calculate(Lists.newArrayList(eduClassId), courseGradecomposeIds)) {
			result.put("isSuccess", Boolean.FALSE);
			return renderSUC(result, response, header);
		}
        Date date2 = new Date();
        String dateS1 = sdf.format(date2);
        logger.info("结束统计达成度教学班"+eduClassId+"时间："+dateS1);

        long timeSub = DateUtil.timeSub(dateS, dateS1);

        logger.info("统计达成度教学班"+eduClassId+"共用时间："+timeSub+"秒");
        result.put("isSuccess", Boolean.TRUE);
        return renderSUC(result, response, header);
    }
}