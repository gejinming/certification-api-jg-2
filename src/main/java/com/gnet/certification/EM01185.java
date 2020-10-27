package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcCourse;
import com.gnet.model.admin.CcCourseGroup;
import com.gnet.model.admin.CcCourseGroupMange;
import com.gnet.object.CcCourseGroupOrderType;
import com.gnet.pager.Pageable;
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
 * 查看课程分组表--限选列表
 * 
 * @author gjm
 * 
 * @date 2020年05月28日 11:10:53
 * 
 */
@Service("EM01185")
@Transactional(readOnly=true)
public class EM01185 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		//Integer type = paramsIntegerFilter(param.get("type"));
		// 这个暂时用不着，但是预留着
		String remark = paramsStringFilter(param.get("remark"));
		Long planId = paramsLongFilter(param.get("planId"));
		if(param.containsKey("planId") && planId == null){
			return renderFAIL("1009", response, header, "planId的参数值非法");
		}
		
		Integer type = CcCourseGroupMange.TYPE_LIMITED_SELECT;
		
		// 进行分页
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcCourseGroupOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		Map<String, Object> ccCourseGroupsMap = Maps.newHashMap();
		Page<CcCourseGroupMange> ccCourseGroupPage = CcCourseGroupMange.dao.page(pageable, planId, remark, type);
		List<CcCourseGroupMange> ccCourseGroupMangeList = ccCourseGroupPage.getList();
		// 判断是否分页
		if(pageable.isPaging()){
			ccCourseGroupsMap.put("totalRow", ccCourseGroupPage.getTotalRow());
			ccCourseGroupsMap.put("totalPage", ccCourseGroupPage.getTotalPage());
			ccCourseGroupsMap.put("pageSize", ccCourseGroupPage.getPageSize());
			ccCourseGroupsMap.put("pageNumber", ccCourseGroupPage.getPageNumber());
	    }
		
		// 增加课程的一些信息
		/*
		 * 1. 获取idList
		 * 2. 获取courseList
		 * 3. 根据id放入map，等待提取
		 */
		// Map<courseGroupId, List<Ccourse>>
		Map<Long, List<CcCourse>> courseGroupMap = new HashMap<>();
		if(!ccCourseGroupMangeList.isEmpty()){
			Long[] courseGroupMangeIds = new Long[ccCourseGroupMangeList.size()];
			for (int i = 0; i < ccCourseGroupMangeList.size(); i++) {
				CcCourseGroupMange temp = ccCourseGroupMangeList.get(i);
                courseGroupMangeIds[i] = temp.getLong("id");
			}
			// 初始化map
			for(Long id : courseGroupMangeIds) {
                courseGroupMap.put(id, new ArrayList<CcCourse>());
			}
			// 找到courseList--如果发现course已经被分配到一个课程组了，然后error
			//List<CcCourseGroup> ccCoursegroupList = CcCourseGroup.dao.findFilteredByColumnIn("course_group_id", courseGroupMangeIds);
			List<CcCourse> ccCoursegroupList=CcCourse.dao.coursesLists(0);
			if(!ccCoursegroupList.isEmpty()){
				for(CcCourse temp : ccCoursegroupList) {
					Long groupMangeId = temp.getLong("course_group_mange_id");
					List<CcCourse> tempList = courseGroupMap.get(temp.getLong("course_group_mange_id"));
					if ( tempList!=null){
						tempList.add(temp);
					}


				}
			}
		}
		
		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (CcCourseGroupMange temp : ccCourseGroupMangeList) {
			Map<String, Object> ccCourseGroupMange = new HashMap<>();
            ccCourseGroupMange.put("id", temp.get("id"));
            ccCourseGroupMange.put("createDate", temp.get("create_date"));
            ccCourseGroupMange.put("modifyDate", temp.get("modify_date"));
            ccCourseGroupMange.put("credit", temp.get("credit"));
            ccCourseGroupMange.put("type", temp.get("type"));
            ccCourseGroupMange.put("allHours", temp.get("all_hours"));
            ccCourseGroupMange.put("planId", temp.get("plan_id"));
            ccCourseGroupMange.put("remark", temp.get("remark"));
            ccCourseGroupMange.put("groupName", temp.get("group_name"));
			//获取多选一分组的数据

			// 获取coursegroup中的数据
			List<CcCourse> courseGroupList = courseGroupMap.get(temp.get("id"));
			List<CcCourse> courseListReturn = new ArrayList<>();
			if(courseGroupList != null){
				for(CcCourse courseGroup : courseGroupList) {
                    CcCourse courseGroupTemp = new CcCourse();
                    courseGroupTemp.set("id", courseGroup.getLong("id"));
                    courseGroupTemp.set("name", courseGroup.getStr("name"));
                    //courseGroupTemp.set("code", courseGroup.getStr("code"));
					courseListReturn.add(courseGroupTemp);
				}
			}
            ccCourseGroupMange.put("courseList", courseListReturn);
			list.add(ccCourseGroupMange);
		}
		
		ccCourseGroupsMap.put("list", list);
		
		return renderSUC(ccCourseGroupsMap, response, header);
	}
}
