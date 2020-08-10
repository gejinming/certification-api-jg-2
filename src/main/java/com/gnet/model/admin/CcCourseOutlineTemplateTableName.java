package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @type model
 * @description 课程教学大纲模板表名
 * @table cc_course_outline_template_table_name
 * @author xzl
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course_outline_template_table_name")
public class CcCourseOutlineTemplateTableName extends DbModel<CcCourseOutlineTemplateTableName> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcCourseOutlineTemplateTableName dao = new CcCourseOutlineTemplateTableName();

	/**
	 * 模板表名称
	 * @param templateId
	 * @return
	 */
	public List<CcCourseOutlineTemplateTableName> findByTemplateId(Long templateId) {
		StringBuffer sql = new StringBuffer("select * from " + tableName + " ");
		sql.append("where course_outline_template_id = ? and is_del = ? ");
		sql.append("order by indexes, number ");
		return find(sql.toString(), templateId, DEL_NO);
	}
}
