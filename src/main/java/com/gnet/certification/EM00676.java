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
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 某个版本的排课信息年级列表
 * 
 * @author xzl
 * 
 * @Date 2016年11月8日18:50:50
 */
@Service("EM00676")
@Transactional(readOnly=true)
public class EM00676 extends BaseApi implements IApi{

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Map<String, Object> result = Maps.newHashMap();
		
		Long versionId = paramsLongFilter(params.get("versionId"));
        List<Integer> gradeList = Lists.newArrayList();
        
        if(versionId == null){
        	return renderFAIL("0140", response, header);
        }
        
        List<CcTeacherCourse> ccTeacherCourses = CcTeacherCourse.dao.findByVersionId(versionId);
        
        if(!ccTeacherCourses.isEmpty()){
            // 获取所有的不同【年级】
            for(CcTeacherCourse temp : ccTeacherCourses) {
        	   Integer grade = temp.getInt("grade");
        	   if(!gradeList.contains(grade)) {
        		   gradeList.add(grade);
        	   }
            }
        }
        
		result.put("list", gradeList);
		//返回结果
		return renderSUC(result, response, header);
		
				
	}

}
