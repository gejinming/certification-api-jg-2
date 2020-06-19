package com.gnet.model.admin;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * @type model
 * @description 专业表操作，包括对数据的增删改查与列表
 * @table cc_major
 * @author SY
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_major")
public class CcMajor extends DbModel<CcMajor> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcMajor dao = new CcMajor();
	
	/**
	 * 查看专业列表分页
	 * @param pageable
	 * @param majorIds
	 * 			
	 * @param instituteId
	 * 			学院编号
	 * @param majorName
	 * 			专业名字
	 * @param instituteName 
	 * @return
	 */
	public Page<CcMajor> page(Pageable pageable, Long[] majorIds, Long instituteId, String majorName, String instituteName) {
		String selectString = "select cm.*, sozy.code as code, sozy.name as majorName, soxy.name as instituteName, soxy.id instituteId, su.name as userName ";
		List<Object> params = Lists.newArrayList();
		StringBuilder exceptSql = new StringBuilder("from " + CcMajor.dao.tableName + " cm ");
		// 这里代表着：专业级别
		exceptSql.append("left join " + Office.dao.tableName + " sozy on sozy.id = cm.id ");
		// 这里代表着：学院级别
		exceptSql.append("left join " + Office.dao.tableName + " soxy on soxy.id = sozy.parentid ");
		exceptSql.append("left join " + User.dao.tableName + " su on su.id = cm.officer_id ");
		exceptSql.append("where cm.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and sozy.is_del =  ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and soxy.is_del =  ? ");
		params.add(Boolean.FALSE);
		if(instituteId != null) {
			exceptSql.append("and soxy.id = ? ");
			params.add(instituteId);
		}
		
		// 数据权限过滤
		if (majorIds.length > 0) {
			exceptSql.append("and cm.id in (" + CollectionKit.convert(majorIds, ",") + ") ");
		} else {
			exceptSql.append("and 0=1 ");
		}
		if(StrKit.notBlank(instituteName)){
			exceptSql.append("and soxy.name like '%" + StringEscapeUtils.escapeSql(instituteName) + "%'");
		}
		if(StrKit.notBlank(majorName)) {
			exceptSql.append("and sozy.name like '%" + StringEscapeUtils.escapeSql(majorName) + "%'");
		}
		
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcMajor.dao.paginate(pageable, selectString, exceptSql.toString(), params.toArray());

	}
	
	/**
	 * 获取本表所有数据
	 * @param officeType
	 * 			系统部门类型 
	 * @return
	 */
	@Deprecated
	public List<CcMajor> findByOfficeType(Integer officeType) {
		StringBuilder sb = new StringBuilder("select cm.*, so.parentid, so.code, so.name, so.type, so.is_system, so.description  from " + tableName + " cm ");
		sb.append("left join " + Office.dao.tableName + " so on so.id = cm.id ");
		sb.append("where so.type = ? ");
		return find(sb.toString(), officeType);
	}

	/**
	 * 同一个学校下专业名称是否重复
	 * @param schoolId
	 *           学校编号
	 * @param name
	 *          专业名称
	 * @param majorId
	 *          专业编号
	 * @return
	 */
	public boolean isExisted(Long schoolId, String name, Long majorId) {
		List<Object> param = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select count(1) from " + Office.dao.tableName + " major ");
		sql.append("inner join " + Office.dao.tableName + " institute on institute.id = major.parentid and institute.is_del = ? ");
        param.add(DEL_NO);
		sql.append("inner join " + Office.dao.tableName + " school on school.id = institute.parentid and school.id = ? and school.is_del = ? ");
		param.add(schoolId);
		param.add(DEL_NO);
		sql.append("where major.name = ? ");
		param.add(name);
		sql.append("and major.is_del = ? ");
		param.add(DEL_NO);
		if(majorId != null){
			sql.append("and major.id != ? ");
			param.add(majorId);
		}
		return Db.queryLong(sql.toString(), param.toArray()) > 0;
	}
	


	/**
	 * 找到专业对应的所有信息
	 * @param majorId
	 * 			专业编号
	 * @return
	 */
	public CcMajor findById(Long majorId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder sb = new StringBuilder("select cm.*, sozy.parentid as instituteId, sozy.code as code, sozy.name as majorName, soxy.name as instituteName, su.name as userName  from " + tableName + " cm ");
		// 这里代表着：专业级别
		sb.append("left join " + Office.dao.tableName + " sozy on sozy.id = cm.id ");
		// 这里代表着：学院级别
		sb.append("left join " + Office.dao.tableName + " soxy on soxy.id = sozy.parentid ");
		sb.append("left join " + User.dao.tableName + " su on su.id = cm.officer_id ");
		
		sb.append("where cm.id = ? ");
		params.add(majorId);
		sb.append("and cm.is_del = ? ");
		params.add(Boolean.FALSE);
		return findFirst(sb.toString(), params.toArray());
	}

	public CcMajor findByOfficerId(Long officerId) {
		List<Object> params = Lists.newArrayList();
		StringBuffer sql = new StringBuffer("select cm.*, sozy.code as code, sozy.parentid as instituteId, sozy.name as majorName, soxy.name as instituteName from " + tableName + " cm ");
		// 这里代表着：专业级别
		sql.append("left join " + Office.dao.tableName + " sozy on sozy.id = cm.id ");
		//这里代表：学院级别
		sql.append("left join " + Office.dao.tableName + " soxy on soxy.id = sozy.parentid ");
		sql.append("where cm.officer_id = ? ");
		params.add(officerId);
		sql.append("and cm.is_del = ? ");
		params.add(Boolean.FALSE);
		return findFirst(sql.toString(), params.toArray());
	}
	
	/**
	 * 根据持续版本获得专业信息
	 * 
	 * @param versionId 持续改进版本编号
	 * @return
	 */
	public CcMajor findByVersionId(Long versionId) {
		StringBuilder sql = new StringBuilder("select cm.*, so.name major_name, cpv.build_date build_date from " + tableName + " cm ");
		sql.append("inner join sys_office so on so.id = cm.id ");
		sql.append("left join cc_version cv on cv.major_id = cm.id ");
		sql.append("inner join cc_plan_version cpv on cpv.id = cv.id ");
		sql.append("where cpv.id = ?");
		return findFirst(sql.toString(), versionId);
	}

	/**
	 * 学校下所有专业
	 * @param schoolId
	 * @return
	 */
	public List<CcMajor> findBySchoolId(Long schoolId) {
		StringBuilder sql = new StringBuilder("select so.* from " + tableName + " cm ");
		sql.append("inner join " +  Office.dao.tableName + " so on so.id = cm.id and so.is_del = ? ");
		sql.append("inner join " + OfficePath.dao.tableName + " sop on sop.id = so.id ");
		sql.append("where sop.office_ids like '," + schoolId.toString() + ",%' ");
		sql.append("and cm.is_del = ? ");
		return find(sql.toString(), DEL_NO, DEL_NO);
	}
}
