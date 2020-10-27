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


/**
 * 教学班学生导入模板下载
 *
 * @author GJM
 * @Date 2020年10月19日
 */
@Transactional(readOnly = false)
@Service("EM01228")
public class EM01228 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {


		String path = PathKit.getRootClassPath() + File.separator + "excel" + File.separator + "教学班学生导入模板.xlsx";




		return renderFILE(new File(path), response, header);
	}


}