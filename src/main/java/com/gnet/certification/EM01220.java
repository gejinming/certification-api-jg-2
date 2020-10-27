package com.gnet.certification;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcSelfreportContent;
import com.gnet.plugin.id.IdGenerate;
import com.gnet.utils.SpringContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 自评报告按标题保存内容,或修改
 * 
 * @author GJM
 * @Date 2020年09月10日
 */
@Service("EM01220")
public class EM01220 extends BaseApi implements IApi {

	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		HashMap<Object, Object> result = new HashMap<>();
		//标题id
		Long titleId = paramsLongFilter(params.get("titleId"));
		//自评报告id
		Long selfReportId = paramsLongFilter(params.get("selfReportId"));
		//内容
		String content = paramsStringFilter(params.get("content"));
		if (titleId ==null){
			return renderFAIL("2575", response, header);
		}
		if (selfReportId==null){
			return renderFAIL("2574", response, header);
		}
		if (content==null){
			return renderFAIL("2576", response, header);
		}
		//判断保存的内容是否包含图片
		/*if(content.contains(".gif") || content.contains(".png") || content.contains(".jpg")){
			return renderFAIL("2577", response, header);
		}*/
		IdGenerate idGenerate = SpringContextHolder.getBean(IdGenerate.class);
		CcSelfreportContent ccSelfreportContent = CcSelfreportContent.dao.findSelfReportContent(titleId,selfReportId);
		CcSelfreportContent ccSelfreportContents = new CcSelfreportContent();
		ccSelfreportContents.set("content",content);
		ccSelfreportContents.set("title_id",titleId);
		ccSelfreportContents.set("selfreport_id",selfReportId);
		ccSelfreportContents.set("is_del",Boolean.FALSE);
		if (ccSelfreportContent==null){
			ccSelfreportContents.set("id",idGenerate.getNextValue());
			boolean isSuccess = ccSelfreportContents.save();
			result.put("isSuccess", isSuccess);
		}else {
			ccSelfreportContents.set("id",ccSelfreportContent.getLong("id"));
			boolean isSuccess = ccSelfreportContents.update();
			result.put("isSuccess", isSuccess);
		}
		return renderSUC(result, response, header);
	}


}
