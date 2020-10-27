package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcSelfreport;
import com.gnet.model.admin.CcSelfreportContent;
import com.gnet.model.admin.CcSelfreportTitleword;
import com.gnet.object.CcVersionOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;
import com.jfinal.plugin.activerecord.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自评报告列表查询
 * 
 * @author GJM
 * @Date 2020年09月8日
 */
@Service("EM01215")
public class EM01215 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		HashMap<Object, Object> result = new HashMap<>();

		Long majorId = paramsLongFilter(params.get("majorId"));

		Integer pageNumber = paramsIntegerFilter(params.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(params.get("pageSize"));
		String orderProperty = paramsStringFilter(params.get("orderProperty"));
		String orderDirection = paramsStringFilter(params.get("orderDirection"));

		// majorId不能为空信息的过滤
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

		Page<CcSelfreport> page = CcSelfreport.dao.page(pageable, majorId,null);
		List<CcSelfreport> selfreportList = page.getList();
		// 判断是否分页
		if(pageable.isPaging()){
			result.put("totalRow", page.getTotalRow());
			result.put("totalPage", page.getTotalPage());
			result.put("pageSize", page.getPageSize());
			result.put("pageNumber", page.getPageNumber());
		}
		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		//各级标题
		List<CcSelfreportTitleword> titleLevel = CcSelfreportTitleword.dao.findTitleLevel();
		for (CcSelfreport temp : selfreportList){
			Map<String, Object> ccSelfreport = new HashMap<>();
			Long selfId = temp.getLong("id");
			ccSelfreport.put("id", temp.getLong("id"));
			ccSelfreport.put("name", temp.getStr("name"));
			ccSelfreport.put("versionId", temp.get("version_id"));
			ccSelfreport.put("versionName", temp.getStr("versionName"));
			ccSelfreport.put("teacherName", temp.getStr("teacherName"));
			ccSelfreport.put("createDate", temp.get("create_date"));
			ccSelfreport.put("majorName", temp.get("majorName"));
			List<CcSelfreportContent> selfReportContentList = CcSelfreportContent.dao.findSelfReportContentList(selfId);
			if (titleLevel.size()>selfReportContentList.size()){
				ccSelfreport.put("isAchieve","否");
			}else {
				ccSelfreport.put("isAchieve","是");
			}
			list.add(ccSelfreport);
		}

		result.put("list", list);
		return renderSUC(result, response, header);
	}


}
