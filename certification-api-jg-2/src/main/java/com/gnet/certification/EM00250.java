package com.gnet.certification;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.gnet.model.admin.CcClass;
import com.gnet.model.admin.Office;
import com.gnet.object.OfficeOrderType;
import com.gnet.pager.Pageable;
import com.gnet.service.OfficeService;
import com.gnet.utils.ParamSceneUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看行政班列表
 * 
 * @author sll
 * 
 * @date 2016年06月29日 18:38:27
 * 
 */
@Service("EM00250")
@Transactional(readOnly=true)
public class EM00250 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		String name = paramsStringFilter(param.get("name"));
		Integer grade = paramsIntegerFilter(param.get("grade"));
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
		// 通过token获取用户的专业编号
		String token = request.getHeader().getToken();
		Office departmentOffice = UserCacheKit.getDepartmentOffice(token);
		Long[] majorIds = officeService.getMajorIdsByOffice(departmentOffice);
		Long majorId = paramsLongFilter(param.get("majorId"));
		if((param.containsKey("majorId") || (majorIds == null || majorIds.length == 0)) && majorId == null){
			return renderFAIL("1009", response, header, "majorId的参数值非法");
		}

		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, OfficeOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		

		Map<String, Object> classesMap = Maps.newHashMap();
		Page<CcClass> ccClassPage = CcClass.dao.page(pageable, name, majorIds, grade, majorId);
		List<CcClass> ccClassList = ccClassPage.getList();
		
		// 判断是否分页
		if(pageable.isPaging()){
			classesMap.put("totalRow", ccClassPage.getTotalRow());
			classesMap.put("totalPage", ccClassPage.getTotalPage());
			classesMap.put("pageSize", ccClassPage.getPageSize());
			classesMap.put("pageNumber", ccClassPage.getPageNumber());
		}

		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (CcClass temp : ccClassList) {
			Map<String, Object> office = Maps.newHashMap();
			office.put("id", temp.get("id"));
			office.put("name", temp.get("name"));
			office.put("type", temp.get("type"));
			office.put("grade", temp.getInt("grade"));
			office.put("isSystem", temp.get("is_system"));
			office.put("description", temp.get("description"));
			office.put("majorName", temp.get("majorName"));
			office.put("instituteName", temp.get("instituteName"));
			office.put("classLeader", temp.get("class_leader"));
			office.put("remark", temp.get("remark"));
			
			list.add(office);
		}
		
		classesMap.put("list", list);
		
		return renderSUC(classesMap, response, header);
	}
	
}
