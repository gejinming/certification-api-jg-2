package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcGradecompose;
import com.gnet.model.admin.CcMajor;
import com.gnet.model.admin.CcTeacher;
import com.gnet.model.admin.CcVersion;
import com.gnet.model.admin.Office;

/**
 * 删除专业某条信息
 * 
 * @author SY
 * @Date 2016年6月20日14:09:05
 */
@Service("EM00084")
public class EM00084 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Date date = new Date();
		
		List<Long> ids = paramsJSONArrayFilter(params.get("ids"), Long.class);
		
		// majorId不能为空信息的过滤
		if (ids.isEmpty()) {
			return renderFAIL("0130", response, header);
		}
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		result.put("isSuccess", false);
		
		// 查找这一些版本的专业下面是否有班级，有则返回error信息
		if(CcVersion.dao.findFilteredByColumnIn("major_id", idsArray).size() > 0) {
			// 还有版本表在使用
			return renderFAIL("0700", response, header);
		} else if(Office.dao.findFilteredByColumnIn("parentid", idsArray).size() > 0) {
			// 还有教学班表在使用
			return renderFAIL("0701", response, header);
		} else if(CcTeacher.dao.findFilteredByColumnIn("major_id", idsArray).size() > 0) {
			// 还有教师表在使用
			return renderFAIL("0702", response, header);
		} else if(CcGradecompose.dao.findFilteredByColumnIn("major_id", idsArray).size() > 0) {
			// 还有成绩组成元素表在使用
			return renderFAIL("0703", response, header);
//			TODO 这里的代码等待【自评报告教师查询】的一些功能合并以后再取消注释，只要是model类没有
//		} else if(CcEnrollment.dao.findFilteredByColumnIn("major_id", idsArray).size() > 0) {
//			// 还有招生情况表在使用
//			return renderFAIL("0704", response, header);
//		} else if(CcStudentTransfer.dao.findFilteredByColumnIn("major_id", idsArray).size() > 0) {
//			// 还有学生转入转出表在使用
//			return renderFAIL("0705", response, header);
//		} else if(CcGraduateEmployment.dao.findFilteredByColumnIn("major_id", idsArray).size() > 0) {
//			// 还有毕业生就业情况表在使用
//			return renderFAIL("0706", response, header);
		}
		// 删除office
		List<Office> offices = Office.dao.findFilteredByColumnIn("id", idsArray);
		for(Office temp : offices) {
			temp.set("modify_date", date);
			temp.set("is_del", Boolean.TRUE);
		}
		Boolean deleteResult = Office.dao.batchUpdate(offices, "modify_date,is_del"); 
		
		if(!deleteResult) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("message", "更新部门表的时候失败了！");
			return renderSUC(result, response, header);
		}
		deleteResult = CcMajor.dao.deleteAll(idsArray, date);
		if(!deleteResult) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("message", "更新专业表的时候失败了！");
			return renderSUC(result, response, header);
		}
		
		
		// 返回操作是否成功
		result.put("isSuccess", deleteResult);
		return renderSUC(result, response, header);
	}
	
}
