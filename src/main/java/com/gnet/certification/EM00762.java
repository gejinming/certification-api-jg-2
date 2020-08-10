package com.gnet.certification;

import com.gnet.Constant;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.gnet.plugin.configLoader.ConfigUtils;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.service.CcEduclassStudentService;
import com.gnet.service.CcEduindicationStuScoreService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

/**
 * 批量保存教学班学生
 *
 * @author xzl
 * @date 2017-10-11
 */
@Transactional(readOnly = false)
@Service("EM00762")
public class EM00762 extends BaseApi implements IApi {

    @Autowired
    private CcEduclassStudentService ccEduclassStudentService;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Response excute(Request request, Response response, ResponseHeader header, String method) {

        Map<String, Object> params = request.getData();
        List<HashMap> studentsMap = paramsJSONArrayFilter(params.get("students"), HashMap.class);
        String token = request.getHeader().getToken();
        Long schoolId = UserCacheKit.getSchoolId(token);
        Map<String, Object> result = Maps.newHashMap();
        // 学校不存在过滤
        if (schoolId == null) {
            return renderFAIL("0084", response, header);
        }

        if (studentsMap.isEmpty()) {
            return renderFAIL("0336", response, header);
        }


        List<Record> students = Lists.newArrayList();
        for (Map<String, String> student : studentsMap) {
            Record record = new Record();
            record.set("courseMajor", student.get("courseMajor"));
            record.set("grade", student.get("grade"));
            record.set("teacherNo", student.get("teacherNo"));
            record.set("courseCode", student.get("courseCode"));
            record.set("term", student.get("term"));
            record.set("eduClassName", student.get("eduClassName"));
            record.set("type", student.get("type"));
            record.set("studentNo", student.get("studentNo"));
            record.set("studentName", student.get("studentName"));
            record.set("major", student.get("major"));
            record.set("classes", student.get("classes"));
            students.add(record);
        }

        //导入教学班学生时是否需要验证学生是否已录入系统
        Boolean isValidateImportStudent = ConfigUtils.getBoolean("global", "isValidateImportStudent");
        if (isValidateImportStudent == null) {
            return renderFAIL("0954", response, header);
        }
        //学校下的学生
        List<CcStudent> ccStudents = CcStudent.dao.findBySchoolId(schoolId);
        if (isValidateImportStudent && ccStudents.isEmpty()) {
            return renderFAIL("0950", response, header);
        }
        //学校下的专业
        List<CcMajor> ccMajors = CcMajor.dao.findBySchoolId(schoolId);
        if (ccMajors.isEmpty()) {
            return renderFAIL("0951", response, header);
        }
        //学校下的行政班
        List<CcClass> ccClasses = CcClass.dao.getClassBySchool(schoolId);
        //学校下的所有的教师
        List<CcTeacher> ccTeachers = CcTeacher.dao.findFilteredByColumn("school_id", schoolId);
        if (ccTeachers.isEmpty()) {
            return renderFAIL("0952", response, header);
        }

        //还未建立教学班需要加入系统的学生
        Map<String, List<CcStudent>> studentListMap = new HashMap<>();
        //已经建立行政班需要加入系统的学生
        List<CcStudent> ccStudentList = Lists.newArrayList();
        //系统中已经存在教学班，直接增加学生和教学班关联
        List<CcEduclassStudent> classStudents = Lists.newArrayList();
        //系统中已经存在排课但是还未建立教学班，先建立教学班在增加关联
        Map<String, List<CcEduclassStudent>> classStudentMap = new HashMap<>();
        //教师开课课程编号+教学班姓名的字符串
        Set<String> teacherCourseAndClassNames = new HashSet<>();
        //key为专业名称value为专业编号
        Map<String, Long> majorMap = new HashMap<>();

        if (!ccEduclassStudentService.validateImportList(students, ccStudents, ccMajors, ccClasses, ccTeachers, classStudents, classStudentMap, teacherCourseAndClassNames, isValidateImportStudent, ccStudentList, studentListMap, majorMap)) {
            return renderFAIL("0953", response, header);
        }

        Date date = new Date();
        IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
        //key为开课课程编号+教学班名称，value为保存到数据库后的教学班编号
        Map<String, Long> classMap = new HashMap<>();
        //教学班列表
        List<CcEduclass> ccEduclasses = Lists.newArrayList();
        //先保存系统中已经存在的教师开课却还未建立教学班的数据
        for (String set : teacherCourseAndClassNames) {
            String keys[] = set.split("-");
            Long teacherCourseId = Long.valueOf(keys[0]);
            String eduClassName = keys[1];

            Long classId = idGenerate.getNextValue();
            CcEduclass ccEduclass = new CcEduclass();
            ccEduclass.set("id", classId);
            ccEduclass.set("create_date", date);
            ccEduclass.set("modify_date", date);
            ccEduclass.set("educlass_name", eduClassName);
            ccEduclass.set("teacher_course_id", teacherCourseId);
            ccEduclass.set("is_del", false);
            ccEduclasses.add(ccEduclass);

            classMap.put(set, classId);
        }

        //获取已存在教学班的ID
        Map<Long, Long> existEduclassMap = Maps.newHashMap();
        for (CcEduclassStudent educlassStudent : classStudents) {
            Long classId = educlassStudent.getLong("class_id");
            if (classId != null && !existEduclassMap.containsKey(classId)) {
                existEduclassMap.put(classId, classId);
            }
        }
        Set<Long> existEduClassIdSet = existEduclassMap.keySet();

        //保存教学班
        if (!ccEduclasses.isEmpty()) {
            if (!CcEduclass.dao.batchSave(ccEduclasses)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                result.put("isSuccess", false);
                return renderSUC(result, response, header);
            }
        }

        //进行数据保存
        for (Map.Entry<String, List<CcEduclassStudent>> entry : classStudentMap.entrySet()) {
            List<CcEduclassStudent> ccEduclassStudents = entry.getValue();
            for (CcEduclassStudent ccEduclassStudent : ccEduclassStudents) {
                ccEduclassStudent.set("class_id", classMap.get(entry.getKey()));
            }
            classStudents.addAll(ccEduclassStudents);
        }

        if (!CcEduclassStudent.dao.batchSave(classStudents)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.put("isSuccess", false);
            return renderSUC(result, response, header);
        }

        CcEduindicationStuScoreService ccEduindicationStuScoreService = SpringContextHolder.getBean(CcEduindicationStuScoreService.class);

        // 初始化新增教学班的总分和平均成绩
        if (!ccEduclasses.isEmpty()) {
            if (!ccEduindicationStuScoreService.initEduClassGrade(ccEduclasses)) {
                result.put("isSuccess", false);
                return renderSUC(result, response, header);
            }
        }

        // 更新已存在教学班的平均成绩
        if (!existEduClassIdSet.isEmpty()) {
            if (!ccEduindicationStuScoreService.calculate(Lists.newArrayList(existEduClassIdSet), Lists.<Long>newArrayList())) {
                result.put("isSuccess", false);
                return renderSUC(result, response, header);
            }
        }

        if (!isValidateImportStudent) {

            List<CcClass> ccClassList = new ArrayList<>();
            List<Office> offices = new ArrayList<>();
            List<OfficePath> officePaths = new ArrayList<>();
            List<Long> majorIds = new ArrayList<>();
            //key为行政班名称，value为行政班编号
            Map<String, Long> clazzMap = new HashMap<>();

            for (Map.Entry<String, List<CcStudent>> entry : studentListMap.entrySet()) {
                List<CcStudent> studentList = entry.getValue();
                String keys[] = entry.getKey().split("-");
                String majorName = keys[0];
                String className = keys[1];
                Long classId = idGenerate.getNextValue();
                Long majorId = majorMap.get(majorName);

                clazzMap.put(className, classId);
                if (!majorIds.contains(majorId)) {
                    majorIds.add(majorId);
                }

                CcClass ccClass = new CcClass();
                ccClass.set("id", classId);
                ccClass.set("create_date", date);
                ccClass.set("modify_date", date);
                ccClass.set("grade", Integer.valueOf(studentList.get(0).getStr("grade")));
                ccClass.set("is_del", false);
                ccClassList.add(ccClass);

                Office office = new Office();
                office.set("id", classId);
                office.set("create_date", date);
                office.set("modify_date", date);
                office.set("parentid", majorId);
                office.set("code", String.valueOf(classId));
                office.set("name", className);
                office.set("type", Office.TYPE_CLAZZ);
                office.set("is_system", Constant.NOTSYSTEM);
                office.set(Office.IS_DEL_LABEL, Office.DEL_NO);
                offices.add(office);

                for (CcStudent ccStudent : studentList) {
                    ccStudent.set("class_id", classId);
                    ccStudentList.add(ccStudent);
                }
            }

            //专业的路径表
            List<OfficePath> officePathList = majorIds.isEmpty() ? new ArrayList<OfficePath>() : OfficePath.dao.findByColumnIn("id", majorIds.toArray(new Long[majorIds.size()]));
            Map<Long, String> officePathMap = new HashMap<>();
            for (OfficePath officePath : officePathList) {
                officePathMap.put(officePath.getLong("id"), officePath.getStr("office_ids"));
            }

            for (String key : studentListMap.keySet()) {
                String keys[] = key.split("-");
                String majorName = keys[0];
                String className = keys[1];
                Long clazzId = clazzMap.get(className);

                OfficePath officePath = new OfficePath();
                officePath.set("id", clazzId);
                officePath.set("create_date", date);
                officePath.set("modify_date", date);
                officePath.set("office_ids", String.format("%s,%s,", officePathMap.get(majorMap.get(majorName)), clazzId));
                officePaths.add(officePath);
            }

            if (!ccClassList.isEmpty()) {
                if (!CcClass.dao.batchSave(ccClassList)) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    result.put("isSuccess", false);
                    return renderSUC(result, response, header);
                }
            }

            if (!offices.isEmpty()) {
                if (!Office.dao.batchSave(offices)) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    result.put("isSuccess", false);
                    return renderSUC(result, response, header);
                }
            }

            if (!officePaths.isEmpty()) {
                if (!OfficePath.dao.batchSave(officePaths)) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    result.put("isSuccess", false);
                    return renderSUC(result, response, header);
                }
            }

            if (!ccStudentList.isEmpty()) {
                if (!CcStudent.dao.batchSave(ccStudentList)) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    result.put("isSuccess", false);
                    return renderSUC(result, response, header);
                }
            }
        }

        result.put("isSuccess", true);
        return renderSUC(result, response, header);
    }

}
