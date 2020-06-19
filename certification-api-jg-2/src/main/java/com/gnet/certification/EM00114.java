package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.service.UserService;
import com.gnet.utils.SpringContextHolder;

/**
 * 删除教师某条信息
 * 
 * @author SY
 * @Date 2016年6月23日21:58:01
 */
@Service("EM00114")
public class EM00114 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据
		Date date = new Date();
		
		List<Long> ids = paramsJSONArrayFilter(params.get("ids"), Long.class);
		// majorId不能为空信息的过滤
		if (ids.isEmpty()) {
			return renderFAIL("0160", response, header);
		}
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		result.put("isSuccess", false);
		
		// 判断教师是否属于大纲执笔人或审核人
		List<CcCourseOutline> ccCourseOutlines = CcCourseOutline.dao.findFilteredByColumnIn("author_id", idsArray);
		if(!ccCourseOutlines.isEmpty()){
			return renderFAIL("0271", response, header);
		}

		ccCourseOutlines = CcCourseOutline.dao.findFilteredByColumnIn("auditor_id", idsArray);
		if(!ccCourseOutlines.isEmpty()){
			return renderFAIL("0272", response, header);
		}

		// 判断是否还有教师开课课程表在使用
		List<CcTeacherCourse> ccTeacherCourses = CcTeacherCourse.dao.findFilteredByColumnIn("teacher_id", idsArray);
		if(!ccTeacherCourses.isEmpty()) {
			return renderFAIL("0713", response, header);
		}
		// 判断是否还有专业认证教师课程表在使用
		List<CcMajorTeacher> ccMajorTeachers = CcMajorTeacher.dao.findFilteredByColumnIn("teacher_id", idsArray);
		if(!ccMajorTeachers.isEmpty()) {
			return renderFAIL("0714", response, header);
		}
		// 判断是否还有专业表在使用
		List<CcMajor> ccMajors = CcMajor.dao.findFilteredByColumnIn("officer_id", idsArray);
		if(!ccMajors.isEmpty()) {
			return renderFAIL("0715", response, header);
		}
//		TODO 等待被延迟的表被合并以后，这里取消注释
//		// 判断是否还有教师进修表在使用
//		List<CcTeacherFurtherEducation> ccTeacherFurtherEducations = CcTeacherFurtherEducation.dao.findFilteredByColumnIn("teacher_id", idsArray);
//		if(!ccTeacherFurtherEducations.isEmpty()) {
//			return renderFAIL("0716", response, header);
//		}
		
		Boolean deleteResult = CcTeacher.dao.deleteAll(idsArray, date);
		if(!deleteResult) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return renderSUC(result, response, header);
		}
		
		UserService userService = SpringContextHolder.getBean(UserService.class);
		// 删除用户、角色、cache
		if(!userService.deleteUser(idsArray, date)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return renderSUC(result, response, header);
		}
		// 返回操作是否成功
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}
	
}
