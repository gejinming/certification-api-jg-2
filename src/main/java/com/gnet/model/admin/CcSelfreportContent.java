package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


/**
 * @type model
 * @description 自评报告内容
 * @table cc_selfreport_content
 * @author GJM
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_selfreport_content")
public class CcSelfreportContent extends DbModel<CcSelfreportContent> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcSelfreportContent dao = new CcSelfreportContent();


	/*
	 * @param id
	 * @return com.gnet.model.admin.CcSelfreport
	 * @author Gejm
	 * @description: 查找自评信息内容
	 * @date 2020/7/7 10:36
	 */
	public CcSelfreportContent findSelfReportContent(Long titleId,Long selfReportId){
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select id,content from " + tableName +" where is_del = 0 and title_id=? and selfreport_id=? ");
		params.add(titleId);
		params.add(selfReportId);
		return findFirst(sql.toString(), params.toArray());
	}

	/*
	 * @param selfReportId
	 * @return java.util.List<com.gnet.model.admin.CcSelfreportContent>
	 * @author Gejm
	 * @description: 自评报告所有信息
	 * @date 2020/9/14 10:35
	 */
	public List<CcSelfreportContent> findSelfReportContentList(Long selfReportId){
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select a.id,a.content,b.title_no,b.title_name from " + tableName +" a ");
		sql.append("left join cc_selfreport_titleword b on a.title_id=b.id ");
		sql.append("where a.selfreport_id=? and a.is_del=0 ");
		params.add(selfReportId);
		return find(sql.toString(),params.toArray());

	}




}
