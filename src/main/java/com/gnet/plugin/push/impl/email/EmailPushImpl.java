package com.gnet.plugin.push.impl.email;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.gnet.plugin.configLoader.ConfigUtils;
import com.gnet.plugin.push.IPushMonitor;
import com.gnet.plugin.push.Push;
import com.gnet.plugin.push.impl.email.exception.PushFailException;
import com.jfinal.log.Logger;

/**
 * Email推送
 * 
 * @author xuq
 * @date 2015年11月16日
 * @version 2.0
 * @changelog 增加SSL的支持 增加QQ的支持 增加授权码的支持
 */
public class EmailPushImpl extends Push {

	private static final Logger logger = Logger.getLogger(EmailPushImpl.class);

	private static final String CONTENT_TYPE_HTML = "text/html;charset=utf-8";
	private static final String CONNECT_SMTP_SOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";
	private static final String CONNECT_SMTP_SSL_PORT = "465";
	private static final String CONNECT_SMTP_PORT = "";

	private Properties 		emailConfig; 		// Email推送全局配置

	private List<Email> 	emails; 			// 邮箱内容

	private String			authUser;			// 认证用户
	private String 			authPasswd; 		// 认证密码/授权码

	/**
	 * Email推送实例化<br/>
	 * 需要邮件和认证密码
	 * 
	 * @param email
	 *            邮件
	 * @param auth
	 *            认证密码
	 */
	public EmailPushImpl(Email email, String authUser, String authPasswd) {
		this(Arrays.asList(email), authUser, authPasswd);
	}

	/**
	 * Email推送实例化<br/>
	 * 需要邮件列表和认证密码
	 * 
	 * @param emails
	 *            邮件列表
	 * @param auth
	 *            认证密码
	 */
	public EmailPushImpl(List<Email> emails, String authUser, String authPasswd) {
		super();
		this.emailConfig = ConfigUtils.getProps("pushemail");

		this.emails = emails;

		this.authUser = authUser;
		this.authPasswd = authPasswd;
	}

	@Override
	public void push(IPushMonitor monitor) {
		if (monitor != null) {
			monitor.pushStart();
		}
		Properties properties = new Properties();
		properties.setProperty("mail.debug", emailConfig.getProperty("dev_mode", "false"));
		properties.setProperty("mail.smtp.auth", emailConfig.getProperty("mail.smtp.auth", "true"));
		properties.setProperty("mail.host", emailConfig.getProperty("mail.host"));
		properties.setProperty("mail.transport.protocol", emailConfig.getProperty("mail.transport.protocol"));
		if (Boolean.valueOf(emailConfig.getProperty("mail.ssl.enable", "false"))) {
			// enable ssl support
			properties.setProperty("mail.smtp.socketFactory.port", emailConfig.getProperty("mail.smtp.socketFactory.port", CONNECT_SMTP_SSL_PORT));
			properties.setProperty("mail.smtp.socketFactory.class", emailConfig.getProperty("mail.smtp.socketFactory.class", CONNECT_SMTP_SOCKET_FACTORY));
			properties.setProperty("mail.smtp.port", properties.getProperty("mail.smtp.socketFactory.port"));
		} else {
			properties.setProperty("mail.smtp.port", emailConfig.getProperty("mail.smtp.port", CONNECT_SMTP_PORT));
		}

		// 重新构建发送session
		Session session = Session.getInstance(properties, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(authUser, authPasswd);
			}
		});
		
		Integer delivered = 0;
		Integer allToDelivered = emails.size();
		EmailDelivered emailDelivered = new EmailDelivered(EmailDelivered.NOT_DELIVERED, delivered, allToDelivered);
		for (Email email : emails) {
			final String from = email.getFrom();
			final Address[] tos = email.getTos();
			final Address[] ccs = email.getCcs();

			final String title = email.getTitle();

			Message msg = new MimeMessage(session);
			try {
				msg.setSubject(title);							// 设置标题
				setContent(email, msg);							// 设置内容和附件
				
				msg.setFrom(new InternetAddress(from)); 		// 禁止使用多个发件人
				msg.setRecipients(RecipientType.TO, tos); 		// 设置收件人
				if (ccs != null && ccs.length > 0) {
					msg.setRecipients(RecipientType.CC, ccs); 	// 如果存在抄送人，设置抄送人
				}
			} catch (MessagingException e) {
				if (logger.isErrorEnabled()) {
					logger.error(e.getMessage(), e);
				}
				if (monitor != null) {
					monitor.pushFail(email, e);
				}
				continue;
			} catch (UnsupportedEncodingException e) {
				if (logger.isErrorEnabled()) {
					logger.error(e.getMessage(), e);
				}
				if (monitor != null) {
					monitor.pushFail(email, e);
				}
				continue;
			}
			pushItem(session, msg, email, emailDelivered, monitor);
		}
		
		if (monitor != null) {
			monitor.pushFinish();
		}
	}

	/**
	 * 设置邮件内容和附件
	 * 
	 * @param email
	 *            邮件
	 * @param msg
	 *            消息
	 * @throws MessagingException
	 *             邮件异常
	 * @throws UnsupportedEncodingException
	 *             编码异常
	 */
	private void setContent(Email email, Message msg) throws MessagingException, UnsupportedEncodingException {
		Multipart multipart = new MimeMultipart();

		BodyPart contentPart = new MimeBodyPart();
		if (email.getHtml()) {
			contentPart.setContent(email.getContnet(), CONTENT_TYPE_HTML);
		} else {
			contentPart.setText(email.getContnet());
		}
		multipart.addBodyPart(contentPart);

		if (email.getAttachments() != null && email.getAttachments().size() > 0) {
			for (File attachment : email.getAttachments()) {
				BodyPart attachmentBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(attachment);
				attachmentBodyPart.setDataHandler(new DataHandler(source));
				attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachment.getName()));	// 防止乱码
				multipart.addBodyPart(attachmentBodyPart);
			}
		}
		
		// add mimeMessage content
		msg.setContent(multipart);
	}

	/**
	 * 推送一个邮件
	 * 
	 * @param session
	 * @param msg
	 * @param emailDelivered
	 * @param monitor
	 */
	private void pushItem(Session session, Message msg, Email email, EmailDelivered emailDelivered, IPushMonitor monitor) {
		Transport transport;
		try {
			transport = session.getTransport();
		} catch (NoSuchProviderException e) {
			if (logger.isErrorEnabled()) {
				logger.error(e.getMessage(), e);
			}
			monitor.pushFail(email, e);
			return;
		}

		// 监控发送情况
		transport.addTransportListener(new MailTransportListener(email, emailDelivered, monitor));

		try {
			transport.connect();
		} catch (MessagingException e) {
			if (logger.isErrorEnabled()) {
				logger.error(e.getMessage(), e);
			}
			if (monitor != null) {
				monitor.pushFail(email, e);
			}
			return;
		}
		try {
			transport.sendMessage(msg, msg.getAllRecipients());
		} catch (MessagingException e) {
			if (logger.isErrorEnabled()) {
				logger.error(e.getMessage(), e);
			}
			if (monitor != null) {
				monitor.pushFail(email, e);
			}
			return;
		}
	}

	/**
	 * 邮件发送监听
	 * 
	 * @author xuq
	 * @date 2015年11月17日
	 * @version 1.0
	 */
	private class MailTransportListener implements TransportListener {

		private Email 				email; 				// 邮件
		private EmailDelivered 		emailDelivered; 	// 邮件发送结果
		private IPushMonitor 		monitor; 			// 邮件发送监听

		public MailTransportListener(Email email, EmailDelivered emailDelivered, IPushMonitor monitor) {
			this.emailDelivered = emailDelivered;
			this.monitor = monitor;
		}

		@Override
		public void messageDelivered(TransportEvent arg0) {
			emailDelivered.deliveredSuccess();
			if (monitor != null) {
				monitor.pushProgress(email, emailDelivered.getPercentage());
			}
		}

		@Override
		public void messageNotDelivered(TransportEvent arg0) {
			emailDelivered.deliveredFail();
			if (monitor != null) {
				monitor.pushFail(email, new PushFailException("邮件发送失败"));
			}
		}

		@Override
		public void messagePartiallyDelivered(TransportEvent arg0) {
			emailDelivered.deliveredPartily();
			if (monitor != null) {
				monitor.pushPartily(email);
			}
		}

	}

}
