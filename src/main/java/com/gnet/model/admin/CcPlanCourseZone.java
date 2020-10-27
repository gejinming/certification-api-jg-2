package com.gnet.model.admin;

import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.jfinal.plugin.activerecord.Db;

/**
 * 培养计划课程分区表
 * 
 * @author SY
 * @date 2016年8月3日19:57:15
 */
@TableBind(tableName = "cc_plan_course_zone")
public class CcPlanCourseZone extends DbModel<CcPlanCourseZone> {

	private static final long serialVersionUID = 1110001419816988051L;
	public static final CcPlanCourseZone dao = new CcPlanCourseZone();
	
	/**
	 * 最高层区域的父级编号 0
	 */
	public static final Long NOPARENTZONEID = 0L;
	
	/**
	 * 区域类型：课程层次 1
	 */
	public static final Integer TYPE_HIERARCHY = 1;
	
	/**
	 * 区域类型：课程性质 2
	 */
	public static final Integer TYPE_PROPERTY = 2;
	
	/**
	 * 区域类型：专业方向 3
	 */
	public static final Integer TYPE_DIRECTION = 3;
	
	/**
	 * 区域类型：所属模块 4
	 */
	public static final Integer TYPE_MODULE = 4;

	/**
	 * 区域类型：次要课程层次 5
	 */
	public static final Integer TYPE_HIERARCHY_SECONDARY = 5;
	
	/**
	 * 区域类型：次要课程性质 6
	 */
	public static final Integer TYPE_PROPERTY_SECONDARY = 6;
	/**
	 * 合计信息项
	 */
	public static final Integer ITEM_SUMINFO = 1;
	/**
	 * 至少信息项
	 */
	public static final Integer ITEM_LEASTINFO = 2;
	
	/**
	 * 硬删除对应记录
	 * 
	 * @param ids 编号组
	 * @return
	 */
	public boolean deleteAllHard(List<Long> ids) {
		return Db.update("delete from " + tableName + " where id in (" +  CollectionKit.convert(ids, ",") + ")") >= 0;
	}
	


	/**
	 * 获得培养计划下的所有课程区域
	 * 
	 * @param planId 培养计划编号
	 * @return
	 */
	public List<CcPlanCourseZone> findAllByPlan(Long planId, Integer reportType) {
		StringBuilder sql = new StringBuilder("select cpcz.*, cch.name hierarchy_name, cchs.name hierarchy_secondary_name "
				+ ", ccp.property_name property_name, ccps.property_name property_secondary_name"
				+ ", cmd.name direction_name, ccm.module_name module_name, ccm.sum_group_id sum_group_id, cpczt.all_ave_week_hours all_ave_week_hours, cpczt.least_ave_week_hours least_ave_week_hours, cpczt.plan_term_id plan_term_id from " + tableName + " cpcz ");
		sql.append("left join cc_plan_course_zone_term cpczt on cpczt.plan_course_zone_id = cpcz.id and cpczt.is_del = ? ");
		sql.append("left join cc_course_hierarchy cch on cch.id = cpcz.zone_id and cch.is_del = ? ");
		// edit by SY 增加一列次要课程层次 2019年12月10日02:55:17
		sql.append("left join " + CcCourseHierarchySecondary.dao.tableName + " cchs on cchs.id = cpcz.zone_id and cchs.is_del = ? ");
		sql.append("left join cc_course_property ccp on ccp.id = cpcz.zone_id and ccp.is_del = ? ");
		// edit by SY 增加一列次要课程性质 2019年12月10日02:55:17
		sql.append("left join cc_course_property_secondary ccps on ccps.id = cpcz.zone_id and ccps.is_del = ? ");
		sql.append("left join cc_major_direction cmd on cmd.id = cpcz.zone_id and cmd.is_del = ? ");
		sql.append("left join cc_course_module ccm on ccm.id = cpcz.zone_id and ccm.is_del = ? ");
		sql.append("where cpcz.plan_id = ? and cpcz.plan_report_type = ? and cpcz.is_del = ?   ");
		sql.append("order by cpcz.sort desc,cpcz.zone_type asc, ccm.sum_group_id desc, cpcz.zone_id asc, cpcz.id asc");
		return find(sql.toString(), DEL_NO, DEL_NO, DEL_NO, DEL_NO, DEL_NO, DEL_NO, DEL_NO, planId, reportType, DEL_NO);
	}
	
}
