package com.blg.dorm.common.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class StringUtil extends StringUtils {

	/**
	 * 判断字符串是否为null
	 * 
	 * @param str
	 *            待验证的字符串
	 * @return 如果为NULL返回true，否则返回false
	 */
	public static boolean isNull(String str) {
		boolean result = str == null ? true : false;
		return result;
	}

	/**
	 * 判断字符串是否不为null
	 * 
	 * @param str
	 *            待验证的字符串
	 * @return 如果为NULL返回false，否则返回true
	 */
	public static boolean isNotNull(String str) {
		boolean result = str != null ? true : false;
		return result;
	}

	/**
	 * 判断字符串是否为空白
	 * 
	 * @param str
	 *            待验证的字符串
	 * @return 如果为空白字符串或者Null返回true，否则返回false
	 */
	public static boolean isBlank(String str) {
		boolean result = false;
		if (isNull(str)) {
			result = true;
		} else if ("".equals(str.trim())) {
			result = true;
		} else if ("null".equals(str.trim())) {
			result = true;
		}
		return result;
	}

	/**
	 * 判断字符串是否不为空白
	 * 
	 * @param str
	 *            待验证的字符串
	 * @return 不过为空白字符串返回false，否则返回true
	 */
	public static boolean isNotBlank(String str) {
		boolean result = true;
		if (isNull(str)) {
			result = false;
		} else if ("".equals(str.trim())) {
			result = false;
		} else if ("null".equals(str.trim())) {
			result = false;
		}
		return result;
	}

	/**
	 * <p>
	 * Title: str2HexStr
	 * </p>
	 * <p>
	 * Description: 将字符串转换为16进制的形式
	 * </p>
	 * 
	 * @param content
	 *            待转换的字符串
	 * @return 转换后的字符串
	 */
	public static String str2HexStr(final String content) {
		Assert.hasText(content, "content不能为空");

		byte[] b = content.getBytes();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			String strTmp = Integer.toHexString(b[i]);
			if (strTmp.length() > 2) {
				strTmp = strTmp.substring(strTmp.length() - 2);
			}
			sb.append(strTmp);
		}

		return sb.toString();
	}

	/**
	 * <p>
	 * Title: hexStr2Str
	 * </p>
	 * <p>
	 * Description: 将16进制形式的字符串还原为原文
	 * </p>
	 * 
	 * @param hexStrContent
	 *            待还原的字符串
	 * @param charSet
	 *            使用的字符编码
	 * @return 转换后的字符原文
	 * @throws UnsupportedEncodingException
	 *             charSet设置错误时将会抛出该异常
	 */
	public static String hexStr2Str(String hexStrContent, String charSet) throws UnsupportedEncodingException {
		Assert.hasText(hexStrContent, "hexStrContent不能为空");
		Assert.hasText(charSet, "charSet不能为空");
		byte[] contByte = new byte[hexStrContent.length() / 2];
		for (int i = 0; i < contByte.length; i++) {
			contByte[i] = (byte) (0xff & Integer.parseInt(hexStrContent.substring(i * 2, i * 2 + 2), 16));
		}
		return new String(contByte, charSet);
	}

	/**
	 * <p>
	 * Title: base64Encode
	 * </p>
	 * <p>
	 * Description: 使用base64的方式对二进制数据进行转码
	 * </p>
	 * 
	 * @param data
	 *            待转码的byte[]
	 * @return 转码后的字符串内容
	 */
	public static String base64Encode(byte[] data) {
		return Base64.encodeBase64String(data);
	}

	/**
	 * <p>
	 * Title: base64Decode
	 * </p>
	 * <p>
	 * Description: 将Base64转码后的字符串反转为byte[]
	 * </p>
	 * 
	 * @param content
	 *            待反转的字符串
	 * @return 反转后的byte[]
	 */
	public static byte[] base64Decode(String content) {
		return Base64.decodeBase64(content);
	}

	/**
	 * <p>
	 * Title: toSBC
	 * </p>
	 * <p>
	 * Description: 半角字符转全角
	 * </p>
	 * 
	 * @param input
	 *            待转换的字符串
	 * @return 转换后的字符串
	 */
	public static String toSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);

			}
		}
		return new String(c);
	}

	/**
	 * <p>
	 * Title: toDBC
	 * </p>
	 * <p>
	 * Description: 全角字符转半角
	 * </p>
	 * 
	 * @param input
	 *            待转换的字符
	 * @return 转换后的字符
	 */
	public static String toDBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);
			}
		}
		return new String(c);
	}

	public static String Collections2String(Collection<String> origin, String pattern) {
		if (origin == null || origin.size() == 0) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		int i = 0;
		for (String info : origin) {
			i++;
			buffer.append(info);
			if (i < origin.size()) {
				buffer.append(pattern);
			}
		}
		return buffer.toString();
	}

	/**
	 * 空字符串处理
	 * 
	 * @param v
	 * @return
	 */
	public static String stringNullDeal(String v) {
		v = v == null ? "" : v.trim();
		v = "null".equals(v) ? "" : v;
		return v;
	}

	/**
	 * 将字符串转为list
	 * 
	 * @param src
	 * @param patern
	 * @return
	 */
	public static List<String> convertStringToList(String src, String patern) {
		List<String> list = new ArrayList<String>();
		if (src == null) {
			return list;
		}
		String[] strArr = src.split(patern);
		for (int i = 0; i < strArr.length; i++) {
			list.add(strArr[i]);
		}
		return list;
	}

	/**
	 * \n 回车( ) \t 水平制表符( ) \s 空格(\u0008) \r 换行( )
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
}
