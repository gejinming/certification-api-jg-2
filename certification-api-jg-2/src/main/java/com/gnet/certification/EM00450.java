package com.gnet.certification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcTeacherFurtherEducation;
import com.gnet.model.admin.Office;
import com.gnet.object.CcTeacherFurtherEducationOrderType;
import com.gnet.pager.Pageable;
import com.gnet.service.OfficeService;
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看教师进修经历表列表
 * 
 * @author sll
 * 
 * @date 2016年07月21日 21:08:48
 * 
 */
@Service("EM00450")
@Transactional(readOnly=true)
public class EM00450 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		String majorName = paramsStringFilter(param.get("majorName"));
		Integer educationType = paramsIntegerFilter(param.get("educationType"));
		if(param.containsKey("educationType") && educationType == null) {
		    return renderFAIL("1009", response, header, "educationType的参数值非法");
		}		
		Long majorId = paramsLongFilter(param.get("majorId"));
		if(param.containsKey("majorId") && majorId == null) {
		    return renderFAIL("1009", response, header, "majorId的参数值非法");
		}
		Long instituteId = paramsLongFilter(param.get("instituteId"));
		if(param.containsKey("instituteId") && instituteId == null) {
		    return renderFAIL("1009", response, header, "instituteId的参数值非法");
		}
		String token = request.getHeader().getToken();
		OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
		Office departmentOffice = UserCacheKit.getDepartmentOffice(token);
		Long[] majorIds = officeService.getMajorIdsByOffice(departmentOffice);
		Long[] instituteIds = officeService.getInsitituteIdsByOffice(departmentOffice);
		if(majorIds == null || majorIds.length == 0) {
			return renderFAIL("1004", response, header);
		}else if(instituteId != null && (instituteIds == null || instituteIds.length == 0)){
			return renderFAIL("1004", response, header);
		}	
		
		if(majorId != null){
			if(!Arrays.asList(majorIds).contains(majorId)){
				return renderFAIL("0771", response, header);
			}		
		}else if(instituteId != null){
			if(!Arrays.asList(instituteIds).contains(instituteId)){
				return renderFAIL("0771", response, header);
			}
		}
		
		// 进行分页
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcTeacherFurtherEducationOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		Map<String, Object> ccTeacherFurtherEducationsMap = Maps.newHashMap();
		Page<CcTeacherFurtherEducation> ccTeacherFurtherEducationPage = CcTeacherFurtherEducation.dao.page(pageable, majorName, educationType, majorId, majorIds, instituteId);
		List<CcTeacherFurtherEducation> ccTeacherFurtherEducationList = ccTeacherFurtherEducationPage.getList();	
		// 判断是否分页
		if(pageable.isPaging()){	
		ccTeacherFurtherEducationsMap.put("totalRow", ccTeacherFurtherEducationPage.getTotalRow());
		ccTeacherFurtherEducationsMap.put("totalPage", ccTeacherFurtherEducationPage.getTotalPage());
		ccTeacherFurtherEducationsMap.put("pageSize", ccTeacherFurtherEducationPage.getPageSize());
		ccTeacherFurtherEducationsMap.put("pageNumber", ccTeacherFurtherEducationPage.getPageNumber());
		
	   }
		
		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (CcTeacherFurtherEducation temp : ccTeacherFurtherEducationList) {
			Map<String, Object> ccTeacherFurtherEducation = new HashMap<>();
			ccTeacherFurtherEducation.put("id", temp.get("id"));
			ccTeacherFurtherEducation.put("createDate", temp.get("create_date"));
			ccTeacherFurtherEducation.put("modifyDate", temp.get("modify_date"));
			ccTeacherFurtherEducation.put("teacherId", temp.get("teacher_id"));
			ccTeacherFurtherEducation.put("teacherName", temp.get("teacher_name"));
			ccTeacherFurtherEducation.put("educationType", temp.get("education_type"));
			ccTeacherFurtherEducation.put("educationTypeName", DictUtils.findLabelByTypeAndKey("educationType", temp.getInt("education_type")));
			ccTeacherFurtherEducation.put("majorId", temp.get("major_id"));
			ccTeacherFurtherEducation.put("majorName", temp.get("major_name"));
			ccTeacherFurtherEducation.put("startTime", temp.get("start_time"));
			ccTeacherFurtherEducation.put("endTime", temp.get("end_time"));
			ccTeacherFurtherEducation.put("content", temp.get("content"));
			ccTeacherFurtherEducation.put("site", temp.get("site"));
			ccTeacherFurtherEducation.put("remark", temp.get("remark"));
			list.add(ccTeacherFurtherEducation);
		}
		
		ccTeacherFurtherEducationsMap.put("list", list);
		
		return renderSUC(ccTeacherFurtherEducationsMap, response, header);
	}
}
