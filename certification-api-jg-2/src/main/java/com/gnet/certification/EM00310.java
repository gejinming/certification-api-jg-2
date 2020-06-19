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
import com.gnet.model.admin.CcEduclass;
import com.gnet.object.CcEduclassOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看教学班列表
 * 
 * @author SY
 * 
 * @date 2016年07月01日 14:41:11
 * 
 */
@Service("EM00310")
@Transactional(readOnly=true)
public class EM00310 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		String courseName = paramsStringFilter(param.get("courseName"));
		Long teacherCourseId = paramsLongFilter(param.get("teacherCourseId"));
		if(param.containsKey("teacherCourseId") && teacherCourseId == null){
			return renderFAIL("1009", response, header, "teacherCourseId的参数值非法");
		}
		
		// 进行分页
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcEduclassOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		Map<String, Object> ccEduclasssMap = Maps.newHashMap();
		Page<CcEduclass> ccEduclassPage = CcEduclass.dao.page(pageable, courseName, teacherCourseId);
		List<CcEduclass> ccEduclassList = ccEduclassPage.getList();
		// 判断是否分页
		if(pageable.isPaging()){
			ccEduclasssMap.put("totalRow", ccEduclassPage.getTotalRow());
			ccEduclasssMap.put("totalPage", ccEduclassPage.getTotalPage());
			ccEduclasssMap.put("pageSize", ccEduclassPage.getPageSize());
			ccEduclasssMap.put("pageNumber", ccEduclassPage.getPageNumber());
		}

		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (CcEduclass temp : ccEduclassList) {
			Map<String, Object> ccEduclass = new HashMap<>();
			ccEduclass.put("id", temp.get("id"));
			ccEduclass.put("createDate", temp.get("create_date"));
			ccEduclass.put("modifyDate", temp.get("modify_date"));
			ccEduclass.put("educlassName", temp.get("educlass_name"));
			ccEduclass.put("teacherCourseId", temp.get("teacher_course_id"));
			ccEduclass.put("studentNum", temp.get("studentNum"));
			ccEduclass.put("courseId", temp.get("courseId"));
			ccEduclass.put("courseName", temp.get("courseName"));
			ccEduclass.put("courseCode", temp.get("courseCode"));
			ccEduclass.put("teacherId", temp.get("teacher_id"));
			ccEduclass.put("teacherName", temp.get("teacher_name"));
			ccEduclass.put("resultType", temp.get("resultType"));
			ccEduclass.put("resultTypeName", DictUtils.findLabelByTypeAndKey("courseResultType", temp.getInt("resultType")));
			list.add(ccEduclass);
		}
		
		ccEduclasssMap.put("list", list);
		
		return renderSUC(ccEduclasssMap, response, header);
	}
	
}
