package com.gnet.model.admin;

import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.jfinal.plugin.activerecord.Db;

/**
 * 培养计划课程分区学期表
 * 
 * @author SY
 * @date 2016年8月3日19:57:15
 */
@TableBind(tableName = "cc_plan_course_zone_term")
public class CcPlanCourseZoneTerm extends DbModel<CcPlanCourseZoneTerm> {

	private static final long serialVersionUID = 2910112802329176249L;
	public static final CcPlanCourseZoneTerm dao = new CcPlanCourseZoneTerm();
	
	/**
	 * 获得课程分组学期列表
	 * 
	 * @param planId 培养计划编号
	 * @return
	 */
	public List<CcPlanCourseZoneTerm> findAllByPlan(Long planId) {
		StringBuilder sql = new StringBuilder("select cpczt.*, cpcz.zone_path zone_path from " + tableName + " cpczt ");
		sql.append("inner join cc_plan_course_zone cpcz on cpcz.id = cpczt.plan_course_zone_id and cpcz.is_del = ? and cpcz.plan_id = ? ");
		sql.append("where cpczt.is_del = ?");
		return find(sql.toString(), DEL_NO, planId, DEL_NO);
	}
	
	/**
	 * 硬删除对应记录
	 * 
	 * @param deleteZoneTermIds 编号组
	 * @return
	 */
	public boolean deleteAllHard(List<Long> deleteZoneTermIds) {
		return Db.update("delete from " + tableName + " where id in (" +  CollectionKit.convert(deleteZoneTermIds, ",") + ")") >= 0;
	}

}
