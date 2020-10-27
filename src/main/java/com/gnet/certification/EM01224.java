package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.jfinal.kit.PathKit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 培养计划导入模板下载
 *
 * @author GJM
 * @Date 2020年10月15日
 */
@Transactional(readOnly = false)
@Service("EM01224")
public class EM01224 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		//课程类型
		Integer type = paramsIntegerFilter(param.get("type"));
		if (type == null) {
			return renderFAIL("0563", response, header);
		}
		String path;
		if (type==1){
			 path = PathKit.getRootClassPath() + File.separator + "excel" + File.separator + "理论课培养计划导入模板.xlsx";
		}else {
			 path = PathKit.getRootClassPath() + File.separator + "excel" + File.separator + "实践课培养计划导入模板.xlsx";
		}



		return renderFILE(new File(path), response, header);
	}


}