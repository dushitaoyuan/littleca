package com.taoyuanx.ca.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class AppServerApplication {
	/**
	 * 文件上传临时路径
	 */
	/* @Bean
	 MultipartConfigElement multipartConfigElement() {
	    MultipartConfigFactory factory = new MultipartConfigFactory();
	    factory.setLocation("e://tmp/");
	    return factory.createMultipartConfig();
	}*/
	
	public static void main(String[] args) {
		SpringApplication.run(AppServerApplication.class, args);
	}



}
