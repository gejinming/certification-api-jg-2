package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.api.kit.UserCacheKit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcClass;
import com.gnet.model.admin.CcStudent;
import com.gnet.model.admin.Office;
import com.gnet.model.admin.OfficePath;
import com.gnet.service.OfficeService;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.kit.StrKit;

/**
 * 编辑行政班
 * 
 * @author sll
 * 
 * @date 2016年06月29日 17:46:25
 *
 */
@Service("EM00253")
@Transactional(readOnly=false)
public class EM00253 extends BaseApi implements IApi{
	
	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		Map<String, Object> param = request.getData();
		Map<String, Boolean> result = new HashMap<>();
		Long id = paramsLongFilter(param.get("id"));
		String name = paramsStringFilter(param.get("name"));
		String description = paramsStringFilter(param.get("description"));
		String classLeader = paramsStringFilter(param.get("classLeader"));
		String remark = paramsStringFilter(param.get("remark"));
		Long majorId = paramsLongFilter(param.get("majorId"));
		Integer grade = paramsIntegerFilter(param.get("grade"));
		
		if (id == null) {
			return renderFAIL("0294", response, header);
		}
		if (StrKit.isBlank(name)) {
			return renderFAIL("0298", response, header);
		}
		//因为编辑行政班时还能选择新专业，所以专业不能为空
		if(majorId == null){
			return renderFAIL("0296", response, header);
		}
		
		if(grade == null){
			return renderFAIL("0302", response, header);
		}
		
		CcClass ccClass = CcClass.dao.findById(id);
		if (ccClass == null) {
			return renderFAIL("0295", response, header);
		}
		if(!grade.equals(ccClass.getInt("grade")) && CcStudent.dao.countFiltered("class_id", id) > 0){
			// 当发现更改年级的时候，班级下面有学生了，则不允许修改
			return renderFAIL("0306", response, header);
		}
		
		//获取当前用户所在学校
		Long schoolId = UserCacheKit.getSchoolId(request.getHeader().getToken());
		if(schoolId == null){
			return renderFAIL("0084", response, header);
		}
		//验证同一学校行政班名称是否唯一
		if(CcClass.dao.isExistedName(schoolId, name, id)){
			return renderFAIL("0299", response, header);
		}

		Date date = new Date();
		Office office = Office.dao.findFilteredById(id);
		if(office == null) {
			return renderFAIL("0285", response, header);
		}
        
		//修改部门
		office.set("modify_date", date);
		office.set("name", name);
		office.set("description", description);
		if(!office.getLong("parentid").equals(majorId)){
			//如果行政班的专业发生变化
			office.set("parentid", majorId);
			//并且设置行政班的path
			OfficePath fatherOfficePath = OfficePath.dao.findById(majorId);
			if(fatherOfficePath == null){
				return renderFAIL("0053", response, header);
			}
			OfficePath officePath = OfficePath.dao.findById(id);
			if(officePath == null){
				return renderFAIL("0053", response, header);
			}
			officePath.set("modify_date", date);
			officePath.set("office_ids", fatherOfficePath.getStr("office_ids") + "," + id + ",");
			if(!officePath.update()){
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}
		if (!office.update()){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		ccClass.set("class_leader", classLeader);
		ccClass.set("remark", remark);
		ccClass.set("grade", grade);
		if (!ccClass.update()){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}
	
}
