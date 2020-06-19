package com.gnet.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import com.gnet.model.admin.Office;
import com.gnet.model.admin.OfficePath;
import com.gnet.model.admin.User;
import com.gnet.utils.SpringContextHolder;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;

/**
 * @author SY
 * @date 2016年6月25日19:09:13
 */
@Component("officeService")
public class OfficeService {
	
	public Office findById(Long id) {
		return Office.dao.findById(id);
	}
	
	/**
	 * 根据编号获得部门及部门路径
	 * 
	 * @param id
	 * @return
	 */
	public Office findByIdWithPath(Long id) {
		return Office.dao.findByIdWithPath(id);
	}
	
	/**
	 * 根据部门获取所有相关的专业
	 * 
	 * @param office
	 * @return
	 */
	public Long[] getMajorIdsByOffice(Office office) {
		if(office == null){
			return new Long[]{};
		}
		List<Office> majors = Office.dao.findMajorByPath(office.getStr("office_path"));
		Long[] ids = new Long[majors.size()];
		for (int i = 0; i < ids.length; i ++) {
			ids[i] = majors.get(i).getLong("id");
		}
		
		return ids;
	}
	
	/**
	 * 根据部门获取所有相关的学院
	 * 
	 * @param office
	 * @return
	 */
	public Long[] getInsitituteIdsByOffice(Office office) {
		List<Office> majors = Office.dao.findInstituteByPath(office.getStr("office_path"));
		Long[] ids = new Long[majors.size()];
		for (int i = 0; i < ids.length; i ++) {
			ids[i] = majors.get(i).getLong("id");
		}
		
		return ids;
	}
	
	
	/**
	 * 获取用户所在学校
	 * @param userId
	 * 			用户编号
	 * @return
	 * 			用户所在学校
	 */
	public Office getSchoolByUserId(Long userId) {
		List<Office> offices = this.getOfficesByUserId(userId);
		if(offices.isEmpty()) {
			return null;
		}
		
		return offices.get(0);
	}
	
	/**
	 * 获取用户所在学院
	 * @param userId
	 * 			用户编号
	 * @return
	 * 			用户所在学院
	 */
	public Office getInstituteByUserId(Long userId) {
		List<Office> offices = this.getOfficesByUserId(userId);
		if(offices.size() <= 1) {
			return null;
		}
		return offices.get(1);
	}
	
	/**
	 * 获取用户所在专业
	 * @param userId
	 * 			用户编号
	 * @return
	 * 			用户所在专业
	 */
	public Office getMajorByUserId(Long userId) {
		List<Office> offices = this.getOfficesByUserId(userId);
		if(offices.size() <= 2) {
			return null;
		}
		return offices.get(2);
	}
	
	/**
	 * 获取用户所在班级
	 * @param userId
	 * @return
	 */
	public Office getClazzByUserId(Long userId) {
		List<Office> offices = this.getOfficesByUserId(userId);
		if(offices.size() <= 3) {
			return null;
		}
		return offices.get(3);
	}
	
	/**
	 * 获取用户的所在学校、所在学院、所在专业、所在行政班：学校-》学院-》专业-》行政班
	 * @param userId
	 * 			用户编号
	 * @return
	 */
	public List<Office> getOfficesByUserId(Long userId) {
		User user = User.dao.findFilteredById(userId);
		if(user == null) {
			return Lists.newArrayList();
		}
		if (StrKit.isBlank(user.getStr("department"))) {
			return Lists.newArrayList();
		}
		return this.getOfficesByOfficeId(Long.parseLong(user.getStr("department")));
	}
	
	/**
	 * 根据部门编号查找所有父级编号，按学校=》学院=》专业=》行政班排序
	 * @param officeId
	 * @return
	 */
	public List<Office> getOfficesByOfficeId(Long officeId) {
		List<Long> officeIdList = this.getPathByOfficeId(officeId);
		if(officeIdList == null){
			return Lists.newArrayList();
		}
		Long[] officeArray = new Long[officeIdList.size()];
		officeIdList.toArray(officeArray);
		List<Office> offices = Office.dao.findByColumnIn("id", officeArray);
		
		return offices;
	}
	
	/**
	 * 查询部门的完整路径，以List形式返回
	 * @param officeId
	 * 			部门编号
	 * @return
	 * 			部门路径
	 */
	public List<Long> getPathByOfficeId(Long officeId) {
		OfficePath path = OfficePath.dao.findById(officeId);
		if(path == null) {
			return null;
		}
		String[] officeIdStr = path.getStr("office_ids").split(",");
		List<Long> officeIdList = Lists.newArrayList();
		for(String temp : officeIdStr) {
			if(StringUtils.isEmpty(temp)) {
				continue;
			}
			officeIdList.add(Long.valueOf(temp));
		}
		return officeIdList;
	}

	/**
	 * 更新office和officepath
	 * @param office
	 * @param parentId
	 * 			父亲编号(可以为空，如果为空，则不修改路径)
	 * @param date
	 */
	@Transactional
	public boolean update(Office office, Long parentId, Date date) {
		Boolean result = office.update();
		// 如果不为空，则更新officePath
		if(parentId == null) {
			return result;
		}
		
		Long id = office.getLong("id");
		OfficePathService officePathService = SpringContextHolder.getBean(OfficePathService.class);
		result = officePathService.update(parentId, id, date);
		if(!result) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			// 返回操作是否成功
			return result;
		}
		
		return result;
	}

	/**
	 * 保存office和officepath
	 * @param office
	 * @param parentId
	 * 			父亲编号
	 * @return
	 */
	public Boolean save(Office office, Long parentId) {
		return save(office, parentId, new Date());
	}
	
	/**
	 * 保存office和officepath
	 * @param office
	 * @param parentId
	 * 			父亲编号
	 * @param date
	 * @return
	 */
	public Boolean save(Office office, Long parentId, Date date) {
		Boolean result = office.save();
		
		OfficePath fatherOfficePath = OfficePath.dao.findById(parentId);
		
		if(!result || fatherOfficePath == null || fatherOfficePath.getStr("office_ids").isEmpty()) {
			// 返回操作是否成功
			return false;
		}
		
		Long id = office.getLong("id");
		OfficePath officePath = new OfficePath();
		officePath.set("id", id);
		officePath.set("create_date", date);
		officePath.set("modify_date", date);
		officePath.set("office_ids", fatherOfficePath.getStr("office_ids") + "," + id + ",");
		result = officePath.save();
		
		if(!result) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			// 返回操作是否成功
			return false;
		}
		
		return result;
	}
	
	/**
	 * 删除和office和officePath
	 * 
	 * @param ids
	 * @param date
	 * @return
	 */
	public Boolean delete(Long[] ids, Date date){
		//删除office
		if(!Office.dao.deleteAll(ids, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		//删除officePath
		if(!OfficePath.dao.deleteAll(ids, date)){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		return true;
	}

}
