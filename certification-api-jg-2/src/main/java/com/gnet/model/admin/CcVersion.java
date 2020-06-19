package com.gnet.model.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
 * @description 版本表操作，包括对数据的增删改查与列表
 * @table cc_version
 * @author SY
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_version")
public class CcVersion extends DbModel<CcVersion> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcVersion dao = new CcVersion();
	
	/**
	 * 专业认证版本状态--编辑
	 */
	public final static Integer STATUE_EDIT = 1;
	/**
	 * 专业认证版本状态--发布
	 */
	public final static Integer STATUE_PUBLISH = 2;
	/**
	 * 专业认证版本状态--废弃
	 */
	public final static Integer STATUE_CLOSE = 3;
	
	/**
	 * 专业认证版本类型--大版本
	 */
	public final static Integer TYPE_MAJOR_VERSION = 1;
	/**
	 * 专业认证版本类型--小版本
	 */
	public final static Integer TYPE_MINOR_VERSION = 2;
	
	/**
	 * 大版本初始数值
	 */
	public final static Integer INITIAL_MAJOR = 1;
	/**
	 * 小版本初始数值
	 */
	public final static Integer INITIAL_MINOR = 0;
	/**
	 * 步长，每此增长多少
	 */
	public final static Integer VERSION_STEP = 1;
	
	/**
	 * 无小版本
	 */
	public final static Integer MINOR_VERSION_NULL = 0;
	
	/**
	 * 
	 * 级（含）以后用 + 替换
	 */
	public final static String GRADE_CHARACTER = "+";
	
	/**
	 * 适用年级之间的分割符
	 */
	public final static String SPLIT = ",";
	
	/**
	 * 版本的最小适用年级类型（只有一个年级时，用于区分是否是最小适用年级）
	 */
	public final static Integer MINGRADE = 1;
	
	/**
	 * 版本适用年级类型（只有一个年级时，用于区分是否是最小适用年级）
	 */
	public final static Integer GRADELIST = 2;
	
	/**
	 * 版本名称唯一性验证
	 * @param name
	 * @param originValue
	 * @return
	 */
	public boolean isExisted(String name, Long majorId, String originValue) {
		if (StrKit.notBlank(originValue)) {
			return Db.queryLong("select count(1) from cc_version where name = ? and name != ? and is_del = ? and major_id = ?  ",  name, originValue, Boolean.FALSE, majorId) > 0;
		} else {
			return Db.queryLong("select count(1) from cc_version where name = ? and is_del = ? and major_id = ? ",  name, Boolean.FALSE, majorId) > 0;
		}
	}
	
	/**
	 * 版本名称唯一性验证
	 * @param name
	 * @return
	 */
	public boolean isExisted(String name, Long majorId) {
		return isExisted(name, majorId, null);
	}
	
	
	/**
	 * 通过专业编号和状态获取数据
	 * @param versionId
	 * @return
	 */
	public CcVersion findFilteredById(Long versionId) {
		String selectString = "select cv.*, cgv.pass, cpv.name planName, cpv.course_version_name planCourseVersionName, cgv.name graduateName, cgv.indication_version_name graduateIndicationVersionName, office.name majorName, version.name parentName ";
		StringBuilder exceptSql = new StringBuilder("from " + CcVersion.dao.tableName + " cv ");
		exceptSql.append("left join " + CcGraduateVersion.dao.tableName + " cgv on cgv.id = cv.id ");
		exceptSql.append("left join " + CcPlanVersion.dao.tableName + " cpv on cpv.id = cv.id ");
		exceptSql.append("left join " + Office.dao.tableName + " office on cv.major_id = office.id ");
		exceptSql.append("left join " + CcVersion.dao.tableName + " version on version.id = cv.parent_id ");
		List<Object> params = Lists.newArrayList();

		exceptSql.append("where cv.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cgv.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cpv.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cv.id = ? ");
		params.add(versionId);
		
		exceptSql.append("order by cv.major_version desc, cv.minor_version desc ");
		
		return findFirst(selectString + exceptSql.toString(), params.toArray());
	}

	/**
	 * 查看版本列表分页
	 * @param pageable
	 * @param majorId
	 * @param statues
	 * @return
	 */
	public Page<CcVersion> page(Pageable pageable, Long majorId, Integer[] statues) {
		String selectString = "select cv.*, cpv.name planName, cgv.pass, cpv.course_version_name planCourseVersionName, cgv.name graduateName, cgv.indication_version_name graduateIndicationVersionName, so.name major_name ";
		StringBuilder exceptSql = new StringBuilder("from " + CcVersion.dao.tableName + " cv ");
		exceptSql.append("left join " + CcGraduateVersion.dao.tableName + " cgv on cgv.id = cv.id ");
		exceptSql.append("left join " + CcPlanVersion.dao.tableName + " cpv on cpv.id = cv.id ");
		exceptSql.append("left join " + Office.dao.tableName + " so on so.id = cv.major_id " );
		List<Object> params = Lists.newArrayList();

		exceptSql.append("where cv.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cgv.is_del = ? ");
		params.add(Boolean.FALSE);
		exceptSql.append("and cpv.is_del = ? ");
		params.add(Boolean.FALSE);
		
		// 筛选条件
		if(majorId != null) {
			exceptSql.append("and cv.major_id = ? ");
			params.add(majorId);
		}
		// 筛选条件
		if(statues != null && statues.length > 0) {
			exceptSql.append("and cv.state in (" + CollectionKit.convert(statues, ",") + ") ");
		}
		
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		} else {
			exceptSql.append("order by cv.major_version desc, cv.minor_version desc ");
		}
		
		return CcVersion.dao.paginate(pageable, selectString, exceptSql.toString(), params.toArray());

	}
	
	/**
	 *只显示没有小版本的大版本或者大版本里面最新的小版本列表
	 * @param pageable
	 * @param majorId
	 * @param statues
	 * @return
	 */
	public Page<CcVersion> versionPage(Pageable pageable, Long majorId, Integer[] statues) {
		
		String selectString = "select cv.*, cpv.name planName, cgv.pass, cpv.course_version_name planCourseVersionName, cgv.name graduateName, cgv.indication_version_name graduateIndicationVersionName, cgv.is_del, cpv.is_del, office.name major_name, pcv.id parentId, pcv.name parentName, pcv.is_del parentIsDel ";
	
		StringBuilder sb = new StringBuilder(" from " + CcVersion.dao.tableName + " cv "); 
		sb.append("left join " + CcVersion.dao.tableName + " pcv on pcv.id = cv.parent_id ");
		sb.append("left join " + CcGraduateVersion.dao.tableName + " cgv on cgv.id = cv.id ");
		sb.append("left join " + CcPlanVersion.dao.tableName + " cpv on cpv.id = cv.id ");
		sb.append("left join " + Office.dao.tableName + " office on office.id = cv.major_id ");
		List<Object> params = Lists.newArrayList();
		
		//大版本
		sb.append(" where ( (cv.type = ? and cv.max_minor_version= ?)  ");
		//小版本
		sb.append(" or (cv.type = ? and cv.minor_version = cv.max_minor_version) ) ");
		sb.append(" and cv.is_del = ? and cgv.is_del = ? and cpv.is_del = ? ");
		params.add(CcVersion.TYPE_MAJOR_VERSION);
		params.add(CcVersion.MINOR_VERSION_NULL);
		params.add(CcVersion.TYPE_MINOR_VERSION);
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		
		// 筛选条件
		if(majorId != null) {
			sb.append("and cv.major_id = ? ");
			params.add(majorId);
		}
		// 筛选条件
		if(statues != null && statues.length > 0) {
			sb.append("and cv.state in (" + CollectionKit.convert(statues, ",") + ") ");
		}
		/*
		 * Edit By SY for Bug #5080, cj学长表示，父亲现在不再要求未删除或者不存在，可以随便了……
		 * 2016年12月13日14:04:23
		 * sb.append("and ( pcv.is_del = ? or pcv.is_del is null ) ");
		 * params.add(Boolean.FALSE);
		 */
		
		if (StrKit.notBlank(pageable.getOrderProperty())) {
			sb.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
		}
		
		return CcVersion.dao.paginate(pageable, selectString, sb.toString(), params.toArray());
	}

	/**
	* 查找大版本下最新的小版本编号
	 * @param majorId 
	 * 
	 * @return
	 */
	public Integer findFilteredMinorVersion(Integer majorVersion, Long majorId) {
		return Db.queryInt("select max(minor_version) from " + CcVersion.dao.tableName + " where state != ? and is_del = ? and major_version = ? and major_id = ?", CcVersion.STATUE_CLOSE, Boolean.FALSE, majorVersion, majorId);
	}
	
	/**
	 * 更新同一大版本下的最新的小版本编号
	 * @return
	 */
	public Boolean updateMaxMinorVersion(Integer maxMinorVersion, Integer majorVersion, Long majorId){
		Date date = new Date();
		return Db.update("update " + CcVersion.dao.tableName + " set max_minor_version = ?, modify_date = ? where major_version = ? and major_id = ? ", maxMinorVersion, date, majorVersion, majorId) >= 0;
	}


	/**
	 * 通过课程编号得到专业编号和课程代码
	 * @param courseId
	 *           课程编号
	 * @return
	 */
	public CcVersion findMajorIdByCourseId(Long courseId) {
		List<Object> param = Lists.newArrayList();
	    StringBuffer sql = new StringBuffer("select cv.major_id majorId, cc.code from " + tableName + " cv ");
	    sql.append("left join " + CcCourse.dao.tableName + " cc on cc.plan_id = cv.id ");
	    sql.append("where cc.id = ? ");
	    param.add(courseId);
	    sql.append("and cv.is_del =  ? ");
        param.add(Boolean.FALSE);
        sql.append("and cc.is_del = ? ");
        param.add(Boolean.FALSE);
        return findFirst(sql.toString(), param.toArray());
	}

	/**
	 * 查询该专业年级最新的持续改进版本编号
	 * @param majorId
	 *           专业编号
	 * @param grade
	 *           年级
	 * @return
	 */
	public Long findNewestVersion(Long majorId, Integer grade) {
		List<Object> param = Lists.newArrayList();
		StringBuilder sql = new StringBuilder("select id from " + tableName + " where major_id = ? ");
		param.add(majorId);
		if(grade != null){
			sql.append("and enable_grade <= ? ");
			param.add(grade);
		}
		sql.append("and is_del = ? ");
		param.add(DEL_NO);
		sql.append("and minor_version = max_minor_version ");
		sql.append("order by enable_grade desc");
		return Db.queryLong(sql.toString(), param.toArray());
	}



	/**
	 * 查询专业下所有最新的版本
	 * @param majorIds
	 * @return
	 */
	public List<CcVersion> findByMajorIds(Long[] majorIds) {
		String sql = "select * from " + tableName + " where major_id in (" + CollectionKit.convert(majorIds, ",") + ") and is_del = ? and minor_version = max_minor_version ";
        return find(sql, DEL_NO);
	}

	/**
	 * 某个专业启用年级最大的版本
	 * @param majorId
	 * @return
	 */
	public CcVersion findMaxGradeByMajorId(Long majorId) {
		StringBuilder sql = new StringBuilder("select * from " + tableName + " cv ");
		sql.append("where cv.major_id = ? ");
		sql.append("and cv.is_del = ? ");
		sql.append("order by cv.enable_grade desc ");
		return findFirst(sql.toString(), majorId, Boolean.FALSE);
	}

	/**
	 * 某个专业启用年级最小的版本
	 * @param majorId
	 * @return
	 */
	public CcVersion findMinGradeByMajorId(Long majorId) {
		StringBuilder sql = new StringBuilder("select * from " + tableName + " cv ");
		sql.append("where cv.major_id = ? ");
		sql.append("and cv.is_del = ? ");
		sql.append("order by cv.enable_grade ");
		return findFirst(sql.toString(), majorId, Boolean.FALSE);
	}

	/**
	 * 返回小于版本的版本列表
	 * @param majorVersion
	 * @return
	 * @Deletor SY：暂时没用啊？要不删了？谁创建的，看到这个确定一下是否删除
	 */
	@Deprecated
	public List<CcVersion> findPreviousVersion(Integer majorVersion, Long majorId) {
		return find("select * from " + tableName + " where major_version < ? and major_id = ? and is_del = ? order by major_version desc, minor_version desc ", majorVersion, majorId, Boolean.FALSE);
	}

	/**
	 * 返回这个大版本上一个大版本的最新未删除小版本
	 * @param majorId
	 * 			专业编号
	 * @param majorVersion
	 * 			大版本号
	 * @param isAll
	 * 			是否显示所有，（是：包括删除的，否：未删除的）
	 * @return
	 * @author SY 
	 * @version 创建时间：2016年11月9日 下午2:47:45 
	 */
	public CcVersion findBefore(Long majorId, Integer majorVersion) {
		return findBefore(majorId, majorVersion, Boolean.FALSE);
	}
	
	/**
	 * 返回这个大版本上一个大版本的最新小版本
	 * @param majorId
	 * 			专业编号
	 * @param majorVersion
	 * 			大版本号
	 * @param isAll
	 * 			是否显示所有，（是：包括删除的，否：未删除的）
	 * @return
	 * @author SY 
	 * @version 创建时间：2016年11月9日 下午2:47:45 
	 */
	public CcVersion findBefore(Long majorId, Integer majorVersion, Boolean isAll) {
		if(majorVersion == null || majorVersion <= 1) {
			return null;
		}
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select cv.* from " + tableName + " cv ");
		sql.append("where cv.major_id = ? ");
		params.add(majorId);
		if(isAll == null || !isAll) {
			sql.append("and cv.is_del = ? ");
			params.add(Boolean.FALSE);
		}
		// 增加限制条件
		sql.append("and cv.major_version < ? ");
		params.add(majorVersion);
		sql.append("order by cv.major_version desc, cv.minor_version desc ");
		return findFirst(sql.toString(), params.toArray());
	}

	/**
	 * 某个专业某个大版本的名称
	 * @param majorId
	 * @param majorVersion
	 * @return
	 */
	public CcVersion findVersion(Long majorId, Integer majorVersion, Integer minorVersion) {
		String sql = "select * from " + tableName + " where major_id = ? and major_version = ? and minor_version = ? and is_del = ? ";
		return findFirst(sql, majorId, majorVersion, minorVersion, Boolean.FALSE);
	}

	/**
	 * 通过专业编号和适用年级查找版本
	 * @param majorId
	 * @param grade
	 * @return
	 */
	public CcVersion findByGradeAndMajorId(Long majorId, Integer grade) {
		// Edit by SY . BUG 6431. 逻辑改成，找到最接近这个年级的  启用年级 的版本。
		// 即，如果grade是2016，则找到数据库中enable_grade 最接近 2016，但是不超过2016的
//		String sql = "select * from " + tableName + " where major_id = ? and enable_grade = ? and is_del = ? ";
		String sql = "select * from " + tableName + " where major_id = ? and enable_grade <= ? and is_del = ? order by enable_grade desc ";
		return findFirst(sql, majorId, grade, DEL_NO);
	}

	/**
	 * 通过教学班编号找到对应的数据
	 * @param eduClassId
	 * @return
	 * @author SY 
	 * @version 创建时间：2017年12月4日 下午6:02:20 
	 */
	public CcVersion findByEduClassId(Long eduClassId) {
		List<Object> params = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select cv.*, ctc.grade from " +  CcEduclass.dao.tableName + " ce ");
		sql.append("inner join " + CcTeacherCourse.dao.tableName + " ctc on  ctc.id = ce.teacher_course_id and ctc.is_del = ? ");
		params.add(DEL_NO);
		sql.append("inner join " + CcCourse.dao.tableName + " cc on cc.id = ctc.course_id and cc.is_del = ? ");
		params.add(DEL_NO);
		sql.append("inner join " + tableName + " cv on cc.plan_id = cv.id and cv.is_del = ? ");
		params.add(DEL_NO);
		// 增加限制条件
		sql.append("where ce.id = ? ");
		params.add(eduClassId);
		return findFirst(sql.toString(), params.toArray());
	}
}
