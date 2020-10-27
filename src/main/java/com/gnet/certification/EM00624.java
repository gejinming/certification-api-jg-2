package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourse;
import com.gnet.model.admin.CcCourseGroup;

/**
 * 批量删除课程组--限选表
 * 
 * @author SY
 * 
 * @date 2016年07月14日 11:10:53
 *
 */
@Service("EM00624")
@Transactional(readOnly=false)
public class EM00624 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String,Boolean> result = new HashMap<>();
		
		Date date = new Date();
		Long ids = paramsLongFilter(param.get("id"));
		//List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		Integer type = paramsIntegerFilter(param.get("typeId"));
		//Long[] idsArray = ids.toArray(new Long[ids.size()]);

		// 删除
		List<CcCourse> courseList= CcCourse.dao.findFilteredByColumnIn("course_group_id", ids);
		if (type==1){
			for(CcCourse temp : courseList) {
				temp.set("modify_date", date);
				temp.set("course_group_id", null);
			}
			if(!courseList.isEmpty() && !CcCourse.dao.batchUpdate(courseList, "modify_date,course_group_id")) {
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}else {

			for(CcCourse temp : courseList) {
				temp.set("modify_date", date);
				temp.set("course_group_mange_id", null);
			}
			if(!courseList.isEmpty() && !CcCourse.dao.batchUpdate(courseList, "modify_date,course_group_mange_id")) {
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}


		
		// 删除CcCourseGroup
		if(!CcCourseGroup.dao.deleteAllById(ids, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}	
	
