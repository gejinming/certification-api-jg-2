
package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcVersion;

/**
 * 根据专业编号和年级（或只有专业编号）查询该专业年级最新的持续改进版本编号
 * 
 * @author xzl
 * 
 * @date 2016年7月28日
 *
 */
@Service("EM00640")
@Transactional(readOnly=true)
public class EM00640 extends BaseApi implements IApi  {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		@SuppressWarnings({ "unchecked" })
		Map<String, Object> param = request.getData();
		
		Long majorId = paramsLongFilter(param.get("majorId"));
		Integer grade = paramsIntegerFilter(param.get("grade"));
		
		if (majorId == null) {
			return renderFAIL("0130", response, header);
		}
		
		Map<String, Object> map = new HashMap<>();
		//返回最新版本编号,如果为空就证明还没有持续改进方案版本，页面就不需要根据编号来查询数据
		Long versionId = CcVersion.dao.findNewestVersion(majorId, grade);
		map.put("versionId", versionId);
		return renderSUC(map, response, header);
	}

}
