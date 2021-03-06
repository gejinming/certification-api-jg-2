package com.gnet.model.admin;

import java.util.Date;
import java.util.List;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.gnet.utils.CollectionKit;
import com.gnet.pager.Pageable;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

import org.apache.commons.lang3.StringUtils;

import javax.xml.crypto.Data;


/**
 * 
 * @type model
 * @table cc_teacher_further_education
 * @author sll
 * @version 1.0
 * @date 2016年07月21日 21:08:48
 *
 */
@TableBind(tableName = "cc_teacher_further_education")
public class CcTeacherFurtherEducation extends DbModel<CcTeacherFurtherEducation> {

	private static final long serialVersionUID = -3958125598237390759L;
	public static final CcTeacherFurtherEducation dao = new CcTeacherFurtherEducation();
	
	/**
	 * 查看教师进修经历表列表分页
	 * @param pageable
	 * @param majorName
	 * @param educationType
	 * @param majorId
	 * @param majorIds
	 * 			专业编号列表，可以为空，用于用户属于比专业范围更大，比如学院等的时候
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年1月6日 下午6:52:39 
	 * @param instituteId 
	 */
	public Page<CcTeacherFurtherEducation> page(Pageable pageable, String majorName, Integer educationType, Long majorId, Long[] majorIds, Long instituteId) {
		List<Object> params = Lists.newArrayList();
		StringBuilder exceptSql = new StringBuilder("from " + CcTeacherFurtherEducation.dao.tableName + " ctfe ");
		exceptSql.append("inner join " + CcTeacher.dao.tableName + " ct on ct.id = ctfe.teacher_id ");
		exceptSql.append("left join " + Office.dao.tableName + " so on so.id = ct.major_id " );
		exceptSql.append("left join " + Office.dao.tableName + " institute on institute.id = ct.institute_id " );
		/**
		 * #5336   
		 * 现在修改后：只显示当前专业的教师，而不是显示专业认证的教师，由陈剑学长确认。
		 * 所以删掉left join CcMajorTeacher 和 CcVersion
		 * @date 2017年1月6日18:31:25
		 * @author SY
		 */
		
		exceptSql.append("where ctfe.is_del = ? and ct.is_del = ? ");
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		
		// 删选条件
		if (StrKit.notBlank(majorName)) {
			exceptSql.append("and so.name = ? ");
			params.add(majorName); 
		}
		if (educationType != null) {
			exceptSql.append("and ctfe.education_type = ? ");
			params.add(educationType);
		}
		
		if(majorId != null){
			exceptSql.append("and ct.major_id = ? ");
			params.add(majorId);
		}else if(instituteId != null){
			exceptSql.append("and ct.institute_id = ? ");
			params.add(instituteId);
		}else if(majorIds != null && majorIds.length > 0) {
			// 当发现专业编号为空，但是专业编号列表不为空的时候，直接in搜索判断
			exceptSql.append("and ct.major_id in ( " + CollectionKit.convert(majorIds, ",") +  " ) ");
		}
		
		
		if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcTeacherFurtherEducation.dao.paginate(pageable, "select ctfe.*, ct.name teacher_name, so.name major_name ", exceptSql.toString(), params.toArray());
	}

	/* (non-Javadoc)
	 * 根据编号查看详细
	 * 
	 * @see com.gnet.model.DbModel#findFilteredById(java.lang.Long)
	 */
	public CcTeacherFurtherEducation findFilteredById(Long id){
		StringBuilder exceptSql = new StringBuilder("select ctfe.*, ct.name teacher_name, so.name major_name from " + CcTeacherFurtherEducation.dao.tableName + " ctfe ");
		exceptSql.append("left join " + CcTeacher.dao.tableName + " ct on ct.id = ctfe.teacher_id ");
		exceptSql.append("left join " + Office.dao.tableName + " so on so.id = ct.major_id " );
		
		List<Object> params = Lists.newArrayList();
		exceptSql.append("where ctfe.is_del = ? and ct.is_del = ? and so.is_del = ? ");
		exceptSql.append("and ctfe.id = ? ");
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		params.add(id);
		
		return findFirst(exceptSql.toString(), params.toArray());
	}
	/*
	 * @param startData
		 * @param endData
		 * @param type
		 * @param majorId
	 * @return java.util.List<com.gnet.model.admin.CcTeacherFurtherEducation>
	 * @author Gejm
	 * @description: 根据开始与结束时间查询教师经历数据
	 * @date 2020/8/21 11:06
	 */
	public List<CcTeacherFurtherEducation> finaAllTeacherFurther(Integer startData,Integer endData,Integer type,Long majorId){
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("SELECT ctfe.*, ct.NAME teacher_name,so.NAME major_name FROM cc_teacher_further_education ctfe");
		sql.append(" INNER JOIN cc_teacher ct ON ct.id = ctfe.teacher_id ");
		sql.append("LEFT JOIN sys_office so ON so.id = ct.major_id ");
		sql.append("LEFT JOIN sys_office institute ON institute.id = ct.institute_id ");
		sql.append("WHERE ctfe.is_del = 0 AND ct.is_del = 0 ");
		if (type != null){
			sql.append("AND ctfe.education_type = ? ");
			params.add(type);
		}
		if (startData !=null){
			sql.append("and ctfe.start_time>=? ");
			params.add(startData);
		}
		if (endData != null){
			sql.append("and ctfe.end_time<=? ");
			params.add(endData);
		}
		if (majorId != null){
			sql.append("AND ct.major_id = ? ");
			params.add(majorId);
		}
		sql.append("ORDER BY  ct.id");
		return find(sql.toString(),params.toArray());


	}
	/*
	 * @param startData
	 * @param endData
	 * @param type
	 * @param majorId
	 * @return java.util.List<com.gnet.model.admin.CcTeacherFurtherEducation>
	 * @author Gejm
	 * @description: 根据开始与结束时间查询教师经历的个数
	 * @date 2020/8/21 11:06
	 */
	public List<CcTeacherFurtherEducation> finaAllTeacherFurtherNum(Integer startData, Integer endData, Integer type, Long majorId){
		List<Object> params = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("SELECT  teacher_id,count(teacher_id) mageNum from cc_teacher_further_education  ctfe ");
		sql.append("inner join cc_teacher ct ON ct.id = ctfe.teacher_id ");
		sql.append("WHERE ctfe.is_del = 0 AND ct.is_del = 0 ");
		if (type != null){
			sql.append("AND ctfe.education_type = ? ");
			params.add(type);
		}
		if (startData !=null){
			sql.append("and ctfe.start_time>=? ");
			params.add(startData);
		}
		if (endData != null){
			sql.append("and ctfe.end_time<=? ");
			params.add(endData);
		}
		if (majorId != null){
			sql.append("AND ct.major_id = ? ");
			params.add(majorId);
		}
		sql.append(" group by teacher_id ORDER BY ct.id ");
		return find(sql.toString(),params.toArray());
	}

}
