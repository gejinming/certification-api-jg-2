package com.gnet.certification;

import java.io.File;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.jfinal.kit.PathKit;


/**
 * 毕业要求以及课程指标点导入模板下载
 * 
 * @author SY
 * @Date 2018年1月5日
 */
@Transactional(readOnly = false)
@Service("EM00900")
public class EM00900 extends BaseApi implements IApi {
	
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		
		
		String path = PathKit.getRootClassPath() + File.separator + "excel" + File.separator + "graduateImportTemplate.xls";
		
		return renderFILE(new File(path), response, header);
	}
	
}