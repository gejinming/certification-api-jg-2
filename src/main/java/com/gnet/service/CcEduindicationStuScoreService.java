package com.gnet.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gnet.utils.DateUtil;
import com.jfinal.log.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.model.admin.CcCourseGradecompose;
import com.gnet.model.admin.CcCourseGradecomposeIndication;
import com.gnet.model.admin.CcEduclass;
import com.gnet.model.admin.CcEduindicationStuScore;
import com.gnet.model.admin.CcScoreStuIndigrade;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.ConvertUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: yuhailun
 * @date: 2017/11/27
 * @description: 对 ccEduindicationStuScoreService 表中的数据进行维护
 **/
@Component("ccEduindicationStuScoreService")
public class CcEduindicationStuScoreService {
    private static final Logger logger = Logger.getLogger(CcEduindicationStuScoreService.class);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * @description: 通过 开课成绩组成课程目标ID集合 删除数据
     * @param gradecomposeIndicationIds
     * @return
     */
    public boolean deleteByGradecomposeIndicationIds(Long[] gradecomposeIndicationIds) {
        return CcEduindicationStuScore.dao.deleteAllByColumn("gradecompose_indication_id", gradecomposeIndicationIds, new Date());
    }

    /**
     * @description: 通过 教学班ID数组 和 开课成绩组成ID数组 删除数据
     * @param eduClassIds
     * @param courseGradecomposeIds
     * @return
     */
    private boolean delete(List<Long> eduClassIds, List<Long> courseGradecomposeIds) {

        List<CcEduindicationStuScore> eduindicationStuScoreList = CcEduindicationStuScore.dao.findByEduclassIdsAndCourseGradecomposeIds(eduClassIds, courseGradecomposeIds);
        if (!eduindicationStuScoreList.isEmpty()) {
            List<Long> idList = ConvertUtils.modelListToIdList(eduindicationStuScoreList, null);
            if (!CcEduindicationStuScore.dao.deleteAll(idList.toArray(new Long[idList.size()]), new Date())) {
                return false;
            }
        }

        return true;
    }

    /**
     * @description: 计算 某些教学班 在 某个开课成绩组成下的 总分和平均分
     * @param eduClassIds 教学班ID数组
     * @param courseGradecomposeId 开课成绩组成ID
     * @return
     */
    public boolean calculate(Long[] eduClassIds, Long courseGradecomposeId) {
        List<Long> courseGradecomposeIds = Lists.newArrayList();
        if (courseGradecomposeId != null) {
            courseGradecomposeIds.add(courseGradecomposeId);
        }
        return calculate(Lists.newArrayList(eduClassIds), courseGradecomposeIds);
    }
    
    /**
     * @description: 计算 某些教学班 在 某个开课成绩组成下的 总分和平均分(剔除部分学生之后)
     * @param eduClassIds 教学班ID数组
     * @param courseGradecomposeId 开课成绩组成ID
     * @return
     */
    public boolean calculateExcept(Long[] eduClassIds, Long courseGradecomposeId) {
    	List<Long> courseGradecomposeIds = Lists.newArrayList();
    	if (courseGradecomposeId != null) {
    		courseGradecomposeIds.add(courseGradecomposeId);
    	}
    	return calculateExcept(Lists.newArrayList(eduClassIds), courseGradecomposeIds);
    }

    /**
     * @description: 计算 某些教学班在 某些开课成绩组成下的 总分和平均分
     * @param eduClassIds  教学班ID数组
     * @param courseGradecomposeIds 开课成绩组成ID数组
     * @return
     */
    public boolean calculate(List<Long> eduClassIds, List<Long> courseGradecomposeIds) {
        Date date1 = new Date();
        if (eduClassIds == null || eduClassIds.isEmpty()) {
            return false;
        }

        if (courseGradecomposeIds == null || courseGradecomposeIds.isEmpty()) {
            List<CcCourseGradecompose> courseGradecomposes = CcCourseGradecompose.dao.findByEduClassIds(eduClassIds);
            if (courseGradecomposes.isEmpty()) {
                return true;
            }
            courseGradecomposeIds = ConvertUtils.modelListToIdList(courseGradecomposes, "id");
        }

        // 查询以前的数据
        List<CcEduindicationStuScore> eduindicationStuScoreOldList = CcEduindicationStuScore.dao.findByEduclassIdsAndCourseGradecomposeIds(eduClassIds, courseGradecomposeIds);
        // Map<“educlass_id,gradecompose_indication_id”, CcEduindicationStuScore>
        Map<String, CcEduindicationStuScore> eduindicationStuScoreListMap = new HashMap<>();
        for(CcEduindicationStuScore temp : eduindicationStuScoreOldList) {
        	String key = temp.getLong("educlass_id")+","+temp.getLong("gradecompose_indication_id");
        	eduindicationStuScoreListMap.put(key, temp);
        }
        
        // 根据条件 查询出教学班在开课成绩组成课程目标下的平均分和总分
        List<CcScoreStuIndigrade> scoreStuIndigradeList = CcScoreStuIndigrade.dao.findClassGradeByEduclassIdsAndCourseGradecomposeIds(Lists.newArrayList(eduClassIds), Lists.newArrayList(courseGradecomposeIds));

        List<CcEduindicationStuScore> eduindicationStuScoreList = Lists.newArrayList();
        IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
        Date date = new Date();
        for (CcScoreStuIndigrade scoreStuIndigrade : scoreStuIndigradeList) {
        	// 是否存在老的数据
        	Long educlassId = scoreStuIndigrade.getLong("eduClassId");
            Long gradecomposeIndicationId = scoreStuIndigrade.getLong("gradeComposeIndicationId");
            String key = educlassId + "," + gradecomposeIndicationId;
            CcEduindicationStuScore eduindicationStuScoreOld = eduindicationStuScoreListMap.get(key);
              
            CcEduindicationStuScore eduindicationStuScore = new CcEduindicationStuScore();
            eduindicationStuScore.set("id", idGenerate.getNextValue());
            eduindicationStuScore.set("create_date", date);
            eduindicationStuScore.set("modify_date", date);
            eduindicationStuScore.set("gradecompose_indication_id", scoreStuIndigrade.getLong("gradeComposeIndicationId"));
            eduindicationStuScore.set("educlass_id", scoreStuIndigrade.getLong("eduClassId"));
            if(eduindicationStuScoreOld != null) {
            	// 老的数据放进去
            	eduindicationStuScore.set("total_score", eduindicationStuScoreOld.getBigDecimal("total_score"));
    			eduindicationStuScore.set("except_total_score", eduindicationStuScoreOld.getBigDecimal("except_total_score"));
                eduindicationStuScore.set("avg_score", eduindicationStuScoreOld.getBigDecimal("avg_score"));
                eduindicationStuScore.set("except_avg_score", eduindicationStuScoreOld.getBigDecimal("except_avg_score"));            	
            }
            // 新的数据更新
            eduindicationStuScore.set("total_score", scoreStuIndigrade.getBigDecimal("totalScore"));
            eduindicationStuScore.set("avg_score", scoreStuIndigrade.getBigDecimal("avgScore"));
            eduindicationStuScoreList.add(eduindicationStuScore);
        }

        if (!delete(eduClassIds, courseGradecomposeIds)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }

        if (!eduindicationStuScoreList.isEmpty()) {
            if (!CcEduindicationStuScore.dao.batchSave(eduindicationStuScoreList)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        }
        Date date2 = new Date();
        logger.info("计算 某些教学班在 某些开课成绩组成下的 总分和平均分calculate共用时"+ DateUtil.timeSub(sdf.format(date1),sdf.format(date2)));
       /* Delete by SY 2019年11月5日02:29:30， 因为发现后面还会计算一下剔除部分：calculateExcept(eduClassIds, courseGradecomposeIds)
        * 而且发行这个方法里面还会计算一遍，那就不重复了
        // Edit by SY 因为个人修改之后，立马需要影响所有报表，所以do it
        CcIndicationService ccIndicationService = SpringContextHolder.getBean(CcIndicationService.class);
        if (!ccIndicationService.statisticsAllForJGByDiffrentEduClassId(eduClassIds.toArray(new Long[eduClassIds.size()]))) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }*/
        
        // 同时也计算一下剔除掉部分学生的达成度计算
    	return calculateExcept(eduClassIds, courseGradecomposeIds);
    }
    
    /**
     * @description: 计算 某些教学班在 某些开课成绩组成下的 总分和平均分(剔除部分学生之后)
     * @param eduClassIds  教学班ID数组
     * @param courseGradecomposeIds 开课成绩组成ID数组
     * @return
     */
    public boolean calculateExcept(List<Long> eduClassIds, List<Long> courseGradecomposeIds) {
        Date date1 = new Date();
        if (eduClassIds == null || eduClassIds.isEmpty()) {
    		return false;
    	}
    	
    	if (courseGradecomposeIds == null || courseGradecomposeIds.isEmpty()) {
    		List<CcCourseGradecompose> courseGradecomposes = CcCourseGradecompose.dao.findByEduClassIds(eduClassIds);
    		if (courseGradecomposes.isEmpty()) {
    			return true;
    		}
    		courseGradecomposeIds = ConvertUtils.modelListToIdList(courseGradecomposes, "id");
    	}
    	
    	// 查询以前的数据
        List<CcEduindicationStuScore> eduindicationStuScoreOldList = CcEduindicationStuScore.dao.findByEduclassIdsAndCourseGradecomposeIds(eduClassIds, courseGradecomposeIds);
        // Map<“educlass_id,gradecompose_indication_id”, CcEduindicationStuScore>
        Map<String, CcEduindicationStuScore> eduindicationStuScoreListMap = new HashMap<>();
        for(CcEduindicationStuScore temp : eduindicationStuScoreOldList) {
        	String key = temp.getLong("educlass_id")+","+temp.getLong("gradecompose_indication_id");
        	eduindicationStuScoreListMap.put(key, temp);
        }
        
    	// 根据条件 查询出教学班在开课成绩组成课程目标下的平均分和总分
    	List<CcScoreStuIndigrade> scoreStuIndigradeList = CcScoreStuIndigrade.dao.findClassGradeByEduclassIdsAndCourseGradecomposeIds(Lists.newArrayList(eduClassIds), Lists.newArrayList(courseGradecomposeIds), Boolean.TRUE);
    	
    	List<CcEduindicationStuScore> eduindicationStuScoreList = Lists.newArrayList();
    	IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
    	Date date = new Date();
    	for (CcScoreStuIndigrade scoreStuIndigrade : scoreStuIndigradeList) {
    		// 是否存在老的数据
        	Long educlassId = scoreStuIndigrade.getLong("eduClassId");
            Long gradeComposeIndicationId = scoreStuIndigrade.getLong("gradeComposeIndicationId");
            String key = educlassId + "," + gradeComposeIndicationId;
            CcEduindicationStuScore eduindicationStuScoreOld = eduindicationStuScoreListMap.get(key);
            
    		CcEduindicationStuScore eduindicationStuScore = new CcEduindicationStuScore();
    		eduindicationStuScore.set("id", idGenerate.getNextValue());
    		eduindicationStuScore.set("create_date", date);
    		eduindicationStuScore.set("modify_date", date);
    		eduindicationStuScore.set("gradecompose_indication_id", scoreStuIndigrade.getLong("gradeComposeIndicationId"));
    		eduindicationStuScore.set("educlass_id", scoreStuIndigrade.getLong("eduClassId"));
    		if(eduindicationStuScoreOld != null) {
            	// 老的数据放进去
    			eduindicationStuScore.set("total_score", eduindicationStuScoreOld.getBigDecimal("total_score"));
    			eduindicationStuScore.set("except_total_score", eduindicationStuScoreOld.getBigDecimal("except_total_score"));
                eduindicationStuScore.set("avg_score", eduindicationStuScoreOld.getBigDecimal("avg_score"));
                eduindicationStuScore.set("except_avg_score", eduindicationStuScoreOld.getBigDecimal("except_avg_score"));            	
            }
            // 新的数据更新
    		eduindicationStuScore.set("except_total_score", scoreStuIndigrade.getBigDecimal("totalScore"));
    		eduindicationStuScore.set("except_avg_score", scoreStuIndigrade.getBigDecimal("avgScore"));
    		eduindicationStuScoreList.add(eduindicationStuScore);
    	}
    	
    	if (!delete(eduClassIds, courseGradecomposeIds)) {
    		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    		return false;
    	}
    	
    	if (!eduindicationStuScoreList.isEmpty()) {
    		if (!CcEduindicationStuScore.dao.batchSave(eduindicationStuScoreList)) {
    			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    			return false;
    		}
    	}
    	
    	// Edit by SY 因为个人修改之后，立马需要影响所有报表，所以do it
    	CcIndicationService ccIndicationService = SpringContextHolder.getBean(CcIndicationService.class);
    	if (!ccIndicationService.statisticsAllForJGByDiffrentEduClassId(eduClassIds.toArray(new Long[eduClassIds.size()]))) {
    		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    		return false;
    	}
        Date date2 = new Date();
        logger.info("计算 某些教学班在 某些开课成绩组成下的 总分和平均分(剔除部分学生之后) calculateExcept共用时"+ DateUtil.timeSub(sdf.format(date1),sdf.format(date2)));
    	return true;
    }


    /**
     * 初始化教学班的平均成绩和总分
     * @param ccEduClassList
     * @return
     */
    public boolean initEduClassGrade(List<CcEduclass> ccEduClassList) {

        if (ccEduClassList.isEmpty()) {
            return false;
        }

        Map<Long, Long> teacherCourseIdMap = Maps.newHashMap();
        for (CcEduclass educlass : ccEduClassList) {
            Long teacherCourseId = educlass.getLong("teacher_course_id");
            if (teacherCourseId != null) {
                teacherCourseIdMap.put(teacherCourseId, teacherCourseId);
            }
        }

        Set<Long> teacherCourseIds = teacherCourseIdMap.keySet();

        List<CcCourseGradecomposeIndication> courseGradecomposeIndications = CcCourseGradecomposeIndication.dao.findByTeacherCourseIds(Lists.newArrayList(teacherCourseIds));

        if (courseGradecomposeIndications.isEmpty()) {
            return true;
        }

        List<CcEduindicationStuScore> ccEduindicationStuScoreList = Lists.newArrayList();
        IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
        Date date = new Date();

        for (CcEduclass educlass : ccEduClassList) {
            Long teacherCourseId = educlass.getLong("teacher_course_id");
            Long eduClassId = educlass.getLong("id");
            if (eduClassId != null && teacherCourseId != null) {
                for (CcCourseGradecomposeIndication courseGradecomposeIndication : courseGradecomposeIndications) {
                    if (teacherCourseId.equals(courseGradecomposeIndication.getLong("teacherCourseId")) ) {
                        CcEduindicationStuScore ccEduindicationStuScore = new CcEduindicationStuScore();
                        ccEduindicationStuScore.set("id", idGenerate.getNextValue());
                        ccEduindicationStuScore.set("create_date", date);
                        ccEduindicationStuScore.set("modify_date", date);
                        ccEduindicationStuScore.set("gradecompose_indication_id", courseGradecomposeIndication.getLong("id"));
                        ccEduindicationStuScore.set("educlass_id", eduClassId);
                        ccEduindicationStuScoreList.add(ccEduindicationStuScore);
                    }
                }
            }
        }

        if (ccEduindicationStuScoreList.isEmpty() || !CcEduindicationStuScore.dao.batchSave(ccEduindicationStuScoreList)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }

        return true;
    }


    /**
     * 初始化教学班的平均成绩和总分
     * @param eduClassIds
     * @param teacherCourseId  eduClassIds对应的教学班 属于 teahcerCourseId对应的开课课程下
     * @return
     */
    public boolean initEduClassGrade(List<Long> eduClassIds, Long teacherCourseId) {

        if (eduClassIds.isEmpty()) {
            return false;
        }

        if (teacherCourseId == null) {
            return false;
        }

        List<CcCourseGradecomposeIndication> ccCourseGradecomposeIndicationList = CcCourseGradecomposeIndication.dao.findByTeacherCourseId(teacherCourseId);

        if (ccCourseGradecomposeIndicationList.isEmpty()) {
            return true;
        }

        List<CcEduindicationStuScore> ccEduindicationStuScoreList = Lists.newArrayList();
        IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
        Date date = new Date();

        for (Long eduClassId : eduClassIds) {
            for (CcCourseGradecomposeIndication courseGradecomposeIndication : ccCourseGradecomposeIndicationList) {
                CcEduindicationStuScore ccEduindicationStuScore = new CcEduindicationStuScore();
                ccEduindicationStuScore.set("id", idGenerate.getNextValue());
                ccEduindicationStuScore.set("create_date", date);
                ccEduindicationStuScore.set("modify_date", date);
                ccEduindicationStuScore.set("gradecompose_indication_id", courseGradecomposeIndication.getLong("id"));
                ccEduindicationStuScore.set("educlass_id", eduClassId);
                ccEduindicationStuScoreList.add(ccEduindicationStuScore);
            }
        }

        if (ccEduindicationStuScoreList.isEmpty() || !CcEduindicationStuScore.dao.batchSave(ccEduindicationStuScoreList)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }

        return true;
    }


    /**
     * 初始化改开课课程下的教学班在改成绩组成课程目标下的平均分和总分
     * @param teacherCourseId
     * @param courseGradecomposeIndicationId
     * @return
     */
    public boolean initEduClassGrade(Long teacherCourseId, Long courseGradecomposeIndicationId) {

        if (courseGradecomposeIndicationId == null) {
            return false;
        }

        if (teacherCourseId == null) {
            return false;
        }

        List<CcEduclass> educlassList = CcEduclass.dao.findAllByCourseId(teacherCourseId);

        if (educlassList.isEmpty()) {
            return true;
        }

        List<CcEduindicationStuScore> ccEduindicationStuScoreList = Lists.newArrayList();
        IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
        Date date = new Date();

        for (CcEduclass eduClass : educlassList) {
            CcEduindicationStuScore ccEduindicationStuScore = new CcEduindicationStuScore();
            ccEduindicationStuScore.set("id", idGenerate.getNextValue());
            ccEduindicationStuScore.set("create_date", date);
            ccEduindicationStuScore.set("modify_date", date);
            ccEduindicationStuScore.set("gradecompose_indication_id", courseGradecomposeIndicationId);
            ccEduindicationStuScore.set("educlass_id", eduClass.getLong("id"));
            ccEduindicationStuScoreList.add(ccEduindicationStuScore);
        }

        if (ccEduindicationStuScoreList.isEmpty() || !CcEduindicationStuScore.dao.batchSave(ccEduindicationStuScoreList)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }

        return true;
    }


}
