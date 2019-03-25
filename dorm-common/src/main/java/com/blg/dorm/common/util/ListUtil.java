package com.blg.dorm.common.util;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dozer.Mapper;

public class ListUtil {

	/**
	 * 根据对象属性名获取属性值
	 * 
	 * @param fieldName
	 *            属性名
	 * @param o
	 *            对象
	 * @return 属性值
	 */
	private static Object getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(o, new Object[] {});
			return value;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 对比两个同类型的List<T>返回差异List<T>集合 Created by zhajl
	 * 
	 * @param <T>
	 *            泛型类型
	 * @param destList
	 *            修改后的数据集合
	 * @param srcList
	 *            原始数据集合
	 * @return 返回新集合与原始集合有差异的集合
	 */
	public static <T> List<T> getModifyList(List<T> destList, List<T> srcList) {
		// srcList为空，直接返回destList
		if (srcList == null || srcList.size() == 0) {
			return destList;
		}

		List<T> modifyList = new ArrayList<T>();
		// destList，返回Empty List
		if (destList == null || destList.size() == 0) {
			return modifyList;
		}

		for (T destObject : destList) {
			boolean isExist = false;
			for (T srcObject : srcList) {
				// 取得老实体对象的属性集合
				Field[] srcFields = srcObject.getClass().getDeclaredFields();
				// 定义记数器
				int i = 0;

				// 将老实体对象的没一个属性值和新实体对象进行循环比较
				for (Field srcField : srcFields) {
					// 防止object.Equals时实例化对象发生异常
					Object destValue = getFieldValueByName(srcField.getName(), destObject);
					if (destValue == null)
						destValue = (Object) "";

					// 防止object.Equals时实例化对象发生异常
					Object srcValue = getFieldValueByName(srcField.getName(), srcObject);
					if (srcValue == null)
						srcValue = (Object) "";

					// 新老实体比较并记录成功次数
					if (destValue.equals(srcValue)) {
						i++;
					}

					// 若成功次数和属性数目相等则说明已经存在或者没有发生过修改条出循环
					if (i == srcFields.length) {
						isExist = true;
						break;
					}
				}

				// 没有发生过修改条出循环
				if (isExist)
					break;
			}

			// 如果不存在则添加该实体到List<T>中
			if (!isExist)
				modifyList.add(destObject);
		}

		return modifyList;
	}

	/**
	 * 对比两个同类型的List<T>返回差异List<T>集合 Created by zhajl
	 * 
	 * @param <T>
	 *            泛型类型
	 * @param destList
	 *            修改后的数据集合
	 * @param srcList
	 *            原始数据集合
	 * @param keyField
	 *            数据主键
	 * @return 返回与原始集合有差异的集合
	 */
	public static <T> List<T> getModifyListByKey(List<T> destList, List<T> srcList, String keyField) {
		// srcList为空，直接返回destList
		if (srcList == null || srcList.size() == 0) {
			return destList;
		}

		List<T> modifyList = new ArrayList<T>();
		// destList，返回Empty List
		if (destList == null || destList.size() == 0) {
			return modifyList;
		}

		for (T destObject : destList) {
			// 取得新实体的数据主键值
			Object destKeyValue = getFieldValueByName(keyField, destObject);
			if (destKeyValue == null)
				destKeyValue = (Object) "";

			boolean isExist = false;
			for (T srcObject : srcList) {
				// 取得老实体的数据主键值
				Object srcKeyValue = getFieldValueByName(keyField, srcObject);
				if (srcKeyValue == null)
					srcKeyValue = (Object) "";

				// 如果新老实体主键值相等则进行属性的比较
				if (destKeyValue.equals(srcKeyValue)) {
					if (destObject.equals(srcObject)) {
						isExist = true;
					}
				}
			}

			// 如果不存在则添加该实体到List<T>中
			if (!isExist)
				modifyList.add(destObject);
		}
		return modifyList;
	}

	/**
	 * 获取List<T>指定属性的值集合
	 * 
	 * @param srcList
	 * @param keyField
	 *            属性名称
	 * @return
	 */
	public static <T> List<String> getFieldValueByFieldName(List<T> srcList, String keyField) {
		// srcList为空，直接返回destList
		List<String> destList = new ArrayList<String>();
		if (srcList == null || srcList.size() == 0) {
			return destList;
		}

		for (T srcObject : srcList) {
			// 取得新实体的数据主键值
			Object destValue = getFieldValueByName(keyField, srcObject);
			String destStr = null;
			if (destValue == null)
				destStr = "";
			else
				destStr = destValue.toString();

			if (!destList.contains(destStr)) {
				destList.add(destStr);
			}
		}
		return destList;
	}

	/**
	 * 获取List<T>指定属性的值集合<value,T>
	 * 
	 * @param srcList
	 * @param keyField
	 *            属性名称
	 * @return
	 */
	public static <T> Map<String, T> getFieldValueMapByFieldName(List<T> srcList, String keyField) {
		// srcList为空，直接返回destList
		Map<String, T> destMap = new HashMap<String, T>();
		if (srcList == null || srcList.size() == 0) {
			return destMap;
		}

		for (T srcObject : srcList) {
			// 取得新实体的数据主键值
			Object destValue = getFieldValueByName(keyField, srcObject);
			String destStr = null;
			if (destValue == null)
				destStr = "";
			else
				destStr = destValue.toString();

			destMap.put(destStr, srcObject);
		}
		return destMap;
	}

	/**
	 * 获取List<T>指定属性的值集合<value,List<T>>
	 * 
	 * @param srcList
	 * @param keyField
	 *            属性名称
	 * @return
	 */
	public static <T> Map<String, List<T>> getFieldValueListMapByFieldName(List<T> srcList, String keyField) {
		// srcList为空，直接返回destList
		Map<String, List<T>> destMap = new HashMap<String, List<T>>();
		if (srcList == null || srcList.size() == 0) {
			return destMap;
		}

		for (T srcObject : srcList) {
			// 取得新实体的数据主键值
			Object destValue = getFieldValueByName(keyField, srcObject);
			String destStr = null;
			if (destValue == null)
				destStr = "";
			else
				destStr = destValue.toString();
			if (destMap.containsKey(destStr)) {
				destMap.get(destStr).add(srcObject);
			} else {
				List<T> list = new ArrayList<T>();
				list.add(srcObject);
				destMap.put(destStr, list);
			}
		}
		return destMap;
	}

	public static List<String> splitString(String src, String patten) {
		List<String> list = new ArrayList<String>();
		if (src != null && !"null".equals(src)) {
			String[] strArr = src.split(patten);
			if (strArr != null && strArr.length > 0) {
				for (int i = 0; i < strArr.length; i++) {
					if (strArr[i] == null || strArr[i].trim() == "" || strArr[i] == "null")
						continue;
					list.add(strArr[i]);
				}
			}
		}
		return list;
	}

	/**
	 * 样本中含有正则内容
	 * 
	 * @param src
	 *            样本字符串
	 * @param reg
	 *            正则表达式
	 * @return
	 */
	public static List<String> match(String src, String reg) {
		List<String> list = new ArrayList<String>();
		if (src != null) {
			if (reg != null) {
				Pattern pattern = Pattern.compile(reg);// 匹配的模式
				Matcher m = pattern.matcher(src);
				while (m.find()) {
					list.add(m.group());
				}
			} else {
				list.add(src);
			}
		}
		return list;
	}

	public static String toString(Object[] objs, String splitChar) {
		if (objs == null) {
			return null;
		}
		if (splitChar == null) {
			splitChar = ",";
		}
		int max = objs.length;
		if (max < 1) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		int i = 0;
		Object[] arr$ = objs;
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; ++i$) {
			Object obj = arr$[i$];
			if (obj != null) {
				sb.append(String.valueOf(obj));
				if (i < max - 1) {
					sb.append(splitChar);
				}
			} else if ((i == max - 1) && (sb.length() > 0)) {
				sb.delete(sb.length() - splitChar.length(), sb.length());
			}
			++i;
		}
		return sb.toString();
	}

	public static boolean hasElement(Object[] objs) {
		if (objs == null) {
			return false;
		}
		return (objs.length > 0);
	}

	public static boolean contains(Object[] objs, Object target) {
		if (!(hasElement(objs)))
			return false;

		Object[] arr$ = objs;
		int len$ = arr$.length;
		for (int i$ = 0; i$ < len$; ++i$) {
			Object obj = arr$[i$];
			if (target.equals(obj)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 造新的destinationClass实例对象列表，通过source对象列表中的字段内容
	 * 映射到destinationClass实例对象列表中，并返回新的destinationClass实例对象列表。
	 * 
	 * @param <T>
	 * @param sourceList
	 * @param destinationClass
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> List<T> mapList(Mapper mapper, Collection sourceList, Class<T> destinationClass) {
		List destinationList = new ArrayList();
		for (Iterator i$ = sourceList.iterator(); i$.hasNext();) {
			Object sourceObject = i$.next();
			Object destinationObject = mapper.map(sourceObject, destinationClass);
			destinationList.add(destinationObject);
		}
		return destinationList;
	}
}
