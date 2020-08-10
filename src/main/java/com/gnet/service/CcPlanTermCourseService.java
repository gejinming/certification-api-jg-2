package com.gnet.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.gnet.model.admin.CcPlanTermCourse;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.SpringContextHolder;

/**
 * 培养计划课程学期详情表
 * 
 * @author SY
 * 
 * @date 2016年7月18日18:31:04
 */
@Component("ccPlanTermCourseService")
public class CcPlanTermCourseService {

	/**
	 * 保存培养计划课程学期详情表
	 * @param courseId
	 * 			课程编号
	 * @param planTermExamIds
	 * 			考试学期id列表
	 * @param planTermClassIds
	 * 			上课学期id列表(不得为空)
	 * @param date
	 * 			时间（可以为空）
	 * @return
	 */
	public Boolean addCcPlanTermCourse(Long courseId, List<Long> planTermExamIds, List<Long> planTermClassIds, Date date) {
		
		Date newdate = (date == null) ? new Date() : date;
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		
		List<CcPlanTermCourse> ccPlanTermCourseSaveList = new ArrayList<>();
		// 建立培养计划课程学期详情表
		if(!planTermExamIds.isEmpty()) {
			for(Long id : planTermExamIds) {
				CcPlanTermCourse temp = new CcPlanTermCourse();
				temp.set("id", idGenerate.getNextValue());
				temp.set("create_date", newdate);
				temp.set("modify_date", newdate);
				temp.set("plan_term_id", id);
				temp.set("course_id", courseId);
				temp.set("type", CcPlanTermCourse.TYPE_EXAM);
				temp.set("is_del", Boolean.FALSE);
				
				ccPlanTermCourseSaveList.add(temp);
			}
		}
		
		if (!planTermClassIds.isEmpty()) {
			for(Long id : planTermClassIds) {
				CcPlanTermCourse temp = new CcPlanTermCourse();
				temp.set("id", idGenerate.getNextValue());
				temp.set("create_date", newdate);
				temp.set("modify_date", newdate);
				temp.set("plan_term_id", id);
				temp.set("course_id", courseId);
				temp.set("type", CcPlanTermCourse.TYPE_CLASS);
				temp.set("is_del", Boolean.FALSE);
				
				ccPlanTermCourseSaveList.add(temp);
			}
		}
		
		return CcPlanTermCourse.dao.batchSave(ccPlanTermCourseSaveList);
	}

}
