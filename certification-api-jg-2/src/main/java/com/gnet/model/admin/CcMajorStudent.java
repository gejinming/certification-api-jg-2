package com.gnet.model.admin;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

/**
 * 参与专业认证的学生
 * 
 * @author wct
 * @date 2016年7月30日
 */
@TableBind(tableName = "cc_major_student")
public class CcMajorStudent extends DbModel<CcMajorStudent>{

	private static final long serialVersionUID = 2192164559072767106L;
	public static final CcMajorStudent dao = new CcMajorStudent();
	
	/**
	 *  学生专业方向信息列表
	 * @param pageable
	 * @param majorId
	 * @param grade
	 * @param classId
	 * @param name
	 * @param studentNo
	 * @return
	 */
	public Page<CcMajorStudent> page(Pageable pageable, Long latestVersionId, Integer grade, Long classId, String name, String studentNo) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder(" from " + CcStudent.dao.tableName + " cs ");
		sql.append("left join " + Office.dao.tableName + " class on class.id = cs.class_id ");
		sql.append("left join " + Office.dao.tableName + " major on major.id = class.parentid ");
		sql.append("left join " + CcMajorStudent.dao.tableName + " cms on cms.student_id = cs.id and cms.version_id = ? ");
		param.add(latestVersionId);
		sql.append("left join " + CcMajorDirection.dao.tableName + " cmd on cmd.id = cms.major_direction_id ");
		sql.append("left join " + CcVersion.dao.tableName + " cv on cv.major_id = major.id and cv.id = ? ");
		param.add(latestVersionId);
		sql.append("where class.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and cs.grade = ? ");
		param.add(grade);
		sql.append("and cs.is_del = ? ");
		param.add(DEL_NO);
		sql.append("and major.is_del = ? ");
		param.add(Boolean.FALSE);
		sql.append("and (cmd.is_del = ? or cmd.is_del is null ) ");
		param.add(Boolean.FALSE);
		sql.append("and cv.is_del = ? ");
		param.add(Boolean.FALSE);
		if(classId != null){
			sql.append("and class.id = ? ");
			param.add(classId);
		}
		if (StrKit.notBlank(name)) {
			sql.append("and cs.name like '" + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		if (StrKit.notBlank(studentNo)) {
			sql.append("and cs.student_no like '" + StringEscapeUtils.escapeSql(studentNo) + "%' ");
		}
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			sql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcMajorStudent.dao.paginate(pageable, "select cs.id studenId, cs.name, cs.student_no studentNo, cs.sex, class.name className, cmd.name majorDirectionName, cv.id versionId ", sql.toString(), param.toArray());
	}

}
