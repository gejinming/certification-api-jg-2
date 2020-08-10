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
 * @description 课程教学大纲模板
 * @table cc_course_outline
 * @author xzl
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course_outline_template")
public class CcCourseOutlineTemplate extends DbModel<CcCourseOutlineTemplate> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcCourseOutlineTemplate dao = new CcCourseOutlineTemplate();


	/**
	 * 大纲模板名称是否重复
	 * @param templateName
	 * @param outlineTemplateId
	 *@param courseOutlineTypeId  @return
	 */
	public boolean isExisted(String templateName, Long outlineTemplateId, Long courseOutlineTypeId) {
		if(outlineTemplateId != null){
			return Db.queryLong("select count(1) from " + tableName + " where name = ? and is_del = ? and outline_type_id = ? and id != ? ", templateName, DEL_NO, courseOutlineTypeId, outlineTemplateId) > 0;
		}else{
			return Db.queryLong("select count(1) from " + tableName + " where name = ? and is_del = ? and outline_type_id = ? ", templateName, DEL_NO, courseOutlineTypeId) > 0;
		}
	}


	/**
	 * 大纲模板列表
	 * @param pageable
	 * @param name
	 * @param outlineTypeId
     * @return
	 */
	public Page<CcCourseOutlineTemplate> page(Pageable pageable, String name, Long outlineTypeId) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("from " + tableName + " ");
		sql.append("where is_del = ? ");
		param.add(DEL_NO);
		sql.append("and outline_type_id = ? ");
		param.add(outlineTypeId);
		if(StrKit.notBlank(name)){
			sql.append("and name like '" + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			sql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		return CcCourseOutlineTemplate.dao.paginate(pageable, "select * ", sql.toString(), param.toArray());
    }

}
