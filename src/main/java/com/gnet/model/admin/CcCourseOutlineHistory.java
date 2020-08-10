package com.gnet.model.admin;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;

/**
 * @type model
 * @description 课程教学大纲文本历史表操作
 * @table cc_course_outline
 * @author SY
 * @version 1.0
 *
 */
@TableBind(tableName = "cc_course_outline_history")
public class CcCourseOutlineHistory extends DbModel<CcCourseOutlineHistory> {

	private static final long serialVersionUID = -3958125598237390759L;
	public final static CcCourseOutlineHistory dao = new CcCourseOutlineHistory();

	/**
	 * 指派
	 */
    public static final Integer TYPE_ASSIGN = 0;
   
    /**
	 * 教师提交大纲
	 */
    public static final Integer TYPE_SUBMIT = 1;
    
    /**
	 * 审核通过
	 */
	public static final Integer TYPE_AUDIT_PASS = 2;
	
	/**
	 * 审核驳回
	 */
	public static final Integer TYPE_AUDIT_DISMISSED = 3;
    
	/**
	 * 删除
	 */
	public static final Integer TYPE_DELETE = 4;
    
	/**
	 * 强制提交
	 */
	public static final Integer TYPE_FORCE_SUBMIT = 5;
	
	/**
	 * 教师编写大纲
	 */
    public static final Integer TYPE_COMPILE = 6;
   
    /**
     * 历史版本复制（当专业认证版本复制时会用到的状态）
     */
    public static final Integer TYPE_VERSION_COPY = 7;
    
    /**
     * 确认指派（当版本复制后会对之前的教学大纲执笔人进行确认）
     */
    public static final Integer TYPE_CONFIRM_ASSIGN = 8; 
    
    /**
     * 下发任务
     */
    public static final Integer TYPE_ISSUE_TASK = 9;
    
    /**
     * 新建
     */
    public static final Integer TYPE_NEW_BUILD = 10;
    
    /**
     * 撤回
     */
    public static final Integer TYPE_WITHDRAW = 11;

	/**
	 * 负责人创建了大纲
	 */
	public static final Integer TYPE_CREATE = 12;

	/**
	 * 负责人编辑了大纲
	 */
    public static final Integer TYPE_EDIT = 13;

	/**
	 * 负责人强制把审核通过改为待审核
	 */
	public static final Integer TYPE_FORCE_PENDING_AUDIT = 14;
}
