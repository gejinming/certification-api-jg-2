package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.*;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.service.CcMajorTeacherService;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 指定课程大纲的执笔人和审核人
 * 
 * @author xzl
 * @Date 2017-8-23 15:15:17
 */
@Service("EM00711")
public class EM00711 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();

		Long courseId = paramsLongFilter(params.get("courseId"));
        Long authorId = paramsLongFilter(params.get("authorId"));
        Long auditorId = paramsLongFilter(params.get("auditorId"));
		List<Long> courseOutlineTypeIds = paramsJSONArrayFilter(params.get("courseOutlineTypeIds"), Long.class);
		User user  = UserCacheKit.getUser(request.getHeader().getToken());

		if(courseId == null){
             return renderFAIL("0250", response, header);
		}

        if(authorId == null){
        	return  renderFAIL("0894", response, header);
		}

		if(auditorId == null){
        	return  renderFAIL("0895", response, header);
		}

		if(courseOutlineTypeIds.isEmpty()){
			return  renderFAIL("0892", response, header);
		}

		CcCourse course = CcCourse.dao.findFilteredById(courseId);
		if(course == null){
			return renderFAIL("0251", response, header);
		}


		// 结果返回
		Map<String, Object> result = Maps.newHashMap();

		Date date = new Date();
		Long[] courseOutlineTypeIdArray = courseOutlineTypeIds.toArray(new Long[courseOutlineTypeIds.size()]);
		//先删除旧的课程大纲
		List<CcCourseOutline> ccCourseOutlineList = CcCourseOutline.dao.findByCourseOutlineTypeIds(courseId, courseOutlineTypeIdArray);
        if(!ccCourseOutlineList.isEmpty()){
            Long[] courseOutlineIds = new Long[ccCourseOutlineList.size()];
            for(int i=0; i<ccCourseOutlineList.size(); i++){
				courseOutlineIds[i] = ccCourseOutlineList.get(i).getLong("id");
			}

			if(!CcCourseOutline.dao.deleteAll(courseOutlineIds, date)){
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}

		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		List<CcCourseOutline> ccCourseOutlines = Lists.newArrayList();
		List<CcCourseOutlineHistory> ccCourseOutlineHistories = Lists.newArrayList();
		for(Long id : courseOutlineTypeIds){
			CcCourseOutline ccCourseOutline = new CcCourseOutline();
			ccCourseOutline.set("id", idGenerate.getNextValue());
            ccCourseOutline.set("create_date", date);
			ccCourseOutline.set("modify_date", date);
			ccCourseOutline.set("course_id", courseId);
			ccCourseOutline.set("name",String.format("%s%s",course.getStr("name"), "课程大纲"));
			ccCourseOutline.set("is_support_course_indication", false);
			ccCourseOutline.set("author_id", authorId);
			ccCourseOutline.set("auditor_id", auditorId);
			ccCourseOutline.set("status", CcCourseOutline.STATUS_NOT_START);
			ccCourseOutline.set("outline_type_id",id);
			ccCourseOutline.set("is_del", false);
			ccCourseOutlines.add(ccCourseOutline);

			CcCourseOutlineHistory ccCourseOutlineHistory = new CcCourseOutlineHistory();
			ccCourseOutlineHistory.set("id", idGenerate.getNextValue());
			ccCourseOutlineHistory.set("create_date", date);
			ccCourseOutlineHistory.set("modify_date", date);
			ccCourseOutlineHistory.set("outline_id", id);
			ccCourseOutlineHistory.set("trigger_id", user.getLong("id"));
			ccCourseOutlineHistory.set("event", String.format("负责人(编号：%s)%s指派了【%s(编号：%s)】的课程大纲模板的执笔人和审核人", user.getLong("id"), user.getStr("name"), course.getStr("name"), course.getLong("id")));
			ccCourseOutlineHistory.set("event_type", CcCourseOutlineHistory.TYPE_EDIT);
			ccCourseOutlineHistory.set("is_del", Boolean.FALSE);
			ccCourseOutlineHistories.add(ccCourseOutlineHistory);
		}

		if(!CcCourseOutlineHistory.dao.batchSave(ccCourseOutlineHistories)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

        if(!CcCourseOutline.dao.batchSave(ccCourseOutlines)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

		CcMajorTeacherService ccMajorTeacherService = SpringContextHolder.getBean(CcMajorTeacherService.class);
		if(!ccMajorTeacherService.addMajorTeacher(course.getLong("plan_id"), authorId, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		if(!ccMajorTeacherService.addMajorTeacher(course.getLong("plan_id"), auditorId, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}

        result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}
	
}
