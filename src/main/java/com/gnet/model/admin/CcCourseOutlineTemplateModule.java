package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;

import java.util.List;

/**
 * @type model
 * @description 课程大纲模板模块基本信息
 * @table cc_course_outline_template_module
 * @author xzl
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course_outline_template_module")
public class CcCourseOutlineTemplateModule extends DbModel<CcCourseOutlineTemplateModule> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcCourseOutlineTemplateModule dao = new CcCourseOutlineTemplateModule();


    /**
     * 查看某个模板下的详细信息
     * @param templateId
     * @return
     */
    public List<CcCourseOutlineTemplateModule> findByTemplateId(Long templateId) {
        String sql = "select * from " + tableName + " where course_outline_template_id = ? and is_del = ? order by indexes ";
        return find(sql, templateId, DEL_NO);
    }
}
