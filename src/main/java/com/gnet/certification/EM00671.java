package com.gnet.certification;


import java.util.List;
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 返回年级列表
 * @author xzl
 * @Date 2016年8月31日
 */
@Service("EM00671")
@Transactional(readOnly=true)
public class EM00671 extends BaseApi implements IApi{

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Map<String, Object> result = Maps.newHashMap();
		
		Long planId = paramsLongFilter(params.get("planId"));
		Long majorId = paramsLongFilter(params.get("majorId"));
		
        if(planId == null && majorId == null){
        	return renderFAIL("0750", response, header);
        }
        
        List<Integer> gradeList = Lists.newArrayList();
        CcVersion version = null;
        
        if(majorId != null){
        	CcVersion maxVersion = CcVersion.dao.findMaxGradeByMajorId(majorId);
        	if(maxVersion == null){
        		return renderFAIL("0141", response, header);
        	}
        	
        	planId = maxVersion.getLong("id");
        	version = CcVersion.dao.findMinGradeByMajorId(majorId);
        }else{
        	if(planId == null){
        		return renderFAIL("0140", response, header);
        	}
        	version = CcVersion.dao.findFirstFilteredByColumn("id", planId);
        }
		
		//这个版本下开课课程最大的年级
        CcTeacherCourse teacherCourse = CcTeacherCourse.dao.findMaxGrade(planId);
        
        if(version == null){
        	return renderFAIL("0141", response, header);
        }
        
        if(teacherCourse != null && version.getInt("enable_grade") != null){
        	for(int i=version.getInt("enable_grade"); i <=teacherCourse.getInt("grade"); i++){
        		gradeList.add(i);
        	}
        }
        	
		result.put("list", gradeList);
		
		//返回结果
		return renderSUC(result, response, header);
		
				
	}

}
