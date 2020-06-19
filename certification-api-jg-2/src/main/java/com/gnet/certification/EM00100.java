package com.gnet.certification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcVersion;
import com.gnet.object.CcVersionOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看版本表信息列表
 * 
 * @author SY
 * @Date 2016年6月22日09:57:06
 */
@Service("EM00100")
public class EM00100 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据
		Boolean couldStatueClose = paramsBooleanFilter(params.get("couldStatueClose"));
		Long majorId = paramsLongFilter(params.get("majorId"));
		
		Integer pageNumber = paramsIntegerFilter(params.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(params.get("pageSize"));
		String orderProperty = paramsStringFilter(params.get("orderProperty"));
		String orderDirection = paramsStringFilter(params.get("orderDirection"));
		
		// officeType不能为空信息的过滤
		if (majorId == null) {
			return renderFAIL("0130", response, header);
		}
		
		Pageable pageable = new Pageable(pageNumber, pageSize);
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcVersionOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		List<Integer> statues = new ArrayList<>();
		statues.add(CcVersion.STATUE_EDIT);
		statues.add(CcVersion.STATUE_PUBLISH);
		if(couldStatueClose != null && couldStatueClose == Boolean.TRUE) {
			statues.add(CcVersion.STATUE_CLOSE);
		}
		// 获取整个列表
		Map<String, Object> returnMap = new HashMap<>();
		Page<CcVersion> majorPage = CcVersion.dao.page(pageable, majorId, statues.toArray(new Integer[statues.size()]));
		List<CcVersion> ccVersionList = majorPage.getList();
		// 判断是否分页
		if(pageable.isPaging()){
			returnMap.put("totalRow", majorPage.getTotalRow());
			returnMap.put("totalPage", majorPage.getTotalPage());
			returnMap.put("pageSize", majorPage.getPageSize());
			returnMap.put("pageNumber", majorPage.getPageNumber());
		}
		
		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (CcVersion temp : ccVersionList) {
			Map<String, Object> version = new HashMap<>();
			version.put("id", temp.get("id"));
			version.put("createDate", temp.get("create_date"));
			version.put("modifyDate", temp.get("modify_date"));
			version.put("majorId", temp.get("major_id"));
			version.put("majorName", temp.get("major_name"));
			version.put("name", temp.get("name"));
			version.put("state", temp.get("state"));
			version.put("stateName", DictUtils.findLabelByTypeAndKey("state", temp.getInt("state")));
			version.put("type", temp.get("type"));
			version.put("typeName", DictUtils.findLabelByTypeAndKey("versionType", temp.getInt("type")));
			version.put("description", temp.get("description"));
			version.put("enableGrade", temp.get("enable_grade"));
			version.put("applyGrade", temp.get("apply_grade"));
			version.put("publishDate", temp.get("publish_date"));
			version.put("planName", temp.get("planName"));
			version.put("planCourseVersionName", temp.get("planCourseVersionName"));
			version.put("graduateName", temp.get("graduateName"));
			version.put("graduateIndicationVersionName", temp.get("graduateIndicationVersionName"));
			version.put("pass", temp.get("pass"));
			version.put("remark", temp.get("remark"));
			list.add(version);
		}
		
		returnMap.put("list", list);
		
		// 结果返回
		return renderSUC(returnMap, response, header);
	}
}
