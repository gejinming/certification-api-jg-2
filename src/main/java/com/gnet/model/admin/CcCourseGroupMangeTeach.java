package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


/**
 * 
 * @type model
 * @table cc_course_group_teach
 * @author GJM
 * @version 1.0
 * @date 2020年05月14日 11:10:53
 *
 */
@TableBind(tableName = "cc_course_group_teach")
public class CcCourseGroupMangeTeach extends DbModel<CcCourseGroupMangeTeach> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcCourseGroupMangeTeach dao = new CcCourseGroupMangeTeach();
	
	/**
	 * 类型-限选
	 */
	public static final Integer TYPE_LIMITED_SELECT = 1;
	

	/**
	 * 查看课程组表列表分页
	 * @param pageable 
	 * @param planId 
	 * @param remark
	 * @param type 
	 * @return
	 */
	public Page<CcCourseGroupMangeTeach> page(Pageable pageable, Long planId, String remark, Integer type) {
		StringBuilder exceptSql = new StringBuilder("from " + CcCourseGroupMangeTeach.dao.tableName + " ");
		List<Object> params = Lists.newArrayList();
		
		exceptSql.append("where plan_id = ? ");
		params.add(planId);
		exceptSql.append("and is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and type = ? ");
		params.add(type);
		// 删选条件
		if (!StrKit.isBlank(remark)) {
			exceptSql.append("and remark like '%" + StringEscapeUtils.escapeSql(remark) + "%' ");
		}
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcCourseGroupMangeTeach.dao.paginate(pageable, "select * ", exceptSql.toString(), params.toArray());
	}

}
