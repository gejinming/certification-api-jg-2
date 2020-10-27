package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;

import java.util.List;


/**
 * @type model
 * @description 教学班的课程目标的各种达成的数据
 * @table cc_educlass_assess_report
 * @author GJM
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_educlass_assess_report")
public class CcEduclassAssessReport extends DbModel<CcEduclassAssessReport> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcEduclassAssessReport dao = new CcEduclassAssessReport();

	/*
	 * @param educlassId
	 * @return com.gnet.model.admin.CcEduclassAchieveReport
	 * @author Gejm
	 * @description: 查询教学班的课程目标的各种达成的数据
	 * @date 2020/8/19 11:15
	 */
	public List<CcEduclassAssessReport> findAssessReport(Long educlassId,Long indicationId,Long indicationPortId){
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select * from cc_educlass_assess_report ");
		sql.append("where class_id=? and is_del=0 ");
		params.add(educlassId);
		if (indicationId !=null){
			sql.append("and indication_id=? ");
			params.add(indicationId);
		}
		if (indicationPortId !=null){
			sql.append("and indicator_port_id=? ");
			params.add(indicationPortId);
		}
		return find(sql.toString(),params.toArray());
	}
	




}
