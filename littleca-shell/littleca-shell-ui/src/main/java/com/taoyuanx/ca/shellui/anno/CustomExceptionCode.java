package com.taoyuanx.ca.shellui.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 都市桃源
 * 注解在异常上 表示该异常处理时返回的code和默认异常描述
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CustomExceptionCode {
	int code() default 500;
	String msg() default "未知异常";
}
