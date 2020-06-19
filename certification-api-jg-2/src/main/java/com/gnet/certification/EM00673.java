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
import com.gnet.model.admin.CcVersion;
import com.gnet.object.CcEduclassOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 获取专业下的教学班 
 * 
 * @author xzl
 * 
 * @date 2016年8月31日
 * 
 */
@Service("EM00673")
@Transactional(readOnly=true)
public class EM00673 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Long majorId = paramsLongFilter(param.get("majorId"));
		if(param.containsKey("majorId") && majorId == null){
			return renderFAIL("1009", response, header, "majorId的参数值非法");
		}
		Integer grade = paramsIntegerFilter(param.get("grade"));
		if(param.containsKey("grade") && grade == null){
			return renderFAIL("1009", response, header, "grade的参数值非法");
		}
		Long versionId = paramsLongFilter(param.get("versionId"));
		if(param.containsKey("versionId") && versionId == null){
			return renderFAIL("1009", response, header, "versionId的参数值非法");
		}
		if(majorId != null && grade != null){
			versionId = CcVersion.dao.findNewestVersion(majorId, grade);
			if (versionId == null) {
				return renderFAIL("0671", response, header);
			}
		}
		String courseName = paramsStringFilter(param.get("courseName"));
				
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		
		if((majorId == null || grade == null) && versionId == null){
			// （majorId + grade） 与 versionId不得同时为空
			return renderFAIL("0386", response, header);
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
		Page<CcEduclass> ccEduclassPage = CcEduclass.dao.page(pageable, majorId, versionId, courseName, grade);
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
