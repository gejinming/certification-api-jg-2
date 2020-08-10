package com.gnet.certification;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradecomposeStudetail;
import com.gnet.model.admin.CcEduclassStudent;
import com.gnet.model.admin.CcMajorStudent;
import com.gnet.model.admin.CcReportPersonalCourse;
import com.gnet.model.admin.CcReportPersonalIndication;
import com.gnet.model.admin.CcScoreStuIndigrade;
import com.gnet.model.admin.CcStudent;
import com.gnet.model.admin.CcStudentEvalute;
/**
 * 批量删除学生信息
 * 
 * @author wct
 * 
 * @date 2016年06月29日 22:26:58
 *
 */
@Service("EM00204")
@Transactional(readOnly=false)
public class EM00204 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String, Object> result = new HashMap<>();
		
		Date date = new Date();
		
		List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		// 判断是否还有教学班学生表在使用
		List<CcEduclassStudent> ccEduclassStudents = CcEduclassStudent.dao.findFilteredByColumnIn("student_id", idsArray);
		if(!ccEduclassStudents.isEmpty()) {
			return renderFAIL("0717", response, header);
		}
		// 判断是否还有成绩组成元素明细学生关联表在使用
		List<CcCourseGradecomposeStudetail> ccCourseGradecomposeStudetails = CcCourseGradecomposeStudetail.dao.findFilteredByColumnIn("student_id", idsArray);
		if(!ccCourseGradecomposeStudetails.isEmpty()) {
			return renderFAIL("0718", response, header);
		}
		// 判断是否还有专业认证学生表在使用
		List<CcMajorStudent> ccMajorStudents = CcMajorStudent.dao.findByColumnIn("student_id", idsArray);
		if(!ccMajorStudents.isEmpty()) {
			return renderFAIL("0719", response, header);
		}
		// 判断是否还有学生考评点成绩表在使用
		List<CcStudentEvalute> ccStudentEvalutes = CcStudentEvalute.dao.findFilteredByColumnIn("student_id", idsArray);
		if(!ccStudentEvalutes.isEmpty()) {
			return renderFAIL("0720", response, header);
		}
		// 判断是否还有个人课程达成度报表统计表(课程指标点记录表)在使用
		List<CcReportPersonalCourse> ccReportPersonalCourses = CcReportPersonalCourse.dao.findFilteredByColumnIn("student_id", idsArray);
		if(!ccReportPersonalCourses.isEmpty()) {
			return renderFAIL("0721", response, header);
		}
		// 判断是否还有个人指标点达成度统计表在使用
		List<CcReportPersonalIndication> ccReportPersonalIndications = CcReportPersonalIndication.dao.findFilteredByColumnIn("student_id", idsArray);
		if(!ccReportPersonalIndications.isEmpty()) {
			return renderFAIL("0722", response, header);
		}
		// 判断是否还有考核成绩分析法学生指标点成绩表在使用
		List<CcScoreStuIndigrade> ccScoreStuIndigrades = CcScoreStuIndigrade.dao.findFilteredByColumnIn("student_id", idsArray);
		if(!ccScoreStuIndigrades.isEmpty()) {
			return renderFAIL("0723", response, header);
		}
		
		// 删除CcStudent
		if(!CcStudent.dao.deleteAll(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}	
	
