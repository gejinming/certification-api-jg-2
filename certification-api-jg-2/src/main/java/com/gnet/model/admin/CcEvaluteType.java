package com.gnet.model.admin;

import java.util.ArrayList;
import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;

/**
 * @type model
 * @description 课程考评点类型关系表操作，包括对数据的增删改查与列表
 * @table cc_evalute_type
 * @author SY
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_evalute_type")
public class CcEvaluteType extends DbModel<CcEvaluteType> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcEvaluteType dao = new CcEvaluteType();
	
	/**
	 * 通过教师开课获取数据，按照type排序
	 * @param teacherCourseId
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年10月10日 下午8:50:05 
	 */
	public List<CcEvaluteType> findByTeacherCourseId(Long teacherCourseId) {
		List<Object> params = new ArrayList<>();
		StringBuilder sb = new StringBuilder("select * from " + tableName + " cet ");
		sb.append("where cet.is_del = ? ");
		params.add(DEL_NO);
		sb.append("and teacher_course_id = ? ");
		params.add(teacherCourseId);
		sb.append("order by type ");
		return find(sb.toString(), params.toArray());
	}
	
}
