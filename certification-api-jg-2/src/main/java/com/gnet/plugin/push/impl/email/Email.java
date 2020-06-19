package com.gnet.plugin.push.impl.email;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.Address;

import com.gnet.plugin.push.PushModel;

/**
 * 邮箱
 * 
 * @author xuq
 * @date 2015年11月17日
 * @version 1.0
 */
public class Email extends PushModel {
	
	private static final long serialVersionUID = 5775814604986521308L;
	
	private String 				from;			// 发件人
	private Address[]	 		tos;			// 收件人
	private Address[]	 		ccs;			// 抄送人
	
	private String 				title;			// 标题
	private String 				contnet;		// 内容
	private Boolean 			html;			// 是否为HTML
	
	private List<File>			attachments;	// 附件列表
	
	public Email(String from, Address to, String title, String content) {
		this(from, to, title, content, false);
	}
	
	public Email(String from, Address to, String title, String content, Boolean html) {
		this(from, new Address[]{to}, null, title, content, html);
	}
	
	public Email(String from, Address to, String title, String content, Boolean html, File attachment) {
		this(from, new Address[]{to}, null, title, content, html, attachment);
	}
	
	public Email(String from, Address to, String title, String content, Boolean html, List<File> attachments) {
		this(from, new Address[]{to}, null, title, content, html, attachments);
	}
	
	public Email(String from, Address[] tos, String title, String content, Boolean html) {
		this(from, tos, null, title, content, html);
	}
	
	public Email(String from, Address[] tos, String title, String content, Boolean html, File attachment) {
		this(from, tos, null, title, content, html, attachment);
	}
	
	public Email(String from, Address[] tos, String title, String content, Boolean html, List<File> attachments) {
		this(from, tos, null, title, content, html, attachments);
	}
	
	public Email(String from, Address[] tos, Address[] ccs, String title, String content, Boolean html) {
		this(from, tos, ccs, title, content, html, new ArrayList<File>());
	}
	
	public Email(String from, Address[] tos, Address[] ccs, String title, String content, Boolean html, File attachment) {
		this(from, tos, ccs, title, content, html, Arrays.asList(attachment));
	}
	
	public Email(String from, Address[] tos, Address[] ccs, String title, String content, Boolean html, List<File> attachments) {
		this.from = from;
		this.tos = tos;
		this.ccs = ccs;
		
		this.title = title;
		this.contnet = content;
		this.html = html;
		
		this.attachments = attachments;
	}

	public String getFrom() {
		return from;
	}

	public Address[] getTos() {
		return tos;
	}

	public Address[] getCcs() {
		return ccs;
	}

	public String getTitle() {
		return title;
	}

	public String getContnet() {
		return contnet;
	}

	public Boolean getHtml() {
		return html;
	}

	public List<File> getAttachments() {
		return attachments;
	}
	
}