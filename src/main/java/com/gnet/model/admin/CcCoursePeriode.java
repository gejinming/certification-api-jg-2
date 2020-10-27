package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @type model
 * @table cc_course_periode
 * @author GJM
 * @version 1.0
 * @date 2020年8月29日 11:10:53
 *
 */
@TableBind(tableName = "cc_course_periode")
public class CcCoursePeriode extends DbModel<CcCoursePeriode> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcCoursePeriode dao = new CcCoursePeriode();

	public CcCoursePeriode findCoursePeriode(Long courseId,Long classId){
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select * from cc_course_periode where course_id=? and class_id=? and is_del=0 ");
		params.add(courseId);
		params.add(classId);
		return findFirst(sql.toString(),params.toArray());

	}
}
