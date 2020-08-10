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
import com.gnet.model.admin.CcStudent;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 根据专业编号返回学生年级列表
 * 
 * @author xzl
 * 
 * @Date 2016年11月10日
 */
@Service("EM00678")
@Transactional(readOnly=true)
public class EM00678 extends BaseApi implements IApi{

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Map<String, Object> result = Maps.newHashMap();
		
		Long majorId = paramsLongFilter(params.get("majorId"));
        if(majorId == null){
        	return renderFAIL("0283", response, header);
        }
        List<Integer> gradeList = Lists.newArrayList(); 
        List<CcStudent> studentList = CcStudent.dao.findGradeListByMajorId(majorId);
        if(!studentList.isEmpty()){
        	for(CcStudent student : studentList){
        		gradeList.add(student.getInt("grade"));
        	}
        }
        
		result.put("list", gradeList);
		
		//返回结果
		return renderSUC(result, response, header);
		
				
	}

}
