package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;

import java.util.List;


/**
 * 
 * @type model
 * @table cc_course_group_teach_mange
 * @author GJM
 * @version 1.0
 * @date 2020年07月14日 11:10:53
 *
 */
@TableBind(tableName = "cc_course_group_teach_mange")
public class CcCourseGroupTeachMange extends DbModel<CcCourseGroupTeachMange> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcCourseGroupTeachMange dao = new CcCourseGroupTeachMange();

	/*
	 * @param courseTeachGroupId
	 * @return java.util.List<com.gnet.model.admin.CcCourseGroupTeachMange>
	 * @author Gejm
	 * @description: 根据教学分组的id获取课程分组的课程id
	 * @date 2020/6/20 23:02
	 */
	public List<CcCourseGroupTeachMange> getTeachGroupMangeIds(Long courseTeachGroupId,Long courseId){
		StringBuilder sql = new StringBuilder(" select group_id,course_id from cc_course_group_teach_mange ccgtm " );
		sql.append("left join cc_course_group_mange_group ccgmg on ccgtm.group_id=ccgmg.mange_group_id ");
		sql.append("where teach_group_id = " +courseTeachGroupId);
		sql.append(" and course_id="+courseId);
		return find(sql.toString());

	}

	public List<CcCourseGroupTeachMange> finTeachGroups(List<Long> groupIds){
		StringBuilder sql = new StringBuilder("select * from cc_course_group_teach_mange where teach_group_id in (" + CollectionKit.convert(groupIds, ",") + ") ");

		return find(sql.toString());
	}
}
