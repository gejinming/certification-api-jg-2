package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourse;
import com.gnet.model.admin.CcCourseGroup;
import com.gnet.model.admin.CcCourseGroupMange;
import com.gnet.model.admin.CcCourseGroupMangeGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 删除课程组--限选表
 * 
 * @author gjm
 * 
 * @date 20120年06月10日 11:10:53
 *
 */
@Service("EM01190")
@Transactional(readOnly=false)
public class EM01190 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		Map<String,Boolean> result = new HashMap<>();
		
		Date date = new Date();

		Long id = paramsLongFilter(param.get("id"));
		if (id==null){

			return renderFAIL("2500", response, header);
		}
		
		if (CcCourseGroupMange.dao.isGroupTeach(id)){
			return renderFAIL("2550", response, header);
		}
		//先删除中间关联表CcCourseGroupMangeGroup
		CcCourseGroupMangeGroup.dao.deleteAllByColumn("mange_group_id",id,date);
		// 删除CcCourseGroupMange
		if( !CcCourseGroupMange.dao.deleteAllById(id, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}

}	
	
