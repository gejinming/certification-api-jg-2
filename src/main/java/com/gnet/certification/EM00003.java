package com.gnet.certification;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.kit.UserCacheKit;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.User;
import com.gnet.utils.PasswdKit;
import com.jfinal.kit.StrKit;

/**
 * 忘记密码接口
 * 
 * @author wct
 * 2016年6月16日
 */
@Service("EM00003")
public class EM00003 extends BaseApi implements IApi {
	
	
	/**
	 * 输密改密
	 */
	private final Integer INPUT_PASSWORD = 1;
	
	/**
	 * 重置改密
	 */
	private final Integer RESET_PASSWORD = 2;

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		Long schoolId = paramsLongFilter(params.get("schoolId"));
		String loginName = paramsStringFilter(params.get("loginName"));
		String newPassword = paramsStringFilter(params.get("password"));
		String rePassword = paramsStringFilter(params.get("rePassword"));
		Integer type = paramsIntegerFilter(params.get("type"));
		if(params.containsKey("type") && type == null){
			return renderFAIL("1009", response, header, "type的参数值非法");
		}
		
		// 学校编号不为空过滤
		if (schoolId == null) {
			return renderFAIL("0084", response, header);
		}
		
		// 登录名为空过滤
		if (StrKit.isBlank(loginName)) {
			return renderFAIL("0001", response, header);
		}
		
		User user = getByLoginName(schoolId.toString() + "-" + loginName);
		
		// 用户不存在过滤
		if (user == null) {
			return renderFAIL("0081", response, header);
		}
		
		boolean isSuccess = false;
		if (INPUT_PASSWORD.equals(type)) {
			// 新密码为空过滤
			if (StrKit.isBlank(newPassword)) {
				return renderFAIL("0006", response, header);
			}
			
			// 有重复输入密码时, 新密码和重复输入密码是否相同验证 
			if (StrKit.notBlank(rePassword) && !newPassword.equals(rePassword)) {
				return renderFAIL("0008", response, header);
			}
			
			// 设置新的密码
			user.set("password", PasswdKit.entryptPassword(newPassword));
			isSuccess = update(user);
		} else if (RESET_PASSWORD.equals(type)){
			// 设置新的密码
			user.set("password", PasswdKit.entryptPassword(User.DEFAULT_PASSWORD));
			isSuccess = update(user);
		} else {
			isSuccess = Boolean.FALSE;
		}
		
		if (isSuccess) {
			List<String> tokens = UserCacheKit.token(user.getLong("id"));
			if (!tokens.isEmpty()) {
				for (String token : tokens) {
					if (StrKit.notBlank(token)) {
						UserCacheKit.logout(token);
					}
				}
			}
		}
		
		// 返回结果
		JSONObject result = new JSONObject();
		result.put("isSuccess", isSuccess);
		return renderSUC(result, response, header);
	}
	
	/**
	 * 根据登录名获得用户
	 * 
	 * @param loginName
	 * @return
	 */
	private User getByLoginName(String loginName) {
		return User.dao.findByLoginName(loginName);
	}
	
	/**
	 * 进行密码更新
	 * 
	 * @param user
	 * @return
	 */
	@Transactional(readOnly = false)
	private boolean update(User user) {
		user.set("modify_date", new Date());
		return user.update();
	}

}
