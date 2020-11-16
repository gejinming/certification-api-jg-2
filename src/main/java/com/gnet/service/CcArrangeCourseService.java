package com.gnet.service;

import com.gnet.api.sign.Result;
import com.gnet.excel.GraduateExcel;
import com.gnet.model.admin.*;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.ExcelUtil;
import com.gnet.utils.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: certification-api-jg-2
 * @description: 导入排课信息处理
 * @author: Gjm
 * @create: 2020-10-19 16:55
 **/
@Slf4j
@Component("CcArrangeCourseService")
public class CcArrangeCourseService {
    Long majorId;
    Long schoolId;
    /**
     * 读取excel数据
     *
     * @param path
     */
    public Result readExcelToObj(String path,Long majorIds,Long schoolIds) {
        majorId=majorIds;
        schoolId=schoolIds;
        Workbook wb = null;
        try {
            wb = WorkbookFactory.create(new File(path));
            Result result = readExcel(wb, 0, 1, 0);
            return result;
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.error("导入失败！");
    }

    /**
     * 读取excel文件
     *
     * @param wb
     * @param sheetIndex    sheet页下标：从0开始
     * @param startReadLine 开始读取的行:从0开始
     * @param tailLine      去除最后读取的行
     */
    public Result readExcel(Workbook wb, int sheetIndex, int startReadLine, int tailLine) {
        Sheet sheet = wb.getSheetAt(sheetIndex);
        ArrayList<CcTeacherCourse> teacherCourseList = new ArrayList<>();
        Date date = new Date();
        IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
        for (int i = startReadLine; i < sheet.getLastRowNum() - tailLine + 1; i++) {
            Row  row = sheet.getRow(i);
            CcTeacherCourse ccTeacherCourse = new CcTeacherCourse();
            Long courseId=null;
            Integer grade=null;
            String couseName="";
            Integer term_type=2;
            Long termId=null;
            Long teacherId=null;
            Long planId=null;
            //年级
            if (i>1){
                try {
                    String grades = ExcelUtil.getCellValue(row.getCell(1));
                    if (grades!=""&& grades !=null){
                        if(grades.contains(".")){
                            grades=grades.substring(0,grades.indexOf("."));
                        }
                        grade = Integer.parseInt(grades);
                        ccTeacherCourse.set("grade", grade);
                        planId = CcVersion.dao.findNewestVersion(majorId, grade);
                    }else{
                        return Result.error("错误：课程的年级不可为空，请检查！");
                    }

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return Result.error("错误：该年级类型无法转换为Integer，请检查年级格式是否正确！");
                }
            }
            for(Cell c : row){
                //判断表头属性是否正确
                //获取值换行符
                String returnStr = ExcelUtil.getCellValue(c);
                if (i==1){
                    if (c.getColumnIndex()==0){
                        if (!returnStr.equals("课程名称")){
                            return Result.error("错误：导入模板有问题，请重新下载");
                        }
                        continue;
                    }
                    if (c.getColumnIndex()==1){
                        if (!returnStr.equals("年级")){
                            return Result.error("错误：导入模板有问题，请重新下载");
                        }
                        continue;
                    }
                    if (c.getColumnIndex()==2){
                        if (!returnStr.equals("学年学期")){
                            return Result.error("错误：导入模板有问题，请重新下载");
                        }
                        continue;
                    }
                    if (c.getColumnIndex()==3){
                        if (!returnStr.equals("教师工号")){
                            return Result.error("错误：导入模板有问题，请重新下载");
                        }
                        continue;
                    }
                    if (c.getColumnIndex()==4){
                        if (!returnStr.equals("是否是分享人（是/否）")){
                            return Result.error("错误：导入模板有问题，请重新下载");
                        }
                        continue;
                    }
                    if (c.getColumnIndex()==5){
                        if (!returnStr.equals("达成度计算类型")){
                            return Result.error("错误：导入模板有问题，请重新下载");
                        }
                        continue;
                    }
                }else{

                    //导入内容

                    if (c.getColumnIndex()==0){
                        //课程名称
                        if (returnStr!=""&& returnStr !=null){
                            couseName=returnStr;
                            List<CcCourse> course = CcCourse.dao.findNameCourse(returnStr, planId);
                            if (course.size()==1){
                               courseId=course.get(0).getLong("id");
                               ccTeacherCourse.set("course_id", courseId);
                           }else if (course.size()>1){
                               return Result.error("错误：该"+returnStr+"课程名称系统里有多个重复的，请检查！");
                           }else {
                               return Result.error("错误：该"+returnStr+"课程名称在该年级对应的版本里没有找到，请检查！");
                           }
                        }else{
                            return Result.error("错误：课程名称不可为空，请检查！");
                        }

                        continue;
                    }
                    if (planId==null){
                        return Result.error("错误：未找到"+couseName+"课程的年级相对于的版本号，请检查！");
                    }
                    if (c.getColumnIndex()==1){
                        if (returnStr!=""&& returnStr !=null){

                            CcCourseService ccCourseService = SpringContextHolder.getBean(CcCourseService.class);
                            if(!ccCourseService.validateGrade(grade, courseId)) {
                                return Result.error("错误：该"+couseName+"课程年级应该大于等于当前版本的启用年级，小于下一个版本的启用年级！");
                            }
                        }
                        else{
                            return Result.error("错误：该"+couseName+"课程的年级不可为空，请检查！");
                        }
                        continue;
                    }

                    //学年学期
                    if (c.getColumnIndex()==2){
                        if (returnStr!=""&& returnStr !=null){
                            try {
                                String startYears = null;
                                String endYears = null;
                                String terms = null;
                                try {
                                    startYears = returnStr.substring(0, returnStr.indexOf("年至"));
                                    endYears = returnStr.substring(returnStr.indexOf("年至") + 2, returnStr.indexOf("年第"));
                                    terms = returnStr.substring(returnStr.indexOf("年第") + 2, returnStr.indexOf("年第") + 3);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return Result.error("错误：该"+couseName+"课程学年学期格式错误，请检查学年学期格式是否正确！");
                                }
                                Integer startYear = Integer.parseInt(startYears);
                                Integer endYear = Integer.parseInt(endYears);
                                Integer term = Integer.parseInt(terms);
                                //判断是长学期还是短学期
                                if (returnStr.contains("长")){
                                    term_type=1;
                                }
                                List<CcTerm> ccTermList = CcTerm.dao.findTerm(startYear, endYear, term, term_type, schoolId);
                                if (ccTermList.size()==1){
                                    termId=ccTermList.get(0).getLong("id");
                                    ccTeacherCourse.set("term_id", termId);
                                }else if (ccTermList.size()>1){
                                    return Result.error("错误：该"+couseName+"课程的学年学期系统里有多个重复的，请检查！");
                                }else {
                                    return Result.error("错误：该"+couseName+"课程的学年学期系统里没有找到，请检查！");
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                return Result.error("错误：该"+couseName+"课程学年学期格式错误，请检查年级格式是否正确！");
                            }

                        }else{
                            return Result.error("错误：该"+couseName+"课程的学年学期不可为空，请检查！");
                        }
                        continue;
                    }
                    //教师工号
                    if (c.getColumnIndex()==3){
                        if (returnStr!=""&& returnStr !=null){
                            List<CcTeacher> teacherList = CcTeacher.dao.findNameTeacher(returnStr, schoolId);
                            if (teacherList.size()==1){
                                teacherId=teacherList.get(0).getLong("id");
                                ccTeacherCourse.set("teacher_id", teacherId);
                            }else if (teacherList.size()>1){
                                return Result.error("错误：该"+couseName+"课程的教师工号系统里有多个重复的，请检查！");
                            }else {
                                return Result.error("错误：该"+couseName+"课程的教师工号系统里没有找到，请检查！");
                            }
                        }
                        else{
                            return Result.error("错误：该"+couseName+"课程的教师工号不可为空，请检查！");
                        }
                        //判断是否重复课程 验证教师+课程+学期+年级 不能重复
                        if(CcTeacherCourse.dao.isExisted(courseId, teacherId, termId, grade, null)){
                            return Result.error("错误：该"+couseName+"课程的排课信息已存在（课程+教师+学年学期+年级不能重复），请检查！");
                        }
                        continue;
                    }

                    //是否分享人
                    if (c.getColumnIndex()==4){
                        if (returnStr!=""&& returnStr !=null){
                            if (returnStr.equals("是")){
                                ccTeacherCourse.set("is_sharer", Boolean.TRUE);
                                // 找到之前的分享人，把他变成非分享人
                                CcTeacherCourse sharedTeacherCourse = CcTeacherCourse.dao.findSharer(courseId, termId, grade);
                                if(sharedTeacherCourse != null) {
                                    sharedTeacherCourse.set("modify_date", date);
                                    sharedTeacherCourse.set("is_sharer", null);
                                    sharedTeacherCourse.set("is_shared", null);
                                    if(!sharedTeacherCourse.update()) {
                                        return Result.error("错误：该"+couseName+"课程的的分享人变更失败！");
                                    }
                                }
                            }else {
                                ccTeacherCourse.set("is_sharer", Boolean.FALSE);
                            }
                        }else{
                            return Result.error("错误：该"+couseName+"课程的是否为分享人不可为空，请检查！");
                        }
                        continue;
                    }
                    if (c.getColumnIndex()==5){
                        if (returnStr!=""&& returnStr !=null){
                            if (returnStr.equals("考核成绩分析法")){
                                ccTeacherCourse.set("result_type", 1);
                            }else{
                                ccTeacherCourse.set("result_type", 2);
                            }
                        }
                        continue;
                    }


                }

            }
            if (i>1){
                ccTeacherCourse.set("id",idGenerate.getNextValue());
                ccTeacherCourse.set("is_del", Boolean.FALSE);
                ccTeacherCourse.set("create_date", date);
                ccTeacherCourse.set("modify_date", date);
                teacherCourseList.add(ccTeacherCourse);
                // 检测某个课程所在专业是否已经关联了当前教师，如果不存在，则增加关联。
                CcMajorTeacherService ccMajorTeacherService = SpringContextHolder.getBean(CcMajorTeacherService.class);
                boolean isSuccess = ccMajorTeacherService.addMajorTeacher(teacherId, date, courseId);
                if (!isSuccess) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }


        }
        if (teacherCourseList.size()>0){
            log.info("需要新增"+teacherCourseList.size()+"条排课信息。");
            if (!CcTeacherCourse.dao.batchSave(teacherCourseList)){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.error("错误：排课信息添加失败，请检查！");
            }
        }
        return Result.ok();
    }

}
