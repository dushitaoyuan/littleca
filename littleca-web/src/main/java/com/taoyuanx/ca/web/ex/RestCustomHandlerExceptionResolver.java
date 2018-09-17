package com.taoyuanx.ca.web.ex;

import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.taoyuanx.ca.web.anno.CustomExceptionCode;
import com.taoyuanx.ca.web.common.Result;
import com.taoyuanx.ca.web.common.ResultCode;

/**
 * @author 都市桃源 统一的异常处理
 */
public class RestCustomHandlerExceptionResolver implements
		HandlerExceptionResolver {
	private static final Logger log = LoggerFactory
			.getLogger(RestCustomHandlerExceptionResolver.class);



	/**
	 * 封装默认异常返回必要信息
	 * 
	 * @param request
	 * @param e
	 * @return
	 */
	public Result createError(HttpServletRequest request, Exception e) {
		Result errorResult = new Result();
		errorResult.setStatus(ResultCode.FAIL.code);
		errorResult.setCode(ResultCode.INTERNAL_SERVER_ERROR.code);
		return errorResult;
	}

	/**
	 * 异常处理
	 */
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
				HttpServletResponse response, Object handler, Exception e) {
		    Result error=createError(request,e);
        	if(e instanceof CustomException){// 自定义异常
        		return  handleCustom((CustomException) e, error, request,response);
            }else if (e instanceof NoHandlerFoundException) {//404
        		error.setCode(ResultCode.NOT_FOUND.code);
        		error.setMsg(request.getRequestURI()+"接口不存在");
        		responseResult(response, error);
        		return new ModelAndView();
            }else if(e instanceof HttpRequestMethodNotSupportedException){//405
        		error.setCode(ResultCode.METHOD_NOT_SUPPORT.code);
        		HttpRequestMethodNotSupportedException me=(HttpRequestMethodNotSupportedException) e;
        		error.setMsg("["+me.getMethod().toString()+"]method 不支持,只支持"+Arrays.toString(me.getSupportedMethods())+"method");
        		responseResult(response, error);
        		return new ModelAndView();
            }else if (e instanceof ServletException) {//内部异常
        		error.setCode(ResultCode.INTERNAL_SERVER_ERROR.code);
        		error.setMsg(e.getMessage());
        		responseResult(response, error);
        		return new ModelAndView();
            }else {//未知异常
        		error.setCode(ResultCode.INTERNAL_SERVER_ERROR.code);
        		error.setMsg(e.getMessage());
            	HandlerMethod handlerMethod = (HandlerMethod) handler;
                log.error("接口 [{}] 出现异常，方法：{}.{}，异常摘要：{}", 
                		 request.getRequestURI(),
                         handlerMethod.getBean().getClass().getName(),
                         handlerMethod.getMethod().getName(),
                         e);
                responseResult(response, error);
        		return new ModelAndView();
            }
            
			
            
        }

	/**
	 * 处理自定义异常
	 * 
	 * @param e
	 * @param error
	 */
	public ModelAndView handleCustom(CustomException e, Result error,
			HttpServletRequest request,HttpServletResponse response) {
		CustomExceptionCode code=e.getClass().getAnnotation(CustomExceptionCode.class);
		if(code!=null){
			error.setCode(code.code());
		}
		error.setMsg(e.getMessage());
		responseResult(response, error);
		return new ModelAndView();
		
	}
	
	

	
	
	/**
	 * 结果返回
	 * @param response
	 * @param error
	 */
	private void responseResult(HttpServletResponse response, Result error) {

		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-type", "application/json;charset=UTF-8");
		response.setStatus(200);
		try {
			response.getWriter().write(
					JSON.toJSONString(error,
							SerializerFeature.WriteEnumUsingToString));
		} catch (Exception ex) {
			log.error("异常信息为{}", ex);
		}
	}
	

	
}
