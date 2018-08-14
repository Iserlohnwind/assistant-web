package com.momassistant.starter;

import com.momassistant.iterceptor.TokenValidateInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * Created by zhufeng on 2018/8/14.
 */
//@SpringBootConfiguration
public class WebConfiguration extends WebMvcConfigurationSupport{
    @Autowired
    private TokenValidateInterceptor tokenValidateInterceptor;
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenValidateInterceptor).addPathPatterns("/testAuth");
        System.out.println("===========   拦截器注册完毕   ===========");
    }
}
