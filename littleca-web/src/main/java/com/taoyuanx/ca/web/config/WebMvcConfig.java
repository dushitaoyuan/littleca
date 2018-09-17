package com.taoyuanx.ca.web.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.taoyuanx.ca.web.ex.RestCustomHandlerExceptionResolver;
import com.taoyuanx.ca.web.security.TokenInterceptor;



/**
 * @author 都市桃源
 *
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
	
	@Autowired
	TokenInterceptor tokenInterceptor;
    public static final Logger log=LoggerFactory.getLogger(WebMvcConfig.class);
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //1.需要定义一个convert转换消息的对象;
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        //2.添加fastJson的配置信息，比如：是否要格式化返回的json数据;
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        //3处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastJsonConfig.setCharset(Charset.forName("UTF-8"));
        //4.在convert中添加配置信息.
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        //5.将convert添加到converters当中.
        converters.add(fastJsonHttpMessageConverter);
        super.configureMessageConverters(converters);
    }

  
    
    @Override
	public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(tokenInterceptor).addPathPatterns("/**").excludePathPatterns("/block/toPublicTrading","/error","/page");
    	super.addInterceptors(registry);
	}
	 //统一异常处理
	@Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
    	RestCustomHandlerExceptionResolver restCustomHandlerExceptionResolver = new RestCustomHandlerExceptionResolver();
    	exceptionResolvers.add(restCustomHandlerExceptionResolver);
        super.configureHandlerExceptionResolvers(exceptionResolvers);
    }

}