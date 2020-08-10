package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gnet.utils.RandomUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.Constant;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcClass;
import com.gnet.model.admin.Office;
import com.gnet.service.OfficeService;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.kit.StrKit;

/**
 * 增加行政班
 * 
 * @author sll
 * 
 * @date 2016年06月29日 17:46:25
 *
 */
@Service("EM00252")
@Transactional(readOnly=false)
public class EM00252 extends BaseApi implements IApi{

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> param = request.getData();
		Map<String, Boolean> result = new HashMap<>();
		Long majorId = paramsLongFilter(param.get("majorId"));
		String name = paramsStringFilter(param.get("name"));
		String description = paramsStringFilter(param.get("description"));
		String classLeader = paramsStringFilter(param.get("classLeader"));
		String remark = paramsStringFilter(param.get("remark"));
		Integer grade = paramsIntegerFilter(param.get("grade"));
		
		if (majorId == null) {
			return renderFAIL("0296", response, header);
		}
		if (StrKit.isBlank(name)) {
			return renderFAIL("0298", response, header);
		}
		
		if(grade == null){
			return renderFAIL("0302", response, header);
		}
		
		//获取当前用户所在学校
		OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
		List<Long> officeIdList = officeService.getPathByOfficeId(majorId);
		if(officeIdList.isEmpty()){
			return renderFAIL("0390", response, header);
		}
		Long schoolId = officeIdList.get(0);
		
		//验证同一学校行政班名称是否唯一
		if(CcClass.dao.isExistedName(schoolId, name, null)){
			return renderFAIL("0299", response, header);
		}
		
		Date date = new Date();
		
		CcClass ccClass = new CcClass();
		Office office = new Office();
		
		
		//增加行政班
		ccClass.set("create_date", date);
		ccClass.set("modify_date", date);
		ccClass.set("class_leader", classLeader);
		ccClass.set("remark", remark);
		ccClass.set("grade", grade);
		ccClass.set(CcClass.IS_DEL_LABEL, CcClass.DEL_NO);
		if (!ccClass.save()) {
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		Long classId = ccClass.getLong("id");
		
		//增加部门
		office.set("id", classId);
		office.set("create_date", date);
		office.set("modify_date", date);
		office.set("parentid", majorId);
		office.set("name", name);
		office.set("description", description);
		office.set("type", Office.TYPE_CLAZZ);
		office.set("code", RandomUtils.randomString(Office.TYPE_CLAZZ));
		office.set("is_system", Constant.NOTSYSTEM);
		office.set(Office.IS_DEL_LABEL, Office.DEL_NO);
		
		//保存office和officepath，save方法里面已经有手动回滚了
		if (!officeService.save(office, majorId)) {
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
		
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}
}
