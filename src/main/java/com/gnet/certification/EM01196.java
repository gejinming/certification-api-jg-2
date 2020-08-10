package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGroup;
import com.gnet.model.admin.CcCourseGroupMange;
import com.gnet.model.admin.CcCourseGroupMangeTeach;
import com.gnet.model.admin.CcCourseGroupTeachMange;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 批量删除课程组--限选表
 * 
 * @author SY
 * 
 * @date 2016年07月14日 11:10:53
 *
 */
@Service("EM01196")
@Transactional(readOnly=false)
public class EM01196 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String,Boolean> result = new HashMap<>();
		
		Date date = new Date();
		
		List<Long> ids = paramsJSONArrayFilter(param.get("ids"), Long.class);
		Long[] idsArray = ids.toArray(new Long[ids.size()]);
		
		// CcCourseGroupMange
		//List<CcCourseGroupMange> courseList = CcCourseGroupMange.dao.findFilteredByColumnIn("course_group_id", ids.toArray(new Long[ids.size()]));
		/*for(CcCourseGroupMange temp : courseList) {
			temp.set("modify_date", date);
			temp.set("course_group_id", null);
		}
		if(!courseList.isEmpty() && !CcCourseGroupMange.dao.batchUpdate(courseList, "modify_date,course_group_id")) {
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}*/
		//删除中间表cc_course_group_teach_mange
		CcCourseGroupTeachMange.dao.deleteAllByColumn("teach_group_id",idsArray,date);
		// 删除CcCourseGroupMange
		if(!ids.isEmpty() && !CcCourseGroupMangeTeach.dao.deleteAll(idsArray, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}	
	
