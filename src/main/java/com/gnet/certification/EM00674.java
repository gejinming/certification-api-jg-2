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
import com.gnet.model.admin.CcTeacherCourse;
import com.google.common.collect.Maps;

/**
 * 返回某专业下的排课信息年级列表
 * @author SY
 * @Date 2016年11月7日18:17:53
 */
@Service("EM00674")
@Transactional(readOnly=true)
public class EM00674 extends BaseApi implements IApi{

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Map<String, Object> result = Maps.newHashMap();
		
		Long majorId = paramsLongFilter(params.get("majorId"));
        
        if(majorId == null){
        	return renderFAIL("0130", response, header);
        }
        
        List<CcTeacherCourse> ccTeacherCourses = CcTeacherCourse.dao.findGradeByMajorId(majorId);
        List<Integer> gradeList = new ArrayList<>();
        if(!ccTeacherCourses.isEmpty()){
            // 获取所有的不同【年级】
            for(CcTeacherCourse temp : ccTeacherCourses) {
    		   gradeList.add(temp.getInt("grade"));
            }
        }
        
		result.put("list", gradeList);	
		//返回结果
		return renderSUC(result, response, header);
		
				
	}

}
