package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseGradecompose;
import com.google.common.collect.Lists;

/**
 * 编辑开课课程成绩组成元素重新排序接口
 * 
 * @author xzl
 * 
 * @date 2016年7月7日
 *
 */
@Service("EM00523")
@Transactional(readOnly=false)
public class EM00523 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		JSONArray courseGradecomposeSortArray = paramsJSONArrayFilter(param.get("courseGradecomposeSortArray"));
		List<CcCourseGradecompose> courseGradecomposes = Lists.newArrayList();
		
		if(courseGradecomposeSortArray.isEmpty()){
			return renderFAIL("0471", response, header);
		}
		Date date = new Date();
		//验证id和sort不能为空
		for(int i = 0; i < courseGradecomposeSortArray.size(); i++) {
			JSONObject map = (JSONObject) courseGradecomposeSortArray.get(i);
			CcCourseGradecompose temp = new CcCourseGradecompose();
			if(map.get("id") == null){
				return renderFAIL("0471", response, header);
			}
			if(map.get("sort") == null){
				return renderFAIL("0474", response, header);
			}
			temp.set("id", map.get("id"));
			temp.set("modify_date", date);
			temp.set("sort", Integer.valueOf(String.valueOf(map.get("sort"))));
			courseGradecomposes.add(temp);
		}
		Boolean isSuccess = CcCourseGradecompose.dao.batchUpdate(courseGradecomposes, "modify_date,sort");
		Map<String, Boolean> result = new HashMap<>();
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}
	
}
