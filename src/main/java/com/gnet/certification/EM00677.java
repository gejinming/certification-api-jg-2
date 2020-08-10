
package com.gnet.certification;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcVersion;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;

/**
 * 版本内的适用年级列表
 * 
 * @author xzl
 * 
 * @Date 2016年11月8日19:21:11
 */
@Service("EM00677")
@Transactional(readOnly=true)
public class EM00677 extends BaseApi implements IApi{

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Map<String, Object> result = Maps.newHashMap();
		
		Long versionId = paramsLongFilter(params.get("versionId"));
		List<String> grades = Lists.newArrayList();
        List<Integer> gradeList = Lists.newArrayList();
        
        if(versionId == null){
        	return renderFAIL("0140", response, header);
        }
        CcVersion version = CcVersion.dao.findFilteredById(versionId);
        if(version == null){
        	return renderFAIL("0141", response, header);
        }
    
        String applyGrade = version.getStr("apply_grade");
        if(StrKit.isBlank(applyGrade)){
        	return renderFAIL("0124", response, header);
        }
        
        
        if(applyGrade.endsWith(CcVersion.GRADE_CHARACTER)){
        	result.put("type", CcVersion.MINGRADE);
        	gradeList.add(Integer.valueOf(applyGrade.substring(0, applyGrade.length()-1)));
        }else{
        	result.put("type", CcVersion.GRADELIST);
        	grades = Arrays.asList(applyGrade.split(CcVersion.SPLIT));
            for(String grade : grades){
            	gradeList.add(Integer.valueOf(grade));
            }
        }
        
        //排序
        Collections.sort(gradeList);
		result.put("list", gradeList);
		
		//返回结果
		return renderSUC(result, response, header);
					
	}

}
