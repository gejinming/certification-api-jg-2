package com.gnet.certification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.object.CcGraduateOrderType;
import com.gnet.pager.Pageable;
import com.gnet.service.CcIndicationCourseService;
import com.gnet.utils.ParamSceneUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看毕业要求列表
 *
 * @author SY
 *
 * @date 2016年06月24日 20:55:57
 *
 */
@Service("EM00120")
@Transactional(readOnly=true)
public class EM00120 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {

		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();

		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		Integer indexNum = paramsIntegerFilter(param.get("indexNum"));
		String content = paramsStringFilter(param.get("content"));
		Long graduateVerId = paramsLongFilter(param.get("graduateVerId"));
		Long directionId = paramsLongFilter(param.get("directionId"));
		if(param.containsKey("directionId") && directionId == null){
			return renderFAIL("1009", response, header, "directionId的参数值非法");
		}

		Long majorId = paramsLongFilter(param.get("majorId"));
		if(param.containsKey("majorId") && majorId == null){
			return renderFAIL("1009", response, header, "majorId的参数值非法");
		}
		Integer grade = paramsIntegerFilter(param.get("grade"));
		if(param.containsKey("grade") && grade == null){
			return renderFAIL("1009", response, header, "grade的参数值非法");
		}

		if(majorId != null && grade != null){
			graduateVerId = CcVersion.dao.findNewestVersion(majorId, grade);
			if (graduateVerId == null) {
				return renderFAIL("0672", response, header);
			}
		}
		if(graduateVerId == null){
			return renderFAIL("0181", response, header);
		}

		Pageable pageable = new Pageable(pageNumber, pageSize);

		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcGraduateOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}


		Map<String, Object> ccGraduatesMap = Maps.newHashMap();
		Page<CcGraduate> ccGraduatePage = CcGraduate.dao.page(pageable, graduateVerId, indexNum, content);
		List<CcGraduate> ccGraduateList = ccGraduatePage.getList();
		// 判断是否分页
		if(pageable.isPaging()){
			ccGraduatesMap.put("totalRow", ccGraduatePage.getTotalRow());
			ccGraduatesMap.put("totalPage", ccGraduatePage.getTotalPage());
			ccGraduatesMap.put("pageSize", ccGraduatePage.getPageSize());
			ccGraduatesMap.put("pageNumber", ccGraduatePage.getPageNumber());
		}

		// 设立一个map
		Map<Long, List<Map<String, Object>>> map = new HashMap<>();
		// 通过毕业要求编号获取指标点信息，之后放到他的list中
		Long[] ccGraduateIds = new Long[ccGraduateList.size()];

		for(int i = 0; i < ccGraduateList.size(); i++) {
			ccGraduateIds[i] = ccGraduateList.get(i).getLong("id");
			map.put(ccGraduateIds[i], new ArrayList<Map<String, Object>>());
		}

		if(ccGraduateIds.length != 0) {
			List<CcIndicationCourse> indicationCourseList = CcIndicationCourse.dao.findByGraduateIdsAndDirectionId(ccGraduateIds, directionId);
			CcIndicationCourseService ccIndicationCourseService = SpringContextHolder.getBean(CcIndicationCourseService.class);
			Map<Long, BigDecimal> indicationWeightMap = ccIndicationCourseService.getIndicationWeight(indicationCourseList);
			// 放到map中
			List<CcIndicatorPoint> allList = CcIndicatorPoint.dao.findOrderFilteredByColumnIn("graduate_id", ccGraduateIds);
			for(CcIndicatorPoint temp : allList) {
				Long graduateId = temp.getLong("graduate_id");
				List<Map<String, Object>> tempList = map.get(graduateId);
				Map<String, Object> tempMap = new HashMap<>();
				tempMap.put("id", temp.get("id"));
				tempMap.put("allWeight", indicationWeightMap.get(temp.getLong("id")));
				tempMap.put("content", temp.get("content"));
				tempMap.put("createDate", temp.get("create_date"));
				tempMap.put("modifyDate", temp.get("modify_date"));
				tempMap.put("graduateId", temp.get("graduate_id"));
				tempMap.put("remark", temp.get("remark"));
				tempMap.put("indexNum", temp.get("index_num"));
				tempList.add(tempMap);
				map.put(graduateId, tempList);
			}

		}


		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (CcGraduate temp : ccGraduateList) {
			Map<String, Object> tempMap = new HashMap<>();
			tempMap.put("id", temp.get("id"));
			tempMap.put("createDate", temp.get("create_date"));
			tempMap.put("modifyDate", temp.get("modify_date"));
			tempMap.put("graduateVerId", temp.get("graduate_ver_id"));
			tempMap.put("indexNum", temp.get("index_num"));
			tempMap.put("indicationList", map.get(temp.getLong("id")) == null ? new ArrayList<>() : map.get(temp.getLong("id")));
			tempMap.put("content", temp.get("content"));
			tempMap.put("remark", temp.get("remark"));
			list.add(tempMap);
		}

		ccGraduatesMap.put("list", list);

		return renderSUC(ccGraduatesMap, response, header);
	}

}
