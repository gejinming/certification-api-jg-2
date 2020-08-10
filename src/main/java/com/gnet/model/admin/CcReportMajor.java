package com.gnet.model.admin;

import java.util.Date;
import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;

/**
 * 专业报表
 * 
 * @author wct
 * @date 2016年7月20日
 */
@TableBind(tableName = "cc_report_major")
public class CcReportMajor extends DbModel<CcReportMajor> {

	private static final long serialVersionUID = -9037881817416302565L;
	public static final CcReportMajor dao = new CcReportMajor();
	
	/**
	 * 无专业方向标识编号
	 */
	public static final Long NO_MAJORDIRECTION_ID = 0L;
	
	/**
	 * 根据指标点编号删除所有未删除的记录项
	 * 
	 * @param indicationCourseIds
	 * @return
	 */
	public boolean deleteAllNoDel(Integer grade, List<Long> indicationIds) {
		return Db.update("update " + tableName + " set is_del=?, modify_date=? where is_del=? and grade = ? and indication_id in (" + CollectionKit.convert(indicationIds, ",") + ")", DEL_YES, new Date(), DEL_NO, grade) >= 0;
	}
	
	/**
	 * 根据年级和版本获得毕业要求信息(EM00557)
	 * 
	 * @param grade 年级
	 * @param versionId 版本编号
	 * @return
	 */
	public List<CcReportMajor> findAllByGradeAndVersion(Integer grade, Long versionId) {
		return findAllByGradeAndVersion(grade, versionId, null);
	}
	
	/**
	 * 根据年级和版本获得毕业要求信息(EM00557)
	 * 
	 * @param grade 年级
	 * @param versionId 版本编号
	 * @param marjorDirectionId 专业方向编号
	 * @return
	 */
	public List<CcReportMajor> findAllByGradeAndVersion(Integer grade, Long versionId, Long majorDirectionId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select crm.*, cg.id graduate_id, cg.index_num graduate_index_num, cgv.pass target_value, crm.result result, crm.major_direction_id major_direction_id from " + tableName + " crm ");
		sql.append("inner join " + CcIndicatorPoint.dao.tableName + " ci on ci.id = crm.indication_id ");
		sql.append("inner join cc_graduate cg on cg.graduate_ver_id = ? and cg.id = ci.graduate_id ");
		sql.append("inner join cc_graduate_version cgv on cgv.id = cg.graduate_ver_id ");
		sql.append("where crm.grade = ? and crm.is_del = ? and crm.result is not null ");
		params.add(versionId);
		params.add(grade);
		params.add(DEL_NO);
		// 专业方向编号不为空时进行过滤
		if (majorDirectionId != null) {
			sql.append("and (crm.major_direction_id = ? or crm.major_direction_id is null) ");
			params.add(majorDirectionId);
		}
		
		sql.append("order by cg.index_num");
		return find(sql.toString(), params.toArray());
	}

}
