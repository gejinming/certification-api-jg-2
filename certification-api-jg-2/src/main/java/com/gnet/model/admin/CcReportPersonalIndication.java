package com.gnet.model.admin;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;

/**
 * 学生个人指标点达成度统计表表
 * 
 * @author wct
 * @date 2016年7月28日
 */
@TableBind(tableName = "cc_report_personal_indication")
public class CcReportPersonalIndication extends DbModel<CcReportPersonalIndication> {

	private static final long serialVersionUID = 1098351612383432152L;
	public static final CcReportPersonalIndication dao = new CcReportPersonalIndication();
	
	/**
	 * 个人指标点达成度目标值为1.0
	 */
	public static final BigDecimal TARGET_RESULT = new BigDecimal(1.0).setScale(1, RoundingMode.HALF_UP);
	
	/**
	 * 获得最新统计日期
	 * 
	 * @param studentId 学校编号
	 * @param majorDirectionId 专业方向编号
	 * @return
	 */
	public Date getNewestStatisticsDate(Long studentId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select statistics_date from " + tableName + " ");
		sql.append("where student_id = ? and is_del = ? ");
		params.add(studentId);
		params.add(DEL_NO);
		
		sql.append("order by statistics_date desc");
		return Db.queryDate(sql.toString(), params.toArray());
	}
	
	/**
	 * 删除某个年级学生所有未删除的报表项
	 * 
	 * @param eduClassId
	 * @return
	 */
	public boolean deleteAllNoDel(Integer grade) {
		StringBuilder sql = new StringBuilder("update " + tableName + " crpi ");
		sql.append("left join cc_student cs on cs.grade = ? and crpi.student_id = cs.id ");
		sql.append("set crpi.is_del=?, crpi.modify_date=? ");
		sql.append("where crpi.is_del=?");
		return Db.update(sql.toString(), grade, DEL_YES, new Date(), DEL_NO) >= 0;
	}
	
	/**
	 * 获得根据版本编号和学生编号获得个人专业指标点(EM00665)
	 * 
	 * @param studentId
	 * @param versionId
	 * @return
	 */
	public List<CcReportPersonalIndication> findAllByStudentAndVersion(Long studentId, Long versionId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select crpi.*, cg.id graduate_id, cg.index_num graduate_index_num, cgv.pass target_value, min(crpi.result) indication_result from " + tableName + " crpi ");
		sql.append("inner join " + CcIndicatorPoint.dao.tableName + " ci on ci.id = crpi.indication_id and ci.is_del = ? ");
		params.add(DEL_NO);
		sql.append("inner join cc_graduate cg on cg.id = ci.graduate_id and cg.graduate_ver_id = ? and cg.is_del = ? ");
		params.add(versionId);
		params.add(DEL_NO);
		sql.append("inner join cc_graduate_version cgv on cgv.id = cg.graduate_ver_id and cgv.is_del = ? ");
		params.add(DEL_NO);
		sql.append("where crpi.student_id = ? and crpi.is_del = ? ");
		params.add(studentId);
		params.add(DEL_NO);
		
		sql.append("group by cg.id ");
		sql.append("order by cg.index_num asc ");
		
		return find(sql.toString(), params.toArray());
	}
	
}
