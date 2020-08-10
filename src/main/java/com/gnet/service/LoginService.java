package com.gnet.service;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.gnet.model.admin.CcMajor;
import com.google.common.collect.Maps;

/**
 * 登录时的相关操作Service
 * 
 * @author wct
 * @date 2016年8月15日
 */
@Component("loginService")
public class LoginService {
	
	/**
	 * 获得业务相关额外的登录信息
	 * 
	 * @param userId 用户编号
	 * @return
	 */
	public Map<String, Object> getExtraInfo(Long userId) {
		Map<String, Object> result = Maps.newHashMap();
		// 获得是否为专业负责人信息
		CcMajor ccMajor = CcMajor.dao.findFirstFilteredByColumn("officer_id", userId);
		if (ccMajor != null) {
			result.put("isMajorOfficer", Boolean.TRUE);
			result.put("officerMajorId", ccMajor.getLong("id"));
		}
		
		return result;
	}
}
