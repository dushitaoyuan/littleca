package com.taoyuanx.ca.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author dushitaoyuan
 * @date 2019/9/2622:46
 * @desc: 启动类
 */
@SpringBootApplication
@EnableCaching
@MapperScan(basePackages = "com.taoyuanx.ca.auth.dao")
public class LittleAuthBootApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(LittleAuthBootApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(LittleAuthBootApplication.class);
    }


}
