package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.xpath.operations.String;

import java.util.List;


/**
 * @type model
 * @description 教学班的持续报告和评价表的数据填写和操作
 * @table cc_educlass_achieve_report
 * @author GJM
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_educlass_achieve_report")
public class CcEduclassAchieveReport extends DbModel<CcEduclassAchieveReport> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcEduclassAchieveReport dao = new CcEduclassAchieveReport();

	/*
	 * @param educlassId
	 * @return com.gnet.model.admin.CcEduclassAchieveReport
	 * @author Gejm
	 * @description: 查询教学班的持续报告和评价表的数据
	 * @date 2020/8/19 11:15
	 */
	public CcEduclassAchieveReport findAchieveReport(Long educlassId){
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select * from cc_educlass_achieve_report where class_id=? and is_del=0");
		params.add(educlassId);
		return findFirst(sql.toString(),params.toArray());
	}





}
