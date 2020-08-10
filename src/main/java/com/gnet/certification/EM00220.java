package com.gnet.certification;

import java.util.*;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
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
import com.gnet.model.admin.CcCourse;
import com.gnet.model.admin.CcEduclass;
import com.gnet.model.admin.CcTeacher;
import com.gnet.model.admin.CcTeacherCourse;
import com.gnet.model.admin.CcTerm;
import com.gnet.model.admin.CcVersion;
import com.gnet.model.admin.Office;
import com.gnet.object.CcTeacherCourseOrderType;
import com.gnet.pager.Pageable;
import com.gnet.service.OfficeService;
import com.gnet.utils.CollectionKit;
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看教师开课课程列表
 * 
 * @author SY
 * 
 * @date 2016年06月29日 14:41:41
 * 
 */
@Service("EM00220")
@Transactional(readOnly=true)
public class EM00220 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		Long courseId = paramsLongFilter(param.get("courseId"));
		if(param.containsKey("courseId") && courseId == null){
			return renderFAIL("1009", response, header, "courseId的参数值非法");
		}
		Long teacherId = paramsLongFilter(param.get("teacherId"));
		if(param.containsKey("teacherId") && teacherId == null){
			return renderFAIL("1009", response, header, "teacherId的参数值非法");
		}
		Long termId = paramsLongFilter(param.get("termId"));
		if(param.containsKey("termId") && termId == null){
			return renderFAIL("1009", response, header, "termId的参数值非法");
		}
		Long planId = paramsLongFilter(param.get("planId"));
		if(param.containsKey("planId") && planId == null){
			return renderFAIL("1009", response, header, "planId的参数值非法");
		}
		// 专业编号
		Long majorId = paramsLongFilter(param.get("majorId"));
		if(param.containsKey("majorId") && majorId == null){
			return renderFAIL("1009", response, header, "majorId的参数值非法");
		}
		// 年级
		Integer grade = paramsIntegerFilter(param.get("grade"));
		if(param.containsKey("grade") && grade == null){
			return renderFAIL("1009", response, header, "grade的参数值非法");
		}
		String teacherName = paramsStringFilter(param.get("teacherName"));
		String courseName = paramsStringFilter(param.get("courseName"));
		OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);

		// 通过token获取用户的专业编号
		String token = request.getHeader().getToken();
		Office departmentOffice = UserCacheKit.getDepartmentOffice(token);
		Long[] majorIds = officeService.getMajorIdsByOffice(departmentOffice);
		// 返回内容过滤
		List<CcTeacherCourse> list = new ArrayList<>();
		Map<String, Object> ccTeacherCoursesMap = Maps.newHashMap();
		if(majorIds == null || majorIds.length == 0){
			ccTeacherCoursesMap.put("list", list);
			return renderSUC(ccTeacherCoursesMap, response, header);
		}	
		//如果查询专业不属于用户负责的专业，则不进行查询
		if(majorId != null){
			if(!Arrays.asList(majorIds).contains(majorId)){
				return renderFAIL("0771", response, header);
			}
		}
		
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcTeacherCourseOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		if (planId == null && majorId != null && grade != null) {
			planId = CcVersion.dao.findNewestVersion(majorId, grade);
		}

		//某个版本下教师开课下的班级列表
		Map<Long, List<Map<String, Object>>> educlassMap = new HashMap<>();
		List<CcEduclass> ccEduclassList= CcEduclass.dao.findByPlanId(majorId, majorIds);
        for(CcEduclass ccEduclass : ccEduclassList){
			Long teacherCourseId = ccEduclass.getLong("teacherCourseId");
			Map<String, Object> map = new HashMap<>();
			map.put("id", ccEduclass.getLong("id"));
			map.put("name", ccEduclass.getStr("educlass_name"));

            List<Map<String, Object>> ccEduclasses = educlassMap.get(teacherCourseId);
            if(ccEduclasses == null){
            	ccEduclasses = new ArrayList<>();
				educlassMap.put(teacherCourseId, ccEduclasses);
			}
			ccEduclasses.add(map);
		}

		// 排除掉重复的课程（同一课程在不同版本中），只留下最新的
		Page<CcTeacherCourse> ccTeacherCoursePage = page(pageable, courseId, teacherId, termId, planId, teacherName, courseName, majorId, grade, majorIds);
		List<CcTeacherCourse> ccTeacherCourseList = ccTeacherCoursePage.getList();
		// 判断是否分页
		if (pageable.isPaging()) {
			ccTeacherCoursesMap.put("totalRow", ccTeacherCoursePage.getTotalRow());
			ccTeacherCoursesMap.put("totalPage", ccTeacherCoursePage.getTotalPage());
			ccTeacherCoursesMap.put("pageSize", ccTeacherCoursePage.getPageSize());
			ccTeacherCoursesMap.put("pageNumber", ccTeacherCoursePage.getPageNumber());
		}
		
		// 获取所有课程的分享者map, 是分享者，并且已经分享的
		Map<String, Object> paras = new HashMap<>();
		paras.put("is_sharer", Boolean.TRUE);
		paras.put("is_shared", Boolean.TRUE);
		List<CcTeacherCourse> sharedTeacherCourse = CcTeacherCourse.dao.findFilteredByColumn(paras);
		// Map<courseId, Map<grade, List<termId>>>
		Map<Long , Map<Integer, List<Long>>> sharedCourseIdAndTermIdMap = Maps.newHashMap();
		for(CcTeacherCourse temp : sharedTeacherCourse) {
			Long thisCourseId = temp.getLong("course_id");
			Long thisTermeId = temp.getLong("term_id");
			Integer thisGrade = temp.getInt("grade");
			Map<Integer, List<Long>> thisGradeAndTermIdMap = sharedCourseIdAndTermIdMap.get(thisCourseId);
			if(thisGradeAndTermIdMap == null) {
				thisGradeAndTermIdMap = new HashMap<>();
				sharedCourseIdAndTermIdMap.put(thisCourseId, thisGradeAndTermIdMap);
			}
			List<Long> thisTermIdList = thisGradeAndTermIdMap.get(thisGrade);
			if(thisTermIdList == null) {
				thisTermIdList = new ArrayList<>();
				thisGradeAndTermIdMap.put(thisGrade, thisTermIdList);
			}
			thisTermIdList.add(thisTermeId);
		}
		
		Map<Long, CcTeacherCourse> map = Maps.newHashMap();
		for (CcTeacherCourse temp : ccTeacherCourseList) {
			CcTeacherCourse ccTeacherCourse = new CcTeacherCourse();
			Long thisCourseId = temp.getLong("course_id");
			Long thisTermId = temp.getLong("term_id");
			Integer thisGrade = temp.getInt("grade");
			Long id = temp.getLong("id");
			ccTeacherCourse.put("id", id);
			ccTeacherCourse.put("createDate", temp.get("create_date"));
			ccTeacherCourse.put("modifyDate", temp.get("modify_date"));
			ccTeacherCourse.put("courseId", thisCourseId);
			ccTeacherCourse.put("courseName", temp.get("course_name"));
			ccTeacherCourse.put("teacherId", temp.get("teacher_id"));
			ccTeacherCourse.put("teacherName", temp.get("teacher_name"));
			ccTeacherCourse.put("termId", thisTermId);
			ccTeacherCourse.put("termStartYear", temp.get("term_start_year"));
			ccTeacherCourse.put("termEndYear", temp.get("term_end_year"));
			ccTeacherCourse.put("termNum", temp.get("term_num"));
			ccTeacherCourse.put("termTypeName", DictUtils.findLabelByTypeAndKey("termType", temp.getInt("term_type")));
			ccTeacherCourse.put("resultType", temp.get("result_type"));
			ccTeacherCourse.put("resultTypeName", DictUtils.findLabelByTypeAndKey("courseResultType", temp.getInt("result_type")));
			ccTeacherCourse.put("planId", temp.get("plan_id"));
			ccTeacherCourse.put("majorName", temp.get("major_name"));
			ccTeacherCourse.put("grade", thisGrade);
			ccTeacherCourse.put("isSharer", temp.get("is_sharer"));
			ccTeacherCourse.put("isShared", temp.get("is_shared"));
			// 是否可以接收分享
			Boolean isCouldAcceptShared = sharedCourseIdAndTermIdMap.get(thisCourseId) == null ? Boolean.FALSE : 
				sharedCourseIdAndTermIdMap.get(thisCourseId).get(thisGrade) == null ? Boolean.FALSE : 
					sharedCourseIdAndTermIdMap.get(thisCourseId).get(thisGrade).contains(thisTermId) ? Boolean.TRUE : Boolean.FALSE;
			ccTeacherCourse.put("isCouldAcceptShared", isCouldAcceptShared);
			ccTeacherCourse.put("educlasses", educlassMap.get(id));
			list.add(ccTeacherCourse);

		}
		
		ccTeacherCoursesMap.put("list", list);
		
		return renderSUC(ccTeacherCoursesMap, response, header);
	}
	
	/**
	 * 查看教师开课课程列表分页
	 * 
	 * @param courseId
	 * @param teacherId
	 * @param termId
	 * @param courseName 
	 * @param teacherName 
	 * @param planId 
	 * @param grade 
	 * @param majorId 
	 * @param majorIds 
	 * @return
	 */
	private Page<CcTeacherCourse> page(Pageable pageable, Long courseId, Long teacherId, Long termId, Long planId, String teacherName, String courseName, Long majorId, Integer grade, Long[] majorIds) {
		List<Object> params = Lists.newArrayList();
		StringBuilder selectSql = new StringBuilder("select ctc.* ");
		selectSql.append(", ct.name as teacher_name, cc.name as course_name ");
		selectSql.append(", ctm.start_year term_start_year, ctm.end_year term_end_year, ctm.term term_num, ctm.term_type term_type");
		selectSql.append(", cc.plan_id plan_id, so.name major_name, cc.code, cv.major_id ");
		StringBuilder exceptSql = new StringBuilder("from " + CcTeacherCourse.dao.tableName + " ctc ");
		exceptSql.append("inner join " + CcTeacher.dao.tableName + " ct on ct.id=ctc.teacher_id ");
		exceptSql.append("inner join " + CcCourse.dao.tableName + " cc on cc.id=ctc.course_id ");
		exceptSql.append("inner join " + CcVersion.dao.tableName + " cv on cv.id = cc.plan_id ");
		exceptSql.append("inner join " + Office.dao.tableName + " so on so.id = cv.major_id ");
		exceptSql.append("left join " + CcTerm.dao.tableName + " ctm on ctm.id=ctc.term_id ");
		
		exceptSql.append("where ctc.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cc.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and ct.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and ctm.is_del = ? ");
		params.add(Boolean.FALSE);
		
		// pcf-start：这里的代码是为了在创建修订版本以后，对于重复的教师课程，只取最新的
		exceptSql.append("and cv.minor_version = cv.max_minor_version ");
		// pcf-end
		if (majorId != null) {
			exceptSql.append("and cv.major_id = ? ");
			params.add(majorId);
		}else{
			exceptSql.append("and cv.major_id in (" + CollectionKit.convert(majorIds, ",") + ") ");
		}
		
		if(grade != null){
			exceptSql.append("and ctc.grade = ? ");
			params.add(grade);
		}
		
		// 删选条件
		if (courseId != null) {
			exceptSql.append("and ctc.course_id = ? ");
			params.add(courseId);
		}
		
		if (planId != null) {
			exceptSql.append("and cc.plan_id = ? ");
			params.add(planId);
		}
		
		if (teacherId != null) {
			exceptSql.append("and ctc.teacher_id = ? ");
			params.add(teacherId);
		}
		if (termId != null) {
			exceptSql.append("and ctc.term_id = ? ");
			params.add(termId);
		}
		if (StrKit.notBlank(courseName)) {
			exceptSql.append("and cc.name like '" + StringEscapeUtils.escapeSql(courseName) + "%' ");
		}
		if (StrKit.notBlank(teacherName)) {
			exceptSql.append("and ct.name like '" + StringEscapeUtils.escapeSql(teacherName) + "%' ");
		}
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		} else {
			exceptSql.append("order by ctc.modify_date desc ");
		}
		
		return CcTeacherCourse.dao.paginate(pageable, selectSql.toString(), exceptSql.toString(), params.toArray());
	}

}
