package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcTeacherCourse;
import com.gnet.model.admin.CcVersion;
import com.gnet.service.CcVersionService;
import com.gnet.utils.SpringContextHolder;

/**
 * 指定版本的上一个工作版本的最大排课年级
 * 
 * @author xzl
 * 
 * @date 2016年11月7日
 *
 */
@Service("EM00675")
@Transactional(readOnly=true)
public class EM00675 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long majorId = paramsLongFilter(param.get("majorId"));
		Integer majorVersion = paramsIntegerFilter(param.get("majorVersion"));
		
		if (majorVersion == null || majorVersion < 1) {
			return renderFAIL("0143", response, header);
		}
		if (majorId == null) {
			return renderFAIL("0146", response, header);
		}
		
		Map<String, Object> map = new HashMap<>();
		
		CcVersionService ccVersionService = SpringContextHolder.getBean(CcVersionService.class);
		CcVersion latestVersion = ccVersionService.findBeforeVersion(majorId, majorVersion);
		if(latestVersion == null){
			map.put("grade", 2000);
			return renderSUC(map, response, header);
		}
		
		CcTeacherCourse teacherCourse = CcTeacherCourse.dao.findMaxGrade(latestVersion.getLong("id"));
		Integer grade = teacherCourse == null ? latestVersion.getInt("enable_grade") : teacherCourse.getInt("grade");     
		
		
		map.put("grade", grade);
		return renderSUC(map, response, header);
	}

}
