package com.gnet.certification;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.gnet.model.admin.CcMajorStudent;
import com.gnet.model.admin.CcVersion;
import com.gnet.object.CcMajorStudentOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;
/**
 * 学生专业方向列表
 * @author xzl
 * @Date 2016年12月8日
 */
@Service("EM00681")
@Transactional(readOnly=true)
public class EM00681 extends BaseApi implements IApi{

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 分页参数
		Integer pageNumber = paramsIntegerFilter(params.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(params.get("pageSize"));
		String orderProperty = paramsStringFilter(params.get("orderProperty"));
		String orderDirection = paramsStringFilter(params.get("orderDirection"));
        Long majorId = paramsLongFilter(params.get("majorId"));
		Integer grade = paramsIntegerFilter(params.get("grade"));
	    //版本内的某个年级的学生专业方向
		Long versionId = paramsLongFilter(params.get("versionId"));
		Long classId = paramsLongFilter(params.get("classId"));
		if(params.containsKey("classId") && classId == null){
			return renderFAIL("1009", response, header, "classId的参数值非法");
		}
		String name = paramsStringFilter(params.get("name"));
		String studentNo = paramsStringFilter(params.get("studentNo"));
		
		//版本外专业编号不能为空
		if(versionId == null && majorId == null){
			return renderFAIL("0130", response, header);
		}else{
			if(params.containsKey("versionId") && versionId == null){
				return renderFAIL("1009", response, header, "versionId的参数值非法");
			}
			if(params.containsKey("majorId") && majorId == null){
				return renderFAIL("1009", response, header, "majorId的参数值非法");
			}
		}
		
		if(grade == null){
			return renderFAIL("0316", response, header);
		}
		
		Long latestVersionId = versionId != null ? versionId : CcVersion.dao.findNewestVersion(majorId, grade);
		
		if(latestVersionId == null){
			return renderFAIL("0840", response, header);
		}
		
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
	
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcMajorStudentOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		Map<String, Object> result = new HashMap<String, Object>();	
		Page<CcMajorStudent> page = CcMajorStudent.dao.page(pageable, latestVersionId, grade, classId, name, studentNo);
		List<CcMajorStudent> majorStudentList = page.getList();
		//判断是否分页
		if(pageable.isPaging()){
			result.put("pageNumber", page.getPageNumber());
			result.put("pageSize", page.getPageSize());
			result.put("totalRow", page.getTotalRow());
			result.put("totalPage", page.getTotalPage());
		}
	
		//返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for(CcMajorStudent temp: majorStudentList){
			Map<String, Object> majorStudent =  Maps.newHashMap();
			majorStudent.put("studenId", temp.getLong("studenId"));
			majorStudent.put("name", temp.getStr("name"));
			majorStudent.put("studentNo", temp.getStr("studentNo"));
			majorStudent.put("sex", temp.getInt("sex"));
			majorStudent.put("sexName", DictUtils.findLabelByTypeAndKey("sex", temp.getInt("sex")));
			majorStudent.put("className", temp.getStr("className"));
			majorStudent.put("majorDirectionName", temp.getStr("majorDirectionName"));
			majorStudent.put("versionId", temp.getLong("versionId"));
			
			list.add(majorStudent);
		}
		result.put("list", list);
		
		//返回结果
		return renderSUC(result, response, header);
		
				
	}
}
