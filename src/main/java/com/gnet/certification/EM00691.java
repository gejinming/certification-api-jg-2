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
import com.gnet.model.admin.CcClass;
import com.google.common.collect.Maps;

/**
 * 获取专业下的行政班年级列表
 * @author xzl
 * @Date 2017年1月9日15:51:44
 */
@Service("EM00691")
@Transactional(readOnly=true)
public class EM00691 extends BaseApi implements IApi{

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Map<String, Object> result = Maps.newHashMap();
		
		Long majorId = paramsLongFilter(params.get("majorId"));
        
        if(majorId == null){
        	return renderFAIL("0130", response, header);
        }
        
        List<Integer> gradeList = new ArrayList<>();
        List<CcClass> ccClasses = CcClass.dao.findByMajorId(majorId);
        if(!ccClasses.isEmpty()){
        	for(CcClass temp : ccClasses){
        		gradeList.add(temp.getInt("grade"));
        	}
        }
        
		result.put("list", gradeList);	
		//返回结果
		return renderSUC(result, response, header);
		
				
	}

}
