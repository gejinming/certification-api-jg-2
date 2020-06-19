package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * @type model
 * @description 课程教学大纲模块支持课程目标表
 * @table cc_course_outline_indications
 * @author SY
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course_outline_indications")
public class CcCourseOutlineIndications extends DbModel<CcCourseOutlineIndications> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcCourseOutlineIndications dao = new CcCourseOutlineIndications();

    /**
     * 大纲每个模块支持的指标点信息(EM00703)
     * @param courseOutlineId
     * @return
     */
    public List<CcCourseOutlineIndications> findByCourseOutlineId(Long courseOutlineId) {
        List<Object> param = Lists.newArrayList();
        StringBuffer sql = new StringBuffer("select ccoi.course_target_indication_id courseTargetIndicationId, ccoi.remark, cip.index_num indicationIndexNum, cip.content indicationContent, cg.index_num graduateIndexNum, cg.content graduateContent, " +
                "ci.sort, ci.content from" + tableName + " ccoi ");
        sql.append("inner join " + CcCourseTargetIndication.dao.tableName + " ccti on ccti.id = ccoi.course_target_indication_id ");
        sql.append("inner join " + CcIndication.dao.tableName + " ci on ci.id = ccti.indication_id and ci.is_del = ? ");
        param.add(DEL_NO);
        sql.append("inner join " + CcIndicationCourse.dao.tableName + " cic on cic.id = ccti.indication_course_id and cic.is_del = ? ");
        param.add(DEL_NO);
        sql.append("inner join " + CcIndicatorPoint.dao.tableName + " cip on cip.id = cic.indication_id and cip.is_del = ? ");
        param.add(DEL_NO);
        sql.append("inner join " + CcGraduate.dao.tableName + " cg on cg.id = cip.graduate_id and cg.is_del = ? ");
        param.add(DEL_NO);
        sql.append("where ccoi.course_outline_id = ? and ccoi.is_del = ? ");
        param.add(courseOutlineId);
        param.add(DEL_NO);
        sql.append("order by indexes, cg.index_num, cip.index_num , ci.sort ");
        return find(sql.toString(), param.toArray());
    }

}
