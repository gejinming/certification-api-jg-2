package com.gnet.certification;

import java.util.ArrayList;
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
import com.gnet.model.admin.CcStudentTransfer;
import com.gnet.object.CcStudentTransferOrderType;
import com.gnet.pager.Pageable;
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;

/**
 * 某专业近几年转入（转出）学生情况列表
 * 
 * @author XZL
 * 
 * @date 2016年7月27日
 * 
 */
@Service("EM00633")
@Transactional(readOnly=true)
public class EM00633 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		
		Integer pageNumber = paramsIntegerFilter(param.get("pageNumber"));
		Integer pageSize = paramsIntegerFilter(param.get("pageSize"));
		String orderProperty = paramsStringFilter(param.get("orderProperty"));
		String orderDirection = paramsStringFilter(param.get("orderDirection"));
		
		Long majorId = paramsLongFilter(param.get("majorId"));
		//类型，1：转入，2：转出
		Integer type = paramsIntegerFilter(param.get("type"));
		//查询年份有2个参数,默认情况第一个参数比较小，第二个参数比较大。
		Integer startYear = paramsIntegerFilter(param.get("startYear"));
		if(param.containsKey("startYear") && startYear == null){
			return renderFAIL("1009", response, header, "startYear的参数值非法");
		}
		Integer endYear = paramsIntegerFilter(param.get("endYear"));
		if(param.containsKey("endYear") && endYear == null){
			return renderFAIL("1009", response, header, "endYear的参数值非法");
		}
		
		if(majorId == null){
			return renderFAIL("0640", response, header);
		}
		 
		if(type == null){
			return renderFAIL("0643", response, header);
		}
		
		if(startYear != null && endYear != null){
			if(startYear > endYear){
				return renderFAIL("0650", response, header);
			}
		}
		
		// 进行分页
		Pageable pageable = new Pageable(pageNumber, pageSize);
		
		// 排序处理
		try {
			ParamSceneUtils.toOrder(pageable, orderProperty, orderDirection, CcStudentTransferOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return renderFAIL("0085", response, header);
		} catch (NotFoundOrderDirectionException e) {
			return renderFAIL("0086", response, header);
		}
		
		Map<String, Object> map = Maps.newHashMap();
		Page<CcStudentTransfer> page = CcStudentTransfer.dao.page(pageable, majorId, type, startYear, endYear);
		List<CcStudentTransfer> studentTransferList = page.getList();
		// 判断是否分页
		if(pageable.isPaging()){
			map.put("totalRow", page.getTotalRow());
			map.put("totalPage", page.getTotalPage());
			map.put("pageSize", page.getPageSize());
			map.put("pageNumber", page.getPageNumber());
	    }
		
		// 返回内容过滤
		List<Map<String, Object>> list = new ArrayList<>();
		for (CcStudentTransfer temp : studentTransferList) {
			Map<String, Object> studentTransfer =  Maps.newHashMap();
			studentTransfer.put("id", temp.getLong("id"));
			studentTransfer.put("createDate", temp.getDate("create_date"));
			studentTransfer.put("modifyDate", temp.getDate("modify_date"));
			studentTransfer.put("grade", temp.getInt("grade"));
			studentTransfer.put("majorId", temp.getLong("major_id"));
			studentTransfer.put("year", temp.getInt("year"));
			studentTransfer.put("studentNo", temp.getStr("student_no"));
			studentTransfer.put("studentName", temp.getStr("student_name"));
			studentTransfer.put("student_sex", temp.getInt("student_sex"));
			studentTransfer.put("studentSexName", DictUtils.findLabelByTypeAndKey("sex", temp.getInt("student_sex")));
			studentTransfer.put("type", temp.getInt("type"));
			studentTransfer.put("transferInMajorName", temp.getStr("transfer_in_major_name"));
			studentTransfer.put("transferOutMajorName", temp.getStr("transfer_out_major_name"));
			studentTransfer.put("remark", temp.getStr("remark"));
			
			list.add(studentTransfer);
		}
		
		map.put("list", list);
		
		return renderSUC(map, response, header);
	}
}
