package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Page;
import org.apache.bcel.generic.ARRAYLENGTH;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @type model
 * @description 自评报告文档标题
 * @table cc_selfreport_titleword
 * @author GJM
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_selfreport_titleword")
public class CcSelfreportTitleword extends DbModel<CcSelfreportTitleword> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcSelfreportTitleword dao = new CcSelfreportTitleword();

	/*
	 * @param titleLevel
		 * @param parentTitleId
	 * @return java.util.List<com.gnet.model.admin.CcSelfreportTitleword>
	 * @author Gejm
	 * @description: 查询各级标题
	 * @date 2020/9/10 17:00
	 */
	public List<CcSelfreportTitleword> findReportTitle(Integer titleLevel,Long parentTitleId){
		ArrayList<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder(" select * from cc_selfreport_titleword where is_del=0 ");
		if (titleLevel != null){
			sql.append("and title_level=? ");
			params.add(titleLevel);
		}
		if (parentTitleId!=null){
			sql.append("and parent_id=? ");
			params.add(parentTitleId);
		}
		sql.append("order by title_no ");
		return  find(sql.toString(),params.toArray());

	}
	/*
	 * @param id
	 * @return com.gnet.model.admin.CcSelfreport
	 * @author Gejm
	 * @description: 查找自评标题
	 * @date 2020/7/7 10:36
	 */
	public CcSelfreportTitleword findSelfReport(Long id){
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select * from " + tableName +" where is_del = 0 and id=? ");
		params.add(id);
		return findFirst(sql.toString(), params.toArray());
	}
	/*
	 * @param
	 * @return java.util.List<com.gnet.model.admin.CcSelfreportTitleword>
	 * @author Gejm
	 * @description: 各级标题
	 * @date 2020/9/16 14:19
	 */
	public List<CcSelfreportTitleword> findTitleLevel(){
		StringBuilder sql = new StringBuilder("select a.title_name,b.title_name,c.title_name from cc_selfreport_titleword a ");
		sql.append("left join cc_selfreport_titleword b on a.id=b.parent_id and  b.title_level=2 and b.is_del=0 ");
		sql.append("left join cc_selfreport_titleword c on  b.id=c.parent_id and c.title_level=3 and c.is_del=0 ");
		sql.append("where a.title_level=1 and a.is_del=0 ");
		sql.append("order by a.title_no,b.title_no,c.title_no ");
		return find(sql.toString());
	}



}
