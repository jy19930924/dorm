package com.blg.dorm.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blg.dorm.common.util.JsonUtils;
import com.blg.dorm.common.util.MailClient;
import com.blg.dorm.common.util.NewFixedThreadPoolManager;
import com.blg.dorm.common.util.PropertyReader;

public class CommonLog {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final static String run_environment = PropertyReader.getContextProperty("RUN_ENVIRONMENT");
	private final static String email_host = PropertyReader.getContextProperty("EMAIL_HOST");// 邮箱发送
	private final static String email_port = PropertyReader.getContextProperty("EMAIL_PORT");// 邮箱发送
	private final static String email_userName = PropertyReader.getContextProperty("EMAIL_USER_NAME");// 邮箱发送
	private final static String email_password = PropertyReader.getContextProperty("EMAIL_USER_PASSWORD");// 邮箱发送
	private final static String email_recipient = PropertyReader.getContextProperty("EMAIL_RECIPIENT");// 邮箱发送
	private final static String email_title = PropertyReader.getContextProperty("EMAIL_TITLE");// 邮箱发送
	private final static String phone_sign_name = PropertyReader.getContextProperty("PHONE_SIGN_NAME");// 手机号发送
	private final static String phone_template_code = PropertyReader.getContextProperty("PHONE_TEMPLATE_CODE");// 手机号发送
	private final static String phone_recipient = PropertyReader.getContextProperty("PHONE_RECIPIENT");// 手机号发送

	private final static int ERROR_CODE = 100000; // 默认错误码
	private static final Logger logger = LoggerFactory.getLogger(CommonLog.class);

	// 业务日志
	public static void bussInfo(String syslog) {
		String str = "[BUSSINFO][" + getPath() + "][" + dateFormat.format(new Date()) + "] - " + log(syslog);
		logger.info(str);
	}

	// 错误日志
	public static void bussError(String proj, String leve, String title, Object error) {
		String str = "[BUSSERROR][" + getPath() + "][" + dateFormat.format(new Date()) + "] - |" + proj + "-" + leve + "|" + title + "|" + log(error);
		logger.info(str);
	}

	// debug生产日志
	public static void debug(Object syslog) {
		String str = "[TTYLOG][" + getPath() + "][" + dateFormat.format(new Date()) + "] - " + log(syslog);
		logger.debug(str);
	}

	// info生产日志
	public static void info(Object syslog) {
		String str = "[TTYLOG][" + getPath() + "][" + dateFormat.format(new Date()) + "] - " + log(syslog);
		logger.info(str);
	}

	// error错误生产日志
	public static void error(Object syslog) {
		String str = "[TTYLOG][" + getPath() + "][" + dateFormat.format(new Date()) + "] - " + log(syslog);
		logger.error(str);
		// dingding
		// DingDingUtil.sendTextMessage(str) ;
	}

	private static String getPath() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		StackTraceElement st = stackTrace[3];
		return st.getClassName() + "." + st.getMethodName() + ":" + st.getLineNumber();
	}

	/**
	 * 系统错误记录
	 * 
	 * @param log
	 *            错误对象
	 */
	public static void systemError(Object log) {
		String str = "TTYLOG|" + run_environment + "|" + dateFormat.format(new Date()) + "|" + log(log);
		errorEmail(log, email_title, email_recipient);
		logger.error(str);
	}

	/**
	 * 记录日志并发送邮件
	 * 
	 * @param syslog
	 *            日志内容
	 * @param title
	 *            邮件主题
	 * @param to
	 *            接受人
	 */
	public static void errorEmail(Object syslog, String title, String to) {
		if (to != null && !to.isEmpty()) {
			Integer port = null;
			if (email_port != null && !email_port.isEmpty()) {
				port = Integer.parseInt(email_port);
			}
			MailClient mailUtil = new MailClient(email_host, port, true, email_userName, email_password, null);
			String[] toArr = to.split(",");
			title = run_environment + "_" + title;
			String body = "TTYLOG|" + dateFormat.format(new Date()) + "|" + log(syslog);
			mailUtil.sendTextMail(toArr, null, null, title, body);
			// NewFixedThreadPoolManager.getInstance().getExecutorService().submit(new
			// EmailThread(title, body, toArr, mailUtil));
		}
	}

	public static void errorEmail(Object syslog, int code, String title) {
		if (email_recipient != null && !email_recipient.isEmpty()) {
			errorEmail(syslog, title + "[" + code + "]", email_recipient);
		}
	}

	public static void errorEmail(Object syslog, String title) {
		if (email_recipient != null && !email_recipient.isEmpty()) {
			errorEmail(syslog, title, email_recipient);
		}
		CommonLog.error(syslog);
	}

	public static void errorEmail(Object syslog) {
		if (email_recipient != null && !email_recipient.isEmpty()) {
			errorEmail(syslog, email_title);
		}
	}

	/**
	 * 报警短信发送
	 * 
	 * @param phone
	 *            短信手机号
	 * @param code
	 *            短信编码
	 * @param msg
	 *            错误信息
	 */
	@Deprecated
	public static void errorPhone(int code, String msg, String phone) {
		if (phone != null && !phone.isEmpty()) {
			if (msg == null) {
				msg = "";
			} else if (msg.length() > 20) {
				msg = msg.substring(0, 20);
			}
			if (code > 999999) {
				code = ERROR_CODE;
			}
			Map<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("msg", msg);
			hashMap.put("code", code + "");
			String[] phoneArr = phone.split(",");
			for (String phoneNum : phoneArr) {
				NewFixedThreadPoolManager.getInstance().getExecutorService().submit(new PhoneThread(phoneNum, hashMap));
			}
		}
	}

	@Deprecated
	public static void errorPhone(int code, String msg) {
		CommonLog.errorPhone(code, msg, phone_recipient);
	}

	@Deprecated
	public static void errorPhone(Object syslog, String phone) {
		String messageJson = log(syslog);
		CommonLog.errorPhone(ERROR_CODE, messageJson, phone);
		CommonLog.error(syslog);
	}

	@Deprecated
	public static void errorPhone(Object syslog) {
		CommonLog.errorPhone(syslog, phone_recipient);
	}

	/**
	 * 记录日志并发送邮件，发送短信
	 * 
	 * @param syslog
	 *            错误对象
	 * @param code
	 *            短信错误码
	 * @param phone
	 *            短信手机号
	 * @param title
	 *            邮件标题
	 * @param to
	 *            邮件收件人
	 */
	@Deprecated
	public static void errorEmailPhone(Object syslog, int code, String phone, String title, String to) {
		if (to != null && !to.isEmpty()) {
			CommonLog.errorEmail(syslog, title + "[" + code + "]", to);
		}
		if (phone != null && !phone.isEmpty()) {
			String messageJson = log(syslog);
			CommonLog.errorPhone(ERROR_CODE, messageJson, phone);
		}
		CommonLog.error(syslog);
	}

	@Deprecated
	public static void errorEmailPhone(Object syslog, int code, String title) {
		CommonLog.errorEmailPhone(syslog, code, phone_recipient, title, email_recipient);
	}

	@Deprecated
	public static void errorEmailPhone(Object syslog, int code) {
		CommonLog.errorEmailPhone(syslog, code, "系统报错");
	}

	@Deprecated
	public static void errorEmailPhone(Object syslog) {
		CommonLog.errorEmailPhone(syslog, ERROR_CODE);
	}

	private static String log(Object obj) {
		if (obj instanceof String) {
			return obj.toString();
		}
		if (obj instanceof Number) {
			return obj.toString();
		}
		if (obj instanceof Exception) {
			return getException((Exception) obj);
		}
		return new JsonUtils().setPrettyPrinting(false).bean2json(obj);
	}

	public static String getException(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw, true));
		return sw.toString();
	}

	static class EmailThread implements Callable<String> {
		private String title;
		private String body;
		private String[] toArr;
		private MailClient mailUtil;

		public EmailThread(String title, String body, String[] toArr, MailClient mailUtil) {
			super();
			this.title = title;
			this.body = body;
			this.toArr = toArr;
			this.mailUtil = mailUtil;
		}

		public String call() throws Exception {
			try {
				mailUtil.sendTextMail(toArr, null, null, title, body);
			} catch (Exception e) {
				error(e);
			}
			return "ok";
		}
	}

	static class PhoneThread implements Callable<String> {
		private String phoneNum;
		private Map<String, String> hashMap;

		public PhoneThread(String phoneNum, Map<String, String> hashMap) {
			super();
			this.phoneNum = phoneNum;
			this.hashMap = hashMap;
		}

		public String call() throws Exception {
			try {
				// SmsUtils.getInstance().send(phoneNum, phone_sign_name,
				// phone_template_code, JSONObject.toJSONString(hashMap));
			} catch (Exception e) {
				error(e);
			}
			return "ok";
		}
	}
}
