package com.blg.dorm.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.LongSerializationPolicy;

/**
 * json处理封装
 */
public class JsonUtils {
	private boolean tolow = false; // 字段名是否统一转小写
	private boolean outnull = false; // 是否输出null值
	private String dateFormat = "yyyy-MM-dd HH:mm:ss"; // 日期格式输出
	private boolean prettyPrinting = false; // 对json结果格式化.
	private List<String> allow; // 允许输出字段
	private List<String> shield; // 禁止输出字段

	public JsonUtils setTolow(boolean tolow) {
		this.tolow = tolow;
		return this;
	}

	public JsonUtils setOutnull(boolean outnull) {
		this.outnull = outnull;
		return this;
	}

	public JsonUtils setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
		return this;
	}

	public JsonUtils setAllow(List<String> allow) {
		this.allow = allow;
		return this;
	}

	public JsonUtils setShield(List<String> shield) {
		this.shield = shield;
		return this;
	}

	public JsonUtils setPrettyPrinting(boolean prettyPrinting) {
		this.prettyPrinting = prettyPrinting;
		return this;
	}

	/**
	 * 
	 * @param bean
	 *            输出对象
	 * @return
	 */
	public String bean2json(Object bean) {
		// 创建一个带过滤条件的gson对象
		GsonBuilder gsonBuilder = new GsonBuilder();
		if (outnull) {
			gsonBuilder.serializeNulls();
		}
		if (prettyPrinting) {
			gsonBuilder.setPrettyPrinting();
		}
		Gson gson = gsonBuilder.enableComplexMapKeySerialization() // 支持Map的key为复杂对象的形式
				.serializeSpecialFloatingPointValues().setDateFormat(dateFormat)// 时间转化为特定格式
				.setExclusionStrategies(new ExclusionStrategy() {
					/*** 设置要过滤的属性 */
					public boolean shouldSkipField(FieldAttributes attr) {
						if (allow != null && allow.size() > 0) {
							if (allow.contains(attr.getName().toLowerCase()) || allow.contains(attr.getName().toUpperCase()) || allow.contains(attr.getName())) {
								return false;
							} else {
								return true;
							}
						}
						if (shield != null && shield.size() > 0) {
							if (shield.contains(attr.getName().toLowerCase()) || shield.contains(attr.getName().toUpperCase())
									|| shield.contains(attr.getName())) {
								return true; // 过滤数据
							} else {
								return false;
							}
						}
						// 这里，如果返回true就表示此属性要过滤，否则就输出
						return false;
					}

					/*** 设置要过滤的类 */
					public boolean shouldSkipClass(Class<?> clazz) {
						// 这里，如果返回true就表示此类要过滤，否则就输出
						return false;
					}
				}).setFieldNamingStrategy(new MyFieldNamingStrategy(this.tolow))
				// .setPrettyPrinting() //对json结果格式化.
				.create();

		return gson.toJson(bean);
	}

	public static <T> T json2bean(String json, Class<T> type) throws Exception {
		if (json.trim().length() > 0) {
			try {
				GsonBuilder gsonBuilfer = new GsonBuilder();
				gsonBuilfer
						// .enableComplexMapKeySerialization()
						// //支持Map的key为复杂对象的形式
						.serializeSpecialFloatingPointValues().setDateFormat("yyyy-MM-dd").setLongSerializationPolicy(LongSerializationPolicy.STRING)
						.setPrettyPrinting(); // 对json结果格式化.

				Gson gson = gsonBuilfer.create();
				return gson.fromJson(json, type);
			} catch (Exception e) {
				throw e;
			}
		}
		throw new Exception("json is null.");
	}

	public static <T> List<T> jsonToList(String json, Class<T> cls) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		ArrayList<T> mList = new ArrayList<T>();
		JsonArray array = new JsonParser().parse(json).getAsJsonArray();
		for (final JsonElement elem : array) {
			mList.add(gson.fromJson(elem, cls));
		}
		return mList;
	}
}

class MyFieldNamingStrategy implements FieldNamingStrategy {
	private boolean tolow = false; // 字段名是否统一转小写

	public MyFieldNamingStrategy(boolean tolow) {
		this.tolow = tolow;
	}

	public String translateName(Field f) {
		if (this.tolow) {
			return f.getName().toLowerCase();
		} else {
			return f.getName();
		}
	}

}