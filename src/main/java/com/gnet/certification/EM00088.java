package com.gnet.certification;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.api.Request;
import com.gnet.api.Response;
import com.gnet.api.ResponseHeader;
import com.gnet.api.service.BaseApi;
import com.gnet.api.service.IApi;
import com.gnet.model.admin.CcMajor;
import com.gnet.model.admin.CcTeacher;
import com.gnet.model.admin.UserRole;
import com.gnet.plugin.configLoader.ConfigUtils;
import com.jfinal.kit.StrKit;


/**
 * 新增专业的负责人-某条信息
 * 
 * @author SY
 * @Date 2016年6月29日22:41:45
 */
@Service("EM00088")
@Transactional(readOnly=false)
public class EM00088 extends BaseApi implements IApi {

	@SuppressWarnings("unchecked")
	@Override
	public Response excute(Request request, Response response, ResponseHeader header, String method) {
		Map<String, Object> params = request.getData();
		// 结果返回
		Map<String, Object> result = new HashMap<>();
		// 各种其他信息
		Long officerId = paramsLongFilter(params.get("officerId"));
		// 传入专业编号
		Long majorId = paramsLongFilter(params.get("majorId"));
		String majorDirectorRole = ConfigUtils.getStr("global", "role.major_director_id").trim();
		
		if (majorId == null) {
			return renderFAIL("0130", response, header);
		}
		
		if (officerId == null) {
			return renderFAIL("0131", response, header);
		}
		
		// 根据待成为专业负责人的编号获取专业编号
		CcTeacher ccTeacher = CcTeacher.dao.findById(officerId);
		// 获取不到对应的教师
		if (ccTeacher == null) {
			return renderFAIL("0161", response, header);
		}
		
		Date date = new Date();
		// 根据教师所属专业编号查找到具体专业
		CcMajor ccMajor = CcMajor.dao.findById(ccTeacher.getLong("major_id"));
		if(ccMajor == null) {
			return renderFAIL("0137", response, header);
		}
		
		// 教师不属于该专业
		if (!majorId.equals(ccMajor.getLong("id"))) {
			return renderFAIL("0170", response, header);
		}
		
		ccMajor.set("modify_date", date);
		//专业负责人原本的编号
		Long majorDirectorId = ccMajor.getLong("officer_id");
		if(majorDirectorId != null){
			UserRole userRole = UserRole.dao.findFirstByColumn("user_id", majorDirectorId);
			if(userRole == null){
				return renderFAIL("0018", response, header);
			}
			String roles = userRole.getStr("roles");
			//判断是否以专业负责人的角色编号结尾    
			if(roles.endsWith(majorDirectorRole)){
				if(roles.indexOf(",") > -1){
					roles = roles.replace("," + majorDirectorRole, "");
				}else{
					roles = roles.replace(majorDirectorRole, "");
				}
			}else{
				if(roles.indexOf(",") > -1){
					roles = roles.replace(majorDirectorRole + ",", "");
				}else{
					roles = roles.replace(majorDirectorRole, "");
				}	
			};
			
			userRole.set("roles", roles);
			if(!userRole.update()){
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
			
			//新的专业负责人
			UserRole newUserRole = UserRole.dao.findById(officerId);
			if(newUserRole != null){
                String newRole = newUserRole.getStr("roles");
                if(StrKit.notBlank(newRole)){
            	   newUserRole.set("roles", newRole + "," + majorDirectorRole);
                }else{
                	newUserRole.set("roles", majorDirectorRole);
                }
				if(!newUserRole.update()){
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					result.put("isSuccess", false);
					return renderSUC(result, response, header);
				}
			}else{
				newUserRole = new UserRole();
				newUserRole.set("user_id", officerId);
				newUserRole.set("roles", majorDirectorRole);
				if(!newUserRole.save()){
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					result.put("isSuccess", false);
					return renderSUC(result, response, header);
				}
			}
			
		} else {
			// 当原先没有专业负责人的时候
			UserRole userRole = UserRole.dao.findFirstByColumn("user_id", officerId);
			if(userRole == null){
				return renderFAIL("0018", response, header);
			}
			 String newRole = userRole.getStr("roles");
             if(StrKit.notBlank(newRole)){
            	 userRole.set("roles", newRole + "," + majorDirectorRole);
             }else{
            	 userRole.set("roles", majorDirectorRole);
             }
			if(!userRole.update()){
				result.put("isSuccess", false);
				return renderSUC(result, response, header);
			}
		}
			
		ccMajor.set("officer_id", officerId);
		if(!ccMajor.update()){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result.put("isSuccess", false);
			return renderSUC(result, response, header);
		}
			
		// 返回操作是否成功
		result.put("isSuccess", true);
		return renderSUC(result, response, header);
	}
	
}
