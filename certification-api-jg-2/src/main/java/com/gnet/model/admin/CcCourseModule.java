package com.gnet.model.admin;

import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
/**
 * @type model
 * @description 所属模块表操作，包括对数据的增删改查与列表
 * @table cc_course_module
 * @author xzl
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course_module")
public class CcCourseModule extends DbModel<CcCourseModule>{
	
	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcCourseModule dao = new CcCourseModule();
	
	/**
	 * 验证同一培养计划下的名字不可以重复
	 * 
	 * @param moduleName
	 * @param originValue
	 * @return
	 */
	public boolean isExisted(String moduleName, Long planId, String originValue) {
		if (StrKit.notBlank(originValue)) {
			return Db.queryLong("select count(1) from cc_course_module where module_name = ? and module_name != ? and plan_id = ? and is_del = ? ",  moduleName, originValue, planId , Boolean.FALSE) > 0;
		} else {
			return Db.queryLong("select count(1) from cc_course_module where module_name = ? and plan_id = ? and is_del = ? ",  moduleName, planId, Boolean.FALSE) > 0;
		}
	}
	
	
	public boolean isExisted(String moduleName) {
		return isExisted(moduleName, null, null);
	}

	
	/**
	 * 所属模块列表分页
	 * @param pageable
	 * @param planId
	 *            版本编号
	 * @param moduleName
	 *            所属模块名称
	 * @return
	 */
	public Page<CcCourseModule> page(Pageable pageable, Long planId, String moduleName) {
		String selectString = "select ccm.* ";
		StringBuilder exceptSql = new StringBuilder("from " + CcCourseModule.dao.tableName + " ccm ");
		List<Object> params = Lists.newArrayList();
		exceptSql.append("where ccm.plan_id = ? ");
		params.add(planId);
		// 增加条件，为非软删除的
		exceptSql.append("and ccm.is_del=? ");
		params.add(Boolean.FALSE);
		
		//过滤删选条件
		if(moduleName != null){
			exceptSql.append("and ccm.module_name = ? ");
			params.add(moduleName);
		}
		
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcCourseModule.dao.paginate(pageable, selectString, exceptSql.toString(), params.toArray());
	}
	
	/**
	 * 根据区域编号所属模块
	 * 
	 * @param zoneId 课程区域编号
	 * @return
	 */
	public CcCourseModule findByZoneId(Long zoneId) {
		StringBuilder sql = new StringBuilder("select ccm.* from " + tableName + " ccm ");
		sql.append("inner join cc_plan_course_zone cpcz on cpcz.zone_id = ccm.id ");
		sql.append("where cpcz.id = ? and ccm.is_del = ?");
		return findFirst(sql.toString(), zoneId, DEL_NO);
	}

}
