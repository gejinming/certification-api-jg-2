package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;

import java.util.List;

/**
 * @type model
 * @description 课程教学大纲模板表头
 * @table cc_course_outline_template_header
 * @author xzl
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course_outline_template_header")
public class CcCourseOutlineTemplateHeader extends DbModel<CcCourseOutlineTemplateHeader> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcCourseOutlineTemplateHeader dao = new CcCourseOutlineTemplateHeader();

    /**
     * 模板表头信息
     * @param templateId
     * @return
     */
    public List<CcCourseOutlineTemplateHeader> findByTemplateId(Long templateId) {
        StringBuffer sql = new StringBuffer("select * from " + tableName + " ");
        sql.append("where is_del = ? and course_outline_template_id  = ? ");
        sql.append("order by indexes, number, column_ordinal ");
        return find(sql.toString(), DEL_NO, templateId);
    }
}
