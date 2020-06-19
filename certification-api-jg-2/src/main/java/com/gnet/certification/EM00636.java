package com.gnet.certification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;

/**
 * 验证转入(转出)年份不能小于转入（转出）年级，且转入（转出）年份和转入（转出）年级之差不能大于4
 * 
 * @author xzl
 * @Date 2016年7月27日
 */
@Service("EM00636")
public class EM00636 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> param = request.getData();
		
		Integer year = paramsIntegerFilter(param.get("year"));
		
		Integer grade = paramsIntegerFilter(param.get("grade"));
		
		if(year == null){
			return renderFAIL("0642", response, header);
		}
		
		if(grade == null){
			return renderFAIL("0641", response, header);
		}
		
		// 结果返回
		Map<String, Boolean> result = new HashMap<>();
		
		if(grade > year || year - grade > 4){
			result.put("illegal", true);
		}else{
			result.put("illegal", false);
		}
		
		return renderSUC(result, response, header);
	}

}
