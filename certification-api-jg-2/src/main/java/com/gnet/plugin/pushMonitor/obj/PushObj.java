package com.gnet.plugin.pushMonitor.obj;

import java.io.Serializable;
import java.util.Date;

/**
 * Push数据库抽象对象
 * 
 * @author xuq
 * @date 2015年12月30日
 * @version 1.1
 */
public abstract class PushObj implements Serializable {

	private static final long serialVersionUID = 851628646580440784L;

	private Long id;
	private Date createDate;
	private Date modifyDate;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
}
