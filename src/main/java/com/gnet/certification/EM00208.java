package com.gnet.certification;
import java.io.File;

import org.springframework.stereotype.Service;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.plugin.configLoader.ConfigUtils;
import com.jfinal.kit.PathKit;

/**
 * 下载学生导入模板接口
 * 
 * @author xzl
 * @Date 2016年10月26日
 */
@Service("EM00208")
public class EM00208 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
//		String path = PathKit.getWebRootPath() + ConfigUtils.getStr("excel", "templatePath") + "studentImportTemplate.xls";
		String path = PathKit.getRootClassPath() + File.separator + "excel" + File.separator + "学生名单导入.xls";
		
		return renderFILE(new File(path), response, header);
	}


}
