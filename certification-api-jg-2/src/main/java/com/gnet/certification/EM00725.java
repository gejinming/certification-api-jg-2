package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcCourseOutline;
import com.gnet.model.admin.CcVersion;
import com.gnet.model.admin.User;
import com.gnet.object.CcCourseOrderType;
import com.gnet.object.CcCourseOutlineTypeOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程大纲列表
 * @author xzl
 * @date 2017年9月28日
 */
@Service("EM00725")
@Transactional(readOnly=true)
public class EM00725 extends BaseApi implements IApi{

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 分页参数
		Integer pageNumber = paramsIntegerFilter(params.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(params.get("pageSize"));
		String orderProperty = paramsStringFilter(params.get("orderProperty"));
		String orderDirection = paramsStringFilter(params.get("orderDirection"));
		//课程名称
		String courseName = paramsStringFilter(params.get("courseName"));
		//大纲名称
		String name = paramsStringFilter(params.get("name"));
		Integer status = paramsIntegerFilter(params.get("status"));
		Long planId = paramsLongFilter(params.get("planId"));
		Long majorId = paramsLongFilter(params.get("majorId"));
		Integer grade = paramsIntegerFilter(params.get("grade"));
		if(params.containsKey("planId") && planId == null) {
		    return renderFAIL("1009", response, header, "planId的参数值非法");
		}

		if(params.containsKey("majorId") && majorId == null) {
			return renderFAIL("1009", response, header, "majorId的参数值非法");
		}
		if(params.containsKey("grade") && grade == null) {
			return renderFAIL("1009", response, header, "grade的参数值非法");
		}

		if(majorId != null && grade != null && planId == null){
			planId = CcVersion.dao.findNewestVersion(majorId, grade);
		}

		if(planId == null){
			return renderFAIL("0140", response, header);
		}


		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcCourseOutlineTypeOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		//查询并返回结果		
		Map<String, Object> result = new HashMap<>();
		Page<CcCourseOutline> page = CcCourseOutline.dao.pageByPlanId(pageable, planId, courseName, name, status);

		List<CcCourseOutline> ccCourseOutlineList = page.getList();
		//判断是否分页
		if(pageable.isPaging()){
			// 分页信息返回
			result.put("pageNumber", page.getPageNumber());
			result.put("pageSize", page.getPageSize());
			result.put("totalRow", page.getTotalRow());
			result.put("totalPage", page.getTotalPage());
		}
		
		//返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for(CcCourseOutline temp: ccCourseOutlineList){
			Map<String, Object> ccCourseOutline = Maps.newHashMap();
			ccCourseOutline.put("id", temp.getLong("id"));
			ccCourseOutline.put("majorName", temp.getStr("majorName"));
			ccCourseOutline.put("code", temp.getStr("code"));
			ccCourseOutline.put("courseName", temp.getStr("courseName"));
			ccCourseOutline.put("name", temp.getStr("name"));
            ccCourseOutline.put("outlineTypeName", temp.getStr("outlineTypeName"));
            ccCourseOutline.put("authorName", temp.getStr("authorName"));
            ccCourseOutline.put("auditorName", temp.getStr("auditorName"));
            ccCourseOutline.put("status", DictUtils.findLabelByTypeAndKey("courseOutlineStatus", temp.getInt("status")));
			ccCourseOutline.put("canSetPendingAudit", CcCourseOutline.STATUS_AUDIT_PASS.equals(temp.getInt("status")));
			list.add(ccCourseOutline);
		}
		
		result.put("list", list);

		//返回结果
		return renderSUC(result, response, header);

	}

}
