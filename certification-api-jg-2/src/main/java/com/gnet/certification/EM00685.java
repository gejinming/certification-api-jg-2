
package com.gnet.certification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcClass;
import com.gnet.model.admin.CcVersion;
import com.gnet.object.OfficeOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

/**
 * 通过专业编号和年级显示同一版本下其它年级的行政班
 * 
 * @author xzl
 * 
 * @date 2016年12月15日15:58:43
 * 
 */
@Service("EM00685")
@Transactional(readOnly=true)
public class EM00685 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Long majorId = paramsLongFilter(param.get("majorId"));
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		Integer grade = paramsIntegerFilter(param.get("grade"));
		Pageable pageable = new Pageable(pageNumber, pageSize);
		List<String> grades = Lists.newArrayList();

		if(majorId == null){
			return renderFAIL("0130", response, header);
		}
			
		if(grade == null){
			return renderFAIL("0316", response, header);
		}
			
		CcVersion ccVersion = CcVersion.dao.findByGradeAndMajorId(majorId, grade);
		if(ccVersion == null){
			return renderFAIL("0842", response, header);
		}
        String applyGrade = ccVersion.getStr("apply_grade");
        if(StrKit.isBlank(applyGrade)){
        	return renderFAIL("0124", response, header);
        }
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, OfficeOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		Map<String, Object> classesMap = Maps.newHashMap();
		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
        Integer minApplyGrade = null;
        Page<CcClass> ccClassPage = null;
        if(applyGrade.endsWith(CcVersion.GRADE_CHARACTER)){
        	//applyGrade（适用年级）数据格式为2016+
        	minApplyGrade = Integer.valueOf(applyGrade.substring(0, applyGrade.length()-1));
        	ccClassPage = CcClass.dao.page(pageable, majorId, minApplyGrade, grade);
        }else{
        	//applyGrade（适用年级）数据格式为2011,2012 或者2011
        	grades = Arrays.asList(applyGrade.split(CcVersion.SPLIT));
        	if(grades.size() == 1){
        		classesMap.put("list", list);
        		return renderSUC(classesMap, response, header);
        	}
        	//1.字符串转换成数字 2.排除传进来的grade参数
        	Integer[] gradeArray = new Integer[grades.size()-1];
        	for(int i=0, j=0; i<grades.size(); i++){
        		Integer temp = Integer.valueOf(grades.get(i));
        		if(!grade.equals(temp)){
        			gradeArray[j] = temp;
        		    j++;
        		}
        	}
        	ccClassPage = CcClass.dao.page(pageable, majorId, gradeArray);
        }
        
		List<CcClass> ccClassList = ccClassPage.getList();
		
		// 判断是否分页
		if(pageable.isPaging()){
			classesMap.put("totalRow", ccClassPage.getTotalRow());
			classesMap.put("totalPage", ccClassPage.getTotalPage());
			classesMap.put("pageSize", ccClassPage.getPageSize());
			classesMap.put("pageNumber", ccClassPage.getPageNumber());
		}

		for (CcClass temp : ccClassList) {
			Map<String, Object> office = Maps.newHashMap();
			office.put("id", temp.get("id"));
			office.put("code", temp.get("code"));
			office.put("name", temp.get("name"));
			office.put("type", temp.get("type"));
			office.put("grade", temp.getInt("grade"));
			office.put("isSystem", temp.get("is_system"));
			office.put("description", temp.get("description"));
			office.put("majorName", temp.get("majorName"));
			office.put("classLeader", temp.get("class_leader"));
			office.put("remark", temp.get("remark"));
			
			list.add(office);
		}
		
		classesMap.put("list", list);
		
		return renderSUC(classesMap, response, header);
	}
	
}
