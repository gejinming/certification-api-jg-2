package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.jfinal.kit.PathKit;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * 下载教师培训经历导入模板
 * 
 * @author GJM
 * @Date 2020年08月26日
 */
@Service("EM01209")
public class EM01209 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
//		String path = PathKit.getWebRootPath() + ConfigUtils.getStr("excel", "templatePath") + "studentImportTemplate.xls";
		String path = PathKit.getRootClassPath() + File.separator + "excel" + File.separator + "教师进修培训经历导入模板.xls";
		
		return renderFILE(new File(path), response, header);
	}


}
