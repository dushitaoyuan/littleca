package com.taoyuanx.ca.ui.util;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.taoyuanx.ca.ui.ex.ValidatorException;

/**
 * @author 都市桃源 校验器，
 */
public final class Validator {
	/** 英文字母 、数字和下划线 */
	public final static Pattern GENERAL = Pattern.compile("^\\w+$");
	/** 数字 */
	public final static Pattern NUMBERS = Pattern.compile("\\d+");
	/** 单个中文汉字 */
	public final static Pattern CHINESE = Pattern.compile("[\u4E00-\u9FFF]");
	/** 中文汉字 */
	public final static Pattern CHINESES = Pattern.compile("[\u4E00-\u9FFF]+");
	/** 货币 */
	public final static Pattern MONEY = Pattern.compile("^(\\d+(?:\\.\\d+)?)$");
	/** 邮件，符合RFC 5322规范，正则来自：http://emailregex.com/ */
	public final static Pattern EMAIL = Pattern
			.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
	/** 移动电话 */
	public final static Pattern MOBILE = Pattern
			.compile("(?:0|86|\\+86)?1[3456789]\\d{9}");
	/** 身份证号码 */
	public final static Pattern CITIZEN_ID = Pattern
			.compile("[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}(\\d|X|x)");
	/** 中文字、英文字母、数字和下划线 */
	public final static Pattern GENERAL_WITH_CHINESE = Pattern
			.compile("^[\u4E00-\u9FFF\\w]+$");
	/** UUID */
	public final static Pattern UUID = Pattern
			.compile("^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$");
	/** 不带横线的UUID */
	public final static Pattern UUID_SIMPLE = Pattern.compile("^[0-9a-z]{32}$");
	public static final Validator VALIDATOR = new Validator();

	public static Validator validator() {
		return VALIDATOR;
	};

	/**
	 * 判断空
	 * 
	 * @param str
	 * @param msg
	 * @return
	 * @throws ValidatorException
	 */
	public Validator isNull(String str, String msg) throws ValidatorException {
		if (StringUtils.isEmpty(str)) {
			throw new ValidatorException(msg);
		}
		return this;
	}
	/**
	 * 判断空
	 * 
	 * @param msg
	 * @return
	 * @throws ValidatorException
	 */
	public Validator isNull(Object obj, String msg) throws ValidatorException {
		if (null==obj) {
			throw new ValidatorException(msg);
		}
		return this;
	}
	public Validator isNull(Map map, String msg) throws ValidatorException {
		if (null == map || map.isEmpty()) {
			throw new ValidatorException(msg);
		}
		return this;
	}

	/**
	 * 两个字符串是否相等
	 * 
	 * @param str2
	 * @param msg
	 * @return
	 * @throws ValidatorException
	 */
	public Validator isEqueal(String str1, String str2, String msg)
			throws ValidatorException {
		try {
			if (!str1.equals(str2)) {
				throw new ValidatorException(msg);
			}
			return this;
		} catch (Exception e) {
			throw new ValidatorException(msg);
		}
	}

	public Validator isNull(List list, String msg) throws ValidatorException {
		if (null == list || list.isEmpty()) {
			throw new ValidatorException(msg);
		}
		return this;
	}

	// 判断长度是否在范围内
	public Validator isGeneral(String value, int min, int max, String errMsg)
			throws ValidatorException {
		if (null == value) {
			throw new ValidatorException(errMsg);
		}
		String reg = "^\\w{" + min + "," + max + "}$";
		if (min < 0) {
			min = 0;
		}
		if (max <= 0) {
			reg = "^\\w{" + min + ",}$";
		}

		if (false == isMatch(Pattern.compile(reg), value)) {
			throw new ValidatorException(errMsg);
		}
		return this;
	}

	/**
	 * 给定内容是否匹配正则
	 * 
	 * @param pattern
	 *            模式
	 * @param content
	 *            内容
	 * @return 正则为null或者""则不检查，返回true，内容为null返回false
	 */
	public boolean isMatch(Pattern pattern, String content) {
		if (content == null || pattern == null) {
			// 提供null的字符串为不匹配
			return false;
		}
		return pattern.matcher(content).matches();
	}
	
	
	/**
	 * 给定内容是否匹配正则
	 * 
	 * @param pattern
	 *            模式
	 * @param content
	 *            内容
	 * @return 正则为null或者""则不检查，返回true，内容为null返回false
	 * @throws ValidatorException 
	 */
	public void isMatch(Pattern pattern, String content,String msg) throws ValidatorException {
		if(!isMatch(pattern, content)) {
			throw new ValidatorException(msg);
		}
	}

}
