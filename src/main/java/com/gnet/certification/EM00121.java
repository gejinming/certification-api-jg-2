package com.gnet.certification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gnet.model.admin.CcIndicatorPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcGraduate;
import com.gnet.model.admin.CcIndication;

/**
 * 查看毕业要求详情
 * 
 * @author SY
 * 
 * @date 2016年06月24日 20:55:57
 *
 */
@Service("EM00121")
@Transactional(readOnly=true)
public class EM00121 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long id = paramsLongFilter(param.get("id"));
		
		if (id == null) {
			return renderFAIL("0180", response, header);
		}
		
		CcGraduate temp = CcGraduate.dao.findFilteredById(id);
		if (temp == null) {
			return renderFAIL("0184", response, header);
		}
		
		/*// 放到map中
		List<CcIndicatorPoint> allList = CcIndicatorPoint.dao.findFilteredByColumn("graduate_id", temp.get("id"));
		List<CcIndicatorPoint> tempList = new ArrayList<>();
		for(CcIndicatorPoint ccIndication : allList) {
			CcIndicatorPoint returnTemp = new CcIndicatorPoint();
			returnTemp.put("id", ccIndication.get("id"));
			returnTemp.put("content", ccIndication.get("content"));
			returnTemp.put("createDate", ccIndication.get("create_date"));
			returnTemp.put("modifyDate", ccIndication.get("modify_date"));
			returnTemp.put("graduateId", ccIndication.get("graduate_id"));
			returnTemp.put("remark", ccIndication.get("remark"));
			returnTemp.put("indexNum", ccIndication.get("index_num"));
			returnTemp.put("isDel", ccIndication.get("is_del"));
			tempList.add(returnTemp);
		}
*/
				
		CcGraduate ccGraduate = new CcGraduate();
		ccGraduate.put("id", temp.get("id"));
		ccGraduate.put("createDate", temp.get("create_date"));
		ccGraduate.put("modifyDate", temp.get("modify_date"));
		ccGraduate.put("graduateVerId", temp.get("graduate_ver_id"));
		ccGraduate.put("indexNum", temp.get("index_num"));
		//ccGraduate.put("indicationList", tempList);
		ccGraduate.put("content", temp.get("content"));
		ccGraduate.put("remark", temp.get("remark"));
		ccGraduate.put("isDel", temp.get("is_del"));
		
		return renderSUC(ccGraduate, response, header);
	}

}
