package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.exception.NotFoundOrderDirectionException;
import com.gnet.exception.NotFoundOrderPropertyException;
import com.gnet.model.admin.CcMajor;
import com.gnet.model.admin.Office;
import com.gnet.object.CcMajorOrderType;
import com.gnet.pager.Pageable;
import com.gnet.service.OfficeService;
import com.gnet.utils.DictUtils;
import com.gnet.utils.ParamSceneUtils;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.plugin.activerecord.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取任课课程所属的专业及教师本身所属的专业
 * 
 * @author GJM
 * @Date 2020年8月5日14:09:05
 */
@Service("EM01205")
public class EM01205 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 教师id
		Long teacherId = paramsLongFilter(params.get("id"));
		if (teacherId==null){
			return renderFAIL("0500", response, header);
		}
		//根据教师id获取任课课程所属的专业及教师本身所属的专业
		List<CcMajor> courseMajors = CcMajor.dao.findCourseMajors(teacherId);

		returnMap.put("list", courseMajors);
		
		// 结果返回
		return renderSUC(returnMap, response, header);
	}
}
