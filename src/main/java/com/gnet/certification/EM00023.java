package com.gnet.certification;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import com.gnet.Constant;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.Office;
import com.gnet.model.admin.OfficePath;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.CollectionKit;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;

/**
 * 增加系统部门的接口
 * 
 * @author wct
 * @Date 2016年6月5日
 */
@Service("EM00023")
public class EM00023 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		String code = paramsStringFilter(params.get("code"));
		String name = paramsStringFilter(params.get("name"));
		Integer type = paramsIntegerFilter(params.get("type"));
		String description = paramsStringFilter(params.get("descripton"));
		List<Long> pathArray = paramsJSONArrayFilter(params.get("path"), Long.class);
		Boolean isSystem = paramsBooleanFilter(params.get("isSystem"));
		// 部门编码不能为空过滤
		if (StrKit.isBlank(code)) {
			return renderFAIL("0050", response, header);
		}
		// 部门名称不能为空过滤
		if (StrKit.isBlank(name)) {
			return renderFAIL("0051", response, header);
		}
		// 部门类型不能为空过滤
		if (type == null) {
			return renderFAIL("0052", response, header);
		}
		// 是否为系统内置信息默认为false
		if (isSystem == null) {
			isSystem = Constant.NOTSYSTEM;
		}
		
		boolean successFlag = false;
		if (Integer.valueOf(Office.TYPE_SCHOOL).equals(type)) {
			// 最高级部门
			successFlag = save(code, name, type, isSystem, description, null);
		} else {
			// 非最高级部门，必须有上级部门
			// 上级部门编号不能为空过滤
			if (pathArray.isEmpty()) {
				return renderFAIL("0053", response, header);
			}
			
			successFlag = save(code, name, type, isSystem, description, pathArray);
		}
		
		Map<String, Object> result = Maps.newHashMap();
		result.put("isSuccess", successFlag);
		return renderSUC(result, response, header);
	}

	/**
	 * 保存新增部门
	 * @param code
	 * 			部门编码
	 * @param name
	 * 			部门名称
	 * @param type
	 * 			部门类型
	 * @param isSystem
	 * 			是否系统内置
	 * @param description
	 * 			部门描述
	 * @param parentOffice
	 * 			上级部门
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean save(String code, String name, Integer type, Boolean isSystem, String description, List<Long> pathArray) {
		Date date = new Date();
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		
		// Office创建数据准备
		Long id = idGenerate.getNextValue();
		Office office = new Office();
		OfficePath officePath = new OfficePath();
		office.set("id", id)
			  .set("create_date", date)
			  .set("modify_date", date)
			  .set("code", code)
			  .set("name", name)
			  .set("type", type)
			  .set("description", description)
			  .set("is_system", isSystem)
			  .set("is_del", Boolean.FALSE);
		officePath.set("id", id)
				  .set("create_date", date)
				  .set("modify_date", date);
		if (pathArray == null) {
			office.set("parentid", 0);
			// 系统部门路径表添加部门路径
			officePath.set("office_ids", "," + id + ",");
		} else {
			office.set("parentid", pathArray.get(pathArray.size() - 1));
			officePath.set("office_ids", "," + CollectionKit.convert(pathArray, ",,") + "," + id + ",");
		}
		
		if (!office.save()) {
			return false;
		}
		
		if (!officePath.save()) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		
		return true;
	}

}
