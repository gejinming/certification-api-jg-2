package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcCourse;
import com.gnet.model.admin.CcIndicationCourse;
import com.gnet.model.admin.CcMajorDirection;
import com.gnet.object.CcIndicationCourseOrderType;
import com.gnet.pager.Pageable;
import com.gnet.service.CcIndicationCourseService;
import com.gnet.utils.ParamSceneUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 建工-根据课程编号和指标点编号返回同一指标点同一课组的课程
 *
 * @author xzl
 *
 * @date 2017年11月17日17:18:19
 *
 */
@Service("EM00275")
@Transactional(readOnly=true)
public class EM00275 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {

		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();

		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		Long indicationId = paramsLongFilter(param.get("indicationId"));
		Long courseId = paramsLongFilter(param.get("courseId"));
		Long id = paramsLongFilter(param.get("id"));

		if(indicationId == null){
			return renderFAIL("0230", response, header);
		}

		if(courseId == null){
			return renderFAIL("0250", response, header);
		}

		CcCourse course = CcCourse.dao.findFilteredById(courseId);
		if(course == null){
			return renderFAIL("0251", response, header);
		}
		Long courseGroupId = course.getLong("course_group_id");

		// 进行分页
		Pageable pageable = new Pageable(pageNumber, pageSize);

		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcIndicationCourseOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}

		Map<String, Object> ccIndicationCoursesMap = Maps.newHashMap();
		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		CcIndicationCourseService ccIndicationCourseService = SpringContextHolder.getBean(CcIndicationCourseService.class);
		Long directionId = course.getLong("direction_id");

		//如果该指标点从未关联过课程
		List<CcIndicationCourse> indicationCourses = CcIndicationCourse.dao.getIndicationWeight(indicationId, courseGroupId, courseId, directionId);
		if(indicationCourses.isEmpty()){
			ccIndicationCoursesMap.put("restWeight", CcIndicationCourse.MAX_WEIGHT);
		}else{
			//指标点剩余权重
			Map<String, BigDecimal> restWeightMap = ccIndicationCourseService.getIndicationWeightRest(indicationId, courseGroupId, courseId, directionId);

			// 课程存在方向时，只返回某个方向的剩余权重值
			if (directionId != null) {
				CcMajorDirection ccMajorDirection = CcMajorDirection.dao.findById(directionId);
				String directionName = ccMajorDirection.getStr("name");
				BigDecimal directionRestWeight = restWeightMap.get(directionName) == null ? CcIndicationCourse.MAX_WEIGHT : restWeightMap.get(directionName) ;
				ccIndicationCoursesMap.put("restWeight", directionRestWeight);
			}else{
				ccIndicationCoursesMap.put("restWeight", restWeightMap.get("无方向"));
			}
		}

		if(courseGroupId != null){

			Page<CcIndicationCourse> ccIndicationCoursePage = CcIndicationCourse.dao.page(pageable, indicationId, courseGroupId);
			List<CcIndicationCourse> ccIndicationCourseList = ccIndicationCoursePage.getList();
			// 判断是否分页
			if(pageable.isPaging()){
				ccIndicationCoursesMap.put("totalRow", ccIndicationCoursePage.getTotalRow());
				ccIndicationCoursesMap.put("totalPage", ccIndicationCoursePage.getTotalPage());
				ccIndicationCoursesMap.put("pageSize", ccIndicationCoursePage.getPageSize());
				ccIndicationCoursesMap.put("pageNumber", ccIndicationCoursePage.getPageNumber());
			}

			for (CcIndicationCourse temp : ccIndicationCourseList) {
				Map<String, Object> ccIndicationCourse = new HashMap<>();
				ccIndicationCourse.put("id", temp.get("id"));
				ccIndicationCourse.put("createDate", temp.get("create_date"));
				ccIndicationCourse.put("modifyDate", temp.get("modify_date"));
				ccIndicationCourse.put("indicationId", temp.get("indication_id"));
				ccIndicationCourse.put("courseId", temp.get("course_id"));
				ccIndicationCourse.put("weight", temp.get("weight"));
				ccIndicationCourse.put("eduAim", temp.get("edu_aim"));
				ccIndicationCourse.put("indicationContent", temp.get("indicationContent"));
				ccIndicationCourse.put("courseName", temp.get("courseName"));
				ccIndicationCourse.put("means", temp.get("means"));
				ccIndicationCourse.put("source", temp.get("source"));
				ccIndicationCourse.put("way", temp.get("way"));
				list.add(ccIndicationCourse);
			}
		}

		ccIndicationCoursesMap.put("list", list);

		return renderSUC(ccIndicationCoursesMap, response, header);
	}

}
