package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;

/**
 * @type model
 * @description 课程表操作，包括对数据的增删改查与列表
 * @table cc_course
 * @author SY
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_graduate_version")
public class CcGraduateVersion extends DbModel<CcGraduateVersion> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcGraduateVersion dao = new CcGraduateVersion();

    /**
     * 通过开课课程编号查找毕业要求
     * @param teacherCourseId
     * @return
     */
    public CcGraduateVersion findByTeacherCourseId(Long teacherCourseId) {
        StringBuilder sql = new StringBuilder("select cgv.* from " + tableName + " cgv ");
        sql.append("inner join " + CcCourse.dao.tableName + " cc on cc.plan_id = cgv.id and cc.is_del = ? ");
        sql.append("inner join " + CcTeacherCourse.dao.tableName + " ctc on ctc.course_id = cc.id and ctc.id = ? and ctc.is_del = ? ");
        sql.append("where cgv.is_del = ? ");
        return findFirst(sql.toString(), DEL_NO, teacherCourseId, DEL_NO, DEL_NO);
    }
}
