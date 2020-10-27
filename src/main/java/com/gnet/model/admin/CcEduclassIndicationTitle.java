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
 * @description 期末试卷的行为/技能的标题
 * @table cc_educlass_indication_title
 * @author GJM
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_educlass_indication_title")
public class CcEduclassIndicationTitle extends DbModel<CcEduclassIndicationTitle> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcEduclassIndicationTitle dao = new CcEduclassIndicationTitle();


	/*
	 * @param id
	 * @return com.gnet.model.admin.CcCourseGradecomposeBatch
	 * @author Gejm
	 * @description: 查询期末试卷的行为/技能的标题
	 * @date 2020/7/7 10:36
	 */
	public List<CcEduclassIndicationTitle> findIndicationTitleList(Long classId,Integer titleNo){
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select * from " + tableName +" where is_del = 0  and class_id=?  ");
		params.add(classId);
		if (titleNo !=null ){
			sql.append("and title_no=? ");
			params.add(titleNo);
		}
		sql.append("order by title_no");
		return find(sql.toString(), params.toArray());
	}




}
