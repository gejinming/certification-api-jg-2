package com.gnet.certification;

import java.util.List;
import java.util.Map;

import com.gnet.model.admin.CcTeacherCourse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcEduclass;
import com.gnet.model.admin.CcStudent;
import com.gnet.object.CcStudentOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 通过行政班编号和教学班编号获取不在相同课程、相同学期排课课程教学班中的指定行政班的学生
 * 
 * @author xzl
 * @Date 2016年12月15日
 */
@Transactional(readOnly = false)
@Service("EM00686")
public class EM00686 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		
		// 行政班编号
		Long classId = paramsLongFilter(param.get("classId"));
		// 教学班编号
		Long eduClassId = paramsLongFilter(param.get("eduClassId"));
		
		if (eduClassId == null) {
			return renderFAIL("0380", response, header);
		}
		
		if (classId == null) {
			return renderFAIL("0294", response, header);
		}


		List<CcTeacherCourse> educlassList = CcTeacherCourse.dao.findListCourseByClassId(eduClassId);
		Long[] educlassIds = new Long[educlassList.size()];
		for(int i=0; i<educlassList.size(); i++){
			educlassIds[i] = educlassList.get(i).getLong("id");
		}

		// 分页参数
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcStudentOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		// 查询并返回结果
		Page<CcStudent> page = CcStudent.dao.pageInClassIdNotSameTermTeacherCourse(pageable, classId, educlassIds);
		List<CcStudent> studentList = page.getList();
		Map<String, Object> result = Maps.newHashMap();
		
		if(pageable.isPaging()) {
			// 分页信息返回
			result.put("pageNumber", page.getPageNumber());
			result.put("pageSize", page.getPageSize());
			result.put("totalRow", page.getTotalRow());
			result.put("totalPage", page.getTotalPage());
		}
		
		// 结果参数过滤
		List<Map<String, Object>> list = Lists.newArrayList();
		for (CcStudent ccStudent : studentList) {
			Map<String, Object> object = Maps.newHashMap();
			object.put("id", ccStudent.getLong("id"));
			object.put("name", ccStudent.getStr("name"));
			object.put("createDate", ccStudent.getDate("create_date"));
			object.put("modifyDate", ccStudent.getDate("modify_date"));
			object.put("studentNo", ccStudent.getStr("student_no"));
			object.put("grade", ccStudent.getInt("grade"));
			object.put("sex", ccStudent.getInt("sex"));
			object.put("sexName", DictUtils.findLabelByTypeAndKey("sex", ccStudent.getInt("sex")));
			object.put("idCard", ccStudent.getStr("id_card"));
			object.put("birthday", ccStudent.getDate("birthday"));
			object.put("address", ccStudent.getStr("address"));
			object.put("domitory", ccStudent.getStr("domitory"));
			if(ccStudent.getInt("statue") != null){
				object.put("statue", ccStudent.getInt("statue"));
				object.put("statueName", DictUtils.findLabelByTypeAndKey("studentStatue", ccStudent.getInt("statue")));
			}
			object.put("politics", ccStudent.getStr("politics"));
			object.put("nativePlace", ccStudent.getStr("native_place"));
			object.put("country", ccStudent.getStr("country"));
			object.put("nation", ccStudent.getStr("nation"));
			object.put("mobilePhone", ccStudent.getStr("mobile_phone"));
			object.put("mobilePhoneSec", ccStudent.getStr("mobile_phone_sec"));
			object.put("qq", ccStudent.getStr("qq"));
			object.put("wechat", ccStudent.getStr("wechat"));
			object.put("email", ccStudent.getStr("email"));
			object.put("matriculateDate", ccStudent.getDate("matriculate_date"));
			object.put("graduateDate", ccStudent.getDate("graduate_date"));
			object.put("personal", ccStudent.getStr("personal"));
			object.put("highestEducation", ccStudent.getStr("highest_education"));
			list.add(object);
		}
		result.put("list", list);
		
		return renderSUC(result, response, header);
	}
	
}
