package com.gnet.model.admin;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.gnet.Constant;
import com.gnet.model.DbModel;
import com.gnet.pager.Pageable;
import com.gnet.plugin.tablebind.TableBind;
import com.google.common.collect.Lists;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

/**
 * @type model
 * @description 用户model，可管理用户信息，包括批量增加、批量删除，更改密码等
 * @table sys_user
 * @author cwledit (cwledit@gmail.com)
 * @version 1.0
 *
 */
@TableBind(tableName = "sys_user")
public class User extends DbModel<User> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static User dao = new User();

	/**
	 * 用户默认密码
	 */
	public static final String DEFAULT_PASSWORD = "1234";
	/**
	 * 用户类型——管理员
	 */
	public static final Integer TYPE_ADMIN = 0;
	/**
	 * 用户类型——普通用户
	 */
	public static final Integer TYPE_NORMAL = 1;
	/**
	 * 分割符常量
	 * 
	 */
	public static final String SPLIT =  "-";
	/**
	 * 管理员默认登入名
	 */
	public static final String ADMIN_LOGINNAME = "admin";
	
	/**
	 * 离职
	 */
	public static final String LEAVE = "1";
	
	/**
	 * 启用
	 */
	public static final String ENABLE = "1";
	
	/**
	 * 创建时是否存在此登录名
	 * 
	 * @description 根据登录名判断是否存在该用户
	 * @sql select count(1) from sys_user where loginName=?
	 * @version 1.0
	 * @param loginName
	 * @return
	 */
	public boolean isExisted(String loginName) {
		return isExisted(loginName, null);
	}

	/**
	 * 更新时是否存在此登录名
	 * 
	 * @description 根据登录名判断是否存在该用户
	 * @sql select count(1) from sys_user where loginName=?
	 * @version 1.0
	 * @param loginName
	 * @return
	 */
	public boolean isExisted(String loginName, String originValue) {
		if (StrKit.notBlank(originValue)) {
			return Db.queryLong("select count(1) from " + tableName + " where loginName=? and is_del=? and loginName!=?", loginName, DEL_NO, originValue) > 0;
		} else {
			return Db.queryLong("select count(1) from " + tableName + " where loginName=? and is_del=?", loginName, DEL_NO) > 0;
		}
	}
	
	/**
	 * 验证在同一学校中，loginName是否已经存在
	 * 
	 * @param loginName
	 * @param originValue
	 * @param schoolId
	 * @return
	 */
	public Boolean isExisted(String loginName, String originValue, Long schoolId ){
		
		StringBuilder sb = new StringBuilder("select count(1) from " + tableName + " su ");
		sb.append(" left join " + Office.dao.tableName + " so on so.id = su.department ");
		sb.append(" where loginName = ? and so.is_del = ? and su.is_del = ? ");
		sb.append(" and so.parentid = ? ");
		
		List<Object> params = Lists.newArrayList();
		params.add(loginName);
		params.add(Boolean.FALSE);
		params.add(Boolean.FALSE);
		params.add(schoolId);
		
		if (StrKit.notBlank(originValue)) {
			sb.append(" and loginName != ? ");
			params.add(originValue);
		}
		
		return Db.queryLong(sb.toString(), params.toArray()) > 0;
	}
	

	/**
	 * 通过登陆名查找用户
	 * 
	 * @description 根据登录名查找用户
	 * @sql select * from sys_user where loginName = ? and is_locked = 0 and
	 *      is_enabled = 1 and " + User.isdel =0
	 * @version 1.0
	 * @param loginName
	 * @return
	 */
	public User findByLoginName(String loginName) {
		if (StringUtils.isEmpty(loginName)) {
			return null;
		}

		String sql = " select * from " + tableName + " where loginName = ? and is_locked = ? and is_enabled = ? and " + User.IS_DEL_LABEL + "=? ";
		return findFirst(sql, loginName, Constant.UNLOCKED, Constant.USER_ENABLED, User.DEL_NO);
	}

	/**
	 * 是否存人，是在这个部门里的
	 * 
	 * @description 根据部门查找该部门是否存在用户
	 * @sql select count(1) from sys_user where department=?
	 * @version 1.0
	 * @param office
	 * @return
	 */
	public boolean hasOffice(String office) {
		long count = Db.queryLong("select count(1) from " + tableName + " where department=?", office);
		return count > 0;
	}

	/**
	 * 是否存人，是在这些部门里的
	 * 
	 * @description 根据一系列部门，查找这一系列部门中是否存在用户
	 * @sql select count(1) from sys_user where department=?
	 * @version 1.0
	 * @param offices
	 * @return
	 */
	public boolean hasOffices(String[] offices) {
		for (String office : offices) {
			if (hasOffice(office)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 更改密码
	 * 
	 * @description 通过用户编号更改用户密码
	 * @sql update sys_user set password=? where id=? and password=?
	 * @version 1.0
	 * @param id
	 *            用户编号
	 * @param oldPsd
	 *            旧密码
	 * @param newPsd
	 *            新密码
	 * @return 修改密码是否正确
	 */
	public boolean changePsd(Long id, String oldPsd, String newPsd) {
		return Db.update("update " + tableName + " set password=? where id=? and password=? ", newPsd, id, oldPsd) > 0;
	}

	@Override
	public String toString() {
		return getStr("loginName");
	}

	/* 
	 * 根据用户编号查找用户（包括查出office_ids的值）
	 * 
	 * @see com.gnet.model.DbModel#findFilteredById(java.lang.Long)
	 */
	public User findFilteredById(Long userId){
		StringBuilder sb = new StringBuilder("select user.*, op.office_ids from " + User.dao.tableName + " user ");
		sb.append(" left join " + OfficePath.dao.tableName + " op on op.id = user.department " );
		
		List<Object> params = Lists.newArrayList();
		sb.append(" where user.id = ? ");
		params.add(userId);
		
		sb.append(" and user.is_del = ? ");
		params.add(Boolean.FALSE);
		
		return findFirst(sb.toString(), params.toArray());
	}
	
	/**
	 * 查看用户列表分页
	 * 
	 * @param pageable
	 * @param loginName
	 * @param email
	 * @param name
	 * @return
	 */
    public Page<User> page(Pageable pageable, String loginName, String email, String name,String schoolId) {
        StringBuilder exceptSql = new StringBuilder("from " + User.dao.tableName + " su ");
        exceptSql.append("left join sys_office so on so.id = su.department ");
        exceptSql.append("left join sys_user_role sur on sur.user_id = su.id ");
        exceptSql.append("left join cc_school cc on LEFT(su.loginName,6) = cc.id ");
        exceptSql.append("where 1=1 ");
        List<Object> params = Lists.newArrayList();

        // 删选条件
        if (!StrKit.isBlank(loginName)) {
            exceptSql.append("and su.loginName like '%" + org.apache.commons.lang.StringEscapeUtils.escapeSql(loginName) + "%' ");
        }
        if (!StrKit.isBlank(email)) {
            exceptSql.append("and su.email like '" + StringEscapeUtils.escapeSql(email) + "%' ");
        }
        if (!StrKit.isBlank(name)) {
            exceptSql.append("and su.name like '" + StringEscapeUtils.escapeSql(name) + "%' ");
        }
        if (!StrKit.isBlank(schoolId)){
            exceptSql.append("and cc.id = '" + StringEscapeUtils.escapeSql(schoolId) + "' ");
        }

        // 增加条件，不显示超级管理员
        exceptSql.append(" and su.loginName != '" + Constant.USER_ADMIN + "' ");
        // 增加条件，为非软删除的
        exceptSql.append("and su.is_del=? ");
        params.add(Boolean.FALSE);

        if (StringUtils.isNotBlank(pageable.getOrderProperty())) {
            exceptSql.append("order by " + pageable.getOrderProperty() + " " + pageable.getOrderDirection());
        }

        return User.dao.paginate(pageable, "select su.*, so.name as department_name, sur.roles roles ", exceptSql.toString(), params.toArray());
    }
	/**
	 * 学生登录验证
	 *
	 * @param studentNos
	 * @param schoolId
	 * @return
	 */
	public User existedStudents(String studentNos, String password,String schoolId) {
		List<Object> param = Lists.newArrayList();

		StringBuilder sql = new StringBuilder("select cs.*, so.name class_name from " + CcStudent.dao.tableName + " cs ");
		sql.append("left join sys_office so on so.id = cs.class_id ");
		sql.append("left join sys_office_path sop on sop.id = so.id ");
		sql.append("where sop.office_ids like '," + schoolId + "%' ");
		sql.append("and cs.student_no = ? and password=? and cs.is_del=? and statue=1 ");
		param.add(studentNos);
		param.add(password);
		param.add(Boolean.FALSE);
		return findFirst(sql.toString(), param.toArray());
	}
}
