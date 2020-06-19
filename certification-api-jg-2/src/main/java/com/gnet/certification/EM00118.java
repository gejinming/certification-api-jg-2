package com.gnet.certification;

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
import com.gnet.model.admin.CcTeacher;
import com.gnet.object.CcTeacherOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 获得用户所属学校的教师列表
 * （若传学校编号优先考虑传入的学校编号）
 * 
 * @author wct
 * @date 2016年10月29日
 */
@Service("EM00118")
@Transactional(readOnly = false)
public class EM00118 extends BaseApi implements IApi  {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取学校编号
		Long schoolId = paramsLongFilter(params.get("schoolId"));
		// 分页数据
		Integer pageNumber = paramsIntegerFilter(params.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(params.get("pageSize"));
		String orderProperty = paramsStringFilter(params.get("orderProperty"));
		String orderDirection = paramsStringFilter(params.get("orderDirection"));
		
		if(params.containsKey("schoolId") && schoolId == null){
			return renderFAIL("1009", response, header, "schoolId的参数值非法");
		}
		
		// 获得用户所属的学校编号
		if (schoolId == null) {
			schoolId = UserCacheKit.getSchoolId(request.getHeader().getToken());
		}
		
		Pageable pageable = new Pageable(pageNumber, pageSize);
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcTeacherOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		// 获得教师列表
		Page<CcTeacher> page = CcTeacher.dao.page(pageable, schoolId);
		Map<String, Object> result = Maps.newHashMap();
		// 判断是否分页
		if (pageable.isPaging()) {
			// 分页信息返回
			result.put("totalRow", page.getTotalRow());
			result.put("totalPage", page.getTotalPage());
			result.put("pageSize", page.getPageSize());
			result.put("pageNumber", page.getPageNumber());
		}
		
		List<CcTeacher> teachers = page.getList();
		List<Map<String, Object>> list = Lists.newArrayList();
		for (CcTeacher ccTeacher : teachers) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", ccTeacher.get("id"));
			map.put("name", ccTeacher.get("name"));
			list.add(map);
		}
		
		result.put("list", list);
		
		return renderSUC(result, response, header);
	}

}
