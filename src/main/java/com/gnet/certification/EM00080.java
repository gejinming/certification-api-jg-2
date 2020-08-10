package com.gnet.certification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcMajor;
import com.gnet.model.admin.Office;
import com.gnet.object.CcMajorOrderType;
import com.gnet.pager.Pageable;
import com.gnet.service.OfficeService;
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看专业表信息列表
 * 
 * @author SY
 * @Date 2016年6月20日14:09:05
 */
@Service("EM00080")
public class EM00080 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 查询
		// 学院编号
		Long instituteId = paramsLongFilter(params.get("instituteId"));
		String instituteName = paramsStringFilter(params.get("instituteName"));
		// 专业名字
		String majorName = paramsStringFilter(params.get("majorName"));
			
		Integer pageNumber = paramsIntegerFilter(params.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(params.get("pageSize"));
		String orderProperty = paramsStringFilter(params.get("orderProperty"));
		String orderDirection = paramsStringFilter(params.get("orderDirection"));
		Pageable pageable = new Pageable(pageNumber, pageSize);
		String token = request.getHeader().getToken();
		OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
		Office departmentOffice = UserCacheKit.getDepartmentOffice(token);
		Long[] majorIds = officeService.getMajorIdsByOffice(departmentOffice);
		
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcMajorOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		// 获取整个列表
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Page<CcMajor> majorPage = CcMajor.dao.page(pageable, majorIds, instituteId, majorName, instituteName);
		List<CcMajor> ccMajorList = majorPage.getList();
		if(pageable.isPaging()){
			returnMap.put("totalRow", majorPage.getTotalRow());
			returnMap.put("totalPage", majorPage.getTotalPage());
			returnMap.put("pageSize", majorPage.getPageSize());
			returnMap.put("pageNumber", majorPage.getPageNumber());
		}

		// 返回内容过滤
		List<CcMajor> list = new ArrayList<>();
		for (CcMajor temp : ccMajorList) {
			CcMajor major = new CcMajor();
			major.put("id", temp.get("id"));
			major.put("createDate", temp.get("create_date"));
			major.put("modifyDate", temp.get("modify_date"));
			major.put("officerId", temp.get("officer_id"));
			major.put("educationLength", temp.get("education_length"));
			major.put("specializedFieldsName", DictUtils.findLabelByTypeAndKey("specializedFields", temp.getInt("specialized_fields")));
			major.put("awardDegreeName", DictUtils.findLabelByTypeAndKey("awardDegree", temp.getInt("award_degree")));
			major.put("educationLevelName", DictUtils.findLabelByTypeAndKey("educationLevel", temp.getInt("education_level")));
			major.put("description", temp.get("description"));
			major.put("majorName", temp.get("majorName"));
			major.put("instituteId", temp.get("instituteId"));
			major.put("instituteName", temp.get("instituteName"));
			major.put("userName", temp.get("userName"));
			list.add(major);
		}
		
		returnMap.put("list", list);
		
		// 结果返回
		return renderSUC(returnMap, response, header);
	}
}
