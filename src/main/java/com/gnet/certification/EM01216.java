package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcSelfreport;
import com.gnet.model.admin.CcVersion;
import com.gnet.object.CcVersionOrderType;
import com.gnet.pager.Pageable;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.ParamSceneUtils;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.plugin.activerecord.Page;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 自评报告新增、删除、修改
 * 
 * @author GJM
 * @Date 2020年09月10日
 */
@Service("EM01216")
public class EM01216 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		HashMap<Object, Object> result = new HashMap<>();
		//新增 1 修改 2 删除 3
		Integer type = paramsIntegerFilter(params.get("type"));
		//修改 和删除的时候传
		Long id = paramsLongFilter(params.get("id"));
		Long majorId = paramsLongFilter(params.get("majorId"));
		String name = paramsStringFilter(params.get("name"));
		//教师id
		Long teacherId = paramsLongFilter(params.get("teacherId"));

		if (majorId==null || type ==null ){
			return renderFAIL("0130", response, header);
		}
		if (teacherId==null ){
			return renderFAIL("0160", response, header);
		}
		if (type==2 && id ==null){
			return renderFAIL("2574", response, header);
		}
		CcVersion versionMajor = CcVersion.dao.getVersionMajor(majorId);
		if (versionMajor==null){
			return renderFAIL("0148", response, header);
		}
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		ArrayList<CcSelfreport> addList = new ArrayList<>();
		ArrayList<CcSelfreport> updateList = new ArrayList<>();
		CcSelfreport ccSelfreport = new CcSelfreport();

		ccSelfreport.put("version_id",versionMajor.getLong("id"));
		ccSelfreport.put("name",name);
		ccSelfreport.put("major_id",majorId);
		ccSelfreport.put("teacher_id",teacherId);
		ccSelfreport.put("create_date",new Date());
		if (type ==1){
			ccSelfreport.put("id",idGenerate.getNextValue());
			ccSelfreport.put("is_del",Boolean.FALSE);
			addList.add(ccSelfreport);
		}else if (type==2){
			ccSelfreport.put("id",id);
			ccSelfreport.put("is_del",Boolean.FALSE);
			updateList.add(ccSelfreport);
		}else {
			ccSelfreport.put("id",id);
			ccSelfreport.put("is_del",Boolean.TRUE);
			updateList.add(ccSelfreport);
		}

		boolean resultState=Boolean.TRUE;
		if (type==1){

			resultState = CcSelfreport.dao.batchSave(addList);
		}else {
			resultState=CcSelfreport.dao.batchUpdate(updateList,"name,is_del");
		}
		result.put("isSuccess", resultState);
		return renderSUC(result, response, header);
	}


}
