package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.gnet.plugin.configLoader.ConfigUtils;
import com.gnet.plugin.poi.ExcelExporter;
import com.gnet.plugin.poi.RowDefinition;
import com.gnet.service.CcStudentService;
import com.gnet.utils.FileUtils;
import com.google.common.collect.Lists;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 整个教学班成绩录入Excel模板下载 所有成绩组成的
 *
 * @author GJM
 * @Date 2020年11月4日
 */
@Transactional(readOnly = false)
@Service("EM01231")
public class EM01231 extends BaseApi implements IApi {

    private static final Logger logger = Logger.getLogger(EM01231.class);

    @Autowired
    private CcStudentService ccStudentService;

    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {
        @SuppressWarnings("unchecked")
        Map<String, Object> param = request.getData();
        // 教学班编号
        final Long eduClassId = paramsLongFilter(param.get("eduClassId"));
        // 课程成绩组成编号

      /*  // 课程成绩组成编号列表
        List<Long> courseGradeComposeIds = paramsJSONArrayFilter(param.get("courseGradeComposeIds"), Long.class);
        Long batchId = paramsLongFilter(param.get("batchId"));
        String batchName="";
        if (batchId !=null){
            CcCourseGradecomposeBatch batch = CcCourseGradecomposeBatch.dao.findBatch(batchId);
            if (batch !=null){
                batchName=batch.getStr("name");
            }
        }*/
        // 教学班编号为空过滤
        if (eduClassId == null) {
            return renderFAIL("0500", response, header);
        }
        ArrayList<Long> educlassIds = new ArrayList<>();
        educlassIds.add(eduClassId);
        //成绩组成列表
        List<CcCourseGradecompose> ccCourseGradecomposeList = CcCourseGradecompose.dao.findByEduClassIds(educlassIds);
        List<Long> courseGradeComposeIds =new ArrayList<>();
        for (CcCourseGradecompose temp : ccCourseGradecomposeList){
            Long id = temp.getLong("id");
            Integer inputScoreType = temp.getInt("input_score_type");
            //判断录入题目类型的成绩组成下是否存在题目,不存在题目的过滤掉
            if (inputScoreType==2){
                boolean exist = CcCourseGradeComposeDetail.dao.isExist(id);
                if (exist){
                    courseGradeComposeIds.add(id);
                }
            } else{
                courseGradeComposeIds.add(id);
            }

        }
        // 课程成绩组成为空过滤
        if (courseGradeComposeIds == null || courseGradeComposeIds.isEmpty()) {
            return renderFAIL("0490", response, header);
        }
        // 获得教师开课课程
        CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findCourseByClassId(eduClassId);
        if (ccTeacherCourse == null) {
            return renderFAIL("0501", response, header);
        }
        //教学班名称
        String educlassName = ccTeacherCourse.getStr("educlass_name");
        //课程名称
        String name = ccTeacherCourse.getStr("name");
        // 判断是否是考核成绩法，不是就直接返回。教师开课类型,1:考核成绩分析法，2：评分表分析法 3: 财经大学考核成绩分析法
        Integer resultType = ccTeacherCourse.getInt("result_type");
        if(!CcTeacherCourse.RESULT_TYPE_SCORE.equals(resultType) && !CcTeacherCourse.RESULT_TYPE_SCORE2.equals(resultType)) {
            return renderFAIL("1009", response, header, "课程类型必须为：考核成绩分析法。");
        }

        List<File> fileList = Lists.newArrayList();

        String fileUrl = PathKit.getWebRootPath() + ConfigUtils.getStr("excel", "createPath");
        String exportUrl = PathKit.getWebRootPath() + ConfigUtils.getStr("excel", "createPath") + "学生成绩导入模板-" + name+"-"+educlassName + ".xls";

        try {
            // 判断是否存在路径，不存在就创建
            File dir = new File(fileUrl);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // 而外参数是从第几列开始的
            Integer startNaturalColumnIndex = 5;
//    			// 而外参数是从第几行开始的
//    			Integer startNaturalRowIndex = 1;
            if(CcTeacherCourse.RESULT_TYPE_SCORE == resultType || CcTeacherCourse.RESULT_TYPE_SCORE2 == resultType) {
                // 考核
                RowDefinition rowDefinition = ccStudentService.getScoreDefinition(ccTeacherCourse, courseGradeComposeIds, startNaturalColumnIndex,null);

                ExcelExporter.exportToExcel(2, rowDefinition, new ExcelExporter.ExcelExporterDataProcessor() {
                    @Override
                    public List<List<String>> invoke() {
                        List<List<String>> result = new ArrayList<>();

                        CcEduclass ccEduclass = CcEduclass.dao.findFilteredById(eduClassId);
                        String educlassName = ccEduclass.getStr("educlass_name");

                        List<CcStudent> studentList = CcStudent.dao.findByEduclassIdOrderByStudentNo(eduClassId);

                        for(int i = 0; i < studentList.size();) {
                            CcStudent temp = studentList.get(i);
                            List<String> data1 = new ArrayList<>();
                            i++;
                            temp.put("index", i);
                            data1.add(i+"");
                            data1.add(temp.getStr("student_no"));
                            data1.add(temp.getStr("name"));
                            data1.add(educlassName);

                            result.add(data1);
                        }

                        return result;
                    }
                }, exportUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (logger.isErrorEnabled()) {
                logger.error("生成成绩录入模版失败", e);
            }
        }
        fileList.add(new File(exportUrl));


        if (fileList.isEmpty()) {
            return renderFAIL("", response, header);
        }


        return renderFILE(fileList.get(0), response, header);


    }

}