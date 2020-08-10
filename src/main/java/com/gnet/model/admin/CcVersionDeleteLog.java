package com.gnet.model.admin;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.gnet.model.DbModel;
import com.gnet.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;


/**
 * 
 * @type model
 * @table cc_version_delete_log
 * @author SY
 * @version 1.0
 * @date 2016年8月17日19:09:00
 *
 */
@TableBind(tableName = "cc_version_delete_log")
public class CcVersionDeleteLog extends DbModel<CcVersionDeleteLog> {

	private static final long serialVersionUID = -3958122268257390759L;
	public static final CcVersionDeleteLog dao = new CcVersionDeleteLog();
	

	/**
	 * 删除状态--未开始
	 */
	public static final Integer TYPE_STATUS_NOT_START = 0;
	/**
	 * 删除状态--删除中
	 */
	public static final Integer TYPE_STATUS_DELETING = 1;
	/**
	 * 删除状态--成功
	 */
	public static final Integer TYPE_STATUS_SUCCESS = 2;
	/**
	 * 删除状态--失败
	 */
	public static final Integer TYPE_STATUS_FAIL = 3;
	/**
	 * 删除状态--成功_已读
	 */
	public static final Integer TYPE_STATUS_SUCCESS_READ = 4;
	/**
	 * 删除状态--失败_已读
	 */
	public static final Integer TYPE_STATUS_FAIL_READ = 5;
	
	/**
	 * 步骤--删除的时候总共有多少步
	 */
	public static final Integer STEP_ALL_NUM = 30;
	
	
	/*
	 *  注意！步骤进行到第几步，都按照CcVersionGreateLog里面的来
	 */

	/**
	 * 通过majorId获取当前专业下的所有正在创建/未开始/操作失败/操作成功的versionLog（但现在的道理，应该只有一个！2016年8月17日20:00:50）
	 * @param majorId
	 * @return
	 * @author SY
	 * @date 2016年12月14日16:24:39
	 */
	public List<CcVersionDeleteLog> findAndReadByMajorId(Long majorId) {
		List<CcVersionDeleteLog> returnList = findByMajorId(majorId);
		// 把日志中成功和失败的，变成状态成功和失败的已读状态， 不需要知道返回的结果，因为可能不存在，可能存在。
		readByMajorId(majorId);
		return returnList;
	}
	
	/**
	 * 通过majorId获取当前专业下的所有正在删除/未开始/操作失败/操作成功的versionLog（但现在的道理，应该只有一个！2016年8月17日20:00:50）
	 * @param majorId
	 * @return
	 */
	public List<CcVersionDeleteLog> findByMajorId(Long majorId) {
		StringBuilder sb = new StringBuilder("select cvd.* from " + tableName + " cvd ");
		sb.append("left join " + CcVersion.dao.tableName + " cv on cv.id = cvd.id ");
		sb.append("where cv.major_id = ? and cvd.delete_status in (" + CcVersionDeleteLog.TYPE_STATUS_DELETING + ", " + CcVersionDeleteLog.TYPE_STATUS_NOT_START + ", " + CcVersionDeleteLog.TYPE_STATUS_SUCCESS + ", " + CcVersionDeleteLog.TYPE_STATUS_FAIL + ") ");
		return find(sb.toString(), majorId);
	}

	/**
	 * 通过majorId修改当前专业下的所有操作失败/操作成功的versionLog变成操作失败/操作成功的已读状态（但现在的道理，应该只有一个！2016年8月17日20:00:50）
	 * @param majorId
	 * @return 返回是否已有操作成功的数据（注意，如果不存在数据，也是返回false）
	 * @author SY 
	 * @version 创建时间：2016年12月14日 下午2:21:49 
	 */
	private Boolean readByMajorId(Long majorId) {
		Date date = new Date();
		Object[][] op = new Object[2][4];
		StringBuilder sql = new StringBuilder("update " + tableName + " set modify_date = ? ");
		sql.append(", delete_status = ? ");
		sql.append("where major_id = ? ");
		sql.append("and delete_status = ? ");
		
		op[0][0] = date;
		op[0][1] = CcVersionDeleteLog.TYPE_STATUS_SUCCESS_READ;
		op[0][2] = majorId;
		op[0][3] = CcVersionDeleteLog.TYPE_STATUS_SUCCESS;
		
		op[1][0] = date;
		op[1][1] = CcVersionDeleteLog.TYPE_STATUS_FAIL_READ;
		op[1][2] = majorId;
		op[1][3] = CcVersionDeleteLog.TYPE_STATUS_FAIL;
		
		int[] result = Db.batch(sql.toString(), op);
		if (ArrayUtils.contains(result, 0)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 修改步骤内容
	 * @param id
	 * @param date
	 * @param deleteMessage
	 * @param deleteStep
	 */
	public Boolean updateStep(Long id, Date date, String deleteMessage, Integer deleteStep) {
		return Db.update("update " + tableName + " set modify_date = ? , delete_step = ? , delete_message = ?, delete_status = ?  where id = ? and delete_step < ?", date, deleteStep, deleteMessage, CcVersionDeleteLog.TYPE_STATUS_DELETING, id, deleteStep) > 0;
	}
	
	/**
	 * 版本删除完成的时候修改日志为完成状态
	 * @param id
	 * @param date
	 * @param deleteMessage
	 * @param deleteStep
	 * @return
	 */
	public Boolean finishStep(Long id, Date date, String deleteMessage, Integer deleteStep) {
		return Db.update("update " + tableName + " set modify_date = ? , delete_step = ? , delete_message = ?, delete_status = ?  where id = ? and delete_step < ?", date, deleteStep, deleteMessage, CcVersionDeleteLog.TYPE_STATUS_SUCCESS, id, deleteStep) > 0;
	}

	/**
	 * 废弃操作的时候，出错了，修改步骤内容
	 * @param id
	 * @param date
	 * @param deleteMessage
	 * @param deleteStep
	 * @return
	 */
	public Boolean updateStepForError(Long id, Date date, String deleteMessage, Integer deleteStep) {
		return Db.update("update " + tableName + " set modify_date = ? , delete_step = ? , delete_message = concat( delete_message , ?), delete_status = ?  where id = ? ", date, deleteStep, deleteMessage, CcVersionDeleteLog.TYPE_STATUS_FAIL, id) > 0;
	}

	/**
	 * 验证当前专业下，是否有正在删除的版本
	 * @param majorId
	 * @return
	 */
	public Boolean validateDeletingByMajorId(Long majorId) {
		StringBuilder sb = new StringBuilder("select count(1) from " + tableName + " cvd ");
		sb.append("left join " + CcVersion.dao.tableName + " cv on cv.id = cvd.id ");
		sb.append("where cv.major_id = ? and cvd.delete_status in (" + CcVersionDeleteLog.TYPE_STATUS_DELETING + ", " + CcVersionDeleteLog.TYPE_STATUS_NOT_START + ") ");
		return Db.queryLong(sb.toString(), majorId) > 0;
	}

}
