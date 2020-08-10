package com.gnet.certification;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.gnet.object.CcPlanVersionOrderType;
import org.springframework.stereotype.Service;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcPlanVersion;
import com.gnet.model.admin.Office;
import com.gnet.pager.Pageable;
import com.gnet.service.OfficeService;
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看某个专业的培养计划列表
 * 
 * @author wct
 * @date 2016年7月4日
 */
@Service("EM00370")
public class EM00370 extends BaseApi implements IApi {
	
	@Resource(name = "officeService")
	private OfficeService officeSerivce;

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long appointMajorId = paramsLongFilter(params.get("appointMajorId"));
		if(params.containsKey("appointMajorId") && appointMajorId == null) {
			return renderFAIL("1009", response, header, "appointMajorId的参数值非法");
		}
		String name = paramsStringFilter(params.get("name"));
		// 分页信息
		Integer pageNumber = paramsIntegerFilter(params.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(params.get("pageSize"));
		String orderProperty = paramsStringFilter(params.get("orderProperty"));
		String orderDirection = paramsStringFilter(params.get("orderDirection"));
        Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 从token中获取专业信息
		Long userId = UserCacheKit.getUser(request.getHeader().getToken()).getLong("userId");
		Office office = officeSerivce.getMajorByUserId(userId);
		Long majorId = office != null ? office.getLong("id") : null;
		// 指定专业编号优先
		if (appointMajorId != null) {
			majorId = appointMajorId;
		}
		// 专业编号不能为空
		if (majorId == null) {
			return renderFAIL("0420", response, header);
		}
			
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcPlanVersionOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		Map<String, Object> result = Maps.newHashMap();
		Page<CcPlanVersion> page = CcPlanVersion.dao.page(pageable, name, majorId);
		List<CcPlanVersion> list = page.getList();
		// 是否分页
		if(pageable.isPaging()){
			result.put("pageNumber", page.getPageNumber());
			result.put("pageSize", page.getPageSize());
			result.put("totalPage", page.getTotalPage());
			result.put("totalRow", page.getTotalRow());
		}
		
		// 参数过滤
		List<Map<String, Object>> ccPlanVersions = Lists.newArrayList();
		for (CcPlanVersion ccPlanVersion : list) {
			Map<String, Object> object = Maps.newHashMap();
			object.put("id", ccPlanVersion.getLong("id"));
			object.put("name", ccPlanVersion.getStr("name"));
			object.put("createDate", ccPlanVersion.getDate("create_date"));
			object.put("versionName", ccPlanVersion.getStr("version_name"));
			object.put("versionState", ccPlanVersion.getInt("version_state"));
			object.put("versionStateName", DictUtils.findLabelByTypeAndKey("versionState", ccPlanVersion.getInt("version_state")));
			object.put("majorVersion", ccPlanVersion.getInt("major_version"));
			object.put("minorVersion", ccPlanVersion.getInt("minor_version"));
			object.put("enableGrade", ccPlanVersion.getInt("enable_grade"));
			object.put("isUse", ccPlanVersion.getBoolean("is_use"));
			ccPlanVersions.add(object);
		}
		result.put("list", ccPlanVersions);
		
		return renderSUC(result, response, header);
	}
}
