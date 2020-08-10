package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcCourse;
import com.gnet.model.admin.CcIndication;
import com.gnet.model.admin.CcIndicationCourse;
import com.gnet.model.admin.CcVersion;
import com.gnet.object.CcIndicationCourseOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.DateUtil;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;
import com.lowagie.text.html.HtmlTagMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 查看指标点课程关系表列表
 *
 * @author xzl
 *
 * @date 2017年11月17日16:12:52
 *
 */
@Service("EM00270")
@Transactional(readOnly=true)
public class EM00270 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {

		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();

		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		Long indicationId = paramsLongFilter(param.get("indicationId"));
		if(param.containsKey("indicationId") && indicationId == null){
			return renderFAIL("1009", response, header, "indicationId的参数值非法");
		}
		Long courseId = paramsLongFilter(param.get("courseId"));
		if(param.containsKey("courseId") && courseId == null){
			return renderFAIL("1009", response, header, "courseId的参数值非法");
		}
		String courseName = paramsStringFilter(param.get("courseName"));
		String indicationContent = paramsStringFilter(param.get("indicationContent"));
		Long planId = paramsLongFilter(param.get("planId"));
		/*if(param.containsKey("planId") && planId == null){
			return renderFAIL("1009", response, header, "planId的参数值非法");
		}*/
		Long directionId = paramsLongFilter(param.get("directionId"));
		if(param.containsKey("directionId") && directionId == null){
			return renderFAIL("1009", response, header, "directionId的参数值非法");
		}
		//没有传递directionId参数说明忽略专业方向，所以ignoreDirection为true
		Boolean ignoreDirection = !param.containsKey("directionId");

		Long majorId = paramsLongFilter(param.get("majorId"));
		Integer grade = paramsIntegerFilter(param.get("grade"));
		/*if (grade==null){
			grade=DateUtil.getYear(new Date());
		}*/
		if(planId == null){
			if(courseId != null){
				CcCourse ccCourse = CcCourse.dao.findByCourseId(courseId);
				if(ccCourse == null) {
					return renderFAIL("0140", response, header);
				}
				planId = ccCourse.getLong("plan_id");
			}
			//TODO 只要是执笔人不管是哪个学院的都可以设置课程目标 如果本专业的教师，一旦他被选择为其他专业的任课教师，也能设置其他专业这门课程目标。
			/*if(majorId != null && grade != null){
				planId = CcVersion.dao.findNewestVersion(majorId, grade);
			}
			if(planId == null) {
				return renderFAIL("0140", response, header);
			}*/
		}

		Long teacherId = paramsLongFilter(param.get("teacherId"));
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

		Map<Long, List<Map<String, Object>>> map = new HtmlTagMap();
		List<CcIndication> ccIndications = CcIndication.dao.finfByPlanId(planId);
		for(CcIndication ccIndication : ccIndications){
			Long id = ccIndication.getLong("id");
			Map<String, Object> temp = new HashMap<>();
			temp.put("sort", ccIndication.getInt("sort"));
			temp.put("content", ccIndication.getStr("content"));

			List<Map<String, Object>> list = map.get(id);
			if(list == null){
				list = new ArrayList<>();
				map.put(id, list);
			}
			list.add(temp);
		}

		Map<String, Object> ccIndicationCoursesMap = Maps.newHashMap();
		Page<CcIndicationCourse> ccIndicationCoursePage = CcIndicationCourse.dao.page(pageable, indicationId, courseId, courseName, indicationContent, planId, directionId, ignoreDirection, teacherId,grade);
		List<CcIndicationCourse> ccIndicationCourseList = ccIndicationCoursePage.getList();
		// 判断是否分页
		if(pageable.isPaging()){
			ccIndicationCoursesMap.put("totalRow", ccIndicationCoursePage.getTotalRow());
			ccIndicationCoursesMap.put("totalPage", ccIndicationCoursePage.getTotalPage());
			ccIndicationCoursesMap.put("pageSize", ccIndicationCoursePage.getPageSize());
			ccIndicationCoursesMap.put("pageNumber", ccIndicationCoursePage.getPageNumber());
		}

		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (CcIndicationCourse temp : ccIndicationCourseList) {
			Map<String, Object> ccIndicationCourse = new HashMap<>();
			Long id = temp.getLong("id");
			ccIndicationCourse.put("id", id);
			ccIndicationCourse.put("createDate", temp.get("create_date"));
			ccIndicationCourse.put("modifyDate", temp.get("modify_date"));
			ccIndicationCourse.put("indicationId", temp.get("indication_id"));
			ccIndicationCourse.put("graduateIndexNum", temp.getInt("graduateIndexNum"));
			ccIndicationCourse.put("graduateId", temp.getLong("graduateId"));
			ccIndicationCourse.put("graduateContent", temp.getStr("graduateContent"));
			ccIndicationCourse.put("courseId", temp.get("course_id"));
			ccIndicationCourse.put("weight", temp.get("weight"));
			ccIndicationCourse.put("eduAim", temp.get("edu_aim"));
			ccIndicationCourse.put("indicationContent", temp.get("indicationContent"));
			ccIndicationCourse.put("indexNum", temp.getInt("index_num"));
			ccIndicationCourse.put("courseName", temp.get("courseName"));
			ccIndicationCourse.put("means", temp.get("means"));
			ccIndicationCourse.put("source", temp.get("source"));
			ccIndicationCourse.put("way", temp.get("way"));
			ccIndicationCourse.put("state",temp.getInt("state"));
			List courseTargets = map.get(id);
			if(courseTargets!= null && !courseTargets.isEmpty()){
				ccIndicationCourse.put("courseTargets", courseTargets);
			}
			list.add(ccIndicationCourse);
		}

		ccIndicationCoursesMap.put("list", list);

		return renderSUC(ccIndicationCoursesMap, response, header);
	}

}
