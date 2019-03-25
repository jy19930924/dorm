package com.blg.dorm.common.util;

import static com.blg.dorm.common.util.WebUtil.getResponseAsString;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.blg.dorm.common.CommonLog;

/**
 * HttpClient的应用
 * 
 * @author diyong
 *
 */
public class HttpClientUtil {

	private CloseableHttpClient client;
	private static HttpClientUtil hct = new HttpClientUtil();

	public static HttpClientUtil getInstance() {
		return hct;
	}

	private HttpClientUtil() {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(1000); // 设置连接池线程最大数量
		connectionManager.setDefaultMaxPerRoute(50); // 设置单个路由最大的连接线程数量

		// 创建http request的配置信息
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(3000).setSocketTimeout(10000).build();
		// 设置重定向策略
		LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();
		// 初始化httpclient客户端
		client = HttpClients.custom().setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig)
		// .setUserAgent(NewsConstant.USER_AGENT)
				.setRedirectStrategy(redirectStrategy).build();
	}

	/**
	 * get方法获取网页内容
	 * 
	 * @param url
	 *            // * @param code 网页编码 // * @param bzip 是否支持压缩
	 * @return
	 */
	public String getUrlContent(String url) {
		return this.getUrlContent(url, "utf-8", true);
	}

	public String getUrlContent(String url, String code) {
		return this.getUrlContent(url, code, true);
	}

	public String getUrlContent(String url, String code, boolean bzip) {
		long start = System.currentTimeMillis();
		StringBuffer buf = new StringBuffer("HttpClientUtil|getUrlContent");
		buf.append("|").append(url);

		String html = "";
		HttpGet get = new HttpGet(url);
		CloseableHttpResponse response = null;
		try {
			if (bzip) {
				get.setHeader("Accept-Encoding", "gzip, deflate");
			}
			// 执行get请求.
			response = client.execute(get);
			// 获得响应的消息实体
			HttpEntity entity = response.getEntity();
			// 获取响应状态码
			int statuscode = response.getStatusLine().getStatusCode();
			if (statuscode == HttpStatus.SC_OK) {
				// InputStream inputStream = entity.getContent();
				html = EntityUtils.toString(entity, code);
			}

			// 关闭httpEntity流
			EntityUtils.consume(entity);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			String title = "HttpClientUtil ERROR <br/>" + url;
			CommonLog.error(title + "<br/>" + sw.toString());
			// CommonLog.systemError(e);
		} finally {
			if (null != response) {
				try {
					// 关闭response
					response.close();
				} catch (IOException e) {
					CommonLog.error(CommonLog.getException(e));
				}
			}
		}

		buf.append("|").append(html.length()).append("|").append(System.currentTimeMillis() - start);
		CommonLog.debug(buf.toString());

		return html;
	}

	/**
	 * Post方法获取网页内容
	 * 
	 * @param url
	 * @param map
	 * @return
	 */
	public String postUrlContent(String url, Map<String, String> map) {
		return this.postUrlContent(url, map, "utf-8", true);
	}

	public String postUrlContent(String url, Map<String, String> map, String code, boolean bzip) {
		long start = System.currentTimeMillis();
		StringBuffer buf = new StringBuffer("HttpClientUtil|postUrlContent");
		buf.append("|").append(url);

		String html = "";
		HttpPost post = new HttpPost(url);
		CloseableHttpResponse response = null;
		try {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for (String key : map.keySet()) {
				nvps.add(new BasicNameValuePair(key, map.get(key)));
			}
			post.setEntity(new UrlEncodedFormEntity(nvps, code));
			if (bzip) {
				post.setHeader("Accept-Encoding", "gzip, deflate");
			}
			// 执行get请求.
			response = client.execute(post);
			// 获得响应的消息实体
			HttpEntity entity = response.getEntity();
			// 获取响应状态码
			int statuscode = response.getStatusLine().getStatusCode();
			if (statuscode == HttpStatus.SC_OK) {
				// InputStream inputStream = entity.getContent();
				html = EntityUtils.toString(entity);
			}
			// 关闭httpEntity流
			EntityUtils.consume(entity);

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			String title = "HttpClientUtil ERROR <br/>" + url;
			CommonLog.error(title + "<br/>" + sw.toString());
			// CommonLog.systemError(e);
		} finally {
			if (null != response) {
				try {
					// 关闭response
					response.close();
				} catch (IOException e) {
					CommonLog.error(CommonLog.getException(e));
				}
			}
		}

		buf.append("|").append(html.length()).append("|").append(System.currentTimeMillis() - start);
		CommonLog.info(buf.toString());

		return html.toString();
	}

	public String sendRequest(String url, String uuid) {
		return this.sendRequest(url, uuid, 3000, 3000);
	}

	public String sendRequest(String url, String uuid, int connectTimeout, int readTimeout) {
		StringBuffer buf = new StringBuffer("HttpClientUtil|sendRequest");
		long start = System.currentTimeMillis();
		buf.append("|").append(uuid).append("|").append(url);

		StringBuilder builder = new StringBuilder();
		BufferedReader br = null;
		HttpURLConnection connection = null;
		try {
			connection = HttpURLConnection.class.cast(new URL(url).openConnection());
			connection.setConnectTimeout(connectTimeout);
			connection.setReadTimeout(connectTimeout);
			connection.setUseCaches(false);

			connection.connect();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				String s;
				while ((s = br.readLine()) != null) {
					builder.append(s).append("\n");
				}
			}
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			String title = "HttpClientUtil ERROR <br/>" + url + "&" + uuid;
			CommonLog.error(title + sw.toString());
		} finally {
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					CommonLog.error(CommonLog.getException(e));
				}
			}
			if (null != connection) {
				connection.disconnect();
			}
		}
		buf.append("|").append(builder.length()).append("|").append(System.currentTimeMillis() - start);
		CommonLog.info(buf.toString());

		return builder.toString();
	}

	public String postUrlData(String url, String data) throws Exception {
		return this.postUrlData(url, data, "text/xml", "UTF-8");
	}

	public String postUrlData(String url, String data, String contentType, String charset) throws Exception {
		String html = "";
		CloseableHttpResponse response = null;
		try {
			HttpPost post = new HttpPost(url);
			StringEntity entity = new StringEntity(data, charset);
			post.setEntity(entity);
			post.setHeader("Content-Type", contentType + ";charset=" + charset);
			response = client.execute(post);
			int statuscode = response.getStatusLine().getStatusCode();
			if (statuscode == HttpStatus.SC_OK) {
				html = EntityUtils.toString(response.getEntity());
			}
			// 关闭httpEntity流
			EntityUtils.consume(entity);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			String title = "HttpClientUtil ERROR <br/>" + url;
			CommonLog.error(title + sw.toString());
		} finally {
			if (null != response) {
				try {
					// 关闭response
					response.close();
				} catch (IOException e) {
					CommonLog.error(CommonLog.getException(e));
				}
			}
		}
		return html;
	}

	@Deprecated
	public String postSignData(String url, String data, String publicKey, String privateKey) throws Exception {
		String html = "";
		long time = System.currentTimeMillis();
		CloseableHttpResponse response = null;
		try {
			HttpPost post = new HttpPost(url);
			// StringEntity entity = new StringEntity(data);
			// post.setEntity(entity);
			post.setHeader("supId", publicKey);
			long timestamp = System.currentTimeMillis();
			post.setHeader("timestamp", timestamp + "");
			String signData = StringUtil.stringNullDeal(publicKey) + timestamp + data + StringUtil.stringNullDeal(privateKey);
			String sign = MD5Util.md5(signData).toLowerCase();
			post.setHeader("sign", sign);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("param", data));
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			response = client.execute(post);
			int statuscode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			if (statuscode == HttpStatus.SC_OK) {
				html = EntityUtils.toString(entity);
			}
			// 关闭httpEntity流
			EntityUtils.consume(entity);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			String title = "HttpClientUtil ERROR <br/>" + url;
			CommonLog.error(title + sw.toString());
		} finally {
			if (null != response) {
				try {
					// 关闭response
					response.close();
				} catch (IOException e) {
					CommonLog.error(CommonLog.getException(e));
				}
			}
		}
		CommonLog.debug("postSignData请求参数：" + data + "| postSignData响应结果：" + html + "|" + (System.currentTimeMillis() - time));
		return html;
	}

	@Deprecated
	public String postData(String url, String data) throws Exception {
		String html = "";
		CloseableHttpResponse response = null;
		try {
			HttpPost post = new HttpPost(url);
			StringEntity entity = new StringEntity(data);
			post.setEntity(entity);
			// post.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
			response = client.execute(post);
			int statuscode = response.getStatusLine().getStatusCode();
			if (statuscode == HttpStatus.SC_OK) {
				html = EntityUtils.toString(response.getEntity());
			}
			// 关闭httpEntity流
			EntityUtils.consume(entity);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			String title = "HttpClientUtil ERROR <br/>" + url;
			CommonLog.error(title + sw.toString());
		} finally {
			if (null != response) {
				try {
					// 关闭response
					response.close();
				} catch (IOException e) {
					CommonLog.error(CommonLog.getException(e));
				}
			}
		}
		return html;
	}

	@Deprecated
	public String postXMLData(String url, String xmlData, String code) throws Exception {
		String html = "";
		CloseableHttpResponse response = null;
		try {
			if (code == null || code.isEmpty()) {
				code = "ISO-8859-1";
			}
			HttpPost post = new HttpPost(url);
			StringEntity entity = new StringEntity(xmlData);
			post.setEntity(entity);
			post.setHeader("Content-Type", "text/xml;charset=" + code);
			response = client.execute(post);
			int statuscode = response.getStatusLine().getStatusCode();
			if (statuscode == HttpStatus.SC_OK) {
				html = EntityUtils.toString(response.getEntity(), code);
			}
			// 关闭httpEntity流
			EntityUtils.consume(entity);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			String title = "HttpClientUtil ERROR <br/>" + url;
			CommonLog.error(title + sw.toString());
		} finally {
			if (null != response) {
				try {
					// 关闭response
					response.close();
				} catch (IOException e) {
					CommonLog.error(CommonLog.getException(e));
				}
			}
		}
		return html;
	}

	/**
	 * 执行HTTP POST请求。
	 *
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @return 响应字符串
	 */
	@Deprecated
	public static String doPost(String url, String params, int connectTimeout, int readTimeout) throws IOException {
		return doPost(url, params, "utf-8", connectTimeout, readTimeout);
	}

	@Deprecated
	public static String doPost(String url, String params, String charset, int connectTimeout, int readTimeout) throws IOException {
		return doPost(url, params, charset, connectTimeout, readTimeout, null);
	}

	@Deprecated
	public static String doPost(String url, String params, String charset, int connectTimeout, int readTimeout, Map<String, String> headerMap)
			throws IOException {
		String ctype = "application/x-www-form-urlencoded;charset=" + charset;
		byte[] content = {};
		if (params != null) {
			content = params.getBytes(charset);
		}
		return _doPost(url, ctype, content, connectTimeout, readTimeout, headerMap);
	}

	@Deprecated
	private static String _doPost(String url, String ctype, byte[] content, int connectTimeout, int readTimeout, Map<String, String> headerMap)
			throws IOException {
		HttpURLConnection conn = null;
		OutputStream out = null;
		String rsp = null;
		try {
			try {
				conn = getConnection(new URL(url), "POST", ctype, headerMap, null);
				conn.setConnectTimeout(connectTimeout);
				conn.setReadTimeout(readTimeout);
			} catch (IOException e) {
				throw e;
			}
			try {
				out = conn.getOutputStream();
				out.write(content);
				rsp = getResponseAsString(conn);
			} catch (IOException e) {
				throw e;
			}
		} finally {
			if (out != null) {
				out.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return rsp;
	}

	@Deprecated
	private static HttpURLConnection getConnection(URL url, String method, String ctype, Map<String, String> headerMap, String accept) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod(method);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		if (StringUtil.isEmpty(accept)) {
			conn.setRequestProperty("Accept", "text/xml,text/javascript");
		} else {
			conn.setRequestProperty("Accept", accept);
		}
		conn.setRequestProperty("User-Agent", "top-sdk-java");
		conn.setRequestProperty("Content-Type", ctype);
		if (headerMap != null) {
			for (Map.Entry<String, String> entry : headerMap.entrySet()) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		return conn;
	}

	/**
	 * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static String getIpAddress(HttpServletRequest request) throws IOException {
		// 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
		} else if (ip.length() > 15) {
			String[] ips = ip.split(",");
			for (int index = 0; index < ips.length; index++) {
				String strIp = (String) ips[index];
				if (!("unknown".equalsIgnoreCase(strIp))) {
					ip = strIp;
					break;
				}
			}
		}
		return ip;
	}
}
