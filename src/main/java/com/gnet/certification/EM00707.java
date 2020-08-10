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
import com.gnet.model.admin.CcCourseOutlineType;
import com.gnet.object.CcCourseOutlineTypeOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 编辑教学大纲类型列表
 * 
 * @author xzl
 * 
 * @date 2017-08-22 19:44:18
 *
 */
@Service("EM00707")
@Transactional(readOnly=false)
public class EM00707 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();

		String name = paramsStringFilter(param.get("name"));
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		Long courseId = paramsLongFilter(param.get("courseId"));
		Long schoolId = UserCacheKit.getSchoolId(request.getHeader().getToken());

		if(schoolId == null){
			return renderFAIL("0084", response, header);
		}

		Map<Long, String> statusMap = new HashMap<>();
		Map<Long, Boolean> outTypeMap = new HashMap<>();
		List<Long> outlineTypeIds = Lists.newArrayList();
        if(courseId != null){
			List<CcCourseOutline> ccCourseOutlineList =  CcCourseOutline.dao.findListByCourseId(courseId, null, null, false);
			for(CcCourseOutline courseOutline : ccCourseOutlineList){
				Long outlineTypeId = courseOutline.getLong("outline_type_id");
				Integer status = courseOutline.getInt("status");
				outlineTypeIds.add(outlineTypeId);
				outTypeMap.put(outlineTypeId,  status < CcCourseOutline.STATUS_NOT_SUBMIT);
				statusMap.put(outlineTypeId, DictUtils.findLabelByTypeAndKey("courseOutlineStatus", status));
			}
		}


		// 进行分页
		Pageable pageable = new Pageable(pageNumber, pageSize);

		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcCourseOutlineTypeOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}

		Map<String, Object> map = new HashMap<>();
		Page<CcCourseOutlineType> ccCourseOutlineTypePage = CcCourseOutlineType.dao.page(pageable, name, schoolId);
		// 判断是否分页
		if(pageable.isPaging()){
			map.put("totalRow", ccCourseOutlineTypePage.getTotalRow());

			map.put("totalPage", ccCourseOutlineTypePage.getTotalPage());
			map.put("pageSize", ccCourseOutlineTypePage.getPageSize());
			map.put("pageNumber", ccCourseOutlineTypePage.getPageNumber());
		}

		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for(CcCourseOutlineType temp : ccCourseOutlineTypePage.getList()){
			Map<String, Object> ccCourseOutlineType = new HashMap<>();
			Long id = temp.getLong("id");
			Boolean canEditOutline = outTypeMap.get(id) == null || outTypeMap.get(id);
			Boolean isExistOutline = outlineTypeIds.contains(temp.getLong("id"));
			String statusName = statusMap.get(id) != null ? statusMap.get(id) : isExistOutline ? "已编写" : null;
			ccCourseOutlineType.put("id", id);
			ccCourseOutlineType.put("createDate", temp.getDate("create_date"));
			ccCourseOutlineType.put("modify_date", temp.getDate("modify_date"));
			ccCourseOutlineType.put("name", temp.getStr("name"));
			ccCourseOutlineType.put("canOperate", !temp.getStr("name").equals(CcCourseOutlineType.NAME));
			ccCourseOutlineType.put("isExistOutline", isExistOutline);
			ccCourseOutlineType.put("canEditOutline", canEditOutline);
			ccCourseOutlineType.put("statusName", statusName);
			list.add(ccCourseOutlineType);
		}

        map.put("list", list);
		return renderSUC(map, response, header);
	}
}
