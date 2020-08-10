package com.gnet.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.model.admin.OfficePath;

/**
 * @author SY
 * @date 2016年6月30日18:44:31
 */
@Component("officePathService")
public class OfficePathService {
	
	/**
	 * 输入父亲和本次编号，自动保存到officePath表（会同步更新儿子节点）
	 * @param parentId
	 * 			父亲编号
	 * @param myId
	 * 			我的编号
	 * @return
	 */
	public Boolean update(Long instituteId, Long majorId) {
		return update(instituteId, majorId, new Date());
	}
	
	/**
	 * 输入父亲和本次编号和时间，自动保存到officePath表（会同步更新儿子节点）
	 * @param parentId
	 * 			父亲编号
	 * @param myId
	 * 			我的编号
	 * @param date
	 * 			时间
	 * @return
	 */
	public Boolean update(Long instituteId, Long majorId, Date date) {
		
		Boolean result = false;
		
		OfficePath fatherOfficePath = OfficePath.dao.findById(instituteId);
		
		if(fatherOfficePath == null || fatherOfficePath.getStr("office_ids").isEmpty()) {
			// 返回操作是否成功
			return false;
		}
		
		// 获取之前的officePath 用于对这个专业下的班级进行替换
		OfficePath officePath = OfficePath.dao.findById(majorId);
		String beforeOfficeIds = officePath.getStr("office_ids") + ",";
		String afterOfficeIds = fatherOfficePath.getStr("office_ids") + "," + majorId + ",";
		officePath.set("id", majorId);
		officePath.set("modify_date", date);
		officePath.set("office_ids", afterOfficeIds);
		result = officePath.update();
		
		// 为了防止父类本身被修改，多家一个逗号
		afterOfficeIds = afterOfficeIds + ",";
		if(!result) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			// 返回操作是否成功
			return false;
		}
		
		List<OfficePath> officePaths = OfficePath.dao.findLikeOfficeIds(beforeOfficeIds);
		if(officePaths.isEmpty()) {
			return result;
		}
		
		// 子节点修改
		for(OfficePath temp : officePaths) {
			temp.set("modify_date", date);
			temp.set("office_ids", temp.getStr("office_ids").replace(beforeOfficeIds, afterOfficeIds));
		}
		result = OfficePath.dao.batchUpdate(officePaths, "modify_date,office_ids");
		if(!result) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			// 返回操作是否成功
			return false;
		}
		return result;
	}

}
