package com.gnet.certification;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.User;
import com.gnet.utils.PasswdKit;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;

/**
 * 密码修改接口
 * 
 * @author wct
 * @Date 2016年6月14日
 */
@Service("EM00032")
@Transactional(readOnly = false)
public class EM00032 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		
		String oldPassword = paramsStringFilter(params.get("oldPassword"));
		String password = paramsStringFilter(params.get("password"));
		String rePassword = paramsStringFilter(params.get("rePassword"));
		String token = request.getHeader().getToken();
		// 新密码为空过滤
		if (StrKit.isBlank(password)) {
			return renderFAIL("0006", response, header);
		}
		
		// 根据token从缓存中获得用户
		User user = UserCacheKit.getUser(token);
		if (user == null) {
			return renderFAIL("0081", response, header);
		}
		user = User.dao.findById(user.getLong("id"));
		
		// 原密码错误过滤
		if (StrKit.notBlank(oldPassword) && !PasswdKit.validatePassword(oldPassword, user.getStr("password"))) {
			return renderFAIL("0007", response, header);
		}
		// 新密码与重新输入的密码不一致过滤
		if (StrKit.notBlank(rePassword) && !password.equals(rePassword)) {
			return renderFAIL("0008", response, header);
		}
		
		// 密码更新
		user.set("password", PasswdKit.entryptPassword(password));
		user.set("modify_date", new Date());
		user.set("modify_date", new Date());
		boolean successFlag = user.update();
		if (successFlag) {
			UserCacheKit.update(token, user, null, null);
		}
		
		// 返回结果
		Map<String, Object> result = Maps.newHashMap();
		result.put("isSuccess", successFlag);
		return renderSUC(result, response, header);
	}
	
}
