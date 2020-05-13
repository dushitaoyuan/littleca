package com.taoyuanx.auth.client.utils;

/**
 * @author 都市桃源
 * 字符串工具
 */
public class StrUtil {
    public static boolean isEmpty(String str) {
        return null == str || "".equals(str);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 参照log4j  模板匹配
     * <p>
     * xx{}xx{} 1,2
     *
     * @param pattern
     * @param objects
     * @return
     */
    public static String log4jFormat(String pattern, Object... objects) {
        if (isEmpty(pattern)) {
            return null;
        }
        char[] arr = pattern.toCharArray();
        StringBuilder temp = new StringBuilder();
        int count = 0, objLen = objects.length, len = arr.length;
        char left = "{".charAt(0);
        char right = "}".charAt(0);
        for (int i = 0; i < len; i++) {
            if (count < objLen) {
                if (i < len - 1 && left == arr[i] && right == arr[i + 1]) {
                    temp.append(objects[count++]);
                    i++;
                } else {
                    temp.append(arr[i]);
                }
            } else {
                temp.append(arr[i]);
            }
        }
        return temp.toString();

    }
}
