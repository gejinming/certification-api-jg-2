package com.gnet.certification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.CcCourseGroupCourse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcCourse;
import com.gnet.model.admin.CcCourseGroup;
import com.gnet.object.CcCourseGroupOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 查看课程组表--限选列表  多选一 功能同一个接口根据typeId区别
 * 
 * @author SY
 * 
 * @date 2016年07月14日 11:10:53
 * 
 */
@Service("EM00620")
@Transactional(readOnly=true)
public class EM00620 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		// 这个暂时用不着，但是预留着
		String remark = paramsStringFilter(param.get("remark"));
        Long planId = paramsLongFilter(param.get("planId"));
		if(param.containsKey("planId") && planId == null){
			return renderFAIL("1009", response, header, "planId的参数值非法");
		}
		
		Integer type = paramsIntegerFilter(param.get("typeId"));
		
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
		Page<CcCourseGroup> ccCourseGroupPage = CcCourseGroup.dao.page(pageable, planId, remark, type);
		List<CcCourseGroup> ccCourseGroupList = ccCourseGroupPage.getList();
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
		Map<Long, List<CcCourse>> courseMap = new HashMap<>();
		if(!ccCourseGroupList.isEmpty()){
			Long[] courseGroupIds = new Long[ccCourseGroupList.size()];
			for (int i = 0; i < ccCourseGroupList.size(); i++) {
				CcCourseGroup temp = ccCourseGroupList.get(i);

				courseGroupIds[i] = temp.getLong("id");

			}
			// 初始化map
			for(Long id : courseGroupIds) {
				courseMap.put(id, new ArrayList<CcCourse>());
			}
			// 找到courseList--如果发现course已经被分配到一个课程组了，然后error
			List<CcCourse> ccCourseList=null;
			if (type==1){
				ccCourseList = CcCourse.dao.findFilteredByColumnIn("course_group_id", courseGroupIds);
				if(!ccCourseList.isEmpty()){
					for(CcCourse temp : ccCourseList) {
						List<CcCourse> tempList = courseMap.get(temp.getLong("course_group_id"));
						tempList.add(temp);
					}
				}
			}else {
				ccCourseList = CcCourse.dao.findFilteredByColumnIn("course_group_mange_id", courseGroupIds);
				if(!ccCourseList.isEmpty()){
					for(CcCourse temp : ccCourseList) {
						List<CcCourse> tempList = courseMap.get(temp.getLong("course_group_mange_id"));
						tempList.add(temp);
					}
				}
			}


        }
		
		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (CcCourseGroup temp : ccCourseGroupList) {
			Map<String, Object> ccCourseGroup = new HashMap<>();
			ccCourseGroup.put("id", temp.get("id"));
			ccCourseGroup.put("createDate", temp.get("create_date"));
			ccCourseGroup.put("modifyDate", temp.get("modify_date"));
			ccCourseGroup.put("credit", temp.get("credit"));
			ccCourseGroup.put("typeId", temp.get("type"));
			ccCourseGroup.put("allHours", temp.get("all_hours"));
			ccCourseGroup.put("planId", temp.get("plan_id"));
			ccCourseGroup.put("remark", temp.get("remark"));
			ccCourseGroup.put("groupName",temp.get("group_name"));
			// 获取course中的数据
			List<CcCourse> courseList = courseMap.get(temp.get("id"));
			List<CcCourse> courseListReturn = new ArrayList<>();
			if(courseList != null){
				for(CcCourse course : courseList) {
					CcCourse courseTemp = new CcCourse();
					courseTemp.set("id", course.getLong("id"));
					courseTemp.set("name", course.getStr("name"));
					courseTemp.set("code", course.getStr("code"));
					courseListReturn.add(courseTemp);
				}
			}
			ccCourseGroup.put("courseList", courseListReturn);
			list.add(ccCourseGroup);
		}
		
		ccCourseGroupsMap.put("list", list);
		
		return renderSUC(ccCourseGroupsMap, response, header);
	}
}
