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
 * @description 课程教学大纲类型
 * @table cc_course_outline_type
 * @author xzl
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course_outline_type")
public class CcCourseOutlineType extends DbModel<CcCourseOutlineType> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcCourseOutlineType dao = new CcCourseOutlineType();

	public final static String NAME = "教学大纲";

	/**
	 * 大纲名称是否重复
	 * @param name
	 * @param id
	 * @param schoolId
	 * @return
	 */
    public boolean isRepeatName(String name, Long id, Long schoolId) {
    	if(id != null){
    		return Db.queryLong("select count(1) from " + tableName + " where name = ? and is_del = ? and id != ? and school_id = ? ", name, DEL_NO, id, schoolId) > 0;
		}else{
    		return  Db.queryLong("select count(1) from " + tableName + " where name= ? and is_del = ? and school_id = ? ", name, DEL_NO, schoolId) > 0;
		}
    }

    public Page<CcCourseOutlineType> page(Pageable pageable, String name, Long schoolId) {
		List<Object> params = Lists.newArrayList();
    	StringBuffer sql = new StringBuffer("from " + tableName + " ccot ");
		sql.append("where ccot.is_del = ? ");
		params.add(DEL_NO);
		sql.append("and ccot.school_id = ? ");
		params.add(schoolId);
		if(StrKit.notBlank(name)){
			sql.append("and ccot.name like '" + StringEscapeUtils.escapeSql(name) + "%' ");
		}
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			sql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}

		return CcCourseOutlineType.dao.paginate(pageable, "select * ", sql.toString(), params.toArray());
    }

	/**
	 * 学校下的大纲类型
	 *
	 * @param schoolId
	 * @param courseId
	 * @return
	 */
	public List<CcCourseOutlineType> findByCourseId(Long courseId, Long schoolId) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select ccot.id, ccot.name outlineTypeName, cco.name, cco.author_id, cco.auditor_id, ct.name authorName, ctr.name auditorName, cco.status from " + tableName + " ccot ");
		sql.append("left join " + CcCourseOutline.dao.tableName + " cco on cco.outline_type_id = ccot.id and cco.course_id = ? and (cco.is_del = ? or cco.is_del is null) ");
		param.add(courseId);
		param.add(DEL_NO);
		sql.append("left join " + CcTeacher.dao.tableName + " ct on ct.id = cco.author_id and (ct.is_del = ? or ct.is_del is null) ");
		param.add(DEL_NO);
		sql.append("left join " + CcTeacher.dao.tableName + " ctr on ctr.id = cco.auditor_id and (ctr.is_del = ? or ctr.is_del is null) ");
		param.add(DEL_NO);
		sql.append("where ccot.is_del = ? and ccot.school_id = ? ");
		param.add(DEL_NO);
		param.add(schoolId);
		return find(sql.toString(), param.toArray());
	}
}
