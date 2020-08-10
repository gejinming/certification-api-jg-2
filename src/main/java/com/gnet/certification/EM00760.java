package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.plugin.configLoader.ConfigUtils;
import com.jfinal.kit.PathKit;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * 教学班学生导入模板下载
 * 
 * @author xzl
 * @Date 2017年10月8日
 */
@Service("EM00760")
public class EM00760 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		String path = PathKit.getWebRootPath() + ConfigUtils.getStr("excel", "templatePath") + "学生名单导入(所有教学班).xls";
		
		return renderFILE(new File(path), response, header);
	}


}
