package com.gnet.certification;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
import com.gnet.model.admin.CcStudent;
import com.gnet.model.admin.Office;
import com.gnet.object.CcStudentOrderType;
import com.gnet.pager.Pageable;
import com.gnet.service.OfficeService;
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 学生信息的列表
 * 
 * @author wct
 * @Date 2016年6月29日
 */
@Transactional(readOnly = false)
@Service("EM00200")
public class EM00200 extends BaseApi implements IApi {
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 学生姓名
		String name = paramsStringFilter(params.get("name"));
		// 学生学号
		String studentNo = paramsStringFilter(params.get("studentNo"));
		// 学生身份证号
		String idCard = paramsStringFilter(params.get("idCard"));
		// 学生所属行政班名称
		String className = paramsStringFilter(params.get("className"));
		// 学生的学籍状态
		Integer statue = paramsIntegerFilter(params.get("statue"));
		
		Long majorId = paramsLongFilter(params.get("majorId"));
		if(params.containsKey("majorId") && majorId == null){
			return renderFAIL("1009", response, header, "majorId的参数值非法");
		}	
		Integer grade  = paramsIntegerFilter(params.get("grade"));
		
		Long classId = paramsLongFilter(params.get("classId"));
		// 分页参数
		Integer pageNumber = paramsIntegerFilter(params.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(params.get("pageSize"));
		String orderProperty = paramsStringFilter(params.get("orderProperty"));
		String orderDirection = paramsStringFilter(params.get("orderDirection"));
		String token = request.getHeader().getToken();
		OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
		Office departmentOffice = UserCacheKit.getDepartmentOffice(token);
		Long[] majorIds = officeService.getMajorIdsByOffice(departmentOffice);
		if((params.containsKey("majorId") || (majorIds == null || majorIds.length == 0)) && majorId == null){
			return renderFAIL("1009", response, header, "majorId的参数值非法");
		}
		if(majorIds != null && majorIds.length > 0 && majorId != null){
			if(!Arrays.asList(majorIds).contains(majorId)){
				return renderFAIL("0771", response, header);
			}		
		}
		
		// 进行分页
		Pageable pageable = new Pageable(pageNumber, pageSize);
		// 查询并返回结果
		Map<String, Object> result = Maps.newHashMap();
			
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcStudentOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		Page<CcStudent> page = CcStudent.dao.page(pageable, name, studentNo, idCard, className, statue, majorIds, majorId, grade, classId);
		List<CcStudent> studentList = page.getList();
	
		//是否分页
		if(pageable.isPaging()){
			result.put("pageNumber", page.getPageNumber());
			result.put("pageSize", page.getPageSize());
			result.put("totalRow", page.getTotalRow());
			result.put("totalPage", page.getTotalPage());
		}
		
		// 结果参数过滤
		List<Map<String, Object>> list = Lists.newArrayList();
		for (CcStudent ccStudent : studentList) {
			Map<String, Object> object = Maps.newHashMap();
			if (ccStudent.getInt("statue") != null) {
				object.put("statueName", DictUtils.findLabelByTypeAndKey("studentStatue", ccStudent.getInt("statue")));
			}
			object.put("id", ccStudent.getLong("id"));
			object.put("createDate", ccStudent.getDate("create_date"));
			object.put("studentNo", ccStudent.getStr("student_no"));
			object.put("name", ccStudent.getStr("name"));
			object.put("sex", DictUtils.findLabelByTypeAndKey("sex", ccStudent.getInt("sex")));
			object.put("idCard", ccStudent.getStr("id_card"));
			object.put("birthday", ccStudent.getDate("birthday"));
			object.put("address", ccStudent.getStr("address"));
			object.put("domitory", ccStudent.getStr("domitory"));
			object.put("statueName", DictUtils.findLabelByTypeAndKey("studentStatue", ccStudent.getInt("statue")));
			object.put("politics", ccStudent.getStr("politics"));
			object.put("nativePlace", ccStudent.getStr("native_place"));
			object.put("country", ccStudent.getStr("country"));
			object.put("nation", ccStudent.getStr("nation"));
			object.put("mobilePhone", ccStudent.getStr("mobile_phone"));
			object.put("mobilePhone2", ccStudent.getStr("mobile_phone_sec"));
			object.put("qq", ccStudent.getStr("qq"));
			object.put("wechat", ccStudent.getStr("wechat"));
			object.put("email", ccStudent.getStr("email"));
			object.put("matriculateDate", ccStudent.getDate("matriculate_date"));
			object.put("graduateDate", ccStudent.getDate("graduate_date"));
			object.put("className", ccStudent.getStr("className"));
			object.put("personal", ccStudent.getStr("personal"));
			object.put("highestEducation", ccStudent.getStr("highest_education"));
			object.put("majorId", ccStudent.getLong("major_id"));
			object.put("majorName", ccStudent.getStr("major_name"));
			object.put("grade", ccStudent.getInt("grade"));
			object.put("remark", ccStudent.getStr("remark"));
			list.add(object);
		}
		result.put("list", list);
		
		return renderSUC(result, response, header);
	}
	
}
