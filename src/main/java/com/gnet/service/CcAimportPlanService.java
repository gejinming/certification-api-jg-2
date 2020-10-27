package com.gnet.service;

import com.gnet.api.sign.Result;
import com.gnet.model.admin.*;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.ExcelUtil;
import com.gnet.utils.PriceUtils;
import com.gnet.utils.SpringContextHolder;
import com.gnet.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @program: certification-api-jg-2
 * @description: 导入培养计划数据解析处理服务类
 * @author: Gjm
 * @create: 2020-10-10 09:33
 **/
@Slf4j
@Component("CcAimportPlanService")
public class CcAimportPlanService {

    private Long planId;
    //课程类型 1理论 2实践
    private Integer courseType;
    /**
     * 读取excel数据
     *
     * @param path
     */
    public Result readExcelToObj(String path,Long planIds,Integer type) {
        planId=planIds;
        courseType=type;
        Workbook wb = null;


        try {
            wb = WorkbookFactory.create(new File(path));
            Result result = readExcel(wb, 0, 3, 0);
            return result;
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.ok();
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

        Row row = null;
        /*HashMap result = new HashMap<>();*/
        //教学安排和各学期周学时分配的属性
        HashMap<Integer, String> teachAndweekHoursMap = new HashMap<>();
        //教学安排的一些课时 <列，名称>
        HashMap<Integer, String> teachPlanHoursMap = new HashMap<>();
        // 各学期周学时分配<列，名称>
        HashMap<Integer, String> weekHoursMap = new HashMap<>();
        //课程层次
        ArrayList<String> ccCourseHierarchieList = new ArrayList<>();
        //次要课程层次
        ArrayList<String> ccCourseHierarchySecondaryList = new ArrayList<>();
        //课程性质
        ArrayList<String> ccCoursePropertyList = new ArrayList<>();
        //次要课程性质
        ArrayList<String> ccCoursePropertySecondaryList = new ArrayList<>();
        //专业方向
        ArrayList<String> majorDirectionList = new ArrayList<>();
        //课程信息List
        ArrayList<HashMap<String, Object>> couserInfoList = new ArrayList<>();
        //课程类别
        ArrayList<CcCourseType> ccCourseTypes = new ArrayList<>();
        //课程模块
        ArrayList<String> ccCourseModule = new ArrayList<>();
        for (int i = startReadLine; i < sheet.getLastRowNum() - tailLine + 1; i++) {

            //课程信息
            HashMap<String, Object> courseMap = new HashMap<>();
            row = sheet.getRow(i);
            for (Cell c : row) {
                String returnStr = "";
                boolean isMerge = ExcelUtil.isMergedRegion(sheet, i, c.getColumnIndex());
                //判断是否具有合并单元格
                if (isMerge) {
                    String rs = ExcelUtil.getMergedRegionValue(sheet, row.getRowNum(), c.getColumnIndex());
                    returnStr = rs;
                } else {
                    returnStr = ExcelUtil.getCellValue(c).replaceAll(" ","").replace("\n","");
                }
                //处理表头属性
                if (i>2 && i<=3){
                    String teachPlan= "";
                    //取表头属性
                    Row row0 = sheet.getRow(2);
                    Cell cell = row0.getCell(c.getColumnIndex());

                    boolean isMerge0 = ExcelUtil.isMergedRegion(sheet, 2, c.getColumnIndex());
                    //判断是否具有合并单元格
                    if (isMerge0) {
                        String rs = ExcelUtil.getMergedRegionValue(sheet, row0.getRowNum(), c.getColumnIndex());
                        teachPlan = rs;
                    } else {
                        teachPlan = ExcelUtil.getCellValue(cell).replaceAll(" ","").replace("\n","");
                    }
                    if (courseType==1 && c.getColumnIndex()==0){
                        if (!teachPlan.equals("课程层次")){
                            return Result.error("错误：导入模板有问题，请重新下载");
                        }
                    }
                    if (courseType==2 && c.getColumnIndex()==0){
                        if (!teachPlan.equals("课程代码")){
                            return Result.error("错误：导入模板有问题，请重新下载");
                        }
                    }
                    if (teachPlan.equals("教学安排")){
                        //存入map 把课时列与名称
                        teachPlanHoursMap.put(c.getColumnIndex(),returnStr);
                        teachAndweekHoursMap.put(c.getColumnIndex(),returnStr);
                    }
                    else if (teachPlan.equals("各学期周学时分配") && i==3){
                        //这里从第四行开始获取学年、第五行学期、第六行周数
                        String yearWearks="";
                        for (int j=3; j<=5;j++){
                            if (j==3){
                                yearWearks = ExcelUtil.isMeargeStringValue(sheet, j, c.getColumnIndex());
                            }else if (j==4){
                                yearWearks=yearWearks+":"+ExcelUtil.isMeargeStringValue(sheet, j, c.getColumnIndex());
                            }else {
                                yearWearks=yearWearks+"-"+ExcelUtil.isMeargeStringValue(sheet, j, c.getColumnIndex());
                            }

                        }
                        weekHoursMap.put(c.getColumnIndex(),yearWearks);
                        teachAndweekHoursMap.put(c.getColumnIndex(),yearWearks);

                    }else if (teachPlan.contains("课程类别")){
                        teachAndweekHoursMap.put(c.getColumnIndex(),returnStr);
                        //处理课程类型的属性
                        String courseTypeString = teachPlan.substring(teachPlan.indexOf("（")+1, teachPlan.indexOf("）"));
                        String[] courseType = courseTypeString.split("；");
                        if (courseType.length>0){
                            for (String type : courseType){
                                String typeValue = type.substring(0, type.indexOf(":"));
                                String typeName = type.substring(type.indexOf(":")+1);
                                if (!CcCourseType.dao.isExisted(typeValue, planId) && !CcCourseType.dao.isExisted(typeName, planId)) {
                                    CcCourseType ccCourseType = new CcCourseType();
                                    ccCourseType.set("type_value",typeValue);
                                    ccCourseType.set("type_name",typeName);
                                    ccCourseTypes.add(ccCourseType);
                                }
                            }

                        }

                    }
                    else {

                        int columnIndex = c.getColumnIndex();
                        if (columnIndex==1 && courseType==1){
                            teachAndweekHoursMap.put(c.getColumnIndex(),"次要课程层次");
                        }else if (columnIndex==3 && courseType==1){
                            teachAndweekHoursMap.put(c.getColumnIndex(),"次要课程性质");
                        }else {
                            teachAndweekHoursMap.put(c.getColumnIndex(),returnStr);
                        }

                    }

                }

                //以下为课程数据--从第7行开始取------------------------------

                if (i>=6 && !returnStr.equals("")){
                    //理论课程
                    if (courseType==1){
                        //课程层次
                        if (c.getColumnIndex() == 0) {

                            if (!ccCourseHierarchieList.contains(returnStr) ){
                                ccCourseHierarchieList.add(returnStr);
                            }

                            //次要课程层次
                        } else if (c.getColumnIndex() == 1) {

                            if (!ccCourseHierarchySecondaryList.contains(returnStr)){
                                ccCourseHierarchySecondaryList.add(returnStr);
                            }

                            //课程性质
                        } else if (c.getColumnIndex() == 2) {

                            if (!ccCoursePropertyList.contains(returnStr)){
                                ccCoursePropertyList.add(returnStr);
                            }
                            //次要课程性质
                        } else if (c.getColumnIndex() == 3) {

                            if (!ccCoursePropertySecondaryList.contains(returnStr)){
                                ccCoursePropertySecondaryList.add(returnStr);
                            }
                            //专业方向
                        }else if (teachAndweekHoursMap.get(c.getColumnIndex()).equals("专业方向")) {

                            if (!majorDirectionList.contains(returnStr)){
                                majorDirectionList.add(returnStr);
                            }
                        }
                    }else{
                        if (teachAndweekHoursMap.get(c.getColumnIndex()).equals("专业方向")) {

                            if (!majorDirectionList.contains(returnStr)){
                                majorDirectionList.add(returnStr);
                            }
                        }
                        if (teachAndweekHoursMap.get(c.getColumnIndex()).equals("所属模块")) {

                            if (!ccCourseModule.contains(returnStr)){
                                ccCourseModule.add(returnStr);
                            }
                        }
                    }


                    courseMap.put(teachAndweekHoursMap.get(c.getColumnIndex()),returnStr);

                }

            }
            if (courseMap.size()!=0){
                couserInfoList.add(courseMap);
            }


        }

        //处理学年、学期的属性
        if (weekHoursMap!=null){
            Result coursePropertyResult = manageCourseProperty(weekHoursMap);


            if (!coursePropertyResult.getFlag()){
                return coursePropertyResult;
            }
            log.info("学年、学期处理成功-----------");
        }else {
            log.error("错误：学期学年数据为空------------");
            return Result.error("错误：学期学年数据不能为空！");
        }

        //处理课程层次
        if ( courseType ==1){
            if (ccCourseHierarchieList.size()!=0 ){
                Result courseHierarchyResult = manageCourseHierarchy(ccCourseHierarchieList);
                if (!courseHierarchyResult.getFlag()){
                    return courseHierarchyResult;
                }
                log.info("课程层次处理成功-----------");
            }
            else {
                log.error("错误：课程层次为空------------");
                return Result.error("错误：课程层次不能为空");
            }

            //处理课程次要层次
            if (ccCourseHierarchySecondaryList.size()!=0 ){
                Result result = manageCourseHierarchySecondary(ccCourseHierarchySecondaryList);
                if (!result.getFlag()){
                    return result;
                }
                log.info("次要课程层次处理成功-----------");
            }
            //处理课程性质
            if (ccCoursePropertyList.size()!=0){
                Result result = manageCourseProperty(ccCoursePropertyList);
                if (!result.getFlag()){
                    return result;
                }
                log.info("课程性质处理成功-----------");
            }else {
                log.error("错误：课程性质为空------------");
                return Result.error("错误：课程性质不能为空");
            }
            //处理课程次要性质
            if (ccCoursePropertySecondaryList.size()!=0 ){
                Result result = manageCoursePropertySecondary(ccCoursePropertySecondaryList);
                if (!result.getFlag()){
                    return result;
                }
                log.info("次要课程性质处理成功-----------");
            }
            if (ccCourseTypes.size()!=0 ){
                Result result = manageCourseType(ccCourseTypes);
                if (!result.getFlag()){
                    return result;
                }
                log.info("课程类型处理成功-----------");
            }

        }else {
            if (ccCourseModule.size()!=0){
                Result result = manageCourseModel(ccCourseModule);
                if (!result.getFlag()){
                    return result;
                }
                log.info("课程所属模块处理成功-----------");
            }else {
                log.error("错误：课程所属模块为空------------");
                return Result.error("错误：课程所属模块不能为空");
            }
        }

        if (majorDirectionList.size()!=0){
            Result result = manageMajorDirection(majorDirectionList);
            if (!result.getFlag()){
                return result;
            }
            log.info("课程专业方向处理成功-----------");
        }


        //处理课程
        if (couserInfoList.size()!=0){
            Result result = manageCourse(couserInfoList, weekHoursMap);
            if (!result.getFlag()){
                return result;
            }
            log.info("---新增课程处理成功-----------");
        }
        else {
            log.error("错误：课程信息为空--------");
            return Result.error("错误：课程信息为空");
        }
        /*//所有的属性
        result.put("teachAndweekHoursMap",teachAndweekHoursMap);
        //教学安排的属性
        result.put("teachPlanHoursMap",teachPlanHoursMap);*/

        return Result.ok();
    }

    /*
     * @param couserInfoList
     * @return boolean
     * @author Gejm
     * @description: 课程信息处理
     * @date 2020/10/12 15:51
     */
    public Result manageCourse(ArrayList<HashMap<String, Object>> couserInfoList,HashMap<Integer, String> weekHoursMap){
        IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
        Date date = new Date();
        ArrayList<CcCourse> ccCourses = new ArrayList<>();
        List<CcPlanTermCourse> ccPlanTermCourseSaveList = new ArrayList<>();
        for (int i=0; i<couserInfoList.size(); i++){
            HashMap<String, Object> courseInfo = couserInfoList.get(i);
            CcCourse ccCourse = new CcCourse();
            long courseId = idGenerate.getNextValue();
            //----课程固定信息----
            ccCourse.set("id",courseId);
            ccCourse.set("type",courseType);
            ccCourse.set("plan_id",planId);
            ccCourse.set("create_date",date);
            ccCourse.set("modify_date",date);
            ccCourse.set("is_del", Boolean.FALSE);
            //周学时
            BigDecimal week_hour = new BigDecimal("0");
            String courseName;
            //-------以下理论与实践共有信息------------------
            if (courseInfo.get("课程代码")!=null){
                String courseCode = courseInfo.get("课程代码").toString();
                if (CcCourse.dao.isExisted("code", planId, courseCode)) {
                    log.error("错误：课程代码已存在！");
                    return Result.error("错误："+courseCode+"课程代码已存在！");
                }
                if (courseCode.length()>40){
                    return Result.error("错误："+courseCode+"的课程名称太长，请检查！");
                }
                ccCourse.set("code",courseCode);
            }else {
                log.error("错误：课程代码不可为空！");
                return Result.error("错误：课程代码不可为空！");
            }
            if (courseInfo.get("课程名称")!=null){
                 courseName = courseInfo.get("课程名称").toString();
                 if (courseName.length()>15){
                     return Result.error("错误："+courseName+"的课程名称太长，请检查！");
                 }
                ccCourse.set("name",courseName);
            }else {
                log.error("错误：课程名称不可为空！");
                return Result.error("错误：课程名称不可为空！");

            }
            if (courseInfo.get("英文名称")!=null){
                String englishName = courseInfo.get("英文名称").toString();
                if (englishName.length()>40){
                    return Result.error("错误："+courseName+"的英文名称太长，请检查！");
                }
                ccCourse.set("english_name",englishName);
            }else {
                log.error("错误：课程英文名称不可为空！");
                return Result.error("错误：课程英文名称不可为空！");
            }
            if (courseInfo.get("专业方向")!=null){
                String majorDirection = courseInfo.get("专业方向").toString();
                CcMajorDirection direction = CcMajorDirection.dao.findDirection(majorDirection, planId);
                if (direction != null){
                    ccCourse.set("direction_id",direction.getLong("id"));
                }else {
                    log.error("错误：没有找到专业方向！------");
                    return Result.error("错误：没有找到专业方向--"+majorDirection);
                }
            }
            if (courseInfo.get("学分")!=null){
                String credit = courseInfo.get("学分").toString();
                log.info("-----------学分转换为Bigdecimal------");
                ccCourse.set("credit",new BigDecimal(credit) );
            }else {
                log.error("错误：课程学分不可为空！");
                return Result.error("错误：课程"+courseName+"的学分不可为空！");
            }



            //循环各个周学时是否都为空,只取获得值的第一个数据，取到之后就取消循环
            int a=0;
            for (String value : weekHoursMap.values()) {
                if (courseInfo.get(value)!=null && a==0){
                    String week = courseInfo.get(value)+"";
                    week_hour=week_hour.add(new BigDecimal(week));
                    a=1;
                }
                //---这里是上课学期与课程关联的地方
                if (courseInfo.get(value)!=null ){
                    String termName=value.substring(value.indexOf(":")+1,value.indexOf("-"));
                    CcPlanTerm ccPlanTerm = CcPlanTerm.dao.findPlanId(planId, termName);
                    if (ccPlanTerm != null){
                        Long planTermId = ccPlanTerm.getLong("planTermId");
                        CcPlanTermCourse temp = new CcPlanTermCourse();
                        temp.set("id", idGenerate.getNextValue());
                        temp.set("create_date", date);
                        temp.set("modify_date", date);
                        temp.set("plan_term_id", planTermId);
                        temp.set("course_id", courseId);
                        temp.set("type", CcPlanTermCourse.TYPE_CLASS);
                        temp.set("is_del", Boolean.FALSE);
                        ccPlanTermCourseSaveList.add(temp);
                    }
                }


            }
            if (PriceUtils.greaterThan(week_hour,new BigDecimal(0))){
                ccCourse.set("week_hour",week_hour);
            }else {
                log.error("------错误：周学时不能小于等于0----");
                return Result.error("错误：课程"+courseName+"的周学时不能小于等于0！");
            }

            if (courseInfo.get("备注")!=null){
                String remark = courseInfo.get("备注")+"";
                ccCourse.set("remark",remark);

            }

            if (courseType==1){
                if (courseInfo.get("课程层次")!=null){
                    String hierarchyName = courseInfo.get("课程层次").toString();
                    CcCourseHierarchy hierarchy = CcCourseHierarchy.dao.findHierarchyId(hierarchyName,planId);
                    if (hierarchy!=null){
                        ccCourse.set("hierarchy_id",hierarchy.getLong("id"));
                    }else {
                        log.error("错误：没有找到课程层次！------");
                        return Result.error("错误：课程"+courseName+"没有找到课程层次！");

                    }

                }else{
                    log.error("错误：课程层次不可为空！------");
                    return Result.error("错误：课程"+courseName+"的课程层次不可为空！");

                }
                if (courseInfo.get("次要课程层次")!=null){
                    String hierarchyName = courseInfo.get("次要课程层次").toString();
                    CcCourseHierarchySecondary hierarchySecondary= CcCourseHierarchySecondary.dao.findHierarchySecondaryId(hierarchyName,planId);
                    if (hierarchySecondary != null){
                        ccCourse.set("hierarchy_secondary_id",hierarchySecondary.getLong("id"));
                    }else {
                        log.error("错误：没有找到次要课程层次！------");
                        return Result.error("错误：课程"+courseName+"没有找到次要课程层次！");
                    }
                }

                if (courseInfo.get("课程性质")!=null){
                    String propertyName = courseInfo.get("课程性质").toString();
                    CcCourseProperty courseProperty = CcCourseProperty.dao.findCourseProperty(propertyName,planId);
                    if (courseProperty != null){
                        ccCourse.set("property_id",courseProperty.getLong("id"));
                    }else {
                        log.error("错误：没有找到课程性质！------");
                        return Result.error("错误：课程"+courseName+"没有找到课程性质！");

                    }
                }else{
                    log.error("错误：课程性质不可为空！------");
                    return Result.error("错误：课程"+courseName+"没有找到课程性质不可为空！");

                }

                if (courseInfo.get("次要课程性质")!=null){
                    String propertyName = courseInfo.get("次要课程性质").toString();
                    CcCoursePropertySecondary courseProperty = CcCoursePropertySecondary.dao.findCoursePropertySecondary(propertyName,planId);
                    if (courseProperty != null){
                        ccCourse.set("property_secondary_id",courseProperty.getLong("id"));
                    }else {
                        log.error("错误：没有找到次要课程性质！------");
                        return Result.error("错误：课程"+courseName+"没有找到次要课程性质！");
                    }
                }
                //理论学时
                BigDecimal theory_hours = new BigDecimal("0");
                //实验学时
                BigDecimal experiment_hours = new BigDecimal("0");
                //实践学时
                BigDecimal practice_hours = new BigDecimal("0");
                //习题学时
                BigDecimal exercises_hours = new BigDecimal("0");
                //研讨学时
                BigDecimal dicuss_hours = new BigDecimal("0");
                //上机学时
                BigDecimal operate_computer_hours = new BigDecimal("0");

                if (courseInfo.get("理论学时")!=null){
                    String theory = courseInfo.get("理论学时").toString();
                    log.info("-----------理论学时转换为Bigdecimal------");
                    theory_hours=new BigDecimal(theory);
                    if (!PriceUtils.lessThan(theory_hours,new BigDecimal(0))){
                        ccCourse.set("theory_hours",theory_hours);
                    }else{
                        log.error("------错误：理论学时不能小于0----");
                        return Result.error("错误：课程"+courseName+"的理论学时不能小于0！");
                    }

                }
                if (courseInfo.get("实验学时")!=null){
                    String experiment = courseInfo.get("实验学时").toString();
                    log.info("-----------实验学时转换为Bigdecimal------");
                    experiment_hours=new BigDecimal(experiment);
                    if (!PriceUtils.lessThan(experiment_hours,new BigDecimal(0))){
                        ccCourse.set("experiment_hours",experiment_hours);
                    }else{
                        log.error("------错误：实验学时不能小于0----");
                        return Result.error("错误：课程"+courseName+"的实验学时不能小于0！");
                    }

                }
                if (courseInfo.get("实践学时")!=null){
                    String practice = courseInfo.get("实践学时").toString();
                    log.info("-----------实践学时转换为Bigdecimal------");
                    practice_hours=new BigDecimal(practice);
                    if (!PriceUtils.lessThan(practice_hours,new BigDecimal(0))){
                        ccCourse.set("practice_hours",practice_hours);
                    }else{
                        log.error("------错误：实践学时不能小于0----");
                        return Result.error("错误：课程"+courseName+"的实践学时不能小于0！");
                    }

                }
                if (courseInfo.get("习题学时")!=null){
                    String exercises = courseInfo.get("习题学时").toString();
                    log.info("-----------习题学时转换为Bigdecimal------");
                    exercises_hours=new BigDecimal(exercises);
                    if (!PriceUtils.lessThan(exercises_hours,new BigDecimal(0))){
                        ccCourse.set("exercises_hours",exercises_hours);
                    }else{
                        log.error("------错误：习题学时不能小于0----");
                        return Result.error("错误：课程"+courseName+"的习题学时不能小于0！");
                    }

                }
                if (courseInfo.get("研讨学时")!=null){
                    String dicuss = courseInfo.get("研讨学时").toString();
                    log.info("-----------研讨学时转换为Bigdecimal------");
                    dicuss_hours=new BigDecimal(dicuss);
                    if (!PriceUtils.lessThan(dicuss_hours,new BigDecimal(0))){
                        ccCourse.set("dicuss_hours",dicuss_hours);
                    }else{
                        log.error("------错误：研讨学时不能小于0----");
                        return Result.error("错误：课程"+courseName+"的研讨学时不能小于0！");
                    }

                }
                if (courseInfo.get("上机学时")!=null){
                    String operate_computer = courseInfo.get("上机学时").toString();
                    log.info("-----------上机学时转换为Bigdecimal------");
                    operate_computer_hours=new BigDecimal(operate_computer);

                    if (!PriceUtils.lessThan(operate_computer_hours,new BigDecimal(0))){
                        ccCourse.set("operate_computer_hours",operate_computer_hours);
                    }else{
                        log.error("------错误：上机学时不能小于0----");
                        return Result.error("错误：课程"+courseName+"的上机学时不能小于0！");
                    }
                }
                if (courseInfo.get("课外学时")!=null){
                    String extracurricular = courseInfo.get("课外学时").toString();
                    log.info("-----------课外学时转换为Bigdecimal------");
                    if (!PriceUtils.lessThan(new BigDecimal(extracurricular),new BigDecimal(0))){
                        ccCourse.set("extracurricular_hours",new BigDecimal(extracurricular));
                    }else{
                        log.error("------错误：课外学时不能小于0----");
                        return Result.error("错误：课程"+courseName+"的课外学时不能小于0！");
                    }

                }

                //总学时=理论、研讨、上机、习题、实践、实验相加
                BigDecimal all_hours=theory_hours.add(experiment_hours).add(practice_hours).add(exercises_hours).add(dicuss_hours).add(operate_computer_hours);
                if (PriceUtils.greaterThan(all_hours,new BigDecimal(0))){
                    ccCourse.set("all_hours",all_hours);
                }else{
                    log.error("------错误：总学时不能小于等于0----");
                    return Result.error("错误：课程"+courseName+"的教学安排学时不能都为空或0！");
                }
                //获取课程类型
                for (String key : courseInfo.keySet()){
                    //判断key是否包含课程类型这几个字
                    if(key.contains("课程类别")){
                        if (courseInfo.get(key) != null){
                            CcCourseType courseTypea = CcCourseType.dao.findCourseType(planId, courseInfo.get(key).toString());
                            if (courseTypea !=null){
                                ccCourse.set("type_id",courseTypea.getLong("id"));
                            }
                        }
                    }

                }
                if (courseInfo.get("考试学期")!=null){
                    String planTermExamIdsStr = courseInfo.get("考试学期").toString();
                    //英文逗号隔开
                    String[] planTermExamIds = planTermExamIdsStr.split(",");
                    // 建立培养计划课程学期详情表 这是考试学期与课程关联的地方
                    if( planTermExamIds.length > 0) {
                        for(String term : planTermExamIds) {
                            //因为取学期得时候有文本存储的有数字形式存储的，数字存储的会存在后缀.0，所以去掉
                            if(term.contains(".")){
                                term=term.substring(0,term.indexOf("."));
                            }
                            // 理论课只有长学期
                            CcPlanTerm ccPlanTerm = CcPlanTerm.dao.findPlanId(planId, "长"+term);
                            if (ccPlanTerm != null){
                                Long planTermId = ccPlanTerm.getLong("planTermId");
                                CcPlanTermCourse temp = new CcPlanTermCourse();
                                temp.set("id", idGenerate.getNextValue());
                                temp.set("create_date", date);
                                temp.set("modify_date", date);
                                temp.set("plan_term_id", planTermId);
                                temp.set("course_id", courseId);
                                temp.set("type", CcPlanTermCourse.TYPE_EXAM);
                                temp.set("is_del", Boolean.FALSE);
                                ccPlanTermCourseSaveList.add(temp);
                            }else {
                                log.error("错误：课程"+courseName+"的考试学期没有找到！");
                                return Result.error("错误：课程"+courseName+"的考试学期没有找到！");
                            }
                        }
                    }else{
                        log.error("------错误：考试学期读取错误----");
                        return Result.error("错误：课程"+courseName+"的考试学期读取错误！");
                    }

                }else{
                    log.error("------错误：考试学期不能为空----");
                    return Result.error("错误：课程"+courseName+"的考试学期不能都为空！");
                }

            }else {
                //实践课程所属模块
                if (courseInfo.get("所属模块")!=null){
                    String name = courseInfo.get("所属模块").toString();
                    CcCourseModule ccCourseModule = CcCourseModule.dao.findByModel(planId, name);
                    if (ccCourseModule!=null){
                        ccCourse.set("module_id",ccCourseModule.getLong("id"));
                    }else {
                        log.error("错误：没有找到课程所属模块！------");
                        return Result.error("错误：课程"+courseName+"没有找到课程所属模块！");
                    }

                }else{
                    log.error("错误：课程所属模块不可为空！------");
                    return Result.error("错误：课程"+courseName+"课程所属模块不可为空！");
                }
                if (courseInfo.get("周或学时")!=null){
                    String all_hours = courseInfo.get("周或学时").toString();
                    log.info("-----------学分转换为Bigdecimal------");
                    ccCourse.set("all_hours",new BigDecimal(all_hours) );
                }else {
                    log.error("错误：周或学时不可为空！");
                    return Result.error("错误：课程"+courseName+"周或学时不可为空！");
                }
            }

            ccCourses.add(ccCourse);




        }
        if (ccPlanTermCourseSaveList.size()!=0){
            log.info("需要添加"+ccPlanTermCourseSaveList.size()+"条课程学期数据");
            if (!CcPlanTermCourse.dao.batchSave(ccPlanTermCourseSaveList)){
                log.error("--------错误：添加课程学期失败了----------");
                TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
                return Result.error("错误：添加课程学期失败了");
            }
        }
        if (ccCourses.size()!=0){
            log.info("需要添加"+ccCourses.size()+"条课程");
            try {
                boolean result = CcCourse.dao.batchSave(ccCourses);
                if (!result){
                    log.error("--------错误：添加课程失败了，请检查课程信息是否格式错误！----------");

                    return Result.error("错误：添加课程失败了，请检查课程信息是否格式错误！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                //设置手动回滚 ，捕捉异常会导致@Transactional失效
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.error("错误：添加课程失败了，请检查课程信息是否格式错误！");
            }

        }else {
            return Result.error("错误：没有要添加的课程！");
        }

        return Result.ok();
    }

    private Result manageCourseModel(ArrayList<String> ccCourseModels){
        Date date = new Date();
        IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
        ArrayList<CcCourseModule> ccCourseModuleList = new ArrayList<>();
        for (int i=0;i<ccCourseModels.size();i++){
            CcCourseModule ccCourseModule = new CcCourseModule();
            String name = ccCourseModels.get(i);
            ccCourseModule.set("id",idGenerate.getNextValue());
            ccCourseModule.set("create_date", date);
            ccCourseModule.set("modify_date", date);
            ccCourseModule.set("plan_id", planId);
            ccCourseModule.set("module_name", name);
            ccCourseModule.set("is_del", Boolean.FALSE);
            ccCourseModuleList.add(ccCourseModule);
        }
        log.info("需要添加"+ccCourseModuleList.size()+"条课程模块");
        try {
            boolean result=CcCourseModule.dao.batchSave(ccCourseModuleList);
            if (!result){
                log.error("--------错误：添加课程模块失败了----------");

                return Result.error("错误：添加课程模块失败了");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //设置手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return Result.ok();

    }
    /*
     * @param ccCourseTypes
     * @return boolean
     * @author Gejm
     * @description: 处理课程类型
     * @date 2020/10/13 17:28
     */
    private Result manageCourseType(ArrayList<CcCourseType> ccCourseTypes){
        Date date = new Date();
        IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
        for (CcCourseType ccCourseType : ccCourseTypes){
            ccCourseType.set("id",idGenerate.getNextValue());
            ccCourseType.set("plan_id",planId);
            ccCourseType.set("modify_date",date);
            ccCourseType.set("is_del",Boolean.FALSE);
        }
        log.info("需要添加"+ccCourseTypes.size()+"条课程类别");

        try {
            boolean result=CcCourseType.dao.batchSave(ccCourseTypes);
            if (!result){
                log.error("--------错误：添加课程类型失败了----------");
                TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
                return Result.error("错误：添加课程类型失败了");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //设置手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return Result.ok();
    }
    /*
     * @param majorDirection
     * @return boolean
     * @author Gejm
     * @description: 专业方向处理
     * @date 2020/10/13 9:46
     */
    private Result manageMajorDirection(ArrayList<String> majorDirectionList){
        Date date = new Date();
        IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
        ArrayList<CcMajorDirection> ccMajorDirections = new ArrayList<>();
        for(int i=0; i<majorDirectionList.size();i++){
            String name = majorDirectionList.get(i);
            if (name.equals("")||name==null){
                break;
            }
            if (!CcMajorDirection.dao.isExisted(name,planId,null)){
                CcMajorDirection ccMajorDirection = new CcMajorDirection();
                ccMajorDirection.set("id", idGenerate.getNextValue());
                ccMajorDirection.set("create_date", date);
                ccMajorDirection.set("modify_date", date);
                ccMajorDirection.set("plan_id", planId);
                ccMajorDirection.set("name", name);
                ccMajorDirection.set("is_del", Boolean.FALSE);
                ccMajorDirections.add(ccMajorDirection);
            }

        }
        if (ccMajorDirections.size()!=0){
            log.info("需要添加"+ccMajorDirections.size()+"条课程专业方向");
            try {
                boolean result=CcMajorDirection.dao.batchSave(ccMajorDirections);
                if (!result){
                    log.error("--------错误：添加课程专业方向失败了----------");
                    TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
                    return Result.error("错误：添加课程专业方向失败了");
                }
            } catch (Exception e) {
                e.printStackTrace();
                //设置手动回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }

        }

        return  Result.ok();

    }
    /*
     * @param ccCourseHierarchieList
     * @return boolean
     * @author Gejm
     * @description: 处理课程层次属性
     * @date 2020/10/12 14:57
     */
    private  Result manageCourseHierarchy(ArrayList<String> ccCourseHierarchieList){
        Date date = new Date();
        IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
        ArrayList<CcCourseHierarchy> ccCourseHierarchies = new ArrayList<>();
        for(int i=0; i<ccCourseHierarchieList.size();i++){
            String name = ccCourseHierarchieList.get(i);
            if (name.equals("")||name==null){
                break;
            }
            if (!CcCourseHierarchy.dao.isExisted(name, null, planId)) {
                CcCourseHierarchy ccCourseHierarchy = new CcCourseHierarchy();
                ccCourseHierarchy.set("id", idGenerate.getNextValue());
                ccCourseHierarchy.set("create_date", date);
                ccCourseHierarchy.set("modify_date", date);
                ccCourseHierarchy.set("plan_id", planId);
                ccCourseHierarchy.set("name", name);
                ccCourseHierarchy.set("is_del", Boolean.FALSE);
                ccCourseHierarchies.add(ccCourseHierarchy);
            }

        }
        if (ccCourseHierarchies.size()!=0){
            log.info("需要添加"+ccCourseHierarchies.size()+"条课程层次");

            try {
                boolean result=CcCourseHierarchy.dao.batchSave(ccCourseHierarchies);
                if (!result){
                    log.error("--------错误：添加课程层次失败了----------");
                    TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
                    return Result.error("错误：添加课程层次失败了!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                //设置手动回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }


        }
        return Result.ok();
    }
    /*
     * @param ccCourseHierarchySecondaryList
     * @return boolean
     * @author Gejm
     * @description: 处理课程次要层次信息
     * @date 2020/10/12 15:23
     */
    private  Result manageCourseHierarchySecondary(ArrayList<String> ccCourseHierarchySecondaryList){
        Date date = new Date();
        IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
        ArrayList<CcCourseHierarchySecondary> ccCourseHierarchySecondarys = new ArrayList<>();
        for(int i=0; i<ccCourseHierarchySecondaryList.size();i++){
            String name = ccCourseHierarchySecondaryList.get(i);
            if (name.equals("")||name==null){
                continue;
            }
            if (!CcCourseHierarchySecondary.dao.isExisted(name, null, planId)) {
                CcCourseHierarchySecondary ccCourseHierarchySecondary = new CcCourseHierarchySecondary();
                ccCourseHierarchySecondary.set("id", idGenerate.getNextValue());
                ccCourseHierarchySecondary.set("create_date", date);
                ccCourseHierarchySecondary.set("modify_date", date);
                ccCourseHierarchySecondary.set("plan_id", planId);
                ccCourseHierarchySecondary.set("name", name);
                ccCourseHierarchySecondary.set("is_del", Boolean.FALSE);
                ccCourseHierarchySecondarys.add(ccCourseHierarchySecondary);
            }

        }
        if (ccCourseHierarchySecondarys.size()!=0){
            log.info("需要添加"+ccCourseHierarchySecondarys.size()+"条课程次要层次");
            try {
                boolean result=CcCourseHierarchySecondary.dao.batchSave(ccCourseHierarchySecondarys);
                if (!result){
                    log.error("--------错误：添加课程次要层次失败了----------");
                    TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
                    return Result.error("错误：添加课程次要层次失败了!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                //设置手动回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }

        }
        return Result.ok();
    }
    /*
     * @param coursePropertyList
     * @return boolean
     * @author Gejm
     * @description: 处理课程性质信息
     * @date 2020/10/12 15:23
     */
    private  Result manageCourseProperty(ArrayList<String> coursePropertyList){
        Date date = new Date();
        IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
        ArrayList<CcCourseProperty> coursePropertys = new ArrayList<>();
        for(int i=0; i<coursePropertyList.size();i++){
            String name = coursePropertyList.get(i);
            if (name.equals("")||name==null){
                return  Result.error("错误：课程性质名称为空");
            }
            if (!CcCourseProperty.dao.isExisted(name, planId)) {

                CcCourseProperty ccCourseProperty = new CcCourseProperty();
                ccCourseProperty.set("id", idGenerate.getNextValue());
                ccCourseProperty.set("create_date", date);
                ccCourseProperty.set("modify_date", date);
                ccCourseProperty.set("plan_id", planId);
                ccCourseProperty.set("property_name", name);
                ccCourseProperty.set("is_del", Boolean.FALSE);
                coursePropertys.add(ccCourseProperty);
            }

        }
        if (coursePropertys.size()!=0){
            log.info("需要添加"+coursePropertys.size()+"条课程性质");
            try {
                boolean result=CcCourseProperty.dao.batchSave(coursePropertys);
                if (!result){
                    log.error("--------错误：添加课程性质失败了----------");
                    TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
                    return Result.error("错误：添加课程性质失败了");
                }
            } catch (Exception e) {
                e.printStackTrace();
                //设置手动回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }

        }
        return Result.ok();
    }
    /*
     * @param coursePropertySecondaryList
     * @return boolean
     * @author Gejm
     * @description: 处理次要课程性质
     * @date 2020/10/12 15:37
     */
    private  Result manageCoursePropertySecondary(ArrayList<String> coursePropertySecondaryList){
        Date date = new Date();
        IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
        ArrayList<CcCoursePropertySecondary> coursePropertySecondarys = new ArrayList<>();
        for(int i=0; i<coursePropertySecondaryList.size();i++){
            String name = coursePropertySecondaryList.get(i);
            if (name.equals("")||name==null){
                return  Result.error("错误：次要课程性质名称为空");
            }
            if (!CcCoursePropertySecondary.dao.isExisted(name,null, planId)) {

                CcCoursePropertySecondary ccCoursePropertySecondary = new CcCoursePropertySecondary();
                ccCoursePropertySecondary.set("id", idGenerate.getNextValue());
                ccCoursePropertySecondary.set("create_date", date);
                ccCoursePropertySecondary.set("modify_date", date);
                ccCoursePropertySecondary.set("plan_id", planId);
                ccCoursePropertySecondary.set("property_name", name);
                ccCoursePropertySecondary.set("is_del", Boolean.FALSE);
                coursePropertySecondarys.add(ccCoursePropertySecondary);
            }

        }
        if (coursePropertySecondarys.size()!=0){
            log.info("需要添加"+coursePropertySecondarys.size()+"条次要课程性质");
            try {
                boolean result=CcCoursePropertySecondary.dao.batchSave(coursePropertySecondarys);
                if (!result){
                    log.error("--------错误：添加次要课程性质失败了----------");
                    TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
                    return Result.error("错误：添加次要课程性质失败了");
                }
            } catch (Exception e) {
                e.printStackTrace();
                //设置手动回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }

        }
        return Result.ok();
    }
    /*
     * @param weekHoursMap 
     * @return boolean
     * @author Gejm
     * @description: 处理学期周学时属性及保存
     * @date 2020/10/12 10:12
     */

    private Result manageCourseProperty(HashMap<Integer, String> weekHoursMap){
        log.info("----------------开始提取学年、学期-------------------------");
        Date date = new Date();
        IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
        if (weekHoursMap==null){
            return Result.error("错误：提取不到学年、学期");
        }
        ArrayList<CcPlanTerm> planTermList = new ArrayList<>();
        //循环map
        for (String value : weekHoursMap.values()) {
            System.out.println("学年、学期、周数: "+value);
            //学年
            String yearName=value.substring(0,value.indexOf(":"));
            //学期名
            String termName=value.substring(value.indexOf(":")+1,value.indexOf("-"));
            //学期值
            String term =termName.substring(1);
            //判断学期值是否为数字
            if (!StringUtil.isInteger(term)){
                term="1";
            }
            System.out.println(term);
            String weekNumS=value.substring(0,value.indexOf(("-")));
            //周数
            String weekNum=value.substring(weekNumS.length()+1,value.length()-1);

            if (yearName.equals("")||yearName==null){
                log.error("错误：-----------学年获取为空！--------------------");
                return Result.error("错误：学年获取为空");
            }
            if (termName.equals("")||termName==null){
                log.error("错误：-----------学期获取为空！--------------------");
                return Result.error("错误：学期获取为空！");
            }
            if (weekNum.equals("")||weekNum==null){
                log.error("错误：-----------周数获取为空！--------------------");
                return Result.error("错误：周数获取为空!");
            }
            Integer termType;
            //判断是长学期还是短学期
            if (termName.contains("长")){
                termType=1;
            }else {
                termType=2;
            }
            Integer year;
            if (yearName.contains("一")){
                year=1;
            }else if (yearName.contains("二")){
                year=2;
            }
            else if (yearName.contains("三")){
                year=3;
            }else {
                year=4;
            }
            //判断是否存在
            if (!CcPlanTerm.dao.isExists(yearName, termName, termType, null, planId)) {
                CcPlanTerm ccPlanTerm = new CcPlanTerm();
                ccPlanTerm.set("id", idGenerate.getNextValue());
                ccPlanTerm.set("create_date", date);
                ccPlanTerm.set("modify_date", date);
                ccPlanTerm.set("year_name", yearName);
                ccPlanTerm.set("year", year);
                ccPlanTerm.set("term_name", termName);
                //学期值都默认为1
                ccPlanTerm.set("term", term);
                ccPlanTerm.set("term_type", termType);
                ccPlanTerm.set("plan_id", planId);
                ccPlanTerm.set("week_nums", weekNum);
                ccPlanTerm.set("is_del", Boolean.FALSE);
                planTermList.add(ccPlanTerm);
            }

        }
        if (planTermList.size()!=0){

            try {
                boolean result=CcPlanTerm.dao.batchSave(planTermList);
                if (!result){
                    log.error("--------错误：添加学年、学期失败了----------");
                    TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
                    return Result.error("错误：添加学年、学期失败了！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                //设置手动回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }

        }

        return Result.ok();
    }
}
