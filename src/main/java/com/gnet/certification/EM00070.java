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
import com.gnet.model.admin.CcGradecompose;
import com.gnet.object.CcGradecomposeOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看成绩组成表信息
 * 
 * @author SY
 * @Date 2016年6月20日14:09:05
 */
@Service("EM00070")
public class EM00070 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据：
		Integer pageNumber = paramsIntegerFilter(params.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(params.get("pageSize"));
		String orderProperty = paramsStringFilter(params.get("orderProperty"));
		String orderDirection = paramsStringFilter(params.get("orderDirection"));
		Long majorId = paramsLongFilter(params.get("majorId"));
		
		// majorId不能为空信息的过滤
		if (majorId == null) {
			return renderFAIL("0130", response, header);
		}
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcGradecomposeOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		
		Map<String, Object> returnMap = new HashMap<>();	
		Page<CcGradecompose> page = CcGradecompose.dao.page(pageable, majorId);
		List<CcGradecompose> ccGradecomposeList = page.getList();
		
		// 判断是否分页
		if(pageable.isPaging()){
			returnMap.put("totalRow", page.getTotalRow());
			returnMap.put("totalPage", page.getTotalPage());
			returnMap.put("pageSize", page.getPageSize());
			returnMap.put("pageNumber", page.getPageNumber());
		}

		// 返回内容过滤
		List<CcGradecompose> list = new ArrayList<>();
		for (CcGradecompose temp : ccGradecomposeList) {
			CcGradecompose gradecompose = new CcGradecompose();
			gradecompose.put("id", temp.get("id"));
			gradecompose.put("createDate", temp.get("create_date"));
			gradecompose.put("modifyDate", temp.get("modify_date"));
			gradecompose.put("majorId", temp.get("major_id"));
			gradecompose.put("name", temp.get("name"));
			gradecompose.put("remark", temp.get("remark"));
			list.add(gradecompose);
		}
		
		returnMap.put("list", list);
		
		// 结果返回
		return renderSUC(returnMap, response, header);
	}
	
}
