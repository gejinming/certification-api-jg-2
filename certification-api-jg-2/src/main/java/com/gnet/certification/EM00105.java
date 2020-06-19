package com.gnet.certification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcCourseOutline;
import com.gnet.model.admin.CcVersion;
import com.gnet.service.CcVersionService;
import com.gnet.utils.SpringContextHolder;

/**
 * 发布版本
 * 
 * @author SY
 * @Date 2016年6月22日18:44:23
 */
@Service("EM00105")
public class EM00105 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 获取数据
		Long versionId = paramsLongFilter(params.get("id"));
		
		if (versionId == null) {
			return renderFAIL("0140", response, header);
		}
		
		String message = "操作成功！";
		
		// 检验教学大纲的状态，只有在未分配以及通过审核才可以发布，但是未分配的时候会返回一些信息提示客户端。
		Integer[] statuses = {CcCourseOutline.STATUS_AUDIT_PASS};
		List<CcCourseOutline> ccCourseOutlines = CcCourseOutline.dao.findByVersionIdAndNotInStatus(versionId, statuses);
		for(CcCourseOutline temp : ccCourseOutlines) {
			if(CcCourseOutline.STATUS_NOT_DISTRIBUTION.equals(temp.getInt("status"))) {
				message = "存在状态为：“未分配”的课程大纲，已经发布，单特此提醒..";
				// 因为是逆序的，所以当这里判断进来的时候，已经全都是状态为未分配的了，所以一次判断通过直接break。
				break;
			} else {
				return renderFAIL("0126", response, header);
			}
		}
		
		// 保存这个信息
		CcVersionService ccVersionService = SpringContextHolder.getBean(CcVersionService.class);
		Boolean updateResult = ccVersionService.changeState(versionId, CcVersion.STATUE_PUBLISH, CcVersion.STATUE_EDIT);
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		// 返回操作是否成功
		result.put("isSuccess", updateResult);
		result.put("message", message);
		return renderSUC(result, response, header);
	}
	
}
