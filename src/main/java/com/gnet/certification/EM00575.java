package com.gnet.certification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcCourse;
import com.gnet.model.admin.CcVersion;
import com.gnet.object.CcVersionOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;


/**
 * 教师查看某门课程在各个大纲历史版本下的信息(code相同的即为同一门课)
 * 
 * @author xzl
 * 
 * @date 2016年7月13日
 *
 */
@Service("EM00575")
@Transactional(readOnly=false)
public class EM00575 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		//课程编号
		Long courseId = paramsLongFilter(param.get("courseId"));
		if(courseId == null){
			return renderFAIL("0312", response, header);
		}
		
		CcVersion version = CcVersion.dao.findMajorIdByCourseId(courseId);
		//版本信息不能为空
		if(version == null){
			return renderFAIL("0148", response, header);
		}
		//专业编号
		Long majorId = version.getLong("majorId");
		//课程代码
		String code = version.getStr("code");
		
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcVersionOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		Map<String, Object> result = new HashMap<String, Object>();			
		Page<CcCourse> page = CcCourse.dao.page(pageable, code, majorId);
		List<CcCourse> courseList = page.getList();
		//判断是否分页
		if(pageable.isPaging()){
			result.put("pageNumber", page.getPageNumber());
			result.put("pageSize", page.getPageSize());
			result.put("totalRow", page.getTotalRow());
			result.put("totalPage", page.getTotalPage());
		}
		
		List<Map<String, Object>> list = new ArrayList<>();
		for(CcCourse temp: courseList){
			Map<String, Object> ccCourse = Maps.newHashMap();
			ccCourse.put("courseId", temp.getLong("id"));
			ccCourse.put("courseName", temp.getStr("name"));
			ccCourse.put("courseOutlineId", temp.getLong("courseOutlineId"));
			ccCourse.put("planVersionName", temp.getStr("planVersionName"));
			ccCourse.put("courseVersionName", temp.getStr("courseVersionName"));
			ccCourse.put("versionName", temp.getStr("versionName"));
			ccCourse.put("content", temp.getStr("content"));
			ccCourse.put("courseCode", temp.getStr("code"));
			list.add(ccCourse);
		}
			
		result.put("list", list);
		//返回结果
		return renderSUC(result, response, header);	
	}
}
