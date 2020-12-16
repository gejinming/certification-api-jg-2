package com.gnet.certification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.gnet.model.admin.User;
import com.gnet.object.CcTeacherCourseOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

/**
 * 教师我的课程列表
 * 
 * @author xzl
 * 
 * @date 2016年12月13日
 * 
 */
@Service("EM00684")
@Transactional(readOnly=true)
public class EM00684 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		Long termId = paramsLongFilter(param.get("termId"));
		if(param.containsKey("termId") && termId == null){
			return renderFAIL("1009", response, header, "termId的参数值非法");
		}
		String courseName = paramsStringFilter(param.get("courseName"));
		// 通过token获取用户的专业编号
		String token = request.getHeader().getToken();
        User user = UserCacheKit.getUser(token);
		if(user == null){
			return renderFAIL("0081", response, header);
		}
		
		// 返回内容过滤
		List<CcTeacherCourse> list = new ArrayList<>();
		Map<String, Object> ccTeacherCoursesMap = Maps.newHashMap();
		
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcTeacherCourseOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
				
		// 排除掉重复的课程（同一课程在不同版本中），只留下最新的
		Page<CcTeacherCourse> ccTeacherCoursePage = page(pageable, termId, courseName, user.getLong("id"));
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
			Long educlassId = temp.getLong("educlassId");
			String educlassName = temp.getStr("educlass_name");
			Long id = temp.getLong("id");
            if(map.get(id) == null){
                List<Map<String, Object>> educlasses = Lists.newArrayList();
                if(StrKit.notBlank(educlassName) && educlassId != null){
                    Map<String, Object> eduMap = Maps.newHashMap();
                    eduMap.put("id", educlassId);
                    eduMap.put("name", educlassName);
                    educlasses.add(eduMap);
                }

                Long thisCourseId = temp.getLong("course_id");
                Long thisTermId = temp.getLong("term_id");
                Integer thisGrade = temp.getInt("grade");
                CcTeacherCourse ccTeacherCourse = new CcTeacherCourse();
                ccTeacherCourse.put("id", id);
                ccTeacherCourse.put("createDate", temp.get("create_date"));
                ccTeacherCourse.put("modifyDate", temp.get("modify_date"));
                ccTeacherCourse.put("courseId", thisCourseId);
                ccTeacherCourse.put("courseName", temp.get("course_name"));
				ccTeacherCourse.put("courseType", temp.get("course_type"));
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
				ccTeacherCourse.put("enableGrade", temp.getInt("enable_grade"));
                ccTeacherCourse.put("isSharer", temp.get("is_sharer"));
                ccTeacherCourse.put("isShared", temp.get("is_shared"));
                int i = 0;
                if(temp.get("create_date") != null){
                    i++;
                }
                if(temp.get("modify_date") != null){
                    i++;
                }
                if(temp.get("course_name") != null){
                    i++;
                }
                if(temp.get("teacher_id") != null){
                    i++;
                }
                if(temp.get("term_start_year") != null){
                    i++;
                }
                if(temp.get("term_end_year") != null){
                    i++;
                }
                if(temp.get("term_num") != null){
                    i++;
                }
                if(temp.get("result_type") != null){
                    i++;
                }
                if(temp.get("plan_id") != null){
                    i++;
                }
                if(temp.get("major_name") != null){
                    i++;
                }
                if(temp.get("is_sharer") != null){
                    i++;
                }
                if(temp.get("is_shared") != null){
                    i++;
                }
                ccTeacherCourse.put("schedule", i/12*100);
                // 是否可以接收分享
                Boolean isCouldAcceptShared = sharedCourseIdAndTermIdMap.get(thisCourseId) == null ? Boolean.FALSE :
                        sharedCourseIdAndTermIdMap.get(thisCourseId).get(thisGrade) == null ? Boolean.FALSE :
                                sharedCourseIdAndTermIdMap.get(thisCourseId).get(thisGrade).contains(thisTermId) ? Boolean.TRUE : Boolean.FALSE;
                ccTeacherCourse.put("isCouldAcceptShared", isCouldAcceptShared);
                ccTeacherCourse.put("educlasses", educlasses);
                list.add(ccTeacherCourse);
                temp.put("index", list.size()-1);
                map.put(id, temp);
            }else{
            	CcTeacherCourse techerCourse = list.get(map.get(id).getInt("index"));
            	List<Map<String, Object>> educlasses = techerCourse.get("educlasses");
            	if(StrKit.notBlank(educlassName) && educlassId != null){
            		Map<String, Object> eduMap = Maps.newHashMap();
            		eduMap.put("id", educlassId);
            		eduMap.put("name", educlassName);
            		educlasses.add(eduMap);
            		techerCourse.put("educlasses", educlasses);
            	}	
            }
                
		}
		
		ccTeacherCoursesMap.put("list", list);
		
		return renderSUC(ccTeacherCoursesMap, response, header);
	}
	
	
	private Page<CcTeacherCourse> page(Pageable pageable,Long termId, String courseName, Long teacherId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder selectSql = new StringBuilder("select ctc.* ");
		selectSql.append(", ct.name as teacher_name, cc.name as course_name ");
		selectSql.append(", ctm.start_year term_start_year, ctm.end_year term_end_year, ctm.term term_num, ctm.term_type term_type");
		selectSql.append(", cc.plan_id plan_id, so.name major_name, ce.educlass_name, ce.id educlassId, cc.code, cv.major_id,cv.enable_grade,cc.course_type ");
		StringBuilder exceptSql = new StringBuilder("from " + CcTeacherCourse.dao.tableName + " ctc ");
		exceptSql.append("left join " + CcTeacher.dao.tableName + " ct on ct.id=ctc.teacher_id ");
		exceptSql.append("left join " + CcCourse.dao.tableName + " cc on cc.id=ctc.course_id ");
		exceptSql.append("left join " + CcEduclass.dao.tableName + " ce on ce.teacher_course_id = ctc.id and ( ce.is_del = ? or ce.is_del is null ) ");
		params.add(Boolean.FALSE);
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
		exceptSql.append("and ctc.teacher_id = ? ");
		params.add(teacherId);
		
		if (termId != null) {
			exceptSql.append("and ctc.term_id = ? ");
			params.add(termId);
		}
		if (StrKit.notBlank(courseName)) {
			exceptSql.append("and cc.name like '%" + StringEscapeUtils.escapeSql(courseName) + "%' ");
		}		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		} else {
			exceptSql.append("order by ctc.modify_date desc ");
		}
		
		return CcTeacherCourse.dao.paginate(pageable, selectSql.toString(), exceptSql.toString(), params.toArray());
	}

}
