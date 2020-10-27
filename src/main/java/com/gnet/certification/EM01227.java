package com.gnet.certification;

import com.gnet.FileInfo;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.api.sign.Result;
import com.gnet.service.CcAimportPlanService;
import com.gnet.service.CcArrangeCourseService;
import com.gnet.service.FileService;
import com.gnet.utils.SpringContextHolder;
import com.jfinal.kit.PathKit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * 排课信息解析保存
 *
 * @author GJM
 * @Date 2020年10月19日
 */
@Transactional(readOnly = false)
@Service("EM01227")
public class EM01227 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		HashMap<Object, Object> result = new HashMap<>();
		Map<String, Object> param = request.getData();
		Object fileInfoObject = param.get("fileInfo");
        Long majorId = paramsLongFilter(param.get("majorId"));
		String token = request.getHeader().getToken();
		Long schoolId = UserCacheKit.getSchoolId(token);
        // fileInfo合法性验证
		if (fileInfoObject == null || !(fileInfoObject instanceof FileInfo)) {
			return renderFAIL("0087", response, header);
		}
		if (majorId == null) {
			return renderFAIL("0130", response, header);
		}
		if (schoolId == null) {
			return renderFAIL("0395", response, header);
		}
		FileInfo fileInfo = (FileInfo) fileInfoObject;
		FileService fileService = SpringContextHolder.getBean(FileService.class);
		// 上传失败验证
		if (!fileService.upload(fileInfo, Boolean.FALSE)) {
			return renderFAIL("0088", response, header);
		}
		CcArrangeCourseService arrangeCourseService = SpringContextHolder.getBean(CcArrangeCourseService.class);
		Result manageResult = arrangeCourseService.readExcelToObj(PathKit.getWebRootPath() + fileInfo.getTargetUrl(),majorId,schoolId);
		if (manageResult.getFlag()){
			result.put("isSuccess",true);
			result.put("message","培养计划导入成功请重新更新！");
		}else {
			result.put("isSuccess",false);
			result.put("message",manageResult.getMessage());
		}

		return renderSUC(result, response, header);
	}


}