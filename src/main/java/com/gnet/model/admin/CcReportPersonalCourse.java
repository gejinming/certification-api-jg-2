package com.gnet.model.admin;

import java.util.Date;
import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;

/**
 * 学生个人统计表表
 * 
 * @author wct
 * @date 2016年7月27日
 */
@TableBind(tableName = "cc_report_personal_course")
public class CcReportPersonalCourse extends DbModel<CcReportPersonalCourse> {

	private static final long serialVersionUID = 3297691350480163410L;
	public static final CcReportPersonalCourse dao = new CcReportPersonalCourse();
	
	/**
	 * 学生个人达成度指标点(EM00559)
	 * 
	 * @param studentId 学生编号
	 * @param majorDirectionId 专业方向编号
	 * @return
	 */
	public List<CcReportPersonalCourse> findByStudentWithIndicationDetail(Long versionId, Long studentId, Long majorDirectionId, Long graduateId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select crpc.*, crpi.result indication_result, cic.course_id course_id, "
				+ "ci.id indication_id, cic.weight indication_course_weight, cg.index_num graduate_num, "
				+ "ci.index_num indication_num from " + CcIndicationCourse.dao.tableName + " cic ");
		sql.append("left join " + tableName + " crpc on cic.id = crpc.indication_course_id and crpc.is_del = ? and crpc.student_id = ? ");
		sql.append("inner join " + CcIndicatorPoint.dao.tableName + " ci on ci.id = cic.indication_id ");
		sql.append("inner join cc_graduate cg on cg.id = ci.graduate_id and cg.graduate_ver_id = ? ");
		sql.append("left join cc_report_personal_indication crpi on crpi.indication_id = ci.id and crpi.student_id = ? and crpi.is_del = ? ");
		sql.append("left join cc_course cc on cc.id = cic.course_id and cc.plan_id = ? ");
		
		params.add(DEL_NO);
		params.add(studentId);
		params.add(versionId);
		params.add(studentId);
		params.add(DEL_NO);
		params.add(versionId);
        
		sql.append("where cic.is_del = ? ");
		params.add(DEL_NO);
		
		// 专业方向编号不能为空
		if (majorDirectionId != null) {
			sql.append("and (cc.direction_id = ? or cc.direction_id is null) ");
			params.add(majorDirectionId);
		}
		
		// 毕业要求
		if (graduateId != null) {
			sql.append("and cg.id = ? ");
			params.add(graduateId);
		}
		
		sql.append("order by cg.index_num asc, ci.index_num asc");
		return find(sql.toString(), params.toArray());
	}
	
	/**
	 * 获得某个版本某个年级的所有未删除的学生统计成绩
	 * 
	 * @param versionId 版本编号
	 * @param grade 年级
	 * @return
	 */
	public List<CcReportPersonalCourse> findAllByVersionAndGrade(Long versionId, Integer grade) {
		StringBuilder sql = new StringBuilder("select crpc.* from " + tableName + " crpc ");
		sql.append("inner join cc_student cs on cs.is_del = ? and cs.grade = ? and cs.id = crpc.student_id ");
		sql.append("inner join cc_indication_course cic on cic.is_del = ? and cic.id = crpc.indication_course_id ");
		sql.append("inner join cc_course cc on cc.plan_id = ? and cc.id = cic.course_id ");
		sql.append("where crpc.is_del is not null ");
		return find(sql.toString(), DEL_NO, grade, DEL_NO, versionId);
	}


	/**
	 * 是否删除学生成绩成功
	 * 
	 * @param ids 编号
	 * @param date 时间
	 * @return
	 */
	public boolean deleteAll(Long[] ids, Date date) {
		String sql = "update "  + tableName + " set is_del = ?, modify_date = ?, statistics_date = ? where id in (" + CollectionKit.convert(ids, ",") + ")";
		return Db.update(sql, DEL_YES, date, date) >= 0;
	}
	
}
