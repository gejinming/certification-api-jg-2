package com.gnet.model.admin;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * @type model
 * @description 培养计划版本表操作，包括对数据的增删改查与列表
 * @table cc_plan_version
 * @author SY
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_plan_version")
public class CcPlanVersion extends DbModel<CcPlanVersion> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcPlanVersion dao = new CcPlanVersion();
	
	/**
	 * 报表类型：理论教学 1
	 */
	public static final Integer PLAN_REPORT_TYPE_THEORY = 1;
	
	/**
	 * 报表类型：实践教学 2
	 */
	public static final Integer PLAN_REPORT_TYPE_PRACTICE = 2;
	
	/**
	 * 版本:15版
	 */
	public static final Integer VERSION_15 = 15;
	
	/**
	 * 版本:17版
	 */
	public static final Integer VERSION_17 = 17;
	
	

	/**
	 * 培养计划列表(分页)
	 * 
	 * @param pageable
	 * @param name
	 * @param majorId
	 * @return
	 */
	public Page<CcPlanVersion> page(Pageable pageable, String name, Long majorId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder exceptSql = new StringBuilder("from " + CcPlanVersion.dao.tableName + " cpv ");
		exceptSql.append("left join cc_version cv on cv.id = cpv.id ");
		exceptSql.append("where cv.major_id=? and cv.is_del=? ");
		params.add(majorId);
		params.add(CcPlanVersion.DEL_NO);
		// 根据版本名称进行查询
		if (StrKit.notBlank(name)) {
			exceptSql.append("and cpv.name like '" + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty()  + " " + pageable.getOrderDirection() + "");
		} else {
			exceptSql.append("order by cv.sort desc, cv.major_version desc, cv.minor_version desc");
		}
		return CcPlanVersion.dao.paginate(pageable, "select cpv.*, cv.name version_name, cv.state version_state, cv.major_version major_version, cv.minor_version minor_version, cv.enable_grade enable_grade ", exceptSql.toString(), params.toArray());
	}
	
	/**
	 * 判断是否需要更新
	 * 
	 * @param planId 培养计划编号
	 * @return
	 */
	public boolean needToUpdateById(Long planId) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select count(1) from " + tableName + " cpv ");
		sql.append("left join cc_course cc on cc.plan_id = cpv.id and cc.is_del = ? ");
		params.add(DEL_NO);
		sql.append("left join cc_plan_course_zone cpcz on cpcz.zone_id = cc.hierarchy_id or cpcz.zone_id = cc.module_id  and cpcz.is_del = ? ");
		params.add(DEL_NO);
		sql.append("left join cc_plan_course_zone_term cpczt on cpczt.plan_course_zone_id = cpcz.id and cpczt.is_del = ? ");
		params.add(DEL_NO);
		sql.append("left join cc_plan_term cpt on cpt.id = cpczt.plan_term_id and cpt.is_del = ? ");
		params.add(DEL_NO);
		sql.append("where cpv.id = ? and ((cpv.build_date < cpcz.modify_date or cpv.build_date < cpt.modify_date or cpv.build_date < cc.modify_date) or (cpv.build_date is null and cc.modify_date is not null))");
		params.add(planId);
		sql.append("and cpv.is_del = ? ");
		params.add(DEL_NO);
		return Db.queryLong(sql.toString(), params.toArray()) > 0;
		
	}
}
