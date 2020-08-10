package com.gnet.model.admin;

import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;

/**
 * 系统部门路径表
 * 
 * @author wct
 * @Date 2016年6月28日
 */
@TableBind(tableName = "sys_office_path")
public class OfficePath extends DbModel<OfficePath> {

	private static final long serialVersionUID = -6847787011645149567L;
	
	public static final OfficePath dao = new OfficePath();

	/**
	 * 通过officeIds找到对应的子节点
	 * @param beforeOfficeIds
	 * @return
	 */
	public List<OfficePath> findLikeOfficeIds(String beforeOfficeIds) {
		return find("select * from " + tableName + " where office_ids like '" + beforeOfficeIds + "%' ");
	}
}
