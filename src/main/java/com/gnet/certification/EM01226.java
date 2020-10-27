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
import java.util.Map;


/**
 * 排课信息导入模板下载
 *
 * @author GJM
 * @Date 2020年10月19日
 */
@Transactional(readOnly = false)
@Service("EM01226")
public class EM01226 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {


		String path = PathKit.getRootClassPath() + File.separator + "excel" + File.separator + "排课信息导入.xlsx";




		return renderFILE(new File(path), response, header);
	}


}