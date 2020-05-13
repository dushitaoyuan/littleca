package com.taoyuanx.ca.util;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 都市桃源 2018年5月30日下午10:17:23 TODO auth 工具集合
 */
@SuppressWarnings("all")
public class Utils {
	/**
	 * 合并两个数组
	 * 
	 * @param array1
	 * @param array2
	 * @return
	 */
	public static byte[] addAll(byte[] array1, byte[] array2) {
		if (array1 == null) {
			return clone(array2);
		} else if (array2 == null) {
			return clone(array1);
		}
		byte[] joinedArray = (byte[]) Array.newInstance(byte.class, array1.length + array2.length);
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	public static byte[] clone(byte[] array) {
		if (array == null) {
			return null;
		}
		return (byte[]) array.clone();
	}

	/**
	 * 截取字节数组
	 * 
	 * @param array
	 * @param startIndexInclusive
	 * @param endIndexExclusive
	 * @return
	 */
	public static byte[] subarray(byte[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return null;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		int newSize = endIndexExclusive - startIndexInclusive;

		if (newSize <= 0) {
			return (byte[]) Array.newInstance(byte.class, 0);
		}
		byte[] subarray = (byte[]) Array.newInstance(byte.class, newSize);
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	/**
	 * 判断字符是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || "".equals(str);
	}

	/**
	 * 字符长度是否为某个值
	 * 
	 * @param str
	 * @param len
	 * @return
	 */
	public static boolean len(String str, Integer len) {
		if (isEmpty(str)) {
			return false;
		}
		return str.length() == len;

	}

	public static boolean isNotEmpty(Object obj) {
		if (obj instanceof String) {
			return !(null == obj || "".equals(obj));
		}
		if (obj instanceof String[]) {
			String[] tmp = (String[]) obj;
			return !(null == obj || tmp.length == 0);
		}
		if (obj instanceof Map) {
			Map tmp = (Map) obj;
			return !(null == obj || tmp.isEmpty());
		}
		if (obj instanceof Map) {
			Map tmp = (Map) obj;
			return !(null == obj || tmp.isEmpty());
		}
		if (obj instanceof Set) {
			Set tmp = (Set) obj;
			return !(null == obj || tmp.isEmpty());
		}
		if (obj instanceof List) {
			List tmp = (List) obj;
			return !(null == obj || tmp.isEmpty());
		}
		return null != obj;
	}

}
