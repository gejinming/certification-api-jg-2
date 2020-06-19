package com.gnet.certification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourse;
import com.gnet.model.admin.CcEduclass;
import com.gnet.model.admin.CcTeacher;
import com.gnet.model.admin.CcTeacherCourse;
import com.gnet.model.admin.CcTerm;
import com.gnet.model.admin.CcVersion;
import com.gnet.model.admin.Office;
import com.gnet.service.CcTeacherCourseService;
import com.gnet.utils.DictUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;

/**
 * 查看当前可接受分享的教师开课表列表信息的的接口
 * 
 * @author SY
 * 
 * @date 2017年10月22日
 * 
 */
@Service("EM00784")
@Transactional(readOnly=true)
public class EM00784 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Long teacherCourseId = paramsLongFilter(param.get("teacherCourseId"));

		if (teacherCourseId == null) {
			return renderFAIL("0310", response, header);
		}

		// 分享人的信息
		CcTeacherCourse ccTeacherCourse = CcTeacherCourse.dao.findFilteredById(teacherCourseId);
		if(ccTeacherCourse == null) {
			return renderFAIL("0311", response, header);
		}
		
		CcTeacherCourseService ccTeacherCourseService = SpringContextHolder.getBean(CcTeacherCourseService.class);
		List<CcTeacherCourse> list = ccTeacherCourseService.findSharedTeacherCourse(ccTeacherCourse);
		Map<String, Object> ccTeacherCoursesMap = Maps.newHashMap();
		
		
		ccTeacherCoursesMap.put("list", list);
		
		return renderSUC(ccTeacherCoursesMap, response, header);
	}
	
}
