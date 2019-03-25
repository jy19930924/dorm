package com.blg.dorm.common.util;

import java.io.File;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.Assert;

import com.blg.dorm.common.util.ListUtil;

public class MailClient {

	private static Logger logger = LoggerFactory.getLogger(MailClient.class);

	private String host;
	private Integer port = 25;
	private String userName;
	private String password;
	private boolean sslMail = false;
	private Integer timeOut = Integer.valueOf(20000);
	private Properties prop;

	public MailClient(String host, Integer port, String userName, String password, Integer timeOut) {
		this.host = host;
		if ((null != port) && (port.intValue() >= 0))
			this.port = port;
		this.userName = userName;
		this.password = password;
		if ((null != timeOut) && (timeOut.intValue() >= 0))
			this.timeOut = timeOut;

		initProp();
	}

	public MailClient(String host, Integer port, boolean sslMail, String userName, String password, Integer timeOut) {
		this.host = host;
		if ((null != port) && (port.intValue() >= 0))
			this.port = port;
		this.sslMail = sslMail;
		this.userName = userName;
		this.password = password;
		if ((null != timeOut) && (timeOut.intValue() >= 0))
			this.timeOut = timeOut;

		initProp();
	}

	private void initProp() {
		this.prop = new Properties();
		this.prop.put("mail.smtp.auth", "true");
		this.prop.put("mail.smtp.port", "" + this.port);
		if (this.sslMail) {
			this.prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			this.prop.put("mail.smtp.socketFactory.port", "" + this.port);
		}
		this.prop.put("mail.smtp.timeout", "" + this.timeOut);
		this.prop.put("mail.smtp.connectiontimeout", "" + this.timeOut);
	}

	private JavaMailSenderImpl createJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(this.host);
		mailSender.setUsername(this.userName);
		mailSender.setPassword(this.password);
		mailSender.setJavaMailProperties(this.prop);
		mailSender.setDefaultEncoding("UTF-8");
		return mailSender;
	}

	/**
	 * 批量发送邮件
	 * 
	 * @param to
	 *            收件人邮箱地址，不能为空
	 * @param cc
	 *            抄送人邮箱地址，可以为空
	 * @param bcc
	 *            密送人邮箱地址，可以为空
	 * @param subject
	 *            标题
	 * @param content
	 *            内容
	 */
	public void sendTextMail(String[] to, String[] cc, String[] bcc, String subject, String content) {
		logger.debug("[tty-mail]开始验证必要参数是否传递：to，subject，content。");
		Assert.notEmpty(to, "to，收件人列表不能为空！");
		Assert.hasText(subject, "subject，邮件标题不能为空！");
		Assert.hasText(content, "content，邮件正文不能为空！");
		logger.debug("[tty-mail]必要参数验证通过，开始组装发送对象。");
		try {
			JavaMailSenderImpl mailSender = createJavaMailSender();
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(this.userName);
			message.setTo(to);
			if (ListUtil.hasElement(cc))
				message.setCc(cc);

			if (ListUtil.hasElement(bcc))
				message.setBcc(bcc);

			message.setSubject(subject);
			message.setText(content);

			logger.debug("[tty-mail]发送对象组装完成，开始执行发送。");
			mailSender.send(message);
			logger.info("[tty-mail]Text邮件发送完成。");
		} catch (Exception e) {
			logger.error("发送邮件失败：" + e.getMessage(), e);
		}
	}

	/**
	 * 批量发送邮件
	 * 
	 * @param to
	 *            收件人邮箱地址，不能为空
	 * @param cc
	 *            抄送人邮箱地址，可以为空
	 * @param bcc
	 *            密送人邮箱地址，可以为空
	 * @param subject
	 *            标题
	 * @param content
	 *            内容
	 * @param files
	 *            文件
	 */
	public void sendHTMLMail(String[] to, String[] cc, String[] bcc, String subject, String content, File[] files) {
		logger.debug("[tty-mail]开始验证必要参数是否传递：to，subject，content。");
		Assert.notEmpty(to, "to，收件人列表不能为空！");
		Assert.hasText(subject, "subject，邮件标题不能为空！");
		Assert.hasText(content, "content，邮件正文不能为空！");
		logger.debug("[tty-mail]必要参数验证通过，开始组装发送对象。");
		try {
			JavaMailSenderImpl mailSender = createJavaMailSender();
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, Boolean.TRUE.booleanValue(), "UTF-8");

			messageHelper.setFrom(this.userName);
			messageHelper.setTo(to);
			if (ListUtil.hasElement(cc))
				messageHelper.setCc(cc);

			if (ListUtil.hasElement(bcc))
				messageHelper.setBcc(bcc);

			messageHelper.setSubject(subject);
			messageHelper.setText(content, true);

			if (ListUtil.hasElement(files)) {
				File[] arr$ = files;
				int len$ = arr$.length;
				for (int i$ = 0; i$ < len$; ++i$) {
					File file = arr$[i$];
					FileSystemResource res = new FileSystemResource(file);
					messageHelper.addAttachment(file.getName(), res);
				}
			}

			logger.debug("[tty-mail]发送对象组装完成，开始执行发送。");
			mailSender.send(message);
			logger.info("[tty-mail]HTML邮件发送完成。");
		} catch (Exception e) {
			logger.error("发送邮件失败：" + e.getMessage(), e);
		}
	}

	/**
	 * @param to
	 *            收件人邮箱地址，不能为空
	 * @param subject
	 *            标题
	 * @param content
	 */
	public void sendSimpleTextMail(String to, String subject, String content) {
		sendTextMail(new String[] { to }, null, null, subject, content);
	}

	public void sendSimpleHTMLMail(String to, String subject, String content) {
		sendHTMLMail(new String[] { to }, null, null, subject, content, null);
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getTimeOut() {
		return this.timeOut;
	}

	public void setTimeOut(Integer timeOut) {
		this.timeOut = timeOut;
	}

	public Properties getProp() {
		return this.prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public boolean isSslMail() {
		return sslMail;
	}

	public void setSslMail(boolean sslMail) {
		this.sslMail = sslMail;
	}

	public static void sendTtyMail(String subject, String content, String[] toEmail) {
		MailClient mailUtil = new MailClient("smtp.mxhichina.com", 465, true, "test@ttylink.com", "12345@com", 20000);
		mailUtil.sendTextMail(toEmail, null, null, subject, content);
	}

	public static void sendBussinessMail(String subject, String content, String toEmail, String fromEmail, String fromPwd) {
		MailClient mailUtil = new MailClient("smtp.mxhichina.com", 465, true, fromEmail, fromPwd, 20000);
		mailUtil.sendTextMail(new String[] { toEmail }, null, null, subject, content);
	}
}
