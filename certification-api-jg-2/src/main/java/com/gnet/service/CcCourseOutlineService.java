package com.gnet.service;

import java.math.BigDecimal;
import java.util.*;

import com.gnet.Constant;
import com.gnet.model.admin.*;
import com.gnet.response.ServiceResponse;
import com.gnet.utils.ConvertUtils;
import com.gnet.utils.PriceUtils;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.SpringContextHolder;

/**
 * 课程教学大纲
 * 
 * @author SY
 * 
 * @date 2016-7-28 14:06:102
 */
@Component("ccCourseOutlineService")
public class CcCourseOutlineService {

	/**
	 * 通过课程ids删除课程大纲以及增加操作历史
	 * @param courseIds
	 * @param user 
	 * @param date
	 * @return
	 */
	public boolean deleteByCourseIds(Long[] courseIds, User user, Date date) {
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		List<CcCourseOutline> ccCourseOutlines = CcCourseOutline.dao.findFilteredByColumnIn("course_id", courseIds);
		Long[] outlineIds = new Long[ccCourseOutlines.size()];
		// 增加课程大纲删除历史
		List<CcCourseOutlineHistory> ccCourseOutlineHistories = new ArrayList<>();
		for(int i = 0; i < ccCourseOutlines.size(); i++) {
			CcCourseOutline temp = ccCourseOutlines.get(i);
			outlineIds[i] = temp.getLong("id");
			CcCourseOutlineHistory tempSave = new CcCourseOutlineHistory();
			tempSave.set("id", idGenerate.getNextValue());
			tempSave.set("create_date", date);
			tempSave.set("modify_date", date);
			tempSave.set("outline_id", temp.getLong("id"));
			tempSave.set("trigger_id", user.getLong("id"));
			tempSave.set("event", "删除课程大纲！");
			tempSave.set("event_type", CcCourseOutlineHistory.TYPE_DELETE);
			tempSave.set("is_del", Boolean.FALSE);
			ccCourseOutlineHistories.add(tempSave);
		}
		if(!CcCourseOutlineHistory.dao.batchSave(ccCourseOutlineHistories)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}

		if(outlineIds != null && outlineIds.length > 0){
			// 删除课程大纲
			if(!CcCourseOutline.dao.deleteAllByColumn("course_id", courseIds, date)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}

			if(!deleteCourseOutline(courseIds, date)){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}

		}

		return true;
	}


	/**
	 * 删除大纲
	 * @param outlineIds
	 * @param date
	 * @return
	 */
	public Boolean deleteCourseOutline(Long[] outlineIds, Date date){

		if(!CcCourseOutlineCourseInfo.dao.deleteAllByColumn(CcCourseOutline.COURSE_OUTLINE_ID, outlineIds, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		if(!CcCourseOutlineHeader.dao.deleteAllByColumn(CcCourseOutline.COURSE_OUTLINE_ID, outlineIds, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		if(!CcCourseOutlineTableName.dao.deleteAllByColumn(CcCourseOutline.COURSE_OUTLINE_ID, outlineIds, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		if(!CcCourseOutlineIndications.dao.deleteAllByColumn(CcCourseOutline.COURSE_OUTLINE_ID, outlineIds, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		if(!CcCourseOutlineModule.dao.deleteAllByColumn(CcCourseOutline.COURSE_OUTLINE_ID, outlineIds, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		if(!CcCourseOutlineSecondaryContent.dao.deleteAllByColumn(CcCourseOutline.COURSE_OUTLINE_ID, outlineIds, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		if(!CcCourseOutlineTableDetail.dao.deleteAllByColumn(CcCourseOutline.COURSE_OUTLINE_ID, outlineIds, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		if(!CcCourseOutlineTeachingContent.dao.deleteAllByColumn(CcCourseOutline.COURSE_OUTLINE_ID, outlineIds, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		if(!CcCourseOutlineHours.dao.deleteAllByColumn(CcCourseOutline.COURSE_OUTLINE_ID, outlineIds, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		return true;
	}


	/**
	 * 保存大纲与课程相关的基础信息
	 * @param courseInfoMap
	 * @param courseOutlineId
	 * @return
	 */
   public ServiceResponse saveCourseInfo(List<LinkedHashMap> courseInfoMap, Long courseOutlineId){
	   IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
	   Date date = new Date();
	   List<CcCourseOutlineCourseInfo> courseInfoList = Lists.newArrayList();

	   for(int i=1; i<=courseInfoMap.size(); i++){
		   Map<String, Object> map = courseInfoMap.get(i-1);
		   CcCourseOutlineCourseInfo ccCourseOutlineCourseInfo = new CcCourseOutlineCourseInfo();
		   String courseInfoName = ConvertUtils.convert(map.get("name"), String.class);
		   String databaseField = ConvertUtils.convert(map.get("databaseField"), String.class);
		   String content = ConvertUtils.convert(map.get("content"), String.class);

		   if (StrKit.isBlank(courseInfoName)) {
			   return ServiceResponse.error(String.format("第%s个课程大纲中与课程相关的课程基本信息名称不能为空", i));
		   }
		   if(courseInfoName.length() > 50){
			   return ServiceResponse.error(String.format("第%s个课程大纲中与课程相关的课程基本信息名称长度不能大于50",i));
		   }

		   ccCourseOutlineCourseInfo.set("id", idGenerate.getNextValue());
		   ccCourseOutlineCourseInfo.set("create_date", date);
		   ccCourseOutlineCourseInfo.set("modify_date", date);
		   ccCourseOutlineCourseInfo.set(CcCourseOutline.COURSE_OUTLINE_ID, courseOutlineId);
		   ccCourseOutlineCourseInfo.set("name", courseInfoName);
		   ccCourseOutlineCourseInfo.set("database_field", databaseField);
		   ccCourseOutlineCourseInfo.set("content", content);
		   ccCourseOutlineCourseInfo.set("is_del", Boolean.FALSE);

		   courseInfoList.add(ccCourseOutlineCourseInfo);
	   }

	   if (!CcCourseOutlineCourseInfo.dao.batchSave(courseInfoList)) {
		   TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		   return ServiceResponse.error("保存课程大纲失败");
	   }
	   return  ServiceResponse.succ(true);
   }


	/**
	 * 是否能提交或强制提交大纲
	 * @param courseOutline
	 * @param course
	 * @return
	 */
	public ServiceResponse canChangeStatus(CcCourseOutline courseOutline, CcCourse course) {
		Boolean isNeedValidateAllHours = CcCourseOutlineType.NAME.equals(courseOutline.getStr("outlineTypeName"));
		BigDecimal allHours = course.getBigDecimal("all_hours");
/*		if(!courseOutline.getBoolean("is_support_course_indication")){
			return ServiceResponse.error("该大纲没有全部支持该门课已关联的课程目标！");
		}*/

        List<CcCourseOutlineHours> ccCourseOutlineHoursList = CcCourseOutlineHours.dao.findFilteredByColumn(CcCourseOutline.COURSE_OUTLINE_ID, courseOutline.getLong("id"));

		if(allHours == null || PriceUtils.isZero(allHours)){
			return ServiceResponse.succ(true);
		}else if(ccCourseOutlineHoursList.isEmpty()){
			return ServiceResponse.error("该大纲的总学时(或总周数)与课程的不一致！");
		}

        CcCourseOutlineModuleService ccCourseOutlineModuleService = SpringContextHolder.getBean(CcCourseOutlineModuleService.class);
        for(CcCourseOutlineHours courseOutlineHours : ccCourseOutlineHoursList){
			Integer index = courseOutlineHours.getInt("indexes");
			Integer number = courseOutlineHours.getInt("number");
			ServiceResponse allResponse = ccCourseOutlineModuleService.validateAllHours(allHours, courseOutlineHours.getBigDecimal("all_hours"), index, number, "学时(或周数)");

			if(CcCourse.TYPE_THEORY.equals(course.getInt("type"))){
				ServiceResponse theoryResponse = ccCourseOutlineModuleService.validateHours(course.getBigDecimal("theory_hours"), courseOutlineHours.getBigDecimal("theory_hours"), index, number, Constant.THEORY_HOURS);
				ServiceResponse experimentResponse = ccCourseOutlineModuleService.validateHours(course.getBigDecimal("experiment_hours"), courseOutlineHours.getBigDecimal("experiment_hours"), index, number, Constant.EXPERIMENT_HOURS);
                ServiceResponse practiceResponse = ccCourseOutlineModuleService.validateHours(course.getBigDecimal("practice_hours"), courseOutlineHours.getBigDecimal("practice_hours"), index, number, Constant.PRACTICE_HOURS);
                ServiceResponse operateComputerResponse = ccCourseOutlineModuleService.validateHours(course.getBigDecimal("operate_computer_hours"), courseOutlineHours.getBigDecimal("operate_computer_hours"), index, number, Constant.OPERATE_COMPUTER_HOURS);
				ServiceResponse dicussResponse = ccCourseOutlineModuleService.validateHours(course.getBigDecimal("dicuss_hours"), courseOutlineHours.getBigDecimal("dicuss_hours"), index, number, Constant.DICUSS_HOURS);
				ServiceResponse exercisesResponse = ccCourseOutlineModuleService.validateHours(course.getBigDecimal("exercises_hours"), courseOutlineHours.getBigDecimal("exercises_hours"), index, number, Constant.EXERCISES_HOURS);

                if(!theoryResponse.isSucc()){
                	return ServiceResponse.error(theoryResponse.getContent());
				}
				if(!experimentResponse.isSucc()){
                	return ServiceResponse.error(experimentResponse.getContent());
				}
				if(!practiceResponse.isSucc()){
					return ServiceResponse.error(practiceResponse.getContent());
				}
				if(!operateComputerResponse.isSucc()){
					return ServiceResponse.error(operateComputerResponse.getContent());
				}
				if(!dicussResponse.isSucc()){
					return ServiceResponse.error(dicussResponse.getContent());
				}
				if(!exercisesResponse.isSucc()){
					return  ServiceResponse.error(exercisesResponse.getContent());
				}
				if(isNeedValidateAllHours && !allResponse.isSucc()){
					return ServiceResponse.error(allResponse.getContent());
				}
			}else{
				if(!allResponse.isSucc()){
					return ServiceResponse.error(allResponse.getContent());
				}
			}
		}

		return ServiceResponse.succ(true);
	}
}
