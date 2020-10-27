package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


/**
 * @type model
 * @description 自评报告列表
 * @table cc_selfreport
 * @author GJM
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_selfreport")
public class CcSelfreport extends DbModel<CcSelfreport> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcSelfreport dao = new CcSelfreport();

	/*
	 * @param pageable
	 * @return com.jfinal.plugin.activerecord.Page<com.gnet.model.admin.CcSelfreport>
	 * @author Gejm
	 * @description: 自评报告列表分页
	 * @date 2020/7/6 16:13
	 */
	public Page<CcSelfreport> page(Pageable pageable, Long majorId,Long versionId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder exceptSql = new StringBuilder("from " + CcSelfreport.dao.tableName + " cs ");
		exceptSql.append("left join cc_version cv on cv.id=cs.version_id ");
		exceptSql.append("left join cc_teacher ct on cs.teacher_id=ct.id ");
		exceptSql.append("left join sys_office so on so.id=cs.major_id and so.type=2 ");
		exceptSql.append("where cs.is_del = ? and cs.major_id=? order by cs.id ");
		params.add(Boolean.FALSE);
		params.add(majorId);
		if (versionId != null) {
			exceptSql.append("and cs.version_id = ? ");
			params.add(versionId);
		}



		return CcSelfreport.dao.paginate(pageable, "select cs.*,cv.name versionName,ct.name teacherName,so.name majorName ", exceptSql.toString(), params.toArray());
	}
	/*
	 * @param id
	 * @return com.gnet.model.admin.CcSelfreport
	 * @author Gejm
	 * @description: 查找自评信息
	 * @date 2020/7/7 10:36
	 */
	public CcSelfreport findSelfReport(Long id){
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select * from " + tableName +" where is_del = 0 and id=? ");
		params.add(id);
		return findFirst(sql.toString(), params.toArray());
	}




}
