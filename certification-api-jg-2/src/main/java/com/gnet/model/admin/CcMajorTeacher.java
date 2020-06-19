package com.gnet.model.admin;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.gnet.model.DbModel;
import com.gnet.model.admin.CcTeacher;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;


/**
 * 
 * @type model
 * @table sys_major_teacher
 * @author sll
 * @version 1.0
 * @date 2016年06月30日 19:27:15
 *
 */
@TableBind(tableName = "cc_major_teacher")
public class CcMajorTeacher extends DbModel<CcMajorTeacher> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcMajorTeacher dao = new CcMajorTeacher();
	
	/**
	 * 教师工号是否存在判断
	 * 
	 * @param code
	 * @param originValue  排除值，一般用于编辑校验
	 * @return
	 */
	public boolean isExisted(String code, String originValue) {
		if (StrKit.notBlank(originValue)) {
			return Db.queryLong("select count(1) from sys_major_teacher where code = ? and code != ? and is_del = ? ", code, originValue, Boolean.FALSE) > 0;
		} else {
			return Db.queryLong("select count(1) from sys_major_teacher where code = ? and is_del = ? ", code, Boolean.FALSE) > 0;
		}
	}
	
	/**
	 * 
	 * 是否存在此教师工号
	 * 
	 * @description 根据教师工号查询是否存在该CcMajorTeacher
	 * @sql select count(1) from sys_major_teacher where code=?
	 * @version 1.0
	 * @param code
	 * @return
	 */
	public boolean isExisted(String code) {
		return isExisted(code, null);
	}

	/**
	 * 查看专业教师联系列表（包括本专业下的教师）分页
	 * @param pageable
	 * @param code
	 * @param name
	 * @param versionId
	 * @param majorId
	 * @return
	 * @author SY 
	 * @version 编辑时间：2016年11月30日 上午9:46:27（修改注释，内容没变） 
	 */
	public Page<CcTeacher> page(Pageable pageable, String code, String name, Long versionId, Long majorId) {
		List<Object> params = Lists.newArrayList();
		
		StringBuilder exceptSql = new StringBuilder("from " + CcTeacher.dao.tableName + " ct ");
		exceptSql.append("left join " + CcMajorTeacher.dao.tableName + " cmt on cmt.teacher_id = ct.id and cmt.version_id = ? and cmt.is_del = ? " );
		params.add(versionId);
		params.add(DEL_NO);
		exceptSql.append("left join " + Office.dao.tableName + " major on major.id = ct.major_id ");
		exceptSql.append("left join " + Office.dao.tableName + " institute on institute.id = ct.institute_id ");
		exceptSql.append("left join " + CcVersion.dao.tableName + " cv on cmt.version_id = cv.id and cv.is_del = ? ");
		params.add(Boolean.FALSE);
		
		exceptSql.append(" where ct.is_del = ? ");
		params.add(Boolean.FALSE);
		/*
		 *  exceptSql.append("and (((ct.major_id != ? or ct.major_id is null) and cmt.is_del = ? ) or ( ct.major_id = ? and (cmt.is_del = ? or cmt.is_del is null)))");
		 *  edit by SY, 因为发现上述代码，只能查询出专业下教师，所以废弃
		 */
		exceptSql.append("and (( cv.major_id = ? AND cmt.is_del = ? ) or ( ct.major_id = ? and (cmt.is_del = ? or cmt.is_del is null)))");
		params.add(majorId);
		params.add(Boolean.FALSE);
		params.add(majorId);
		params.add(Boolean.FALSE);
		exceptSql.append("and (major.is_del = ? or major.is_del is null) ");
		params.add(Boolean.FALSE);
		exceptSql.append("and (institute.is_del = ? or institute.is_del is null) ");
		params.add(Boolean.FALSE);
		
		// 删选条件
		if (!StrKit.isBlank(name)) {
			exceptSql.append("and ct.name like '" + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		if (!StrKit.isBlank(code)) {
			exceptSql.append("and ct.code like '" + StringEscapeUtils.escapeSql(code) + "%' ");
		}
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcTeacher.dao.paginate(pageable, "select cmt.id majorTeacherId, ct.id teacher_id, ct.*, major.name majorName, institute.name instituteName ", exceptSql.toString(), params.toArray());
	}
	
	/**
	 * 根据版本编号和教师编号删去联系
	 * 
	 * @param versionId
	 * @param teacherIds
	 * @return
	 */
	public Boolean deleteByVersionIdAndTeacherIds(Long versionId, Long[] teacherIds){
		
		StringBuilder sql = new StringBuilder("update " + CcMajorTeacher.dao.tableName + " set is_del = ? ");
		sql.append("where version_id = ? and teacher_id in (" + CollectionKit.convert(teacherIds, ",") + ") ");
		
		List<Object> params = Lists.newArrayList();
		params.add(Boolean.TRUE);
		params.add(versionId);
		
		return Db.update(sql.toString(), params.toArray()) > 0;
	}

	/**通过教师编号和版本编号查找教师
	 * @param teacherId
	 * @param versionId
	 * @return
	 */
	public Boolean isExistTeacher(Long teacherId, Long versionId) {
		String sql = "select count(1) from " + tableName + " where teacher_id = ? and version_id = ? and is_del = ? ";
		return Db.queryLong(sql, teacherId, versionId, false) > 0;
	}


}





